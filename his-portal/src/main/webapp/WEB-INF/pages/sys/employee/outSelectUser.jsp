<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script>
	var sexMap=new Map();
	//性别渲染
	$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});

</script>
</head>
	<body>
		<div id="divLayout" class="easyui-layout" fit=true style="width:100%;height:100%;overflow-y: auto;">
			<div style="padding:5px 5px 0px 5px;">
					<table style="width:100%;border:1px solid #95b8e7;padding:5px;">
						<tr>
							<td style="width: 200px;">姓名：<input type="text" id="userName" name="name" onkeydown="KeyDown()"/></td>
							<td style="width: 200px;">账号：<input type="text" id="accountSearch" name="account" onkeydown="KeyDown()"/></td>
							<td>
								<a href="javascript:void(0)"  class="easyui-linkbutton" iconCls="icon-search" onclick="searchFromUser()">查询</a>
							</td>
						</tr>
					</table>
			</div>
			<div style="padding: 0px 5px 5px 5px;">
				<input type="hidden" value="${id }" id="id" ></input>
				<table id="listUser" style="width:100%;" data-options="url:'${pageContext.request.contextPath}/baseinfo/employee/queryEmployeeUser.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true">
					<thead>
						<tr>
							<th data-options="field:'id',checkbox:true, width : '10%'"></th>
							<th data-options="field:'name', width : '15%'">姓名</th>
							<th data-options="field:'nickName', width : '20%'">昵称</th>
							<th data-options="field:'account', width : '20%'">账号</th>
							<th data-options="field:'birthday', width : '15%'">出生日期</th> 
							<th data-options="field:'sex',align:'center',formatter:formatCheckBox, width : '5%'" >&nbsp;性别</th>
							<th data-options="field:'phone', width : '15%'">电话</th>
							</tr>
					</thead>
				</table>
			</div>		
		</div>
		<script type="text/javascript">
			//加载页面
			$(function(){
				$('#listUser').datagrid({
					pagination:true,
			   		pageSize:20,
			   		pageList:[20,30,50,80,100],
					onDblClickRow: function (rowIndex, rowData) {//双击查看
						$('#account').val(rowData.account);
						$('#userId').val(rowData.id);
						closeDialog('selectUser');
					}    
				});
			});
	   		//查询
	   		function searchFromUser(){
	   		    var account =$('#accountSearch').val();
	   		    var name =	$('#userName').val();
			    $('#listUser').datagrid('load', {
					name: name,
					account: account 
				});
			}	
			/**
			 * 格式化复选框
			 * @author  
			 * @date 2015-5-26 9:25       
			 * @version 1.0
			 */
			function formatCheckBox(val,row){
				return sexMap.get(value);
			}		
					
			/**
			 * 回车键查询
			 * @author 
			 * @param title 标签名称
			 * @param title 跳转路径
			 * @date 2015-05-27
			 * @version 1.0
			 */
			function KeyDown(){  
			    if (event.keyCode == 13){  
			        event.returnValue=false;  
			        event.cancel = true;  
			        searchFromUser();  
			    }  
			} 
			
		</script>
	</body>
</html>