<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%> 
<html>
	<head>
		<title>
			<s:text name="oaThread.list.title" />
		</title>
	</head>

	<body>
		<%@ include file="/page/common/messages.jsp"%>
		<fieldset
			style="border: hidden 1px #000000; ">
			<legend>
				 <s:text name="label.list.filter" />
			</legend>
			
			<s:form action="oaThread" namespace="/app" style="margin-top:0;margin-bottom:5">
				<table cellpadding="0" cellspacing="0" align="center">

					<tr >
						<td><s:text name="oaThread.threadid" />:</td>
						<td><s:textfield name="s_threadid" /> </td>
					</tr>	


					<tr >
						<td><s:text name="oaThread.forumid" />:</td>
						<td><s:textfield name="s_forumid" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaThread.titol" />:</td>
						<td><s:textfield name="s_titol" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaThread.content" />:</td>
						<td><s:textfield name="s_content" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaThread.wirterid" />:</td>
						<td><s:textfield name="s_wirterid" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaThread.wirter" />:</td>
						<td><s:textfield name="s_wirter" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaThread.posttime" />:</td>
						<td><s:textfield name="s_posttime" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaThread.viewnum" />:</td>
						<td><s:textfield name="s_viewnum" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaThread.replnum" />:</td>
						<td><s:textfield name="s_replnum" /> </td>
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

		<ec:table action="app/oaThread!list.do" items="objList" var="oaThread"
			imagePath="${STYLE_PATH}/images/table/*.gif" retrieveRowsCallback="limit">
			<ec:exportXls fileName="oaThreads.xls" ></ec:exportXls>
			<ec:exportPdf fileName="oaThreads.pdf" headerColor="blue" headerBackgroundColor="white" ></ec:exportPdf>
			<ec:row>

				<c:set var="tthreadid"><s:text name='oaThread.threadid' /></c:set>	
				<ec:column property="threadid" title="${tthreadid}" style="text-align:center" />


				<c:set var="tforumid"><s:text name='oaThread.forumid' /></c:set>	
				<ec:column property="forumid" title="${tforumid}" style="text-align:center" />

				<c:set var="ttitol"><s:text name='oaThread.titol' /></c:set>	
				<ec:column property="titol" title="${ttitol}" style="text-align:center" />

				<c:set var="tcontent"><s:text name='oaThread.content' /></c:set>	
				<ec:column property="content" title="${tcontent}" style="text-align:center" />

				<c:set var="twirterid"><s:text name='oaThread.wirterid' /></c:set>	
				<ec:column property="wirterid" title="${twirterid}" style="text-align:center" />

				<c:set var="twirter"><s:text name='oaThread.wirter' /></c:set>	
				<ec:column property="wirter" title="${twirter}" style="text-align:center" />

				<c:set var="tposttime"><s:text name='oaThread.posttime' /></c:set>	
				<ec:column property="posttime" title="${tposttime}" style="text-align:center" />

				<c:set var="tviewnum"><s:text name='oaThread.viewnum' /></c:set>	
				<ec:column property="viewnum" title="${tviewnum}" style="text-align:center" />

				<c:set var="treplnum"><s:text name='oaThread.replnum' /></c:set>	
				<ec:column property="replnum" title="${treplnum}" style="text-align:center" />
		
				<c:set var="optlabel"><s:text name="opt.btn.collection"/></c:set>	
				<ec:column property="opt" title="${optlabel}" sortable="false"
					style="text-align:center">
					<c:set var="deletecofirm"><s:text name="label.delete.confirm"/></c:set>
					<a href='app/oaThread!view.do?threadid=${oaThread.threadid}&ec_p=${ec_p}&ec_crd=${ec_crd}'><s:text name="opt.btn.view" /></a>
					<a href='app/oaThread!edit.do?threadid=${oaThread.threadid}'><s:text name="opt.btn.edit" /></a>
					<a href='app/oaThread!delete.do?threadid=${oaThread.threadid}' 
							onclick='return confirm("${deletecofirm}oaThread?");'><s:text name="opt.btn.delete" /></a>
				</ec:column>

			</ec:row>
		</ec:table>

	</body>
</html>
