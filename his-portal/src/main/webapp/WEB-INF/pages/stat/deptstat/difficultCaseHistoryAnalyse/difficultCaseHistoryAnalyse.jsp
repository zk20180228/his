<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>疑难重症病历人数比例统计分析</title>

</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var menuAlias = '${menuAlias}';
var flag;
$(function(){
	$("#startData").val("${firstData }");
    $("#endData").val("${endData }");
    
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
	if(flag==''){
		$("#list").datagrid();
		$("body").setLoading({
			id:"body",
			isImg:false,
			text:"无数据权限"
		});
	}else{
		//查询人员map
		$.ajax({
			url:'<%=basePath%>baseinfo/employee/getEmplMap.action',
			async:false,
			success:function(datavalue){
				empMap=datavalue;
			}
		});	
		
		var firstData=$("#startData").val();
		var endData=$("#endData").val();
		$("#list").datagrid({
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
			url:"<%=basePath%>statistics/CostStatistics/queryPatientCostStatistice.action",
			queryParams:{firstData:firstData,endData:endData,inpatientNo:null},
			onLoadSuccess:function(data){
				var pager = $(this).datagrid('getPager');
				var aArr = $(pager).find('a');
				var iArr = $(pager).find('input');
				$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
				for(var i=0;i<aArr.length;i++){
					$(aArr[i]).tooltip({
						content:toolArr[i],
						hideDelay:1
					});
					$(aArr[i]).tooltip('hide');
				}
			}
		});
	}
});
	
	function findMesIn(){

	}
	
	//查询
	function searchFrom(){

	}
	//导出
	function exportDown(){

	}
	//打印方法
	function experStamp() {

	}
	/**
	* 重置方法
	*/
	function clear(){
		$('#deptName').val('');
		$('#deptName').attr('name','');
		$("#tableList").datagrid('loadData', { total: 0, rows: [] });
	}
</script>
<body>
<div id="cc" class="easyui-layout" data-options="fit:true">   
		<form action="" style="height: 35px;padding: 7px 5px 5px 5px;width: 100%">
			<input type="hidden" id="inpatientNo">
			<table>
				<tr>
					<td>科室</td>
						<td  class="newMenu" style="width:160px;z-index:1;position: relative;">
							<div class="deptInput menuInput" style="margin-top:0px;">
								<input id="deptName" class="ksnew" name="t1.id" readonly/>
								<span></span>
							</div>
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
		    				</td>
					<td>
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
						<a href="javascript:void(0)" onclick="experStamp()" class="easyui-linkbutton" iconCls="icon-printer">打印</a>
						<a href="javascript:void(0)" onclick="exportDown()" class="easyui-linkbutton" iconCls="icon-down">导出</a>
					</td>
				</tr>
			</table>
		</form>
	<div data-options="region:'center',border:false" style="height:94%" align="center">
		<table id="list"  class="easyui-datagrid" >
			<thead>
				<tr>
					<th data-options="field:'recipeDeptcode',width:'7%'">部门名称</th>
					<th data-options="field:'recipeDoccode',width:'7%'">数量</th>
					<th data-options="field:'inpatientNo',width:'7%'">治愈情况</th>
					<th data-options="field:'name',width:'7%'">好转</th>
					<th data-options="field:'feeCode',width:'7%'">未治愈</th>
					<th data-options="field:'totCost',width:'7%'">死亡</th>
					<th data-options="field:'executeDeptcode',width:'7%'">其他</th>
					<th data-options="field:'feeOpercode',width:'7%'">治愈率百分比</th>
					<th data-options="field:'feeDate',width:'7%'">死亡率百分比</th>
					<th data-options="field:'feeDate',width:'7%'">平均住院日</th>
					<th data-options="field:'feeDate',width:'7%'">平均费用</th>
					<th data-options="field:'feeDate',width:'7%'">并发症感染</th>
					<th data-options="field:'feeDate',width:'7%'">全员患者</th>
					<th data-options="field:'feeDate',width:'7%'">占全院比</th>
				</tr>
			</thead>
		</table>
		<form id="exportForm"/>
	</div>
</div>
<div id="InpatientMes" class="easyui-dialog" title="选择" style="width:650;height:600;padding:10" data-options="modal:true, closed:true">   
 	<a href="javascript:void(0)" onclick="findMesIn()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
 	<br></br>
   <table id="infoDatagridMes" class="easyui-datagrid" data-options="fitColumns:true,checkOnSelect:true,fit:true"></table>
</div>
</body>		
</html>