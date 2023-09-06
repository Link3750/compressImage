package com.example.compressimage.util.asp;

import com.example.compressimage.util.ano.CompressAnnotation;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;


/**
 * @author: 雪竹
 * @description: TODO
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
        Integer finalSize = Integer.valueOf(annotation.finalSize());

        Object[] args = joinPoint.getArgs();
        MultipartFile multipartFile = null;
        for(Object arg : args) {
            if (arg instanceof MultipartFile) {
                multipartFile = (MultipartFile)arg;
            }
        }
        Objects.requireNonNull(multipartFile, "empty image.");

        double compressQuality = 0.9;
        File compressFile = File.createTempFile("compressed",
                "." + Objects.requireNonNull(multipartFile.getOriginalFilename()).split("\\.")[1]);
        while (multipartFile.getSize() / 1024 > finalSize && compressQuality > 0.1) {
            Thumbnails.of(multipartFile.getInputStream()).outputQuality(compressQuality)
                    .toFile(compressFile);

            // 创建一个MultipartFile对象来表示压缩后的图像文件
        }
        return joinPoint.proceed();
    }

    public static MultipartFile getMultipartFile(File file) {
        FileItem item = new DiskFileItemFactory().createItem("file"
                , MediaType.MULTIPART_FORM_DATA_VALUE
                , true
                , file.getName());
        try (InputStream input = new FileInputStream(file);
             OutputStream os = item.getOutputStream()) {
            // 流转移
            IOUtils.copy(input, os);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file: " + e, e);
        }

        return new CommonsMultipartFile(item);
    }
}
