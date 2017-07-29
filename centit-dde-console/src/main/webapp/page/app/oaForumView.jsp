<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%> 
<html>
<head>
<title><s:text name="oaForum.view.title" /></title>
</head>

<body>
<p class="ctitle"><s:text name="oaForum.view.title" /></p>

<%@ include file="/page/common/messages.jsp"%>

<a href='app/oaForum!list.do?ec_p=${param.ec_p}&ec_crd=${param.ec_crd}' property="none">
	<s:text name="opt.btn.back" />
</a>
<p>	
	
<table width="200" border="0" cellpadding="1" cellspacing="1">		
  
				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.forumid" />
					</td>
					<td align="left">
						<s:property value="%{forumid}" />
					</td>
				</tr>


				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.boardid" />
					</td>
					<td align="left">
						<s:property value="%{boardid}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.forumname" />
					</td>
					<td align="left">
						<s:property value="%{forumname}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.forumpic" />
					</td>
					<td align="left">
						<s:property value="%{forumpic}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.announcement" />
					</td>
					<td align="left">
						<s:property value="%{announcement}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.joinright" />
					</td>
					<td align="left">
						<s:property value="%{joinright}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.viewright" />
					</td>
					<td align="left">
						<s:property value="%{viewright}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.postright" />
					</td>
					<td align="left">
						<s:property value="%{postright}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.replyright" />
					</td>
					<td align="left">
						<s:property value="%{replyright}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.isforumer" />
					</td>
					<td align="left">
						<s:property value="%{isforumer}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.createtime" />
					</td>
					<td align="left">
						<s:property value="%{createtime}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.mebernum" />
					</td>
					<td align="left">
						<s:property value="%{mebernum}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.threadnum" />
					</td>
					<td align="left">
						<s:property value="%{threadnum}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.replynum" />
					</td>
					<td align="left">
						<s:property value="%{replynum}" />
					</td>
				</tr>	

</table>


<p/>
<div class="eXtremeTable" >
	<table id="ec_table"  border="0"  cellspacing="0"  cellpadding="0"  class="tableRegion"  width="100%" >

		<thead>
			<tr>
    
				<td class="tableHeader">
					<s:text name="oaThread.threadid" />
				</td>


  
				<td class="tableHeader">
					<s:text name="oaThread.titol" />
				</td>
  
				<td class="tableHeader">
					<s:text name="oaThread.content" />
				</td>
  
				<td class="tableHeader">
					<s:text name="oaThread.wirterid" />
				</td>
  
				<td class="tableHeader">
					<s:text name="oaThread.wirter" />
				</td>
  
				<td class="tableHeader">
					<s:text name="oaThread.posttime" />
				</td>
  
				<td class="tableHeader">
					<s:text name="oaThread.viewnum" />
				</td>
  
				<td class="tableHeader">
					<s:text name="oaThread.replnum" />
				</td>
		
				<td class="tableHeader"><s:text name="opt.btn.collection" /></td>
			</tr>  
		</thead>
		
		<tbody class="tableBody" >
		<c:set value="odd" var="rownum" />
		
		<c:forEach var="oaThread" items="${object.oaThreads}">    
			<tr class="${rownum}"  onmouseover="this.className='highlight'"  onmouseout="this.className='${rownum}'" >
    
				<td><c:out value="${oaThread.threadid}"/></td>  

  
				<td><c:out value="${oaThread.titol}"/></td>  
  
				<td><c:out value="${oaThread.content}"/></td>  
  
				<td><c:out value="${oaThread.wirterid}"/></td>  
  
				<td><c:out value="${oaThread.wirter}"/></td>  
  
				<td><c:out value="${oaThread.posttime}"/></td>  
  
				<td><c:out value="${oaThread.viewnum}"/></td>  
  
				<td><c:out value="${oaThread.replnum}"/></td>  
		
				<td>
					<c:set var="deletecofirm"><s:text name="label.delete.confirm"/></c:set>
					<a href='app/oaThread!edit.do?forumid=${oaForum.forumid}&threadid=${oaThread.threadid}'><s:text name="opt.btn.edit" /></a>
					<a href='app/oaThread!delete.do?forumid=${oaForum.forumid}&threadid=${oaThread.threadid}' 
							onclick='return confirm("${deletecofirm}oaThread?");'><s:text name="opt.btn.delete" /></a>
				</td>
			</tr>  
            <c:set value="${rownum eq 'odd'? 'even': 'odd'}" var="rownum" />
		</c:forEach> 
		</tbody>        
	</table>
</div>


</body>
</html>
