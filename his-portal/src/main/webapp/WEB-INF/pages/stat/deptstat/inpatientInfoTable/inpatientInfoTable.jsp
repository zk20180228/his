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
<style>
.panel-body,.layout-panel,.panel{
overflow: visible;
}
</style>
  <script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
	<script type="text/javascript">
			 
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var startTime;//重置开始时间
var endTime;//重置结束时间
var menuAlias = '${menuAlias}';
var flag;//全局科室
$(function(){
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
		$("body").setLoading({
			id:"body",
			isImg:false,
			text:"无数据权限"
		});
	}else{
// 		var deptCode = $('#deptName').getMenuIds();
		$('#tableList').datagrid({ 
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			fitColumns:true,
			singleSelect:true,
			border:true,
			fit:true,
			rownumbers:true,
			checkOnSelect:true,
			selectOnCheck:false,
			data:{total:0,rows:[]}
		});
	}
});

//查询按钮
function searchFrom(){
	 var deptCode = $('#deptName').getMenuIds();
	 var startTime = $('#startTime').val();
	 var endTime = $('#endTime').val();
	 if(startTime==''||endTime==''){
		 $.messager.alert('提示','时间不能为空');
		 return false;
	 }
	 if(deptCode==''){
		 deptCode=flag;
	 }
	 $('#tableList').datagrid({    
		url: '<%=basePath%>statistics/InpatientInfoTable/queryInpatientInfoTable.action',
			queryParams: {
				deptCode: deptCode,
				startTime:startTime,
				endTime:endTime
			}
		}); 
}

 
//重置按钮
 function clear(){
	$('#startTime').val("${startTime }");
	$('#endTime').val("${endTime }");
 	$('#deptName').val('');
	$('#deptName').attr('name','');
 	$("#tableList").datagrid('loadData', { total: 0, rows: [] });
 }
 
</script>
<body class="easyui-layout" data-options="fit:true"> 
	<div data-options="region:'north',border:false" style="width: 100%;height: 100%;">
	<div class="easyui-layout" data-options="fit:true">
		<div style="width:100%;height:80px;padding: 8px 5px 0px 5px;" data-options="region:'north',border:false">
			<table  style="width:100%;">
				<tr>
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
						<td style="width:55px;" align="center">科室:</td>
						<td id = "classA" class="newMenu" style="width:110px;z-index:1;position: relative;">
						<div class="deptInput menuInput">
							<input id="deptName" class="ksnew" readonly/><span></span></div>
							<div id="m3" class="xmenu" style="display: none;">
		    	       		<div class="searchDept">
							<input type="text" name="searchByDeptName" placeholder="回车查询" />
							<span class="searchMenu"><i></i>查询</span> <a
								name="menu-confirm-cancel" href="javascript:void(0);"
								class="a-btn"> <span class="a-btn-text">取消</span>
							</a> <a name="menu-confirm-clear" href="javascript:void(0);"
								class="a-btn"> <span class="a-btn-text">清空</span>
							</a> <a name="menu-confirm" href="javascript:void(0);" class="a-btn">
								<span class="a-btn-text">确定</span>
							</a>
						</div>
						<div class="select-info" style="display: none">
							<label class="top-label">已选部门：</label>
							<ul class="addDept">

							</ul>
						</div>
						<div class="depts-dl">
							<div class="addList"></div>
							<div class="tip" style="display: none">没有检索到数据</div>
						</div>
					<td>
					&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" iconCls="reset">重置</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-2012081511202">打印</a>
<!-- 					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportList()" iconCls="icon-down">导出</a> -->
					</td>
				</tr>
			</table>
				<div align="center"><font size="6" class="empWorkTit">住院病人动态报表</font></div>
		</div>
		<div data-options="region:'center',border:false" style="height:8px;" >
		<div data-options="region:'north'" style="height:2px;width: 100%;">
  			<table style="width:100%;z-index: 0">
		    	</table>
		</div>
		<div data-options="region:'center',border:false,fit:true" style="height:100%;padding: 0px 0px 0px 3px;" >
			<table class="easyui-datagrid" id="tableList"  style="" data-options="fit:true,border:false">
				<thead>
					<tr>
					
						<th data-options="field:'deptName',width:'6%',align:'center',rowspan:4">部门名称</th>
						<th data-options="field:'firstBeds',width:'6%',align:'center',rowspan:4">期初床位数</th>
						<th data-options="field:'finalBeds',width:'6%',align:'center',rowspan:4">期末床位数</th>
						<th data-options="field:'test',width:'6%',align:'center',colspan:14">出院者动态</th>
						<th data-options="field:'actOpenBedDays',width:'7%',align:'center',rowspan:4">实际开放总床日数</th>
						<th data-options="field:'avgOpenBeds',width:'7%',align:'center',rowspan:4">平均开放病床数</th>
						<th data-options="field:'actUseBedDays',width:'7%',align:'center',rowspan:4">实际占用总床日数</th>
 						<th data-options="field:'outAvgInDay',width:'7%',align:'center',rowspan:4">平均住院天数</th>
						<th data-options="field:'other',width:'6%',align:'center',colspan:2">有效率(%)</th>
						<th data-options="field:'deathRate',width:'3%',align:'center',rowspan:4">死亡率(%)</th>
						<th data-options="field:'bedTurnNum',width:'6%',align:'center',rowspan:4">病床周转次数</th>
						<th data-options="field:'avgBedWorkDay',width:'7%',align:'center',rowspan:4">平均病床工作日</th>
						<th data-options="field:'bedUseRate',width:'7%',align:'center',rowspan:4">床位使用率(%)</th>
						<th data-options="field:'avgBeforeOperDay',width:'7%',align:'center',rowspan:4">术前平均住院天数</th>
						<th data-options="field:'criPatient',width:'5%',align:'center',rowspan:4">危重病人数</th>
						<th data-options="field:'addBedDays',width:'5%',align:'center',rowspan:4">加床总日数</th>
						<th data-options="field:'freeBedDays',width:'5%',align:'center',rowspan:4">空床总日数</th>
						<th data-options="field:'hangBedDays',width:'5%',align:'center',rowspan:4">挂床日数</th>
						<th data-options="field:'levelA',width:'5%',align:'center',rowspan:4">一级护理</th> 
					</tr>
					<tr>
						<th data-options="field:'firstStayHos',width:'4%',align:'center',rowspan:3">期初留院</th>
						<th data-options="field:'withinInHos',width:'4%',align:'center',rowspan:3">期内入院</th>
						<th data-options="field:'otherTransIn',width:'4%',align:'center',rowspan:3">他科转入</th>
						<th data-options="field:'radiationNum',align:'center',colspan:9">期内出院人</th>
						<th data-options="field:'transOther',width:'4%',align:'center',rowspan:3">他科转往</th>
						<th data-options="field:'finalStayHos',width:'4%',align:'center',rowspan:3">期末留院</th>
						<th data-options="field:'cureRate',width:'3%',align:'center',rowspan:3">治愈率(%)</th>
						<th data-options="field:'betterRate',width:'3%',align:'center',rowspan:3">好转率(%)</th>
						</tr>
					<tr>
						<th data-options="field:'total',width:'3%',align:'center',rowspan:2">总数</th>
						<th data-options="field:'inspectCost1',align:'center',colspan:5">出院人数</th>
						<th data-options="field:'others',width:'3%',align:'center',rowspan:2">其他</th>
						<th data-options="field:'childBirth',width:'3%',align:'center',rowspan:2">正常产</th>
						<th data-options="field:'familyPlan',width:'3%',align:'center',rowspan:2">计生</th>
					</tr>
					<tr>
						<th data-options="field:'subTotal',width:'3%',halign:'center',align:'center'">小计</th>
						<th data-options="field:'cure',width:'3%',halign:'center',align:'center'">治愈</th>
						<th data-options="field:'better',width:'3%',halign:'center',align:'center'">好转</th>
						<th data-options="field:'notCure',width:'3%',halign:'center',align:'center'">未愈</th>
						<th data-options="field:'death',width:'3%',halign:'center',align:'center'">死亡</th>
					</tr>
				</thead>
			</table>
		
		</div>
		 	
		</div>
	</div>
	</div>
</body>
</html>