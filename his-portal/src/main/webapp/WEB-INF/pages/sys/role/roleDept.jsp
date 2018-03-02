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
	<div class="easyui-panel"  id="panelEast"  data-options="title:'科室',iconCls:'icon-form'" style="width:580px">
		<div style="padding:10px">
			<form id="editForm" method="post" >
				<input type="hidden" id="roleId" name="id" value="${roleid }">
				<table id="divLayout" class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
		    		<c:forEach var="list" items="${rDeptList }" varStatus="status">
		    	 	<input type="hidden" id="deptId" name="deptId" value="${list.deptId.id }">
			    		<tr>
			    		<td>
			    	     <input type="checkbox" id="checkbox" name="checkbox" value="${list.deptId.id }">
			    		</td>
			    			<td class="honry-view">科室名称:</td>
					     <td class="honry-view" >
			    			${list.deptId.deptName }</td>
			    		</tr>
		    		</c:forEach>
		    	</table>
		    		<div id="dialogDivId"></div>
				<div style="text-align:center;padding:5px">
				    	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="addContinue()" >添加</a>
				    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="delRoleDept()">删除</a>
				    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
				</div>
			</form>	
		</div>
	</div>	
	<script type="text/javascript">
	//弹框  科室增加
         function AddDeptdilogs(title, url) {
				$('#dialogDivId').dialog({    
				    title: title,    
				    width: '30%',    
				    height:'90%',    
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true   
				   });    
			}
		/**
			 * 加载科室树框及信息
			 * @author  wj
			 * @date 2015-06-26
			 * @version 1.0
			 */
			function addContinue(){
					AddDeptdilogs("科室信息", "<c:url value='/sys/queryDeptRole.action'/>?rid="+$('#roleId').val()+"&param="+new Date().getTime());
			}
			
	
		 		//删除
			function delRoleDept(){
				var getD=document.getElementsByName('checkbox'); 
			   	if (getD.length > 0) {//选中几行的话触发事件	    
			   		var ids = '';
			   		var isdel="";
					 for(var i=0;i<getD.length;i++){  	 	
					  	whichObj=getD[i];
					  		if (whichObj.checked) {
					  			isdel=1;
				  			 	ids+=whichObj.value+',';  
								}
							};
							$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
									if (res){
										$.ajax({
											url: "<c:url value='/sys/delRoleDept.action'/>?deptid="+ids+"&roleid="+$('#roleId').val(),
											type:'post',
											success: function() {
												$.messager.alert('提示','删除成功');
												AddOrShowEast('EditForm',"<c:url value='/sys/editRoleDept.action'/>?id="+getIdUtil("#list"),'35%');
												}
											});
										}
					        		});
							if (isdel!=1) {
								$.messager.alert('提示','请选择要删除的信息！');
								close_alert();
							}
			   	}
			}
		
		//清除所填信息
		function closeLayout(){
			$('#divLayout').layout('remove','east');
			$('#dialogDivId').dialog('destroy'); 
		}
		
			//加载模式窗口
	function Adddilog(title, url, width, height) {
		$('#dialogDivId').dialog({    
		    title: title,    
		    width: width,    
		    height: height,    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		   });    
	}
	
	//打开模式窗口
	function openDialog() {
		$('#dialogDivId').dialog('open'); 
	}
	
	//关闭模式窗口
	function closeDialog() {
		$('#dialogDivId').dialog('destroy');  
	}
	</script>
	</body>
</html>