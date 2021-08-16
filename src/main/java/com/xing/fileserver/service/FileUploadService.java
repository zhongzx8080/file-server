package com.xing.fileserver.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xing.fileserver.common.constant.FileStatusEnum;
import com.xing.fileserver.common.exception.BusinessException;
import com.xing.fileserver.common.model.PageResultBean;
import com.xing.fileserver.config.DownloadConfig;
import com.xing.fileserver.dto.*;
import com.xing.fileserver.mapper.FileUploadMapper;
import com.xing.fileserver.entity.FileUpload;
import io.minio.ObjectStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class FileUploadService extends ServiceImpl<FileUploadMapper, FileUpload> {

    @Autowired
    private FileUploadMapper fileUploadMapper;

    @Autowired
    private MinioService minioService;

    @Autowired
    private DownloadConfig downloadConfig;

    public UploadResultDTO upload(MultipartFile file, UploadDTO dto) {

        LocalDateTime now = LocalDateTime.now();
        String id = UUID.randomUUID().toString();
        String filename = file.getOriginalFilename();
        String ext = FileUtil.extName(filename);
        String path = buildPath(id, dto.getModule(), now, filename);

        minioService.save(file, path);

        FileUpload fileUpload = new FileUpload();
        String url = String.format("%s/%s", downloadConfig.getApi(), id);
        fileUpload.setId(id);
        fileUpload.setName(filename);
        fileUpload.setPath(path);
        fileUpload.setUrl(url);
        fileUpload.setBusinessId(dto.getBizId());
        fileUpload.setModule(dto.getModule());
        fileUpload.setSize(file.getSize());
        fileUpload.setExt(ext);
        fileUpload.setUploadTime(now.toString());
        fileUpload.setStatus(FileStatusEnum.UPLOADED);
        fileUploadMapper.insert(fileUpload);

        presignedFile(fileUpload);

        UploadResultDTO uploadResultDTO = BeanUtil.toBean(fileUpload, UploadResultDTO.class);

        return uploadResultDTO;
    }

    public UploadPresignedResultDTO uploadPresigned(UploadPresignedDTO dto) {

        LocalDateTime now = LocalDateTime.now();
        String id = UUID.randomUUID().toString();
        String filename = dto.getName();
        String ext = FileUtil.extName(filename);
        String path = buildPath(id, dto.getModule(), now, filename);

        String presignedUrl = minioService.presignedPut(path);

        FileUpload fileUpload = new FileUpload();
        String url = String.format("%s/%s", downloadConfig.getApi(), id);
        fileUpload.setId(id);
        fileUpload.setName(filename);
        fileUpload.setPath(path);
        fileUpload.setUrl(url);
        fileUpload.setModule(dto.getModule());
        fileUpload.setExt(ext);
        fileUpload.setUploadTime(now.toString());
        fileUpload.setStatus(FileStatusEnum.PRESIGNED);
        fileUploadMapper.insert(fileUpload);

        UploadPresignedResultDTO presignedDTO = new UploadPresignedResultDTO();
        presignedDTO.setId(id);
        presignedDTO.setUrl(presignedUrl);
        presignedDTO.setName(dto.getName());

        return presignedDTO;
    }

    public int uploadFinished(UploadFinishedDTO dto) {
        List<String> fileIds = dto.getFileIds();
        LambdaQueryWrapper<FileUpload> query = Wrappers.lambdaQuery(FileUpload.class);
        query.eq(FileUpload::getStatus, FileStatusEnum.PRESIGNED).in(FileUpload::getId, fileIds);
        List<FileUpload> fileUploads = fileUploadMapper.selectList(query);
        fileUploads.forEach(item -> {

            ObjectStat stat = minioService.stat(item.getPath());
            if (Objects.nonNull(stat)) {
                item.setSize(stat.length());
            }
            item.setStatus(FileStatusEnum.UPLOADED);

        });

        boolean updated = saveOrUpdateBatch(fileUploads);
        int count = updated ? fileUploads.size() : 0;

        return count;
    }

    public PageResultBean<UploadedFileDTO> listFiles(UploadFilePageDTO dto) {
        LambdaQueryWrapper<FileUpload> query = Wrappers.lambdaQuery(FileUpload.class);
        if (StrUtil.isNotBlank(dto.getModule())) {
            query.eq(FileUpload::getModule, dto.getModule());
        }

        if (Objects.nonNull(dto.getStatus())) {
            query.eq(FileUpload::getStatus, dto.getStatus());
        }

        if (StrUtil.isNotBlank(dto.getKeyword())) {
            query.like(FileUpload::getName, dto.getKeyword());
        }

        Page<FileUpload> paramPage = new Page<>(dto.getPage(), dto.getRows());
        if (StrUtil.isNotBlank(dto.getSort())) {
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(dto.getSort());
            orderItem.setAsc(dto.isAscending());
            paramPage.addOrder(orderItem);
        }

        Page<FileUpload> resultPage = fileUploadMapper.selectPage(paramPage, query);

        List<FileUpload> records = Optional.ofNullable(resultPage.getRecords()).orElse(new ArrayList<>());
        records.forEach(item -> {
            presignedFile(item);
        });
        List<UploadedFileDTO> uploadedFiles = BeanUtil.copyToList(records, UploadedFileDTO.class);


        return new PageResultBean(resultPage.getTotal(), uploadedFiles);

    }

    public void download(String id, HttpServletResponse response) {
        FileUpload fileUpload = fileUploadMapper.selectById(id);
        if (Objects.isNull(fileUpload) || !Objects.equals(FileStatusEnum.UPLOADED, fileUpload.getStatus())) {
            throw new BusinessException("文件不存在");
        }

        String path = fileUpload.getPath();
        byte[] data = minioService.get(path);

        try {
            response.setHeader("Content-Disposition", String.format("attachment;filename=%s", fileUpload.getName()));
            response.getOutputStream().write(data);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            throw new BusinessException("下载异常");
        }
    }

    public int bind(UploadBindDTO dto) {
        String bizId = dto.getBizId();
        List<String> fileIds = dto.getFileIds();
        LambdaQueryWrapper<FileUpload> query = Wrappers.lambdaQuery(FileUpload.class);
        query.isNull(FileUpload::getBusinessId).ne(FileUpload::getStatus, FileStatusEnum.DELETED).in(FileUpload::getId, fileIds);
        List<FileUpload> fileUploads = fileUploadMapper.selectList(query);
        fileUploads.forEach(item -> {
            item.setBusinessId(bizId);
        });

        boolean updated = saveOrUpdateBatch(fileUploads);
        int count = updated ? fileUploads.size() : 0;
        return count;
    }

    public UploadedFileDTO getById(String id) {
        LambdaQueryWrapper<FileUpload> query = Wrappers.lambdaQuery(FileUpload.class);
        query.eq(FileUpload::getId, id).eq(FileUpload::getStatus, FileStatusEnum.UPLOADED);
        FileUpload upload = fileUploadMapper.selectOne(query);
        UploadedFileDTO uploadedFileDTO = null;
        if (Objects.nonNull(upload)) {
            presignedFile(upload);
            uploadedFileDTO = BeanUtil.toBean(upload, UploadedFileDTO.class);
        }
        return uploadedFileDTO;

    }

    public List<UploadedFileDTO> getByBizId(String bizId) {
        LambdaQueryWrapper<FileUpload> query = Wrappers.lambdaQuery(FileUpload.class);
        query.eq(FileUpload::getBusinessId, bizId);
        query.eq(FileUpload::getStatus, FileStatusEnum.UPLOADED);
        List<FileUpload> fileUploads = Optional.ofNullable(fileUploadMapper.selectList(query)).orElse(new ArrayList<>());

        fileUploads.forEach(item -> {
            presignedFile(item);
        });

        List<UploadedFileDTO> uploadedFileS = BeanUtil.copyToList(fileUploads, UploadedFileDTO.class);
        return uploadedFileS;
    }

    public int delete(String id) {
        FileUpload fileUpload = fileUploadMapper.selectById(id);
        if (Objects.isNull(fileUpload) || !Objects.equals(fileUpload.getStatus(), FileStatusEnum.UPLOADED)) {
            return 0;
        }

        minioService.delete(fileUpload.getPath());

        fileUpload.setStatus(FileStatusEnum.DELETED);
        int count = fileUploadMapper.updateById(fileUpload);
        return count;
    }

    public int deleteByBizId(String bizId) {

        LambdaQueryWrapper<FileUpload> queryWrapper = Wrappers.lambdaQuery(FileUpload.class);
        queryWrapper.eq(FileUpload::getBusinessId, bizId).eq(FileUpload::getStatus, FileStatusEnum.UPLOADED);

        List<FileUpload> fileUploads = fileUploadMapper.selectList(queryWrapper);
        if (Objects.isNull(fileUploads) || fileUploads.size() == 0) {
            return 0;
        }

        List<String> paths = fileUploads.stream().map(item -> item.getPath()).collect(Collectors.toList());
        minioService.delete(paths);

        LambdaUpdateWrapper<FileUpload> updateWrapper = Wrappers.lambdaUpdate(FileUpload.class);
        updateWrapper.eq(FileUpload::getBusinessId, bizId).set(FileUpload::getStatus, FileStatusEnum.DELETED);

        boolean updated = update(updateWrapper);
        int count = updated ? fileUploads.size() : 0;
        return count;
    }


    public UploadPresignedResultDTO getPresigned(String id) {
        FileUpload fileUpload = fileUploadMapper.selectById(id);
        if (Objects.isNull(fileUpload) || !Objects.equals(fileUpload.getStatus(), FileStatusEnum.UPLOADED)) {
            throw new BusinessException("文件不存在");
        }
        String path = fileUpload.getPath();
        String presignedUrl = minioService.presignedGet(path);

        UploadPresignedResultDTO presignedDTO = new UploadPresignedResultDTO();
        presignedDTO.setId(id);
        presignedDTO.setUrl(presignedUrl);
        presignedDTO.setName(fileUpload.getName());
        return presignedDTO;
    }

    public FileUpload presignedFile(FileUpload fileUpload) {
        if (Objects.isNull(fileUpload) || !downloadConfig.isPresigned()) {
            return fileUpload;
        }

        String presignedUrl = minioService.presignedGet(fileUpload.getPath());
        fileUpload.setUrl(presignedUrl);

        return fileUpload;
    }


    private String buildPath(String id, String module, LocalDateTime time, String filename) {
        String ext = FileUtil.extName(filename);
        // module/2021/08/14/id.ext
        String path = String.format("%s/%s/%s/%s/%s.%s", module, time.getYear(), time.getMonthValue(), time.getDayOfMonth(), id, ext);
        return path;
    }
}
