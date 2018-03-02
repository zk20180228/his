<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<body>
			<table id="idcardList" class="easyui-datagrid"
					style="width:100%;height:445px;"
				data-options="method:'post',rownumbers:true,idField: 'id',border:true,singleSelect:false,pagination:true">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true"></th>
						<th
							data-options="field:'patient',formatter:function(value,row,index){
								if (row.patient){
									return row.patient.patientName;
								} else {
									return value;
								}
							}">
							患者姓名
						</th>
						<th data-options="field:'idcardNo'">
							卡号
						</th>
						<th data-options="field:'medicalrecordId',formatter:function(value,row,index){
								if (row.patient){
									return row.patient.medicalrecordId;
								} else {
									return value;
								}
							}">
							病历号
						</th>
						<th data-options="field:'idcardCreatetime'">
							建卡时间
						</th>
						<th data-options="field:'idcardType'">
							卡类型
						</th>
						<th data-options="field:'idcardOperator'">
							操作人员
						</th>
						<!--<th data-options="field:'createUser'"></th>
							<th data-options="field:'period'" style="width:15">时间点</th>
							<th data-options="field:'description'">说明</th>
							<th data-options="field:'order'">排序</th>
							<th data-options="field:'canSelect'" formatter="formatCheckBox">可选标志</th>
							<th data-options="field:'isDefault'" formatter="formatCheckBox">默认标志</th>
						-->
					</tr>
				</thead>
			</table>
</body>
