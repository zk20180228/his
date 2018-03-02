<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>文本框</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1" >
     <link rel="stylesheet" href="../bootstrap/css/bootstrap.css">
    <!--[if lte IE 6]>
    <link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap-ie6.css">
    <![endif]-->
    <!--[if lte IE 7]>
    <link rel="stylesheet" type="text/css" href="bootstrap/css/ie.css">
    <![endif]-->
    <link rel="stylesheet" href="../leipi.style.css">
    <script type="text/javascript" src="../../dialogs/internal.js"></script>
    <link id="easyuiTheme" rel="stylesheet" type="text/css" href="../../../themes/easyui/metro/easyui.css" ></link>
    <script type="text/javascript" src="../../../javascript/js/jquery.min.js"></script>
    <script type="text/javascript" src="../../../javascript/js/jquery.easyui.min.js"></script>
</head>
<body>
<div class="content">
	<input id="hidname"  type="hidden"/>
	<input id="id"  type="hidden"/>
    <table class="table table-bordered table-striped" style="margin:2px;">
		<tr>
        	<th><span>控件名称</span> <span class="label label-important">*</span></th>
        	<td><input id="attrName" placeholder="控件名称" type="text" readonly="readonly" /></td>
    	</tr>
    	<tr>
        	<th><span>控件内容</span> <span class="label label-important">*</span></th>
        	<td><input id="attrValue" placeholder="控件内容" type="text"  /></td>
    	</tr>
    </table>
</div>
<script type="text/javascript">
	var attrKind;
	var oNode = null,thePlugins = 'text';
	var gId,gCode,gTitle,gName,gInputCode,gCodetype,gPrefix,gSuffix,gNotnullChecked,gMustSelectChecked,gStatflgChecked,gPrintflgChecked;
	window.onload = function() {
			oNode = UE.plugins[thePlugins].editdom.firstChild.nextSibling;
			text = oNode.firstChild.nextSibling;
			//获取属性值
			gId = oNode.getAttribute('id');
			gCode = oNode.getAttribute('code').replace(/&quot;/g,"\"");
			gTitle = oNode.getAttribute('title').replace(/&quot;/g,"\"");
			gName = oNode.getAttribute('name').replace(/&quot;/g,"\"");
			gInputCode = oNode.getAttribute('inputcode').replace(/\"/g,"&quot;");
			gCodetype = oNode.getAttribute('codetype').replace(/\"/g,"&quot;");
			gPrefix = oNode.getAttribute('prefix').replace(/\"/g,"&quot;");
			gSuffix = oNode.getAttribute('suffix').replace(/\"/g,"&quot;");
			gNotnullChecked = oNode.getAttribute('notnullchecked');
			gMustSelectChecked = oNode.getAttribute('mustselectchecked');
			gStatflgChecked = oNode.getAttribute('statflgchecked');
			gPrintflgChecked = oNode.getAttribute('printflgchecked');
			gValue = text.getAttribute('value');
			if(gTitle != null && gTitle != ""){
				$('#attrName').val(gTitle);
			}
			if(gValue != null && gValue != ""){
				$('#attrValue').val(gValue);
			}
			
	}
	dialog.oncancel = function () {
		if( UE.plugins[thePlugins].editdom ) {
			delete UE.plugins[thePlugins].editdom;
		}
	};
	dialog.onok = function (){
		var attrValue = $('#attrValue').val();
		var value = '';
		if(attrValue != ''){
			value = ' value="'+attrValue+'"';
		}else if(gNotnullChecked == 1){
			$.messager.alert('提示','控件内容不能为空');
			return false;
		}
		var gName=oNode.getAttribute('name').replace(/&quot;/g,"\"");
		   var label = oNode.parentNode;
	        var html = '{请输入'+gTitle+'<span leipiplugins="text" title="'+gTitle+'" code="'+gCode+'" id="'+gId+
        	'" name="leipiNewField" inputcode="'+gInputCode+'" displaytype="1" codetype="'+gCodetype+
        	'" attrkind="0" prefix="'+gPrefix+'" suffix="'+gSuffix+'" notnullChecked="'+gNotnullChecked+'" attrtype="4"'+
        	' mustSelectChecked="'+gMustSelectChecked+'" statflgChecked="'+gStatflgChecked+
        	'" printflgChecked="'+gPrintflgChecked+'" >'+gPrefix+'<input type="text" id="'+gCode+'" placeholder="'+gTitle+'"'+value+' readonly="readonly"/>'+gSuffix+'</span>}';
        	label.innerHTML = html;
		delete UE.plugins[thePlugins].editdom; 
		return true;
	}
</script>
</body>
</html>