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
	<div class="easyui-panel" id="panelEast" data-options="title:'编辑',iconCls:'icon-form',border:false,fit:true">
		<div style="padding: 10px">
			<form id="editForm" method="post">
			<input type="hidden" id="id" name="sysDbAdmin.id" value="${sysDbAdmin.id }">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="honry-lable">
							数据库名:
						</td>
						<td class="honry-view">
							${sysDbAdmin.database }
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							表空间名:
						</td>
						<td class="honry-view">
							${sysDbAdmin.tablespaceName }
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							表名:
						</td>
						<td class="honry-view">
							${sysDbAdmin.tableName }
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							分区名:
						</td>
						<td class="honry-view">
							${sysDbAdmin.partitionName }
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							分区字段:
						</td>
						<td class="honry-view">
							${sysDbAdmin.columnName }
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							分区类型:
						</td>
						<td class="honry-view">
							<input id="zoneTypeId" name="sysDbAdmin.zoneType" value="${sysDbAdmin.zoneType }"/> 
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							分区值:
						</td>
						<td class="honry-view">
							${sysDbAdmin.highValue }
						</td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<a href="javascript:void(0)" class="easyui-linkbutton"data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"data-options="iconCls:'icon-save'" onclick="save()">保存</a>
				</div>
			</form>	
		</div>
	</div>
	<script>
		$('#zoneTypeId').combobox({
			data:getZoneTypeArray() ,
			valueField:'encode',
			textField:'name'
		});
		function save() {
			$('#editForm').form('submit',{  
	        	url:"<%=basePath%>baseinfo/sysDbAdmin/save.action",  
	        	onSubmit:function(){
					if (!$('#editForm').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
	        	},  
		        success:function(){  
		        	$.messager.alert('提示','保存成功');
		        	$('#list').datagrid('reload');
		        	closeLayout();
		        },
				error : function(data) {
					$.messager.alert('提示','请求失败！');	
				}							         
	   		}); 
		}
		function closeLayout() {
			$('#divLayout').layout('remove', 'east');
		}
	</script>
</body>
</html>