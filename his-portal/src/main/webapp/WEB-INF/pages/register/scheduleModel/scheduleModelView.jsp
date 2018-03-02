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
					<td class="honry-lable">
						所属科室:
					</td>
					<td class="honry-view">
						${deptMap[model.department] }
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						排班分类:
					</td>
					<td class="honry-view" style="font-size: 14">
						<c:choose>
							<c:when test="${model.modelClass eq '1'}">
							挂号排班
							</c:when>
							<c:when test="${model.modelClass eq '2'}">
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
						${deptMap[model.modelWorkdept] }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						模板类型:
					</td>
					<td class="honry-view">
						<c:choose>
							<c:when test="${model.modeType eq '1'}">
							科室模板
							</c:when>
							<c:when test="${model.modeType eq '2'}">
							医生模板
							</c:when>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						工作诊室:
					</td>
					<td class="honry-view" style="font-size: 14">
						${cliMap[model.clinic] }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						员工:
					</td>
					<td class="honry-view">
						${empMap[model.modelDoctor] }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						星期:
					</td>
					<td class="honry-view">
						<input type="hidden" id="modelWeek" value="${model.modelWeek }"><p id="modelWeekpId"></p>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						午别:
					</td>
					<td class="honry-view">
						<input type="hidden" id="modelMidday" value="${model.modelMidday }"><p id="modelMiddaypId"></p>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						开始时间:
					</td>
					<td class="honry-view">
						${model.modelStartTime }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						结束时间:
					</td>
					<td class="honry-view">
						${model.modelEndTime }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						挂号级别:
					</td>
					<td class="honry-view">
						${gradeMap[model.modelReggrade] }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						挂号限额:
					</td>
					<td class="honry-view">
						${model.modelLimit }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						预约限额:
					</td>
					<td class="honry-view">
						${model.modelPrelimit }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						电话限额:
					</td>
					<td class="honry-view">
						${model.modelPhonelimit }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						网络限额:
					</td>
					<td class="honry-view">
						${model.modelNetlimit }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						特诊限额:
					</td>
					<td class="honry-view">
						${model.modelSpeciallimit }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						是否加号:
					</td>	
					<td class="honry-view">
						<c:choose>
							<c:when test="${model.modelAppflag eq '1'}">
							是
							</c:when>
							<c:otherwise>
							否
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">备注:</td>
				    <td><p>${model.modelRemark }</p></td>
				</tr>
			</table>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			</div>
		</div>
	</div>
	<script>
	var weekMap=new Map();
	var midDayMap=new Map();
		$(function(){
			$.ajax({
			    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=midday",
				type:'post',
				success: function(data) {
					midDayMap = data;
					var mid= $('#modelMidday').val();
					$('#modelMiddaypId').text(getMidday(mid));
				}
			});
			$.ajax({
				url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action',  
				data:{'type':'week'},
				type:'post',
				success: function(data) {
					weekMap=data;
					var wk=$('#modelWeek').val();
					$('#modelWeekpId').text(week(wk));
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
		function getMidday(value){
			for(var i=0;i<midDayMap.length;i++){
    			if(value==midDayMap[i].encode){
	        		return midDayMap[i].name;
	        	}
    		}
			
		}
	</script>
</body>
</html>