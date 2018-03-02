/** easyui-tabs-method-load.js  aizhonghua 2016-11-24 **/
(function () {
	$.extend($.fn.tabs.methods, {
		//显示遮罩  
		loading: function (jq, msg) {
			return jq.each(function () {
				var panel = $(this).tabs("getSelected");
				if (msg == undefined) {
					msg = "正在加载数据，请稍候...";  
				}
				$("<div class=\"datagrid-mask\"></div>").css({ display: "block", width: panel.width(), height: panel.height() }).appendTo(panel);  
				$("<div class=\"datagrid-mask-msg\"></div>").html(msg).appendTo(panel).css({ display: "block", left: (panel.width() - $("div.datagrid-mask-msg", panel).outerWidth()) / 2, top: (panel.height() - $("div.datagrid-mask-msg", panel).outerHeight()) / 2 });  
			});
		},
		//隐藏遮罩  
		loaded: function (jq) {  
				return jq.each(function () {
				var panel = $(this).tabs("getSelected");
				panel.find("div.datagrid-mask-msg").remove();
				panel.find("div.datagrid-mask").remove();
			});  
		}  
	});  
})(jQuery); 