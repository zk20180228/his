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
	<div id="cc" class="easyui-layout" fit="true">   
		<div style="padding:10px">
			<input type="hidden" name="id" value="${extend.id }">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0"  style="width:100%;padding:5px">
					<tr>
						<td class="honry-lable">
							<span>所属部门：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.department}
						</td>
						<td class="honry-lable">
							<span>部门编号：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.departmentCode}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>学部：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.division}
						</td>
						<td class="honry-lable">
							<span>学部编号：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.divisionCode}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>总支：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.generalbranch}
						</td>
						<td class="honry-lable">
							<span>总支编号：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.generalbranchCode}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>工号：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.employeeJobNo}
						</td>
						<td class="honry-lable">
							<span>姓名：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.employeeName}
						</td>
					</tr>
					<tr>
				  		<td class="honry-lable">
							<span>性别：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.employeeSexName}
						</td>
						<td class="honry-lable">
							<span>证件号码：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.employeeIdentityCard}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>出生日期：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.strBirthday}
						</td>
						<td class="honry-lable">
							<span>年龄：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.employeeAge}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>人员职称：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.titleName}
						</td>
						<td class="honry-lable">
							<span>人员职务：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.dutiesName}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>人员类型：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.employeeTypeName}
						</td>
						<td class="honry-lable">
							<span>人员级别：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<c:choose>
							<c:when test="${extend.dutiesLevel eq '1'}">
								一级
							</c:when>
							<c:when test="${extend.dutiesLevel eq '2'}">
								二级
							</c:when>
							<c:when test="${extend.dutiesLevel eq '3'}">
								三级
							</c:when>
							<c:when test="${extend.dutiesLevel eq '4'}">
								四级
							</c:when>
							<c:otherwise>
								无级别
							</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>民族：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.nationalName}
						</td>
						<td class="honry-lable">
							<span>编制类型：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.organizationName}
						</td>
					</tr>
					<tr>
				  		<td class="honry-lable">
							<span>政治面貌：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.politicalstatusName}
						</td>
						<td class="honry-lable">
							<span>手机号码：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							${extend.employeeMobile}
						</td>
					</tr>
					<tr>
				  		<td class="honry-lable">
							<span>决策组：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;" >
							${extend.manageGroup}
						</td>
				  		<td class="honry-lable">
							<span>所属院区：</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;" >
							${extend.areaCodeName}
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
	 * @version 1.0
	 */
	function closeLayout(){
		window.close();
	}
</script>
</body>
