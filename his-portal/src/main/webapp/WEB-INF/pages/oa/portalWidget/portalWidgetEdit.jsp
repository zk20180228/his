<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>组件编辑</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px; padding: 0px">
		<div class="easyui-panel" id="panelEast" data-options="iconCls:'icon-form',border:false,fit:true">
			<div style="padding:5px">
	    		<form id="editForm" method="post">
					<input type="hidden" id="id" name="portalWidget.id" value="${portalWidget.id }">
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
		    			<tr>
			    			 <td class="honry-lable">组件名称：</td>
			    			<td class="honry-info"><input class="easyui-textbox" style="width:95%" id="name" name="portalWidget.name" value="${portalWidget.name}" data-options="required:true" missingMessage="请输入名称" /></td>
			    		</tr>
						<tr>
							<td class="honry-lable">组件地址：</td>
			    			<td class="honry-info"><input class="easyui-textbox" style="width:95%" id="url"  name="portalWidget.url" value="${portalWidget.url}" data-options="required:true" missingMessage="请输入地址" /></td>
						</tr>
						<tr>
							<td class="honry-lable">查看地址：</td>
			    			<td class="honry-info"><input class="easyui-textbox" style="width:95%" id="viewUrl"  name="portalWidget.viewUrl" value="${portalWidget.viewUrl}"/></td>
						</tr>
						<tr>
							<td class="honry-lable">列表地址：</td>
			    			<td class="honry-info"><input class="easyui-textbox" style="width:95%" id="moreUrl"  name="portalWidget.moreUrl" value="${portalWidget.moreUrl}"/></td>
						</tr>
						<tr>
							<td class="honry-lable">组件状态：</td>
							<td>
								<span>禁用</span>
				    			<input type="hidden" id="statusHidden" name="portalWidget.status" value="${portalWidget.status }"/>
				    			<input type="checkBox" id="status" onclick="javascript:onclickBox('status')"/>
			    			</td>
						</tr>
		    	</table>
			    <div style="text-align:center;padding:5px">
			     <c:if test="${portalWidget.id==null }">
			    	<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
			    </c:if>
			    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
			    <div id="roleWins"></div>
	    	</form>
	    </div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<style type="text/css">
		.window .panel-header .panel-tool .panel-tool-close{
  	  		background-color: red;
  	  	}
	</style>
	<script type="text/javascript">
	//页面加载
	$(function(){
		//复选框赋值
		if($('#statusHidden').val()==1){
			$('#status').attr("checked", true); 
		}
	});
		/**
		 * 表单提交
		 * @author  liujinliang
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
		function submit(){ 
		   $('#editForm').form('submit',{  
		        url:"<c:url value='/oa/portalWidget/savePortalWidget.action'/>",
		        onSubmit:function(param){
		        	if (!$('#editForm').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						$.messager.alert('提示信息','验证没有通过,不能提交表单!','warning');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}
		        },  
		        success:function(data){ 
		        	if(data=='true'){
	        			$.messager.alert('提示信息','保存成功！','ok');
		        	}else{
		        		$.messager.alert('提示信息','保存失败！','warning');
		        	}
	        		$('#divLayout').layout('remove','east');
	        		//实现刷新栏目中的数据
                     $('#list').datagrid('reload');
		        },
				error : function(data) {
					$.messager.alert('提示信息','保存失败！','warning');
				}							         
		    });
	    }	    
	    
		/**
		 * 连续添加
		 * @author  liujinliang
		 * @date 2015-6-1 10:58
		 * @version 1.0
		 */
		function addContinue(){
		    $('#editForm').form('submit',{  
		        url:"<c:url value='/oa/portalWidget/savePortalWidget.action'/>", 
		        onSubmit:function(param){
					if (!$('#editForm').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						$.messager.alert('提示信息','验证没有通过,不能提交表单!','warning');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}
		        },  
		        success:function(data){ 
	        		$.messager.alert('提示信息','保存成功！');
		            //实现刷新栏目中的数据
		             clear();
		           //实现刷新栏目中的数据
                     $('#list').datagrid('reload');
                     add();
		        },
				error : function(data) {
					$.messager.alert('提示信息','保存失败！');
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
			 var portalId = $('#id').val();
			$('#editForm').form('clear');
			$('#id').textbox('setText',portalId);
		}
		/**
		 * 关闭编辑窗口
		 * @author  liujinliang
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
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
		function checkBoxSelect(obj,defalVal,selVal){
			var name = obj.id+"s";
			var element = document.getElementById(name);
			if(obj.checked==true){
				element.value=selVal;
			}else{
				element.value=defalVal;
			}
		}
		//加载模式窗口
		function Adddilog(title, url, width, height) {
			$('#roleWins').dialog({    
			    title: title,    
			    width: width,    
			    height: height,    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true   
			   });    
		}
		/**
		 * 打开用法界面弹框
		 * @author  zhuxiaolu
		 * @date 2015-5-25 10:53
		 * @version 1.0
		 */
		
		function popWinToUseage(){
			
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?textId=useMode&type=useage";
			var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+(screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
					
		}
		//复选框赋值
		function onclickBox(id) {
			if ($('#' + id).is(':checked')) {
				$('#' + id + 'Hidden').val(1);
			} else {
				$('#' + id + 'Hidden').val(0);
			}
		}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>