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
</head>
<body>
	<div class="easyui-panel" id = "panelEast" data-options="title:'添加/编辑',iconCls:'icon-form',fit:true" style="width:580px;">
		<div style="padding:5px">
			<form id="editForm" method="post" enctype="multipart/form-data">
			<input id="id" type="hidden" name="info.id" value="${info.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;">					
					<tr id="trType">
						<td class="honry-lable">科室：</td>
		    			<td class="honry-info">
		    				<input class="easyui-combobox" id="deptCode" name="info.deptCode" value="${info.deptCode }" data-options="required:true" style="width:200px"/>
		    				<input type="hidden" id="deptName" name="info.deptName" value="${info.deptName }"/>
		    			</td>
	    			</tr>	
					<tr>
						<td class="honry-lable">输入码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="inputCode" name="info.inputCode" value="${info.inputCode }" style="width:200px"/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">自定义码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="customCode" name="info.customCode" value="${info.customCode }"style="width:200px"/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">五笔码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="inputCodeWB" name="info.inputCodeWB" value="${info.inputCodeWB }" style="width:200px"/></td>
	    			</tr>
	    			<tr id="trType">
						<td class="honry-lable">临床路径ID</td>
		    			<td class="honry-info">
		    				<input class="easyui-combobox" id="cpID" value="${info.cpID }" data-options="required:true" style="width:200px"/>
		    				<input type="hidden" id="cpIDH" name="info.cpID" value="${info.cpID }"/>
		    			</td>
	    			</tr>	
	    			<tr>
						<td class="honry-lable">临床路径版本号：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="versionNo" name="info.versionNo" value="${info.versionNo }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">所属医院：</td>
		    			<td class="honry-info">
		    				<input class="easyui-textbox" id="hospitalid" value="${info.hospitalidName }" style="width:200px" data-options="editable:false"/>
		    				<input type="hidden" id="hospitalidH" name="info.hospitalid" value="${info.hospitalid }"/>
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">所属院区：</td>
		    			<td class="honry-info">
		    				<input class="easyui-textbox" id="areaName" value="${info.areaCodeName }" style="width:200px" data-options="editable:false"/>
		    				<input type="hidden" id="areaCode" name="info.areaCode" value="${info.areaCode }"/>
	    				</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">是否停用：</td>
		    			<td class="honry-info">&nbsp;<input id="yesId" type="checkbox" style="width:18px;" onclick="onClickIsStop('yesId')"/><input type="hidden" id="stopId" name="info.stop_flg" value="0"/></td>
	    			</tr>		    		 
	    			<tr>
						<td class="honry-lable">是否删除：</td>
		    			<td class="honry-info">&nbsp;<input id="delId" type="checkbox" style="width:18px;" onclick="onClickIsDel('delId')"/><input type="hidden" id="delIdH" name="info.del_flg" value="0"/></td>
	    			</tr>		    		 
				</table>
				<div style="text-align:center;padding:5px">
			    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
			</form>
		</div>
	</div>
<script type="text/javascript">
$(function(){
	$('#deptCode').combobox({
		url:'<%=basePath%>inpatient/info/zyDeptCombobox.action',
		valueField: 'deptCode',
		textField: 'deptName',
		multiple: false,
		editable: true,
		required: true,
	    onSelect: function(rec){
	    	$('#deptName').val(rec.deptName);
	    	$('#areaCode').val(rec.areaCode);
	    	$('#inputCode').textbox('setValue',rec.deptPinyin);
	    	$('#customCode').textbox('setValue',rec.deptInputcode);
	    	$('#inputCodeWB').textbox('setValue',rec.deptWb);
	    	$('#areaName').textbox('setText',rec.areaName);
	    	$('#hospitalid').val(rec.deptName);
	    },filter:function(q,row){
			var keys = new Array();
			keys[keys.length] = 'deptCode';
			keys[keys.length] = 'deptName';
			keys[keys.length] = 'pinyin';
			keys[keys.length] = 'wb';
			keys[keys.length] = 'inputCode';
			return filterLocalCombobox(q, row, keys);
		},onHidePanel:function(none){
		    var data = $(this).combobox('getData');
		    var val = $(this).combobox('getValue');
		    var result = true;
		    for (var i = 0; i < data.length; i++) {
		        if (val == data[i].deptCode) {
		            result = false;
		        }
		    }
		    if (result) {
		        $(this).combobox('clear');
		    }else{
		        $(this).combobox('unselect',val);
		        $(this).combobox('select',val);
		    }
		}
		,onLoadSuccess: function(){
// 			$('#').combobox('setValue',row.deptCode);
		}

	});
	$('#cpID').combobox({
		url:'<%=basePath%>inpatient/pathWay/getCpWay.action',
		valueField: 'id',
		textField: 'cpName',
		multiple: false,
		editable: true,
		required: true,
	    onSelect: function(rec){
	    	$("#cpIDH").val(rec.id);
	    	$("#versionNo").textbox('setValue',rec.cpId);
	    }
		,filter:function(q,row){
			var keys = new Array();
			keys[keys.length] = 'cpId';
			keys[keys.length] = 'cpName';
			keys[keys.length] = 'inputCode';
			keys[keys.length] = 'inputCodeWb';
			keys[keys.length] = 'customCode';
			return filterLocalCombobox(q, row, keys);
		},onHidePanel:function(none){
		    var data = $(this).combobox('getData');
		    var val = $(this).combobox('getValue');
		    var result = true;
		    for (var i = 0; i < data.length; i++) {
		        if (val == data[i].id) {
		            result = false;
		        }
		    }
		    if (result) {
		        $(this).combobox('clear');
		    }else{
		        $(this).combobox('unselect',val);
		        $(this).combobox('select',val);
		    }
		}
// 		,onLoadSuccess: function(){
// 			var row = $('#list').datagrid('getSelected');
// 			$('#deptCode').combobox('setValue',row.deptCode);
// 		}

	});
});
//本地下拉查询方法
function filterLocalCombobox(q, row, keys){
	if(keys!=null && keys.length > 0){//
		for(var i=0;i<keys.length;i++){ 
			if(row[keys[i]]!=null&&row[keys[i]]!=''){
					var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1;
					if(istrue==true){
						return true;
					}
			}
		}
	}else{
		var opts = $(this).combobox('options');
		return row[opts.textField].indexOf(q.toUpperCase()) > -1;
	}
}
//清除
function clear(){
	$('#editForm').form('clear');
}
//关闭
function closeLayout(){
	$('#divLayout').layout('remove','east');
}
//是否停用
function onClickIsStop(id){
	if($('#'+id).is(':checked')){	
		$('#stopId').val(1);
	}else{
		$('#stopId').val(0);
	}
}
function onClickIsDel(id){
	if($('#'+id).is(':checked')){	
		$('#delIdH').val(1);
	}else{
		$('#delIdH').val(0);
	}
}
$(function(){
	var node = $('#tDt').tree('getSelected');
	if(node){
		if(node.id!='root'){
			$('#deptCode').val(node.deptCode);
		}
	}
});
//提交表单
function submit(flg) {
	var checkflg=1;
	if($('#yesId').is(':checked')){
		checkflg=1;
	}else{
		checkflg=2;
	}
	var flag=1;
	var node = $('#tDt').tree('getSelected');
	if(node){
		if(node.id=='root'){
			flag=0;
		}
	}
	$('#editForm').form('submit', {
		url:"<%=basePath %>inpatient/pathWay/save.action",
		onSubmit: function(){
			var dept = $('#deptCode').combobox('getValue');
			if(dept==null||dept==""){
				$.messager.alert('操作提示', '请选择科室!','error');  
                return false;  
			}
			$.messager.progress({text:'保存中，请稍后...',modal:true});
		},
		success : function(data) {
			$.messager.progress('close');
			data = eval("("+data+")");
			$.messager.alert('提示',data.resMsg);	
			closeLayout();
			reload();
			refresh();
		},
		error : function(date) {
			 $.messager.progress('close');
			 $.messager.alert('提示','保存失败！');	
		}
	});
}
</script>
</body>
</html>