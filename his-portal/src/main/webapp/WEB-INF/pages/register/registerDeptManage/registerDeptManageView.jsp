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
	<div class="easyui-panel" id = "panelEast" data-options="title:'查看',iconCls:'icon-form'">
		<div style="padding:10px">
			<input type="hidden" id="id" name="id" value="${sysDepartment.id }">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="honry-lable">
						系统编号:
					</td>
					<td class="honry-view">
						${sysDepartment.deptCode}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						部门名称:
					</td>
					<td class="honry-view">
						${sysDepartment.deptName}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						部门简称:
					</td>
					<td class="honry-view">
						${sysDepartment.deptBrev }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						部门英文:
					</td>
					<td class="honry-view">
						${sysDepartment.deptEname }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						部门地点:
					</td>
					<td class="honry-view">
						${sysDepartment.deptAddress }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						拼音码:
					</td>
					<td class="honry-view">
						${sysDepartment.deptPinyin }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						五笔码:
					</td>
					<td class="honry-view">
						${sysDepartment.deptWb }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						自定义码:
					</td>
					<td class="honry-view">
						${sysDepartment.deptInputcode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						备注:
					</td>
					<td class="honry-view">
						${sysDepartment.deptRemark }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						排序:
					</td>
					<td class="honry-view">
						${sysDepartment.deptRegisterOrder }&nbsp;
					</td>
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
