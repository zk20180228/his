var xform;

window.onload = function() {
	xform = new xf.Xform('xf-form');
	xform.addSection(new xf.TextSection('h1', 'title'));
	xform.addSection(new xf.GridSection(2, 4));

	xform.initEvents();

	xform.selectionListener = {
		select: function(field) {
			if(!field) {
				return;
			}
			var el = xf.$('xf-form-attribute');
			field.viewForm(el);
		}
	};

	xform.render();

	xf.$('xFormName').onblur = function() {
		xform.name = this.value;
	}
	xf.$('xFormCode').onblur = function() {
		xform.code = this.value;
	}
	xf.$('xFormmula').onblur = function() {
		xform.mula = this.value;
	}
	if($('#__gef_content__').val() != null && $('#__gef_content__').val() != ''){
		xform.doImport($('#__gef_content__').val());
		xf.$('xFormCode').value = xform.code;
		xf.$('xFormName').value = xform.name;
		xf.$('xFormmula').value = xform.mula;
	}else{
		document.getElementById("xFormName").placeholder = "请输入表名字"
		document.getElementById("xFormCode").placeholder = "请输入表标识"
		document.getElementById("xFormmula").placeholder = "请输入计算公式(可选)"
	}

	$(".xf-pallete").on("mousedown",function(){
		xform.mode = 'EDIT';
	})
	$("#xf-form").on("mousedown",function(){
		xform.mode = 'MERGE';
	})
}

function doImport() {
	var value = prompt('text', '{"name":"name","code":"code","mula":"mula","sections":[{"type":"text","tag":"h1",text:"title"},{"type":"grid","row":"2",col:"4","fields":[{"type":"label","row":0,"col":0,"text":"11111111111"},{"type":"textfield","row":0,"col":1,"name":"test","required":true}]}]}');
	if(value != '') {
		xform.doImport(value);
		xf.$('xFormName').value = xform.name;
		xf.$('xFormCode').value = xform.code;
		xf.$('xFormmula').value = xform.mula;
	}
}
//合并
function doMerge() {
	if(xform.mode == 'MERGE') {
		xform.doMerge();
		xform.doImport(xform.doExport());
	}
}
//拆分
function doSplit() {
	if(xform.mode == 'MERGE') {
		xform.doSplit();
		xform.doImport(xform.doExport());
	}
}
//添加列
function addRow (){
	xform.addRow()
	xform.doImport(xform.doExport());
}
//删除列
function removeRow (){
	xform.removeRow()
	xform.doImport(xform.doExport());
}
//添加行
function addCol (){
	xform.addCol()
	xform.doImport(xform.doExport());
}
//删除行
function removeCol (){
	xform.removeCol()
	xform.doImport(xform.doExport());
}
function closeWin(){
	window.close();
}

function doSave(url) {
	if(xform.code!=null&&xform.code!=''){
		xf.$('__gef_code__').value = xform.code;
	}else{
		$.alert('提示','表单标识不能为空!');
		return;
	}
	if(xform.name!=null&&xform.name!=''){
		xf.$('__gef_name__').value = xform.name;
	}else{
		$.alert('提示','表单名称不能为空!');
		return;
	}
	if(xform.mula!=null&&xform.mula!=''){
		xf.$('__gef_mula__').value = xform.mula;
	}
	$('#__gef_content__').val(xform.doExport());
	$.ajax({    
		url:url,
		type:'POST',    
		data:new FormData($("#f")[0]),
		async:false,    
		cache:false,    
		contentType:false,    
		processData:false,    
		success: function (dataMap) {    
			if(dataMap.resCode=="success"){
				window.opener.loadDatagrid();
				$.alert('提示',dataMap.resMsg,function(){window.close();});
			}else{
				$.alert('提示',dataMap.resMsg);
			}
		},    
		error: function () {    
			$.alert('提示','请求失败!');
		}    
	});
}