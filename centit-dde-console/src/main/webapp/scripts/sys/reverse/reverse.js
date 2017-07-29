/**
 * 反转Ajax
 */
$(function() {
	dwr.engine.setActiveReverseAjax(true);

});

jQuery.receive = {
	receiveMessages : function(message) {
		alert(message);
	}
};

