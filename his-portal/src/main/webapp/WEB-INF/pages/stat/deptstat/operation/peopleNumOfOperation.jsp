<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>手术科室手术人数统计</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<style type="text/css">
	.panel-body,.panel{
			overflow:visible;
		}
		.addList dl:first-child ul {
			overflow:visible; !important; 
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
			overflow:visible; !important; 
			clear:both;
		}
		.clearfix{
		*zoom:1;    /* IE/7/6*/
		}
</style>

<style type="text/css">
	.tableCss{
		border-collapse: collapse;border-spacing: 0;border: 1px solid #95b8e7;width:100%;
	}
	.tableCss .TDlabel{
		text-align: right;
		font-size:14px;
		width:200px;
	}
	.tableCss td{
		border: 1px solid #95b8e7;
		padding: 5px 5px;
		word-break: keep-all;
		white-space:nowrap;
	}
	.easyuiInput{
		width:200px;
	}
	.Input{
		width:200px;
	}
</style>
</head>
<script type="text/javascript">
var menuAlias = '${menuAlias}';
var deptMap=new Map();//科室渲染
var flag;//全局授权科室
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
		var deptCode = $('#deptName').getMenuIds();
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

	//科室渲染
	$.ajax({
			url: '<%=basePath %>baseinfo/department/getDeptMap.action',
			type:'post',
			success: function(data) {
				deptMap=data;
			}
		});
	});
// 	/**
// 	 * 科室列表页 显示
// 	 */
// function deptFamater(value,row,index){
// 	if(value!=null&&value!=''){
// 		return deptMap[value];
// 	}
// }

//查询按钮
function searchFrom(){
	 var startTime = $('#startTime').val();
	 var endTime = $('#endTime').val();
	 if(startTime==''||endTime==''){
		 $.messager.alert('提示','时间不能为空');
		 return false;
	 }
	 var deptCode = $('#deptName').getMenuIds();
	 if(deptCode==''||deptCode==null){
		 deptCode=flag;
	 }
	 $('#tableList').datagrid({    
		 url: '<%=basePath %>statistics/peopleNumOfOperation/queryPeopleNumOfOperation.action',
			queryParams: {
				deptCode: deptCode,
				startTime:startTime,
				endTime:endTime,
				menuAlias:menuAlias
			}
		}); 
}

function iReportInvoiceNo(){
			var rows = $('#tableList').datagrid('getRows');
			if(rows.length > 0){
				$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否导出
			 	if (res) {
			 		var rows = $('#tableList').datagrid('getRows');
					$('#reportJson').val(JSON.stringify(rows));
					//表单提交 target
					var formTarget="reportForm";
					var tmpPath = "<%=basePath%>statistics/peopleNumOfOperation/reportPeopleNumOfOperation.action";
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
			$.messager.alert("友情提示", "列表无数据，无法打印");
			setTimeout(function(){
				$(".messager-body").window('close');
			},2000);
			return;
		}
}


//导出列表
function exportList() {
	var rows = $('#tableList').datagrid('getRows');
	if(rows.length > 0){
		$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
	 		if (res) {
	 			var rows = $('#tableList').datagrid('getRows');
				$('#reportJson').val(JSON.stringify(rows));
				$('#reportForm').form('submit', {
					url :"<%=basePath%>statistics/peopleNumOfOperation/exportPeopleNumOfOperation.action",
					onSubmit : function(param) {
					
					},
					success : function(data) {
						$.messager.alert("操作提示", "导出失败！", "success");
					},
					error : function(data) {
						$.messager.alert("操作提示", "网络繁忙！", "error");
					}
				});
	 		}
		});
	}else{
		$.messager.alert("友情提示", "列表无数据，无法导出");
		setTimeout(function(){
			$(".messager-body").window('close');
		},2000);
		return;
	}
	
}

//重置
function clears(){
	$("#startTime").val("${startTime }");
	$('#endTime').val("${endTime}");
	$('a[name=\'menu-confirm-clear\']').click();
	$("#tableList").datagrid('loadData',{total:0,rows:[]});
}
</script>
<body>
<div class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north',border:false" style="height:42px;width:100%">
    	<table id="tab" style="width: 100%;padding: 5px 5px 5px 5px;">
			<tr>
			<!-- 开始时间 --> 
				<td style="width:80px;" align="left">开始时间:</td>
				<td style="width:110px;">
					<input id="startTime" class="Wdate" type="text" name="startTime" value="${startTime }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
				</td>
				<!-- 结束时间 --> 
				<td style="width:80px;" align="center">结束时间:</td>
				<td style="width:110px" id="td1">
					<input id="endTime" class="Wdate" type="text" name="endTime" value="${endTime }"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
				</td>
				<td style="width: 50px">科室:</td>
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
									<ul class="addDept"></ul>
								</div>	
								<div class="depts-dl">
									<div class="addList">
									</div>
									<div class="tip" style="display:none">没有检索到数据</div>
								</div>	
							</div>
		    		</td>
				<td>
					<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:print">
						<a href="javascript:void(0)" onclick="iReportInvoiceNo()" class="easyui-linkbutton" data-options="iconCls:'icon-printer'">打印</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:export">
						<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" data-options="iconCls:'icon-down'">导出</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</table>
    </div>
    <div data-options="region:'center',border:false" style="width:100%">
    	<div class="easyui-layout" data-options="fit:true">
    		<div data-options="region:'north',border:false" style=";width:100%">
    			<table style="width:100%;padding: 5px 5px 5px 0px;">
		    		<tr>
		    			<h5 align="center"style="font-size: 32;font: bold;text-align: center;padding-top: 15px">手术科室手术人数统计</h5>
		    		</tr>
		    	</table>
    		</div>
    		<div data-options="region:'center',border:false" style="width:100%">
    			<table class="easyui-datagrid"  id="tableList"  data-options="method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">   
				    <thead>   
				        <tr> 
				            <th data-options="field:'dept_name',halign:'left',align:'left'" style="width: 22%">科室名称</th>   
				            <th data-options="field:'optnums',halign:'center',align:'center'" style="width: 13%">手术人数</th>
				            <th data-options="field:'optcounts',halign:'center',align:'center'" style="width: 13%">手术例数</th>
				            <th data-options="field:'cyzs',align:'center',halign:'center'" style="width: 13%">出院总数</th>
				            <th data-options="field:'operaPro',align:'center',halign:'center'" style="width: 13%">手术占比</th>
				            <th data-options="field:'totalPatient',align:'center',halign:'center'," style="width: 13%">全院患者</th>
				            <th data-options="field:'ssrszqybb',align:'center',halign:'center'," style="width: 11%">手术人数占全院比</th>
				        </tr>   
				    </thead>   
				</table>
				<form id="exportForm" method="post">
				</form>
    		</div>
    	</div>
    </div>   
</div>  
		<form id="reportForm" method="post">
			<input type="hidden" id="reportTime" name="reportTime"/>
			<input type="hidden" id="reportJson" name="reportJson">
			<input type="hidden" id="fileName" name="fileName" value="SSKSSSRSTJHXN">
		</form>
</body>
</html>