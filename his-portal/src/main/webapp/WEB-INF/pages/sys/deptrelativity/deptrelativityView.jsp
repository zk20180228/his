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
		<div class="easyui-panel" id="panelEast" data-options="title:'部门科室查看',iconCls:'icon-form'" style="width:580px">
			<div style="padding:10px">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
					<tr>
					    <td class="honry-lable">部门:</td>
					    <td class="honry-view"><p>${deptMap[sysDeptRelativity.dept] }
					    
					    </p></td>
					</tr>
					<tr>
					    <td class="honry-lable">部门分类:</td>
					    <td class="honry-view"><p>${sysDeptRelativity.deptType }</p></td>
					</tr>
					<tr>
					    <td class="honry-lable">相关部门:</td>
					    <td class="honry-view"><p>
					    ${deptMap[sysDeptRelativity.refdept] }</p></td>
					</tr>
					<tr>
					    <td class="honry-lable">相关部门分类:</td>
					    <td class="honry-view"><p>${sysDeptRelativity.refdeptType }</p></td>
					</tr>
					<tr>
					    <td class="honry-lable">备注:</td>
					    <td class="honry-view"><p>${sysDeptRelativity.remark }</p></td>
					</tr>
					
				</table>
			</div>
			<div style="text-align:center;padding:5px">
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
		    </div>
		</div>
		<script>	
		//关闭
		function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
		</script>	    
	</body>
</html>