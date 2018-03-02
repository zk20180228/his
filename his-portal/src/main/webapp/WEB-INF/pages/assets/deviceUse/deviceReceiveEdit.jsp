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
		<title>添加页面</title>
	</head>
	<body>
		<div id="panelEast" class="easyui-panel deviceDossiertEditFont" title="编辑" style="padding: 10px; background: #fafafa;" data-options="fit:'true',border:'false'">
			<form id="deviceDossierForm" action="" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" value="${deviceDossier.id }">
					<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%">
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">领用科室：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceDossier.useDeptName }" id="useDeptName" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">领用人：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceDossier.useName }" id="useName" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">领用数量：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-numberbox" type="text" value="${deviceDossier.useDeptCode }" id="useNum" name="useNum" data-options="required:true,missingMessage:'请填写领用数量!'" style="width: 200px"></input>
							<input type="hidden" id="afterNum" value="${deviceDossier.useDeptCode }">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">联系电话：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceDossier.useAcc }" id="useAcc" style="width: 200px"></input>
						</td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<a id="hospitalOKbtn" href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="onClickOKbtn()" class="easyui-linkbutton">保存</a>
					<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeLayout()" class="easyui-linkbutton">关闭</a>
				</div>
			</form>
		</div>
		<script type="text/javascript">
	$(function() {
		$.extend($.fn.validatebox.defaults.rules, {
			email : {
				validator : function(value) { //email验证	
					return /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(value);
				},
				message : '请输入有效的邮箱账号(例：123456@qq.com)'
			},
			Phone : function(value) {
				var rex = /^1[3-8]+\d{9}$/;
				//var rex=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
				//区号：前面一个0，后面跟2-3位数字 ： 0\d{2,3}
				//电话号码：7-8位数字： \d{7,8
				//分机号：一般都是3位数字： \d{3,}
				//这样连接起来就是验证电话的正则表达式了：/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/		 
				var rex2 = /^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
				if (rex.test(value) || rex2.test(value)) {
					return true;
				} else {
					return false;
				}

			},
			message : '请输入正确电话或手机格式'
		});
	});
	//保存
	function onClickOKbtn() {
		var url;
		url = '<%=basePath %>assets/deviceDossier/saveDeviceUse.action';
			$('#deviceDossierForm').form('submit', {
				url : url,
				data : $('#deviceDossierForm').serialize(),
				dataType : 'json',
				onSubmit : function() {
					if($('#useNum').val()-$('#afterNum').val()>0){
						$.messager.alert('提示',"申请数量大于库存数量!");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
		  		 		return false ;
		  		 	}
					if (!$('#deviceDossierForm').form('validate')) {
						$.messager.alert('提示',"验证没有通过,不能提交表单!");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});	//显示进度条
				},
				success : function(data) {
					$.messager.progress('close');
					$('#listReceive').datagrid('reload');
					closeLayout();
					$.messager.alert('提示',"操作成功!");
				},
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert('提示',"操作失败!");
				}
			});
	}
	//清除所填信息
	function clears() {
		$('#deviceDossierForm').form('clear');
	}
	
</script>
	</body>
</html>
