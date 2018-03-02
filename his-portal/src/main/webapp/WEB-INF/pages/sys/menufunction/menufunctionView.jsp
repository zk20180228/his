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
	<div class="easyui-panel" id = "panelEast" data-options="title:'栏目功能查看',iconCls:'icon-form',border:false,fit:true">
		<div style="padding:10px">
			<input type="hidden" id="id" name="id" value="${menufunction.id }">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="honry-lable">
						分类:
					</td>
					<td class="honry-view">
						${menufunction.mfClass }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						名称:
					</td>
					<td class="honry-view">
						${menufunction.mfName}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						别名:
					</td>
					<td class="honry-view">
						${menufunction.mfAlias }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						图标:
					</td>
					<td class="honry-view">
						${menufunction.mfIcon }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						访问页面请求:
					</td>
					<td class="honry-view">
						${menufunction.mfAction }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						页面路径:
					</td>
					<td class="honry-view">
						${menufunction.mfFile }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						说明:
					</td>
					<td class="honry-view">
						${menufunction.mfDescription }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						排序:
					</td>
					<td class="honry-view">
						${menufunction.mfOrder }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						创建时间:
					</td>
					<td class="honry-view"><fmt:formatDate value="${menufunction.createTime }" pattern="yyyy-MM-dd hh:mm:ss"/></td>
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
