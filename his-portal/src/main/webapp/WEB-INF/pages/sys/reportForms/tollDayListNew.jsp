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
	<style type="text/css">
	.tableCss {
		border-collapse: collapse;
		border-spacing: 0;
	}
	
	.tableLabel {
		text-align: center;
		width: 150px;
	}
	
	.tableCss td {
		border-right: 1px solid #95b8e7;
		border-bottom: 1px solid #95b8e7;
		padding: 5px 15px;
		word-break: keep-all;
		white-space: nowrap;
	}
	</style>
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',split:false" style="padding:10px;height:49px;border-top:0">	        
<%-- 		<form id="searchForm" method="post" action="<%=basePath %>finance/tollDay/queryInvoiceinfoNew.action?menuAlias=${menuAlias }"> --%>
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td style="padding: 5px">日结时间:</td>
					<td>
					<input id="startTime"  name="startTime"  class="Wdate" type="text"  value="<fmt:formatDate value="${dayBalance.beginDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:175px;border: 1px solid #95b8e7;border-radius: 5px;"/>
<%-- 					<input class="easyui-datetimebox" id="startTime" name="dayBalance.beginDate" value="<fmt:formatDate value="${dayBalance.beginDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" data-options="showSeconds:true" style="width:180px"> --%>
					</td>
					<td>&nbsp;至&nbsp;</td>
					<td>
<%-- 					<input class="easyui-datetimebox" id="endTime" name="dayBalance.endDate" value="<fmt:formatDate value="${dayBalance.endDate }" pattern="yyyy-MM-dd HH:mm:ss"/>"  data-options="showSeconds:true" style="width:180px"> --%>
					<input id="endTime"  name="endTime"  class="Wdate" type="text" value="<fmt:formatDate value="${dayBalance.endDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:175px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
					<td>
					<shiro:hasPermission name="${menuAlias }:function:query">
						&nbsp;<a href="javascript:searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					</shiro:hasPermission>
					</td>
				</tr>
			</table>
<!-- 		</form>	 -->
	</div>
	<div data-options="region:'center',title:'日结信息'" >
<!-- 			<form id="saveForm" method="post"> -->
				<table class="easyui-datagrid" id="list" style="width:auto;height:auto" data-options="method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
					<thead>
						<tr>   
						  	<th data-options="field:'payKindCode',hidden:true" ></th>
				            <th data-options="field:'payKindName'" style="width:100px">统计名称</th>   
				            <th data-options="field:'totalCost'" style="width:100px">总金额</th>  
				            <th data-options="field:'sybOwnCost'" style="width:120px">省医保个人负担</th>
				            <th data-options="field:'sybPubCost'" style="width:120px">省医保医保支付</th>
				            <th data-options="field:'smxbOwnCost'" style="width:120px">省慢性病个人负担</th>   
				            <th data-options="field:'smxbPayCost'" style="width:120px">省慢性病个人自付</th>   
				            <th data-options="field:'smxbPubCost'" style="width:120px">省慢性病医保支付</th>   
				            <th data-options="field:'slxOwnCost'" style="width:120px">省离休个人负担</th>   
				            <th data-options="field:'slxPayCost'" style="width:120px">省离休医保支付</th>   
				            <th data-options="field:'shiybOwnCost'" style="width:120px">市医保个人负担</th>   
				            <th data-options="field:'shiybPubCost'" style="width:120px">市医保医保支付</th>   
				            <th data-options="field:'ownCost'" style="width:120px">自费</th>   
				            <th data-options="field:'ssyOwnCost'" style="width:120px">省生育个人负担</th>   
				            <th data-options="field:'ssyPayCost'" style="width:120px">省生育个人自付</th>   
				            <th data-options="field:'ssyPuvCost'" style="width:120px">省生育医保支付</th>   
				            <th data-options="field:'nhOwnCost'" style="width:120px">农合个人负担</th>   
				            <th data-options="field:'nhPayCost'" style="width:120px">农合个人自付</th>   
				            <th data-options="field:'nhPubCost'" style="width:120px">农合医保支付</th>   
				            <th data-options="field:'nyhgOwnCost'" style="width:120px">能源化工个人负担</th>   
				            <th data-options="field:'nyhgPayCost'" style="width:120px">能源化工个人自付</th>   
				            <th data-options="field:'nyhgPubCost'" style="width:120px">能源化工医保支付</th>   
				            <th data-options="field:'sbjjbxOwnCost'" style="width:150px">省保健局保险个人负担</th>   
				            <th data-options="field:'sbjjbxPayCost'" style="width:150px">省保健局保险个人自付</th>   
				            <th data-options="field:'sbjjbxPubCost'" style="width:150px">省保健局保险优惠</th>   
				            <th data-options="field:'sjxnhptOwnCost'" style="width:180px">省级新农合平台个人负担</th>   
				            <th data-options="field:'sjxnhptPayCost'" style="width:180px">省级新农合平台个人自付</th>   
				            <th data-options="field:'sjxnhptPubCost'" style="width:180px">省级新农合平台 医保支付</th>   
				            <th data-options="field:'ydjysdOwnCost'" style="width:180px">异地就医(试点)个人负担</th>   
				            <th data-options="field:'ydjysdPayCost'" style="width:180px">异地就医(试点)个人自付</th>   
				            <th data-options="field:'ydjysdPubCost'" style="width:180px">异地就医(试点)医保支付</th>   
				            <th data-options="field:'stlylbxOwnCost'" style="width:180px">郑州铁路医疗保险个人负担</th>   
				            <th data-options="field:'stlylbxPayCost'" style="width:180px">郑州铁路医疗保险个人自付</th>   
				            <th data-options="field:'stlylbxPubCost'" style="width:180px">郑州铁路医疗保险医保支付</th>   
				        </tr>   
					</thead>
				</table>
				<div style="text-align:center;padding:5px">
				    <shiro:hasPermission name="${menuAlias}:function:checkout">
				    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">结账</a>
				    </shiro:hasPermission>
			    </div>
<!-- 			</form> -->
<input type="hidden" id="startTimeStr" name="dayBalance.beginDate" value="${dayBalance.beginDate }">
<input type="hidden" id="endTimeStr" name="dayBalance.endDate" value="${dayBalance.endDate }">
	</div>
</div>
<script type="text/javascript">

/***
 * 时间段查询
 */
function searchFrom(){
	var startTime = $('#startTime').val();	
	var endTime = $('#endTime').val();	
	if(!isStartEndDate(startTime,endTime,false)){
		$.messager.alert('提示','开始时间必须小于结束时间！');
		close_alert();
		return false;
	}
	$('#list').datagrid({
		url:"<%=basePath %>finance/tollDay/queryInvoiceinfoNew.action?menuAlias=${menuAlias }",
		queryParams:{startTime:startTime,endTime:endTime}
	});
}

//结账
function submit(){
	var listRows = $('#list').datagrid('getRows');
	if(listRows==null||listRows.length<=0){
		$.messager.alert('提示',"未查询到结账信息，不可结账！");
		close_alert();
		return;
	}
	//获得应该结账的开始和结束时间
	var subStartTime = $('#startTime').val();
	var subEndTime = $('#endTime').val();
	//获得当前要结账的开始和结束时间
	var startTimeStr = $('#startTimeStr').val();
	var endTimeStr = $('#endTimeStr').val();
	//如果当前要结账的开始时间小于应该结账的开始　　则结账数据有可能重复　不予许结账
	if(isStartEndDate(startTimeStr,subStartTime,false)){
		$.messager.alert('提示',"结账数据重复,请重新选择！");
		close_alert();
		return;
	}
	//如果当前要结账的开始时间大于应该结账的开始　　则结账数据有可能漏结　不予许结账
	if(!isStartEndDate(startTimeStr,subStartTime,true)){
		$.messager.alert('提示',"开始结账时间前仍有未结账信息,请重新选择！");
		close_alert();
		return;
	}
	//结账
	$.messager.progress({text:'执行中，请稍后...',modal:true});
	$.ajax({
		url:"<c:url value='/finance/tollDay/saveDaybalanceNew.action'/>?menuAlias=${menuAlias}",
		data:{subStartTime:subStartTime,subEndTime:subEndTime},
		success:function(data){
			$.messager.progress('close');
			if(data.resCode == 'success'){
        		$.messager.alert('提示',data.resMsg,'info',function(){
	        		window.location.href="<c:url value='/finance/tollDay/tollDayList.action'/>?menuAlias=${menuAlias}";
        		});
        	}
		},
		error : function(data) {
			$.messager.progress('close');
			$.messager.alert('提示','保存失败！');
		}
	});
}

/***
 * 时间比较
 */
function isStartEndDate(startDate, endDate,bool) {
	if (startDate.length > 0 && endDate.length > 0) {
		var startDateTemp = startDate.split(" ");
		var endDateTemp = endDate.split(" ");
		var arrStartDate = startDateTemp[0].split("-");
		var arrEndDate = endDateTemp[0].split("-");
		var arrStartTime = startDateTemp[1].split(":");
		var arrEndTime = endDateTemp[1].split(":");
		var allStartDate = new Date(arrStartDate[0], arrStartDate[1],arrStartDate[2], arrStartTime[0], arrStartTime[1],arrStartTime[2]);
		var allEndDate = new Date(arrEndDate[0], arrEndDate[1],arrEndDate[2], arrEndTime[0], arrEndTime[1], arrEndTime[2]);
		if (allStartDate.getTime() == allEndDate.getTime()) {
			if(bool){
				return true;
			}
			return false;
		}
		if (allStartDate.getTime() > allEndDate.getTime()) {	
			return false;
		}
	}
	return true;
}
</script>
</body>
</html>