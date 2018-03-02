<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%=basePath %>javascript/echarts/newChart/echarts-all-3.js"></script>

<link rel="stylesheet" type="text/css" href="<%=basePath %>themes/system/css/inpatientIncome.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>住院收入分析</title>
<script type="text/javascript">
/*
 * 时间控件的点击事件
 */
var strTime1;//记录两个时间的值
var strTime2;
function pickedFunc1(){
	$(".addRow").empty();
	if ($('#time1').val() == $('#time2').val()) {
		$('#time1').val(strTime1);
		$('#time2').val(strTime2);
		start();
		$.messager.alert("提示","查询时间不能相同！");
		return;
	}else{
		start();
	}
}
function pickedFunc2(){
	$(".addRow").empty();
	if ($('#time2').val() == $('#time1').val()) {
		$('#time1').val(strTime1);
		$('#time2').val(strTime2);
		start();
		$.messager.alert("提示","查询时间不能相同！");
		return;
	}else{
		start();
	}
}
	
var lzName = "住院总实收金额";	
var deptNames=[];
var tongbi1=[];
var tongbi2=[];
var costs1=[];
var costs2=[];
var arr1=[];
var arr2=[];
var time1;
var time2;
var persent=null;
$(function(){
	
	top1();
	start();
})
function start(){
	$("#style").setLoading("ida")
           $.ajax({
               type: "post",
               url: "<%=basePath %>statistics/inpatientIncome/queryInpatientIncomeVo.action",
               data: {time1:$('#time1').val(),time2:$('#time2').val()},
               dataType: "json",
               success: function (data) {
            	   var obj=data.inpatientIncomeVos;
            	   deptNames=data.deptNames;
            	   costs1=data.supplyCosts1;
            	   costs2=data.supplyCosts2;
            	   tongbi1=data.tongbi1;
            	   tongbi2=data.tongbi2;
            	   var html = "";
            	   time1=$('#time1').val();
            	   time2=$('#time2').val();
            	   var spanNum = $('#allDep').attr('rowSpan');
            	   persent = 8/(obj.length)*100;
            	   $('#allDep').attr('rowSpan',Number(spanNum)+obj.length);
					for (var i = 0; i < obj.length; i++) {
                       html += "<tr class='addRow'><td align='right' class='blue td2'>"+obj[i].deptName+"</td><td align='right' class='blue td2'>"+obj[i].cost1.toFixed(2)+"</td><td align='right' class='blue td2'>"+obj[i].NoCost1.toFixed(2)+"</td>"+
               						  "<td align='right' class='blue td2'>"+obj[i].costs1.toFixed(2)+"</td><td align='right' class='blue td2'>"+obj[i].tongbi1+"</td><td align='right' class='blue td2'>"+obj[i].cost2.toFixed(2)+"</td><td align='right' class='blue td2'>"+obj[i].NoCost2.toFixed(2)+"</td><td align='right' class='blue td2'>"+obj[i].costs2.toFixed(2)+"</td>"+
               						  "<td align='right' class='blue td2'>"+obj[i].tongbi2+"</td><td align='right' class='blue td2'>"+obj[i].cost3.toFixed(2)+"</td><td align='right' class='blue td2'>"+obj[i].NoCost3.toFixed(2)+"</td><td align='right' class='blue td2'>"+obj[i].costs3.toFixed(2)+"</td><td align='right' class='blue td2'>"+obj[i].tongbi3+"</td></tr>";
                   }
              		$("#maintable").append(html);
					
                   if(document.getElementById('maintable').clientHeight>(document.getElementById('style').clientHeight-document.getElementById('style2').clientHeight)){
              		 $("#style").css("overflow-y","scroll"); 
              		 $("#maintable").css({
              			"width":"100%",
              			"padding":"20px"
              		});
              		}else{
              			$("#style").css("overflow-y",""); 
              	 	 	$("#maintable").css({
               				"width":"100%",
               				"padding":"0px"
               			});
              		}
                   arr1 = costs1;
				   arr2 = costs2;
				   lzName = "住院总金额";
				   document.getElementById("mySelect").options[0].selected = true;
				   $("#style").rmoveLoading ("ida")
                   top1();
               }, error: function () {
                   $.messager.alert("操作提示","网络异常");
               }
           })
           strTime1=$('#time1').val();
		   strTime2=$('#time2').val();
	
       }
function top1(){
		var colors = ['#5793f3', '#d14a61'];
		option = {
			    tooltip: {
			        trigger: 'axis',
			        axisPointer: { // 坐标轴指示器，坐标轴触发有效
			            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
			        }
			    },
			    legend: {
			        data: [time1,time2],
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
			    xAxis: [{
			        type: 'category',
			        data: deptNames
			    }],
			    yAxis: [{
			        type: 'value',
			        name: lzName,
			        axisLabel: {
			            formatter: '{value}'
			        }
			    }],
			    series: [{
			        name: time1,
			        type: 'bar',
					data: arr1,
					barWidth : 30
			    }, {
			        name: time2,
			        type: 'bar',
					data: arr2,
					barWidth : 30
			    }]
			};
		
			var myChart = echarts.init(document.getElementById('tb3chart'));  
    		myChart.setOption(option, true);
    		window.addEventListener("resize", function () {
    	        setTimeout(function () {
    	        	myChart.resize();
    	        }, 300)
    		});
    		$(".jcfx select").change(function(){
    			var num=$(this).val();
    			if(num==1){
					arr1 = costs1;
					arr2 = costs2;
             	    lzName = "住院总金额"
             	    top1();
    			}
    			if(num==2){
    				arr1=tongbi1;
             	   	arr2=tongbi2;
             	    lzName = "住院总金额同比增长%"
             	    top1();
    			}
    		});
    		$("body").on("click",".td2",function(){
    	   		$(this).addClass("hblue").siblings().removeClass("hblue").parents().siblings().children("td").removeClass("hblue");
    	    });
		
}
	
		
	</script>
</head>
<body>
	<div class="main">
		<div id="style" class="table3top" style="width: 90%;height:40%;">
			<h2 id="style2">数据透视表</h2>
			<table id="maintable" class="tableTop">
				<tr class="tr1" id="tr1">
					<td colspan="2" ><p class="fk">住院收入分析</p></td>
					<td colspan="4" class="yellow">
						年月
		                 <input id="time1" class="Wdate" type="text" value="${Etime1}" onClick="WdatePicker({dateFmt:'yyyy-MM',maxDate:'%y-%M',onpicked:pickedFunc1})" />
					</td>
					<td colspan="4" class="yellow">
						年月
						<input id="time2" class="Wdate" type="text" value="${Etime2}" onClick="WdatePicker({dateFmt:'yyyy-MM',maxDate:'%y-%M',onpicked:pickedFunc2})" />
					</td>
					<td colspan="4" class="total dblue">总计</td>
				</tr>
                <tr id= "tr2">
                    <td align="center">下单处室</span></td>
                    <td align="center">下单科室</td>
                    <td align="center">住院药品金额</td>
                    <td align="center">住院非药品金额</td>
                    <td align="center">住院总金额</td>
                    <td align="center">住院总金额同比增长%</td>
                    <td align="center">住院药品金额</td>
                    <td align="center">住院非药品金额</td>
                    <td align="center">住院总金额</td>
                    <td align="center">住院总金额同比增长%</td>
                    <td align="center">住院药品金额</td>
                    <td align="center">住院非药品金额</td>
                    <td align="center">住院总金额</td>
                    <td align="center">住院总金额同比增长%</td>
                </tr>
               	<tr >
	   				<td id="allDep" valign="top" rowspan="1" class="yellow" align="right">全院</td>
	   			</tr>
		</table>
		</div>
		<div class="table3bottom" style="width:90%">
			<h2>决策分析图</h2>
			<div class="jcfx">
				<span>量值</span>
				<select id="mySelect">
					<option value="1">住院总金额</option>
					<option value="2">住院总金额同比增长%</option>
				</select>
			</div>
			<div class="tb3chart">
				<div id="tb3chart" style="height: 350px;width:100%;"></div>
			</div>
		</div>
	</div>
</body>
</html>