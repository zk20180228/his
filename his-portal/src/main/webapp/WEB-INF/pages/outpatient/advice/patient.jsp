<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',split:false,border:false" style="height:65px,margin-top:5px" class="patientInfoDateSize">
				<table>
					<tr>
						<td style="width:80px" class="inpatientSize1">起始时间:</td>
						<td style="width:220px" class="inpatientSize2">
							<input id="patientStartTime" class="Wdate" type="text" name="patientStartTime" value="${startTime}" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							<a href="javascript:void(0)" onclick="delTime('startTime')" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
						<td style="width:80px" class="inpatientSize3">终止时间:</td>
						<td style="width:220px" class="inpatientSize4">
							<input id="patientEndTime" class="Wdate" type="text" name="patientEndTime" value="${endTime}" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							<a href="javascript:void(0)"  onclick="delTime('endTime')" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr>
						<td style="width:80px" class="inpatientSize5">查询条件：</td>
						<td style="width:200px">
							<select id="ec" class="easyui-combobox" style="width:170px;">   
							    <option value="0">全部</option>   
							    <option value="1">病历号</option>   
							    <option value="2">发票号</option>   
							    <option value="3">姓名</option>   
							    <option value="4">处方号</option>   
							</select>
						</td>
						<td colspan="2" style="width:270px"><input id="et" class="easyui-textbox" style="width:242px"/></td>
						<td style="width:80px"><input id="cb" type="checkbox"/>模糊查询</td>
						<td><a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" id="searchDgButId" onclick="searchDg()">查询</a></td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',border:false" >
				<table id="patientEdvId"></table>
			</div>
		</div>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/datagrid-detailview.js"></script>
		<script type="text/javascript">
		$('#et').textbox({prompt:'请输入查询条件'});
		$('#patientEdvId').datagrid({  
			view:detailview,
			fit:true,
			rownumbers:true,
			striped:true,
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			pagination:true,
			pageNumber:1,
			pageSize:20,
			pageList:[10,20,30,40,50],
			//url: "<%=basePath%>outpatient/advice/queryPatientInfo.action",
			data:[],
			queryParams:{startTime:$('#patientStartTime').val(),endTime:$('#patientEndTime').val()},
			columns:[[
					{field:'recipeNo',title:'处方号',width:100,halign:'center',align:'right'},  
					{field:'name',title:'患者姓名',width:70,halign:'center',align:'right'},  
					{field:'sex',title:'性别',width:45,halign:'center',align:'right'},  
					{field:'age',title:'年龄',width:45,halign:'center',align:'right'},  
					{field:'recordNo',title:'病历号',width:100,halign:'center',align:'right'},  
					{field:'invoiceNo',title:'发票号',width:110,halign:'center',align:'right'},  
					{field:'disTable',title:'配药台',width:80,halign:'center',align:'right'},  
					{field:'disUser',title:'配药人',width:60,halign:'center',align:'right'},  
					{field:'disTime',title:'配药时间',width:80,halign:'center',align:'right'},  
					{field:'medTable',title:'发药台',width:80,halign:'center',align:'right'},  
					{field:'medUser',title:'发药人',width:80,halign:'center',align:'right'},  
					{field:'medTime',title:'发药时间',width:80,halign:'center',align:'right'}  
				]  
			],
			onLoadSuccess:function(data){
				var pager = $(this).datagrid('getPager');
				var aArr = $(pager).find('a');
				var iArr = $(pager).find('input');
				$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
				for(var i=0;i<aArr.length;i++){
					$(aArr[i]).tooltip({
						content:toolArr[i],
						hideDelay:1
					});
					$(aArr[i]).tooltip('hide');
				}
			},
			detailFormatter:function(index,row){
			    return '<div style="padding:2px"><table id="patientEdvId-' + index + '"></table></div>';  
			},  
			onExpandRow:function(index,row){
				$('#patientEdvId-'+index).datagrid({  
					url: "<%=basePath%>outpatient/advice/queryRecipelInfo.action",
					queryParams:{recipeNo:row.recipeNo,tab:row.tab},
					rownumbers:true,
					striped:true,
					border:true,
					checkOnSelect:true,
					selectOnCheck:false,
					singleSelect:true,
					height:'auto',  
					columns:[[  
					{field:'goodsName',title:'商品名',width:'12%',align:'center'},  
					{field:'oneDosage',title:'每次量',width:'8%',align:'center'},  
					{field:'usage',title:'用法',width:'10%',align:'center'},  
					{field:'frequency',title:'频次',width:'10%',align:'center'},  
					{field:'gross',title:'总量',width:'8%',align:'center'},  
					{field:'retailPrice',title:'零售价',width:'8%',align:'center'},  
					{field:'money',title:'金额',width:'8%',align:'center'},  
					{field:'dosageNum',title:'剂数',width:'8%',align:'center'},  
					{field:'validity',title:'有效性',width:'8%',align:'center',formatter:function(value,row,index){
								if(row.validity!=null&&row.validity!=''){
									if (row.validity==1){
										return '无效';
									} else {
										return '有效';
									}
								}
						}
					}
					]],  
					onResize:function(){  
						$('#patientEdvId').datagrid('fixDetailRowHeight',index);  
					},  
					onLoadSuccess:function(){  
					setTimeout(function(){  
						$('#patientEdvId').datagrid('fixDetailRowHeight',index);  
					},0);  
					}  
				});  
				$('#patientEdvId').datagrid('fixDetailRowHeight',index);  
			}  
	    });  
		setTimeout(function(){bindEnterEvent('et',searchDg,'easyui');},100);
		
		/**  
		 *  
		 * 查询
		 * @Author：aizhonghua
		 * @CreateDate：2016-6-22 下午04:41:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-6-22 下午04:41:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function searchDg(){
			var startTime = $('#patientStartTime').val();
			var endTime = $('#patientEndTime').val();
			var type = $('#ec').combobox('getValue');
			var para = $('#et').textbox('getText');
			var vague = 0;
			if($('#cb').is(':checked')){
				vague = 1;
			}
			$('#patientEdvId').datagrid({
				url: "<%=basePath%>outpatient/advice/queryPatientInfo.action",
				queryParams:{startTime:startTime,endTime:endTime,type:type,para:para,vague:vague}
				});
		}
		
		/**  
		 *  
		 * 清空时间查询
		 * @Author：aizhonghua
		 * @CreateDate：2016-6-22 下午04:41:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-6-22 下午04:41:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function delTime(id){
			$('#'+id).datetimebox('clear');
		}
		</script>
	</body>
</html>