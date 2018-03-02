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
		<div class="easyui-panel" id="panelEast" data-options="title:'用户',iconCls:'icon-form',fit:true,border:false" style="padding:0px" >
			<div data-options="region:'center',border:false" style="width:100%;padding:0px">
	    		<form id="editForm" method="post">
					<input type="hidden" id="id" name="user.id" value="${user.id }">
					<input type="hidden" id="createUser" name="user.createUser" value="${user.createUser }">
					<input type="hidden" id="createDept" name="user.createDept" value="${user.createDept }">
					<input type="hidden" id="createTime" name="user.createTime" value="${user.createTime }">
		    		<table class="honry-table" cellpadding="1" cellspacing="1" border="0px" style="width:100%;border-left:0;" data-options="border:false">
						<tr>
							<td class="honry-lable">账号：</td>
			    			<td class="honry-info">
			    			<input class="easyui-textbox" id="account" name="user.account" value="${user.account }" data-options="required:true" validtype="account" style="width:95%" missingMessage="请输入账号"/></td>
		    			</tr>
						<tr>
							<td class="honry-lable">姓名：</td>
			    			<td class="honry-info">
			    			<input class="easyui-textbox" id="name" name="user.name" value="${user.name }" data-options="required:true" validtype="name" style="width:95%" missingMessage="请输入拼姓名"/></td>
		    			</tr>
		    			<tr>
			    			<td class="honry-lable">昵称：</td>
			    			<td class="honry-info">
			    			<input class="easyui-textbox" id="nickName" name="user.nickName" value="${user.nickName }" data-options="required:true" style="width:95%" missingMessage="请输入昵称"/></td>					
						</tr>
						<tr>
							<td class="honry-lable">出生日期：</td>
			    			<td class="honry-info">
			    			<input id="birthday" name="user.birthday" value="${strBirthDay}" style="width:95%" class="Wdate" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/>
				         <tr>
							<td class="honry-lable">性别：</td>
							<td class="honry-info"><input type="hidden" id="sexHid" value="${user.sex }">
			    				<input id="sex" name="user.sex"  data-options="required:true" style="width:95%"> 
			    			</td>
						</tr>
						<tr>					
							<td class="honry-lable">最后一次登录时间：</td>
			    			<td class="honry-info">
			    			<input id="lastLoginTime" name="user.lastLoginTime" value="${strLastLoginTime}" style="width:95%" class="Wdate" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})"/>
		    			</tr>
		    			<tr>
			    			<td class="honry-lable">电话：</td>
							<td class="honry-info">
							<input class="easyui-textbox" id="phone" name="user.phone" value="${user.phone }" data-options="required:true" validtype="phone" style="width:95%" missingMessage="请输入电话"/></td>
						</tr>
		    			<tr>
			    			<td class="honry-lable">电子邮箱：</td>
							<td class="honry-info">
							<input class="easyui-textbox" id="email" name="user.email" value="${user.email }" data-options="required:true" validtype="email"  style="width:95%" missingMessage="请输入电子邮箱"/></td>
						</tr>
						 <tr>
							<td class="honry-lable">APP状态：</td>
							<td class="honry-info"><input type="hidden" id="useStatusHid" value="${user.useStatus }">
			    				<input id="useStatus" name="user.useStatus"  style="width:95%"> 
			    			</td>
						</tr>
						<tr>
			    			<td class="honry-lable">排序：</td>
							<td class="honry-info">
							<input class="easyui-numberbox" id="order" name="user.order" disabled="disabled" value="${user.order }" data-options="required:true" style="width:95% "missingMessage="请输入序号"/></td>
						</tr>
						<tr>
							<td class="honry-lable">停用标志：</td>
			    			<td>
				    			<input type="hidden" id="stopflgHidden" name="user.stop_flg" value="${user.stop_flg }"/>
				    			<input type="checkBox" id="stopflg" onclick="javascript:checkBoxSelect('stopflg')"/>
			    			</td>
						</tr>
						<tr>
							<td class="honry-lable">备注：</td>
			    			<td>
			    			<input class="easyui-textbox" id="remark" name="user.remark" value="${user.remark }" data-options="multiline:true" style="width:95%;height:60px;"/></td>
						</tr>
		    	</table>
		    	<div style="text-align:center;padding:5px">
		    		<c:if test="${user.id==null }">
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
			var useState =[{    
			    "encode":0,    
			    "name":"可用"   
			},{    
			    "encode":1,    
			    "name":"不可用"   
			}]  

			//适用性别
			$('#sex').combobox({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
                valueField:'encode',    
                textField:'name',
			    editable:false
			});
			$('#useStatus').combobox({
				 valueField:'encode',    
	             textField:'name',
	             data:useState,
				 editable:false
			});
			var sexHid = $('#sexHid').val();
			if(sexHid!=null&&sexHid!=""){
				$('#sex').combobox('setValue',sexHid);
			}
			var useStatusHid = $('#useStatusHid').val();
			if(useStatusHid!=null&&useStatusHid!=""){
				$('#useStatus').combobox('setValue',useStatusHid);
			}
			//复选框赋值
			if($('#canAuthorizeHidden').val()==1){
				$('#canAuthorize').attr("checked", true); 
			}
			//复选框赋值
			if($('#stopflgHidden').val()==1){
				$('#stopflg').attr("checked", true); 
			}
		});
 			//验证
			var account="";
  			$.extend($.fn.validatebox.defaults.rules, { 
  				 phone: {// 验证电话号码
	            	   validator: function(value){
 	            		   var rex=/^1[3-9]+\d{9}$/;
	            		    //区号：前面一个0，后面跟2-3位数字 ： 0\d{2,3}
	            		    //电话号码：7-8位数字： \d{7,8
	            		    //分机号：一般都是3位数字： \d{3,}
	            		     //这样连接起来就是验证电话的正则表达式了：/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/		 
	            		    var rex2=/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;	//页面原来的正则验证
	            		    if(rex.test(value)||rex2.test(value)){
	            		       return true;
	            		    }else{
	            		       return false;
	            		    } 
	            		  },
	            		  message: '请输入正确电话或手机格式'
	              },
			      account: {     
			          validator: function(value, param){   //帐号验证  
			        	  account=value;
			        	  return value.length >= 2 && value.length <= 15;     
			          },     
			          message: '输入内容必须为2~15个字符之间'    
			      },
			       name: {     
			          validator: function(value, param){  //姓名验证   
			        	  return value.length >= 2 && value.length <= 20;     
			          },     
			          message: '输入内容必须为2~4字符之间'    
			      },
			      
			      email: {  
                      validator: function (value) {       //email验证	
	                   return /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(value);  
	               },  
	               message: '请输入有效的邮箱账号(例：123456@qq.com)'  
	             }
	              
  				});
 
		//提交验证
		function submit(){
			$('#editForm').form('submit', {
				url : "<%=basePath%>sys/editInfoUser.action",
				data:$('#editForm').serialize(),
				dataType:'json',
				onSubmit : function() {
					if(!$('#editForm').form('validate')){
						$.messager.show({  
							title:'提示信息' ,   
							msg:'验证没有通过,不能提交表单!'  
						}); 
						$.messager.alert('提示信息','验证没有通过,不能提交表单!','warning');
						close_alert();
						return false ;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});
				},
				success : function(data){ 
					$.messager.progress('close');									
					if (data=="error") {
						$.messager.alert('提示','保存失败！');
					}else if(data=="success"){
						$.messager.alert('提示','保存成功！');
						$('#divLayout').layout('remove','east');
						//实现刷新
						 $("#list").datagrid("reload");
					}else{
						$.messager.alert('提示','此账号已存在请重新填写！');
					}
				},
				error : function(data) {
					$.messager.progress('close');
				}
			}); 			
		}
		
		//连续添加
		function addContinue(){ 
			$('#editForm').form('submit',{  
		        url:"<%=basePath%>sys/editInfoUser.action",  
		        data:$('#editForm').serialize(),
				dataType:'json',
		        onSubmit:function(){
					if (!$('#editForm').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						$.messager.alert('提示信息','验证没有通过,不能提交表单!','warning');
						close_alert();
						return false;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});
		        },  
		        success:function(data){ 
		        	$.messager.progress('close');
		        	if (data=="success") {
		        		$.messager.alert('提示','保存成功！');
		        		$("#list").datagrid("reload");
		                $('#editForm').form('reset');
		                 
					}else if(data == "error"){
						$.messager.alert('提示','保存失败！');	
					}else{
						$.messager.alert('提示','此账号已存在请重新填写！');
					}
                   
		        },
				error : function(data) {
					$.messager.progress('close');
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
			
		/**
		 * 复选框选中
		 * @param defalVal 默认值
		 * @param selVal 选中值
		 * @author  liujinliang
		 * @date 2015-5-25 10:53
		 * @version 1.0
		 */
		function checkBoxSelect(id){
			if($('#'+id).is(':checked')){
				$('#'+id+'Hidden').val(1);
			}else{
				$('#'+id+'Hidden').val(0);
			}
		}
		
	</script>
	</body>
</html>