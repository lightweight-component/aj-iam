CREATE TABLE `access_token` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Access Token',
  `refresh_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Refresh Token',
  `jwt_token` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'JWT Token JSON 格式，以便复用',
  `id_token` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'JWT Token 进行 Base64 后的，以便测试',
  `user_id` int DEFAULT NULL COMMENT '关联的用户 id',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名（冗余的）',
  `client_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接入的客户端ID',
  `expires_date` datetime DEFAULT NULL COMMENT '过期的具体时间',
  `refresh_expires` datetime DEFAULT NULL COMMENT 'Refresh Token 过期的具体时间',
  `grant_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '授权类型，比如：authorization_code',
  `tenant_id` int DEFAULT NULL,
  `scope` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '可被访问的用户的权限范围，比如：basic、super',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AccessToken 令牌信息';

CREATE TABLE `adp_data_service` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '该命令的描述',
  `datasource_id` int NOT NULL DEFAULT '0' COMMENT '数据源 id',
  `table_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '对应的表名',
  `url_root` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'URL 前缀',
  `url_dir` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'URL 目录',
  `json` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置 JSON',
  `key_gen` tinyint DEFAULT '1' COMMENT '主键生成策略',
  `tags` int DEFAULT NULL COMMENT '标签 total',
  `stat` tinyint DEFAULT NULL COMMENT '数据字典：状态',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` int DEFAULT NULL COMMENT '修改人 id',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='表配置';

CREATE TABLE `app` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `pid` int NOT NULL DEFAULT '0' COMMENT '父级 id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简介',
  `client_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户端 id',
  `client_secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户端秘钥',
  `redirect_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户授权完成之后重定向回你的应用',
  `tenant_id` int DEFAULT NULL COMMENT '租户 id，可为 null 即与租户不强关联',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'MISC' COMMENT '应用类型：HTML, APP，API_SERVICE, RPC_SERVICE, MISC',
  `logo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图标',
  `login_page` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录页面的 URL 地址',
  `stat` tinyint DEFAULT '0' COMMENT '数据字典：状态',
  `expires` int DEFAULT NULL COMMENT 'Token 有效期，单位：分钟',
  `refresh_expires` int DEFAULT NULL COMMENT 'RefreshToken 有效期，单位：天',
  `extend` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '扩展 JSON 字段',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` int DEFAULT NULL COMMENT '修改人 id',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应用/客户端';

CREATE TABLE `app_secret_mgr` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '说明',
  `app_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户端 id',
  `app_secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户端秘钥',
  `owner` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '所属于实体的 id，可以是 AppId 或其他实体的 id',
  `stat` tinyint DEFAULT '0' COMMENT '数据字典：状态',
  `extend` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '扩展 JSON 字段',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT (now()) COMMENT '创建日期',
  `updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` int DEFAULT NULL COMMENT '修改人 id',
  `update_date` datetime DEFAULT (now()) ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应用密钥管理器';

CREATE TABLE `ds_common_api` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `pid` int DEFAULT '-1' COMMENT '父节点 id，如果是顶级节点为 -1 或者空',
  `name` varchar(90) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '说明',
  `namespace` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '命名空间，标识',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型 SINGLE | CRUD',
  `table_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '表名',
  `clz_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '实体类引用名称',
  `id_field` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '主键字段名称',
  `sql` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '单条 SQL 命令',
  `info_sql` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '查询详情的 SQL（可选的）',
  `list_sql` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '查询列表的 SQL（可选的）',
  `create_sql` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '创建的 SQL（可选的）',
  `update_sql` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '修改的 SQL（可选的）',
  `delete_sql` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '删除的 SQL（可选的）',
  `del_field` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'stat' COMMENT '删除字段名称',
  `has_is_deleted` tinyint(1) DEFAULT '1' COMMENT '是否有逻辑删除标记',
  `tenant_isolation` tinyint unsigned DEFAULT NULL COMMENT '是否加入租户数据隔离',
  `id_type` tinyint unsigned DEFAULT NULL COMMENT '1=自增；2=雪花；3=UUID',
  `stat` tinyint unsigned DEFAULT '0' COMMENT '数据字典：状态',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT (now()) COMMENT '创建日期',
  `updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` int DEFAULT NULL COMMENT '修改人 id',
  `update_date` datetime NOT NULL DEFAULT (now()) ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通用万能型 API 接口的配置';

CREATE TABLE `ds_namespace` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `namespace` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称 KEY',
  `table_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '表名',
  `list_order_by_date` tinyint DEFAULT '1' COMMENT 'Whether to automatically add sorting by date',
  `tenant_isolation` tinyint DEFAULT '0' COMMENT 'Whether to add tenant data isolation',
  `current_user_only` tinyint DEFAULT '0' COMMENT 'Whether to restrict the query results to include only data belonging to the current user.',
  `filter_deleted` tinyint DEFAULT '1' COMMENT 'Whether to filter deleted data',
  `stat` smallint DEFAULT '0' COMMENT '状态',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT (now()) COMMENT '创建日期',
  `updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` int DEFAULT NULL COMMENT '修改人 id',
  `update_date` datetime DEFAULT (now()) ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Fast CRUD 配置';

CREATE TABLE `ds_widget_config` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '名称、描述',
  `datasource_id` int DEFAULT NULL COMMENT '数据源 id',
  `datasource_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据源 名称',
  `table_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '关联的表',
  `config` json DEFAULT NULL COMMENT 'JSON 配置',
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型',
  `stat` tinyint NOT NULL DEFAULT '0' COMMENT '数据字典：状态',
  `creator` int DEFAULT NULL COMMENT '创建者 id',
  `create_date` datetime NOT NULL DEFAULT (now()) COMMENT '创建日期',
  `update_date` datetime NOT NULL DEFAULT (now()) ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='UI组件配置器';

CREATE TABLE `oauth_scope` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户端名称',
  `content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简介',
  `stat` tinyint DEFAULT NULL COMMENT '数据字典：状态',
  `extend` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '扩展 JSON 字段',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` int DEFAULT NULL COMMENT '修改人 id',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='可被访问的用户权限';

CREATE TABLE `per_api` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `app_id` int NOT NULL COMMENT '應用 id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'API 名称',
  `url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'API 请求相对地址(ANT 风格路径表达式)',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限类别【1=模块，2=方法】',
  `content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `stat` tinyint DEFAULT NULL COMMENT '数据字典：状态',
  `extend` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '扩展 JSON 字段',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` int DEFAULT NULL COMMENT '修改人 id',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='API 定義';

CREATE TABLE `per_app_api_auth` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `app_auth_id` int NOT NULL COMMENT '应用与应用授权的 id',
  `app_id` int NOT NULL COMMENT 'API 接口 id',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应用与 API 的授权';

CREATE TABLE `per_app_auth` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `app_id` int NOT NULL COMMENT '應用 id',
  `access_app_id` int NOT NULL COMMENT '被访问應用 id，提供接口 API 的 App',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应用与应用之间的授权';

CREATE TABLE `per_function` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `parent_id` int NOT NULL COMMENT '父级 id',
  `path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '路径',
  `app_id` int NOT NULL COMMENT '应用 id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '功能编码',
  `url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '页面地址/路由地址',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限类别【1=模块，2=页面，3=元素】',
  `seq` int DEFAULT NULL COMMENT '排序号',
  `content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `stat` tinyint DEFAULT NULL COMMENT '数据字典：状态',
  `extend` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '扩展 JSON 字段',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` int DEFAULT NULL COMMENT '修改人 id',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='功能';

CREATE TABLE `per_module` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '模块权限名称',
  `content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '模块权限备注',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '模块权限编码',
  `stat` tinyint DEFAULT '0' COMMENT '数据字典：状态',
  `extend` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '扩展 JSON 字段',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT (now()) COMMENT '创建日期',
  `updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` int DEFAULT NULL COMMENT '修改人 id',
  `update_date` datetime NOT NULL DEFAULT (now()) ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模块权限';

CREATE TABLE `per_permission` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限名称',
  `content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限备注',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限编码',
  `tenant_id` int NOT NULL COMMENT '租户 id',
  `stat` tinyint DEFAULT (0) COMMENT '数据字典：状态',
  `extend` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '扩展 JSON 字段',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT (now()) COMMENT '创建日期',
  `updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` int DEFAULT NULL COMMENT '修改人 id',
  `update_date` datetime NOT NULL DEFAULT (now()) ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限';

CREATE TABLE `per_role` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `parent_id` int NOT NULL COMMENT '父 id',
  `tenant_id` int DEFAULT NULL COMMENT '租户 id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `is_inherited_parent` tinyint(1) NOT NULL DEFAULT (1) COMMENT '是否继承父级权限',
  `permission_value` bigint NOT NULL DEFAULT (0) COMMENT '权限总值',
  `module_value` bigint NOT NULL DEFAULT '0' COMMENT '模块权限总值',
  `content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色编码',
  `stat` tinyint DEFAULT (0) COMMENT '数据字典：状态',
  `extend` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '扩展 JSON 字段',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` int DEFAULT NULL COMMENT '修改人 id',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色';

CREATE TABLE `per_role_permission` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `app_id` int NOT NULL COMMENT '應用 id',
  `role_id` int NOT NULL COMMENT '角色ID',
  `permission_id` bigint NOT NULL COMMENT '权限ID',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限类型【FUNCTION/API】',
  `is_leaf` tinyint(1) DEFAULT NULL COMMENT '1=叶子权限，非父级权限 0/null=父级权限',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限表';

CREATE TABLE `per_user_role` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `app_id` int DEFAULT NULL COMMENT '應用 id',
  `user_id` int NOT NULL COMMENT '用户 id',
  `role_id` int NOT NULL COMMENT '角色 id',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT (now()) COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE,
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户-角色';

CREATE TABLE `tenant` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '租户编码',
  `login_modes` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '1、账号密码   2、二维码   3、手机验证码   4、微信授权   5、企业微信授权，一个租户类型可以选择多个登录方式，采用逗号隔开的方式',
  `login_page` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录地址',
  `default_role_id` int DEFAULT NULL COMMENT '默认的角色 id',
  `content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简介',
  `stat` tinyint DEFAULT '0' COMMENT '数据字典：状态',
  `extend` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '扩展 JSON 字段',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` int DEFAULT NULL COMMENT '修改人 id',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户';

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `uid` bigint DEFAULT NULL COMMENT '唯一不重复 id，可以是雪花 id',
  `org_id` int DEFAULT NULL COMMENT '部门 ID',
  `tenant_id` int DEFAULT NULL COMMENT '租户 id',
  `login_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户登录 id，统一租户下不可重复',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名称/昵称，可以重复',
  `real_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户真实姓名',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系电话',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `gender` enum('MALE','FEMALE','UNKNOWN') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `job_title` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '岗位',
  `location` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地区',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像，如为 http 开头即 URL 地址，否则为磁盘文件地址',
  `avatar_blob` blob COMMENT '头像二进制文件',
  `bind_state` int DEFAULT NULL COMMENT '绑定第三方登录账号的状态总值，采用 8421 码',
  `verified_state` int(10) unsigned zerofill DEFAULT NULL COMMENT '是否验证了（手机号码、邮箱、实名、银行卡）的状态总值，采用 8421 码',
  `set_state` int DEFAULT NULL COMMENT '是否设置了（密码、邮件、手机、用户名）的状态（8421码）',
  `stat` tinyint DEFAULT '0' COMMENT '数据字典：状态',
  `extend` json DEFAULT NULL COMMENT '扩展 JSON 字段',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` int DEFAULT NULL COMMENT '修改人 id',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE,
  UNIQUE KEY `uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='关于用户的一些基本情况';

CREATE TABLE `user_account` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `user_id` int NOT NULL COMMENT '用户id',
  `login_type` tinyint DEFAULT NULL COMMENT '登录类型（手机号 邮箱 用户名）或第三方应用名称（微信 微博等）',
  `type` enum('PASSWORD','WECHAT_MINI','PHONE','EMAIL') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录类型（New Ver.）',
  `register_type` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '注册类型',
  `register_ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '注册 ip',
  `identifier` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '第三方应用的 User 唯一标识，如微信的 OpenId',
  `identifier2` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '其他 token 的保存，比如小程序的 session key',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '密码凭证（站内的保存密码，站外的不保存或保存 token）',
  `stat` tinyint DEFAULT '0' COMMENT '数据字典：状态',
  `uid` bigint DEFAULT NULL COMMENT '唯一 id，通过 uuid 生成不重复 id',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` int DEFAULT NULL COMMENT '修改人 id',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_password_date` datetime DEFAULT NULL COMMENT '上次更新密码日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户账号';

CREATE TABLE `user_login_log` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `user_id` int NOT NULL DEFAULT '0' COMMENT '用户 id',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户登录名（冗余）',
  `login_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录类型',
  `ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登录 ip',
  `ip_location` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '登录地区',
  `extend` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '扩展 JSON 字段',
  `tenant_id` smallint DEFAULT NULL COMMENT '租户 id。0 = 不设租户',
  `user_agent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '客户端标识',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE,
  KEY `tenantlId` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户登录信息';

CREATE TABLE `user_org` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `pid` int NOT NULL COMMENT '父组织 id',
  `tenant_id` int DEFAULT NULL COMMENT '租户 id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名称',
  `content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路径',
  `stat` tinyint DEFAULT NULL COMMENT '数据字典：状态',
  `extend` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '扩展 JSON 字段',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` int DEFAULT NULL COMMENT '创建人 id',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` int DEFAULT NULL COMMENT '修改人 id',
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE,
  KEY `Index 3` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组织';
