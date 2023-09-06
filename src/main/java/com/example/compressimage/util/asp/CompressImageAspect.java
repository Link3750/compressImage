package com.example.compressimage.util.asp;

import com.example.compressimage.util.ano.CompressAnnotation;
import net.coobird.thumbnailator.Thumbnails;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Objects;


/**
 * @author: 雪竹
 * @description: 压缩图片的切面类
 * @dateTime: 2023/9/5 15:43
 **/
@Aspect
@Component
public class CompressImageAspect{

    @Pointcut("@annotation(com.example.compressimage.util.ano.CompressAnnotation)")
    public void pointCut() {
    }

    @Around("pointCut() && @annotation(compressAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint, CompressAnnotation compressAnnotation) throws Throwable {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        CompressAnnotation annotation = method.getAnnotation(CompressAnnotation.class);

        // 图像大小
        int finalSize = Integer.parseInt(annotation.finalSize());
        // 图像压缩大小
        double scale = Double.parseDouble(annotation.scale());
        // 图像压缩质量
        double defaultQuantity = Double.parseDouble(annotation.defaultQuantity());
        // 图像压缩质量缩减比例
        double quantityGap = Double.parseDouble(annotation.quantityGap());


        Object[] args = joinPoint.getArgs();
        MultipartFile multipartFile;
        for(int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (!(arg instanceof MultipartFile)) {
                continue;
            }
            multipartFile = (MultipartFile)arg;
            double compressQuality = defaultQuantity;
            String[] split = Objects.requireNonNull(multipartFile.getOriginalFilename()).split("\\.");
            File compressFile = File.createTempFile(split[0], "." + split[1]);
            while (multipartFile.getSize() / 1024 > finalSize && compressQuality > quantityGap) {
                Thumbnails.of(multipartFile.getInputStream())
                        .scale(scale)
                        .outputQuality(compressQuality)
                        .toFile(compressFile);

                // 创建一个MultipartFile对象来表示压缩后的图像文件
                multipartFile = getMultipartFile(compressFile);
                compressQuality -= quantityGap;
            }
            System.out.println(multipartFile.getSize() / 1024);
            args[i] = multipartFile;
        }

        return joinPoint.proceed(args);
    }

    public static MultipartFile getMultipartFile(File file) throws IOException {
        // 读取文件内容为字节数组
        byte[] fileBytes = readBytesFromFile(file);

        // 创建一个 MultiValueMap 用于模拟 HTTP 请求中的文件参数
        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        multipartRequest.add("file", new MultipartFileWrapper(file.getName(), fileBytes));

        // 获取 MultipartFile
        return (MultipartFile) multipartRequest.getFirst("file");
    }

    private static byte[] readBytesFromFile(File file) throws IOException {
        byte[] fileBytes;
        try (InputStream is = new FileInputStream(file)) {
            fileBytes = new byte[is.available()];
            is.read(fileBytes);
        }
        return fileBytes;
    }

    // 自定义 MultipartFile 实现
    private static class MultipartFileWrapper implements MultipartFile {
        private final String name;
        private final byte[] content;

        public MultipartFileWrapper(String name, byte[] content) {
            this.name = name;
            this.content = content;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return name;
        }

        @Override
        public String getContentType() {
            return null; // 请根据需要返回适当的内容类型
        }

        @Override
        public boolean isEmpty() {
            return content == null || content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() {
            return content;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            Files.write(dest.toPath(), content);
        }
    }
}
