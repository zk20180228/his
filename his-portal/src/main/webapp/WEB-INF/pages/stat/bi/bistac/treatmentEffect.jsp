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
.tableCss{
	border-collapse: collapse;border-spacing: 0;
}
.tableCss .TDlabel{
/* 	text-align: right; */
	font-size:14px;
/* 	width:30%; */
}
.tableCss td{
 	padding: 7px 6px;
/* 	word-break: keep-all; */
/* 	white-space:nowrap; */
/* 	width:80px !important; */
}
.tableData{
	width:50%;height:100%;border-style:none;
}
#table,.tableCss td.treatmentFont{
	border-top:0 !important;
}
#fir{position:relative}

#fir #sec #thr{position:absolute;bottom:0}

</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
    <script type="text/javascript">
    var deptMap = null;
    var persent = null;
    $(function(){
    	//渲染科室
		$.ajax({
			url: "<c:url value='/outpatient/scheduleModel/querydeptCodeAndNameMap.action'/>",
			type:'post',
			success: function(deptData) {
				deptMap = deptData;
		    	monitor();
		        $("#mySelect").combobox({  
		            onChange:choose
		        }); 
			}
		});	
    });
	// 路径配置
	function monitor(){
		require.config({
		    paths: {
		        echarts: '${pageContext.request.contextPath}/javascript/echarts'
		    }
		});
		
		require(
			    [
			        'echarts',
			        'echarts/chart/gauge' // 使用柱状图就加载bar模块，按需加载
			    ],
			    function (ec) {
		    var myChart2 = ec.init(document.getElementById('left'));
		    option2 = {
	    		 title: {
	                 text : '当前在院人数',
	                 x : 'left'
	             },
		        tooltip : {
		            formatter: "{a} : {c}人"
		        },
		        series: [
		            {
		                name: '当前在院人数',
		                min: 0,
		                max: 20000,
		                splitNumber: 5,
		                type:'gauge',
		                detail : {
		                	formatter:'{value}人',
		                	textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	    	                    color: 'auto',
	    	                    fontSize : 16
	    	                }
		                },
		                data: [{value: "${inPeople}" }],
		            }
		        ]
		    };
		    setInterval(function () {
		    	option2.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
		      
		    },2000);
		      myChart2.setOption(option2, true);
			    }
		    );
	}
function decision(){
	// 路径配置
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	
	require(
		    [
		        'echarts',
		        'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
		        'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
		    ],
		    function (ec) {
			 	var myChart1 = ec.init(document.getElementById('main'));
		    	data1 = chooseMap.get("data1");
		    	data2 = chooseMap.get("data2");
		    	data3 = chooseMap.get("data3");
		    	data4 = chooseMap.get("data4");
		    	data5 = chooseMap.get("data5");
		    	data6 = chooseMap.get("data6");
		    	data8 = chooseMap.get("data8");
				var option1 = {
				    tooltip : {
				        trigger: 'axis',
				        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				        },
// 				        formatter : '{b}<br/>{a1}:{c1}%<br/>{a0}:{c0}%<br/>{a2}:{c2}%<br/>{a3}:{c3}%<br/>{a4}:{c4}%'
				    },
// 				    toolbox: {
// 				        show : true,
// 				        feature : {
// 				            magicType : {show: true, type: ['line', 'bar']},
// 				        }
// 				    },
				    legend: {
// 				        data:['好转','其他','死亡','未治','无效','治愈'],
				        data:['治愈','好转','未愈','死亡','其他'],
				        selected: {
	  						'治愈': true,
	  						'好转': true,
	  						'未愈': true,
	  						'死亡': true,
	  						'其他': true,
// 	  						'治愈': true
	  					}
				    },
				    grid: {
				        left: '3%',
				        right: '4%',
				        bottom: '3%',
				        containLabel: true
				    },
				    dataZoom : {
				        show : true,
				        realtime : true,
				        zoomLock : true,
				        start : 0,
				        end : persent
				    },
				    xAxis : [
				        {
				            type : 'category',
				            data : data8
				        }
				    ],
				    yAxis : [
				        {
				            type : 'value',
				            axisLabel : {
				                formatter: yAxisLabel
				            }
				        }
				    ],
				    series : [
// 						barGap :'20',
				        {
				            name:'治愈',
				            type:'bar',
				            stack: '好转',
				            data: data1,
				            barWidth : 30
				        },
				        {
				            name:'好转',
				            type:'bar',
				            stack: '好转',
				            data:data2,
				            barWidth : 30
				        },
				        {
				            name:'未愈',
				            type:'bar',
				            stack: '好转',
				            data:data3,
				            barWidth : 30
				        },
				        {
				            name:'死亡',
				            type:'bar',
				            stack: '好转',
				            data:data4,
				            barWidth : 30
				        },
				        {
				            name:'其他',
				            type:'bar',
				            stack: '好转',
				            data:data5,
				            barWidth : 30
				        }/* ,
				        {
				            name:'治愈',
				            type:'bar',
				            stack: '好转',
				            data:data6
				        } */
				    ]
				};
			myChart1.setOption(option1);
		    }
		  );
}
	var chooseMap = new Map();
	var yAxisLabel = '{value} 人';
//下拉分析柱状图
function choose(){
	var magnitude = $("#mySelect").combobox("getValue"); 
	var yearSelect = $("#yearSelect").val(); 
	var data1 = new Array();//治愈
	var data2 = new Array();//好转
	var data3 = new Array();//未愈
	var data4 = new Array();//死亡
	var data5 = new Array();//其他
	var data6 = new Array();//========预留数组
	var data7 = new Array();//总计
	var data8 = new Array();//科室编码
	var data9 = new Array();//科室名称
	yAxisLabel = '{value} 人';
	$.ajax({
		url: '<%=basePath%>statistics/treatmentEffect/queryTreamentByMongo.action?yearSelect='+yearSelect,
		type:'post',
		success: function(userData) {
				var dNum = 0;
			if(userData==""){
				for(var key in deptMap){
					data8[dNum]=key;
					data9[dNum]=deptMap[key];
					data1[dNum]=0;
					data2[dNum]=0;
					data3[dNum]=0;
					data4[dNum]=0;
					data5[dNum]=0;
					data6[dNum]=0;
					data7[dNum]=0;
					dNum++;
				}
			}else{
				var rowTotal = 0;
				var j = 0;
				for(var i = 0;i<userData.length;i++){
					if(data8.indexOf(userData[i].deptName)==-1){
						data8[data8.length]=userData[i].deptName;
						data9[data9.length]=deptMap[userData[i].deptName];
						data1[dNum]=0;
						data2[dNum]=0;
						data3[dNum]=0;
						data4[dNum]=0;
						data5[dNum]=0;
						data6[dNum]=0;
						data7[dNum]=0;
						dNum++;
					}
					j = data8.indexOf(userData[i].deptName);
					switch (userData[i].outState) {
					//出院状态 0 治愈1 好转 2 未愈3 死亡 4 其他
					case 0:
						data1[j]=userData[i].outStateTotal;
						data7[j] += userData[i].outStateTotal;
						break;
					case 1:
						data2[j]=userData[i].outStateTotal;
						data7[j] += userData[i].outStateTotal;
						break;
					case 2:
						data3[j]=userData[i].outStateTotal;
						data7[j] += userData[i].outStateTotal;
						break;
					case 3:
						data4[j]=userData[i].outStateTotal;
						data7[j] += userData[i].outStateTotal;
						break;
					case 4:
						data5[j]=userData[i].outStateTotal;
						data7[j] += userData[i].outStateTotal;
						break;
					default:
						break;
					} 
				}
				if('ten'==magnitude){
					for(var i = 0;i<data1.length;i++){
						data1[i] = ((Number(data1[i])/Number(data7[i]))*100).toFixed(2);
						data2[i] = ((Number(data2[i])/Number(data7[i]))*100).toFixed(2);
						data3[i] = ((Number(data3[i])/Number(data7[i]))*100).toFixed(2);
						data4[i] = ((Number(data4[i])/Number(data7[i]))*100).toFixed(2);
						data5[i] = ((Number(data5[i])/Number(data7[i]))*100).toFixed(2);
						data6[i] = ((Number(data6[i])/Number(data7[i]))*100).toFixed(2);
					}
					yAxisLabel = '{value} %';
				}
			}
			chooseMap.put("data1",data1);
			chooseMap.put("data2",data2);
			chooseMap.put("data3",data3);
			chooseMap.put("data4",data4);
			chooseMap.put("data5",data5);
			chooseMap.put("data6",data6);
			chooseMap.put("data7",data7);
			chooseMap.put("data8",data9);
			persent = 20/dNum*100;
			decision();
			if('ten'!=magnitude){
				addTable();
			}
		}
	});	
}


function statistics(){
	var staYear = $("#staSelect").combobox("getValue");
	
}
//选择科室添加一行
function addTable(){
	
	
	$(".addRow").each(function(){$(this).remove(0)});
	$('#allDep').attr('rowSpan', 0);

// 	$(".addRow").remove;
// 	$(".addRow").empty();
	data1 = chooseMap.get("data1");
	data2 = chooseMap.get("data2");
	data3 = chooseMap.get("data3");
	data4 = chooseMap.get("data4");
	data5 = chooseMap.get("data5");
	data6 = chooseMap.get("data6");
	data7 = chooseMap.get("data7");
	data8 = chooseMap.get("data8");
	var dataP1 = new Array();//治愈
	var dataP2 = new Array();//好转
	var dataP3 = new Array();//未愈
	var dataP4 = new Array();//死亡
	var dataP5 = new Array();//其他
	for(var i = 0;i<data1.length;i++){
		if(data7[i]==0){
			dataP1[i] = ((Number(data1[i])/1)*100).toFixed(2);
			dataP2[i] = ((Number(data2[i])/1)*100).toFixed(2);
			dataP3[i] = ((Number(data3[i])/1)*100).toFixed(2);
			dataP4[i] = ((Number(data4[i])/1)*100).toFixed(2);
			dataP5[i] = ((Number(data5[i])/1)*100).toFixed(2);
		}else{
			dataP1[i] = ((Number(data1[i])/Number(data7[i]))*100).toFixed(2);
			dataP2[i] = ((Number(data2[i])/Number(data7[i]))*100).toFixed(2);
			dataP3[i] = ((Number(data3[i])/Number(data7[i]))*100).toFixed(2);
			dataP4[i] = ((Number(data4[i])/Number(data7[i]))*100).toFixed(2);
			dataP5[i] = ((Number(data5[i])/Number(data7[i]))*100).toFixed(2);
	// 		dataP6[i] = ((Number(data6[i])/Number(data7[i]))*100).toFixed(2);
		}
	}
	var spanNum = $('#allDep').attr('rowSpan')+1;
		$('#allDep').attr('rowSpan',Number(spanNum)+Number(data8.length));
	var tableWidth = $("#table1").width()+2;
	$("#table").width(tableWidth);
	for(var i = 0;i<data8.length;i++){
		var html = "<tr class='addRow'><td  width='8%'><input value='surgery' type='hidden'/>"+data8[i]+
		"</td><td width='7%'>"+data1[i]+"</td><td width='7%'>"+dataP1[i]+"%</td>"+
		"<td width='7%'>"+data2[i]+"</td><td width='7%'>"+dataP2[i]+"%</td><td width='7%'>"+data3[i]+
		"</td><td width='7%'>"+dataP3[i]+"%</td><td width='7%'>"+data4[i]+"</td><td width='7%'>"+dataP4[i]+"%</td>"+
		"<td width='7%'>"+data5[i]+"</td><td width='7%'>"+dataP5[i]+"%</td><td width='7%'>"+data7[i]+"</td><td width='7%'>100%</td></tr>";
		$("#table").append(html);
	}
	
}

</script>
</head>
<body>

	<div id="fir" style=" width:100%; height:38%">
	    <div id="left" style="width:30%;height:100%; float: left;border-bottom:0"></div>
	    <div id="right" style="width:70%;height:155px;float:right;overflow : hidden;" >
	    	 <table style="width:100%;height:100%;border-top:0 !important;" id="table1"  class="tableCss" >
	    	 	<tr  class="TDlabel" height="47px"><td colspan="14" style="font-size: 20;font-weight: bold;" class="treatmentFont">数据透视表</td></tr>
		    	  <tr height="29px">
	    	  		<td rowspan="2" colspan="2" >
	    	  			住院诊断分析
	    	  		</td>
	    	  		<td colspan="12" class="TDlabel">
	    	  			<div class="treatmentFontYear">统计年
	    	  			<input id="yearSelect" value="2017" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy',onpicked:choose})"/>
				    	</div>
	    	  		</td>
	    	  	</tr>
	    	  	<tr>
	    	  		<td colspan="2">治愈</td>
	    	  		<td colspan="2">好转</td>
	    	  		<td colspan="2">未愈</td>
	    	  		<td colspan="2">死亡</td>
	    	  		<td colspan="2">其他</td>
<!-- 	    	  		<td colspan="2">无效</td> -->
	    	  		<td colspan="2">总计</td>
	    	  	</tr>
	    	  	<tr class="TDlabel " height="50px" >
	    	  		<td width="8%">入院处室</td>
	    	  		<td width="8%">
						入院科室
	    	  		</td>
	    	  		<td width="7%" id="zlrc">治疗人次</td>
	    	  		<td width="7%"id="zlrcb">治疗人次%</td>
	    	  		<td width="7%">治疗人次</td>
	    	  		<td width="7%">治疗人次%</td>
	    	  		<td width="7%">治疗人次</td>
	    	  		<td width="7%">治疗人次%</td>
	    	  		<td width="7%">治疗人次</td>
	    	  		<td width="7%">治疗人次%</td>
	    	  		<td width="7%">治疗人次</td>
	    	  		<td width="7%">治疗人次%</td>
	    	  		<td width="7%">治疗人次</td>
	    	  		<td width="7%">治疗人次%</td>
	    	  	</tr>
	    	  	</table>
	    </div>
	    <div id="right" style="width:70%;height:59%;float:right;overflow-y:scroll;overflow-x:hidden" >
	    	 <table style="width:100%;height:100%;" id="table"  class="tableCss" >
		   		<tr>
		   			<td id="allDep" rowspan="0" valign="top"  width="8%">全院</td>
		   		</tr>
		   </table>
	    </div>
	</div>
	
	<div id="sec" style="width:100%;height:5%;" text-align="left">
		<table style="width:100%;height:100%;" id="table"  class="tableCss" >
	    	 	<tr class="TDlabel"><td style="font-size: 14 ;font-weight: bold;" class="treatmentFontYear">数据透视表
	    	 	<!-- <input class="easyui-combobox" id="mySelect"   
		                data-options="valueField: 'value',textField: 'label',data: [{  
		                label: '治疗人次',  
		                value: 'one',  
		                selected:true}, 
		                {label: '治疗人次%',  
		                value: 'ten'}]"                    
		              /> -->
		              </td>
		        </tr>
	    </table>
				<div style="padding-top: 5px" class="treatmentFontYear">
				量值 
				  	<input class="easyui-combobox" id="mySelect" 
				                data-options="valueField: 'value',textField: 'label',data: [{   
				                label: '治疗人次',  
				                value: 'one', 
				                selected:true}, 
				                {label: '治疗人次%',
				                value: 'ten'}]"             
				              />  
				 </div>
	</div>
	<div  id="thr" style="width:100%;height:53%;">
<!-- 		<div style="padding-top: 82px" class="treatmentFontYear">   -->
<!-- 		  	<input class="easyui-combobox" id="mySelect"  -->
<!-- 		                data-options="valueField: 'value',textField: 'label',data: [{    -->
<!-- 		                label: '治疗人次',   -->
<!-- 		                value: 'one',  -->
<!-- 		                selected:true},  -->
<!-- 		                {label: '治疗人次%', -->
<!-- 		                value: 'ten'}]"              -->
<!-- 		              />   -->
<!-- 		 </div> -->
		<div id="main" style="padding-top: 100px;width:100%;height:75%">
		</div>
	</div>
	<div style="width:100%;height:4%;"></div>
</body>
</html>