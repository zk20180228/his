<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>药房药库库存初始化</title>
<%@ include file="/common/metas.jsp"%>
</head>
	<body>
		<div class="easyui-layout" data-options="fit:true">
			<div class="storageList" id="p" data-options="region:'west',split:true"  style="width:15%;height:100%;padding:5px;border-top:0">
				<fieldset style="border: 0;">
				<ul id="tDrug"></ul>
				</fieldset>
				<br><br>
				<hr>
				<br>
				<fieldset style="border: 0;">
				<ul id="tDt"></ul> 
				</fieldset> 
			</div>
			<div data-options="region:'center',border:false" style="width:84%;height:100%;">
				<div id="divLayout" class="easyui-layout" data-options="fit:true">
			        <div class="storageList" data-options="region:'north',split:false" style="height:65px;border-top:0">	        
						<form id="search" method="post" style="padding:5px 0px 0px 0px;">
							<table cellspacing="0" cellpadding="0" border="0px solid black" >
								<tr>
									<td>
										&nbsp;查询条件：<input class="easyui-textbox" name="name" id="name"  data-options="prompt:'药品名称,拼音,五笔,自定义'" style="width:180px"/>
									</td>
									<td align="left">
										<shiro:hasPermission name="${menuAlias}:function:query">
										&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
										<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
										</shiro:hasPermission>
									</td>
								</tr>
								
								<tr>
									<td>
									 	&nbsp;初始化量：<input type="text" class="easyui-numberbox" data-options="min:0,precision:0" ID="num" style="width: 180px;"></input>
									</td>
									<td align="left"><shiro:hasPermission name="${menuAlias}:function:initialization">
									&nbsp;<a href="javascript:void(0)" onclick="saveOrUpdate()" class="easyui-linkbutton" iconCls="icon-initialization">初始化</a>
										</shiro:hasPermission>
									</td>
					    		</tr>
							</table>
						</form>
					</div>
					<div data-options="region:'center',split:false,title:'药品列表',iconCls:'icon-book'" >
						<table id="list"  class="easyui-datagrid"  data-options="url:'${pageContext.request.contextPath}/drug/info/queryDrugInfo.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,
						border:false,checkOnSelect:true,selectOnCheck:true,fitColumns:false,pagination:true,pageList: [20,30,50,80,100],pageSize:20,fit:true,onLoadSuccess:function(row, data){
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
								}}">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true" ></th>
									<th data-options="field:'name'" style="width:13%">名称</th>
									<th data-options="field:'drugNamepinyin'" style="width:13%">名称拼音码</th>
									<th data-options="field:'drugNamewb'" style="width:12%">名称五笔码</th>
									<th data-options="field:'drugNameinputcode'" style="width:10%">名称自定义码</th>
									<th data-options="field:'drugCommonname'" style="width:10%">通用名称</th>
									<th data-options="field:'drugCnamepinyin'" style="width:10%">通用名称拼音码</th>
									<th data-options="field:'drugCnamewb'" style="width:10%">通用名称五笔码</th>
									<th data-options="field:'drugCnameinputcode'" style="width:9%">通用名称自定义码</th>
									<th data-options="field:'drugBiddingcode'" style="width:9%">招标识别码</th>
								</tr>
							</thead>
						</table>				
					</div>
				</div>
			</div>	
		</div>	
		<script type="text/javascript">
		$(function(){
		 bindEnterEvent('name',searchFrom,'easyui');//姓名
		});
			//加载药品类别树
		   	$('#tDrug').tree({    
			    url: "<c:url value='/drug/info/drugInfoTreeBytype.action'/>",
			    method:'get',
			    animate:true,
			    lines:true,
			    onClick: function(node){//点击节点
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
					$('#list').datagrid('load', {
					drugType: node.id
				});
				}
			}); 
	   		
			//加载部门树
		   	$('#tDt').tree({    
		   		url:"<c:url value='/drug/storage/treeDrugstore.action'/>?flag=1",
			    method:'get',
			    animate:true,
			    lines:true,
			    formatter:function(node){//统计节点总数
					var s = node.text;
					if (node.children){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
					return s;
				},onClick: function(node){//点击节点attributes
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
				}
			}); 
			//初始化操作
			function saveOrUpdate() {
				 //选中要删除的行
                var rows = $('#list').datagrid('getChecked');
               	if (rows.length > 0) {//选中几行的话触发事件	
               		var ids = '';
					for(var i=0; i<rows.length; i++){
						if(ids!=''){
							ids += ',';
						}
						ids += rows[i].code;
					};
               	}else{
               		$.messager.alert("操作提示", "请在列表选择需要初始化的药品！","warning");
               		setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
               		return;
               	} 
				if($("#num").numberbox('getValue')==null || $("#num").numberbox('getValue')==""){
					$.messager.alert("操作提示", "请填写初始化数量！","warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
				if(isNumeric($("#num").numberbox('getValue'))){
					$.messager.alert("操作提示",isNumeric($("#num").numberbox('getValue')),"warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
				
				var node = $('#tDt').tree('getSelected');
				if(node){
					if(!$('#tDt').tree('isLeaf',node.target)){
						$.messager.alert("操作提示", "请在左侧部门科室信息选择具体科室！","warning");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return;
					}
				}else{
					$.messager.alert("操作提示", "请在左侧部门科室信息选择目标科室！","warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
				$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
				$.ajax({
					url:"<%=basePath%>drug/storage/saveOrUpdateStorage.action",
					data:{'drugId':ids,'deptId':node.attributes.deptCode,'num':$("#num").numberbox('getValue')},
					type:'post',
					success: function() {
						$.messager.progress('close');
						$.messager.alert("操作提示", "初始化成功！","info");
						window.location="<%=basePath%>drug/storage/listStorage.action?menuAlias=${menuAlias}";
					}
				});
			} 
			//查询
			function searchFrom(){
				var name =	$.trim($('#name').textbox('getValue'));
			    $('#list').datagrid('load', {
					name: name
				});
			}
			//查询重置
			function searchReload() {
				$('#name').textbox('setValue','');
				searchFrom();
			}
		</script>
	</body>
</html>