package com.newsupplytech.projectapplication.modules.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newsupplytech.projectapplication.comm.base.BaseController;
import com.newsupplytech.projectapplication.comm.base.DefaultPage;
import com.newsupplytech.projectapplication.comm.base.RequestResult;
import com.newsupplytech.projectapplication.comm.core.exception.NstException;
import com.newsupplytech.projectapplication.comm.utils.HttpQueryWrapper;
import com.newsupplytech.projectapplication.modules.entity.ProjectFlowNode;
import com.newsupplytech.projectapplication.modules.service.ProjectFlowNodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/projectFlowNode")
@Api(tags="项目信息")
public class ProjectFlowNodeController extends BaseController {
    @Autowired
    private ProjectFlowNodeService projectFlowNodeService;

    @PostMapping("save")
    @ApiOperation("保存(新增/更新)")
    public RequestResult save(@RequestBody ProjectFlowNode projectFlowNode){
        boolean bool=true;
        String message="";
        try {
            bool= projectFlowNodeService.saveOrUpdate(projectFlowNode);
        }catch (NstException e){
            bool=false;
            message=e.getMessage();
        }
        if(bool){
            return RequestResult.ok("更新完成");
        }else{
            return RequestResult.error(message);
        }

    }

    @GetMapping("list")
    @ApiOperation("分页查询")
    public RequestResult list(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                              HttpServletRequest request){
        QueryWrapper<ProjectFlowNode> queryWrapper = HttpQueryWrapper.genQueryWrapper(request.getParameterMap());
        queryWrapper.ne("is_del","1");
        IPage<ProjectFlowNode> page = new DefaultPage<>(pageNo,pageSize);
        projectFlowNodeService.page(page, queryWrapper);
        return RequestResult.ok(page);
    }

    @DeleteMapping("remove/{id}")
    @ApiOperation("根据ID删除")
    public RequestResult remove(@PathVariable("id")Long id){
        projectFlowNodeService.removeById(id);
        return RequestResult.ok("删除成功");
    }
}
