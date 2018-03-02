<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
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
<title>登录日志</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
			var userMap = "";//用户数据源
			$(function(){
				var winH=$("body").height();
				$('#pageShow').height(winH-45);
				$.ajax({
					url: "<c:url value='/sys/queryUserAllMap.action'/>",
					type:'post',
					success: function(userData) {
						userMap = eval(userData);
						$('#list').datagrid({
							pagination:true,
							fitColumns:false,
							url:'<%=basePath%>except/HIASMongo/queryLoginLog.action',
					   		pageSize:20,
					   		pageList:[20,30,50,100],
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
					        },onDblClickRow: function (rowIndex, rowData) {//双击查看
									$('#loginId').val(rowData.id);
									$('#loginUser').val(userMap[rowData.LOGIN_USERID]);
									$('#loginIp').val(rowData.LOGIN_IP);
									$('#loginHttp').val(rowData.LOGIN_HTTP);
									$('#loginSession').val(rowData.LOGIN_SESSION);
									$('#loginTimeShow').val(rowData.LOGIN_TIME);
									AddOrShowEast();
								}
							});
					}
				});	
					bindEnterEvent('userId',searchFrom,'easyui');
				});
	   		
			var isFirst = true;
	   		//查询
	   		function searchFrom(){
	   			closeLayoutLogin();
	   			if(isFirst){
	   				isFirst = false;
	   				$('#list').datagrid('clearSelections');
				    $('#list').datagrid('load', {
						userId: $('#userId').val(),
						loginTime: $('#loginTime').val(),
						userMessageMap: JSON.stringify(userMap),
					});
	   			}else{
	   				$('#list').datagrid('clearSelections');
				    $('#list').datagrid('load', {
						userId: $('#userId').val(),
						loginTime: $('#loginTime').val(),
					});
	   			}
			}	
			 function closeLayoutLogin(){
				 $("#showDetail").hide();
				 var divLayoutWidth = $("#divLayout").width();
					if(!count){
					$("#divLayout").width(divLayoutWidth+580);
					count=true;
					}
				}
			 
			 /**
				 * 重置
				 * @author huzhenguo
				 * @date 2017-03-17
				 * @version 1.0
				 */
				function clears(){
					$('#loginTime').val("");
					$('#userId').textbox('setValue','');
					searchFrom();
				}
			
			/**
			 * 动态添加标签页
			 * @author  sunshuo
			 * @param title 标签名称
			 * @param title 跳转路径
			 * @date 2015-05-21
			 * @version 1.0
			 */
			 var count = true;
			function AddOrShowEast() {
				$("#showDetail").show();
				var divLayoutWidth = $("#divLayout").width();
				if(count){
				$("#divLayout").width(divLayoutWidth-580);
				count=false;
				}
			}
			
			//适用用户名字
			function functionUser(value,row,index){
				return userMap[value];
			}	
			$(function(){
				$("#pageShow").height($("#divLayout").height()-35);
			})
		</script>

</head>
	<body>
	<div style="width:100%">
		<div id="divLayout" style="height:100%;width:100%;float:left;">
	        <div data-options="region:'north',split:false,border:false" style="height: 30px;margin-left: 5px;margin-top: 5px;">	        
					<form id="search" method="post">
						<table style="width:100%;border:false;,collapsible:false">
							<tr>
								<td  >登录用户：
								<input class="easyui-textBox" ID="userId" name="userLogin.userId"/>
								登录时间：
								<input id="loginTime"  name="loginTime" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:120px;border: 1px solid #95b8e7;border-radius: 5px;"/>&nbsp;
								<shiro:hasPermission name="${menuAlias}:function:query ">
									<a href="javascript:void(0)" onclick="searchFrom()"class="easyui-linkbutton" iconCls="icon-search">查询</a>
								</shiro:hasPermission>
								<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</form>
			</div>
			<div  id='pageShow' style="width:100%;">
				<table id="list" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#usertoolbarId',fit:true">
					<thead>
						<tr>
							<th data-options="field:'LOGIN_USERID',formatter: functionUser ,width:170">登录用户</th>
							<th data-options="field:'LOGIN_IP',width:200">登录ip</th>
							<th data-options="field:'LOGIN_HTTP',width:210">登录客户端类型</th> 
							<th data-options="field:'LOGIN_SESSION',width:380">登录会话</th>
							<th data-options="field:'LOGIN_TIME',width:250">登录时间</th>
							</tr>
					</thead>
				</table>				
			</div>	
		</div>
		<div id="showDetail" style="width:580px;float:right;display: none;" >
		<div class="loginLogNew" style="padding:10px;height:100%;margin-top: 35px;border-top:1px solid #95B8E7;border-left:1px solid #95B8E7">
			<table class="honry-table" cellpadding="1" cellspacing="1">
				<tr>
					<td class="honry-lable" style="width:200px; height:22px; font-weight:bold; text-align: left" colspan="2">
						登录日志查看
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="wih:200px; height:22px">
						登录编号:
					</td>
					<td class="honry-view"  style="padding:1px">
						<textarea id='loginId' readonly="readonly" style="width:100%;"></textarea>
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:200px; height:22px">
						登录用户:
					</td>
					<td class="honry-view"  style="padding:1px; ">
						<textarea id='loginUser' readonly="readonly" style="width:100%;"></textarea>
					</td>
				</tr>
			    <tr>
					<td class="honry-lable" style="width:200px; height:22px">
						登录ip:
					</td>
					<td class="honry-view"  style="padding:1px">
						<textarea id='loginIp' readonly="readonly" style="width:100%;"></textarea>
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:200px; height:22px">
						登录客户端类型:
					</td>
					<td class="honry-view"  style="padding:1px">
						<textarea id='loginHttp' readonly="readonly" style="width:100%;"></textarea>
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:200px; height:22px">
						登录会话:
					</td>
					<td class="honry-view"  style="padding:1px">
						<textarea id='loginSession' readonly="readonly" style="width:100%;"></textarea>
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:200px; height:22px">
						登录时间:
					</td>
					<td class="honry-view"  style="padding:1px">
						<textarea id='loginTimeShow' readonly="readonly" style="width:400px;"></textarea>
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
	</body>
</html>