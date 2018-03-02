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
</head>
<body>
<div class="content">
	<form id="editForm" method="post">
		<input id="attrType" type="hidden" name="attr.attrType" value="6"/>
	    <input id="attrId" type="hidden" name="attr.attrId" value="${attr.attrId }"/>
    	<input id="attrNotnull" type="hidden" name="attr.attrNotnull" value="${attr.attrNotnull }"/>
    	<input id="attrMustSelect" type="hidden" name="attr.attrMustSelect" value="${attr.attrMustSelect }"/>
    	<input id="attrStatflg" type="hidden" name="attr.attrStatflg" value="${attr.attrStatflg }"/>
    	<input id="attrPrintflg" type="hidden" name="attr.attrPrintflg" value="${attr.attrPrintflg }"/>
	    <table class="honry-table" style="margin-left:auto;margin-right:auto;border-top:0">
			<tr>
	        	<td style="border-top:0" class="honry-lable"><span>控件代码</span> <span class="label label-important">*</span></td>
	        	<td style="border-top:0"><input id="attrCode" data-options="prompt:'控件代码'" type="text" class="easyui-textbox" name="attr.attrCode" value="${attr.attrCode }"/></td>
	        	<td style="border-top:0" class="honry-lable"><span>控件名称</span> <span class="label label-important">*</span></td>
	        	<td style="border-top:0"><input id="attrName" data-options="prompt:'控件名称'" type="text" class="easyui-textbox" name="attr.attrName" value="${attr.attrName }"/></td>
	    	</tr>
    		<tr>
		    	<td class="honry-lable"><span>自定义码</span></td>
		    	<td><input id="inputcode" data-options="prompt:'自定义码'" type="text" class="easyui-textbox" name="attr.inputcode" value="${attr.inputcode }"/></td>
		    	<td class="honry-lable"><span>编码系统</span></td>
		    	<td><input id="attrCodetype" data-options="prompt:'编码系统'" type="text" class="easyui-textbox" name="attr.attrCodetype" value="${attr.attrCodetype }"/></td>
		    </tr>
		    <tr>
		    	<td class="honry-lable"><span>长度</span></td>
		    	<td><input id="attrLength" type="text" class="easyui-numberbox" data-options="min:0,precision:0,prompt:'长度'" onkeydown="onlyNum();" name="attr.attrLength" value="${attr.attrLength }" style="ime-mode:Disabled" /></td>
		    	<td class="honry-lable"><span>小数位</span></td>
		    	<td><input id="attrPrecision" type="text" class="easyui-numberbox" data-options="min:0,precision:0,prompt:'小数位'" onkeydown="onlyNum();" name="attr.attrPrecision" value="${attr.attrPrecision }" style="ime-mode:Disabled" /></td>
		    </tr>
		    <tr>
		    	<td class="honry-lable"><span>有效范围上限</span></td>
		    	<td><input id="attrValidup" type="text" class="easyui-numberbox" data-options="min:0,precision:0,prompt:'有效范围上线'" name="attr.attrValidup" value="${attr.attrValidup }"/></td>
		    	<td class="honry-lable"><span>有效范围下限</span></td>
		    	<td><input id="attrValiddown" type="text" class="easyui-numberbox" data-options="min:0,precision:0,prompt:'有效范围下限'" name="attr.attrValiddown" value="${attr.attrValiddown }"/></td>
		    </tr>
		    <tr>
		    	<td class="honry-lable"><span>前缀</span></td>
		    	<td><input id="attrPrefix" data-options="prompt:'前缀'" type="text" class="easyui-textbox" name="attr.attrPrefix" value="${attr.attrPrefix }"/></td>
		    	<td class="honry-lable"><span>后缀</span></td>
		    	<td><input id="attrSuffix" data-options="prompt:'后缀'" type="text" class="easyui-textbox" name="attr.attrSuffix" value="${attr.attrSuffix }"/></td>
		    </tr>
    		<tr>
    			<td class="honry-lable"><span>是否必选必填</span></td>
		    	<td><input id="attrNotnull1" checked="checked" name="attrNotnull" type="radio" value="1">  是  <input id="attrNotnull0" name="attrNotnull" type="radio" value="0"> 否</td>
		    	<td class="honry-lable"><span>是否必须选择</span></td>
		    	<td><input id="attrMustSelect1" checked="checked" name="attrMustSelect" type="radio" value="1"> 是 <input id="attrMustSelect0" name="attrMustSelect" type="radio" value="0"> 否 </td>
		    </tr>
    		<tr>
    			<td class="honry-lable"><span>是否用于科研</span></td>
		    	<td><input id="attrStatflg1" checked="checked" name="attrStatflg" type="radio" value="1"> 是 <input id="attrStatflg0" name="attrStatflg" type="radio" value="0"> 否</td>
		    	<td class="honry-lable"><span>是否打印</span></td>
		    	<td><input id="attrPrintflg1" checked="checked" name="attrPrintflg" type="radio" value="1"> 打印 <input id="attrPrintflg0" name="attrPrintflg" type="radio" value="0"> 不打印 </td>
		    </tr>
		</table>
	</form>
		<div style="padding-top: 5px ;text-align: center;" >
	        <a href="javascript:void(0)" onclick="submit()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">保存</a>
	        <a href="javascript:void(0)" onclick="closeLayout()" class="easyui-linkbutton" data-options="iconCls:'icon-close',plain:true">取消</a>
		</div>
	</div>
<script type="text/javascript">
//页面加载
$(function(){
	var gNotnullChecked = $('#attrNotnullChecked').value;
	var gMustSelectChecked = $('#attrMustSelectChecked').value;
	var gStatflgChecked = $('#attrStatflgChecked').value;
	var gPrintflgChecked = $('#attrPrintflgChecked').value;
	
	gNotnullChecked == 0? $('#attrNotnullChecked0').checked = true : $('#attrNotnullChecked1').checked = true;
	gMustSelectChecked == 0? $('#attrMustSelectChecked0').checked = true : $('#attrMustSelectChecked1').checked = true;
	gStatflgChecked == 0? $('#attrStatflgChecked0').checked = true : $('#attrStatflgChecked1').checked = true;
	gPrintflgChecked == 0? $('#attrPrintflgChecked0').checked = true : $('#attrPrintflgChecked1').checked = true;

});
/**
 * 判断添加/修改表单提交
 */
function submit(){ 
	if( $('#attrCode').value == '') {
		$.messager.alert('提示','控件代码不能为空！'); 
		setTimeout(function(){$(".messager-body").window('close')},3500);
        return false;
    }
    if( $('#attrName').value == '') {
    	$.messager.alert('提示','控件名称不能为空！'); 
    	setTimeout(function(){$(".messager-body").window('close')},3500);
        return false;
    }
    $('#attrNotnull').val($("input[name='attrNotnull']:checked").val());
	$('#attrMustSelect').val($("input[name='attrMustSelect']:checked").val());
	$('#attrStatflg').val($("input[name='attrStatflg']:checked").val());
	$('#attrPrintflg').val($("input[name='attrPrintflg']:checked").val());
    var url;
	if($('#attrId').val() ==''){
	    $.ajax({
			url: '<%=basePath %>emrs/attr/queryAttr.action?queryCode='+$('#attrCode').val(),
			success: function(data) {
				if(data.total > 0){
					$.messager.alert('提示',"您输入的控件编码已存在！！！！");
					setTimeout(function(){$(".messager-body").window('close')},3500);
					return false;
				}else{
					 url = "<%=basePath %>emrs/attr/add.action"; 
					 sub(url);
				}
			},
	    	error: function(){
	    	}
		});
	}else{
		url = "<%=basePath %>emrs/attr/edit.action";
 		sub(url);
	}
}	
	/**
	* 表单提交
	*/
	function sub(url) {
		$('#editForm').form('submit',{  
        	url: url,  
	        success:function(data){  
	        	closeLayout();
	        },
			error : function(data) {
				$.messager.alert("提示",'保存失败！');	
			}							         
   		}); 
	}

	/* 
	* 关闭界面
	*/
	function closeLayout(){
		$('#temWins').dialog('close'); 
		$('#list').datagrid('reload');
	}
	
	 function onlyNum(){ 
			if(!(event.keyCode == 46) && !(event.keyCode == 8) && !(event.keyCode == 37) && !(event.keyCode == 39)) 
			if(!((event.keyCode >= 48 && event.keyCode <= 57)||(event.keyCode >= 96 && event.keyCode <= 105))) 
			event.returnValue=false; 
		} 
</script>
</body>
</html>