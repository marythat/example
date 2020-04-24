package com.newsupplytech.projectapplication.modules.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newsupplytech.projectapplication.comm.base.BaseController;
import com.newsupplytech.projectapplication.comm.base.DefaultPage;
import com.newsupplytech.projectapplication.comm.base.RequestResult;
import com.newsupplytech.projectapplication.comm.core.NstProperties;
import com.newsupplytech.projectapplication.comm.core.exception.NstException;
import com.newsupplytech.projectapplication.comm.utils.DateUtils;
import com.newsupplytech.projectapplication.comm.utils.FileStoreUtils;
import com.newsupplytech.projectapplication.comm.utils.FtpUtil;
import com.newsupplytech.projectapplication.comm.utils.HttpQueryWrapper;
import com.newsupplytech.projectapplication.modules.entity.CommAttachment;
import com.newsupplytech.projectapplication.modules.service.CommAttachmentService;
import com.newsupplytech.projectapplication.modules.service.ProjectInformationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.UUID;


@RestController
@RequestMapping("/commAttachment")
@Api(tags = "通用文件上传")
@Slf4j
public class CommAttachmentController extends BaseController {
    @Autowired
    private CommAttachmentService commAttachmentService;

    @Autowired
    private ProjectInformationService projectInformationService;

    @Autowired
    private NstProperties nstProperties;

    /**
     * 保存文件
     * @param relatedTable
     * @param relatedId
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("store")
    @ApiOperation("保存文件")
    public RequestResult store(@RequestParam(value = "relatedTable",required = false) @ApiParam("关联的表") String relatedTable,
                               @RequestParam(value = "relatedId",required = false) @ApiParam("关联记录的ID") String relatedId,
                               @RequestParam(value = "extProperty",required = false) @ApiParam("附件额外属性") String extProperty,
                               @RequestParam("file") MultipartFile file) throws IOException {
        CommAttachment commAttachment = new CommAttachment();
        if(StringUtils.isNotBlank(relatedTable)) {
            commAttachment.setRelatedTable(relatedTable);
        }
        if(StringUtils.isNotBlank(relatedId)) {
            commAttachment.setRelatedId(relatedId);
        }
        if (file != null) {
            String filePath = FileStoreUtils.store(file);
            commAttachment.setFilePath(filePath);
            commAttachment.setFileName(file.getOriginalFilename());
        } else {
            return RequestResult.error("文件参数错误");
        }
        if(StringUtils.isNotEmpty(extProperty))commAttachment.setExtProperty(extProperty);
        commAttachmentService.save(commAttachment);
        return RequestResult.ok(commAttachment);
    }
    /**
     * ftp保存文件
     * @param relatedTable
     * @param relatedId
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("ftpStore")
    @ApiOperation("保存文件")
    public RequestResult ftpStore(@RequestParam(value = "relatedTable",required = false) @ApiParam("关联的表") String relatedTable,
                               @RequestParam(value = "relatedId",required = false) @ApiParam("关联记录的ID") String relatedId,
                               @RequestParam(value = "extProperty",required = false) @ApiParam("附件额外属性") String extProperty,
                               @RequestParam("file") MultipartFile file) throws IOException {
        FtpUtil myFtp = new FtpUtil();
        CommAttachment commAttachment = new CommAttachment();
        if(StringUtils.isNotBlank(relatedTable)) {
            commAttachment.setRelatedTable(relatedTable);
        }
        if(StringUtils.isNotBlank(relatedId)) {
            commAttachment.setRelatedId(relatedId);
        }
        if (file != null) {
            try {
                // 文件保存路径
                myFtp.connect(nstProperties.getFtpserver(), nstProperties.getFtpport(), nstProperties.getFtpuser(), nstProperties.getFtppwd());
                String filePath = DateUtils.getDay(new Date()) + "/" + UUID.randomUUID().toString();
                myFtp.upload(file,nstProperties.getFtpgetdir()+filePath);
                commAttachment.setFilePath(filePath);
                commAttachment.setFileName(file.getOriginalFilename());
            }catch (Exception e){
                log.error("上传附件错误：", e);
                return RequestResult.error("服务器错误");
            }
        } else {
            return RequestResult.error("文件参数错误");
        }
        if(StringUtils.isNotEmpty(extProperty))commAttachment.setExtProperty(extProperty);
        commAttachmentService.save(commAttachment);
        return RequestResult.ok(commAttachment);
    }

    /**
     * 建立关联关系
     * 当文件上传的时间先于对象保存的时候调用,更新relatedTable和relatedId
     * @param commAttachment
     * @return
     * @throws IOException
     */
    @PostMapping("updateRelation")
    @ApiOperation("建立关联关系(当文件上传的时间先于对象保存的时候调用,更新relatedTable和relatedId)")
    public RequestResult updateRelation(@RequestBody CommAttachment commAttachment) throws IOException {
        if(commAttachment.getId()==null||StringUtils.isBlank(commAttachment.getRelatedId())){
            return RequestResult.error("文件参数错误");
        }
        //查询出记录用于更新
        CommAttachment dbRecord = commAttachmentService.getById(commAttachment.getId());
        if(dbRecord==null){
            return RequestResult.error("找不到ID对应记录");
        }
        //更新关联的内容
        if(StringUtils.isNotBlank(commAttachment.getRelatedTable())) {
            dbRecord.setRelatedTable(commAttachment.getRelatedTable());
        }
        dbRecord.setRelatedId(commAttachment.getRelatedId());

        commAttachmentService.updateById(dbRecord);
        return RequestResult.ok(dbRecord);
    }

    /**
     * 单个删除
     * @param id
     * @return
     */
    @DeleteMapping("remove/{id}")
    @ApiOperation("单个删除")
    public RequestResult remove(@PathVariable("id") String id) {
        //先删除相关文件
        CommAttachment commAttachment = commAttachmentService.getById(id);
        if(commAttachment==null){
            RequestResult.error("找不到记录");
        }
//        FileStoreUtils.remove(commAttachment.getFilePath());
        commAttachmentService.removeById(id);
        return RequestResult.ok("删除成功");
    }

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("list")
    @ApiOperation("分页查询")
    public RequestResult list(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                              HttpServletRequest request) {
        QueryWrapper<CommAttachment> queryWrapper = HttpQueryWrapper.genQueryWrapper(request.getParameterMap());
        queryWrapper.ne("is_del","1");
        IPage<CommAttachment> page = new DefaultPage<>(pageNo, pageSize);
        commAttachmentService.page(page, queryWrapper);
        return RequestResult.ok(page);
    }

    @GetMapping("download/{date}/{fileUuid}")
    @ApiOperation("文件下载")
    public ResponseEntity<byte[]> download(@PathVariable("date") String date, @PathVariable("fileUuid") String fileUuid) throws IOException {
        String filePath = date + "/" + fileUuid;
        QueryWrapper<CommAttachment> queryMapper = new QueryWrapper<>();
        queryMapper.ne("is_del","1");
        queryMapper.eq("file_path", filePath);
        CommAttachment commAttachment = commAttachmentService.getOne(queryMapper);

        if(commAttachment==null){
            throw new NstException("找不到文件");
        }

        InputStream is = FileStoreUtils.getFileStream(commAttachment.getFilePath());

        return transStreamToResponseEntity(is,commAttachment.getFileName());
    }
    @GetMapping(value = "/downloadFile/{date}/{fileUuid}")
    public void downloadFile(@PathVariable("date") String date, @PathVariable("fileUuid") String fileUuid,HttpServletRequest request, HttpServletResponse response) throws Exception {
        String filePath = date + "/" + fileUuid;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            QueryWrapper<CommAttachment> queryMapper = new QueryWrapper<>();
            queryMapper.ne("is_del","1");
            queryMapper.eq("file_path", filePath);
            CommAttachment commAttachment = commAttachmentService.getOne(queryMapper);

            if(commAttachment==null){
                throw new NstException("找不到文件");
            }

            response.setContentType("application/force-download");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(commAttachment.getFileName().getBytes("UTF-8"),"iso-8859-1"));
            inputStream = FileStoreUtils.getFileStream(commAttachment.getFilePath());
            outputStream = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            response.flushBuffer();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @GetMapping(value = "/ftpDownload/{date}/{fileUuid}")
    public void ftpDownload(@PathVariable("date") String date, @PathVariable("fileUuid") String fileUuid,HttpServletRequest request, HttpServletResponse response) throws Exception {
        FtpUtil myFtp = new FtpUtil();
        String filePath = date + "/" + fileUuid;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        nstProperties.getFtpgetdir();
        try {
            myFtp.connect(nstProperties.getFtpserver(), nstProperties.getFtpport(), nstProperties.getFtpuser(), nstProperties.getFtppwd());
            QueryWrapper<CommAttachment> queryMapper = new QueryWrapper<>();
            queryMapper.ne("is_del","1");
            queryMapper.eq("file_path", filePath);
            CommAttachment commAttachment = commAttachmentService.getOne(queryMapper);

            if(commAttachment==null){
                throw new NstException("找不到文件");
            }
            inputStream = myFtp.download(nstProperties.getFtpgetdir()+commAttachment.getFilePath());
            if(inputStream==null){
                throw new NstException("找不到文件");
            }
            response.setContentType("application/force-download");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(commAttachment.getFileName().getBytes("UTF-8"),"iso-8859-1"));
            outputStream = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            response.flushBuffer();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    /**
     * 获取审批记录相关附件
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("getList")
    @ApiOperation("分页查询")
    public RequestResult getList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                              HttpServletRequest request) {
        QueryWrapper<CommAttachment> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("is_del",'1');
        String attachmentIds = request.getParameter("attachmentIds");
        queryWrapper.and(wrapper->{
            if(attachmentIds!=null&&!attachmentIds.isEmpty()){
                String[] split = attachmentIds.substring(0,attachmentIds.length()-1).split(",");
                for(String attachmentId :split){
                    wrapper.or().eq("id",attachmentId);
                }
            }
        });
        IPage<CommAttachment> page = new DefaultPage<>(pageNo, pageSize);
        commAttachmentService.page(page, queryWrapper);
        return RequestResult.ok(page);
    }
}
