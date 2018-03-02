<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<title>退费查询</title>
	<script type="text/javascript">
	var deptMap=new Map();
	var empMap=new Map();
	var packunit= new Map();
	var medicalrecordId="";
	$(function(){
		
		/**
		*回车事件
		*/
		//就诊卡号回车查询
		bindEnterEvent('medicalrecordId',queryList,'easyui');
		var winH=$("body").height();
		//查询科室map
		$.ajax({
			url:'<%=basePath%>baseinfo/department/getDeptMap.action',
			async:false,
			success:function(datavalue){
				deptMap=datavalue;
			}
		});	
		//查询人员map
		$.ajax({
			url:'<%=basePath%>baseinfo/employee/getEmplMap.action',
			async:false,
			success:function(datavalue){
				empMap=datavalue;
			}
		});	
		
		//查询单位map
		$.ajax({
			url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=packunit',
			async:false,
			success:function(datavalue){
				for(var i=0;i<datavalue.length;i++){
					packunit.put(datavalue[i].encode,datavalue[i].name);
				}
			}
		});	
		//查询患者信息
		$.ajax({
			url:'<%=basePath %>/inpatient/cancelitem/queryInpatientByMedicalRecordId.action',
			async:false,
			success:function(datavalue){
				for(var i=0;i<datavalue.length;i++){
					packunit.put(datavalue[i].encode,datavalue[i].name);
				}
			}
		});	
		
		//加载数据表格
		$("#countList").datagrid({
			pagination : true,
			pageSize : 20,
			pageList : [ 20, 30, 50, 80, 100 ],
			rownumbers:true,
// 			idField:'id',
// 			border:true,
// 			checkOnSelect:true,
// 			selectOnCheck:false,
// 			singleSelect:true,
			fitColumns:false,
			pagination:true,
			fit:true,
// 			queryParams:{inpatientNo:null},
			url:'<%=basePath %>/inpatient/cancelitem/queryInpatientReturns.action',
		});

	});
	/*******************************开始读卡***********************************************/
	//定义一个事件（读卡）
	function read_card_ic(){
		var card_value = app.read_ic();
		if(card_value=='0'||card_value==undefined||card_value==''){
			$.messager.alert('提示','此卡号['+card_value+']无效');
			return;
		}
		$.ajax({
			url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
			data:{idcardOrRe:card_value},
			type:'post',
			async:false,
			success: function(data) { 
				if(data==null||data==''){
					$.messager.alert('提示','此卡号无效');
					return;
				}
				$('#medicalrecordId').textbox('setValue',data);
				queryList();
			}
		});	
	};
	/*******************************结束读卡***********************************************/
	/*******************************开始读身份证***********************************************/
		//定义一个事件（读身份证）
		function read_card_sfz(){
			var card_value = app.read_sfz();
			if(card_value=='0'||card_value==undefined||card_value==''){
				$.messager.alert('提示','此卡号['+card_value+']无效');
					return;
				}
				 $.ajax({
						url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
						data:{idcardOrRe : card_value},
						type:'post',
						async:false,
						success: function(data) {
							if(data==null||data==''){
								$.messager.alert('提示','此卡号无效');
								return;
							}
							$('#medicalrecordId').textbox('setValue',data);
							queryList();
						}
				 });
			};
	/*******************************结束读身份证***********************************************/
//      /**
// 		 * 查询
// 		 * @author wujiao
// 		 * @date 2015-08-31
// 		 * @version 1.0
// 		 */
// 		function searchFrom() {
    	 
// 			$('#countList').datagrid('load',{
				
// 			});
// 		}
     
		
		
	function deptFunction(value,row,index){
		if(value!=null&&value!=""){
			return deptMap[value];
		}
	}
	
	
	function empFunction(value,row,index){
		if(value!=null&&value!=""){
			return empMap[value];
		}
	}
	
	//渲染单位以及包装单位
	function unitFunction(value,row,index){
		if(value!=null&&value!=""){
			if(packunit.get(value)!=null&&packunit.get(value)!=""){
				return packunit.get(value);
			}else{
				return value;
			}
		}
	}
	
	
	function clear(){
		$('#STime').val("${Stime}");
		$('#ETime').val("${Etime}");
		$('#dept').combobox('setValue',"${dept}");
		$('#drug').combobox('setValue',"${drug}");
		searchFrom();
	}
	
	function queryList(){
		medicalrecordId=$('#medicalrecordId').textbox("getValue");
		medicalrecordId=medicalrecordId.replace(/(^\s*)|(\s*$)/g, "");
		if(medicalrecordId==null||medicalrecordId==""){
			$.messager.alert('提示','请输入病历号');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			$('#countList').datagrid('loadData',{total:0,rows:[]});
		}else{
			$.ajax({
				  url:'<%=basePath %>/inpatient/cancelitem/queryInpatientByMedicalRecordId.action?medicalrecordId='+medicalrecordId,
				  success:function(data){ 
					  $.messager.progress('close');
					  var data = eval("("+data+")");
				      var total=data.total;
// 				      alert(total);
					 if(total>1){
						$("#diaInpatient").window('open');
						$("#infoDatagrid").datagrid({
							url:'<%=basePath %>/inpatient/cancelitem/queryInpatientByMedicalRecordId.action?menuAlias=${menuAlias}&medicalrecordId='+medicalrecordId,
							    columns:[[    
							        {field:'patientName',title:'姓名',width:'24%',align:'center'} ,  
							        {field:'reportSexName',title:'性别',width:'10%',align:'center'} ,
							        {field:'inDate',title:'入院日期',width:'20%',align:'center'} ,   
							        {field:'outDate',title:'出院日期',width:'20%',align:'center'},
							        {field:'inpatientNo',title:'流水号',width:'25%',align:'center'} 
							    ]],
							    onDblClickRow:function(index,row){
							    	$("#diaInpatient").window('close');
							    	var no=row.inpatientNo;
							    	$("#countList").datagrid('load',{
										inpatientNo:no
									});
							    }
						});
						}else if(total==1){
							var no=data.rows[0].inpatientNo;
							$("#countList").datagrid('load',{
								inpatientNo:no
							});
						}else{
							$.messager.alert('提示','请核对病历号！');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							$('#countList').datagrid('loadData',{total:0,rows:[]});
						}
							}
						});
					 
					 
		}
	}
	</script>
</head>
<body>
		<div id="divLayout"  class="easyui-layout" data-options="fit:true" >
		 <div data-options="region:'north',border:false" style="height:50px;padding: 13px 5px 1px 5px">
	       	病历号：
			<input id='medicalrecordId' class="easyui-textbox"  prompt="输入后回车查询" style="width:150px"> 
			<a  href="javascript:void(0)" onclick="queryList()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
			<shiro:hasPermission name="${menuAlias}:function:readCard">
				<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
			</shiro:hasPermission>
        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
			</shiro:hasPermission>	
		</div>
		 <div data-options="region:'center',border:false" style="height:95%;width:100%;padding: 5px 0px 0px 0px;">
				<table id="countList"  data-options="fit:true,method:'post'">
					<thead>
						<tr>
<!-- 							<th data-options="field:'checkCode'" width="9%"> 盘点单号</th> -->
							<th data-options="field:'name'" width="7%">患者姓名</th>
							<th data-options="field:'deptName'" width="8%">科室名称</th>
							<th data-options="field:'itemName'" width="18%">项目名称</th>
<!-- 							<th data-options="field:'specs'" width="7%">规格</th> -->
							<th data-options="field:'salePrice'" width="5%"align="right" halign="left">零售价/元</th>
							<th data-options="field:'priceUnit',formatter:unitFunction" width="5%">计价单位</th>
							<th data-options="field:'quantity'" width="5%">数量</th>
<!-- 							<th data-options="field:''" width="5%">总额</th>  -->
							<th data-options="field:'operDate'" width="15%">操作时间</th>
							<th data-options="field:'operName'" width="10%">操作人</th>
							<th data-options="field:'recipeNo'" width="10%">处方号</th>
							<th data-options="field:'sequenceNo'" width="10%">处方内流水号</th>
						</tr>
					</thead>
				</table>
				<form id="saveForm" method="post"/></form>
		</div>
	</div>
	<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:650;height:600;padding:10" data-options="modal:true, closed:true">   
   		<table id="infoDatagrid"  data-options="fitColumns:true,singleSelect:true,fit:true,rownumbers:true">   
		</table>  
	</div>
</body>
</html>