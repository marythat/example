/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MariaDB
 Source Server Version : 100403
 Source Host           : 127.0.0.1:3306
 Source Schema         : nst_project_application

 Target Server Type    : MariaDB
 Target Server Version : 100403
 File Encoding         : 65001

 Date: 16/04/2020 19:38:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_val` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典值',
  `dict_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '创新创业奖励政策', '1', 'sys_project', '0', '', NULL, '', NULL, '项目分类列表');
INSERT INTO `sys_dict_type` VALUES (2, '协同倍增', '2', 'sys_project', '0', '', NULL, '', NULL, '项目分类列表');
INSERT INTO `sys_dict_type` VALUES (3, '智能制造专项', '3', 'sys_project', '0', '', NULL, '', NULL, '项目分类列表');
INSERT INTO `sys_dict_type` VALUES (4, '服务型制造专项', '4', 'sys_project', '0', '', NULL, '', NULL, '项目分类列表');
INSERT INTO `sys_dict_type` VALUES (5, '省促进经济发展专项', '5', 'sys_project', '0', '', NULL, '', NULL, '项目分类列表');
INSERT INTO `sys_dict_type` VALUES (6, '绿色制造专项', '6', 'sys_project', '0', '', NULL, '', NULL, '项目分类列表');
INSERT INTO `sys_dict_type` VALUES (7, '倍增专项', '7', 'sys_project', '0', '', NULL, '', NULL, '项目分类列表');
INSERT INTO `sys_dict_type` VALUES (8, '其他', '8', 'sys_project', '0', '', NULL, '', NULL, '项目分类列表');

SET FOREIGN_KEY_CHECKS = 1;
