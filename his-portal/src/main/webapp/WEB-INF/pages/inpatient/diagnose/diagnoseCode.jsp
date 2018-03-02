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
		<div id="divLayout" class="easyui-layout" fit=true style="width:100%;height:100%;overflow-y: auto;">
			<div style="height:10%;padding:5px 5px 0px 5px;">
					<table style="width:100%;border:1px solid #95b8e7;padding:5px;">
						<tr>
							<td>
								诊断代码：<input class="easyui-textbox" id="queryName" name="code"/>
								<a href="javascript:searchFromUser()"  class="easyui-linkbutton" iconCls="icon-search" >查询</a>
							</td>
						</tr>
					</table>
			</div>
			<div style="height:90%;padding: 0px 5px 5px 5px;">
				<input type="hidden" name="id" value="${id }" id="id" ></input>
				<table id="listUser"  style="width:100%;" data-options="rownumbers:true,idField: 'id',fit:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true">
					<thead>
						<tr>
							<th data-options="field:'code', width : '35%'">诊断代码</th>
							<th data-options="field:'diagName', width : '60%'">诊断名称</th>
						</tr>
					</thead>
				</table>
			</div>		
		</div>
		<script type="text/javascript">
			//加载页面
			$(function(){
				$('#listUser').datagrid({
					url:"<%=basePath %>inpatient/diagnose/queryCode.action",
					queryParams:{id:'${id}',name:''},
					method:'post',
					pagination:true,
					pagePosition:'bottom',
			   		pageSize:20,
			   		pageList:[20,30,50,80,100],
					onDblClickRow: function (rowIndex, rowData) {//双击查看
						$("#icdCode1").val(rowData.code);
						$("#icdCode").textbox("setValue",rowData.diagName);
						$("#diagName").val(rowData.diagName);
						closeDialog('selectUser');
					},onLoadSuccess:function(data){
						var pager = $(this).datagrid('getPager');
						var aArr = $(pager).find('a');
						var iArr = $(pager).find('input');
						$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
						for(var i=0;i<aArr.length;i++){
							$(aArr[i]).tooltip({
								content:toolArr[i],
								hideDelay:1
							});
							$(aArr[i]).tooltip('hide');
						}
					}    
				});
			});
	   		//查询
	   		function searchFromUser(){
	   		    var diagName =	$('#queryName').val();
			    $('#listUser').datagrid('load', {
			    		id:'${id}',
			    		diagName:diagName
				});
			}	
		</script>
	</body>
</html>