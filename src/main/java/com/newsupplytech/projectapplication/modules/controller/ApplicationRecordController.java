package com.newsupplytech.projectapplication.modules.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Maps;
import com.isoftstone.epsp.common.util.TokenEncrypt;
import com.newsupplytech.projectapplication.comm.base.BaseController;
import com.newsupplytech.projectapplication.comm.base.DefaultPage;
import com.newsupplytech.projectapplication.comm.base.RequestResult;
import com.newsupplytech.projectapplication.comm.core.NstProperties;
import com.newsupplytech.projectapplication.comm.core.exception.NstException;
import com.newsupplytech.projectapplication.comm.utils.*;
import com.newsupplytech.projectapplication.modules.entity.*;
import com.newsupplytech.projectapplication.modules.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/applicationRecord")
@Api(tags = "项目申请记录")
public class ApplicationRecordController extends BaseController {
    @Autowired
    private ApplicationRecordService applicationRecordService;
    @Autowired
    private FlowExecuteService flowExecuteService;
    @Autowired
    private EpspBaseDataService epspBaseDataService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProjectInformationService projectInformationService;
    @Autowired
    private NstProperties nstProperties;
    @Autowired
    private AutoSavePropertiesService autoSavePropertiesService;
    @Autowired
    private FreemarkerWordUtils freemarkerWordUtils;

    @PostMapping("save")
    @ApiOperation("保存(新增/更新)")
    public RequestResult save(@RequestBody ApplicationRecord applicationRecord) {
        applicationRecordService.saveOrUpdate(applicationRecord);
        ProjectInformation projectInformation = projectInformationService.getById(applicationRecord.getProjectId());
        return RequestResult.ok("更新完成");
    }

    @GetMapping("list")
    @ApiOperation("分页查询")
    public RequestResult list(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                              HttpServletRequest request) {
        SessionUserInfo sessionUserInfo = currentUserInfo();
        EpspUser epspUser = sessionUserInfo.getEpspUser();
        Map<String, String[]> map = Maps.newHashMap(request.getParameterMap());
        String checked = null;
        if (StringUtils.isNotEmpty(map.get("checked"))) {
            checked = map.get("checked")[0];
            map.remove("checked");
        }

        QueryWrapper<ApplicationRecord> queryWrapper = HttpQueryWrapper.genQueryWrapper(map);
        queryWrapper.ne("is_del", "1");

        if (epspUser.getOrgid().equals(SessionUserInfo.ORG_ID_COMPANY)) {
            queryWrapper.eq("create_by", epspUser.getUserid());
        }else{
            //默认不查询草稿状态
            if (StringUtils.isEmpty(map.get("status"))) {
                queryWrapper.ne("status", "DRAFT");
            }else{
                if (StringUtils.isEmpty(map.get("status")[0])) {
                    queryWrapper.ne("status", "DRAFT");
                }
            }
//            queryWrapper.ne("status","DRAFT");
        }
        //若登入企业用户phone不为空则加入检索条件
        if (StringUtils.isNotEmpty(sessionUserInfo.getPhone())) {
            queryWrapper.and(wrapper -> wrapper.like("permission_keys", "%," + sessionUserInfo.getPhone() + ",%").or().eq("permission_keys", sessionUserInfo.getPhone())
                    .or().likeLeft("permission_keys", "," + sessionUserInfo.getPhone()).or().likeRight("permission_keys", sessionUserInfo.getPhone() + ","));
        }
        queryWrapper = (QueryWrapper<ApplicationRecord>) rightFilterSqlStr(queryWrapper, sessionUserInfo, checked);
        IPage page = new DefaultPage<>(pageNo, pageSize);
        applicationRecordService.page(page, queryWrapper);
        return RequestResult.ok(page);
    }

    @GetMapping("details")
    @ApiOperation("详情汇总")
    public RequestResult details(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                 HttpServletRequest request) {
        SessionUserInfo sessionUserInfo = currentUserInfo();
        EpspUser epspUser = sessionUserInfo.getEpspUser();
        QueryWrapper<Map> queryWrapper = HttpQueryWrapper.genQueryWrapper(request.getParameterMap());
        queryWrapper.ne("is_del", "1");
        if (epspUser.getOrgid().equals(SessionUserInfo.ORG_ID_COMPANY)) {
            queryWrapper.eq("create_by", epspUser.getUserid());
        }
        queryWrapper = (QueryWrapper<Map>) rightFilterSqlStr(queryWrapper, sessionUserInfo, null);
        IPage<Map> page = new DefaultPage<>(pageNo, pageSize);
        applicationRecordService.getBaseMapper().details(page, queryWrapper);
        if(page.getRecords().size()>0){
            for(Map map:page.getRecords()){
                List<Map> maps = applicationRecordService.getBaseMapper().detailsOne(page,queryWrapper,map.get("projectId").toString());
                for(Map map1:maps) {
                    if(map1.get("nextNodeId")!=null) map.put(map1.get("nextNodeId")+"",map1.get("value"));
                }
            }
        }
        return RequestResult.ok(page);
    }

    @DeleteMapping("remove/{id}")
    @ApiOperation("根据ID删除")
    public RequestResult remove(@PathVariable("id") Long id) {
        applicationRecordService.removeById(id);
        autoSavePropertiesService.remove(id);
        return RequestResult.ok("删除成功");
    }

    @GetMapping("queryOne")
    @ApiOperation("查询详情")
    public RequestResult queryOne(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                  HttpServletRequest request) throws Exception {
        QueryWrapper<ApplicationRecord> queryWrapper = HttpQueryWrapper.genQueryWrapper(request.getParameterMap());
        queryWrapper.ne("is_del", "1");
        Long userid = currentUserInfo().getEpspUser().getUserid();
        if (request.getParameter("id") == null || request.getParameter("id").isEmpty()) {
            //立即申报，如果登入用户为company，则排除不受理申请
            if (currentUserInfo().getEpspUser().getOrgid().equals(SessionUserInfo.ORG_ID_COMPANY)) {
                queryWrapper.ne("status", ApplicationRecord.Status.REJECTED);
            }
            queryWrapper.eq(StringUtils.humpToLine("createBy"), userid);
        }
        IPage<ApplicationRecord> page = new DefaultPage<>(pageNo, pageSize);
        applicationRecordService.page(page, queryWrapper);
        return RequestResult.ok(page);
    }

    @GetMapping("queryAutoInputProperties")
    @ApiOperation("查询详情")
    public RequestResult queryAutoInputProperties() throws Exception {
        SessionUserInfo sessionUserInfo = currentUserInfo();
        if (sessionUserInfo.getEpspUser().getOrgid().equals(SessionUserInfo.ORG_ID_COMPANY)) {
            IPage<ApplicationRecord> page = new DefaultPage<>(0, 1);
            QueryWrapper<ApplicationRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.ne("is_del", "1");
            queryWrapper.orderByDesc("commit_date");
            queryWrapper.eq("create_by", sessionUserInfo.getEpspUser().getUserid());
            applicationRecordService.page(page, queryWrapper);
            if (page.getRecords().size() > 0) {
                JSONArray dataArray = (JSONArray) JSON.parse(page.getRecords().get(0).getFormJson());
                JSONObject dataResult = (JSONObject) dataArray.get(0);
                return RequestResult.ok(dataResult.toString());
            }
            return RequestResult.ok(getAutoInputProperties(sessionUserInfo.getCompanyUser()));
        }
        return null;
    }

    @PostMapping("submit")
    @ApiOperation(value = "提交申请", tags = "提交已经确认的流程，流程进入审批阶段")
    public RequestResult submit(@RequestBody ApplicationRecord applicationRecord) {
        SessionUserInfo sessionUserInfo = currentUserInfo();
        Long orgid = sessionUserInfo.getEpspUser().getOrgid();
        if (orgid.equals(SessionUserInfo.ORG_ID_COMPANY)) {
            ProjectInformation projectInformation = projectInformationService.getById(applicationRecord.getProjectId());
            Street street = epspBaseDataService.queryStreet(sessionUserInfo.getCompanyUser().getStreetid());
            applicationRecord.setStreet(street.getName());

            applicationRecord.setProjectName(projectInformation.getProjectName());
            applicationRecord.setPermissionKeys(sessionUserInfo.getPhone());
            applicationRecord = flowExecuteService.startFlow(sessionUserInfo,applicationRecord,street,projectInformation);
            applicationRecordService.saveOrUpdate(applicationRecord);
            return RequestResult.ok(applicationRecord);
        } else {
            return RequestResult.error("非企业用户不能提交申请");
        }
    }

    private String getAutoInputProperties(CompanyUser companyUser) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Charset", "UTF-8");
        headers.setContentType(MediaType.APPLICATION_JSON);
        String key = "lxgsgjkpylx1edc*";
        String data = "gscompany";
        Map<String, String> tokenIv = TokenEncrypt.getTokenIv(key, data);
        headers.add("iv", tokenIv.get("iv"));
        headers.add("token", tokenIv.get("token"));
        HashMap<String, Object> map = new HashMap<>();
        map.put("DGS_TYSHXYDM", companyUser.getCompanycode());
        HttpEntity<Map<String, Object>> objectHttpEntity = new HttpEntity<>(map, headers);
        String url = nstProperties.getUrl();
        String resp;
        resp = restTemplate.postForObject(url, objectHttpEntity, String.class);
        JSONObject jsonObject = (JSONObject) JSON.parse(resp);
        JSONArray dataArray = (JSONArray) jsonObject.get("data");
        if (!(jsonObject.get("success").equals(true) && dataArray.size() > 0)) {
            map.remove("DGS_TYSHXYDM");
            map.put("QYMC", companyUser.getCompanyname());
            resp = restTemplate.postForObject(url, objectHttpEntity, String.class);
            jsonObject = (JSONObject) JSON.parse(resp);
            dataArray = (JSONArray) jsonObject.get("data");
        }
        if (jsonObject.get("success").equals(true)) {
            JSONObject dataResult = (JSONObject) dataArray.get(0);
            //法定代表人手机
            dataResult.put("street", companyUser.getStreet());
            dataResult.put("leaderPerson", companyUser.getLeaderPerson());
            dataResult.put("leaderPhone", companyUser.getLeaderPhone());
            dataResult.put("managePerson", companyUser.getManagePerson());
            dataResult.put("managePhone", companyUser.getManagePhone());
            dataResult.put("userName", companyUser.getUsername());
            dataResult.put("mobile", companyUser.getMobile());
            dataResult.put("email", companyUser.getEmail());
            return dataResult.toString();
        }
        return null;
    }

    private QueryWrapper<?> rightFilterSqlStr(QueryWrapper<?> queryWrapper, SessionUserInfo sessionUserInfo, String checked) {
        EpspUser epspUser = sessionUserInfo.getEpspUser();
        DepartmentUser departmentUser = sessionUserInfo.getDepartmentUser();
        if (epspUser.getOrgid().equals(SessionUserInfo.ORG_ID_DEPART) && !sessionUserInfo.getEpspUser().getPer().contains("managerApplyAdmin")) {
            //新增项目时选择的科室部门，该部门人员能够看到此项目的所有申请记录
            String deptRightSql = "select 1 from nst_project_information m where m.id = x.project_id and m.belong_dept_id = " + departmentUser.getDptid();
            //申请流转到该部门或者该人员时，他们能够看到此申请记录，并且在他们处理后，仍然能查看此记录
            String existsExecuteSql = "select 1 from nst_flow_execute_record n where n.application_id = x.id and (n.executor_id = '" + epspUser.getUserid()
                    + "' or n.executor_id like '%," + epspUser.getUserid() + ",%' or n.executor_id like '%," + epspUser.getUserid()
                    + "' or n.executor_id like '" + epspUser.getUserid() + ",%' or n.enforce_depart = '" + departmentUser.getDptid() + "' )";
            queryWrapper.and(wrapper -> wrapper.exists(deptRightSql)
                    .or().exists(existsExecuteSql));
        }
        if (StringUtils.isNotEmpty(checked) && checked.equals("true")) {
            //新增项目时选择的科室部门，该部门人员能够看到此项目的所有申请记录
            String deptRightSql = "select 1 from nst_project_information m where m.id = x.project_id and m.belong_dept_id = " + departmentUser.getDptid();
            //查询待处理申请
            String waitDealSql = "select 1 from nst_application_record nar where nar.id = x.id and nar.status not in ('RETURN','PASSED' ) and (nar.executor_id = '" + epspUser.getUserid()
                    + "' or nar.executor_id like '%," + epspUser.getUserid() + ",%' or nar.executor_id like '%," + epspUser.getUserid()
                    + "' or nar.executor_id like '" + epspUser.getUserid() + ",%' or nar.enforce_depart = '" + departmentUser.getDptid() + "' )";
            queryWrapper.and(wrapper -> wrapper.exists(deptRightSql)
                    .or().exists(waitDealSql));
        }
        return queryWrapper;
    }


    @GetMapping(value = "/exportDetail/{id}.{templetName}")
    public ResponseEntity<byte[]> exportWordDetail(@PathVariable("id") String id, @PathVariable("templetName") String templetName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Step.1 查询导出的相关数据
        QueryWrapper<ApplicationRecord> queryMapper = new QueryWrapper<>();
        queryMapper.eq("id", id);
        ApplicationRecord applicationRecord = applicationRecordService.getOne(queryMapper);
        Map<String, Object> dataMap = new HashMap<>();
        List<Object>  list=JSON.parseArray(applicationRecord.getFormJson());
        if(list.size()==1){
            dataMap.put("item",list.get(0));
        }else{
            throw new NstException("json数据错误");
        }

        String time = DateUtils.convert(new Date(), "yyyyMMddHHmmss");

        //临时文件
        String temporaryFile = applicationRecord.getCompanyName() + ".pdf";
        //最终地址以及名称
        String fileBasePath = FileStoreUtils.getFileBasePath();
        String pdfName = applicationRecord.getCompanyName() + time + ".pdf";
        String backgroundPdfName = applicationRecord.getCompanyName() + time + ".png";

        //判断选定的模板类型
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(templetName)){
            if("project-detail".equals(templetName)){
                templetName = templetName + ".doc";
            }else{
                templetName = templetName + ".ftl";
            }
        }
        String wordPath = freemarkerWordUtils.createWord(dataMap, templetName, applicationRecord.getCompanyName() + ".doc");
        String pdfUrl=fileBasePath+pdfName;
        String temporaryPdfUrl=fileBasePath+temporaryFile;
        //把word转成pdf
        PDFUtils.word2PDF(fileBasePath+"/"+wordPath, temporaryPdfUrl, applicationRecord.getAcceptCode(), pdfUrl,fileBasePath+backgroundPdfName);

        FileStoreUtils.remove(wordPath);//删除临时生成的word
        String deletePath = null;
        InputStream input = null;
        //根据受理编号区分打印信息内容
        if (StringUtils.isNotBlank(applicationRecord.getAcceptCode())) {
            input = new FileInputStream(new File(pdfUrl));
            FileStoreUtils.remove(temporaryFile);
            FileStoreUtils.remove(backgroundPdfName);//删除临时pdf背景图片
            deletePath = pdfName;
        } else {
            input = new FileInputStream(new File(temporaryPdfUrl));
            deletePath = temporaryFile;
        }
        return this.transStreamToResponseEntity(input, applicationRecord.getCompanyName() + time + ".pdf", deletePath);
    }

    protected ResponseEntity<byte[]> transStreamToResponseEntity(InputStream inputStream, String fileName, String deletePath) throws IOException {
        byte[] body = new byte[inputStream.available()];
        inputStream.read(body);
        HttpHeaders headers = new HttpHeaders();

        if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        }
        headers.add("Content-Disposition", "attchement;filename=" + fileName);

        ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, HttpStatus.OK);

        inputStream.close();
        //删除转换临时文件
        FileStoreUtils.remove(deletePath);
        return entity;
    }

    @GetMapping("queryApplyCount")
    @ApiOperation("查询企业用户申请详情")
    public RequestResult queryApplyCount(HttpServletRequest request) throws Exception {
        SessionUserInfo sessionUserInfo = currentUserInfo();
        EpspUser epspUser = sessionUserInfo.getEpspUser();
        QueryWrapper<Map> queryWrapper = HttpQueryWrapper.genQueryWrapper(request.getParameterMap());
        Map<String, String[]> map = Maps.newHashMap(request.getParameterMap());
        Map map2=new HashMap();
        String message="";
        boolean bool=true;
        boolean draftBool=false;
        int count=0;//记录总数
        int sb_count=0;//已经申报数据
        QueryWrapper<ApplicationRecord> queryMapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(map.get("project_id")[0])) {
           String projectId=map.get("project_id")[0];
            ProjectInformation projectInformation=projectInformationService.getById(projectId);
            queryMapper.eq("project_id", projectId);
            queryMapper.eq("company_id", sessionUserInfo.getCompanyUser().getCompanyid());
            queryMapper.ne("is_del", "1");
            count= applicationRecordService.count(queryMapper);
            //如果有填写可申报次数
            if(StringUtils.isNotEmpty(projectInformation.getDeclareCount())){
                //获取可申报次数
                int itemCount=projectInformation.getDeclareCount();
                if(itemCount<=count){
                    //区分提交项目、存稿
                    queryMapper.ne("status","DRAFT");
                    sb_count= applicationRecordService.count(queryMapper);//已经申请的
                    if(itemCount<=sb_count){
                        bool=false;
                        draftBool=true;
                        message="项目【"+projectInformation.getProjectName()+"】企业可申报数已满，请联系管理员!";
                    }else{
                        int surplus=itemCount-sb_count;
                        bool=false;
                        draftBool=false;
                        message="项目【"+projectInformation.getProjectName()+"】企业可申报数剩余："+surplus
                                +",现存草稿："+surplus+"   请删除存稿或由存稿发起";
                    }
                }

            }
        }

      map2.put("message",message);
      map2.put("bool",bool);
      map2.put("draftBool",draftBool);
      map2.put("count",count);
      return RequestResult.ok(map2);
    }

}
