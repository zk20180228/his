<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<%@ include file="/common/metas.jsp"%>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>themes/system/css/public.css">
	<style type="text/css">
		.window .panel-header .panel-tool a{
			background-color: red;
		}
	</style>
</head>
	<body>
		<div  id="panelEast"  style="padding:10px;background-color: white;">
			<table class="honry-table removeBorders"  cellpadding="0" cellspacing="0" border="0" style="width:100%">
				<tr>
					<td class="honry-lable">
						所属分组：
					</td>
					<td class="honry-view">
						${personalAddress.belongGroupName}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						姓名:
					</td>
					<td class="honry-view">
						${personalAddress.perName}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						性别:
					</td>
					<td class="honry-view" id='perSex' >
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						自定义码:
					</td>
					<td class="honry-view">
						${personalAddress.perInputCode}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						出生日期:
					</td>
					<td	class="honry-view" id="perBirthday"	>
						<fmt:formatDate value="${personalAddress.perBirthday}" pattern="yyyy-MM-dd" />
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						移动电话:
					</td>
					<td  class="honry-view">
						${personalAddress.mobilePhone}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						办公电话:
					</td>
					<td  class="honry-view">
						${personalAddress.workPhone}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						电子邮箱:
					</td>
					<td  class="honry-view">
						${personalAddress.perEmail}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						家庭住址:
					</td>
					<td  class="honry-view">
						${personalAddress.perAddress}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						备注:
					</td>
					<td  class="honry-view">
						${personalAddress.perRemark}&nbsp;
					</td>
				</tr>
			</table>
			<div style="text-align:center;padding:5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout('')">关闭</a>
			</div>
			</div>
			<script type="text/javascript">
			$(function(){
				var perSex="${personalAddress.perSex}";
				$("#perSex").text(sexFamater(perSex));
			});
			
			
		    function closeLayout(){
				$('#divLayout').layout('remove','east');
			}

			</script>
			
	</body>
</html>