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
	$('#campus').combobox({
		url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=hospitalArea",
		valueField : 'encode',
		textField : 'name',
		multiple : false, 
	});
	$('#m3 .addList h2 input').click();
	$('a[name=\'menu-confirm\']').click();
	
	$("#table1").datagrid({  
        fit:true,
        rownumbers:true,
        border:true,
        checkOnSelect:true,
        selectOnCheck:false,
        singleSelect:true,
        url: '<%=basePath%>/deptStat/diagnosticRate/queryList.action',
        queryParams:{endTime:endTime,dept1:depts1,dept2:depts2},
        onLoadSuccess: function(){

        }
	});
	
})
/**
* 重置方法
*/
function clear(){
	$('#Stime').val("${Etime}");
	$("#countList").datagrid('loadData', { total: 0, rows: [] });
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript" src="<%=basePath%>javascript/js/jqloadingA.js"></script>
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
	<table id="searchTab" style="width: 100%;" border="0">
		<tr>
			<!-- 开始时间 --> 
						<td style="width:70px;" align="left">开始时间:</td>
						<td style="width:110px;">
							<input id="Stime" class="Wdate" type="text" name="Stime" value="${startTime }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
						<!-- 结束时间 --> 
						<td style="width:80px;" align="center">结束时间:</td>
						<td style="width:110px" id="td1">
							<input id="Etime" class="Wdate" type="text" name="Etime" value="${endTime }"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
						<td style="width:220px;">&nbsp;院区:
							<input id="campus" class="easyui-combobox" name="campus"  /> 
						</td>
						<td style="width:45px;" align="center">科室:</td>				    			
						<td  class="newMenu" style="width:160px;z-index:1;position: relative;">
							<div class="deptInput menuInput" style="margin-top:0px;">
								<input id="queryDept" class="ksnew"  readonly/>
								<span></span>
							</div>
								<div id="m3" class="xmenu" style="display: none;">
									<div class="searchDept">
					<input type="text" name="searchByDeptName" placeholder="回车查询" />
					<span class="searchMenu"><i></i>查询</span> 
					<a name="menu-confirm-cancel" href="javascript:void(0);"class="a-btn"> <span class="a-btn-text">取消</span></a> 
					<a name="menu-confirm-clear" href="javascript:void(0);"class="a-btn"> <span class="a-btn-text">清空</span></a> 
					<a name="menu-confirm" href="javascript:void(0);" class="a-btn"><span class="a-btn-text">确定</span></a>
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
			<td style='text-align: left'>
			<a href="javascript:void(0)"  class="easyui-linkbutton" id="searchForm" data-options="iconCls:'icon-search'">查询</a>
			<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
			</td>
		</tr>
	</table>
	<h5 id="hh" style="font-size: 30;font: bold;text-align: center;padding-top: 5px">科室诊断符合率统计</h5>
	</div>
	<div data-options="region:'center',border:false" style="width: 100%;">
		<table id="table1" class="easyui-datagrid" data-options="fit:true,fitColumns:true">
			<thead>
				<tr>
					<th data-options="field:'campus',rowspan:2,width:'10%',halign:'center',align:'left'">院区名称</th>
					<th data-options="field:'dept',rowspan:2,width:'6%',halign:'center',align:'right'">科室名称</th>
					<th data-options="field:'q',colspan:3,align:'center'">门诊与出院诊断符合率(%)</th>
					<th data-options="field:'w',colspan:3,align:'center'">入院与出院诊断符合率(%)</th>
					<th data-options="field:'e',colspan:3,align:'center'">住院手术前后诊断符合率(%)</th>
				</tr>
				<tr>
					<th data-options="field:'outClicTotal',width:'10%',halign:'center',align:'right'">门诊诊断相符人次</th>
					<th data-options="field:'outCliConsistent',width:'10%',halign:'center',align:'right'">门诊诊断总人次</th>
					<th data-options="field:'registerRise',width:'8%',halign:'center',align:'right'">符合率</th>
					<th data-options="field:'inhosConsistent',width:'10%',halign:'center',align:'right'">入院诊断相符人次</th>
					<th data-options="field:'inhosTotal',width:'10%',halign:'center',align:'right'">入院诊断总人次</th>
					<th data-options="field:'inHospitalCount',width:'8%',halign:'center',align:'right'">符合率</th>
					<th data-options="field:'operaConsis',width:'10%',halign:'center',align:'right'">术前术后诊断相符人次</th>
					<th data-options="field:'inHospitalPercent',width:'10%',halign:'center',align:'right'">术前术后相符总人次</th>
					<th data-options="field:'operaTotal',width:'8%',halign:'center',align:'right'">符合率</th>
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