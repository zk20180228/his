<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
		<div class="easyui-panel" id = "panelEast" data-options="title:'',iconCls:'icon-form'" style="width:490px;height:100%;border-top:0">
			<div class="easyui-panel" data-options="region:'center',split:false,border:false" >
				<div id="divLayout" class="easyui-layout" fit=true style="width: 100%;border-top:0">
					<div style="padding:5px;">
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:false"" onclick="closeLayout()">关闭</a>
				    </div>
				    <input type="hidden" id="id" name="id" value="${medicalgroup.id }">
					<table class="honry-table" cellpadding="1" cellspacing="1" style="width: 100%;border-left:0">
						<tr>
							<td class="honry-lable" style="border-left:0">
								科室:
							</td>
							<td class="honry-view">
								${deptName}&nbsp;
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="border-left:0">
								名称:
							</td>
							<td class="honry-view">
								${medicalgroup.name}&nbsp;
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="border-left:0">
								拼音码:
							</td>
							<td class="honry-view">
								${medicalgroup.pinyin }&nbsp;
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="border-left:0">
								五笔码:
							</td>
							<td class="honry-view">
								${medicalgroup.wb }&nbsp;
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="border-left:0">
								自定义码:
							</td>
							<td class="honry-view">
								${medicalgroup.inputCode }&nbsp;
							</td>
						</tr>
						<tr>
					    <td class="honry-lable" style="border-left:0">所属院区:</td>
					    <td class="honry-view">
							<input id="areaName" style="border:none" readonly="readonly" ></input>
							<input id="areaCode"  value="${medicalgroup.areaCode }"  type="hidden">
						</td>
					</tr>	
						<tr>
							<td class="honry-lable" style="border-left:0">
								备注:
							</td>
							<td class="honry-view">
								${medicalgroup.remark }&nbsp;
							</td>
						</tr>
						<tr>
							<td class="honry-lable">创建时间:</td>
							<td class="honry-view"><fmt:formatDate value="${medicalgroup.createTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
						</tr>
					</table>
				 </div>
			</div>
			<div class="easyui-panel" data-options="region:'south',split:false,border:false" style="width: 100%;border-top:0;">
				<input type="hidden" id="inpId">
				<table id="listInfo" title="组套医生" data-options="method:'post',rownumbers:false,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
					<thead>
						<tr>
							<th data-options="field:'doctorId',hidden:true" >
								医生编号
							</th>
							<th data-options="field:'doctor',width :'15%'">
								医生姓名
							</th>
							<th data-options="field:'remark',width :'25%'">
								备注
							</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<script>
			var areaNameMap=new Map();
			var groupId=$('#id').val();
			$(function(){
				$('#listInfo').datagrid({
					url:"<c:url value='/baseinfo/medicalGroup/findGroupInfoList.action'/>?groupId="+groupId
				});
				$.ajax({
					url:  "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action",
					data:{type:"hospitalArea"},
					async:false,
					success: function(data) {
						var type = data;
					 	for(var i=0;i<type.length;i++){
					 		areaNameMap.put(type[i].encode,type[i].name);
						}
					}
				});
				if($('#areaCode').val()!=null){
					var value =$('#areaCode').val();
					$('#areaName').val(areaNameMap.get(value));
				}
			});
			function closeLayout(){
				$('#divLayout').layout('remove','east');
			}
		</script>
	</body>
</html>