<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<link rel="stylesheet" href="${contextPath }/scripts/plugin/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${contextPath }/scripts/plugin/zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${contextPath }/scripts/plugin/zTree/js/jquery.ztree.excheck-3.5.js"></script>

<SCRIPT type="text/javascript">
		<!--
		var setting = {
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: zTreeOnClick
			}
		};
		      
		var unitinfos = $.parseJSON('${unitinfos}');
		for(var i=0;i<unitinfos.length;i++){
			if(unitinfos[i].pId=="0"){
				unitinfos[i].icon="${contextPath }/scripts/plugin/zTree/css/zTreeStyle/img/diy/1_open.png";
			}
			else{
				unitinfos[i].icon="${contextPath }/scripts/plugin/zTree/css/zTreeStyle/img/diy/2.png";
			}
		}
		unitinfos[0].open=true;
		var groups = $.parseJSON('${groups}');
		unitinfos.push({"id":"600000","pId":"0","name":"客户",
			            "icon":"${contextPath }/scripts/plugin/zTree/css/zTreeStyle/img/diy/1_open.png"});
		unitinfos.push({"id":"600001","pId":"0","name":"供应商",
						"icon":"${contextPath }/scripts/plugin/zTree/css/zTreeStyle/img/diy/1_open.png"});
		unitinfos.push({"id":"600002","pId":"0","name":"人才信息库",
						"icon":"${contextPath }/scripts/plugin/zTree/css/zTreeStyle/img/diy/1_open.png"});
		
		for(var j=0;j<groups.length;j++){
			groups[j].icon="${contextPath }/scripts/plugin/zTree/css/zTreeStyle/img/diy/2.png";
		}
		
		
		function zTreeOnClick(event, treeId, treeNode) {
			var url;
			if(treeNode.id=='600000'){
				url="/oa/sys/addressBook!listAddressBookByUnit.do?s_bodytype=2";
			}else if(treeNode.id=='600001'){
				url="/oa/sys/addressBook!listAddressBookByUnit.do?s_bodytype=3";
			}else if(treeNode.id=='600002'){
				url="/oa/sys/addressBook!listAddressBookByUnit.do?s_bodytype=4";
			}else if(treeNode.remark=='group'){
				url="/oa/sys/addressBook!listAddressBookByGroup.do?s_groupid="+treeNode.id;
			}else{				
			    url="/oa/sys/addressBook!listAddressBookByUnit.do?s_unitcode="+treeNode.id+"&s_bodytype=1";
			}
			$("#addressBookListByUnit").attr("action",url).submit();
		};
		
		$(document).ready(function(){
			$.fn.zTree.init($("#addressBook"), setting, unitinfos.concat(groups));
		});
		//-->
	</SCRIPT>

<div class="pageContent" style="padding:5px">
<form onsubmit="return validateCallback(this, navTabAjaxDone)" action="${pageContext.request.contextPath }/sys/userDefGroup!save.do" 
      method="post">
	<div class="panel">
		<h1>自定义分组</h1>
		<div>
			分组名称：<input type="text" name="groupname" class="required"/>
			分组描述：<input type="text" name="groupdesc" />
			<ul class="rightTools">
				<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">自定义分组</button>
							</div>
						</div></li>
				<li><a class="button" target="navTab" href="${pageContext.request.contextPath }/sys/userDefGroup!list.do" rel="userDefGroup"><span>分组管理</span></a></li>
			</ul>
		</div>
	</div>
</form>
	<div class="tabs">
		<div class="tabsHeader">
			<div class="tabsHeaderContent">
				<ul>
					<li><a href="javascript:;"><span>通讯录</span></a></li>
				</ul>
			</div>
		</div>
		<div class="tabsContent">	
			<div>
				    <div style="float: left; display: block; overflow: auto; width: 240px; border: 1px solid rgb(204, 204, 204); line-height: 21px; background-color: rgb(255, 255, 255); height: 473px; background-position: initial initial; background-repeat: initial initial;">
		                 <ul id="addressBook" class="ztree"></ul>
	                </div>
				
				
				<div class="unitBox" style="margin-left:246px;" id="addressBookShow">
					<jsp:include page="./addressBookListByUnit.jsp"></jsp:include>
				</div>	
			</div>		
		</div>
		<div class="tabsFooter">
			<div class="tabsFooterContent"></div>
		</div>
	</div>
	
</div>




