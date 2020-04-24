-- 新增项目记录字段   2020-03-3 chad
ALTER TABLE `nst_project_information`
  ADD COLUMN `auto_save_properties` varchar(255) NULL COMMENT '自动保存的属性';

-- 新增项目自定义键值对表  2020-03-4 chad
DROP TABLE IF EXISTS `nst_auto_save_properties`;
CREATE TABLE `nst_auto_save_properties` (
                                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                          `user_id` int(11) DEFAULT NULL COMMENT '申请人id',
                                          `project_id` int(11) DEFAULT NULL COMMENT '项目id',
                                          `apply_id` int(11) DEFAULT NULL COMMENT '申请记录id',
                                          `auto_key` varchar(64) DEFAULT NULL COMMENT '键',
                                          `auto_value` varchar(64) DEFAULT NULL COMMENT '值',
                                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `is_del` int(11) DEFAULT NULL COMMENT '是否删除（0否1是）',
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- 增加必须上传文件   2020-03-03 arthur
ALTER TABLE nst_project_information ADD COLUMN `necessary_upload` varchar (200) DEFAULT null COMMENT '必须上传文件';
-- 增加必须上传文件   2020-03-03 arthur
ALTER TABLE nst_project_information ADD COLUMN `business_name` varchar (50) DEFAULT null COMMENT '业务名称';

ALTER TABLE `nst_comm_attachment` ADD COLUMN `ext_property` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '附件额外属性' AFTER `type`;

-- 新增受理编号id   2020-03-5 chad
ALTER TABLE `nst_application_record`
  ADD COLUMN `accept_code` varchar(55) NULL COMMENT '受理编号';

-- 新增创建人所属部门字段   2020-03-10 arthur
ALTER TABLE `nst_flow_execute_record`
  ADD COLUMN `create_Belong_Dept` varchar(55) NULL COMMENT '创建人所属部门';

-- 增加短信通知人员字段  2020-03-12 arthur
ALTER TABLE nst_project_flow_node ADD COLUMN `sms_notification_names` varchar (200) DEFAULT NULL COMMENT '短信通知人员';

-- 新增记录项目申请权限   2020-03-12 chad
DROP TABLE IF EXISTS `nst_company_permission`;
CREATE TABLE `nst_company_permission` (
                                        `id` bigint(15) NOT NULL AUTO_INCREMENT,
                                        `company_id` int(11) DEFAULT NULL COMMENT '企业id',
                                        `project_id` int(11) DEFAULT NULL COMMENT '项目id',
                                        `permission_keys` longtext COMMENT '授权对象',
                                        `is_del` int(11) DEFAULT NULL COMMENT '是否删除（0否1是）',
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 增加短信通知人员字段  2020-03-12 arthur
ALTER TABLE nst_application_record ADD COLUMN `sms_notification_names` varchar (200) DEFAULT NULL COMMENT '短信通知人员';

-- 增加创建申请的companyPerson的mobile  2020-03-20 chad
ALTER TABLE nst_application_record ADD COLUMN `create_person` varchar (200) DEFAULT NULL COMMENT '创建申请的companyPerson的mobile';


-- 增大必传附件长度 2020-03-30
ALTER TABLE nst_project_information
MODIFY COLUMN `necessary_upload` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '必须上传文件';




