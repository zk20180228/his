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
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var menuAlias = '${menuAlias}';
var STime;
var ETime;
var flag;
$(function(){
	var STime = $('#startTime').val();
	var ETime = $('#endTime').val();
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
		$("#tableList").datagrid();
		$("body").setLoading({
			id:"body",
			isImg:false,
			text:"无数据权限"
		});
	}
	
});
//导出列表
function exportList() {
	var startTime = $('#startTime').val();
	var endTime = $('#endTime').val();
	var rows = $('#tableList').datagrid('getRows');
	if(rows==null||rows==""){
		$.messager.alert("提示", "列表无数据,无法导出！");
		return;
	}
	$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
		if (res) {
			$('#saveForm').form('submit', {
				url :"<%=basePath%>statistics/DetailedDaily/expDetailedDailylist.action",
				queryParams:{stime:beginDate,etime:endDate},
				onSubmit : function() {
					return $(this).form('validate');
				},
				success : function(data) {
					$.messager.alert("操作提示", "导出成功！", "success");
				},
				error : function(data) {
					$.messager.alert("操作提示", "导出失败！", "error");
				}
			});
		}
	});
}
//报表打印
function exportPDF() {
	var rowsCount=$('#tableList').datagrid('getRows')
	if(''!=rowsCount&&null!=rowsCount){
	var startTime = $('#startTime').val();
	var endTime = $('#endTime').val();
	window.open ("<c:url value='queryDetailedDailylistPDF.action?stime='/>"+beginDate+"&etime="+endDate,'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	}else{
		$.messager.alert('提示','列表无数据,无法打印！');
	}	
}
//重置按钮
function clear(){
	$('#startTime').val("${Stime}");
	$('#endTime').val("${Etime}");
	$('#deptName').val('');
	$('#deptName').attr('name','');
	$('#diagnosisName').textbox('setValue',"");
	$("#tableList").datagrid('loadData', { total: 0, rows: [] });
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true" style="margin-left:auto;margin-right:auto;">
				<form action="" style="padding:5px 5px 0px 5px;width:100%;height:80px;">
					<table style="width: 100%" cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td class="chargeBillistSize" width="320px">出院日期:
								<input id="startTime"  name="startTime" class="Wdate" type="text" value="${Stime }" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								至
								<input id="endTime"  name="endTime"  class="Wdate" type="text" value="${Etime }" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td style="width:80px;">	
								科室名称：
							</td>
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
								</div>
		    				</td>
							<td>
								&nbsp;诊断名称：<input id="diagnosisName" class='easyui-textbox'/>
								<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:clear();void(0)" class="easyui-linkbutton" onclick="clear()" iconCls="reset">重置</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportList()" iconCls="icon-down">导出</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportPDF()" iconCls="icon-printer">打印</a>
								</shiro:hasPermission>
							</td>
						</tr>
						<tr style="height:5px;"></tr>
						<tr align="center">
							<td colspan="4">
								<font size="6" style = "font-size: 32px !important;" class="outpatienttit">疾病诊断科室排序查询</font>
							</td>
						</tr>
					</table>
		</form>
		<div data-options="region:'center',border:false" style="width: 100%;margin-left:auto;margin-right:auto;height: 100%">
			<table id="tableList" class="easyui-datagrid" data-options="fitColumns:true,singleSelect:true,fit:true,border:false" >
				<thead>
					<tr>
						<th data-options="field:'state',width:'5%',align:'center'">部门编号</th>
						<th data-options="field:'itemName',width:'6%'" align="center" halign="center">部门名称</th>
						<th data-options="field:'currentUnit',width:'5%'"align="center" halign="center">序号</th>
						<th data-options="field:'unitPrice',width:'7%'" align="center" halign="center">主要诊断编码</th>
						<th data-options="field:'qty',width:'7%'" align="center" halign="center">主要诊断名称</th>
						<th data-options="field:'executeDeptcode',width:'5%'" align="center" halign="center">数量</th>
						<th data-options="field:'chargeOpercode',width:'5%'" align="center" halign="center">治愈情况</th>
						<th data-options="field:'chargeDate',width:'5%',align:'center'" align="center" halign="center">好转</th>
						<th data-options="field:'totCost',width:'4%'" align="right" halign="center">未治愈</th>
						<th data-options="field:'totCost',width:'5%'" align="right" halign="center">死亡</th>
						<th data-options="field:'ownCost',width:'5%'" align="right" halign="center">其它</th>
						<th data-options="field:'payCost',width:'7%'" align="right" halign="center">治愈率百分比</th>
						<th data-options="field:'pubCost',width:'7%'" align="right" halign="center">死亡率百分比</th>
						<th data-options="field:'ecoCost',width:'7%'" align="right" halign="center">平均住院日</th>
						<th data-options="field:'ecoCost',width:'5%'" align="right" halign="center">平均费用</th>
						<th data-options="field:'ecoCost',width:'5%'" align="right" halign="center">并发症感染</th>
						<th data-options="field:'ecoCost',width:'9%'" align="right" halign="center">疑难杂症例数</th>
						<th data-options="field:'ecoCost',width:'5%'" align="right" halign="center">急危重例数</th>
						<th data-options="field:'ecoCost',width:'9%'" align="right" halign="center">三级手术例数</th>
						<th data-options="field:'ecoCost',width:'9%'" align="right" halign="center">四级手术例数</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>
</html>