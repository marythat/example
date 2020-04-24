DROP TABLE IF EXISTS nst_application_record;
CREATE TABLE nst_application_record(
	id BIGINT (15) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
	project_id BIGINT (15) COMMENT '项目ID',
	company_id BIGINT (15) COMMENT '企业ID',
	company_name VARCHAR (50) COMMENT '企业名称',
	form_json VARCHAR (50) COMMENT '表单提交的Json',
	create_by VARCHAR (50) COMMENT '申请创建人ID',
	creator_name VARCHAR (50) COMMENT '申请创建人名称',
	commit_date DATETIME COMMENT '申请提交时间',
	create_date DATETIME COMMENT '申请创建时间',
	PRIMARY KEY (id)
)ENGINE = InnoDB
 DEFAULT CHARSET = utf8
 COLLATE = utf8_bin COMMENT '企业申报记录';
