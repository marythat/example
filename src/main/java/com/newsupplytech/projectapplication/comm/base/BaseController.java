package com.newsupplytech.projectapplication.comm.base;

import com.newsupplytech.projectapplication.comm.utils.SessionUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

public abstract class BaseController {

    @Autowired
    protected HttpServletRequest request;

    protected ResponseEntity<byte[]> transStreamToResponseEntity(InputStream inputStream,String fileName) throws IOException {
        byte[] body = new byte[inputStream.available()];
        inputStream.read(body);
        HttpHeaders headers = new HttpHeaders();

        if(fileName.endsWith(".html")||fileName.endsWith(".htm")){
            headers.setContentType(MediaType.TEXT_HTML);
        }else if(fileName.endsWith(".jpeg")||fileName.endsWith(".jpg")||fileName.endsWith(".png")||fileName.endsWith(".gif")){
            headers.setContentType(MediaType.IMAGE_JPEG);
        }else {
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
            headers.add("Content-Disposition", "attchement;filename=" + fileName);
        }

        ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, HttpStatus.OK);
        return entity;
    }

    /**
     * 获取当前登陆用户信息
     * @return
     */
    protected SessionUserInfo currentUserInfo(){
        return (SessionUserInfo)request.getSession().getAttribute(SessionUserInfo.SESSION_USER_INFO_KEY);
    }
}
