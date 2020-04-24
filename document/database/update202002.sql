-- 新增项目流程执行记录字段   2020-02-18 chad
ALTER TABLE nst_flow_execute_record
  ADD COLUMN enforce_depart VARCHAR (50) COMMENT '执行部门ID',
  ADD COLUMN create_id BIGINT (15) COMMENT '创建人id',
  ADD COLUMN create_name VARCHAR (50) COMMENT '创建人名称';
-- 修改项目流程执行记录字段   2020-02-18 chad
ALTER TABLE nst_flow_execute_record
  modify COLUMN `executor_id` VARCHAR (200) DEFAULT NULL COMMENT '执行人ID(可能有多个)';
-- 删除项目流程执行记录字段   2020-02-18 chad
ALTER TABLE nst_flow_execute_record DROP COLUMN executor_name
-- 增加是否默认镇街审核标识字段   2020-02-18 chad
ALTER TABLE nst_project_flow_node ADD COLUMN `is_checked` int(2) DEFAULT NULL COMMENT '0非默认镇街审核1默认镇街审核';
-- 新增项目记录字段   2020-02-22 chad
ALTER TABLE `nst_project_application`.`nst_application_record`
  ADD COLUMN `auto_input_properties` varchar(255) NULL COMMENT '自动填写属性，Json';
-- 增加文件上传文件类型   2020-02-24 arthur
ALTER TABLE nst_comm_attachment ADD COLUMN `type` varchar (2) DEFAULT NULL COMMENT '1是模板文件,2申报指南文件';
-- 增加流程流水记录创建人电话号码字段   2020-02-25 arthur
ALTER TABLE nst_flow_execute_record ADD COLUMN `create_phone` varchar (11) DEFAULT NULL COMMENT '创建人电话号码'
-- 自动填写属性，Json长度修改   2020-02-28 blue
ALTER TABLE `nst_project_application`.`nst_application_record`
  MODIFY COLUMN `auto_input_properties` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '自动填写属性，Json' AFTER `street`;
-- 增加删除标记字段   2020-02-28 arthur
ALTER TABLE nst_project_information ADD COLUMN `is_del` varchar (2) DEFAULT 0 COMMENT '0默认有效,1删除状态';
ALTER TABLE nst_application_record ADD COLUMN `is_del` varchar (2) DEFAULT 0 COMMENT '0默认有效,1删除状态';
ALTER TABLE nst_comm_attachment ADD COLUMN `is_del` varchar (2) DEFAULT 0 COMMENT '0默认有效,1删除状态';
ALTER TABLE nst_project_flow_node ADD COLUMN `is_del` varchar (2) DEFAULT 0 COMMENT '0默认有效,1删除状态';

-- 修改申请记录表添加申请记录状态相关字段  2020-02-28 arthur
ALTER TABLE nst_application_record CHANGE auto_input_properties auto_input_properties TEXT COMMENT '自动填写属性，Json';



