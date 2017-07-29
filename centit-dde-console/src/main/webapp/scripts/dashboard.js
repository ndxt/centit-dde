$(function() {
	$.myHome = {
		op: {titleHeight:28, sideWidthLeft: 4, sideWidthRight: 4, sideWidthBottom: 4, sideWidthTop: 3, scrollWidth: 17, parentPadding: 10, speed: 200, 
			winMarginLeft: 10, winMarginTop: 10, sizeWidth: 12, sizeHeight: 2,
			container: '.dashboard-container', content: '.panelContent', border: '.right-side', parent: '.tabsPageContent',
			maxable: true,split:10,
			width: 480, height:300},
		container: null,
		maxWin: null,
		windows: [],
		
		init: function(op) {
			this.op = $.extend(this.op, op);
			
			if (!this.container) {
				$('body').append("<div class='dashboard-container'></div>");
				this.container = $(this.op.container);
				this.container.height($(this.op.parent).height() - this.op.parentPadding);
			}
		},
		
		open: function(id, title, src, op) {
			this.init(op);
			
			var width = this.op.width;
			var height = this.op.height;
			
			var html = DWZ.frag["MyHome"].replace(/\$\{id\}/ig, id);
			html =html.replace(/\$\{title\}/ig, title);
			
			var win = $(html);
			this._load(win, src);
			win.hide();
			this._resize(win, 480, 300);
			this.container.append(win);
			
			win = $('#'+win[0].id);
			width = (width>this.op.sizeWidth)? this.op.sizeWidth:width;
			height = (height>this.op.sizeHeight)?this.op.sizeHeight:height;
			
			this._adapitveWindowSize(win, width, height);
			this._attachEvent(win, this.op);
			
		},
		
		/**
		 * 在收缩菜单栏时，同步窗口宽度
		 */
		synchronizeWidth: function() {
			if (!this.container) return;
			
			this.container.height($(this.op.parent).height() - this.op.parentPadding);
			
			if(this.maxWin) {
				this._resize(this.maxWin, this._getMaxWidth(), this._getMaxHeight());// - this.op.scrollWidth);
			}
			
			this._adapitveWindowSize();
		},
		
		_attachEvent: function(win, op) {
			// 鼠标悬停在窗口上改变背景色
			win.hover(function() {
				$(this).addClass('hover');
			}, function() {
				$(this).removeClass('hover');
			});
			
			// 最大化按钮
			var btnMax = $('.maximize', win);
			var btnRestore = $('.restore', win);
			btnRestore.hide();
			
			if (!op.maxable) {
				btnMax.hide();
			}else {
				btnMax.click(function() {
					btnMax.hide();
					btnRestore.show();
					$.myHome._maxsize(win);
				}).hover(function() {
					$(this).addClass('hover');
				}, function() {
					$(this).removeClass('hover');
				});
				
				btnRestore.click(function() {
					btnRestore.hide();
					btnMax.show();
					$.myHome._restore(win);
				}).hover(function() {
					$(this).addClass('hover');
				}, function() {
					$(this).removeClass('hover');
				});
			};
		},
		
		_maxsize: function(win) {
			win.data('width', win.width()-2);
			win.data('height', win.height()-2);
			win.data('margin-left', win.css('margin-left'));
			win.data('margin-top', win.css('margin-top'));
			
			var top = null;
			var left = null; 
			var topTo = 0;
			var leftTo = 0;
			
			top = parseInt(win.position().top)+this.op.split;
			left = parseInt(win.position().left)+this.op.split;
			
			win.data('top', top);
			win.data('left', left);
			win.css({
				position:'absolute',
				top:top,
				left:left,
				margin:0,
				zIndex:100
			});
			
			topTo += this.op.winMarginTop;
			leftTo += this.op.winMarginLeft;
			
			if (!$.browser.msie) {
				topTo = this.op.parentPadding;
				leftTo = this.op.parentPadding;
			}
			
			
			if (top != topTo || left != leftTo) {
				win.animate({"left":leftTo, "top":topTo }, this.op.speed, function() {
					$.myHome._maxsizeWin(win);
				});
			}
			else {
				$.myHome._maxsizeWin(win);
			} 
			
		},
		
		_maxsizeWin: function(win) {
			var w = this._getMaxWidth();
			var h = this._getMaxHeight();
			
			$.myHome._resize(win, w, h);
			$.myHome.maxWin = win;
		},
		
		_getMaxWidth: function() {
			return $(this.op.parent).width()-2*this.op.split;
		},
		
		_getMaxHeight: function() {
			return $(this.op.parent).height() - 2*this.op.split;
		},
		
		_restore: function(win) {
			var top = win.data('top');
			var left = win.data('left');
			
			this._resize(win, win.data('width'), win.data('height'));
			
			win.animate({"left":left, "top":top }, this.op.speed, function() {
				$.myHome._restoreWin(win);
			});
		},
		
		_restoreWin: function(win) {
			win.css({
				position:'static',
				'margin-left': win.data('margin-left'),
				'margin-top': win.data('margin-top')
			});
			this.maxWin = null;
		},
		
		_adapitveWindowSize: function(win, width, height) {
			// 将窗口划分成 12 * 2格子
			var gridWidth = ((this._getMaxWidth()+this.op.split )/ this.op.sizeWidth);
			var gridHeight = ((this._getMaxHeight()+this.op.split) / this.op.sizeHeight);
			
			
			// 单个调整窗口大小
			if (win) {
				win.data('wSize', width);
				win.data('hSize', height);             
				width = this._countSize(gridWidth, this.op.winMarginLeft, width);
				height = this._countSize(gridHeight, this.op.winMarginTop, height);
				this._resize(win, width, height);
				win.css({
					'margin-left':this.op.winMarginLeft+'px',
					'margin-top':this.op.winMarginTop+'px'
				});
				win.show();
				this.windows.push(win);
				
				return true;
			}
			
			for(var i=0; i<this.windows.length; i++) {
				var win = this.windows[i];
				width = win.data('wSize');
				height = win.data('hSize');
				width = this._countSize(gridWidth, this.op.winMarginLeft, width);
				height = this._countSize(gridHeight, this.op.winMarginTop, height);
				
				// 已经最大化的窗口改变大小
				if (this.maxWin && this.maxWin[0].id == win[0].id) {
					win.data('width', width);
					win.data('height', height);
				}
				else {
					this._resize(win, width, height);
					win.css({
						'margin-left':this.op.winMarginLeft+'px',
						'margin-top':this.op.winMarginTop+'px'
					});
				}
				
			}
		},
		
		_countSize: function(grid, padding, count) {
			return parseInt(count * grid - padding-2);
		},
		
		_load: function(win, src) {
			var iframe = $('iframe', win);
			iframe[0].src = src;
			iframe[0].style.marginLeft="4px";
			iframe[0].style.marginTop="4px";
		},
		
		_resize: function(win, width, height) {
			
			// 如果有固定宽度或者高度
			if (width) this._resizeWidth(win, width);
			if (height) this._resizeHeight(win, height);
			
		},
		
		_resizeHeight: function(obj, height) {
			if (!height) return obj;
			
			// 调整窗口高度
			$(this.op.border, obj).height(height);
			
			// 同时调整窗口内IFRMAE高度
			$('iframe', obj)[0].height = height - this.op.titleHeight - this.op.sideWidthBottom - this.op.sideWidthTop;
			return obj;
		},
		
		_resizeWidth: function(obj, width) {
			if (!width) return obj;
			
			// 调整窗口宽度
			$(this.op.content, obj).width(width);
			$(obj).width(width+2);
			
			// 同时调整窗口内IFRMAE宽度 
			$('iframe', obj)[0].width = width - this.op.sideWidthLeft - this.op.sideWidthRight;
			return obj;
		}
	};
});