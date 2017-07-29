<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="panelBar">
	<div class="pages">
		<span>显示</span> <select name="numPerPage" class="combox" onchange="navTabPageBreak({numPerPage:this.value} )">

			<c:forEach var="per" items="20,50,100,200">
				<option value="${per }" <c:if test='${per eq pageDesc.pageSize }'> selected="selected"</c:if>>${per }</option>
			</c:forEach>
		</select> <span>条，共${empty pageDesc.totalRows ? 0 : pageDesc.totalRows }条</span>
	</div>
	
		
		
		
		<div class="newStyle" style="float:right;">
			${panel_buttons }
		</div>
		<div class="pagination" targetType="navTab" totalCount="${pageDesc.totalRows }" numPerPage="${pageDesc.pageSize }" pageNumShown="10" currentPage="${pageDesc.pageNo }"></div>
</div>