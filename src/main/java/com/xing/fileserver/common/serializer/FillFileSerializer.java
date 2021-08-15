package com.xing.fileserver.common.serializer;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.xing.fileserver.common.annotation.FillFiles;
import com.xing.fileserver.dto.UploadedFileDTO;
import com.xing.fileserver.service.FileUploadService;
import com.xing.fileserver.vo.UploadedFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class FillFileSerializer extends JsonSerializer<List<UploadedFileVO>> {

    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public void serialize(List<UploadedFileVO> value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        // 如果有值则直接序列化
        if (Objects.nonNull(value) && value.size() > 0) {
            gen.writeObject(value);
            return;
        }

        JsonStreamContext outputContext = gen.getOutputContext();
        // 获取当前序列化的字段名
        String currentName = outputContext.getCurrentName();
        // 获取当前序列化字段的所属对象
        Object currentValue = outputContext.getCurrentValue();

        try {
            /*
             *
             * 通过反射获取到当前序列化字段，再判断字段是否有@FillFile注解
             * 有则获取指向的id字段，通过反射获取到id的值，
             * 再通过id值获取对应的文件
             *
             * */
            Field currentFiled = ReflectUtil.getField(currentValue.getClass(), currentName);
            FillFiles annotation = currentFiled.getAnnotation(FillFiles.class);
            if (Objects.nonNull(annotation)) {
                String filedValue = annotation.value();
                if (StrUtil.isNotBlank(filedValue)) {
                    Object targetValue = ReflectUtil.getFieldValue(currentValue, filedValue);
                    if (Objects.nonNull(targetValue) && targetValue instanceof String) {
                        String bizId = (String) targetValue;
                        List<UploadedFileDTO> uploadedFiles = fileUploadService.getByBizId(bizId);
                        if (Objects.nonNull(uploadedFiles) && uploadedFiles.size() > 0) {
                            value = BeanUtil.copyToList(uploadedFiles, UploadedFileVO.class);
                        }
                    }
                }
            }

        } catch (Exception exception) {
            log.info("序列化异常 {}", exception.getMessage());
        }

        gen.writeObject(value);
    }
}
