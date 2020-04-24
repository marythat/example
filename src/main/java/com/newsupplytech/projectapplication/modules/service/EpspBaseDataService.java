package com.newsupplytech.projectapplication.modules.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Maps;
import com.newsupplytech.projectapplication.comm.base.DefaultPage;
import com.newsupplytech.projectapplication.comm.base.RequestResult;
import com.newsupplytech.projectapplication.comm.core.annotion.DataSource;
import com.newsupplytech.projectapplication.comm.core.enums.DataSourceEnum;
import com.newsupplytech.projectapplication.comm.core.enums.FiledsTipsEnum;
import com.newsupplytech.projectapplication.comm.utils.SessionUserInfo;
import com.newsupplytech.projectapplication.comm.utils.StringUtils;
import com.newsupplytech.projectapplication.modules.entity.*;
import com.newsupplytech.projectapplication.modules.mapper.*;
import com.newsupplytech.projectapplication.modules.vo.DepartmentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@DataSource(value= DataSourceEnum.DB2)
public class EpspBaseDataService {
    @Autowired
    private IndustryTypeMapper industryTypeMapper;
    @Autowired
    private CompanyBusinessMapper companyBusinessMapper;
    @Autowired
    private EpspUserMapper userMapper;
    @Autowired
    private DepartmentUserService departmentUserService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private CompanyUserService companyUserService;
    @Autowired
    private ExpertUserService expertUserService;
    @Autowired
    private GuildUserService guildUserService;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private CompanyPersonMapper companyPersonMapper;
    @Autowired
    private StreetMapper streetMapper;
    @Autowired
    private UserPerMapper userPerMapper;
    @Autowired
    private CompanyExcelBusinessMapper companyExcelBusinessMapper;
    @Autowired
    private FiledsTipsMapper filedsTipsMapper;

    public List<IndustryType> industryTypeMapperList(){
        return industryTypeMapper.selectList(new QueryWrapper<>());
    }

    public List<CompanyBusiness> companyBusinessList(){
        return companyBusinessMapper.selectList(new QueryWrapper<>());
    }

    /**
     * 规上企业是否需要补充信息
     * @param companyId
     * @return
     */
    public RequestResult getTips(Integer companyId, String field){
        Map map = Maps.newHashMap();
        QueryWrapper<CompanyExcelBusiness> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("companyId",companyId);
        queryWrapper.orderByDesc("month,dataTime");
        List<CompanyExcelBusiness> list = companyExcelBusinessMapper.selectList(queryWrapper);
        List<FiledsTips> filedsTipsList = filedsTipsMapper.selectList(null);
        Map<String, CompanyExcelBusiness> companyExcelBusinessMap = Maps.newHashMap();
        for(CompanyExcelBusiness companyExcelBusiness:list){
            companyExcelBusinessMap.put(companyExcelBusiness.getDataTime().toString(),companyExcelBusiness);
        }
        map.put("companyExcelBusinessMap",companyExcelBusinessMap);
        Map<String, FiledsTips> filedsTipsMap = Maps.newHashMap();
        for(FiledsTips filedsTips:filedsTipsList){
            filedsTipsMap.put(filedsTips.getFiled(),filedsTips);
        }
        try {
            Class cls = CompanyExcelBusiness.class;
            if(StringUtils.isEmpty(field)) {
                String[] fields = CompanyExcelBusiness.operationField;
                for(int x=0;x<3;x++){
                    if(companyExcelBusinessMap.containsKey(CompanyExcelBusiness.years[x])){
                        for(int i=0; i<fields.length; i++){
                            String name = fields[i];
                            if(StringUtils.isNotEmpty(FiledsTipsEnum.getMessage(name+"_"+ CompanyExcelBusiness.years[x]))) {
                                Method target = cls.getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1), new Class[]{});
                                if (StringUtils.isEmpty(target.invoke(companyExcelBusinessMap.get(CompanyExcelBusiness.years[x]), new CompanyExcelBusiness[]{}))) {
                                    map.put("filedsTips",filedsTipsMap.get(FiledsTipsEnum.getMessage(name + "_" + CompanyExcelBusiness.years[x])));
                                    return RequestResult.ok(map);
                                }
                            }
                        }
                    }else{
                        map.put("filedsTips",filedsTipsMap.get(FiledsTipsEnum.getMessage(CompanyExcelBusiness.years[x])));
                        return RequestResult.ok(map);
                    }
                }
            }else{
                map.put("filedsTips",filedsTipsMap.get(FiledsTipsEnum.getMessage(field)));
                return RequestResult.ok(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return RequestResult.ok(map);
    }

    public EpspUser getUser(Long userId){
        QueryWrapper<EpspUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        return userMapper.selectOne(queryWrapper);
    }

    public SessionUserInfo sessionUserInfo(Long userId){
        EpspUser user = this.getUser(userId);
        SessionUserInfo sessionUserInfo = new SessionUserInfo();
        if(!StringUtils.isEmpty(user.getOrgid().toString())){
            if(user.getOrgid().equals(SessionUserInfo.ORG_ID_DEPART)){
                sessionUserInfo.setDepartmentUser(departmentUserService.getOne(user.getUserid()));
                sessionUserInfo.setDepartment(departmentService.getOne(sessionUserInfo.getDepartmentUser().getDptid()));
            }else if(user.getOrgid().equals(SessionUserInfo.ORG_ID_COMPANY)){
                sessionUserInfo.setCompanyUser(companyUserService.getOne(user.getUserid()));
            }else if(user.getOrgid().equals(SessionUserInfo.ORG_ID_GUILD)){
                sessionUserInfo.setGuildUser(guildUserService.getOne(user.getUserid()));
            }else if(user.getOrgid().equals(SessionUserInfo.ORG_ID_EXPERT)){
                sessionUserInfo.setExpertUser(expertUserService.getOne(user.getUserid()));
            }
            sessionUserInfo.setEpspUser(user);
        }
        return sessionUserInfo;
    }
    public List<DepartmentModel> departmentTree() {
        List<Department> list = departmentMapper.selectList(null);
        List<DepartmentModel> treeList = new ArrayList<>();
        List<DepartmentModel> listResult = findChildren(list,treeList);
        return listResult;
    }
    public List<Department> getDepartments(String dptIds) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper();
        queryWrapper.in("dptId",(Object[])dptIds.split(","));
        List<Department> list = departmentMapper.selectList(queryWrapper);
        return list;
    }
    public IPage departmentUser(Long dptId,Integer pageNo,Integer pageSize) {
        QueryWrapper<DepartmentUser> queryWrapper = new QueryWrapper();
        IPage<DepartmentUser> page = new DefaultPage<>(pageNo, pageSize);
        queryWrapper.eq("dptId",dptId);
        departmentUserService.page(page,queryWrapper);
        return page;
    }
    public List<EpspUser> searchUser(String ids) {
        QueryWrapper<EpspUser> queryWrapper = new QueryWrapper();
        if(StringUtils.isNotEmpty(ids)) {
            queryWrapper.in("userId", Arrays.asList(ids.split(",")));
            return userMapper.selectList(queryWrapper);
        }else {
            return null;
        }
    }

    /**
     * departmentTree的子方法 ====2=====
     * 该方法是找到并封装顶级父类的节点到TreeList集合
     */
    private static List<DepartmentModel> findChildren(List<Department> recordList,
                                                      List<DepartmentModel> treeList) {

        for (int i = 0; i < recordList.size(); i++) {
            Department branch = recordList.get(i);
            if (branch.getParentdptid()==0) {
                DepartmentModel departIdModel = new DepartmentModel().convert(branch);
                treeList.add(departIdModel);
            }
        }
        getGrandChildren(recordList,treeList);

        return treeList;
    }

    /**
     * departmentTree的子方法====3====
     *该方法是找到顶级父类下的所有子节点集合并封装到TreeList集合
     */
    private static void getGrandChildren(List<Department> recordList, List<DepartmentModel> treeList) {

        for (int i = 0; i < treeList.size(); i++) {
            DepartmentModel model = treeList.get(i);
            for (int i1 = 0; i1 < recordList.size(); i1++) {
                Department m = recordList.get(i1);
                if (m.getParentdptid()!=null && m.getParentdptid().equals(model.getKey())) {
                    DepartmentModel dm = new DepartmentModel().convert(m);
                    model.getChildren().add(dm);
                }
            }
            getGrandChildren(recordList,treeList.get(i).getChildren());
        }

    }
    public List<CompanyPerson> getCompanyPersons(SessionUserInfo sessionUserInfo){
        QueryWrapper queryWrapper = new QueryWrapper<CompanyPerson>();
        queryWrapper.eq("userId",sessionUserInfo.getEpspUser().getUserid());
        CompanyUser companyUser = companyUserService.getOne(queryWrapper);
        queryWrapper = new QueryWrapper();
        queryWrapper.eq("companyId",companyUser.getCompanyid());
        List<CompanyPerson> companyPeoples = companyPersonMapper.selectList(queryWrapper);
        return companyPeoples;
    }
    public CompanyPerson getCompanyPerson(String phone){
        QueryWrapper queryWrapper = new QueryWrapper<CompanyPerson>();
        queryWrapper.eq("mobile",phone);
        CompanyPerson companyPeople = companyPersonMapper.selectOne(queryWrapper);
        return companyPeople;
    }
    public String getStreet(Integer streetId){
        QueryWrapper queryWrapper = new QueryWrapper<Street>();
        queryWrapper.eq("id",streetId);
        return streetMapper.selectOne(queryWrapper).getName();
    }
    public Street queryStreet(Integer streetId){
        QueryWrapper queryWrapper = new QueryWrapper<Street>();
        queryWrapper.eq("id",streetId);
        return streetMapper.selectOne(queryWrapper);
    }
    public UserPer queryUserPer(Long accountId){
        QueryWrapper queryWrapper = new QueryWrapper<Street>();
        queryWrapper.eq("ACCOUNT_ID",accountId);
        return userPerMapper.selectOne(queryWrapper);
    }
}
