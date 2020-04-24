DROP TABLE IF EXISTS nst_flow_execute_record;
CREATE TABLE nst_flow_execute_record(
	id BIGINT (15) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
	flow_name VARCHAR (50) COMMENT '节点名称',
	project_id BIGINT (15) COMMENT '项目ID',
	form_json VARCHAR (50) COMMENT '表单提交的Json',
	executor_id BIGINT (15) COMMENT '执行人ID',
	executor_name VARCHAR (50) COMMENT '执行人名称',
	create_date DATETIME COMMENT '执行时间',
	PRIMARY KEY (id)
)ENGINE = InnoDB
 DEFAULT CHARSET = utf8
 COLLATE = utf8_bin COMMENT '项目流程执行记录';
