<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%> 
<script src="${pageContext.request.contextPath}/scripts/centitui/jquery-1.7.1.js" type="text/javascript"></script>
<link href="${pageContext.request.contextPath}/themes/default/style.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="${pageContext.request.contextPath}/themes/default/style-custom.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="${pageContext.request.contextPath}/themes/css/core.css" rel="stylesheet" type="text/css" media="screen" />
<link href="${pageContext.request.contextPath}/themes/css/core-custom.css" rel="stylesheet" type="text/css" media="screen" />

<script type="text/javascript">
	function msgView(uri) {
	 		window.parent.frames["bulletinView"].document.location=uri;
	}

	function orgType(type) {
		document.getElementById("receivetype").value = 'O';
		document.getElementById("isReveive").value = type;
		document.innermsg.submit();
	}
</script>




<div  class="pageHeader" align="left">
 <form action="${pageContext.request.contextPath }/app/innermsg!bulletinList.do" 
        method="post" onsubmit="return navTabSearch(this)">
	 	&emsp;
	 	<c:forEach var="user" items="${USER}">
		<c:if test="${user eq '1' }">
				<div class="buttonActive" style="margin-right: 10px;">
					<div class="buttonContent">
						<a class="buttonActive"
							onclick="msgView('<%=request.getContextPath()%>/app/innermsg!bulletinEdit.do')">
							<span>新建公告</span></a>
					</div>
				</div>
				<br>
		<br>
		&emsp;<a href="javascript:orgType('S');">公告发件箱</a>		
		</c:if>
		<c:if test="${user eq '0' }">
	 	&emsp;<a href="javascript:orgType('R');">公告收件箱</a>   
		</c:if>
		</c:forEach>
	 	<input type="hidden" name="receivetype" value=""/>
	 	<input type="hidden" name="isReveive" />	
</form><br></div>
<div>
	<div id="bullettintable" class="tableList" style="float: left; ">
		<table id="bulletinintable" class="list" width="100%">
			<thead align="center">
				<tr>
					<th width='70'>标题</th>
					<th width='120'>发送时间</th>
					<th width='120'>收件人</th>
					<th width='80'>操作</th>
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach var="innermsg" items="${objList }">
					<tr>
						<td>${innermsg.msgtitle}</td>
						<td>${innermsg.senddate}</td>
						<td><c:if test="${innermsg.receivetype eq 'O' }">
						${cp:MAPVALUE("unitcode",innermsg.receive)}
					</c:if> <c:if test="${innermsg.receivetype eq 'P' }">
						${cp:MAPVALUE("usercode",innermsg.receive)}
					</c:if></td>
						<td><a
							href="javascript:msgView('<%=request.getContextPath()%>/app/innermsg!viewBulletin.do?msgcode=${innermsg.msgcode}')">查看</a>
							<c:forEach var="user" items="${USER}">
								<c:if test="${user eq '1' }">
									<a
										href='app/innermsg!deleteBulletin.do?msgcode=${innermsg.msgcode}&isReveive=${isReveive}'
										onclick='return confirm("删除公告？");'>删除</a>
								</c:if>
							</c:forEach></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
				<option value="20">20</option>
				<option value="50">50</option>
				<option value="100">100</option>
				<option value="200">200</option>
			</select>
			<span>条，共${totals}条</span>
			
		</div>
		
		<div class="pagination" targetType="navTab" totalCount="200" numPerPage="20" pageNumShown="10" 
		currentPage="1"></div>
</div>

		

<script type="text/javascript">
		var a = $('table a:first')[0];
		if (a) {
			eval(a.href);
		}
</script>
		
