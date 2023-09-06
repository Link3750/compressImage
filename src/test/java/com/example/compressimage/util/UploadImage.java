package com.util;

import com.util.ano.CompressAnnotation;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: 雪竹
 * @description: TODO
 * @dateTime: 2023/9/5 16:49
 **/
@Service
public class UploadImage implements DemoInterface{

    @CompressAnnotation()
    public void test(MultipartFile multipartFile) {
        System.out.println(multipartFile);
    }
}
