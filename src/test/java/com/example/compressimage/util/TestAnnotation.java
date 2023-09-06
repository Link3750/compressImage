package com.example.compressimage.util;


import com.example.compressimage.CompressImageApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author: 雪竹
 * @description: 测试类
 * @dateTime: 2023/9/5 16:40
 **/
@SpringBootTest(classes = CompressImageApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestAnnotation {

    @Resource
    UploadImage uploadImage;

    @Test
    public void test() throws IOException {
// 模拟上传的图片文件
        File imageFile = new File("src/main/resources/static/微信图片_20230726102901.jpg"); // 替换成实际的图片路径

        // 创建一个MultipartFile对象
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                imageFile.getName(),
                "image/jpeg", // 图片的Content-Type
                new FileInputStream(imageFile)
        );
        uploadImage.uploadImage(multipartFile);
    }
}
