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
	<div class="easyui-panel" id = "panelEast" data-options="title:'查看',iconCls:'icon-form',border: false">
		<div style="padding:10px">
			<input type="hidden" name="id" value="${contrast.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;">
					<tr>
						<td class="honry-lable">类型分类名称：</td>
		    			<td class="honry-view">${contrast.empTypeName}&nbsp;</td>
	    			</tr>
					<tr>
						<td class="honry-lable">类型分类代码：</td>
		    			<td class="honry-view">${contrast.empTypeCode}&nbsp;</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">对照自定义码：</td>
		    			<td class="honry-view">${contrast.inputCode}&nbsp;</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">类型名称：</td>
		    			<td class="honry-view">${contrast.name} &nbsp;</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">统计费用名称：</td>
		    			<td class="honry-view">${contrast.code}&nbsp;</td>
	    			</tr>
				</table>
		    <div style="text-align:center;padding:5px">
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
		    </div>
		</div>
	</div>
<script type="text/javascript">
	/**
	 * 关闭查看窗口
	 * @author  liujinliang
	 * @date 2015-5-22 10:53
	 * @version 1.0
	 */
	function closeLayout(){
		$('#divLayout').layout('remove','east');
	}
</script>
</body>
