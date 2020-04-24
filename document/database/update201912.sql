ALTER TABLE  nst_application_record
CHANGE COLUMN create_date create_time datetime(0) NULL DEFAULT NULL COMMENT '创建时间' AFTER commit_date,
ADD COLUMN update_time datetime(0) NULL DEFAULT NULL COMMENT '更新时间' AFTER create_time;