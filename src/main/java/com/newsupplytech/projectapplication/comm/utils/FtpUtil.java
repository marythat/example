package com.newsupplytech.projectapplication.comm.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 *
 * @version 0.3 实现中文目录创建及中文文件创建，添加对于中文的支持
 */
public class FtpUtil {
    private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    public FTPClient ftpClient = new FTPClient();

    public FtpUtil() {

        // 设置将过程中使用到的命令输出到控制台
        // this.ftpClient.addProtocolCommandListener(new
        // PrintCommandListener(new PrintWriter(System.out)));
    }

    /**
     * 连接到FTP服务器
     *
     * @param hostname 主机名
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return 是否连接成功
     * @throws IOException
     */
    public boolean connect(String hostname, int port, String username, String password)
            throws Exception {

        try {
            ftpClient.connect(hostname, port);
        } catch (Exception e) {
            throw new Exception("登陆异常，请检查主机端口");
        }
        ftpClient.setControlEncoding("GBK");
        if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            if (ftpClient.login(username, password)) {
                return true;
            } else
                throw new Exception("登陆异常，请检查密码账号");
        } else
            throw new Exception("登陆异常");

    }

    public String[] getFileList(String filedir)
            throws IOException {
        ftpClient.enterLocalPassiveMode();

        FTPFile[] files = ftpClient.listFiles(filedir);

        String[] sfiles = null;
        if (files != null) {
            sfiles = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                // System.out.println(files[i].getName());
                sfiles[i] = files[i].getName();
            }
        }
        return sfiles;
    }

    /**
     *
     * @param remote 远程文件路径
     * @return 上传的状态
     * @throws IOException
     */
    public InputStream download(String remote)
            throws IOException {
        // 设置被动模式
        ftpClient.enterLocalPassiveMode();

        // 设置以二进制方式传输
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        DownloadStatus result;
        System.out.println(new String(remote.getBytes("GBK"), "iso-8859-1"));
        // 检查远程文件是否存在
        FTPFile[] files = ftpClient.listFiles(remote);
        if (files.length != 1) {
            // System.out.println("远程文件不存在");
            logger.info("远程文件不存在");
            return null;
        }

        return ftpClient.retrieveFileStream(new String(remote.getBytes("GBK"), "iso-8859-1"));
    }

    /**
     *
     * @param file  本地文件
     * @param remote 远程文件路径，使用/home/directory1/subdirectory/file.ext 按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
     * @return 上传结果
     * @throws IOException
     */
    public UploadStatus upload(MultipartFile file, String remote)
            throws IOException {
        // 设置PassiveMode传输
        ftpClient.enterLocalPassiveMode();
        // 设置以二进制流的方式传输
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.setControlEncoding("GBK");
        UploadStatus result;
        // 对远程目录的处理
        String remoteFileName = remote;
        if (remote.contains("/")) {
            remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
            // 创建服务器远程目录结构，创建失败直接返回
            if (CreateDirecroty(remote, ftpClient) == UploadStatus.Create_Directory_Fail) {
                return UploadStatus.Create_Directory_Fail;
            }
        }

        result = uploadFile(remoteFileName, file, ftpClient);
        return result;
    }

    /**
     * 断开与远程服务器的连接
     *
     * @throws IOException
     */
    public void disconnect()
            throws IOException {
        if (ftpClient.isConnected()) {
            ftpClient.disconnect();
        }
    }

    /**
     * 递归创建远程服务器目录
     *
     * @param remote    远程服务器文件绝对路径
     * @param ftpClient FTPClient对象
     * @return 目录创建是否成功
     * @throws IOException
     */
    public UploadStatus CreateDirecroty(String remote, FTPClient ftpClient)
            throws IOException {
        UploadStatus status = UploadStatus.Create_Directory_Success;
        String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
        if (!directory.equalsIgnoreCase("/")
                && !ftpClient.changeWorkingDirectory(new String(directory.getBytes("GBK"), "iso-8859-1"))) {
            // 如果远程目录不存在，则递归创建远程服务器目录
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            while (true) {
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
                if (!ftpClient.changeWorkingDirectory(subDirectory)) {
                    if (ftpClient.makeDirectory(subDirectory)) {
                        ftpClient.changeWorkingDirectory(subDirectory);
                    } else {
                        System.out.println("创建目录失败");
                        return UploadStatus.Create_Directory_Fail;
                    }
                }

                start = end + 1;
                end = directory.indexOf("/", start);

                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return status;
    }

    /**
     *
     * @param remoteFile  远程文件名，在上传之前已经将服务器工作目录做了改变
     * @param localFile   本地文件File
     * @param ftpClient   FTPClient引用
     * @return
     * @throws IOException
     */
    public UploadStatus uploadFile(String remoteFile, MultipartFile localFile, FTPClient ftpClient)
            throws IOException {
        UploadStatus status;
        InputStream raf = localFile.getInputStream();
        OutputStream out = ftpClient.appendFileStream(new String(remoteFile.getBytes("GBK"), "iso-8859-1"));
        byte[] bytes = new byte[1024];
        int c;
        while ((c = raf.read(bytes)) != -1) {
            out.write(bytes, 0, c);
        }
        out.flush();
        raf.close();
        out.close();
        boolean result = ftpClient.completePendingCommand();
        status = result ? UploadStatus.Upload_New_File_Success : UploadStatus.Upload_New_File_Failed;

        return status;
    }

    public enum DownloadStatus {
        Remote_File_Noexist, // 远程文件不存在
        Local_Bigger_Remote, // 本地文件大于远程文件
        Download_New_Success, // 全新下载文件成功
        Download_New_Failed; // 全新下载文件失败
    }

    public enum UploadStatus {
        Create_Directory_Fail, // 远程服务器相应目录创建失败
        Create_Directory_Success, // 远程服务器闯将目录成功
        Upload_New_File_Success, // 上传新文件成功
        Upload_New_File_Failed, // 上传新文件失败
        File_Exits, // 文件已经存在
        Remote_Bigger_Local, // 远程文件大于本地文件
        Delete_Remote_Faild; // 删除远程文件失败
    }
}
