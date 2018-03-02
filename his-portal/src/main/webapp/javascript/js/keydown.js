//jQuery(function() {
//	jQuery('input:text:first').focus();
//	var $inp = jQuery('input:text');
//	$inp.bind('keydown', function(e) {
//		var key = e.which;
//		if (key == 13) {
//			e.preventDefault();
//			var nxtIdx = $inp.index(this) + 1;
//			jQuery(":input:text:eq(" + nxtIdx + ")").focus();
//		}
//	});
//});
/**
*监听键盘回车事件
**调用举例：
*	$(function(){
*		bindEnterEvent('idno',query,'easyui'); 
*	});
*targetIdName input元素的id
*callback 回调函数
*type input使用框架类型  easyui,无框架   若class="easyui-validatebox",则按普通元素处理
**/
function bindEnterEvent(targetId,callback,type){
	if(targetId==null||$.trim(targetId)==""){
		return;
	}
	if($('#'+targetId)==null){
		return;
	}
	if(type=="easyui"){
		$('#'+targetId).textbox('textbox').bind('keyup', function(event) {
	        if (event.keyCode == "13") {
	        	if(callback instanceof Function){
	        		callback();
	        		return;
	        	}
	        }
    	});
	}else{
		$('#'+targetId).bind('keyup', function(event) {
	        if (event.keyCode == "13") {
	        	if(callback instanceof Function){
	        		callback();
	        		return;
	        	}
	        }
    	});
	}
}
/**
*监听键盘空格事件  
*调用举例：
*	$(function(){
*		bindBlackEvent('idno',query,'easyui'); 
*	});
*targetIdName input元素的id
*callback 回调函数
*type input使用框架类型  easyui,无框架 若class="easyui-validatebox",则按普通元素处理
**/
function bindBlackEvent(targetId,callback,type){
	if(targetId==null||$.trim(targetId)==""){
		return;
	}
	if($('#'+targetId)==null){
		return;
	}
	if(type=="easyui"){
		$('#'+targetId).textbox('textbox').bind('keyup', function(event) {
	        if (event.keyCode == "32") {
	        	if(callback instanceof Function){
	        		callback();
	        		return;
	        	}
	        }
    	});
	}else{
		$('#'+targetId).bind('keyup', function(event) {
	        if (event.keyCode == "32") {
	        	if(callback instanceof Function){
	        		callback();
	        		return;
	        	}
	        }
    	});
	}
}
/**
*监听键盘回车和空格事件  
*调用举例：
*	$(function(){
*		bindEnterAndBlackEvent('idno',query,'easyui'); 
*	});
*targetIdName input元素的id
*callback 回调函数
*type input使用框架类型  easyui,无框架
**/
function bindEnterAndBlackEvent(targetId,callback,type){
	if(targetId==null||$.trim(targetId)==""){
		return;
	}
	if($('#'+targetId)==null){
		return;
	}
	if(type=="easyui"){
		$('#'+targetId).textbox('textbox').bind('keyup', function(event) {
	        if (event.keyCode == "13") {
	        	if(callback instanceof Function){
	        		callback();
	        		return;
	        	}
	        }
	        if (event.keyCode == "32") {
	        	if(callback instanceof Function){
	        		callback();
	        		return;
	        	}
	        }
    	});
	}else{
		$('#'+targetId).bind('keyup', function(event) {
			if (event.keyCode == "13") {
	        	if(callback instanceof Function){
	        		callback();
	        		return;
	        	}
	        }
	        if (event.keyCode == "32") {
	        	if(callback instanceof Function){
	        		callback();
	        		return;
	        	}
	        }
    	});
	}
	
}