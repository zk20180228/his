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
	<body>
		<div id="divLayout" class="easyui-layout" fit=true>
	        <div data-options="region:'north',split:false" style="padding:10px;min-height:80px;height:auto;">	        
				<div class="easyui-panel" data-options="title:'用户账户信息查询',iconCls:'icon-search'">
						<table cellspacing="0" cellpadding="0" border="0">
							<tr>
								<td style="padding: 5px 15px;">名称：</td>
								<td>
									<input type="text" ID="accountNameSerc" name="account.accountName" onkeydown="KeyDown()"/>
								</td>
								<td style="padding: 5px 15px;">类型：</td>
								<td>
									<input type="text" ID="accountTypeSerc" name="account.accountType" onkeydown="KeyDown()"/>
								</td>
								<td>
									&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								</td>
							</tr>
						</table>
				</div>
			</div>			
			<div data-options="region:'center',split:false,title:'用户账户列表',iconCls:'icon-book'" style="padding:10px;">
				<table id="list" data-options="url:'queryAccount.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,fitColumns:false">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true" ></th>
							<th data-options="field:'idcard',formatter:function(value,row,index){
								if (row.idcard){
									return row.idcard.idcardNo;
								} else {
									return value;
								}
						}">就诊卡编号</th>
							<th data-options="field:'accountRefid'">参考编号</th>
							<th data-options="field:'accountName'">名称</th>
							<th data-options="field:'accountType'">类型</th>
							<th data-options="field:'accountBalance'">金额</th>
							<th data-options="field:'accountFrozencapital'">冻结金额</th>
							<th data-options="field:'accountFrozentime'">冻结时间</th>
							<th data-options="field:'accountUnfrozentime'">解冻时间</th>
							<th data-options="field:'accountRemark'">备注</th>
							<!--<th data-options="field:'canSelect'" formatter="formatCheckBox">可选标志</th>
							<th data-options="field:'isDefault'" formatter="formatCheckBox">默认标志</th>
						--></tr>
					</thead>
				</table>
			</div>
			 <!-- 添加框 -->
		</div>
		<!-- 添加框 -->
		<jsp:include page="../../../pages/patient/account/updatePassword.jsp"></jsp:include>
		<script type="text/javascript">
		//加载页面
			$(function(){
				$('#modifyPwd').window('close');  // close a window  
				var id='${id}'; //存储数据ID
				//添加datagrid事件及分页
				$('#list').datagrid({
					pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],
					onLoadSuccess: function (data) {//默认选中
			            var rowData = data.rows;
			            $.each(rowData, function (index, value) {
			            	if(value.id == id){
			            		$('#list').datagrid('checkRow', index);
			            	}
			            });
			        },toolbar: [{
	                    id: 'btnAdd',
	                    text: '添加',
	                    iconCls: 'icon-add',
	                    handler: function () {
	                        AddOrShowEast('添加','addAccount.action');
	                    }
	                }, '-', {
	                    id: 'btnEdit',
	                    text: '修改',
	                    iconCls: 'icon-edit',
	                    handler: function () {
	                    	var row = $("#list").datagrid("getSelections"); 
							var i = 0;    
							var getid = ""; 
							if(row.length!=1){
							    $.messager.alert("操作提示", "请选择一条用户记录！","warning");
							    return null;
							}else{  
								for(i;i<row.length;i++){ 
									getid = row[i].id; 
								}
							} 
	                        AddOrShowEast('编辑','editInfoAccount.action?id='+getid);
					   		
	                    }
	                }, '-', {
	                    id: 'btnDelete',
	                    text: '删除',
	                    iconCls: 'icon-remove',
	                    handler: function () {
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
										$.ajax({
											url: 'delAccount.action?id='+ids,
											success: function() {
												$.messager.alert('提示','删除成功');
												$('#list').datagrid('reload');
											}
										});
									}
	                        });
	                    }
	                }
	            }, '-', {
	                    id: 'btnView',
	                    text: '查看',
	                    iconCls: 'icon-book',
	                    handler: function () {
	                        var row = $('#list').datagrid('getSelected');	                        
	                        if(row){
	                        	AddOrShowEast('EditForm','viewAccount.action?id='+row.id);
					   		}
	                    }
	                }, '-', {
	                    id: 'btnReload',
	                    text: '刷新',
	                    iconCls: 'icon-reload',
	                    handler: function () {
	                        //实现刷新栏目中的数据
	                        $('#list').datagrid('reload');
	                    }
	                }]	                
					,onDblClickRow: function (rowIndex, rowData) {//双击查看
							var row = $('#list').datagrid('getSelected');	                        
	                        if(row){
	                        	AddOrShowEast('EditForm','viewAccount.action?id='+row.id);
					   		}
						}    
					});
				});
				/**
				 * 格式化复选框
				 * @author  lt
				 * @date 2015-6-18 9:25
				 * @version 1.0
				 */
				function formatCheckBox(val,row){
					if (val == 1){
						return '是';
					} else {
						return '否';
					}
				}				
			
				/**
				 * 查询
				 * @author  lt
				 * @param title 标签名称
				 * @param title 跳转路径
				 * @date 2015-06-18
				 * @version 1.0
				 */
				function searchFrom() {
					var accountNameSerc = $('#accountNameSerc').val();
					var accountTypeSerc = $('#accountTypeSerc').val();
					/*var idcardTypeSerc = $('#idcardTypeSerc').combobox('getValue');*/
					$('#list').datagrid('load', {
						accountName : accountNameSerc,
						accountType : accountTypeSerc
					});
				}
			/**
				 * 动态添加标签页
				 * @author  lt
				 * @param title 标签名称
				 * @param url 跳转路径
				 * @date 2015-06-18
				 * @version 1.0
				 */
				function AddOrShowEast(title, url) {
					$('#divLayout').layout('add', {
						region : 'east',
						width : 580,
						split : true,
						href : url,
						closable : true
					});
				}
				
				/**
				 * 回车键查询
				 * @author  lt
				 * @param title 标签名称
				 * @param title 跳转路径
				 * @date 2015-05-27
				 * @version 1.0
				 */
				function KeyDown()  
				{  
				    if (event.keyCode == 13)  
				    {  
				        event.returnValue=false;  
				        event.cancel = true;  
				        searchFrom();  
				    }  
				} 
		</script>
	</body>
</html>