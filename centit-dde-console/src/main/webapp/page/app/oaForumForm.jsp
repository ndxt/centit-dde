<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%> 
<%@ include file="/page/common/css.jsp"%> 
<html>
<head>
<title><s:text name="oaForum.edit.title" /></title>
</head>

<body>
<p class="ctitle"><s:text name="oaForum.edit.title" /></p>

<%@ include file="/page/common/messages.jsp"%>

<s:form action="oaForum"  method="post" namespace="/app" id="oaForumForm" >
	<s:submit name="save"  method="save" cssClass="btn" key="opt.btn.save" />
	<s:submit type="button" name="back" cssClass="btn" key="opt.btn.back"/>
		
<table width="200" border="0" cellpadding="1" cellspacing="1">		
 
				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.forumid" />
					</td>
					<td align="left">
	
  
							<s:textfield name="forumid" size="40" />
	
					</td>
				</tr>


				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.boardid" />
					</td>
					<td align="left">
	
  
						<s:textfield name="boardid"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.forumname" />
					</td>
					<td align="left">
  
						<s:textarea name="forumname" cols="40" rows="2"/>
	
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.forumpic" />
					</td>
					<td align="left">
  
						<s:textarea name="forumpic" cols="40" rows="2"/>
	
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.announcement" />
					</td>
					<td align="left">
	
  
						<s:textfield name="announcement"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.joinright" />
					</td>
					<td align="left">
	
  
						<s:textfield name="joinright"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.viewright" />
					</td>
					<td align="left">
	
  
						<s:textfield name="viewright"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.postright" />
					</td>
					<td align="left">
	
  
						<s:textfield name="postright"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.replyright" />
					</td>
					<td align="left">
	
  
						<s:textfield name="replyright"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.isforumer" />
					</td>
					<td align="left">
	
  
						<s:textfield name="isforumer"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.createtime" />
					</td>
					<td align="left">
	
  
						<s:textfield name="createtime"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.mebernum" />
					</td>
					<td align="left">
	
  
						<s:textfield name="mebernum"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.threadnum" />
					</td>
					<td align="left">
	
  
						<s:textfield name="threadnum"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaForum.replynum" />
					</td>
					<td align="left">
	
  
						<s:textfield name="replynum"  size="40"/>
	
					</td>
				</tr>

</table>


<p/>
<div class="eXtremeTable" >
	<table id="t_oaThread"  border="0"  cellspacing="0"  cellpadding="0"  class="tableRegion"  width="100%" >

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
		
				<td class="tableHeader">
					<a href='javascript:void(0)' onclick='addOaThreadItem(this);'><s:text name="opt.btn.new" /></a>
				</td>
			</tr>  
		</thead>
		
		<tbody class="tableBody" >
		<c:set value="odd" var="rownum" />
		 <s:iterator value="oaThreads" status="status" >    
			<tr class="${rownum}"  onmouseover="this.className='highlight'"  onmouseout="this.className='${rownum}'" >
    
				<td><s:textfield name="threadid" /> </td>   

  
				<td><s:textfield name="titol" /> </td>   
  
				<td><s:textfield name="content" /> </td>   
  
				<td><s:textfield name="wirterid" /> </td>   
  
				<td><s:textfield name="wirter" /> </td>   
  
				<td><s:textfield name="posttime" /> </td>   
  
				<td><s:textfield name="viewnum" /> </td>   
  
				<td><s:textfield name="replnum" /> </td>   
		
				<td>
					<a href='javascript:void(0)' onclick='delOaThreadItem(this);'><s:text name='opt.btn.delete' /></a>
				</td>
			</tr>  
            <c:set value="${rownum eq 'odd'? 'even': 'odd'}" var="rownum" />
		</s:iterator> 
		</tbody>        
	</table>
</div>

</s:form>

	<script type="text/javascript">
	    
		var t_oaThreadRowCount; // 行数

	    $(function()
	    {
	
			t_oaThreadRowCount = $("table#t_oaThread tr").length - 1; // 除去标题行   
	    	var oaThreadColName = 
	    	          ["threadid",
                      "titol","content","wirterid","wirter","posttime","viewnum","replnum","guard"];
	    	
			
	        $("input[name='method:save']").bind("click", function()
	        {
	            $("table#t_oaThread tr").each(function(i)
	            {
	                $(this).attr("id", "tr_oaThread" + i);
	                $("#tr_oaThread" + i + "  input[type='text']").each(function(j)
	                {
	                    $(this).attr("name", "newOaThreads["+(i-1)+"]." + oaThreadColName[j]);
	                });
	            });
	            
	        });
	    });    
	    
        function addOaThreadItem()
        {
             var htmlItem = '<tr>';
  
			htmlItem += '<td><input type="text" name="newOaThreads['+t_oaThreadRowCount+'].threadid" /></td>'; 


			htmlItem += '<td><input type="text" name="newOaThreads['+t_oaThreadRowCount+'].titol" /></td>'; 

			htmlItem += '<td><input type="text" name="newOaThreads['+t_oaThreadRowCount+'].content" /></td>'; 

			htmlItem += '<td><input type="text" name="newOaThreads['+t_oaThreadRowCount+'].wirterid" /></td>'; 

			htmlItem += '<td><input type="text" name="newOaThreads['+t_oaThreadRowCount+'].wirter" /></td>'; 

			htmlItem += '<td><input type="text" name="newOaThreads['+t_oaThreadRowCount+'].posttime" /></td>'; 

			htmlItem += '<td><input type="text" name="newOaThreads['+t_oaThreadRowCount+'].viewnum" /></td>'; 

			htmlItem += '<td><input type="text" name="newOaThreads['+t_oaThreadRowCount+'].replnum" /></td>'; 
            
            t_oaThreadRowCount++;
            htmlItem += "<td> <a href='javascript:void(0)' onclick='delOaThreadItem(this);'><s:text name='opt.btn.delete' /></a></td></tr>";
            $("table#t_oaThread").append(htmlItem);

   		    $('table#t_oaThread.tableRegion tr:odd').attr('class','odd')
   		    .hover(function(){
       		    	$(this).addClass("highlight");
       		    },function(){
       		    	$(this).removeClass("highlight");
       		});
   		    $('table#t_oaThread.tableRegion tr:even').attr('class','even')
   		    .hover(function(){
	   		    	$(this).addClass("highlight");
	   		    },function(){
	   		    	$(this).removeClass("highlight");
   		    });
     	}
        
        function delOaThreadItem(varBtn)
        {
            $(varBtn).parent().parent().remove();
            t_oaThreadRowCount--;
   		    $('table#t_oaThread.tableRegion tr:odd').attr('class','odd');
   		    $('table#t_oaThread.tableRegion tr:even').attr('class','even');
        }

    </script>	


</body>
</html>
