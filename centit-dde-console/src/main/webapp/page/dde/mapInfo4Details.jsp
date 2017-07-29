<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<!-- <style>
 td {height:32px} 
</style> -->

<div class="pageContent">
	<%-- <div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${contextPath }/dde/mapInfoDetail!add.do" rel="mapinfoDetailAdd" target='dialog' width="500" height="350"><span>新增</span></a></li>
			<li><a class="delete" onclick="deletetbale();"><span>删除</span></a></li>
			<li><a class="edit" warn="请选择一个用户" href="${contextPath }/dde/mapInfoDetail!edit.do?s_columnNo={pkSource}&s_mapinfoId=${param['s_mapinfoId']}" rel="mapinfoDetailEdit" target='dialog' width="500" height="350"><span>编辑</span></a></li>
			<li><a class="icon" onclick="myFunction();"><span>保存</span></a></li>
			<li class="line">line</li>
		</ul>
	</div> --%>
	
	<c:set var="mapinfoDetailcaozuo">
        <div class="panelBar">
            <ul class="toolBar">
                <li class="new"> 
                  <a class="add" href="${contextPath }/dde/mapInfoDetail!add.do" rel="mapinfoDetailAdd" target='dialog' width="500" height="350"><span>新增</span></a>
                  <a class="edit" onclick="myFunction1();"><span>保存</span></a>
                  <a class="delete" onclick="deletetbale();"><span>删除整行</span></a>
                </li>
            </ul>
        </div>
     </c:set>
     
     ${mapinfoDetailcaozuo}

	<div layoutH="110" id="r_list_print">
		<form id="mapinfoDetailForm" action="${pageContext.request.contextPath}/dde/mapInfoDetail!showMapinfoDetail.do"
		    method="post" onsubmit="return navTabSearch(this)">
	        <input type="hidden" name="s_goalTableName" value="${s_goalTableName}"/>
	        <input type="hidden" name="s_mapinfoId" value="${param['s_mapinfoId']}"/>
	        <input type="hidden" name="s_soueceTableName" value="${s_soueceTableName }"/>
	        <div>
	        <div style="float:left;width:5%">
					<table class="list" width="100%">
						<thead align="center">
							<tr>
								<th nowrap>序号</th>
							</tr>
						</thead>
						<tbody>
						  <c:forEach items="${LENGTH}" var="obj" varStatus="s">
						     <tr>
						        <td  nowrap class="notMove" align="center">${s.index+1}</td>
						     </tr>
						  </c:forEach>
						</tbody>
					</table>
					
		    </div>
	        <div style="float:left;width:38%">
	        <table class="list" width="100%">
			<thead align="center">
				<tr>
					
					<th nowrap>源字段名</th>
					<th nowrap>源字段语句描述</th>
					<th nowrap>字段类型</th>	
					<th nowrap width="8%">操作</th>									
				</tr>
			</thead>
			<tbody align="center" class="sortDrag sourcetable">
				<c:forEach items="${LENGTH}" var="obj" varStatus="s">
						<tr target="pkSource" rel="${s.index+1}"  class="lefttr">							
														
								<td nowrap title="${TABLESTRUCT[0][s.index].COLUMNNAME}">${TABLESTRUCT[0][s.index].COLUMNNAME}&nbsp;
								<!-- <INPUT class=editGoalColumnName value=ENTERPRISEID type=hidden name=GoalColumnName> -->
								</td>
								<td nowrap title="${TABLESTRUCT[0][s.index].SOURCECOLUMNSENTENCE}">${TABLESTRUCT[0][s.index].SOURCECOLUMNSENTENCE}&nbsp;
								    <input type="hidden" name="sourceColumnName" class="editSourceColumnName" value="${TABLESTRUCT[0][s.index].COLUMNNAME}"/>
								    <input type="hidden" name="SourceColumnSentence" class="editSourceColumnSentence" value="${TABLESTRUCT[0][s.index].SOURCECOLUMNSENTENCE}"/>
								</td>				
								<td nowrap title="${TABLESTRUCT[0][s.index].COLUMNTYPE}">${TABLESTRUCT[0][s.index].COLUMNTYPE}&nbsp;<input type="hidden" name="SourceColumnType" class="editSourceColumnType" value="${TABLESTRUCT[0][s.index].COLUMNTYPE}"/></td>
							    <td>
							       <a onclick="" class="delete"><span class="icon icon-trash"></span></a>
							    </td>
						</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
		<div style="float:left;width:52%">
		  <table class="list" width="100%">
			<thead align="center">
				<tr>
					<th nowrap>目标字段名</th>
					<th nowrap>字段类型</th>
					<th nowrap width="8%">是否为主键</th>
					<th nowrap width="8%">允许空</th>
					<th nowrap>常量</th>	
					<th nowrap width="8%">操作</th>						
				</tr>
			</thead>
			<tbody align="center" class="sortDrag goaltable">
				<c:forEach items="${LENGTH}" var="obj" varStatus="s">
						<tr target="pkGoal" rel="${s.index+1}">
								<td nowrap title="${TABLESTRUCT[1][s.index].COLUMNNAME}">${TABLESTRUCT[1][s.index].COLUMNNAME}&nbsp;<input type="hidden" name="GoalColumnName" class="editGoalColumnName" value="${TABLESTRUCT[1][s.index].COLUMNNAME}"/></td>
							
								<td nowrap title="${TABLESTRUCT[1][s.index].COLUMNTYPE}">${TABLESTRUCT[1][s.index].COLUMNTYPE}<input type="hidden" name="GoalColumnType" class="editGoalColumnType" value="${TABLESTRUCT[1][s.index].COLUMNTYPE}"/> </td>
							
							    <td nowrap><c:if test="${TABLESTRUCT[1][s.index].ISPK eq '0'}">否</c:if> 
							        <c:if test="${TABLESTRUCT[1][s.index].ISPK eq '1'}">是</c:if>
							        <input type="hidden" name="GoalisPk" class="editGoalisPk" value="${TABLESTRUCT[1][s.index].ISPK}"/>
							    </td>
							    
								<td nowrap><c:if test="${TABLESTRUCT[1][s.index].ISNULLABLE eq '0'}">否</c:if> 
							        <c:if test="${TABLESTRUCT[1][s.index].ISNULLABLE eq '1'}">是</c:if>
							        <input type="hidden" name="GoalisNullable" class="editGoalisNullable" value="${TABLESTRUCT[1][s.index].ISNULLABLE}"/>
							    </td>
							
								<td nowrap>${TABLESTRUCT[1][s.index].DESTFIELDDEFAULT}<input type="hidden" name="GoalFieldDefault" class="editGoalFieldDefault" value="${TABLESTRUCT[1][s.index].DESTFIELDDEFAULT}"/></td>
							
							    <td>
							       <a onclick="" class="delete"><span class="icon icon-trash"></span></a>
							       <a href="${contextPath }/dde/mapInfoDetail!edit.do?s_columnNo=${s.index+1}&s_mapinfoId=${param['s_mapinfoId']}"
							          rel="mapinfoDetailEdit" target='dialog' width="500" height="350"><span class="icon icon-edit"></span></a>
							    </td>
						</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	    </div>
		</form>
	</div>
</div>
<script type="text/javascript">
	function myFunction1(){
		//debugger;
		var mapInfoName = $("#mapinfoName_edit").val();
		var isRepeat = $(".isRepeat_edit:checked").val();
		var mapInfoDesc = $("#mapinfoDesc_edit").val();
		/* var tableOperate = $("#tableOperate").val(); */
		var recordOperate = $(".recordOperate_edit:checked").val();
		
		var mapinfoNameElement = DWZ.frag["INPUTHIDDEN"]
		.replaceAll('{name}', 'mapInfoName')
		.replaceAll('{value}', mapInfoName);
		var isRepeatElement = DWZ.frag["INPUTHIDDEN"]
		.replaceAll('{name}', 'isRepeat')
		.replaceAll('{value}', isRepeat);
		var mapinfoDescElement = DWZ.frag["INPUTHIDDEN"]
		.replaceAll('{name}', 'mapInfoDesc')
		.replaceAll('{value}', mapInfoDesc);
		var recordOperateElement = DWZ.frag["INPUTHIDDEN"]
		.replaceAll('{name}', 'recordOperate')
		.replaceAll('{value}', recordOperate);
		
		$("#mapinfoDetailForm").append(mapinfoNameElement);
		$("#mapinfoDetailForm").append(isRepeatElement);
		$("#mapinfoDetailForm").append(mapinfoDescElement);
		$("#mapinfoDetailForm").append(recordOperateElement);
		
		$("#mapinfoDetailForm").attr("action","${pageContext.request.contextPath}/dde/mapInfoDetail!showMapinfoDetail.do?s_method=save&s_type=save").submit();
	}
	
	$("#mapinfoDetailForm a[class='delete']").click(function(event){
		$(this).parents("tr").remove();
	});
	
	function deletetbale(){
		var $mapinfoDetailForm = $("#mapinfoDetailForm");
		//debugger;
		var $select = $mapinfoDetailForm.find("tr.selected"); 
		
		if(0 == $select.size()) {
			return;
		}
		
		var index = 0;
		var $tbody = $mapinfoDetailForm.find('table>tbody>tr');
		$.each($tbody, function(i, object){
			if($(object).hasClass('selected')) {
				index = i;
				return false;
			}
		});
		
		
		var $selecttr = $mapinfoDetailForm.find('table>tbody');
		
		$.each($selecttr, function(i, objects){
			var $object = $(objects).find('tr:eq('+(index)+')');
			
			$object.remove();
		});
		
	}
	
	function addMapinfoDetail(){
		$("tr.selected").remove();
		$("#mapinfoDetailForm").attr("action","${pageContext.request.contextPath}/dde/mapInfoDetail!showMapinfoDetail.do?s_method=save&s_type=save").submit();
	}
	
	$(function() {
		var options = {
				cursor: 'move', // selector 的鼠标手势
				sortBoxs: 'tbody.sortDrag', //拖动排序项父容器
				replace: true, //2个sortBox之间拖动替换
				items: '> tr', //拖动排序项选择器
				selector: 'td:first:not(.notMove)', //拖动排序项用于拖动的子元素的选择器，为空时等于item
				zIndex: 1000
			};
		$('tbody', navTab.getCurrentPanel()).sortDrag(options);
	}); 

</script>
