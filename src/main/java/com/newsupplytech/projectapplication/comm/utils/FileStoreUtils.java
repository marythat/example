package com.newsupplytech.projectapplication.comm.utils;

import com.newsupplytech.projectapplication.comm.core.exception.NstException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.UUID;

@Component
public class FileStoreUtils {
    private static String fileBasePath;

    public FileStoreUtils(@Value("${sysconfig.file-store.disk.base-path}")String fileBasePath) {
        this.fileBasePath = fileBasePath;
    }

    public static String getFileBasePath() {
        return fileBasePath;
    }

    /**
     * 存储文件
     * @param file
     * @return
     * @throws IOException
     */
    public static String store(MultipartFile file) throws IOException {
        // 文件保存路径
        String filePath = DateUtils.getDay(new Date())+"/"+UUID.randomUUID().toString();
        File distFile = new File(fileBasePath + filePath);
        if(!distFile.exists()) {
            distFile.mkdirs();
        }
        // 转存文件
        file.transferTo(new File(fileBasePath + filePath));
        return filePath;
    }

    /**
     * 移除文件
     * @param filePath
     */
    public static void remove(String filePath) {
        new File(fileBasePath+filePath).delete();
    }

    public static InputStream getFileStream(String filePath) throws FileNotFoundException {
        if(StringUtils.isBlank(filePath)){
            throw new NstException("路径不能为空");
        }
        return new FileInputStream(new File(fileBasePath+filePath));
    }
}
