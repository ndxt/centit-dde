<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageFormContent dde" style="min-width:928px; width:98%; height:171px; ">
<div style="float:left">
	<dl class="nowrap">
		<dt>左（数据源）定义：</dt>
		<dd>
		    <input name="s_sourceDatabaseName" id="sourceDatabaseName" type="text" size="10"
				   value="${s_sourceDatabaseName }" readonly="readonly"/>
			<input name="s_soueceTableName" id="s_soueceTableName" type="text" size="25"
				class="datasource" value="${s_soueceTableName }" /> <a
				class="btnLook"
				href="${pageContext.request.contextPath }/dde/mapInfoDetail!defSourceData.do?s_soueceTableName=${s_soueceTableName }&s_goalTableName=${s_goalTableName}&s_mapinfoId=${param['s_mapinfoId'] }&s_sourceDatabaseName=${s_sourceDatabaseName }&s_goalDatabaseName=${s_goalDatabaseName }"
				target="dialog" rel="defSourceAndGoalData" mask="true" title="定义数据源"><span>定义数据源</span>
			</a>
		</dd>
	</dl>

	<dl class="nowrap">
		<dt>右（数据目标）定义：</dt>
		<dd>
		    <input name="s_goalDatabaseName" id="goalDatabaseName" type="text" size="10"
				   value="${s_goalDatabaseName }" readonly="readonly"/>
			<input name="s_goalTableName" id="s_goalTableName" type="text" size="25" class="datadest"
				value="${s_goalTableName}" /> <a class="btnLook"
				href="${pageContext.request.contextPath }/dde/mapInfoDetail!defDestData.do?s_goalTableName=${s_goalTableName}&s_soueceTableName=${s_soueceTableName }&s_mapinfoId=${param['s_mapinfoId'] }&s_sourceDatabaseName=${s_sourceDatabaseName }&s_goalDatabaseName=${s_goalDatabaseName }"
				target="dialog" rel="defSourceAndGoalData" mask="true"
				title="定义数据目标"><span>定义数据目标</span> </a>
		</dd>
	</dl>
	
	<dl class="nowrap">
				<dt>交换名称：</dt>
				<dd>
				<input type="text" name="mapInfoName" value="${mapInfoName}" size="40" id="mapinfoName_copy"/>
				</dd>
	</dl>
	
	<dl class="nowrap">
				<dt>交换说明：</dt>
				<dd>
				<textarea rows="3" cols="33" name="mapInfoDesc" id="mapinfoDesc_copy">${mapInfoDesc }</textarea>
				</dd>'
	</dl>
 </div>
  <div style="float:left">
   
    
    <fieldset style="width:500px;">
      <legend>记录操作</legend>
     <dl class="nowrap">
				<dd style="width:300px">
				<input type="radio" name="recordOperate" value="1" class="recordOperate_copy" <c:if test="${recordOperate eq '1'}">checked=true</c:if> />插入（insert）<br/>
				<input type="radio" name="recordOperate" value="2" class="recordOperate_copy" <c:if test="${recordOperate eq '2'}">checked=true</c:if>/>更新（update）<br/>
				<input type="radio" name="recordOperate" value="3" class="recordOperate_copy" <c:if test="${recordOperate eq '3'}">checked=true</c:if>/>合并（merge）
				</dd>
	</dl>
   </fieldset>
    <dl class="nowrap" style="display: inline;">
				<dt>是否重复执行：</dt>
				<dd>
				<input type="radio" name="isRepeat" value="1" class="isRepeat_copy" <c:if test="${isRepeat eq '1'}">checked=true</c:if> />是
				<input type="radio" name="isRepeat" value="0" class="isRepeat_copy" <c:if test="${isRepeat eq '0'}">checked=true</c:if>/>否
				</dd>
	</dl>
  </div>
</div>

<div class="pageContent" style="min-width:928px; width:100%;">
    <div class="tabs tabsExtra">
		<div class="tabsHeader">
			<div class="tabsHeaderContent">
				<ul>
					<li><a id="mapInfo4Details_herf" href="${pageContext.request.contextPath}/dde/mapInfoDetail!showMapinfoDetail.do?s_soueceTableName=${s_soueceTableName }&s_goalTableName=${s_goalTableName}&s_mapinfoId=${param['s_mapinfoId'] }&s_type=initcopy4" target="ajax" rel="jbsxBox"><span>对应关系</span></a></li>
					<li><a href="${pageContext.request.contextPath }/dde/mapInfoTrigger!list.do?s_mapinfoId=${param['s_mapinfoId'] }" target="ajax" rel="jbsxBox"><span>触发器</span></a></li>
				</ul>
			</div>
		</div>
		<div class="tabsContentAjax">
			<div class="unitBox" id="jbsxBox">
				<%@ include file="/page/dde/copyMapInfo4Details.jsp"%>
			</div>
		</div>
		<div class="tabsFooter">
			<div class="tabsFooterContent"></div>
		</div>
	</div>
</div>

