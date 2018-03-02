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
		<div id="anaEl" class="easyui-layout" data-options="fit:true,border:false">
			<div data-options="region:'north',split:false,border:false" style="height:68px;padding-left:5px;padding-top:5px;border-bottom:1px solid #95b8e7;">
				<div id="ananEl" class="easyui-layout" data-options="fit:true,border:false">
					<div data-options="region:'north',split:false,border:false" style="height:30px;padding-left:5px;padding-top:5px;">
						&nbsp;<span style="font-size:12px">新开立：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#00FF00">&nbsp;&nbsp;</span>
						&nbsp;<span style="font-size:12px">已审核：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#4A4AFF">&nbsp;&nbsp;</span>
						&nbsp;<span style="font-size:12px">已执行：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#EEEE00">&nbsp;&nbsp;</span>
						&nbsp;<span style="font-size:12px">已作废：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#FF0000">&nbsp;&nbsp;</span>
						&nbsp;<span style="font-size:12px">重整：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#000000">&nbsp;&nbsp;</span>
						&nbsp;<span style="font-size:12px">医嘱存在批注：</span><span style="color:red">*</span>
						&nbsp;<span style="font-size:12px">知情同意书：</span><span style="font-size:12px">√</span>
						&nbsp;<span style="font-size:12px">存在附材：</span><span style="font-size:12px">@</span>
						&nbsp;<span style="font-size:12px">需审核药品：</span><span style="display:inline-block;width:12px;height:12px;background-image:url(${pageContext.request.contextPath}/themes/system/images/button/shen1.png)"></span>
					</div>
					<div id="anaCenId" data-options="region:'center',border:false">
						分解天数：<input id="anaDaysNumId" class="easyui-numberspinner" data-options="min:1,max:100" value="1" style="width:80px;">
					</div>
				</div>
			</div>
			<div id="anaCenId" data-options="region:'center',border:false">
				<div id="anaCenEl" class="easyui-layout" data-options="fit:true,border:false">   
					<div data-options="region:'west',split:false,border:true" title="医嘱信息" style="width:50%;">
						<table id="anaAdvEd"></table>
					</div>
					<div data-options="region:'center',border:false,title:'执行信息'">
						<div id="anaEt" class="easyui-tabs" data-options="tabPosition:'bottom',fit:true,border:false">
							<div title="药品">
								<table id="anaDrugEd"></table>
							</div>
							<div title="非药品">
								<table id="anaUnDrugEd"></table>
							</div>
						</div>
					</div> 
				</div>
			</div> 
		</div>
		<script type="text/javascript">
		/**  
		 *  
		 * panel事件
		 * @Author：aizhonghua
		 * @CreateDate：2016-6-22 下午04:41:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-6-22 下午04:41:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		$.extend($.fn.layout.paneldefaults,{ 
			 onCollapse:function () { 
				 $('#anaCenId').prop('style','');
				 var layout = $(this).parents("div.layout"); 
				 var opts = $(this).panel("options"); 
				 var expandKey = "expand" + opts.region.substring(0, 1).toUpperCase() + opts.region.substring(1); 
				 var expandPanel = layout.data("layout").panels[expandKey]; 
				 if (opts.region == "west" || opts.region == "east"){ 
					 var split = []; 
					 for (var i = 0; i < opts.title.length; i++) {
						 split.push(opts.title.substring(i, i + 1)); 
					 } 
					 expandPanel.panel("body").addClass("panel-title").css("text-align", "center").html(split.join("<br>")); 
				 }else { 
					 expandPanel.panel("setTitle", opts.title); 
				 }
			 }
		});
		
		/**  
		 *  
		 * 审核分解-医嘱信息
		 * @Author：aizhonghua
		 * @CreateDate：2016-6-22 下午04:41:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-6-22 下午04:41:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 * 
		 */
		$('#anaAdvEd').datagrid({
			view:detailview,
			method:'post',
			rownumbers:true,
			striped:true,
			border:false,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			pagination:false,
			fit:true,
			columns: [  
				[  	{field:'ck',checkbox:true},
				   	{field:'bedName',title:'床号',width:100,align:'center'},
					{field:'patientName',title:'姓名',width:100,align:'center'},
					{field:'inpatientNo',title:"住院流水号",width:550}
				]  
			],  
			detailFormatter:function(index,row){
				return '<div><table id="anaAdvEd_' + index + '"></table></div>';  
			},
			onCheck:function(index, row){
				if($('#anaAdvEd_'+index).attr('class')=='datagrid-f'){
					var ckArr = $('#anaAdvEd_'+index).datagrid('getPanel').find('input');
					for(var i=1;i<ckArr.length;i++){
						if(!$(ckArr[i]).is(":checked")){
							$('#anaAdvEd_'+index).datagrid('checkRow',i-1);
						}
					}
				}
			},
			onUncheck:function(index, row){
				if($('#anaAdvEd_'+index).attr('class')=='datagrid-f'){
					var ckArr = $('#anaAdvEd_'+index).datagrid('getPanel').find('input');
					for(var i=1;i<ckArr.length;i++){
						if($(ckArr[i]).is(":checked")){
							$('#anaAdvEd_'+index).datagrid('uncheckRow',i-1);
						}
					}
				}
			},
			onCheckAll:function(rows){
				var rows = $(this).datagrid('getRows');
				for(var i=0;i<rows.length;i++){
					$(this).datagrid('checkRow',i);
				}
			},
			onUncheckAll:function(rows){
				var rows = $(this).datagrid('getRows');
				for(var i=0;i<rows.length;i++){
					$(this).datagrid('uncheckRow',i);
				}
			},
			onExpandRow:function(index,row){
				$('#anaAdvEd_'+index).datagrid({  
					url: "<%=basePath%>nursestation/analyze/queryAdvInfoList.action",
					queryParams:{no:row.inpatientNo},
					rownumbers:true,
					striped:true,
					border:true,
					checkOnSelect:true,
					selectOnCheck:false,
					singleSelect:true,
					height:'auto',  
					columns:[[  
						{field: 'ck',checkbox:true},
						{field:'indexCC',title:'Ａ',width:25,align:'center',styler:functionColour},
						{field:'itemName',title:'项目名称',width:150,align:'left'},
						{field:'useName',title:'用法',width:70,align:'left'},
						{field:'frequencyName',title:'频次',width:100,align:'left'},
						{field:'doseOnce',title:'每次剂量',width:70,align:'center'},
						{field:'baseDose',title:'基本剂量',width:70,align:'center'},
						{field:'combNo',title:'组合',width:50,align:'center',formatter:function(value,row,i){
							return functionGroupAnaAdvEd('anaAdvEd_'+index,value,row,i);
						}},
						{field:'qtyTot',title:'数量',width:60,align:'center'},
						{field:'minUnit',title:'单位',width:60,align:'center',formatter:function(value,row,i){
							if(row.itemType==1){
								return minunitMap[value];
							}else{
								return value;
							}
						}},
						{field:'moDate',title:'开立时间',width:80,align:'center'},
						{field:'pharmacyCode',title:'取药科室',width:100,align:'center',formatter:forDept}
					]],  
					onResize:function(){  
						$('#anaAdvEd').datagrid('fixDetailRowHeight',index);
					},  
					onLoadSuccess:function(){
						if(isChecked('anaAdvEd',row)){
							$('#anaAdvEd_'+index).datagrid('checkAll');
						}
						setTimeout(function(){  
							$('#anaAdvEd').datagrid('fixDetailRowHeight',index);
						},0);  
					},
					onCheck:function(index, row){
						var rows = $(this).datagrid('getRows');
						var checkRows = $(this).datagrid('getChecked');
						if(checkRows.length==rows.length){
							var id = $(this).prop('id');
							var i = id.split('_')[1];
							$('#anaAdvEd').datagrid('checkRow',parseInt(i));
						}
					},
					onUncheck:function(index, row){
						var rows = $(this).datagrid('getRows');
						var checkRows = $(this).datagrid('getChecked');
						if(checkRows.length!=rows.length){
							var id = $(this).prop('id');
							var i = id.split('_')[1];
							var ckArr = $(this).datagrid('getPanel').find('input');
							var nckArr = new Array();
							for(var j=1;j<=ckArr.length;j++){
								nckArr[nckArr.length] = $(ckArr[j]).is(":checked");
							}
							$('#anaAdvEd').datagrid('uncheckRow',parseInt(i));
							for(var x=0;x<nckArr.length;x++){
								if(nckArr[x]){
									$(this).datagrid('checkRow',x);
								}
							}
						}
					},
					onCheckAll:function(rows){
						var id = $(this).prop('id');
						var i = id.split('_')[1];
						$('#anaAdvEd').datagrid('checkRow',parseInt(i));
					},
					onUncheckAll:function(rows){
						var id = $(this).prop('id');
						var i = id.split('_')[1];
						$('#anaAdvEd').datagrid('uncheckRow',parseInt(i));
					}
				});  
				$('#anaAdvEd').datagrid('fixDetailRowHeight',index);
			}
		}); 
		
		/**  
		 *  
		 * 审核分解-药品列表
		 * @Author：aizhonghua
		 * @CreateDate：2016-6-22 下午04:41:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-6-22 下午04:41:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		$('#anaDrugEd').datagrid({ 
			view:detailview,
			method:'post',
			rownumbers:true,
			striped:true,
			border:false,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			pagination:false,
			fit:true,
			columns: [  
				[  	{field:'ck',checkbox:true},
				   	{field:'bedName',title:'床号',width:100,align:'center'},
					{field:'patientName',title:'姓名',width:100,align:'center'},
					{field:'inpatientNo',title:"住院流水号",width:550}
				]  
			],  
			detailFormatter:function(index,row){
				return '<div><table id="anaDrugEd_' + index + '"></table></div>';  
			},
			onCheck:function(index, row){
				if($('#anaDrugEd_'+index).attr('class')=='datagrid-f'){
					var ckArr = $('#anaDrugEd_'+index).datagrid('getPanel').find('input');
					for(var i=1;i<ckArr.length;i++){
						if(!$(ckArr[i]).is(":checked")){
							$('#anaDrugEd_'+index).datagrid('checkRow',i-1);
						}
					}
				}
			},
			onUncheck:function(index, row){
				if($('#anaDrugEd_'+index).attr('class')=='datagrid-f'){
					var ckArr = $('#anaDrugEd_'+index).datagrid('getPanel').find('input');
					for(var i=1;i<ckArr.length;i++){
						if($(ckArr[i]).is(":checked")){
							$('#anaDrugEd_'+index).datagrid('uncheckRow',i-1);
						}
					}
				}
			},
			onCheckAll:function(rows){
				var rows = $(this).datagrid('getRows');
				for(var i=0;i<rows.length;i++){
					$(this).datagrid('checkRow',i);
				}
			},
			onUncheckAll:function(rows){
				var rows = $(this).datagrid('getRows');
				for(var i=0;i<rows.length;i++){
					$(this).datagrid('uncheckRow',i);
				}
			},
			onExpandRow:function(index,row){
				$('#anaDrugEd_'+index).datagrid({  
					url: "<%=basePath%>nursestation/analyze/queryAnaDrugInfoList.action",
					queryParams:{no:row.inpatientNo},
					rownumbers:true,
					striped:true,
					border:true,
					checkOnSelect:true,
					selectOnCheck:false,
					singleSelect:true,
					height:'auto',  
					columns: [[
						{field:'ck',checkbox:true},
// 						{field:'rowIndex',title:'Ａ',align:'center'}, 
						{field:'drugName',title:'项目名称',width:150,align:'left'},  
						{field:'useName',title:'用法',width:80,align:'left'},  
						{field:'frequencyName',title:'频次',width:100,align:'left'},	
						{field:'doseOnce',title:'每次剂量',width:80,align:'center'},
						{field:'baseDose',title:'基本剂量',width:80,align:'center'},  
						{field:'combNo',title:'组合',width:50,align:'center',formatter:function(value,row,i){
							return functionGroupAnaDrugEd('anaDrugEd_'+index,value,row,i);
						}},   
						{field:'qtyTot',title:'数量',width:50,align:'center'},
						{field:'minUnit',title:'单位',width:50,align:'center',formatter:function(value,row,i){
							return minunitMap[value];
						}}, 
						{field:'moDate',title:'开立时间',width:80,align:'center'},
						{field:'pharmacyName',title:'取药科室',width:100,align:'center'}
					]], 
					onResize:function(){  
						$('#anaDrugEd').datagrid('fixDetailRowHeight',index);  
					},  
					onLoadSuccess:function(){  
						if(isChecked('anaDrugEd',row)){
							$('#anaDrugEd_'+index).datagrid('checkAll');
						}
						setTimeout(function(){  
							$('#anaDrugEd').datagrid('fixDetailRowHeight',index);  
						},0);  
					}
					,
					onCheck:function(index, row){
						var rows = $(this).datagrid('getRows');
						var checkRows = $(this).datagrid('getChecked');
						if(checkRows.length==rows.length){
							var id = $(this).prop('id');
							var i = id.split('_')[1];
							$('#anaDrugEd').datagrid('checkRow',parseInt(i));
						}
					},
					onUncheck:function(index, row){
						var rows = $(this).datagrid('getRows');
						var checkRows = $(this).datagrid('getChecked');
						if(checkRows.length!=rows.length){
							var id = $(this).prop('id');
							var i = id.split('_')[1];
							var ckArr = $(this).datagrid('getPanel').find('input');
							var nckArr = new Array();
							for(var j=1;j<=ckArr.length;j++){
								nckArr[nckArr.length] = $(ckArr[j]).is(":checked");
							}
							$('#anaDrugEd').datagrid('uncheckRow',parseInt(i));
							for(var x=0;x<nckArr.length;x++){
								if(nckArr[x]){
									$(this).datagrid('checkRow',x);
								}
							}
						}
					},
					onCheckAll:function(rows){
						var id = $(this).prop('id');
						var i = id.split('_')[1];
						$('#anaDrugEd').datagrid('checkRow',parseInt(i));
					},
					onUncheckAll:function(rows){
						var id = $(this).prop('id');
						var i = id.split('_')[1];
						$('#anaDrugEd').datagrid('uncheckRow',parseInt(i));
					}
				});  
				$('#anaDrugEd').datagrid('fixDetailRowHeight',index);  
			}
		});  
		
		/**  
		 *  
		 * 审核分解-非药品列表
		 * @Author：aizhonghua
		 * @CreateDate：2016-6-22 下午04:41:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-6-22 下午04:41:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		$('#anaUnDrugEd').datagrid({  
			view:detailview,
			method:'post',
			rownumbers:true,
			striped:true,
			border:false,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			pagination:false,
			fit:true,
			columns: [  
				[  	{field:'ck',checkbox:true},
				   	{field:'bedName',title:'床号',width:100,align:'center'},
					{field:'patientName',title:'姓名',width:100,align:'center'},
					{field:'inpatientNo',title:"住院流水号",width:550}
				]  
			],  
			detailFormatter:function(index,row){
				return '<div><table id="anaUnDrugEd_' + index + '"></table></div>';  
			},
			onCheck:function(index, row){
				if($('#anaUnDrugEd_'+index).attr('class')=='datagrid-f'){
					var ckArr = $('#anaUnDrugEd_'+index).datagrid('getPanel').find('input');
					for(var i=1;i<ckArr.length;i++){
						if(!$(ckArr[i]).is(":checked")){
							$('#anaUnDrugEd_'+index).datagrid('checkRow',i-1);
						}
					}
				}
			},
			onUncheck:function(index, row){
				if($('#anaUnDrugEd_'+index).attr('class')=='datagrid-f'){
					var ckArr = $('#anaUnDrugEd_'+index).datagrid('getPanel').find('input');
					for(var i=1;i<ckArr.length;i++){
						if($(ckArr[i]).is(":checked")){
							$('#anaUnDrugEd_'+index).datagrid('uncheckRow',i-1);
						}
					}
				}
			},
			onCheckAll:function(rows){
				var rows = $(this).datagrid('getRows');
				for(var i=0;i<rows.length;i++){
					$(this).datagrid('checkRow',i);
				}
			},
			onUncheckAll:function(rows){
				var rows = $(this).datagrid('getRows');
				for(var i=0;i<rows.length;i++){
					$(this).datagrid('uncheckRow',i);
				}
			},
			onExpandRow:function(index,row){
				$('#anaUnDrugEd_'+index).datagrid({  
					url: "<%=basePath%>nursestation/analyze/queryAnaUnDrugInfoList.action",
					queryParams:{no:row.inpatientNo},
					rownumbers:true,
					striped:true,
					border:true,
					checkOnSelect:true,
					selectOnCheck:false,
					singleSelect:true,
					height:'auto',  
					columns: [[	
						{field:'ck',checkbox:true},
// 						{field:'rowIndex',title:'Ａ',align:'center'},
						{field:'undrugName',title:'项目名称',width:150,align:'left'},
						{field:'dfqCexp',title:'频次',width:80,align:'center'},
						{field:'combNo',title:'组合',width:80,align:'center',formatter:function(value,row,i){
							return functionGroupAnaUnDrugEd('anaUnDrugEd_'+index,value,row,i);
						}},
						{field:'qtyTot',title:'数量',width:80,align:'center'},
						{field:'stockUnit',title:'单位',width:80,align:'center'},
						{field:'moDate',title:'开立时间',width:100,align:'center'},
						{field:'execDeptcd',title:'执行科室',width:100,align:'center'}
					]],
					onResize:function(){  
						$('#anaUnDrugEd').datagrid('fixDetailRowHeight',index);  
					},  
					onLoadSuccess:function(){
						if(isChecked('anaUnDrugEd',row)){
							$('#anaUnDrugEd_'+index).datagrid('checkAll');
						}
						setTimeout(function(){  
							$('#anaUnDrugEd').datagrid('fixDetailRowHeight',index);  
						},0);  
					},
					onCheck:function(index, row){
						var rows = $(this).datagrid('getRows');
						var checkRows = $(this).datagrid('getChecked');
						if(checkRows.length==rows.length){
							var id = $(this).prop('id');
							var i = id.split('_')[1];
							$('#anaUnDrugEd').datagrid('checkRow',parseInt(i));
						}
					},
					onUncheck:function(index, row){
						var rows = $(this).datagrid('getRows');
						var checkRows = $(this).datagrid('getChecked');
						if(checkRows.length!=rows.length){
							var id = $(this).prop('id');
							var i = id.split('_')[1];
							var ckArr = $(this).datagrid('getPanel').find('input');
							var nckArr = new Array();
							for(var j=1;j<=ckArr.length;j++){
								nckArr[nckArr.length] = $(ckArr[j]).is(":checked");
							}
							$('#anaUnDrugEd').datagrid('uncheckRow',parseInt(i));
							for(var x=0;x<nckArr.length;x++){
								if(nckArr[x]){
									$(this).datagrid('checkRow',x);
								}
							}
						}
					},
					onCheckAll:function(rows){
						var id = $(this).prop('id');
						var i = id.split('_')[1];
						$('#anaUnDrugEd').datagrid('checkRow',parseInt(i));
					},
					onUncheckAll:function(rows){
						var id = $(this).prop('id');
						var i = id.split('_')[1];
						$('#anaUnDrugEd').datagrid('uncheckRow',parseInt(i));
					}
				});  
				$('#anaUnDrugEd').datagrid('fixDetailRowHeight',index);  
			}
		});
		
		/**  
		 *  
		 * 审核分解-医嘱状态渲染
		 * @Author：aizhonghua
		 * @CreateDate：2016-6-22 下午04:41:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-6-22 下午04:41:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function functionColour(value,row,index){
			if(row.moStat==null||row.moStat==''){
				return '';
			}else if(row.moStat==0){
				return 'background-color:#00FF00;';
			}else if(row.moStat==1){
				return 'background-color:#4A4AFF;';
			}else if(row.moStat==2){
				return 'background-color:#EEEE00;';
			}else if(row.moStat==3){
				return 'background-color:#FF0000;';
			}else if(row.moStat==4){
				return 'background-color:#000000;';
			}else{
				return '';
			}
		}
		
		/**  
		 *  
		 * 审核分解-医嘱信息组合号渲染
		 * @Author：aizhonghua
		 * @CreateDate：2016-6-22 下午04:41:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-6-22 下午04:41:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function functionGroupAnaAdvEd(id,value,row,index){
			return functionGroup(id,value,row,index);
		}
		
		/**  
		 *  
		 * 审核分解-药品信息组合号渲染
		 * @Author：aizhonghua
		 * @CreateDate：2016-6-22 下午04:41:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-6-22 下午04:41:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function functionGroupAnaDrugEd(id,value,row,index){
			return functionGroup(id,value,row,index);
		}
		
		/**  
		 *  
		 * 审核分解-非药品信息组合号渲染
		 * @Author：aizhonghua
		 * @CreateDate：2016-6-22 下午04:41:31  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-6-22 下午04:41:31  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function functionGroupAnaUnDrugEd(id,value,row,index){
			return functionGroup(id,value,row,index);
		}
		$('#anaEt').tabs({
				onSelect: function(title, index) {
					analyzeIndex = index
					var selectOk = tabObjectTrueKeyChangeArr(analyzetmpTabsOn);
					if(index == 0) {
						setTimeout(function(){
							selectOk.del.length > 0 && (eliminateAnalyze(selectOk.del.join(",")))
							if(selectOk.add.length > 0) {
								$('#anaDrugEd').datagrid('loading');
								AnalyzeDrugEd(selectOk.add.join(","))
							}
						},100)
					}
					if(index == 1) {
						setTimeout(function(){
							selectOk.del.length > 0 && (eliminateAnalyze(selectOk.del.join(",")))
							if(selectOk.add.length > 0) {
								$('#anaUnDrugEd').datagrid('loading');
								AnalyzeUnDrugEd(selectOk.add.join(","))
							}
						},100)
					}
					analyzetmpTabsOn = {};
				}
			});
		</script>
	</body>
</html>