<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<%--<script type="text/javascript" src="${contextPath }/scripts/sys/selectUnitUser.js"></script>--%>
<script type="text/javascript">
	$(function() {
		var param = {
			'treeObj' : 'roleuseadd_tree'
		};

		var $tree = $('#' + param.treeObj);

		var settingCheckbox = {
			data : {
				simpleData : {
					enable : true
				}
			},
			check : {
				enable : true
			}
		};

		$.fn.zTree.init($tree, settingCheckbox, $.parseJSON('${unitJson }'));

        var $treeObj = $.fn.zTree.getZTreeObj(param.treeObj);

        $.each($.parseJSON('${chooseUserJson }'), function(index, u){
			var $checkNode = $treeObj.getNodeByParam('id', u);

            $treeObj.checkNode($checkNode, true, true);

		});


        $('#btn_roleusersave').click(function(){
            $checkNodes = $treeObj.getCheckedNodes(true);

            var usercode =  [];
            $.each($checkNodes, function(index, node){
                usercode.push(node.id);
            });

            //debugger;
            if(usercode.length > 0) {
                $('#hidRoleUserUsercode').val(usercode.join(','));
            }else{
                $('#hidRoleUserUsercode').val('');
            }


            $('#frmRoleUserAdd').submit();
        });
	});
</script>
<style>
<!--
ul.ztree {
	width: 430px;
	height: 560px;
}
-->
</style>

<form id="frmRoleUserAdd" onsubmit="return validateCallback(this, dialogAjaxDone);" action="${contextPath}/sys/userDef!roleUserSave.do" method="post">
    <input id="hidRoleUserUsercode" type="hidden" name="usercode"/>
    <input type="hidden" name="s_rolecode" value="${param['s_rolecode']}"/>
    <input type="hidden" name="tabid" value="${param['tabid']}"/>
</form>

<div class="pageContent">
	<div class="pageFormContent" layoutH="58">

		<div class="unit zTreeDemoBackground">
			<ul id="roleuseadd_tree" class="ztree"></ul>
		</div>
	</div>


	<div class="formBar">
		<ul>
			<li>
				<div class="buttonActive">
					<div class="buttonContent">
						<button id="btn_roleusersave" type="button">保存</button>
					</div>
				</div>
			</li>

			<li>
				<div class="buttonActive">
					<div class="buttonContent">
						<button id="btn_roleuserclose" type="button" class="close">取消</button>
					</div>
				</div>
			</li>
		</ul>
	</div>
</div>

