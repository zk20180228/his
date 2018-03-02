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
</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var menuAlias = '${menuAlias}';
var flag;
$(function(){
	var STime = $('#STime').val();
	var ETime = $('#ETime').val();
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
	}
});

//重置按钮
function clear(){
	$('#STime').val("${Stime}");
	$('#Etime').val("${Etime}");
	$('#deptName').val('');
	$('#deptName').attr('name','');
	$("#list").datagrid('loadData', { total: 0, rows: [] });
}
</script>
<body class="easyui-layout" data-options="fit:true"> 
	<form action="" style="width:100%;height:80px;padding: 8px 5px 0px 5px;">
		<table  border="0" style="width:100%;">
			<tr>
				<tr>
					<!-- 开始时间 --> 
					<td style="width:80px;" align="left">开始时间:</td>
					<td style="width:110px;">
						<input id="STime" class="Wdate" type="text" name="STime" value="${Stime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
					<td style="width:90px;" align="left">&nbsp;结束时间:</td>
					<td style="width:110px;">
						<input id="ETime" class="Wdate" type="text" name="ETime" value="${Etime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
					<td style="width:55px;" align="center">科室:</td>
					<td  class="newMenu" style="width:160px;z-index:1;position: relative;">
						<div class="deptInput menuInput" style="margin-top:0px;">
							<input id="deptName" class="ksnew" name="t1.id" readonly/><span></span>
						</div>
							<div id="m3" class="xmenu" style="display: none;">
							<div class="searchDept">
								<input type="text" name="searchByDeptName" placeholder="回车查询" /><span class="searchMenu"><i></i>查询</span> 
								<a name="menu-confirm-cancel" href="javascript:void(0);"class="a-btn"> <span class="a-btn-text">取消</span></a> 
								<a name="menu-confirm-clear" href="javascript:void(0);"class="a-btn"> <span class="a-btn-text">清空</span></a> 
								<a name="menu-confirm" href="javascript:void(0);" class="a-btn"> <span class="a-btn-text">确定</span></a>
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
						</div>
    				</td>
					<td>
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" iconCls="reset">重置</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="print()" iconCls="icon-2012081511202">打印</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportList()" iconCls="icon-down">导出</a>
					</td>
			</tr>
			<tr>
	    		<td align="center" colspan="7"><font style = "font-size: 32px !important;" size="6" class="empWorkTit">住院病人动态报表</font></td>
	    	</tr>
		</table>
	</form>
	<div data-options="region:'center',border:false,fit:true" style="height:100%;" >
		<table class="easyui-datagrid" id="list"  data-options="fit:true">
			<thead>
				<tr>
					<th data-options="field:'check',width:'6%',align:'center',rowspan:4">部门名称</th>
					<th data-options="field:'treatment',width:'7%',align:'center',rowspan:4">期初床位数</th>
					<th data-options="field:'radiation',width:'7%',align:'center',rowspan:4">期末床位数</th>
					<th data-options="field:'test',width:'7%',align:'center',colspan:14">出院者动态</th>
					<th data-options="field:'blood',width:'7%',align:'center',rowspan:4">实际开放总床日数</th>
					<th data-options="field:'other',width:'7%',align:'center',rowspan:4">平均开方病床数</th>
					<th data-options="field:'other',width:'7%',align:'center',rowspan:4">实际占用总床日数</th>
					<th data-options="field:'other',width:'7%',align:'center',rowspan:4">平均住院天数</th>
					<th data-options="field:'other',width:'7%',align:'center',colspan:2">有效率(%)</th>
					<th data-options="field:'other',width:'7%',align:'center',rowspan:4">死亡率(%)</th>
					<th data-options="field:'other',width:'7%',align:'center',rowspan:4">病床周转次数</th>
					<th data-options="field:'other',width:'7%',align:'center',rowspan:4">平均病床工作日</th>
					<th data-options="field:'other',width:'7%',align:'center',rowspan:4">床位使用率(%)</th>
					<th data-options="field:'other',width:'7%',align:'center',rowspan:4">术前平均住院天数</th>
					<th data-options="field:'other',width:'7%',align:'center',rowspan:4">危重病人数</th>
					<th data-options="field:'other',width:'6%',align:'center',rowspan:4">加床总日数</th>
					<th data-options="field:'other',width:'6%',align:'center',rowspan:4">空床总日数</th>
					<th data-options="field:'other',width:'6%',align:'center',rowspan:4">挂床日数</th>
					<th data-options="field:'other',width:'6%',align:'center',rowspan:4">一级护理</th> 
				</tr>
				<tr>
					<th data-options="field:'inspectCost',width:'6%',align:'center',rowspan:3">期初留院</th>
					<th data-options="field:'treatmentNum',width:'6%',align:'center',rowspan:3">期内入院</th>
					<th data-options="field:'treatmentCost',width:'6%',align:'center',rowspan:3">他科转入</th>
					<th data-options="field:'radiationNum',align:'center',colspan:9">期内出院人</th>
					<th data-options="field:'radiationCost',width:'6%',align:'center',rowspan:3">他科转往</th>
					<th data-options="field:'testNum',width:'6%',align:'center',rowspan:3">期末留院</th>
					<th data-options="field:'testNum',width:'3%',align:'center',rowspan:3">治愈率(%)</th>
					<th data-options="field:'testNum',width:'3%',align:'center',rowspan:3">好转率(%)</th>
					</tr>
				<tr>
					<th data-options="field:'inspectCost',width:'3%',align:'center',rowspan:2">总数</th>
					<th data-options="field:'treatmentNum1',width:'3%',align:'center',colspan:5">出院人数</th>
					<th data-options="field:'treatmentCost',width:'3%',align:'center',rowspan:2">其他</th>
					<th data-options="field:'radiationNum',width:'3%',align:'center',rowspan:2">正常产</th>
					<th data-options="field:'radiationCost',width:'3%',align:'center',rowspan:2">计生</th>
				</tr>
				<tr>
					<th data-options="field:'inspectCost',width:'3%',align:'center'">小计</th>
					<th data-options="field:'treatmentNum',width:'3%',align:'center'">治愈</th>
					<th data-options="field:'treatmentCost',width:'3%',align:'center'">好转</th>
					<th data-options="field:'radiationNum',width:'3%',align:'center'">未愈</th>
					<th data-options="field:'radiationCost',width:'3%',align:'center'">死亡</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
</body>
</html>