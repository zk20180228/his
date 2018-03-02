<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>电子病历常用词常用词维护</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
		var wordTypeList = "";
		//加载页面
			$(function(){
			var winH=$("body").height();
		    //$('#p').height(winH-78-30-27-2);//78:页面顶部logo的高度，30:菜单栏的高度，27：tab栏的高度，
		    $('#list').height(winH-78-30-27-26);
				//添加datagrid事件及分页
				$('#list').datagrid({
					pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],
					onBeforeLoad:function(){
						$('#list').datagrid('clearChecked');
						$('#list').datagrid('clearSelections');
					},
					onDblClickRow: function (rowIndex, rowData) {//双击查看
					   	    AddOrShowEast('ViewForm',"<%=basePath %>emrs/word/toWordView.action?idd="+rowData.id);
					   	},
					onLoadSuccess:function(row, data){
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
				$.ajax({
					url: "<%=basePath %>emrs/word/wordTypeCombobox.action",
	 				type:'post',
	 				success: function(data) {
	 					wordTypeList = data;
					}
				});
				bindEnterEvent('wordName',searchFrom,'easyui');
				});
				//添加
				function add(){
					AddOrShowEast('EditForm','<%=basePath %>emrs/word/toAddView.action?menuAlias=${menuAlias}');
				}
				//修改
				function edit(){
					var id = ""; 
					var row = $("#list").datagrid("getSelected");  
				    if(row == undefined || row == null){
				    	$.messager.alert('提示',"请选择一条记录！");
				    	setTimeout(function(){$(".messager-body").window('close')},3500);
				       	return null;
				    }else { 
					  		id += row.id; 
					  	}
				    AddOrShowEast('EditForm',"<%=basePath %>emrs/word/toEditView.action?menuAlias=${menuAlias}&idd=" + id);
			   		}
				
				function del(){
				 //选中要删除的行
	                    var rows = $('#list').datagrid('getChecked');
                    	if (rows.length > 0) {//选中几行的话触发事件	                        
						 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
								if (res){
									var ids = '';
									for(var i=0; i<rows.length; i++){
										if(ids!=''){
											ids += ',';
										}
										ids += rows[i].id;
									};
									$.messager.progress({text:'保存中，请稍后...',modal:true});
									$.ajax({
										url: '<%=basePath %>emrs/word/remove.action?ids='+ids,
										type:'post',
										success: function() {
											$.messager.progress('close');
											$.messager.alert('提示','删除成功');
											$('#list').datagrid('reload');
											rows.length = 0;
										}
									});
								}
                        });
                    }else{
                    	$.messager.alert('提示','请勾选要删除的词条');
                    	setTimeout(function(){$(".messager-body").window('close')},3500);
                    }
				}
				//刷新
				function reload(){
					//实现刷新栏目中的数据
					$('#list').datagrid('unselectAll');
					$('#list').datagrid('reload');
				}
				
				/**
			 * 动态添加LayOut
			 * @param title 标签名称
			 * @param url 跳转路径
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
							closable : true,
							border:false
						});
					}
				}
				/**
				 * 关闭查看窗口
				 */
				function closeLayout(){
					$('#divLayout').layout('remove','east');
				}
				//条件查询
				function searchFrom(){
		   		    var wordName =  $.trim($('#wordName').val());
		   		 $('#list').datagrid('unselectAll');
				    $('#list').datagrid('load', {
				    	queryName: wordName 
					});
				}	
				//渲染类别
				function formatwordType(val,row,index){
					for(var i = 0;i < wordTypeList.length;i++){
						if(row.wordType == wordTypeList[i].encode){
							return wordTypeList[i].name;
						}
					}
				}	
				
		</script>
</head>
<body>
<!-- 电子病历常用词常用词维护 -->
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;"fit=true>
		 <div data-options="region:'north'" style="height: 39px;border-top:0">
				<form id="search" method="post">
					<table style="width:100%;border-bottom:1px solid #95b8e7;padding:4px;" class="changeskinBottom">
						<tr>
							<td  style="width: 300px;">名称：<input class="easyui-textbox"ID="wordName" name="emrWord.wordName"/>
								<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</table>
				</form>
		</div>
		<div data-options="region:'center'" style="height: 95%;border-top:0">
			<table id="list" fit="true" data-options="url:'<%=basePath %>emrs/word/queryWord.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true" ></th>
						<th data-options="field:'wordCode',width:'10%'">编码</th>
						<th data-options="field:'wordName',width:'10%'">名称</th>
						<th data-options="field:'wordType',formatter:formatwordType,width:'20%'">类别</th>
						<th data-options="field:'wordPinYin',width:'20%'">拼音码</th>
						<th data-options="field:'wordWb',width:'10%'">五笔码</th>
						<th data-options="field:'wordInputCode',width:'10%'">自定义码</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<div id="toolbarId">
	<shiro:hasPermission name="${menuAlias}:function:add">
		<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:edit">
		<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>	
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:delete">
		<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
</body>

