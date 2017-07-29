<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>
<div class="pageHeader">
	<form id="frm_main" action="${pageContext.request.contextPath }/dde/importOpt!save.do" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
		<%-- 隐藏Field表单 --%>
		<fieldlist id="hid_field_form_list_import">
			<c:forEach items="${object.importFields }" var="field">
				<field columnNo="${field.cid.columnNo }">
					<input type="hidden" name="importFields[${field.cid.columnNo }].sourceFieldName" value="${field.sourceFieldName }"/>
					<input type="hidden" name="importFields[${field.cid.columnNo }].destFieldName" value="${field.destFieldName }"/>
					<input type="hidden" name="importFields[${field.cid.columnNo }].destFieldType" value="${field.destFieldType }"/>
					<input type="hidden" name="importFields[${field.cid.columnNo }].isPk" value="${field.isPk }"/>
					<input type="hidden" name="importFields[${field.cid.columnNo }].isNull" value="${field.isNull }"/>
					<input type="hidden" name="importFields[${field.cid.columnNo }].destFieldDefault" value="${field.destFieldDefault }"/>
				</field>
			</c:forEach>
			
		</fieldlist>
	
	    <div id="div_import_field" class="pageFormContent" style="height: 130px;">
	        <div class="unit">
	        		<input type="hidden" name="importId" value="${object.importId }" />
	        		<input id="hid_sdn_import" type="hidden" name="destDatabaseName" value="${object.destDatabaseName }" />

	        		<input id="hid_recordOperate_import" type="hidden" />
	        		<input id="hid_querysql_import" type="hidden" />
	        		
	        		
					<label>导入名称：</label> <input name="importName" type="text" size="30" value="${object.importName }" class="required" />
	
					
					<label>源数据库名：</label> 
					
					<input id="txt_sdn_import" type="text" size="30" class="datasource" value="${object.destDatabaseName }" /> 
					<a class="btnLook" href="${pageContext.request.contextPath }/dde/importOpt!defDataSource.do"
						height="280" width="400" target="dialog" callback="new ExportSql().pubfuns.exportSqlInit" rel="defSourceAndGoalData" mask="true" title="定义数据源"><span>定义数据源</span>
					</a>
					
				</div>
	
				<div class="unit">
					<label>导入表：</label><input id="hid_tableName" type="text" name="tableName" value="${object.tableName }" size="30" readonly="readonly" />
	        		<label>记录操作：</label> 
					<input type="radio" name="recordOperate" value="1" <c:if test="${('2' ne object.recordOperate) and ('3' ne object.recordOperate) }">checked="checked"</c:if>  />插入
					<input type="radio" name="recordOperate" value="2" <c:if test="${'2' eq object.recordOperate }">checked="checked"</c:if>  />更新
					<input type="radio" name="recordOperate" value="3" <c:if test="${'3' eq object.recordOperate }">checked="checked"</c:if>  />合并
	
				</div>
				<div class="unit">
					<label>导入说明：</label> <textarea name="importDesc" rows="4" cols="136">${object.importDesc }</textarea>
	
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
	                        <button onclick="navTab.closeCurrentTab('external_IMPORTOPT')">返回</button>
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
            	<a class="add" href="${pageContext.request.contextPath }/dde/importOpt!formField.do?importId=${object.importId }" target="dialog" rel="detailForm" title="新增数据导出内容字段" ${dialog_height } resizable="false" maxable="false"><span>新增</span></a>
            </c:set>
            <li class="new">${pageScoreAdd }</li>
        </ul>
    </div>

    <div layoutH=".pageHeader 54">
        <table class="list" style="min-width: 1000px; width: 100%">
            <thead align="center">
            <tr>
                <th width="13%">源字段名</th>
                <th width="40%">目标字段名</th>
                <th width="10%">目标字段类型</th>
                <th width="10%">是否为主键</th>
                <th width="10%">目标是否可以为空</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="tbody_import_field" align="center" class="sortDrag sourcetable">
	            <c:forEach items="${object.importFields }" var="field">
	                <tr columnNo="${field.cid.columnNo }">
	                    <td nowrap width="13%">${field.sourceFieldName }</td>
	                    <td nowrap width="15%">${field.destFieldName }</td>
	                    <td nowrap width="10%">${field.destFieldType }</td>
	                    <td nowrap width="10%">${'1' eq field.isPk ? '是' : '否' }</td>
	                    <td nowrap width="10%">${'1' eq field.isNull ? '是' : '否' }</td>
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






<script src="${pageContext.request.contextPath }/scripts/module/dde/importsql/importsql.js"></script>


















