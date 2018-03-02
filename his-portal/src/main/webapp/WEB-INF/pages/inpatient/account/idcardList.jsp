<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
	<body>
		<div id="divLayout" class="easyui-layout" fit=true>
	        	
			<div data-options="region:'center',split:false,title:'就诊卡信息',iconCls:'icon-book'" style="padding:10px;">
				<table id="idcardList" class="easyui-datagrid" data-options="method:'post',rownumbers:true,idField: 'id',border:true,singleSelect:false,pagination:true">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true" ></th>
							<th data-options="field:'patient',formatter:function(value,row,index){
								if (row.patient){
									return row.patient.patientName;
								} else {
									return value;
								}
							}">
							患者姓名</th>
							<th data-options="field:'idcardNo'">卡号</th>
							<th data-options="field:'medicalrecordId'">病历号</th>
							<th data-options="field:'idcardCreatetime'">建卡时间</th>
							<th data-options="field:'idcardType'">卡类型</th>
							<th data-options="field:'idcardOperator'">操作人员</th>
							<!--<th data-options="field:'createUser'"></th>
							<th data-options="field:'period'" style="width:15">时间点</th>
							<th data-options="field:'description'">说明</th>
							<th data-options="field:'order'">排序</th>
							<th data-options="field:'canSelect'" formatter="formatCheckBox">可选标志</th>
							<th data-options="field:'isDefault'" formatter="formatCheckBox">默认标志</th>
						--></tr>
					</thead>
				</table>
			</div>
		</div>
	</body>
</html>