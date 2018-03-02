<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

	<body>
		<!-- <div class="easyui-panel" data-options="title:'编码类别查看',iconCls:'icon-form'">-->
		<div style="padding:10px">
			<input type="hidden" id="id" name="id" value="${sysCodeType.id }">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
					<tr>
		    			<td class="honry-lable">代码:</td>
		    			<td class="honry-view">${sysCodeType.code }&nbsp;</td>
		    		</tr>
		    		<tr>
		    			<td class="honry-lable">名称:</td>
		    			<td class="honry-view">${sysCodeType.name}&nbsp;</td>
		    		</tr>
		    		<tr>
		    			<td class="honry-lable">允许层级:</td>
		    			<td class="honry-view">${sysCodeType.level}&nbsp;</td>
		    		</tr>
		    		<tr>
		    			<td class="honry-lable">适用医院:</td>
		    			<td class="honry-view">${sysCodeType.hospital }&nbsp;</td>
		    		</tr>
		    		<tr>
		    			<td class="honry-lable">排序:</td>
		    			<td class="honry-view">${sysCodeType.order}&nbsp;</td>
		    		</tr>
					<tr>
						<td class="honry-lable">说明:</td>
		    			<td class="honry-view" >${sysCodeType.description}&nbsp;</td>
					</tr>
					<tr>
					<td class="honry-lable">
						停用标志:
					</td>
					<td >
		    			<c:choose>
						<c:when test="${sysCodeType.stop_flg eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
					</td>
				  </tr>
			</table>
			<div style="text-align:center;padding:5px">
		    	<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="closeWin()">关闭</a>
		    </div>
					
		</div>
		<script>
			/**
			 * 关闭弹出窗口
			 * @author  hedong
			 * @date 2015-6-3
			 * @version 1.0
			 */
			function closeWin(){
				parent.win.dialog('close');
			}
		</script>
</body>
</html>