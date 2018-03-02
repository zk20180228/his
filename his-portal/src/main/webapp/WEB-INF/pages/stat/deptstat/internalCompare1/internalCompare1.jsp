<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
var menuAlias = '${menuAlias}';
$(function(){	
	//内科医学部
	var depts1 = "2072";
	//内二医学部
	var depts2 = "2073";
	var endTime = $("#Stime").val();
	$("#table1").datagrid({  
        fit:true,
        rownumbers:true,
        border:true,
        checkOnSelect:true,
        selectOnCheck:false,
        singleSelect:true,
        url: "<%=basePath%>deptstat/internalCompare1/queryInternalCompare1list.action",
        queryParams:{endTime:endTime,dept1:depts1,dept2:depts2},
        onLoadSuccess: function(){

        }
	});
	var time = $("#Stime").val();
	var toTime = time.split("-")[0];
	var toTimeMonth = time.split("-")[1];
	var lastTime = Number(toTime)-1;
	$("#year").val(toTime);
	$("#month").val(toTimeMonth);
	$("#lastYear1").val(lastTime);
	$("#lastYear2").val(lastTime);
	$("#lastYear3").val(lastTime);
	$("#lastYear4").val(lastTime);
	$("#lastYear5").val(lastTime);
	$("#lastYear6").val(lastTime);
	$("#lastYear7").val(lastTime);
	$("#toYear1").val(toTime);
	$("#toYear2").val(toTime);
	$("#toYear3").val(toTime);
	$("#toYear4").val(toTime);
	$("#toYear5").val(toTime);
	$("#toYear6").val(toTime);
	$("#toYear7").val(toTime);
	var dates = time.split('-');
	document.getElementById("changeTime").innerHTML = dates[0] + '年' + dates[1] + '月';
	
	$("#searchForm").click(function(){
		var time = $("#Stime").val();
		$("#table1").datagrid({  
	        fit:true,
	        rownumbers:true,
	        border:true,
	        checkOnSelect:true,
	        selectOnCheck:false,
	        singleSelect:true,
	        url: "<%=basePath%>deptstat/internalCompare1/queryInternalCompare1list.action",
	        queryParams:{endTime:time,dept1:depts1,dept2:depts2},
	        onLoadSuccess: function(){

	        }
		});
		
		var toTime = time.split("-")[0];
		var toTimeMonth = time.split("-")[1];
		var lastTime = Number(toTime)-1;
		$("#year").val(toTime);
		$("#month").val(toTimeMonth);
		$("#lastYear1").val(lastTime);
		$("#lastYear2").val(lastTime);
		$("#lastYear3").val(lastTime);
		$("#lastYear4").val(lastTime);
		$("#lastYear5").val(lastTime);
		$("#lastYear6").val(lastTime);
		$("#lastYear7").val(lastTime);
		$("#toYear1").val(toTime);
		$("#toYear2").val(toTime);
		$("#toYear3").val(toTime);
		$("#toYear4").val(toTime);
		$("#toYear5").val(toTime);
		$("#toYear6").val(toTime);
		$("#toYear7").val(toTime);
		querylist();
	})
	//为查询绑定回车事件
	$(document).keydown(function(e){
		if(e.keyCode == 13){
			$("#searchForm").click();
		}
	})
	//导出
	$("#exportExcel").click(function(){
		var rows=$('#table1').datagrid('getRows');
		if(rows==null||rows.length==0){
			$.messager.alert("提示","没有数据，不能导出！");
			return;
		}else{
		$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
			if (res) {
				$('#exportJson').val(JSON.stringify(rows));
				var date =$('#Stime').val();
				$('#saveForm').form('submit', {/*+"&deptName"+deptName  */
					url :'<%=basePath%>deptstat/internalCompare1/excelPort.action?',
					queryParams:{endTime:date},
					type:'post',
					onSubmit : function() {
						return $(this).form('validate');
					},
					success : function(data) {
						$.messager.alert("提示", "导出失败！", "success");
					},
					error : function(data) {
						$.messager.alert("提示", "导出失败！", "error");
					}
				});
			}
		});
	
	}
	});
})
/**
	* 打印方法
	*/
	function printOut(){
		var Stime = $('#Stime').val();
		if(Stime != ''){
			var rows = $('#table1').datagrid('getRows');
			if(rows.length > 0){
				$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否导出
					if(res){
						$('#reportTime').val(Stime);
						$('#reportJson').val(JSON.stringify(rows));
						//表单提交 target
						var formTarget="reportForm";
						var tmpPath = "<%=basePath%>deptstat/internalCompare1/reportList.action";
						//设置表单target
						$("#reportForm").attr("target",formTarget);
						//设置表单访问路径
						$("#reportForm").attr("action",tmpPath); 
						//表单提交时打开一个空的窗口
						$("#reportForm").submit(function(e){
							var timerStr = Math.random();
							window.open('about:blank',formTarget,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');
							window.close();
						});
						$("#reportForm").submit();
					}
				});
			}else{
				$.messager.alert("提示","当前统计无数据，无法打印！");
				close_alert();
				return;
			}
		}else{
			$.messager.alert("提示","请先选择查询日期！");
			close_alert();
			return;
		}
	}
	
function querylist(){
	var time = $("#Stime").val();
	var dates = time.split('-');
	document.getElementById("changeTime").innerHTML = dates[0] + '年' + dates[1] + '月';
}
function changeTime(){
	var time = $("#Stime").val();
	var toTime = time.split("-")[0];
	var toTimeMonth = time.split("-")[1];
	var lastTime = Number(toTime)-1;
	$("#year").val(toTime);
	$("#month").val(toTimeMonth);
	$("#lastYear1").val(lastTime);
	$("#lastYear2").val(lastTime);
	$("#lastYear3").val(lastTime);
	$("#lastYear4").val(lastTime);
	$("#lastYear5").val(lastTime);
	$("#lastYear6").val(lastTime);
	$("#lastYear7").val(lastTime);
	$("#toYear1").val(toTime);
	$("#toYear2").val(toTime);
	$("#toYear3").val(toTime);
	$("#toYear4").val(toTime);
	$("#toYear5").val(toTime);
	$("#toYear6").val(toTime);
	$("#toYear7").val(toTime);
	querylist();
}

/**
* 重置方法
*/
function clear(){
	$('#Stime').val("${Etime}");
	$("#countList").datagrid('loadData', { total: 0, rows: [] });
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<style type="text/css">
		.panel-body,.panel{
			overflow:visible;
		}
		.addList dl:first-child ul {
			overflow:visible !important; 
			clear:both;
		}
		.clearfix:after{
		    content:"";
		    display:table;
		    height:0;
		    visibility:hidden;
		    clear:both;
		}
		.xmenu dl dd ul{
			overflow:visible !important; 
			clear:both;
		}
		.clearfix{
		*zoom:1;    /* IE/7/6*/
		}
		#hh span{
			font-size: 30;
			font: bold;
			text-align: center;
			padding-top: 5px
		}
</style>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false" style="width: 100%;height:85px;padding:8px 5px 5px 5px">
	<table id="searchTab" style="width: 100%;">
		<tr>
			<td style="width:75px;" align="left">对比时间:</td>
			<td style="width:110px;">
				<input id="Stime" class="Wdate" type="text" value="${Etime}" name="Stime" onClick="WdatePicker({dateFmt:'yyyy-MM',onpicked:changeTime})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
			</td>			
			<td style="text-align: left;padding-left: 10px;">
			<a href="javascript:void(0)"  class="easyui-linkbutton" id="searchForm" data-options="iconCls:'icon-search'">查询</a>
			<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
			<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="printOut()" data-options="iconCls:'icon-printer'">打印</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-down'" id="exportExcel">导出</a>
			</td>
		</tr>
	</table>
	<h5 id="hh" style="font-size: 30;font: bold;text-align: center;padding-top: 5px"><span id="changeTime">2017年04月</span>${title }内科医学部和内二医学部对比表1</h5>
	</div>
	<div data-options="region:'center',border:false" style="width: 100%;">
		<table id="table1" class="easyui-datagrid" data-options="fit:true,fitColumns:true">
			<thead>
				<tr>
					<th data-options="field:'deptName',rowspan:2,width:'10%',halign:'center',align:'left'">部门名称</th>
					<th data-options="field:'obstetricsPerson',rowspan:2,width:'10%',halign:'center',align:'right'">病区负责人</th>
					<th data-options="field:'q',colspan:4,align:'center'">门诊量（人次）</th>
					<th data-options="field:'w',colspan:4,align:'center'">入院人数（人次）</th>
					<th data-options="field:'e',colspan:4,align:'center'">出院人数（人次）</th>
					<th data-options="field:'e',colspan:4,align:'center'">平均住院天数（天）</th>
					<th data-options="field:'e',colspan:4,align:'center'">病床周转次数（次）</th>
					<th data-options="field:'e',colspan:4,align:'center'">床位使用率（%）</th>
					<th data-options="field:'e',colspan:4,align:'center'">危急重抢救成功率（%）</th>
				</tr>
				<tr>
					<th data-options="field:'registerCountPrev',width:'8%',halign:'center',align:'right'"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="lastYear1" value="2016"/>年</th>
					<th data-options="field:'registerCount',width:'8%',halign:'center',align:'right'"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="toYear1" value="2017"/>年</th>
					<th data-options="field:'registerRise',width:'8%',halign:'center',align:'right'">增长数</th>
					<th data-options="field:'registerRisePercent',width:'8%',halign:'center',align:'right'">增长率（%）</th>
					<th data-options="field:'inHospitalCountPrev',width:'8%',halign:'center',align:'right'"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="lastYear2" value="2016"/>年</th>
					<th data-options="field:'inHospitalCount',width:'8%',halign:'center',align:'right'"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="toYear2" value="2017"/>年</th>
					<th data-options="field:'inHospitalRise',width:'8%',halign:'center',align:'right'">增长数</th>
					<th data-options="field:'inHospitalPercent',width:'8%',halign:'center',align:'right'">增长率（%）</th>
					<th data-options="field:'outHospitalCountPrev',width:'8%',halign:'center',align:'right'"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="lastYear3" value="2016"/>年</th>
					<th data-options="field:'outHospitalCount',width:'8%',halign:'center',align:'right'"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="toYear3" value="2017"/>年</th>
					<th data-options="field:'outHospitalRise',width:'8%',halign:'center',align:'right'">增长数</th>
					<th data-options="field:'outHospitalPercent',width:'8%',halign:'center',align:'right'">增长率（%）</th>
					<th data-options="field:'avgInpatientCountPrev',width:'8%',halign:'center',align:'right'"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="lastYear4" value="2016"/>年</th>
					<th data-options="field:'avgInpatientCount',width:'8%',halign:'center',align:'right'"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="toYear4" value="2017"/>年</th>
					<th data-options="field:'avgInpatientRise',width:'8%',halign:'center',align:'right'">增长数</th>
					<th data-options="field:'avgInpatientPercent',width:'8%',halign:'center',align:'right'">增长率（%）</th>
					<th data-options="field:'bedTurnoverCountPrev',width:'8%',halign:'center',align:'right'"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="lastYear5" value="2016"/>年</th>
					<th data-options="field:'bedTurnoverCount',width:'8%',halign:'center',align:'right'"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="toYear5" value="2017"/>年</th>
					<th data-options="field:'bedTurnoverRise',width:'8%',halign:'center',align:'right'">增长数</th>
					<th data-options="field:'bedTurnoverPercent',width:'8%',halign:'center',align:'right'">增长率（%）</th>
					<th data-options="field:'bedUseRatePrev',width:'8%',halign:'center',align:'right'"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="lastYear6" value="2016"/>年</th>
					<th data-options="field:'bedUseRate',width:'8%',halign:'center',align:'right'"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="toYear6" value="2017"/>年</th>
					<th data-options="field:'bedUseRateRise',width:'8%',halign:'center',align:'right'">增长数</th>
					<th data-options="field:'bedUseRatePercent',width:'8%',halign:'center',align:'right'">增长率（%）</th>
					<th data-options="field:'rescueSuccessRatePrev',width:'8%',halign:'center',align:'right'"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="lastYear7" value="20176"/>年</th>
					<th data-options="field:'rescueSuccessRate',width:'8%',halign:'center',align:'right'"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="toYear7" value="2017"/>年</th>
					<th data-options="field:'rescueSuccessRateRise',width:'8%',halign:'center',align:'right'">增长数</th>
					<th data-options="field:'rescueSuccessRatePercent',width:'8%',halign:'center',align:'right'">增长率（%）</th>
				</tr>
			</thead>
		</table>
						<form id="saveForm" method="post">
							<input type="hidden" id="exportJson" name="exportJson">
						</form>
						<form method="post" id="reportForm" >
						<input type="hidden" name="reportTime" id="reportTime" />
						<input type="hidden" name="reportJson" id="reportJson" />
						<input type="hidden" name="fileName" id="fileName" value="internalCompare1"/>
						</form>
	</div>

</body>
</html>