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
		<div class="easyui-panel clinicView" id="panelEast" data-options="title:'诊室查看',iconCls:'icon-form'" style="width:100%;">
			<div style="padding:10px" data-options="border:false">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1">
						<tr>
							<td class="honry-lable">
								诊室名称:
							</td>
							<td class="honry-view">${clinic.clinicName}</td>
						</tr>
						<tr>
							<td class="honry-lable">
								诊室拼音:
							</td>
							<td class="honry-view">${clinic.clinicPiyin}</td>
						</tr>
						<tr>
							<td class="honry-lable">
								诊室五笔:
							</td>
							<td class="honry-view">${clinic.clinicWb}</td>
						</tr>
						<tr>
							<td class="honry-lable">
								自定义码:
							</td>
							<td class="honry-view">${clinic.clinicInputcode}</td>
						</tr>
						<tr>
							<td class="honry-lable">
								所属科室:
							</td>
							<td class="honry-view">${deptName}</td>
						</tr>
						<tr>
							<td class="honry-lable">
								备注:
							</td>
			    			<td class="honry-view">${clinic.clinicRemark }</td>
						</tr>
						<tr>
							<td class="honry-lable">创建时间:</td>
							<td class="honry-view"><fmt:formatDate value="${clinic.createTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
						</tr>
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