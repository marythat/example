package com.newsupplytech.projectapplication.modules.controller;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.newsupplytech.projectapplication.comm.base.BaseController;
import com.newsupplytech.projectapplication.comm.base.RequestResult;
import com.newsupplytech.projectapplication.comm.utils.*;
import com.newsupplytech.projectapplication.modules.entity.*;
import com.newsupplytech.projectapplication.modules.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/applicationRecordPdfEditDate")
@Api(tags = "项目申请PDF数据修改")
public class ApplicationRecordPdfEditDateController extends BaseController {
    @Autowired
    private ApplicationRecordService applicationRecordService;

    @PostMapping("updateCapialLease")
    @ApiOperation("2020年融资租赁贴息项目资金申请表")
    public RequestResult updateCapialLease() {
        // 项目id = 27
        // Step.1 查询导出的所有数据
        QueryWrapper<ApplicationRecord> queryMapper = new QueryWrapper<>();
        queryMapper.eq("project_id", 27);
        List<ApplicationRecord> applicationRecordList = applicationRecordService.list(queryMapper);
        Map<String, Object> dataMap = new HashMap<>();

        for (ApplicationRecord applicationRecord : applicationRecordList) {
            List<JSONObject> jsonObjects = (List<JSONObject>) JSONObject.parse(applicationRecord.getFormJson());
            JSONObject jsonObject = jsonObjects.get(0);
            //判断是否存在dynamicTableVlue
            boolean dynamicTableVlueFlag = jsonObject.containsKey("dynamicTableVlue");
            if (!dynamicTableVlueFlag) {
                List<JSONObject> dynamicTableList = new ArrayList<JSONObject>();
                //查询存在多少个动态数据对象
                for (int i = 0; i < jsonObject.size(); i++) {
                    boolean hthFlag = jsonObject.containsKey("HTH_" + i);
                    //存在数据
                    if (hthFlag) {
                        //生成一个动态数据对象
                        HashMap<String, Object> dynamicTableObj = new HashMap<String, Object>();
                        dynamicTableObj.put("HTH_" + i, jsonObject.get("HTH_" + i));
                        dynamicTableObj.put("SQHKR_" + i, jsonObject.get("SQHKR_" + i));
                        dynamicTableObj.put("RZZLJGMC_" + i, jsonObject.get("RZZLJGMC_" + i));
                        dynamicTableObj.put("SFK_" + i, jsonObject.get("SFK_" + i));
                        dynamicTableObj.put("RZE_" + i, jsonObject.get("RZE_" + i));
                        dynamicTableObj.put("DYNLXZE_" + i, jsonObject.get("DYNLXZE_" + i));
                        dynamicTableList.add(new JSONObject(dynamicTableObj));
                    }
                }
                //添加到原来json数据中
                if (dynamicTableList.size() > 0) {
                    jsonObject.put("dynamicTableVlue", dynamicTableList);
                }
                List<JSONObject> jsonListNew = new ArrayList<>();
                jsonListNew.add(jsonObject);

                //开始更新数据库
                applicationRecord.setFormJson(jsonListNew.toString());
                applicationRecordService.saveOrUpdate(applicationRecord);
            }
        }
        return RequestResult.ok("更新完成");
    }


    @PostMapping("updateEnterprisesReorganization1")
    @ApiOperation("2020年东莞市鼓励和支持“倍增计划”企业兼并重组资金_1")
    public RequestResult updateEnterprisesReorganization1() {
        // 项目id = 26
        // Step.1 查询导出的所有数据
        QueryWrapper<ApplicationRecord> queryMapper = new QueryWrapper<>();
        queryMapper.eq("project_id", 26);
        List<ApplicationRecord> applicationRecordList = applicationRecordService.list(queryMapper);
        Map<String, Object> dataMap = new HashMap<>();

        for (ApplicationRecord applicationRecord : applicationRecordList) {
            List<JSONObject> jsonObjects = (List<JSONObject>) JSONObject.parse(applicationRecord.getFormJson());
            JSONObject jsonObject = jsonObjects.get(0);

            //判断是否存在dynamicValue
            boolean dynamicReorganizationInformationValueFlag = jsonObject.containsKey("dynamicReorganizationInformationValue");
            if (!dynamicReorganizationInformationValueFlag) {
                List<JSONObject> dynamicReorganizationInformationValueList = new ArrayList<JSONObject>();
                //查询存在多少个动态数据对象
                for (int i = 0; i < jsonObject.size(); i++) {
                    boolean falg = jsonObject.containsKey("BGQYMC_" + i);
                    boolean falg1 = jsonObject.containsKey("BGQYZCD_" + i);
                    if (falg && falg1) {
                        //存在数据
                        //生成一个动态数据对象
                        HashMap<String, Object> dynamicTableObj = new HashMap<String, Object>();
                        String bgqymc = "BGQYMC_" + i;
                        String bgqyzcd = "BGQYZCD_" + i;
                        String bgqycode = "BGQYCODE_" + i;
                        String bgqyzczj = "BGQYZCZJ_" + i;
                        String bgqybgjy = "BGQYBGJY_" + i;
                        String bgqybgsj = "BGQYBGSJ_" + i;
                        String bgqybl = "BGQYBL_" + i;
                        dynamicTableObj.put(bgqymc, jsonObject.get(bgqymc));
                        dynamicTableObj.put(bgqyzcd, jsonObject.get(bgqyzcd));
                        dynamicTableObj.put(bgqycode, jsonObject.get(bgqycode));
                        dynamicTableObj.put(bgqyzczj, jsonObject.get(bgqyzczj));
                        dynamicTableObj.put(bgqybgjy, jsonObject.get(bgqybgjy));
                        dynamicTableObj.put(bgqybgsj, jsonObject.get(bgqybgsj));
                        dynamicTableObj.put(bgqybl, jsonObject.get(bgqybl));
                        dynamicReorganizationInformationValueList.add(new JSONObject(dynamicTableObj));
                    }

                }

                if (dynamicReorganizationInformationValueList.size() > 0) {
                    jsonObject.put("dynamicReorganizationInformationValue", dynamicReorganizationInformationValueList);
                }
                List<JSONObject> jsonListNew = new ArrayList<>();
                jsonListNew.add(jsonObject);
                //开始更新数据库
                applicationRecord.setFormJson(jsonListNew.toString());
                applicationRecordService.saveOrUpdate(applicationRecord);
            }
        }
        return RequestResult.ok("更新完成");
    }


    @PostMapping("updateEnterprisesReorganization2")
    @ApiOperation("2020年东莞市鼓励和支持“倍增计划”企业兼并重组资金_2")
    public RequestResult updateEnterprisesReorganization2() {
        // 项目id = 26
        // Step.1 查询导出的所有数据
        QueryWrapper<ApplicationRecord> queryMapper = new QueryWrapper<>();
        queryMapper.eq("project_id", 26);
        List<ApplicationRecord> applicationRecordList = applicationRecordService.list(queryMapper);
        Map<String, Object> dataMap = new HashMap<>();

        for (ApplicationRecord applicationRecord : applicationRecordList) {
            List<JSONObject> jsonObjects = (List<JSONObject>) JSONObject.parse(applicationRecord.getFormJson());
            JSONObject jsonObject = jsonObjects.get(0);

            //判断是否存在dynamicValue
            boolean dynamicIncentiveShareholedersValueFlag = jsonObject.containsKey("dynamicIncentiveShareholedersValue");
            if (!dynamicIncentiveShareholedersValueFlag) {
                List<JSONObject> dynamicIncentiveShareholedersValueList = new ArrayList<JSONObject>();

                //查询存在多少个动态数据对象
                for (int i = 0; i < jsonObject.size(); i++) {
                    //判断企业推荐申报资本利得奖励股东名单判断
                    boolean lineNameFalg = jsonObject.containsKey("line_name_" + i);
                    boolean linePassportFlag = jsonObject.containsKey("line_passport_" + i);
                    if (lineNameFalg && linePassportFlag) {
                        HashMap<String, Object> dynamicIncentiveShareholedersObj = new HashMap<String, Object>();
                        String lineName = "line_name_" + i;
                        String linePassport = "line_passport_" + i;
                        String linePassportPercent = "line_passport_percent" + i;
                        String lineMoney = "line_money_" + i;

                        dynamicIncentiveShareholedersObj.put(lineName, jsonObject.get(lineName));
                        dynamicIncentiveShareholedersObj.put(linePassport, jsonObject.get(linePassport));
                        dynamicIncentiveShareholedersObj.put(linePassportPercent, jsonObject.get(linePassportPercent));
                        dynamicIncentiveShareholedersObj.put(lineMoney, jsonObject.get(lineMoney));
                        dynamicIncentiveShareholedersValueList.add(new JSONObject(dynamicIncentiveShareholedersObj));
                    }
                }

                if (dynamicIncentiveShareholedersValueList.size() > 0) {
                    jsonObject.put("dynamicIncentiveShareholedersValue", dynamicIncentiveShareholedersValueList);
                }

                List<JSONObject> jsonListNew = new ArrayList<>();
                jsonListNew.add(jsonObject);

                //开始更新数据库
                applicationRecord.setFormJson(jsonListNew.toString());
                applicationRecordService.saveOrUpdate(applicationRecord);
            }
        }
        return RequestResult.ok("更新完成");
    }


    @PostMapping("updateReceivablesFinanceAward")
    @ApiOperation("2020年省应收账款融资奖励项目资金申请表")
    public RequestResult updateReceivablesFinanceAward() {
        // 项目id = 29
        // Step.1 查询导出的所有数据
        QueryWrapper<ApplicationRecord> queryMapper = new QueryWrapper<>();
        queryMapper.eq("project_id", 29);
        List<ApplicationRecord> applicationRecordList = applicationRecordService.list(queryMapper);
        Map<String, Object> dataMap = new HashMap<>();

        for (ApplicationRecord applicationRecord : applicationRecordList) {
            List<JSONObject> jsonObjects = (List<JSONObject>) JSONObject.parse(applicationRecord.getFormJson());
            JSONObject jsonObject = jsonObjects.get(0);
            //判断是否存在dynamicValue
            boolean dynamicReceivablesFinanceAwardValueFlag = jsonObject.containsKey("dynamicReceivablesFinanceAwardValue");
            if (!dynamicReceivablesFinanceAwardValueFlag) {
                List<JSONObject> dynamicReceivablesFinanceAwardValueList = new ArrayList<JSONObject>();

                //查询存在多少个动态数据对象
                for (int i = 0; i < jsonObject.size(); i++) {
                    boolean falg = jsonObject.containsKey("XH_" + i);
                    if (falg) {
                        //存在数据
                        //生成一个动态数据对象
                        HashMap<String, Object> dynamicTableObj = new HashMap<String, Object>();
                        String xh = "XH_" + i;
                        String zxqymc_ = "ZXQYMC_" + i;
                        String sszj = "SSZJ_" + i;
                        String dkje = "DKJE_" + i;
                        String hdyh = "HDYH_" + i;
                        String fkrq = "FKRQ_" + i;
                        String hkrq = "HKRQ_" + i;

                        dynamicTableObj.put(xh, jsonObject.get(xh));
                        dynamicTableObj.put(zxqymc_, jsonObject.get(zxqymc_));
                        dynamicTableObj.put(sszj, jsonObject.get(sszj));
                        dynamicTableObj.put(dkje, jsonObject.get(dkje));
                        dynamicTableObj.put(hdyh, jsonObject.get(hdyh));
                        dynamicTableObj.put(fkrq, jsonObject.get(fkrq));
                        dynamicTableObj.put(hkrq, jsonObject.get(hkrq));

                        dynamicReceivablesFinanceAwardValueList.add(new JSONObject(dynamicTableObj));
                    }
                }
                //添加到原来json数据中
                if (dynamicReceivablesFinanceAwardValueList.size() > 0) {
                    jsonObject.put("dynamicReceivablesFinanceAwardValue", dynamicReceivablesFinanceAwardValueList);
                }

                List<JSONObject> jsonListNew = new ArrayList<>();
                jsonListNew.add(jsonObject);

                //开始更新数据库
                applicationRecord.setFormJson(jsonListNew.toString());
                applicationRecordService.saveOrUpdate(applicationRecord);
            }
        }
        return RequestResult.ok("更新完成");
    }


}
