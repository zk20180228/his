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
							<span style="font-size: 13">患者姓名:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" value="${patient.patientName }" name="patientName" data-options="required:true,missingMessage:'请填写患者姓名!'"></input>
						</td>
						<td class="honry-lable">
							<span style="font-size: 13">拼音码:</span>&nbsp;&nbsp;
						</td>
						<td class="honry-lable">
							<input id="patientPinyin" value="${patient.patientPinyin }" name="patientPinyin" class="easyui-textbox" data-options="required:true,missingMessage:'请填写拼音码!'" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">五笔码:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" value="${patient.patientWb }" name="patientWb" data-options="required:true,missingMessage:'请填写五笔码!'"></input>
						</td>
						<td class="honry-lable">
							<span style="font-size: 13">自定义码:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" value="${patient.patientInputcode }" name="patientInputcode" data-options="required:true,missingMessage:'请填写自定义码!'"></input>

						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">性别:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="cc" class="easyui-combobox" name="patientSex" data-options="valueField:'id',textField:'text',url:'',required:true,missingMessage:'请选择性别!'" />
						</td>
						<td class="honry-lable">
							<span style="font-size: 13">出生日期:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="dd" value="${patient.patientDoorno }" name="patientBirthday" class="easyui-datebox" data-options="required:true,missingMessage:'请选择日期!'"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">家庭地址:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" value="${patient.patientAddress }" name="patientAddress" data-options="required:true,missingMessage:'请填写家庭地址!'"></input>
						</td>
						<td class="honry-lable">
							<span style="font-size: 13">门牌号:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" value="${patient.patientDoorno }" name="patientDoorno" data-options="required:true,missingMessage:'请填门牌号!'"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">电话:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="cost" class="easyui-textbox" value="${patient.patientPhone }" name="patientPhone" data-options="required:true,validType:'Phone',missingMessage:'请填电话!'"></input>
						</td>
						<td class="honry-lable">
							<span style="font-size: 13">证件类型 :</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="cc" class="easyui-combobox" name="patientCertificatestype" data-options="valueField:'id',textField:'text',url:'',required:true,missingMessage:'请选择证件类型!'" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">证件号码:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" value="${patient.patientCertificatesno }" name="patientCertificatesno" data-options="required:true,missingMessage:'请填写证件号码!'"></input>
						</td>
						<td class="honry-lable">
							<span style="font-size: 13">出生地:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="cc" class="easyui-combobox" name="patientBirthplace" data-options="valueField:'id',textField:'text',url:'',required:true,missingMessage:'请选择出生地!'" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">籍贯:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="cc" class="easyui-combobox" name="patientNativeplace" data-options="valueField:'id',textField:'text',url:'',required:true,missingMessage:'请选择籍贯!'" />
						</td>
						<td class="honry-lable">
							<span style="font-size: 13">国籍:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="cc" class="easyui-combobox" name="patientNationality" data-options="valueField:'id',textField:'text',url:'',required:true,missingMessage:'请选择国籍!'" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">民族:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="cc" class="easyui-combobox" name="patientNation" data-options="valueField:'id',textField:'text',url:'',required:true,missingMessage:'请选择民族!'" />
						</td>
						<td class="honry-lable">
							<span style="font-size: 13">工作单位:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" value="${patient.patientWorkunit }" name="patientWorkunit" data-options="required:true,missingMessage:'请填写工作单位!'"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">单位电话:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" value="${patient.patientWorkphone }" name="patientWorkphone" data-options="required:true,validType:'Phone',missingMessage:'请填写单位电话!'"></input>
						</td>
						<td class="honry-lable">
							<span style="font-size: 13">婚姻状况:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="cc" class="easyui-combobox" name="patientWarriage" data-options="valueField:'id',textField:'text',url:'',required:true,missingMessage:'请选择婚姻状态!'" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">职业:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="cc" class="easyui-combobox" name="patientOccupation" data-options="valueField:'id',textField:'text',url:'',required:true,missingMessage:'请选择职业!'" />
						</td>
						<td class="honry-lable">
							<span style="font-size: 13">医保手册号:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" value="${patient.patientHandbook }" name="patientHandbook" data-options="required:true,missingMessage:'请填写医保手册!'"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">电子邮箱:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" value="${patient.patientEmail }" name="patientEmail" data-options="required:true,validType:'email',missingMessage:'请填写电子邮箱!'"></input>
						</td>
						<td class="honry-lable">
							<span style="font-size: 13">母亲姓名:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" value="${patient.patientMother }" name="patientMother" data-options="required:true,missingMessage:'请填写母亲姓名!'"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">联系人:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" value="${patient.patientLinkman }" name="patientLinkman" data-options="required:true,missingMessage:'请填写联系人!'"></input>
						</td>
						<td class="honry-lable">
							<span style="font-size: 13">联系人关系:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="cc" class="easyui-combobox" name="patientLinkrelation" data-options="valueField:'id',textField:'text',url:'',required:true,missingMessage:'请选择联系人关系!'" />

						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">联系人地址:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" value="${patient.patientLinkaddress }" name="patientLinkaddress" data-options="required:true,missingMessage:'请填写联系人地址!'"></input>
						</td>
						<td class="honry-lable">
							<span style="font-size: 13">联系人门牌号:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" value="${patient.patientLinkdoorno }" name="patientLinkdoorno" data-options="required:true,missingMessage:'请填写联系人门牌号!'"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">联系电话:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" value="${patient.patientLinkphone }" name="patientLinkphone" data-options="required:true,validType:'Phone',missingMessage:'请请填写联系电话!'"></input>
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
			url = 'savePatient.action';
		} else {//修改
			url = 'editPatient.action?Id=' + getbachIdUtil("#list", 1, 0);
		}
		$('#hospitalForm').form('submit', {
			url : url,
			data : $('#hospitalForm').serialize(),
			dataType : 'json',
			onSubmit : function() {
				/*if (!$('#hospitalForm').form('validate')) {
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					return false;
				}*/
			},
			success : function(data) {
				$('#list').datagrid('load', 'listPatient.action');
				closeLayout();
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
	}
	/**
	 *
	 *连续添加
	 *
	 */
	function addContinue() {
		if (addAndEdit == 0) {
			$('#hospitalForm').form('submit', {
				url : 'savePatient.action',
				data : $('#hospitalForm').serialize(),
				dataType : 'json',
				onSubmit : function() {
					if (!$('#hospitalForm').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
				},
				success : function(data) {
					$('#list').datagrid('load', 'listPatient.action');
					closeLayout();
					$.messager.show({
						title : '提示信息',
						msg : '添加成功!'
					});
				},
				error : function(data) {
					$.messager.show({
						title : '提示信息',
						msg : '添加失败!'
					});
				}
			});
		} else {
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
