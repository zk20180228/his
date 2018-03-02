<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>添加页面</title>
	</head>
	<body>
		<div id="p" class="easyui-panel" title="编辑" style="padding: 10px; background: #fafafa;" data-options="fit:'true',border:'false'">
			<form id="hospitalForm" action="" method="post">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">数量:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="hospitalID" class="easyui-textbox" value="${financeFixedcharge.chargeAmount}" data-options="required:true,missingMessage:'请填写数量!'" style="width: 200px" name="chargeAmount">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">单价:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${financeFixedcharge.chargeUnitprice}" name="chargeUnitprice" data-options="required:true,missingMessage:'请填写单价!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">开始时间:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-datebox"  value="${financeFixedcharge.sDate}" name="chargeStarttime" data-options="required:true,missingMessage:'请选择开始时间!'" style="width: 200px"></input>

						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">结束时间:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-datebox" value="${financeFixedcharge.eDate }" name="chargeEndtime" data-options="required:true,missingMessage:'请选择结束时间!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">是否与婴儿相关:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${financeFixedcharge.chargeIsaboutchildren}" name="chargeIsaboutchildren" data-options="required:true,missingMessage:'是否与婴儿相关!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">是否与时间相关:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${financeFixedcharge.chargeIsabouttime }" name="chargeIsabouttime" data-options="required:true,missingMessage:'是否与时间相关!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">状态:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${financeFixedcharge.chargeState }" name="chargeState" data-options="required:true,missingMessage:'请填写状态!'" style="width: 200px"></input>
						</td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
					<a id="hospitalOKbtn" href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="onClickOKbtn()" class="easyui-linkbutton">确定</a>
					<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-clear'" onclick="clear()" class="easyui-linkbutton">清空</a>
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
	function onClickOKbtn() {
		var url;
		if (addAndEdit == 0) {//增加
			url = 'drug/finance/saveOrupdataFinanceFixedcharge.action';
		} else {//修改
			url = 'drug/finance/saveOrupdataFinanceFixedcharge.action?Id=' + getbachIdUtil("#list", 1, 0);
		}
		$('#hospitalForm').form('submit', {
			url : url,
			data : $('#hospitalForm').serialize(),
			dataType : 'json',
			onSubmit : function() {
				if (!$('#hospitalForm').form('validate')) {
					$.messager.alert('提示',"验证没有通过,不能提交表单!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
			},
			success : function(data) {
				$.messager.progress('close');
				$('#list').datagrid('load', 'drug/finance/queryFinanceFixedcharge.action');
				closeLayout();
				$.messager.show({
					title : '提示信息',
					msg : '操作成功!'
				});
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.show({
					title : '提示信息',
					msg : '操作失败!'
				});
			}
		});
	}
	/**
	 *
	 *连续添加
	 *
	 */
	function addContinue() {
		if (addAndEdit == 0) {
			$('#hospitalForm').form('submit', {
				url : 'drug/finance/saveOrupdataFinanceFixedcharge.action',
				data : $('#hospitalForm').serialize(),
				dataType : 'json',
				onSubmit : function() {
					if (!$('#hospitalForm').form('validate')) {
						$.messager.alert('提示',"验证没有通过,不能提交表单!");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}
				},
				success : function(data) {
					$('#list').datagrid('load', 'drug/finance/queryFinanceFixedcharge.action');
					$.messager.show({
						title : '提示信息',
						msg : '操作成功!'
					});
				},
				error : function(data) {
					$.messager.show({
						title : '提示信息',
						msg : '操作失败!'
					});
				}
			});
		}else{
			$.messager.show({
				title : '提示信息',
				msg : '添加按钮不能执行修改操作!'
			});
		}
	}
	//清除所填信息
	function clear() {
		$('#hospitalForm').form('clear');
	}
</script>
	</body>
</html>
