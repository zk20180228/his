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
			<input type="hidden" name="id" value="${dutiesContrast.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;">
					<tr>
						<td class="honry-lable">职务类型名称：</td>
		    			<td class="honry-view">${dutiesContrast.belongLevelName}&nbsp;</td>
	    			</tr>
					<tr>
						<td class="honry-lable">职务类型代码：</td>
		    			<td class="honry-view">${dutiesContrast.belongLevel}&nbsp;</td>
	    			</tr>
					<tr>
						<td class="honry-lable">职务名称：</td>
		    			<td class="honry-view">${dutiesContrast.dutiesName}&nbsp;</td>
	    			</tr>
					<tr>
						<td class="honry-lable">职务代码：</td>
		    			<td class="honry-view">${dutiesContrast.dutiesCode}&nbsp;</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">职务等级：</td>
		    			<td class="honry-view" id='dutiesLevel'></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">拼音码：</td>
		    			<td class="honry-view">${dutiesContrast.dutiesPinyin } &nbsp;</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">五笔码：</td>
		    			<td class="honry-view">${dutiesContrast.dutiesWb}&nbsp;</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">自定义码：</td>
		    			<td class="honry-view">${dutiesContrast.dutiesInputCode}&nbsp;</td>
	    			</tr>
				</table>
		    <div style="text-align:center;padding:5px">
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
		    </div>
		</div>
	</div>
<script type="text/javascript">
	$(function(){
		var dutiesLevel="${dutiesContrast.dutiesLevel }";
		$("#dutiesLevel").text(formatLevel(dutiesLevel));
	});

	/**
	 * 关闭查看窗口
	 * @author  zxl
	 * @date 2015-5-22 10:53
	 * @version 1.0
	 */
	function closeLayout(){
		$('#divLayout').layout('remove','east');
	}
</script>
</body>
