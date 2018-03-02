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
		<div class="easyui-panel" id="panelEast" data-options="title:'栏目资源编码编辑',iconCls:'icon-form',fit:true" style="width:580px">
			<div style="padding:10px">
	    		<form id="editForm" method="post">
					<input type="hidden" id="id" name="id" value="${menuResource.id }">
					<input type="hidden" name="mrcOrder" value="${menuResource.mrcOrder }">
		    		<table class="honry-table" cellpadding="1" cellspacing="1" style="margin:10px auto 0;">
						<tr>
							<td class="honry-lable">资源名称：</td>
			    			<td class="honry-info">
			    			<input class="easyui-textbox" id="mrcName" name="mrcName" value="${menuResource.mrcName }" data-options="required:true"  style="width:95%" missingMessage="请输入资源名称"/></td>
		    			</tr>
						<tr>
							<td class="honry-lable">资源别名：</td>
			    			<td class="honry-info">
			    			<input class="easyui-textbox" id="mrcAlias" name="mrcAlias" value="${menuResource.mrcAlias }" 
			    			data-options="required:true" validtype="mrcAlias"  style="width:95%" missingMessage="请输入资源别名" /></td>
		    			</tr>
		    			<tr>
			    			<td class="honry-lable">资源类型：</td>
			    			<td class="honry-info">
			    			<input class="easyui-numberbox" id="mrcType" name="mrcType" value="${menuResource.mrcType }" data-options="required:true" style="width:95%" missingMessage="请输入资源类型"/></td>					
						</tr>
						<tr>
							<td class="honry-lable">资源说明：</td>
			    			<td class="honry-info">
			    			<input class="easyui-textbox" id="mrcDescription" name="mrcDescription" value="${menuResource.mrcDescription }" style="width:95% "/></td>
				         
						</tr>
		    	</table>
		    	<div style="text-align:center;padding:5px">
		    		<c:if test="${menuResource.id==null }">
						<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
				    </c:if>
			    	<a href="javascript:submit();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
			    </form>
	    </div>
	</div>

<script>
 	 $(function(){
  			$.extend($.fn.validatebox.defaults.rules, {     
			      mrcAlias: {
				        validator: function (value, param) {
				            if(!/^[A-Za-z]+$/.test(value)){
				                $.fn.validatebox.defaults.rules.mrcAlias.message = '请输入英文';
				                return false;
				            }
				            return true;
				        }
				    }
  				});
  		});
 

		//提交验证
		function submit(){
                  $('#editForm').form('submit',{  
		        	url:'<%=basePath%>sys/editInfoResource.action', 
		        	dataType:'json',
		        	onSubmit:function(){
						if (!$('#editForm').form('validate')) {
							$.messager.show({
								title : '提示信息',
								msg : '验证没有通过,不能提交表单!'
							});
							return false;
						}
		        	},  
			        success:function(dataA){ 
			        	if(dataA=="repeat"){
			        		$.messager.alert('提示','栏目名称已存在！');	
			        	}else if(dataA == "success"){
			        		$.messager.alert('提示','保存成功');
				        	$('#divLayout').layout('remove','east');
					   //实现刷新
			           $("#list").datagrid("reload");
			        	}
			        	
			        },
					error : function(data) {
						$.messager.alert('提示','保存失败！');	
					}							         
		   		 }); 
	    }
		//连续添加
		function addContinue(){ 
		    $('#editForm').form('submit',{  
		        url:'<%=basePath%>sys/editInfoResource.action',  
		        onSubmit:function(){
					if (!$('#editForm').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
		        },  
		        success:function(){  
                    $("#list").datagrid("reload");
                    $('#editForm').form('reset');
		        },
				error : function(data) {
					$.messager.alert('提示','保存失败！');	
				}							         
		    }); 
	    }
		//清除所填信息
		function clear(){
			$('#editForm').form('reset');
		}
		function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
			function changeName(){
			
			if(test==0){
			
			}
			
			
			}
		
	</script>
	</body>
</html>