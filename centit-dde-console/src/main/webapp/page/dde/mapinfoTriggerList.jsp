<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageContent">
	<%-- <div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${contextPath }/dde/mapInfoTrigger!add.do?mapInfoId={s_mapinfoId}"  target='dialog' width="500" height="350"><span>添加</span></a></li>
			<li><a class="edit" href="${contextPath }/dde/mapInfoTrigger!edit.do?mapInfoId={s_mapinfoId}&triggerId={pk}" warn="请选择一条记录" target='dialog' width="500" height="350"><span>编辑</span></a></li>
			<li><a class="delete" href="${contextPath }/dde/mapInfoTrigger!delete.do?mapInfoId={s_mapinfoId}&triggerId={pk}" warn="请选择一条记录" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
		</ul>
	</div> --%>
	
	<c:set var="addtrigger1">
        <div class="panelBar">
            <ul class="toolBar">
                <li class="new">
                  <a class="add" href="${contextPath }/dde/mapInfoTrigger!add.do?mapInfoId={s_mapinfoId}"
                     target='dialog' width="550" height="450"><span>添加触发器</span></a>                   
                </li>
            </ul>
        </div>
    </c:set>
    
    ${addtrigger1}

	<div layoutH="55">
	<form>
	<input type="hidden" name="s_mapinfoId" value="${param['s_mapinfoId']}" id="s_mapinfoId"/>
	
		<table class="list" width="98%" targetType="navTab" asc="asc" desc="desc">
			<thead align="center">
				<tr>
					<th>序号</th>
					<th>执行语句</th>
					<th>执行语句说明</th>
					<th>触发类别</th>
					<th>触发时机</th>
					<th>执行对象</th>
					<th>触发器顺序</th>
					<th>操作</th>									
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach items="${objList }" var="mapInfoTrigger" varStatus="s">
						<tr target="pk" rel="${mapInfoTrigger.triggerId}">
							
							    <td>${s.index+1}</td>
								<%-- <td>${mapInfoTrigger.mapInfoId}</td>
							
								<td>${mapInfoTrigger.triggerId}</td> --%>
							
								<td title="${mapInfoTrigger.triggerSql}">${mapInfoTrigger.triggerSql}</td>
							
								<td title="${mapInfoTrigger.triggerDesc}">${mapInfoTrigger.triggerDesc}</td>
							
								<td>
								  <c:if test="${mapInfoTrigger.triggerType eq 'L'}">行触发器</c:if>
								  <c:if test="${mapInfoTrigger.triggerType eq 'T'}">表触发器</c:if>
								</td>
							
								<td>
								  <c:if test="${mapInfoTrigger.triggerTime eq 'B'}">交换前</c:if>
								  <c:if test="${mapInfoTrigger.triggerTime eq 'A'}">交换后 </c:if>
								  <c:if test="${mapInfoTrigger.triggerTime eq 'E'}">交换失败后</c:if>
								</td>
							
								<td>
								  <c:if test="${mapInfoTrigger.triggerDatabase eq 'S'}">数据源</c:if>
								  <c:if test="${mapInfoTrigger.triggerDatabase eq 'D'}">数据目标</c:if>
								</td>
							
								<td title=" ${mapInfoTrigger.tiggerOrder} ">
								  ${mapInfoTrigger.tiggerOrder}
								</td>
								<td>
								   <a href="${contextPath }/dde/mapInfoTrigger!edit.do?mapInfoId={s_mapinfoId}&triggerId=${mapInfoTrigger.triggerId}"
								      target='dialog' width="550" height="450" title="编辑触发器"><span class="icon icon-edit"></span></a>
								   <a href="${contextPath }/dde/mapInfoTrigger!delete_add.do?mapInfoId={s_mapinfoId}&triggerId=${mapInfoTrigger.triggerId}"
						              target="ajaxTodo" title="删除触发器"><span class="icon icon-trash"></span></a>	
								</td>
							
						</tr>
				</c:forEach>
			</tbody>
		</table>
	</form>
	</div>
</div>