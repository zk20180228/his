<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Access-Control-Allow-Origin" content="*">  
</head>
	<body>
		<div class="easyui-layout" data-options="fit:true">   
			<div data-options="region:'north'" style="height:40px;padding:5px;">
				<input id="pacsQueryName" >
				<a href="javascript:void(0)" onclick="pacsSearchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
			</div>
			<div data-options="region:'center',border:false" class="pacs1">
				<table id="pacsPatEdId">
					<thead>
						<tr>
							<th data-options="field:'id',width:130">编号</th>
<!-- 							<th data-options="field:'image',width:70">图像</th> -->
							<th data-options="field:'report',width:70">报告</th>
							<th data-options="field:'name',width:80">姓名</th>
							<th data-options="field:'sex',width:50">性别</th>
							<th data-options="field:'age',width:50">年龄</th>
							<th data-options="field:'unit',width:50">单位</th>
							<th data-options="field:'source',width:80">来源</th>
							<th data-options="field:'no',width:130">病历号</th>
							<th data-options="field:'insNo',width:220">检查号</th>
							<th data-options="field:'insPro',width:100">检查项目</th>
							<th data-options="field:'insPos',width:260">检查部位</th>
							<th data-options="field:'dept',width:150">科室</th>
							<th data-options="field:'insTime',width:160">时间</th>
						</tr>
					</thead>
				</table>
			</div> 
		</div>
		<div id="pacsWinId"></div>
		<script type="text/javascript">
		$('#pacsQueryName').textbox({prompt:'回车查询'});
		setTimeout(function(){bindEnterEvent('pacsQueryName',pacsSearchFrom,'easyui');},100);
		$('#pacsPatEdId').datagrid({
			striped:true,
			border:true,
			singleSelect:true,
			fit:true,
			url:"<%=basePath%>outpatient/advice/queryListByNo.action",
			onLoadSuccess:function(data){
				var rows = data.rows;
				if(rows!=null&&rows.length>0){
					var image = null;
					var report = null;
					for(var i=0;i<rows.length;i++){
						image = '';
						report = '';
						if(rows[i].image!=null&&rows[i].image!=''){
							image='<a class="imageCls" onclick="toImage(\''+rows[i].image+'\')" href="javascript:void(0)" style="height:20"></a>';
						}
						if(rows[i].report!=null&&rows[i].report!=''){
							report='<a class="reportCls" onclick="toReport(\''+rows[i].report+'\')" href="javascript:void(0)" style="height:20"></a>';
						}
						$(this).datagrid('updateRow',{
							index: i,
							row: {
								image : image,
								report : report
							}
						});
					}
					$('.imageCls').linkbutton({text:'图像',plain:true,iconCls:'icon-up'});
					$('.reportCls').linkbutton({text:'报告',plain:true,iconCls:'icon-up'});
				}
			}
		});
		
		function pacsSearchFrom(){
			var pacsQueryName =$('#pacsQueryName').textbox('getValue'); 
			$('#pacsPatEdId').datagrid('load', {
				clinicNo:pacsQueryName
			});
		}
		
		function toImage(url) {
			$.dialog({
				title: '影像',	
				window:true,
				border:false,
				href:'<%=basePath%>outpatient/advice/queryImage.action?para='+url,
			})
		}
		
		function toReport(url) {
			$.dialog({
				title: '报告',	
				width:1100,
				height:650,
				href:'<%=basePath%>outpatient/advice/queryReport.action?para='+url,
			})
		}
		</script>
	</body>
</html>