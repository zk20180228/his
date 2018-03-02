<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>参数查看</title>
<%@ include file="/common/metas.jsp" %>
</head>
<body style="margin: 100%;padding: 100%">
	<div id="panelEast" class="easyui-panel" data-options="title:'参数查看',iconCls:'icon-form',border:false,fit:true">
		<div style="padding: 5px">
			<input type="hidden" id="id" name="id" value="${parameter.id }">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
				
				<tr>
					<td class="honry-lable">
						参数名称：
					</td>
					<td class="honry-view">
						${parameter.parameterName}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						参数代码：
					</td>
					<td class="honry-view">
						${parameter.parameterCode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						参数类型：
					</td>
					<td class="honry-view">
						${parameter.parameterType }&nbsp;
					</td>
				</tr>
				<c:if test="${parameter.parameterValue!=null }">
					<tr>
					    <td class="honry-lable">参数值：</td>
					    <td class="honry-view">${parameter.parameterValue }&nbsp;</td>
				    </tr>
				</c:if>
				
				<c:if test="${parameter.parameterDownlimit!=null}">
					<tr>
					    <td class="honry-lable">参数上限：</td>
					    <td class="honry-view">${parameter.parameterDownlimit }&nbsp;</td>
				    </tr>
				</c:if>
				
				<c:if test="${parameter.parameterUplimit!=null}">
					<tr>
					<td class="honry-lable">参数下限：</td>
					<td class="honry-view">${parameter.parameterUplimit }&nbsp;</td>
				</tr>
				</c:if>
				
				<tr>
					<td class="honry-lable">参数单位：</td>
					<td class="honry-view">
						${parameter.parameterUnit }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">备注：</td>
					<td class="honry-view">
						${parameter.parameterRemark }&nbsp;
					</td>
				</tr>
				<tr>
				    <td class="honry-lable">适用医院名称：</td>
				    <td class="honry-view">
				        <select multiple="multiple" id="selectBut" name="hosid" style="width:160px;height:160px;">
							<c:forEach var="list" items="${hoslocList }">
								<c:if test="${list.id!=null }">
									<option value="${list.id }">${list.name }</option>
								</c:if>
							 </c:forEach>
						</select>
				    </td>
				</tr>
				<tr>
				    <td class="honry-lable">不适用医院名称：</td>
				    <td id="buttonListId">
					    <select multiple="multiple" id="selectAll" style="width:160px;height:160px;">
					        <c:if test="${null!=hospitalList && !hospitalList.isEmpty() }">
						        <c:forEach var="list" items="${hospitalList }">
						             <option value="${list.id }">${list.name }</option>
						        </c:forEach>
					        </c:if>
					    </select>
					</td>
				</tr>
			</table>
		</div>
		<div style="text-align: center; padding: 5px">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
		</div>
	</div>		
	<script>
	/**
	 * 关闭查看窗口
	 * @author  lt
	 * @date 2015-6-19 10:53
	 * @version 1.0
	 */
	function closeLayout(){
		$('#divLayout').layout('remove','east');
	}
</script>
</body>
</html>