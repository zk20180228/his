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
	<div style=" width:100%; height:100%;  ">
		<table id="listphoto"style=" width:95%; height:95%;">
			<tr  height="20%">
				<td   width="16%" height="110px" style="padding-left: 10px" >
					<img id="t1t1"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_other.png" width="100" height="100" onclick="onclic('role-O')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t2"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_doctor.png" width="100" height="100" onclick="onclic('role-D')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t3"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_yaoshi.png" width="100" height="100" onclick="onclic('role-P')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t4"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_nurse.png" width="100" height="100" onclick="onclic('role-N')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px"> 
					<img id="t1t5"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_register.png" width="100" height="100" onclick="onclic('role-R')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t6"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_shoufei.png" width="100" height="100" onclick="onclic('role-F')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px"	>
					<img id="t1t7"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_jishi.png" width="100" height="100" onclick="onclic('role-T')" />
				</td>
			</tr>
			<tr  height="20%">
				<td   width="16%" height="110px" style="padding-left: 10px" >
					<img id="t1t1"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_other.png" width="100" height="100" onclick="onclic('role-O')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t2"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_doctor.png" width="100" height="100" onclick="onclic('role-D')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t3"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_yaoshi.png" width="100" height="100" onclick="onclic('role-P')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t4"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_nurse.png" width="100" height="100" onclick="onclic('role-N')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px"> 
					<img id="t1t5"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_register.png" width="100" height="100" onclick="onclic('role-R')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t6"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_shoufei.png" width="100" height="100" onclick="onclic('role-F')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px"	>
					<img id="t1t7"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_jishi.png" width="100" height="100" onclick="onclic('role-T')" />
				</td>
			</tr>
			<tr  height="20%">
				<td   width="16%" height="110px" style="padding-left: 10px" >
					<img id="t1t1"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_other.png" width="100" height="100" onclick="onclic('role-O')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t2"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_doctor.png" width="100" height="100" onclick="onclic('role-D')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t3"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_yaoshi.png" width="100" height="100" onclick="onclic('role-P')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t4"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_nurse.png" width="100" height="100" onclick="onclic('role-N')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px"> 
					<img id="t1t5"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_register.png" width="100" height="100" onclick="onclic('role-R')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t6"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_shoufei.png" width="100" height="100" onclick="onclic('role-F')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px"	>
					<img id="t1t7"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_jishi.png" width="100" height="100" onclick="onclic('role-T')" />
				</td>
			</tr>
			<tr height="20%">
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t8"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_keyan.png" width="100" height="100" onclick="onclic('role-S')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t9"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_manager.png" width="100" height="100" onclick="onclic('role-M')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t8"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_keyan.png" width="100" height="100" onclick="onclic('role-S')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t9"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_manager.png" width="100" height="100" onclick="onclic('role-M')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t8"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_keyan.png" width="100" height="100" onclick="onclic('role-S')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px">
					<img id="t1t9"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_manager.png" width="100" height="100" onclick="onclic('role-M')" />
				</td>
				<td width="16%"  height="110px" style="padding-left: 10px"	>
					<img id="t1t7"src="${pageContext.request.contextPath}/themes/easyui/default/images/role_jishi.png" width="100" height="100" onclick="onclic('role-T')" />
				</td>
			</tr>
		</table>
		
	</div>
	<script type="text/javascript">
function onclic(src) {
	  $('#icon').textbox('setText',src);
	  $('#icon').textbox('setValue',src);
	  $('#rolephoto').dialog('close');
}

</script>
</body>
</html>