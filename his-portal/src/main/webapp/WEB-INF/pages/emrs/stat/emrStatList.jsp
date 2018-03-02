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
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
<script type="text/javascript">
var colorYQ = [ 
		'#ff7f50', '#da70d6', '#6495ed', 
		'#ff69b4', '#ba55d3', '#cd5c5c', '#ffa500', '#40e0d0', 
		'#1e90ff', '#ff6347', '#7b68ee',  
		'#6b8e23', '#ff00ff', '#3cb371', '#b8860b', '#30e0e0' 
			];
var sum = 0;
var beginTime = '${beginTime}'
var endTime = '${endTime}'
$("#beginTime").val(beginTime);
$("#endTime").val(endTime);
var dateName = ['输血病历','检验异常病历','危重病历','手术病历','死亡病历','抗生素使用病历'];
var pieValue = [
				{value:0, name:'输血病历'},
				{value:0, name:'检验异常病历'},
				{value:0, name:'危重病历'},
				{value:0, name:'手术病历'},
				{value:0, name:'死亡病历'},
				{value:0, name:'抗生素使用病历'},
			];;
var barValue = [0,0,0,0,0,0];
	//柱状图
	function echarZhu(dateList, sum){
		// 路径配置
		require.config({
			paths: {
				echarts: '${pageContext.request.contextPath}/javascript/echarts'
			}
		});
		// 使用
		require(
			[
				'echarts',
				'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
				'echarts/chart/line'
			],
			function (ec) {
				  // 基于准备好的dom，初始化echarts图表
				 var myChart = ec.init(document.getElementById('areaByYear')); 
				 myChart.showLoading({text: '正在努力的读取数据中...',textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
				  var option = {
							title : {
								text: '病历数量',
								x:'left'
							},
							tooltip : {
								formatter: function (params, ticket, callback) {
									var i = "--";
									if(sum != 0){
										i = (Number(params.data)/Number(sum)*100).toFixed(2)+"%";
									}
									var num = '病历数';
									var bz = "总占比(%)";
									return num+"<br/>"+params.data+"<br/>"+bz+"<br/>"+i;
								},
								axisPointer : {			// 坐标轴指示器，坐标轴触发有效
									type : 'shadow'		// 默认为直线，可选为：'line' | 'shadow'
								},
							},
							toolbox:{
								show:true,
								feature:{
									restore:{show:true}
								},
							},
							xAxis : [
								{
									type : 'category',
									data : dateName,
									axisLabel:{
										rotate : 30		
									}
								}
							],
							yAxis : [
								{
									type : 'value'
								}
							],
							calculable : true,//显示边框
							color : colorYQ,
							series : [
								{
									name:'病历数',
									type:'bar',
									data: dateList,
									barWidth : 20
								},
								{
									name:'病历数',
									type:'line',
									data: dateList
								}
							]
						};
				  // 为echarts对象加载数据 
				  myChart.setOption(option); 
				  myChart.hideLoading();
				}
		);
	 }
//饼图
function echarBing(dateList){
	// 路径配置
	require.config({
		paths: {
			echarts: '${pageContext.request.contextPath}/javascript/echarts'
		}
	});
	// 使用
	require(
		[
			'echarts',
			'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
		],
		function (ec) {
			// 基于准备好的dom，初始化echarts图表
			var myChart = ec.init(document.getElementById('main2')); 
			myChart.showLoading({text: '正在努力的读取数据中...',textStyle: { color: '#444' },effectOption: {backgroundColor: 'rgba(0, 0, 0, 0.1)'}});
			var option = {
				title : {
					text: '病历占比',
					x:'left'
				},
				toolbox:{
					show:true,
					feature:{
						restore:{show:true}
					},
				},
				tooltip : {
					trigger: 'item',
					formatter: "{b} : <br/>{c} ({d}%)"
				},
				calculable : true,//显示边框
				color : colorYQ,
				series : [
					{
						name: '病历数',
						minAngle: 20,
						type: 'pie',
						radius : '50%',
						center: ['50%', '60%'],
						data:dateList,
						itemStyle:{ 
							normal:{ 
								label:{ 
									show: true, 
									position:'top',
									formatter: "{b}:{c}({d}%)"
								}, 
								labelLine :{show:true}
							} 
						}
					}
				]
			};
			// 为echarts对象加载数据 
			myChart.setOption(option); 
			myChart.hideLoading();
		}
	);
}

function pickedEndTime(){
	var begin = $("#beginTime").val();
	var end = $("#endTime").val();
 	if(begin != '' && begin > end){
 		$("#endTime").val('');
 		$.messager.alert("操作提示", "结束时间需大于开始时间", "warning");
		setTimeout(function(){
			$(".messager-body").window('close');
		},1500);
 	}
}

function pickedBeginTime(){
	var begin = $("#beginTime").val();
	var end = $("#endTime").val();
	console.log(end == '');
	if(end != '' && begin > end){
		$("#beginTime").val('');
 		$.messager.alert("操作提示", "开始时间需小于结束时间", "warning");
		setTimeout(function(){
			$(".messager-body").window('close');
		},1500);
 	}
	
}

function start(){
	$.ajax({
		url:"<%=basePath%>emr/emrStat/queryValues.action",
		type:'post',
		data:{beginTime:$("#beginTime").val(),endTime:$("#endTime").val()},
		success:function(data){
			pieValue = [
				{value:data.blood, name:'输血病历'},
				{value:data.check, name:'检验异常病历'},
				{value:data.sick, name:'危重病历'},
				{value:data.opretion, name:'手术病历'},
				{value:data.death, name:'死亡病历'},
				{value:data.antibiotic, name:'抗生素使用病历'},
			];
			barValue = [data.blood, data.check, data.sick,
						data.opretion, data.death, data.antibiotic];
			echarBing(pieValue);
			sum = data.sum;
			echarZhu(barValue, sum);
			$('#list').datagrid('load',{
				beginTime: $("#beginTime").val(),
				endTime: $("#endTime").val(),
				type: $('#type').combobox('getValue')
			});
		}
	});
}
$(function(){
	$('#type').combobox({
		editable: false,
		valueField: 'code',
		textField: 'name',
		data:[{code:'1',name:'输血病历'},{code:'2',name:'危重病历'},{code:'3',name:'死亡病历'},
			{code:'4',name:'检查检验异常病历'},{code:'5',name:'手术病历'},{code:'6',name:'抗生素使用病历'},{code:'7',name:'全部'}]
	})
	$('#type').combobox('setValue','1');
	//添加datagrid事件及分页
	$('#list').datagrid({
		url: "<%=basePath%>emr/emrStat/queryList.action",
		pagination:true,
		pageSize:20,
		pageList:[20,30,50,80,100],
		queryParams: {
			beginTime: $("#beginTime").val(),
			endTime: $("#endTime").val(),
			type: $('#type').combobox('getValue')
		},
		onBeforeLoad:function(){
			$('#list').datagrid('clearChecked');
			$('#list').datagrid('clearSelections');
		},
		onLoadSuccess:function(row, data){
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
				}}
	});
	start()
	echarBing(pieValue);
	echarZhu(barValue, sum);
	
	
});


</script>
<style type="text/css">
	.datagrid-htable .datagrid-sort-icon{
		display: none;
	}
</style>
</head>
<body>
	<div id="cc" class="easyui-layout" fit="true"> 
		<div data-options="region:'north'" style="height:40px;width:50%;">
			<table style="width:100%;border:false;padding:5px 5px 5px 5px;">
				<tr>
					<td>
						查询时间:
						<input id="beginTime" class="Wdate" type="text" value="${beginTime}" onClick="WdatePicker({dateFmt:'yyyy/MM/dd',onpicked:pickedBeginTime,maxDate:'%y-%M-%d'})" />
						至
						<input id="endTime" class="Wdate" type="text" value="${endTime}" onClick="WdatePicker({dateFmt:'yyyy/MM/dd',onpicked:pickedEndTime,maxDate:'%y-%M-%d'})" />
					&nbsp;
						查询类型：
						<input id="type" class="easyui-combobox" style="padding: 0;" />
						
					<a href="javascript:void(0)" onclick="start()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center'" style="width:55%;height: 90%;float: left;">
			<table id="list" class="easyui-datagrid" 
				data-options="fit:true,method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false"> 
				<thead> 
					<tr> 
						<th field="name" width="20%">患者姓名</th> 
						<th field="sex" width="10%">性别</th> 
						<th field="inpatientNo" width="15%">住院号</th> 
						<th field="deptInnm" width="15%" >入院科室</th> 
						<th field="firstSubmitOper" width="15%" >病历提交人</th> 
						<th field="firstSubmitDate" width="20%" >病历提交时间</th> 
					</tr> 
				</thead> 
			</table>
		</div>
		<div data-options="region:'east'" style="width:45%;height: 100%;float: left;">
			<!-- 柱状图 -->
			<div  style="height:45%;width: 90%;">
				<div id="main2" style="height:100%;width:100%" ></div>
			</div>
			<!-- 饼状图 -->
			<div data-options="region:'east',border:false" style="height:45%;width: 90%;">
				<div id="areaByYear" style="height:100%;width:100%;" ></div>
			</div> 
		</div>
	</div>
</body>
		</div>
</html>