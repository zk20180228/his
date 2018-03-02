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
	<div id="panelEast" class="easyui-panel" data-options="title:'详细信息',iconCls:'icon-book',fit:true" style="width: 580px">
		<div style="padding: 10px">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr>
					<td class="honry-lable">
						公司编码:
					</td>
					<td class="honry-view">
						${supplier.code}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						公司名称:
					</td>
					<td class="honry-view">
						${supplier.name }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						公司法人:
					</td>
					<td class="honry-view">
						${supplier.legal }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						公司电话:
					</td>
					<td class="honry-view">
						${supplier.phone }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						公司地址:
					</td>
					<td>
					${supplier.address }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						公司传真:
					</td>
					<td>
						${supplier.telautogram }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						公司邮箱:
					</td>
					<td>
						${supplier.mail }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						开户银行:
					</td>
					<td>
						${supplier.bankName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						开户账号:
					</td>
					<td class="honry-view">
						${supplier.bankAcco }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						联系人:
					</td>
					<td class="honry-view">
						${supplier.linkMan }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						联系电话:
					</td>
					<td class="honry-view">
						${supplier.linkPhone }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						创建时间:
					</td>
					<td class="honry-view">
						${supplier.createTime }&nbsp;
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