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
			<input id="id" type="hidden" name="cate.id" value="${cate.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;">					
					<tr id="trType">
						<td class="honry-lable">分类：</td>
		    			<td class="honry-info">
		    				<input class="easyui-combobox" id="deptCode" name="cate.diseaseCode" value="${cate.diseaseCode}" data-options="required:true" style="width:200px"/>
		    				<input type="hidden" id="deptName" name="cate.diseaseName" value="${cate.diseaseName }"/>
		    			</td>
	    			</tr>	
					<tr>
						<td class="honry-lable">名称：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="name" name="cate.name" value="${cate.name }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
	    			<tr id="code">
						<td class="honry-lable">编码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="catecode" name="cate.code" value="${cate.code }" data-options="required:true,editable:false" style="width:200px"/>
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">是否停用：</td>
		    			<td class="honry-info">&nbsp;<input id="yesId" type="checkbox" style="width:18px;" onclick="onClickIsStop('yesId')"/><input type="hidden" id="stopId" name="cate.stop_flg" value="0"/></td>
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
		url:'<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action?type=diseasetype',
		valueField:'encode',    
	    textField:'name',
	    onSelect: function(rec){
	    	$('#deptName').val(rec.name);
// 	    	$('#catecode').textbox("setValue",rec.encode);
	    },filter:function(q,row){
			var keys = new Array();
			keys[keys.length] = 'encode';
			keys[keys.length] = 'name';
			keys[keys.length] = 'pinyin';
			keys[keys.length] = 'wb';
			keys[keys.length] = 'inputCode';
			return filterLocalCombobox(q, row, keys);
		}
	    ,onHidePanel:function(none){
		    var data = $(this).combobox('getData');
		    var val = $(this).combobox('getValue');
		    var result = true;
		    for (var i = 0; i < data.length; i++) {
		        if (val == data[i].encode) {
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
// 			var row = $('#list').datagrid('getSelected');
// 			if(row !=null && row!=''){
// 				$('#deptCode').combobox('setValue',row.encode);
// 			}
		}

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
$(function(){
	var depc = $('#deptCode').combobox('getValue');
	if(depc==null||depc==''){
		var node = $('#tDt').tree('getSelected');
		if(node){
			if(node.id!='root'){
				$('#deptCode').combobox("setValue",node.deptCode);
			}
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
		url:"<%=basePath %>oa/repositoryCateg/saveCateg.action",
		onSubmit: function(){
			var dept = $('#deptCode').combobox('getValue');
			if(dept==null||dept==""){
				$.messager.alert('操作提示', '请选择科室!','error');  
                return false;  
			}
			if($('#name').val()==""){
       		 	$.messager.alert('操作提示', '名称不能为空!','error');  
                return false;  
			}
			if($('#catecode').is(':checked') && $('#enCode').val()==""){
       		 	$.messager.alert('操作提示', '编码不能为空!','error');  
                return false;  
			}
			var reCode=/^[0-9a-zA-Z]*$/g;
			if($('#code').is(':checked') && !reCode.test($('#enCode').val())){
       		 	$.messager.alert('操作提示', '编码不能输入符号!','error');  
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