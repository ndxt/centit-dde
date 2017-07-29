<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%> 
<html>
	<head>
		<title>
			<s:text name="oaForum.list.title" />
		</title>
	</head>

	<body>
		<%@ include file="/page/common/messages.jsp"%>
		<fieldset
			style="border: hidden 1px #000000; ">
			<legend>
				 <s:text name="label.list.filter" />
			</legend>
			
			<s:form action="oaForum" namespace="/app" style="margin-top:0;margin-bottom:5">
				<table cellpadding="0" cellspacing="0" align="center">

					<tr >
						<td><s:text name="oaForum.forumid" />:</td>
						<td><s:textfield name="s_forumid" /> </td>
					</tr>	


					<tr >
						<td><s:text name="oaForum.boardid" />:</td>
						<td><s:textfield name="s_boardid" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaForum.forumname" />:</td>
						<td><s:textfield name="s_forumname" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaForum.forumpic" />:</td>
						<td><s:textfield name="s_forumpic" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaForum.announcement" />:</td>
						<td><s:textfield name="s_announcement" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaForum.joinright" />:</td>
						<td><s:textfield name="s_joinright" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaForum.viewright" />:</td>
						<td><s:textfield name="s_viewright" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaForum.postright" />:</td>
						<td><s:textfield name="s_postright" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaForum.replyright" />:</td>
						<td><s:textfield name="s_replyright" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaForum.isforumer" />:</td>
						<td><s:textfield name="s_isforumer" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaForum.createtime" />:</td>
						<td><s:textfield name="s_createtime" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaForum.mebernum" />:</td>
						<td><s:textfield name="s_mebernum" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaForum.threadnum" />:</td>
						<td><s:textfield name="s_threadnum" /> </td>
					</tr>	

					<tr >
						<td><s:text name="oaForum.replynum" />:</td>
						<td><s:textfield name="s_replynum" /> </td>
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

		<ec:table action="app/oaForum!list.do" items="objList" var="oaForum"
			imagePath="${STYLE_PATH}/images/table/*.gif" retrieveRowsCallback="limit">
			<ec:exportXls fileName="oaForums.xls" ></ec:exportXls>
			<ec:exportPdf fileName="oaForums.pdf" headerColor="blue" headerBackgroundColor="white" ></ec:exportPdf>
			<ec:row>

				<c:set var="tforumid"><s:text name='oaForum.forumid' /></c:set>	
				<ec:column property="forumid" title="${tforumid}" style="text-align:center" />


				<c:set var="tboardid"><s:text name='oaForum.boardid' /></c:set>	
				<ec:column property="boardid" title="${tboardid}" style="text-align:center" />

				<c:set var="tforumname"><s:text name='oaForum.forumname' /></c:set>	
				<ec:column property="forumname" title="${tforumname}" style="text-align:center" />

				<c:set var="tforumpic"><s:text name='oaForum.forumpic' /></c:set>	
				<ec:column property="forumpic" title="${tforumpic}" style="text-align:center" />

				<c:set var="tannouncement"><s:text name='oaForum.announcement' /></c:set>	
				<ec:column property="announcement" title="${tannouncement}" style="text-align:center" />

				<c:set var="tjoinright"><s:text name='oaForum.joinright' /></c:set>	
				<ec:column property="joinright" title="${tjoinright}" style="text-align:center" />

				<c:set var="tviewright"><s:text name='oaForum.viewright' /></c:set>	
				<ec:column property="viewright" title="${tviewright}" style="text-align:center" />

				<c:set var="tpostright"><s:text name='oaForum.postright' /></c:set>	
				<ec:column property="postright" title="${tpostright}" style="text-align:center" />

				<c:set var="treplyright"><s:text name='oaForum.replyright' /></c:set>	
				<ec:column property="replyright" title="${treplyright}" style="text-align:center" />

				<c:set var="tisforumer"><s:text name='oaForum.isforumer' /></c:set>	
				<ec:column property="isforumer" title="${tisforumer}" style="text-align:center" />

				<c:set var="tcreatetime"><s:text name='oaForum.createtime' /></c:set>	
				<ec:column property="createtime" title="${tcreatetime}" style="text-align:center" />

				<c:set var="tmebernum"><s:text name='oaForum.mebernum' /></c:set>	
				<ec:column property="mebernum" title="${tmebernum}" style="text-align:center" />

				<c:set var="tthreadnum"><s:text name='oaForum.threadnum' /></c:set>	
				<ec:column property="threadnum" title="${tthreadnum}" style="text-align:center" />

				<c:set var="treplynum"><s:text name='oaForum.replynum' /></c:set>	
				<ec:column property="replynum" title="${treplynum}" style="text-align:center" />
		
				<c:set var="optlabel"><s:text name="opt.btn.collection"/></c:set>	
				<ec:column property="opt" title="${optlabel}" sortable="false"
					style="text-align:center">
					<c:set var="deletecofirm"><s:text name="label.delete.confirm"/></c:set>
					<a href='app/oaForum!view.do?forumid=${oaForum.forumid}&ec_p=${ec_p}&ec_crd=${ec_crd}'><s:text name="opt.btn.view" /></a>
					<a href='app/oaForum!edit.do?forumid=${oaForum.forumid}'><s:text name="opt.btn.edit" /></a>
					<a href='app/oaForum!delete.do?forumid=${oaForum.forumid}' 
							onclick='return confirm("${deletecofirm}oaForum?");'><s:text name="opt.btn.delete" /></a>
				</ec:column>

			</ec:row>
		</ec:table>

	</body>
</html>
