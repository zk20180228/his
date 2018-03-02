<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>退号</title>
<%@ include file="/common/metas.jsp"%>
	<script type="text/javascript">
		var deptMap = "";//科室
		var gradeMap = "";//级别
		var empMap = "";//专家
		var contMap="";//合同单位
		//加载退号列表
		$(function(){
			$.ajax({
				url: "<c:url value='/outpatient/changeDeptLog/querydeptComboboxs.action'/>", 
				type:'post',
				success: function(deptData) {
					deptMap = eval("("+deptData+")");
					$('#list').datagrid({
						url:"<c:url value='/outpatient/info/queryBackNo.action?menuAlias=${menuAlias}'/>",
						onClickRow: function (rowIndex, rowData) {
							$('#backName').text(rowData.patientId.patientName);
							$('#backFee').text(rowData.fee);
						}
					});
				}
			});	
			$.ajax({
				url: "<c:url value='/outpatient/changeDeptLog/querygradeComboboxs.action'/>",
				type:'post',
				success: function(gradeData) {
					gradeMap = eval("("+gradeData+")");
				}
			});	
			$.ajax({
				url: "<c:url value='/outpatient/changeDeptLog/queryempComboboxs.action'/>",
				type:'post',
				success: function(empData) {
					empMap = eval("("+empData+")");
				}
			});
			$.ajax({
				url: "<c:url value='/outpatient/changeDeptLog/querycontComboboxs.action'/>",
				type:'post',
				success: function(contData) {
					contMap = eval("("+contData+")");
				}
			});
			//回车事件
			bindEnterEvent('idcardId',searchFrom,'easyui');
		});
		//根据就诊卡号查询列表
		function searchFrom(){
			var idcardId = $('#idcardId').textbox('getValue');
			if(idcardId==null||idcardId==""){
				$.messager.alert("操作提示", "请输入卡号");
				return false;
			}
			$('#list').datagrid('load', {
				idcardId : idcardId,
			});
		}
		//渲染科室
		function functionDept(value,row,index){
			if(value!=null&&value!=''){
				return deptMap[value];
			}
		}	
		//渲染人员
		function functionEmp(value,row,index){
			if(value!=null&&value!=''){
				return empMap[value];
			}
		}	
		//渲染级别
		function functionGrade(value,row,index){
			if(value!=null&&value!=''){
				return gradeMap[value];
			}
		}	
		//渲染合同单位
		function functionConit(value,row,index){
			if(value!=null&&value!=''){
				return contMap[value];
			}
		}
		//退号
		function delFee(){
			var infoId = getIdUtil('#list');
			if(infoId==""||infoId==null){
				return;
			}
			$('#infoId').val(infoId);
			$('#windowOpen').window('open');
		}
		//提交
		function updateInfo(){
			var infoId = $('#infoId').val(); 
			var quitreason = $('#quitreason').val(); 
			var payType = $('#payType').combobox('getValue');
			$.messager.progress({text:'保存中，请稍后...',modal:true});
			$.post("<c:url value='/outpatient/info/updateInfo.action'/>",
		        {"infoId":infoId,"quitreason":quitreason,"payType":payType},
		        function(data){
		        	$.messager.progress('close');
		        	if(data.resMsg=="error"){
		        		$.messager.alert("操作提示",data.resCode);
		        	}else{
		        		$.messager.alert("操作提示",data.resCode);
		        		$('#windowOpen').window('close');
						$('#list').datagrid('load');
		        	}
		        	
		   	});
		}
	</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout"  data-options="fit:true">
	   <div data-options="region:'north'" style="width: 100%;height: 50px">
		<div style="padding:5px 5px 5px 5px;">	        
			<table style="width:100%;border:1px solid #95b8e7;">
				<tr>
					<td>
						就诊卡号：<input name="idcardId" id="idcardId" class="easyui-textbox">
						&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</td>
				</tr>
			</table>
		</div>
		</div>
		<div data-options="region:'center'" style="width: 100%;">
		<div >
			<table id="list" style="width:100%;" class="easyui-datagrid" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true,border:false" >
				<thead>
					<tr>
						<th data-options="field:'no',width :'10%'">门诊号</th>
						<th data-options="field:'patientId',formatter: function(value,row,index){
												if (row.patientId){
													return row.patientId.patientName;
												} else {
													return value;
												}
												},width :'10%'">姓名</th>
						<th data-options="field:'patien',formatter: function(value,row,index){
												if (row.patientId){
													return row.patientId.patientPhone;
												} else {
													return value;
												}
												},width :'10%'">联系方式</th>
						<th data-options="field:'expxrt',width :'10%'" formatter="functionEmp">挂号专家</th>
						<th data-options="field:'grade',width :'10%'" formatter="functionGrade">挂号级别</th>
						<th data-options="field:'dept',width :'10%'"  formatter="functionDept">挂号科室</th>
						<th data-options="field:'contractunit',width:'10%'" formatter="functionConit">合同单位</th>
						<th data-options="field:'fee',width :'10%'">挂号费</th>
					</tr>
				</thead>
			</table>
		</div>
	  </div>
	</div>
	<div id="toolbarId">
	<div id="windowOpen" class="easyui-window" title="退号操作" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false" style="width:800px;height:400px;">
			<input type="hidden" id="infoId">
			<table  class="honry-table" style="width: 100%">
				<tr>
					<td style="font-size:14px;background: #E0ECFF;width:50px">姓名：</td>
					<td id="backName" style="font-size:14px;width:50px" ></td>
					<td style="font-size:14px;background: #E0ECFF;width:50px">挂号费：</td>
					<td id="backFee"  style="font-size:14px;width:50px"></td>
					<td style="font-size:14px;background: #E0ECFF;width:50px">退费方式：</td>
					<td  style="font-size:14px;width:50px"><input id="payType" class="easyui-combobox" data-options="required:true,valueField: 'id', textField: 'value',data: [{ id: '1', value: '现金'},{ id: '2', value: '院内账户'}]"></td>
				</tr>
				<tr>
					<td class="honry-lable">退号原因：</td>
					<td colspan="5"><textarea class="easyui-validatebox" rows="2" cols="50" id="quitreason" name="quitreason" data-options="multiline:true"></textarea></td>
				</tr>
				<tr>
					<td colspan="6" align="center">
						<a href="javascript:updateInfo();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">提交</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#windowOpen').window('close')" data-options="iconCls:'icon-cancel'">关闭</a>
					</td>
				</tr>
			</table>		
		</div>
	<shiro:hasPermission name="${menuAlias}:function:delete">
		<a href="javascript:void(0)" onclick="delFee()" class="easyui-linkbutton" data-options="iconCls:'icon-door_in',plain:true">退号</a>
	</shiro:hasPermission>
	</div>
</body>
</html>