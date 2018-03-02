<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp" %>
</head>
	<body>
		<div class="easyui-panel" id = "panelEast" data-options="title:'编辑',iconCls:'icon-form',fit:true,border:false" >
			<div style="width: 100%;height: 98%">
	    		<form id="editForm" method="post" >
					<input type="hidden" id="id" name="menuControl.id" value="${menuControl.id }">
					<table class="honry-table" cellpadding="1" cellspacing="1" data-options="fit:true"  style="margin-left:auto;margin-right:auto;margin-top:10px;">
		    			<tr>
			    			 <td class="honry-lable">pc端名称:</td>
			    			<td class="honry-info"><input  id="pc" class="easyui-combobox"  value="${menuControl.pcName}" required="true"  style="width:300px" /></td>
							<input id="pcCode" name = "menuControl.pcCode"  type="hidden"   value="${menuControl.pcCode}"/>
							<input id="pcName" name = "menuControl.pcName"  type="hidden"   value="${menuControl.pcName}"/>
			    		</tr>
						<tr>
							<td class="honry-lable">移动端名称:</td>
							<td class="honry-info"><input class="easyui-combobox" id="mobile"  value="${menuControl.mobileName}"  required="true" style="width:300px" /></td>
							<input name = "menuControl.mobileCode" id="mobileCode"  type="hidden"   value="${menuControl.mobileCode}"/>
							<input name = "menuControl.mobileName" id = "mobileName"  type="hidden"   value="${menuControl.mobileName}"/>
						</tr>
						
		    		</table>
			    <div style="text-align:center;padding:5px">
			    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();void(0)" id="cle" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>
	
	<script>
	var id = $('#id').val();
	//加载页面
		$(function(){
			
		})
		/**
		 * 表单提交
		 * @author  liujinliang
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
			function submit(flg){
			  	$('#editForm').form('submit',{
			  		url:"<%=basePath%>sys/menuControlAction/saveMenuControl.action",
			  		 onSubmit:function(){ 
			  			if (!$('#editForm').form('validate')) {
							$.messager.show({
								title : '提示信息',
								msg : '验证没有通过,不能提交表单!'
							});
							return false;
						}
					 },  
					 success : function(data) {
							data = JSON.parse(data);
							if (data.resCode == 'success') {
								$.messager.alert('提示',data.resMsg);
								clear();
								closeLayout();
							}else if(data.resCode == 'error'){
								$.messager.alert('提示',data.resMsg);
							}
						},
					error:function(date){
						 $.messager.progress('close');
						 $.messager.alert('提示','保存失败');
					}
			  	});
			  	
	 	 	}

		/**
		 * 清除页面填写信息
		 * @author  liujinliang
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
			function clear(){
				$('#editForm').form('reset');
			}
			function closeLayout(){
				$('#divLayout').layout('remove','east');
				$("#list").datagrid("reload");
			}
			
			//加载表单
			 $('#pc').combobox({
				url: '<%=basePath%>sys/menuControlAction/findMenuType.action?type=1',
				valueField:'id',
			    textField:'text',
			    filter:function(q,row){
					var keys = new Array();
					keys[keys.length] = 'id';
					keys[keys.length] = 'text';
					if(filterLocalCombobox(q, row, keys)){
						row.selected=true;
					}else{
						row.selected=false;
					}
					return filterLocalCombobox(q, row, keys);
			    },
                 onChange: function (node) {
                     var value=$('#pc').combobox('getValue');
                     var text=$('#pc').combobox('getText');
                     $('#pcCode').val(value);
                     $('#pcName').val(text);
                 },
			    editable:true,
			});
			 $('#mobile').combobox({
				url: '<%=basePath%>sys/menuControlAction/findMenuType.action?type=3',
				valueField:'id',
			    textField:'text',
			    filter:function(q,row){
					var keys = new Array();
					keys[keys.length] = 'id';
					keys[keys.length] = 'text';
					if(filterLocalCombobox(q, row, keys)){
						row.selected=true;
					}else{
						row.selected=false;
					}
					return filterLocalCombobox(q, row, keys);
			    },
                 onChange: function (node) {
                     var value=$('#mobile').combobox('getValue');
                     var text=$('#mobile').combobox('getText');
                     $('#mobileCode').val(value);
                     $('#mobileName').val(text);
                 },
			    editable:true,
			});
			 
			 $(".combo").click(function(){
					$(this).prev().combobox("showPanel");

					})

			 function filterLocalCombobox(q, row, keys){
					if(keys!=null && keys.length > 0){
						for(var i=0;i<keys.length;i++){
							if(row[keys[i]]!=null&&row[keys[i]]!=''){
								var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1||row[keys[i]].indexOf(q) > -1;
								if(istrue==true){
									return true;
								}
							}
						}
					}else{
						var opts = $(this).combobox('options');
						return row[opts.textField].indexOf(q.toUpperCase()) > -1||row[opts.textField].indexOf(q) > -1;
					}
				}

	</script>
	
	</body>
</html>