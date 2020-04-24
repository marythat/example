DROP TABLE IF EXISTS nst_project_information;
CREATE TABLE nst_project_information(
	id BIGINT (15) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
	project_name VARCHAR (50) COMMENT '项目名称',
	project_code VARCHAR (50) UNIQUE COMMENT '项目编码',
	belong_dept VARCHAR (50) COMMENT '项目所属科室名称',
	belong_dept_id INT (11) COMMENT '所属科室部门Id',
	ask_phone VARCHAR (50) COMMENT '科室咨询电话',
	apply_start_time DATETIME COMMENT '申报开始日期',
	apply_end_time DATETIME COMMENT '申报结束日期',
	apply_conditions VARCHAR (50) COMMENT '申报条件',
	project_data_item VARCHAR (50) COMMENT '项目申报表单的数据项名称json字符串',
	apply_form_url VARCHAR (50) COMMENT '项目申报的填报表单URL地址',
	project_status INT (11) COMMENT '项目状态:1：接受申报 2：申报结束',
	company_tag VARCHAR (50) COMMENT '项目适合的企业类型标签',
	audit_template VARCHAR (50) COMMENT '当前项目审批流转的模板编码',
	create_time DATETIME COMMENT '项目发布时间',
	update_time TIMESTAMP COMMENT '修改时间',
	PRIMARY KEY (id)
)ENGINE = InnoDB
 DEFAULT CHARSET = utf8
 COLLATE = utf8_bin COMMENT '项目申报定义';
