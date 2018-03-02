<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>医技预约</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css"/>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
#endTime{
	width:200px !important;
}
.panel-header,.panel-body{
	border-left-width:0;
}
</style>
</head>
	<body style="margin: 0px;padding: 0px;">
		<div id="l" class="easyui-layout" data-options="fit:true"> 
			<div data-options="region:'north',border:false" style="width:100%;height:40px;" id="n1">
				<div id="toolbarId" style="padding:7px 5px 5px 5px;">
					<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
					<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>&nbsp;
					<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>&nbsp;
					<a href="javascript:void(0)" onclick="apply()" class="easyui-linkbutton" data-options="iconCls:'icon-yuyue2'">预约</a>&nbsp;
					<a href="javascript:void(0)" onclick="cancel()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">取消</a>&nbsp;
				</div>
			</div>
			<div data-options="region:'center',border:true" style="width:100%;height:95%;" id="c1">
				<div id="l2" class="easyui-layout" data-options="fit:true"> 
					<div data-options="region:'west',split:false,border:false" style="width:280px;;height:100%;" id="w1">
						<div id="l2" class="easyui-layout" data-options="fit:true">
							<div data-options="region:'north',split:false,border:false" style="height:50px;">
								<div style="padding: 5px 5px 5px 5px;">
						    		<table>
						    			<tr>
<!-- 						    				<td>门诊号：</td> -->
						    				<td colspan="3"><input class="easyui-textbox" id="clinicCode" data-options="prompt:'请输入门诊号/住院号回车键查询'" style="width:220px" /></td>
						    			</tr>
<!-- 						    			<tr> -->
<!-- 						    				<td>住院号：</td> -->
<!-- 						    				<td colspan="3"><input class="easyui-textbox" id="inpatientNo"  style="width:200px" /></td> -->
<!-- 						    			</tr> -->
						    		</table>
						    	</div>
							</div>
							<div data-options="region:'center',border:false" style="height:100%;">
								<div style="padding: 5px">
					    			<b>预约选择项目</b>
					    		</div>
					    		<div style="padding: 5px 5px 5px 5px;">
									<ul id="tDt">加载中，请稍等.....</ul>
								</div>
							</div>
						</div>
					</div>
					<div data-options="region:'center',border:false" style="width:82%;height:100%;" id="c2">
						<div id="l3" class="easyui-layout" data-options="fit:true" > 
							<div data-options="region:'north'" style="padding-left:5px;padding-top:5px;padding-bottom:5px;border-left-width:1px;overflow: hidden;" id="n2">
								<table>
									<tr>
										<td align="right">
											姓名：
										</td>
										<td style="width:210px;">
											<input class="easyui-textbox" id="pname" style="width:200px"/>
										</td>
										<td>
											性别：
										</td>
										<td style="width:210px;">
											<input class="easyui-textbox" id="psex" style="width:200px"/>
										</td>
										<td>
											年龄：
										</td>
										<td style="width:210px;">
											<input class="easyui-textbox" id="page" style="width:200px"/>
										</td>
									</tr>
									<tr>
										<td align="right">
											预约时间区间：
										</td>
										<td>
											<input id="startTime" class="Wdate" type="text" value="${startTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}'})" style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
										</td>
										<td align="center">
											- 
										</td>
										<td colspan="3">
											<input id="endTime" class="Wdate" type="text" value="${endTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}',maxDate:'(%y+1)-%M-%d'})" style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
											<input type="hidden" id="sche">
											<input type="hidden" id="finapp">
										</td>
									</tr>
								</table>
							</div>
							<div data-options="region:'center',border:false" style="width:100%;height:90%;" id="c3">
								<div id="tt" class="easyui-tabs" data-options="fit:true">
			    					<div title="排班信息" style="width:100%;border: 0px;">
			    						 <div id="l4" class="easyui-layout" data-options="fit:true"> 
			    							<div data-options="region:'north',border:false" style="width:100%;height:43%;" id="n3">
			    								<table id="scheduleInfo">
													<thead>
														<tr>
															<th data-options="field:'ck',checkbox:true"></th>
															<th style="width: 10%;" data-options="field:'itemCode'" align="center">项目代码</th>
															<th style="width: 10%;" data-options="field:'itemName'" align="center">项目名称</th>
															<th style="width: 10%;" data-options="field:'unitFlag',formatter:functionUnit" align="center">单位标识</th>
															<th style="width: 10%;" data-options="field:'deptCode',formatter:functionDept" align="center">执行科室</th>
															<th style="width: 10%;" data-options="field:'bookDate'" align="center">预约时间</th>
															<th style="width: 10%;" data-options="field:'midDay',formatter:functionNoon" align="center">午别</th>
															<th style="width: 9%;" data-options="field:'preLimit'" align="center">预约限额</th>
															<th style="width: 9%;" data-options="field:'speciallimit'" align="center">特诊限额</th> 
															<th style="width: 9%;" data-options="field:'bookNum',formatter:changeColorHaveApply" align="center">已预约数</th>
															<th style="width: 9%;" data-options="field:'feeOperCode'" align="center">特诊预约数</th>
														</tr>
													</thead>
												</table> 
			    							</div>
			    							<div data-options="region:'center',border:false" style="width:100%;height:42%;" id="c4">
			    								<table id="finApplyItem">
													<thead>
														<tr>
															<th data-options="field:'ck',checkbox:true"></th>
															<th style="width: 16%;" data-options="field:'itemName'" align="center">项目名称</th>
															<th style="width: 16%;" data-options="field:'bookDate'" align="center">预约时间</th>
															<th style="width: 16%;" data-options="field:'noonCode',formatter:functionNoon" align="center">午别</th>
															<th style="width: 16%;" data-options="field:'executeLocate',formatter:functionDept " align="center">执行地点</th>
															<th style="width: 16%;" data-options="field:'remark'" align="center">注意事项</th>
															<th style="width: 16%;" data-options="field:'bookId'" align="center">预约单号</th>
														</tr>
													</thead>
												</table> 
			    							</div>
			    							<div title="患者注意事项" data-options="region:'south',border:false,collapsible:false" style="width:100%;height:100px;" id="s1">
			    								<div style="padding-top:5px;padding-left:5px;padding-right:5px;padding-bottom:5px;">
			    									<textarea id="remark" style="width:100%;height:62px;"></textarea>
			    								</div>
			    							</div>
			    						</div> 
			   						</div>   
			    					<div title="已排班病人"  style="width:100%;"> 
			    						 <table id="finSchedulePatient" class="easyui-datagrid" data-options="rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:true,fixed:true,fit:true">
											<thead>
												<tr>
													<th style="width: 14%;" data-options="field:'clinicCode'" align="center">卡号</th>
													<th style="width: 14%;" data-options="field:'name'" align="center">姓名</th>
													<th style="width: 14%;" data-options="field:'itemName'" align="center">项目名称</th>
													<th style="width: 14%;" data-options="field:'deptName'" align="center">执行科室</th>
													<th style="width: 14%;" data-options="field:'noonCode',formatter: functionNoon" align="center">午别</th>
													<th style="width: 14%;" data-options="field:'operCode',formatter:functionEmp" align="center">操作人</th>
													<th style="width: 13%;" data-options="field:'feeDate'" align="center">收费时间</th>
												</tr>
											</thead>
										</table>    
			   						</div>   
			    					<div title="病人收费信息"  style="width:100%;">   
			    						 <table id="PatientCostInfo" class="easyui-datagrid"  data-options="idField:'id',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:true,fixed:true,fit:true">
											<thead>
												<tr>
													<th style="width: 16%;" data-options="field:'clinicCode'" align="center">卡号</th>
													<th style="width: 16%;" data-options="field:'name'" align="center">姓名</th>
													<th style="width: 16%;" data-options="field:'itemName'" align="center">项目名称</th>
													<th style="width: 16%;" data-options="field:'deptName'" align="center">执行科室</th>
													<th style="width: 16%;" data-options="field:'itemQty'" align="center">数量</th>
													<th style="width: 16%;" data-options="field:'feeDate'" align="center">收费时间</th>
												</tr>
											</thead>
										</table>  
			    					</div> 
								</div> 
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		 <div id="patientWin" title='患者信息' class="easyui-window" align="center" data-options="modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false" style="width:700px;height:500px">
			<table id="dg" class="easyui-datagrid">
			</table>  
		</div>
		<script type="text/javascript">
var itemCode="";
var deptMap=new Map();
var sexMap=new Map();
var qty = new Map();//项目预约数量，以项目的code为key，数量为value
var empMap = null;
//加载页面
$(function(){
	//获取性别
	$.ajax({
			url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
			data:{"type":"sex"},
			type:'post',
			success: function(data) {
				var v = data;
				for(var i=0;i<v.length;i++){
					sexMap.put(v[i].encode,v[i].name);
				}
			}
		});
	$.ajax({//渲染科室
		url: '<%=basePath%>baseinfo/department/getDeptMap.action' ,
		type:'post',
		success: function(deptData) {
			deptMap = deptData;
		}
	});
	$.ajax({//渲染人员
		url: '<%=basePath%>baseinfo/employee/getEmplMap.action',
 		type:'post',
 		async:true,
 		success: function(empData) {	
 			empMap = empData;
 		}
 	});
	// 已预约项目
		$("#finApplyItem").datagrid({
			idField:'id',
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			fit:true,
			title:"已预约项目",
			url:'<%=basePath %>technical/tecapply/getFinApplyItem.action',
			queryParams:{clinicCode:null},
			onLoadSuccess: function(data){
				//分页工具栏作用提示
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
			onClickRow:function(rowIndex, rowData){
				$("#remark").val(rowData.remark);
			}
			
		});
		//排版信息
		var startTime=$('#startTime').val();
		var endTime=$('#endTime').val();
		setTimeout(function(){
			 $("#scheduleInfo").datagrid({
					border:false,
					checkOnSelect:true,
					selectOnCheck:false,
					singleSelect:true,
					fitColumns:false,
					pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],
					fit:true,
					url:'<%=basePath %>technical/tecapply/findScheByItem.action',
					queryParams:{clinicCode:null,startTime:startTime,endTime:endTime},
					onLoadSuccess: function(data){
						//分页工具栏作用提示
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
					onClickRow:function(rowIndex, rowData){
						$("#remark").val("");
					}
					
				});
		},200);
	
	tree();
	//给树绑定回车键查询事件
	bindEnterEvent('clinicCode',query,'easyui');
	$('#tt').tabs({
		onSelect: function(title,index){
			var startTime=$('#startTime').val();
			var endTime=$('#endTime').val();
			if(title=="排班信息"){
				
			}else if(title=="已排班病人"){
			  $("#finSchedulePatient").datagrid({
				  	url: '<%=basePath %>technical/tecapply/findFinScheInp.action',
					pageSize:20,
					pageList:[20,30,50,80,100],
					pagination:true,
					method:"post",
					onLoadSuccess: function(data){
						//分页工具栏作用提示
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
						$("#finSchedulePatient").datagrid('uncheckAll');
					}
			
				});
			}else{
// 				var inpatientNo = $('#inpatientNo').textbox('getValue');
				var clinicCode = $('#clinicCode').textbox('getValue');
				 $("#PatientCostInfo").datagrid({
					 	url: '<%=basePath %>technical/tecapply/findInpFeeInfo.action',
					 	queryParams:{"clinicCode":clinicCode},
						pageSize:20,
						pageList:[20,30,50,80,100],
						pagination:true,
						method:"post",
						onLoadSuccess: function(data){
							//分页工具栏作用提示
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
							$("#PatientCostInfo").datagrid('uncheckAll');
						}
				});
			}
		}
	});
})	
	
//预约
function apply(){
	var startTime=$("#startTime").val();
	var endTime=$("#endTime").val();
	var clinicCode = $("#clinicCode").textbox("getValue");
	var rows = $('#scheduleInfo').datagrid('getChecked');
// 	var rows2 = $('#finApplyItem').datagrid('getChecked');
// 	var rows3 = $('#finSchedulePatient').datagrid('getChecked');
// 	var rows4 = $('#PatientCostInfo').datagrid('getChecked');
// 	if(rows3.length>0||rows4.length>0){
// 		$.messager.alert('提示','该信息无法预约！');
// 		return ;
// 	}
// 	if(rows.length>0&&rows2.length>0){
// 		$.messager.alert('提示','不能同时进行排班信息预约和对已预约信息进行修改！');
// 	}else if(rows.length>0){
		if(rows.length<1){
			$.messager.alert('提示','请选择一条排班信息进行预约！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}else{
			for(var i=0;i<rows.length;i++){
				if(qty.get(rows[i].itemCode)>(rows[i].preLimit-rows[i].specialbookNum)){//当达到预约限额的时候判断是否继续
					$.messager.confirm('确认',rows[i].itemName+'剩余预约额不足，是否继续本次操作？',function(r){    
					    if (r){    
					    	executeApply();
					    }else{
					    	return false;
					    }   
					});  
				}else{
					executeApply();
					
				}
			}
		}
// 	}else if(rows2.length>0){
// 		if(rows2.length!=1){
// 			$.messager.alert('提示','请选择一条已预约信息进行修改！');
// 		}else{
// 			var id=rows2[0].id;
// 			var rem=$("#remark").val();
// 			$.ajax({
<%-- 				url: '<%=basePath %>technical/tecapply/updTecApply.action', --%>
// 				data:{tid:id,rem:rem},
// 				type:'post',
// 				success: function() {
// 					$("#remark").val("");
// 					//重新加载已预约项目列表
// 					 $("#finApplyItem").datagrid('load',{clinicCode:clinicCode});
// 				}
// 			});
// 		}
// 	}
}

function executeApply(){
	var rows = $('#scheduleInfo').datagrid('getChecked');
	var clinicCode =$('#clinicCode').textbox("getValue");
	var startTime=$("#startTime").val();
	var endTime=$("#endTime").val();
	var rem=$("#remark").val();
	/* var aid=rows[0].pid; */
	var itemCodes="";//预约项目编码组
	for(var i=0;i<rows.length;i++){
		if(itemCodes!=""){
			itemCodes = itemCodes + "','"
		}
		itemCodes = itemCodes + rows[i].itemCode
	}
	$.messager.progress({text:'预约中，请稍后...',modal:true});
	$.ajax({
		url: '<%=basePath %>technical/tecapply/tecApply.action',
		data:{clinicCode:clinicCode,startTime:startTime,endTime:endTime,rem:rem,aid:null,itemCodes:itemCodes},
		type:'post',
		success: function(data) {
			$.messager.progress('close');
			if(data.resMsg=="OK"){
				$.messager.alert('提示',data.resCode);
				//重新加载排班信息  修改预约限额，已预约数
				 $("#scheduleInfo").datagrid('reload');
				//重新加载已预约项目列表
				 $("#finApplyItem").datagrid('reload');
				//刷新树
				 $('#tDt').tree('reload');
			}else if(data.resMsg=="error"){
				$.messager.alert('提示',data.resCode);
			}else{
				$.messager.alert('提示','预约失败！');
			}
		}
	});
}
//渲染 单位标识
function functionUnit(value){
	if(value==1){
		return "明细";
	}else{
		return "组套";
	}
}
//渲染 午别
function functionNoon(value){
	if(value==1){
		return "上午";
	}else if(value==2){
		return "下午";
	}else{
		return "晚上";
	}
}
function functionDept(value){
	if(value!=""&&deptMap!=null){
		return deptMap[value];
	}
}

function changeColorHaveApply(value,row){
	if(value>row.preLimit){
		return '<span style="color:red">'+value+'</span>';
	}else{
		return value;
	}
}

	function query(){
		$.messager.progress({text:'查询中，请稍后...',modal:true});
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		var clinicCode = $("#clinicCode").textbox("getValue");
		 tree();
		 getPatient()
		 $("#scheduleInfo").datagrid('reload',{clinicCode:clinicCode,startTime:startTime,endTime:endTime});
		 $("#finSchedulePatient").datagrid('reload',{clinicCode:clinicCode});
		 $("#PatientCostInfo").datagrid('reload',{clinicCode:clinicCode});
		 $("#finApplyItem").datagrid('reload',{clinicCode:clinicCode});
		 $.messager.progress('close');
	}

	/**
	 * 重置
	 * @author huzhenguo
	 * @date 2017-03-21
	 * @version 1.0
	 */
	function clears(){
		$('#startTime').val('${startTime}');
		$('#endTime').val('${endTime}');
		$("#clinicCode").textbox("setValue","");
		$("#pname").textbox("setValue","");
		$("#psex").textbox("setValue","");
		$("#page").textbox("setValue","");
		query();
	}
	function tree(){
		var clinicCode = $("#clinicCode").textbox("getValue");
		$('#tDt').tree({ 
			url:"<%=basePath%>technical/tecapply/getTree.action?clinicCode="+clinicCode,
		    method:'post',
		    animate:true,  //点在展开或折叠的时候是否显示动画效果
		    lines:true,  //是否显示树控件上的虚线
		    onLoadSuccess:function(node, data){
		    	for(var i = 0;i<data[0].children.length;i++){
		    		qty.put(data[0].children[i].id,data[0].children[i].attributes.qty);
		    	}
		    }
		});
	}
	function getPatient(){
		var clinicCode = $("#clinicCode").textbox("getValue");
		$.ajax({
			url: '<%=basePath%>technical/tecapply/getPatientByClinicCode.action?clinicCode='+clinicCode,
			type:'post',
			success: function(data) {
				if(clinicCode!=""&&(data[0].name==null||data[0].name=="")){
					$("#pname").textbox("setValue","");
					$("#psex").textbox("setValue","");
					$("#page").textbox("setValue","");
					$.messager.alert('提示','未查询到患者，请检查门诊号是否有误！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else if(data.length==1){
					$("#pname").textbox("setValue",data[0].name);
						if(sexMap.get(data[0].sex)){
							$("#psex").textbox("setValue",sexMap.get(data[0].sex));
						}
					$("#page").textbox("setValue",data[0].age);
				}else if(data.length>1){
					$('#patientWin').window('open');
					 $('#dg').datagrid({    
			    		    url:'<%=basePath%>technical/tecapply/getPatientByClinicCode.action?clinicCode='+clinicCode,
			    		    columns:[[    
			    				        {field:'name',title:'姓名',width:100,align:'center',hidden:true},    
			    				        {field:'sex',title:'性别',width:100,align:'center',resizable:true,
			    				        	formatter: function(value,row,index){//状态显示值
			    				        		if(sexMap.get(value)){
			    									$("#psex").textbox("setValue",sexMap.get(value));
			    								}
		    		    					}
			    				        },
			    				        {field:'certificatesno',title:'证件号',width:100,align:'center',resizable:true},
			    				        {field:'birthday',title:'出生日期',width:100,align:'center',resizable:true,sortable:true},    
			    				        {field:'age',title:'年龄',width:100,align:'center',resizable:true},    
			    				    ]],
			    		    onDblClickRow:function(index,row){
			    		    	$("#pname").textbox("setValue",row.name);
								if(sexMap.get(row.sex)){
									$("#psex").textbox("setValue",sexMap.get(row.sex));
								}
								$("#page").textbox("setValue",row.age);
								$('#patientWin').window('close');
			    		    }
			    		});
				}
			}
		});
	}
	
	//取消预约
	function cancel(){
		var clinicCode = $("#clinicCode").textbox("getValue");
	 	//选中要删除的行
		var rows = $('#finApplyItem').datagrid('getChecked');
		if (rows.length > 0) {//选中几行的话触发事件	                        
			$.messager.confirm('确认', '确定要取消选中信息吗?', function(res){//提示是否删除
				if (res){
					var ids = '';
					for(var i=0; i<rows.length; i++){
						if(ids!=''){
							ids += ',';
						}
						ids += rows[i].id;
					};
					$.messager.progress({text:'取消中，请稍后...',modal:true});
					$.ajax({
						url: '<%=basePath %>technical/tecapply/delFinApplyItem.action?ids='+ids,
						type:'post',
						data:{},
						success: function() {
							$.messager.progress('close');
							$.messager.alert('提示','取消成功');
							//重新加载已预约项目列表
							 $("#finApplyItem").datagrid('load',{clinicCode:clinicCode});
						}
					});
				}
			});
		}else{
			$.messager.alert('提示','请选择要取消的项目！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	function functionEmp(value,row,index){
		if(value!=null&&value!=''){
			return empMap[value];
		}
	}
</script>
</body>
</html>