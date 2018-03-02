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
<title>药房药库综合查询</title>
<script type="text/javascript">
var startTime;//重置开始时间
var endTime;//重置结束时间
var m=0;//记录汇总类型(0-按药品;1-按单据;2-按部门)
var n=0;//记录查询类别(0-入库;1-出库;2-盘点;3-调价)
var columns=[];
var formatFie=['inState','lpx','payState','companyCode','drugStorageCode','drugDeptCode','profitFlag','checkState','currentState',
               'outState','num','packUnit1','retailCost','purchaseCost','sum',"storeNum"];
var company="";
var dept="";
var packUnit="";
$(function(){
	var myDate = new Date();
	var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
	var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
	month=month<10?"0"+month:month;
	var day=year+"-"+month+"-"+"01";
	$('#beginTime').val(day);
	startTime=day;
	myDate = new Date();
	 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
	 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
	 date=myDate.getDate();
	 month=month<10?"0"+month:month;
	 date=date<10?"0"+date:date;
	 day=year+"-"+month+"-"+date;
    $('#endTime').val(day);
    endTime=day;
    var v = $('#beginTime').val();
	$('#date1').html(v);
	var v1 = $('#endTime').val();
	$('#date2').html(v1);
	if(typeof(listParam)=='undefined'){
		listParam="";
	}
	$.ajax({
		url:'<%=basePath%>statistics/InteQuery/getColumns.action?kind='+m+'&type='+n,
		type:'post',
		dataType:'json',
		success:function(data){
			col='[';			
			var j=data.length;
			for (var i = 0; i < j; i++) {
				var fieldName=data[i].fieldName;
				if(formatFie.indexOf(fieldName)!=-1){
					var field='{field:"'+data[i].fieldName+'",title:"'+data[i].titleName+'",width:"8%",formatter:'+fieldName+'Formatter}';
				}else{
					var field='{field:"'+data[i].fieldName+'",title:"'+data[i].titleName+'",width:"8%"}';
				}
				if(i<data.length-1){
					col+=field+',';
				}else{
					col+=field;
				}
			}
			col+=']';
			col = eval("(" + col + ")");	
			columns.push(col);
 			gridData();
		}
	});
	
	$('#beginTime').blur(function(){//当下拉面板隐藏的时候触发
			var v = $('#beginTime').val();
			$('#date1').html(v);
	});
	$('#endTime').blur(function(){//当下拉面板隐藏的时候触发
			var v = $('#endTime').val();
			$('#date2').html(v);
	});
	
	$('#drugName').combobox({
		url : "<c:url value='/statistics/InOutStore/getDrugInfo.action'/>",
		valueField:'drugCode',
		textField:'tradeName',
		mode:'remote'
	});
	
	$('#tree1').tree({
		data: [{
			id:0,
			text: '入库'
		},{
			id:1,
			text: '出库'
		},{
			id:2,
			text: '盘点'
		},{
			id:3,
			text: '调价'
		}],
		lines:true,
		onClick: function(node){
			n=node.id;
			columns=[];
			getCol();
		}
	});
	
	$('#tree2').tree({
		url:'<%=basePath%>statistics/InteQuery/getDrugTree.action',
		lines:true,
		onClick:function(node){
			var deptId=node.id;
			$('#drugDeptCode').val(deptId);
			var formData=getFormData('searchForm');
			$('#grid').datagrid('load',formData);
		}
	});
});

function gridData(){
	$('#grid').datagrid({
		url:'<%=basePath%>statistics/InteQuery/getInteData.action?kind='+m+'&type='+n+'&'+listParam,
		columns:columns,
		singleSelect:true,
		pagination:true,
		rownumbers:true,
		pageSize:20,
		pageList:[20,40,80],
		onBeforeLoad:function(){
			if(company==""){
				$.ajax({
					url:'<%=basePath%>statistics/InteQuery/getCompanyName.action',
					type:'post',
					success:function(data){
						company= data;
					}
				});
			}
			if(dept==""){
				$.ajax({
					url:'<%=basePath%>statistics/InteQuery/getDeptIdName.action',
					type:'post',
					success:function(data){
						dept= data;
					}
				});
			}
			if(packUnit==""){
				$.ajax({
					url:'<%=basePath%>/statistics/InteQuery/getPackUnit.action',
					type:'post',
					success:function(data){
						packUnit= data;
					}
				});
			}
		},onLoadSuccess : function(data){
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
		}
	});
	
}
function queryKind(i){
	if(i==0){
		$('#kind').html("按药品 ");
		m=0;
	}
	if(i==1){
		$('#kind').html("按单据 ");
		m=1;
	}
	if(i==2){
		$('#kind').html("按部门 ");
		m=2;
	}
	columns=[];
	getCol();
}

function inStateFormatter(value){//入库状态
	if(value==2){
		return '核准';
	}
}

function outStateFormatter(value){//入库状态
	if(value==2){
		return '核准';
	}
}

function checkStateFormatter(value){//盘点状态
	if(value==2){
		return '解封';
	}
}
function currentStateFormatter(value){//盘点状态
	if(value==1){
		return '已调价';
	}
}

function profitFlagFormatter(value){//盘点盈亏标记
	if(value==0){
		return '盘亏';
	}
	if(value==1){
		return '盘盈';
	}
}

function payStateFormatter(value){//付款状态
	//0 未付 1 未全付 2 付清
	if(value==0){
		return '未付';
	}
	if(value==1){
		return '未全付';
	}
	if(value==2){
		return '付清';
	}
}
function packUnitFormatter(value,row,index){//单位
	for (var i = 0; i < packUnit.length; i++) {
		if(packUnit[i].encode==value){
			return packUnit[i].name;
		}
	}
}
function packUnit1Formatter(value,row,index){//包装单位
	for (var i = 0; i < packUnit.length; i++) {
		if(packUnit[i].id==row.packUnit){
			return packUnit[i].name;
		}
	}
}
function lpxFormatter(value,row,index){//零批差
	return (row.retailCost-row.purchaseCost).toFixed(2);
}
function companyCodeFormatter(value,row,index){//供货公司
	for (var i = 0; i < company.length; i++) {
		if(company[i].companyId==value){
			return company[i].companyName;
		}
	}
}

function drugStorageCodeFormatter(value,row,index){//出库科室
	for (var i = 0; i < dept.length; i++) {
		if(dept[i].deptCode==value){
			return dept[i].deptName;
		}
	}
}

function drugDeptCodeFormatter(value,row,index){//领药科室
	for (var i = 0; i < dept.length; i++) {
		if(dept[i].id==value){
			return dept[i].deptName;
		}
	}
}
function numFormatter(value,row,index){//出库数量
	var num=(row.outNum/row.packQty);
	return num.toFixed(2);
}
function retailCostFormatter(value,row,index){//零售总金额
	var num=value;
	if(typeof(num)=='undefined'){
		num=0;
	}
	return num.toFixed(2);
}
function purchaseCostFormatter(value,row,index){//入库总金额
	var num=value;
	if(typeof(num)=='undefined'){
		num=0;
	}
	return num.toFixed(2);
}
function sumFormatter(value,row,index){//入库总金额
	var sum=row.applyNum;
	if(typeof(sum)=='undefined'){
		sum=0;
	}
	return sum.toFixed(2);
}
function storeNumFormatter(value,row,index){//库存数量
	var sum=row.storeNum;
	if(typeof(sum)=='undefined'){
		sum=0;
	}
	return sum.toFixed(2);
}

function getCol(){
	$.ajax({
		url:'<%=basePath%>statistics/InteQuery/getColumns.action?kind='+m+'&type='+n,
		type:'post',
		dataType:'json',
		success:function(data){
			col='[';			
			var j=data.length;
			var w=100/j;
			for (var i = 0; i < j; i++) {
				var fieldName=data[i].fieldName;
				if(formatFie.indexOf(fieldName)!=-1){
					var field='{field:"'+data[i].fieldName+'",title:"'+data[i].titleName+'",width:"'+w+'%",formatter:'+fieldName+'Formatter}';
				}else{
					var field='{field:"'+data[i].fieldName+'",title:"'+data[i].titleName+'",width:"'+w+'%"}';
				}
				if(i<data.length-1){
					col+=field+',';
				}else{
					col+=field;
				}
			}
			col+=']';
			col = eval("(" + col + ")");	
			columns.push(col);
 			gridData();
		}
	});
}

function queryData(){
		var startTime=$('#beginTime').val();
		var endTime=$('#endTime').val();
		if(startTime&&endTime){
		    if(startTime>endTime){
		      $.messager.alert("提示","开始时间不能大于结束时间！");
		      close_alert();
		      return ;
		    }
		  }
	var formData=getFormData('searchForm');
	$('#grid').datagrid('load',formData);
}

//按时间段查询
function queryMidday(val){
	if(val==1){
		var myDate = new Date();
		var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		var date=myDate.getDate();
		month=month<10?"0"+month:month;
		date=date<10?"0"+date:date;
		var day=year+"-"+month+"-"+date;
		$('#beginTime').val(day);
	    $('#endTime').val(day);
	    var v = $('#beginTime').val();
		$('#date1').html(v);
		var v1 = $('#endTime').val();
		$('#date2').html(v1);
	    queryData();
	}else if(val==2){
		var nowd = new Date();
		var myDate=new Date(nowd.getTime() - 3 * 24 * 3600 * 1000);
		var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		var date=myDate.getDate();
		month=month<10?"0"+month:month;
		date=date<10?"0"+date:date;
		var day2=year+"-"+month+"-"+date;
		$('#beginTime').val(day2);
		 nowd = new Date();
		 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
		 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		 date=myDate.getDate();
	  	month=month<10?"0"+month:month;
		 date=date<10?"0"+date:date;
		 day2=year+"-"+month+"-"+date;
	    $('#endTime').val(day2);
	    var v = $('#beginTime').val();
		$('#date1').html(v);
		var v1 = $('#endTime').val();
		$('#date2').html(v1);
	    queryData();
	}else if(val==3){
		var nowd = new Date();
		var myDate=new Date(nowd.getTime() - 7 * 24 * 3600 * 1000);
		var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		var date=myDate.getDate();
		month=month<10?"0"+month:month;
		date=date<10?"0"+date:date;
		var day3=year+"-"+month+"-"+date;
		$('#beginTime').val(day3);
		 nowd = new Date();
		 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
		 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		 date=myDate.getDate();
	  	month=month<10?"0"+month:month;
		 date=date<10?"0"+date:date;
		 day2=year+"-"+month+"-"+date;
	    $('#endTime').val(day2);
	    var v = $('#beginTime').val();
		$('#date1').html(v);
		var v1 = $('#endTime').val();
		$('#date2').html(v1);
	    queryData();
	}else if(val==4){
		var myDate = new Date();
		var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		month=month<10?"0"+month:month;
		var day=year+"-"+month+"-"+"01";
		$('#beginTime').val(day);
		myDate = new Date();
		 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		 date=myDate.getDate();
		 month=month<10?"0"+month:month;
		 date=date<10?"0"+date:date;
		 day=year+"-"+month+"-"+date;
	    $('#endTime').val(day);
	    var v = $('#beginTime').val();
		$('#date1').html(v);
		var v1 = $('#endTime').val();
		$('#date2').html(v1);
	    queryData();
	}else if(val==5){
		var myDate = new Date();
		var year=myDate.getFullYear();
		var day=year+"-"+"01"+"-"+"01";
		$('#beginTime').val(day);
		myDate = new Date();
		 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		 date=myDate.getDate();
		 month=month<10?"0"+month:month;
		 date=date<10?"0"+date:date;
		 day=year+"-"+month+"-"+date;
	     $('#endTime').val(day);
	    var v = $('#beginTime').val();
		$('#date1').html(v);
		var v1 = $('#endTime').val();
		$('#date2').html(v1);
	    queryData();
	}else if(val==6){
		var nowd = new Date();
		var myDate=new Date(nowd.getTime() - 15 * 24 * 3600 * 1000);
		var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		var date=myDate.getDate();
		month=month<10?"0"+month:month;
		date=date<10?"0"+date:date;
		var day3=year+"-"+month+"-"+date;
		$('#beginTime').val(day3);
		 nowd = new Date();
		 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
		 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
		 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
		 date=myDate.getDate();
	  	month=month<10?"0"+month:month;
		 date=date<10?"0"+date:date;
		 day2=year+"-"+month+"-"+date;
	    $('#endTime').val(day2);
	    var v = $('#beginTime').val();
		$('#date1').html(v);
		var v1 = $('#endTime').val();
		$('#date2').html(v1);
	    queryData();
	}else if(val==7){
		var date=new Date();
		var year=date.getFullYear();
		var month=date.getMonth();
		if(month==0){
			year=year-1;
			month=12;
		}
		var startTime=year+'-'+month+'-01';
		 $('#beginTime').val(startTime);
		var date=new Date(year,month,0);
		var endTime=year+'-'+month+'-'+date.getDate();
		$('#endTime').val(endTime);
		 queryData();
	}
	
}

function conveterParamsToJson(paramsAndValues) {
	var jsonObj = {};

	var param = paramsAndValues.split("&");
	for (var i = 0; param != null && i < param.length; i++) {
		var para = param[i].split("=");
		jsonObj[para[0]] = para[1];
	}

	return jsonObj;
}

/**
 * 将表单数据封装为json
 * @param form
 * @returns
 */
function getFormData(form) {
	var formValues = $("#" + form).serialize();

	//关于jquery的serialize方法转换空格为+号的解决方法  
	formValues = formValues.replace(/\+/g, " "); // g表示对整个字符串中符合条件的都进行替换  
	var temp = decodeURIComponent(JSON
			.stringify(conveterParamsToJson(formValues)));
	var queryParam = JSON.parse(temp);
	return queryParam;
}
function clear(){
	$('#beginTime').val(startTime);
	$('#endTime').val(endTime);
	$('#drugName').combobox('setValue','');
	$('#drugName').combobox({
		url : "<c:url value='/statistics/InOutStore/getDrugInfo.action'/>",
		valueField:'drugCode',
		textField:'tradeName',
		mode:'remote'
	});
	n=0;
	queryKind(0);
	queryData();
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body style="margin: 0px; padding: 0px;">
<div class="easyui-layout" style="width: 100%; height: 100%;"data-options="fit:true">
	<div data-options="region:'north',border:false" style="width: 100%; height: 40px;padding:5px 5px 5px 5px;">
		<a href="javascript:void(0)" onclick="queryKind(0)" class="easyui-linkbutton" data-options="iconCls:'icon-page_white_excel'">按药品</a>
		<a href="javascript:void(0)" onclick="queryKind(1)" class="easyui-linkbutton" data-options="iconCls:'icon-page_white_excel'">按单据</a>
		<a href="javascript:void(0)" onclick="queryKind(2)" class="easyui-linkbutton" data-options="iconCls:'icon-page_white_excel'">按部门</a>
	</div>
	<div data-options="region:'west'" style="width: 15%; height: 93%;"data-options="fit:true">
		<div class="easyui-layout" style="width: 100%; height: 100%;" data-options="fit:true">
			<div data-options="region:'north',border:false" style="width: 100%; height: 20%;">
			&nbsp;分类信息:
				<form id="tree1" style="padding-left:5%">
				</form>
			</div>
			<div data-options="region:'center'" style="width: 100%; height: 80%;border-left:0;border-right:0">
				<form id="tree2"></form>
			</div>
		</div>
	</div>
	<div data-options="region:'center'" style="width: 85%; height: 93%;border-left:0"data-options="fit:true">
		<div class="easyui-layout" style="width: 100%; height: 98%;"data-options="fit:true">
			<div data-options="region:'north',border:false" style="height:40px;padding:5px 5px 5px 5px;">
				<form id="searchForm">
					<table style="width: 100%" cellspacing="0" cellpadding="0">
						<tr>
							<td>
								&nbsp;日期:
								<input id="beginTime" name="t.beginTime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								至
								<input id="endTime" name="t.endTime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								药品名称:<input id="drugName" class="easyui-combobox" name="t.drugCode"/>
								<input id="drugDeptCode" type="hidden" name="t.drugDeptCode"/>
								<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" onclick="queryData()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:set">
									<a href="javascript:clear();void(0)" onclick="" class="easyui-linkbutton" data-options="iconCls:'reset'">重置</a>
								</shiro:hasPermission>
							</td>
							<td style='text-align: right'>
								<a href="javascript:void(0)" onclick="queryMidday(5)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当年</a>
								<a href="javascript:void(0)" onclick="queryMidday(4)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当月</a>
								<a href="javascript:void(0)" onclick="queryMidday(7)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">上月</a>
								<a href="javascript:void(0)" onclick="queryMidday(6)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">十五天</a>
								<a href="javascript:void(0)" onclick="queryMidday(3)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">七天</a>
								<a href="javascript:void(0)" onclick="queryMidday(2)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">三天</a>
								<a href="javascript:void(0)" onclick="queryMidday(1)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当天</a>
								
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div data-options="region:'center',border:false" style="width: 100%; height: 90%;">
				<div id="cc" class="easyui-layout" data-options="fit:true">   
					<div data-options="region:'north',border:false" style="height:50px;">
					</div>
					<div data-options="region:'center'" style="">
						<table class="easyui-datagrid" id="grid" data-options="fit:true,border:false,pageSize:20,
							pageList:[20,40,80]"></table>
					</div>   
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>