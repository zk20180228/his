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
		<div id="panelEast" class="easyui-panel devicetEditFont" title="编辑" style="padding: 10px; background: #fafafa;" data-options="fit:'true',border:'false'">
			<form id="deviceForm" action="" method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" value="${device.id }">
					<input type="hidden" name="deviceState" id="deviceState" value="${device.deviceState }">
					<input type="hidden" name="applAcc" id="applAcc" value="${device.applAcc }">
					<input type="hidden" name="applName" id="applName" value="${device.applName }">
					<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%">
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">办公用途：</span>
						</td>
						<td style="text-align: left;">
							<input type="hidden" id="officeCode" value="${device.officeCode }" name="officeCode">
							<input class="easyui-combogrid" id="officeName" name="officeName" value="${device.officeName }" data-options="required:true,missingMessage:'请选择办公用途!'" style="width: 200px"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">设备分类：</span>
						</td>
						<td style="text-align: left;">
							<input type="hidden" id="classCode" value="${device.classCode }" name="classCode">
							<input class="easyui-textbox" type="text" value="${device.className }" name="className" id="className" data-options="required:true,missingMessage:'请填写设备分类!'" style="width: 200px" readonly="readonly"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">设备名称：</span>
						</td>
						<td style="text-align: left;">
							<input type="hidden" id="deviceCode" value="${device.deviceCode }" name="deviceCode">
							<input class="easyui-textbox" type="text" value="${device.deviceName }" name="deviceName" id="deviceName" data-options="required:true,missingMessage:'请填写设备名称!'" style="width: 200px" readonly="readonly"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">计量单位：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${device.meterUnit }" id="meterUnit" name="meterUnit" data-options="required:true,missingMessage:'请填写计量单位!'" style="width: 200px" readonly="readonly"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">所属仓库：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" id="depotName" name="depotName" value="${device.depotName}" data-options="required:true,missingMessage:'请选择所属仓库!'" style="width: 200px"/>
							<input type="hidden" id="depotCode" name="depotCode" value="${device.depotCode}" /></td>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">采购单价(元)：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-numberbox" type="text" value="${device.purchPrice }" id="purchPrice" name="purchPrice" data-options="required:true,missingMessage:'请填写采购单价!',min:0,precision:2,prompt:'请输入数字'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">入库数量：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-numberbox" type="text" value="${device.deviceNum }" id="deviceNum" name="deviceNum" data-options="required:true,missingMessage:'请填写入库数量!',min:0,prompt:'请输入整数'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">采购总价(元)：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" readonly="readonly" value="${device.purchTotal }" id="purchTotal" name="purchTotal" data-options="required:true,missingMessage:'请填写采购总价!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">折旧年限：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${device.depreciation }" id="depreciation" name="depreciation" data-options="required:true,missingMessage:'请填写折旧年限!'" style="width: 200px"></input>
						</td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<a id="hospitalOKbtn" href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="onClickOKbtnDraft()" class="easyui-linkbutton">保存草稿</a>
					<a id="hospitalOKbtn" href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="onClickOKbtnStorage()" class="easyui-linkbutton">提交申请</a>
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
		
		//仓库下拉框渲染
		$('#depotName').combobox({
			url : "<%=basePath%>assets/depot/findAllDepot.action",
			valueField : 'depotName',
			textField : 'depotName',
			multiple : false,
			onSelect : function(data){
				$('#depotCode').val(data.depotCode);
			},
		});
		
		var name = $.trim($('#officeName').val());
		$('#officeName').combogrid({
			  url:'<%=basePath%>assets/assetsPurchase/queryAssetsDeviceseRvice.action', 
			    queryParams:{officeName:name},	
			    editable:true,
				pagination:true,
	    		required:true,
	    		pageSize:10,
				pageList:[10,30,50,80,100],
	    		panelWidth:500,
	    		panelHeight:320,
	            idField:'officeName',    
	            textField:'officeName', 
	            mode:'remote',
			    columns:[[    
			        {field:'officeName',title:'办公用途',width:200},    
			        {field:'classCode',title:'类别代码',width:100},    
			        {field:'className',title:'设备分类',width:100},    
			        {field:'deviceName',title:'设备名称',width:200}, 
			        {field:'meterUnit',title:'计量单位',width:100}  
			    ]],
			    onClickRow:function(rowIndex, rowData){
			    	$('#officeCode').val(rowData.officeCode);
			    	$('#classCode').val(rowData.classCode);
			    	$('#className').textbox('setValue',rowData.className);
			    	$('#deviceCode').val(rowData.deviceCode);
			    	$('#deviceName').textbox('setValue',rowData.deviceName);
			    	$('#meterUnit').textbox('setValue',rowData.meterUnit);
			    },
		})
	});
	//申请
	function onClickOKbtnStorage() {
		$('#deviceState').val('1');
		var url;
		url = '<%=basePath %>assets/device/saveOrupdateDevice.action';
			$('#deviceForm').form('submit', {
				url : url,
				data : $('#deviceForm').serialize(),
				dataType : 'json',
				onSubmit : function() {
					if (!$('#deviceForm').form('validate')) {
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
					$('#listStorage').datagrid('load', '<%=basePath %>assets/device/queryAssetsDeviceStorage.action');
					closeLayout();
					$.messager.alert('提示',"操作成功!");
				},
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert('提示',"操作失败!");
				}
			});
	}
	//草稿
	function onClickOKbtnDraft() {
		$('#deviceState').val('0');
		var url;
		url = '<%=basePath %>assets/device/saveOrupdateDevice.action';
			$('#deviceForm').form('submit', {
				url : url,
				data : $('#deviceForm').serialize(),
				dataType : 'json',
				onSubmit : function() {
					if (!$('#deviceForm').form('validate')) {
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
					$('#listDraft').datagrid('load', '<%=basePath %>assets/device/queryAssetsDeviceDraft.action');
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
		$('#deviceForm').form('clear');
	}
	//计算金额
	function changeCost(){
		var deviceNum = $("#deviceNum").numberbox("getValue");
        var purchPrice = $("#purchPrice").numberbox("getValue");
        if(deviceNum!=null && deviceNum!='' && deviceNum!=undefined){
        	var cost = (deviceNum*purchPrice).toFixed(2)
        	$('#purchTotal').textbox('setValue',cost);
        }
	}
	$('#deviceNum').numberbox({  
	    onChange: function(value) {
	    	changeCost();        
	    }
	});
	$('#purchPrice').numberbox({  
	    onChange: function(value) {
	    	changeCost();        
	    }
	});
</script>
	</body>
</html>
