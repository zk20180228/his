<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head> 
<body>
	<div class="easyui-panel" id = "panelEast" data-options="title:'',iconCls:'icon-form'" style="width: 100%;height: 100%;border-left:0">
		<form id="editForms" method="post">
			<input type="hidden" id="treeId" name="treeId" value='${treeId}'>
			<input type="hidden" id="deptCode" name="deptCode"/>
			<input type="hidden" id="feelId" name="feel" value='${feel}'>
			<input type="hidden" id="businessJson" name="businessJson">
			<table id="dg" class="easyui-edatagrid"  style="width:100%;"data-options="iconCls: 'icon-edit',checkOnSelect:false,selectOnCheck:false,onClickRow: onClickRow,border:false" >
						<thead> 
							<tr>
								<th field="getIdUtil" style="width:5%;" checkbox="true" ></th>
							    <th data-options="field:'consoleName',editor:{type:'textbox',options:{required:true,missingMessage:'该输入项为必输项，且同一手术房间的手术台名称不能重复！'}}"  style="width:120" >手术台名称</th>
								<th data-options="field:'inputCode',editor:{type:'textbox',options:{required:true}}" id="yz" style="width:80">输入码</th>
					        	<th data-options="field:'usingState',editor:{type:'combobox',options:{valueField:'id',textField:'name',multiple:false,editable:false,method:'get', url:'<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=operatTableStatus',valueField:'encode',textField:'name',required:true}},formatter:funUsingState" style="width:20%">当前使用状态</th>
								<th data-options="field:'remark',width:210,align:'center',editor:'textbox'">备注</th>
							</tr>
						</thead>
					</table>
		</form>			
	</div>
	<div id="tooId" style="display:none">
	<shiro:hasPermission name="${menuAlias}:function:add"> 
		<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:save"> 
		<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">保存</a>	
	</shiro:hasPermission>	
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true"" onclick="closeLayout()">关闭</a>
	<shiro:hasPermission name="${menuAlias}:function:delete"> 
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true"" onclick="delempt()">删除</a>
	</shiro:hasPermission>
	</div>
    <div id="toooId" style="display:none">
    <shiro:hasPermission name="${menuAlias}:function:delete"> 
		<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">保存</a>
	</shiro:hasPermission>	
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="closeLayout()">关闭</a>
	</div>
<script type="text/javascript">
 var map=new Map();
$(function(){
	//判断选中是否为科室
	if($('#tDt').tree('getSelected').attributes.isdept=='Y'){
		$('#deptCode').val($('#tDt').tree('getSelected').id);
	}else{
		$('#deptCode').val($('#tDt').tree('getSelected').attributes.pid);
	}
	
	
	var id=$('#trId').val();
	var feel = $("#feelId").val();
	if(feel=="1"){//添加操作
			var id="${id}"; //存储数据ID
			//添加edatagrid事件及分页
			$('#tooId').show();
			$('#toooId').hide(); 
			$('#dg').edatagrid({
				toolbar:'#tooId',
		   		onLoadSuccess: function (data) {//默认选中
		            var rowData = data.row;
		            $.each(rowData, function (index, value) {
		            	if(value.id == id){
		            		$("#dg").edatagrid("checkRow", index);
		            	}
		            });
	  	        },onDblClickRow: function (rowIndex, rowData) {//双击查看
	  	        		$('#dg').edatagrid('beginEdit',rowIndex);
				},onClickRow: function (rowIndex, rowData) {//双击查看
					$('#list').datagrid('clearChecked');
					$('#list').datagrid('clearSelections');
				} 
			});
		}else if(feel=="2"){
			$.ajax({
				url: '<%=basePath %>operation/oproomsole/deptNameCombobox.action',
				type:'post',
				success: function(deptData) {
					deptName = deptData;
					setTimeout(function(){
						var id="${id}"; //存储数据ID
						//添加edatagrid事件及分页
						$('#toooId').show();
						$('#tooId').hide(); 
						$('#dg').edatagrid({
							url:'<%=basePath %>operation/oproomsole/queryRoom.action?id='+id,
							toolbar:'#toooId',
					   		onLoadSuccess: function (data) {//默认选中
					            var rowData = data.row;
					   		    //hedong 20170303  数据加载完毕后，即打开行编辑，以方便用户。
					            editIndex = $('#dg').edatagrid('getRows').length-1;
					    	    $('#dg').edatagrid('selectRow', editIndex)
					    			.edatagrid('beginEdit', editIndex);
				  	        },onDblClickRow: function (rowIndex, rowData) {//双击查看
				  	        		$('#dg').edatagrid('beginEdit',rowIndex);
							} 
						});		
		            },100);
				}
			});	
		}
	  });
	
	//添加一行
	 function add(){
		var treeId = $('#treeId').val();
	    if(treeId==""||treeId==1){
	    	return;
	    }
        $('#dg').edatagrid('appendRow',{
        	usingState:0
        });
	    editIndex = $('#dg').edatagrid('getRows').length-1;
	    $('#dg').edatagrid('selectRow', editIndex)
			.edatagrid('beginEdit', editIndex);
	}
	 /**
	 * 手术台保存
	 * @author  zxl
	 * @date 2015-05-21
	 * @modifiedTime 2015-6-18
	 * @modifier liujl
	 * @version 1.0
	 */
	function edit(){
		$('#dg').edatagrid('acceptChanges');
		var row=$('#dg').edatagrid("getRows");
	 		var m=new Map();
	 		var m2=new Map();
	 		for(var i=0;i<row.length;i++){
	 			if(m.get(row[i].consoleName)!="1"){
	 				m.put(row[i].consoleName,"1");
	 			}else{
	 				$.messager.alert("提示","手术台名称不可相同");
	 				setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
	 				return;
	 			}
	 			if(m2.get(row[i].inputCode)!="1"){
	 				m2.put(row[i].inputCode,"1");
	 			}else{
	 				$.messager.alert("提示","手术台输入码不可相同");
	 				setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
	 				return;
	 			}
	 		}
		$('#businessJson').val(JSON.stringify( $('#dg').edatagrid("getRows")));
		$('#editForms').form('submit',{ 
	        url:'<%=basePath %>operation/oproomsole/saveOrUpdateBusinessOpconsole.action',  
	        onSubmit:function(){ 
			if (!$('#editForms').form('validate')){
	  		 		$.messager.progress('close');
					$.messager.show({  
					     title:'提示信息' ,   
					     msg:'验证没有通过,不能提交表单!'  
					}); 
					   return false ;
			     }
	            $.messager.progress({text:'保存中，请稍后...',modal:true});
	        },  
	        success:function(data){
	        	$.messager.progress('close');	
	        	if(data!=''){
	        		$.messager.alert("提示",'手术台名称'+'“'+data+'”'+'已存在！无需进行修改保存操作！');	
	        		setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
	        	}else{
	        		$('#divLayout').layout('remove','east');
                    $("#list").edatagrid("reload");
	        	}
	        },
			error : function(data) {
				$.messager.progress('close');	
				$.messager.alert("提示","保存失败！");	
			}			         
		  }); 
	 }
	
	 /**
	 * 删除手术台
	 * @author  zxl
	 * @date 2015-05-21
	 * @modifiedTime 2015-6-18
	 * @modifier liujl
	 * @version 1.0
	 */
	function delempt(){
		var row=$("#dg").datagrid("getChecked");
		if(row.length>0){//hedong 20170306   添加删除的判断 未选中时提示用户选择要删除的手术台
			for(var i=0;i<row.length;i++){
				$("#dg").datagrid("deleteRow",$("#dg").datagrid("getRowIndex",row[i]));
			}
			$("#dg").datagrid("clearChecked");
		}else{
			$.messager.alert("提示",'请先选择要删除的手术台！');	
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<style  type="text/css">
  .window .panel-header .panel-tool .panel-tool-close{
  	  	background-color: red;
  	}
</style>	
</body>