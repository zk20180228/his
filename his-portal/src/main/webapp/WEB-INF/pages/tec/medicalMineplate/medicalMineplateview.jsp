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
	<div class="easyui-panel" id="panelEast"
		data-options="title:'医技排班模板查看',iconCls:'icon-form'">
		<div style="padding: 10px">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="honry-lable">
						项目名称:
					</td>
					<td class="honry-view">
						${model.modelItemName }
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						模板类型:
					</td>
					<td class="honry-view" style="font-size: 14">
						<c:choose>
							<c:when test="${model.modelClass eq '1'}">
							项目排班
							</c:when>
							<c:when test="${model.modelClass eq '2'}">
							设备排班
							</c:when>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						科室:
					</td>
					<td class="honry-view">
						${model..modelDeptid }
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="font-size: 14">
						诊室:
					</td>
					<td class="honry-view" style="font-size: 14">
						${model.modelClinicid }
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
						${model.modelStarttime }
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
						预约限额:
					</td>
					<td class="honry-view">
						${model.modelPrelimit }
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
							<c:when test="${model.modelAppflag }">
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
				    <td><p>${model.modelAttentions }</p></td>
				</tr>
			</table>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			</div>
		</div>
	</div>
	<script>
	$(function(){
		var midDayMap=new Map();
		$.ajax({
		    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=midday",
			type:'post',
			success: function(data) {
				var daytype = data;
				for(var i=0;i<daytype.length;i++){
					midDayMap.put(daytype[i].encode,daytype[i].name);
				}
			}
		});
		var weekMap=new Map();
		$.ajax({
		    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=midday",
			type:'post',
			success: function(data) {
				var weektype = data;
				for(var i=0;i<weektype.length;i++){
					weekMap.put(weektype[i].encode,weektype[i].name);
				}
			}
		});
		$('#modelWeekpId').text(weekMap.get($('#modelWeek').val()));
		$('#modelMiddaypId').text(midDayMap.get($('#modelMidday').val()));
	});
		function closeLayout() {
			$('#divLayout').layout('remove', 'east');
		}
	</script>
</body>
</html>