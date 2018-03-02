<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>文本框</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1" >
    <meta name="generator" content="www.leipi.org" />
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
    <script type="text/javascript">
    function onlyNum(){ 
	    if(!(event.keyCode == 46) && !(event.keyCode == 8) && !(event.keyCode == 37) && !(event.keyCode == 39)) 
	    if(!((event.keyCode >= 48 && event.keyCode <= 57)||(event.keyCode >= 96 && event.keyCode <= 105))) 
	    event.returnValue=false; 
    } 
    
    </script>
</head>
<body>
<div class="content">
	<input id="hidname"  type="hidden"/>
	<input id="id"  type="hidden"/>
    <table class="table table-bordered table-striped" style="margin:2px;font-size: 17px;">
		<tr>
        	<th><span>控件名称</span> <span class="label label-important">*</span></th>
        	<td><input id="attrName" placeholder="控件名称" type="text" readonly="readonly" style="height: 30px"/></td>
    	</tr>
	    <tr>
	    	<th><span>长度</span></th>
	    	<td><input id="attrLength" type="number" readonly="readonly" style="height: 30px"/></td>
	    	<th><span>小数位</span></th>
	    	<td><input id="attrPrecision" type="number" readonly="readonly" style="height: 30px"/></td>
	    </tr>
	    <tr>
	    	<th><span>有效范围上限</span></th>
	    	<td><input id="attrValidup" type="number" readonly="readonly" style="height: 30px"/></td>
	    	<th><span>有效范围下限</span></th>
	    	<td><input id="attrValiddown"  type="number" readonly="readonly" style="height: 30px"/></td>
	    </tr>
	    <tr>
	    	<th><span>控件内容</span></th>
	    	<td><input id="attrValue" type="number" style="height: 30px"/></td>
	    </tr>
	</table>
</div>
<script type="text/javascript">
	
	var attrKind = 0;
	var oNode = null,thePlugins = 'number';
	var gId,gCode,gTitle,gName,gInputCode,gCodetype,gPrefix,gSuffix,gValidup,gValiddown,gDateformat,gNotnullChecked,gMustSelectChecked,gStatflgChecked,gPrintflgChecked;
	window.onload = function() {
			oNode = UE.plugins[thePlugins].editdom.firstChild.nextSibling;
			number = oNode.firstChild.nextSibling;
			console.log(oNode);
			//获取属性值
			gId = oNode.getAttribute('id');
			gCode = oNode.getAttribute('code').replace(/&quot;/g,"\"");
			gTitle = oNode.getAttribute('title').replace(/&quot;/g,"\"");
			gName = oNode.getAttribute('name').replace(/&quot;/g,"\"");
			gInputCode = oNode.getAttribute('inputcode').replace(/\"/g,"&quot;");
			gCodetype = oNode.getAttribute('codetype').replace(/\"/g,"&quot;");
			gLength = oNode.getAttribute('length');
			gPrecision = oNode.getAttribute('precision');
			gValidup = oNode.getAttribute('validup');
			gValiddown = oNode.getAttribute('validdown');
			gPrefix = oNode.getAttribute('prefix').replace(/\"/g,"&quot;");
			gSuffix = oNode.getAttribute('suffix').replace(/\"/g,"&quot;");
			gNotnullChecked = oNode.getAttribute('notnullchecked');
			gMustSelectChecked = oNode.getAttribute('mustselectchecked');
			gStatflgChecked = oNode.getAttribute('statflgchecked');
			gPrintflgChecked = oNode.getAttribute('printflgchecked');
			if(gValiddown != 'undefined' && gValiddown != ''){
				$('#attrValiddown').val(gValiddown);
				$('#attrValue').attr("min",gValiddown);
			}
			if(gValidup != 'undefined' && gValidup != ''){
				$('#attrValidup').val(gValidup);
				$('#attrValue').attr("max",gValidup);
			}
			if(gLength != 'undefined' && gLength != ''){
				$('#attrLength').val(gLength);
				$('#attrValue').attr("maxlength",gLength);
			}
			var step = 1;
			if(gPrecision != 'undefined' && gPrecision != '' && gPrecision > 0){
				$('#attrPrecision').val(gPrecision);
				if(gPrecision != '' && gPrecision > 0){
					for(var i = 0; i < gPrecision; i++){
						step = step/10;
					}
				}
				$('#attrValue').attr("step",step);
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
	
		var min = '';
		if(gValiddown != ''){
			min = ' min:"'+gValiddown+'"';
		}
		var max = '';
		if(gValidup != ''){
			max = ' max="'+gValidup+'"';
		}
		var step = 1;
		if(gPrecision != '' && gPrecision > 0){
			for(var i = 0; i < gPrecision; i++){
				step = step/10;
			}
		}
		step = ' step="'+ step+'"';
        var gName=oNode.getAttribute('name').replace(/&quot;/g,"\"");
        var label = oNode.parentNode;
        var html = '{请输入'+gTitle+'<span leipiplugins="number" title="'+gTitle+'" code="'+gCode+'" id="'+gId+
       	'" name="leipiNewField" inputcode="'+gInputCode+'" displaytype="1" codetype="'+gCodetype+'" attrKind="0'+
       	'" length="'+gLength+'" precision="'+gPrecision+'" validup="'+gValidup+'" validdown="'+gValiddown+
       	'" prefix="'+gPrefix+'" suffix="'+gSuffix+'" notnullChecked="'+gNotnullChecked+'" attrtype="6"'+
       	' mustSelectChecked="'+gMustSelectChecked+'" statflgChecked="'+gStatflgChecked+
       	'" printflgChecked="'+gPrintflgChecked+'" >'+gPrefix+'<input type="number" id="'+gCode+'" placeholder="'+gTitle+'"'+min+max+step+value+' readonly="readonly"/>'+gSuffix+'</span>}';
       	label.innerHTML = html;
        delete UE.plugins[thePlugins].editdom; 
        return true;
    }
</script>
</body>
</html>