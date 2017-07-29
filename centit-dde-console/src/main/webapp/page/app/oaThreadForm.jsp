<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%> 
<html>
<head>
<title><s:text name="oaThread.edit.title" /></title>
</head>

<body>
<p class="ctitle"><s:text name="oaThread.edit.title" /></p>

<%@ include file="/page/common/messages.jsp"%>

<s:form action="oaThread"  method="post" namespace="/app" id="oaThreadForm" >
	<s:submit name="save"  method="save" cssClass="btn" key="opt.btn.save" />
	<s:submit type="button" name="back" cssClass="btn" key="opt.btn.back"/>
		
<table width="200" border="0" cellpadding="1" cellspacing="1">		
 
				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.threadid" />
					</td>
					<td align="left">
	
  
							<s:textfield name="threadid" size="40" />
	
					</td>
				</tr>


				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.forumid" />
					</td>
					<td align="left">
	
  
						<s:textfield name="forumid"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.titol" />
					</td>
					<td align="left">
  
						<s:textarea name="titol" cols="40" rows="2"/>
	
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.content" />
					</td>
					<td align="left">
	
  
						<s:textfield name="content"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.wirterid" />
					</td>
					<td align="left">
	
  
						<s:textfield name="wirterid"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.wirter" />
					</td>
					<td align="left">
  
						<s:textarea name="wirter" cols="40" rows="2"/>
	
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.posttime" />
					</td>
					<td align="left">
	
  
						<s:textfield name="posttime"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.viewnum" />
					</td>
					<td align="left">
	
  
						<s:textfield name="viewnum"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaThread.replnum" />
					</td>
					<td align="left">
	
  
						<s:textfield name="replnum"  size="40"/>
	
					</td>
				</tr>

</table>


<p/>
<div class="eXtremeTable" >
	<table id="t_oaReply"  border="0"  cellspacing="0"  cellpadding="0"  class="tableRegion"  width="100%" >

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
		
				<td class="tableHeader">
					<a href='javascript:void(0)' onclick='addOaReplyItem(this);'><s:text name="opt.btn.new" /></a>
				</td>
			</tr>  
		</thead>
		
		<tbody class="tableBody" >
		<c:set value="odd" var="rownum" />
		 <s:iterator value="oaReplys" status="status" >    
			<tr class="${rownum}"  onmouseover="this.className='highlight'"  onmouseout="this.className='${rownum}'" >
    
				<td><s:textfield name="replyid" /> </td>   

  
				<td><s:textfield name="reply" /> </td>   
  
				<td><s:textfield name="replytime" /> </td>   
  
				<td><s:textfield name="userid" /> </td>   
  
				<td><s:textfield name="username" /> </td>   
		
				<td>
					<a href='javascript:void(0)' onclick='delOaReplyItem(this);'><s:text name='opt.btn.delete' /></a>
				</td>
			</tr>  
            <c:set value="${rownum eq 'odd'? 'even': 'odd'}" var="rownum" />
		</s:iterator> 
		</tbody>        
	</table>
</div>

</s:form>

	<script type="text/javascript">
	    
		var t_oaReplyRowCount; // 行数

	    $(function()
	    {
	
			t_oaReplyRowCount = $("table#t_oaReply tr").length - 1; // 除去标题行   
	    	var oaReplyColName = 
	    	          ["replyid",
                      "reply","replytime","userid","username","guard"];
	    	
			
	        $("input[name='method:save']").bind("click", function()
	        {
	            $("table#t_oaReply tr").each(function(i)
	            {
	                $(this).attr("id", "tr_oaReply" + i);
	                $("#tr_oaReply" + i + "  input[type='text']").each(function(j)
	                {
	                    $(this).attr("name", "newOaReplys["+(i-1)+"]." + oaReplyColName[j]);
	                });
	            });
	            
	        });
	    });    
	    
        function addOaReplyItem()
        {
             var htmlItem = '<tr>';
  
			htmlItem += '<td><input type="text" name="newOaReplys['+t_oaReplyRowCount+'].replyid" /></td>'; 


			htmlItem += '<td><input type="text" name="newOaReplys['+t_oaReplyRowCount+'].reply" /></td>'; 

			htmlItem += '<td><input type="text" name="newOaReplys['+t_oaReplyRowCount+'].replytime" /></td>'; 

			htmlItem += '<td><input type="text" name="newOaReplys['+t_oaReplyRowCount+'].userid" /></td>'; 

			htmlItem += '<td><input type="text" name="newOaReplys['+t_oaReplyRowCount+'].username" /></td>'; 
            
            t_oaReplyRowCount++;
            htmlItem += "<td> <a href='javascript:void(0)' onclick='delOaReplyItem(this);'><s:text name='opt.btn.delete' /></a></td></tr>";
            $("table#t_oaReply").append(htmlItem);

   		    $('table#t_oaReply.tableRegion tr:odd').attr('class','odd')
   		    .hover(function(){
       		    	$(this).addClass("highlight");
       		    },function(){
       		    	$(this).removeClass("highlight");
       		});
   		    $('table#t_oaReply.tableRegion tr:even').attr('class','even')
   		    .hover(function(){
	   		    	$(this).addClass("highlight");
	   		    },function(){
	   		    	$(this).removeClass("highlight");
   		    });
     	}
        
        function delOaReplyItem(varBtn)
        {
            $(varBtn).parent().parent().remove();
            t_oaReplyRowCount--;
   		    $('table#t_oaReply.tableRegion tr:odd').attr('class','odd');
   		    $('table#t_oaReply.tableRegion tr:even').attr('class','even');
        }

    </script>	


</body>
</html>
