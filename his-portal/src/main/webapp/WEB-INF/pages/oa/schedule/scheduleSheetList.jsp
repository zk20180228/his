<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>日程列表管理</title>
<%@ include file="/common/metas.jsp"%>

<style type="text/css">
	.panel-header{
		border-top:0;
	}
	body,html{
		width:100%;
		height:100%;
	}
	.window .panel-header .panel-tool .panel-tool-close{
  	  		background-color: red;
  	  	}
</style>
</head>
<body>
		<div style="width:100%;height:100%">
		<div id="edit"></div>
            <div style="width:100%;height:100%;padding-top:0px"><table id="dg" style="width:100%;height:100%"></table></div>
			<div id="toolbarId" >
			    <shiro:hasPermission name="${menuAlias}:function:add">
				<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
	 			</shiro:hasPermission>
	 			<shiro:hasPermission name="${menuAlias}:function:edit"> 
				<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
	 			</shiro:hasPermission> 
	 			<shiro:hasPermission name="${menuAlias}:function:delete"> 
				<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	 			</shiro:hasPermission> 		
				<a href="javascript:void(0)" onclick="updateFlag()" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true">标记已完成</a>		
				<a href="javascript:void(0)" onclick="changeViews()" class="easyui-linkbutton" data-options="iconCls:'icon-06'">日历视图</a>		
		  </div>
	 </div>
</body>
</html>

<script type="text/javascript">
 		 	var falg = "1";//用户map	
 			//加载页面
			$(function(){
				$('#dg').datagrid({  
					url:'<%=basePath%>oa/agenda/agendaAction/queryScheduleList.action',
					fit:true,
					singleSelect:true,
					method:'post',
					rownumbers:true,
					idField: 'id',
					striped:true,
					border:false,
					selectOnCheck:false,
					checkOnSelect:true,
					fitColumns:true,
					toolbar:"#toolbarId",
					pagination : true,//是否显示分页栏
					pageSize : 20,//每页显示的记录条数，默认为10  
					pageList : [ 20,30,50,100 ],//可以设置每页记录条数的列表  
					columns:[[ 
					          {field:'id',title:'事件id',hidden:true},      
					          {field:'title',title:'标题',width:"15%"},      
					          {field:'dayFlg',title:'是否全天',width:"5%",align:"center",formatter: function(value,row,index){
									if (value==1){
										return "√";
									} else {
										return "×";
									}
								}
},      
					          {field:'end',title:'起始时间',width:"15%"},  
					          {field:'isZDY',title:'是否自定义',width:"5%",align:"center",formatter: function(value,row,index){
									if (value==1){  
										return "√";
									} else {
										return "×";
									}
								}
}, 
					          {field:'time',title:'提醒时间',width:"15%"},    
					          {field:'isFinish',title:'是否已完成',width:"5%",align:"center",formatter: function(value,row,index){
									if (value=='1'){
										return "√";
									} else {
										return "×";
									}
								}
}, 
					          {field:'remark',title:'备注',width:"20%"}, 
					      ]]
					});
			});
			//弹出添加窗口								
			function add(){
				var currDate=new Date();
				var year=currDate.getFullYear(); 
				var month=currDate.getMonth()+1;
				month=month>9?month:"0"+month;
				var day=currDate.getDate();
				day=day>9?day:"0"+day;
				var time=year+'-'+month+'-'+day;
				Adddilog("新建日程","<c:url value='/oa/agenda/agendaAction/toEdit.action'/>?menuAlias=${menuAlias}&t="+time+"&flag=1",'530','465');
			}
			
			//加载模式窗口
			function Adddilog(title, url,width,height) {
				$('#edit').dialog({    
				    title: title,    
				    width: width,    
				    height:height,    
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true,
				    shadow: false
				});    
			}
			//关闭模式窗口
			function closeLayout(){
				$('#edit').window('close'); 
			}
			
			
		
			//删除
			function del(){
				var obj = $('#dg').datagrid('getSelected');
			 	if (obj!=null) {//选中几行的话触发事件	                        
					$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
						if(res){
						   var id=obj.id;
						   $.messager.progress({text:'删除中，请稍后...',modal:true});
							$.ajax({
							 	url: "<%=basePath%>oa/agenda/agendaAction/del.action?t="+id,   
								type:'post',
								success: function(data) {
									$.messager.progress('close');
									if("success"==data.resCode){
										$.messager.alert('提示',data.resMsg);
										$('#dg').datagrid('reload');
									}else{
										$.messager.alert('提示',data.resMsg);
									}
								}
							});
						}
						})
			 }else{
				$.messager.alert('提示','请先选择要删除的日程!'); 
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
			  return;
			 }
			}
			
			//修改
			function edit(){	
			    var node = $('#dg').datagrid('getSelected');
			    if(node==null){
				$.messager.alert('提示','请选择一条记录!'); 
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
			    return;
			    }
			   var id=node.id;
			   Adddilog("编辑","<c:url value='/oa/agenda/agendaAction/toEditView.action'/>?t="+id+"&flag=1",'530','465');
			}
			
			//刷新
			function reload(){
				//实现刷新栏目中的数据
				$('#dg').datagrid('reload');
			}
			
			function changeViews(){
				window.location.href="<c:url value='/oa/agenda/agendaAction/agendaActionToView.action'/>";
			}
			function updateFlag(){
				var node = $('#dg').datagrid('getSelected');
				 if(node==null){
						$.messager.alert('提示','请选择一条记录!'); 
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
					    return;
					    }
				var isFinish=node.isFinish
				if(isFinish==1){
					$.messager.alert('提示','已标记为过期事件!'); 
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
			    return;
			    }
				var id=node.id;
				   $.ajax({
					     async:false,
			             type: "POST",
			             url: "<c:url value='/oa/agenda/agendaAction/toFinished.action'/>",
			             data: {"flag":isFinish,"t":id},
			             success:function(data){
			            	
			            	 if(data=='0'){
									$.messager.alert('提示','标记成功！');	
			  						close_alert();
			  						return;
								}else{
									$.messager.alert('提示','标记失败！');	
									close_alert();
								}
							}
				   });
				 $('#dg').datagrid('reload');
			}
			
</script>