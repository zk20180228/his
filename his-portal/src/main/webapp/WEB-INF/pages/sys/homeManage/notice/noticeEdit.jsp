<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<!-- 引入两个js -->
<script type="text/javascript" src="<%=path%>/ueditor1_4_3_2/ueditor.config.js"></script>
<script type="text/javascript" src="<%=path%>/ueditor1_4_3_2/ueditor.all.min.js"></script>
<script type="text/javascript" src="<%=path%>/ueditor1_4_3_2/ueditor.all.js"></script>
<script type="text/javascript">
var fileServersURL = "${fileServersURL}";
var acount = '${acount }';
UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
UE.Editor.prototype.getActionUrl = function(action) {
    if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadimage') 
    {
        return '<%=basePath%>sys/ueditorUpload/upload.action?comefrom=0&acount='+acount;
    }
    else if(action == 'catchimage')
    { 
    	var editor = UE.getEditor("editor");
    	var content = editor.getContent();
    	var imgs = UE.dom.domUtils.getElementsByTagName(this.document, "img");
    	var source =new Array();
    	$.each(imgs,function(index, element){
    		var src=$(element).attr('src');
    		source.push(src);
    	});
    	var url = fileServersURL+"/uploadFile/catcherImgUpload.action?comefrom=0&acount="+acount;
    	UE.ajax.getJSONP(url, {
    		source:source,fileServersURL:fileServersURL
        }, function(result) {
        	var list = result.list;
        	for(var i=0;i<imgs.length;i++){
        		var img = imgs[i];
        		$(img).attr('_src',list[i].source);
        		$(img).attr('src',list[i].source);
        	} 
        });
    	return;
    }
    else if(action=="uploadfile"||action=="listfile")
    {
    	 return '<%=basePath%>sys/ueditorUpload/uploadfile.action?comefrom=0&acount='+acount;
    }
    else
    {
        return this._bkGetActionUrl.call(this, action);
    }
}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文本编辑</title>
<style type="text/css">
.accordion .accordion-body {
    border-width: 0px 0 0px;
}
</style>
</head>
<body style="overflow-x:hidden;overflow-y:auto;">
<div style="padding: 20px;">
	<form id="editForm" method="post">
		<div style="margin-top: 10px;">
			<h3>标题：</h3>
			<input  id="infoTitle" name="info.infoTitle" value="${info.infoTitle}"
				class="easyui-textbox" data-options="required:true" style="width: 70%;height: 25px">
		</div>
		<div style="margin-top: 10px;">
			<h3>内容：</h3>
			<table style="width: 100%">
				<tr>
					<td>
						<textarea id="editor" style="width: 100%">${content}</textarea>
						<input type="hidden" id="infoContent" name="content"/>
						<input type="hidden" name='info.id' value="${info.id}"/>
					</td>
				</tr>
			</table>
		</div>
		<div style="margin-top: 10px;" class="noticeEditFont">
			<span>文章类型：</span>
			<input type="radio" id="infoType1" name="info.infoType" value=1
				<c:if test="${info.infoType == 1}">checked="checked"</c:if> />
			<label for="infoType1">通知</label>
			<input type="radio" id="infoType3" name="info.infoType" value=2 
				<c:if test="${info.infoType == 2}">checked="checked"</c:if> />
			<label for="infoType2">公告</label>
			<input type="radio" id="infoType2" name="info.infoType" value=3 
				<c:if test="${info.infoType == 3}">checked="checked"</c:if> />
			<label for="infoType2">新闻</label>
			<input type="radio" id="infoType4" name="info.infoType" value=4 
				<c:if test="${info.infoType == 4}">checked="checked"</c:if> />
			<label for="infoType2">医疗前沿</label>
			<input type="radio" id="infoType5" name="info.infoType" value=5 
				<c:if test="${info.infoType == 5}">checked="checked"</c:if> />
			<label for="infoType2">信息提醒</label>
		</div>
		<div class="easyui-accordion" data-options="multiple:true" style="width:100%;height1:300px;margin-top: 10px;">
			<div title="常用选项" data-options="iconCls:'icon-ok'" style="overflow:auto;padding:10px;">
				<div style="margin-top: 10px;">
					<div style="padding: 5">
						<span>简介：&nbsp</span>
						<input id="infoBrev" name="info.infoBrev" value="${info.infoBrev }" 
							class="easyui-textbox" style="width: 70%;">
					</div>
					<div style="padding: 5">
						<span>关键字：</span>
						<input id="infoKeyword" name="info.infoKeyword" value="${info.infoKeyword }"
							 class="easyui-textbox" style="width: 70%;">
					</div>
					<div style="padding: 5">
						<span>来源：&nbsp</span>
						<input id="infoSource" name="info.infoSource" value="${info.infoSource }"
							 class="easyui-textbox" style="width: 70%;">
					</div>
				</div>				
			</div>
		</div>
	</form>
	<div style="margin-top: 10px;">
		<a id='' href="javascript:saveForm(1)" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload'" >发布</a>
		<a id='' href="javascript:saveForm(0)" class="easyui-linkbutton" data-options="iconCls:'icon-disk_download'" >存为草稿</a>
		<a id='' href="javascript:funCancel()" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" >取消</a>
	</div>
</div>
<script type="text/javascript">
//获取浏览器宽度
UE.getEditor("editor",{
     initialFrameHeight: 300,
     focus: true
     , toolbars: [[
	          'fullscreen', 'source', '|', 'undo', 'redo', '|',
	          'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
	          'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
	          'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
	          'directionalityltr', 'directionalityrtl', 'indent', '|',
	          'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
	          'link', 'unlink', 'anchor', '|', 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
	          'simpleupload', 'insertimage', 'emotion',
	          'attachment', 'map',
	          'insertframe', 
	          'template', 'background', '|',
	          'horizontal', 'date', 'time', 'spechars', 
	          '|','inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts', '|',
	          'print', 'preview', 'searchreplace', 
      ]]
});

/***
 * 发布操作
 */
function saveForm(param){
	$('#editForm').form('submit',{    
	    url:'<%=basePath%>sys/noticeManage/save.action?info.infoPubflag='+param,   
	    onSubmit: function(){ 
	    	var isValid = $(this).form('validate');
			if (!isValid){
				$.messager.show({
					title:'友情提示',
					msg:'请输入标题!',
					timeout:5000,
					showType:'slide'
				});
				return isValid;
			}
	    	//将内容赋值
	    	var chtml = UE.getEditor('editor').getContent();
	    	var imgs = $(chtml).find('img');
	    	for(var i=0;i<imgs.length;i++){
	    		var img = imgs[i];
	    		var src = $(img).attr('src');
	    		var servers = src.split('/');
	    		if("fileTypeImages"==servers[servers.length-2]){
	    			continue;
	    		}
	    		var newsrc = "";
	    		for(var j=servers.length-1;j>=0;j--){
		    		newsrc = "/"+servers[j]+newsrc;
		    		if(servers[j]=="upload"){
		    			chtml = chtml.replace(src,newsrc);
		    			newsrc = "";
		    			break;
		    		}
	    		}
	    	}
	    	var ahrefs = $(chtml).find('a');
	    	for(var i=0;i<ahrefs.length;i++){
	    		var ahref = ahrefs[i];
	    		var src = $(ahref).attr('href');
	    		var servers = src.split('/');
	    		var newsrc = "";
	    		for(var j=servers.length-1;j>=0;j--){
		    		newsrc = "/"+servers[j]+newsrc;
		    		if(servers[j]=="upload"){
		    			chtml = chtml.replace(src,newsrc);
		    			newsrc = "";
		    			break;
		    		}
	    		}
	    	}
	    	$('#infoContent').val(chtml);
	    	if(chtml.length == 0){
	    		$.messager.show({
					title:'友情提示',
					msg:'请输入内容!',
					timeout:5000,
					showType:'slide'
				});
	    		return false;
	    	}
	    	if(!($("#infoType1").is(':checked') || $("#infoType2").is(':checked') || $("#infoType3").is(':checked')|| $("#infoType4").is(':checked')|| $("#infoType5").is(':checked'))){
	    		$.messager.show({
					title:'友情提示',
					msg:'请选择文章类型!',
					timeout:5000,
					showType:'slide'
				});
	    		return false;
	    	}
	    }, 
	    success:function(data){  
	    	data = jQuery.parseJSON(data);
	    	if(data.resCode == 0){
				$.messager.show({
					title:'友情提示',
					msg:'操作完成！',
					timeout:5000,
					showType:'slide'
				});
				funCancel();
	    	}else{
	    		$.messager.show({
					title:'友情提示',
					msg:data.resMsg,
					timeout:5000,
					showType:'slide'
				});
	    	}
	    }    
	}); 
}

/***
 * 取消操作
 */
function funCancel(){
	//获取当前选项卡状态
	var currTab = window.parent.$('#tabs').tabs('getSelected');
	var currindex = window.parent.$('#tabs').tabs('getTabIndex',currTab);
	
	//刷新通知管理选项卡
	if (window.parent.$('#tabs').tabs('exists','通知管理')){
		window.parent.$('#tabs').tabs('select','通知管理');
		var currTab = window.parent.$('#tabs').tabs('getSelected');
		var url = $(currTab.panel('options').content).attr('src');
		if (url != undefined && currTab.panel('options').title != 'Home') {
			window.parent.$('#tabs').tabs('update', {
				tab : currTab,
				options : {
					content : window.parent.createFrame(url)
				}
			});
		}
	}
	//关闭当前选项卡
	window.parent.$('#tabs').tabs('close', currindex);
}


</script>
</body>
</html>