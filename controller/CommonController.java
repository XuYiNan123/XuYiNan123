package com.itheima.reggie01.controller;

import com.itheima.reggie01.common.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.upload}")
    private String uploadPath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException  {
        File file1 = new File(uploadPath);
        if(!file1.exists()){
            file1.mkdirs();
        }
        String filename = file.getOriginalFilename();   //获取源文件名称   a.jpg
        String suffix = filename.substring(filename.lastIndexOf("."));//获取源文件的扩展名
        System.out.println("suffix = " + suffix);

        String newFileName = UUID.randomUUID()+suffix;  //xxx.jpg


        //2.设置要上传的文件在服务器磁盘上存储位置
        String path = uploadPath+ newFileName;

        //3.将用户上传的文件保存到指定位置
        file.transferTo(new File(path));

        //4.响应上传成功文件的文件名称
        return R.success(newFileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        //1.获取用户要下载文件的字节输入流对象
        FileInputStream is = new FileInputStream(uploadPath + name);

        //2.获取字节输出流对象 字节输出流关联的目的地是客户端浏览器
        ServletOutputStream os = response.getOutputStream();

        //3.IO循环读写
        byte[] b = new byte[1024];
        int len = -1;
        while ((len=is.read(b))!=-1){
            os.write(b,0,len);
        }

        //4.关闭流对象 释放资源
        os.close();
        is.close();
    }

}