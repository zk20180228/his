<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<jsp:include page="/javascript/default.jsp"></jsp:include>

<body>
	<div id="panelEast" class="easyui-panel" data-options="title:'厂商详细信息',iconCls:'icon-book',fit:true" style="width: 580px">
		<div style="padding: 10px">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr>
					<td class="honry-lable">
						名称:
					</td>
					<td class="honry-view">
						${manufacturer.manufacturerName}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						拼音码:
					</td>
					<td class="honry-view">
						${manufacturer.manufacturerPinyin }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						五笔码:
					</td>
					<td class="honry-view">
						${manufacturer.manufacturerWb }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						自定义码:
					</td>
					<td class="honry-view">
						${manufacturer.manufacturerInputcode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						联系方式:
					</td>
					<td>
					${manufacturer.manufacturerLink }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						开户银行:
					</td>
					<td>
						${manufacturer.manufacturerBank }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						开户账号:
					</td>
					<td class="honry-view">
						${manufacturer.manufacturerAccount }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						地址:
					</td>
					<td class="honry-view">
						${manufacturer.manufacturerAddress }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						GMP:
					</td>
					<td class="honry-view">
						${manufacturer.manufacturerGmp }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						备注:
					</td>
					<td class="honry-view">
						${manufacturer.manufacturerRemark }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						创建时间:
					</td>
					<td class="honry-view">
						${manufacturer.createTime }&nbsp;
					</td>
				</tr>
			</table>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			</div>
		</div>
	</div>
</body>
</html>