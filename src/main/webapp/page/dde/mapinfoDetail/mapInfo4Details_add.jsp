<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageContent">
	<%-- <div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${contextPath }/dde/mapInfoDetail!add_add.do" rel="mapinfoDetailAdd" target='dialog' width="500" height="350"><span>新增</span></a></li>
			<li><a class="icon" onclick="myFunction();"><span>保存</span></a></li>
		</ul>
	</div> --%>
	
	<c:set var="addandsavemapinfoDetail">
        <div class="panelBar">
            <ul class="toolBar">
                <li class="new"> 
                  <a class="add" href="${contextPath }/dde/mapInfoDetail!add_add.do" rel="mapinfoDetailAdd" target='dialog' width="500" height="350"><span>新增</span></a>
                  <a class="edit" onclick="myFunction();"><span>保存</span></a>
                  <a class="delete" onclick="deletetbale();"><span>删除整行</span></a>
                </li>
            </ul>
        </div>
     </c:set>

   ${addandsavemapinfoDetail}
   
	<div layoutH="110" id="r_list_print">
		<form id="addmapinfoDetailForm" action="${pageContext.request.contextPath}/dde/mapInfoDetail!addAndsaveMapinfoDatails.do"
		    method="post" onsubmit="return navTabSearch(this)">
	        <input type="hidden" name="s_goalTableName" value="${s_goalTableName}"/>
	        <input type="hidden" name="s_mapinfoId" value="${param['s_mapinfoId']}"/>
	        <input type="hidden" name="s_soueceTableName" value="${s_soueceTableName }"/>
	        <input type="hidden" name="s_sourcedatabaseName" value="${s_sourcedatabaseName }"/>
	        <input type="hidden" name="s_goaldatabaseName" value="${s_goaldatabaseName }"/>
	        <div>
	        <div style="float:left;width:42%">
	        <table class="list" width="100%">
			<thead align="center">
				<tr>
					<th nowrap>序号</th>
					<th nowrap>源字段名</th>
					<th nowrap>源字段语句描述</th>
					<th nowrap>字段类型</th>	
					<th>操作</th>								
				</tr>
			</thead>
			<tbody align="center" class="sortDrag sourcetable">
				<c:forEach items="${LENGTH}" var="obj" varStatus="s">
						<tr  target="pkSource" rel="${s.index+1}">							
								<td nowrap>${s.index+1}</td>						
								<td nowrap title=${TABLESTRUCT[0][s.index].COLUMNNAME}>${TABLESTRUCT[0][s.index].COLUMNNAME}
								<td nowrap title=${TABLESTRUCT[0][s.index].SOURCECOLUMNSENTENCE}>${TABLESTRUCT[0][s.index].SOURCECOLUMNSENTENCE}
								    <input type="hidden" name="sourceColumnName" class="editSourceColumnName" value="${TABLESTRUCT[0][s.index].COLUMNNAME}"/>
								    <input type="hidden" name="SourceColumnSentence" class="editSourceColumnSentence" value="${TABLESTRUCT[0][s.index].SOURCECOLUMNSENTENCE}"/>
								</td>				
								<td nowrap title=${TABLESTRUCT[0][s.index].COLUMNTYPE}>${TABLESTRUCT[0][s.index].COLUMNTYPE}<input type="hidden" name="SourceColumnType" class="editSourceColumnType" value="${TABLESTRUCT[0][s.index].COLUMNTYPE}"/></td>
							    <td nowrap><a onclick="" class="delete"><span class="icon icon-trash"></span></a></td>
						</tr>
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
					<th nowrap width="15%">是否为主键</th>
					<th nowrap width="15%">允许空</th>
					<th nowrap>常量</th>	
					<th nowrap>操作</th>					
				</tr>
			</thead>
			<tbody align="center" class="sortDrag goaltable">
				<c:forEach items="${LENGTH}" var="obj" varStatus="s">
						<tr target="pkGoal" rel="${s.index+1}">
								<td nowrap title=${TABLESTRUCT[1][s.index].COLUMNNAME}>${TABLESTRUCT[1][s.index].COLUMNNAME}<input type="hidden" name="GoalColumnName" class="editGoalColumnName" value="${TABLESTRUCT[1][s.index].COLUMNNAME}"/></td>
							
								<td nowrap title=${TABLESTRUCT[1][s.index].COLUMNTYPE}>${TABLESTRUCT[1][s.index].COLUMNTYPE}<input type="hidden" name="GoalColumnType" class="editGoalColumnType" value="${TABLESTRUCT[1][s.index].COLUMNTYPE}"/> </td>
							
							    <td nowrap><c:if test="${TABLESTRUCT[1][s.index].ISPK eq '0'}">否</c:if> 
							        <c:if test="${TABLESTRUCT[1][s.index].ISPK eq '1'}">是</c:if>
							        <input type="hidden" name="GoalisPk" class="editGoalisPk" value="${TABLESTRUCT[1][s.index].ISPK}"/>
							    </td>
							    
								<td nowrap><c:if test="${TABLESTRUCT[1][s.index].ISNULLABLE eq '0'}">否</c:if> 
							        <c:if test="${TABLESTRUCT[1][s.index].ISNULLABLE eq '1'}">是</c:if>
							        <input type="hidden" name="GoalisNullable" class="editGoalisNullable" value="${TABLESTRUCT[1][s.index].ISNULLABLE}"/>
							    </td>
							
								<td nowrap>${TABLESTRUCT[1][s.index].DESTFIELDDEFAULT}<input type="hidden" name="GoalFieldDefault" class="editGoalFieldDefault" value="${TABLESTRUCT[1][s.index].DESTFIELDDEFAULT}"/></td>
							    
							    <td nowrap>
							       <a onclick="" class="delete"><span class="icon icon-trash"></span></a>
							       <a href="${contextPath }/dde/mapInfoDetail!edit_add.do?s_columnNo=${s.index+1}&s_mapinfoId=${param['s_mapinfoId']}"
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
	function myFunction(){
		//debugger;
		
		//父页面中表单信息
		var $mapinfoDetailForm = $('#mapinfoDetailForm');
		
		var mapInfoName = $("#mapInfoName").val();
		
		if('' == $.trim(mapInfoName)) {
			DWZ.ajaxDone({
				statusCode : DWZ.statusCode.error,
				message : '请填写交换名称'
			});
			
			return;
		}
		
		var isRepeat = $(".isRepeat:checked").val();
		var mapInfoDesc = $("#mapInfoDesc").val();
		/* var tableOperate = $("#tableOperate").val(); */
		var recordOperate = $(".recordOperate:checked").val();
		
		
		var mapinfoNameElement = DWZ.frag["INPUTHIDDEN"].replaceAll('{name}', 'mapInfoName').replaceAll('{value}', mapInfoName);
		var isRepeatElement = DWZ.frag["INPUTHIDDEN"].replaceAll('{name}', 'isRepeat').replaceAll('{value}', isRepeat);
		var mapinfoDescElement = DWZ.frag["INPUTHIDDEN"].replaceAll('{name}', 'mapInfoDesc').replaceAll('{value}', mapInfoDesc);
		/* var tableOperateElement = DWZ.frag["INPUTHIDDEN"]
		.replaceAll('{name}', 'tableOperate')
		.replaceAll('{value}', tableOperate); */
		var recordOperateElement = DWZ.frag["INPUTHIDDEN"].replaceAll('{name}',
				'recordOperate').replaceAll('{value}', recordOperate);

		$("#addmapinfoDetailForm").append(mapinfoNameElement);
		$("#addmapinfoDetailForm").append(isRepeatElement);
		$("#addmapinfoDetailForm").append(mapinfoDescElement);
		/* $("#addmapinfoDetailForm").append(tableOperateElement); */
		$("#addmapinfoDetailForm").append(recordOperateElement);

		$("#addmapinfoDetailForm").attr("action",
						"${pageContext.request.contextPath}/dde/mapInfoDetail!addAndsaveMapinfoDatails.do?s_method=save&s_type=save").submit();
	}

	$("#addmapinfoDetailForm a[class='delete']").click(function(event) {
		$(this).parents("tr").remove();
	});

	function deletetbale() {
		//$("tr.selected").remove();
		
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

	$(function() {
		var options = {
			cursor : 'move', // selector 的鼠标手势
			sortBoxs : 'tbody.sortDrag', //拖动排序项父容器
			replace : true, //2个sortBox之间拖动替换
			items : '> tr', //拖动排序项选择器
			selector : 'td:first', //拖动排序项用于拖动的子元素的选择器，为空时等于item
			zIndex : 1000
		};
		$('tbody', navTab.getCurrentPanel()).sortDrag(options);
	});
</script>