<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="java.net.URLDecoder"%>

<jsp:include page="/javascript/default.jsp"></jsp:include>

	<body>
		<div id="panelEast" class="easyui-panel" data-options="title:'财务组信息',iconCls:'icon-book',border:false" style="width: 580px">
			<div style="padding: 10px">
				<input type="hidden" id="id" name="id" value="${financeUsergroup.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
					<tr>
						<td class="honry-lable">
							分组名称:
						</td>
						<td class="honry-view">
							${financeUsergroup.groupName}&nbsp;
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							拼音码:
						</td>
						<td class="honry-view">
							${financeUsergroup.groupPinyin}&nbsp;
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							五笔码:
						</td>
						<td class="honry-view">
							${financeUsergroup.groupWb}&nbsp;
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							自定义码:
						</td>
						<td class="honry-view">
							${financeUsergroup.groupInputcode }&nbsp;
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							备注:
						</td>
						<td>
						${financeUsergroup.stackRemark }&nbsp;
						</td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
				</div>
			</div>
		</div>
	<script>
		//关闭页面
		function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
	</script>
	</body>
</html>