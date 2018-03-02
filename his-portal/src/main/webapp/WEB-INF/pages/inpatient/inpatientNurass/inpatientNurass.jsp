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
<title>入院评估单</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
var sexMap=new Map();//性别渲染
var deptList='';//科别渲染
var type=1;
$(function(){
	findInvoiceNoSummary()

	//绑定回车事件
	$(document).keydown(function(event){ 
		if(event.keyCode==13){ 
			console.log("13")
			query()
		} 
	})
	$('#tabs').tabs({
	    onSelect:function(title){ 
	    	if(title == "未评估"){
	    		findInvoiceNoSummary()
	    		type=1
	    	}else{
	    		findInvoiceDetailed()
	    		type=2
	    	}
	    } 
	});
});

function query(){
	if(type==1){
		findInvoiceNoSummary() 
	}else{
		findInvoiceDetailed()
	}
}

/**
 * 重置
 * @author huzhenguo
 * @date 2017-03-17
 * @version 1.0
 */
function clears(){
	$("#userName").val("")
	$("#admissionNumber").val("")
	$("#bedNum").val("")
	findInvoiceNoSummary()
	findInvoiceDetailed()
}
function findInvoiceNoSummary(){
	$('#invoiceNoSummary').datagrid({
		url: "<%=basePath%>publics/assessment/queryVoList.action",
		queryParams:getSendData(),
		pagination:true,
       	pageSize:20,
       	pageList:[20,30,50,100],
       	singleSelect: true,
       	onDblClickRow: function (index, row) {
			var tempWinPath = "<c:url value='/publics/assessment/querInpatientNurass.action'/>?inpatientNo="+row.inpatientNo;
				window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -680) +',height='+ (screen.availHeight-370) +',scrollbars,resizable=yes,toolbar=no')
		},
       	onLoadSuccess:function(data){
			//分页工具栏作用提示
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
function findInvoiceDetailed(){
	$('#invoiceDetailed').datagrid({
		url: "<%=basePath%>publics/assessment/queryVoListed.action",
		queryParams:getSendData(),
		pagination:true,
		pageSize:20,
       	pageList:[20,30,50,100],
       	singleSelect: true,
       	onDblClickRow: function (index, row) {
			var tempWinPath = "<c:url value='/publics/assessment/querInpatientNurassed.action'/>?inpatientNo="+row.inpatientNo;
				window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -680) +',height='+ (screen.availHeight-370) +',scrollbars,resizable=yes,toolbar=no')
		},
       	onLoadSuccess:function(data){
			//分页工具栏作用提示
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

function getSendData (){
	return {
		name:$("#userName").val(),
		MEDICALRECORD_ID:$("#admissionNumber").val(),
		bedNo:$("#bedNum").val()
	}
}
$.ajax({
	url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
	data:{"type":"sex"},
	type:'post',
	success: function(data) {
		var v = data;
		for(var i=0;i<v.length;i++){
			sexMap.put(v[i].encode,v[i].name);
		}
	}
});
$.ajax({
	url: "${pageContext.request.contextPath}/baseinfo/department/queryDepartments.action",
	type:'post',
	async:false,
	success: function(deptListData) {
		deptList = deptListData;
	}
});
//性别渲染
function sexRend(value){
	return sexMap.get(value);
}
//科别渲染
function deptFamater(value){
	if(value!=null){
		for(var i=0;i<deptList.length;i++){
			if(value==deptList[i].id){
				return deptList[i].deptName;
			}
		}	
	}
}
</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north',border:false" style="height:40px;width: 100%;padding:5px 5px 0px 5px;">
    	<table style="width: 100%;">
			<tr>
				<td style="width:60px;" align="left">姓名:</td>								
				<td style="width:100px;">
				<input id = "userName"  type="text" style="height:22px; width:110px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
				</td>
				<td style="width:80px;" align="center">住院号</td>
				<td style="width:110px;">
				<input id= "admissionNumber" type="text" style="height:22px;width:110px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
				</td>
				<td style="width:60px;" align="center">床号:</td>
				<td style="width:120px;">
				<input id = "bedNum" type="text" style="height:22px;width:110px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
				</td>
				<td>
					<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</td>
			</tr>
		</table>
    </div>   
    <div data-options="region:'center'" style="width: 100%">
    	<div id="tabs" class="easyui-tabs" data-options="fit:true">   
		    <div title="未评估" data-options="fit:true">   
		        <table id="invoiceNoSummary" class="easyui-datagrid" style="width:100%" data-options="fit:true,border:false">    
				    <thead>   
				        <tr>
				            <th data-options="field:'pname',width:'10%'">姓名</th>
				            <th data-options="field:'psex',width:'10%',formatter: sexRend">性别</th>  
				            <th data-options="field:'page',width:'10%'">年龄</th>   
				            <th data-options="field:'deptCode',width:'25%',formatter: deptFamater">科别</th>   
				            <th data-options="field:'bedNo',width:'15%'">床号</th> 
				            <th data-options="field:'medicalrecodeId',width:'20%'">住院号</th>  
				            <th data-options="field:'inpatientNo',hidden:true"></th>
				        </tr>   
				    </thead>   
				</table>
		    </div>   
		    <div title="已评估"  data-options="fit:true">   
		    	<table id="invoiceDetailed" class="easyui-datagrid" style="width:100%" data-options="fit:true,border:false">    
				    <thead>   
				        <tr>
				            <th data-options="field:'pname',width:'10%'">姓名</th>
				            <th data-options="field:'psex',width:'10%'">性别</th>  
				            <th data-options="field:'page',width:'10%'">年龄</th>   
				            <th data-options="field:'deptCode',width:'25%',formatter: deptFamater">科别</th>   
				            <th data-options="field:'bedNo',width:'15%'">床号</th> 
				            <th data-options="field:'medicalrecodeId',width:'20%'">住院号</th>
				            <th data-options="field:'inpatientNo',hidden:true"></th>  
	 				   </tr>   
				    </thead>   
				</table>
		    </div>   
		</div>  
    </div>   
</div>

</body>
</html>