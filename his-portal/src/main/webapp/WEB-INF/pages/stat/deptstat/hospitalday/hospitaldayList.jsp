<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<style type="text/css">
	*{
		box-sizing: border-box;
	}
	.tiemBox {
		width: 100%;
		top:0;
		left: 0;
		height: 40px;
		float: left;
		padding-left: 15px;
		line-height: 40px;
	}

	.table_box {
		padding: 0 50px;
	}
	.table_box table{
		width: 100% ;
		text-align: center;
		border-color: #ffffff;
	}
	.echar_main{
		width:100%;
		margin: 0 auto;
	}
	.brenTitle{
		margin: 0 auto;
		text-align: center;
		font-size: 30px;
		line-height: 30px;
		font-weight: 900;
		height: 100px;
		line-height: 100px;
	}
	.table_box table thead {
		line-height: 30px;
		border: 1px solid #ccc; 
	}
	.table_box table thead th {
		border: 1px solid #ccc; 
	}
	.table_box table tbody tr td {
		line-height: 30px;
		border: 1px solid #ccc; 
	}
	.table_box table tbody tr td:hover{
		background-color: #ccc
	}
	.clearfix:after {
	  content: "";
	  display: block;
	  clear: both;
	  height: 0;
	}
	.clearfix {
	  zoom: 1;
	}
	.table_box table,
	.table_box table td,
	.table_box table th{
		border:1px solid #ff0000;
		border-collapse:collapse;
	}
	table td{
		cursor: default;
	}
	
	.echarboxAll {
		padding: 10px;
		height: 490px;
	}
	
	.echarbox {
		padding: 10px;
		height: 100%;
	}
	
	.chartTitle {
		height: 60px;
		line-height: 60px;
		font-size: 23px;
		font-weight: 900;
		background-color: #fff;
	}
	
	.chartTitleLeft {
		margin: 0 25px;
		float: left;
	}
	
	.chartTitleRight {
		margin: 0 25px;
		float: right;
	}
	
	.chart {
		width: 100%;
		height: 390px;
		background-color: #fff;
	}
	.tableTmpBox {
		padding: 70px 20px 0 20px;
		width: 100%;
		height: 100%;
	}
	.tableTmp {
		width: 100%;
		text-align: center;
		line-height: 50px;
		border:1px solid #CCCCCC;
		border-collapse:collapse;
		font-weight: 600;
		font-size: 18px;
	}
</style>
</head>
<body>
<div>
	<!--<ul class="nav nav-tabs" role="tablist">
		<li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">全部</a></li>
		 <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">门诊</a></li>
		<li role="presentation"><a href="#messages" aria-controls="messages" role="tab" data-toggle="tab">在院</a></li>
		<li role="presentation"><a href="#settings" aria-controls="settings" role="tab" data-toggle="tab">手术</a></li>
		<li role="presentation"><a href="#income" aria-controls="income" role="tab" data-toggle="tab">收入</a></li>
		<li role="presentation"><a href="#leavehospital" aria-controls="leavehospital" role="tab" data-toggle="tab">出院</a></li>
		<li role="presentation"><a href="#inhospital" aria-controls="inhospital" role="tab" data-toggle="tab">入院</a></li>
		<li role="presentation"><a href="#drugratio" aria-controls="drugratio" role="tab" data-toggle="tab">药比</a></li> 
 	</ul>-->
	<div class="tab-content">
		<div role="tabpanel" class="tab-pane active" id="home">
			<div class = "tiemBox">
				日期:
				<input id="time" class="Wdate" type="text" value="${endTime}"onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d',onpicked:FuncByDay,})"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;height: 24px;"/>
			</div>
			<div id = "echar_main" class="echar_main clearfix">
			</div>
		</div>
		<!-- <div role="tabpanel" class="tab-pane" id="profile">
			<span>正在建设中。。。</span>
		</div>
		<div role="tabpanel" class="tab-pane" id="messages"><span>正在建设中。。。</span></div>
		<div role="tabpanel" class="tab-pane" id="settings"><span>正在建设中。。。</span></div>
		<div role="tabpanel" class="tab-pane" id="income"><span>正在建设中。。。</span></div>
		<div role="tabpanel" class="tab-pane" id="leavehospital"><span>正在建设中。。。</span></div>
		<div role="tabpanel" class="tab-pane" id="inhospital"><span>正在建设中。。。</span></div>
		<div role="tabpanel" class="tab-pane" id="drugratio"><span>正在建设中。。。</span></div> -->
	</div>
</div>

	<script type="text/html" id="chartemp">
		<div class="echarboxAll">
			{{each data as value index}}
			<div class="echarbox col-lg-4 col-md-6 col-sm-6 col-xs-12">
				<div class="chartTitle clearfix">
					<span class="chartTitleLeft">{{value.total.name}}</span>
					<span class="chartTitleRight">{{value.total.value}}{{value.config.unit}}</span>
				</div>
				<div id="chartItem{{index}}" class="chart"></div>
			</div>
			{{/each}}
		</div>
	</script>
	<script type="text/html" id="tableTmp">
		<div class="tableTmpBox">
			<table border="1" class="tableTmp">
				<tr>
					<td>{{data.config.titalinfo.split(',')[0]}}</td>
					<td>{{data.config.titalinfo.split(',')[1]}}</td>
					<td>{{data.config.titalinfo.split(',')[2]}}</td>
				</tr>
				{{each data.data as value index}}
				<tr>
					<td style='color: {{data.color[index]}};'>{{value.name.replace('院区','')}}</td>
					<td>{{value.value.split(',')[0]}}</td>
					<td>{{value.value.split(',')[1]}}</td>
				</tr>
				{{/each}}
			</table>
		</div>
	</script>
	
	
	
<script src="<%=basePath%>javascript/echarts/echarts.min.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
	FuncByDay()
	function FuncByDay() {
		var dateTime = $('#time').val();
		$("body").setLoading("honeLoad")
		$.ajax({
			type: "post",
			data: {endTime: dateTime},
			url: '<%=basePath%>statistics/hospitalday/queryHospitaldaylist.action',
			success: function(data) {
				$("#echar_main").html(null)
				$("body").rmoveLoading("honeLoad")
				if(data['resCode'] == "success") {
					var str = template("chartemp", {
						data: data.baseData
					})
					$("#echar_main").append(str)
					ajaxCallbackInitChart(data.baseData)
				}
			}
		});
	}
	function ajaxCallbackInitChart(chartData) {
		// set echarts config
		var config = {
			color: ['#48cda6', '#FF7F50', '#11abff']
		}
		var eChart = {}
		// set echartsDom
		var chartObj = $(".chart")
		for(var i = 0; i < chartObj.length; i++) {
			eChart[chartObj[i].id] = {
				chartDom: chartObj[i]
			}
		}
		//set echarts Data
		for(var key in chartData) {
			eChart["chartItem" + key]["data"] = chartData[key].data
			var legendDatatmp = chartData[key].data
			var legendDataArr = []
			var legendDatashow = {}
			for(var i = 0; i < legendDatatmp.length; i++) {
				var tmp = legendDatatmp[i]["name"]
				legendDataArr.push(tmp)
				legendDatashow[tmp] = tmp.replace("院区", "") + "：" + legendDatatmp[i]["value"]
			}
			eChart["chartItem" + key]["legendData"] = legendDataArr
			eChart["chartItem" + key]["legendshowData"] = legendDatashow
			eChart["chartItem" + key]["config"] = chartData[key].config
			fn[eChart["chartItem" + key]["config"]["type"]](config, eChart["chartItem" + key]) 
		}
	}

	var fn = {
		table:setTable,
		pie:setEchart,
	}
	
	function setTable(config, data) {
		var data = data
		data.color = config.color
		var str = template("tableTmp", {
			data:data
		})
		data.chartDom.innerHTML = str
	}

	function setEchart(config, data) {
		echarts.init(data.chartDom).setOption({
			color: config["color"],
			backgroundColor: "#ffffff",
			tooltip: {
				trigger: 'item',
				formatter: "{a} <br/>{b} : {c} ({d}%)"
			},
			legend: {
				data: data.legendData,
				bottom: '5%',
				itemHeight: 20,
				textStyle: {
					fontWeight: 900,
					fontSize: 15
				},
				formatter: function(showData) {
					return data.legendshowData[showData]+"   ";
				}
			},
			series: [{
				name: data.config.unit,
				type: 'pie',
				radius: '45%',
				center: ['50%', '40%'],
				data: data.data,
				itemStyle: {
					normal: {
						label: {
							show: true,
							formatter: '{b} \n {c} ({d}%)',
							textStyle: {
								fontWeight: 'normal',
								fontSize: 15
							}
						},
						labelLine: {
							show: true
						}
					}
				},
				fontSize: 30
			}]
		});
	}
</script>
</body>
</html>