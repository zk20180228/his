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
		<div id="panelEast" class="easyui-panel deviceCodetEditFont" title="编辑" style="padding: 10px; background: #fafafa;" data-options="fit:'true',border:'false'">
			<form id="deviceCodeForm" action="" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" value="${deviceCode.id }">
					<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%">
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">办公用途编码：</span>
						</td>
						<td style="text-align: left;">
							<input id="officeCode" class="easyui-textbox" value="${deviceCode.officeCode }" data-options="required:true,missingMessage:'请填写办公用途编码!'" style="width: 200px" name="officeCode" readonly="readonly">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">办公用途名称：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceCode.officeName }" name="officeName" id="officeName" data-options="required:true,missingMessage:'请填写办公用途名称!'" style="width: 200px" readonly="readonly"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">设备分类编码：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceCode.classCode }" name="classCode" id="classCode" data-options="required:true,missingMessage:'请填写设备分类编码!'" style="width: 200px" readonly="readonly"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">设备分类名称：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceCode.className }" name="className" id="className" data-options="required:true,missingMessage:'请填写设备分类名称!'" style="width: 200px" readonly="readonly"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">设备条码号：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceCode.deviceNo }" name="deviceNo" id="deviceNo" data-options="required:true,missingMessage:'请填写设备条码号!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">设备代码：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceCode.deviceCode }" name="deviceCode" id="deviceCode" data-options="required:true,missingMessage:'请填写设备代码!'" style="width: 200px" readonly="readonly"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">设备名称：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceCode.deviceName }" name="deviceName" id="deviceName" data-options="required:true,missingMessage:'请填写设备名称!'" style="width: 200px" readonly="readonly"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">计量单位：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceCode.meterUnit }" id="meterUnit" name="meterUnit" data-options="required:true,missingMessage:'请填写计量单位!'" style="width: 200px" readonly="readonly"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">采购单价(元)：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceCode.purchPrice }" id="purchPrice" name="purchPrice" data-options="required:true,missingMessage:'请填写采购单价!'" style="width: 200px" readonly="readonly"></input>
						</td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<a id="hospitalOKbtn" href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="onClickOKbtn()" class="easyui-linkbutton">确定</a>
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
		if($('#stopFlgHidden').val()==1){
			$('#stopFlg').attr("checked", true); 
		}
	});
	function onClickOKbtn() {
		var url;
		url = '<%=basePath %>assets/deviceCode/saveOrupdateDeviceCode.action';
			$('#deviceCodeForm').form('submit', {
				url : url,
				data : $('#deviceCodeForm').serialize(),
				dataType : 'json',
				onSubmit : function() {
					if (!$('#deviceCodeForm').form('validate')) {
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
					reload();
					closeLayout();
					$.messager.alert('提示',"操作成功!");
				},
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert('提示',"操作失败!");
				}
			});
	}
	/**
	* @Description 可选标志的渲染
	* @author   
	*/
	function onclickBox(id){
		if($('#'+id).is(':checked')){
			$('#'+id+'Hidden').val(1);
		 }else{
			$('#'+id+'Hidden').val(0);
		}
	}
</script>
	</body>
</html>
