 <%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'center'" style="padding:20px;" id="tubiao">
	    	
	    </div>   
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
	<script type="text/javascript">
		var dataObj='';
		$(function(){
			$.ajax({
				url:'<%=basePath%>nursestation/nurse/getChartList.action',
				async:false,
				success:function(data){
					dataObj=data;
				}
			});
			functione(dataObj);
		})
		function functione(dataObj){
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
		        'echarts/chart/pie', // 使用柱状图就加载bar模块，按需加载
		        'echarts/chart/bar',
		        'echarts/chart/line'
		    ],
		    function (ec) {
		    	  // 基于准备好的dom，初始化echarts图表
		    	  myChart = ec.init(document.getElementById('tubiao')); 
		    	  var option = {
		    			    tooltip : {
		    			        trigger: 'axis'
		    			    },
		    			    toolbox: {
		    			        show : true,
		    			        y: 'bottom',
		    			        feature : {
		    			            mark : {show: true},
		    			            dataView : {show: true, readOnly: false},
		    			            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
		    			            restore : {show: true},
		    			            saveAsImage : {show: true}
		    			        }
		    			    },
		    			    calculable : true,
		    			    legend: {
		    			        data:['占用量','其他量','增长量','占用','其他']
		    			    },
		    			    xAxis : [
		    			        {
		    			            type : 'category',
		    			            splitLine : {show : false},
		    			            data : ['1月','2月','3月','4月','5月','6月','7月']
		    			        }
		    			    ],
		    			    yAxis : [
		    			        {
		    			            type : 'value',
		    			            position: 'right'
		    			        }
		    			    ],
		    			    series : [
		    			        {
		    			            name:'占用量',
		    			            type:'bar',
		    			            data:[320, 332, 301, 334, 390, 330, 320]
		    			        },
		    			        {
		    			            name:'其他量',
		    			            type:'bar',
		    			            tooltip : {trigger: 'item'},
		    			            stack: '广告',
		    			            data:[120, 132, 101, 134, 90, 230, 210]
		    			        },
		    			        {
		    			            name:'增长量',
		    			            type:'line',
		    			            data:[862, 1018, 964, 1026, 1679, 1600, 1570]
		    			        },

		    			        {
		    			            name:'床位状态占用率',
		    			            type:'pie',
		    			            tooltip : {
		    			                trigger: 'item',
		    			                formatter: '{a} <br/>{b} : {c} ({d}%)'
		    			            },
		    			            center: [160,130],
		    			            radius : [0, 50],
		    			            itemStyle :{
		    			                normal : {
		    			                    labelLine : {
		    			                        length : 20
		    			                    }
		    			                }
		    			            },
		    			            data:[
		    			                {value:dataObj[1].bedStatezhan, name:'其他'},
		    			                {value:dataObj[0].bedStatezhan, name:'占用'}
		    			            ]
		    			        }
		    			    ]
		    			};
		    	  // 为echarts对象加载数据 
		    	  myChart.setOption(option); 
		    	}
		);
	}
	</script> 
</body>
</html>