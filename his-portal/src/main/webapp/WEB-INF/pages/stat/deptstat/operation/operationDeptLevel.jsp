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
<title>手术科室手术分级统计</title>
<%@ include file="/common/metas.jsp"%>

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
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var menuAlias = '${menuAlias}';
var deptMap;//科室渲染
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
		//科室渲染
		$.ajax({
				url: '<%=basePath %>baseinfo/department/getDeptMap.action',
				type:'post',
				success: function(data) {
					deptMap=data;
				}
			});
	}
});
	
//查询按钮
function searchFrom(){
	 var startTime = $('#startTime').val();
	 var endTime = $('#endTime').val();
	 if(startTime==''||endTime==''){
		 $.messager.alert('提示','时间不能为空');
		 return false;
	 }
	 var deptCode = $('#deptName').getMenuIds();
	 if(deptCode!=''){
		 deptArr=deptCode.split(',');
		 deptCode=''
		 for(var i=0;i<deptArr.length;i++){
			 if(i>0){
				 deptCode+=',';
			 }
			 deptCode+=deptMap[deptArr[i]];
		 }
	 }else{
		 deptArr=flag.split(',');
		 deptCode=''
			 for(var i=0;i<deptArr.length;i++){
				 if(i>0){
					 deptCode+=',';
				 }
				 deptCode+=deptMap[deptArr[i]];
			 }
	 }
	 $('#tableList').datagrid({    
			url: '<%=basePath%>statistics/operationDeptLevel/queryOperationDeptLevel.action',
			queryParams: {
				deptCode: deptCode,
				startTime:startTime,
				endTime:endTime,
				menuAlias:menuAlias
			}
		}); 
}
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
function iReportInvoiceNo(){
	var data = $('#tableList').datagrid('getData');
	if (data.total==0) {
		$.messager.alert("友情提示", "列表无数据，无法打印");
		setTimeout(function(){
			$(".messager-body").window('close');
		},2000);
		return;
	}
}

//导出列表
function exportList() {
	var data=$("#tableList").datagrid('getData');
	if(data.total==0){
		$.messager.alert("友情提示", "列表无数据，无法导出");
		setTimeout(function(){
			$(".messager-body").window('close');
		},2000);
		return;
	}
}
/**
 * 重置
 * @author huzhenguo
 * @date 2017-03-17
 * @version 1.0
 */
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
					<input id="endTime" class="Wdate" type="text" name="endTime" value="${endTime }"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'})" style="height:22px;width:110px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
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
					&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					</shiro:hasPermission>
<%-- 					<shiro:hasPermission name="${menuAlias}:function:export"> --%>
<!-- 					<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" data-options="iconCls:'icon-down'">导出</a> -->
<%-- 					</shiro:hasPermission> --%>
					<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</td>
			</tr>
		</table>
    </div>
    <div data-options="region:'center',border:false" style="width:100%">
    	<div class="easyui-layout" data-options="fit:true">
    		<div data-options="region:'north',border:false" style=";width:100%">
    			<table style="width:100%;padding: 5px 5px 5px 0px;border: 0px;">
		    		<tr>
		    			<td align="center"><font size="6" class="outpatienttit">手术科室手术分级统计</font></td>
		    		</tr>
		    	</table>
    		</div>
    		<div data-options="region:'center',border:false" style="width:100%">
    			<table class="easyui-datagrid"  id="tableList"  data-options="method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">   
				    <thead>   
				        <tr> 
				            <th data-options="field:'deptName',align:'left',halign:'center' " style="width: 10%">科室名称</th>   
				            <th data-options="field:'levelA',align:'center',halign:'center' " style="width: 7%">一级</th>
				            <th data-options="field:'levelB',align:'center',halign:'center' " style="width: 7%">二级</th>
				            <th data-options="field:'levelC',align:'center',halign:'center' " style="width: 7%">三级</th>
				            <th data-options="field:'levelD',align:'center',halign:'center' " style="width: 7%">四级</th>
				            <th data-options="field:'sum',align:'center',halign:'center' " style="width: 7%">手术人数合计</th>
				            <th data-options="field:'transOut',align:'center',halign:'center'" style="width: 7%">转出</th>
				            <th data-options="field:'sumAndTransOut',align:'center',halign:'center' " style="width: 7%">合计(含转出)</th>
				            <th data-options="field:'percentA',align:'center',halign:'center',formatter:functionRate" style="width: 7%">一级百分比</th>
				            <th data-options="field:'percentB',align:'center',halign:'center',formatter:functionRate" style="width: 7%">二级百分比</th>
				            <th data-options="field:'percentC',align:'center',halign:'center',formatter:functionRate" style="width: 7%">三级百分比</th>
				            <th data-options="field:'percentD',align:'center',halign:'center',formatter:functionRate" style="width: 7%">四级百分比</th>
				        </tr>   
				    </thead>   
				</table>
    		</div>
    	</div>
    </div>   
</div>  
</body>
</html>