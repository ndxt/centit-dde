<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>
<div class="pageHeader">
	<form id="frm_main" action="${pageContext.request.contextPath }/dde/dataOptInfo!save.do" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
		<%-- 隐藏Field表单 --%>
		<fieldlist id="hid_field_form_list_dataoptinfo">
			<c:forEach items="${object.dataOptSteps }" var="field">
				<field columnNo="${field.mapInfoOrder - 1 }">
					<input type="hidden" name="dataOptSteps[${field.mapInfoOrder - 1 }].optStepId" value="${field.optStepId }"/>
					<input type="hidden" name="dataOptSteps[${field.mapInfoOrder - 1 }].importId" value="${field.importId }"/>
					<input type="hidden" name="dataOptSteps[${field.mapInfoOrder - 1 }].optType" value="${field.optType }"/>
					<input type="hidden" name="dataOptSteps[${field.mapInfoOrder - 1 }].dataOptId" value="${field.dataOptId }"/>
					<input type="hidden" name="dataOptSteps[${field.mapInfoOrder - 1 }].osId" value="${field.osId }"/>
					<input type="hidden" name="dataOptSteps[${field.mapInfoOrder - 1 }].mapInfoOrder" value="${field.mapInfoOrder }"/>
				</field>
			</c:forEach>
			
		</fieldlist>
	
	    <div id="div_export_field_dataoptinfo" class="pageFormContent" style="height: 100px;">
	        <div class="unit">
	        		<%-- <input id="hid_sdn" type="hidden" name="destDatabaseName" value="${object.destDatabaseName }" />
	        		<input id="hid_tableName" type="hidden" name="tableName" value="${object.tableName }" />
	        		<input id="hid_beforeImportBlock" type="hidden" name="beforeImportBlock" beforeval="" value="${object.beforeImportBlock }"/>
	        		<input id="hid_afterImportBlock" type="hidden" name="afterImportBlock" beforeval="" value="${object.afterImportBlock }"/>
	        		<input id="hid_recordOperate" type="hidden" name="recordOperate" beforeval="" value="${object.recordOperate }"/> --%>
	        		
	        		
					<label title="与导出时数据处理操作ID一致，可导入相应的离线文件">数据处理操作ID：</label> <input name="dataOptId" type="text" size="30" value="${object.dataOptId }" class="required" maxlength="20" />
					<label>处理名称：</label> <input name="optName" type="text" size="30" value="${object.optName }" class="required" maxlength="100"/>
	
					
				</div>
	
				<div class="unit">
					<label>处理说明：</label> <textarea name="optDesc" rows="4" cols="136">${object.optDesc }</textarea>
	
				</div>
	    </div>
	    <div class="subBar">
	        <ul>
	            <li style="float:right; margin-right:50px;">
	                    <div class="buttonActive">
	                        <div class="buttonContent">
	                            <button type="submit">保存</button>
	                        </div>
	                    </div>
	
	
	                <div class="buttonActive">
	                    <div class="buttonContent">
	                        <button onclick="navTab.closeCurrentTab('external_DATAOPTINFO')">返回</button>
	                    </div>
	                </div>
	            </li>
	        </ul>
	    </div>
    </form>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
        	<c:set var="dialog_height">width="350" height="230" </c:set>
            <c:set var="pageScoreAdd">
            	<a class="add" href="${pageContext.request.contextPath }/dde/dataOptInfo!formField.do?dataOptId=${object.dataOptId }" target="dialog" rel="detailForm" title="新增数据导出内容字段" ${dialog_height } resizable="false" maxable="false"><span>新增</span></a>
            </c:set>
            <li class="new">${pageScoreAdd }</li>
        </ul>
    </div>

    <div layoutH=".pageHeader 54">
        <table class="list" style="min-width: 1000px; width: 100%">
            <thead align="center">
            <tr>
                <th width="30%">导入名称</th>
                <th width="30%">业务系统</th>
                <th width="10%">操作类型</th>
                <!-- <th width="13%">数据处理ID</th>
                <th width="40%">操作步骤ID</th> -->
                <th width="10%">执行顺序</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="tbody_dataoptinfo_field" align="center" class="sortDrag sourcetable">
	            <c:forEach items="${object.dataOptSteps }" var="field">
					<tr columnNo="${field.mapInfoOrder - 1 }">
						<td nowrap width="30%"><c:if test="${'1' eq field.optType }">${field.importName }</c:if></td>
						<td nowrap width="30%"><c:if test="${'2' eq field.optType }">${field.osId }</c:if></td>
						<td nowrap width="10%">
							<c:if test="${'1' eq field.optType }">导入</c:if>
							<c:if test="${'2' eq field.optType }">调用接口</c:if>
						</td>
<%-- 						<td nowrap width="13%">${field.optStepId }</td>
						<td nowrap width="10%">${field.dataOptId }</td> --%>
						<td nowrap width="10%">${field.mapInfoOrder }</td>
						<td align="center"><a class="edit" href="" target="dialog" callback="javascript:new ExportSql().pubfuns.exportFieldCallback(this);" rel="detailForm" width="530" height="430"
							resizable="false" maxable="false" title="编辑数据导出内容字段"><span class="icon icon-compose"></span></a> <a class="delete" href="javascript:;"><span class="icon icon-trash"></span></a></td>
					</tr>
				</c:forEach>
            </tbody>
        </table>
    </div>



    <div class="panelBar">
        <ul class="toolBar">
            <li class="new">${pageScoreAdd }</li>
        </ul>
    </div>
</div>






<script src="${pageContext.request.contextPath }/scripts/module/dde/dataoptinfo/dataoptinfo.js"></script>
<script>
 $(function(){
	 new ExportSql().init();
 });
</script>


















