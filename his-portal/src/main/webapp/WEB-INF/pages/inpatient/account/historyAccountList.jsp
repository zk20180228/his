<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
	<body>
	<div id="divLayout" class="easyui-layout" fit=true title="历史预存金">
		<div data-options="region:'center',split:false,title:'历史预存金',iconCls:'icon-book'" style="padding:10px;">
						<table id="hislist" title="历史预存金" fitColumns='true' border="false" class="easyui-datagrid"
							data-options="method:'post',rownumbers:true,idField: 'id',border:true,singleSelect:false,pagination:true">
							<thead>
								<tr>
									<th field="ck" checkbox="true"></th>								
									<th data-options="field:'detailDebitamount'" style="width:6%">
										充值金额	
									</th>
									<th data-options="field:'detailCreditamount'" style="width:6%">
										消费金额
									</th>
									<th data-options="field:'detailPaytype'" style="width:6%">
										支付方式
									</th>
									<th data-options="field:'detailBankunit'" style="width:7%">
										开户单位
									</th>
									<th data-options="field:'detailBank'" style="width:7%">
										开户银行
									</th>
									<th data-options="field:'detailBankaccount'" style="width:9%">
										银行账号
									</th>
									<th data-options="field:'detailOptype',formatter:function(val,row){
														if (val == 1){
															return '付款';
														} else if (val == 2){
															return '返还';
														} else if (val == 3){
															return '补打';
														} else if (val == 4){
															return '结清账户';
														}
													}" style="width:9%">
										类型
									</th>
								</tr>
							</thead>
							
						</table>
					</div>
			</div>
	</body>

</html>
