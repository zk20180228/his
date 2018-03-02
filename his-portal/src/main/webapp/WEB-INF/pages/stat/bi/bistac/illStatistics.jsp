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
<%-- <script type="text/javascript" src="<%=basePath %>javascript/js/illStatistics.js"></script> --%>
<script type="text/javascript" src="<%=basePath %>javascript/echarts/newChart/echarts-all-3.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath %>themes/system/css/illStatistics.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
		option1 = {
			tooltip : {
				trigger: 'item',
				formatter: "{a} <br/>{b} : {c} ({d}%)"
			},
			legend: {
				orient: 'vertical',
				x: 'right',
				data:['腹痛','肺恶性肿瘤史','龋齿','耳鸣','甲状腺肿','皮炎','高血压病','鼻炎','胃炎','糖尿病']
			},
			series: [
			{
				name:'患病人群比例',
				type:'pie',
				radius: ['0%', '90%'],
				avoidLabelOverlap: false,
				startAngle:-20,
				label: {
					normal: {
						textStyle: {
							color: '#000'
						}
					}
				},
				labelLine: {
					normal: {
						show: false,
						length:8
					}
				},
				data:[
					{value:3213, name:'腹痛'},
					{value:2333, name:'肺恶性肿瘤史'},
					{value:4333, name:'龋齿'},
					{value:2232, name:'耳鸣'},
					{value:2232, name:'甲状腺肿'},
					{value:2233, name:'皮炎'},
					{value:5132, name:'高血压病'},
					{value:2334, name:'鼻炎'},
					{value:3232, name:'胃炎'},
					{value:3322, name:'糖尿病'}
				]
			}
			],
			color: ['#e0aa82','#e4bbec','#86807a','#5eb765','#34b38f','#edd159','#93a889','#aae0ab','#e7a768','#aa664f']
		};
		option2 = {
			color: ['#5793f3', '#d14a61'],
    		tooltip: {
        		trigger: 'axis',
    		},
    		grid: {
        		left: '5%',
        		right: '4%',
        		bottom: '3%',
        		top: '15%',
        		containLabel: true
    		},
			legend: {
        		data:['男','女']
    		},
    		yAxis: [{
        		type: 'value',
        		name: '男',
        		axisLabel: {
                	formatter: '{value}'
            	}
            },
            {
        		type: 'value',
        		name: '女',
        		axisLabel: {
                	formatter: '{value}'
            	}
            }],
    		xAxis: {
        		type: 'category',
        		axisTick: {
                	alignWithLabel: true
            	},
        		data: ['腹痛','肺恶性肿瘤史','龋齿','耳鸣','甲状腺肿','皮炎','高血压病','鼻炎','胃炎','糖尿病']
    		},
    		series: [{
        		name: '男',
        		type: 'bar',
				data: [6,100,291,314,143,843,2093,645,271,822]
			},
			{
        		name: '女',
        		type: 'bar',
        		yAxisIndex: 1,
				data: [436,800,291,314,143,143,2093,645,271,322]
			}]
		};
		$(function(){
			var myChart1 = echarts.init(document.getElementById('piechart'));  
    		myChart1.setOption(option1, true);
    		var myChart2 = echarts.init(document.getElementById('barchart'));  
    		myChart2.setOption(option2, true);
    		window.addEventListener("resize", function () {
    	        setTimeout(function () {
    	        	myChart1.resize();
    	        	myChart2.resize();
    	        }, 300)
    		});
    		$(".zbzc span").click(function(){
    			var a=$(this).attr("class");
    			$(this).addClass("changebg").siblings().removeClass("changebg");
    			$(".zbzt ."+a).addClass("block").siblings().removeClass("block");
    		});
    		$("body").on("click",".td2",function(){
    	   		$(this).addClass("hblue").siblings().removeClass("hblue").parents().siblings().children("td").removeClass("hblue");
    	    });
		})
	</script>
</head>
<body>
<div class="main">
	<div class="top">
		<table>
			<tr>
				<td width="12%">患者性别</td>
				<td rowspan="3" width="15%" class="td1">请选择不同的年龄段：</td>
				<td rowspan="3" class="zbz">
					<div style="width:873px;overflow:hidden;margin:0 auto;">
					<div class="zbzt">
						<span class="span1 none block">1->5</span>
						<span class="span2 none">6->10</span>
						<span class="span3 none">11->15</span>
						<span class="span4 none">16->20</span>
						<span class="span5 none">21->25</span>
						<span class="span6 none">26->30</span>
						<span class="span7 none">31->35</span>
						<span class="span8 none">36->40</span>
						<span class="span9 none">41->45</span>
						<span class="span10 none">46->50</span>
						<span class="span11 none">51->55</span>
						<span class="span12 none">56->60</span>
						<span class="span13 none">61->65</span>
						<span class="span14 none">66->70</span>
						<span class="span15 none">71->75</span>
						<span class="span16 none">76->80</span>
						<span class="span17 none">81及以上</span>
					</div>
					<div class="zbzc">
						<span class="span1 changebg"></span>
						<span class="span2"></span>
						<span class="span3"></span>
						<span class="span4"></span>
						<span class="span5"></span>
						<span class="span6"></span>
						<span class="span7"></span>
						<span class="span8"></span>
						<span class="span9"></span>
						<span class="span10"></span>
						<span class="span11"></span>
						<span class="span12"></span>
						<span class="span13"></span>
						<span class="span14"></span>
						<span class="span15"></span>
						<span class="span16"></span>
						<span class="span17"></span>
					</div>
					<div class="zbzb">
						<span>0</span>
						<span>5</span>
						<span>10</span>
						<span>15</span>
						<span>20</span>
						<span>25</span>
						<span>30</span>
						<span>35</span>
						<span>40</span>
						<span>45</span>
						<span>50</span>
						<span>55</span>
						<span>60</span>
						<span>65</span>
						<span>70</span>
						<span>75</span>
						<span>80</span>
					</div>
					</div>
				</td>
			</tr>
			<tr>
				<td class="sfont"><input type="checkbox" name="sex" value="男" checked="checked"/>男</td>
			</tr>
			<tr>
				<td class="sfont"><input type="checkbox" name="sex" value="女" />女</td>
			</tr>
		</table>
	</div>
	<div class="center">
		<div class="cleft">
			<h2>患病人群比例分析-TOP10</h2>
			<div id="piechart"></div>
		</div>
		<div class="cright">
			<h2>患病人群分析-TOP10</h2>
			<div id="barchart"></div>
		</div>
	</div>
	<div class="bottom1">
		<table>
			<tr class="tit">
				<td colspan="12">门诊患者疾病年龄分布趋势-C</td>
			</tr>
			<tr class="orange">
				<td rowspan="2"  align="right">年龄</td>
				<td></td>
				<td colspan="10">男</td>
			</tr>
			<tr class="orange">
				<td>总计</td>
				<td>糖尿病</td>
				<td>高血压</td>
				<td>胃炎</td>
				<td>鼻炎</td>
				<td>皮炎</td>
				<td>肺恶性肿瘤史</td>
				<td>多处挫伤</td>
				<td>腹痛</td>
				<td>龋齿</td>
				<td>湿疹</td>
			</tr>
			<tr align="right">
				<td class="orange" width="8%">55</td>
				<td class="bold td2" width="8%">1,265</td>
				<td width="8%" class="td2">502</td>
				<td width="8%" class="td2">160</td>
				<td width="8%" class="td2">94</td>
				<td width="8%" class="td2">132</td>
				<td width="8%" class="td2">86</td>
				<td width="9%" class="td2">26</td>
				<td width="9%" class="td2">76</td>
				<td width="9%" class="td2">60</td>
				<td width="9%" class="td2">61</td>
				<td>68</td>
			</tr>
			<tr align="right">
				<td class="orange">56</td>
				<td class="bold td2">1,467</td>
				<td class="td2">649</td>
				<td class="td2">184</td>
				<td class="td2">104</td>
				<td class="td2">129</td>
				<td class="td2">85</td>
				<td class="td2">36</td>
				<td class="td2">80</td>
				<td class="td2">59</td>
				<td class="td2">73</td>
				<td class="td2">68</td>
			</tr>
			<tr align="right">
				<td class="orange" align="right">57</td>
				<td class="bold td2">1,555</td>
				<td class="td2">726</td>
				<td class="td2">160</td>
				<td class="td2">94</td>
				<td class="td2">162</td>
				<td class="td2">99</td>
				<td class="td2">38</td>
				<td class="td2">75</td>
				<td class="td2">66</td>
				<td class="td2">62</td>
				<td class="td2">73</td>
			</tr>
		</table>
	</div>
</div>
</body>
</html>