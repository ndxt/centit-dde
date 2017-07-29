<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<%-- 
	TODO
	本功能实现通过exchange.js中方法操作页面所有表单元素，将数据源，目标字段均放入隐藏Form中，页面操作修改表格显示和替换隐藏域中的值。所有操作完成后统一提交数据，保持数据一致性。
 --%>

<div class="pageHeader">
	<form id="frm_main_exchange_mapinfo" action="${pageContext.request.contextPath }/dde/exchangeMapinfoNew!save.do" method="post" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
		<%-- 隐藏Field表单 --%>
		<ul id="hid_field_form_list">
			<c:forEach items="${object.mapInfoDetails }" var="field">
				<li columnNo="${field.cid.columnNo }" type="source">
					<input type="hidden" name="mapInfoDetails[${field.cid.columnNo }].sourceTableName" value="${field.sourceTableName }"/>
					<input type="hidden" name="mapInfoDetails[${field.cid.columnNo }].sourceUrl" value="${field.sourceUrl }"/>
					<input type="hidden" name="mapInfoDetails[${field.cid.columnNo }].sourceFieldName" value="${field.sourceFieldName }"/>
					<input type="hidden" name="mapInfoDetails[${field.cid.columnNo }].sourceFieldSentence" value="${field.sourceFieldSentence }"/>
					<input type="hidden" name="mapInfoDetails[${field.cid.columnNo }].sourceFieldType" value="${field.sourceFieldType }"/>
				</li>
				<li columnNo="${field.cid.columnNo }" type="dest">
					<input type="hidden" name="mapInfoDetails[${field.cid.columnNo }].goalTableName" value="${field.goalTableName }"/>
					<input type="hidden" name="mapInfoDetails[${field.cid.columnNo }].goalUrl" value="${field.goalUrl }"/>
					<input type="hidden" name="mapInfoDetails[${field.cid.columnNo }].destFieldName" value="${field.destFieldName }"/>
					<input type="hidden" name="mapInfoDetails[${field.cid.columnNo }].destFieldType" value="${field.destFieldType }"/>
					<input type="hidden" name="mapInfoDetails[${field.cid.columnNo }].isPk" value="${field.isPk }"/>
					<input type="hidden" name="mapInfoDetails[${field.cid.columnNo }].isNull" value="${field.isNull }"/>
					<input type="hidden" name="mapInfoDetails[${field.cid.columnNo }].destFieldDefault" value="${field.destFieldDefault }"/>
				</li>
			</c:forEach>
			
		</ul>
	
		<ul id="hid_trigger_form_list">
			<c:forEach items="${object.mapInfoTriggers }" var="trigger">
				<li columnNo="${trigger.cid.triggerId }">
					<input type="hidden" name="mapInfoTriggers[${trigger.cid.triggerId}].triggerSql" value="${trigger.triggerSql }"/>
					<input type="hidden" name="mapInfoTriggers[${trigger.cid.triggerId}].triggerDesc" value="${trigger.triggerDesc }"/>
					<input type="hidden" name="mapInfoTriggers[${trigger.cid.triggerId}].triggerDatabase" value="${trigger.triggerDatabase }"/>
					<input type="hidden" name="mapInfoTriggers[${trigger.cid.triggerId}].triggerType" value="${trigger.triggerType }"/>
					<input type="hidden" name="mapInfoTriggers[${trigger.cid.triggerId}].triggerTime" value="${trigger.triggerTime }"/>
					<input type="hidden" name="mapInfoTriggers[${trigger.cid.triggerId}].tiggerOrder" value="${trigger.tiggerOrder }"/>
					<input type="hidden" name="mapInfoTriggers[${trigger.cid.triggerId}].isprocedure" value="${trigger.isprocedure }"/>
				</li>
			</c:forEach>
			
		</ul>
	
		  
		  
		<div id="div_export_field" class="pageFormContent" style="height: 135px;">
			<%-- ExchangeMapInfo 字段隐藏域  start --%>
			<input type="hidden"  name="mapInfoId" value="${object.mapInfoId }" />
			<input id="hid_querysql" type="hidden" name="querySql" beforeval="" value="${object.querySql }"/>
			<input id="hid_dest_querysql" type="hidden" />

      		
      		<%-- ExchangeMapInfo 字段隐藏域  end --%>
      		
      		
      		
      		
			<div class="unit">
				<label>左（数据源）定义：</label> 
				
					<input id="txt_exchangemapinfo_sds" name="sourceDatabaseName" type="text" size="10" value="${object.sourceDatabaseName }" readonly="readonly"/>
					<input id="txt_exchangemapinfo_st" name="sourceTableName" type="text" size="25" class="datasource" value="${object.sourceTableName }" readonly="readonly"/>
					<a class="btnLook" href="${ctx }/dde/exchangeMapinfoNew!sourceDs.do" height="350" width="600" callback="new ExportSql().pubfuns.defSourceDsInit" target="dialog"
					   rel="defSourceAndGoalData<c:if test="${not empty object.mapInfoId}">_edit</c:if>" mask="true" title="定义数据源"><span>定义数据源</span></a>
				
				<label>右（数据目标）定义：</label> 
				
					<input id="txt_exchangemapinfo_dds" name="destDatabaseName" type="text" size="10" value="${object.destDatabaseName }" readonly="readonly"/>
					<input id="txt_exchangemapinfo_dt" name="destTableName" type="text" size="25" class="datadest" value="${object.destTableName}" readonly="readonly"/>
					<a class="btnLook" href="${ctx }/dde/exchangeMapinfoNew!destDs.do" target="dialog" height="350" width="600" callback="new ExportSql().pubfuns.defDestDsInit"
					   rel="defDestAndGoalData<c:if test="${not empty object.mapInfoId}">_edit</c:if>" mask="true" title="定义数据目标"><span>定义数据目标</span> </a>
			</div>
			<div class="unit">
				<label>交换名称：</label> 
				<input type="text"  name="mapInfoName" value="${object.mapInfoName}" size="42" />
				
				<label style="margin-left: 22px;">是否重复执行：</label>
				<div style="float:left; ">
					<input type="radio" name="isRepeat" value="1"  <c:if test="${'1' eq object.isRepeat}">checked=true</c:if> />是
					<input type="radio" name="isRepeat" value="0"  <c:if test="${'0' eq object.isRepeat or empty object.isRepeat}">checked=true</c:if>/>否
				</div>
			</div>
			<div class="unit">
				<label>记录操作：</label>
				
				<div style="float:left;">
				<input type="radio" name="recordOperate" value="1"  <c:if test="${'1' eq object.recordOperate }">checked=true</c:if> />插入（insert）
				<input type="radio" name="recordOperate" value="2"  <c:if test="${'2' eq object.recordOperate }">checked=true</c:if>/>更新（update）
				<input type="radio" name="recordOperate" value="3"  <c:if test="${'3' eq object.recordOperate or empty object.recordOperate }">checked=true</c:if>/>合并（merge）
				</div>
				
				
				
			</div>
			
			<div class="unit">
				<label>交换说明：</label> <textarea rows="3" cols="33" name="mapInfoDesc" id="mapinfoDesc_edit">${object.mapInfoDesc }</textarea>
			
			</div>
		</div>
	  
	  
		<div class="subBar">
	        <ul>

	            <li style="float:right; width: 400px;">
	            	    <div class="">
	                        <div class="" style="">
	                        <c:if test="${empty object.mapInfoName }">
	                      
	                            <button id="exchange_file_upload" ></button>
	                        </c:if>
	                        </div>
	                    </div>
	                    <div class="buttonActive">
	                    <c:if test="${not empty object.mapInfoName }">
	                        <div class="buttonContent">
	                            <a class="" href="${pageContext.request.contextPath }/dde/exchangeMapinfoNew!exportMapinfoDetail.do?mapInfoId=${object.mapInfoId }">导出字段配置</a>
	                        </div>
	                    </c:if>
	                    </div>
	                    
	                    <div class="buttonActive">
	                        <div class="buttonContent">
	                            <button type="submit">保存</button>
	                        </div>
	                    </div>

	
	                <div class="buttonActive">
	                    <div class="buttonContent">
	                        <a onclick="navTab.closeCurrentTab('external_JLJHDYGX1')">返回</a>
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
					<li><a href="javascript:;"><span>数据交换内容字段</span></a></li>
					<li><a href="javascript:;"><span>数据交换触发器</span></a></li>
				</ul>
			</div>
		</div>
		
		<c:set var="pageHeaderHeight" value="88"/>
       	<c:set var="dialog_height"> height="400" </c:set>


        <c:set var="hasData">
            <c:if test="${20 > fn:length(object.mapInfoDetails)}">layoutH=".pageHeader ${pageHeaderHeight }"</c:if>
        </c:set>
		
		<div class="tabsContent">
		
			<%-- 数据导出内容字段 --%>
			<div>
				<div class="panelBar">
			        <ul class="toolBar">
                        <span style="color: green;">可拖动源字段或目标字段数据列更换字段对应关系</span>
			        	<c:set var="dialog_height"> height="400" </c:set>
			            <c:set var="pageScoreAdd">
			            	<a class="add" href="${pageContext.request.contextPath }/dde/exchangeMapinfoNew!formField.do" target="dialog" rel="detailForm" title="新增数据数据交换字段" ${dialog_height } resizable="false" maxable="false"><span>新增</span></a>
			            </c:set>
			            <c:set var="pageScoreDel">
			            	<a class="delete" href="javascript:;" title="选中序号删除整行"><span>删除整行</span></a>
			            </c:set>
			            <li class="delete" style="float: right;">${pageScoreDel }</li>
			            <li class="new">${pageScoreAdd }</li>
			        </ul>
			    </div>
			
			
				<div style="float:left;width:5%">
					<table class="list" width="100%">
						<thead align="center">
							<tr>
								<th nowrap>序号</th>
							</tr>
						</thead>
						<tbody id="tbody_index"></tbody>
					</table>
					
		   		</div>
			
			    <div  style="float:left;width:40%">
			        <table class="list" width="100%">
			            <thead align="center">
			            <tr>
			                <th nowrap>源字段名</th>
							<th nowrap>源字段语句描述</th>
							<th nowrap>字段类型</th>	
							<th nowrap width="10%">操作</th>
			            </tr>
			            </thead>
			            <tbody id="tbody_source_field" align="center" class="sortDrag sourcetable">
				            <c:forEach items="${object.mapInfoDetails }" var="field">
			                    <%-- <c:if test="${not empty field.sourceFieldName }"> --%>
					                <tr columnNo="${field.cid.columnNo }" type='fieldSource'>
					                    <td nowrap width="13%" title="${field.sourceFieldName }">${field.sourceFieldName }</td>
					                    <td nowrap width="15%" title="${field.sourceFieldSentence }">${field.sourceFieldSentence }</td>
					                    <td nowrap width="10%">${field.sourceFieldType }</td>
					                    
					                    	<td align="center">
						                    	<a class="edit" href=""
					               						target="dialog" callback="javascript:new ExportSql().pubfuns.exportFieldCallback(this, 'formField');" rel="destFieldForm" width="530" height="430" resizable="false" maxable="false" title="编辑数据交换字段"><span class="icon icon-compose"></span></a>
					                            <a class="delete" href="javascript:;"><span class="icon icon-trash"></span></a>
						                    </td>
					                </tr>
			                    <%-- </c:if> --%>
				            </c:forEach>
			            </tbody>
			        </table>
			    </div>
			    <div style="float:left;width:55%">
			        <table class="list" width="100%">
			            <thead align="center">
			            <tr>
			                <th nowrap>目标字段名</th>
							<th nowrap>字段类型</th>
							<th nowrap width="8%">主键</th>
							<th nowrap width="8%">允许空</th>
							<th nowrap>常量</th>
							<th nowrap width="8%">操作</th>
			            </tr>
			            </thead>
			            <tbody id="tbody_dest_field" align="center" class="sortDrag sourcetable">
				            <c:forEach items="${object.mapInfoDetails }" var="field">
			                    <%-- <c:if test="${not empty field.destFieldName }"> --%>
					                <tr columnNo="${field.cid.columnNo }" type='fieldDest'>
					                    <td nowrap width="13%" title="${field.destFieldName }">${field.destFieldName }</td>
					                    <td nowrap width="15%">${field.destFieldType }</td>
					                    <td nowrap width="10%">
					                    	<c:choose>
					                    		<c:when test="${'1' eq field.isPk }">是</c:when>
					                    		<%--<c:when test="${empty field.isPk }"></c:when>--%>
					                    		<c:otherwise>否</c:otherwise>
					                    	</c:choose>
					                    </td>
					                    <td nowrap width="10%">
					                    	<c:choose>
					                    		<c:when test="${'1' eq field.isNull }">是</c:when>
					                    		<%--<c:when test="${empty field.isNull }"></c:when>--%>
					                    		<c:otherwise>否</c:otherwise>
					                    	</c:choose>
					                    </td>
					                    <td nowrap width="10%">${field.destFieldDefault }</td>
						                    <td align="center">
						                            <a class="edit" href=""
					               						target="dialog" callback="javascript:new ExportSql().pubfuns.exportFieldCallback(this, 'formField');" rel="destFieldForm" width="530" height="430" resizable="false" maxable="false" title="编辑数据交换字段"><span class="icon icon-compose"></span></a>
						
						                            <a class="delete" href="javascript:;"><span class="icon icon-trash"></span></a>
						                        
						                    </td>
					                </tr>
			                    <%-- </c:if> --%>
				            </c:forEach>
			            </tbody>
			        </table>
			    </div>
			
			
			    <div class="panelBar" style="float:left; width:100%;">
			        <ul class="toolBar">
			        	<li class="delete" style="float: right;">${pageScoreDel }</li>
			            <li class="new">${pageScoreAdd }</li>
			        </ul>
			    </div>
				
	
			</div>
			
			<%-- 数据导出触发器 --%>
			<div>
				<div>
					<div class="panelBar">
				        <ul class="toolBar">
                            <span style="color: green;">可拖动触发器数据列更换触发器执行顺序</span>
				            <c:set var="pageScoreAdd">
				            	<a class="add" href="${pageContext.request.contextPath }/dde/exchangeMapinfoNew!formTrigger.do?exportId=${object }"
                                   target="dialog" rel="detailForm" title="新增数据导出内容字段" height="600" width="680" resizable="false" maxable="false"><span>新增</span></a>
				            </c:set>
				            <li class="new">${pageScoreAdd }</li>
				        </ul>
				    </div>
				
				    <div layoutH=".pageHeader ${pageHeaderHeight }">
				        <table class="list" style="min-width: 1000px; width: 100%">
				            <thead align="center">
				            <tr>
				                <th width="40%">执行语句</th>
				                <th width="10%">执行对象</th>
				                <th width="10%">触发类别</th>
				                <th width="10%">触发时机</th>
				                <th width="10%">触发器顺序</th>
				                <th width="10%">无参数存储过程</th>
				                <th>操作</th>
				            </tr>
				            </thead>
				            <tbody id="tbody_export_trigger" align="center" class="sortDrag sourcetable">
				            	<c:forEach items="${object.mapInfoTriggers }" var="trigger">
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
					                    		<c:when test="${'S' eq trigger.triggerDatabase }">数据源</c:when>
					                    		<c:when test="${'D' eq trigger.triggerDatabase }">数据目标</c:when>
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
				                            <a class="edit" href="javascript:;" target="dialog" callback="javascript:new ExportSql().pubfuns.exportFieldCallback(this, 'formTrigger');" rel="exchange_trigger" height="600" width="680" resizable="false" maxable="false" title="编辑数据导出触发器"><span class="icon icon-compose"></span></a>
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

<script src="${pageContext.request.contextPath }/scripts/module/dde/exchange/exchange.js"></script>
<script src="${pageContext.request.contextPath }/scripts/module/dde/public.js"></script>
<script>

	var exchange = new ExportSql();
	exchange.init();
	exchange.pubfuns.updateIndex();
	
</script>