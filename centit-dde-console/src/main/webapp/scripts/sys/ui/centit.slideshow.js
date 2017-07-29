/**


 * 
 */

$(function() {
	$.Slideshow = {
		_option: {
			// 跳转到某一页（系统）
			gotoFn: function(index, indexTo, container) {
				var datas = container.data('datas');
				var count = container.data('count');
				
				// 循环的方式指定页数
				indexTo = indexTo - Math.floor((indexTo-1)/count) * count;
				
				var data = (index >= 0) ? datas[index-1] : null;
				var dataNew = datas[indexTo-1];
				
				this.drawFn(data, dataNew, container);
				this.callbackFn(index, indexTo, container);
			},
			
			// 跳转后的回调函数（系统）
			callbackFn: function(index, indexTo, container) {
				var count = container.data('count');
				var prev = container.data('prev');
				var next = container.data('next');
				
				if (indexTo == 1 && !this.cycle) {
					prev.hide(this.animation);
				}
				else {
					prev.show(this.animation);
				}
				
				if (indexTo == count && !this.cycle) {
					next.hide(this.animation);
				}
				else {
					next.show(this.animation);
				}
				
				container.find(this.BUTTON_PAGE).removeClass('selected').eq(indexTo-1).addClass('selected');
				container.data('index', indexTo);
			},
			
			// 默认绘制方法，可由用户修改
			drawFn: function(data, dataNew, container) {
				var datas = container.data('datas');
				var animation = this.animation;
				
				$.each(datas, function(index, value){
					$(value, container).hide();
				});
				
				container.find(dataNew).fadeIn(animation);
			},
			
			type: 'navTab', TYPE_TAB: 'navTab', TYPE_DIALOG: 'dialog',
			auto: false, cycle: false, animation: 'normal',
			
			BUTTON: '.slideshowBtn', BUTTON_BOTTOM: '.bottom', BUTTON_PREV: '.prev', BUTTON_NEXT: '.next', BUTTON_PAGE: '.page', BUTTON_SIDE: '.side', 
			CONTAINER: '.slideshowContainer', IDENTITY: 'slideshow',
			
			BUTTON_PAGE_HTML: '<div class="slideshowBtn page"></div>', SLIDESHOW_CONTAINER: 'slideshowContainer'
		},

		init: function(renderTo, datas, option) {
			$.extend(true, this._option, (option || {}));
			
			var container = this._getCurrent(renderTo, this._option.type);
			if (!container) return false;
			
			// 绘制必要元素
			this._draw(container, datas);
			
			// 添加事件
			this._attachEvent(container, datas);
			
			return this._container;
		},
		
		/**
		 * 绘制必要元素
		 * 
		 * @param container
		 * @param datas
		 */
		_draw: function(container, datas) {
			if (container.children(this._option.BUTTON).length == 0) {
				container.append(DWZ.frag["Slideshow"]);
				
				container.data('prev', container.children(this._option.BUTTON_PREV))
							.data('next', container.children(this._option.BUTTON_NEXT));
			}
			
			// 重新绘制页数选择按钮
			var bottom = container.children(this._option.BUTTON_BOTTOM).html('');
			
			for (var i=0; i<datas.length; i++) {
				bottom.append(this._option.BUTTON_PAGE_HTML);
			}
			
			container.addClass(this._option.SLIDESHOW_CONTAINER)
						.data(this._option.IDENTITY, true)
						.data('page', bottom.children(this._option.BUTTON_PAGE))
						.data('count', datas.length)
						.data('index', -1)
						.data('datas', datas)
						.data('side', container.children(this._option.BUTTON_SIDE));
			
			// 调整按钮位置 左右按钮
			var width = container.width();
			var height = container.height();
			container.children(this._option.BUTTON_SIDE).css({
				top:(height - container.children(this._option.BUTTON_SIDE).first().height())*0.5
			});
			
			// 调整按钮位置 底部按钮
			container.children(this._option.BUTTON_BOTTOM).css({
				left:(width - container.children(this._option.BUTTON_BOTTOM).first().width())*0.5
			});
		},
		
		/**
		 * 添加事件
		 * @param container
		 * @param datas
		 */
		_attachEvent: function(container, datas) {
			//悬停事件
			container.data('side').add(container.data('page')).hover(function() {
				$(this).addClass('hover');
			}, function() {
				$(this).removeClass('hover');
			});
			
			// 前一页
			container.data('prev').click(function() {
				var index = container.data('index');
				$.Slideshow._option.gotoFn(index, index-1, container);
			});
			
			// 后一页
			container.data('next').click(function() {
				var index = container.data('index');
				$.Slideshow._option.gotoFn(index, index+1, container);
			});
			
			// 某一页点击
			container.data('page').click(function() {
				var index = container.data('index');
				var indexTo = $(this).index() + 1;
				
				if (index == indexTo) return false;
				
				$.Slideshow._option.gotoFn(index, indexTo, container);
			});
			
			this.gotoPage(container, 1);
		},
		
		/**
		 * 跳转到某页
		 * 
		 * @param container
		 * @param page
		 * @returns {Boolean}
		 */
		gotoPage: function(container, page) {
			if (!container.data(this._option.IDENTITY)) return false;
			
			var count = container.data('count');
			if (page < 1 || page > count) return false;
			
			container.data('page').eq(page-1).trigger('click');
		},
		
		/**
		 * 跳转到第一页
		 * @param container
		 */
		firstPage: function(container) {
			this.gotoPage(container, 1);
		},
		
		/**
		 * 跳转到最后一页
		 * @param container
		 */
		lastPage: function(container) {
			this.gotoPage(container, container.data('count'));
		},
		
		/**
		 * 向前一页
		 * @param container
		 */
		prev: function(container) {
			this.gotoPage(container, container.data('index')-1);
		},
		
		/**
		 * 向后一页
		 */
		prev: function(container) {
			this.gotoPage(container, container.data('index')+1);
		},
		
		_getCurrent: function(renderTo, type) {
			if (this._option.TYPE_TAB == type && navTab) {
				return $(renderTo, navTab.getCurrentPanel());
				
			} else if (this._option.TYPE_DIALOG == type && $.pdialog) {
				return $(renderTo, $.pdialog.getCurrent());
				
			}; 
			
			return $(renderTo);
		}
	};
	
	$.fn.extend({
		slideshow: function(datas) {
			alert(this[0]);
			alert(1);
			
			return $(this).each(function() {
				Slideshow.init(this, datas);
			});
		}
	});
});