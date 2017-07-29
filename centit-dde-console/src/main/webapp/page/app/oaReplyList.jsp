<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%> 
<html>
	<head>
		<title>
			<s:text name="oaReply.list.title" />
		</title>
	</head>

	<body>
		<%@ include file="/page/common/messages.jsp"%>
		<fieldset
			style="border: hidden 1px #000000; ">
			<legend>
				 <s:text name="label.list.filter" />
			</legend>
			
			<s:form action="oaReply" namespace="/app" style="margin-top:0;margin-bottom:5">
				<table cellpadding="0" cellspacing="0" align="center">

					<tr >
						<td><s:text name="oaReply.replyid" />:</td>
						<td><s:textfield name="s_replyid" /> </td>
					</tr>	


					<tr >
						<td><s:text name="oaReply.threadid" />:</td>
						<td><s:textfield name="s_threadid" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaReply.reply" />:</td>
						<td><s:textfield name="s_reply" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaReply.replytime" />:</td>
						<td><s:textfield name="s_replytime" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaReply.userid" />:</td>
						<td><s:textfield name="s_userid" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaReply.username" />:</td>
						<td><s:textfield name="s_username" /> </td>
					</tr>	

					<tr>
						<td>
							<s:submit method="list"  key="opt.btn.query" cssClass="btn"/>
						</td>
						<td>
							<s:submit method="built"  key="opt.btn.new" cssClass="btn"/>
						</td>
					</tr>
				</table>
			</s:form>
		</fieldset>

		<ec:table action="app/oaReply!list.do" items="objList" var="oaReply"
			imagePath="${STYLE_PATH}/images/table/*.gif" retrieveRowsCallback="limit">
			<ec:exportXls fileName="oaReplys.xls" ></ec:exportXls>
			<ec:exportPdf fileName="oaReplys.pdf" headerColor="blue" headerBackgroundColor="white" ></ec:exportPdf>
			<ec:row>

				<c:set var="treplyid"><s:text name='oaReply.replyid' /></c:set>	
				<ec:column property="replyid" title="${treplyid}" style="text-align:center" />


				<c:set var="tthreadid"><s:text name='oaReply.threadid' /></c:set>	
				<ec:column property="threadid" title="${tthreadid}" style="text-align:center" />

				<c:set var="treply"><s:text name='oaReply.reply' /></c:set>	
				<ec:column property="reply" title="${treply}" style="text-align:center" />

				<c:set var="treplytime"><s:text name='oaReply.replytime' /></c:set>	
				<ec:column property="replytime" title="${treplytime}" style="text-align:center" />

				<c:set var="tuserid"><s:text name='oaReply.userid' /></c:set>	
				<ec:column property="userid" title="${tuserid}" style="text-align:center" />

				<c:set var="tusername"><s:text name='oaReply.username' /></c:set>	
				<ec:column property="username" title="${tusername}" style="text-align:center" />
		
				<c:set var="optlabel"><s:text name="opt.btn.collection"/></c:set>	
				<ec:column property="opt" title="${optlabel}" sortable="false"
					style="text-align:center">
					<c:set var="deletecofirm"><s:text name="label.delete.confirm"/></c:set>
					<a href='app/oaReply!view.do?replyid=${oaReply.replyid}&ec_p=${ec_p}&ec_crd=${ec_crd}'><s:text name="opt.btn.view" /></a>
					<a href='app/oaReply!edit.do?replyid=${oaReply.replyid}'><s:text name="opt.btn.edit" /></a>
					<a href='app/oaReply!delete.do?replyid=${oaReply.replyid}' 
							onclick='return confirm("${deletecofirm}oaReply?");'><s:text name="opt.btn.delete" /></a>
				</ec:column>

			</ec:row>
		</ec:table>

	</body>
</html>
