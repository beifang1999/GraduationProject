/*
 Navicat Premium Data Transfer

 Source Server         : Hadoop01
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : 192.168.193.111:3306
 Source Schema         : travel_re

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 22/03/2022 16:45:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for table_process
-- ----------------------------
DROP TABLE IF EXISTS `table_process`;
CREATE TABLE `table_process`  (
  `source_table` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '来源表',
  `operate_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作类型 insert,update,delete',
  `sink_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '输出类型 hbase kafka',
  `sink_table` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '输出表(主题)',
  `sink_columns` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '输出字段',
  `sink_pk` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主键字段',
  `sink_extend` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '建表扩展',
  PRIMARY KEY (`source_table`, `operate_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of table_process
-- ----------------------------
INSERT INTO `table_process` VALUES ('dwd_order', 'insert', 'kafka', 'dwd_order', 'user_id,user_mobile,product_id,product_traffic,product_traffic_grade,product_traffic_type,product_pub,travel_member_adult,travel_member_yonger,travel_member_baby,product_price,has_activity,product_fee,order_id,order_ct', 'order_id', NULL);
INSERT INTO `table_process` VALUES ('product', 'delete', 'hbase', 'dim_product', '', NULL, NULL);
INSERT INTO `table_process` VALUES ('product', 'insert', 'hbase', 'dim_product', 'product_id,product_title,product_level,travel_day,product_type,product_price,departure_code,des_city_code,toursim_tickets_type', 'product_id', NULL);
INSERT INTO `table_process` VALUES ('product', 'update', 'hbase', 'dim_product', 'product_id,product_title,product_level,travel_day,product_type,product_price,departure_code,des_city_code,toursim_tickets_type', NULL, NULL);
INSERT INTO `table_process` VALUES ('pub', 'delete', 'hbase', 'dim_pub', NULL, NULL, NULL);
INSERT INTO `table_process` VALUES ('pub', 'insert', 'hbase', 'dim_pub', 'pub_id,pub_name,pub_star,pub_grade_desc,pub_grade,pub_area_code,pub_address,is_national', 'pub_id', NULL);
INSERT INTO `table_process` VALUES ('pub', 'update', 'hbase', 'dim_pub', 'pub_id,pub_name,pub_star,pub_grade_desc,pub_grade,pub_area_code,pub_address,is_national', NULL, NULL);
INSERT INTO `table_process` VALUES ('region', 'delete', 'hbase', 'dim_region', NULL, NULL, NULL);
INSERT INTO `table_process` VALUES ('region', 'insert', 'hbase', 'dim_region', 'region_city_code,region_city_name,region_province_code,region_province_name,iso_code', 'region_city_code', NULL);
INSERT INTO `table_process` VALUES ('region', 'update', 'hbase', 'dim_region', 'region_city_code,region_city_name,region_province_code,region_province_name,iso_code', 'region_city_code', NULL);

SET FOREIGN_KEY_CHECKS = 1;
