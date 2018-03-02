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
<style type="text/css">
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
	/* border-right: 1px solid black;
	border-bottom: 1px solid black; */
	/* border: 1px solid #95b8e7; */
	padding: 5px 5px;
	word-break: keep-all;
	white-space:nowrap;
	width: 143px;
}
.progressbar-value  .progressbar-text{
	background-color:#53CA22;
}
.progressbar-value  .progressbar-text rate{
	background-color:LightBlue;
}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
<script type="text/javascript">
var map = new Map();
var mapDetail = new Map();
var mapAll = new Map();
$(function(){
// 	decision();
    $("#monthSelect").combobox({  
        onChange:addTable
    }); 
    $("#monthSelect").combobox("setValue","2");
    $("#yearSelect").combobox("setValue","2015");
//     addTable1();
    $("#monthSelect").combobox("setValue","3");
    $("#yearSelect").combobox("setValue","2015");
//     addTable1();
    $("#mySelect").combobox({  
        onChange:decision
    }); 
});









//决策图
function decision(){
	var magnitude = $("#mySelect").combobox("getValue"); 
	var data1 = [];
	var data2 = [];
	var data3 = [];
	var data4 = [];
	var data7 = [];
	// 路径配置
	require.config({
	    paths: {
	        echarts: '${pageContext.request.contextPath}/javascript/echarts'
	    }
	});
	
	require(
		    [
		        'echarts',
		        'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
		    ],
		    function (ec) {
		    	  // 基于准备好的dom，初始化echarts图表
		    	  decisionChart = ec.init(document.getElementById('deci'));

		    	  map = choose(magnitude);
		    	  data1 = map.get("data1");
		    	  data2 = map.get("data2");
		    	  data3 = map.get("data3");
		    	  data4 = map.get("data4");
		    	  data7 = map.get("data7");
		    	  decisionOption = {
		  				color: ['#ff3d3d', '#00a0e9', '#f603ff','#00b419','#5f52a0'],
		  				tooltip: {
		  					trigger: 'axis'
		  				},
		  				legend: {
		  					x: 'right',
		  					padding: [10, 20,0,20],
		  					data: ['1病区(重症监护新生儿)', '2病区(呼吸消化)', '3病区(神经内分泌)', '4病区(心血管肾脏)', '7病区(血液风湿免疫)'],
		  					selected: {
		  						'1病区(重症监护新生儿)': true,
		  						'2病区(呼吸消化)': true,
		  						'3病区(神经内分泌)': false,
		  						'4病区(心血管肾脏)': false,
		  						'7病区(血液风湿免疫)': false
		  					}
		  				},
		  				grid: {
		  					left: '0',
		  					right: '3%',
		  					bottom: '3%',
		  					top:'13%',
		  					containLabel: true
		  				},
		  				xAxis: {
		  					type: 'category',
		  					boundaryGap: false,
		  					splitLine:{//网格线
		  		                show: true,
		  		                lineStyle:{
		  		                    color:['#b1b1b1'],
		  		                    type:'dashed'
		  		                }
		  		            },
		  					data: ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月']
		  				},
		  				yAxis: {
		  					axisLabel : {
		  	                    formatter: '{value} %'
		  	                },
		  	                splitNumber: 5,
		  	                max: 100,
		  	                min: 95,
		  					splitLine:{//网格线
		  		                show: true,
		  		                lineStyle:{
		  		                    color:['#b1b1b1'],
		  		                    type:'dashed'
		  		                }
		  		            }
		  				},
		  				series: [
		  					{
		  						name:'1病区(重症监护新生儿)',
		  						type:'line',
		  						data: data1,
		  						label: {
		  							normal: {
		  								show: true,
		  								position: 'top'//值显示
		  							}
		  						}
		  					},
		  					{
		  						name:'2病区(呼吸消化)',
		  						type:'line',
		  						data: data2,
		  						label: {
		  							normal: {
		  								show: true,
		  								position: 'top'
		  							}
		  						}
		  					},
		  					{
		  						name:'3病区(神经内分泌)',
		  						type:'line',
		  						data: data3,
		  						label: {
		  							normal: {
		  								show: true,
		  								position: 'top'
		  							}
		  						}
		  					},
		  					{
		  						name:'4病区(心血管肾脏)',
		  						type:'line',
		  						data:data4,
		  						label: {
		  							normal: {
		  								show: true,
		  								position: 'top'
		  							}
		  						}
		  					},
		  					{
		  						name:'7病区(血液风湿免疫)',
		  						type:'line',
		  						data: data7,
		  						label: {
		  							normal: {
		  								show: true,
		  								position: 'top'
		  							}
		  						}
		  					}
		  				]
		  			};
		    	  // 为echarts对象加载数据 
		    	  decisionChart.setOption(decisionOption,true); 
		    	}
		);
}

//折现图的下拉
//ajax 异步加载配置数据项  
function choose(magnitude)  
{  
	var chooseMap = new Map();
	if('week'==magnitude){
	  data1 = ['97.5','98.5','99.5','100','98.2','97.8','100','97.2','95.5','96.8','96.8','100'];
   	  data2 = ['98.5','97.5','98.5','99','99.2','98.8','96','97','95.9','99.8','97.8','99.9'];
   	  data3 = ['100','97','98','99.9','99.5','98.5','96.5','97.7','95.5','99.9','97.7','99.9'];
   	  data4 = ['98.8','97.7','98.8','99.9','99.9','98.8','96.6','97.7','95.5','99.9','97.7','99.9'];
   	  data7 = ['97','98','98.5','99.3','97.2','97.8','96.5','97.3','96.9','98.8','100','99.2'];
	}
	if('month'==magnitude){
	  data1 = ['97','98','99','100','98','97','100','97','95','96','96','100'];
   	  data2 = ['98','97','98','99','99','98','96','97','95','99','97','99'];
   	  data3 = ['100','97','98','99','99','98','96','97','95','99','97','99'];
   	  data4 = ['98','97','98','99','99','98','96','97','95','99','97','99'];
   	  data7 = ['97','98','98','99','97','97','96','97','96','98','100','99'];
	}
	if('year'==magnitude){
	  data1 = ['96.5','97.5','98.5','99','98.2','95.8','100','98.2','97.5','99.8','98.8','100'];
   	  data2 = ['98.8','97.7','98.8','99.9','99.9','98.8','96.6','97.7','95.5','99.9','97.7','98.8'];
   	  data3 = ['100','97.7','97.7','98.8','96.6','96.9','96.9','97.9','97.9','98.6','98.9','99.2'];
   	  data4 = ['99.5','98.9','97.9','97.5','99.1','99.4','96.9','99.7','96.5','99.6','97.3','99.3'];
   	  data7 = ['97.6','98.6','98.9','97.3','96.5','98.3','96.9','98.3','96.9','97.8','100','99.2'];
	}
   	  chooseMap.put("data1",data1);
   	  chooseMap.put("data2",data2);
   	  chooseMap.put("data3",data3);
   	  chooseMap.put("data4",data4);
   	  chooseMap.put("data7",data7);
	return chooseMap;
// 	decisionChart.setOption(decisionOption,true);  
    /* $.ajax({  
        url: '/Home/GetJson?year='+year,  
        type: 'get',  
        dataType: 'json',  
        async: false,  
        success: function (result) {  
            if (result)  
            {  
                var option = MYCHART._option;  //e2中感觉这个命名十分奇怪，居然要这样获取，必须先设置才有这个属性  
                debugger  
                if (result.seriesData == null) {  
                    option.series[0].data = [''];  
                    option.xAxis[0].data = ['']  
                }  
                else {  
                    option.series[0].data = result.seriesData;  
                    option.xAxis[0].data = result.xAxisData;  
                }  
                MYCHART.setOption(option,true);  
            }  
        },  
        error: function ()  
        {  
            alert("不好意思请求失败了");  
        }  
    }); */ 
} 
//确诊人数
function putMap(m,y)  
{  
	var mapDetail1 = new Map();
	if(m=="2"){
    mapDetail1.put("0","142");
    mapDetail1.put("1","180");
    mapDetail1.put("2","156");
    mapDetail1.put("3","129");
    mapDetail1.put("4","147");
    mapDetail1.put("5","142");
    mapDetail1.put("6","180");
    mapDetail1.put("7","156");
    mapDetail1.put("8","129");
    mapDetail1.put("9","147");
	}else if(m=="3"){
    mapDetail1.put("0","123");
    mapDetail1.put("1","197");
    mapDetail1.put("2","169");
    mapDetail1.put("3","145");
    mapDetail1.put("4","158");
    mapDetail1.put("5","123");
    mapDetail1.put("6","197");
    mapDetail1.put("7","169");
    mapDetail1.put("8","145");
    mapDetail1.put("9","158");
	}else{
    mapDetail1.put("0","145");
    mapDetail1.put("1","136");
    mapDetail1.put("2","154");
    mapDetail1.put("3","187");
    mapDetail1.put("4","168");
    mapDetail1.put("5","145");
    mapDetail1.put("6","136");
    mapDetail1.put("7","154");
    mapDetail1.put("8","187");
    mapDetail1.put("9","168");
	}
    return mapDetail1;
}
//7日确诊数
function putAllMap(m,y)  
{  
	var mapDetail1 = new Map();
	if(m=="2"){
    mapDetail1.put("0","141");
    mapDetail1.put("1","178");
    mapDetail1.put("2","154");
    mapDetail1.put("3","123");
    mapDetail1.put("4","146");
    mapDetail1.put("5","141");
    mapDetail1.put("6","178");
    mapDetail1.put("7","154");
    mapDetail1.put("8","123");
    mapDetail1.put("9","146");
	}else if(m=="3"){
    mapDetail1.put("0","121");
    mapDetail1.put("1","196");
    mapDetail1.put("2","157");
    mapDetail1.put("3","137");
    mapDetail1.put("4","156");
    mapDetail1.put("5","121");
    mapDetail1.put("6","196");
    mapDetail1.put("7","157");
    mapDetail1.put("8","137");
    mapDetail1.put("9","156");
	}else{
    mapDetail1.put("0","143");
    mapDetail1.put("1","132");
    mapDetail1.put("2","149");
    mapDetail1.put("3","182");
    mapDetail1.put("4","164");
    mapDetail1.put("5","143");
    mapDetail1.put("6","132");
    mapDetail1.put("7","149");
    mapDetail1.put("8","182");
    mapDetail1.put("9","164");
	}
    return mapDetail1;
}
//找出map中的最大值
function maxNum(whatMap){
	var len = whatMap.size();
	if(len>0){
		var temp = 0;
		for(var i = 5;i<len+4;i++){
			if(whatMap.get(i)>temp){
				temp = whatMap.get(i);
			}
		}
		return temp;
	}
}
//找出map中的最大值
function maxNum(whatMap){
	var len = whatMap.size();
	if(len>0){
		var temp = 0;
		for(var i = 5;i<len+4;i++){
			if(whatMap.get(i)>temp){
				temp = whatMap.get(i);
			}
		}
		return temp;
	}
}
//合计
function countNum(countMap){
	var len = countMap.size();
	if(len>0){
		var temp = 0;
		for(var i = 5;i<len+4;i++){
				temp += Number(countMap.get(i));
			}
	return temp;
	}
}
//添加
function addTable(){
	var month = $("#monthSelect").combobox("getValue");
	var year = $("#yearSelect").combobox("getValue");
    mapDetail = putMap(month,year);
    mapAll = putAllMap(month,year);
    detailMax = maxNum(mapDetail);
    allMax = maxNum(mapAll);
    detailCount = countNum(mapDetail);
    allCount = countNum(mapAll);
	var checkHave = $("#checkHave").val();
	var trs = $("#table").find("tr");
	var trs1 = $("#table1").find("tr");
	var  havYear = trs.eq(2).find("td");
	var  havMonth = trs.eq(3).find("td");
	var  havYear1 = trs1.eq(2).find("td");
	var  havMonth1 = trs1.eq(3).find("td");
	//第一次加载界面
	if(checkHave==1){
		$("#checkHave").val(1+Number(checkHave));
		return;
	}
	for(var i=0; i<havYear.length ;i++){
		var m1=havMonth.eq(i).find("input").val()
		if(year==havYear.eq(i).find("input").val()){
			if(month==havMonth.eq(i).find("input").val()){
				$.messager.alert("操作提示","该时间已选择");
				return;				
			}
		}
	}
	$("#checkHave").val(1+Number(checkHave));
	
	var spanNum = $('#chooseTime').attr('colSpan');
	$('#chooseTime').attr('colSpan',spanNum+3);
	$('#tableTitle').attr('colSpan',spanNum+3);
	trs.each(function(index,element){
		if(index==4){
			var html = "<td>确诊病人数</td>";
			html+="<td>七日内确诊数</td>";
			html+="<td>七日内确诊率</td>";
			$(this).append(html);
		}else if(index > 4 && index < 10){
			var html = "<td><div id='' style='width:100%'><div style='width:"+mapDetail.get(index)/detailMax*100+"%;background-color:#00BFFF'>"+mapDetail.get(index)+"</div></div></td>";
			html+="<td><div align='center'>"+mapAll.get(index)+"</div></td>";
			html+="<td><div id='' style='width:100%'><div style='width:"+mapAll.get(index)/allMax*100+"%;background-color:#53CA22'>"+(mapAll.get(index)/mapDetail.get(index)*100).toFixed(2)+"%</div></div></td>";
			$(this).append(html);
		}else if(index == 10){
			var html = "<td><div id='' style='width:100%'><div style='width:100%;background-color:#00BFFF'>"+detailCount+"</div></div></td>";
			html+="<td><div align='center'>"+allCount+"</div></td>";
			html+="<td><div id='' style='width:100%'><div style='width:100%;background-color:#53CA22'>"+(allCount/detailCount*100).toFixed(2)+"%</div></div></td>";
			$(this).append(html);
		}else if(index == 3){
			var html = "<td colspan='3'><input value='"+month+"' type='hidden'/><a  class='easyui-linkbutton' data-options=\"iconCls:'icon-opera_clear',plain:true\" onclick='delTable(this)'>"+month+"月</a></td>";
			$(this).append(html);
			$.parser.parse($(this));
		}else if(index == 2){
			var html = "<td  colspan='3'><input value='"+year+"' type='hidden'/>"+year+"年</td>";
			$(this).append(html);
		}
	});
	trs1.each(function(index,element){
	 if(index >= 0 && index <=10){
			var html = "<td><div id='' style='width:100%'><div style='width:"+mapDetail.get(index)/detailMax*100+"%;background-color:#00BFFF'>"+mapDetail.get(index)+"</div></div></td>";
			html+="<td><div align='center'>"+mapAll.get(index)+"</div></td>";
			html+="<td><div id='' style='width:100%'><div style='width:"+mapAll.get(index)/allMax*100+"%;background-color:#53CA22'>"+(mapAll.get(index)/mapDetail.get(index)*100).toFixed(2)+"%</div></div></td>";
			$(this).append(html);
		}else if(index == 10){
			var html = "<td><div id='' style='width:100%'><div style='width:100%;background-color:#00BFFF'>"+detailCount+"</div></div></td>";
			html+="<td><div align='center'>"+allCount+"</div></td>";
			html+="<td><div id='' style='width:100%'><div style='width:100%;background-color:#53CA22'>"+(allCount/detailCount*100).toFixed(2)+"%</div></div></td>";
			$(this).append(html);
		}
	});
}

//除去年份
function delTable(obj){
	var checkHave = $("#checkHave").val();
	//第一次加载界面
	if(checkHave==2){
		$.messager.alert("操作提示","请至少保留一个时间数据");
		return;
	}
	$("#checkHave").val(checkHave-1);
	
	var indexCell = $(obj).parents("td")[0].cellIndex;
	var table = document.getElementById("table");
	var table1 = document.getElementById("table1");
	var len = table.rows.length;
	table.rows[2].deleteCell(indexCell);
	table.rows[3].deleteCell(indexCell);
	table.rows[4].deleteCell((indexCell+1)*3-2);
	table.rows[4].deleteCell((indexCell+1)*3-2);
	table.rows[4].deleteCell((indexCell+1)*3-2);
	table.rows[5].deleteCell((indexCell+1)*3-1);
	table.rows[5].deleteCell((indexCell+1)*3-1);
	table.rows[5].deleteCell((indexCell+1)*3-1);
	
	for(var i=6; i<len; i++){
		table.rows[i].deleteCell((indexCell+1)*3-2);
		table.rows[i].deleteCell((indexCell+1)*3-2);
		table.rows[i].deleteCell((indexCell+1)*3-2);
	}

	var spanNum = $('#chooseTime').attr('colSpan');
	$('#chooseTime').attr('colSpan',spanNum-3);
	$('#tableTitle').attr('colSpan',spanNum-3);
// 	table.rows[4].deleteCell(index);
}

</script>
</head>
<body>
	<div id="cc" class="easyui-layout sevenConfirmed" data-options="fit:true"> 
<!-- 	    <div style="height:4%;width: 100%;border:0"> -->
<!-- 	    	  <table style="width:100%;height:100%;border:0 !important" class="tableCss"> -->
<!-- <!-- 	    	  	<tr  class="TDlabel1"><th style="font-size:15px" class="changecolor">七日确诊决策分析</th></tr> -->
<!-- 	    	  </table> -->
<!-- 	    </div> -->
	    <div id="right"  style="height:150px;width: 100%;  "  overflow:auto>
	    	  <table style="width:100%;height:100%;" id="table"  class="tableCss" data-options="fit:true">
	    	  	  <tr  class="TDlabel"><th id="tableTitle" colspan="5" style="text-align: left;">决策分析表</th></tr>
		    	  <tr>
	    	  		<th rowspan="3" colspan="2" id=hisName><input id="checkHave" value="1" type="hidden"/>
	    	  		</th>
	    	  		<td colspan="3" id="chooseTime" class="TDlabel">
	    	  			<div   style="text-align: left;">
				    	<input class="easyui-combobox" id="yearSelect" 
			                   data-options="valueField: 'value',textField: 'label',data: [{  
			                   label: '2015年',  
			                   value: '2015',  
			                   selected:true},   //把日计量做为默认值  
			                   {label: '2016年',  
			                   value: '2016'},
			                   {label: '2017年',  
			                   value: '2017'}]"                    
			                 /> 
				    	<input class="easyui-combobox" id="monthSelect"   
			                   data-options="valueField: 'value',textField: 'label',data: [{  
			                   label: '1月',  
			                   value: '1',  
			                   selected:true},   //把日计量做为默认值  
			                   {label: '2月',  
			                   value: '2'},
			                   {label: '3月',  
			                   value: '3'},
			                   {label: '4月',  
			                   value: '4'},
			                   {label: '5月',  
			                   value: '5'},
			                   {label: '6月',  
			                   value: '6'},
			                   {label: '7月',  
			                   value: '7'},
			                   {label: '8月',  
			                   value: '8'},
			                   {label: '9月',  
			                   value: '9'},
			                   {label: '10月',  
			                   value: '10'},
			                   {label: '11月',  
			                   value: '11'},
			                   {label: '12月',  
			                   value: '12'}]"                    
			                 /> 
				    	</div>
	    	  		</td>
	    	  	</tr>
	    	  	<tr>
	    	  		<td  id="chooseYear" colspan="3"><input  value="2015" type="hidden"/>2015年</td>
	    	  	</tr>
	    	  	<tr>
	    	  		<td  colspan="3"><input value="1" type="hidden" /><a  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true" onclick="delTable(this)">1月</a></td>
	    	  		
	    	  	</tr>
		   		<tr class="TDlabel">
		   			<td id = "depName" style="width:171px">
		   				<div>
					    	<input class="easyui-combobox" id="depSelect"   
				                   data-options="valueField: 'value',textField: 'label',data: [{  
				                   label: '--科室名称--',  
				                   value: '',  
				                   selected:true},  
				                   {label: '内科',  
				                   value: 'inner'},
				                   {label: '外科',  
				                   value: 'surgery'},
				                   {label: '骨科',  
				                   value: 'bone'}]"                    
				                 /> 
				    	</div>
		   			</td>
		   			<td id="wardName"  style="width:168px">
		   				入院病区
		   			</td>
		   			<td>
		   				确诊病人数
		   			</td>
		   			<td>
		   				七日内确诊数
		   			</td>
		   			<td>
		   				七日内确诊率
		   			</td>
		   		</tr>
		   </table>
		  <!--  <table style="width:100%;height:2%;"><tr ><td></td></tr></table> -->
	    </div> 
	   <div id="right" style="width:100%;height:27%;overflow-y:scroll;overflow-x:hidden" >
	    	 <table style="width:1920px;height:100%;" id="table1"  class="tableCss" >
		   		<tr >
		   			<td rowspan="5" style="width:171px">
		   				内科
		   			</td>
		   			<td style="width:168px">
		   				<span>1病区(重症监护新生儿)</span>
		   			</td>
		   			<td>
		   				<div id="" ><div style="width:81%;background-color:#00BFFF">167</div></div>
		   			</td>
		   			<td>
						<div align="center">166</div>
		   			</td>
		   			<td>
		   				<div id="" style="width:100%"><div style="width:100%;background-color:#53CA22">99.40%</div></div>
		   			</td>
		   		</tr>
		   		<tr>
		   			<td>
		   				<span>2病区(呼吸消化)</span>
		   			</td>
		   			<td>
		   				<div id="" style="width:100%"><div style="width:100%;background-color:#00BFFF">206</div></div>
		   			</td>
		   			<td>
		   				<div align="center">201</div>
		   			</td>
		   			<td>
		   				<div id="" style="width:100%"><div style="width:98.13%;background-color:#53CA22">97.57%</div></div>
		   			</td>
		   		</tr>
		   		<tr>
		   			<td>
		   				<span>3病区(神经内分泌)</span>
		   			</td>
		   			<td>
		   				<div id="" style="width:100%"><div style="width:89.81%;background-color:#00BFFF">185</div></div>
		   			</td>
		   			<td>
		   				<div align="center">180</div>
		   			</td>
		   			<td>
		   				<div id="" style="width:100%"><div style="width:97.89%;background-color:#53CA22">97.30%</div></div>
		   			</td>
		   		</tr>
		   		<tr>
		   			<td>
		   				<span>4病区(心血管肾脏)</span>
		   			</td>
		   			<td>
		   				<div id="" style="width:100%"><div style="width:71.40%;background-color:#00BFFF">147</div></div>
		   			</td>
		   			<td>
		   				<div align="center">145</div>
		   			</td>
		   			<td>
		   				<div id="" style="width:100%"><div style="width:99.24%;background-color:#53CA22">98.64%</div></div>
		   			</td>
		   		</tr>
		   		<tr>
		   			<td>
		   				<span>7病区(血液风湿免疫)</span>
		   			</td>
		   			<td>
		   				<div id="" style="width:100%"><div style="width:74.76%;background-color:#00BFFF">154</div></div>
		   			</td>
		   			<td>
		   				<div align="center">148</div>
		   			</td>
		   			<td>
		   				<div id="" style="width:100%"><div style="width:96.68%;background-color:#53CA22">96.10%</div></div>
		   			</td>
		   		</tr>
		   		<tr>
		   			<td colspan="2">
		   				内科 合计
		   			</td>
		   			<td>
		   				<div id="" style="width:100%"><div style="width:100%;background-color:#00BFFF">859</div></div>
		   			</td>
		   			<td>
		   				<div align="center">840</div>
		   			</td>
		   			<td>
		   				<div id="" style="width:100%"><div style="width:96.68%;background-color:#53CA22">97.79%</div></div>
		   			</td>
		   		</tr>
		   	
		   </table>
	    </div>
	    <div  style="height:50%;width: 100%">
	    	<div style="height:5%;width:100%;margin-bottom:0px;background:#E0ECFF">
	    		 <table style="width:100%;height:10px;">
		    	  	<tr align="left" class="changeskinbg"><th class="sevenConfirmedFont">决策分析图</th></tr>
		    	  </table>
		    	<input class="easyui-combobox" id="mySelect"  
	                   data-options="valueField: 'value',textField: 'label',data: [{  
	                   label: '七日确诊率',  
	                   value: 'week',  
	                   selected:true},   //把日计量做为默认值  
	                   {label: '月确诊率',  
	                   value: 'month'},
	                   {label: '年确诊率',  
	                   value: 'year'}]"                    
	                 /> 
	    	</div>
	    	<div id="deci" style="height:95%;width:100%" ></div>
	    </div>
	</div> 
</body>
</html>