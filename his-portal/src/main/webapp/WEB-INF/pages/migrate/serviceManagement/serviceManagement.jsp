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
.layout-split-east {
    border-left: 0px;
}
.panel-body-noheader{
	border-left: 0px;
}
.layout-split-east .panel-header{
	border-top:0;
	border-left:0;
}
.panel-noscroll{
	border-right:0;
}
</style>
</head>
<body>
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;overflow-y: auto;" data-options="fit:true">
					<div  data-options="region:'north',split:false" style="width: 100%; height: 35px;border-top:0">
						<form id="searchForm" method="post" style="padding: 1px;margin-top: 5px;margin-left: 5px;">
							<table style="width:100%;border:false;">
								<tr>
									<td  style="width:250px;">服务名称:<input id="name" name="menu.name" class="easyui-textbox"  style="width:150px;"/></td>
									<td  style="width:250px;">服务类型:<input id="serviceType"  class="easyui-combobox"  style="width:150px;" readonly="readonly"/></td>
									<td  style="width:250px;">状态:<input id="serviceState"  class="easyui-combobox"  style="width:150px;" readonly="readonly"/></td>
									<td>
										<shiro:hasPermission name="${menuAlias}:function:query">
											<a href="javascript:void(0)" onclick="searchForm()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
											<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
										</shiro:hasPermission>
									</td>
								</tr>
							</table>
						</form>
				</div>
				<div data-options="region:'center',split:false" style="width: 100%; height: 30px; border-top:0">
					<table id="list" style="width:100%;"class="easyui-datagrid" data-options="idField:'id',treeField:'name',striped:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
						<thead>
							<tr>
								<th data-options="field:'ck',checkbox:true,height:40"></th>
								<th data-options="field:'id',hidden:true">id</th>
								<th data-options="field:'code'">服务编码</th>
								<th data-options="field:'name',width:'10%'">服务名称</th>
								<th data-options="field:'type',width:'7%',formatter:formatterType">服务类型</th>
								<th data-options="field:'masterprePare',width:'7%',formatter:formatterMaster">服务状态</th>
								<th data-options="field:'ip',width:'10%'">IP</th>
								<th data-options="field:'port',width:'10%'">占用端口</th>
								<th data-options="field:'heartRate',width:'10%'">心跳频率</th>
								<th data-options="field:'heartUnit',width:'10%',formatter:formatterHeartUnit">心跳频率单位</th>
								<th data-options="field:'heartNewtime',width:'10%'">最新心跳时间</th>
								<th data-options="field:'state',width:'7%',formatter:formatterState">状态</th>
								<th data-options="field:'operation',width:350,formatter:fromatterButton" style="padding-left: 5px;" align="center">操作</th>
								<th data-options="field:'remarks',width:'15%'">备注</th>
							</tr>
						</thead>
					</table>
				</div>
		</div>
	<div id="menuWin"></div>
	<div id="toolbarId">
	<shiro:hasPermission name="${menuAlias }:function:add">
		<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias }:function:edit">
		<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias }:function:delete">		
		<a href="javascript:void(0)" onclick="delRwo()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</shiro:hasPermission>
<%-- 	<shiro:hasPermission name="${menuAlias }:function:delete">		 --%>
<!-- 		<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-02',plain:true">停用</a> -->
<%-- 	</shiro:hasPermission> --%>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<script type="text/javascript">
	var menuAlias="${menuAlias}";
	var typeMap={'0':'同步服务','1':'接口服务'};
	var heartUnitMap={'S':'秒','M':'分','H':'时','D':'天','W':'周'};
	var stateMap={'0':'正常','1':'停用'};
	var masterMap={'1':'主','2':'备'};//主备
	$(function(){
		$('#serviceState').combobox({
			valueField: 'id',
			textField: 'value',
			data:[
			{id:'0',value:'正常'},
			{id:'1',value:'停用'}
			]
		});
		$("#serviceType").combobox({
			valueField: 'id',
			textField: 'value',
			data:[
			{id:'0',value:'同步服务'},
			{id:'1',value:'接口服务'},
			]
		   });
		searchForm();
		bindEnterEvent('name',searchForm,'easyui');//绑定回车事件
	});
	//条件查询
	function searchForm(){
		$('#list').datagrid({
			rownumbers: true,
			pageSize: "20",
			fit:true,
			singleSelect:true,
			checkOnSelect:false,
			selectOnCheck:false,
			pageList: [10, 20, 30, 50, 80, 100],
			pagination: true,
			method: "post",
			url: '<%=basePath%>/migrate/ServiceManagement/queryServiceManagement.action',
			queryParams:{code:$('#name').textbox('getValue'),menuAlias:menuAlias,serviceType:$('#serviceType').combobox('getValue'),serviceState:$('#serviceState').combobox('getValue')},
			onLoadSuccess:function(data){    
	        	 $("a[name='opera']").linkbutton({});
	        	 $('#list').datagrid('unselectAll');
	           	 $('#list').datagrid('uncheckAll');
			},
			onDblClickRow:function(index, row){
				AddOrShowEast('EditForm','<%=basePath %>migrate/ServiceManagement/viewServiceManagement.action?code='+row.id,'30%');
			}
		});
	}
	 //重置
	function clears(){
		$('#name').textbox('setValue','');
		$('#serviceType').combobox('clear');
		$('#serviceState').combobox('clear');
		searchForm();
	}
	//添加
	function add(){
		AddOrShowEast('EditForm',"<%=basePath %>migrate/ServiceManagement/addServiceManagement.action","30%");
	}
	//修改
	function edit(){
		var data=$('#list').datagrid('getSelected');
		if(data==""||data==null){
			$.messager.alert("操作提示", "请选择操作数据");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}else{
			AddOrShowEast('EditForm','<%=basePath %>migrate/ServiceManagement/addServiceManagement.action?code='+data.id,'30%');
		}
		
	}
	//停用
	function del(id,state,serviceName){
			var stateName="停用";
			if(state!=1){
				stateName="启用";
			}
			$.messager.confirm("提示","确认"+stateName+serviceName+"数据？",function(r){
				if(r){
					$.messager.progress({text:'处理中，请稍后...',modal:true});
					$.ajax({
						url: '<%=basePath %>migrate/ServiceManagement/delServiceManagement.action',
						data : {id:id,state:state},
						success : function(result){
							$.messager.progress('close');
							$.messager.alert('提示',result.resMsg);
							searchForm();
						}
						,error : function(){
							$.messager.progress('close');
							$.messager.alert('提示','网络繁忙,请稍后重试...');
						}
					});
				}
			});
	}
	//删除
	function delRwo(){
		var row=$("#list").datagrid('getSelected');
		if(row!=null){
			$.messager.confirm("提示","确认删除"+row.name+"服务？",function(r){
				if(r){
					$.messager.progress({text:'处理中，请稍后...',modal:true});
					$.ajax({
						url: '<%=basePath %>migrate/ServiceManagement/delService.action',
						data : {code:row.id},
						success : function(result){
							$.messager.progress('close');
							$.messager.alert('提示',result.resMsg,result.resCode);
							if('success'==result.resCode){
								searchForm();
							}
						}
						,error : function(){
							$.messager.progress('close');
							$.messager.alert('提示','网络繁忙,请稍后重试...');
						}
					});
				}
			});
		}else{
			$.messager.alert('提示','请选择删除的数据','info');
		}
	}
	//渲染服务类型   0同步服务 1接口服务 
	function formatterType(value,row,index){
		if(value=='0'||value!=null&&value!=''){
			return typeMap[value];
		}
		return value;
	}
	//渲染心跳单位   'S':'秒','M':'分','H':'时','D':'天','W':'周  
	function formatterHeartUnit(value,row,index){
		if(value=='0'||value!=null&&value!=''){
			return heartUnitMap[value];
		}
		return value;
	}
	//渲染状态   0正常1停用 
	function formatterState(value,row,index){
		if(value=='0'||value!=null&&value!=''){
			return stateMap[value];
		}
		return value;
	}
	//渲染主备   1主 2备 
	function formatterMaster(value,row,index){
		if(value!=null&&value!=''){
			return masterMap[value];
		}
		return value;
	}
	//刷新
	function reload(){
		$('#list').datagrid('reload');
	}
	
	//动态添加LayOut
	function AddOrShowEast(title, url,width,method,params) {
		if(!method){
			method="get";
		}
		if(!params){
			params={};
		}
		var eastpanel=$('#panelEast'); //获取右侧收缩面板
		if(eastpanel.length>0){ //判断右侧收缩面板是否存在
			$('#divLayout').layout('remove','east');
			$('#divLayout').layout('add', {
				region : 'east',
				width : width,
				split : true,
				href : url,
				method:method,
				queryParams:params,
				closable : true
			});
		}else{//打开新面板
			$('#divLayout').layout('add', {
				region : 'east',
				width : width,
				split : true,
				href : url,
				method:method,
				queryParams:params,
				closable : true
			});
		}
	}
			
    //加载模式窗口
	function Adddilog(title, url, width, height) {
		$('#menuWin').dialog({    
		    title: title,    
		    width: width,    
		    height: height,    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		   });    
	}
    
	//打开模式窗口
	function openDialog() {
		$('#roleWin').dialog('open'); 
	}
	
	//关闭模式窗口
	function closeDialog() {
		$('#roleWin').dialog('close');  
	}
	function reloadService(serviceIp,servicePort){
		$.messager.confirm("提示","确认重启"+serviceIp+"服务？",function(r){
			if(r){
				$.messager.progress({text:'处理中，请稍后...',modal:true});
				$.ajax({
						url: '<%=basePath %>migrate/ServiceManagement/reloadService.action',
					data : {ip:serviceIp,port:servicePort},
					success : function(result){
						$.messager.progress('close');
						$.messager.alert('提示',result.resMsg,result.resCode);
					}
					,error : function(){
						$.messager.progress('close');
						$.messager.alert('提示','网络繁忙,请稍后重试...');
					}
				});
			}
		});
	}
	function ping1(ip,system){
		$.messager.progress({text:'处理中，请稍后...',modal:true});
		$.ajax({
			url: '<%=basePath %>migrate/ServiceManagement/pingAction.action',
			data : {ip:ip,system:system},
			success : function(result){
				$.messager.progress('close');
				$.messager.alert('提示',result.resMsg);
				searchForm();
			}
			,error : function(){
				$.messager.progress('close');
				$.messager.alert('提示','网络繁忙,请稍后重试...');
			}
		});
	}
	//渲染按钮 
	function  fromatterButton(value,row,index){

			var htm="<a  name='opera' href='javascript:void(0);reloadService(\""+row.ip+"\",\""+row.port+"\");' style='width:50px;height:20px;' class='easyui-linkbutton' >重启</a>&nbsp;";
			htm+="<a  name='opera' href='javascript:void(0);logManage(\""+row.code+"\");' style='width:50px;height:20px;' class='easyui-linkbutton' >日志</a>&nbsp;";
			htm+='<a name="opera" onclick="ping1(\''+row.ip+'\',\''+row.system+'\')"  href="javascript:void(0);" style="width:50px;height:20px;" class="easyui-linkbutton" >ping</a>&nbsp;';
			if(row.state!=null&&row.state!='1'){
				htm+="<a name='opera'  href='javascript:void(0);del(\""+row.id+"\",\"1\",\""+row.name+"\");' style='width:50px;height:20px;' class='easyui-linkbutton' >停用</a>";
			}else{
				htm+="<a name='opera'  href='javascript:void(0);del(\""+row.id+"\",\"0\",\""+row.name+"\")'; style='width:50px;height:20px;' class='easyui-linkbutton' >启用</a>";
			}
			return htm;

	}
	//跳转到日志查询页面
	function logManage(code){
		window.open('<%=basePath %>/migrate/ServiceManagement/listLogService.action?code='+code,'日志查询','width=1000,height=500,top=200,left=450,resizable=yes,status=yes,menubar=no,scrollbars=yes');
	}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>