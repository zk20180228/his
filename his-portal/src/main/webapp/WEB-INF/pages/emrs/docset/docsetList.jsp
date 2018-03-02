<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>三级检诊医生设置</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
var empMap = "";//医生
var deptMap = "";//科室
	$(function(){
		setTimeout(function(){
			$('#list').datagrid({
				pagination:true,
				pageSize:20,
				pageList:[20,30,50,80,100],
				url:'<%=basePath %>emrs/threedoctorSet/getDocSet.action'
			});
		},1000);
		//科室
		$.ajax({
			url: '<%=basePath%>baseinfo/department/getDeptMap.action' ,
			type:'post',
			success: function(deptData) {
				deptMap = deptData;
			}
		});
		$.ajax({//渲染人员
			url: '<%=basePath%>baseinfo/employee/getEmplMap.action',
	 		type:'post',
	 		async:true,
	 		success: function(empData) {	
	 			empMap = empData;
	 		}
	 	});
		//科室下拉
		$('#deptCode').combobox({
			url:'${pageContext.request.contextPath}/finance/medicinelist/queryEdComboboxDept.action',
			valueField:'deptCode',
			textField:'deptName',
			method:'post',
			onHidePanel:function(none){
			    var data = $(this).combobox('getData');
			    var val = $(this).combobox('getValue');
			    var result = true;
			    for (var i = 0; i < data.length; i++) {
			        if (val == data[i].deptCode) {
			            result = false;
			        }
			    }
			    if (result) {
			        $(this).combobox("clear");
			    }else{
			        $(this).combobox('unselect',val);
			        $(this).combobox('select',val);
			    }
			},
			filter: function(q, row){
			    var keys = new Array();
			    keys[keys.length] = 'deptCode';
			    keys[keys.length] = 'deptName';
			    keys[keys.length] = 'deptPinyin';
			    keys[keys.length] = 'deptWb';
			    keys[keys.length] = 'deptInputcode';
			    return filterLocalCombobox(q, row, keys);
			}
		});
		$('#higherDoc').combobox({
			url:'${pageContext.request.contextPath}/baseinfo/employee/employeeCombobox.action',
			valueField:'jobNo',
			textField:'name',
			multiple:false,
			method:'post',
			onHidePanel:function(none){
			    var data = $(this).combobox('getData');
			    var val = $(this).combobox('getValue');
			    var result = true;
			    for (var i = 0; i < data.length; i++) {
			        if (val == data[i].jobNo) {
			            result = false;
			        }
			    }
			    if (result) {
			        $(this).combobox("clear");
			    }else{
			        $(this).combobox('unselect',val);
			        $(this).combobox('select',val);
			    }
			},
			filter: function(q, row){
			    var keys = new Array();
			    keys[keys.length] = 'jobNo';
			    keys[keys.length] = 'name';
			    keys[keys.length] = 'pinyin';
			    keys[keys.length] = 'wb';
			    return filterLocalCombobox(q, row, keys);
			}
		});
		$('#chiefDoc').combobox({
			url:'${pageContext.request.contextPath}/baseinfo/employee/employeeCombobox.action',
			valueField:'jobNo',
			textField:'name',
			multiple:false,
			method:'post',
			onHidePanel:function(none){
			    var data = $(this).combobox('getData');
			    var val = $(this).combobox('getValue');
			    var result = true;
			    for (var i = 0; i < data.length; i++) {
			        if (val == data[i].jobNo) {
			            result = false;
			        }
			    }
			    if (result) {
			        $(this).combobox("clear");
			    }else{
			        $(this).combobox('unselect',val);
			        $(this).combobox('select',val);
			    }
			},
			filter: function(q, row){
			    var keys = new Array();
			    keys[keys.length] = 'jobNo';
			    keys[keys.length] = 'name';
			    keys[keys.length] = 'pinyin';
			    keys[keys.length] = 'wb';
			    return filterLocalCombobox(q, row, keys);
			}
		});
	});
function formatedept(value,row,index){
	return deptMap[value];
}
function formateEmp(value,row,index){
	return empMap[value];
}
//添加
function add(){
	$('#submitType').val(1);
	$('#win').window('open');
}
//修改
function update(){
	$('#submitType').val(0);
	var list = $('#list').datagrid('getSelected');
	$('#docsetid').val(list.id);
	$('#deptCode').combobox('setValue',list.deptCode);
	$('#higherDoc').combobox('setValue',list.higherDoc);
	$('#chiefDoc').combobox('setValue',list.chiefDoc);
	$('#win').window('open');
}
//删除
function del(){
	$.messager.progress({text:'删除中，请稍后...',modal:true});
	var list = $('#list').datagrid('getChecked');
	var id="";
	if(list!=null&&list!=""&&list.length>0){
		for(var i=0;i<list.length;i++){
			if(id!=""){
				id += ",";
			}
			id += list[i].id;
		}
	}
	$.ajax({
		url:"<%=basePath%>emrs/threedoctorSet/del.action",
		data:{id:id}
		,success: function(data){
			$.messager.progress('close');
        	$.messager.alert('提示',data.resMsg,'info',function(){
        		$('#list').datagrid('reload');
        		$('#addDForm').form('reset');
        		$('#win').window('close');
        	});   
	    }
		,error: function(data){ 
			$.messager.progress('close');
	    	$.messager.alert('提示','保存失败！！','info');   
	    }    
	});
	
}
function submit(){
	var type = $('#submitType').val();
	var url = "<%=basePath%>emrs/threedoctorSet/add.action";
	var message = "保存中，请稍后...";
	if(type==0){
		url = "<%=basePath%>emrs/threedoctorSet/update.action";
		message = "修改中，请稍后...";
	}
	$.messager.progress({text:message,modal:true});
	$('#addDForm').form('submit',{
		url:url,
		onSubmit: function(){
			if(!$(this).form('validate')){
				$.messager.alert('操作提示','尚有数据不符合规定，请修改后保存！！');
				return false;
			}
		},
		success: function(data){
			$.messager.progress('close');
			var data = eval("("+data+")");
        	$.messager.alert('提示',data.resMsg,'info',function(){
        		$('#list').datagrid('reload');
        		$('#addDForm').form('reset');
        		$('#win').window('close');
        	});   
	    },    
	    error: function(data){    
	    	$.messager.progress('close');
	    	$.messager.alert('提示','保存失败！！','info');   
	    }    
	});
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
<div class="easyui-layout" style="width:100%;height:100%;overflow-y: auto;"fit=true>
		<div data-options="region:'north',border:false" style="height:35px;">
	        <form data-options="border:false">	        
					<table style="width:100%;border:0;padding:1px;">
						<tr>
							<td>
								&nbsp;<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" iconCls="icon-add">添加</a>
								&nbsp;&nbsp;<a href="javascript:void(0)" onclick="update()" class="easyui-linkbutton" iconCls="l-btn-icon icon-edit">修改</a>
								&nbsp;&nbsp;<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" iconCls="l-btn-icon icon-remove">删除</a>
							</td>
						</tr>
					</table>
			</form>
		</div>
	        <div data-options="region:'center',border:false">
				<table id="list" class="easyui-datagrid" style="width:100%" data-options="rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,fitColumns:true,singleSelect:true,fit:true">   
				    <thead>   
				        <tr>   
				        	<th data-options="field:'ck',checkbox:true" ></th>
				            <th data-options="field:'deptCode',width:100,formatter:formatedept ">科室编号</th>   
				            <th data-options="field:'higherDoc',width:100,formatter:formateEmp ">上级医生</th>   
				            <th data-options="field:'chiefDoc',width:100,formatter:formateEmp ">主任医师</th>   
				        </tr>   
				    </thead>   
				</table>  
			</div>
	</div>
	<div id="win" class="easyui-window docsetList" title="添加" style="width:600px;height:400px" data-options="modal:true,closed:true,iconCls:'icon-save',collapsible:false,minimizable:false,maximizable:false,closable:true">
		<form id="addDForm" method="post">
			<input id="docsetid" type="hidden" name="docset.id" value="${docset.id}"/>
			<input id="submitType" type="hidden">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0" >
				<tr>
					<td class="honry-lable">
						<span style="font-size: 13">科室:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-combobox" id="deptCode"
									name="docset.deptCode"
									value="${docset.deptCode }"
									data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span style="font-size: 13">上级医师:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-combobox" id="higherDoc"
									name="docset.higherDoc"
									value="${docset.higherDoc }"
									data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						<span style="font-size: 13">主任医师:</span>
					</td>
					<td class="honry-info">
						<input style="width: 200px;" class="easyui-combobox" id="chiefDoc"
									name="docset.chiefDoc"
									value="${docset.higherDoc }"
									data-options="required:true"/>
					</td>
				</tr>
<!-- 				<tr align="center" > -->
<!-- 				    <td> -->
<!-- 						<a href="javascript:submit();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_tick'">确定</a> -->
<!-- 						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#win').window('close');" data-options="iconCls:'icon-cancel'">关闭</a> -->
<!-- 					</td> -->
<!-- 				</tr> -->
			</table>
		</form>
		<div style="text-align: center; padding: 5px">
			<a href="javascript:submit();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_tick'">确定</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#win').window('close');" data-options="iconCls:'icon-cancel'">关闭</a>
		</div>
	</div>
</body>
</html>