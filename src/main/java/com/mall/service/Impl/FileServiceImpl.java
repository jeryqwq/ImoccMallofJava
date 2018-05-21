package com.mall.service.Impl;

import com.google.common.collect.Lists;
import com.mall.service.IFileService;
import com.mall.util.FtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
@Service("iFileService")
public class FileServiceImpl implements IFileService {
    private Logger logger=LoggerFactory.getLogger(FileServiceImpl.class);
public  String upload(MultipartFile file,String path){
String fileName =file.getOriginalFilename();
String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1);
String uploadFileName =UUID.randomUUID().toString()+"."+fileExtensionName;
logger.info("文件上传中，路径{}，文件名{},全宇宙不重复随机文件名{}",fileName,fileExtensionName,uploadFileName);
File fileDir=new File(path);
if(!fileDir.exists()){
    fileDir.setWritable(true);
    fileDir.mkdirs();
}
File targetFile=new File(path,uploadFileName);
    try {
        file.transferTo(targetFile);
        FtpUtil.uploadFile(Lists.newArrayList(targetFile));
    } catch (IOException e) {
        logger.error("文件上传异常",e);
        return  null;
    }
return  targetFile.getName();
}
}
