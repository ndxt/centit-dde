truncate table F_OPTINFO;
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('FGWFUNC', '0', '申报业务', null, '...', null, null, 'Y', null, null, 'S', 11, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('QUICKPAS', 'FGWFUNC', '数据导出', null, 'fgwsb/dataChange!showExport.do', null, null, 'Y', null, null, 'N', 1, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('PTGW', 'FGWFUNC', '数据导入', null, 'fgwsb/dataChange!showImport.do', null, null, 'Y', null, null, 'M', 2, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('XZXK', 'FGWFUNC', '行政许可', null, 'fgwsb/powerMatters!quickpass.do?s_kind=XK', null, null, 'Y', null, null, 'M', 3, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('XZSP', 'FGWFUNC', '行政审批', null, 'fgwsb/powerMatters!quickpass.do?s_kind=SP', null, null, 'Y', null, null, 'M', 4, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('XZQR', 'FGWFUNC', '行政确认', null, 'fgwsb/powerMatters!quickpass.do?s_kind=QR', null, null, 'Y', null, null, 'M', 5, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('XZCF', 'FGWFUNC', '行政处罚', null, 'fgwsb/powerMatters!quickpass.do?s_kind=CF', null, null, 'Y', null, null, 'M', 6, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('QT', 'FGWFUNC', '其他', null, 'fgwsb/powerMatters!quickpass.do?s_kind=QT', null, null, 'Y', null, null, 'M', 7, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('SUPORT', '0', '支持业务', null, '...', null, null, 'Y', null, null, 'S', 4, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('RUNLOG', 'SUPORT', '业务运行记录', null, 'sys/optRunRec!list.do', null, null, 'Y', null, null, 'S', 4, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('DBSX', 'SUPORT', '待办事项', null, 'yxxk/userTasks!list.do', null, null, 'Y', null, null, 'S', 2, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('InnerMsg', 'APPLIC', '留言与公告', null, 'app/innermsg!showMsgFrame.do', null, null, 'Y', null, null, 'S', 5, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('UPLOADFI', 'SUPORT', '上传文件', null, 'app/fileinfo!list.do', null, null, 'Y', null, null, 'S', null, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('SYSCONF', '0', '系统配置', null, '...', null, null, 'Y', null, null, 'M', 5, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('LOGINCAS', '0', 'CAS登录过滤', null, 'sys/mainFrame!logincas.do', null, null, 'N', null, null, 'S', 100, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('DICTSET', 'SYSCONF', '数据字典管理', null, 'sys/dictionary!list.do?s_style=U', null, null, 'Y', null, null, 'M', null, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('SYSPRM', 'SYSCONF', '系统参数配置', null, 'sys/systemParameterSetting.do', null, null, 'Y', null, null, 'M', null, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('APPLIC', '0', '通用业务', null, '...', null, null, 'Y', null, null, 'S', 1, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('ADDRESS', 'SUPORT', '通讯录', null, 'sys/addressBook!list.do', null, null, 'Y', null, null, 'N', null, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('ORGMAG', '0', '组织管理', null, '...', null, null, 'Y', null, null, 'M', 3, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('USERMAG', 'ORGMAG', '用户管理', null, 'sys/userDef!list.do', null, null, 'Y', null, null, 'M', 1, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('ROLEMAG', 'ORGMAG', '角色定义', null, 'sys/roleDef!list.do', null, null, 'Y', null, null, 'M', 3, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('PFW', 'FGWFUNC', '批复文', null, 'fgwsb/pfw!list.do', null, null, 'Y', null, null, 'N', 1, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('UNITMAG', 'ORGMAG', '机构管理', null, 'sys/unit!list.do', null, null, 'Y', null, null, 'M', 2, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('SEARCH', 'SUPORT', '全文检索', null, 'app/searcher!search.do', null, null, 'Y', null, null, 'S', null, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('DEPTMAG', '0', '部门管理', null, '...', null, null, 'Y', null, null, 'M', 2, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('OPTINFO', 'SYSCONF', '系统业务', null, 'sys/optInfo!list.do', null, null, 'Y', null, null, 'M', 4, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('OPTDEF', 'OPTINFO', '业务操作', null, 'sys/optDef!list.do', null, null, 'N', null, null, 'M', null, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('DEPTUR', 'DEPTMAG', '部门用户权限', null, 'sys/deptManager!listuser.do?unitcode=thisunit', null, null, 'Y', null, null, 'M', null, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('DEPTPOW', 'DEPTMAG', '下属部门权限', null, 'sys/deptManager!listunit.do', null, null, 'Y', null, null, 'M', null, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('DEPTROLE', 'DEPTMAG', '部门角色定义', null, 'sys/deptManager!listrole.do', null, null, 'Y', null, null, 'M', null, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('FLOWOPT', '0', '流程业务', null, '...', null, null, 'Y', null, null, 'N', 99, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('FLOWDEF', 'SUPORT', '工作流程定义', null, 'sampleflow/sampleFlowDefine!list.do', null, null, 'Y', null, null, 'S', 1, 'ceshi', 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('BAOXIAO', 'FLOWOPT', '报销流程', null, 'app/reimburse!list.do', null, null, 'N', null, null, 'W', 1, '1000000', 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('FLOWINST', 'SUPORT', '流程实例管理', null, 'sampleflow/sampleFlowManager!list.do', null, null, 'Y', null, null, 'N', null, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('NODE', 'SUPORT', '孤儿节点管理', null, 'sampleflow/sampleFlowManager!listNoOptNodes.do', null, null, 'Y', null, null, 'M', 3, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('qingjia', 'FLOWOPT', '请假', null, '/app/qingjia', null, null, 'N', null, null, 'W', null, '1000002', 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('QINGJIA2', 'FLOWOPT', '请假二', null, '...', null, null, 'N', null, null, 'W', null, '1000003', 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('yxxkflow', 'FLOWOPT', '行权许可', null, 'yxxk/exercisePermit!list.do', null, null, 'N', null, null, 'W', null, '000006', 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('Calendar', 'SUPORT', '企业日历', null, 'app/calendar!view.do', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('XZGL', 'FGWFUNC', '行政事项管理', null, 'fgwsb/powerMatters!list.do', null, null, 'Y', null, null, 'M', 8, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('TZGG', 'FGWFUNC', '通知公告', null, 'fgwsb/report!list.do', null, null, 'Y', null, null, 'M', 9, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('BJCL', '0', '办件处理', null, '...', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('LINK', 'FGWTEST', '流程环节设置', null, 'yxxk/qlyxLink!optlist.do?s_preoptid=FLOWOPT', null, null, 'Y', null, null, 'S', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('PUBLISH', 'FGWTEST', '办件登记', null, 'yxxk/exercisePermit!flowInit.do', null, null, 'Y', null, null, 'N', 1, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('CS2', 'BJCL', '测试', null, 'dasd', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('QLYX', 'FLOWOPT', '办件登记', null, 'yxxk/abc', null, null, 'N', null, null, 'W', null, '100062', 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('VERIFY', 'ORGMAG', '用户审核', null, 'sys/userDef!toVerify.do?s_userState=0', null, null, 'Y', null, null, 'M', 4, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('YHTS', 'FGWFUNC', '用户投诉', null, 'fgwsb/complaint!built.do', null, null, 'Y', null, null, 'M', 8, null, 'D');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('fgwflow', 'FLOWOPT', '发改委流程', null, 'app/reimburse!list.do', null, null, 'N', null, null, 'W', 7, '1000002', 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('ZBJ', 'BJCL', '在办件', null, 'yxxk/qlyxQlinfo!save_init.do', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('DBJ', 'BJCL', '待办件', null, 'yxxk/qlyxQlinfo!list.do', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('BJJ', 'BJCL', '办结件', null, 'yxxk/qlyxQlinfo!delete.do', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('DYJ', 'BJCL', '待阅件', null, '建设中...', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('DGDFW', 'BJCL', '待归档发文', null, '建设中...', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('DSW', 'DBJ', '待收文', null, '.建设中..', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('DFW', 'DBJ', '待发文', null, '建设中...', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('DLDQF', 'DBJ', '代领导签发', null, '建设中...', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('DLDYS', 'DBJ', '代领导阅示', null, '建设中...', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('QTDB', 'DBJ', '其他待办', null, '建设中...', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('ZSW', 'ZBJ', '在收文', null, '建设中...', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('ZFW', 'ZBJ', '在发文', null, '建设中...', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('ZSCZ', 'ZBJ', '在收传真', null, '建设中...', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('ZFCZ', 'ZBJ', '在发传真', null, '建设中...', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('ZBJY', 'ZBJ', '在办机要', null, '建设中...', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('ZBTA', 'ZBJ', '在办提案', null, '建设中...', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('ZBTZS', 'ZBJ', '在办通知书', null, '建设中...', null, null, 'Y', null, null, 'N', null, null, 'F');
--insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
--values ('YBJBJ', 'BJJ', '已办结办件', null, '建设中...', null, null, 'Y', null, null, null, null, null, null);
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('YBJCZ', 'BJJ', '已办结传真', null, '建设中...', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('YBJJY', 'BJJ', '已办结机要', null, '已办结机要...', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('FGWTEST', '0', '许可运行平台', null, '...', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('FlowDef', 'FGWTEST', '事项流程定义', null, 'yxxk/qlyxFlow!list.do', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('TEST1', 'FGWTEST', '行政权力维护', null, 'yxxk/qlyxQlinfo!list.do', null, null, 'Y', null, null, 'N', null, null, 'F');
insert into F_OPTINFO (OPTID, PREOPTID, OPTNAME, FORMCODE, OPTURL, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, OPTTYPE, ORDERIND, WFCODE, PAGETYPE)
values ('BJBL', 'FGWTEST', '办件办理', null, 'yxxk/userTasks!list.do', null, null, 'Y', null, null, 'N', 2, null, 'F');
commit;
