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
	<div class="easyui-panel" id="panelEast" style="border:0px">
		<div style="padding: 10px">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="honry-lable" style="font-size: 14">
						所属科室:
					</td>
					<td class="honry-view" style="font-size: 14">
						${schedule.scheduleDeptname }
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						排班分类:
					</td>
					<td class="honry-view" style="font-size: 14">
						<c:choose>
							<c:when test="${schedule.scheduleClass eq '1'}">
							挂号排班
							</c:when>
							<c:when test="${schedule.scheduleClass eq '2'}">
							工作排班
							</c:when>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						工作科室:
					</td>
					<td class="honry-view" style="font-size: 14">
						${deptMap[schedule.scheduleWorkdept] }
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						排班类型:
					</td>
					<td class="honry-view" style="font-size: 14">
						<c:choose>
							<c:when test="${schedule.type eq '1'}">
							科室排班
							</c:when>
							<c:when test="${schedule.type eq '2'}">
							医生排班
							</c:when>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						工作诊室:
					</td>
					<td class="honry-view" style="font-size: 14">
						${schedule.scheduleClinicname }
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						员工:
					</td>
					<td class="honry-view" style="font-size: 14">
						${schedule.scheduleDoctorname }
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						星期:
					</td>
					<td class="honry-view" style="font-size: 14">
						<input type="hidden" id="week" value="${schedule.week }"><p id="weekpId"></p>
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						午别:
					</td>
					<td class="honry-view" style="font-size: 14">
						${schedule.scheduleMiddayname }
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						开始时间:
					</td>
					<td class="honry-view" style="font-size: 14">
						${schedule.startTime }
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						结束时间:
					</td>
					<td class="honry-view" style="font-size: 14">
						${schedule.endTime }
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						日期:
					</td>
					<td class="honry-view" style="font-size: 14">
						<fmt:formatDate value="${schedule.date }" pattern="yyyy年MM月dd日"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						挂号级别:
					</td>
					<td class="honry-view" style="font-size: 14">
						${gradeMap[schedule.reggrade] }
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						挂号限额:
					</td>
					<td class="honry-view" style="font-size: 14">
						${schedule.limit }
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						预约限额:
					</td>
					<td class="honry-view" style="font-size: 14">
						${schedule.preLimit }
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						电话限额:
					</td>
					<td class="honry-view" style="font-size: 14">
						${schedule.phoneLimit }
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						网络限额:
					</td>
					<td class="honry-view" style="font-size: 14">
						${schedule.netLimit }
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						特诊限额:
					</td>
					<td class="honry-view" style="font-size: 14">
						${schedule.speciallimit }
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						是否加号:
					</td>	
					<td class="honry-view">
						<c:choose>
							<c:when test="${schedule.appFlag eq '1'}">
							是
							</c:when>
							<c:when test="${schedule.appFlag eq '0'}">
							否
							</c:when>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						是否停诊:
					</td>	
					<td class="honry-view">
						<c:choose>
							<c:when test="${schedule.isStop eq '1'}">
							是
							</c:when>
							<c:when test="${schedule.isStop eq '2'}">
							否
							</c:when>
						</c:choose>
					</td>
				</tr>
				<c:if test="${schedule.isStop eq '1'}">
				<tr>
					<td class="honry-lable" style="font-size: 14">
						停诊人:
					</td>
					<td class="honry-view" style="font-size: 14">
						${schedule.stopDoctor.name }
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						停诊时间:
					</td>
					<td class="honry-view" style="font-size: 14">
						<fmt:formatDate value="${schedule.stopTime }" pattern="yyyy年MM月dd日"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">停诊原因:</td>
				    <td style="font-size: 14" >
				    <input type="hidden" id="stoprEason" value="${schedule.stoprEason }"><p id="stoprEasonId"></p>
					</td>
				</tr>
				</c:if>
				<tr>
					<td class="honry-lable" style="font-size: 14">备注:</td>
				    <td style="font-size: 14"><p>${schedule.remark }</p></td>
				</tr>
			</table>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			</div>
		</div>
	</div>
	<script>
	var weekMap='';
	
		$(function(){
			$.ajax({
				url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action',  
				data:{'type':'week'},
				type:'post',
				success: function(data) {
					weekMap=data;
					$('#weekpId').text(week($('#week').val()));
				}
			});	
			
		});
	var stopResonMap='';
	
		$(function(){
			$.ajax({
				url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action',   
				data:{'type':'stopReason'},
				type:'post',
				success: function(data) {
					var stopReson = $('#stoprEason').val();
					stopResonMap = data;
					$('#stoprEasonId').text(stopResonFun(stopReson));
				}
			});	
			
		});
		function closeLayout() {
			$('#divLayout').layout('remove', 'east');
		}
		
		function week(value){
			for(var i=0;i<weekMap.length;i++){
    			if(value==weekMap[i].encode){
	        		return weekMap[i].name;
	        	}
    		}
    	}
		
		function stopResonFun(value){
			for(var i=0;i<stopResonMap.length;i++){
    			if(value==stopResonMap[i].encode){
	        		return stopResonMap[i].name;
	        	}
    		}
    	}
		
	</script>
</body>
</html>