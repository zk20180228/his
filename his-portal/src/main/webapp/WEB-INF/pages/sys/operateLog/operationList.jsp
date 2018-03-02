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
<style type="text/css">
textarea {
resize: none;
disabled: disabled;
border:0;
height:19px; 
font-size:14px;
}
</style>
</head>
	<body>
	<div style="width:100%;height:100%">
		<div id="divLayout" style="height:100%;width:100%;float:left;">
	        <div data-options="border:false" style="height:35px;">	        
					<table style="width:100%;border:false;padding:1px;">
						<tr style="width:600px;">
							<td>
								<input id="sName" class="easyui-textbox" data-options="prompt:'操作用户'"/>
								<shiro:hasPermission name="${menuAlias}:function:query ">
									<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</table>
			</div>
	           <div  id='pageShow' style="width:100%;">
				<table id="list" data-options="url:'<%=basePath%>except/HIASMongo/queryOperateLog.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
					<thead>
						<tr>
							<th data-options="field:'LOG_USERID',formatter: functionUser,width:100">操作用户</th>
							<th data-options="field:'LOG_ACTION',width:80">操作行为</th>
							<th data-options="field:'LOG_DEPTID',width:100">操作部门</th>
							<th data-options="field:'LOG_MENUID',width:150">操作栏目</th>
							<th data-options="field:'LOG_SQL',width:100">操作sql</th>
							<th data-options="field:'LOG_TABLE',width:250">操作表</th>
							<th data-options="field:'LOG_TARGETID',width:300">目标编号</th>
							<th data-options="field:'LOG_TIME',width:150">操作时间</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<div id="showDetail" style="width:580px;float:right;margin-top: 35px;display: none;" >
			<div class="loginLogNew" style="height:100%;padding:10px;border-top:1px solid #95B8E7;border-left:1px solid #95B8E7">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
					<tr>
						<td class="honry-lable" style="width:200px; height:22px; font-weight:bold; text-align: left" colspan="2">
							操作日志查看
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="width:200px; height:22px">
							操作用户:
						</td>
						<td class="honry-view"  style="padding:1px">
							<textarea id='oUserShow' readonly="readonly" style="width:100%; "></textarea>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="width:200px; height:22px">
							操作行为:
						</td>
						<td class="honry-view"  style="padding:1px">
							<textarea id='oActionShow' readonly="readonly" style="width:100%;"></textarea>
						</td>
					</tr>
				    <tr>
						<td class="honry-lable" style="width:200px; height:22px">
							操作部门:
						</td>
						<td class="honry-view"  style="padding:1px">
							<textarea id='oDeptShow' readonly="readonly" style="width:100%;"></textarea>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="width:200px; height:22px">
							操作栏目:
						</td>
						<td class="honry-view"  style="padding:1px">
							<textarea id='oMenuShow' readonly="readonly" style="width:100%;"></textarea>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="width:200px; height:22px">
							操作sql:
						</td>
						<td class="honry-view"  style="padding:1px">
							<textarea id='oSqlShow' readonly="readonly" style="width:100%;"></textarea>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="width:200px; height:22px">
							操作表:
						</td>
						<td class="honry-view"  style="padding:1px">
							<textarea id='oTableShow' readonly="readonly" style="width:400px;"></textarea>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="width:200px; height:22px">
							目标编号:
						</td>
						<td class="honry-view"  style="padding:1px">
							<textarea id='oTargetShow' readonly="readonly" style="width:400px;"></textarea>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="width:200px; height:22px">
							操作时间:
						</td>
						<td class="honry-view"  style="padding:1px">
							<textarea id='oTimeShow' readonly="readonly" style="width:400px;"></textarea>
						</td>
					</tr>
					<tr>
						<td class="honry-view"  style="padding:1px;text-align: center" colspan="2">
					    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayoutLogin()">关闭</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
		
		<script type="text/javascript">
		var userMap = "";//用户数据源
			//加载页面
			$(function(){
				$.ajax({
					url: "<c:url value='/sys/queryUserAllMap.action'/>",
					type:'post',
					success: function(userData) {
						userMap = userData;
						$('#list').datagrid({
							pagination:true,
							pageSize:20,
							pageList:[20,30,50,80,100],
							onLoadSuccess: function(data){
								//分页工具栏作用提示
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
							},
							onBeforeLoad: function (param) {//加载数据
					        }
							,onDblClickRow: function (rowIndex, rowData) {//双击查看
								$('#oUserShow').val(userMap[rowData.LOG_USERID]);
								$('#oActionShow').val(rowData.LOG_ACTION);
								$('#oDeptShow').val(rowData.LOG_DEPTID);
								$('#oMenuShow').val(rowData.LOG_MENUID);
								$('#oSqlShow').val(rowData.LOG_SQL);
								$('#oTableShow').val(rowData.LOG_TABLE);
								$('#oTargetShow').val(rowData.LOG_TARGETID);
								$('#oTimeShow').val(rowData.LOG_TIME);
								AddOrShowEast();
							}    
						});
					}
				});	
			var winH=$("body").height();
				$('#pageShow').height(winH-45);
				bindEnterEvent('sName',searchFrom,'easyui');//回车键查询	
			});
				
				function reload(){
					//实现刷新栏目中的数据
					 $('#list').datagrid('reload');
				}
				var isFirst = true;
				//查询
				function searchFrom() {
					closeLayoutLogin();
					var queryName = $('#sName').textbox('getText');
					if(isFirst){
						isFirst = false;	
						$('#list').datagrid('clearSelections');
						$('#list').datagrid('load', {
							queryName : queryName,
							userMessageMap: userMap
						});
					}else{
						$('#list').datagrid('clearSelections');
						$('#list').datagrid('load', {
							queryName : queryName
						});
					}
				}
				/**
				 * 重置
				 * @author huzhenguo
				 * @date 2017-03-17
				 * @version 1.0
				 */
				function clears(){
					$('#sName').textbox('setValue','');
					searchFrom();
				}
				var count = true;
				//动态添加LayOut
				function AddOrShowEast() {
					$("#showDetail").show();
					var divLayoutWidth = $("#divLayout").width();
					if(count){
					$("#divLayout").width(divLayoutWidth-580);
					count=false;
					}
				}
				//关闭layout
				 function closeLayoutLogin(){
					 $("#showDetail").hide();
					 var divLayoutWidth = $("#divLayout").width();
						if(!count){
						$("#divLayout").width(divLayoutWidth+580);
						count=true;
						}
					}
				//操作用户
				function functionUser(value,row,index){
					return userMap[value];
				}	
				$(function(){
					$("#pageShow").height($("#divLayout").height()-35);
				})
		</script>
	</body>
</html>