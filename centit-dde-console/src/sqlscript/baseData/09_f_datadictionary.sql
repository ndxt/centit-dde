truncate table F_DATADICTIONARY;
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('MattersType', 'XK', null, null, 'T', '行政许可', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('MattersType', 'CJ', null, null, 'T', '行政裁决', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('MattersType', 'ZS', null, null, 'T', '行政征收', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('MattersType', 'QT', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('MattersType', 'SP', null, null, 'T', '非许可审批', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('MattersType', 'CF', null, null, 'T', '行政处罚', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('MattersType', 'QZ', null, null, 'T', '行政强制', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('MattersType', 'GF', null, null, 'T', '行政给付', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('MattersType', 'JL', null, null, 'T', '行政奖励', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('MattersType', 'QR', null, null, 'T', '行政确认', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('MattersType', 'ZY', null, null, 'T', '行政征用', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFNotice', 'interval', null, null, 'T', '3', null, '检测时间间隔，单位为分钟');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('divisionAdvice', '0', null, null, 'T', '退回办公室批分', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('divisionAdvice', '1', null, null, 'T', '同意通过', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('divisionAdvice', '2', null, null, 'T', '准予受理', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('divisionAdvice', '4', null, null, 'T', '补充材料', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('CollectionStat', 'T', null, null, 'T', '接件', null, '接件');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('CollectionStat', 'F', null, null, 'T', '退回', null, '退回');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('zgysidea', 'T', null, null, 'T', '同意通过', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('zgysidea', 'F', null, null, 'T', '退回拟搞人', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('StationType', 'manu', null, null, 'T', '生产', null, 'manufacture');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('SYSPARAM', 'ACTIONRUNLOG', '1', '1', '1', 'T', '1', 'T 为记录，其他不记录运行日志');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'gf_wtjsxy', 'gfzjs', null, null, '委托建设协议', 'I', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'gfzjs', null, null, null, '公费制节水', 'I', '流程内');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'gf_hbht', 'gfzjs', null, null, '户表合同', 'I', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'gf_wtglxy', 'gfzjs', null, null, '委托管理协议', 'I', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'gf_dbht', 'gfzjs', null, null, '地表合同', 'I', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'gf_wtzgxy', 'gfzjs', null, null, '委托整改协议', 'I', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'hbjs', null, null, null, '户表接水', 'I', '流程内');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('framelayout', 'fixed', null, null, 'T', '固定宽度1024', null, '固定宽度1024，导航在左侧');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'hb_dbjs', 'hbjs', null, null, '地表合同', 'I', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'dbgh', null, null, null, '地表过户', 'I', '流程内');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'dbgh_dbht', 'dbgh', null, null, '地表合同', 'I', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'ecgsgz', null, null, null, '二次供水改造', 'I', ' 流程内');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('SYSPARAM', 'CAS', '单点登录', null, 'F', 'CAS单点登录配置项', null, '"是否允许注册" 属性 用于配置是否使用CAS单点登录，');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'ec_wtglxy', 'ecgsgz', null, null, '委托管理协议', 'I', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'ec_hbht', 'ecgsgz', null, null, '户表合同', 'I', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'ec_wtjsxy', 'ecgsgz', null, null, '委托建设协议', 'I', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'ec_wtzgxy', 'ecgsgz', null, null, '委托整改协议', 'I', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'sqbz', null, null, null, '申请补装', 'I', '流程内');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'sq_hbht', 'sqbz', null, null, '户表合同', 'I', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'sfbfz', null, null, null, '私房包费制', 'I', '流程内');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'sf_hbht', 'sfbfz', null, null, '户表合同', 'I', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'fwzl', null, null, null, '房屋租赁', 'I', '流程外');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('contractcode', 'fw_fwcz', 'fwzl', null, null, '房屋出租', 'I', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('SYSPARAM', 'EnableWebUsr', 'noname', '000009', 'T', 'G-webuser', null, '标记T为允许用户注册，数值表示注册后的默认角色代码');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('framelayout', 'left', null, null, 'T', '导航栏在左侧', null, '导航栏在左侧');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('framelayout', 'right', null, null, 'T', '导航栏在右侧', null, '导航栏在右侧');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('RankType', 'IM', '3', null, 'T', '项目经理', 'U', '项目经理');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('framelayout', 'top', null, null, 'T', '菜单导航', null, '用菜单作为导航栏，这时菜单风格无效');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('EreaType', 'Q', null, null, 'T', '区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('EreaType', 'S', null, null, 'T', '市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('UnitType', 'fgwxt', null, null, 'T', '发改委系统', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('UnitType', 'tj', null, null, 'T', '厅局', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('RankType', 'GM', '1', null, 'T', '总经理', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('RankType', 'DM', '2', null, 'T', '部门经理', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('RankType', 'EM', '9', null, 'T', '员工', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('menustyle', 'outlook', null, null, 'T', 'outlook风格', null, 'outlook风格的菜单，最多可以有3层，选用这个菜单时 设置系统业务要注意');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('menustyle', 'tree', null, null, 'T', '树形风格', null, '树形风格的菜单，不限制层次');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('OptType', 'N', null, null, null, '普通业务', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('StationType', 'mngr', null, null, 'T', '管理', null, 'manager');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('pagestyle', 'default', null, null, 'T', '系统默认的先腾蓝色', null, '系统默认的先腾蓝色');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('pagestyle', 'gray', null, null, 'T', '冷酷的灰色', null, '冷酷的灰色');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('boardlayout', '2X2', null, null, 'T', '两横两列', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('SYSPARAM', 'SysName', null, null, 'T', '江苏省发改委行政权力网上公开透明运行系统', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('OptType', 'M', null, null, null, '系统管理', null, '系统管理');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('OptType', 'S', null, null, null, '系统业务', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('OptType', 'F', null, null, null, '流程业务', null, '流程业务');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('StationType', 'kj', null, null, 'T', '会计', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('StationType', 'cn', null, null, 'T', '出纳', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('RankType2', 'GM', null, null, 'T', '总经理', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('OPTTYPE', 'S', null, null, 'T', '系统业务', null, 'S: 系统业务');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('RankType2', 'IM', null, null, 'T', '项目经理', 'U', '项目经理');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('RankType23', 'IM', null, null, 'T', '项目经理', 'U', '项目经理');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('OPTTYPE', 'O', null, null, 'T', '普通业务', null, 'O:普通业务,W: 流程业务');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('OPTTYPE', 'W', null, null, 'T', '流程业务', null, 'W:流程业务');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('OPTTYPE', 'M', null, null, 'T', '管理业务', null, 'M:管理业务');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFVarType', 'N', null, null, 'T', '数据', null, ' N：数据 ');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFVarType', 'S', null, null, 'T', '字符串', null, 'S:字符串 ');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFVarType', 'D', null, null, 'T', '日期', null, 'D:日期');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFRoleType', 'en', null, null, 'T', '引擎', null, 'en：引擎');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFRoleType', 'gw', null, null, 'T', '岗位', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFRoleType', 'xz', null, null, 'T', '行政', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFRoleType', 'bj', null, null, 'T', '办件', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFTeamRole', 'leader', null, null, 'T', '项目负责人', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('StationType', 'nq', null, null, 'T', '内勤', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('StationType', 'sfy', null, null, 'T', '收费员', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFTeamRole', 'jbr', null, null, 'T', '经办人', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFInstType', 'N', null, null, 'T', '正常', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFInstType', 'C', null, null, 'T', '完成', null, '流程实例状态');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFInstType', 'S', null, null, 'T', '挂起', null, '流程实例状态');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFInstType', 'F', null, null, 'T', '强制结束', null, ' 因为流程强制结束而被强制结束');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFInstType', 'R', null, null, 'T', '游离运行', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('UnitType', 'qsydw', null, null, 'T', '企事业单位', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('UnitType', 'xs', null, null, 'T', '县市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('UnitType', 'qt', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('PaperType', 'sfz', null, null, 'T', '身份证', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('PaperType', 'yyzz', null, null, 'T', '营业执照', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('PaperType', 'zczs', null, null, 'T', '注册证书', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('PaperType', 'qt', null, null, 'T', '其他', null, null);
commit;

insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('PaperType', 'hz', null, null, 'T', '护照', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('PaperType', 'hxz', null, null, 'T', '回乡证', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('PaperType', 'jzz', null, null, 'T', '居住证', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('PaperType', 'jgz', null, null, 'T', '军官证', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('PaperType', 'tqhz', null, null, 'T', '特区护照', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('PaperType', 'txz', null, null, 'T', '通行证', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('PaperType', 'tbz', null, null, 'T', '台胞证', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('FormType', '10', null, null, 'T', '只有公文', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfActionType', 'E', null, null, 'T', '终止节点', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('FormType', '11', null, null, 'T', '公文和项目', null, '公文和项目');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('FormType', '00', null, null, 'T', '不能申报', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('SysMail', 'host_user', null, null, 'T', 'accounts@centit.com', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('SysMail', 'host_name', null, null, 'T', 'email.centit.com', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFName', '1000001', '1000001', '1000001', 'T', '报销事项', null, '报销事项');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('SubFormType', 'sqgjzj', null, null, 'T', '申请国家资金', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('SysMail', 'U', null, null, 'T', '未读', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('SysMail', 'R', null, null, 'T', '已读', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('SysMail', 'D', null, null, 'T', '已删除', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('SysMail', 'O', null, null, 'T', '机构公告', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('SysMail', 'P', null, null, 'T', '个人消息', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('SYSPARAM', 'uploadfile', null, null, 'F', 'D:\centit\uploadfile', null, '标记 F为文件，D为数据库， 数值为文件存储路径');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfActionType', 'X', null, null, 'T', '唤醒超时节点', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfManageType', 'S', null, null, 'T', '挂起流程', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('UnitType', 'O', 'DDD', null, 'T', '业务', null, 'operator');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('UnitType', 'L', 'BBB', null, 'T', '后勤', null, 'logistics');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('UnitType', 'A', 'CCCC', null, 'T', '管理', null, 'administrator');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('UnitType', 'R', 'A', null, 'T', '研发', null, 'Rearch & develop');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfActionType', 'S', null, null, 'T', '提交节点', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfActionType', 'C', null, null, 'T', '创建节点', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfActionType', 'W', null, null, 'T', '创建首节点', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfManageType', 'F', null, null, 'T', '强行结束', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFInstType', 'X', null, null, 'T', '超时挂起', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFInstType', 'D', null, null, 'T', '强制提交', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfActionType', 'R', null, null, 'T', '唤醒节点', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfActionType', 'U', null, null, 'T', '更改数据', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFInstType', 'B', null, null, 'T', '已回退', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFTaskState', 'C', null, null, 'T', '已完成', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfActionType', 'A', null, null, 'T', '挂起节点', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFTaskState', 'A', null, null, 'T', '已分配', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFTaskState', 'W', null, null, 'T', ' 已委托给别人', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('SysMail', 'host_password', null, null, 'T', 'yhs@yhs1', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFInstType', 'E', null, null, 'T', '结束', null, '因为流程完成而结束');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFInstType', 'W', null, null, 'T', '等待', null, '等待子流程返回');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFExpireOpt', 'C', null, null, 'N', '完成', null, ' C：完成（仅对节点有效，强制提交,提交失败就挂起）');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFInstType', 'I', null, null, 'T', '失效', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320100', null, null, null, '南京市', null, '测试');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320102', '320100', null, 'T', '玄武区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '111111', null, null, null, '--', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320103', '320100', null, null, '白下区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320104', '320100', null, null, '秦淮区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320105', '320100', null, null, '建邺区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320106', '320100', null, null, '鼓楼区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320107', '320100', null, null, '下关区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320111', '320100', null, null, '浦口区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320113', '320100', null, null, '栖霞区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320114', '320100', null, null, '雨花区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320115', '320100', null, null, '江宁区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320116', '320100', null, null, '六合区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320124', '320100', null, null, '溧水县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320125', '320100', null, null, '高淳县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320200', null, null, null, '无锡市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320202', '320200', null, null, '崇安区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320203', '320200', null, null, '南长区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320204', '320200', null, null, '北塘区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320205', '320200', null, null, '锡山区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320206', '320200', null, null, '惠山区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320211', '320200', null, null, '滨湖区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320281', '320200', null, null, '江阴市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320282', '320200', null, null, '宜兴市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320300', null, null, null, '徐州市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320302', '320300', null, null, '鼓楼区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320303', '320300', null, null, '云龙区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320304', '320300', null, null, '九里区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320305', '320300', null, null, '贾汪区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320311', '320300', null, null, '泉山区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320321', '320300', null, null, '丰县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320322', '320300', null, null, '沛县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320323', '320300', null, null, '铜山县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320324', '320300', null, null, '雎宁县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320381', '320300', null, null, '新沂市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320382', '320300', null, null, '邺州市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320400', null, null, null, '常州市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320402', '320400', null, null, '天宁区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320404', '320400', null, null, '钟楼区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320405', '320400', null, null, '戚墅堰', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320411', '320400', null, null, '新北区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320412', '320400', null, null, '武进区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320481', '320400', null, null, '溧阳区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320482', '320400', null, null, '金坛市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320500', null, null, null, '苏州市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320502', '320500', null, null, '沧浪区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320503', '320500', null, null, '平江区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320504', '320500', null, null, '金阊区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320505', '320500', null, null, '虎丘区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320506', '320500', null, null, '吴中区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320507', '320500', null, null, '相城区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320581', '320500', null, null, '常熟市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320582', '320500', null, null, '张家港', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320583', '320500', null, null, '昆山市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320584', '320500', null, null, '吴江市', null, null);
commit;

insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320585', '320500', null, null, '太仓市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320600', null, null, null, '南通市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320602', '320600', null, null, '崇川区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320611', '320600', null, null, '港闸区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320621', '320600', null, null, '海安县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320623', '320600', null, null, '如东县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320681', '320600', null, null, '启东市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320682', '320600', null, null, '如皋市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320683', '320600', null, null, '通州市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320684', '320600', null, null, '海门市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320700', null, null, null, '连云港', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320703', '320700', null, null, '连云区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320705', '320700', null, null, '新浦区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320706', '320700', null, null, '海州区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320721', '320700', null, null, '赣榆县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320722', '320700', null, null, '东海县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320723', '320700', null, null, '灌云县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320724', '320700', null, null, '灌南县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320800', null, null, null, '淮安市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320802', '320800', null, null, '清河区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320803', '320800', null, null, '楚州区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320804', '320800', null, null, '淮阴区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320811', '320800', null, null, '涟水县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320826', '320800', null, null, '洪泽县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320829', '320800', null, null, '盱眙县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320830', '320800', null, null, '清河区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320831', '320800', null, null, '金湖县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320900', null, null, null, '盐城市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320902', '320900', null, null, '亭湖区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320903', '320900', null, null, '盐都区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320921', '320900', null, null, '响水县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320922', '320900', null, null, '亭湖区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320923', '320900', null, null, '阜宁县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320924', '320900', null, null, '射阳县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320925', '320900', null, null, '建湖县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320981', '320900', null, null, '东台市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '320982', '320900', null, null, '大丰市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321000', '321000', null, null, '扬州市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321002', '321000', null, null, '广陵区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321003', '321000', null, null, '邗江区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321011', '321000', null, null, '郊（维扬）区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321023', '321000', null, null, '宝应县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321081', '321000', null, null, '仪征市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321084', '321000', null, null, '高邮市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321088', '321000', null, null, '江都市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321100', null, null, null, '镇江市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321102', '321100', null, null, '京口区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321111', '321100', null, null, '润州区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321112', '321100', null, null, '丹徒区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321181', '321100', null, null, '丹阳市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321182', '321100', null, null, '扬中市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321183', '321100', null, null, '句容市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321200', null, null, null, '泰州市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321202', '321200', null, null, '海陵区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321203', '321200', null, null, '高港区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321281', '321200', null, null, '兴化市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321282', '321200', null, null, '靖江市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321283', '321200', null, null, '泰兴市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321284', '321200', null, null, '姜堰市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321300', null, null, null, '宿迁市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321302', '321300', null, null, '宿城区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321311', '321300', null, null, '宿豫区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321322', '321300', null, null, '沭阳县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321323', '321300', null, null, '泗阳县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('jsdq', '321324', '321300', null, null, '泗洪县', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('TrueOrFalse', 'T', null, null, 'T', '是', null, '是');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('TrueOrFalse', 'F', null, null, 'T', '否', null, '否');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('SysMail', 'admin_email', null, null, 'T', 'no-reply@centit.com', null, '系统以通知形式发送邮件的发送方');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('link', '1', null, null, 'T', '申请', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('link', '2', null, null, 'T', '受理', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('link', '3', null, null, 'T', '审核', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('CollectionStat', 'P', null, null, 'T', '暂缓接收', 'S', '暂缓接收');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('link', '4', null, null, 'T', '其他', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFNotice', 'cron', null, null, 'T', '1 8-17 * * ?', null, '从8点到18点，每隔两分钟运行一次');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_ydly', '01', null, null, 'T', '电网供电', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_ydly', '02', null, null, 'T', '已有自备电站', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_ydly', '03', null, null, 'T', '新建电站', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_yqly', '01', null, null, 'T', '开发区集中供源', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_yqly', '02', null, null, 'T', '自有锅炉供气', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_yqly', '03', null, null, 'T', '新建锅炉', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_ysyq', '01', null, null, 'T', '国家审批', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_ysyq', '02', null, null, 'T', '省审批', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_ysyq', '03', null, null, 'T', '市审批', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_ysyq', '04', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_zydzqk', '01', null, null, 'T', '需淘汰', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_zjcnh', '01', null, null, 'T', '省级及以上银行', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_zjcnh', '02', null, null, 'T', '市级银行', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_zjcnh', '03', null, null, 'T', '县级银行', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_zjcnh', '04', null, null, 'T', '非银行金融机构', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_zssm', '01', null, null, 'T', '有', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_zssm', '02', null, null, 'T', '无', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('wztzzsd', '1', null, null, 'T', '|-港澳台', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('wztzzsd', '10', null, null, 'T', '|-北美其它地区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('wztzzsd', '11', null, null, 'T', '|-其它地区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('wztzzsd', '2', null, null, 'T', '|-日本', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('wztzzsd', '3', null, null, 'T', '|-韩国', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('wztzzsd', '4', null, null, 'T', '|-亚洲其它地区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('wztzzsd', '5', null, null, 'T', '|-德国', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('wztzzsd', '6', null, null, 'T', '|-英国', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('wztzzsd', '7', null, null, 'T', '|-法国', null, null);
commit;

insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('wztzzsd', '8', null, null, 'T', '|-欧洲其它地区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('wztzzsd', '9', null, null, 'T', '|-美国', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmfrxz', '1', null, null, 'T', '|-行政（省级机关）', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmfrxz', '10', null, null, 'T', '|-企业（外商投资-合资）', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmfrxz', '11', null, null, 'T', '|-企业（外商投资-合作）', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmfrxz', '12', null, null, 'T', '|-企业（外商投资-独资）', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmfrxz', '2', null, null, 'T', '|-行政（市级机关）', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmfrxz', '3', null, null, 'T', '|-行政（县级机关）', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmfrxz', '4', null, null, 'T', '|-事业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmfrxz', '5', null, null, 'T', '|-企业（国有）', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmfrxz', '6', null, null, 'T', '|-企业（集体）', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_zydzqk', '02', null, null, 'T', '暂不淘汰', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('industry', '01', null, null, 'T', '|-农林牧渔业水利', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('industry', '02', null, null, 'T', '|-工业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('industry', '03', null, null, 'T', '|-高技术', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('industry', '04', null, null, 'T', '|-能源', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('industry', '05', null, null, 'T', '|-交通', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('industry', '06', null, null, 'T', '|-城建', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('industry', '07', null, null, 'T', '|-资源节约和环境保护', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('industry', '08', null, null, 'T', '|-社会事业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('industry', '09', null, null, 'T', '|-公共管理（党政机关、公检法司、社会团体等的设施建设）', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('industry', '10', null, null, 'T', '|-房地产', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('industry', '11', null, null, 'T', '|-经贸流通', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('industry', '12', null, null, 'T', '|-服务业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('industry', '13', null, null, 'T', '|-其它', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '1', null, null, 'T', '|-央企', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '10', null, null, 'T', '|-湖南', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '11', null, null, 'T', '|-湖北', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '12', null, null, 'T', '|-广东', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '13', null, null, 'T', '|-广西', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '14', null, null, 'T', '|-黑龙江', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '15', null, null, 'T', '|-吉林', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '16', null, null, 'T', '|-辽宁', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '17', null, null, 'T', '|-浙江', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '18', null, null, 'T', '|-安徽', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '19', null, null, 'T', '|-江苏', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '2', null, null, 'T', '|-北京', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '20', null, null, 'T', '|-福建', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '21', null, null, 'T', '|-甘肃', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '22', null, null, 'T', '|-江西', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '23', null, null, 'T', '|-云南', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '24', null, null, 'T', '|-贵州', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '25', null, null, 'T', '|-四川', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '26', null, null, 'T', '|-青海', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '27', null, null, 'T', '|-陕西', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '28', null, null, 'T', '|-宁夏', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '29', null, null, 'T', '|-西藏', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '3', null, null, 'T', '|-天津', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '30', null, null, 'T', '|-海南', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '31', null, null, 'T', '|-内蒙古', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '32', null, null, 'T', '|-新疆', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '4', null, null, 'T', '|-上海', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '5', null, null, 'T', '|-重庆', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '6', null, null, 'T', '|-山东', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '7', null, null, 'T', '|-山西', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '8', null, null, 'T', '|-河南', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('nzxmtzzlx', '9', null, null, 'T', '|-河北', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sbStateType', '31', null, null, 'T', '退回修改完成', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sbStateType', '41', null, null, 'T', '补充材料完毕', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sbStateType', '5', null, null, 'T', '同意受理办理中', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sbStateType', '6', null, null, 'T', '不予受理', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sbStateType', '7', null, null, 'T', '暂缓', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sbStateType', '8', null, null, 'T', '结束', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sbStateType', '9', null, null, 'T', '受理中', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sfzdxm', '1', null, null, 'T', '是', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sfzdxm', '2', null, null, 'T', '否', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sfzdxm', '3', null, null, 'T', '市县重点项目', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_aqpj', '01', null, null, 'T', '有', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_aqpj', '02', null, null, 'T', '无', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_aqyq', '01', null, null, 'T', '国家控制', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('UserType', 'wnyh', null, null, 'T', '委内用户', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('UserType', 'qyyh', null, null, 'T', '企业用户', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('UserType', 'gly', null, null, 'T', '管理员用户', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sbStateType', '1', null, null, 'T', '未申报', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sbStateType', '2', null, null, 'T', '已申报', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sbStateType', '3', null, null, 'T', '退回', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sbStateType', '4', null, null, 'T', '材料补充', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylbType', 'GY', null, null, 'T', '工业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylbType', 'GKJ', null, null, 'T', '高科技', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylbType', 'GY_CKY', 'GY', null, 'T', 'cky', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylbType', 'GY_QG', 'GY', null, 'T', 'qg', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFExpireOpt', 'N', null, null, 'T', '通知', null, 'N：通知');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFExpireOpt', 'O', null, null, 'T', '不处理', null, 'O:不处理');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('Update_Type', '3', null, null, 'T', '废止', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFExpireOpt', 'E', null, null, 'T', '终止', null, 'E：终止');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFExpireOpt', 'X', null, null, 'T', '挂起', null, 'X：挂起');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('Update_Type', '1', null, null, 'T', '新增', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('Update_Type', '2', null, null, 'T', '变更', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('QL_DepState', '1', null, null, 'T', '法定', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('QL_DepState', '2', null, null, 'T', '授权', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('QL_DepState', '3', null, null, 'T', '受委托', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('QL_State', 'A', null, null, 'T', '在用（正常状态）', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('QL_State', 'T', null, null, 'T', '挂起', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('QL_State', 'X', null, null, 'T', '废止', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('Promise_Type', '0', null, null, 'T', '天', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('Promise_Type', '1', null, null, 'T', '工作日', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('Promise_Type', '2', null, null, 'T', '月', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('Promise_Type', '3', null, null, 'T', '年', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('Anticipate_Type', '0', null, null, 'T', '天', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('Anticipate_Type', '1', null, null, 'T', '工作日', null, null);
commit;

insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('Anticipate_Type', '2', null, null, 'T', '月', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('Anticipate_Type', '3', null, null, 'T', '年', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('Charge_Flag', '1', null, null, 'T', '是', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('Charge_Flag', '0', null, null, 'T', '否', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('StationType', 'xbr', null, null, 'T', '协办处室负责人', null, '协办处室负责人');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('StationType', 'bgszr', null, null, 'T', '办公室主任', null, '办公室主任');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFTeamRole', 'nwckld', null, null, 'T', '拟文查看领导', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfManageType', 'B', null, null, 'T', '强行回退', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfManageType', 'C', null, null, 'T', '强行提交', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfManageType', 'W', null, null, 'T', '唤醒流程', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfManageType', 'A', null, null, 'T', '分配任务', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfManageType', 'D', null, null, 'T', '删除任务', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfManageType', 'P', null, null, 'T', '禁用任务', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfManageType', 'E', null, null, 'T', '设置任务期限', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfManageType', 'R', null, null, 'T', '重置节点', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfManageType', 'Q', null, null, 'T', '创建游离节点', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfManageType', 'I', null, null, 'T', '失效流程', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfManageType', 'i', null, null, 'T', '失效游离节点', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfManageType', 'X', null, null, 'T', '超时唤醒', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WfManageType', 'q', null, null, 'T', '游离节点', 'S', null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_aqyq', '02', null, null, 'T', '省控', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_aqyq', '03', null, null, 'T', '市控', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_aqyq', '04', null, null, 'T', '一般要求', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_aqyq', '05', null, null, 'T', '无要求', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cpbz', '01', null, null, 'T', '国际', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cpbz', '02', null, null, 'T', '国家', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cpbz', '03', null, null, 'T', '企业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cpbz', '04', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cysx', '01', null, null, 'T', '电子信息', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cysx', '02', null, null, 'T', '装备制造', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cysx', '03', null, null, 'T', '石油化工', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cysx', '04', null, null, 'T', '新能源', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cysx', '05', null, null, 'T', '新医药', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cysx', '06', null, null, 'T', '新材料', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cysx', '07', null, null, 'T', '生物', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cysx', '08', null, null, 'T', '纺织', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cysx', '09', null, null, 'T', '轻工', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cysx', '10', null, null, 'T', '冶金', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cysx', '11', null, null, 'T', '建材', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cysx', '12', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cyzc', '01', null, null, 'T', '鼓励', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cyzc', '02', null, null, 'T', '允许', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cyzc', '03', null, null, 'T', '限制', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_cyzc', '04', null, null, 'T', '淘汰', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_dwxz', '01', null, null, 'T', '中央企业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_dwxz', '02', null, null, 'T', '国有事业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_dwxz', '03', null, null, 'T', '地方国有', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_dwxz', '04', null, null, 'T', '股份制企业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_dwxz', '05', null, null, 'T', '中外合资', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_dwxz', '06', null, null, 'T', '外商独资', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_dwxz', '07', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_ghjsxzpf', '01', null, null, 'T', '省级', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_ghjsxzpf', '02', null, null, 'T', '市级', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_ghjsxzpf', '03', null, null, 'T', '县级', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_ghjsxzpf', '04', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_gnthypm', '01', null, null, 'T', '前三名', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_gnthypm', '02', null, null, 'T', '三到十名', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_gnthypm', '03', null, null, 'T', '十到二十名', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_gnthypm', '04', null, null, 'T', '二十名之后', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_gtbmyj', '01', null, null, 'T', '国家级预审', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_gtbmyj', '02', null, null, 'T', '省级预审', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_gtbmyj', '03', null, null, 'T', '招拍挂合同', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_gtbmyj', '04', null, null, 'T', '兼有', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_gtbmyj', '05', null, null, 'T', '市级及以下', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_hppf', '01', null, null, 'T', '国家环保局', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_hppf', '02', null, null, 'T', '省环保局', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_hppf', '03', null, null, 'T', '市环保局', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_hppf', '04', null, null, 'T', '县环保局', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_hppf', '05', null, null, 'T', '无批复', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_hxzbly', '01', null, null, 'T', '国外', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_hxzbly', '02', null, null, 'T', '国内', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_hxzbly', '03', null, null, 'T', '自研', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_hxzbly', '04', null, null, 'T', '兼有', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_hyzr', '01', null, null, 'T', '有限制', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_hyzr', '02', null, null, 'T', '无限制', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jnpj', '01', null, null, 'T', '有', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jnpj', '02', null, null, 'T', '无', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jscgsp', '01', null, null, 'T', '国际先进', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jscgsp', '02', null, null, 'T', '国内先进', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jscgsp', '03', null, null, 'T', '省内先进', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jscgsp', '04', null, null, 'T', '市级先进', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jscgsp', '05', null, null, 'T', '限制', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jscgsp', '06', null, null, 'T', '淘汰', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jscsd', '01', null, null, 'T', '已规模化生产', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jscsd', '02', null, null, 'T', '已完成中试生产', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jscsd', '03', null, null, 'T', '完成实验室小试', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jscsd', '04', null, null, 'T', '原理性实验', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jscsd', '05', null, null, 'T', '没有任何实验', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jscsd', '06', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jsdd', '01', null, null, 'T', '省级及以上园区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jsdd', '02', null, null, 'T', '产业集聚区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jsdd', '03', null, null, 'T', '新城区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jsdd', '04', null, null, 'T', '老城区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jsdd', '05', null, null, 'T', '农田', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jslb', '01', null, null, 'T', '新建', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jslb', '02', null, null, 'T', '增资', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jslb', '03', null, null, 'T', '并购', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jslb', '04', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jsly', '01', null, null, 'T', '自主发明专利', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jsly', '02', null, null, 'T', '自主实用新型专利', null, null);
commit;

insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jsly', '03', null, null, 'T', '省级以上技术成果鉴定', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jsly', '04', null, null, 'T', '技术诀窍', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jsly', '05', null, null, 'T', '购买专利', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jsly', '06', null, null, 'T', '转让国外技术', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_jsly', '07', null, null, 'T', '转让国内技术', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_qyxsgm', '01', null, null, 'T', '五百万以下', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_qyxsgm', '02', null, null, 'T', '五百万到五千万', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_qyxsgm', '03', null, null, 'T', '五千万到一亿', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_qyxsgm', '04', null, null, 'T', '一到五亿', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_qyxsgm', '05', null, null, 'T', '五亿以上', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_qyxsgm', '06', null, null, 'T', '五到十亿', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_qyxsgm', '07', null, null, 'T', '十到二十亿', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_qyxsgm', '08', null, null, 'T', '二十到五十亿', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_qyxsgm', '09', null, null, 'T', '五十到一百亿', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_qyxsgm', '10', null, null, 'T', '一百亿以上', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_sczb', '01', null, null, 'T', '全部国产', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_sczb', '02', null, null, 'T', '全部进口', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_sczb', '03', null, null, 'T', '关键设备国产', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_sczb', '04', null, null, 'T', '关键设备进口', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_spyq', '01', null, null, 'T', '国家审批', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_spyq', '02', null, null, 'T', '省级核准', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_spyq', '03', null, null, 'T', '省级备案', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_spyq', '04', null, null, 'T', '市级核备', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_spyq', '05', null, null, 'T', '县级核备', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_spyq', '06', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_xmbgbzdw', '01', null, null, 'T', '甲级', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_xmbgbzdw', '02', null, null, 'T', '乙级', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_xmbgbzdw', '03', null, null, 'T', '丙级', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_xmbgbzdw', '04', null, null, 'T', '无资质', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_xmbgbzdw', '05', null, null, 'T', '项目法人自编', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_xmbgbzdw', '06', null, null, 'T', '主持部门代编', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_xmbgbzdw', '07', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_xmlb', '01', null, null, 'T', '产业振兴', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_xmlb', '02', null, null, 'T', '技改', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_xmlb', '03', null, null, 'T', '创新能力', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_xmlb', '04', null, null, 'T', '电子信息', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_xmlb', '05', null, null, 'T', '中小企业专项', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_xmlb', '06', null, null, 'T', '信息化', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_xmlb', '07', null, null, 'T', '高技术服务业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sqgjzj_xmlb', '08', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmfrxz', '7', null, null, 'T', '|-企业（民营）', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmfrxz', '8', null, null, 'T', '|-企业（股份制）', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmsbjdlb', '1', null, null, 'T', '|-审批（项目建议书）', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmsbjdlb', '2', null, null, 'T', '|-审批(可行性研究报告)', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmsbjdlb', '3', null, null, 'T', '|-审批（初步设计）', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmsbjdlb', '4', null, null, 'T', '|-核准', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmsbjdlb', '5', null, null, 'T', '|-备案', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmxz', '1', null, null, 'T', '|-新建', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmxz', '2', null, null, 'T', '|-扩建', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmxz', '3', null, null, 'T', '|-改造', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmxz', '4', null, null, 'T', '|-并购', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmxz', '5', null, null, 'T', '|-融资上市', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('xmxz', '6', null, null, 'T', '|-其它类型项目', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('SubFormType', 'mr', null, null, 'T', '默认', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '01', null, null, 'T', '|-农林牧渔业水利', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0101', '01', null, 'T', '|-农林', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0102', '01', null, 'T', '|-牧渔', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0103', '01', null, 'T', '|-水利', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '02', null, null, 'T', '|-工业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0201', '02', null, 'T', '|-采矿业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0202', '02', null, 'T', '|-轻工', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0203', '02', null, 'T', '|-纺织', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0204', '02', null, 'T', '|-石化', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0205', '02', null, 'T', '|-医药', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0206', '02', null, 'T', '|-建材', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0207', '02', null, 'T', '|-钢铁', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0208', '02', null, 'T', '|-有色金属', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0209', '02', null, 'T', '|-装备制造', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0210', '02', null, 'T', '|-汽车', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0211', '02', null, 'T', '|-船舶', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0212', '02', null, 'T', '|-其他制造业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '03', null, null, 'T', '|-高技术', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0301', '03', null, 'T', '|-新医药和生物技术', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0302', '03', null, 'T', '|-新材料', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0303', '03', null, 'T', '|-电子信息', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0304', '03', null, 'T', '|-先进制造', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0305', '03', null, 'T', '|-航空航天', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0306', '03', null, 'T', '|-海洋技术', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0307', '03', null, 'T', '|-创新能力', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0308', '03', null, 'T', '|-其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '04', null, null, 'T', '|-能源', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0401', '04', null, 'T', '|-电站', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0402', '04', null, 'T', '|-电网', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0403', '04', null, 'T', '|-石油天然气', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0404', '04', null, 'T', '|-煤炭', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0405', '04', null, 'T', '|-新能源', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0406', '04', null, 'T', '|-能源装备', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0407', '04', null, 'T', '|-其他能源项目', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '05', null, null, 'T', '|-交通', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0501', '05', null, 'T', '|-铁路', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0502', '05', null, 'T', '|-公路', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0503', '05', null, 'T', '|-机场', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0504', '05', null, 'T', '|-港口', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0505', '05', null, 'T', '|-航道', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('SubFormType', 'hzba', null, null, 'T', '核准备案', null, '核准备案项目审查信息表');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sbfl', '3', null, null, 'T', '|-省政府', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sbfl', '4', null, null, 'T', '|-省委', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sbfl', '5', null, null, 'T', '|-部委', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sbfl', '6', null, null, 'T', '|-其它', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sbfl', '1', null, null, 'T', '|-地市', null, null);
commit;

insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('sbfl', '2', null, null, 'T', '|-省直', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFNotice', 'type', null, null, 'T', 'message', null, 'message 或者 email');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFNotice', 'timelimit', null, null, 'T', '5', null, '数值单位为天，或者时间表达式，比如：5D4H');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFNotice', 'times', null, null, 'F', '3', null, '通知次数，不得大于5');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('codetype', 'sfz', null, null, 'T', '身份证', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('codetype', 'hz', null, null, 'T', '护照', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('RankType', 'KZ', '10', null, 'T', '科长', null, '科长');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('RankType', 'ZR', '10', null, 'T', '主任', null, '主任');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('RankType', 'KY', '10', null, 'T', '办公室科员', null, '办公室科员');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('RankType', 'CZ', '10', null, 'T', '处长', null, '处长');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('StationType', 'fzr', null, null, 'T', '主办处室负责人', null, '主办处室负责人');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('StationType', 'sjr', null, null, 'T', '办公室收件人', null, '办公室收件人');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('StationType', 'pf', null, null, 'T', '办公室批分人', null, '办公室批分人');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('StationType', 'cbr', null, null, 'T', '主办承办人', null, '主办承办人');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('StationType', 'syr', null, null, 'T', '办公室审阅人', null, '办公室审阅人');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('StationType', 'sr', null, null, 'T', '办公室人员', null, '办公室人员');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('StationType', 'fgzr', null, null, 'T', '分管主任', null, '分管主任');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('StationType', 'zr', null, null, 'T', '主任', null, '主任');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('bjsqfs', '0', null, null, 'T', '当面', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('bjsqfs', '1', null, null, 'T', '网上', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('bjsqfs', '2', null, null, 'T', '电话', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('bjsqfs', '3', null, null, 'T', '传真', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('bjsqfs', '4', null, null, 'T', '电报', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('bjsqfs', '5', null, null, 'T', '信函', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('bjsqfs', '6', null, null, 'T', '电子邮件', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('bjsqfs', '7', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFTeamRole', 'zbfzr', null, null, 'T', '主任处室负责人', null, '主任处室负责人');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_wzlx', '01', null, null, 'T', '港资', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFTeamRole', 'xbfzr', null, null, 'T', '协办处室负责人', null, '协办处室负责人');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_wzlx', '02', null, null, 'T', '台资', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_wzlx', '03', null, null, 'T', '日资', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_wzlx', '04', null, null, 'T', '韩资', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_wzlx', '05', null, null, 'T', '美资', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_wzlx', '06', null, null, 'T', '欧盟', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_wzlx', '07', null, null, 'T', '英资', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_wzlx', '08', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0506', '05', null, 'T', '|-地铁轻轨BRT等城市快速交通系统', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0507', '05', null, 'T', '|-其他交通系统', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '06', null, null, 'T', '|-城建', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0601', '06', null, 'T', '|-供水', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0602', '06', null, 'T', '|-污水处理', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0603', '06', null, 'T', '|-垃圾处理', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0604', '06', null, 'T', '|-医疗废物和危险废弃物处置', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0605', '06', null, 'T', '|-燃气', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0606', '06', null, 'T', '|-市政道路桥梁公交站场', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0607', '06', null, 'T', '|-其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '07', null, null, 'T', '|-资源节约和环境保护', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0701', '07', null, 'T', '|-河流湖泊清淤，生态修复等水污染处理', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0702', '07', null, 'T', '|-微咸水、劣质水、海水的开发利用', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0703', '07', null, 'T', '|-自然保护区动物保护区等保护区建设', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0704', '07', null, 'T', '|-节能工程', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0705', '07', null, 'T', '|-再生资源、循环经济', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0706', '07', null, 'T', '|-资源利用', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0707', '07', null, 'T', '|-应对气候变化项目', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0708', '07', null, 'T', '|-其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '08', null, null, 'T', '|-社会事业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0801', '08', null, 'T', '|-教育', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0802', '08', null, 'T', '|-卫生', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0803', '08', null, 'T', '|-文化', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0804', '08', null, 'T', '|-体育', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0805', '08', null, 'T', '|-旅游娱乐', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0806', '08', null, 'T', '|-社会福利', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0807', '08', null, 'T', '|-广播电影电视', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0808', '08', null, 'T', '|-新闻出版音像', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '0809', '08', null, 'T', '|-其他社会事业项目', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '09', null, null, 'T', '|-公共管理（党政机关、公检法司、社会团体等的设施建设）', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '10', null, null, 'T', '|-房地产', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1001', '10', null, 'T', '|-普通商品房', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1002', '10', null, 'T', '|-高档商品房', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1003', '10', null, 'T', '|-经济适用房、廉租房', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1004', '10', null, 'T', '|-拆迁安置房等政策性住房', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1005', '10', null, 'T', '|-商业地产（百货商店、购物中心、超市、专业市场等）', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1006', '10', null, 'T', '|-工业地产（工业厂房租赁园区、总部基地等产业园区）', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1007', '10', null, 'T', '|-其他房地产业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '11', null, null, 'T', '|-经贸流通', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1101', '11', null, 'T', '|-仓储设备', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1102', '11', null, 'T', '|-物流配送', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1103', '11', null, 'T', '|-专业市场', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1104', '11', null, 'T', '|-商业网点', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1105', '11', null, 'T', '|-物流园区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1106', '11', null, 'T', '|-其他经贸流通项目', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '12', null, null, 'T', '|-服务业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1201', '12', null, 'T', '|-连锁配送', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1202', '12', null, 'T', '|-产品市场', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1203', '12', null, 'T', '|-科技研发', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1204', '12', null, 'T', '|-软件和服务外包', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1205', '12', null, 'T', '|-信息服务', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1206', '12', null, 'T', '|-创意设计', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1207', '12', null, 'T', '|-文化产业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1208', '12', null, 'T', '|-金融', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1209', '12', null, 'T', '|-商务会展', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1210', '12', null, 'T', '|-旅游', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1211', '12', null, 'T', '|-研发中心', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '1212', '12', null, 'T', '|-其他服务业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hylb', '13', null, null, 'T', '|-其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_aqpj', '01', null, null, 'T', '有', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_aqpj', '02', null, null, 'T', '无', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_aqyq', '01', null, null, 'T', '国家控制', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_aqyq', '02', null, null, 'T', '省控', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_aqyq', '03', null, null, 'T', '市控', null, null);
commit;

insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_aqyq', '04', null, null, 'T', '一般要求', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_aqyq', '05', null, null, 'T', '无要求', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cpbz', '01', null, null, 'T', '国际', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cpbz', '02', null, null, 'T', '国家', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cpbz', '03', null, null, 'T', '企标', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cpbz', '04', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cysx', '01', null, null, 'T', '电子信息', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cysx', '02', null, null, 'T', '装备制造', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cysx', '03', null, null, 'T', '石油化工', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cysx', '04', null, null, 'T', '新能源', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cysx', '05', null, null, 'T', '新医药', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cysx', '06', null, null, 'T', '新材料', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cysx', '07', null, null, 'T', '生物', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cysx', '08', null, null, 'T', '纺织', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cysx', '09', null, null, 'T', '轻工', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cysx', '10', null, null, 'T', '冶金', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cysx', '11', null, null, 'T', '建材', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cysx', '12', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cyzc', '01', null, null, 'T', '鼓励', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cyzc', '02', null, null, 'T', '允许', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cyzc', '03', null, null, 'T', '限制', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_cyzc', '04', null, null, 'T', '淘汰', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_frdbbj', '01', null, null, 'T', '工业生产', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_frdbbj', '02', null, null, 'T', '商业经营', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_frdbbj', '03', null, null, 'T', '金融投资', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_frdbbj', '04', null, null, 'T', '科技开发', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_frdbbj', '05', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_ghjsxzpf', '01', null, null, 'T', '省级', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_ghjsxzpf', '02', null, null, 'T', '市级', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_ghjsxzpf', '03', null, null, 'T', '县级', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_ghjsxzpf', '04', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_gjjssp', '01', null, null, 'T', '国际先进', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_gjjssp', '02', null, null, 'T', '国内先进', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_gjjssp', '03', null, null, 'T', '省内先进', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_gjjssp', '04', null, null, 'T', '市级先进', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_gjjssp', '05', null, null, 'T', '限制', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_gjjssp', '06', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_gnxycpgqqk', '01', null, null, 'T', '供大于求', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_gnxycpgqqk', '02', null, null, 'T', '供求平衡', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_gnxycpgqqk', '03', null, null, 'T', '供小于求', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_gtbmyj', '01', null, null, 'T', '国家级预审', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_gtbmyj', '02', null, null, 'T', '省级预审', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_gtbmyj', '03', null, null, 'T', '招拍挂合同', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_gtbmyj', '04', null, null, 'T', '兼有', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_gtbmyj', '05', null, null, 'T', '市级及以下', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_hppf', '01', null, null, 'T', '国家环保局', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_hppf', '02', null, null, 'T', '省环保局', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_hppf', '03', null, null, 'T', '市环保局', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_hppf', '04', null, null, 'T', '县环保局', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_hppf', '05', null, null, 'T', '无批复', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_hxzbly', '01', null, null, 'T', '国内', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_hxzbly', '02', null, null, 'T', '国外', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_hxzbly', '03', null, null, 'T', '自研', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_hxzbly', '04', null, null, 'T', '兼有', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_hyjjgmyq', '01', null, null, 'T', '符合', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_hyjjgmyq', '02', null, null, 'T', '小于', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_hyjjgmyq', '03', null, null, 'T', '大于', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_hyzr', '01', null, null, 'T', '有限制', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_hyzr', '02', null, null, 'T', '无限制', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jnpj', '01', null, null, 'T', '国家', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jnpj', '02', null, null, 'T', '省', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jnpj', '03', null, null, 'T', '市级以下', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jnpj', '04', null, null, 'T', '无', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jsdd', '01', null, null, 'T', '省级及以上园区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jsdd', '02', null, null, 'T', '产业集聚区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jsdd', '03', null, null, 'T', '新城区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jsdd', '04', null, null, 'T', '老城区', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jsdd', '05', null, null, 'T', '农田', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jslb', '01', null, null, 'T', '新建', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jslb', '02', null, null, 'T', '增资', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jslb', '03', null, null, 'T', '技改', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jslb', '04', null, null, 'T', '并购', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jslb', '05', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jsly', '01', null, null, 'T', '专利', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jsly', '02', null, null, 'T', '自主研发', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jsly', '03', null, null, 'T', '技术转让', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jsly', '04', null, null, 'T', '产学研合作', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jsly', '05', null, null, 'T', '授权使用', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_jsly', '06', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_pfsq', '01', null, null, 'T', '核准', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_pfsq', '02', null, null, 'T', '备案', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_pfsq', '03', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_spqx', '01', null, null, 'T', '国务院', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_spqx', '02', null, null, 'T', '国家投资主管部门', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_spqx', '03', null, null, 'T', '省发改委', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_spqx', '04', null, null, 'T', '省经贸委', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_spqx', '05', null, null, 'T', '市级', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_spqx', '06', null, null, 'T', '县级', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_spqx', '07', null, null, 'T', '企业自身', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_spqx', '08', null, null, 'T', '其他部门', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmbgbzdwzz', '01', null, null, 'T', '甲级', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmbgbzdwzz', '02', null, null, 'T', '乙级', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmbgbzdwzz', '03', null, null, 'T', '丙级', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmbgbzdwzz', '04', null, null, 'T', '无资质', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmbgbzdwzz', '05', null, null, 'T', '项目法人自编', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmbgbzdwzz', '06', null, null, 'T', '主持部门代编', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmbgbzdwzz', '07', null, null, 'T', '其他', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmfrlx', '01', null, null, 'T', '已注册三资企业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmfrlx', '02', null, null, 'T', '已注册中资企业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmfrlx', '03', null, null, 'T', '拟注册三资企业', null, null);
commit;

insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmfrxz', '01', null, null, 'T', '国有', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmfrxz', '02', null, null, 'T', '民企', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmfrxz', '03', null, null, 'T', '三资企业', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmfrxz', '04', null, null, 'T', '上市公司', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmlx', '01', null, null, 'T', '工业类', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmlx', '02', null, null, 'T', '科技类', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmlx', '03', null, null, 'T', '基建类', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('hzba_xmlx', '04', null, null, 'T', '电子信息', null, null);
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFTeamRole', 'zchgr', null, null, 'T', '再次核稿人', null, '再次核稿人');
insert into F_DATADICTIONARY (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC)
values ('WFTeamRole', 'ecfgfzr', null, null, 'T', '二次分工负责人', null, '二次分工负责人');
commit;
