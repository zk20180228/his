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
		<div id="panelEast" class="easyui-panel depottEditFont" title="编辑" style="padding: 10px; background: #fafafa;" data-options="fit:'true',border:'false'">
			<form id="depotForm" action="" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" value="${depot.id }">
					<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%">
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">仓库编码：</span>
						</td>
						<td style="text-align: left;">
							<input id="depotCode" class="easyui-textbox" value="${depot.depotCode }" data-options="required:true,missingMessage:'请填写仓库编码!'" style="width: 200px" name="depotCode">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">仓库名称：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${depot.depotName }" name="depotName" id="depotName" data-options="required:true,missingMessage:'请填写仓库名称!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">仓库地点：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${depot.address }" name="address" id="address"  style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">仓库管理员工号：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${depot.manageAcc }" name="manageAcc" id="manageAcc"  style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">仓库管理员姓名：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${depot.manageName }" name="manageName" id="manageName"  style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">联系电话：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${depot.phone }" name="phone" id="phone"  style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							停用标志:
						</td>
						<td style="text-align: left;">
							<input type="hidden" id="stopFlgHidden" name="depot.stop_flg" value="${depot.stop_flg}"/>
							<input type="checkBox" id="stopFlg" onclick="javascript:onclickBox('stopFlg')"/>
						</td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<c:if test="${depot.id == null}">
						<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
					</c:if>
					<a id="hospitalOKbtn" href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="onClickOKbtn()" class="easyui-linkbutton">确定</a>
					<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-clear'" onclick="clears()" class="easyui-linkbutton">清空</a>
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
		url = '<%=basePath %>assets/depot/saveOrupdateDepot.action';
			$('#depotForm').form('submit', {
				url : url,
				data : $('#depotForm').serialize(),
				dataType : 'json',
				onSubmit : function() {
					if (!$('#depotForm').form('validate')) {
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
					$('#list').datagrid('load', '<%=basePath %>assets/depot/queryAssetsDepot.action');
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
	 *
	 *连续添加
	 *
	 */
	function addContinue() {
		if (addAndEdit == 0) {
			$('#depotForm').form('submit', {
				url : '<%=basePath %>assets/depot/saveOrupdateDepot.action',
				data : $('#depotForm').serialize(),
				dataType : 'json',
				onSubmit : function() {
					if (!$('#depotForm').form('validate')) {
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
					$('#list').datagrid('load', '<%=basePath %>assets/depot/queryAssetsDepot.action');
					$.messager.alert('提示',"操作成功!");
					clears();
				},
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert('提示',"操作失败!");
				}
			});
		} else {
			$.messager.alert('操作提示',"添加按钮不能执行修改操作!");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	//清除所填信息
	function clears() {
		$('#depotForm').form('clear');
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
