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
		<div id="advEl" class="easyui-layout" data-options="fit:true,border:false">   
			<div data-options="region:'north',split:false,border:false" style="height:40px;padding-left:10px;padding-top:5px;border-bottom:1px solid #95b8e7;">
				<input id="advSearchName" style="width:150px;">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="advQueryAdv()">查询</a>
			</div>
			<div data-options="region:'center',border:false">
				<div id="advEt" class="easyui-tabs" data-options="tabPosition:'bottom',fit:true,border:false" style="width:100%;height:100%;">
					<div title="长期医嘱">
						<table id="advSecularEd"></table>
					</div>
					<div title="临时医嘱">
						<table id="advInterimEd"></table>
					</div>
				</div>
			</div> 
		</div>
		<script type="text/javascript">
			$(function(){
				$('#advSearchName').textbox({prompt:'项目名称'});
				bindEnterEvent('advSearchName',advQueryAdv,'easyui');
			});
			/**  
			 *  
			 * 医嘱查询-长期医嘱列表
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			$('#advSecularEd').datagrid({
				url:'<%=basePath%>nursestation/analyze/queryAdvSecularInfo.action',
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
					[	{field:'ck',checkbox:true},
						{field:'sortId',width:'80',title:'顺序号'},
						{field:'typeName',width:'80',title:'医嘱类型'},
						{field:'moStat',width:'80',title:'医嘱状态',formatter:forMoSt},
						{field:'mainDrug',width:'50',title:'主药',formatter:forNoOrYse},
						{field:'itemName',width:'200',title:'医嘱名称'},
						{field:'combNo',width:'50',title:'组合',formatter:functionGroupAdvSecularEd},
						{field:'moNote2',width:'100',title:'备注'},
						{field:'qtyTot',width:'80',title:'总量'},
						{field:'priceUnit',width:'80',title:'包装单位',formatter:forDrugUnDrugUnitMap},
						{field:'minUnit',width:'80',title:'单位',formatter:forMinunitMap},
						{field:'doseOnce',width:'80',title:'每次量'},
						{field:'doseUnit',width:'80',title:'剂量单位',formatter:forDoseUnitMap},
						{field:'useDays',width:'80',title:'付数'},
						{field:'frequencyName',width:'100',title:'频次'},
						{field:'useName',width:'100',title:'用法'},
//  						{field:'usageCode',width:'100',title:'大类'},
						{field:'dateBgn',width:'150',title:'开始时间'},
						{field:'dateEnd',width:'150',title:'结束时间'},
						{field:'docName',width:'100',title:'开立医生'},
						{field:'execDpnm',width:'100',title:'执行科室'},
						{field:'emcFlag',width:'80',title:'加急',formatter:forYserOrNo},
						{field:'itemNote',width:'80',title:'检查部位'},
						{field:'labCode',width:'80',title:'样本类型'},
						{field:'pharmacyCode',width:'100',title:'扣库科室',formatter:forDept},
						{field:'recUsernm',width:'100',title:'录入人'},
						{field:'listDpcd',width:'100',title:'开立科室'},
						{field:'moDate',width:'150',title:'开立时间'},
						{field:'dcDate',width:'150',title:'停止时间'},
						{field:'dcUsernm',width:'100',title:'停止人'},
// 						{field:'hypotest',width:'80',title:'皮试标志'},
						{field:'subtblFlag',width:'80',title:'附材标志',formatter:forYserOrNo}
					]
				] 
			});  
			
			/**  
			 *  
			 * 医嘱查询-临时医嘱列表
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			$('#advInterimEd').datagrid({  
				url:'<%=basePath%>nursestation/analyze/queryAdvInterimInfo.action',
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
					[	{field:'ck',checkbox:true},
						{field:'sortId',width:'80',title:'顺序号'},
						{field:'typeName',width:'80',title:'医嘱类型'},
						{field:'moStat',width:'80',title:'医嘱状态',formatter:forMoSt},
						{field:'mainDrug',width:'50',title:'主药',formatter:forNoOrYse},
						{field:'itemName',width:'200',title:'医嘱名称'},
						{field:'combNo',width:'50',title:'组合',formatter:functionGroupAdvInterimEd},
						{field:'moNote2',width:'100',title:'备注'},
						{field:'qtyTot',width:'80',title:'总量'},
						{field:'priceUnit',width:'80',title:'包装单位',formatter:forDrugUnDrugUnitMap},
						{field:'minUnit',width:'80',title:'单位',formatter:forMinunitMap},
						{field:'doseOnce',width:'80',title:'每次量'},
						{field:'doseUnit',width:'80',title:'剂量单位',formatter:forDoseUnitMap},
						{field:'useDays',width:'80',title:'付数'},
						{field:'frequencyName',width:'100',title:'频次'},
						{field:'useName',width:'100',title:'用法'},
//  						{field:'usageCode',width:'100',title:'大类'},
						{field:'dateBgn',width:'150',title:'开始时间'},
						{field:'dateEnd',width:'150',title:'结束时间'},
						{field:'docName',width:'100',title:'开立医生'},
						{field:'execDpnm',width:'100',title:'执行科室'},
						{field:'emcFlag',width:'80',title:'加急',formatter:forYserOrNo},
						{field:'itemNote',width:'80',title:'检查部位'},
						{field:'labCode',width:'80',title:'样本类型',formatter:forSampleMap},
						{field:'pharmacyCode',width:'100',title:'扣库科室',formatter:forDept},
						{field:'recUsernm',width:'100',title:'录入人'},
						{field:'listDpcd',width:'100',title:'开立科室'},
						{field:'moDate',width:'150',title:'开立时间'},
						{field:'dcDate',width:'150',title:'停止时间'},
						{field:'dcUsernm',width:'100',title:'停止人'},
// 						{field:'hypotest',width:'80',title:'皮试标志'},
						{field:'subtblFlag',width:'80',title:'附材标志',formatter:forYserOrNo}
					]  
				]
			});
			
			/**  
			 *  
			 * 医嘱查询-条件查询
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function advQueryAdv(){
				var advSearchName = $('#advSearchName').textbox('getText');
				var tab = $('#advEt').tabs('getSelected');
				var index = $('#advEt').tabs('getTabIndex',tab);
				var dgId = index==0?'advSecularEd':'advInterimEd';
				$('#'+dgId).datagrid('load',{
					type:advSearchName
				});
			}
			
			/**  
			 *  
			 * 医嘱查询-长期医嘱组合号渲染
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function functionGroupAdvSecularEd(value,row,index){
				return functionGroup('advSecularEd',value,row,index);
			}
			
			/**  
			 *  
			 * 医嘱查询-临时医嘱组合号渲染
			 * @Author：aizhonghua
			 * @CreateDate：2016-6-22 下午04:41:31  
			 * @Modifier：aizhonghua
			 * @ModifyDate：2016-6-22 下午04:41:31  
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			function functionGroupAdvInterimEd(value,row,index){
				return functionGroup('advInterimEd',value,row,index);
			}
			
		</script>
	</body>
</html>