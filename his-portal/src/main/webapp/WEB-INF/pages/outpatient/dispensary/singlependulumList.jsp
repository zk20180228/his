<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + 

request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div id="cc" class="easyui-layout" style="width:100%;height:100%;">   
		<div data-options="region:'north',split:true" style="height:8%;">
			<form id="search" method="post">
					<table style="width:100%;padding:15px;">
						<tr>
							<td style="padding: 0px 0px 0px 100px;">
								<a href="javascript:void(0)" onclick="searchFrom()"  class="easyui-linkbutton" >合并</a>
							</td>
							<td style="padding: 0px 900px 0px 0px;">
								<a href="javascript:void(0)" onclick="tuichu()"  class="easyui-linkbutton" >退出</a>
							</td>
						</tr>
					</table>
				</form>
		</div>
		<div data-options="region:'center'" style="height:92%;width:85%;">
			<div id="aa" class="easyui-layout" style="width:100%;height:100%;">   
			    <div data-options="region:'north',title:'摆药确认'" style="height:11%;width: 100%">
			    	<table style="width:100%;padding:15px;">
						<tr>
							<td>核准人：<input id="patientName" name="patientName"
								class="easyui-textbox" data-options="required:true">&nbsp;&nbsp;&nbsp;&nbsp;姓名：<input id="patientName" name="patientName"
								class="easyui-textbox" data-options="required:true">&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<a href=""  class="easyui-linkbutton" >确定</a>
								<span style="color: blue;">多张同时核准前 请先进行合并单据操作</span>
								</td>	
						</tr>
					</table>
			    </div>   
			    <div data-options="region:'center'" style="height:85%;width: 100%">
			    	<div id="bb" class="easyui-tabs" fit="true" style="width:100%;">   
					    <div title="摆药单预览">    
							<div id="dd" class="easyui-tabs" fit="true" style="width:100%;">   
							    <div title="明细摆药单" style="padding:20px;text-align: center;">
							    	<span id="baiyaodan1" style="font-size: 22;text-align: center;"></span><br/>
							    	<span id="baiyaodan2" style="text-align: left;"></span><span id="baiyaodan3" style="text-align: right;"></span>      
									<table id="adDgList1" style="text-align: center;" data-options="striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,width:'100%'">   
									</table>
							    </div>   
							</div>
					    </div>   
					    <div title="摆药明细" style="padding:20px;">   
					        <table id="adDgList2" data-options="striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,width:'100%'">   
							</table>    
					    </div>
					    <div title="科室汇总" style="padding:20px;text-align: center;">   
					        <table id="adDgList3" data-options="striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,width:'80%'">   
							</table>    
					    </div>   
					</div>
			    </div>   
			</div>
		</div>   
	    <div data-options="region:'west'" style="height:92%;width:15%;">
	    	<div id="aa" class="easyui-layout" style="width:100%;height:100%;">   
			    <div data-options="region:'north'" style="height:15%;width: 100%">
			    	<table style="width:100%;padding:15px;">
						<tr>
							<td style="padding: 0px 0px 25px 0px;">摆药日期：</td>
							<td style="padding: 0px 0px 25px 0px;"><input id="baiyaoriqi" name="patientName"
								class="easyui-datebox" data-options="required:true">
							</td>
						</tr>
						<tr>
							<td style="padding: 0px 0px 10px 0px;">摆药单号：</td>
							<td style="padding: 0px 0px 10px 0px;"><input id="baiyaodanhao" name="patientName"
								class="easyui-textbox" data-options="required:true">
							</td>
						</tr>
					</table>
			    </div>   
			    <div data-options="region:'center'" style="height:85%;width: 100%">
			    	<div id="tt" class="easyui-tabs" fit="true" style="width:100%;">   
					    <div title="未核准" style="padding:20px;">    
							<ul id="tDt1"></ul>
							<input type="hidden" id="baiyaotai" value="${controlName }"/>
					    </div>   
					    <div title="已核准" style="padding:20px;">   
					        <ul id="tDt"></ul>    
					    </div>   
					</div>
			    </div>   
			</div>
	    </div>  
	</div>
	<script type="text/javascript">
		var drugMinimumunitList="";
		var now = "${now}";
		var drugedBill = "";
		var deptCode = "";
		var typeCodeList = "";
		var unitMap = "";
		var controlName="";
		function tuichu(){
			window.location.href="<%=basePath %>outpatient/dispensarylist.action"; 
		}
		$(function(){
			//回车时间
			bindEnterEvent('baiyaodanhao',query,'easyui');
		});
		//回车时间
		function query(){
			var drugedBill = $('#baiyaodanhao').textbox('getValue');
				$('#adDgList1').datagrid({
					striped : true,
					checkOnSelect : true,
					selectOnCheck : true,
					singleSelect : true,
					fitColumns : false,
					pagination : true,
					rownumbers : true,
					pageSize : 10,
					pageList : [ 10, 20, 30, 50, 100 ],
					url : '<%=basePath%>outpatient/queryDrugApplyoutList.action?deptCode='+deptCode+'&drugedBill='+drugedBill,  
				    columns:[[    
				        {field:'patientNameAndbed',title:'[床号]姓名',width:200},    
				        {field:'placeCode',title:'货位号',width:200},    
				        {field:'tradeName',title:'药品名称',width:200},
				        {field:'specs',title:'规格',width:200},
				        {field:'packQty',title:'数量',width:100,align:'center'},
				        {field:'useName',title:'给药途径',width:200},
				        {field:'dfqCexp',title:'频次',width:200},
				        {field:'doseOnce',title:'用量',width:100,align:'right'},
				        {field:'doseUnit',title:'单位',width:100}
				    ]]    
				});
				 $('#adDgList2').datagrid({
					striped : true,
					checkOnSelect : true,
					selectOnCheck : true,
					singleSelect : true,
					fitColumns : false,
					pagination : true,
					rownumbers : true,
					remoteSort :false,
					sortable :true,
					pageSize : 10,
					pageList : [ 10, 20, 30, 50, 100 ],
					onBeforeLoad:function (param) {
						$.ajax({
							url : '<%=basePath%>inpatient/billInpatientKind/likeInpatientKind.action',
							type : 'post',
							success : function(ordercategoryData) {
								typeCodeList = eval("("
										+ ordercategoryData + ")");
							}
						});
						$.ajax({
							url:"<%=basePath %>likeCodeDrugpackagingunit.action",
							type:'post',
							success: function(unitData) {
								unitMap = eval("("+unitData+")");
							}
						});
				 	},
					url : '<%=basePath%>outpatient/queryDrugApplyoutmingxiList.action?deptCode='+deptCode+'&drugedBill='+drugedBill,  
				    columns:[[    
				        {field:'bedName',title:'床号',width:50},
				        {field:'patientName',title:'姓名',width:100},
				        {field:'tradeNameAndspecs',title:'药品名称[规格]',width:200},    
				        {field:'retailPrice',title:'零售价',width:80},
				        {field:'doseOnce',title:'用量',width:80},
				        {field:'doseUnit',title:'单位',width:80},
				        {field:'applyNum',title:'总量',width:80},
				        {field:'minUnit',title:'单位',width:80,
				        	formatter:function(value,row,index){
				        		for ( var i = 0; i < unitMap.length; i++) {
									if (value == unitMap[i].id) {
										return unitMap[i].name;
									}
								}
				        	}
				        },
				        {field:'dfqCexp',title:'频次',width:200},
				        {field:'useName',title:'用法',width:100},
				        {field:'applyDate',title:'申请时间',width:150},
				        {field:'drugedEmpl',title:'发药人',width:100},
				        {field:'drugedDate',title:'发药时间',width:150},
				        {field:'orderType',title:'医嘱类型',width:100,
				        	formatter: function(value, row, index) {
								for ( var i = 0; i < typeCodeList.length; i++) {
									if (value == typeCodeList[i].id) {
										return typeCodeList[i].typeName;
									}
								}
				        	}
				        }
				    ]]
				});
				 $.ajax({
					 	url : '<%=basePath%>outpatient/queryDrugApplyoutmingxibaiyaoriqiList.action?deptCode='+deptCode+'&drugedBill='+drugedBill,  
					    type:'get',
						success: function(date){
							var	Lists = eval("("+date+")");
							var baiyaoriqi = Lists[0].drugedDate;
							$("#baiyaoriqi").datebox('setValue',baiyaoriqi);
						}
					});
				$('#adDgList3').datagrid({
					striped : true,
					checkOnSelect : true,
					selectOnCheck : true,
					singleSelect : true,
					fitColumns : false,
					pagination : true,
					rownumbers : true,
					pageSize : 10,
					pageList : [ 10, 20, 30, 50, 100 ],
					onBeforeLoad:function (param) {
						$.ajax({
							url:"<%=basePath %>likeCodeDrugpackagingunit.action",
							type:'post',
							success: function(unitData) {
								unitMap = eval("("+unitData+")");
							}
						});
				 	},
					url : '<%=basePath%>outpatient/queryDrugApplyoutkeshiList.action?deptCode='+deptCode+'&drugedBill='+drugedBill,  
				    columns:[[    
				        {field:'tradeNameAndspecs',title:'药品名称[规格]',width:300},
				        {field:'retailPrice',title:'零售价',width:300},
				        {field:'applyNum',title:'总量',width:300},
				        {field:'minUnit',title:'单位',width:300,
				        	formatter:function(value,row,index){
				        		for ( var i = 0; i < unitMap.length; i++) {
									if (value == unitMap[i].id) {
										return unitMap[i].name;
									}
								}
				        	}	
				        }    
				    ]]
				});
		};
		controlName = $('#baiyaotai').val();
		//加载摆药单信息已核准
		$('#tDt').tree({
			url:"<%=basePath %>outpatient/TreeBillCalss.action?controlName="+controlName,
		    method:'get',
		    animate:true,
		    lines:true,
		    formatter:function(node){//统计节点总数
		    	if(node.children!=null||node.children!=""){
		    		var s = node.text;
					if (node.children){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
					return s;
		    	}else{
					$.messager.alert('提示信息',"您所在的科室没有设置未核准摆药台，请先设置本科室的摆药台！");
		    		var s = node.text;
		    		return s;
				}
			},
			onClick:function(node){
					var billclassId = node.attributes.pid;
					$.ajax({
						   url: '<%=basePath%>outpatient/querydrugBillClassNameList.action?billclassId='+billclassId,
							type:'post',
							success: function(date){
								var	payList = eval("("+date+")");
								var billclassName=payList[0].billclassName;
								var baiyaodan = node.text;
								drugedBill = node.attributes.drugedBill;
								deptCode = node.attributes.deptCode;
								$('#baiyaodan1').html(baiyaodan+""+billclassName+"   补打(明细)已发");
								$('#baiyaodan3').html("摆药单号："+drugedBill);
								liebiao();
							}
						});
					}
				});
		//加载摆药单信息未核准
		$('#tDt1').tree({
			url:"<%=basePath %>outpatient/TreeBillCalssWei.action?controlName="+controlName,
		    method:'get',
		    animate:true,
		    lines:true,
		    formatter:function(node){//统计节点总数
		    	if(node.children!=null||node.children!=""){
		    		var s = node.text;
					if (node.children){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
					return s;
		    	}else{
					$.messager.alert('提示信息',"您所在的科室没有设置未核准摆药台，请先设置本科室的摆药台！");
		    		var s = node.text;
		    		return s;
				}
			},
			onClick:function(node){
					var billclassId = node.attributes.pid;
					$.ajax({
						   url: '<%=basePath%>outpatient/querydrugBillClassNameList.action?billclassId='+billclassId,
							type:'post',
							success: function(date){
								var	payList = eval("("+date+")");
								var billclassName=payList[0].billclassName;
								var baiyaodan = node.text;
								drugedBill = node.attributes.drugedBill;
								deptCode = node.attributes.deptCode;
								$('#baiyaodan1').html(baiyaodan+""+billclassName+"   补打(明细)已发");
								$('#baiyaodan3').html("摆药单号："+drugedBill);
								liebiao();
							}
						});
					}
				});
				            
				         function liebiao(){
								$('#adDgList1').datagrid({
									striped : true,
									checkOnSelect : true,
									selectOnCheck : true,
									singleSelect : true,
									fitColumns : false,
									pagination : true,
									rownumbers : true,
									pageSize : 10,
									pageList : [ 10, 20, 30, 50, 100 ],
									url : '<%=basePath%>outpatient/queryDrugApplyoutList.action?deptCode='+deptCode+'&drugedBill='+drugedBill,  
								    columns:[[    
								        {field:'patientNameAndbed',title:'[床号]姓名',width:200},    
								        {field:'placeCode',title:'货位号',width:200},    
								        {field:'tradeName',title:'药品名称',width:200},
								        {field:'specs',title:'规格',width:200},
								        {field:'packQty',title:'数量',width:100,align:'center'},
								        {field:'useName',title:'给药途径',width:200},
								        {field:'dfqCexp',title:'频次',width:200},
								        {field:'doseOnce',title:'用量',width:100,align:'right'},
								        {field:'doseUnit',title:'单位',width:100}
								    ]]    
								});
								 $('#adDgList2').datagrid({
									striped : true,
									checkOnSelect : true,
									selectOnCheck : true,
									singleSelect : true,
									fitColumns : false,
									pagination : true,
									rownumbers : true,
									remoteSort :false,
									sortable :true,
									pageSize : 10,
									pageList : [ 10, 20, 30, 50, 100 ],
									onBeforeLoad:function (param) {
										$.ajax({
											url : '<%=basePath%>inpatient/billInpatientKind/likeInpatientKind.action',
											type : 'post',
											success : function(ordercategoryData) {
												typeCodeList = eval("("
														+ ordercategoryData + ")");
											}
										});
										$.ajax({
											url:"<%=basePath %>likeCodeDrugpackagingunit.action",
											type:'post',
											success: function(unitData) {
												unitMap = eval("("+unitData+")");
											}
										});
								 	},
									url : '<%=basePath%>outpatient/queryDrugApplyoutmingxiList.action?deptCode='+deptCode+'&drugedBill='+drugedBill,  
								    columns:[[    
								        {field:'bedName',title:'床号',width:50},
								        {field:'patientName',title:'姓名',width:100},
								        {field:'tradeNameAndspecs',title:'药品名称[规格]',width:200},    
								        {field:'retailPrice',title:'零售价',width:80},
								        {field:'doseOnce',title:'用量',width:80},
								        {field:'doseUnit',title:'单位',width:80},
								        {field:'applyNum',title:'总量',width:80},
								        {field:'minUnit',title:'单位',width:80,
								        	formatter:function(value,row,index){
								        		for ( var i = 0; i < unitMap.length; i++) {
													if (value == unitMap[i].id) {
														return unitMap[i].name;
													}
												}
								        	}
								        },
								        {field:'dfqCexp',title:'频次',width:200},
								        {field:'useName',title:'用法',width:100},
								        {field:'applyDate',title:'申请时间',width:150},
								        {field:'drugedEmpl',title:'发药人',width:100},
								        {field:'drugedDate',title:'发药时间',width:150},
								        {field:'orderType',title:'医嘱类型',width:100,
								        	formatter: function(value, row, index) {
												for ( var i = 0; i < typeCodeList.length; i++) {
													if (value == typeCodeList[i].id) {
														return typeCodeList[i].typeName;
													}
												}
								        	}
								        }
								    ]]
								});
								 $.ajax({
									 	url : '<%=basePath%>outpatient/queryDrugApplyoutmingxibaiyaoriqiList.action?deptCode='+deptCode+'&drugedBill='+drugedBill,  
									    type:'get',
										success: function(date){
											var	Lists = eval("("+date+")");
											var baiyaoriqi = Lists[0].drugedDate;
											$("#baiyaoriqi").datebox('setValue',baiyaoriqi);
										}
									});
								$('#adDgList3').datagrid({
									striped : true,
									checkOnSelect : true,
									selectOnCheck : true,
									singleSelect : true,
									fitColumns : false,
									pagination : true,
									rownumbers : true,
									pageSize : 10,
									pageList : [ 10, 20, 30, 50, 100 ],
									onBeforeLoad:function (param) {
										$.ajax({
											url:"<%=basePath %>likeCodeDrugpackagingunit.action",
											type:'post',
											success: function(unitData) {
												unitMap = eval("("+unitData+")");
											}
										});
								 	},
									url : '<%=basePath%>outpatient/queryDrugApplyoutkeshiList.action?deptCode='+deptCode+'&drugedBill='+drugedBill,  
								    columns:[[    
								        {field:'tradeNameAndspecs',title:'药品名称[规格]',width:300},
								        {field:'retailPrice',title:'零售价',width:300},
								        {field:'applyNum',title:'总量',width:300},
								        {field:'minUnit',title:'单位',width:300,
								        	formatter:function(value,row,index){
								        		for ( var i = 0; i < unitMap.length; i++) {
													if (value == unitMap[i].id) {
														return unitMap[i].name;
													}
												}
								        	}	
								        }    
								    ]]
								});
				         	}  
			       //渲染最小费用
					function minimumunitFamater(value,row,index){
						for(var i=0;i<drugMinimumunitList.length;i++){
							if(value==drugMinimumunitList[i].id){
								return drugMinimumunitList[i].name;
							}
						}	
					}
								         
	</script>
</body>
</html>