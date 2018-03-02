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
						入库单号:
					</td>
					<td class="honry-view">
						${matInput.inListCode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						单内序号:
					</td>
					<td class="honry-view">
						${matInput.inSerialNo }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						入库科室:
					</td>
					<td>
						${matInput.storageCode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						生产厂商:
					</td>
					<td>
						${matInput.companyName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						物品科目:
					</td>
					<td>
						${matInput.kindCode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						物品名称:
					</td>
					<td>
						${matInput.itemName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						规格:
					</td>
					<td>
						${matInput.specs }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						最小单位:
					</td>
					<td>
						${matInput.minUnit }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						大包装单位:
					</td>
					<td>
						${matInput.packUnit }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						大包装数量:
					</td>
					<td>
						${matInput.packQty }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						零售价格:
					</td>
					<td>
						${matInput.salePrice }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						零售金额:
					</td>
					<td>
						${matInput.saleCost }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						入库数量:
					</td>
					<td>
						${matInput.inNum }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						入库时间:
					</td>
					<td class="honry-view">
						${matInput.inDate }&nbsp;
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