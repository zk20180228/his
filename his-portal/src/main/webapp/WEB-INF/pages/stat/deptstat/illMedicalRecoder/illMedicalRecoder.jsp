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
<title>危重病历人数比例统计分析</title>
</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var menuAlias = '${menuAlias}';
// var deptMap="";
var flag;//全局科室
$(function(){
	//科室下拉
	$(".deptInput").MenuList({
		width :530, //设置宽度，不写默认为530，不要加单位
		height :400, //设置高度，不写默认为400，不要加单位
		menulines:2, //设置菜单每行显示几列（1-5），默认为2
		dropmenu:"#m3",//弹出层id，必须要写
		isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
		haveThreeLevel:false,//是否有三级菜单
		para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
		firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
	});
	$('#m3 .addList h2 input').click();
	$('a[name=\'menu-confirm\']').click();
	flag=$('#deptName').getMenuIds();
	$('a[name=\'menu-confirm-clear\']').click();
	if(flag==''||flag==null){
		$("#tableList").datagrid();
		$("body").setLoading({
			id:"body",
			isImg:false,
			text:"无数据权限"
		});
	}else{
		$('#tableList').datagrid();
// 		//查询科室
// 		$.ajax({
<%-- 			url: "<%=basePath%>statistics/DeptLeadDrug/queryDeptMap.action",		 --%>
// 			type:'post',
// 			success: function(data) {
// 				deptMap=data;	
// 			}
// 		});
	}
});
// //渲染科室
// function deptFunction(value,row,index){
// 	if(value!=null&&value!=""){
// 		if(deptMap[value]!=null&&deptMap[value]!==""){
// 		return deptMap[value];					
// 		}else{
// 			return value;
// 		}
// 	}
// }
//小数点前是0 渲染
function functionRate(value,row,index){
	if(value!=null){
		if(value.substr(0,1)=='.'){
			return '0'+value;
		}else{
			return value;
		}
	}
}
//查询按钮
 function searchFrom(){
	 var deptCode = $('#deptName').getMenuIds();
	 if(deptCode==''){
		 deptCode=flag;
	 }
	 var startTime = $('#startTime').val();
	 var endTime = $('#endTime').val();
	 if(startTime==''||endTime==''){
		 $.messager.alert('提示','时间不能为空');
		 return false;
	 }
	 $('#tableList').datagrid({    
			url: '<%=basePath%>statistics/illMedicalRecoder/queryIllMedical.action',
			queryParams: {
				deptCode: deptCode,
				startTime:startTime,
				endTime:endTime,
				menuAlias:menuAlias
			}
		}); 
 }
 
//重置按钮
 function clear(){
	 $('#startTime').val("${startTime}");
	 $('#endTime').val("${endTime}");
 	$('#deptName').val('');
	$('#deptName').attr('name','');
 	$("#tableList").datagrid('loadData', { total: 0, rows: [] });
 }
 
//导出列表
 function exportList() {
 	var rows = $('#tableList').datagrid('getRows');
 	if(rows==null||rows==""){
 		$.messager.alert("提示", "列表无数据,无法导出！");
 		return;
 	}
 	$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
 		if (res) {
 			rows = $('#tableList').datagrid('getRows');
			$('#reportJson').val(JSON.stringify(rows));
			$('#reportForm').form('submit', {
				url :"<%=basePath%>statistics/illMedicalRecoder/exportMedicalRecoder.action",
				success:function(data){
					$.messager.alert("操作提示", "导出失败！", "success");
				},
				error:function(data) {
					$.messager.alert("操作提示", "网络繁忙！", "error");
				}
			});
 		}
 	});
 	
 }
 
//报表打印
 function exportPDF() {
 	var rows=$('#tableList').datagrid('getRows')
	if(rows.length > 0){
		$('#reportJson').val(JSON.stringify(rows));
		//表单提交 target
		var formTarget="reportForm";
		var tmpPath = "<%=basePath%>statistics/illMedicalRecoder/printIllMedicalRecoder.action";
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
	}else{
		$.messager.alert("提示","当前统计无数据，无法打印！");
		setTimeout(function(){$(".messager-body").window('close')},1500);
		return;
	}
 }
</script>
<body style="margin: 0px; padding: 0px;">
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	 	<form id="saveForm" action="" style="height:80px;width: 100%;padding-top: 8px;padding-top: 8px;padding-right: 10px;padding-left: 10px;">
	 		<table border="0" style="width: 100%;height: 10%">
	 			<tr>
	 			<!-- 开始时间 --> 
					<td style="width:80px;" align="left">开始时间:</td>
					<td style="width:110px;">
						<input id="startTime" class="Wdate" type="text" name="startTime" value="${startTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}'})" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
					<td style="width:90px;" align="left">&nbsp;结束时间:</td>
					<td style="width:110px;">
						<input id="endTime" class="Wdate" type="text" name="endTime" value="${endTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'})" style="width:110px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
		 			<td style="width: 40px;"> 科室:</td>
				    <td  class="newMenu" style="width:160px;z-index:1;position: relative;">
							<div class="deptInput menuInput" style="margin-top:0px;">
								<input id="deptName" class="ksnew"  readonly/>
								<span></span>
							</div>
							<div id="m3" class="xmenu" style="display: none;">
								<div class="searchDept">
									<input type="text" name="searchByDeptName" placeholder="回车查询"/>
									<span class="searchMenu"><i></i>查询</span>
									<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn">
										<span class="a-btn-text">取消</span>
									</a>						
									<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn">
										<span class="a-btn-text">清空</span>
									</a>
									<a name="menu-confirm" href="javascript:void(0);" class="a-btn">
										<span class="a-btn-text">确定</span>
									</a>
								</div>
								<div class="select-info" style="display:none">
									<label class="top-label">已选部门：</label>
									<ul class="addDept">
									</ul>
								</div>	
								<div class="depts-dl">
									<div class="addList">
									</div>
									<div class="tip" style="display:none">没有检索到数据</div>
								</div>	
										
							</div>
		    		</td>
		    		<td>
			    		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search">查询</a>
			 			<a href="javascript:clear();void(0)" class="easyui-linkbutton" onclick="clear()" iconCls="reset">重置</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportList()" iconCls="icon-down">导出</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportPDF()" iconCls="icon-printer">打印</a>
		    		</td>
	    		</tr>
	    	 	
	    	 	<tr>
	    	 		<td align="center" colspan="10"  ><font style = "font-size: 32px !important;" class="empWorkTit" size="6">危重病历人数比例统计分析</font></td>
	    	 	</tr>
	    	 </table>
	    </form>
	    <div data-options="region:'center',border:false" style="width: 100%;height: 89%;">
	    	<table id="tableList" class="easyui-datagrid" style="padding:5px 5px 5px 5px;" data-options="singleSelect:'true',striped:true,fitColumns:true,rownumbers:true,fit:true">
				<thead>
					<tr>
						<th data-options="field:'deptCode',width:'7%'">部门编号</th>
<!-- 						,formatter:deptFunction -->
						<th data-options="field:'deptName',width:'9%'">部门名称</th>
						<th data-options="field:'num',width:'7%'">数量</th>
						<th data-options="field:'cured',width:'7%'">治愈情况</th>
						<th data-options="field:'better',width:'5%'">好转</th>
						<th data-options="field:'noCured',width:'5%'">未治愈</th>
						<th data-options="field:'death',width:'5%'">死亡</th>
						<th data-options="field:'other',width:'5%'">其他</th>
						<th data-options="field:'cureRate',width:'7%',formatter:functionRate" align="right">治愈率百分比%</th>
						<th data-options="field:'deathRate',width:'7%',formatter:functionRate" align="right">死亡率百分比%</th>
						<th data-options="field:'averInhost',width:'5%'">平均住院日</th>
						<th data-options="field:'averFeeCost',width:'7%'" align="right">平均费用</th>
						<th data-options="field:'compliInterface',width:'7%'">并发症感染</th>
						<th data-options="field:'allIll',width:'7%'">全院患者</th>
						<th data-options="field:'thanFloor',width:'7%',formatter:functionRate" align="right">占全院比%</th>
					</tr>
				</thead>
			</table>
	    </div>   
	</div>
	<form id="reportForm" method="post">
		<input type="hidden" id="reportTime" name="reportTime"/>
		<input type="hidden" id="reportJson" name="reportJson">
		<input type="hidden" id="fileName" name="fileName" value="ZYRB">
	</form>
	<form id="exportForm" method="post">
		<input type="hidden" id="exportJson" name="exportJson">
	</form>
</body>
</html>
