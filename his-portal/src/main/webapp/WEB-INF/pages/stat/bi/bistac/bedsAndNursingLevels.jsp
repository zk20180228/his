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
<title>当日床位使用情况统计及当日护理级别统计</title>
<style type="text/css">
	#left_echar_box{
		float: left;
	}
	#right_echar_box{
		float: left;
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
	th{
		text-align: center;
	}
</style>
</head>
<body>
	<div class = "brenTitle" style="font-family: '宋体';font-size:'30px'">当日床位使用情况统计及当日护理级别统计</div>
	<div class="echar_main clearfix">
		<div id="left_echar_box" style="width:50%;height: 60%">
			<div  id="left_echar" style=";width:80%;height:80%;margin: 0 0 0 auto;"></div>
			<div class="table_box" id="left_table">
				<table  border="0" cellspacing="0" cellpadding="0" style = "width:80%; margin: 0 0 0 auto">
					<thead  class="datagrid-header" id = "left_echar_head"></thead>
					<tbody id = "left_echar_body"></tbody>
				</table>
			</div>
		</div>
		<div id="right_echar_box" style="width:50%;height: 60%">
			<div id="right_echar" style="width:80%;height:80%;   margin: 0 auto 0 0;"></div>
			<div class="table_box " id="left_table" >
				<table id = "right_table" border="0" cellspacing="0" cellpadding="0" style = "width:80%; margin: 0 auto 0 0">
					<thead class="datagrid-header" id = "right_echar_head"></thead>
					<tbody id = "right_echar_body"></tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>
<script src="<%=basePath%>javascript/echarts/echarts.min.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
		var echarObj = {
			leftechar: echarts.init(document.getElementById('left_echar')),
			rightechar: echarts.init(document.getElementById('right_echar'))
		};
		(function(echarObj,$) {
			$("#left_echar_box").setLoading("left")
			$("#right_echar_box").setLoading("right")
			var leftResData = {
				title: {
					text: '当日床位使用情况统计',
					x: 'center',
					y: "top"
				},
				color: ['#27727B', '#FDB12A','#FF9190', '#79C2F5', '#A3E874',  "#CCA6D9", "#ECE579", "#E1B070"],
				tooltip: {
					trigger: 'item',
					formatter: "{a} <br/>{b} : {c} ({d}%)"
				},
				legend: {
					orient: 'vertical',
					x: 'left',
					data: [],
					padding:[40, 0, 0, 40]
				},
				series: [{
					name: '张数',
					type: 'pie',
					radius: '60%',
					center: ['60%', '60%'],
					data: [],
					itemStyle: {
						normal:{ 
	                        label:{ 
	                            show: true, 
	                            formatter: '{b} : {c} ({d}%)' 
	                        }, 
	                        labelLine :{show:true} 
	                    }
					},
						fontSize:30
					
				}]

			}
			echarObj.leftechar.setOption(leftResData);
			var rightResData = {
				title: {
					text: '当日护理级别情况统计',
					x: 'center',
					y: "top"

				},
				color: ['#27727B', '#FDB12A', '#79C2F5', '#A3E874', '#FF9190', "#CCA6D9", "#ECE579", "#E1B070"],
				tooltip: {
					trigger: 'item',
					formatter: "{a} <br/>{b} : {c} ({d}%)"
				},
				legend: {
					orient: 'vertical',
					x: 'left',
					data: [],
					padding:[40, 0, 0, 40]
					
				},
				series: [{
					name: '数量',
					type: 'pie',
					radius: '60%',
					center: ['60%', '60%'],
					data: [],
					itemStyle: {
						normal:{ 
	                        label:{ 
	                            show: true, 
	                            formatter: '{b} : {c} ({d}%)' 
	                        }, 
	                        labelLine :{show:true} 
	                    } 
					}

				}]

			}
			echarObj.rightechar.setOption(rightResData);
			

			$.ajax({
				type:"post",
				url:"<%=basePath%>statistics/bedsAndNursingLevels/queryBeds.action",
				success:function(data){
					$("#left_echar_head").html(null)
					$("#left_echar_body").html(null)
					var lefttmpArr = []
					for(var i = 0 ;i <data.length ;i++){
						lefttmpArr.push(data[i].name)
					}
					echarObj.leftechar.setOption({
						legend: {
							data:lefttmpArr
						},
	       				series: [{
	    					data:data
	    				}]
					})
					var left_head = "<th>名称</th>"
					var left_body = "<tr><td>数量</td>"
					for(var i = 0 ;i<data.length;i++){
						left_head+="<th>"+ data[i].name +"</th>"
						left_body+="<td>"+ data[i].value +"</td>"
					}		
					
					$("#left_echar_head").html(left_head)
					$("#left_echar_body").html(left_body+"</tr>")
					
					
					$("#left_echar_box").rmoveLoading("left")
				}
			});
			$.ajax({
				type:"post",
				url:"<%=basePath%>statistics/bedsAndNursingLevels/queryNursingLevels.action",
				success:function(data){
					$("#right_echar_head").html(null)
					$("#right_echar_body").html(null)
					var righttmpArr = []
					var tmp = []
					for(var i = 1 ;i <data.length ;i++){
						righttmpArr.push(data[i].name)
						tmp.push(data[i])
					}
					echarObj.rightechar.setOption({
						legend: {
							data:righttmpArr
						},
	       				series: [{
	    					data:tmp
	    				}]
					})
					
					var right_head = "<th>名称</th>"
					var right_body = "<tr><td>数量</td>"
					for(var i = 1 ;i<data.length;i++){
						right_head+="<th>"+ data[i].name +"</th>"
						right_body+="<td>"+ data[i].value +"</td>"
					}		
					
					$("#right_echar_head").html(right_head)
					$("#right_echar_body").html(right_body+"</tr>")
					
					$("#right_echar_box").rmoveLoading("right")
				}
			});
		})(echarObj,$);
		
	</script>
	