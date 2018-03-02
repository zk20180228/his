<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div class="easyui-panel" id = "panelEast" data-options="title:'编辑',iconCls:'icon-form',border: false" >
		<div style="padding:5px">
			<form id="editForm" method="post">
			<input type="hidden" name="contrast.id" value="${contrast.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;">
					<tr>
						<td class="honry-lable">类型分类名称：</td>
						<td class="honry-info"><input  id="empTypeName" name="contrast.empTypeName" value="${contrast.empTypeName }" data-options="required:true" style="width:200px"/>
						<a href="javascript:delData('empTypeName','empTypeCode');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
					</tr>
					<tr>
						<td class="honry-lable">类型分类代码：</td>
						<td class="honry-info"><input class="easyui-textbox" name="contrast.empTypeCode" id="empTypeCode"  value="${contrast.empTypeCode}" readonly="readonly" style="width:200px" /></td>
					</tr>
					<tr>
						<td class="honry-lable">对照自定义码：</td>
						<td class="honry-info"><input class="easyui-textbox" id="inputCode" name="contrast.inputCode" value="${contrast.inputCode}" data-options="required:true"  style="width:200px" /></td>
					</tr>
					<tr>
						<td class="honry-lable">类型名称：</td>
						<td class="honry-info"><input  id="name" name="contrast.name" value="${contrast.name }" data-options="required:true" style="width:200px" />
						<a href="javascript:delData('name','codeT');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
					</tr>
					<tr>
						<td class="honry-lable">类型代码：</td>
						<td class="honry-info"><input class="easyui-textbox" id="code" name="contrast.code" value="${contrast.code }" data-options="required:true" style="width:200px"/>
						</td>
					</tr>
				</table>
				<div style="text-align:center;padding:5px">
					<a href="javascript:submitForm();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
					<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
	$(function(){
		//类型分类名称
		$('#empTypeName').combobox({
			url:"<%=basePath %>baseinfo/employeeTypeContrast/empTypeComb.action", 
			valueField:'name',
			textField:'name',
			onSelect:function(record){
				$('#empTypeCode').textbox('setValue',record.encode);
			},
			onHidePanel:function(none){
				var data = $(this).combobox('getData');
				var val = $(this).combobox('getValue');
				var result = true;
				for (var i = 0; i < data.length; i++) {
					if (val == data[i].name) {
						result = false;
					}
				}
				if (result) {
					$(this).combobox("clear");
				}else{
					$(this).combobox('unselect',val);
					$(this).combobox('select',val);
				}
			},
			filter: function(q, row){
				var keys = new Array();
				keys[keys.length] = 'encode';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			} 
		}); 
		//统计费用名称
		$('#name').combobox({	
			url:"<%=basePath %>baseinfo/employeeTypeContrast/typeComb.action",
			valueField:'name',
			textField:'name',
			onSelect:function(record){
				$('#code').textbox('setValue',record.encode);
			},
			onHidePanel:function(none){
				var data = $(this).combobox('getData');
				var val = $(this).combobox('getValue');
				var result = true;
				for (var i = 0; i < data.length; i++) {
					if (val == data[i].name) {
						result = false;
					}
				}
				if (result) {
					$(this).combobox("clear");
				}else{
					$(this).combobox('unselect',val);
					$(this).combobox('select',val);
				}
			},
			filter: function(q, row){
				var keys = new Array();
				keys[keys.length] = 'encode';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			} 
		});
	});
		
	
	
	function submitForm(){ 
		$('#editForm').form('submit',{  
			url:"<%=basePath %>baseinfo/employeeTypeContrast/saveContrast.action",
			onSubmit:function(){
				if (!$('#editForm').form('validate')) {
					$.messager.alert('提示','验证没有通过,不能提交表单!');
					closeLayout();
					return false;
				}
				$.messager.progress({text:'保存中，请稍后...',modal:true});
			},  
			success:function(data){
				$.messager.progress('close');
				var res = eval('(' + data + ')');
				$.messager.alert('提示',res.resMsg);
				if(res.resCode == 'success'){
					$("#list").datagrid("reload");
					closeLayout();
				}
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示','保存失败！');	
			}
		}); 
	}
	
	//清空下拉框
	function delData(comb,text){
		delSelectedData(comb);
		$('#' + text).textbox('setText','');
	}
	
	//清除
	function clear(){
		$('#editForm').form('reset');
	}
	//关闭
	function closeLayout(){
		$('#divLayout').layout('remove','east');
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>