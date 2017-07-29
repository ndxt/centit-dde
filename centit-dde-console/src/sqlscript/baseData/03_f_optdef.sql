truncate table F_OPTDEF;
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000116', '快捷通道', 'QUICKPAS', 'quickpass', null, null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000128', '查询', 'XZGL', 'list', null, null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000129', '查询', 'TZGG', 'list', null, null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000266', '协办部门拟文意见', 'yxxkflow', 'nwSuggestion', '协办部门拟文意见', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000279', '协办收文分工', 'yxxkflow', 'cooperativeFg', '协办收文分工', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000280', '协办收文二次分工', 'yxxkflow', 'cooperativeSecondFg', '协办收文二次分工', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000281', '主办处室二次分工', 'yxxkflow', 'officeSecondFg', '主办处室二次分工', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000282', '负责人审核', 'yxxkflow', 'managerVerify', '负责人审核', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000283', '协办拟文分工', 'yxxkflow', 'cooperativeNwFg', '协办拟文分工', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000284', '协办拟文二次分工', 'yxxkflow', 'cooperativeSecondNwFg', '协办拟文二次分工', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000105', '查询', 'RUNLOG', 'LIST', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000003', '新建/编辑机构', 'UNITMAG', 'EDIT', null, 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000004', '删除/禁用机构', 'UNITMAG', 'DELETE', null, 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000005', '查看机构详情', 'UNITMAG', 'VIEW', '系统管理', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000011', '机构权限', 'UNITMAG', 'editUnitPower', '更改设定机构权限', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000009', '操作定义', 'OPTDEF', 'LIST', null, 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000048', '删除字典', 'DICTSET', 'DELTE', '删除数据字典', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000012', '配置使用偏好', 'DESKSET', 'EDIT', '更具自己的喜好配置使用偏好', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000046', '列举字典', 'DICTSET', 'LIST', '初始页面', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000047', '编辑/新建字典', 'DICTSET', 'EDIT', '系统自动生成1', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000049', '查看字典明细', 'DICTSET', 'VIEW', '查看字典条目明细', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000050', '编辑字典条目', 'DICTSET', 'editDetail', '编辑/新建字典条目', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000051', '删除字典条目', 'DICTSET', 'deleteDetail', '删除字典条目', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000052', '角色授予', 'USERMAG', 'editUserRole', '用户权限管理-编辑用户角色', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000053', '编辑用户机构', 'USERMAG', 'editUserUnit', '用户机构管理-新建/编辑', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000054', '删除用户机构', 'USERMAG', 'deleteUserUnit', '用户机构管理-删除', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000055', '回收角色', 'USERMAG', 'deleteUserRole', '用户权限管理-回收角色', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000056', '删除', 'ROLEMAG', 'delete', '删除角色及其权限', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000077', '查询', 'EMPLOY', 'LIST', '系统自动添加', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000078', '修改/添加', 'EMPLOY', 'EDIT', '系统自动添加', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000079', '删除', 'EMPLOY', 'DELTE', '系统自动添加', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000080', '查看明细', 'EMPLOY', 'VIEW', '系统自动添加', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000091', '查询', 'InnerMsg', 'LIST', '？？系统自动添加', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000092', '修改/添加', 'InnerMsg', 'EDIT', '给比人留言', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000093', '删除', 'InnerMsg', 'DELTE', '？？系统自动添加', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000094', '发公告', 'InnerMsg', 'publicmsg', '给部门、组织发公告', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000057', '设置机构用户', 'UNITMAG', 'editUnitUser', '设置机构用户', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000058', '删除机构用户', 'UNITMAG', 'deleteUnitUser', '删除机构用户', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000059', '编辑系统业务', 'OPTINFO', 'edit', '编辑系统业务', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000060', '删除系统业务', 'OPTINFO', 'delete', '删除系统业务', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000061', '编辑业务操作', 'OPTDEF', 'edit', '编辑业务操作', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000062', '删除业务操作', 'OPTDEF', 'delete', '删除业务操作', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000081', '查询', 'TESTREF2', 'LIST', '系统自动添加', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000068', '上传文件', 'UPLOADFI', 'upload', '上传文件 文件保存在upload目录中', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000069', '删除', 'UPLOADFI', 'DELTE', '删除文件', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000082', '修改/添加', 'TESTREF2', 'EDIT', '？？系统自动添加', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000083', '删除', 'TESTREF2', 'DELTE', '？？系统自动添加', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000084', '查看明细', 'TESTREF2', 'VIEW', '？？系统自动添加', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000025', '编辑/新建', 'ROLEMAG', 'EDIT', '编辑/新建 角色及其权限', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000073', '查询', 'ADDRESS', 'LIST', '查询和导出', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000002', '系统业务定义', 'OPTINFO', 'LIST', null, 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000074', '修改/添加', 'ADDRESS', 'EDIT', '添加通讯录条目', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000075', '删除', 'ADDRESS', 'DELTE', '系统自动添加', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000076', '查看明细', 'ADDRESS', 'VIEW', '系统自动添加', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000095', '检索', 'SEARCH', 'search', '基于Lucene的全文检索', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000090', '修改/添加', 'USERSET', 'edit', '系统自动添加', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000088', '登录', 'LOGINCAS', 'logincas', 'CAS登录过滤，除了匿名角色，其他角色都应该有这个权限', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000014', '编辑/新建', 'USERMAG', 'EDIT', '系统自动生成', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000015', '删除/禁用', 'USERMAG', 'DELTE', '系统自动生成', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000007', '查看', 'ROLEMAG', 'LIST', '查看角色列表', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000063', '查询', 'addBook', 'LIST', '系统自动添加', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000064', '修改/添加', 'addBook', 'EDIT', '系统自动添加', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000016', '查看明细', 'USERMAG', 'VIEW', '查看权限、机构信息', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000065', '删除', 'addBook', 'DELTE', '系统自动添加', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000066', '查看明细', 'addBook', 'VIEW', '系统自动添加', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000070', '下载文件', 'UPLOADFI', 'download', '从upload目录中下载', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000071', '保存', 'UPLOADFI', 'save', '文件BASE64编码后保存在数据库CLOB字段中', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000072', '查看', 'UPLOADFI', 'view', '从数据库中下载文件', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000035', '授予下属部门权限', 'DEPTPOW', 'editDeptPower', '授予下属部门权限', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000040', '删除部门角色', 'DEPTROLE', 'deleteDeptRole', '删除部门角色', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000039', '编辑/新建部门角色', 'DEPTROLE', 'editDeptRole', '编辑/新建部门角色', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000044', '删除用户角色', 'DEPTUR', 'deleteUserRole', '回收用户部门权限', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000045', '编辑用户角色', 'DEPTUR', 'editUserRole', '授予用户部门角色', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000097', '查询', 'ceshixx', 'LIST', '？？系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000098', '修改/添加', 'ceshixx', 'EDIT', '？？系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000099', '删除', 'ceshixx', 'DELTE', '？？系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000100', '查看明细', 'ceshixx', 'VIEW', '？？系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000101', '查询', 'TEST', 'LIST', '？？系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000102', '修改/添加', 'TEST', 'EDIT', '？？系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000103', '删除', 'TEST', 'DELTE', '？？系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000104', '查看明细', 'TEST', 'VIEW', '？？系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000096', '流程定义', 'FLOWDEF', 'edit', '编辑流程定义', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000114', '编辑报销申请单', 'BAOXIAO', 'edit', null, 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000109', '查看明细', 'BAOXIAO', 'viewReimburse', null, 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000110', '查询', 'DBSX', 'LIST', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000115', '查询', 'FLOWINST', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('00000121', '节点查询', 'NODE', 'list', null, null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('00000116', '报销付款', 'BAOXIAO', 'payReimburse', null, 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000132', '办公室批分', 'yxxkflow', 'officeBranch', '办公室批分', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000130', '办件登记', 'yxxkflow', 'applyRegistration', '办件登记', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000131', '办公室收件', 'yxxkflow', 'officeCollection', '办公室收件', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000133', '负责人分工', 'yxxkflow', 'principalDivision', '负责人分工', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000134', '相关协办处室', 'yxxkflow', 'cooperativeRoom', '相关协办处室', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000135', '主任批阅', 'yxxkflow', 'directorReadover', '主任批阅', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000136', '主办承办人受理', 'yxxkflow', 'registrarAccept', '主办承办人受理', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000137', '主办承办人办理', 'yxxkflow', 'thereunderFor', '主办承办人办理', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000138', '拟文', 'yxxkflow', 'syntax', '拟文', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000139', '负责人审核拟文', 'yxxkflow', 'principalAudit', '负责人审核拟文', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000140', '办公室审阅', 'yxxkflow', 'officeReview', '办公室审阅', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000141', '分管主任签发', 'yxxkflow', 'chargedirectorIssued', '分管主任签发', 'T');
commit;
prompt 100 records committed...
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000142', '主任签发', 'yxxkflow', 'directorIssued', '主任签发', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000143', '办公室置发文号', 'yxxkflow', 'homeofficeIssued', '办公室置发文号', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000144', '办公室签章、印发', 'yxxkflow', 'OfficeStamp', '办公室签章、印发', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000145', '归档', 'yxxkflow', 'placeonFile', '归档', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000260', '查询', 'Calendar', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000261', '修改/添加', 'Calendar', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000262', '删除', 'Calendar', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000263', '查看明细', 'Calendar', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000268', '查询', 'PFW', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000269', '修改/添加', 'PFW', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000270', '删除', 'PFW', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000271', '查看明细', 'PFW', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000117', '普通公文', 'PTGW', 'quickpass', null, null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000234', '查询', 'LINK', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000235', '修改/添加', 'LINK', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000236', '删除', 'LINK', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000237', '查看明细', 'LINK', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000246', '查询', 'dj', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000247', '修改/添加', 'dj', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000248', '删除', 'dj', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000249', '查看明细', 'dj', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000256', '查询', 'PUBLISH', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000257', '修改/添加', 'PUBLISH', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000258', '删除', 'PUBLISH', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000259', '查看明细', 'PUBLISH', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000273', '审核', 'VERIFY', 'verify', '审核', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000277', null, 'DICTSET', 'ceshi', '测试', 'F');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000150', '查询', 'CS2', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000151', '修改/添加', 'CS2', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000152', '删除', 'CS2', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000153', '查看明细', 'CS2', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000230', '查询', 'BJJ', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000231', '修改/添加', 'BJJ', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000232', '删除', 'BJJ', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000233', '查看明细', 'BJJ', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000250', '申请', 'QLYX', 'applyRegistration', '申请', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000251', '受理', 'QLYX', 'registrarAccept', '受理', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000252', '审查', 'QLYX', 'directorReadover', '审查', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000253', '审核', 'QLYX', 'principalAudit', '审核', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000254', '批准', 'QLYX', 'officeReview', '批准', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000255', '送达', 'QLYX', 'OfficeStamp', '送达', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000264', '查看', 'BJBL', 'view', null, 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000265', '办件办理', 'BJBL', 'list', null, 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000267', '承办人汇总意见', 'yxxkflow', 'suggestion', '承办人汇总意见', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000272', '领导查看', 'yxxkflow', 'leaderview', null, 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000278', '再次核稿', 'yxxkflow', 'againHg', '再次核稿', 'T');
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000118', '行政许可', 'XZXK', 'quickpass', null, null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000119', '行政审批', 'XZSP', 'quickpass', null, null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000120', '行政确认', 'XZQR', 'quickpass', null, null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000121', '行政处罚', 'XZCF', 'quickpass', null, null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000122', '其他', 'QT', 'quickpass', null, null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000123', '查询', 'yhts', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000124', '修改/添加', 'yhts', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000125', '删除', 'yhts', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000126', '查看明细', 'yhts', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('000127', '列表', 'YHTS', 'list', null, null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000146', '查询', 'DBJ', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000147', '修改/添加', 'DBJ', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000148', '删除', 'DBJ', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000149', '查看明细', 'DBJ', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000154', '查询', 'DGDFW', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000155', '修改/添加', 'DGDFW', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000156', '删除', 'DGDFW', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000157', '查看明细', 'DGDFW', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000158', '查询', 'DYJ', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000159', '修改/添加', 'DYJ', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000160', '删除', 'DYJ', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000161', '查看明细', 'DYJ', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000162', '查询', 'DSW', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000163', '修改/添加', 'DSW', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000164', '删除', 'DSW', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000165', '查看明细', 'DSW', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000166', '查询', 'DFW', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000167', '修改/添加', 'DFW', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000168', '删除', 'DFW', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000169', '查看明细', 'DFW', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000170', '查询', 'DLDQF', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000171', '修改/添加', 'DLDQF', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000172', '删除', 'DLDQF', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000173', '查看明细', 'DLDQF', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000174', '查询', 'DLDYS', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000175', '修改/添加', 'DLDYS', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000176', '删除', 'DLDYS', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000177', '查看明细', 'DLDYS', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000178', '查询', 'QTDB', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000179', '修改/添加', 'QTDB', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000180', '删除', 'QTDB', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000181', '查看明细', 'QTDB', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000182', '查询', 'ZSW', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000183', '修改/添加', 'ZSW', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000184', '删除', 'ZSW', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000185', '查看明细', 'ZSW', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000186', '查询', 'ZFW', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000187', '修改/添加', 'ZFW', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000188', '删除', 'ZFW', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000189', '查看明细', 'ZFW', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000190', '查询', 'ZSCZ', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000191', '修改/添加', 'ZSCZ', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000192', '删除', 'ZSCZ', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000193', '查看明细', 'ZSCZ', 'view', '系统自动添加', null);
commit;
prompt 200 records committed...
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000194', '查询', 'ZFCZ', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000195', '修改/添加', 'ZFCZ', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000196', '删除', 'ZFCZ', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000197', '查看明细', 'ZFCZ', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000198', '查询', 'ZBJY', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000199', '修改/添加', 'ZBJY', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000200', '删除', 'ZBJY', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000201', '查看明细', 'ZBJY', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000202', '查询', 'ZBTA', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000203', '修改/添加', 'ZBTA', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000204', '删除', 'ZBTA', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000205', '查看明细', 'ZBTA', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000206', '查询', 'ZBTZS', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000207', '修改/添加', 'ZBTZS', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000208', '删除', 'ZBTZS', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000209', '查看明细', 'ZBTZS', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000210', '查询', 'YBJBJ', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000211', '修改/添加', 'YBJBJ', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000212', '删除', 'YBJBJ', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000213', '查看明细', 'YBJBJ', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000214', '查询', 'YBJCZ', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000215', '修改/添加', 'YBJCZ', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000216', '删除', 'YBJCZ', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000217', '查看明细', 'YBJCZ', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000218', '查询', 'YBJJY', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000219', '修改/添加', 'YBJJY', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000220', '删除', 'YBJJY', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000221', '查看明细', 'YBJJY', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000222', '查询', 'ZBJ', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000223', '修改/添加', 'ZBJ', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000224', '删除', 'ZBJ', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000225', '查看明细', 'ZBJ', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000226', '查询', 'FlowDef', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000227', '修改/添加', 'FlowDef', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000228', '删除', 'FlowDef', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000229', '查看明细', 'FlowDef', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000238', '查询', 'TEST1', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000239', '修改/添加', 'TEST1', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000240', '删除', 'TEST1', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000241', '查看明细', 'TEST1', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000242', '查询', 'TEST2', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000243', '修改/添加', 'TEST2', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000244', '删除', 'TEST2', 'delete', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000245', '查看明细', 'TEST2', 'view', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000274', '查询', 'qingjia', 'list', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000275', '修改/添加', 'qingjia', 'edit', '系统自动添加', null);
insert into F_OPTDEF (OPTCODE, OPTNAME, OPTID, OPTMETHOD, OPTDESC, ISINWORKFLOW)
values ('10000276', '删除', 'qingjia', 'delete', '系统自动添加', null);
commit;
