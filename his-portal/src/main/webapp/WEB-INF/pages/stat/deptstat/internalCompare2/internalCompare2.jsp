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
</style>
<script type="text/javascript">
var menuAlias ='${menuAlias}';
var deptMap=null;
$(function(){
	var date=new Date();
	var nowDate=date.getFullYear()+'-'+(date.getMonth()+1);
	nowDate=nowDate.replace(/-(\d{1})\b/g,'-0$1');
	$('#Stime').val(nowDate);
	var ss =$('#Stime').val();
	var strs= new Array(); //定义一数组 
	strs=ss.split("-"); //字符分割 
	var year =strs[0]+'年';
	var month =strs[1]+'月';
	var yeared =strs[0]-1;
	$('#lastYear1').val(yeared);
	$('#toYear1').val(strs[0]);
	$('#lastYear2').val(yeared);
	$('#toYear2').val(strs[0]);
	$('#lastYear3').val(yeared);
	$('#toYear3').val(strs[0]);
	//查询住院科室（渲染）
	$.ajax({
		url:'<%=basePath%>statistics/ChargeBill/queryZYDept.action',
		success:function(data){
			deptMap=data;
		}
	});
	$('#m1').find("[name='menu-confirm']").find('.a-btn-text').click(function(){
		var texts=new Array();
		var iflag = 0;
		$("#m1 .select-info ul li").each(function(key,value){					
			if($(this).attr("rel") != "none"){
				texts[iflag] = $(this).html();
				iflag++;
			}
		});
		if(texts.length!=1){
			$.messager.alert('提示','科室只能选择一个!');
			setTimeout(function(){
				$('#ksnew1').val("");
				$('#ksnew1').attr("name","");
			},1500);
			return ;
		}else{
			$('#d1').html(texts);
		}
		
	})
	$('#m2').find("[name='menu-confirm']").find('.a-btn-text').click(function(){
		var texts=new Array();
		var iflag = 0;
		$("#m2 .select-info ul li").each(function(key,value){					
			if($(this).attr("rel") != "none"){
				texts[iflag] = $(this).html();
				iflag++;
			}
			if(texts.length!=1){
				$.messager.alert('提示','科室只能选择一个!');
				setTimeout(function(){
					$('#ksnew2').val("");
					$('#ksnew2').attr("name","");
				},1500)
				return ;
			}else{
				$('#d2').html(texts);
			}
		}); 
	})
	$('#changeTime').html(year+month);

})
	function changeTime(){
		var ss =$('#Stime').val();
		var strs= new Array(); //定义一数组 
		strs=ss.split("-"); //字符分割 
		var year =strs[0]+'年';
		var month =strs[1]+'月';
		$('#changeTime').html(year+month);
		var time = $("#Stime").val();
		var yeared =strs[0]-1;
		$('#lastYear1').val(yeared);
		$('#toYear1').val(strs[0]);
		$('#lastYear2').val(yeared);
		$('#toYear2').val(strs[0]);
		$('#lastYear3').val(yeared);
		$('#toYear3').val(strs[0]);
	querylist()
	}
	function querylist(){
		
		var Stime = $('#Stime').val();
		var strs= new Array(); //定义一数组 
		strs=Stime.split("-"); //字符分割 
		var yeared =strs[0]-1;
		$('#table1').datagrid({
			url:'<%=basePath%>deptstat/internalCompare2/queryinternalCompare2listHJ.action',
			queryParams:{
				deptCode1: '2072',
				deptCode2: '2073',
				Stime:Stime
			},
			singleSelect:true,
			method:'post',
		});
		$('#lastYear1').val(yeared);
		$('#toYear1').val(strs[0]);
		$('#lastYear2').val(yeared);
		$('#toYear2').val(strs[0]);
		$('#lastYear3').val(yeared);
		$('#toYear3').val(strs[0]);
	}
	function formatdeptId(value,row,index){
		if(value!=null&&value!=''){
			if('combobox0'==value){
				return '内科医学部';
			}
			if('combobox1'==value){
				return '内二医学部';
			}
			if(deptMap[value]){
				return deptMap[value];
			}else {
				return value;
			}
			
		}
	}
	/***
	 * 导出功能
	 */
	function queryBottenWeek(){
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
						url :'<%=basePath%>deptstat/internalCompare2/excelPort.action?',
						queryParams:{date:date},
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
	}
	//打印
	function printOut(){
		var rows=$('#table1').datagrid('getRows');
		if(rows==null||rows.length==0){
			$.messager.alert("提示","没有数据，不能打印！");
			return;
		}else{
		$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否导出
		window.open ("<%=basePath %>deptstat/internalCompare2/printinternalCompare2list.action?Stime="+$('#Stime').val()+"&deptCode1=2072&deptCode2=2073",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	
			});
			}
		}
	function formateCost(value,row,index){
		if(row.deptCode=='combobox0'||row.deptCode=='combobox1'){
		
		}else{
			if(''!=value){
					return parseFloat(parseFloat(value).toFixed(2)).toLocaleString();
			}else {
				return 0.0;
			}
		}
		
	}
	function formateSpace(value,row,index){
		if(row.deptCode=='combobox0'||row.deptCode=='combobox1'){
			return '';
		}
		if(value=='0'||value!=''){
			return value+'%';
		}
	}
	//金额格式
	function changeCost(manny) {
		if(manny != null) {
			var value = manny/10000;
			value = parseFloat((value + "").replace(/[^\d\.-]/g, "")).toFixed(2) + "";
			var l = value.split(".")[0].split("").reverse(),
				r = value.split(".")[1],
				t = "";
			for(i = 0; i < l.length; i++) {
				t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
			}
			return t.split("").reverse().join("") + "." + r;
		} else {
			return "0.00";
		}
	}
	//重置按钮
	function clear(){
		var date=new Date();
		var nowDate=date.getFullYear()+'-'+(date.getMonth()+1);
		nowDate=nowDate.replace(/-(\d{1})\b/g,'-0$1');
		$('#Stime').val(nowDate);
		$("#table1").datagrid('loadData', { total: 0, rows: [] });
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false" style="width: 100%;height:85px;padding:8px 5px 5px 5px">
	<table id="searchTab" style="width: 100%;">
		<tr>
			<td style="width:75px;" align="left">对比时间:
			<input id="Stime" class="Wdate" type="text" name="Stime"  onClick="WdatePicker({dateFmt:'yyyy-MM',onpicked:changeTime})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
			<shiro:hasPermission name="${menuAlias}:function:query">
				<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="querylist()" data-options="iconCls:'icon-search'">查询</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:set">	
				<a href="javascript:clear();void(0)" class="easyui-linkbutton" onclick="clear()" iconCls="reset">重置</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:print">	
				<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="printOut()" data-options="iconCls:'icon-printer'">打印</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:export">	
				<a href="javascript:queryBottenWeek()" class="easyui-linkbutton" data-options="iconCls:'icon-down'">导出</a>
			</shiro:hasPermission>
			</td>
		</tr>
	</table>
	<h5 id="zzyy" style="font-size: 30;font: bold;text-align: center;padding-top: 5px"><span id ="changeTime" style="font-size: 30;font: bold;text-align: center;padding-top: 5px">2017年04月</span>郑州大学第一附属医院<span id ="d1" style="font-size: 30;font: bold;text-align: center;padding-top: 5px">内科医学部</span>和<span id ="d2" style="font-size: 30;font: bold;text-align: center;padding-top: 5px">内二医学部</span>对比表2</h5>
	</div>
	<div data-options="region:'center',border:false" style="width: 100%;">
		<table id="table1" class="easyui-datagrid" data-options="fit:true,fitColumns:true">
			<thead>
				<tr>
					<th data-options="field:'dept',hidden:true">组合号</th>
					<th data-options="field:'deptCode',rowspan:2,width:'10%',halign:'center',align:'left',formatter:formatdeptId">部门名称</th>
					<th data-options="field:'bingqu',rowspan:2,width:'10%',halign:'center',align:'right'">病区负责人</th>
					<th data-options="field:'q',colspan:4,align:'center'">总收入（万元）</th>
					<th data-options="field:'w',colspan:4,align:'center'">门诊收入（万元）</th>
					<th data-options="field:'e',colspan:4,align:'center'">住院收入（万元）</th>
				</tr>
				<tr>
					<th data-options="field:'stackId',hidden:true">组合号</th>
					<th data-options="field:'yearedTol',width:'8%',halign:'center',align:'right',formatter:formateCost"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="lastYear1" />年</th>
					<th data-options="field:'yearTol',width:'8%',halign:'center',align:'right',formatter:formateCost"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="toYear1" />年</th>
					<th data-options="field:'increaseTol',width:'8%',halign:'center',align:'right',formatter:formateCost">增长数</th>
					<th data-options="field:'rateTol',width:'8%',halign:'center',align:'right',formatter:formateSpace">增长率</th>
					<th data-options="field:'yearedMZ',width:'8%',halign:'center',align:'right',formatter:formateCost"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="lastYear2"/>年</th>
					<th data-options="field:'yearMZ',width:'8%',halign:'center',align:'right',formatter:formateCost"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="toYear2" />年</th>
					<th data-options="field:'increaseMZ',width:'8%',halign:'center',align:'right',formatter:formateCost">增长数</th>
					<th data-options="field:'rateMZ',width:'8%',halign:'center',align:'right',formatter:formateSpace">增长率</th>
					<th data-options="field:'yearedZY',width:'8%',halign:'center',align:'right',formatter:formateCost"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="lastYear3" />年</th>
					<th data-options="field:'yearZY',width:'8%',halign:'center',align:'right',formatter:formateCost"><input style="border: 0 ;text-align: center;font-size: 15px;width: 50px;background-color:transparent" id="toYear3" />年</th>
					<th data-options="field:'increaseZY',width:'8%',halign:'center',align:'right',formatter:formateCost">增长数</th>
					<th data-options="field:'rateZY',width:'8%',halign:'center',align:'right',formatter:formateSpace">增长率</th>
				</tr>
			</thead>
		</table>
				<form id="saveForm" method="post">
					<input type="hidden" id="exportJson" name="exportJson">
				</form>
				<form method="post" id="reportToHiddenForm" >
				<input type="hidden" name="date" id="reportToLogin" value=""/>
				<input type="hidden" name="rows" id="reportToRows" value=""/>
				<input type="hidden" name="fileName" id="reportToFileName" value=""/>
				</form>
	</div>

</body>
</html>