function ProInfoDashboard(options) {
	var _this = this;

	var _funs = {
		log : function(obj) {
			var $span = $(this);
			$.getJSON($span.attr('url'), {
				'pageNum' : parseInt($span.attr('pageNum')) + 1
			}, function(json) {
				var $table = $('#pro_info_dashboard_table');

				var arr = [];

				$.each(json.logs, function(index, log) {
					arr.push("<tr><td>" + _funs.getUserName(log.usercode) + " " + log.optmethod + "</td>");
					arr.push("<td>" + log.optcontent + "</td></tr>");
				});

				$table.append(arr.join(''));

				$span.attr('pageNum', parseInt($span.attr('pageNum')) + 1);

				var len = $table.find('tr').size();
				if (len == json.pageDesc.totalRows) {
					$span.addClass('hidden');
				}

			});
		},

		getUserName : function(usercode) {
			var username = null;
			$.each(options.users, function(index, u) {
				if (usercode == u.usercode) {
					username = u.username;
					return false;
				}
			});

			return username;
		}
	};

	var _events = {
		init : function() {
			for ( var e in _events) {
				if ('init' != e) {
					_events[e]();
				}
			}
		},

		logBind : function() {
			$('#pro_info_dashboard_pageNo').click(_funs.log);
		}
	};

	this.init = function() {
		_events.init();
	};

}

function D(obj) {
	console.error(obj);
}