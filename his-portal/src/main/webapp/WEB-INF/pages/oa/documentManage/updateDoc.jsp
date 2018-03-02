<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@ include file="/common/metas.jsp"%>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>themes/system/css/public.css">
	<style type="text/css">
		.window .panel-header .panel-tool a{
			background-color: red;	
		}
	</style>
<body>
	<div style="padding:10px" id="panelEast" >
		<form id="editForm" enctype="multipart/form-data" method="post">
			<input type="hidden" id="deptName" name="deptName" value="${docManage.uploadDept}">
			<input type="hidden" id="id" name="id" value="${docManage.id}">
			<input type="hidden" id="docDownAddr" name="docDownAddr" value="${docManage.docDownAddr}">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="0px" style="width:100%;border-left:0;" data-options="border:false">
				<tr>
					<td class="honry-lable">文档名称</td>
					<td class="honry-info">
					<input id="docName" name="docName" value="${docManage.docName}" data-options="required:true" style="width:200px" missingMessage="请输入文档名称" class="easyui-textbox"/>
				</tr>
				<tr>
					<td class="honry-lable">上传人员</td>
					<td class="honry-info">
					<input id="createUser" name="createUser" value="${docManage.createUser}" data-options="required:true" style="width:200px" missingMessage="请输入文档名称" class="easyui-textbox"/>
				</tr>
				<tr>
					<td class="honry-lable">所属范围</td>
					<td class="honry-info">
					<input class="easyui-combobox" style="width:200px"  name="deptType" value="${docManage.deptType}"
    				data-options="required:true,panelHeight:'80',editable:false,valueField: 'value',textField: 'text',data:[{value: '个人',text: '个人'},{value: '科室',text: '科室'}]" />
					</td>
				</tr>
				<tr id="trType">
						<td class="honry-lable">上传科室</td>
		    			<td class="honry-info">
		    				<input class="easyui-combobox" id="deptCode" value="${docManage.uploadDept}" name="uploadDept" data-options="required:true" style="width:200px"/>
		    				<input type="hidden" id="deptName" name="uploadDept" value="0"/>
		    			</td>
	    		</tr>
				<tr>
					<td class="honry-lable">上传时间</td>
					<td class="honry-info">
					<input id="createDate" id="createDate" name="createDate" value="${docManage.createDate}" data-options="required:true" style="width:200px" missingMessage="上传文件后默认为当前时间" class="easyui-textbox"/>
				</tr>
				<tr>
					<td class="honry-lable">上传文件</td>
					<td class="honry-info">
					<input type="file" id="mFile" name="mFile" style="width:200px" >
				</tr>
				<tr>
					<td class="honry-lable">文档简述 </td>
					<td class="honry-info">
					<textarea id="docDes" name="docDes" rows="6" cols="50" >${docManage.docDes}</textarea>
				</td>
			</table>
			<div style="text-align:center;padding:5px">
				<c:if test="${docManage.id==null }">
					<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
				</c:if>
				<a href="javascript:submit(2);void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout('edit')">关闭</a>
			</div>
		</form>
	</div>
<script>
		$(function(){
			$('#deptCode').combobox({
				url:'<%=basePath %>/baseinfo/department/departmentCombobox.action',
				valueField:'deptCode',    
			    textField:'deptName',
			    onSelect: function(rec){
			    	console.log(rec.deptName);
			    	$('#deptName').val(rec.deptName);
			    },filter:function(q,row){
					var keys = new Array();
					keys[keys.length] = 'deptCode';
					keys[keys.length] = 'deptName';
					keys[keys.length] = 'deptPinyin';
					keys[keys.length] = 'pinyin';
					keys[keys.length] = 'deptInputcode';
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
		
			});
		})
		
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
		
		/*
		* form提交
		* flag 1连续添加 2保存
		*/
		function submit(flag){
			var id=$('#id').val();
			if(id==null||id==""){
				var apk=$('#createDate').val();
				if(apk==null||apk==""){
					$.messager.alert('提示信息','上传时间为空');
					close_alert();
					return;
				}
			}  
			$('#editForm').form('submit', {
				url : "<%=basePath%>/oa/userPortal/saveDocument.action",
				onSubmit : function() {
					 if(!$('#editForm').form('validate')){
						$.messager.alert('提示信息','验证没有通过,不能提交表单!');
						close_alert();
						return false ;
					} 
					$.messager.progress({text:'保存中，请稍后...',modal:true});
				},
				success:function(data){ 
					$.messager.progress('close');
					var res = eval("(" + data + ")");
					if (res.resCode == "0") {
						if(flag == 1){
							clear();
							$('#list').datagrid('reload');
						}else if(flag == 2){
							closeLayout('edit');
							$.messager.alert('提示',res.resMsg);
							close_alert();
						}
					}else {
						$.messager.alert('提示',res.resMsg);
					}
				},
				error : function(data) {
					$.messager.progress('close');
				}
			}); 
		}
		//清除所填信息
		function clear(){
			var id = '${mApkVersion.id }';
			$('#editForm').form('reset');
			if(id){
				$('#id').val(id);
			}
		}
		
		/* 
		* 关闭界面
		*/
		function closeLayout(flag){
			$('#divLayout').layout('remove','east');
			if(flag == 'edit'){
				$('#list').datagrid('reload');
			}
		}
	</script>
	</body>
</html>
