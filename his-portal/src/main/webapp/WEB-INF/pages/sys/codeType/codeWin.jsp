<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

	<body>
		<div align="center" class="easyui-panel" style="padding:10px">
		<form id="editForm" method="post">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				 <tr>
		    			<td class="honry-lable">代码:</td>
		    			<td class="honry-info"><input class="easyui-validatebox" type="text" id="code" name="code" data-options="required:true" value="" style="width:290px" missingMessage="请输入代码"/></td>
		    	 </tr>
		    	 <tr>
		    			<td class="honry-lable">名称:</td>
		    			<td class="honry-info"><input class="easyui-validatebox" type="text" id="name" name="name" data-options="required:true" value="" style="width:290px" missingMessage="请输入名称"/></td>
		    	 </tr>
		    	 <tr>
		    			<td class="honry-lable">层级:</td>
		    			<td class="honry-info"><input class="easyui-validatebox" type="text" id="level" name="level" data-options="required:true" value="" style="width:290px" missingMessage="请输入层级"/></td>
		    	 </tr>
		    	 <tr>
		    	        <td class="honry-lable">需要生成的代码:</td>
		    			<td class="honry-info">
		    			<select id="cc" class="easyui-combobox" name="needGenerate" style="width:200px;" data-options="required:true" missingMessage="请选择需要生成的代码">   
   							<option value="jsp" >jsp页面</option>   
   							<option value="action">action层</option>   
   							<option value="service">service层</option>   
   							<option value="dao">dao层</option> 
   							<option value="model">model层</option>  
   							<option value="all" selected="selected">全部</option>  
						</select> 
		    			</td>
		    	 
		    	 </tr>
			</table>
			 <div style="text-align:center;padding:5px;margin-top: 30px;">
	  			   <a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
	  			   <a href="javascript:void(0)" class="easyui-linkbutton"  onclick="closeWin()">关闭</a>
			    </div>	
		</form>	
		</div>
		
		<script type="text/javascript">
		
			//表单提交submit信息
		  	function submit(){
			  	$('#editForm').form('submit',{
			  		url:'generateCode.action',
			  		 onSubmit:function(){ 
			  		     
					     return $(this).form('validate');  
					 },  
					success:function(data){
						$.messager.alert('提示','代码生成成功');
					  closeWin();
					 },
					error:function(date){
						$.messager.alert('提示','代码生成失败');
					}
			  	});
		  	}
	        /* 关闭弹出窗口
			 * @author  hedong
			 * @date 2015-6-3
			 * @version 1.0
			 */
			function closeWin(){
			  
			   parent.win.dialog('close');
				
			}
</script>
	</body>

</html>