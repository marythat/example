package com.newsupplytech.projectapplication.modules.controller;


import com.newsupplytech.projectapplication.modules.service.AutoSavePropertiesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 申请记录自动保存属性表
 * @Date:   2020-02-20
 * @Version: V1.0
 */
@RestController
@RequestMapping("/AutoSaveProperties")
@Slf4j
public class AutoSavePropertiesController {
	@Autowired
	private AutoSavePropertiesService autoSavePropertiesService;
}
