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
	<div class="easyui-layout" style="width:100%;height:100%; overflow-y: hidden;" id="treeLayOut">
		<div data-options="region:'center'"  id="content">
				<div id="divLayout" class="easyui-layout" fit=true style="width: 100%; height: 100%; overflow: hidden;">
					<div style="height: 40px">
						<form id="search" method="post">
							<table
								style="width: 100%; border: 1px solid #95b8e7; padding: 6px 0px 5px 0px;">
								<tr>
									<td style="width: 320px;" nowrap="nowrap">
										关键字：<input type="text" id="queryDFName" name="queryName" onkeydown="KeyDown(0)"/>
									</td>
									<td>
										<a href="javascript:void(0)" onclick="searchDFFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									</td>
								</tr>
							</table>
						</form>
					</div>
				<div style="padding: 0px 5px 5px 5px;">
					<table id="formList" style="width:100%" data-options="url:'<%=basePath%>queryCodeDosageform.action',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
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
<script type="text/javascript">
	//加载页面
		$(function(){
			$('#formList').height(500);
			
			var id="${id}"; //存储数据ID
			//添加操作按钮
			$('#formList').datagrid({
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				pagination:true,pageList:[20,30,50,80,100],pageSize:20,
				onLoadSuccess: function (data) {//默认选中
				var rowData = data.rows;
				$.each(rowData, function (index, value) {
				  if(value.id == id){
				     $("#formList").datagrid("checkRow", index);
				  }
			    });
			    },toolbar: [{
	               id: 'btnAdd',
	               text: '确定',
	               iconCls: 'icon-add',
	               handler: function () {
	               		var row = $('#formList').datagrid("getSelected");
	               		$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
	               		$.ajax({
							url: '<%=basePath%>drug/split/checkDosageFormWithId.action',
							data:{"dosageFormId" : row.id},
							type:'post',
							success: function(data) {
								$.messager.progress('close');
								if(data=="yes"){
									$('#infolist').datagrid('clearChecked');
				                    var index = $('#infolist').edatagrid('appendRow', {
										drugCode : row.id,
										drugName : row.name,
										deptCode:"",
										type:2
									}).edatagrid('getRows').length - 1;
									$('#infolist').edatagrid('checkRow', index);
									$('#infolist').edatagrid('beginEdit', index);
									//关闭弹出框
									$("#windowOpen").dialog('close');
								}else{
									$.messager.alert("操作提示", "此剂型已维护拆分属性，请重新选择！", "warning");
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
								}
							}
						});
	          	   }
	            }]
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
			var row = $("#formList").datagrid("getSelections");  
			var i = 0;   
			if(parameter=='single'){//获得单个id
			    if(row.length<1){
			    	$.messager.alert('提示',"请选择一条记录！");
			    	setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				  	return null;
				}else if(row.length>1){
					$.messager.alert('提示',"只能选择一条记录！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
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
					   setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
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
				    $('#formList').datagrid('selectRow', 0);
				    var row = $("#formList").datagrid("getSelections");  
				  }
				  id += row[0].id; 
				  return id;
			}else{
				$.messager.alert('提示',"参数无效！");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return null;
			}
		}
		/*查询
		*多个条件组合成一个条件查询 
		*
		*
		*/
	   	function searchDFFrom(){ 
	   		 var queryName = $('#queryDFName').val();
			 $('#formList').datagrid('load', {
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
</body>
</html>
