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
		<title>编辑页面</title>
	</head>
	<body>
		<div id="panelEast" class="easyui-panel devicetEditFont" title="编辑" style="padding: 10px; background: #fafafa;" data-options="fit:'true',border:'false'">
			<form id="deviceDossierForm" action="" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" value="${assetsDeviceCheck.id }"></input>
					<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%">
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">办公用途：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${assetsDeviceCheck.officeName }" name="officeName" id="officeName" data-options="readonly:true" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">类别代码：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${assetsDeviceCheck.classCode }" name="classCode" id="classCode" data-options="readonly:true" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">设备分类：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${assetsDeviceCheck.className }" name="className" id="className" data-options="readonly:true" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">设备名称：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${assetsDeviceCheck.deviceName }" name="deviceName" id="deviceName" data-options="readonly:true" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">计量单位：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${assetsDeviceCheck.meterUnit }" id="meterUnit" name="meterUnit" data-options="readonly:true" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">库存数量：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${assetsDeviceCheck.reperNum }" id="reperNum" name="reperNum" data-options="readonly:true" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">实盘量：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-numberbox" type="text" value="${assetsDeviceCheck.checkNum }" id="checkNum" name="checkNum" data-options="required:true,missingMessage:'请填写实盘量!'" style="width: 200px"></input>
						</td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<a href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="onClickOKbtnStorage()" class="easyui-linkbutton">确定</a>
					<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeLayout()" class="easyui-linkbutton">取消</a>
				</div>
			</form>
		</div>
<script type="text/javascript">
var havSelect = true;
	function onClickOKbtnStorage() {
		var url;
		url = '<%=basePath %>assets/assetsDeviceCheck/saveOrUpdate.action';
			$('#deviceDossierForm').form('submit', {
				url : url,
				data : $('#deviceDossierForm').serialize(),
				dataType : 'json',
				onSubmit : function() {
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
					query();
					closeLayout();
					$.messager.alert('提示',"操作成功!");
				},
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert('提示',"操作失败!");
				}
			});
		}
	//关闭Layout
	function closeLayout(){
		$('#divLayout').layout('remove', 'east');
	}
</script>
</body>
</html>
