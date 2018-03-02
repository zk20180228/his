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
<link rel="stylesheet" type="text/css" href="<%=basePath %>themes/system/css/public.css">
<style type="text/css">
	table.honry-table{
		width: 0%	
	}
</style>
<title></title>
<script type="text/javascript">
	$(function(){
		$('#acceptUser').textbox({
			editable : false,
			onClickButton : function(){
				var width = 1050;
				var height = 600;
				var top = (window.screen.availHeight-30-height)/2;
				var left = (window.screen.availWidth-10-width)/2;
				//打开用户选择页面
				openWindow("<%=basePath%>messagerSend/toUserSelect.action","请选择接收人",width,height,top,left);
			}
		});
	});
	function openWindow(url,name,width,height,top,left){
		window.open(url, name, 'height=' + height + ',,innerHeight=' + height + ',width=' + width + ',innerWidth=' + width + ',top=' + top + ',left=' + left + ',status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no'); 
	}
	function showbackUser(value){
		$('#acceptUser').textbox('setValue',value);
	};
	function msgsend(){
		var acceptUser = $('#acceptUserH').val();
		var content = $('#content').val();
		var definedaccept = $('#definedaccept').val();
		if((acceptUser==null||acceptUser == "")&&(definedaccept==null || definedaccept == "")){
			$.messager.alert('提示','接收人不能为空...',"info");
			return ;
		}
		if(content==null||content == ""){
			$.messager.alert('提示','请输入短信发送内容...',"info");
			return ;
		}
		$.messager.progress({text:'发送中,请稍候...',modal:true});
		$.ajax({
			url : "<%=basePath%>messagerSend/msgsendbyjsp.action",
			data:{acceptUser : acceptUser,content : content,definedaccept : definedaccept},
			type : "post",
			success : function(result){
				$.messager.alert("提示",result.resMsg,'info');
				$.messager.progress("close");
				return ;
			},
			error : function(){
				$.messager.progress("close");
				$.messager.alert("提示","未知错误","info");
				return ;
			}
		});
	}
	function clearFrom(){
		$('#acceptUserH').val('');
		$('#content').val('');
		$('#acceptUser').textbox('setText','');
	}
</script>
</head>
<body style="text-align: center;padding-top: 2%">
<div align="center" style="border: 2px;">
	<form >
		<input type="hidden" id="acceptUserH">
		<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td class="honry-lable"  style="font-size: 14;width:150px">短信内容</td>
				<td><textarea id="content" rows="6" cols="50"></textarea></td>
			</tr>
			<tr>
				<td class="honry-lable"  style="font-size: 14;width:150px">接收人</td>
				<td><input id="acceptUser" class="easyui-textbox" data-options="buttonIcon :'icon-book'" style="width:422px"/></td>
			</tr>
			<tr>
				<td class="honry-lable"  style="font-size: 14;width:150px">自定义接收人</td>
				<td><textarea id="definedaccept" rows="6" cols="50"></textarea><br><font color="#808080">直接输入手机号，号码之间用英文","（逗号）隔开</font></td>
			</tr>
		</table>
	</form>
	<div data-options="region:'south',split:false" style="height:50px;border-width:1px 0 0 0;">
		<div style="text-align: center; padding: 20px;">
			<a href="javascript:msgsend();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_tick'">确定</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearFrom()" data-options="iconCls:'icon-clear'">清空</a>
		</div>	
	</div> 
</div>
</body>
</html>