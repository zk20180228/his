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
		<div style="padding:10px" id="panelEast" >
    		<form id="editForm" method="post">
				<input type="hidden" id="id" name="id" value="${role.id }">
				<input type="hidden" id="order" name="order" value="${role.order }">
				<input type="hidden" id="path" name="path" value="${role.path }">
				<input type="hidden" id="uppath" name="uppath" value="${role.uppath }">
				<input type="hidden" id="createUser" name="createUser" value="${role.createUser }">
				<input type="hidden" id="createDept" name="createDept" value="${role.createDept }">
				<input type="hidden" id="createTime" name="createTime" value="${role.createTime }">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width:100%">
				<tr>
						<td class="honry-lable">父级名称：</td>
		    			<td class="honry-info">
		    			<input id="parentRoleId" name="parentRoleId" value="${role.parentRoleId }" /></td>
	    			</tr>
						<tr>
							<td class="honry-lable">角色名称：</td>
			    			<td class="honry-info"><input class="easyui-textbox"  id="name" name="name" style="width: 300" value="${role.name }" data-options="required:true"  missingMessage="请输入角色名称"  />
                          </td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">角色别名：</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="alias" name="alias" style="width: 300" value="${role.alias }"  data-options="required:true" validtype="alias" missingMessage="请输入英文的角色别名"/></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">角色描述：</td>
			    			<td class="honry-info"><input class="easyui-textbox" id="description" name="description" style="width: 300" value="${role.description }" data-options="required:true" missingMessage="请输入角色描述"/></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">角色图标：</td>
			    			<td class="honry-info"><input id="icon" name="icon" style="width: 300" value="${role.icon }" data-options="required:true" placeHolder="请按回车选择图标" missingMessage="请按回车选择图标"/></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">停用标志:</td>
					    	<td>
					    	<input type="hidden" id="stopFlgHidden" name="stop_flg" value="${role.stop_flg }"/>
					    	<input type="checkBox" id="stopFlg" onclick="javascript:onclickBox('stopFlg')"/>
				    		</td>
						</tr>
		    	</table>
			    <div style="text-align:center;padding:5px">
				    <c:if test="${role.id==null }">
				    	<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
			    	</c:if>
			    	<a id="save" href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
	    	</form>
	    </div>
	    <div id="rolephoto"></div>
	<script type="text/javascript">
		//加载页面
		$(function(){
			//添加验证
			$.extend($.fn.validatebox.defaults.rules, {     
  				alias: {
			        validator: function (value, param) {
			            if(!/^[A-Za-z]+$/.test(value)){
			                $.fn.validatebox.defaults.rules.alias.message = '请输入英文';
			                return false;
			            }
			            return true;
			        }
				}
  			});
			//父节点加载
			$('#parentRoleId').combotree({    
				url: "<c:url value='/sys/queryRoleTree.action'/>?type="+"${type}"+'&parId='+"${role.id}",    
				required: true,   
				width:'300',
			    editable:false
			});
			//复选框赋值
			if($('#stopFlgHidden').val()==1){
				$('#stopFlg').attr("checked", true); 
			}
			$('#icon').textbox({
				required:true
			});
			bindEnterAndBlackEvent('icon',menuPhoto,'easyui'); 
		});
		
		//表单提交
		function submitForm(){
			$('#editForm').form('submit',{  
	        	url:"<c:url value='/sys/saveRole.action'/>",  
	        	onSubmit:function(){
					if (!$('#editForm').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});
	        	},  
		        success:function(data){
		        	var res = jQuery.parseJSON(data);
		        	$.messager.progress('close');
		        	if(res.resCode == "success"){
			        	$('#list').treegrid('reload');
			        	$('#divLayout').layout('remove','east');
			      	 	$('#rolephoto').dialog('destroy');
		        	}else if(res.resCode == "repeat"){
			            $('#alias').val("");
						$("#alias").focus();
		        	}
		        	$.messager.alert('提示',res.resMsg);
		        },
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert('提示','保存失败！');	
				}							         
	   		}); 
		}
		
		//表单提交前验证
		function submit(){ 
			if(!"${role.id}"==null){
				if("${role.parentRoleId}"!=$('#parentRoleId').combotree('getValue')){
					if(!confirm("您修改了该角色的所在父级,将会删除该角色下的全部授权，是否保存?")){
						return;
					}
				}
			}
			var id = "${role.id}";
		    submitForm();
	    }	    
	    
		//连续添加
		function addContinue(){ 
	        $('#editForm').form('submit',{  
	    	    url:"<c:url value='/sys/saveRole.action'/>",  
	    	    onSubmit:function(){
	    		if (!$('#editForm').form('validate')) {
	    			$.messager.show({
	    				title : '提示信息',
	    				msg : '验证没有通过,不能提交表单!'
	    			});
	    			return false;
	    		}
	    		$.messager.progress({text:'保存中，请稍后...',modal:true});
	    	},  
	    	success:function(data){ 
	    		var res = jQuery.parseJSON(data);
	        	$.messager.progress('close');
	        	if(res.resCode == "success"){
	        		$('#list').treegrid('reload');
	    		    $('#editForm').form('reset');

	        	}else if(res.resCode == "repeat"){
		            $('#alias').val("");
					$("#alias").focus();
	        	}
	        	$.messager.alert('提示',res.resMsg);
	    	},
	    		error : function(data) {
	    			$.messager.progress('close');
	    			$.messager.alert('提示','保存失败！');	
	    		}							         
	    	}); 
	    }
	    
		//清除页面填写信息
		function clear(){
			$('#editForm').form('reset');
			//复选框赋值
			if($('#stopFlgHidden').val()==1){
				$('#stopFlg').attr("checked", true); 
			}
		}
		
		//关闭编辑窗口
		function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
		
		//复选框赋值
		function onclickBox(id){
			if($('#'+id).is(':checked')){
				$('#'+id+'Hidden').val(1);
			}else{
				$('#'+id+'Hidden').val(0);
			}
		}
		//加载模式窗口
		function Adddilog(title, url, width, height) {
			$('#rolephoto').dialog({    
			    title: title,    
			    width: width,    
			    height: height,    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true   
			   });    
		}
		function menuPhoto(){ 
			Adddilog("图标","<c:url value='/sys/Rolephoto.action'/>?time="+new Date().getTime(),'50%','80%');
		}
	</script>
	</body>
</html>