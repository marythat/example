-- 增加待审核节点ID字段  2020-04-16 arthur
ALTER TABLE nst_application_record ADD COLUMN `next_node_id` varchar (20) DEFAULT NULL COMMENT '待审核节点ID';

-- 增加待审核节点名称字段  2020-04-16 arthur
ALTER TABLE nst_application_record ADD COLUMN `next_node_name` varchar (50) DEFAULT NULL COMMENT '待审核节点名称';

--新增企业可申报数 2020-04-15 blue
ALTER TABLE `nst_project_application`.`nst_project_information`
ADD COLUMN `declare_count` int(11) NULL COMMENT '可申报数' AFTER `business_name`;



