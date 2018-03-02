/** easyui-datagrid-method-load.js  aizhonghua 2016-11-15 **/
(function (){ 
	$.extend($.fn.datagrid.methods, { 
		//显示遮罩 
		loading: function(jq){ 
			return jq.each(function(){ 
				$(this).datagrid("getPager").pagination("loading"); 
				var opts = $(this).datagrid("options"); 
				var wrap = $.data(this,"datagrid").panel; 
				if(opts.loadMsg){ 
					$("<div class=\"datagrid-mask\"></div>").css({display:"block",width:wrap.width(),height:wrap.height()}).appendTo(wrap); 
					$("<div class=\"datagrid-mask-msg\"></div>").html(opts.loadMsg).appendTo(wrap).css({display:"block",left:(wrap.width()-$("div.datagrid-mask-msg",wrap).outerWidth())/2,top:(wrap.height()-$("div.datagrid-mask-msg",wrap).outerHeight())/2}); 
				} 
			}); 
		}, 
		//隐藏遮罩 
		loaded: function(jq){ 
			return jq.each(function(){ 
				$(this).datagrid("getPager").pagination("loaded"); 
				var wrap = $.data(this,"datagrid").panel; 
				wrap.find("div.datagrid-mask-msg").remove(); 
				wrap.find("div.datagrid-mask").remove(); 
			}); 
		} 
	});
})(jQuery);