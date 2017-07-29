<script type="text/javascript">
/*
* Create time:2012-03-01
*/
$(function(){
	var cssStyle = $.cookie("CentitUI_theme") || "default";
	var surl = "${pageContext.request.contextPath}/themes/"+cssStyle;
	
	var optionList = {
		"view":{"src":"/images/option/vcard.png","title":"<s:text name='opt.btn.view' />","alt":"<s:text name='opt.btn.view' />"},
		"viewDetail":{"src":"/images/option/vcard.png","title":"<s:text name='opt.btn.viewdetail' />","alt":"<s:text name='opt.btn.viewdetail' />"}, 
		"viewInDialog":{"src":"/images/option/application.png","title":"<s:text name='opt.btn.viewindialog' />","alt":"<s:text name='opt.btn.viewindialog' />"}, 
		"edit":{"src":"/images/option/edit.png","title":"<s:text name='opt.btn.edit' />","alt":"<s:text name='opt.btn.edit' />"}, 
		"delete":{"src":"/images/option/cross.png","title":"<s:text name='opt.btn.delete' />","alt":"<s:text name='opt.btn.delete' />"}, 
		"disable":{}, 
		"enabled":{}  
	};
	var $pageOption = jQuery(".option-btn a");
	$pageOption.each(function(){
		var $this = jQuery(this);
		if(!!$this.attr("styleopt")&& typeof optionList[$this.attr("styleopt")] != "undefined" )
			$this.html("<img src='"+surl+optionList[$this.attr("styleopt")]["src"]+"' title='"+optionList[$this.attr("styleopt")]["title"]+"' alt='"+optionList[$this.attr("styleopt")]["alt"]+"' />");
	});
});

</script>