<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
</style>
	<script>
		function preview(oper) {
			if (oper < 10) {
				bdhtml = window.document.body.innerHTML;//获取当前页的html代码 
				sprnstr = "<!--startprint" + oper + "-->";//设置打印开始区域 
				eprnstr = "<!--endprint" + oper + "-->";//设置打印结束区域 
				prnhtml = bdhtml.substring(bdhtml.indexOf(sprnstr) + 18); //从开始代码向后取html

				prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));//从结束代码向前取html 
				window.document.body.innerHTML = prnhtml;
				window.print();
				window.document.body.innerHTML = bdhtml;
			} else {
				window.print();
			}
		}
	</script>
	<!--startprint1-->
		<c:if test="${!empty balancedetailList }">
		<div align="center" style="padding: 30px;">
			<table  border="1" cellspacing="0" cellpadding="0"
				width="1200px;" height="400px;" >
				<tr>
					<td>&nbsp;&nbsp;日结时间：</td>
					<td colspan="7">&nbsp;&nbsp;<fmt:formatDate value="${startTime}"
							pattern="yyyy年MM月dd日  HH时mm分ss秒" />
						&nbsp;&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;&nbsp; <fmt:formatDate
							value="${endTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></td>
				</tr>
				<c:forEach var="list" items="${balancedetailList }"
					varStatus="status">
					<tr>
						<td rowspan="2">&nbsp;&nbsp;${psyTypeMap[list.payType] }:</td>
						<td>&nbsp;&nbsp;挂号数量：</td>
						<td align="center">${list.regNum }</td>
						<td>&nbsp;&nbsp;挂号金额：</td>
						<td align="center">${list.regFee }</td>
						<td rowspan="2">&nbsp;&nbsp;合计：</td>
						<td rowspan="2"  align="center">数量：${list.sumNum }&nbsp;&nbsp;金额：${list.sumFee }</td>
					</tr>
					<tr>
						<td>&nbsp;&nbsp;退号数量：</td>
						<td align="center">${list.quitNum }</td>
						<td>&nbsp;&nbsp;退号金额：</td>
						<td align="center">${list.quitFee }</td>
					</tr>
				</c:forEach>
				<tr>
					<td>&nbsp;&nbsp;总计：</td>
					<td colspan="7">&nbsp;&nbsp;总挂号人数：${registerDaybalance.inNum }&nbsp;人&nbsp;&nbsp;&nbsp;&nbsp;总退号人数：${registerDaybalance.outNum }&nbsp;人&nbsp;&nbsp;&nbsp;&nbsp;实际看诊人数：${registerDaybalance.actualNum }&nbsp;人&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						共收入：${registerDaybalance.income }&nbsp;元&nbsp;&nbsp;&nbsp;&nbsp;共退费：${registerDaybalance.refund }&nbsp;元&nbsp;&nbsp;&nbsp;&nbsp;实际总收入：${registerDaybalance.actualIncome }&nbsp;元
					</td>
				</tr>
			</table>
		</div>	
		</c:if>
	<!--endprint1-->
	<div align="center">
		<input id="btnPrint" type="button" value="打印" onclick=preview(1)  style="text-align: center;" />
	</div>
</body>
</html>