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
	<div class="easyui-panel"  id="roleWinss"  data-options="title:'科室',iconCls:'icon-form'" style="width:580px">
			<form id="editForm" method="post" >
				<input type="hidden" id="roleId" name="id" value="${roleid }">
				<table id="divLayouts" class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<c:forEach var="list" items="${rDeptList }" varStatus="status">
			    		<tr>
			    		<td>
			    	     <input type="checkbox" id="checkbox" name="checkbox" value="${list.deptId.id }">
			    		</td>
			    			<td class="honry-lable">科室名称:</td>
					     <td class="honry-view" >
			    			${list.deptId.deptName }</td>
			    		</tr>
			    </c:forEach>	
		    	</table>
				<div style="text-align:center;padding:5px">
					<shiro:hasPermission name="${menuAlias}:function:add">
				    	<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="addContinue()" >添加</a>
				    </shiro:hasPermission>
				    <shiro:hasPermission name="${menuAlias}:function:delete">
				    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="delRoleDept()">删除</a>
				    </shiro:hasPermission>
				    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout1()">关闭</a>
				</div>
			</form>	
			
	</div>		
	<script type="text/javascript">
		//弹框  科室增加
		function addContinue(){
				AdddilogTree("科室","<c:url value='/sys/queryUserDeptRoletree.action'/>?rid="+$('#roleId').val(),'30%','90%');
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
					  				$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
									if (res){
										$.ajax({
											url: "<c:url value='/sys/delRoleDept.action'/>?deptid="+ids+"&roleid="+$('#roleId').val(),
											type:'post',
											success: function() {
												$.messager.alert('提示','删除成功');
												AddOrShowEasts('EditForm',"<c:url value='/sys/querUserRoleDept.action'/>?id="+getIdUtil("#Rolelist")); 
												}
											});
										}
					        		});
								}
							};
							if (isdel!=1) {
								$.messager.alert('提示','请选择要删除的信息！');
								close_alert();
							}
			   	}
			}
		
		
		function closeLayout1(){
				$('#RolelistdivLayout').layout('remove','east');
				
			}
	</script>
	</body>
</html>