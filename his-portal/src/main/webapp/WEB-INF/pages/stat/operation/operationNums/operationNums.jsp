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
		<meta charset="utf-8" />
		<title>手术例数统计</title>
		<style type="text/css">
			html,body{
				width: 100%;
				height: 115%;
			}
			html{
			 overflow-y:scroll;
			}
			*{
				padding: 0;
				margin: 0;
			}
			li{
			    list-style-type:none; 
				width: 25%;
				height: 100%;
				float: left;
				box-sizing:border-box;
				border-right: 1px solid #79C2F5;
			}
			li>div {
				height: 100%;
			}
			ul:nth-child(1){
				width: 100%;
				height: 5%;
			}
			ul:nth-child(2){
				width: 100%;
				height: 13%;
			}
			ul:nth-child(3){
				width: 100%;
				height: 30%;
			}
			ul:nth-child(4){
				width: 100%;
				height: 52%;
			}
		   .tableCss{
				border-collapse: collapse;border-spacing: 0;border: 1px solid #95b8e7;width:100%;
			}
			.tableCss .TDlabel{
			 	text-align: center;
				font-size:14px;
			}
			.TDlabel1{
				background: #E0ECFF;	
			 	text-align: left;
				font-size:20px;
			}
			.tableCss td{
				text-align: center;
				padding: 5px 5px;
				word-break: keep-all;
				white-space:nowrap;
				width: 143px;
			}
		</style>
		<script type="text/javascript" src="<%=basePath %>javascript/echarts/newChart/echarts-all-3.js"></script>
	</head>
	<body>
		<ul>
			<li >
				<table style="padding-top: 10px;margin-left: 20px;">
				<tr>
					<td >当日:<span id="dayTime"></span></td>
					<td >（截止时间:<span id="dayTimeStop"></span>）</td>
				</tr>
				</table>
			</li>
			<li>
				<table style="padding-top: 10px;margin-left: 20px;">
					<tr>
						<td >日:
							<input id="time2" value="${yesterDayTime}" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d',onpicked:queryYesterday})" />
						</td>
					</tr>
				</table>
			</li>
			<li>
				<table  style="padding-top: 10px;margin-left: 20px;">
					<tr>
						<td  >
							月:
			                 <input id="time3" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM',maxDate:'%y-%M',onpicked:queryMonth})" />
						</td>
					</tr>
				</table>
			</li>
			<li>
				<table  style="padding-top: 10px;margin-left: 20px;">
					<tr>
						<td  >
							年:
							<input id="time4" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy',onpicked:queryYear})"  type="text" />
						</td>
					</tr>
				</table>
			</li>
		</ul>
		<ul>
			<li>
				<table id="maintable1"  class="tableCss" style="width: 90%;border: 1px;margin-left: 20px;">
	                <tr class="TDlabel">
	                	<td align="center"></td>
	                    <td align="center">总例数</td>
	                    <td align="center">河医院区</td>
	                    <td align="center">郑东院区</td>
	                    <td align="center">惠济院区</td>
	                </tr>
	                <tr >
	                	<td align="center" class='TDlabel' >完成手术</td>
	                    <td align="center" id="toDayTotalwc">-</td>
	                    <td align="center" id="toDayhywc">-</td>
	                    <td align="center" id="toDayzdwc">-</td>
	                    <td align="center" id="toDayhjwc">-</td>
	                </tr>
<!-- 	                <tr > -->
<!-- 	                	<td align="center" class='TDlabel'>在途手术</td> -->
<!-- 	                    <td align="center" id="toDayTotalzt">-</td> -->
<!-- 	                    <td align="center" id="toDayhyzt">-</td> -->
<!-- 	                    <td align="center" id="toDayzdzt">-</td> -->
<!-- 	                    <td align="center" id="toDayhjzt">-</td> -->
<!-- 	                </tr> -->
				</table>
			</li>
			<li>
				<table id="maintable2"  class="tableCss" style="width: 90%;border: 1px;margin-left: 20px;">
	                <tr class="TDlabel">
	                	<td align="center"></td>
	                    <td align="center">总例数</span></td>
	                    <td align="center">河医院区</td>
	                    <td align="center">郑东院区</td>
	                    <td align="center">惠济院区</td>
	                </tr>
	                <tr >
	                	<td align="center" class='TDlabel' >完成手术</td>
	                    <td align="center" id="yesTotalwc">-</td>
	                    <td align="center" id="yeshywc">-</td>
	                    <td align="center" id="yeszdwc">-</td>
	                    <td align="center" id="yeshjwc">-</td>
	                </tr>
				</table>
			</li>
			<li>
				<table id="maintable3"  class="tableCss" style="width: 90%;border: 1px;margin-left: 20px;">
	                <tr class="TDlabel">
	                	<td align="center"></td>
	                    <td align="center">总例数</span></td>
	                    <td align="center">河医院区</td>
	                    <td align="center">郑东院区</td>
	                    <td align="center">惠济院区</td>
	                </tr>
	                <tr >
	                	<td align="center" class='TDlabel' >完成手术</td>
	                    <td align="center" id="monthTotalwc">-</td>
	                    <td align="center" id="monthhywc">-</td>
	                    <td align="center" id="monthzdwc">-</td>
	                    <td align="center" id="monthhjwc">-</td>
	                </tr>
				</table>
			</li>
			<li>
				<table id="maintable4"  class="tableCss" style="width: 90%;border: 1px;margin-left: 20px;">
	                <tr class="TDlabel">
	                	<td align="center"></td>
	                    <td align="center">总例数</span></td>
	                    <td align="center">河医院区</td>
	                    <td align="center">郑东院区</td>
	                    <td align="center">惠济院区</td>
	                </tr>
	                <tr >
	                	<td align="center" class='TDlabel' >完成手术</td>
	                    <td align="center" id="yearTotalwc">-</td>
	                    <td align="center" id="yearhywc">-</td>
	                    <td align="center" id="yearzdwc">-</td>
	                    <td align="center" id="yearhjwc">-</td>
	                </tr>
				</table>
			</li>
		</ul>
		<ul>
			<li>
				<div id="outOrInDayBar" style="float: left;width: 100%;"> </div> 
<!-- 				<div id="commOremergDayBar" style="float: right;width: 49%;" ></div> -->
			</li>
			<li>
				<div id="outOrInYesterdayBar" style="float: left;width: 100%;"> </div> 
<!-- 				<div id="commOremergYesterdayBar" style="float: right;width: 49%;" ></div> -->
			</li>
			<li>
				<div id="outOrInMonthBar" style="float: left;width: 100%;"> </div> 
<!-- 				<div id="commOremergMonthBar" style="float: right;width: 49%;" ></div> -->
			</li>
			<li>
				<div id="outOrInYearBar" style="float: left;width: 100%;"> </div> 
<!-- 				<div id="commOremergYearBar" style="float: right;width: 49%;" ></div> -->
			</li>
		</ul>
		<ul>
			<li>
<!-- 				<div  style="height:15%;"></div> -->
				<div  id='todayRatioAxis' style="width: 100%;height:50%;"></div>
				<div  id='todayNumsAxis' style="height: 50%;width: 100%;"></div>
			</li>
			<li> 
				<div  id='yesterdayYoyAxis' style="height: 50%;width: 100%;"></div>
				<div  id='yesterdayRatioAxis' style="width: 100%;height:50%;"></div>
			</li>
			<li>
				<div  id='monthYoyAxis' style="height: 50%;width: 100%"></div>
				<div  id='monthRatioAxis' style="height: 50%;width: 100%"></div>
			</li>
			<li>
			    <div  style="height:15%;"></div>
				<div  id='yearRatioAxis' style="height:70%;"></div>
			</li>
		</ul>
	</body>
</html>
<script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
<script type="text/javascript">
var firstShow = true;
$(function(){
	//加载当天数据
	queryDay();
	var todayTime="${todayTime}";
	var yesterDayTime="${yesterDayTime}";
	$("#dayTime").text(/\d{4}-\d{1,2}-\d{1,2}/g.exec(todayTime));
	$("#dayTimeStop").text(/\d{1,2}:\d{1,2}/g.exec(todayTime));
	$("#yesTime").text(yesterDayTime);
	$("#time3").val(/\d{4}-\d{1,2}/g.exec(todayTime));
	$("#time4").val(/\d{4}/g.exec(todayTime));
});
//将表格中没有数据用-替换掉（当天）
function copyData(data){
	var wcTotal=Number(data.wchy)+Number(data.wczd)+Number(data.wchj);
	 var zzTotal=Number(data.zzhy)+Number(data.zzzd)+Number(data.zzhj);
	if(wcTotal==0){
		$("#toDayTotalwc").text("-"); 
	}else{
		$("#toDayTotalwc").text(wcTotal); 
	}
	if(zzTotal==0){
		$("#toDayTotalzt").text("-"); 
	}else{
		$("#toDayTotalzt").text(zzTotal); 
	}
	
	if(data.wchy==0){
		$("#toDayhywc").text("-"); 
	}else{
		$("#toDayhywc").text(data.wchy); 
	}
	if(data.wczd==0){
		$("#toDayzdwc").text("-"); 
	}else{
		$("#toDayzdwc").text(data.wczd); 
	}
	if(data.wchj==0){
		$("#toDayhjwc").text("-"); 
	}else{
		$("#toDayhjwc").text(data.wchj); 
	}
	if(data.zzhy==0){
		$("#toDayhyzt").text("-"); 
	}else{
		$("#toDayhyzt").text(data.zzhy); 
	}
	if(data.zzzd==0){
		$("#toDayzdzt").text("-"); 
	}else{
		$("#toDayzdzt").text(data.zzzd); 
	}
	if(data.zzhj==0){
		$("#toDayhjzt").text("-"); 
	}else{
		$("#toDayhjzt").text(data.zzhj); 
	}
	
}
/**  
 *  
 * @Description：表格、门诊住院、普通急诊、科室前五查询（当天）
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function queryDay(){
	queryMomYoy("time2", "0");
	$.ajax({
        type: "post",
        url: "<%=basePath %>statistics/operationNums/query.action",
        data: {startDate:$("#time2").val(),timeType:'0'},
        dataType: "json",
        async:true,
        success: function (data) {
			if(firstShow){
			 	queryYesterday();
			}
        	copyYes(data, "toDay");
			//门诊住院
			if(data.zyMzBar.length <= 2){
			  	 $('#outOrInDayBar').html('&nbsp;&nbsp;暂无数据');
			}else{
				setData1(JSON.parse(data.zyMzBar));
			}
        }
    });
}
//将表格中没有数据用-替换掉（年）
function copyYear(data){
    var wcTotal=Number(data.wchy)+Number(data.wczd)+Number(data.wchj);
    if(wcTotal==0){
		$("#yearTotalwc").text("-"); 
	}else{
		$("#yearTotalwc").text(wcTotal); 
	}
	if(data.wchy==0){
		$("#yearhywc").text("-"); 
	}else{
		$("#yearhywc").text(data.wchy); 
	}
	if(data.wczd==0){
		$("#yearzdwc").text("-"); 
	}else{
		$("#yearzdwc").text(data.wczd); 
	}
	if(data.wchj==0){
		$("#yearhjwc").text("-"); 
	}else{
		$("#yearhjwc").text(data.wchj); 
	}
}
/**  
 *  
 * @Description：表格、门诊住院、普通急诊、同比环比查询（年）
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function queryYear(){
	queryMomYoy("time4", "3");
	$.ajax({
        type: "post",
        url: "<%=basePath %>statistics/operationNums/query.action",
        data: {startDate:$("#time4").val(),timeType:'3'},
        dataType: "json",
        async:true,
        success: function (data) {
        	copyYes(data, "year");
			//门诊住院
			if(data.zyMzBar.length <= 2){
			  	 $('#outOrInYearBar').html('&nbsp;&nbsp;暂无数据');
			}else{
				setData6(JSON.parse(data.zyMzBar));
			}
        }
    });
}
// ===========================按天====================================================
//将昨天表格中没有数据用-替换掉
function copyYes(data, timeType){
//     var wcTotal=Number(data.wchy)+Number(data.wczd)+Number(data.wchj);
    if(data.zjNum==0){
		$("#"+timeType+"Totalwc").text("-"); 
	}else{
		$("#"+timeType+"Totalwc").text(data.zjNum); 
	}
	if(data.hyNum==0){
		$("#"+timeType+"hywc").text("-"); 
	}else{
		$("#"+timeType+"hywc").text(data.hyNum); 
	}
	if(data.zdNum==0){
		$("#"+timeType+"zdwc").text("-"); 
	}else{
		$("#"+timeType+"zdwc").text(data.zdNum); 
	}
	if(data.hjNum==0){
		$("#"+timeType+"hjwc").text("-"); 
	}else{
		$("#"+timeType+"hjwc").text(data.hjNum); 
	}
}
/**  
 *  
 * @Description：表格、同比环比查询（昨天）==============(按天)
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function queryYesterday(){
	queryMomYoy("time2", "1");
	$.ajax({
        type: "post",
        url: "<%=basePath %>statistics/operationNums/query.action",
        data: {startDate:$("#time2").val(),timeType:'1'},
        dataType: "json",
        async:true,
        success: function (data) {
        	if(firstShow){
			 	queryMonth();
			}
        	copyYes(data, "yes");
			//门诊住院
			if(data.zyMzBar.length <= 2){
			  	 $('#outOrInYesterdayBar').html('&nbsp;&nbsp;暂无数据');
			}else{
				setData37(JSON.parse(data.zyMzBar));
			}
        }
    });
}
/**  
 *  
 * @Description：同环比===========按天
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function queryMomYoy(momYoyTimeId, momYoyType){
	$.ajax({
        type: "post",
        url: "<%=basePath %>statistics/operationNums/queryMom.action",
        data: {startDate:$("#"+momYoyTimeId).val(),timeType:momYoyType},
        dataType: "json",
        async:true,
        success: function (data) {
        	if("0" == momYoyType){
        		setDataToDay(data.dateList,data.valList);//按日
			}else{
// 				 $('#todayNumsAxis').html('&nbsp;&nbsp;暂无数据');
			}
        	if("1" == momYoyType){
	        	setData30(data.dateList,data.valList);//按日
			}else{
// 				 $('#yesterdayRatioAxis').html('&nbsp;&nbsp;暂无数据');
			}
			if("2" == momYoyType){
				setData32(data.dateList,data.valList);//按月
			}else{
			}
			if("3" == momYoyType){
				setData33(data.dateList,data.valList);//按年
			}else{
// 				 $('#yearRatioAxis').html('&nbsp;&nbsp;暂无数据');
			}
        }
    });
	if("3" != momYoyType){
		$.ajax({
	        type: "post",
	        url: "<%=basePath %>statistics/operationNums/queryYoy.action",
	        data: {startDate:$("#"+momYoyTimeId).val(),timeType:momYoyType},
	        dataType: "json",
	        async:true,
	        success: function (data) {
	        	if("0" == momYoyType){
	        		setDataToDayAxis(data.dateList,data.valList);//按日
				}
	        	if("1" == momYoyType){
					setData29(data.dateList,data.valList);//按日
				}
				if("2" == momYoyType){
					setData31(data.dateList,data.valList);//按月
				}
	        }
	    });
	}
}










//将月表格中没有数据用-替换掉
function copyMonth(data){
    var wcTotal=Number(data.wchy)+Number(data.wczd)+Number(data.wchj);
    if(wcTotal==0){
		$("#monthTotalwc").text("-"); 
	}else{
		$("#monthTotalwc").text(wcTotal); 
	}
	if(data.wchy==0){
		$("#monthhywc").text("-"); 
	}else{
		$("#monthhywc").text(data.wchy); 
	}
	if(data.wczd==0){
		$("#monthzdwc").text("-"); 
	}else{
		$("#monthzdwc").text(data.wczd); 
	}
	if(data.wchj==0){
		$("#monthhjwc").text("-"); 
	}else{
		$("#monthhjwc").text(data.wchj); 
	}
}
/**  
 *  
 * @Description：表格、门诊住院、普通急诊、同比环比查询（月）
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function queryMonth(){
	queryMomYoy("time3", "2");
	$.ajax({
        type: "post",
        url: "<%=basePath %>statistics/operationNums/query.action",
        data: {startDate:$("#time3").val(),timeType:'2'},
        dataType: "json",
        async:true,
        success: function (data) {
        	if(firstShow){
				queryYear();
				firstShow = false;
			}
			copyYes(data, "month");
			//门诊住院
			if(data.zyMzBar.length <= 2){
			  	$('#outOrInMonthBar').html('&nbsp;&nbsp;暂无数据');
			}else{
				setData4(JSON.parse(data.zyMzBar));
			}
        }
    });
}

/**  
 *  
 * @Description：环比（年）
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function setData33(finalDate,nums){
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
	        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
	    ],
	    function (ec) {
	    	  var myChart = echarts.init(document.getElementById('yearRatioAxis')); 
		      var yearRatioAxis = {
		    			title: {
		    				text: '年环比(例数：人)',
		    				x: 'center',
		    				y: "top"
		    			},
		    			tooltip: {
		    				formatter: function (params, ticket, callback) {
					        	var i = "--";
					        	var nextNum = "1";
					        	if("0"!=nums[(params.dataIndex-1)]){
					        		nextNum = nums[(params.dataIndex-1)];
					        	}
					        	if((params.dataIndex-1)<0){
					        	}else{
					        		if(Number(params.data)>0&&Number(nextNum)==0){
					        			i='100.00';
					        		}else if(Number(params.data)>0&&Number(nextNum)>0){
					        			i = ((Number(params.data)-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
					        		}else if(Number(params.data)==0&&Number(nextNum)>0){
					        			i='-100.00';
					        		}
					        	}
							    var rc = (""+params.seriesName).substring(0,3)+"人次";
							    var bz = "增减(%)";
							    
							    return rc+"<br/>"+params.data+"<br/>"+bz+"<br/>"+i;
							},
					        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
					        }
		    			},
		    			xAxis: [{
		    				name:'日期',
		    				type: 'category',
		    				axisLabel:{
		    	                //X轴刻度配置
		    	                interval:0, //0：表示全部显示不间隔；auto:表示自动根据刻度个数和宽度自动设置间隔个数
		    	                rotate:30//30度角倾斜显示
		    	           },
		    				data: finalDate
		    			}],
		    			yAxis: [{
		    				name:'例数(人)',
		    				type: 'value'
		    			}],
		    			 grid: { // 控制图的大小，调整下面这些值就可以，
		    	             x: 90,
		    	             x2: 100,
		    	             y2: 50,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
		    	         },
		    			series: [{
		    					name: '年环比(单位：人)',
		    					type: 'bar',
		    					barWidth : 20,//柱图宽度
		    			        barMaxWidth:25,//最大宽度,
		    					data: nums,
		    					itemStyle: {
		    					    normal: {
		    					    	color:"#F67E43"
		    					    }
		    					}
		    				},
		    				 {
		    	                name:'折线',
		    	                type:'line',
		    	                itemStyle : {  /*设置折线颜色*/
		    	                    normal : {
		    	                        color:'#7498f3'
		    	                    }
		    	                },
		    	                data:nums
		    	            }
		    			],
		    		}
	    	  // 为echarts对象加载数据 
	    	  myChart.setOption(yearRatioAxis); 
	    	  myChart.hideLoading();
	    	}
	);
}
/**  
 *  
 * @Description：门诊住院（天）
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function setData1(nums){
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
        	var myChart1 = echarts.init(document.getElementById('outOrInDayBar')); 
        	option1 = {
        			tooltip : {
        		        trigger: 'item',
        		        formatter: "{b} : {c} ({d}%)"
        		    },
        		    legend: {
//         		        orient : 'vertical',
        		        x : 'left',
        		        data:['门诊','住院']
        		    },
        		    calculable : true,
        		    series : [
        		        {
        		            type:'pie',
        		            radius : '70%',
        		            center: ['50%', '60%'],
        		            data:nums,
        		            itemStyle:{ 
        	                    normal:{ 
        	                        label:{ 
        	                           show: true, 
        	                           formatter: '{b}({d}%)' 
        	                        }, 
        	                        labelLine :{show:true}
        	                    } 
        	                },
        		        }
        		    ],
        			color: ['#e0aa82','#e4bbec','#5eb765']
        		};
        	  
        	  // 为echarts对象加载数据 
        	  myChart1.setOption(option1); 
        	  myChart1.hideLoading();
        	}
    );
	
}
/**  
 *  
 * @Description：普通急诊（天）
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function setData2(nums){
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
        	var myChart2 = echarts.init(document.getElementById('commOremergDayBar')); 
        	commOremergDayBar = {
        			tooltip : {
        		        trigger: 'item',
        		        formatter: "{b} : {c} ({d}%)"
        		    },
        		    legend: {
        		        orient : 'vertical',
        		        x : 'left',
        		        data:['急诊','普通']
        		    },
        		    calculable : true,
        		    series : [
        		        {
        		            type:'pie',
        		            radius : '30%',
        		            center: ['50%', '50%'],
        		            data:nums,
        		            itemStyle:{ 
        	                    normal:{ 
        	                        label:{ 
        	                           show: true, 
        	                           formatter: '{b}({d}%)' 
        	                        }, 
        	                        labelLine :{show:true}
        	                    } 
        	                },
        		        }
        		    ],
        			color: ["#79EC7C","#82B8EB","#92E2DA"]
        		};
        	  
        	  // 为echarts对象加载数据 
        	  myChart2.setOption(commOremergDayBar); 
        	  myChart2.hideLoading();
        	}
    );
}
/**  
 *  
 * @Description：门诊住院（月）
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function setData4(nums){
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
        	var myChart4 = echarts.init(document.getElementById('outOrInMonthBar'));
        	outOrInMonthBar = {
        			tooltip : {
        		        trigger: 'item',
        		        formatter: "{b} : {c} ({d}%)"
        		    },
        		    legend: {
//         		        orient : 'vertical',
        		        x : 'left',
        		        data:['门诊','住院']
        		    },
        		    calculable : true,
        		    series : [
        		        {
        		            type:'pie',
        		            radius : '70%',
        		            center: ['50%', '60%'],
        		            data:nums,
        		            itemStyle:{ 
        	                    normal:{ 
        	                        label:{ 
        	                           show: true, 
        	                           formatter: '{b}({d}%)' 
        	                        }, 
        	                        labelLine :{show:true}
        	                    } 
        	                },
        		        }
        		    ],
        			color: ['#e0aa82','#e4bbec','#5eb765']
        		};
        	  
        	  // 为echarts对象加载数据 
        	  myChart4.setOption(outOrInMonthBar); 
        	  myChart4.hideLoading();
        	}
    );
}
/**  
 *  
 * @Description：普通急诊（月）
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function setData5(nums){
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
        	var myChart5 = echarts.init(document.getElementById('commOremergMonthBar'));
        	commOremergMonthBar = {
        			tooltip : {
        		        trigger: 'item',
        		        formatter: "{b} : {c} ({d}%)"
        		    },
        		    legend: {
        		        orient : 'vertical',
        		        x : 'left',
        		        data:['急诊','普通']
        		    },
        		    calculable : true,
        		    series : [
        		        {
        		            type:'pie',
        		            radius : '30%',
        		            center: ['50%', '50%'],
        		            data:nums,
        		            itemStyle:{ 
        	                    normal:{ 
        	                        label:{ 
        	                           show: true, 
        	                           formatter: '{b}({d}%)' 
        	                        }, 
        	                        labelLine :{show:true}
        	                    } 
        	                },
        		        }
        		    ],
        			color: ["#79EC7C","#82B8EB","#92E2DA"]
        		};
        	  
        	  myChart5.setOption(commOremergMonthBar); 
        	  myChart5.hideLoading();
        	}
    );
}
/**  
 *  
 * @Description：住院门诊（年）
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function setData6(nums){
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
        	var myChart6 = echarts.init(document.getElementById('outOrInYearBar'));
        	outOrInYearBar = {
        			tooltip : {
        		        trigger: 'item',
        		        formatter: "{b} : {c} ({d}%)"
        		    },
        		    legend: {
//         		        orient : 'vertical',
        		        x : 'left',
        		        data:['门诊','住院']
        		    },
        		    calculable : true,
        		    series : [
        		        {
        		            type:'pie',
        		            radius : '70%',
        		            center: ['50%', '60%'],
        		            data:nums,
        		            itemStyle:{ 
        	                    normal:{ 
        	                        label:{ 
        	                           show: true, 
        	                           formatter: '{b}({d}%)' 
        	                        }, 
        	                        labelLine :{show:true}
        	                    } 
        	                },
        		        }
        		    ],
        			color: ['#e0aa82','#e4bbec','#5eb765']
        		};
        	  
        	  myChart6.setOption(outOrInYearBar);
        	  myChart6.hideLoading();
        	}
    );
}
/**  
 *  
 * @Description：普通或急诊（年）
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function setData7(nums){
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
        	var myChart7 = echarts.init(document.getElementById('commOremergYearBar'));
        	commOremergYearBar = {
        			tooltip : {
        		        trigger: 'item',
        		        formatter: "{b} : {c} ({d}%)"
        		    },
        		    legend: {
        		        orient : 'vertical',
        		        x : 'left',
        		        data:['急诊','普通']
        		    },
        		    calculable : true,
        		    series : [
        		        {
        		            type:'pie',
        		            radius : '30%',
        		            center: ['50%', '50%'],
        		            data:nums,
        		            itemStyle:{ 
        	                    normal:{ 
        	                        label:{ 
        	                           show: true, 
        	                           formatter: '{b}({d}%)' 
        	                        }, 
        	                        labelLine :{show:true}
        	                    } 
        	                },
        		        }
        		    ],
        			color: ["#79EC7C","#82B8EB","#92E2DA"]
        		};
        	  
        	  
        	  myChart7.setOption(commOremergYearBar); 
        	  myChart7.hideLoading();
        	}
    );
}
/**  
 *  
 * @Description：月环比折线
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function setData32(finalDate,nums){
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
	        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
	    ],
        function (ec) {
        	var myChart = echarts.init(document.getElementById('monthRatioAxis'));
        	var monthRatioAxis = {
        			title: {
        				text: '月环比(例数：人)',
        				x: 'center',
        				y: "top"
        			},
        			tooltip: {
        				formatter: function (params, ticket, callback) {
				        	var i = "--";
				        	var nextNum = "1";
				        	if("0"!=nums[(params.dataIndex-1)]){
				        		nextNum = nums[(params.dataIndex-1)];
				        	}
				        	if((params.dataIndex-1)<0){
				        	}else{
// 							    i = ((Number(params.data)-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
				        		if(Number(params.data)>0&&Number(nextNum)==0){
				        			i='100.00';
				        		}else if(Number(params.data)>0&&Number(nextNum)>0){
				        			i = ((Number(params.data)-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
				        		}else if(Number(params.data)==0&&Number(nextNum)>0){
				        			i='-100.00';
				        		}
				        	}
						    var rc = (""+params.seriesName).substring(0,3)+"人次";
						    var bz = "增减(%)";
						    
						    return rc+"<br/>"+params.data.replace(".0","")+"<br/>"+bz+"<br/>"+i;
						},
				        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				        }
        			},
        			xAxis: [{
        				name:'日期',
        				type: 'category',
        				axisLabel:{
        	                //X轴刻度配置
        	                interval:0, 
        	                rotate:30//30度角倾斜显示
        	           },
        				data: finalDate
        			}],
        			yAxis: [{
        				name:'例数(人)',
        				type: 'value'
        			}],
        			 grid: { // 控制图的大小，调整下面这些值就可以，
        	             x: 95,
        	             x2: 100,
        	             y2: 35,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
        	         },
        			
        			series: [{
        					name: '月环比(单位：人)',
        					type: 'bar',
        					barWidth : 20,//柱图宽度
        			        barMaxWidth:25,//最大宽度,
        					data: nums,
        					itemStyle: {
        					    normal: {
        					    	color:"#F67E43"
        					    }
        					}
        				},
        				 {
        	                name:'折线',
        	                type:'line',
        	                itemStyle : {  /*设置折线颜色*/
        	                    normal : {
        	                        color:'#7498f3'
        	                    }
        	                },
        	                data:nums
        	            }
        			],
        		}
        	  myChart.setOption(monthRatioAxis); 
        	  myChart.hideLoading();
        	}
    );
}
/**  
 *  
 * @Description：月同比折线
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function setData31(finalDate,nums){
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
	        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
	    ],
        function (ec) {
        	var myChart = echarts.init(document.getElementById('monthYoyAxis'));
        	var monthYoyAxis = {
        			title: {
        				text: '月同比(例数：人)',
        				x: 'center',
        				y: "top"
        			},
        			tooltip: {
        				formatter: function (params, ticket, callback) {
				        	var i = "--";
				        	var nextNum = "1";
				        	if("0"!=nums[(params.dataIndex-1)]){
				        		nextNum = nums[(params.dataIndex-1)];
				        	}
				        	if((params.dataIndex-1)<0){
				        	}else{
// 							    i = ((Number(params.data)-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
				        		if(Number(params.data)>0&&Number(nextNum)==0){
				        			i='100.00';
				        		}else if(Number(params.data)>0&&Number(nextNum)>0){
				        			i = ((Number(params.data)-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
				        		}else if(Number(params.data)==0&&Number(nextNum)>0){
				        			i='-100.00';
				        		}
				        	}
						    var rc = (""+params.seriesName).substring(0,3)+"人次";
						    var bz = "增减(%)";
						    
						    return rc+"<br/>"+params.data.replace(".0","")+"<br/>"+bz+"<br/>"+i;
						},
				        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				        }
        			},
        			xAxis: [{
        				name:'日期',
        				type: 'category',
        				axisLabel:{
        	                //X轴刻度配置
        	                interval:0,
        	                rotate:30//30度角倾斜显示
        	           },
        				data: finalDate
        			}],
        			yAxis: [{
        				name:'例数(人)',
        				type: 'value'
        			}],
        			 grid: { // 控制图的大小，调整下面这些值就可以，
        	             x: 95,
        	             x2: 100,
        	             y2: 35,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
        	         },
        			series: [{
        					name: '月同比(单位：人)',
        					type: 'bar',
        					barWidth : 20,//柱图宽度
        			        barMaxWidth:25,//最大宽度,
        					data: nums,
        					itemStyle: {
        					    normal: {
        					    	color:"#F67E43"
        					    }
        					}
        				},
        				 {
        	                name:'折线',
        	                type:'line',
        	                itemStyle : {  /*设置折线颜色*/
        	                    normal : {
        	                        color:'#7498f3'
        	                    }
        	                },
        	                data:nums
        	            }
        			],
        		}

        	  myChart.setOption(monthYoyAxis); 
        	  myChart.hideLoading();
        	}
    );
}

/**  
 *  
 * @Description：昨天环比折线
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */ 
function setData30(finalDate,nums){
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
	        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
	    ],
        function (ec) {
        	var myChart = echarts.init(document.getElementById('yesterdayRatioAxis'));
        	var yesterdayRatioAxis = {
        			title: {
        				text: '环比(例数：人)',
        				x: 'center',
        				y: "top"
        			},
        			tooltip: {
        				formatter: function (params, ticket, callback) {
				        	var i = "--";
				        	var nextNum = "1";
				        	if("0"!=nums[(params.dataIndex-1)]){
				        		nextNum = nums[(params.dataIndex-1)];
				        	}
				        	if((params.dataIndex-1)<0){
				        	}else{
// 							    i = ((Number(params.data)-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
				        		if(Number(params.data)>0&&Number(nextNum)==0){
				        			i='100.00';
				        		}else if(Number(params.data)>0&&Number(nextNum)>0){
				        			i = ((Number(params.data)-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
				        		}else if(Number(params.data)==0&&Number(nextNum)>0){
				        			i='-100.00';
				        		}
				        	}
						    var rc = (""+params.seriesName).substring(0,3)+"人次";
						    var bz = "增减(%)";
						    
						    return rc+"<br/>"+params.data.replace(".0","")+"<br/>"+bz+"<br/>"+i;
						},
				        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				        }
        			},
        			xAxis: [{
        				name:'日期',
        				type: 'category',
        				axisLabel:{
        	                //X轴刻度配置
        	                interval:0,
        	                rotate:30//30度角倾斜显示
        	           },
        				data:finalDate
        			}],
        			 grid: { // 控制图的大小，调整下面这些值就可以，
        	             x: 60,
        	             x2: 100,
        	             y2: 50,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
        	         },
        			yAxis: [{
        				name:'例数(人)',
        				type: 'value'
        			}],
        			series: [{
        					name: '环比(单位：人)',
        					type: 'bar',
        					barWidth : 20,//柱图宽度
        			        barMaxWidth:25,//最大宽度,
        					data: nums,
        					itemStyle: {
        					    normal: {
        					    	color:"#F67E43"
        					    }
        					}
        				},
        				 {
        	                name:'折线',
        	                type:'line',
        	                itemStyle : {  /*设置折线颜色*/
        	                    normal : {
        	                        color:'#7498f3'
        	                    }
        	                },
        	                data:nums
        	            }
        			],
        		}

        	  myChart.setOption(yesterdayRatioAxis); 
        	  myChart.hideLoading();
        	}
    );
}
function setDataToDayAxis(finalDate,nums){
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
	        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
	    ],
        function (ec) {
        	var myChart = echarts.init(document.getElementById('todayRatioAxis'));
        	var yesterdayRatioAxis = {
        			title: {
        				text: '同比(例数：人)',
        				x: 'center',
        				y: "top"
        			},
        			tooltip: {
        				formatter: function (params, ticket, callback) {
				        	var i = "--";
				        	var nextNum = "1";
				        	if("0"!=nums[(params.dataIndex-1)]){
				        		nextNum = nums[(params.dataIndex-1)];
				        	}
				        	if((params.dataIndex-1)<0){
				        	}else{
// 							    i = ((Number(params.data)-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
				        		if(Number(params.data)>0&&Number(nextNum)==0){
				        			i='100.00';
				        		}else if(Number(params.data)>0&&Number(nextNum)>0){
				        			i = ((Number(params.data)-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
				        		}else if(Number(params.data)==0&&Number(nextNum)>0){
				        			i='-100.00';
				        		}
				        	}
						    var rc = (""+params.seriesName).substring(0,3)+"人次";
						    var bz = "增减(%)";
						    
						    return rc+"<br/>"+params.data.replace(".0","")+"<br/>"+bz+"<br/>"+i;
						},
				        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				        }
        			},
        			xAxis: [{
        				name:'日期',
        				type: 'category',
        				axisLabel:{
        	                //X轴刻度配置
        	                interval:0,
        	                rotate:30//30度角倾斜显示
        	           },
        				data:finalDate
        			}],
        			 grid: { // 控制图的大小，调整下面这些值就可以，
        	             x: 60,
        	             x2: 100,
        	             y2: 50,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
        	         },
        			yAxis: [{
        				name:'例数(人)',
        				type: 'value'
        			}],
        			series: [{
        					name: '环比(单位：人)',
        					type: 'bar',
        					barWidth : 20,//柱图宽度
        			        barMaxWidth:25,//最大宽度,
        					data: nums,
        					itemStyle: {
        					    normal: {
        					    	color:"#F67E43"
        					    }
        					}
        				},
        				 {
        	                name:'折线',
        	                type:'line',
        	                itemStyle : {  /*设置折线颜色*/
        	                    normal : {
        	                        color:'#7498f3'
        	                    }
        	                },
        	                data:nums
        	            }
        			],
        		}

        	  myChart.setOption(yesterdayRatioAxis); 
        	  myChart.hideLoading();
        	}
    );
}
/**  
 *  
 * @Description：昨天同比折线
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */ 
function setData29(finalDate,nums){
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
	        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
	    ],
        function (ec) {
        	var myChart = echarts.init(document.getElementById('yesterdayYoyAxis'));
        	var yesterdayYoyAxis = {
        			title: {
        				text: '同比(例数：人)',
        				x: 'center',
        				y: "top"
        			},
        			tooltip: {
        				formatter: function (params, ticket, callback) {
				        	var i = "--";
				        	var nextNum = "1";
				        	if("0"!=nums[(params.dataIndex-1)]){
				        		nextNum = nums[(params.dataIndex-1)];
				        	}
				        	if((params.dataIndex-1)<0){
				        	}else{
				        		if(Number(params.data)>0&&Number(nextNum)==0){
				        			i='100.00';
				        		}else if(Number(params.data)>0&&Number(nextNum)>0){
				        			i = ((Number(params.data)-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
				        		}else if(Number(params.data)==0&&Number(nextNum)>0){
				        			i='-100.00';
				        		}
				        	}
						    var rc = (""+params.seriesName).substring(0,3)+"人次";
						    var bz = "增减(%)";
						    
						    return rc+"<br/>"+params.data.replace(".0","")+"<br/>"+bz+"<br/>"+i;
						},
				        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				        }
        			},
        			xAxis: [{
        				name:'日期',
        				type: 'category',
        				axisLabel:{
        	                //X轴刻度配置
        	                interval:0,
        	                rotate:30//30度角倾斜显示
        	           },
        				data: finalDate
        			}],
        			yAxis: [{
        				name:'例数(人)',
        				type: 'value'
        			}],
        			 grid: { // 控制图的大小，调整下面这些值就可以，
        	             x: 60,
        	             x2: 100,
        	             y2: 50,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
        	         },
        	  
        			series: [{
        					name: '同比(单位：人)',
        					type: 'bar',
        					barWidth : 20,//柱图宽度
        			        barMaxWidth:25,//最大宽度,
        					data: nums,
        					itemStyle: {
        					    normal: {
        					    	color:"#F67E43"
        					    }
        					}
        				},
        				 {
        	                name:'折线',
        	                type:'line',
        	                itemStyle : {  /*设置折线颜色*/
        	                    normal : {
        	                        color:'#7498f3'
        	                    }
        	                },
        	                data:nums
        	            }
        			],
        		}

        	  myChart.setOption(yesterdayYoyAxis); 
        	  myChart.hideLoading();
        	}
    );
}
function setDataToDay(finalDate,nums){
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
	        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
	    ],
        function (ec) {
        	var myChart = echarts.init(document.getElementById('todayNumsAxis'));
        	var yesterdayYoyAxis = {
        			title: {
        				text: '环比(例数：人)',
        				x: 'center',
        				y: "top"
        			},
        			tooltip: {
        				formatter: function (params, ticket, callback) {
				        	var i = "--";
				        	var nextNum = "1";
				        	if("0"!=nums[(params.dataIndex-1)]){
				        		nextNum = nums[(params.dataIndex-1)];
				        	}
				        	if((params.dataIndex-1)<0){
				        	}else{
				        		if(Number(params.data)>0&&Number(nextNum)==0){
				        			i='100.00';
				        		}else if(Number(params.data)>0&&Number(nextNum)>0){
				        			i = ((Number(params.data)-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
				        		}else if(Number(params.data)==0&&Number(nextNum)>0){
				        			i='-100.00';
				        		}
				        	}
						    var rc = (""+params.seriesName).substring(0,3)+"人次";
						    var bz = "增减(%)";
						    
						    return rc+"<br/>"+params.data.replace(".0","")+"<br/>"+bz+"<br/>"+i;
						},
				        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				        }
        			},
        			xAxis: [{
        				name:'日期',
        				type: 'category',
        				axisLabel:{
        	                //X轴刻度配置
        	                interval:0,
        	                rotate:30//30度角倾斜显示
        	           },
        				data: finalDate
        			}],
        			yAxis: [{
        				name:'例数(人)',
        				type: 'value'
        			}],
        			 grid: { // 控制图的大小，调整下面这些值就可以，
        	             x: 60,
        	             x2: 100,
        	             y2: 50,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
        	         },
        	  
        			series: [{
        					name: '同比(单位：人)',
        					type: 'bar',
        					barWidth : 20,//柱图宽度
        			        barMaxWidth:25,//最大宽度,
        					data: nums,
        					itemStyle: {
        					    normal: {
        					    	color:"#F67E43"
        					    }
        					}
        				},
        				 {
        	                name:'折线',
        	                type:'line',
        	                itemStyle : {  /*设置折线颜色*/
        	                    normal : {
        	                        color:'#7498f3'
        	                    }
        	                },
        	                data:nums
        	            }
        			],
        		}

        	  myChart.setOption(yesterdayYoyAxis); 
        	  myChart.hideLoading();
        	}
    );
}

/**  
 *  
 * @Description：门诊住院（昨天）
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function setData37(nums){
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
        	var myChart4 = echarts.init(document.getElementById('outOrInYesterdayBar'));
        	outOrInYesterdayBar = {
        			tooltip : {
        		        trigger: 'item',
        		        formatter: "{b} : {c} ({d}%)"
        		    },
        		    legend: {
//         		        orient : 'vertical',
        		        x : 'left',
        		        data:['门诊','住院']
        		    },
        		    calculable : true,
        		    series : [
        		        {
        		            type:'pie',
        		            radius : '70%',
        		            center: ['50%', '60%'],
        		            data:nums,
        		            itemStyle:{ 
        	                    normal:{ 
        	                        label:{ 
        	                           show: true, 
        	                           formatter: '{b}({d}%)' 
        	                        }, 
        	                        labelLine :{show:true}
        	                    } 
        	                },
        		        }
        		    ],
        			color: ['#e0aa82','#e4bbec','#5eb765']
        		};
        	  
        	  // 为echarts对象加载数据 
        	  myChart4.setOption(outOrInYesterdayBar); 
        	  myChart4.hideLoading();
        	}
    );
}

/**  
 *  
 * @Description：普通或急诊（昨天）
 * @Author：zxl
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function setData38(nums){
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
        	var myChart7 = echarts.init(document.getElementById('commOremergYesterdayBar'));
        	commOremergYesterdayBar = {
        			tooltip : {
        		        trigger: 'item',
        		        formatter: "{b} : {c} ({d}%)"
        		    },
        		    legend: {
        		        orient : 'vertical',
        		        x : 'left',
        		        data:['急诊','普通']
        		    },
        		    calculable : true,
        		    series : [
        		        {
        		            type:'pie',
        		            radius : '30%',
        		            center: ['50%', '50%'],
        		            data:nums,
        		            itemStyle:{ 
        	                    normal:{ 
        	                        label:{ 
        	                           show: true, 
        	                           formatter: '{b}({d}%)' 
        	                        }, 
        	                        labelLine :{show:true}
        	                    } 
        	                },
        		        }
        		    ],
        			color: ["#79EC7C","#82B8EB","#92E2DA"]
        		};
        	  
        	  
        	  myChart7.setOption(commOremergYesterdayBar); 
        	  myChart7.hideLoading();
        	}
    );
}
</script>