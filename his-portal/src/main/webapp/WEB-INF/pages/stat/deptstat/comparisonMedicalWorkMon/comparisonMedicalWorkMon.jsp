<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>医技工作时期对比</title>
<%@ include file="/common/metas.jsp"%>
<!-- 修改行数宽度 -->
<style type="text/css">
	.datagrid-header-rownumber,.datagrid-cell-rownumber{
		   width:30px;
		 }
.panel-body,.layout-panel,.panel{
overflow: visible;
}
</style>
</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">	
var menuAlias = '${menuAlias}';
var flag;
$(function(){
	var time1=$("#STime").val();
	var time=time1.substring(0,4)+"年"+time1.substring(5,7)+"月";
	var timeDate = time1.replace(/-/g,"/");
	var lastDate = new Date(timeDate);
	lastDate.setFullYear(lastDate.getFullYear()-1);
	var lastTime=lastDate.getFullYear()+"年"+time1.substring(5,7)+"月";
	$("#time1").html(lastTime);
	$("#time2").html(time);
	
	
	$(".deptInput").MenuList({
		width :530, //设置宽度，不写默认为530，不要加单位
		height :400, //设置高度，不写默认为400，不要加单位
		menulines:2, //设置菜单每行显示几列（1-5），默认为2
		dropmenu:"#m3",//弹出层id，必须要写
		isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
//		haveThreeLevel:true,//是否有三级菜单
		para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
		chId:"1001",
		firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
		relativeInput:".doctorInput",	//与其级联的文本框，必须要写
		relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
	});
	
	$('#m3 .addList h2 input').click();
	$('a[name=\'menu-confirm\']').click();
	
	flag=$('#deptName').getMenuIds();
	$('a[name=\'menu-confirm-clear\']').click();
	if(flag==''){
		$("#dataGrid1").datagrid();
		$("body").setLoading({
			id:"body",
			isImg:false,
			text:"无数据权限"
		});
	}
	
});
/***
 * 打印
 */
function queryTopWeek(){
	var beginDate = $('#beginDate').val();
	var endDate = $('#endDate').val();
	var fileName="iReportPhaRefund";
	if(beginDate==null||beginDate==''||endDate==null||endDate==''){
        $.messager.alert("提示","日期不能为空！");
        return;
	}
	var rows = $("#dataGrid1").datagrid("getRows");
	var feeStatCode = $('#feeStatCode').combobox("getValue");
	if(feeStatCode == null || feeStatCode == ""){
		$.messager.alert('提示','请添加药费类别！');
		return ;
	}
	if (rows==null||rows.length==0) {
		$.messager.alert("提示","没有数据，不能打印！"); 	
		return;
	}else{
	$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否打印
			if (res) {
			window.open ("<%=basePath%>statistics/RefundStat/iReportPhaRefund.action?feeStatCode="+feeStatCode+"&beginDate="+beginDate+"&endDate="+endDate+"&fileName="+fileName+"",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
			}
		});
	}
}

/***
 * 导出
 */
function queryBottenWeek(){
	var rows = $("#dataGrid1").datagrid("getRows");
	var beginDate = $('#beginDate').val();
	var endDate = $('#endDate').val();
	if(beginDate==null||beginDate==''||endDate==null||endDate==''){
        $.messager.alert("提示","日期不能为空！");
        return;
	}
	if (rows==null||rows.length==0) {
		$.messager.alert("提示","没有数据，不能导出！");
		return;
	}else{
	$.messager.confirm('确认', '确定要导出吗?', function(res) {
		if (res) {
			var opt = $('#dataGrid1').datagrid('options');
			var page = opt.pageNumber;
			var rows = opt.pageSize;
			
			$('#form1').form('submit', {
				url:'<%=basePath%>statistics/RefundStat/exportExcel.action',
				queryParams:{'page':page,'rows':rows},
				success : function(data) {
					$.messager.alert("提示", "导出成功！", "success");
				},
				error : function(data) {
					$.messager.alert("提示", "导出失败！", "error");
				}
			});
		}
	});
   }
}
  /**
	* 重置方法
	*/
	function clear(){
		$('#STime').val("${STime}");
		$('#deptName').val('');
		$('#deptName').attr('name','');
		$("#dataGrid1").datagrid('loadData', { total: 0, rows: [] });
	}
</script>
<body style="margin: 0px;padding: 0px;"> 
<div id="cc" class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north',border:false" >
  		<table cellspacing="0" cellpadding="0"  data-options="border:false"  style="padding: 7px 7px 0px;width: 100% ;"  >
			<tr style = "">
				<td style="width:40px;" align="left">日期:</td>
				<td style="width:110px;">
					<input id="STime" class="Wdate" type="text" name="STime" value="${STime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
				</td>
			<td style="width:40px;" align="center">科室:</td>
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
			</td>
				<td>
					<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
					<a href="javascript:queryTopWeek()" class="easyui-linkbutton" data-options="iconCls:'icon-printer'">打印</a>
					<a href="javascript:queryBottenWeek()" class="easyui-linkbutton" data-options="iconCls:'icon-down'">导出</a>
				</td>
				</tr>
		</table>
  		<div data-options="region:'north'" style="height:50px;width: 100%;margin-top:5px">
  			<table style="width:100%;z-index: 0">
		    		<tr>
		    			<td align="center"><font size="6" class="empWorkTit">医技工作时期对比</font></td>
		    		</tr>
		    	</table>
		</div>
    </div>   
    <div data-options="region:'center',noheader:true">
	    <!-- <table id="dataGrid1"></table> -->	    
	    <table id="dataGrid1" class="easyui-datagrid"    
        data-options="fitColumns:true,singleSelect:true">   
	    <thead>   
	        <tr>   
	            <th data-options="field:'name',width:'20%'">项目</th>   
	            <th data-options="field:'time1',width:'20%'"><span id="time1"></span></th>   
	            <th data-options="field:'time2',width:'20%'"><span id="time2"></span></th>  
	            <th data-options="field:'zengJian',width:'20%'">增减数</th> 
	            <th data-options="field:'zengJianPer',width:'20%'">增减%</th>  
	        </tr>   
	    </thead>   
	    <tbody>   
	        <tr>   
	            <td>放射科</td><td>187479</td><td>233325</td><td>45846</td><td>24.45</td>   
	        </tr>   
	        <tr>   
	            <td>超声波</td><td>343782</td><td>448161</td><td>104379</td><td>30.36</td>   
	        </tr>   
	        <tr>   
	            <td>核医学</td><td>455272</td><td>560975</td><td>105703</td><td>23.22</td>   
	        </tr>
	         <tr>   
	            <td>河医院区</td><td>455272</td><td>445173</td><td>105703</td><td>-2.22</td>   
	        </tr>
	         <tr>   
	            <td>郑东院区</td><td>--</td><td>115802</td><td>--</td><td>--</td>   
	        </tr> 
	    </tbody>   
    </div>   
</table>  
</div>  
</body>
</html>