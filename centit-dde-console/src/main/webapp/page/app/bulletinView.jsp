<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/css.jsp"%> 
<%@ include file="/page/common/messages.jsp"%>
<link href="${pageContext.request.contextPath}/themes/default/style.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="${pageContext.request.contextPath}/themes/default/style-custom.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="${pageContext.request.contextPath}/themes/css/core.css" rel="stylesheet" type="text/css" media="screen" />
<link href="${pageContext.request.contextPath}/themes/css/core-custom.css" rel="stylesheet" type="text/css" media="screen" />

<h2 class="contentTitle">查看公告</h2>

<style type="text/css">
	fieldset span {
		margin:5px 5px 0 10px;
		width:60px;
		line-height:12px;
	}
	
	fieldset span p{
		float:right;
	}
</style>

<div class="tabsContent" layoutH="100">

	<%-- <div class="tableList" style="clear: both">
	<table class="list" width="100%">
		
			<tr>
				<td width="80px">发送人：</td>
                <td>${cp:MAPVALUE("usercode",object.sender)}</td>
			</tr>
			<tr>
				<td width="80px">发送时间</td>
                <td><s:property value="%{senddate}" /></td>
			</tr>
			<tr>
				<td width="80px">标题</td>
                <td><s:property value="%{msgtitle}" /></td>
			</tr>
			
			<tr>
				<td width="80px">附件：</td>
                <td><c:forEach var="file" items="${fileList}" varStatus="status">
							&emsp;&emsp;<a href="./fileinfo!download2.do?object.fileCode=${file.fileCode}">${file.fileName}</a>
							<c:if test="${(status.index + 1) % 3 == 0}">
								<br>
							</c:if>
						</c:forEach></td>
			</tr>
			

	</table>
</div> --%>

	<form
		action="${pageContext.request.contextPath }/app/Innermsg!viewBulletin.do"
		method="post" id="innermsgForm">
		<input type="hidden" name="msgcode" value="${msgcode }">
		<table style="width: 80%" border="0" cellpadding="1" cellspacing="1">
			<tr>
				<td width="10%">
					<fieldset style="border: hidden 1px #B8D0D6;">
						<span><p>发送人:</p></span> ${cp:MAPVALUE("usercode",object.sender)}<br>
						<span><p>发送时间:</p></span>${object.senddate}
						
						<br>
						<%-- <span><p>接受人:</p></span>
							${cp:MAPVALUE("unitcode",object.receive)}
						<br> --%>
						<span><p>标题:</p></span>${object.msgtitle}

						<br> <span><p>附件:</p></span><br>
						<c:forEach var="file" items="${fileList}" varStatus="status">
							&emsp;&emsp;
							<a
								href="./fileinfo!download2.do?object.fileCode=${file.fileCode}">${file.fileName}</a>
							<c:if test="${(status.index + 1) % 3 == 0}">
								<br>
							</c:if>
						</c:forEach>
					</fieldset> <br>
					<fieldset style="border: hidden 1px #B8D0D6;">
						<legend>公告内容 </legend>
						<div
							style="width: 100%; height: 200px; OVERFLOW-y: auto; padding: 10px;">
							${msgcontent}</div>
					</fieldset>

				</td>
			</tr>
		</table>
	</form>
</div>
<%-- <center>
<br><br><br><br>
<p class="ctitle"><b><h3><s:property value="%{msgtitle}" /></h3></b></p>
<%@ include file="/page/common/messages.jsp"%><s:form action="innermsg"  method="post" namespace="/app" id="innermsgForm" >
<input type="hidden" name="msgcode" value="${msgcode }">
<table style="width:80%" border="0" cellpadding="1" cellspacing="1">		
		<tr>
					<td width="10%">
					<fieldset style="border: hidden 1px #B8D0D6; ">
						<span><p>发送人:</p></span>
						${cp:MAPVALUE("usercode",object.sender)}<br>
						<span><p>发送时间:</p></span>
						<s:property value="%{senddate}" /><br>
						<span><p>接受人:</p></span>
							${cp:MAPVALUE("unitcode",object.receive)}
						<br>
						<span><p>标题:</p></span>
						<s:property value="%{msgtitle}" />
						<br>
						<span><p>附件:</p></span><br>
						<c:forEach var="file" items="${fileList}" varStatus="status">
							&emsp;&emsp;<a href="./fileinfo!download2.do?object.fileCode=${file.fileCode}">${file.fileName}</a>
							<c:if test="${(status.index + 1) % 3 == 0}">
								<br>
							</c:if>
						</c:forEach>
					</fieldset>
				<br>
					<fieldset style="border: hidden 1px #B8D0D6;">
						<legend>消息内容	</legend>
						<div style="width:100%; height:200px;OVERFLOW-y:auto; padding:10px;">
						${msgcontent}
						</div>
					</fieldset>
					
					<!--<c:forEach var="reply" items="${replyList}" varStatus="status">
					<fieldset style="border: hidden 1px #000000;">
					<legend>回复人${status.index + 1}:${cp:MAPVALUE("usercode",reply.sender)},回复时间${reply. senddate}</legend>
					<br>
					<br>
					${reply. msgcontent}
					</fieldset>
					</c:forEach>
				--></td>
		</tr>
</table>
<br>
<!-- <input type="button"  value="返回" class="btn"  onclick="window.history.back()"/>&emsp; -->
</s:form>
</center>
</body>
</html>
 --%>