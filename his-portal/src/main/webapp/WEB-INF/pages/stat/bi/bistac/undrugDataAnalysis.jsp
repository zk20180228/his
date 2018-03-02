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
	<title>非药品收入分析</title>
<style type="text/css">
html,body{
	  overflow-x: hidden;
}
.fo{
	width:25%;
}
.center{text-align:center; }
.MYchar {
	float: left;
}
#content_table{
	height: 95%
}
body:after {
	content: "";
	display: block;
	width: 100%;
	height: 1px;
	position: absolute;
	top: calc(47% - 10px);
	background-color: #26b5b3;
}
body::before {
	content: "";
	display: block;
	width: 1px;
	height:100%;
	position: absolute;
	left: 50%;
	top:50px;
	background-color: #26b5b3;
	z-index: 10000;
}
#cc1 table {
	height: 80%;
}
#cc1 table th {
	height: 30px
}
#cc2 table {
	height: 80%;
}
#cc2 table th {
	height: 30px
}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/echarts/echarts.min.js"></script>
</head>
<body>
	<div id="topTiltle" class="bottomLine" style=" width:100%; height:40px;" >
		<table style="width:100%;height:40px;margin-top:10px" >
			<tr class="monthlyTit">
				<th style="width:70%;padding-left:10px" align="left">
                                               按：
                    <label><input  name='type' type="radio" value='年'  style='margin-left:25px'/> 年</label>
                    <label><input  name='type' type="radio" value='月'  style='margin-left:25px'/> 月</label>
                    <label><input  name='type' type="radio" value='日' checked style='margin-left:25px'/> 日</label>
					时间段：
					<input id="time1" class="Wdate"  onClick="WdatePicker({dateFmt:'yyyy',onpicked:pickedFunc})"  type="text" />
					<input id="time2" class="Wdate"  onClick="WdatePicker({dateFmt:'yyyy-MM',onpicked:pickedFunc})"  type="text" />
					<input id="time3" class="Wdate"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:pickedFunc})"  type="text" />
				</th>
			</tr>
		</table>	
	</div>
	<div id = "content_table" style="margin-top:30px">
		<div id="classify" class="MYchar" style="width: 50%; height: calc(40% - 40px);"></div>
		<div id="roomIncome" class="MYchar" style="width: 25%; height: calc(40% - 40px);"></div>
		<div id="doctorIncome" class="MYchar" style="width: 25%; height: calc(40% - 40px);"></div>
		<div id= "cc1" class="linkRelativeRatioBox MYchar" style="width: 50%;height: calc(60% - 40px);margin-top:30px">
			<div id="linkRelativeRatio" style="width: 100%; height: calc(100% - 100px);"></div>
			<div id="linkRelativeRatioTable" style="height: 100px; width: 90%; margin: 0 auto;">
				<table style=" border-color: #ffffff;background-color: #EAEAEA; width: 100%;text-align: center;" border="1" cellspacing="0" cellpadding="0">
					<thead id = "linkRelativeRatioTableHead"></thead>
					<tbody id = "linkRelativeRatioTableBody"></tbody>
				</table>
			</div>
		</div>
		<div id="cc2" class="onBasisBox MYchar" style="width: 50%;height: calc(60% - 40px);margin-top:30px">
			<div id="onBasis" style="width: 100%; height: calc(100% - 100px);"></div>
			<div id="onBasistable" style="height: 100px; width: 90%; margin: 0 auto;">
				<table style=" border-color: #ffffff;background-color: #EAEAEA; width: 100%;text-align: center;" border="1" cellspacing="0" cellpadding="0">
					<thead id = "onBasistableHead" >
					</thead>
					<tbody id = "onBasistableBody">
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>

<script type="text/javascript">
var yTime="${YTime}";
var mTime="${MTime}";
var dTime="${DTime}";
/*
 * 时间控件的点击事件
 */
	function pickedFunc(){
	var type=$("input[name='type']:checked").val();
	if(type=="年"){
	var date=$("#time1").val();
	}else if(type=="月"){
	var date=$("#time2").val();	
	}else if(type=="日"){
	var date=$("#time3").val();	
	}
		start(type,date);
	}
    $(function(){
    	$('input:radio[name="type"]').change(function(){
    		var newValue=$("input[name='type']:checked").val();
    		if(newValue=='年'){
				$("#time1").show();
				$("#time2").hide();
				$("#time3").hide();
				$("#time1").val(yTime);
				start("年",yTime);
			}else if(newValue=='月'){
				$("#time1").hide();
				$("#time2").show();
				$("#time3").hide();
				$("#time2").val(mTime);
				start("月",mTime);
			}else if(newValue=='日'){
				$("#time1").hide();
				$("#time2").hide();
				$("#time3").show();
				$("#time3").val(dTime);
				start("日",dTime);
			}
    	});
    	$("#time1").hide();
		$("#time2").hide();
		$("#time3").show();
		$("#time3").val(dTime);
    	start("日",dTime);
    });
    function start(type,date){
    	$("#classify").setLoading("classifysed")
    	$("#roomIncome").setLoading("roomIncomesed")
    	$("#doctorIncome").setLoading("doctorIncomesed")
    	$("#cc1").setLoading("linkRelativeRatiosed")
    	$("#cc2").setLoading("onBasissed") 
    	$.ajax({ 
 		   url:"<%=basePath%>statistics/undrugAnalysis/queryUndrugData.action",
 		   type:"post",
 		   data: {type:type,SearchTime:date},
           success: function(data) {
        	   //初始化表格
        	   var linkRelativeRatioTableHead = $("#linkRelativeRatioTableHead");
			   var linkRelativeRatioTableBody = $("#linkRelativeRatioTableBody");
				
				var onBasistableHead = $("#onBasistableHead");
				var onBasistableBody = $("#onBasistableBody");
				linkRelativeRatioTableHead.html(null);
				linkRelativeRatioTableBody.html(null);
				onBasistableHead.html(null);
				onBasistableBody.html(null);
        	   var classify = $("#classify"),
        	   roomIncome = $("#roomIncome"),
        	   doctorIncome =  $("#doctorIncome"),
        	   linkRelativeRatio = $("#cc1"),
        	   onBasis =$("#cc2");
        	   
        	    classify.rmoveLoading("classifysedload")
				roomIncome.rmoveLoading("roomIncomesedload")
				doctorIncome.rmoveLoading("doctorIncomesedload")
				linkRelativeRatio.rmoveLoading("linkRelativeRatiosedload")
				onBasis.rmoveLoading("onBasissedload") 
        	   
        	   data[0][1].length == 0 && (classify.setLoading({
        		   id :"classifysedload",
        		   isImg:false,
        		   text:"暂无数据",
        		   opacity:1,
        		   backgroudColor:"#ffffff"
        	   }))
			   data[0][2].length == 0 && (roomIncome.setLoading({
        		   id :"roomIncomesedload",
        		   isImg:false,
        		   text:"暂无数据",
        		   opacity:1,
        		   backgroudColor:"#ffffff"
        	   }))
        	   data[0][3].length == 0 && (doctorIncome.setLoading({
        		   id :"doctorIncomesedload",
       			   isImg:false,
           		   text:"暂无数据",
         		   opacity:1,
    		 	   backgroudColor:"#ffffff"
        	   }))
        	   data[0][4].length == 0 && (linkRelativeRatio.setLoading({
        		   id :"linkRelativeRatiosedload",
        		   isImg:false,
        		   text:"暂无数据",
        		   opacity:1,
        		   backgroudColor:"#ffffff"
        	   }))
        	   
        	   
        	   data[0][5].length == 0 && (onBasis.setLoading({
        		   id :"onBasissedload",
        		   isImg:false,
        		   text:"暂无数据",
        		   opacity:1,
        		   backgroudColor:"#ffffff" 
        	   }))
        	   var data1 = data[0][1]
        	   var data1Data = [];
        	   var datatitle = []
        	   for(var i = 0 ;i<data1.length;i++ ){
        		   data1Data.push({
        			   name:data1[i].feeType,
        			   value:Number(data1[i].total/10000).toFixed(2)
        		   })
        		   datatitle.push(data1[i].feeType)
        	   }
        	   charAll.classify.setOption({
	       			series: [{
	    				data:data1Data
	    			}],
	    			legend:{
	       				data:datatitle
	       			}
				});
        	   var data2 = data[0][2]
        	   var data2x = []
        	   var data2y = []
        	   for(var j = 0 ;j<data2.length;j++){
        		   data2y.push((data2[j].total/10000).toFixed(2))
        		   data2x.push(data2[j].deptName)
        	   }
        	  	charAll.roomIncome.setOption({
	       			xAxis: {
	       				data: data2x
	       			},
	       			series: [{
	       					data: data2y
	       				}]
       			}); 
        	   
        	  	var data3 = data[0][3]
        	  	var data3x = []
        	  	var data3y = []
	         	   for(var k = 0 ;k<data3.length;k++){
	         		   data3y.push((data3[k].total/10000).toFixed(2))
	        		   data3x.push(data3[k].docName)
	        	   }
        		charAll.doctorIncome.setOption({
	       			xAxis: {
	       				data: data3x
	       			},
	       			series: [{
	       					data: data3y
	       				}]
       			});
        		
        		var data4   = data[0][4]
        	  	var data4x  = []
        	  	var data4y  = []
        		var databai = []
        		var data4strhead = "<th></th>"
        		var data4strvalue = "<tr><td>总收入(万元)</td>"
        		for(var l = data4.length-1 ;l>=1;l--){
	        		   data4x.push(data4[l-1].name)
	        		   data4y.push(((data4[l-1].total-0)/10000).toFixed(2))
	        		   if((data4[l].total-0)!=0){
	         		   databai.push(((data4[l-1].total-data4[l].total)/data4[l].total*100).toFixed(2) +"%")
	        		   }else{
	        		   databai.push("--");	   
	        		   }
	         			data4strhead+="<th style=\"text-align: center;\">"+data4[l-1].name+"</th>"
	         			data4strvalue+="<td>"+((data4[l-1].total-0)/10000).toFixed(2)+"</td>"
	        	   }
        		data4strvalue+="</tr><tr><td>环比</td>"
       			for(var ll = 0 ;ll<=databai.length-1;ll++){
       				data4strvalue+="<td>" +databai[ll] +"</td>"
           		}
        		charAll.linkRelativeRatio.setOption({
	       			xAxis: {
	       				data: data4x
	       			},
	       			series: [{
	       					data: data4y
	       				}]
       			});
        		
        		linkRelativeRatioTableHead.html(data4strhead);
				linkRelativeRatioTableBody.html(data4strvalue);
        		
        		var data5 = data[0][5]
        	  	var data5x = []
        	  	var data5y = []
        		var databai2 = []
        		var data5strhead = "<th></th>"
        		var data5strvalue = "<tr><td>总收入(万元)</td>"
        		for(var o = data5.length-1;o >=1 ;o-- ){
        			data5x.push(data5[o-1].name)
	        		data5y.push(((data5[o-1].total-0)/10000).toFixed(2))
	        		if((data5[o].total-0)!=0){
     	        	 databai2.push(((data5[o-1].total-data5[o].total)/data5[o].total*100).toFixed(2)+"%")
	        		}else{
	        		 databai2.push("--")
	        		}
	        	 	data5strhead+="<th style=\"text-align: center;\">"+data5[o-1].name+"</th>"
	        	 	data5strvalue+="<td>"+((data5[o-1].total-0)/10000).toFixed(2)+"</td>"
        		}
        		data5strvalue+="</tr><tr><td>同比</td>"
        		for(var oo = 0 ;oo<=databai2.length-1;oo++){
        			data5strvalue+="<td>" +databai2[oo] +"</td>"
        		}
        		data5strvalue +="</tr>"
        		charAll.onBasis.setOption({
	       			xAxis: {
	       				data: data5x
	       			},
	       			series: [{
	       					data: data5y
	       				}]
       			});
        		
        		onBasistableHead.html(data5strhead);
				onBasistableBody.html(data5strvalue);
				
				classify.rmoveLoading("classifysed")
				roomIncome.rmoveLoading("roomIncomesed")
				doctorIncome.rmoveLoading("doctorIncomesed")
				linkRelativeRatio.rmoveLoading("linkRelativeRatiosed")
				onBasis.rmoveLoading("onBasissed") 	
   		}
           
    });

    }
</script>

<script type="text/javascript">
var charAll = {
		classify: echarts.init(document.getElementById('classify')), // 药品分类
		roomIncome: echarts.init(document.getElementById('roomIncome')), // 科室收入
		doctorIncome: echarts.init(document.getElementById('doctorIncome')), // 医生收入
		linkRelativeRatio: echarts.init(document.getElementById('linkRelativeRatio')), // 环比
		onBasis: echarts.init(document.getElementById('onBasis')) // 同比
	};
	
	//初始化
	(function(charAll) {
		var classifyoption = {

			title: {
				text: '非药品收入各费别占比(单位：万元)',
				x: 'center',
				y: "top"
			},
			tooltip: {
				trigger: 'item',
				formatter: "{a} <br/>{b} : {c} ({d}%)"
			},
			legend: {
				orient: 'vertical',
				x: 'left',
				data: [],
				padding:[20, 0, 0, 20]
			},
		    calculable : true,
			series: [{
				name: '非药品收入各费别占比',
				type: 'pie',
				radius: '60%',
				center: ['50%', '55%'],
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

			}],
			color:[ "#CCA6D9", "#ECE579", "#E1B070",'#A3E874','#68C2FF','#9C6DBB','#46EE79','#E4C0FE','#FDB12A','#FF9190','#FFB434','#CE4855', '#79C2F5', '#FF9190','#5DD251'],  

		}
		charAll.classify.setOption(classifyoption);

		var roomIncomeoption = {
				title: {
					text: '科室收入TOP5(单位：万元)',
					x: 'center',
					y: "top"
				},
				tooltip: {
					trigger: 'axis'
				},
				xAxis: [{
					type: 'category',
					data: []
				}],
				yAxis: [{
					name:'收入(万元)',
					type: 'value'
				}],grid: {
			        left: '3%',
			        right: '4%',
			        bottom: '3%',
			        containLabel: true
			    },label:{
			    	normal:{
			    		show: true,
			    		position: 'inside'
			    		}},
				series: [{
						name: '科室收入TOP5(单位：万元)',
						type: 'bar',
					    barWidth : 45,//柱图宽度
				        barMaxWidth:45,//最大宽度
						data: [],
						itemStyle: {
						    normal: {
						    	color:"#FFD06C"
						    }
						}
					}
				
				], 
				animationEasing: 'elasticOut',
		}
		charAll.roomIncome.setOption(roomIncomeoption);
		var doctorIncomeoption = {
			title: {
				text: '医生收入TOP5(单位：万元)',
				x: 'center',
				y: "top"
			},
			tooltip: {
				trigger: 'axis'
			},
			xAxis: [{
				type: 'category',
				data: []
			}],
			yAxis: [{
				name:'收入(万元)',
				type: 'value'
			}],grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },label:{
		    	normal:{
		    		show: true,
		    		position: 'inside'
		    		}},
			series: [{
					name: '医生收入TOP5(单位：万元)',
					type: 'bar',
					barWidth : 45,//柱图宽度
			        barMaxWidth:45,//最大宽度,
					data: [],
					itemStyle: {
					    normal: {
					    	color:"#79C2F5"
					    }
					}
				}
			],
			animationEasing: 'elasticOut',
		}
		charAll.doctorIncome.setOption(doctorIncomeoption);
		var linkRelativeRatiooption = {
			title: {
				text: '环比(单位：万元)',
				x: 'center',
				y: "top"
			},
			tooltip: {
				trigger: 'axis'
			},
			xAxis: [{
				type: 'category',
				data: []
			}],
			yAxis: [{
				name:'收入(万元)',
				type: 'value'
			}],grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },label:{
		    	normal:{
		    		show: true,
		    		position: 'inside'
		    		}},
			series: [{
					name: '非药品收入环比(单位：万元)',
					type: 'bar',
					barWidth : 50,//柱图宽度
			        barMaxWidth:50,//最大宽度,
					data: [],
					itemStyle: {
					    normal: {
					    	color: function(params) {
								var colorList = [
									'#FF9190', '#FDB12A', '#79C2F5', '#A3E874', '#27727B', "#CCA6D9", "#ECE579", "#E1B070"
								];
								return colorList[params.dataIndex]
							}
					    }
					}
				}
			
			],animationEasing: 'elasticOut',
		}
		charAll.linkRelativeRatio.setOption(linkRelativeRatiooption);
		var onBasisoption = {
				title: {
					text: '同比(单位：万元)',
					x: 'center',
					y: "top"
				},
				tooltip: {
					trigger: 'axis'
				},
				xAxis: [{
					type: 'category',
					data: []
				}],
				yAxis: [{
					name:'收入(万元)',
					type: 'value'
				}],grid: {
			        left: '3%',
			        right: '4%',
			        bottom: '3%',
			        containLabel: true
			    },label:{
			    	normal:{
			    		show: true,
			    		position: 'inside'
			    		}},
				series: [{
						name: '非药品收入同比(单位：万元)',
						type: 'bar',
						barWidth : 50,//柱图宽度
				        barMaxWidth:50,//最大宽度,
						data: [],
						itemStyle: {
						    normal: {
						    	color: function(params) {
									var colorList = [
										'#FF9190', '#FDB12A', '#79C2F5', '#A3E874', '#27727B', "#CCA6D9", "#ECE579", "#E1B070"
									];
									return colorList[params.dataIndex]
								}
						    }
						}
					}
				
				],animationEasing: 'elasticOut',
		}
		charAll.onBasis.setOption(onBasisoption);
	})(charAll)
</script>

