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
	<div class="easyui-panel" id = "panelEast" data-options="title:'查看',iconCls:'icon-form',border: false,fit:true">
		<div style="padding:10px">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;">					
					<tr id="trType">
						<td class="honry-lable">分类：</td>
		    			<td class="honry-info">${cate.diseaseName }</td>
	    			</tr>	
	    			<tr id="typeName1">
						<td class="honry-lable">名称：</td>
		    			<td class="honry-info">${cate.name }</td>
	    			</tr>
					<tr>
						<td class="honry-lable">编码：</td>
		    			<td class="honry-info">${cate.code }</td>
	    			</tr>	
	    			
	    			<tr>
						<td class="honry-lable">是否停用：</td>
		    			<td class="honry-info" id="stop">${cate.stop_flg == 1 ? '停用' : '在用' }</td>
	    			</tr>
				</table>
				<div style="text-align:center;padding:5px">
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
			</form>
		</div>
	</div>
</body>
</html>