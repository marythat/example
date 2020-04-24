DROP TABLE IF EXISTS nst_comm_attachment;
CREATE TABLE nst_comm_attachment(
	id BIGINT (15) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
	related_table VARCHAR (50) COMMENT '关联的表名称',
	related_id VARCHAR (50) COMMENT '关联表的ID',
	file_name VARCHAR (50) COMMENT '文件名称',
	file_path VARCHAR (50) COMMENT '文件路径',
	PRIMARY KEY (id)
)ENGINE = InnoDB
 DEFAULT CHARSET = utf8
 COLLATE = utf8_bin COMMENT '通用文件存储';
