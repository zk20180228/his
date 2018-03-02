<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>电子病历医技症状维护</title>
	<%@ include file="/common/metas.jsp"%>
	<script type="text/javascript">
		var symptomTypeList = "";
		//加载页面
			$(function(){
				$.ajax({
					url:"<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=symptomtype",
	 				type:'post',
	 				success: function(data) {
	 					symptomTypeList = data;
					}
				});
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
					   	    AddOrShowEast('ViewForm',"<%=basePath %>emrs/symptom/toSymptomView.action?idd="+rowData.id);
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
				
				bindEnterEvent('symptomName',searchFrom,'easyui');
				});
				//添加
				function add(){
					AddOrShowEast('EditForm','<%=basePath %>emrs/symptom/toAddView.action?menuAlias=${menuAlias}');
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
				    AddOrShowEast('EditForm',"<%=basePath %>emrs/symptom/toEditView.action?menuAlias=${menuAlias}&idd=" + id);
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
										url: '<%=basePath %>emrs/symptom/remove.action?ids='+ids,
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
			 * @author  liujl
			 * @param title 标签名称
			 * @param url 跳转路径
			 * @date 2015-05-21
			 * @modifiedTime 2015-6-18
			 * @modifier liujl
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
				//条件查询
				function searchFrom(){
		   		    var symptomName =  $.trim($('#symptomName').val());
		   		 	$('#list').datagrid('unselectAll');
				    $('#list').datagrid('load', {
				    	queryName: symptomName 
					});
				}	
				//渲染类别
				function formatsymptomType(val,row,index){
					for(var i = 0;i < symptomTypeList.length;i++){
						if(row.symptomType == symptomTypeList[i].encode){
							return symptomTypeList[i].name;
						}
					}
				}	
				/**
				 * 关闭查看窗口
				 */
				 function closeLayout(){
						$('#divLayout').layout('remove','east');
						$("#list").datagrid("reload");
					}
		</script>
</head>
<body>
<!-- 电子病历医技症状维护 -->
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;overflow-y: auto;"fit=true>
		 <div data-options="region:'north',border:false" style="width:100%;height:40px;">
				<form id="search" method="post">
					<table style="width:100%;height:100%;border: none;padding:5px;" data-options="fit:true">
						<tr>
							<td  style="width: 300px;">名称：<input class="easyui-textbox"ID="symptomName" name="symptom.symptomName"/>
								<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</table>
				</form>
		</div>
		<div data-options="region:'center',border:false" style="width:100%;height:94%;">
			<table id="list"style="width:100%;" data-options="url:'<%=basePath %>emrs/symptom/querySymptom.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true" ></th>
						<th data-options="field:'symptomCode',width:'10%'">编码</th>
						<th data-options="field:'symptomName',width:'10%'">名称</th>
						<th data-options="field:'symptomType',formatter:formatsymptomType,width:'20%'">类别</th>
						<th data-options="field:'symptomPinYin',width:'20%'">拼音码</th>
						<th data-options="field:'symptomWb',width:'10%'">五笔码</th>
						<th data-options="field:'symptomInputCode',width:'10%'">自定义码</th>
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
