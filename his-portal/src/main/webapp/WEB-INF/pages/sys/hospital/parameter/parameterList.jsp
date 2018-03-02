<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>参数管理</title>
<%@ include file="/common/metas.jsp" %>
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

</style>
</head>
<body style="margin: 0px; padding: 0px">
	<div id="divLayout" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north'" style="height: 40px;padding:4px;border-top:0">
			<input class="easyui-textbox"ID="parameterNameSerc"
			    name="parameter.parameterName" onkeydown="KeyDown()" data-options="prompt:'参数名称，参数类型'" />
			<shiro:hasPermission name="CSGL:function:query">
			    <a href="javascript:void(0)" onclick="searchFrom()"
				    class="easyui-linkbutton" iconCls="icon-search">查询</a>
				    
				<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
			</shiro:hasPermission>
		</div>
		<div data-options="region:'center'">
			<table id="list" style="width:100%;"
				data-options="url:'${pageContext.request.contextPath}/sys/queryParameter.action',method:'post',
				rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,
				selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
				<thead>
					 <tr>
					 <th data-options="field:'ck',checkbox:true"></th>
						
						<th data-options="field:'parameterName'">
							参数名称
						</th>
						<th data-options="field:'parameterCode'">
							参数代码
						</th>
						<th data-options="field:'parameterType'">
							参数类型
						</th>
						<th data-options="field:'parameterValue'">
							参数值
						</th>
						<th data-options="field:'parameterUnit'">
							参数单位
						</th>
						<th data-options="field:'parameterDownlimit'">
							参数上限
						</th>
						<th data-options="field:'parameterUplimit'">
							参数下限
						</th>
						<th data-options="field:'parameterRemark'">
							备注
						</th>
						
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
	</div>
	<script type="text/javascript">
		//加载页面
			$(function(){
				var id='${id}'; //存储数据ID
				//添加datagrid事件及分页
				$('#list').datagrid({
					pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],
					
					onLoadSuccess: function (data) {//默认选中
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
			            var rowData = data.rows;
			            $.each(rowData, function (index, value) {
			            	if(value.id == id){
			            		$('#list').datagrid('checkRow', index);
			            	}
			            });
			        },
			        onBeforeLoad:function(param){
						$(this).datagrid('uncheckAll');
					},
					onDblClickRow: function (rowIndex, rowData) {//双击查看
							var row = $('#list').datagrid('getSelected');	                        
	                        if(row){
	                        	AddOrShowEast('EditForm','<%=basePath%>sys/viewParameter.action?id='+row.id);
					   		}
						}    
					});
					bindEnterEvent('parameterNameSerc',searchFrom,'easyui');
				});
		
				/**
				* @Description 跳转至添加页面
				* @author   
				* @Modifier tangfeishuai
				* @ModifyDate 2016-04-12
				* @ModifyRmk 
				* @CreateDate 2015-05-23
				* @version   1.0 
				*/
				function add(){
					AddOrShowEast('添加','<%=basePath%>sys/addParameter.action');
				}
				
				/**
				* @Description 跳转至编辑页面
				* @author   
				* @Modifier tangfeishuai
				* @ModifyDate 2016-04-12
				* @ModifyRmk 
				* @CreateDate 2015-05-23
				* @version   1.0 
				*/
				function edit(){
					var row = $('#list').datagrid('getSelected'); //获取当前选中行                        
                    if(row){
                       	 AddOrShowEast('编辑','<%=basePath%>sys/addParameter.action?id='+row.id);
			   		}else{
			   			$.messager.alert('提示',"请选择要修改的信息！");
			    		close_alert();
			   		}
				}
				
			   /**
				* @Description 多行删除功能
				* @author   
				* @Modifier tangfeishuai
				* @ModifyDate 2016-04-12
				* @ModifyRmk 
				* @CreateDate 2015-05-23
				* @version   1.0 
				*/
				function del(){
					 //选中要删除的行
					var delFlag=false;
					var rows = $('#list').datagrid('getChecked');
					if (rows.length > 0) {//选中几行的话触发事件	  
						for (var i = 0; i < rows.length; i++) {
							if(rows[i].del_flg==2){
								$.messager.alert('提示',rows[i].parameterName+'不能删除！');
								close_alert();
								delFlag=true;
							}
					
	                    }
						if(delFlag==false){
							$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
								if (res){
									var ids = '';
									for(var i=0; i<rows.length; i++){
										if(ids!=''){
											ids += ',';
										}
										ids += rows[i].id;
									};
									$.ajax({
										url: '<%=basePath%>sys/delParameter.action?pid='+ids,
										type:'post',
										success: function() {
											$.messager.alert('提示','删除成功');
											$('#list').datagrid('load');
											$('#list').datagrid('clearSelections');
											}
										});
									}
		                        });
						}
					}else{
						$.messager.alert('提示',"请选择要删除的信息！");
			    		close_alert();
					}
				}
				
				function reload(){
					//实现刷新栏目中的数据
					 $('#list').datagrid('reload');
				}
				/**
				 * @Description 查询
				 * @author  lt
				 * @param title 标签名称
				 * @param title 跳转路径
				 * @date 2015-06-19
				 * @version 1.0
				 */
				function searchFrom() {
					var parameterNameSerc = $('#parameterNameSerc').val();
					$('#list').datagrid('load', {
						parameterName : parameterNameSerc
					});
				}
				/**
				 * 重置
				 * @author huzhenguo
				 * @date 2017-03-17
				 * @version 1.0
				 */
				function clears(){
					$('#parameterNameSerc').textbox('setValue','');
					searchFrom();
				}
			    /**
				 * 动态添加标签页
				 * @author  lt
				 * @param title 标签名称
				 * @param url 跳转路径
				 * @date 2015-06-19
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
							width : '40%',
							split : true,
							href : url,
							closable : true
						});
					}
				}
				
				/**
				 * 回车键查询
				 * @author  lt
				 * @param title 标签名称
				 * @param title 跳转路径
				 * @date 2015-06-19
				 * @version 1.0
				 */
				function KeyDown(){  
				    if (event.keyCode == 13){  
				        event.returnValue=false;  
				        event.cancel = true;  
				        searchFrom();  
				    }  
				} 
		</script>
</body>
</html>