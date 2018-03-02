<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<body>
			<table id="hislist"  fitColumns='true' border="false"
				class="easyui-datagrid"
				style="width:100%;height:445px;"
				data-options="method:'post',rownumbers:true,idField: 'id',border:true,pagination:true">
				<thead>
					<tr>
						<th data-options="field:'detailDebitamount'" style="width: 8%">
							充值金额
						</th>
						<th data-options="field:'detailCreditamount'" style="width: 8%">
							消费金额
						</th>
						<th data-options="field:'detailPaytype',formatter:functionPayType" style="width: 10%">
							支付方式
						</th>
						<th data-options="field:'detailBankunit'" style="width: 15%">
							开户单位
						</th>
						<th data-options="field:'detailBank',formatter:functionBank" style="width: 15%">
							开户银行
						</th>
						<th data-options="field:'detailBankaccount'" style="width: 15%">
							银行账号
						</th>
						<th
							data-options="field:'detailOptype',formatter:function(val,row){
														if (val == 1){
															return '付款';
														} else if (val == 2){
															return '返还';
														} else if (val == 3){
															return '补打';
														} else if (val == 4){
															return '结清账户';
														}
													}" style="width: 10%">
							类型
						</th>
					</tr>
				</thead>

			</table>
</body>


