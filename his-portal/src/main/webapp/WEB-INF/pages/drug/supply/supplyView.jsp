<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<body>
	<div class="easyui-panel" data-options="title:'详细信息',iconCls:'icon-book',fit:true" style="width: 580px">
		<div style="padding: 10px">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr>
					<td class="honry-lable">
						名称:
					</td>
					<td class="honry-view">
						${supplycompany.companyName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						拼音码:
					</td>
					<td class="honry-view">
						${supplycompany.companyPinyin }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						五笔码:
					</td>
					<td class="honry-view">
						${supplycompany.companyWb }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						自定义码:
					</td>
					<td class="honry-view">
						${supplycompany.companyInputcode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						联系方式:
					</td>
					<td>
					${supplycompany.companyLink }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						开户银行:
					</td>
					<td>
						${supplycompany.companyBank }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						开户账号:
					</td>
					<td class="honry-view">
						${supplycompany.companyAccount }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						地址:
					</td>
					<td class="honry-view">
						${supplycompany.companyAddress }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						GSP:
					</td>
					<td class="honry-view">
						${supplycompany.companyGsp }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						备注:
					</td>
					<td class="honry-view">
						${supplycompany.companyRemark }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						创建时间:
					</td>
					<td class="honry-view">
						${supplycompany.createTime }&nbsp;
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