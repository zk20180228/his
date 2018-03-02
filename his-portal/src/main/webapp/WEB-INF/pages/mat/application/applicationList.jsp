<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript">
	var departmentMap= "";//科室
	var empMap="";//人员
	var minUnitMap="";//最小单位
	$(function(){
		
		$('#dept').combobox({    
			url: '<%=basePath %>material/materials/findDeptRCombobox.action',
		    valueField:'id',    
		    textField:'deptName',
		    onSelect:function(record){
		    	$('#deptApp').datagrid('load', {
		    		deptId:record.deptId
		    	});
		    }
		});
		
		$('#bigenTime').datebox({
		    onSelect: function(date){
		    	var bigenTime = $('#bigenTime').datebox('getValue');
		    	var endTime = $('#endTime').datebox('getValue');
		    	$('#deptApp').datagrid('load', {
		    		bigenTime: bigenTime,
		        	endTime: endTime
		    	});
		    }
		});

		$('#endTime').datebox({
		    onSelect: function(date){
		    	var endTime = $('#endTime').datebox('getValue');
		    	var bigenTime = $('#bigenTime').datebox('getValue');
		    	$('#deptApp').datagrid('load', {
		    		endTime: endTime,
		    		bigenTime: bigenTime
		    	});
		    }
		});
		
		$.ajax({
			url: '<%=basePath %>mat/application/functionDept.action',
			type:'post',
			success: function(deptData) {
				departmentMap = eval("("+deptData+")");
			}
		});
		$.ajax({
			url: '<%=basePath %>mat/application/functionMinUnit.action',
			type:'post',
			success: function(deptData) {
				departmentMap = eval("("+deptData+")");
			}
		});	
		$.ajax({
			url: '<%=basePath %>mat/application/functionEmp.action',
			type:'post',
			success: function(empData) {
				empMap = eval("("+empData+")");
				$('#deptApp').datagrid({
					url: '<%=basePath %>mat/application/findDeptAppList.action',
					rownumbers: true,
					pagination: true,
			        onDblClickRow: function (rowIndex, rowData) {//双击查看
			        	var start = "";//状态
						var rowsd = $('#list').datagrid('getRows');
						if(rowsd.length>0){
							for(var i=0;i<rowsd.length;i++){
								if(rowData.itemCode==rowsd[i].itemCode){
									start = "1";//状态
									$.messager.alert("操作提示", "该物资存在");
								}
							}
							if(start!="1"){
								$.ajax({
					    			url: '<%=basePath %>mat/application/findRightAdd.action?no='+rowData.applyNo,
					    			type:'post',
					    			success: function(data) {
					    				dataObj = eval("("+data+")");
					    				if(dataObj.length>0){
					    					for(var i=0;i<dataObj.length>0;i++){
					    						var index = $('#list').edatagrid('appendRow',{
							    					itemName: dataObj[i].itemName,
							    					itemCode: dataObj[i].itemCode,
													specs: dataObj[i].specs,
													applyNum: dataObj[i].applyNum,
													minUnit: dataObj[i].minUnit,
													applyCost: dataObj[i].applyCost,
													examNum: dataObj[i].examNum,
													examCost: dataObj[i].examCost,
													saleCost: dataObj[i].saleCost,
													applyPrice: dataObj[i].applyPrice,
													salePrice: dataObj[i].salePrice,
													storeSum: dataObj[i].storeSum,
													memo: dataObj[i].memo
												}).datagrid('getRows').length-1;
					    					}
					    				}
					    			}
					    		});
							}
						}else{
							$.ajax({
								url: '<%=basePath %>mat/application/findRightAdd.action?no='+rowData.applyNo,
				    			type:'post',
				    			success: function(data) {
				    				dataObj = eval("("+data+")");
				    				if(dataObj.length>0){
				    					for(var i=0;i<dataObj.length>0;i++){
				    						var index = $('#list').edatagrid('appendRow',{
						    					itemName: dataObj[i].itemName,
						    					itemCode: dataObj[i].itemCode,
												specs: dataObj[i].specs,
												applyNum: dataObj[i].applyNum,
												minUnit: dataObj[i].minUnit,
												applyCost: dataObj[i].applyCost,
												examNum: dataObj[i].examNum,
												examCost: dataObj[i].examCost,
												saleCost: dataObj[i].saleCost,
												applyPrice: dataObj[i].applyPrice,
												salePrice: dataObj[i].salePrice,
												storeSum: dataObj[i].storeSum,
												memo: dataObj[i].memo
											}).datagrid('getRows').length-1;
				    					}
				    				}
				    			}
				    		});
						}
			        }    
				});
			}
		});	
		
		
		$('#tt').tabs({
			onSelect: function(title,index){
				if(title=="科室申请"){
					$('#deptApp').datagrid({
						url: '<%=basePath %>mat/application/findDeptAppList.action',
						pagination: true
					});
				}else if(title=="申请汇总"){
					$('#appReport').datagrid({
						url: '<%=basePath %>mat/application/findAppReportList.action',
						pagination: true,
						 onDblClickRow: function (rowIndex, rowData) {//双击查看
							 var start = "";//状态
							 var rowsd = $('#list').datagrid('getRows');
							 if(rowsd.length>0){
								 for(var i=0;i<rowsd.length;i++){
									if(rowData.itemCode==rowsd[i].itemCode){
										start = "1";//状态
										$.messager.alert("操作提示", "该物资存在");
									}
								}
								if(start!="1"){
									$.ajax({
						    			url: '<%=basePath %>mat/application/findAppReporSumtList.action?deptId='+rowData.storageCode,
						    			type:'post',
						    			success: function(data) {
						    				dataObj = eval("("+data+")");
						    				if(dataObj.length>0){
						    					for(var i=0;i<dataObj.length>0;i++){
						    						var index = $('#list').edatagrid('appendRow',{
								    					itemName: dataObj[i].itemName,
								    					itemCode: dataObj[i].itemCode,
														specs: dataObj[i].specs,
														applyNum: dataObj[i].applyNum,
														minUnit: dataObj[i].minUnit,
														applyCost: dataObj[i].applyCost,
														examNum: dataObj[i].examNum,
														examCost: dataObj[i].examCost,
														saleCost: dataObj[i].saleCost,
														applyPrice: dataObj[i].applyPrice,
														salePrice: dataObj[i].salePrice,
														storeSum: dataObj[i].storeSum,
														memo: dataObj[i].memo
													}).datagrid('getRows').length-1;
						    					}
						    				}
						    			}
						    		});
								 }
							 }else{
									$.ajax({
										url: '<%=basePath %>mat/application/findAppReporSumtList.action?deptId='+rowData.storageCode,
						    			type:'post',
						    			success: function(data) {
						    				dataObj = eval("("+data+")");
						    				if(dataObj.length>0){
						    					for(var i=0;i<dataObj.length>0;i++){
						    						var index = $('#list').edatagrid('appendRow',{
								    					itemName: dataObj[i].itemName,
								    					itemCode: dataObj[i].itemCode,
														specs: dataObj[i].specs,
														applyNum: dataObj[i].applyNum,
														minUnit: dataObj[i].minUnit,
														applyCost: dataObj[i].applyCost,
														examNum: dataObj[i].examNum,
														examCost: dataObj[i].examCost,
														saleCost: dataObj[i].saleCost,
														applyPrice: dataObj[i].applyPrice,
														salePrice: dataObj[i].salePrice,
														storeSum: dataObj[i].storeSum,
														memo: dataObj[i].memo
													}).datagrid('getRows').length-1;
						    					}
						    				}
						    			}
						    		});
								}
						 }
					});
				}
			}
		});
		
		
		$('#list').datagrid({
			onSelect: function (rowIndex, rowData) {//双击查看
				var rows = $('#list').datagrid('getRows');
				if(rows.length>0){
					for(var m=0;m<rows.length;m++){
						var indexRows = $('#list').datagrid('getRowIndex',rows[m]);
						$('#list').datagrid('endEdit',indexRows);
					}
				}
				$('#list').datagrid('beginEdit',rowIndex);
				var ed = $('#list').datagrid('getEditor', {index:rowIndex,field:'examNum'});
				var t = $(ed.target).numberbox('getText');
				$(ed.target).next("span").children().first().val("").focus().val(t);
				var  salePrice = rowData.salePrice;
				$(ed.target).numberbox('textbox').bind('keyup', function(event){
					var totalNum = $(ed.target).numberbox('getText');
					if(totalNum>rowData.applyNum){
						$.messager.alert("操作提示", "审批数量已经超过申请数量");
						$('#list').datagrid('updateRow',{
							index: rowIndex,
							row: {
								examNum: 0,
								examCost:0
							}
						});
					}else{
						var pirceSum = (rowData.salePrice)*totalNum;
						$('#list').datagrid('updateRow',{
							index: rowIndex,
							row: {
								examNum: totalNum,
								examCost:pirceSum
							}
						});
					}
					$('#list').datagrid('selectRow',rowIndex);
				});
			}
		});
	});
	//渲染科室
	function functionDept(value,row,index){
		if(value!=null&&value!=''){
			return departmentMap[value];
		}
	}
	//渲染人员
	function functionEmp(value,row,index){
		if(value!=null&&value!=''){
			return empMap[value];
		}
	}
	//渲染最小单位
	function functionMinUnit(value,row,index){
		if(value!=null&&value!=''){
			return minUnitMap[value];
		}
	}
	//保存
	function save(){
		var rows = $('#list').datagrid('getRows');
		var rowsJson = JSON.stringify(rows);
	}
</script>
</head>
<body>
<div id="divLayout" class="easyui-layout" fit=true style="width: 100%;height: 100%;">
	<div style="padding:5px 5px 5px 5px;">	
		<table style="width:100%;border:1px solid #95b8e7;padding:5px;">
			<tr>
				<td>
					<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					<a href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" iconCls="icon-save">保存</a>
					<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" iconCls="icon-remove">删除</a>
					<a href="javascript:void(0)" onclick="yulan()" class="easyui-linkbutton" iconCls="icon-search">预览</a>
					<a href="javascript:void(0)" onclick="exper()" class="easyui-linkbutton" iconCls="icon-down">打印</a>
				</td>
			</tr>
		</table>	   
	</div>
	<div id="cc" class="easyui-layout" style="width:100%;height:100%;">
		<div data-options="region:'west',title:'',split:true" style="width:700px;height:100%">
			<div style="padding:5px 5px 5px 5px;">	
				<table style="width:100%;border:1px solid #95b8e7;padding:5px;">
					<tr>
						<td>
							开始时间：<input class="easyui-datebox" id="bigenTime">
						</td>
					</tr>
					<tr>
						<td>
							结束时间：<input class="easyui-datebox" id="endTime">
						</td>
					</tr>
					<tr>
						<td>
							申请科室：<input class="easyui-combobox" id="dept">
						</td>
					</tr>
				</table>	   
			</div>
			<div style="padding:0px 5px 5px 5px;">	
				<div id="tt" class="easyui-tabs" style="width:100%;height:620px;">   
				    <div title="科室申请">   
				       <table id="deptApp" style="width:100%;height:100%" class="easyui-datagrid" data-options="idField:'id',border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
				       	   <thead>
								<tr>
									<th data-options="field:'applyListCode'"  style="width:20%" >单据号</th>
									<th data-options="field:'storageCode',formatter:functionDept" style="width:20%">申请科室</th>
									<th data-options="field:'applyCost'" style="width:10% ">申请金额</th>
									<th data-options="field:'saleCost'" style="width:10% ">零售金额</th>
									<th data-options="field:'applyOper',formatter:functionEmp"  style="width:15%" >申请人</th>
									<th data-options="field:'applyDate'"  style="width:15%" >申请时间</th>
									<th data-options="field:'validState'"  style="width:10%" >是否有效</th>
								</tr>
							</thead>
				    	</table>
				    </div>   
				    <div title="申请汇总">   
				       <table id="appReport" style="width:100%;height:100%" class="easyui-datagrid" data-options="idField:'id',border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
				       	   <thead>
								<tr>
									<th data-options="field:'storageCode',formatter:functionDept"  style="width:35%" >申请科室 </th>
									<th data-options="field:'applyCost'"  style="width:35% ">申请金额</th>
									<th data-options="field:'preDate'"  style="width:25% ">零售金额</th>
								</tr>
							</thead>
				    	</table>
				    </div>   
				</div>  
			</div>
		</div>
		<div data-options="region:'center',title:''" style="padding:5px;">
			<div style="padding:5px 5px 5px 5px;">	
				<table id="list" style="width:100%;height:720px" class="easyui-datagrid" data-options="idField:'id',border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
		       	   <thead>
						<tr>
							<th data-options="field:'itemName'"  style="width:10%" >物品名称 </th>
							<th data-options="field:'itemCode',hidden:true"  style="width:10%" >物品Code </th>
							<th data-options="field:'specs'"  style="width:10% ">规格</th>
							<th data-options="field:'applyNum'"  style="width:10% ">申请数量</th>
							<th data-options="field:'minUnit',formatter:functionMinUnit"  style="width:10%" >最小单位</th>
							<th data-options="field:'applyCost'"  style="width:5%" >申请金额</th>
							<th data-options="field:'examNum',editor:{type:'numberbox'}"  style="width:10%" >审批数量</th>
							<th data-options="field:'examCost'"  style="width:10%" >审批金额 </th>
							<th data-options="field:'saleCost'"  style="width:10%" >审批零售金额 </th>
							<th data-options="field:'applyPrice'"  style="width:10%" >购入价</th>
							<th data-options="field:'salePrice'"  style="width:10%" >零售价</th>
							<th data-options="field:'storeSum'"  style="width:10%" >库存</th>
							<th data-options="field:'memo'"  style="width:10%" >备注</th>
						</tr>
					</thead>
		    	</table>
			</div>
		</div>   
	</div>
</div>
</body>
</html>