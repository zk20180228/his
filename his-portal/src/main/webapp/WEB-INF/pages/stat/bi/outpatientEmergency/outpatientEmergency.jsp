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
		<title>门急诊疾病统计</title>
		<script type="text/javascript" src="<%=basePath %>javascript/echarts/newChart/echarts-all-3.js"></script>
		<%@ include file="/common/metas.jsp"%>
		<script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
		<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
		<style type="text/css">
		   .tableCss{
				border-collapse: collapse;border-spacing: 0;border: 1px solid #95b8e7;width:100%;
			}
			.tableCss .TDlabel{
			 	text-align: center;
				font-size:14px;
			}
			.tableCss td{
				text-align: center;
				padding: 5px 5px;
				word-break: keep-all;
				white-space:nowrap;
				width: 143px;
				height:45px;
			}
		</style>
	</head>
	<body style="margin: 0px; padding: 0px;">
		<div id="cc" class="easyui-layout" fit="true";">   
		    <div data-options="region:'north',border:false" style="width:100%;height:25%;">
	    		<table style="padding-top: 10px;margin-left: 10px;margin-bottom: 10px">
					<tr>
						<td >时间:</td>
						<td >
							<input id="time" value="${todayTime}" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d',onpicked:queryYesterday})" />
						</td>
					</tr>
				</table>
				<table id="maintable1"  class="tableCss" style="width: 98%;border: 1px;margin-left: 10px">
	                <tr class="TDlabel">
	                	<td align="center">指标</td>
	                    <td align="center">人次</td>
	                    <td align="center">外伤人次</td>
	                    <td align="center">发热待查人次</td>
	                    <td align="center">急性胃肠炎人次</td>
	                    <td align="center">急腹痛原因待查人次</td>
	                    <td align="center">呼吸道感染人次</td>
	                    <td align="center">脑血管病变人次</td>
	                    <td align="center">急腹症人次</td>
	                    <td align="center">冠心病人次</td>
	                    <td align="center">急性酒精中毒人次</td>
	                    <td align="center">高血压人次</td>
	                    <td align="center">其他人次</td>
	                </tr>
	                <tr>
	                	<td align="center" class='TDlabel' >急诊</td>
	                    <td align="center" id="totalNum">-</td>
	                    <td align="center" id="WSNum">-</td>
	                    <td align="center" id="FSDCNum">-</td>
	                    <td align="center" id="JXWXYNum">-</td>
	                    <td align="center" id="JFTNum">-</td>
	                    <td align="center" id="HXDGRNum">-</td>
	                    <td align="center" id="NXGBBNum">-</td>
	                    <td align="center" id="JFZNum">-</td>
	                    <td align="center" id="GXBNum">-</td>
	                    <td align="center" id="JJZDNum">-</td>
	                    <td align="center" id="GXYNum">-</td>
	                    <td align="center" id="QTNum">-</td>
	                </tr>
				</table>
		    </div>   
		    <div data-options="region:'center',border:false ">
		    	<div  id='monthYoyAxis' style="height: 75%;width: 100%"></div>
		    </div>   
		</div>  
	</body>
</html>
<script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
<script type="text/javascript">
	$(function(){
		var todayTime="${todayTime}";
		$("#time").val(todayTime);
		queryDateBytime()
	});
	function queryYesterday(){
		queryDateBytime()
	}
	function queryDateBytime(){
		var time = $('#time').val();
		$.messager.progress({text:'查询中，请稍后...',modal:true});
		$.ajax({
	        type: "post",
	        url: "<%=basePath %>statistics/OutpatientEmergency/listStatisticsQuery.action",
	        data: {time:time},
	        dataType: "json",
	        async:true,
	        success: function (map) {
	        	$.messager.progress('close');
	        	if(map.state=='success'){
	        		console.info(map);
	        		displayData(map.data);
	        		setData31(map.xdata,map.ydata);
	        	}else{
	        		$.messager.alert('提示','查询失败！');
	        	}
	        }
	    });		
	}
	//将表格中没有数据用-替换掉（年）
	function displayData(data){
	    if(data.totalNum==0){
			$("#totalNum").text("-"); 
		}else{
			$("#totalNum").text(data.totalNum); 
		}
		if(data.WSNum==0){
			$("#WSNum").text("-"); 
		}else{
			$("#WSNum").text(data.WSNum); 
		}
		if(data.FSDCNum==0){
			$("#FSDCNum").text("-"); 
		}else{
			$("#FSDCNum").text(data.FSDCNum); 
		}
		if(data.JXWXYNum==0){
			$("#JXWXYNum").text("-"); 
		}else{
			$("#JXWXYNum").text(data.JXWXYNum); 
		}
		if(data.JFTNum==0){
			$("#JFTNum").text("-"); 
		}else{
			$("#JFTNum").text(data.JFTNum); 
		}
		if(data.HXDGRNum==0){
			$("#HXDGRNum").text("-"); 
		}else{
			$("#HXDGRNum").text(data.HXDGRNum); 
		}
		if(data.NXGBBNum==0){
			$("#NXGBBNum").text("-"); 
		}else{
			$("#NXGBBNum").text(data.NXGBBNum); 
		}
		if(data.JFZNum==0){
			$("#JFZNum").text("-"); 
		}else{
			$("#JFZNum").text(data.JFZNum); 
		}
		if(data.GXBNum==0){
			$("#GXBNum").text("-"); 
		}else{
			$("#GXBNum").text(data.GXBNum); 
		}
		if(data.JJZDNum==0){
			$("#JJZDNum").text("-"); 
		}else{
			$("#JJZDNum").text(data.JJZDNum); 
		}
		if(data.GXYNum==0){
			$("#GXYNum").text("-"); 
		}else{
			$("#GXYNum").text(data.GXYNum); 
		}
		if(data.QTNum==0){
			$("#QTNum").text("-"); 
		}else{
			$("#QTNum").text(data.QTNum); 
		}
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
								    i = ((Number(params.data)-Number(nextNum))/Number(nextNum)*100).toFixed(2)+"%";
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
	        				name:'急诊类型',
	        				type: 'category',
	        				axisLabel:{
	        	                //X轴刻度配置
	        	                interval:0,
	        	                rotate:10//30度角倾斜显示
	        	           },
	        				data: finalDate
	        			}],
	        			yAxis: [{
	        				name:'人数(人)',
	        				type: 'value'
	        			}],
	        			 grid: { // 控制图的大小，调整下面这些值就可以，
	        	             x: 95,
	        	             x2: 100,
	        	             y2: 35,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
	        	         },
	        			series: [{
	        					type: 'bar',
	        					barWidth : 30,//柱图宽度
	        			        barMaxWidth:30,//最大宽度,
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
</script>