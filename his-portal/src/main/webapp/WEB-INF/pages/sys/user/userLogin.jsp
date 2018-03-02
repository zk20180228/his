<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>登录日志</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
			var userMap = "";//用户数据源
			$(function(){
				//添加datagrid事件及分页
				$.ajax({
					url: "<c:url value='/sys/queryUserAllMap.action'/>",
					type:'post',
					success: function(userData) {
						userMap = eval(userData);
					}
				});	
				setTimeout(function(){
					$('#list').datagrid({
						pagination:true,
						fitColumns:true,
						url:"<c:url value='/sys/queryUserLogin.action'/>",
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
								if(getIdUtil("#list").length!=0){
							   	    AddOrShowEast("查看","<c:url value='/sys/viewUserLogin.action'/>?uid="+getIdUtil("#list"));
							   	}
							}
						});
				},500)
					bindEnterEvent('userId',searchFrom,'easyui');
				});
	   		
	   		//查询
	   		function searchFrom(){
			    $('#list').datagrid('load', {
					userId: $('#userId').val(),
					loginTime: $('#loginTime').datebox('getValue') 
				});
			}	
			 function closeLayoutLogin(){
					$('#divLayout').layout('remove','east');
				}
			 
			 /**
				 * 重置
				 * @author huzhenguo
				 * @date 2017-03-17
				 * @version 1.0
				 */
				function clears(){
					$('#loginTime').datebox('setValue','');
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
			function AddOrShowEast(title, url) {
					var eastpanel=$('#panelEast'); //获取右侧收缩面板
					if(eastpanel.length>0){ //判断右侧收缩面板是否存在
						//重新装载右侧面板
				   		$('#divLayout').layout('panel','east').panel({
	                           href:url 
	                    });
					}else{//打开新面板
						$('#divLayout').layout('add', {
							region : 'east',
							width : 580,
							split : true,
							href : url,
							closable : true
						});
					}
				}
			
				
		   	//加载模式窗口
			function Adddilog(title, url, width, height) {
				$('#roleWins').dialog({    
				    title: title,    
				    width: width,    
				    height: height,    
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true   
				   });    
			}
			//适用用户名字
			function functionUser(value,row,index){
				return userMap[value];
			}	
		</script>
<style type="text/css">
.layout-split-east {
    border-left: 0px;
}
.panel-body-noheader{
	border-left: 0px;
}
.layout-split-east .panel-header{
	border:0;
}
.panel-noscroll{
	border-right:0;
}
</style>
</head>
	<body>
		<div id="divLayout" class="easyui-layout" fit=true>
	        <div data-options="region:'north',split:false,border:false" style="width: 100%;height: 30px;margin-left: 5px;margin-top: 5px;">	        
					<form id="search" method="post">
						<table style="width:100%;border:false;">
							<tr>
								<td  >登录用户：
								<input class="easyui-textBox" ID="userId" name="userLogin.userId" "/>
								登录时间：
								<input id="loginTime"  name="loginTime" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:120px;border: 1px solid #95b8e7;border-radius: 5px;"/>&nbsp;
								<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" onclick="searchFrom()"
								class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</shiro:hasPermission>
								</td>
							</tr>
						</table>
					</form>
			</div>
			<div data-options="region:'center',border:false">
				<input type="hidden" value="${id }" id="id" ></input>
				<table id="list" style="width:100%;" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#usertoolbarId',fit:true">
					<thead>
						<tr>
							<th data-options="field:'id',hidden:true" >登录编号</th>
							<th data-options="field:'userId',formatter: functionUser ">登录用户</th>
							<th data-options="field:'ip'">登录ip</th>
							<th data-options="field:'http'">登录客户端类型</th> 
							<th data-options="field:'sessionId'">登录会话</th>
							<th data-options="field:'loginTime'">登录时间</th>
							</tr>
					</thead>
				</table>				
				</div>	
			</div>
		</div>
		<div id="roleWins"></div>
	</body>
</html>