package com.xing.fileserver.common.serializer;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.xing.fileserver.config.DownloadConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FillUrlSerializer extends JsonSerializer<String> {

    @Value("${server.servlet.context-path:/}")
    private String contextPath = "/";

    @Autowired
    private DownloadConfig downloadConfig;

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (StrUtil.isNotBlank(value) && !(value.startsWith("http") || value.startsWith("https"))) {
            String url = String.format("%s/%s/%s", downloadConfig.getHost(), contextPath, value);
            // 把多余/去掉, // -> /
            int i = url.indexOf("//");
            String start = url.substring(0, i + 2);
            String end = url.substring(i + 2).replaceAll("/+", "/");
            value = String.format("%s%s", start, end);
        }

        jsonGenerator.writeString(value);
    }
}
