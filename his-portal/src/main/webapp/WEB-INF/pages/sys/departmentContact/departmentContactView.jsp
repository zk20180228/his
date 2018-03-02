<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
	<div class="easyui-panel departmentContactEdit" id="panelEast" data-options="title:'部门科室间关系查看',iconCls:'icon-form',fit:true">
		<div style="padding: 10px">
			<input type="hidden" id="id" name="id" value="${deptcontact.id }">
			<input type="hidden" id="treeId" name="treeId" value="${treeId }">
		    <input type="hidden" id="sortId" name="sortId" value="${deptcontact.sortId }">
		    <input type="hidden" id="ordertopath" name="ordertopath" value="${deptcontact.ordertopath }">
			<input type="hidden" id="path" name="path" value="${deptcontact.path  }">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="border-left:0">
				<tr>
					<td class="honry-lable">
						部门代码:
					</td>
					<td class="honry-view" >
						${deptcontact.deptCode }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						部门分类:
					</td>
					<td class="honry-view">
						<input type="hidden" id="deptCalss" value="${deptcontact.deptCalss}"/><p id="deptCalssId"></p>
						
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						部门名称:
					</td>
					<td class="honry-view">
						${deptcontact.deptName}
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						科室类型:
					</td>
					<td class="honry-view">
						<input type="hidden" id="deptType" value="${deptcontact.deptType }"/><p id="deptTypeId"></p>
						
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						拼音码:
					</td>
					<td class="honry-view">
						${deptcontact.spellCode }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						五笔码:
					</td>
					<td class="honry-view">
						${deptcontact.wbCode }
					</td>
				</tr><tr>
					<td class="honry-lable">
						自定义码:
					</td>
					<td class="honry-view">
						${deptcontact.userDefinedCode }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						英文名:
					</td>
					<td class="honry-view">
						${deptcontact.deptEnCode }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						节点层级:
					</td>
					<td class="honry-view">
						${deptcontact.gradeCode }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						顺序号:
					</td>
					<td class="honry-view">
						${deptcontact.sortId }
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						是否停用:
					</td>
					<input id="strstat" type="hidden" value="${deptcontact.validState}" >
					<td id="stat">
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						备注:
					</td>
					<td class="honry-view">
						${deptcontact.mark}
					</td>
				</tr>
				<tr>
					<td class="honry-lable">创建时间:</td>
					<td class="honry-view"><fmt:formatDate value="${deptcontact.createTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
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
			var value=$('#strstat').val();
			var htmlV;
			if(value==0){
				htmlV= '停用';
			}else if(value==1){
				htmlV= '在用';
			}else if(value==2){
				htmlV= '废弃';
			}else{
				htmlV= '在用';
			}
			$('#stat').html(htmlV);
			$.ajax({
				url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action",
				data : {"type":"depttype"},
				success: function(data) {
					for(var i=0;i<data.length;i++){
				 		var type=$('#deptType').val();
				 		if(type==data[i].encode){
				 			$('#deptTypeId').text(data[i].name);
				 		}
					}
				}
			});
			$.ajax({
				url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action",
				data : {"type":"deptclass"},
				success: function(data) {
				 	for(var i=0;i<data.length;i++){
				 		var deptclass=$('#deptCalss').val();
				 		if(deptclass==data[i].encode){
				 			$('#deptCalssId').text(data[i].name);
				 		}
					}
				}
			});
			
		});
		function closeLayout() {
			$('#divLayout').layout('remove', 'east');
		}
		
		
	</script>
</body>
</html>