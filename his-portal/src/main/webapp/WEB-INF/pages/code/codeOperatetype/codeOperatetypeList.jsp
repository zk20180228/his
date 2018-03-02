<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div class="easyui-layout" style="width:100%;height:100%;" id="treeLayOut">
		<div data-options="region:'center'"  id="content">
				<div id="divLayout" class="easyui-layout" fit=true style="width: 100%; height: 100%; overflow: hidden;">
					<div style="padding: 5px 5px 0px 5px;">
						<form id="search" method="post">
							<table
								style="width: 100%; border: 1px solid #95b8e7; padding: 5px;">
								<tr>
									<td style="width: 320px;" nowrap="nowrap">
										关键字：<input type="text" id="queryName" name="queryName" onkeydown="KeyDown(0)"/>
									</td>
									<td>
										&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									</td>
								</tr>
							</table>
						</form>
					</div>
				<div style="padding: 0px 5px 5px 5px;">
					<table id="list" style="width:100%" data-options="url:'queryCodeOperatetype.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
						<thead>
							<tr>
								<th field="ck" checkbox="true"></th>
			    				<th data-options="field:'encode'" style="width:7%">代码</th>
			    				<th data-options="field:'name'" style="width:7%">名称</th>
			    				<th data-options="field:'pinyin'" style="width:7%">拼音码</th>
			    				<th data-options="field:'wb'" style="width:7%">五笔码</th>
			    				<th data-options="field:'inputCode'" style="width:7%">自定义码</th>
			    				<th data-options="field:'description'" style="width:7%">说明</th>
			    				<th data-options="field:'order'" style="width:7%">排序</th>
			    				<th data-options="field:'hospital'" style="width:7%">适用医院</th>
			    				<th data-options="field:'nonhospital'" style="width:7%">不适用医院</th>
								<th data-options="field:'canselect'" formatter="formatCheckBox">可选标志</th>
								<th data-options="field:'isdefault'" formatter="formatCheckBox">默认标志</th>
							</tr>
						</thead>
					</table>
				</div>
				</div>
			</div>
</div>
</body>
	<script type="text/javascript">
	//加载页面
		$(function(){
			var winH=$("body").height();
			$('#p').height(winH-78-30-27-2);
			$('#treeDiv').height(winH-78-30-27-2);
			$('#list').height(winH-78-30-27-22);
			
			var id="${id}"; //存储数据ID
			//添加操作按钮
			$('#list').datagrid({
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				pagination:true,pageList:[20,30,50,80,100],pageSize:20,
				onLoadSuccess: function (data) {//默认选中
				var rowData = data.rows;
				$.each(rowData, function (index, value) {
				  if(value.id == id){
				     $("#list").datagrid("checkRow", index);
				  }
			    });
			    },toolbar: [{
	               id: 'btnAdd',
	               text: '添加',
	               iconCls: 'icon-add',
	               handler: function () {
	                   AddOrShowEast('EditForm', 'addCodeOperatetype.action');
	          	   }
	            },'-',{
		            id:'btnEdit',
					text:'修改',	
					iconCls:'icon-edit',
					handler:function(){
					  if(getIdUtil("#list").length!=0){
						 AddOrShowEast('EditForm', 'updateCodeOperatetype.action?id='+getIdUtil("#list"));
					  }
						
				   }
				 },'-',{
				 	id:'btnDelete',
					text:'删除',
					iconCls:'icon-remove',
					handler:function(){
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
								  url: 'delCodeOperatetype.action?id='+ids,
								  type:'post',
								  success: function() {
									 $.messager.alert('提示','删除成功');
									  $("#list").datagrid("reload");
								  }	
							   });
						   }
		              });
		         }  	
				 }
				}, '-', {
	                  id: 'btnReload',
	                  text: '刷新',
	                  iconCls: 'icon-reload',
	                  handler: function () {
	                    //实现刷新
	                    $("#list").datagrid("reload");
	                  }
	             }],
	             onDblClickRow: function (rowIndex, rowData) {//双击查看
					if(getIdUtil("#list").length!=0){
						 AddOrShowEast('EditForm','viewCodeOperatetype.action?id='+getIdUtil("#list"));
					}
				}    
			});	
		});
		//格式复选框
		function formatCheckBox(val,row){
		  if(val==1){
			return '是';
		  }else{
			return '否';
		  }
		}
		//获得选中id	
		function getId(parameter){
			var row = $("#list").datagrid("getSelections");  
			var i = 0;   
			if(parameter=='single'){//获得单个id
			    if(row.length<1){
				    $.messager.alert('提示',"请选择一条记录！");
				  	return null;
				}else if(row.length>1){
				    $.messager.alert('提示',"只能选择一条记录！");
				    return null;
				}else{ 
				    var id = ""; 
					for(i;i<row.length;i++){    
					  	id += row[i].id; 
					    return id;
					}
				  	} 	
			 }else if(parameter=='plurality'){//获得多个id
				   if(row.length<1){
				       $.messager.alert('提示',"请至少选择一条记录！");
				       return null;
				   }else{  
				       var ids = ""; 
					   for(i;i<row.length;i++){   
					  	  ids += row[i].id+","; 
					   }
					  return ids;
				  } 
			}else if(parameter=='notNull'){//至少获得一个id
				  var id = ""; 
				  if(row.length<1){//如果没有选择数据，默认选中第一行数据
				    $('#list').datagrid('selectRow', 0);
				    var row = $("#list").datagrid("getSelections");  
				  }
				  id += row[0].id; 
				  return id;
			}else{
				$.messager.alert('提示',"参数无效！");
				return null;
			}
		}
		/**
		 * 动态添加标签页
		 * @param title 标签名称
		 * @param title 跳转路径
		 * @date 2015-05-23
		*/
		function AddOrShowEast(title,url){
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
		
		/*查询
		*多个条件组合成一个条件查询 
		*
		*
		*/
	   	function searchFrom(){ 
	   		 var queryName = $('#queryName').val();
			 $('#list').datagrid('load', {
				 name : queryName
			  });
		}
		//回车键
		function keyDown(){  
			if (event.keyCode == 13){  //目前只支持IE
			    event.returnValue=false;  
			    event.cancel = true;  
			    searchFrom();  
			}  
		} 	
	</script>
</html>
