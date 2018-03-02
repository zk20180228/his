<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	</head>
	<body style="margin:0px;padding:0px">
		<div id="exeEl" class="easyui-layout" data-options="fit:true,border:false">   
			<div data-options="region:'north',split:false,border:false" style="height:40px;padding-left:10px;padding-top:5px;border-bottom:1px solid #95b8e7;">
				<input id="exeSearchName" style="width:150px;">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="exeQueryExe()">查询</a>
			</div>   
			<div data-options="region:'center',border:false">
				<div id="exeEt" class="easyui-tabs" data-options="tabPosition:'bottom',fit:true,border:false" style="width:100%;height:100%;">
					<div title="药品">
						<table id="exeDrugEd"></table>
					</div>
					<div title="非药品">
						<table id="exeUnDrugEd"></table>
					</div>
				</div>
			</div> 
		</div>
		<script type="text/javascript">
			$(function(){
				$('#exeSearchName').textbox({prompt:'项目名称'});
				bindEnterEvent('exeSearchName',exeQueryExe,'easyui');
			});
			/**  
			 *  
			 * 医嘱执行查询-药品列表
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			$('#exeDrugEd').datagrid({  
				url:'<%=basePath%>nursestation/analyze/queryExeDrugInfo.action',
				method:'post',
				rownumbers:true,
				striped:true,
				border:false,
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				fitColumns:false,
				pagination:true,
				fit:true,
				pageSize:20,
				pageList:[20,30,50,80,100],
				columns: [  
					[  	{field:'ck',checkbox:true},
					   	{field:'prnFlag',title:'标记',width:80,align:'center',formatter:forPrnFlag},
						{field:'docName',title:'开立医生',width:100,align:'center'},  
						{field:'typeCode',title:'医嘱类型',width:100,align:'center'},
						{field:'validFlag',title:'有效',width:80,align:'center',formatter:forValidFlag},
						{field:'drugedFlag',title:'发送状态',width:80,align:'center',formatter:forDrugedFlag},
						{field:'drugName',title:'药品名称',width:150,align:'center'},
						{field:'specs',title:'规格',width:100,align:'center'},
						{field:'qtyTot',title:'总量',width:100,align:'center'},
						{field:'priceUnit',title:'包装单位',width:100,align:'center',formatter:forPackunitMap},
						{field:'doseOnce',title:'每次剂量',width:100,align:'center'}, 
						{field:'doseUnit',width:'80',title:'剂量单位',formatter:forDoseUnitMap},
						{field:'frequencyName',title:'频次',width:100,align:'center'},  
						{field:'useName',title:'用法',width:100,align:'center'}, 
						{field:'packQty',title:'包装数',width:80,align:'center'},   
						{field:'useDays',title:'付数',width:80,align:'center'}, 
						{field:'useTime',title:'应执行时间',width:180,align:'center'},
						{field:'decoDate',title:'分解时间',width:180,align:'center'},
						{field:'chargeDate',title:'记账时间',width:180,align:'center'},
						{field:'drugedDate',title:'发药时间',width:180,align:'center'}, 
						{field:'moDate',title:'医嘱时间',width:180,align:'center'},   
						{field:'pharmacyCode',title:'取药药房',width:150,align:'center'},   
						{field:'moNote1',title:'医嘱说明',width:150,align:'center'},   
						{field:'moNote2',title:'备注',width:150,align:'center'}, 
						{field:'moOrder',title:'医嘱号',width:100,align:'center'},   
						{field:'combNo',title:'组合号',width:80,align:'center',formatter:functionGroupExeDrugEd},   
						{field:'execId',title:'执行号',width:100,align:'center'}, 
						{field:'chargeFlag',title:'记账标记',width:100,align:'center',formatter:forChargeFlag}, 
						{field:'chargeUsercd',title:'记账人',width:100,align:'center'},
						{field:'drugedDeptcd',title:'发药科室',width:120,align:'center'},
						{field:'drugedUsercd',title:'发药人',width:100,align:'center'},
						{field:'validUsercd',title:'停止人',width:100,align:'center'},
						{field:'recipeNo',title:'处方号',width:100,align:'center'},   
						{field:'sequenceNo',title:'处方内流水号',width:100,align:'center'},   
						{field:'chargePrnflag',title:'发送单打印标记',width:100,align:'center'}, 
						{field:'chargePrndate',title:'打印时间',width:100,align:'center'},   
// 						{field:'doseModelCode',title:'剂型名称',width:100,align:'center'},
						{field:'deptCode',title:'住院科室',width:120,align:'center'},
						{field:'nurseCellCode',title:'护理站',width:120,align:'center'},
						{field:'listDpcd',title:'开立科室',width:120,align:'center'}
					]  
				] 
			});  
			
			/**  
			 *  
			 * 医嘱执行查询-非药品列表
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			$('#exeUnDrugEd').datagrid({ 
				url:'<%=basePath%>nursestation/analyze/queryExeUnDrugInfo.action',
				method:'post',
				rownumbers:true,
				striped:true,
				border:false,
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				fitColumns:false,
				pagination:true,
				fit:true,
				pageSize:20,
				pageList:[20,30,50,80,100],
				columns: [  
					[  	{field:'ck',checkbox:true},
						{field:'docName',title:'开立医生',width:100,align:'center'}, 
						{field:'typeCode',title:'医嘱类型',width:80,align:'center'},
						{field:'validFlag',title:'有效',width:80,align:'center',formatter:forValidFlag},
						{field:'undrugName',title:'项目名称',width:180,align:'center'},
						{field:'stockUnit',title:'单位',width:80,align:'center'},
						{field:'useTime',title:'应执行时间',width:180,align:'center'},
						{field:'decoDate',title:'分解时间',width:180,align:'center'},
						{field:'chargeDate',title:'记账时间',width:180,align:'center'},
						{field:'moDate',title:'医嘱时间',width:180,align:'center'},
						{field:'validDate',title:'停止时间',width:180,align:'center'},
						{field:'dfqCexp',title:'频次',width:100,align:'center'},
						{field:'moNote1',title:'医嘱说明',width:100,align:'center'},
						{field:'moNote2',title:'备注',width:100,align:'center'},
						{field:'moOrder',title:'医嘱号',width:100,align:'center'},   
						{field:'combNo',title:'组合号',width:80,align:'center',formatter:functionGroupExeUnDrugEd},
						{field:'execId',title:'执行号',width:80,align:'center'},  
						{field:'subtblFlag',title:'是否附材',width:80,align:'center',formatter:forYserOrNo}, 
						{field:'execDpnm',title:'执行科室',width:180,align:'center'},
						{field:'chargeFlag',title:'记账标记',width:80,align:'center',formatter:forChargeFlag}, 
						{field:'chargeUsercd',title:'记账人',width:100,align:'center'},
						{field:'validUsercd',title:'停止人',width:100,align:'center'},
						{field:'recipeNo',title:'处方号',width:100,align:'center'},
						{field:'sequenceNo',title:'处方内流水号',width:100,align:'center'},
					]  
				]
			});

			/**  
			 *  
			 * 医嘱执行查询-条件查询
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function exeQueryExe(){
				var exeSearchName = $('#exeSearchName').textbox('getText');
				var tab = $('#exeEt').tabs('getSelected');
				var index = $('#exeEt').tabs('getTabIndex',tab);
				var dgId = index==0?'exeDrugEd':'exeUnDrugEd';
				$('#'+dgId).datagrid('load',{name:exeSearchName});
			}
			
			/**  
			 *  
			 * 医嘱执行查询-药品执行信息组合号渲染
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function functionGroupExeDrugEd(value,row,index){
				return functionGroup('exeDrugEd',value,row,index);
			}
			
			/**  
			 *  
			 * 医嘱执行查询-非药品执行信息组合号渲染
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function functionGroupExeUnDrugEd(value,row,index){
				return functionGroup('exeUnDrugEd',value,row,index);
			}
			
		
		</script>
	</body>
</html>