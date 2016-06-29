<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageContent">
	<%-- <div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${contextPath }/dde/mapinfoTrigger!add.do?mapinfoId={s_mapinfoId}"  target='dialog' width="500" height="350"><span>添加</span></a></li>
			<li><a class="edit" href="${contextPath }/dde/mapinfoTrigger!edit.do?mapinfoId={s_mapinfoId}&triggerId={pk}" warn="请选择一条记录" target='dialog' width="500" height="350"><span>编辑</span></a></li>
			<li><a class="delete" href="${contextPath }/dde/mapinfoTrigger!delete.do?mapinfoId={s_mapinfoId}&triggerId={pk}" warn="请选择一条记录" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
		</ul>
	</div> --%>
	
	<c:set var="addtrigger1">
        <div class="panelBar">
            <ul class="toolBar">
                <li class="new">
                  <a class="add" href="${contextPath }/dde/mapinfoTrigger!add.do?mapinfoId={s_mapinfoId}"  
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
				<c:forEach items="${objList }" var="mapinfoTrigger" varStatus="s">
						<tr target="pk" rel="${mapinfoTrigger.triggerId}">
							
							    <td>${s.index+1}</td>
								<%-- <td>${mapinfoTrigger.mapinfoId}</td>
							
								<td>${mapinfoTrigger.triggerId}</td> --%>
							
								<td title="${mapinfoTrigger.triggerSql}">${mapinfoTrigger.triggerSql}</td>
							
								<td title="${mapinfoTrigger.triggerDesc}">${mapinfoTrigger.triggerDesc}</td>
							
								<td>
								  <c:if test="${mapinfoTrigger.triggerType eq 'L'}">行触发器</c:if>
								  <c:if test="${mapinfoTrigger.triggerType eq 'T'}">表触发器</c:if>
								</td>
							
								<td>
								  <c:if test="${mapinfoTrigger.triggerTime eq 'B'}">交换前</c:if>
								  <c:if test="${mapinfoTrigger.triggerTime eq 'A'}">交换后 </c:if>
								  <c:if test="${mapinfoTrigger.triggerTime eq 'E'}">交换失败后</c:if>
								</td>
							
								<td>
								  <c:if test="${mapinfoTrigger.triggerDatabase eq 'S'}">数据源</c:if>
								  <c:if test="${mapinfoTrigger.triggerDatabase eq 'D'}">数据目标</c:if>
								</td>
							
								<td title=" ${mapinfoTrigger.tiggerOrder} ">
								  ${mapinfoTrigger.tiggerOrder} 
								</td>
								<td>
								   <a href="${contextPath }/dde/mapinfoTrigger!edit.do?mapinfoId={s_mapinfoId}&triggerId=${mapinfoTrigger.triggerId}" 
								      target='dialog' width="550" height="450" title="编辑触发器"><span class="icon icon-edit"></span></a>
								   <a href="${contextPath }/dde/mapinfoTrigger!delete_add.do?mapinfoId={s_mapinfoId}&triggerId=${mapinfoTrigger.triggerId}" 
						              target="ajaxTodo" title="删除触发器"><span class="icon icon-trash"></span></a>	
								</td>
							
						</tr>
				</c:forEach>
			</tbody>
		</table>
	</form>
	</div>
</div>