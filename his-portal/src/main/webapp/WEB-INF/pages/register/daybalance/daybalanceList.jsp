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
<body>
		<div id="divLayout" class="easyui-layout daybalance" data-options="fit:true">
	        <div data-options="region:'north',split:false" style="padding:10px;min-height:12px;height:50px;border-top:0;">	        
				<form id="searchForm" method="post" action="${pageContext.request.contextPath}/finance/daybalance/queryDaybalance.action?menuAlias=GHYRJ">
					<table cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td style="padding: 5px 15px;">日结时间：</td>
							<td>
							<input id="startTime"  name="startTime" onchange="startTime1()" class="Wdate" type="text"  value="<fmt:formatDate value="${registerDaybalance.startTime }" pattern="yyyy-MM-dd HH:mm:ss"/>" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td>&nbsp;&nbsp;至&nbsp;&nbsp;</td>
							<td>
							<input id="endTime"  name="endTime" onchange="endTime1()" class="Wdate" type="text" value="<fmt:formatDate value="${registerDaybalance.endTime }" pattern="yyyy-MM-dd HH:mm:ss"/>" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td>
							<shiro:hasPermission name="${menuAlias }:function:query">
								&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							</shiro:hasPermission>
							</td>
						</tr>
					</table>
				</form>	
			</div>
			<div data-options="region:'center',split:false,title:'日结信息',iconCls:'icon-book'" style="padding:10px;">
				<c:if test="${!empty balancedetailList }">
					<form id="saveForm" method="post">
						<div id = "detailJson" style="display:none">${detailJson }</div>
						<input type="hidden" id="detailJsonHid" name="detailJson">
						<input type="hidden" id="startTimeStr" name="startTimeStr" value="<fmt:formatDate value="${registerDaybalance.startTime }" pattern="yyyy-MM-dd HH:mm:ss"/>">
						<input type="hidden" id="endTimeStr" name="endTimeStr" value="<fmt:formatDate value="${registerDaybalance.endTime }" pattern="yyyy-MM-dd HH:mm:ss"/>">
						<input type="hidden" name="totQty" value="${registerDaybalance.totQty }">
						<input type="hidden" id="subStartTime" value="<fmt:formatDate value="${startTime }" pattern="yyyy-MM-dd HH:mm:ss"/>">
						<input type="hidden" id="subEndTime" value="<fmt:formatDate value="${endTime }" pattern="yyyy-MM-dd HH:mm:ss"/>">
						<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="">
							<tr>
								<td>日结时间：</td>
				    			<td colspan="7">
				    				<fmt:formatDate value="${registerDaybalance.startTime }" pattern="yyyy年MM月dd日  HH时mm分ss秒"/>
				    				&nbsp;&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;&nbsp;
				    				<fmt:formatDate value="${registerDaybalance.endTime }" pattern="yyyy年MM月dd日  HH时mm分ss秒"/>
				    			</td>
			    			</tr>
			    			<c:forEach var="list" items="${balancedetailList }" varStatus="status">
				    			<tr>
									<td rowspan="2">${psyTypeMap[list.payType] }：</td>
									<td >挂号数量：</td>
									<td>${list.regNum }</td>
									<td>挂号金额</td>
									<td>${list.regFee }</td>
									<td rowspan="2">合计：</td>
									<td rowspan="2">数量：${list.sumNum }&nbsp;&nbsp;金额：${list.sumFee }</td>
				    			</tr>
				    			<tr>
									<td>退号数量：</td>
									<td>${list.quitNum }</td>
									<td>退号金额</td>
									<td>${list.quitFee }</td>
				    			</tr>
			    			</c:forEach>
			    			<tr>
								<td>总计：</td>
				    			<td colspan="7">
				    				总挂号人数：${registerDaybalance.inNum }&nbsp;人&nbsp;&nbsp;&nbsp;&nbsp;总退号人数：${registerDaybalance.outNum }&nbsp;人&nbsp;&nbsp;&nbsp;&nbsp;实际看诊人数：${registerDaybalance.actualNum }&nbsp;人&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				    				共收入：${registerDaybalance.income }&nbsp;元&nbsp;&nbsp;&nbsp;&nbsp;共退费：${registerDaybalance.refund }&nbsp;元&nbsp;&nbsp;&nbsp;&nbsp;实际总收入：${registerDaybalance.actualIncome }&nbsp;元
				    			</td>
			    			</tr>
				    	</table>
					    <div style="text-align:center;padding:5px">
						    <shiro:hasPermission name="${menuAlias}:function:checkout">
						    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">结账</a>
						    </shiro:hasPermission>
					    </div>
			    	</form>
				</c:if>
				<div>
		</div>
		<script type="text/javascript">
		function startTime1(){
			searchFrom();
		}
		function endTime1(){
			searchFrom();
		}
		function print(){
			var iLeft='0px';
			var iTop='0px';
			window.open("<c:url value='/finance/daybalance/printList.action'/>?startTime="+$('#startTimeStr').val()+"&endTime="+$('#endTimeStr').val(),'newwindow','left='+iLeft+',top='+iTop+',width="800",height="800",scrollbars,resizable=yes,toolbar=no');
		}
		//页面加载
		$(function(){
			var detailJson = $('#detailJson').text();
			if(detailJson!=null&&detailJson!=""){
				$('#detailJsonHid').val(detailJson);
			}
		});
		//查询
		function searchFrom(){
			var startTime = $('#startTime').val();	
			var endTime = $('#endTime').val();	
			if(!isStartEndDate(startTime,endTime,false)){
				$.messager.alert('提示',"开始时间必须小于结束时间！");
				return false;
			}
			$('#searchForm').submit();
		}
		
		//结账
		function submit(){
			//获得应该结账的开始和结束时间
			var subStartTime = $('#subStartTime').val();
			var subEndTime = $('#subEndTime').val();
			//获得当前要结账的开始和结束时间
			var startTimeStr = $('#startTimeStr').val();
			var endTimeStr = $('#endTimeStr').val();
			//如果当前要结账的开始时间小于应该结账的开始　　则结账数据有可能重复　不予许结账
			if(isStartEndDate(startTimeStr,subStartTime,false)){
				$.messager.alert('提示',"结账数据重复,请重新选择！");
				return;
			}
			//如果当前要结账的开始时间大于应该结账的开始　　则结账数据有可能漏结　不予许结账
			if(!isStartEndDate(startTimeStr,subStartTime,true)){
				$.messager.alert('提示',"开始结账时间前仍有未结账信息,请重新选择！");
				return;
			}
			//如果当前结账结束时间大于当前时间　则结账数据可能漏结　不予许结账
			if(!isStartEndDate(endTimeStr,subEndTime,false)){
				$.messager.alert('提示',"不能提前结账,请重新选择！");
				return;
			}
			//结账
			$.messager.progress({text:'结账中，请稍后...',modal:true});
			$('#saveForm').form('submit',{  
		        url: "<c:url value='/finance/daybalance/saveDaybalance.action'/>?menuAlias=GHYRJ",
		        success:function(data){  
		        	$.messager.progress('close');
		        	var dataMap = eval("("+data+")");
		        	if(dataMap.resMsg=="error"){
		        	}else if(dataMap.resMsg=="success"){
		        		var rid=dataMap.rid;
		        		jQuery.messager.confirm("提示","保存成功，是否打印门诊挂号日结单？",function(event){
		        			if(event){
		        				var startTime = $('#startTimeStr').val(); //开始时间
		        				var endTime = $('#endTimeStr').val();//结束时间
		        				var timerStr = Math.random();
		        			    window.open ("<c:url value='/finance/daybalance/iReportToInspectionDailySettlement.action?randomId='/>"+timerStr+"&startTime="+startTime+"&endTime="+endTime+"&rid="+rid+"&fileName=MZGHYRJ",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
		        				window.location.href="<c:url value='/finance/daybalance/listDaybalance.action'/>?menuAlias=GHYRJ";
		        			}else{
		        				window.location.href="<c:url value='/finance/daybalance/listDaybalance.action'/>?menuAlias=GHYRJ";
		        			}
		        		});
		        		
		  //      		window.location.href="<c:url value='/register/listDaybalance.action'/>?menuAlias=GHYRJ";
		        		
		        	}else{
		        		$.messager.alert('提示','未知错误,请联系管理员!');
		        	}
		        },
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert('提示','保存失败！');	
				}							         
		    }); 
		}
		
		//时间比较
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