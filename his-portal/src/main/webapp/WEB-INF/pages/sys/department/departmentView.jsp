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
		<div class="easyui-panel" id="panelEast" data-options="title:'部门科室查看',iconCls:'icon-form',border:false">
			<div style="padding:10px">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="margin-left:auto;margin-right:auto;">
					<tr>
					    <td class="honry-lable">系统编号:</td>
					    <td class="honry-view"><p>${sysDepartment.deptCode }</p></td>
					</tr>
					<tr>
						<td class="honry-lable">部门名称:</td>
					    <td class="honry-view"><p>${sysDepartment.deptName }</p></td>
					</tr>
					<tr>
					    <td class="honry-lable">部门简称:</td>
					    <td class="honry-view"><p>${sysDepartment.deptBrev }</p></td>
				    </tr>
				    <tr>
				    	<td class="honry-lable">部门英文:</td>
					    <td class="honry-view"><p>${sysDepartment.deptEname }</p></td>
				    </tr>
					<tr>
					    <td class="honry-lable">部门地点:</td>
					    <td class="honry-view"><p>${sysDepartment.deptAddress }</p></td>
				    </tr>
				    <tr>
				    	<td class="honry-lable">拼音码:</td>
					    <td class="honry-view"><p>${sysDepartment.deptPinyin }</p></td>
				    </tr>
					<tr>
					    <td class="honry-lable">五笔码:</td>
					    <td class="honry-view"><p>${sysDepartment.deptWb }</p></td>
					</tr>	
					<tr>
						<td class="honry-lable">自定义码:</td>
					    <td class="honry-view"><p>${sysDepartment.deptInputcode }</p></td>
					</tr>
					<tr>
					    <td class="honry-lable">部门分类:</td>
					    <td class="honry-view">
							<input id="deptName" style="border:none" readonly="readonly" ></input>
							<input id="deptId"  value="${sysDepartment.deptType }"  type="hidden">
						</td>
					</tr>	
					<tr>
						<td class="honry-lable">部门性质:</td>
					    <td><p>${deptProperty }</p></td>
					</tr>
					<tr>
					    <td class="honry-lable">所属院区:</td>
					    <td class="honry-view">
							<input id="areaName" style="border:none" readonly="readonly" ></input>
							<input id="areaCode"  value="${sysDepartment.areaCode }"  type="hidden">
						</td>
					</tr>	
					<tr>
					    <td class="honry-lable">是否挂号部门:</td>
					    <td>
					        <p>
					            <c:if test="${sysDepartment.deptIsforregister !=1}">
					    			否
					            </c:if>
					            <c:if test="${sysDepartment.deptIsforregister ==1}">
					    			是
					            </c:if>
					        </p>
					    </td>
					</tr>	
					<tr>
						<td class="honry-lable">是否核算部门:</td>
					    <td>
					        <p>
					            <c:if test="${sysDepartment.deptIsforaccounting !=1}">
					    			否
					            </c:if>
					            <c:if test="${sysDepartment.deptIsforaccounting ==1}">
					    			是
					            </c:if>
					        </p>
					    </td>
					</tr>
					<tr>
					    <td class="honry-lable">排序:</td>
					    <td><p>${sysDepartment.deptOrder }</p></td>
					</tr>	
					<tr>
						<td class="honry-lable">备注:</td>
					    <td><p>${sysDepartment.deptRemark }</p></td>
					</tr>
					<tr>
					    <td class="honry-lable">停用标志:</td>
					   	<td><p>
					    <c:if test="${sysDepartment.stop_flg ==0}">
					    	否
					    </c:if>
					    <c:if test="${sysDepartment.stop_flg ==1}">
					    	是
					    </c:if>
					    </p></td>
					</tr>	
					<tr>
						<td class="honry-lable">创建时间:</td>
						<td class="honry-view"><fmt:formatDate value="${sysDepartment.createTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
					</tr>
				</table>
			</div>
			<div style="text-align:center;padding:5px">
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
		    </div>
		</div>
		<script>
		var deptTypeMap=new Map();
		var areaNameMap=new Map();
		$(function(){
			$.ajax({
				url:  "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action",
				data:{type:"depttype"},
				async:false,
				success: function(data) {
					var type = data;
				 	for(var i=0;i<type.length;i++){
				 		deptTypeMap.put(type[i].encode,type[i].name);
					}
				}
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
			if($('#deptId').val()!=null){
				var value =$('#deptId').val();
				$('#deptName').val(deptTypeMap.get(value));
			}
			if($('#areaCode').val()!=null){
				var value =$('#areaCode').val();
				$('#areaName').val(areaNameMap.get(value));
			}
		});	
		//关闭
		function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
		</script>	    
	</body>
</html>