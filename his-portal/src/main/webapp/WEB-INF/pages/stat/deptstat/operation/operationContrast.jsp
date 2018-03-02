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
<title>手术及操作时期对比表</title>
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
var flag;
$(function(){
	//医生科室
	$(".deptInput").MenuList({
		width :530, //设置宽度，不写默认为530，不要加单位
		height :400, //设置高度，不写默认为400，不要加单位
		dropmenu:"#m2",//弹出层id，必须要写
		isSecond:false,	//是否是二级联动的第二级
		para:'I',
		firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
	});
	$('#m2 .addList h2 input').click();
	$('a[name=\'menu-confirm\']').click();
	flag=$('#ksnew').getMenuIds();
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

function findData(){
	var start=$("#login").val();
}

function iReportInvoiceNo(){
	var data = $('#list').datagrid('getData');
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
	var data=$("#list").datagrid('getData');
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
	$("#tab").form('clear');
	$(".Input").text("");
	$("#list").datagrid('loadData',{total:0,rows:[]}); 
}
</script>
<body>
<div class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north',border:false" style="height:42px;width:100%">
    	<table id="tab" style="width: 100%;padding: 5px 5px 5px 5px;">
			<tr>
			<td style="width:40px;" align="left">日期:</td>
			<td style="width:110px;">
				<input id="login"  class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM'})" style="width:110px;height:22px;border: 1px solid #95b8e7;border-radius: 5px;"/>
			</td>
				<td style="width:55px;" align="center">科室:</td>
    			<td style="width:120px;" class="newMenu">
	    	       	<div class="deptInput menuInput" style="width:120px"><input style="width:95px" class="ksnew" id="ksnew" readonly="readonly"/><span></span></div> 
	    	       	<div id="m2" class="xmenu" style="display: none;">
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
							<div class="addList"></div>
							<div class="tip" style="display:none">没有检索到数据</div>
						</div>													 
					</div>
				</td>
				<td>
				<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" onclick="findData()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:print">
					<a href="javascript:void(0)" onclick="iReportInvoiceNo()" class="easyui-linkbutton" data-options="iconCls:'icon-printer'">打印</a>
				</shiro:hasPermission>
					<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</td>
			</tr>
		</table>
    </div>
    <div data-options="region:'center',border:false" style="width:100%">
    	<div class="easyui-layout" data-options="fit:true">
    		<div data-options="region:'north',border:false" style=";width:100%">
    			<table style="width:100%;padding: 5px 5px 5px 0px;">
		    		<tr>
		    			<td align="center"><font size="6" class="outpatienttit">手术及操作时期对比表</font></td>
		    		</tr>
		    	</table>
    		</div>
    		<div data-options="region:'center',border:false" style="width:100%">
    			<table class="easyui-datagrid"  id="list"  data-options="method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">   
				    <thead>   
				        <tr> 
				            <th data-options="field:'deptName',halign:'center',align:'right'" style="width:18%">    </th>   
				            <th data-options="field:'operationPeopleCount',halign:'center',align:'right'" style="width: 10%">1月</th>
				            <th data-options="field:'operationCount',halign:'center',align:'right'" style="width: 10%">2月</th>
				            <th data-options="field:'leaveHospitalCount',align:'right',halign:'center'" style="width: 10%">3月</th>
				            <th data-options="field:'operationProportion',align:'right',halign:'center'" style="width: 10%">4月</th>
				            <th data-options="field:'wholeHospitalPatient',align:'right',halign:'center'," style="width: 10%">2017年</th>
				            <th data-options="field:'operationPeopleCountProportion',align:'right',halign:'center'," style="width: 10%">2016年</th>
				            <th data-options="field:'operationPeopleCountProportion',align:'right',halign:'center'," style="width: 10%">增减数</th>
				            <th data-options="field:'operationPeopleCountProportion',align:'right',halign:'center'," style="width: 10%">百分比</th>
				        </tr>   
				    </thead>   
				</table>
    		</div>
    	</div>
    </div>   
</div>  
</body>
</html>