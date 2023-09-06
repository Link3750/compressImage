package com.example.compressimage.util;

import com.example.compressimage.util.ano.CompressAnnotation;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author: 雪竹
 * @description: TODO
 * @dateTime: 2023/9/5 16:49
 **/
@Service
public class UploadImage implements DemoInterface{

    @CompressAnnotation()
    public void uploadImage(MultipartFile multipartFile) {
        System.out.println(multipartFile.getSize() / 1024);
        try {
            // 指定本地文件保存目录
            String fileDir = "src/main/resources/static";
            File uploadDirFile = new File(fileDir);

            // 如果目录不存在，则创建
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            // 获取原始文件名
            String originalFileName = multipartFile.getOriginalFilename();
            String[] split = originalFileName.split("\\.");

            // 构建目标文件路径
            File destFile = new File(uploadDirFile, split[0] + System.currentTimeMillis() + "." + split[1]);
            // 将文件保存到目标路径
            multipartFile.transferTo(destFile);
            // 文件保存成功
            System.out.println("文件已保存到：" + destFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            // 处理文件保存失败的异常
        }
    }
}

