package com.util;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author: 雪竹
 * @description: TODO
 * @dateTime: 2023/9/5 16:40
 **/
public class TestAnnotation {

    @Resource
    UploadImage uploadImage;

    @Test
    public void test() throws IOException {
// 模拟上传的图片文件
        File imageFile = new File("src/main/resources/static/1440X400.jpg"); // 替换成实际的图片路径

        // 创建一个MultipartFile对象
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                imageFile.getName(),
                "image/jpeg", // 图片的Content-Type
                new FileInputStream(imageFile)
        );
        uploadImage.test(multipartFile);
        System.out.println(multipartFile);
    }
}
