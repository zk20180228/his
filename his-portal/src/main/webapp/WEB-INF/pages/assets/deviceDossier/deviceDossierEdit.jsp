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
					<input type="hidden" name="id" value="${deviceDossier.id }"></input>
					<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%">
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">办公用途编码：</span>
						</td>
						<td style="text-align: left;">
							<input id="officeCode" class="easyui-textbox" value="${deviceDossier.officeCode }" data-options="required:true,missingMessage:'请填写办公用途编码!'" style="width: 200px" name="officeCode"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">办公用途名称：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceDossier.officeName }" name="officeName" id="officeName" data-options="required:true,missingMessage:'请填写办公用途名称!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">设备分类编码：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceDossier.classCode }" name="classCode" id="classCode" data-options="required:true,missingMessage:'请填写设备分类编码!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">设备分类名称：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceDossier.className }" name="className" id="className" data-options="required:true,missingMessage:'请填写设备分类名称!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">设备条码号：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceDossier.deviceNo }" name="deviceNo" id="deviceNo" data-options="required:true,missingMessage:'请填写设备条码号!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">设备代码：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceDossier.deviceCode }" name="deviceCode" id="deviceCode" data-options="required:true,missingMessage:'请填写设备代码!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">设备名称：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceDossier.deviceName }" name="deviceName" id="deviceName" data-options="required:true,missingMessage:'请填写设备名称!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">计量单位：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceDossier.meterUnit }" id="meterUnit" name="meterUnit" data-options="required:true,missingMessage:'请填写计量单位!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">采购单价(元)：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-numberbox"  value="${deviceDossier.purchPrice }" id="purchPrice" name="purchPrice" data-options="required:true,width:300,min:0.00,precision:2 ,prompt:'请输入采购单价'" style="width: 200px"></input>
						</td>
					</tr>
<!-- 					<tr> -->
<!-- 						<td class="honry-lable"> -->
<!-- 							<span style="font-size: 13">领用部门编码：</span> -->
<!-- 						</td> -->
<!-- 						<td style="text-align: left;"> -->
<%-- 							<input class="easyui-textbox" type="text" value="${deviceDossier.useDeptCode }" id="useDeptCode" name="useDeptCode" data-options="required:true,missingMessage:'请填写领用部门编码!'" style="width: 200px"></input> --%>
<!-- 						</td> -->
<!-- 					</tr> -->
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">领用部门名称：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-combobox" value="${deviceDossier.useDeptCode }" id="deptCode" name="useDeptCode" data-options="required:true,missingMessage:'请填写领用部门姓名!'" style="width: 200px"></input>
							<input type="hidden" value="${deviceDossier.useDeptName }" id="useDeptName" name="useDeptName"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">领用人工号：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceDossier.useAcc }" id="useAcc" name="useAcc" data-options="required:true,missingMessage:'请填写领用人工号!'" style="width: 200px"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span style="font-size: 13">领用人姓名：</span>
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" type="text" value="${deviceDossier.useName }" id="useName" name="useName" data-options="required:true,missingMessage:'请填写领用人姓名!'" style="width: 200px"></input>
						</td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<a id="hospitalOKbtn" href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="onClickOKbtnStorage()" class="easyui-linkbutton">提交</a>
					<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-clear'" onclick="clears()" class="easyui-linkbutton">清空</a>
					<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeLayout()" class="easyui-linkbutton">关闭</a>
				</div>
			</form>
		</div>
<script type="text/javascript">
var havSelect = true;
	//申请
	function onClickOKbtnStorage() {
		var url;
		url = '<%=basePath %>assets/deviceDossier/saveOrupdateDeviceDossier.action';
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
	//清除所填信息
	function clears() {
		$('#deviceDossierForm').form('clear');
	}
	//关闭Layout
	function closeLayout(){
		$('#divLayout').layout('remove', 'east');
	}
	//科室下拉框及时定位查询 
	$('#deptCode').combobox({
		url: "<%=basePath%>baseinfo/department/departmentCombobox.action", 
		valueField : 'deptCode',
		textField : 'deptName',
		filter:function(q,row){
			var keys = new Array();
			keys[keys.length] = 'deptCode';
			keys[keys.length] = 'deptName';
			keys[keys.length] = 'deptPinyin';
			keys[keys.length] = 'deptWb';
			keys[keys.length] = 'deptInputcode';
			if(filterLocalCombobox(q, row, keys)){
				row.selected=true;
			}else{
				row.selected=false;
			}
			return filterLocalCombobox(q, row, keys);
	    },
		onLoadSuccess:function(data){
			if(data!=null && data.length==1){
				var code= data[0].deptCode;
				$('#deptCode').combobox('select',code);
			}
		},
		onSelect:function(rec){
			var code=rec.deptCode;
			havSelect = false;
			$('#useDeptName').val(rec.deptName);
		},
		onHidePanel:function(){
		 	var data = $(this).combobox('getData');
		    var val = $(this).combobox('getValue');
		    var result = true;
		    for (var i = 0; i < data.length; i++) {
		        if (val == data[i].deptCode) {
		            result = false;
		        }
		    }
		    if (result) {
		        $(this).combobox("clear");
		    }else{
		        $(this).combobox('unselect',val);
		        $(this).combobox('select',val);
		    }
			if(havSelect){
				var isOnly = 0;
				var onlyOne = null;
				for(var i = 0;i<$("#deptCode").combobox("getData").length;i++){
					if($("#deptCode").combobox("getData")[i].selected){
						isOnly++;
						onlyOne = $("#deptCode").combobox("getData")[i];
					}
				}
				if((isOnly-1)==0){
					var depCode = onlyOne.deptCode;
					$('#deptCode').combobox('setValue',deptMap[depCode]);
					$('#deptCode').combobox('select',depCode);
				}
			}
			havSelect=true;							
		}
	});
	 function filterLocalCombobox(q, row, keys){
			if(keys!=null && keys.length > 0){
				for(var i=0;i<keys.length;i++){
					if(row[keys[i]]!=null&&row[keys[i]]!=''){
						var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1||row[keys[i]].indexOf(q) > -1;
						if(istrue==true){
							return true;
						}
					}
				}
			}else{
				var opts = $(this).combobox('options');
				return row[opts.textField].indexOf(q.toUpperCase()) > -1||row[opts.textField].indexOf(q) > -1;
			}
		}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>
