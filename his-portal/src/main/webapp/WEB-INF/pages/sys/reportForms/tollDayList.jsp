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
		border-left: 1px solid #95b8e7;
		border-top: 1px solid #95b8e7;
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
	<div data-options="region:'north',split:false" style="padding:10px;min-height:12px;height:auto;">	        
		<form id="searchForm" method="post" action="<%=basePath %>finance/tollDay/queryInvoiceinfo.action?menuAlias=${menuAlias }">
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td style="padding: 5px 15px;">日结时间:</td>
					<td>
					<input id="startTime"  name="startTime"  class="Wdate" type="text"  value="<fmt:formatDate value="${dayBalance.beginDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>
<%-- 					<input class="easyui-datetimebox" id="startTime" name="dayBalance.beginDate" value="<fmt:formatDate value="${dayBalance.beginDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" data-options="showSeconds:true" style="width:180px"> --%>
					</td>
					<td>&nbsp;至&nbsp;</td>
					<td>
<%-- 					<input class="easyui-datetimebox" id="endTime" name="dayBalance.endDate" value="<fmt:formatDate value="${dayBalance.endDate }" pattern="yyyy-MM-dd HH:mm:ss"/>"  data-options="showSeconds:true" style="width:180px"> --%>
					<input id="endTime"  name="endTime"  class="Wdate" type="text" value="<fmt:formatDate value="${dayBalance.endDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:180px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
					<td>
					<shiro:hasPermission name="${menuAlias }:function:query">
						&nbsp;<a href="javascript:searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					</shiro:hasPermission>
					</td>
				</tr>
			</table>
		</form>	
	</div>
	<div data-options="region:'center',split:false,title:'日结信息',iconCls:'icon-book',fit:true" style="padding:10px;" align="center">
		<c:if test="${!empty vo }">
			<form id="saveForm" method="post">
				<table class="tableCss" cellspacing="5" cellpadding="5" align="center">
					<thead>
						<tr>
							<td class="tableLabel" width="20%">发票起始号码</td>
							<td class="tableLabel" width="10%">张数</td>
							<td class="tableLabel" width="15%">实收</td>
							<td class="tableLabel" width="15%">记账</td>
							<td class="tableLabel" width="15%">合计</td>
							<td class="tableLabel" width="10%">单数</td>
							<td class="tableLabel" width="15%">备注</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="list" items="${voList }" varStatus="status">
							<tr>
								<c:if test="${!status.last}">
									<td>${list.invoices}</td>
								</c:if>
								<c:if test="${status.last}">
									<td class="tableLabel">${list.invoices}</td>
								</c:if>
								<td>${list.num}</td>
								<td>${list.income}</td>
								<td>${list.accounting}</td>
								<td>${list.total}</td>
								<td>${list.extFlag}</td>
								<td>${list.remarks}</td>
							</tr>
						</c:forEach>
						<tr>
							<td class="tableLabel">实收金额（大写）：</td>
							<td colspan="6">${vo.money}</td>
						</tr>
						<tr>
							<td class="tableLabel">收款人：</td>
							<td colspan="2">${vo.gathering}</td>
							<td class="tableLabel">收款员：</td>
							<td colspan="3">${vo.receiver}</td>
						</tr>
						<tr>
							<td class="tableLabel">填表人：</td>
							<td colspan="2">${vo.fill}</td>
							<td class="tableLabel">出纳员：</td>
							<td colspan="3">${vo.cashier}</td>
						</tr>
						<tr>
							<td class="tableLabel">统计时间：</td>
							<td colspan="6">${vo.countTime}<input type="hidden" id="startTimeStr" name="dayBalance.beginDate" value="${vo.startTimeStr}"><input type="hidden" id="endTimeStr" name="dayBalance.endDate" value="${vo.endTimeStr}"></td>
						</tr>
					</tbody>
				</table>
				<div style="text-align:center;padding:5px">
				    <shiro:hasPermission name="${menuAlias}:function:checkout">
				    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">结账</a>
				    </shiro:hasPermission>
			    </div>
			</form>
		</c:if>
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
	$('#searchForm').submit();
}

//结账
function submit(){
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
	//如果当前结账结束时间大于当前时间　则结账数据可能漏结　不予许结账
// 	if(!isStartEndDate(endTimeStr,subEndTime,false)){
// 		$.messager.alert('提示',"不能提前结账,请重新选择！");
// 		return;
// 	}
	//结账
	$.messager.progress({text:'执行中，请稍后...',modal:true});
	$('#saveForm').form('submit',{  
        url: "<c:url value='/finance/tollDay/saveDaybalance.action'/>?menuAlias=${menuAlias}",
        success:function(data){  
        	$.messager.progress('close');
        	var dataMap = eval("("+data+")");
        	$.messager.alert('提示',dataMap.resCode);
        	if(dataMap.resMsg == 'success'){
        		$.messager.alert('提示',dataMap.resCode);
        		window.location.href="<c:url value='/finance/tollDay/tollDayList.action'/>?menuAlias=${menuAlias}";
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