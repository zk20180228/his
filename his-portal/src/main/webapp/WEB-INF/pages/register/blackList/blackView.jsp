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
	<div class="easyui-panel" id="panelEast" data-options="iconCls:'icon-form'" style="width:580px">
		<div style="padding:10px">
			<input type="hidden" id="id" name="id" value="${blacklist.id }">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr>
					<td class="honry-lable">
						员工姓名：
					</td>
					<td class="honry-view">
						${blacklist.dmployeeId.name}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						员工曾用名：
					</td>
					<td class="honry-view">
						${blacklist.dmployeeId.oldName}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						原因：
					</td>
					<td class="honry-view">
						${blacklist.resaon}&nbsp;
					</td>
				</tr>
			</table>
			<div style="text-align:center;padding:5px">
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
		    </div>
		</div>
	</div>
	<script type="text/javascript">
		    function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
	 </script>
</body>
</html>