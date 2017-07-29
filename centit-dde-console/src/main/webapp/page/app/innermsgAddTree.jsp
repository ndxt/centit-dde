<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<link rel="stylesheet" href="${contextPath }/scripts/plugin/zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="${contextPath }/scripts/plugin/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${contextPath }/scripts/plugin/zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${contextPath }/scripts/plugin/zTree/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="${contextPath }/scripts/sys/selectUnitUser.js"></script>
<script type="text/javascript">
	$(function() {
		var param = {
				'treeObj' : 'innermsg_tree',
				'usercode' : 'txt_innermsg_receive_usercode',
				'username' : 'txa_innermsg_receive_name',
				'btnAdd' : 'btn_innermsg_add_tree'
		};
		
		var innermsg = new UnitUser(param);

		innermsg.funs.initCheckbox($.parseJSON('${unit }'));
		
	});
</script>


<div class="pageContent">
	<div class="subBar">
		<ul>
			<li><div class="buttonActive">
					<div class="buttonContent">
						<button id="btn_innermsg_add_tree" type="button">保存</button>
					</div>
				</div></li>
		</ul>
	</div>

	<div class="zTreeDemoBackground">
		<ul id="innermsg_tree" class="ztree"></ul>
	</div>
</div>

