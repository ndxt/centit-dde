<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>
<div class="pageHeader">
	<form id="frm_main" action="${pageContext.request.contextPath }/dde/exportSql!save.do" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
		<%-- 隐藏Field表单 --%>
		<ul id="hid_field_form_list">
			<c:forEach items="${object.exportFields }" var="field">
				<li columnNo="${field.cid.columnNo }">
					<input type="hidden" name="exportFields[${field.cid.columnNo }].fieldName" value="${field.fieldName }"/>
					<input type="hidden" name="exportFields[${field.cid.columnNo }].fieldSentence" value="${field.fieldSentence }"/>
					<input type="hidden" name="exportFields[${field.cid.columnNo }].fieldType" value="${field.fieldType }"/>
					<input type="hidden" name="exportFields[${field.cid.columnNo }].fieldFormat" value="${field.fieldFormat }"/>
					<input type="hidden" name="exportFields[${field.cid.columnNo }].fieldStoreType" value="${field.fieldStoreType }"/>
					<input type="hidden" name="exportFields[${field.cid.columnNo }].isPk" value="${field.isPk }"/>
				</li>
			</c:forEach>
			
		</ul>
	
	    <div id="div_export_field" class="pageFormContent" style="height: 130px;">
	        <div class="unit">
	        		<input type="hidden" name="exportId" value="${object.exportId }" />
	        		<input id="hid_sdn" type="hidden" name="sourceDatabaseName" value="${object.sourceDatabaseName }" />
	        		<input id="hid_querysql" type="hidden" name="querySql" beforeval="" value="${object.querySql }"/>
	        		<input type="hidden" name="afterSqlBlock" />
	        		<input type="hidden" name="beforeSqlBlock" />
			<label>数据处理操作ID：</label> <input name="dataOptId" type="text" size="30" value="${object.dataOptId }" class="required" maxlength="20"/>
			<label>导出名称：</label> <input name="exportName" type="text" size="30" value="${object.exportName }" class="required" />
	        </div>
	        <div class="unit">
			<label>源数据库名：</label> 
			
			<input id="txt_sdn" type="text" size="30" class="datasource" value="${object.sourceDatabaseName }" /> 
			<a class="btnLook" href="${pageContext.request.contextPath }/dde/exportSql!defDataSource.do"
				height="480" width="700" target="dialog" callback="new ExportSql().pubfuns.exportSqlInit" rel="defSourceAndGoalData" mask="true" title="定义数据源"><span>定义数据源</span>
			</a>

			<label>业务系统ID：</label> 
				<select name="sourceOsId" id="sel_sourceOsId">
					<c:forEach var="osinfo" items="${osinfoList }">
						<option value="${osinfo.osId }" <c:if test="${osinfo.osId eq object.sourceOsId }">selected="selected"</c:if> >${osinfo.osId }</option>
					</c:forEach>
				</select>
			
			<%-- <input name="sourceOsId" type="text" size="30" value="${object.sourceOsId }" class="required" /> --%>
			
		</div>

		<div class="unit">
			<label>导出说明：</label> <textarea name="exportDesc" rows="4" cols="136">${object.exportDesc }</textarea>

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
	                        <button onclick="navTab.closeCurrentTab('external_EXPORTSQL')">返回</button>
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
        	<c:set var="dialog_height"> height="400" </c:set>
            <c:set var="pageScoreAdd">
            	<a class="add" href="${pageContext.request.contextPath }/dde/exportSql!formField.do?exportId=${object.exportId }" target="dialog" rel="detailForm" title="新增数据导出内容字段" ${dialog_height } resizable="false" maxable="false"><span>新增</span></a>
            </c:set>
            <li class="new">${pageScoreAdd }</li>
        </ul>
    </div>

    <div layoutH=".pageHeader 54">
        <table class="list" style="min-width: 1000px; width: 100%">
            <thead align="center">
            <tr>
                <th width="13%">源字段名</th>
                <th width="40%">源字段语句</th>
                <th width="10%">源字段类型</th>
                <th width="10%">字段格式</th>
                <th width="10%">存储类型</th>
                <th width="9%">是否为主键</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="tbody_export_field" align="center" class="sortDrag sourcetable">
	            <c:forEach items="${object.exportFields }" var="field">
	                <tr columnNo="${field.cid.columnNo }">
	                    <td nowrap width="13%">${field.fieldName }</td>
	                    <td nowrap width="15%">${field.fieldSentence }</td>
	                    <td nowrap width="10%">${field.fieldType }</td>
	                    <td nowrap width="10%">${field.fieldFormat }</td>
	                    <td nowrap width="10%">
	                    	<c:if test="${'0' eq field.fieldStoreType }">embedded</c:if>
	                    	<c:if test="${'1' eq field.fieldStoreType }">infile</c:if>
	                    </td>
	                    <td nowrap width="10%">${'1' eq field.isPk ? '是' : '否' }</td>
	                    <td align="center">
	                            <a class="edit" href=""
               						target="dialog" callback="javascript:new ExportSql().pubfuns.exportFieldCallback(this);" rel="detailForm" width="530" height="430" resizable="false" maxable="false" title="编辑数据导出内容字段"><span class="icon icon-compose"></span></a>
	
	                            <a class="delete" href="javascript:;"><span class="icon icon-trash"></span></a>
	                        
	                    </td>
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






<script src="${pageContext.request.contextPath }/scripts/module/dde/exportsql/exportsql.js"></script>
<script>
new ExportSql().init();
</script>


















