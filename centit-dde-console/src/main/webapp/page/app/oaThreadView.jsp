<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%> 
<html>
<head>
<title><s:text name="oaThread.view.title" /></title>
</head>

<body>
<p class="ctitle"><s:text name="oaThread.view.title" /></p>

<%@ include file="/page/common/messages.jsp"%>

<a href='app/oaThread!list.do?ec_p=${param.ec_p}&ec_crd=${param.ec_crd}' property="none">
	<s:text name="opt.btn.back" />
</a>
<p>	
	
<table width="200" border="0" cellpadding="1" cellspacing="1">		
  
				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.threadid" />
					</td>
					<td align="left">
						<s:property value="%{threadid}" />
					</td>
				</tr>


				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.forumid" />
					</td>
					<td align="left">
						<s:property value="%{forumid}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.titol" />
					</td>
					<td align="left">
						<s:property value="%{titol}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.content" />
					</td>
					<td align="left">
						<s:property value="%{content}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.wirterid" />
					</td>
					<td align="left">
						<s:property value="%{wirterid}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.wirter" />
					</td>
					<td align="left">
						<s:property value="%{wirter}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.posttime" />
					</td>
					<td align="left">
						<s:property value="%{posttime}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.viewnum" />
					</td>
					<td align="left">
						<s:property value="%{viewnum}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.replnum" />
					</td>
					<td align="left">
						<s:property value="%{replnum}" />
					</td>
				</tr>	

</table>


<p/>
<div class="eXtremeTable" >
	<table id="ec_table"  border="0"  cellspacing="0"  cellpadding="0"  class="tableRegion"  width="100%" >

		<thead>
			<tr>
    
				<td class="tableHeader">
					<s:text name="oaReply.replyid" />
				</td>


  
				<td class="tableHeader">
					<s:text name="oaReply.reply" />
				</td>
  
				<td class="tableHeader">
					<s:text name="oaReply.replytime" />
				</td>
  
				<td class="tableHeader">
					<s:text name="oaReply.userid" />
				</td>
  
				<td class="tableHeader">
					<s:text name="oaReply.username" />
				</td>
		
				<td class="tableHeader"><s:text name="opt.btn.collection" /></td>
			</tr>  
		</thead>
		
		<tbody class="tableBody" >
		<c:set value="odd" var="rownum" />
		
		<c:forEach var="oaReply" items="${object.oaReplys}">    
			<tr class="${rownum}"  onmouseover="this.className='highlight'"  onmouseout="this.className='${rownum}'" >
    
				<td><c:out value="${oaReply.replyid}"/></td>  

  
				<td><c:out value="${oaReply.reply}"/></td>  
  
				<td><c:out value="${oaReply.replytime}"/></td>  
  
				<td><c:out value="${oaReply.userid}"/></td>  
  
				<td><c:out value="${oaReply.username}"/></td>  
		
				<td>
					<c:set var="deletecofirm"><s:text name="label.delete.confirm"/></c:set>
					<a href='app/oaReply!edit.do?threadid=${oaThread.threadid}&replyid=${oaReply.replyid}'><s:text name="opt.btn.edit" /></a>
					<a href='app/oaReply!delete.do?threadid=${oaThread.threadid}&replyid=${oaReply.replyid}' 
							onclick='return confirm("${deletecofirm}oaReply?");'><s:text name="opt.btn.delete" /></a>
				</td>
			</tr>  
            <c:set value="${rownum eq 'odd'? 'even': 'odd'}" var="rownum" />
		</c:forEach> 
		</tbody>        
	</table>
</div>


</body>
</html>
