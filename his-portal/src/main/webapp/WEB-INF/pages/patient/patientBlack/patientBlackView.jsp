<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
		<div class="easyui-panel" id="panelEast" data-options="title:'诊室查看',iconCls:'icon-form'" style="width: 100%">
			<div style="padding:10px">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="honry-lable" style="font-size: 14">患者姓名:</td>
						<td class="honry-view" >${patientBlack.patient.patientName}</td>
					</tr>
					<tr>	
						<td class="honry-lable" style="font-size: 14">病历号:</td>
						<td class="honry-view" >${patientBlack.medicalrecordId}</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">类型：</td>
						<td class="honry-view">${blacklisttype}</td>
					</tr>
					<tr>	
						<td class="honry-lable" style="font-size: 14">有效开始:</td>
						<td>
							<fmt:formatDate value="${patientBlack.blacklistStarttime}" pattern="yyyy-MM-dd"/>&nbsp;
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">有效结束:</td>
						<td>
							<fmt:formatDate value="${patientBlack.blacklistEndtime}" pattern="yyyy-MM-dd"/>&nbsp;
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">进入黑名单原因:</td>
		    			<td class="honry-view">${patientBlack.blacklistIntoreason }</td>
					</tr>
<!-- 					<tr> -->
<!-- 						<td class="honry-lable" style="font-size: 14">退出黑名单原因:</td> -->
<%-- 						<td class="honry-view" colspan="3">${patientBlack.blacklistOutreason}</td> --%>
<!-- 					</tr>	 -->
			</table>
		</div>
		<div style="text-align:center;padding:5px">
	    	<a href="javascript:closeLayout()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
	    </div>
	</div> 
	<script>
		
	</script>
	</body>
</html>