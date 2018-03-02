<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<body>
	<div class="easyui-panel" data-options="title:'病床详细信息',iconCls:'icon-book'" style="width: 580px">
		<div style="padding: 10px">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr>
					<td class="honry-lable">
						数量:
					</td>
					<td class="honry-view">
						${financeFixedcharge.chargeAmount }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						单价:
					</td>
					<td class="honry-view">
						${financeFixedcharge.chargeUnitprice }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						开始时间:
					</td>
					<td class="honry-view">
						${financeFixedcharge.sDate }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						结束时间:
					</td>
					<td class="honry-view">
						${financeFixedcharge.eDate }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						是否与婴儿相关:
					</td>
					<td>
					${financeFixedcharge.chargeIsaboutchildren }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						是否与时间相关:
					</td>
					<td>
						${financeFixedcharge.chargeIsabouttime }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						状态:
					</td>
					<td class="honry-view">
						${financeFixedcharge.chargeState }&nbsp;
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