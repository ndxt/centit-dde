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
	
		<ul id="hid_trigger_form_list">
			<c:forEach items="${object.exportTriggers }" var="trigger">
				<li columnNo="${trigger.cid.triggerId }">
					<input type="hidden" name="exportTriggers[${trigger.cid.triggerId}].triggerSql" value="${trigger.triggerSql }"/>
					<input type="hidden" name="exportTriggers[${trigger.cid.triggerId}].triggerDesc" value="${trigger.triggerDesc }"/>
					<input type="hidden" name="exportTriggers[${trigger.cid.triggerId}].triggerType" value="${trigger.triggerType }"/>
					<input type="hidden" name="exportTriggers[${trigger.cid.triggerId}].triggerTime" value="${trigger.triggerTime }"/>
					<input type="hidden" name="exportTriggers[${trigger.cid.triggerId}].tiggerOrder" value="${trigger.tiggerOrder }"/>
					<input type="hidden" name="exportTriggers[${trigger.cid.triggerId}].isprocedure" value="${trigger.isprocedure }"/>
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
			
			<input id="txt_sdn" type="text" size="25" class="datasource" value="${object.sourceDatabaseName }" readonly="readonly" /> 
			<a class="btnLook" href="${pageContext.request.contextPath }/dde/exportSql!defDataSource.do"
				height="480" width="700" target="dialog" callback="new ExportSql().pubfuns.exportSqlInit" rel="exportFieldDataSource" mask="true" title="定义数据源"><span>定义数据源</span>
			</a>

		</div>

		<div class="unit">
			<label>导出说明：</label> <textarea name="exportDesc" rows="4" cols="79">${object.exportDesc }</textarea>

		</div>
	    </div>
	    <div class="subBar">
	        <ul>
	            <li style="float:right; margin-right:50px;">
                        <c:if test="${not empty object.exportId}">
                            <div class="buttonActive">
                                <div class="buttonContent">
                                    <a class="" href="${pageContext.request.contextPath }/dde/exportSql!exportSourceField.do?exportId=${object.exportId }">导出源字段配置</a>
                                </div>
                            </div>
                        </c:if>

                    <div class="buttonActive">
	                        <div class="buttonContent">
	                            <button type="submit">保存</button>
	                        </div>
	                    </div>
	
	
	                <div class="buttonActive">
	                    <div class="buttonContent">
	                        <a onclick="navTab.closeCurrentTab('external_EXPORTSQL')">返回</a>
	                    </div>
	                </div>

	            </li>
	        </ul>
	    </div>
    </form>
</div>




<div class="pageContent">
	<div class="tabs">
		<div class="tabsHeader">
			<div class="tabsHeaderContent">
				<ul>
					<li><a href="javascript:;"><span>数据导出内容字段</span></a></li>
					<li><a href="javascript:;"><span>数据导出触发器</span></a></li>
				</ul>
			</div>
		</div>
		
		<c:set var="pageHeaderHeight" value="88"/>
       	<c:set var="dialog_height"> height="400" </c:set>

        <c:set var="hasData">
            <c:if test="${20 > fn:length(object.exportFields)}">layoutH=".pageHeader ${pageHeaderHeight }"</c:if>
        </c:set>
		
		<div class="tabsContent">
		
			<%-- 数据导出内容字段 --%>
			<div>
				<div class="panelBar">
			        <ul class="toolBar">
			        	<c:set var="dialog_height"> height="400" </c:set>
			            <c:set var="pageScoreAdd">
			            	<a class="add" href="${pageContext.request.contextPath }/dde/exportSql!formField.do?exportId=${object.exportId }" target="dialog" rel="detailForm" title="新增数据导出内容字段" ${dialog_height } resizable="false" maxable="false"><span>新增</span></a>
			            </c:set>
			            <li class="new">${pageScoreAdd }</li>
			        </ul>
			    </div>
			
			    <div ${hasData }>
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
				                <tr columnNo="${field.cid.columnNo }" type='field'>
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
			               						target="dialog" callback="javascript:new ExportSql().pubfuns.exportFieldCallback(this, 'formField');" rel="detailForm" width="530" height="430" resizable="false" maxable="false" title="编辑数据导出内容字段"><span class="icon icon-compose"></span></a>
				
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
			
			<%-- 数据导出触发器 --%>
			<div>
				<div>
					<div class="panelBar">
				        <ul class="toolBar">
				            <c:set var="pageScoreAdd">
				            	<a class="add" href="${pageContext.request.contextPath }/dde/exportSql!formTrigger.do?exportId=${object.exportId }" target="dialog" rel="detailForm" title="新增数据导出内容字段" ${dialog_height } resizable="false" maxable="false"><span>新增</span></a>
				            </c:set>
				            <li class="new">${pageScoreAdd }</li>
				        </ul>
				    </div>
				
				    <div layoutH=".pageHeader ${pageHeaderHeight }">
				        <table class="list" style="min-width: 1000px; width: 100%">
				            <thead align="center">
				            <tr>
				                <th width="50%">执行语句</th>
				                <th width="10%">触发类别</th>
				                <th width="10%">触发时机</th>
				                <th width="10%">触发器顺序</th>
				                <th width="10%">无参数存储过程</th>
				                <th>操作</th>
				            </tr>
				            </thead>
				            <tbody id="tbody_export_trigger" align="center" class="sortDrag sourcetable">
				            	<c:forEach items="${object.exportTriggers }" var="trigger">
									<tr columnNo="${trigger.cid.triggerId }" type='trigger'>
										<td nowrap width="50%" title="${trigger.triggerSql }">
											<c:choose>
					                    		<c:when test="${35 < fn:length(trigger.triggerSql) }">
					                    			${fn:substring(trigger.triggerSql, 0, 34) }...
					                    		</c:when>
					                    		<c:otherwise>${trigger.triggerSql }</c:otherwise>
					                    	</c:choose>
										</td>
					                    <td nowrap width="10%">
					                    	<c:choose>
					                    		<c:when test="${'L' eq trigger.triggerType }">行触发器</c:when>
					                    		<c:when test="${'T' eq trigger.triggerType }">表触发器</c:when>
					                    	</c:choose>
					                    </td>
					                    <td nowrap width="10%">
					                    	<c:choose>
					                    		<c:when test="${'B' eq trigger.triggerTime }">交换前</c:when>
					                    		<c:when test="${'A' eq trigger.triggerTime }">交换后</c:when>
					                    		<c:when test="${'E' eq trigger.triggerTime }">交换失败后</c:when>
					                    	</c:choose>
					                    </td>
					                    <td nowrap width="10%">${trigger.tiggerOrder + 1}</td>
					                    <td nowrap width="10%">${'T' eq trigger.isprocedure ? '是' : '否' }</td>
					                    <td align="center">
				                            <a class="edit" href="javascript:;" target="dialog" callback="javascript:new ExportSql().pubfuns.exportFieldCallback(this, 'formTrigger');" rel="detailForm" width="560" height="430" resizable="false" maxable="false" title="编辑数据导出触发器"><span class="icon icon-compose"></span></a>
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
			</div>
			
		</div>
		
	</div>
</div>








<script src="${pageContext.request.contextPath }/scripts/module/dde/exportsql/exportsql.js"></script>
<script>
	new ExportSql().init();
</script>


















