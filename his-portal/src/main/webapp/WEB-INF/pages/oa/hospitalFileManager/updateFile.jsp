<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
			<input type="hidden" id="id" name="id" value="${docManage.id}">
			<input type="hidden" id="docDownAddr" name="fileURL" value="${docManage.fileURL}">
			<input type="hidden" id="docDownAddr" name="createUser" value="${docManage.createUser}">
			<input type="hidden" id="docDownAddr" name="cDate" value="${docManage.createTime}">
			<input type="hidden" id="docDownAddr" name="dName" value="${docManage.deptName}">
			<input type="hidden" id="docDownAddr" name="createDept" value="${docManage.createDept}">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="0px" style="width:100%;border-left:0;" data-options="border:false">
				<tr>
					<td class="honry-lable">档案编号</td>
					<td class="honry-info">
					<input id="docName" name="fileNumber" value="${docManage.fileNumber}"  data-options="required:true" style="width:200px" missingMessage="请输入文档名称" class="easyui-textbox"/>
				</tr>
				<tr>
					<td class="honry-lable">档案名称</td>
					<td class="honry-info">
					<input id="docName" name="name"  value="${docManage.name}" data-options="required:true" style="width:200px" missingMessage="请输入文档名称" class="easyui-textbox"/>
				</tr>
				<tr>
					<td class="honry-lable">档案分类</td>
					<td class="honry-info">
					<input class="easyui-combobox" style="width:200px"  name="fileClassify" value="${docManage.fileClassify}"
    				data-options="required:true,panelHeight:'80',editable:false,valueField: 'value',textField: 'text',data:[{value: '命令',text: '命令'},{value: '指示',text: '指示'},{value: '决定',text: '决定'},{value: '布告',text: '布告'},{value: '请示',text: '请示'},{value: '报告',text: '报告'},{value: '批复',text: '批复'},{value: '通知',text: '通知'},{value: '信函',text: '信函'},{value: '简报',text: '简报'},{value: '会议记录',text: '会议记录'},{value: '计划',text: '计划'},{value: '总结',text: '总结'}]" />
					</td>
				</tr>
				<tr>
					<td class="honry-lable">档案级别</td>
					<td class="honry-info">
					<input class="easyui-combobox" style="width:200px"  name="fileRank" value="${docManage.fileRank}"
    				data-options="required:true,panelHeight:'80',editable:false,valueField: 'value',textField: 'text',data:[{value: '普通',text: '普通'},{value: '秘密',text: '秘密'},{value: '机密',text: '机密'},{value: '绝密',text: '绝密'}]" />
					</td>
				</tr>
				<tr>
					<td class="honry-lable">档案类型</td>
					<td class="honry-info">
					<input class="easyui-combobox" style="width:200px"  name="fileType" value="${docManage.fileType}"
    				data-options="required:true,panelHeight:'80',editable:false,valueField: 'value',textField: 'text',data:[{value: '文档',text: '文档'},{value: '语音',text: '语音'},{value: '视频',text: '视频'},{value: '图片',text: '图片'}]" />
					</td>
				</tr>
				<tr>
					<td class="honry-lable">档案状态</td>
					<td class="honry-info">
					<input class="easyui-combobox" style="width:200px"  name="fileStatus" value="${docManage.fileStatus}"
    				data-options="required:true,panelHeight:'80',editable:false,valueField: 'value',textField: 'text',data:[{value: '编辑',text: '编辑'},{value: '保存',text: '保存'},{value: '归档',text: '归档'},{value: '废弃',text: '废弃'}]" />
					</td>
				</tr>
				<tr id="trMan">
					<td class="honry-lable">档案负责人</td>
	    			<td class="honry-info">
	    				<input class="easyui-combobox" id="fileMan" value="${docManage.fileMan}"  name="fileMan" data-options="required:true" style="width:200px"/>
	    				<input type="hidden" id="fileMan" name="fileMan" value="0"/>
	    			</td>
	    		</tr>
				<tr id="trType">
						<td class="honry-lable">科室</td>
		    			<td class="honry-info">
		    				<input class="easyui-combobox" id="deptCode" name="uploadDept" value="${docManage.deptName}"   data-options="required:true" style="width:200px"/>
		    				<input type="hidden" id="deptName" name="uploadDept" value="0"/>
		    			</td>
	    		</tr>
				<tr>
					<td class="honry-lable">上传文件</td>
					<td class="honry-info">
					<input type="file" id="mFile" name="mFile" style="width:200px" >
				</tr>
				<tr>
					<td class="honry-lable">是否可借阅</td>
					<td class="honry-info">
					<input class="easyui-combobox" style="width:200px"  name="borrow" value="${docManage.borrow}"
    				data-options="required:true,panelHeight:'80',editable:false,valueField: 'value',textField: 'text',data:[{value: '是',text: '是'},{value: '否',text: '否'}]" />
					</td>
				</tr>
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
			$('#fileMan').combobox({
				url:'<%=basePath %>/oa/userPortal/deptMan.action',
				valueField:'employeeName',    
			    textField:'employeeName',
			    onSelect: function(rec){
			    	console.log(rec.deptName);
			    	$('#fileMan').val(rec.employeeName);
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
				url : "<%=basePath%>/oa/userPortal/saveFile.action",
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
