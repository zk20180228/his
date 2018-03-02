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
	<title>设置邮箱</title>
</head>
<body>
	<table id="configBox" title="设置邮箱" >
	    <thead>
	        <tr>
	        	<th data-options="field:'ck',checkbox:true"></th>
	        	<th data-options="field:'email',width:'15%'">账号</th>
	        	<th data-options="field:'nickName',width:'8%'">帐户昵称</th>
	            <th data-options="field:'receiveType',width:'7%'">收件类型</th>
	            <th data-options="field:'receiveHost',width:'7%'">收件服务器</th>
	            <th data-options="field:'receivePort',width:'7%'">收件端口</th>
	            <th data-options="field:'receiveSecurity',width:'7%'">收件安全</th>
	            <th data-options="field:'sendHost',width:'7%'">发件服务器</th>
	            <th data-options="field:'sendPort',width:'7%'">发件端口</th>
	            <th data-options="field:'sendSecurity',width:'7%'">发件安全</th>
	            <th data-options="field:'timing',width:'8%'">定时收取（分钟）</th>
<%--
	            <th data-options="field:'companyFlg',width:'12%',formatter:companyFlgFormatter">邮箱分类</th>
--%>
	        </tr>
	    </thead>
	</table>
	<div id="configMailWindow" class="easyui-window" 
		data-options="collapsible:false,minimizable:false,resizable:false,modal:true,closed:true"
		style="width: 40%; height: 60%; padding: 10px;"></div>
	
	<script type="text/javascript">
		$('#configBox').datagrid({
            fit:true,
			method:'get',
			url:'<%=basePath%>oa/mail/queryByEmpCode.action',
//			singleSelect:true,
			fitColumns:true,
			pagination:true,
			toolbar: [{
		        text:"新建",
		        iconCls:"icon-add",
		        handler:function(){
		        	var href = "<%=basePath%>/oa/mail/page/editConfig.action";
		        	$("#configMailWindow").window({
		        		title : "新建邮箱"
		        	});
		        	$("#configMailWindow").window("open");
		        	$('#configMailWindow').window('refresh', href);
		        }
		    },'-',{
		        text:"修改",
		        iconCls:"icon-edit",
		        handler:function(){
		        	var sels = $("#configBox").datagrid("getSelections");
		        	if(sels.length == 0){
		        		$.messager.alert('提示','未选中任何邮箱!');
		        		return ;
		        	}
		        	var href = "<%=basePath%>/oa/mail/page/editConfig.action?mailConfig.id="+sels[0].id;
		        	$("#configMailWindow").window({
		        		title : "修改邮箱"
		        	});
		        	$("#configMailWindow").window("open");
		        	$('#configMailWindow').window('refresh', href);
		        	
		        }
		    },'-',{
		    	text:'删除',
		        iconCls:'icon-remove',
		        handler:function(){
		        	var sels = $("#configBox").datagrid("getSelections");
		        	if(sels.length === 0){
		        		$.messager.alert('提示','未选中任何邮箱!');
		        		return ;
		        	}
		        	$.messager.confirm('确认','是否要删除<b>'+sels[0].email+'</b>邮箱 ？',function(r){
		        	    if (r){
		        	    	var params = {"mailConfig.id":sels[0].id};
		                	$.post("<%=basePath%>oa/mail/deleteMailConfig.action",params, function(data){
		            			if(data.resCode === "success"){
		            				$.messager.alert('提示','删除邮箱成功!',undefined,function(){
		            					$("#configBox").datagrid("reload");
		            				});
		            			}
		            		});
		        	    }
		        	});
		        }
		    },'-',{
                text:"刷新",
                iconCls:"icon-reload",
                handler:function(){
                    $("#configBox").datagrid("reload");
                }
            }]
		});
		function companyFlgFormatter(value){
			if(value === 0){
				return "公司邮箱";
			}else if(value === 1){
				return "个人邮箱";
			}
		}
	</script>
</body>
</html>