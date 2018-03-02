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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="<%=basePath %>javascript/js/kpi.js"></script>
<script type="text/javascript" src="<%=basePath %>javascript/echarts/newChart/echarts-all-3.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath %>themes/system/css/kpi.css">
<script type="text/javascript">
    var danw ='';
    var time ='';
    var jsonArr = [];
    var compare = [];
    var month =[];
    var monthCom =[];
    var result =[];
    var startDate="${Etime}";
	var dateFor=startDate.split('-');
	$('#time1').val(dateFor[0]);
	$('#time2').val(dateFor[0]+'-'+dateFor[1]);
	$(function(){
		//查询每天门急诊人次
		danw = $('#danw').val();
		time = $('#time1').val();
		$(".danw").change(function(){
			var num=$(this).val();
			danw = $('#danw').val();
			if(num==0){
				$('#time1').show();
				$('#time2').hide();
				$('#time1').val(dateFor[0]);
				time = $('#time1').val();
			}
			if(num==1){
				$('#time2').show();
				$('#time1').hide();
				$('#time2').val(dateFor[0]+'-'+dateFor[1]);
				time = $('#time2').val();
			}
			for(var i=0;i<jsonArr.length;i++){
				$(".s"+(i+1)).text('加载中...');
			}
			for(var i=0;i<compare.length;i++){
				$(".qw"+(i+1)).text('加载中...');
			}
			var t1 = window.setInterval(function(){
	    		show(danw);
				window.clearInterval(t1); 
	   		},1000);
		});
		$(".clickchange").click(function() {
			$(this).addClass("hblue").parents().siblings().children(".clickchange").removeClass("hblue");
		});
		var myChart1 = echarts.init(document.getElementById('tbleft_chart')); 
	    myChart1.setOption(option1, true);   //为echarts对象加载数据
	    var myChart2 = echarts.init(document.getElementById('tbmid_chart'));  
	    myChart2.setOption(option2, true);   //为echarts对象加载数据
	    var myChart3 = echarts.init(document.getElementById('tbright_chart1'));  
	    myChart3.setOption(option3, true);   //为echarts对象加载数据
	    var myChart4 = echarts.init(document.getElementById('tbright_chart2'));  
	    myChart4.setOption(option4, true);   //为echarts对象加载数据
	    var myChart5 = echarts.init(document.getElementById('qs1')); 
	    myChart5.setOption(option5, true);   //为echarts对象加载数据
	    var myChart6 = echarts.init(document.getElementById('qs2'));  
	    myChart6.setOption(option6, true);   //为echarts对象加载数据
	    var myChart7 = echarts.init(document.getElementById('qs3'));  
	    myChart7.setOption(option7, true);   //为echarts对象加载数据
	    var myChart11 = echarts.init(document.getElementById('qs7'));  
	    myChart11.setOption(option11, true);   //为echarts对象加载数据
	    var myChart13 = echarts.init(document.getElementById('qs9'));  
	    myChart13.setOption(option13, true);   //为echarts对象加载数据
	    var myChart14 = echarts.init(document.getElementById('zx1'));  
	    myChart14.setOption(option14, true);   //为echarts对象加载数据
	    var myChart15 = echarts.init(document.getElementById('zx2'));  
	    myChart15.setOption(option15, true);   //为echarts对象加载数据
	    var myChart16 = echarts.init(document.getElementById('zx3'));  
	    myChart16.setOption(option16, true);   //为echarts对象加载数据
	    var myChart20 = echarts.init(document.getElementById('zx7'));  
	    myChart20.setOption(option20, true);   //为echarts对象加载数据
	    var myChart22 = echarts.init(document.getElementById('zx9'));  
	    myChart22.setOption(option22, true);   //为echarts对象加载数据
	    window.addEventListener("resize", function () {
	        setTimeout(function () {
	        	myChart1.resize();
	        	myChart2.resize();
	        	myChart3.resize();
	        	myChart4.resize();
	        }, 300)
		});
	    var t1 = window.setInterval(function(){
	    		show(danw);
				window.clearInterval(t1); 
	   },1000);
	})
	function pickedFunc1(){
		time = $('#time1').val();
		for(var i=0;i<jsonArr.length;i++){
			$(".s"+(i+1)).text('加载中...');
		}
		for(var i=0;i<compare.length;i++){
			$(".qw"+(i+1)).text('加载中...');
		}
		 var t1 = window.setInterval(function(){
    		show(danw);
				window.clearInterval(t1); 
	   },1000);
	}
	function pickedFunc2(){
		time = $('#time2').val();
		for(var i=0;i<jsonArr.length;i++){
			$(".s"+(i+1)).text('加载中...');
		}
		for(var i=0;i<compare.length;i++){
			$(".qw"+(i+1)).text('加载中...');
		}
		 var t1 = window.setInterval(function(){
    		show(danw);
				window.clearInterval(t1); 
	   },1000);
	}
	function css1(){
		$(".clickchange").click(function() {
			$(this).addClass("hblue").parents().siblings().children(".clickchange").removeClass("hblue");
		});
			
			var myChart1 = echarts.init(document.getElementById('tbleft_chart')); 
		    myChart1.setOption(option1, true);   //为echarts对象加载数据
		    option1.series[0].data[0].value = jsonArr;
		    var myChart2 = echarts.init(document.getElementById('tbmid_chart'));  
		    option2.series[0].data = jsonArr;
		    myChart2.setOption(option2, true);   //为echarts对象加载数据
		    var myChart3 = echarts.init(document.getElementById('tbright_chart1'));
		    option3.series[0].data = $("#zzlrc").text();
		    option3.series[0].max = (Number($("#zzlrc").text()==0?200:$("#zzlrc").text())/0.8).toFixed(0);
		    myChart3.setOption(option3, true);   //为echarts对象加载数据
		    var myChart4 = echarts.init(document.getElementById('tbright_chart2'));
		    option4.series[0].data = $("#mzzls").text();
		    option4.series[0].max = (Number($("#mzzls").text()==0?200:$("#mzzls").text())/0.8).toFixed(0);
		    myChart4.setOption(option4, true);   //为echarts对象加载数据
		    var myChart5 = echarts.init(document.getElementById('qs1')); 
		    option5.series[0].data = month[0];
		    myChart5.setOption(option5, true);   //为echarts对象加载数据
		    var myChart6 = echarts.init(document.getElementById('qs2'));  
		    option6.series[0].data = month[1];
		    myChart6.setOption(option6, true);   //为echarts对象加载数据
		    var myChart7 = echarts.init(document.getElementById('qs3'));  
		    option7.series[0].data = month[2];
		    myChart7.setOption(option7, true);   //为echarts对象加载数据
		    var myChart11 = echarts.init(document.getElementById('qs7'));  
		    option11.series[0].data = month[3];
		    myChart11.setOption(option11, true);   //为echarts对象加载数据
		    var myChart13 = echarts.init(document.getElementById('qs9')); 
		    option13.series[0].data = month[4];
		    myChart13.setOption(option13, true);   //为echarts对象加载数据
		    var myChart14 = echarts.init(document.getElementById('zx1')); 
		    option14.series[0].data = result[0];
		    myChart14.setOption(option14, true);   //为echarts对象加载数据
		    var myChart15 = echarts.init(document.getElementById('zx2'));
		    option15.series[0].data = result[0];
		    myChart15.setOption(option15, true);   //为echarts对象加载数据
		    var myChart16 = echarts.init(document.getElementById('zx3')); 
		    option16.series[0].data = result[0];
		    myChart16.setOption(option16, true);   //为echarts对象加载数据
		    var myChart20 = echarts.init(document.getElementById('zx7')); 
		    option20.series[0].data = result[3];
		    myChart20.setOption(option20, true);   //为echarts对象加载数据
		    var myChart22 = echarts.init(document.getElementById('zx9'));
		    option22.series[0].data = result[4];
		    myChart22.setOption(option22, true);   //为echarts对象加载数据
		    window.addEventListener("resize", function () {
		        setTimeout(function () {
		        	myChart1.resize();
		        	myChart2.resize();
		        	myChart3.resize();
		        	myChart4.resize();
		        }, 300)
			});
	}
	function css(){
		for(var i=0;i<jsonArr.length;i++){
			var w=jsonArr[i]/1000*100;
			$(".w"+(i+1)).css("width",w+"%");
			$(".s"+(i+1)).text(jsonArr[i]);
		}
		for(var j=0;j<jsonArr.length;j++){
			var t=jsonArr[j]/1000*100;
			$(".t"+(j+1)).css("width",t+"%");
			$(".s"+(j+1)).text(jsonArr[j]);
		}
		for(var j=0;j<compare.length;j++){
			$(".qw"+(j+1)).text(compare[j]);
		}
	}
	function chooseTime(dane,time){
		$.ajax({
			url:"<%=basePath%>statistics/kpi/queryAllData.action",
			data:{danw:danw,time:time},
		    async:true,
		    type:'post',
			success: function(backData) {
				console.info(backData);
				console.info(backData.increase);
			    jsonArr[0] = backData.measure.totalNum;
			    jsonArr[1] = backData.measure.outNum;
			    jsonArr[2] = backData.measure.zjNum;
			    jsonArr[3] = backData.measure.jzShNum;
			    jsonArr[4] = backData.measure.jzZlNum;
			    compare[0] = backData.increase.totalNum;
			    compare[1] = backData.increase.outNum;
			    compare[2] = backData.increase.zjNum;
			    compare[3] = backData.increase.jzShNum;
			    compare[4] = backData.increase.jzZlNum;
			    month[0] =backData.totalNumArr;
			    month[1] =backData.outNumArr;
			    month[2] =backData.zjNumArr;
			    month[3] =backData.jzShNumArr;
			    month[4] =backData.jzZlNumArr;
			    result[0] =backData.totalNumArred;
			    result[1] =backData.outNumArred;
			    result[2] =backData.zjNumArred;
			    result[3] =backData.jzShNumArred;
			    result[4] =backData.jzZlNumArred;
			    css();
		   		css1();
			}
		})
		
	}
	function choose(dane,time){
		var init ='';
		$.ajax({
			url:"<%=basePath%>statistics/kpi/queryKPT.action",
			data:{danw:danw,time:time},
		    async:true,
		    type:'post',
			success: function(data) {
				init = data;
				var jsonObj =  eval(init);
		   		for(var i =0;i < jsonObj.length;i++){
		           jsonArr[i] = jsonObj[i];
		  		}
		   		var init1 ='';
				$.ajax({
					url:"<%=basePath%>statistics/kpi/compareToBefore.action",
					data:{danw:danw,time:time},
				    async:true,
				    type:'post',
					success: function(data) {
						init1 = data;
						var jsonObj =  eval(init1);
				   		for(var i =0;i < jsonObj.length;i++){
				   			compare[i] = jsonObj[i];
				  		}
				   		css();
				   		css1();
					}
				});
			}
		});
	}
	function show(danw){
// 		choose(danw,time);
		chooseTime(danw,time);
// 		var com = compareToBefore(danw,time);
// 		var jsonObj =  eval(com);
//    		for(var i =0;i < jsonObj.length;i++){
//    			compare[i] = jsonObj[i];
//   		}
//    		var monthCom = everMonthToCom(danw,time);
   		
// 		var jsonObj =  eval(monthCom);
//    		for(var i =0;i < jsonObj.length;i++){
//    			monthCom[i] = jsonObj[i];
//   		}
//    		var mod =[];
//    		var mod1 =[];
//    		var mod2 =[];
//    		var mod3 =[];
//    		var mod4 =[];
//    		for(var j=0;j<12;j++){
// 			mod[j] = month[0][j] - monthCom[0][j];
// 			mod1[j] = month[1][j] - monthCom[1][j];
// 			mod2[j] = month[2][j] - monthCom[2][j];
// 			mod3[j] = month[3][j] - monthCom[3][j];
// 			mod4[j] = month[4][j] - monthCom[4][j];
// 		}
//    		result[0] = mod;
//    		result[1] = mod1;
//    		result[2] = mod2;
//    		result[3] = mod3;
//    		result[4] = mod4;
//     	css1();

	}
	function everyMonth(dane,time){
		var init ='';
		$.ajax({
			url:"<%=basePath%>statistics/kpi/queryEverMonth.action",
			data:{danw:danw,time:time},
		    async:false,
		    type:'post',
			success: function(data) {
				init = data;
			}
		});
		return init;
	}
	function everMonthToCom(dane,time){
		var init ='';
		$.ajax({
			url:"<%=basePath%>statistics/kpi/everMonthToCom.action",
			data:{danw:danw,time:time},
		    async:false,
		    type:'post',
			success: function(data) {
				init = data;
			}
		});
		return init;
	}
	
	</script>
	<style>
		.main{
			overflow:auto;
		}
		.qs{
			width:90%;
		}
		#qs1, #qs2, #qs3, #qs4, #qs5, #qs6, #qs7, #qs8, #qs9{
			width:100%;
		}
		.tbright .dlz i{
			margin: 2% 3% 0 33%;
		}
</style>
	</style>
</head>
<body>
<div class="main easyui-layout" data-options="fit:true">
	<div class="table2top" style="width:90%">
		<div class="topleft" style="width:69%">
			<h2 style="width:99%">门诊KPI</h2>
			<div class="top_se">
				<select class="danw" id="danw">
					<option value="0">年</option>
					<option value="1">月</option>
				</select>

				<input id="time1" class="Wdate" type="text" value="${year}" onClick="WdatePicker({dateFmt:'yyyy',onpicked:pickedFunc1,maxDate:'%y'})" style="width:100px !important;height:22px"/>
				
				<input id="time2" class="Wdate" style="display: none;width:100px !important;height:22px";"type="text"  value="${month}" onClick="WdatePicker({dateFmt:'yyyy-MM',onpicked:pickedFunc2,maxDate:'%y-%M'})" />
			</div>
			<div class="table">
				<table cellspacing="0" cellpadding="0" border="0"  style="width:99%">
					<tr class="tr1">
						<td align="center" width="20%" class="reportname">报表名称
						</td>
						<td align="center" width="12%" class="td2">汇编量值</td>
						<td align="center" width="25%">度量值</td>
						<td align="center" width="15%">趋势变化</td>
						<td align="center" width="15%">比去年增加/减少</td>
						<td align="center">增减变化趋势</td>
					</tr>
					<tr>
						<td rowspan="9" class="td1" style="border-left:1px solid #B0ADAD;" valign="top">医院工作报表(门诊部分)
						</td>
						<td class="td1 td2">总诊疗人次</td>
						<td align="right" class="blue"><span style="line-height: 2;" id="zzlrc" class="gray s1">加载中..</span><div class="dlzchart"><div class="gray w1"></div></div></td>
						<td><div class="qs"><div id="qs1"></div></div></td>
						<td align="right" class="blue clickchange"><span class="gray qw1">加载中..</span><i></i></td>
						<td><div class="zx"><div id="zx1"></div></div></td>
					</tr>
					<tr>
						<td class="td1 td2">门诊诊疗数</td>
						<td align="right" class="blue"><span style="line-height: 2;" id="mzzls"  class="gray s2">加载中..</span><div class="dlzchart"><div class="gray w2"></div></div></td>
						<td><div class="qs"><div id="qs2"></div></div></td>
						<td align="right" class="blue clickchange"><span class="gray qw2">加载中..</span><i></i></td>
						<td><div class="zx"><div id="zx2"></div></div></td>
					</tr>
					<tr>
						<td class="td1 td2">专家门诊</td>
						<td align="right" class="blue"><span style="line-height: 2;" class="gray s3">加载中..</span><div class="dlzchart"><div class="gray w3"></div></div></td>
						<td><div class="qs"><div id="qs3"></div></div></td>
						<td align="right" class="blue clickchange"><span class="gray qw3">加载中..</span><i></i></td>
						<td><div class="zx"><div id="zx3"></div></div></td>
					</tr>
<!-- 					<tr> -->
<!-- 						<td class="td1 td2">成功人次数</td> -->
<!-- 						<td align="right" class="blue">1,165<div class="dlzchart"><div class="gray w7"></div></div></td> -->
<!-- 						<td><div class="qs"><div id="qs4"></div></div></td> -->
<!-- 						<td align="right" class="blue clickchange">1165<i></i></td> -->
<!-- 						<td><div class="zx"><div id="zx4"></div></div></td> -->
<!-- 					</tr> -->
<!-- 					<tr> -->
<!-- 						<td class="td1 td2">观察室收容</td> -->
<!-- 						<td align="right" class="blue">28<div class="dlzchart"><div class="gray w9"></div></div></td> -->
<!-- 						<td><div class="qs"><div id="qs5"></div></div></td> -->
<!-- 						<td align="right" class="blue clickchange">28<i></i></td> -->
<!-- 						<td><div class="zx"><div id="zx5"></div></div></td> -->
<!-- 					</tr> -->
<!-- 					<tr> -->
<!-- 						<td class="td1 td2">观察室死亡</td> -->
<!-- 						<td align="right" class="blue">0<div class="dlzchart"><div class="gray w6"></div></div></td> -->
<!-- 						<td><div class="qs"><div id="qs6"></div></div></td> -->
<!-- 						<td align="right" class="blue clickchange">0<i></i></td> -->
<!-- 						<td><div class="zx"><div id="zx6"></div></div></td> -->
<!-- 					</tr> -->
					<tr>
						<td class="td1 td2">急诊手术例</td>
						<td align="right" class="blue"><span style="line-height: 2;" class="gray s4">加载中..</span><div class="dlzchart"><div class="gray w4"></div></div></td>
						<td><div class="qs"><div id="qs7"></div></div></td>
						<td align="right" class="blue clickchange"><span class="gray qw4">加载中..</span><i></i></td>
						<td><div class="zx"><div id="zx7"></div></div></td>
					</tr>
<!-- 					<tr> -->
<!-- 						<td class="td1 td2">急诊危抢救</td> -->
<!-- 						<td align="right" class="blue">2,546<div class="dlzchart"><div class="gray w8"></div></div></td> -->
<!-- 						<td><div class="qs"><div id="qs8"></div></div></td> -->
<!-- 						<td align="right" class="blue clickchange">2546<i></i></td> -->
<!-- 						<td><div class="zx"><div id="zx8"></div></div></td> -->
<!-- 					</tr> -->
					<tr>
						<td class="td1 td2">急诊诊疗数</td>
						<td align="right" class="blue"><span style="line-height: 2;" class="gray s5">加载中..</span><div class="dlzchart"><div class="gray w5"></div></div></td>
						<td><div class="qs"><div id="qs9"></div></div></td>
						<td align="right" class="blue clickchange"><span class="gray qw5">加载中..</span><i></i></td>
						<td><div class="zx"><div id="zx9"></div></div></td>
					</tr>
				</table>
			</div>
		</div>
		<div class="topright" style="width:31%">
			<h2 style="width:99%">KPI排序</h2>
			<table class="table2" cellspacing="0" cellpadding="0" border="0" style="width:99%;">
				<tr class="tr1">
					<td align="center" width="30%"><i></i>汇编量值</td>
					<td align="center">度量值</td>
				</tr>
				<tr>
					<td class="td1"><i></i>总诊疗人次</td>
					<td align="right" class="trchart"><div class="tcsq t1"></div><span class="gray s1"></span></td>
				</tr>
				<tr>
					<td class="td1"><i></i>门诊诊疗数</td>
					<td align="right" class="trchart"><div class="tcsq t2"></div><span class="gray s2"></span></td>
				</tr>
				<tr>
					<td class="td1"><i></i>专家门诊</td>
					<td align="right" class="trchart"><div class="tcsq t3"></div><span class="gray s3"></span></td>
				</tr>
				<tr>
					<td class="td1"><i></i>急诊诊疗数</td>
					<td align="right" class="trchart"><div class="tcsq t5"></div><span class="gray s5"></span></td>
				</tr>
<!-- 				<tr> -->
<!-- 					<td class="td1"><i></i门急诊</td> -->
<!-- 					<td align="right" class="trchart"><div class="tcsq t9"></div><span id="t9" >88,609</span></td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td class="td1"><i></i>健康检查数</td> -->
<!-- 					<td align="right" class="trchart"><div class="tcsq t6"></div><span>29,160</span></td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td class="td1"><i></i>观察室死亡</td> -->
<!-- 					<td align="right" class="trchart"><div class="tcsq t7"></div><span>0</span></td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td class="td1"><i></i>出车</td> -->
<!-- 					<td align="right" class="trchart"><div class="tcsq t8"></div><span>19,170</span></td> -->
<!-- 				</tr> -->
				<tr>
					<td class="td1"><i></i>急诊手术例</td>
					<td align="right" class="trchart"><div class="tcsq t4"></div><span class="gray s4"></span></td>
				</tr>
<!-- 				<tr> -->
<!-- 					<td class="td1"><i></i>急诊危抢救</td> -->
<!-- 					<td align="right" class="trchart"><div class="tcsq t10"></div><span>14,034</span></td> -->
<!-- 				</tr> -->
			</table>
		</div>
	</div>
	<div class="table2bottom" style="width:90%">
		<div class="tbleft" style="width:33%">
			<h2>KPI雷达</h2>
			<div id="tbleft_chart" style="height:300px;width:90%;margin:0 auto;"></div>
		</div>
		<div class="tbmid" style="width:33%;margin-left:0.5%;">
			<h2>KPI变化</h2>
			<div class="tbmid_c">
				<div id="tbmid_chart" style="height:300px;font-size: 15px;"></div>
				<p>汇编量值</p>
			</div>
		</div>
		<div class="tbright" style="width:33%">
			<h2>总医疗人次/门诊诊疗数</h2>
			<div class="tbright_chart" style="width:100%;height:250px;position:static;">
				<div id="tbright_chart1" style="width:45%;margin-left:10%"></div>
				<div id="tbright_chart2" style="width:45%;position:static;"></div>
			</div>
			<div class="dlz">
				<div style="width:50%;"><i></i>度量值<br /><span id="tbright_data1" class="gray s1">加载中...</span></div>
				<div style="width:50%;"><i></i>度量值<br /><span id="tbright_data2" class="gray s2">加载中...</span></div>
			</div>
		</div>
	</div>
</div>
</body>
</html>