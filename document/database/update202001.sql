ALTER TABLE nst_application_record CHANGE form_json form_json TEXT COMMENT '表单提交的Json';


-- 修改申请记录表添加申请记录状态相关字段  2020-01-03 Bill
ALTER TABLE nst_application_record
ADD COLUMN cur_node bigint(15) NULL COMMENT '当前的节点ID' AFTER commit_date,
ADD COLUMN status varchar(16) NULL COMMENT '申请状态' AFTER cur_node,
MODIFY COLUMN create_by bigint(15) NULL DEFAULT NULL COMMENT '申请创建人ID' AFTER commit_date;

-- 修改项目   2020-01-03 Bill
ALTER TABLE nst_project_information
MODIFY COLUMN project_status varchar(16) NULL DEFAULT NULL COMMENT '项目状态: AVAILABLE 接受申报 CLOSED 申报结束' AFTER apply_form_url;

-- 修改执行结果相关字段   2020-01-03 Bill
ALTER TABLE nst_flow_execute_record
CHANGE COLUMN create_date create_time datetime(0) NULL DEFAULT NULL COMMENT '执行时间' AFTER executor_name,
ADD COLUMN application_id bigint(15) NULL COMMENT '申请ID' AFTER project_id,
ADD COLUMN comments text NULL COMMENT '审批意见' AFTER application_id,
ADD COLUMN approval_result varchar(16) NULL COMMENT '审批结果 通过 不通过 回滚' AFTER comments;


-- 新增节点名称字段   2020-01-04 Arthur
ALTER TABLE nst_application_record
  ADD COLUMN flow_name varchar(50) NULL COMMENT '当前节点的名称' AFTER cur_node;

-- 新增节点id字段   2020-01-04 Arthur
ALTER TABLE nst_flow_execute_record
  ADD COLUMN cur_node bigint(15) NULL COMMENT '当前节点的ID' AFTER create_time;

--修改表单json字段为文本类 2020-01-04  Arthur
ALTER TABLE nst_flow_execute_record CHANGE form_json form_json TEXT COMMENT '表单提交的Json';

-- 修改执行部门字段类型   2020-01-06 Ljh
ALTER TABLE nst_project_flow_node
  modify COLUMN `enforce_depart` varchar(100) DEFAULT NULL COMMENT '执行部门ID';

-- 新增申请记录字段   2020-01-07 Arthur
ALTER TABLE nst_application_record
  ADD COLUMN enforce_depart BIGINT (15) COMMENT '执行部门ID' AFTER cur_node,
  ADD COLUMN executor_id VARCHAR (50) COMMENT '执行人ID(可能有多个)' AFTER cur_node;

-- 增加申请类型   2020-01-07 Ljh
ALTER TABLE nst_project_flow_node
  add COLUMN `apply_type` varchar(16) DEFAULT NULL COMMENT 'USER用户DEPART部门';

-- 新增项目字段   2020-01-08 blue
ALTER TABLE nst_project_information
ADD COLUMN project_type varchar(50) NULL COMMENT '项目分类：1项目发布人2业务名称' AFTER project_code;

-- 新增可视用户的ids 2020-01-13 Ljh
ALTER TABLE nst_application_record
  ADD COLUMN permission_keys text COMMENT '可视用户的mobiles';

-- 修改form_json字段类型   2020-01-06 Ljh
ALTER TABLE nst_flow_execute_record
  modify COLUMN `form_json` LONGTEXT DEFAULT NULL COMMENT '表单提交的Json';

-- 新增镇街id 2020-01-13 Ljh
ALTER TABLE nst_application_record
  ADD COLUMN street_id Integer COMMENT '镇街id';

-- 新增镇街名称 2020-01-17 Ljh
ALTER TABLE nst_application_record
  ADD COLUMN street varchar(50) COMMENT '镇街名称';

-- 添加附件ID字符串到申请列表 2020-01-18 Bill
ALTER TABLE nst_application_record
ADD COLUMN attachment_ids varchar(255) NULL COMMENT '附件ID字符串' AFTER status;

-- 修改审批状态类型 2020-01-19 Blue
ALTER TABLE `nst_project_application`.`nst_project_information`
MODIFY COLUMN `project_status` varchar(16) CHARACTER SET utf8 COLLATE
utf8_bin NULL DEFAULT NULL COMMENT '项目状态: AVAILABLE 上线 CLOSED 下线ROUGHDRAFT草稿' AFTER `apply_form_url`;

