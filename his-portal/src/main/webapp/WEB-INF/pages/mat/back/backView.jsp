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
						回收流水号:
					</td>
					<td class="honry-view">
						${back.matBackNo}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						回收序号:
					</td>
					<td class="honry-view">
						${back.itemNo }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						所在科室:
					</td>
					<td class="honry-view">
						${back.matDeptName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						回收科室:
					</td>
					<td>
						${back.matBackDname }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						物品科目:
					</td>
					<td>
						${back.kindName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						回收项目:
					</td>
					<td>
						${back.itemName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						规格:
					</td>
					<td>
						${back.specs }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						最小单位:
					</td>
					<td>
						${back.minUnit }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						大包装单位:
					</td>
					<td>
						${back.packUnit }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						大包装数量:
					</td>
					<td>
						${back.packQty }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						回收价格:
					</td>
					<td>
						${back.backPrice }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						零售价格:
					</td>
					<td>
						${back.salePrice }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						生产厂商:
					</td>
					<td>
						${back.producerName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						回收数量:
					</td>
					<td>
						${back.backNumber }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						创建时间:
					</td>
					<td class="honry-view">
						${back.createTime }&nbsp;
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