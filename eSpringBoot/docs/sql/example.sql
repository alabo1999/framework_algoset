-- exa数据库 表结构 DDL
-- database name : exa
-- -------------------------------------------------------------------------------------
-- 1.数据库设计原则
-- 1.1.新建表不使用自增ID；便于将来的集群部署、分布式部署等。
-- 1.2.字段除datetime、blob和text类型外，均使用NOT NULL DEFAULT 形式。
-- 1.3.timestamp(mysql有至多到2038年问题)改为datetime类型。
-- 1.4.一般的表加上记录操作信息字段；系统配置记录，操作账号可用默认值，表示管理员配置。
-- 1.5.主键尽量使用int类型，以提高联结和查询性能。
-- 1.6.根据需要，建相关索引。
-- 1.7.日期类型，尽量使用datetime类型，便于比较和计算。
-- 1.8.字段命名，统一用下划线。
-- 1.9.表名使用前缀：exa。
-- 1.10.本系统的appid=1，设定的id取值为：取值范围为[appid*10000+1,appid*10000+9999]。

-- -------------------------------------------------------------------------------------

-- -------------------------------------------------------------------------------------
-- 1、全局ID生成器相关表和函数
-- ------------------------------

-- ----------------------------
-- Table structure for exa_table_code_config
-- ID编码配置表
-- ----------------------------
DROP TABLE IF EXISTS exa_table_code_config;
CREATE TABLE exa_table_code_config
(
  table_id			INT(11)			NOT NULL DEFAULT 0 COMMENT '表ID',
  table_name		VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '表名称',
  field_name		VARCHAR(80)		NOT NULL DEFAULT '' COMMENT 'ID字段名称',

  -- 格式化编码使用，如15编码为HR000015，即数字前面用0填补。
  prefix			VARCHAR(20)		NOT NULL DEFAULT '' COMMENT '编码前缀字符串',
  prefix_len		TINYINT(4)		NOT NULL DEFAULT 0 COMMENT '编码前缀字符串长度',
  seqno_len			TINYINT(4)		NOT NULL DEFAULT 0 COMMENT '序列号长度',
  PRIMARY KEY (table_id)
) ENGINE = Innodb
  DEFAULT CHARSET = utf8 COMMENT 'ID编码配置表';

CREATE INDEX exa_table_code_config_table_name ON exa_table_code_config(table_name);

-- 本系统的table_id以10001开始
insert into exa_table_code_config(table_id,table_name,field_name) 
values(10001,'exa_users','user_id');
insert into exa_table_code_config(table_id,table_name,field_name) 
values(10002,'exa_roles','role_id');
insert into exa_table_code_config(table_id,table_name,field_name) 
values(10003,'exa_user_drs','rec_id');
insert into exa_table_code_config(table_id,table_name,field_name) 
values(10004,'exa_orgnizations','org_id');
insert into exa_table_code_config(table_id,table_name,field_name) 
values(10005,'exa_functions','func_id');



-- ----------------------------
-- Table structure for exa_table_id_allocate
-- ID最新可用值表
-- ----------------------------
DROP TABLE IF EXISTS exa_table_id_allocate;
CREATE TABLE exa_table_id_allocate
(
  table_id			INT(11)			NOT NULL DEFAULT 0 COMMENT '表ID',
  last_id			BIGINT(20)		NOT NULL DEFAULT 0 COMMENT '最新可用ID值',
  PRIMARY KEY (table_id)
) ENGINE = Innodb
  DEFAULT CHARSET = utf8 COMMENT 'ID最新可用值表';

-- 初始记录
-- exa_users 预留10个账号，保留给内部系统使用
insert into exa_table_id_allocate(table_id,last_id) values(10001,11);
insert into exa_table_id_allocate(table_id,last_id) values(10002,4);
insert into exa_table_id_allocate(table_id,last_id) values(10003,1);
insert into exa_table_id_allocate(table_id,last_id) values(10004,2);
insert into exa_table_id_allocate(table_id,last_id) values(10005,1000);

-- ----------------------------------------------------------------------
-- 函数：获取全局ID
-- ----------------------------------------------------------------------
DELIMITER ;
DROP FUNCTION IF EXISTS exa_get_global_id;
CREATE FUNCTION exa_get_global_id(tableid INT(11), record_count INT(11))
  RETURNS BIGINT(20)
  DETERMINISTIC
BEGIN
  UPDATE exa_table_id_allocate
  SET
    last_id = (@exaid := last_id) + record_count
  WHERE table_id = tableid;
  RETURN @exaid;
END;


-- -------------------------------------------------------------------------------------
-- 2、用户账号、鉴权相关表
-- ------------------------------

-- ----------------------------
-- Table structure for exa_users
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS exa_users;
CREATE TABLE exa_users
(
  user_id			BIGINT(20)		NOT NULL DEFAULT 0	COMMENT '用户ID',

  -- 登录信息
  user_name			VARCHAR(80) UNIQUE NOT NULL DEFAULT '' COMMENT '用户名',
  -- 加密算法：md5(concat(md5(密码明文),salt));
  -- 前端发送一次md5的值到服务器，服务器添加salt值，计算二次md5(32)大写值，与passwd值比较
  password			VARCHAR(64)		NOT NULL DEFAULT '' COMMENT '用户密码',
  -- salt，可用记录生成的时间
  salt				VARCHAR(64)		NOT NULL DEFAULT '' COMMENT '加盐md5算法中的盐',
  user_type			TINYINT(4)		NOT NULL DEFAULT 3  COMMENT '用户类型，1-系统管理员、2-公司内部用户、3-外部用户',
  org_id			INT(11)			NOT NULL DEFAULT 0  COMMENT '组织机构ID',

  -- 用户资料
  real_name			VARCHAR(64)		NOT NULL DEFAULT '' COMMENT '真实姓名',
  email				VARCHAR(100)	NOT NULL DEFAULT '' COMMENT 'Email',
  phone_number		VARCHAR(20)		NOT NULL DEFAULT '' COMMENT '手机号码',
  sex				TINYINT(4)		NOT NULL DEFAULT 1  COMMENT '性别，1-无值、2-男、3-女、4-其它',
  birth				DATETIME		DEFAULT NULL COMMENT '生日',
  id_no				VARCHAR(30)		NOT NULL DEFAULT '' COMMENT '身份证号码',

  open_id			VARCHAR(40)		NOT NULL DEFAULT "" COMMENT '微信小程序的openid',
  woa_openid		VARCHAR(40)		NOT NULL DEFAULT "" COMMENT '微信公众号openid',
  remark			VARCHAR(200)	NOT NULL DEFAULT '' COMMENT '备注',

  -- 记录操作信息
  operator_name		VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '操作人账号',
  delete_flag		TINYINT(4)		NOT NULL DEFAULT 0  COMMENT '记录删除标记，0-正常、1-禁用',
  create_time		DATETIME		NOT NULL DEFAULT NOW() COMMENT '创建时间',
  update_time		DATETIME		DEFAULT NULL ON UPDATE NOW() COMMENT '更新时间',
  PRIMARY KEY (user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT '用户表';

-- password : Abc123_456
insert into exa_users(user_id,user_name,salt,password,user_type,real_name,org_id)
values (1,'admin','2021-08-01 00:00:00','B599A75058B9BAE1D5834354F930272C',1,'管理员',1);

-- A123456
insert into exa_users(user_id,user_name,salt,password,user_type,real_name,org_id)
values (2,'test','2021-01-18 00:00:00','FF9AD3403CA83EEB690F9640D9B6878D',2,'test',1);

-- ----------------------------
-- Table structure for exa_functions
-- 功能表
-- ----------------------------
DROP TABLE IF EXISTS exa_functions;
CREATE TABLE exa_functions
(
  func_id			INT(11)			NOT NULL DEFAULT 0  COMMENT '功能ID',
  func_name			VARCHAR(100)	NOT NULL DEFAULT '' COMMENT '功能名称',
  parent_id			INT(11)			NOT NULL DEFAULT 0  COMMENT '父功能ID',
  level				TINYINT(4)		NOT NULL DEFAULT 0  COMMENT '功能所在层级',
  order_no			INT(11)			NOT NULL DEFAULT 0  COMMENT '显示顺序',
  url				VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '访问接口url',
  dom_key			VARCHAR(80)		NOT NULL DEFAULT '' COMMENT 'dom对象的ID',
  img_tag			VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '节点icon名称',

  remark			VARCHAR(200)	NOT NULL DEFAULT '' COMMENT '备注',

  -- 记录操作信息
  operator_name		VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '操作人账号',
  delete_flag		TINYINT(4)		NOT NULL DEFAULT 0  COMMENT '记录删除标记，0-正常、1-已删除',
  create_time		DATETIME		NOT NULL DEFAULT NOW() COMMENT '创建时间',
  update_time		DATETIME		DEFAULT NULL ON UPDATE NOW() COMMENT '更新时间',
  PRIMARY KEY (func_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='功能表';

insert into exa_functions(func_id,func_name,parent_id,level,order_no,url,img_tag,dom_key)
values(1,'首页',0,1,0,'','','home');
insert into exa_functions(func_id,func_name,parent_id,level,order_no,url,img_tag,dom_key)
values(5,'用户管理',0,1,1,'','','userMan');

insert into exa_functions(func_id,func_name,parent_id,level,order_no,url,img_tag,dom_key)
values(101,'新增用户',5,2,0,'/user/add','','addUser');
insert into exa_functions(func_id,func_name,parent_id,level,order_no,url,img_tag,dom_key)
values(102,'编辑用户',5,2,1,'/user/edit','','editUser');
insert into exa_functions(func_id,func_name,parent_id,level,order_no,url,img_tag,dom_key)
values(103,'分配角色',5,2,2,'/user/assignRoles','','assignRoles');
insert into exa_functions(func_id,func_name,parent_id,level,order_no,url,img_tag,dom_key)
values(104,'分配组织列表',5,2,3,'/user/assignDrIds','','assignDrIds');
insert into exa_functions(func_id,func_name,parent_id,level,order_no,url,img_tag,dom_key)
values(105,'重置密码',5,2,4,'/user/reset','','resetPassword');
insert into exa_functions(func_id,func_name,parent_id,level,order_no,url,img_tag,dom_key)
values(106,'启用禁用',5,2,5,'/user/deleteFlag','','deleteUser');

-- ----------------------------
-- Table structure for exa_roles
-- 角色表
-- ----------------------------
DROP TABLE IF EXISTS exa_roles;
CREATE TABLE exa_roles
(
  role_id			INT(11)			NOT NULL DEFAULT 0  COMMENT '角色ID',
  role_name			VARCHAR(40)		NOT NULL DEFAULT '' COMMENT '角色名称',
  role_type			TINYINT(4)		NOT NULL DEFAULT 0  COMMENT '角色类型，参见系统参数表',
  remark			VARCHAR(100)	NOT NULL DEFAULT '' COMMENT '描述',

  operator_name		VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '操作人账号',
  delete_flag		TINYINT(4)		NOT NULL DEFAULT 0  COMMENT '记录删除标记，0-正常、1-已删除',
  create_time		DATETIME		NOT NULL DEFAULT NOW() COMMENT '创建时间',
  update_time		DATETIME		DEFAULT NULL ON UPDATE NOW() COMMENT '更新时间',
  PRIMARY KEY (role_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='角色表';

insert into exa_roles(role_id,role_name,role_type) values(1,'系统管理员',1);
insert into exa_roles(role_id,role_name,role_type) values(2,'账号管理员',2);
insert into exa_roles(role_id,role_name,role_type) values(3,'运维人员',3);

-- ----------------------------
-- Table structure for exa_role_func_rights
-- 角色和功能权限关系表
-- 角色和功能权限是多对多关系
-- ----------------------------
DROP TABLE IF EXISTS exa_role_func_rights;
CREATE TABLE exa_role_func_rights
(
  role_id			INT(11)			NOT NULL DEFAULT 0  COMMENT '角色ID',
  func_id			INT(11)			NOT NULL DEFAULT 0  COMMENT '功能ID',
  sub_full_flag		TINYINT(4)		NOT NULL DEFAULT 0  COMMENT '是否包含全部子节点权限，0-否，1-是',

  operator_name		VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '操作人账号',
  delete_flag		TINYINT(4)		NOT NULL DEFAULT 0  COMMENT '记录删除标记，保留字段',
  create_time		DATETIME		NOT NULL DEFAULT NOW() COMMENT '创建时间',
  update_time		DATETIME 		DEFAULT NULL ON UPDATE NOW() COMMENT '更新时间',
  PRIMARY KEY (role_id, func_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='角色和功能权限关系表';

-- 系统管理员
insert into exa_role_func_rights(role_id,func_id,sub_full_flag) values(1,1,1);
insert into exa_role_func_rights(role_id,func_id,sub_full_flag) values(1,5,1);

-- 账号管理员
insert into exa_role_func_rights(role_id,func_id,sub_full_flag) values(2,1,1);
insert into exa_role_func_rights(role_id,func_id,sub_full_flag) values(2,5,1);

-- 运维人员
insert into exa_role_func_rights(role_id,func_id,sub_full_flag) values(3,1,1);

-- ----------------------------
-- Table structure for exa_user_roles
-- 用户和角色关系表
-- 用户和角色是多对多关系
-- ----------------------------
DROP TABLE IF EXISTS exa_user_roles;
CREATE TABLE exa_user_roles
(
  user_id			BIGINT(20)		NOT NULL DEFAULT 0  COMMENT '用户ID',
  role_id			INT(11)			NOT NULL DEFAULT 0  COMMENT '角色ID',

  operator_name		VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '操作人账号',
  delete_flag		TINYINT(4)		NOT NULL DEFAULT 0  COMMENT '记录删除标记，保留字段',
  create_time		DATETIME		NOT NULL DEFAULT NOW() COMMENT '创建时间',
  update_time		DATETIME 		DEFAULT NULL ON UPDATE NOW() COMMENT '更新时间',
  PRIMARY KEY (user_id, role_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='用户和角色关系表';

insert into exa_user_roles(user_id,role_id) values(1,1);
insert into exa_user_roles(user_id,role_id) values(2,2);

-- ----------------------------
-- Table structure for exa_dr_fields
-- 需要纳入数据权限管理的相关字段表
-- 为简化处理，要求权限相关字段，在所有表中具有相同的定义和含义
-- ----------------------------
DROP TABLE IF EXISTS exa_dr_fields;
CREATE TABLE exa_dr_fields
(
  field_id			INT(11)			NOT NULL DEFAULT 0  COMMENT '字段ID',
  field_name		VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '字段名称',
  prop_name			VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '属性名称',
  invalid_value		VARCHAR(20)		NOT NULL DEFAULT '' COMMENT '无效值，用于ID字段无权限时的查询条件',
  has_sub			TINYINT(4)		NOT NULL DEFAULT 0  COMMENT '是否有下级对象，0-否，1-是',
  is_user_prop		TINYINT(4)		NOT NULL DEFAULT 0  COMMENT '是否为用户属性字段，0-否，1-是',
  is_id				TINYINT(4)		NOT NULL DEFAULT 0  COMMENT '是否为ID字段，即起源表的主键，0-否，1-是',
  remark			VARCHAR(200)	NOT NULL DEFAULT '' COMMENT '备注',

  -- 记录操作信息
  operator_name		VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '操作人账号',
  delete_flag		TINYINT(4)		NOT NULL DEFAULT 0  COMMENT '记录标记，，0-正常、1-已删除',
  create_time		DATETIME		NOT NULL DEFAULT NOW() COMMENT '创建时间',
  update_time		DATETIME		DEFAULT NULL ON UPDATE NOW() COMMENT '更新时间',
  PRIMARY KEY (field_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='数据权限相关字段表';

insert into exa_dr_fields(field_id,field_name,prop_name,field_datatype,invalid_value,has_sub,is_user_prop,is_id) 
values(1,'org_id','orgId',7,'-1',1,1,1);

-- ----------------------------
-- Table structure for exa_user_drs
-- 用户数据权限表
-- 配置用户对权限相关字段的数据权限类型
-- 默认规则：用户自身具有的该字段属性值，包括字段属性值对应的记录及下级属性值的记录，如组织的下级
-- 如果字段的is_user_prop=0，不能使用默认规则
-- 自定义：由exa_user_custom_drs表指定的该字段的具体列表
-- 全部：全部记录，不限制
-- 表达式，为扩展留下余地，暂时不用，使用支持sql where clause的语法分析器，支持self和ref关键字，如:
-- org_id = self.orgId and (class_id = ref.class_id or device_id = ref.device_id) 
-- 也可以使用JSON格式（或使用base64来编码存储）
-- 使用表达式时，field_id无效，可设置为0
-- 用户对各个权限相关字段，应都有一条记录，如无记录，表示无权限
-- ----------------------------
DROP TABLE IF EXISTS exa_user_drs;
CREATE TABLE exa_user_drs
(
  user_id			BIGINT(20)		NOT NULL DEFAULT 0  COMMENT '用户ID',
  field_id			INT(11)			NOT NULL DEFAULT 0  COMMENT '字段ID',
  field_name		VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '字段名',
  -- 如果字段为用户属性字段，则允许使用默认规则，否则不允许
  dr_type			TINYINT(4)		NOT NULL DEFAULT 1  COMMENT '数据权限类型，1-默认规则、2-自定义、3-全部',
  -- 如果字段不是ID类型，可以使用表达式，目前暂时不用
  expr				VARCHAR(255)	NOT NULL DEFAULT '' COMMENT '表达式',

  -- 记录操作信息
  operator_name		VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '操作人账号',
  delete_flag		TINYINT(4)		NOT NULL DEFAULT 0  COMMENT '记录标记，保留',
  create_time		DATETIME		NOT NULL DEFAULT NOW() COMMENT '创建时间',
  update_time		DATETIME		DEFAULT NULL ON UPDATE NOW() COMMENT '更新时间',
  PRIMARY KEY (user_id,field_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='数据权限规则表';


-- ----------------------------
-- Table structure for exa_user_custom_drs
-- 用户自定义数据权限表
-- 仅针对数据权限类型为自定义的用户数据权限记录
-- 由于不确定有多少个自定义权限字段，因此使用了记录ID作为主键，方便增加字段
-- ----------------------------
DROP TABLE IF EXISTS exa_user_custom_drs;
CREATE TABLE exa_user_custom_drs
(
  user_id			BIGINT(20)		NOT NULL DEFAULT 0  COMMENT '用户ID',
  field_id			INT(11)			NOT NULL DEFAULT 0  COMMENT '字段ID',
  field_value		INT(11)			NOT NULL DEFAULT 0  COMMENT '字段值',

  -- 其它自定义类型的数据权限字段

  -- 记录操作信息
  operator_name		VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '操作人账号',
  delete_flag		TINYINT(4)		NOT NULL DEFAULT 0  COMMENT '记录标记，保留',
  create_time		DATETIME		NOT NULL DEFAULT NOW() COMMENT '创建时间',
  update_time		DATETIME		DEFAULT NULL ON UPDATE NOW() COMMENT '更新时间',
  PRIMARY KEY (user_id,field_id,field_value)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='用户数据权限关系表';


-- -------------------------------------------------------------------------------------
-- 3、系统参数表
-- ------------------------------

-- ----------------------------
-- Table structure for exa_sys_parameters
-- 系统参数表
-- ----------------------------
DROP TABLE IF EXISTS exa_sys_parameters;
CREATE TABLE exa_sys_parameters
(
  class_id			INT(11)			NOT NULL DEFAULT 0  COMMENT '参数类别ID',
  class_key			VARCHAR(60)		NOT NULL DEFAULT '' COMMENT '参数类别key',
  class_name		VARCHAR(60)		NOT NULL DEFAULT '' COMMENT '参数类别名称',
  item_id			INT(11)			NOT NULL DEFAULT 0  COMMENT '参数类别下子项ID',
  item_key			VARCHAR(200)	NOT NULL DEFAULT '' COMMENT '子项key',
  item_value		VARCHAR(200)	NOT NULL DEFAULT '' COMMENT '子项值',
  item_desc			VARCHAR(512)	NOT NULL DEFAULT '' COMMENT '子项描述',

  -- 记录操作信息
  operator_name		VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '操作人账号',
  delete_flag		TINYINT(4)		NOT NULL DEFAULT 0  COMMENT '记录删除标记，0-正常、1-已删除',
  create_time		DATETIME		NOT NULL DEFAULT NOW() COMMENT '创建时间',
  update_time		DATETIME		DEFAULT NULL ON UPDATE NOW() COMMENT '更新时间',
  PRIMARY KEY (class_id, item_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT '系统参数表';

CREATE INDEX exa_sys_parameters_class_key_item_key ON exa_sys_parameters (class_key, item_key);

-- 10001-10099 保留给基础表，即用户、权限相关的类型定义

INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10001, 'user_type', '用户类型', 1, '1', '系统管理员', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10001, 'user_type', '用户类型', 2, '2', '公司内部用户', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10001, 'user_type', '用户类型', 3, '3', '外部用户', '');

INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10002, 'sex', '性别', 1, '1', '无值', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10002, 'sex', '性别', 2, '2', '男', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10002, 'sex', '性别', 3, '3', '女', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10002, 'sex', '性别', 4, '4', '其它', '');

INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10003, 'dr_type', '数据权限类型', 1, '1', '默认规则', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10003, 'dr_type', '数据权限类型', 2, '2', '自定义', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10003, 'dr_type', '数据权限类型', 3, '3', '全部', '');

INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10004, 'role_type', '角色类型', 1, '1', '系统管理员', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10004, 'role_type', '角色类型', 2, '2', '账号管理员', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10004, 'role_type', '角色类型', 3, '3', '运维人员', '');


INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10007, 'delete_flag', '记录删除标记', 0, '0', '正常', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10007, 'delete_flag', '记录删除标记', 1, '1', '已删除', '');

INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10008, 'has_sub', '是否有下级对象', 0, '0', '否', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10008, 'has_sub', '是否有下级对象', 1, '1', '是', '');

INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10009, 'is_user_prop', '是否有下级对象', 0, '0', '否', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10009, 'is_user_prop', '是否有下级对象', 1, '1', '是', '');

INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10010, 'is_id', '是否为ID字段', 0, '0', '否', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10010, 'is_id', '是否为ID字段', 1, '1', '是', '');


INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10100, 'org_type', '机构类型', 1, '1', '本公司', '');

INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10101, 'district_level', '行政级别', 1, '1', '国家级', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10101, 'district_level', '行政级别', 2, '2', '副国级', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10101, 'district_level', '行政级别', 3, '3', '省部级', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10101, 'district_level', '行政级别', 4, '4', '副省级', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10101, 'district_level', '行政级别', 5, '5', '地市级', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10101, 'district_level', '行政级别', 6, '6', '副市级', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10101, 'district_level', '行政级别', 7, '7', '区县级', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10101, 'district_level', '行政级别', 8, '8', '副县级', '');
INSERT INTO exa_sys_parameters(class_id, class_key, class_name, item_id, item_key, item_value, item_desc)
VALUES (10101, 'district_level', '行政级别', 9, '9', '街道乡镇级', '');

-- -------------------------------------------------------------------------------------
-- 4、组织机构表
-- ------------------------------

-- ----------------------------
-- Table structure for exa_orgnizations
-- 组织机构表
-- ----------------------------
DROP TABLE IF EXISTS exa_orgnizations;
CREATE TABLE exa_orgnizations
(
  org_id			INT(11)			NOT NULL DEFAULT 0  COMMENT '组织ID',
  org_code			VARCHAR(20)		NOT NULL DEFAULT '' COMMENT '组织机构编号',
  org_name			VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '组织机构名称',
  org_fullname		VARCHAR(200)	NOT NULL DEFAULT '' COMMENT '组织机构全称',
  org_type			TINYINT(4)		NOT NULL DEFAULT 0  COMMENT '机构类型，1-本公司，其它待扩展',
  org_category		TINYINT(4)		NOT NULL DEFAULT 0  COMMENT '组织机构分类，保留',
  leader			VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '负责人名称',
  contacts			VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '联系人',
  phone_number		VARCHAR(20)		NOT NULL DEFAULT '' COMMENT '联系电话',
  email				VARCHAR(100)	NOT NULL DEFAULT '' COMMENT 'Email',
  address			VARCHAR(200)	NOT NULL DEFAULT '' COMMENT '地址',
  zipcode			VARCHAR(20)		NOT NULL DEFAULT '' COMMENT '邮编',
  district			VARCHAR(40)		NOT NULL DEFAULT '' COMMENT '行政区省、市、区县名称',
  district_level	TINYINT(4)		NOT NULL DEFAULT 5  COMMENT '行政级别,1-国家级、2-副国级、3-省部级、4、副省级、5-市级、6、副市级、7-区县、8-副县级、9-街道乡镇级',

  parent_id			INT(11)			NOT NULL DEFAULT 0  COMMENT '父组织ID',
  lon				DOUBLE(10,6)	DEFAULT NULL		COMMENT '经度',
  lat				DOUBLE(10,6) 	DEFAULT NULL		COMMENT '纬度',
  remark			VARCHAR(200)	NOT NULL DEFAULT '' COMMENT '备注',

  -- 记录操作信息
  operator_name		VARCHAR(80)		NOT NULL DEFAULT '' COMMENT '操作人账号',
  delete_flag		TINYINT(4)		NOT NULL DEFAULT 0 COMMENT '记录删除标记，0-正常、1-已删除',
  create_time		DATETIME		NOT NULL DEFAULT NOW() COMMENT '创建时间',
  update_time		DATETIME		DEFAULT NULL ON UPDATE NOW() COMMENT '更新时间',
  PRIMARY KEY (org_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT '组织机构表';

-- 本公司为1号记录
insert into exa_orgnizations(org_id,org_code,org_name,org_type,district,district_level) 
values(1,'A0001','abc.com',0,'上海市',3);