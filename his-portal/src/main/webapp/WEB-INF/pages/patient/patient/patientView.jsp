<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<body>
	<div class="easyui-panel" data-options="title:'病床详细信息',iconCls:'icon-book'" style="width: 580px">
		<div style="padding: 10px">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr>
					<td class="honry-lable">
						患者姓名:
					</td>
					<td class="honry-view">
						${patient.patientName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						拼音码:
					</td>
					<td class="honry-view">
						${patient.patientPinyin }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						五笔码:
					</td>
					<td class="honry-view">
						${hospitalpatient.patientWbbed.bedState }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						自定义码:
					</td>
					<td class="honry-view">
						${patient.patientInputcode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						性别:
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						出生日期:
					</td>
					<td>
						${patient.patientDoorno }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						家庭地址:
					</td>
					<td class="honry-view">
						${patient.patientAddress }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						门牌号:
					</td>
					<td class="honry-view">
						${patient.patientDoorno }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						电话:
					</td>
					<td class="honry-view">
						${patient.patientPhone }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						证件类型 :
					</td>
					<td class="honry-view">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						证件号码:
					</td>
					<td class="honry-view">
						${patient.patientCertificatesno}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						出生地:
					</td>
					<td class="honry-view">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						国籍:
					</td>
					<td class="honry-view">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						民族:
					</td>
					<td class="honry-view">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						工作单位:
					</td>
					<td class="honry-view">
						${patient.patientWorkunit }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						单位电话:
					</td>
					<td class="honry-view">
						${patient.patientWorkphone }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						婚姻状况:
					</td>
					<td class="honry-view">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						职业
					</td>
					<td class="honry-view">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						医保手册号:
					</td>
					<td class="honry-view">
						${patient.patientHandbook }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						电子邮箱:
					</td>
					<td class="honry-view">
						${patient.patientEmail }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						母亲姓名:
					</td>
					<td class="honry-view">
						${patient.patientMother }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						联系人:
					</td>
					<td class="honry-view">
						${patient.patientLinkman }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						联系人关系:
					</td>
					<td class="honry-view">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						联系人地址:
					</td>
					<td class="honry-view">
						${patient.patientLinkaddress }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						联系人门牌号:
					</td>
					<td class="honry-view">
						${patient.patientLinkdoorno }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						联系电话:
					</td>
					<td class="honry-view">
						${patient.patientLinkphone }&nbsp;
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