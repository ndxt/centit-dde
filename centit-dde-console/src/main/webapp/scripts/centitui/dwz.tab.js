/**
 * @author ZhangHuihua@msn.com
 * 
 */
(function($){
	$.fn.extend({

		/**
		 * options: reverse[true, false], eventType[click, hover], currentIndex[default index 0]
		 * 			stTab[tabs selector], stTabPanel[tab panel selector]
		 * 			ajaxClass[ajax load], closeClass[close tab]
		 */ 
		tabs: function (options){
			var op = $.extend({reverse:false, eventType:"click", currentIndex:0, stTabHeader:"> .tabsHeader", stTab:">.tabsHeaderContent>ul", stTabPanel:"> .tabsContent", ajaxClass:"j-ajax", closeClass:"close", prevClass:"tabsLeft", nextClass:"tabsRight"}, options);
			
			return this.each(function(){
				initTab($(this));
			});
			
			// 通过改变TAB页URL，刷新页面后保持原来点击的标签
			function changeURLType(currentIndex) {
				var tab = $('ul.navTab-tab>li.selected');
				var url = tab.attr('url');
				
				// 查询currentIndex参数，在中间
				var regNotEnd = /(.*)([\?|&]currentIndex=)(.*)(&.*)/;
				var tempNotEnd = url.match(regNotEnd);
				
				// 查询currentIndex参数，在尾部
				var regEnd = /(.*)([\?|&]currentIndex=)(.*)/;
				var tempEnd = url.match(regEnd);
				
				// currentIndex参数在中间
				if (tempNotEnd) {
					url = url.replace(regNotEnd, "$1$2"+currentIndex+"$4");
				}
				// currentIndex参数在末尾
				else if (tempEnd) {
					url = url.replace(regEnd, "$1$2"+currentIndex);
				}
				// 没有currentIndex参数
				else {
					url += '&currentIndex=' + currentIndex;
				}
				
				tab.attr('url', url);
				return url;
			}
			
			function initTab(jT){
				var jSelector = jT.add($("> *", jT));
				var jTabHeader = $(op.stTabHeader, jSelector);
				var jTabs = $(op.stTab + " li", jTabHeader);
				var jGroups = $(op.stTabPanel + " > *", jSelector);

				jTabs.unbind().find("a").unbind();
				jTabHeader.find("."+op.prevClass).unbind();
				jTabHeader.find("."+op.nextClass).unbind();
				
				jTabs.each(function(iTabIndex){
					$(this).click(function() {
						changeURLType(iTabIndex);
					});
					
					if (op.currentIndex == iTabIndex) $(this).addClass("selected");
					else $(this).removeClass("selected");
					
					if (op.eventType == "hover") $(this).hover(function(event){switchTab(jT, iTabIndex)});
					else $(this).click(function(event){switchTab(jT, iTabIndex)});

					$("a", this).each(function(){
						if ($(this).hasClass(op.ajaxClass)) {
							$(this).click(function(event){
								var jGroup = jGroups.eq(iTabIndex);
								if (this.href) jGroup.loadUrl(this.href,{},function(){
									jGroup.find("[layoutH]").layoutH();
								});
								event.preventDefault();
							});
							
							
						} else if ($(this).hasClass(op.closeClass)) {
							$(this).click(function(event){
								jTabs.eq(iTabIndex).remove();
								jGroups.eq(iTabIndex).remove();
								if (iTabIndex == op.currentIndex) {
									op.currentIndex = (iTabIndex+1 < jTabs.size()) ? iTabIndex : iTabIndex - 1;
								} else if (iTabIndex < op.currentIndex){
									op.currentIndex = iTabIndex;
								}
								initTab(jT);
								return false;
							});
						}
					});
				});

				switchTab(jT, op.currentIndex);
				
				setTimeout(function() {
					$(op.stTab + " li.selected>a", jTabHeader)[0].click();
				}, 24);
			}
			
			function switchTab(jT, iTabIndex){
				var jSelector = jT.add($("> *", jT));
				var jTabHeader = $(op.stTabHeader, jSelector);
				var jTabs = $(op.stTab + " li", jTabHeader);
				var jGroups = $(op.stTabPanel + " > *", jSelector);
				
				var jTab = jTabs.eq(iTabIndex);
				var jGroup = jGroups.eq(iTabIndex);
				if (op.reverse && (jTab.hasClass("selected") )) {
					jTabs.removeClass("selected");
					jGroups.hide();
				} else {
					op.currentIndex = iTabIndex;
					jTabs.removeClass("selected");
					jTab.addClass("selected");
					
					jGroups.hide().eq(op.currentIndex).show();
				}
				
				if (!jGroup.attr("inited")){
					jGroup.attr("inited", 1000).find("input[type=text]").filter("[alt]").inputAlert();
				}
			}
			
		}
	});
})(jQuery);