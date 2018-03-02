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
<link rel="stylesheet" type="text/css" href="<%=basePath %>themes/system/css/inpatientIncome.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>themes/system/css/outpatientStac.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>门诊住院统计</title>

</head>
<body>
<div class="main" style="overflow:auto;heigth:100%">
<!-- 	<div class="table1top" style="width:90%;"> -->
<!-- 		<h2>分析仪表盘</h2> -->
<!-- 		<div class="ybp" style="width:93%;margin-left:6%"> -->
<!-- 			<div id="ybp1"></div> -->
<!-- 			<div id="ybp2"></div> -->
<!-- 			<div id="ybp3"></div> -->
<!-- 			<div id="ybp4"></div> -->
<!-- 			<div id="ybp5"></div> -->
<!-- 		</div> -->
<!-- 	</div> -->
		<div class="full clearfix" style="width:100%;">
			<div class="col-md-6 tibiaopan" style="height: 320px;"><div id="ybp1"></div></div>
			<div class="col-md-6 tibiaopan" style="height: 320px;"><div id="ybp2"></div></div>
			<div class="col-md-4 tibiaopan" style="height: 320px;"><div id="ybp3"></div></div>
			<div class="col-md-4 tibiaopan" style="height: 320px;"><div id="ybp4"></div></div>
			<div class="col-md-4 tibiaopan" style="height: 320px;"><div id="ybp5"></div></div>
		</div>
		
		
<!-- 	<div class="table1bottom" style="width:90%;"> -->
<!-- 		<div class="tbb1"> -->
<!-- 			<h2 align="center">门诊</h2> -->
<!-- 			<table> -->
<!-- 				<tr> -->
<!-- 					<td colspan='4' align="center"><p class="mz">门诊</p></td> -->
<!-- 				</tr> -->
<!-- 				<tr class="tr1"> -->
<!-- 					<td align="center" width="82">指标分类</td> -->
<!-- 					<td align="center" width="64" class="td1">指标</td> -->
<!-- 					<td align="center" class="td1" width="64">数量</td> -->
<!-- 					<td align="center" class="td1">百分比</td> -->
<!-- 				</tr> -->
<!-- 				<tr id="tr1"> -->
<!-- 					<td id="td1" rowspan="1" class="yellow" vertical-align="middle" align="center"> -->
<!-- 						<span>门诊总数</span> -->
<!-- 					</td> -->
<!-- 					<td class="yellow td1">总计</td> -->
<!-- 					<td align="right" class="yellow td1" id="r1">计算中..</td> -->
<!-- 					<td align="center" class="yellow td1" id="r2">计算中..</td> -->
<!-- 				</tr> -->
				
				
<!-- 				<tr id="tr2"> -->
<!-- 					<td id="td2" rowspan="1" class="yellow" vertical-align="middle" align="center"> -->
<!-- 						<span>其中急诊</span> -->
<!-- 					</td> -->
<!-- 					<td class="yellow td1">总计</td> -->
<!-- 					<td align="right" class="yellow td1" id="r3">计算中..</td> -->
<!-- 					<td align="center" class="yellow td1" id="r4">计算中..</td> -->
<!-- 				</tr> -->
				
<!-- 				<tr> -->
<!-- 					<td colspan="2" class="yellow" vertical-align="middle" align="center"> -->
<!-- 						<span>其中体检</span> -->
<!-- 					</td> -->
<!-- 					<td align="right" class="td2">0</td> -->
<!-- 					<td align="center" class="td2">00.00%</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td colspan="2" class="yellow" vertical-align="middle" align="center"> -->
<!-- 						<span>急诊留观</span> -->
<!-- 					</td> -->
<!-- 					<td align="right" class="td2">0</td> -->
<!-- 					<td align="center" class="td2">00.00%</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td colspan="2" class="yellow" vertical-align="middle" align="center"> -->
<!-- 						<span>急诊入院</span> -->
<!-- 					</td> -->
<!-- 					<td align="right" class="td2">0</td> -->
<!-- 					<td align="center" class="td2">00.00%</td> -->
<!-- 				</tr> -->
<!-- 			</table> -->
<!-- 		</div> -->
<!-- 		<div class="tbb2" style="margin-left:1%"> -->
<!-- 			<h2 align="center">住院</h2> -->
<!-- 			<table> -->
<!-- 				<tr> -->
<!-- 					<td colspan='4' align="center"><p class="mz">住院</p></td> -->
<!-- 				</tr> -->
<!-- 				<tr class="tr1"> -->
<!-- 					<td align="center" width="82">指标分类</td> -->
<!-- 					<td align="center" width="64" class="td1">指标</td> -->
<!-- 					<td align="center" class="td1" width="64">数量</td> -->
<!-- 					<td align="center" class="td1">百分比</td> -->
<!-- 				</tr> -->
<!-- 				<tr id="tr3"> -->
<!-- 					<td id="td3" rowspan="1" class="yellow" vertical-align="middle" align="center"> -->
<!-- 						<span>出院</span> -->
<!-- 					</td> -->
<!-- 					<td class="yellow td1" style="text-indent: 3px;">总计</td> -->
<!-- 					<td align="right" class="yellow td1" id="i1">计算中..</td> -->
<!-- 					<td align="center" class="yellow td1" id="i2">计算中..</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td colspan="2" class="yellow" vertical-align="middle" align="center"> -->
<!-- 						<span>核定床位</span> -->
<!-- 					</td> -->
<!-- 					<td align="right" class="td2" id="bu1">计算中..</td> -->
<!-- 					<td align="center" class="td2" id="bu3">计算中..</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td colspan="2" class="yellow" vertical-align="middle" align="center"> -->
<!-- 						<span>全院展开床位</span> -->
<!-- 					</td> -->
<!-- 					<td align="right" class="td2" id="bu2">计算中..</td> -->
<!-- 					<td align="center" class="td2" id="bu4">计算中..</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td colspan="2" class="yellow" vertical-align="middle" align="center"> -->
<!-- 						<span>手术例数</span> -->
<!-- 					</td> -->
<!-- 					<td align="right" class="td2" id="o1">计算中..</td> -->
<!-- 					<td align="center" class="td2" id=o2>计算中..</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td colspan="2" class="yellow" vertical-align="middle" align="center"> -->
<!-- 						<span>死亡</span> -->
<!-- 					</td> -->
<!-- 					<td align="right" class="td2">0</td> -->
<!-- 					<td align="center" class="td2">00.00%</td> -->
<!-- 				</tr> -->
<!-- 				<tr id="tr4"> -->
<!-- 					<td id="td4" rowspan="1" class="yellow" vertical-align="middle" align="center"> -->
<!-- 						<span>新增住院</span> -->
<!-- 					</td> -->
<!-- 					<td class="yellow td1">总计</td> -->
<!-- 					<td align="right" class="yellow td1" id="i3">计算中..</td> -->
<!-- 					<td align="center" class="yellow td1" id="i4">计算中..</td> -->
<!-- 				</tr> -->
<!-- 			</table> -->
<!-- 		</div> -->
<!-- 		<div class="tbb3"> -->
<!-- 			<h2 align="center">门诊住院总计</h2> -->
<!-- 			<table> -->
<!-- 				<tr> -->
<!-- 					<td colspan='4' align="center"><p class="mz">门诊住院总计</p></td> -->
<!-- 				</tr> -->
<!-- 				<tr class="tr1"> -->
<!-- 					<td align="center" width="112">指标分类</td> -->
<!-- 					<td align="center" width="64" class="td1">指标</td> -->
<!-- 					<td align="center" class="td1" width="90">金额（万元）</td> -->
<!-- 					<td align="center" class="td1">百分比</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td rowspan="2" class="yellow" vertical-align="middle" align="center"> -->
<!-- 						<span>当天门诊住院</span> -->
<!-- 					</td> -->
<!-- 					<td class="yellow td1">门诊计价</td> -->
<!-- 					<td align="right" class="td2" id="a1">计算中..</td> -->
<!-- 					<td align="center" class="td2" id="a2">计算中..</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td class="yellow td1">住院计价</td> -->
<!-- 					<td align="right" class="td2" id="a3">计算中..</td> -->
<!-- 					<td align="center" class="td2" id="a4">计算中..</td> -->
<!-- 				</tr> -->
<!-- 				<tr class="blue1"> -->
<!-- 					<td class="td1" colspan="2">当天门诊住院合计</td> -->
<!-- 					<td align="right" id="a5">计算中..</td> -->
<!-- 					<td align="center">100.00%</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td rowspan="2" class="yellow" vertical-align="middle" align="center"> -->
<!-- 						<span>当月门诊住院</span> -->
<!-- 					</td> -->
<!-- 					<td class="yellow td1">门诊计价</td> -->
<!-- 					<td align="right" class="td2" id="b1">计算中..</td> -->
<!-- 					<td align="center" class="td2" id="b2">计算中..</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td class="yellow td1">住院计价</td> -->
<!-- 					<td align="right" class="td2" id="b3">计算中..</td> -->
<!-- 					<td align="center" class="td2" id="b4">计算中..</td> -->
<!-- 				</tr> -->
<!-- 				<tr class="blue1"> -->
<!-- 					<td class="td1" colspan="2">当月门诊住院合计</td> -->
<!-- 					<td align="right" id="b5">计算中..</td> -->
<!-- 					<td align="center">100.00%</td> -->
<!-- 				</tr>				 -->
<!-- 				<tr> -->
<!-- 					<td rowspan="2" class="yellow" vertical-align="middle" align="center"> -->
<!-- 						<span>当年门诊住院</span> -->
<!-- 					</td> -->
<!-- 					<td class="yellow td1">门诊计价</td> -->
<!-- 					<td align="right" class="td2" id="c1">计算中..</td> -->
<!-- 					<td align="center" class="td2" id="c2">计算中..</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td class="yellow td1">住院计价</td> -->
<!-- 					<td align="right" class="td2" id="c3">计算中..</td> -->
<!-- 					<td align="center" class="td2" id="c4">计算中..</td> -->
<!-- 				</tr> -->
<!-- 				<tr class="blue1"> -->
<!-- 					<td class="td1" colspan="2">当年门诊住院合计</td> -->
<!-- 					<td align="right" id="c5">计算中..</td> -->
<!-- 					<td align="center">100.00%</td> -->
<!-- 				</tr> -->
				
<!-- 				<tr class="blue2"> -->
<!-- 					<td class="td1" colspan="2">总计</td> -->
<!-- 					<td align="right" id="total">计算中..</td> -->
<!-- 					<td align="center">300.00%</td> -->
<!-- 				</tr>			 -->
<!-- 			</table> -->
<!-- 			<input id="dd" type="text" value="" style="display:none"/> -->
<!-- 			<input id="mm" type="text" value="" style="display:none"/> -->
<!-- 			<input id="yy" type="text" value="" style="display:none"/> -->
<!-- 		</div> -->
<!-- 		<div class="tbb4"> -->
<!-- 			<h2>当月门诊住院统计图</h2> -->
<!-- 			<div id="piechart"></div> -->
<!-- 		</div> -->
<!-- 	</div> -->
</div>
</body>
<script type="text/javascript" src="<%=basePath %>javascript/echarts/newChart/echarts-all-3.js"></script>
<script type="text/javascript">


var totalD=0.0;
var totalM=0.0;
var totalY=0.0;
var total=0.0;
var td = false;
var tm = false;
var ty = false;
var tt = false;
$(function(){
	myfun();
	var myChart1 = echarts.init(document.getElementById('ybp1'));  
	 myChart1.setOption(option1, true);
	 var myChart2 = echarts.init(document.getElementById('ybp2'));  
	 myChart2.setOption(option2, true);
	 var myChart3 = echarts.init(document.getElementById('ybp3'));  
	 myChart3.setOption(option3, true);
	 var myChart4 = echarts.init(document.getElementById('ybp4'));  
	 myChart4.setOption(option4, true);
	 var myChart5 = echarts.init(document.getElementById('ybp5'));  
	 myChart5.setOption(option5, true);
// 	 var myChart6 = echarts.init(document.getElementById('piechart'));  
// 	 myChart6.setOption(option6, true);
    window.addEventListener("resize", function () {
        setTimeout(function () {
			myChart1.resize();
            myChart2.resize();
            myChart3.resize();
            myChart4.resize();
            myChart5.resize();
//             myChart6.resize();
        }, 300)
	});		
   /*  $("body").on("click",".td2",function(){
   		$(this).addClass("hblue").siblings().removeClass("hblue").parents().siblings().children("td").removeClass("hblue");
    });
   
   var t1 = window.setInterval(function(){
		if(td&&ty&&tm){
			total=totalD+totalM+totalY;
			html1='<span>'+total.toFixed(4)+'</span>'; 
		 	$("#total").html(html1);
			window.clearInterval(t1); 
		}
   },1000); */ 
})
function myfun(){
	var costArrM = new Array();
	var html1='';
	var html2='';
	var html3='';
	var html4='';
	var html5='';
	var html6='';
	$.ajax({ 
		   url:"<%=basePath%>statistics/outpatientStac/queryOperationApplyVo.action",
		   success:function(data){		       
			   html1='<span>'+data.operationApply+'</span>';
			   html2='<span>'+data.operationApplyPer+'</span>';
			   
			   $("#o1").html(html1);
			   $("#o2").html(html2);
		   },
		   error:function(){
			   $.messager.show({
					title:'提示',
					msg:'数据加载失败,请重新刷新页面!',
					timeout:3000,
					showType:'slide',
					height:150
				});
		   }
	   });
	$.ajax({ 
		   url:"<%=basePath%>statistics/outpatientStac/queryBusinessHospitalbedVo.action",
		   success:function(data){		       
			   html1='<span>'+data.businessHospitalbedTotal+'</span>';
			   html2='<span>'+data.businessHospitalbedTotalOver+'</span>';
			   html3='<span>'+data.bedOrganPer+'</span>';
			   html4='<span>'+data.busTotalOverPer+'</span>';
			   
			   $("#bu1").html(html1);
			   $("#bu2").html(html2);
			   $("#bu3").html(html3);
			   $("#bu4").html(html4);
		   },
		   error:function(){
			   $.messager.show({
					title:'提示',
					msg:'数据加载失败,请重新刷新页面!',
					timeout:3000,
					showType:'slide',
					height:150
				});
		   }
	   });
	
// 	$.ajax({
<%-- 		url:"<%=basePath%>statistics/outpatientStac/queryRegistrationTotal.action", --%>
// 		success:function(data){
// 			option1.series.data[0].value = data.registrationTotal;
// 		    option1.title.text="本日门诊量："+data.registrationTotal;
// 		    var myChart1 = echarts.init(document.getElementById('ybp1'));  
// 		    myChart1.setOption(option1, true);
// 		    myChart1.resize();
// 		}
// 	});
	
	
	
	$.ajax({ 
		   url:"<%=basePath%>statistics/outpatientStac/queryRegistrationVo.action",
		   success:function(data){
			   option1.series.data[0].value = data.registrationTotal;
			   option1.title.text="本日门诊量："+data.registrationTotal;
			   var myChart1 = echarts.init(document.getElementById('ybp1'));  
			   myChart1.setOption(option1, true);
			   myChart1.resize();
			   var list=data.list;
			   $("#td1").attr("rowspan",list.length+1);
			   $("#td2").attr("rowspan",list.length+1);
			   for(var i=0;i<list.length;i++){
		  			 html3='<tr><td class="yellow td1">'+list[i].name+'</td><td align="right" class="td2">'+list[i].mzNum+'</td><td align="center" class="td2">'+list[i].mzPer+'</td></tr>';
		  			 $("#tr1").after(html3);
		  		   }
			   for(var i=0;i<list.length;i++){
		  			 html4='<tr><td class="yellow td1">'+list[i].name+'</td><td align="right" class="td2">'+list[i].jiNum+'</td><td align="center" class="td2">'+list[i].jiPer+'</td></tr>';
		  			 $("#tr2").after(html4);
		  		   }
		       
			   html1='<span>'+data.totalD+'</span>';
			   html2='<span>'+data.dPer+'</span>';
			   html5='<span>'+data.totalJi+'</span>';
			   html6='<span>'+data.jiPer+'</span>';
			   $("#r1").html(html1);
			   $("#r2").html(html2);
			   $("#r3").html(html5);
			   $("#r4").html(html6);
		   },
		   error:function(){
			   $.messager.show({
					title:'提示',
					msg:'数据加载失败,请重新刷新页面!',
					timeout:3000,
					showType:'slide',
					height:150
				});
		   }
	   });
	$.ajax({ 
		   url:"<%=basePath%>statistics/outpatientStac/queryInpatientInfoNowVo.action",
		   success:function(data){
			   var listGo=data.listGo;
// 			   var newList=data.newList;
			   $("#td3").attr("rowspan",listGo.length+1);
			   $("#td4").attr("rowspan",listGo.length+1);
			   for(var i=0;i<listGo.length;i++){
		  			 html4='<tr><td class="yellow td1">'+listGo[i].name+'</td><td align="right" class="td2">'+listGo[i].jiNum+'</td><td align="center" class="td2">'+listGo[i].jiPer+'</td></tr>';
		  			 $("#tr3").after(html4);
		  		   }
			   for(var i=0;i<listGo.length;i++){
		  			 html5='<tr><td class="yellow td1">'+listGo[i].name+'</td><td align="right" class="td2">'+listGo[i].mzNum+'</td><td align="center" class="td2">'+listGo[i].mzPer+'</td></tr>';
		  			 $("#tr4").after(html5);
		  		   }
			   option2.series.data[0].value = data.inpatientInfoNowTotal;
			   option2.title.text="当前在院人数："+data.inpatientInfoNowTotal;
			   var myChart2 = echarts.init(document.getElementById('ybp2'));  
			   myChart2.setOption(option2, true);
			   myChart2.resize();
		       
			   html1='<span>'+data.goTotals+'</span>';
			   html2='<span>'+data.goPer+'</span>';
			   html3='<span>'+data.newTotals+'</span>';
			   html6='<span>'+data.newPer+'</span>';
			   
			   $("#i1").html(html1);
			   $("#i2").html(html2);
			   $("#i3").html(html3);
			   $("#i4").html(html6);
		   },
		   error:function(){
			   $.messager.show({
					title:'提示',
					msg:'数据加载失败,请重新刷新页面!',
					timeout:3000,
					showType:'slide',
					height:150
				});
		   }
	   });
	$.ajax({ 
		   url:"<%=basePath%>statistics/outpatientStac/queryOutpatientStacVoD.action",
		   success:function(data){
			   td = true;
			   option3.series.data[0].value = data.outCostD;
			   option3.title.text="当日门诊实收："+data.outCostD+"万元";
			   option4.series.data[0].value=data.inpCostD;
			   option4.title.text="当日住院实收："+data.inpCostD+"万元";
			   option5.series.data[0].value=data.costsD;
			   option5.title.text="当日实收："+data.costsD.toFixed(4)+"万元";
			   
			   var myChart3 = echarts.init(document.getElementById('ybp3'));  
			   myChart3.setOption(option3, true);
			   var myChart4 = echarts.init(document.getElementById('ybp4'));  
			   myChart4.setOption(option4, true);
			   var myChart5 = echarts.init(document.getElementById('ybp5'));  
			   myChart5.setOption(option5, true);
			   myChart3.resize();
		       myChart4.resize();
		       myChart5.resize();
		       
			   html1='<span>'+data.outCostD+'</span>';
			   html2='<span>'+data.outCostPerD+'</span>';
			   html3='<span>'+data.inpCostD+'</span>';
			   html4='<span>'+data.inpCostPerD+'</span>';
			   html5='<span>'+data.costsD.toFixed(4)+'</span>';
			   
			   $("#a1").html(html1);
			   $("#a2").html(html2);
			   $("#a3").html(html3);
			   $("#a4").html(html4);
			   $("#a5").html(html5);
			   
			   totalD=data.costsD;
			   
		   },
		   error:function(){
			   $.messager.show({
					title:'提示',
					msg:'数据加载失败,请重新刷新页面!',
					timeout:3000,
					showType:'slide',
					height:150
				});
		   }
	   });
//     $.ajax({ 
<%-- 	   url:"<%=basePath%>statistics/outpatientStac/queryOutpatientStacVoM.action", --%>
// 	   success:function(data){
// 		   tm = true;
// 		   costArrM[costArrM.length] = data.outCostM;
// 		   costArrM[costArrM.length] = data.inpCostM;
// 		   var val = option6.series[0].data;
// 		   for(var i=0;i<val.length;i++){
// 			   val[i].value = costArrM[i]
// 		   }
// 		   var myChart6 = echarts.init(document.getElementById('piechart'));  
// 		   myChart6.setOption(option6, true);
// 		   myChart6.resize();
// 		   html1='<span>'+data.outCostM+'</span>';
// 		   html2='<span>'+data.outCostPerM+'</span>';
// 		   html3='<span>'+data.inpCostM+'</span>';
// 		   html4='<span>'+data.inpCostPerM+'</span>';
// 		   html5='<span>'+data.costsM.toFixed(4)+'</span>';
		   
// 		   $("#b1").html(html1);
// 		   $("#b2").html(html2);
// 		   $("#b3").html(html3);
// 		   $("#b4").html(html4);
// 		   $("#b5").html(html5);
		   
// 		   totalM=data.costsM;
// 	   },
// 	   error:function(){
// 		   $.messager.show({
// 				title:'提示',
// 				msg:'数据加载失败,请重新刷新页面!',
// 				timeout:3000,
// 				showType:'slide',
// 				height:150
// 			});
// 	   }
//    });
    $.ajax({ 
 	   url:"<%=basePath%>statistics/outpatientStac/queryOutpatientStacVoY.action",
 	   success:function(data){
 		   ty = true;
 		   html1='<span>'+data.outCostY+'</span>';
		   html2='<span>'+data.outCostPerY+'</span>';
		   html3='<span>'+data.inpCostY+'</span>';
		   html4='<span>'+data.inpCostPerY+'</span>';
		   html5='<span>'+data.costsY.toFixed(4)+'</span>';
		   
		   $("#c1").html(html1);
		   $("#c2").html(html2);
		   $("#c3").html(html3);
		   $("#c4").html(html4);
		   $("#c5").html(html5);
		   
		   totalY=data.costsY;
 	   },
 	  error:function(){
 		 $.messager.show({
				title:'提示',
				msg:'数据加载失败,请重新刷新页面!',
				timeout:3000,
				showType:'slide',
				height:150
			});
	   }
    });
    
}    

var option1 = {
		title : {
			text: '本日门诊量：计算中..',
			show: true,
			textStyle: {
				fontSize: 16,
				fontWeight: 'bolder',
				color: '#000',
				fontFamily: '黑体'
			}
		},
		series: {
			min: 0,
	        max: 1000,
			name: '总诊疗人次',
			type: 'gauge',
			startAngle: 285,
			endAngle: 80,
			data: [{value:0, name: ''}],
			splitNumber: 4,
			center: ['42%', '52%'],
			pointer: {
				length : '80%',
				width : 6,
				color : 'auto'
			},
			axisLine: {
				show: true,
				lineStyle: {
					color: [
					[0.2, '#228b22'],
					[0.8, '#48b'],
					[1, '#ff4500']
					],
					width: 15
				}
			},
			detail: {
				show: false,
				textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                fontSize: '16',
	                color: "#000",
	                fontWeight: "bolder"
	            }
			},
		}
	};
var option2 = {
		title : {
			text: '当前在院人数：计算中..',
			show: true,
			textStyle: {
				fontSize: 16,
				fontWeight: 'bolder',
				color: '#000',
				fontFamily: '黑体'
			}
		},
		series: {
			min: 0,
	        max: 20000,
			name: '总诊疗人次',
			type: 'gauge',
			startAngle: 285,
			endAngle: 80,
			data: [{value:0, name: ''}],
			splitNumber: 4,
			center: ['42%', '52%'],
			pointer: {
				length : '80%',
				width : 6,
				color : 'auto'
			},
			axisLine: {
				show: true,
				lineStyle: {
					color: [
					[0.2, '#228b22'],
					[0.8, '#48b'],
					[1, '#ff4500']
					],
					width: 15
				}
			},
			detail: {
				show: false,
				textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                fontSize: '16',
	                color: "#000",
	                fontWeight: "bolder"
	            }
			},
		}
	};
var	option3 = {
		title : {
			text: '当日门诊实收：计算中..',
			show: true,
			textStyle: {
				fontSize: 16,
				fontWeight: 'bolder',
				color: '#000',
				fontFamily: '黑体'
			}
		},
		series: {
			min: 0,
	        max: 100,
			name: '总诊疗人次',
			type: 'gauge',
			startAngle: 285,
			endAngle: 80,
			data: [{value:0, name: ''}],
			splitNumber: 4,
			center: ['42%', '52%'],
			pointer: {
				length : '80%',
				width : 6,
				color : 'auto'
			},
			axisLine: {
				show: true,
				lineStyle: {
					width: 15
				}
			},
			detail: {
				show: false,
				textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                fontSize: '16',
	                color: "#000",
	                fontWeight: "bolder"
	            }
			},
		}
	};
var	option4 = {
		title : {
			text: '当日住院实收：计算中..',
			show: true,
			textStyle: {
				fontSize: 16,
				fontWeight: 'bolder',
				color: '#000',
				fontFamily: '黑体'
			}
		},
		series: {
			min: 0,
	        max: 100,
			name: '总诊疗人次',
			type: 'gauge',
			startAngle: 285,
			endAngle: 80,
			data: [{value:0, name: ''}],
			splitNumber: 4,
			center: ['42%', '52%'],
			pointer: {
				length : '80%',
				width : 6,
				color : 'auto'
			},
			axisLine: {
				show: true,
				lineStyle: {
					width: 15
				}
			},
			detail: {
				show: false,
				textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                fontSize: '16',
	                color: "#000",
	                fontWeight: "bolder"
	            }
			},
		}
	};
var	option5 = {
		title : {
			text: '当日实收：计算中..',
			show: true,
			textStyle: {
				fontSize: 16,
				fontWeight: 'bolder',
				color: '#000',
				fontFamily: '黑体'
			}
		},
		series: {
			min: 0,
	        max: 100,
			name: '总诊疗人次',
			type: 'gauge',
			startAngle: 285,
			endAngle: 80,
			data: [{value:0, name: ''}],
			splitNumber: 4,
			center: ['42%', '52%'],
			pointer: {
				length : '80%',
				width : 6,
				color : 'auto'
			},
			axisLine: {
				show: true,
				lineStyle: {
					width: 15
				}
			},
			detail: {
				show: false,
				textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                fontSize: '16',
	                color: "#000",
	                fontWeight: "bolder"
	            }
			},
		}
	};
var option6 = {
		
		tooltip : {
	       trigger: 'item',
	       formatter: "{a} <br/>{b} : {c} ({d}%)"
	   	},
	    legend: {
	        orient: 'vertical',
	        x: 'left',
	        data:['门诊计价','住院计价']
	    },
	    calculable : true,
	    series: [
	        {
	            name:'数据统计',
	            type:'pie',
	            radius: ['30%', '80%'],
	            avoidLabelOverlap: false,
	            startAngle:-20,
	            label: {
	            	 normal: {
	                    position: 'inner',
	                    textStyle: {
	            			color: '#000'
	            		}
	                }
	            },
	            labelLine: {
	                normal: {
	                    show: false
	                }
	            },
	            data:[
	                {value:0, name:'门诊计价'},
	                {value:0, name:'住院计价'},
	            ],
	            itemStyle:{ 
                    normal:{ 
                        label:{ 
                           show: true, 
                           formatter: "{b} : {c}万元 ({d}%)" 
                        }, 
                        labelLine :{show:true}
                    } 
                }
	        }
	    ],
	    color: ['#f2e55b','#e79df9']
	}; 
</script>
</html>