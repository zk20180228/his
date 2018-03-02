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
	<div class="easyui-panel" id = "panelEast" data-options="title:'厂商维护编辑',iconCls:'icon-form',border:false,fit:true" style="width:580px">
		<div style="padding:10px">
			<form id="editForm" method="post">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
					<tr>
						<input id="firmCode" name="firmMainTain.firmCode" type="hidden" value="${firmMainTain.firmCode }" />
						<input id="id" name="firmMainTain.id" type="hidden" value="${firmMainTain.id }"/>
						<td class="honry-lable">厂商名称:<input id="createTime" name="firmMainTain.createTime" type="hidden" value="${createTime }"/></td>
		    			<td class="honry-info"><input class="easyui-textbox" id="firmName" name="firmMainTain.firmName" value="${firmMainTain.firmName }" data-options="required:true"  style="width:200px" /></td>
	    			</tr>
					<tr>
						<td class="honry-lable">厂商接口密码:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="passWord" name="firmMainTain.passWord" value="${firmMainTain.passWord }" data-options="required:true,type:'password'" style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">再次输入密码:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="passWordSen"  value="${firmMainTain.passWord }" data-options="required:true,type:'password'" style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">厂商账户:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="firmUser" name="firmMainTain.firmUser" value="${firmMainTain.firmUser }" data-options="required:true" style="width:200px;"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">厂商密码:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="firmPassword" name="firmMainTain.firmPassword" value="${firmMainTain.firmPassword }" data-options="required:true" style="width:200px;"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">厂商视图:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="firmView" name="firmMainTain.firmView" value="${firmMainTain.firmView }" data-options="multiline:true" style="width:300px;height:150px;"/></td>
	    			</tr>
				</table>
				</form>
				<div style="text-align:center;padding:5px">
			    	<a href="javascript:submitForm();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
		</div>
	</div>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<script type="text/javascript">
$(function(){
	
});

/**
 * 保存
 * @author wangshujuan
 * @date 2017-9-19 16:53
 * @version 1.0
 */
function submitForm(){ 
      	$('#editForm').form('submit',{  
       	url:"<%=basePath %>migrate/firmMainTain/saveFirmMainTain.action",  
       	onSubmit:function(){
       	 if($(this).form('validate')){
       		 if($('#passWord').textbox('getValue')!=$('#passWordSen').textbox('getValue')){
       			$.messager.alert('提示','两次密码不一致');
       			return false;
       		 }
    		 $.messager.progress({text:'保存中，请稍后...',modal:true});
    		 return true;
    	 }else{
    		 $.messager.alert('提示','请填写完整信息');
    		 return false;
    	 	}
		},  
        success:function(data){  
        	$.messager.progress('close');
        	var res = eval('('+data+')');
        	if(res.resCode == 'success'){
        		$.messager.alert('提示','保存成功！');
	        	$('#list').datagrid('reload');
	        	$('#divLayout').layout('remove','east');
        	}else{
				$.messager.alert('提示','保存失败！');	
        	}
        },
  		}); 
   }
   /**
	 * 连续添加
	 * @author wangshujuan
	 * @date 2017-9-19 16:53
	 * @version 1.0
	 */
   function addContinue(){
   	$('#editForm').form('submit',{  
   		url:"<%=basePath %>migrate/FirmMainTain/saveFirmMainTain.action", 
   		onSubmit:function(){
          	 if($(this).form('validate')){
       		 $.messager.progress({text:'保存中，请稍后...',modal:true});
       		 return true;
       	 }else{
       		 $.messager.alert('提示','请填写完整信息');
       		 return false;
       	 	}
   		},  
        success:function(data){  
        	var res = jQuery.parseJSON(data);
        	$.messager.progress('close');
        	if(res.resCode == "success"){
        		//实现刷新栏目中的数据
                   $('#list').datagrid('reload');
                   $('#editForm').form('reset');
        	}else if(res.resCode == "repeat"){
        		$('#name').val("");
				$("#name").focus();
				close_alert();
        	} 
        	$.messager.alert('提示',res.resMsg);
        },
		error : function(data) {
			$.messager.progress('close');
			$.messager.alert('提示','保存失败！');	
		}							         
  	  }); 
   }
   /**
 * 清除页面填写信息
 * @author wangshujuan
 * @date 2017-9-19 16:53
 * @version 1.0
 */
	function clear(){
		$('#editForm').form('clear');
	}
   //关闭页面
	function closeLayout(){
		$('#divLayout').layout('remove','east');
		$("#list").datagrid("reload");
	}
		
	</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>