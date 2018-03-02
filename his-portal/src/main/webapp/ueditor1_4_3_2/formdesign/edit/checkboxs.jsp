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
    <title>复选框</title>
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
		/*生成tr*/
		function fnAddComboTr(obj)
		{
		    var oTable = document.getElementById('options_table');
		    var new_tr_node= oTable.insertRow(oTable.rows.length);
		    var new_td_node0 = new_tr_node.insertCell(0),new_td_node1 = new_tr_node.insertCell(1);
		
		    var sChecked = '';
		    if(obj.checked) sChecked = 'checked="checked"';
		    if(!obj.name) obj.name = '';
		    if(!obj.value) obj.value = '';
		    new_td_node0.innerHTML = '<td><input name="checkbox" type="checkbox" '+sChecked+'></td>';
			new_td_node1.innerHTML = '<td><input type="text" name="text" value="'+obj.value+'" name="'+obj.name+'" placeholder="选项值"></td>';
		    return true;
		}
		/*组合checkbox*/
		function fnParseOptions(gChecked)
		{
			var disabled = ' disabled="true"';
		    var oTable = document.getElementById('options_table');
		    var nTr = oTable.getElementsByTagName('tr'),trLength = nTr.length,html="";
		    for(var i=0;i<trLength;i++)
		    {
		        var inputs = nTr[i].getElementsByTagName('input');
		        if(inputs.length>0)
		        {
		            if(!inputs[1].value) continue;
		            var sChecked = '';
		            if(inputs[0].checked) sChecked = 'checked="checked"';
		            html += '<input name="'+inputs[1].name+'" value="'+inputs[1].value+'" '+sChecked+' type="checkbox"'+disabled+'/>'+inputs[1].value+'&nbsp;';
		            if(gChecked=='orgchecked1')//竖排
		                html+='<br/>';
		        }
		    }
		    //alert(html);
		    return html;
		
		}
    </script>
</head>
<body>
<div class="content">
	    <input id="hidname"  type="hidden"/>
	    <input id="id"  type="hidden"/>
	    <table class="table table-bordered table-striped" style="margin:2px;">
			<tr>
	        	<th><span>控件名称</span> <span class="label label-important">*</span></th>
	        	<td><input id="attrName" placeholder="控件名称" type="text"/></td>
	    	</tr>
		</table>

        <table class="table table-hover table-condensed" id="options_table" style="margin:2px;">
            <tr>
                <th>是否选中</th>
                <th>选项值</th>
            </tr>
        </table>
	</div>
<script type="text/javascript">
	var attrKind = 0;
	var oNode = null,thePlugins = 'checkboxs';
	var gId,gCode,gTitle,gName,gInputCode,gCodetype,gPrefix,gSuffix,gNotnullChecked,gMustSelectChecked,gStatflgChecked,gPrintflgChecked;
	window.onload = function() {
		if( UE.plugins[thePlugins].editdom ){
	    	oNode = UE.plugins[thePlugins].editdom.firstChild.nextSibling;
    		
    		//获取属性值
    		gId = oNode.getAttribute('id');
    		gCode = oNode.getAttribute('code').replace(/&quot;/g,"\"");
    		gTitle = oNode.getAttribute('title').replace(/&quot;/g,"\"");
    		gName = oNode.getAttribute('name').replace(/&quot;/g,"\"");
    		gInputCode = oNode.getAttribute('inputcode').replace(/\"/g,"&quot;");
    		/* var gDisplaytype = oNode.getAttribute('displaytype'); */
    		gCodetype = oNode.getAttribute('codetype').replace(/\"/g,"&quot;");
    		gPrefix = oNode.getAttribute('prefix').replace(/\"/g,"&quot;");
    		gSuffix = oNode.getAttribute('suffix').replace(/\"/g,"&quot;");
    		gNotnullChecked = oNode.getAttribute('notnullchecked');
    		gMustSelectChecked = oNode.getAttribute('mustselectchecked');
    		gStatflgChecked = oNode.getAttribute('statflgchecked');
    		gPrintflgChecked = oNode.getAttribute('printflgchecked');
    		$('#attrName').val(gTitle);
    		
	        var inputTags = oNode.getElementsByTagName('input');
	        var length = inputTags.length;
	        var aInputs = [];
	        for(var i=0;i<length;i++)
	        {
	            fnAddComboTr(inputTags[i]);
	        }
	    }
	}
	dialog.oncancel = function () {
	    if( UE.plugins[thePlugins].editdom ) {
	        delete UE.plugins[thePlugins].editdom;
	    }
	};
	dialog.onok = function (){
        var gName=oNode.getAttribute('name').replace(/&quot;/g,"\"");
        oNode.innerHTML = fnParseOptions(gName);
        var options = fnParseOptions("leipiNewField",0);
        var gName=oNode.getAttribute('name').replace(/&quot;/g,"\"");
        oNode.innerHTML = fnParseOptions(gName);
        var label = oNode.parentNode;
           //使用边界，防止用户删除了span标签
           var html = '{请选择'+gTitle+'<span leipiplugins="checkboxs" title="'+gTitle+'" code="'+gCode+'" id="'+gId+
           	'" name="leipiNewField" inputcode="'+gInputCode+'" displaytype="1" codetype="'+gCodetype+'" attrKind="0" prefix="'+gPrefix+
           	'" suffix="'+gSuffix+'" notnullChecked="'+gNotnullChecked+'" attrtype="2"'+
           	' mustSelectChecked="'+gMustSelectChecked+'" statflgChecked="'+gStatflgChecked+
           	'" printflgChecked="'+gPrintflgChecked+'" >'+options+'</span>}';
           label.innerHTML = html;
        delete UE.plugins[thePlugins].editdom; 
        return true;
    }
</script>
</body>
</html>