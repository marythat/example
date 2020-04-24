DROP TABLE IF EXISTS nst_project_flow_node;
CREATE TABLE nst_project_flow_node(
	id BIGINT (15) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
	flow_name VARCHAR (50) COMMENT '节点名称',
	flow_seq BIGINT (15) COMMENT '序号',
	pre_flow_id BIGINT (15) COMMENT '上一节点ID',
	`type` VARCHAR (50) COMMENT '节点类型',
	project_id BIGINT (15) COMMENT '项目ID',
	enforce_depart BIGINT (15) COMMENT '执行部门ID',
	executor_id VARCHAR (50) COMMENT '执行人ID(可能有多个)',
	PRIMARY KEY (id)
)ENGINE = InnoDB
 DEFAULT CHARSET = utf8
 COLLATE = utf8_bin COMMENT '项目流程节点定义';
