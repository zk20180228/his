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
	<div id="divLayout">
	<form id="editRoleDeptForm">
			    <input type="hidden" id="rid"  value="${rid }">
				<div style="padding-bottom:15px">
				    	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="submitRoleDeptForm()" >添加</a>
				</div>
				<ul id="tDtRole"></ul> 
				</form>
				</div> 
	<script type="text/javascript">
		$(function(){
              $('#tDtRole').tree({   
 			    onlyLeafCheck:true,
                 url: "<c:url value='/baseinfo/employee/treeemployeebydeptid.action'/>?pid="+"1"+'&param='+new Date().getTime(),   
                 onBeforeExpand:function(node,param){
               	  if(node.attributes.isUser=="0"){
               		  $('#tDtRole').tree('options').url = "<c:url value='/baseinfo/employee/treeemployeebydeptid.action'/>?pid=" + node.id;
               	  }else{
               		  return false;
               	  }
                 }
             });   
		});
		
			//表单提交
		   	function submitRoleDeptForm(){
		   	 var isLeaf = $('#tDtRole').tree('isLeaf', node.target);  
		        if (isLeaf) {
		        	$.messager.alert('提示',isLeaf)
		        	var nodes = $('#tDtRole').tree('getSelected').text;
		               if (nodes.length>0) {
			                $('#approveOper').val(nodes);
			                $('#roleaddUserdiv').dialog('destroy'); 
			                $('#roleaddUserdiv').dialog('close');
		             }else{
		            	 $.messager.alert('提示','请选择审核人！');
		            	 close_alert();
		             }
		        }
		   }
		</script>
	</body>
</html>