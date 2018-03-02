<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body style="margin: 0px;padding: 0px;">
		<div class="easyui-layout" fit=true style="width: 100%; height: 100%;">
			<div data-options="region:'north',split:false,border:false" style="height:40px;">
				<form id="search" method="post">
					<table style="width: 100%;  padding: 3px 0px 3px 0px;">
						<tr>
							<td style="width:350px;">查询条件：<input class="easyui-textbox" id="queryName" name="queryName" onkeydown="KeyDown(0)"  data-options="prompt:'药品名称,拼音,五笔,自定义,调价单号'" style="width: 255px;"/></td>
							<td>
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>							</td>
						</tr>
					</table>
				</form>
			</div>
			<div data-options="region:'center'" style="width:100%;height:100%">
				<table id="list" style="width: 100%;"
					data-options="url:'${pageContext.request.contextPath}/drug/adjustPriceAudit/queryAdjustAudit.action?menuAlias=${menuAlias}',fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true, width : '5%'" ></th>
							 <th data-options="field:'drugCode', width : '10%',align:'center',hidden:true">药品id</th> 
			                <th data-options="field:'tradeName', width : '10%',align:'center'">药品</th> 
			                <th data-options="field:'adjustBillCode', width : '10%',align:'center'">调价单据号</th> 
			                <th data-options="field:'adjustMode', width : '5%',align:'center',formatter: function(value,row,index){
																										if (value==1){
																											return '零售调价';
																										} if (value==2){
																											return '批发调价';
																										}
																									}">调价方式</th> 
			                <th data-options="field:'specs', width : '5%',align:'center'">规格</th> 
			                <th data-options="field:'prdoctName', width : '10%',align:'center'">生产厂家</th> 
			                <th data-options="field:'preRetailPrice', width : '7%',align:'center'">调前零售价</th> 
			                <th data-options="field:'retailPrice', width : '7%',align:'center'">调后零售价</th> 
			                <th data-options="field:'preWholesalePrice', width : '7%',align:'center'">调前批发价</th> 
			                <th data-options="field:'wholesalePrice', width : '7%',align:'center'">调后批发价</th> 
			                <th data-options="field:'prePurchasePrice', width : '7%',align:'center'">调前购入价</th> 
			                <th data-options="field:'purchasePrice', width : '7%',align:'center'">调后购入价</th> 
			                <th data-options="field:'remark', width : '10%',align:'center'">调价原因</th>  
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<div id="toolbarId">
			<shiro:hasPermission name="${menuAlias}:function:check">
				<a href="javascript:void(0)" onclick="check('0')" class="easyui-linkbutton" data-options="iconCls:'icon-tongguo',plain:true">通过</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:check">
				<a href="javascript:void(0)" onclick="check ('1')" class="easyui-linkbutton" data-options="iconCls:'icon-wuxiao',plain:true">无效</a>
			</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
			<shiro:hasPermission name="${menuAlias}:function:print">
			<a href="javascript:void(0)" onclick="printList()" class="easyui-linkbutton" data-options="iconCls:'icon-printer',plain:true">打印</a>
			</shiro:hasPermission>
		</div>
		<div id="viewInfos"></div>
			
		<style type="text/css">
		.window .panel-header .panel-tool a{
  	  			background-color: red;	
			}
		</style>
		<script type="text/javascript">
			$(function(){
				$('#list').datagrid({
					pagination:true,
			   		pageSize:20,
			   		pageList:[20,30,50,80,100],
			   		onBeforeLoad:function(){
			   			$('#list').datagrid('clearChecked');
			   			$('#list').datagrid('clearSelections');
			   		},onDblClickRow: function (rowIndex, rowData) {//双击查看
			        	var id = getId("single");
						if (id != null) {
			        		Adddilog("查看药品详细信息","<c:url value='/drug/adjustPriceAudit/viewInfo.action'/>?id="+id);		
						}
					},onLoadSuccess:function(row, data){
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
						}}    
				});
				
				bindEnterEvent('queryName',searchFrom,'easyui');//绑定回车事件
			});
			function check(flag){
				var type;
				if(flag==0){
					type=1;//通过
				}else{
					type=2;//无效
				}
				$('#list').datagrid('acceptChanges');
				var rows = $('#list').datagrid('getChecked');
             	if (rows.length > 0) {//选中几行的话触发事件	                        
					$.messager.confirm('确认', '确定要审核选中信息吗?', function(res){
						if (res){
							var ids = '';
							for(var i=0; i<rows.length; i++){
								if(ids!=''){
									ids += ',';
								}
								ids += rows[i].id;
							};
							$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条 
							$.post("<c:url value='/drug/adjustPriceAudit/auditAdjustPrice.action'/>?id="+ids+"&type="+type,function(data){
								$.messager.progress('close');
								$.messager.alert('提示',"审核成功！");
								$('#list').datagrid('reload');
								$('#list').datagrid('clearSelections');
								$('#list').datagrid('clearChecked');
							 });
						}
                 	});
             	}
			}
			function searchFrom() {
				var queryName = $.trim($('#queryName').textbox('getValue'));
				$('#list').datagrid('load', {
					name : queryName
				});
			}
			//获得选中id	
			function getId(parameter) {
				var row = $("#list").datagrid("getSelections");
				var i = 0;
				if (parameter == 'single') {//获得单个id
					if (row.length < 1) {
						$.messager.alert('操作提示',"请选择一条记录！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return null;
					} else if (row.length > 1) {
						$.messager.alert('操作提示',"只能选择一条记录！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return null;
					} else {
						var id = "";
						for (i; i < row.length; i++) {
							id += row[i].id;
							return id;
						}
					}
				} else if (parameter == 'plurality') {//获得多个id
					if (row.length < 1) {
						$.messager.alert('操作提示',"请至少选择一条记录！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return null;
					} else {
						var ids = "";
						for (i; i < row.length; i++) {
							ids += row[i].id + ",";
						}
						return ids;
					}
				} else if (parameter == 'notNull') {//至少获得一个id
					var id = "";
					if (row.length < 1) {//如果没有选择数据，默认选中第一行数据
						$('#list').datagrid('selectRow', 0);
						var row = $("#list").datagrid("getSelections");
					}
					id += row[0].id;
					return id;
				} else {
					$.messager.alert('提示',"参数无效！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return null;
				}
			}
			
			function reload(){
				$('#list').datagrid('reload');
			}
			//加载dialog
			function Adddilog(title, url) {
				$('#viewInfos').dialog({    
				    title: title,    
				    width: '50%',    
				    height:'60%',    
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true   
				   });    
			}
			//打开dialog
			function openDialog() {
				$('#stack').dialog('open'); 
			}
			//关闭dialog
			function closeDialog() {
				$('#stack').dialog('close');  
			}
			
			  //打印报表 ,打印的时候没有条件,全体打印
				function printList() {
					var row = $("#list").datagrid("getChecked");
					if(row.length > 0){
						var ids = '';
						for(var i = 0; i < row.length; i++){
							if(ids != ''){
								ids += ',';
							}
							ids += row[i].id;
						}
					var timerStr = Math.random();
					var his_name = encodeURIComponent(encodeURIComponent(name));
					window.open ("<%=basePath %>iReport/iReportPrint/iReportToYPTJD.action?randomId="+timerStr+"&fileName=YPTJD&ids="+ids,'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
					}else{
						$.messager.alert('提示',"请选择要打印的记录！");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}
				}
			  
				// 药品列表查询重置
				function searchReload() {
					$('#queryName').textbox('setValue','');
					searchFrom();
				}
		</script>
	</body>
</html>