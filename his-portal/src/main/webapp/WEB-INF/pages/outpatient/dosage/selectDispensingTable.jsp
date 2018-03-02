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
		<div class="easyui-layout" class="easyui-layout" fit=true style="width: 100%; height: 100%;">
			<div data-options="region:'center',border:false">
				<div style="height: 40px;width: 100%;border-bottom: 1px solid #95b8e7;" class="changeskinBottom">
					<form id="search" method="post">
						<table style="width: 100%; border:none; padding: 5px;">
							<tr>
								<td style="width:300px;">查询条件：<input type="text" id="queryName" name="queryName" data-options="prompt:'编码,名称'"  style="width: 220px;"/>
									&nbsp;
									<a href="javascript:void(0)" onclick="searchFrom()"class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</form>
				</div>
				<div style="width: 100%;height:203px; border:none;" >
					<input type="hidden" id="drugDeptCode" name="drugDeptCode" value="${drugDeptCode }">
					<table id="tableList" 
						data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
						<thead>
							<tr>
								<th data-options="field:'id', width : '20%',hidden:true">id</th>
								<th data-options="field:'code', width : '20%'">配药台编码</th>
								<th data-options="field:'name', width : '20%'" >配药台名称</th>
								<th data-options="field:'closeFlag',width :'15%',formatter: function(value,row,index){
																								if (value==1){
																									return '关闭';
																								} else {
																									return '开放';
																								}
																							}" >配药台开放状态</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			$(function(){
			    var winH=$("body").height();
			    $('#tableList').datagrid({
					url:"<%=basePath %>outpatient/dosage/dispenTableList.action?drugDeptCode="+$('#drugDeptCode').val(),
					onDblClickRow: function (rowIndex, rowData) {
						if(rowData.closeFlag==1){
							$.messager.alert('提示',"此配药台已关闭，请与相关人员联系，开启该配药台！");
							return;
						}else if(rowData.mark == "1"){
							$.messager.confirm('确认', '该终端已在其他电脑登录，不能再次使用，您确认登录此配药台吗？<br>注意：如果强行登录，容易造成配药清单打印混乱', function(res){
								if(res){
									add(rowIndex, rowData);
								}else{
									return;
								}
							});
						}else{
							add(rowIndex, rowData);
						}
					}		
				});
				$('#queryName').textbox({});
				bindEnterEvent('queryName', searchFrom, 'easyui');
			});
			//根据所选的发药药房，配药台查出处方调剂头表的申请状态的记录，以及患者信息
			
<%-- 双击选择配药台begin*****************************************************************************************--%>	
			function add(rowIndex, rowData){
				//判断配药台是否是占用状态
				$.post("<%=basePath %>outpatient/dosage/getStoTerminalState.action?stoType=1&id="+rowData.id+"&drugDeptCode="+$('#drugDeptCode').val(),function(data){
					if(data.resCode == "error"){//后台IP验证不通过
						$.messager.alert('友情提示',data.resMsg);
					}else{//后台IP验证通过
						//更新配药药台mark字段为1，即是使用状态
						$.post("<%=basePath %>outpatient/dosage/updateStoTerminal.action?id="+rowData.id+"&drugDeptCode="+$('#drugDeptCode').val()+"&flag=0",function(data){
							$('#now').text('当前药房：'+$('#tDt').tree('getSelected').text+'，当前登录窗口：'+rowData.name);
							$('#drugedTerminal').val(rowData.code);
							$('#drugedTerminalId').val(rowData.id);
							$('#exitStoTerminal').linkbutton('enable');
							$('#autorefash').linkbutton('enable');
							$('#autorefashPause').linkbutton('enable');
							$('#handReash').linkbutton('enable');
							$('#buttonSubmit').linkbutton('enable');
							$('#ordonnance').linkbutton('enable');
							$('#recipeNo').textbox({    
							    disabled:false  
							 }); 
							bindEnterEvent("recipeNo", reashTree, 'easyui');
							$('#patientDiv').tabs('enableTab',1);
							$('#patientDiv').tabs('enableTab',0);
							reashTree();
							$('#layout1').layout('collapse','west');
							closeDialog('dispenTableDiv');
						});
					}
				});
			}
<%-- 双击选择配药台end*****************************************************************************************--%>	


			function searchReload(){
				$('#queryName').textbox('setValue','');
				searchFrom();
			}
<%--配药台搜索 begin *****************************************************************************************--%>
			function searchFrom(){
				var queryName = $('#queryName').textbox('getValue');
				$('#tableList').datagrid('load', {
					name : queryName
				});
			}
			
		</script>
	</body>
</html>