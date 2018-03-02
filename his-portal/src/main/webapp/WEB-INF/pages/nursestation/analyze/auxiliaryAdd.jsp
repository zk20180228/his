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
	<style type="text/css">
		.tableCss {
			border-collapse: collapse;
			border-right:0px;
			border-top:0px;
		}
		.tableLabel {
			text-align: right;
			width: 70px;
		}
		.tableCss td {
			border-right: 1px solid #95b8e7;
			border-bottom: 1px solid #95b8e7;
			padding: 5px 15px;
			word-break: keep-all;
			white-space: nowrap;
		}
	</style>
</head>
<body>
	<div id="auxAddElId" class="easyui-layout" data-options="fit:true,border:false">
		<input type="hidden" id="auxAddId" value="${id}">
		<div data-options="region:'north',split:false,border:false" style="height:72px;">
			<table class="tableCss" width="100%" >
				<tr>
					<td align="right">附材名称：</td>
					<td align="left"><input id="auxAddNameId" style="width:150px;"><a href="javascript:delauxAddName();" style="display:inline-block;" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
					<td align="right">用法：</td>
					<td align="left"><input id="auxAddUseaId" style="width:150px;"></td>
					<td align="right">执行科室：</td>
					<td align="left"><input id="auxAddDeptId" style="width:150px;"></td>
				</tr>
				<tr>
					<td align="right">数量：</td>
					<td align="left"><input id="auxAddNumId"></td>
					<td align="right">备注：</td>
					<td align="left"><input id="auxAddremark" class="easyui-textbox" style="width:150px;"></td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center',border:false">
			<table id="auxAddDgListId">   
				<thead>   
					<tr>   
						<th data-options="field:'ck',checkbox:true" ></th>
						<th data-options="field:'code',width:100">附材Code</th>   
						<th data-options="field:'name',width:200">附材名称</th>   
						<th data-options="field:'spec',width:100">规格</th>   
						<th data-options="field:'num',width:100">数量</th>   
						<th data-options="field:'price',width:100">单价</th>   
						<th data-options="field:'unit',width:100">单位</th>   
					</tr>   
				</thead>   
			</table>
		</div> 
	</div>
	<script>
		$('#auxAddDgListId').datagrid({
			rownumbers:true,
			striped:true,
			border:false,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,fitColumns:false,
			pagination:false,
			fit:true,
			toolbar: [{
				text:'删除',
				iconCls: 'icon-remove',
				handler: function(){
					var rows = $('#auxAddDgListId').datagrid('getChecked');
					if(rows!=null&&rows.length>0){
						for(var i=0;i<rows.length;i++){
							$('#auxAddDgListId').datagrid('deleteRow',$('#auxAddDgListId').datagrid('getRowIndex',rows[i]));
						}
					}else{
						$.messager.alert('提示','请选择要删除的附材！',null,function(){});	
					}
				}
			},'-',{
				text:'保存',
				iconCls: 'icon-save',
				handler: function(){
					var usea = $('#auxAddUseaId').combobox('getValue');
					if(usea==null||usea==''){
						$.messager.alert('提示','请选择用法！',null,function(){});
						return;
					}
					var dept = $('#auxAddDeptId').combobox('getValue');
					if(dept==null||dept==''){
						$.messager.alert('提示','请选择执行科室！',null,function(){});
						return;
					}
					var auxiliary = $('#auxAddDgListId').datagrid('getRows');
					if(auxiliary!=null&&auxiliary.length>0){
						var remark = $('#auxAddremark').datetimebox('getText');
						$.messager.confirm('提示信息', '确定保存附材信息？', function(r){
							if (r){
								$.ajax({
									url:"<%=basePath%>nursestation/analyze/saveAuxInfo.action",
									type:'post',
									data:{usea:usea,dept:dept,remark:remark,auxiliary:$.toJSON(auxiliary),id:$('#auxAddId').val()},
									success:function(dataMap) {
										$.messager.alert('提示',dataMap.resCode,null,function(){});
										if(dataMap.resMsg=='success'){
											$('#auxEd').datagrid('reload');
											$('#auxModelDivId').dialog('close');
										}
									},
									error:function(){
										$.messager.alert('提示','请求失败！',null,function(){});	
									}
								});
							}else{
								return;
							}
						});
					}else{
						$.messager.alert('提示','请添加附材信息！',null,function(){});
						return;
					}
				}
			}],
			onSelect:function(index,row){
				$('#auxAddNumId').numberbox('setValue',row.num);
			}
		});
		$('#auxAddNameId').combogrid({
			url:"<%=basePath%>drug/undrug/queryunDrug.action",
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			panelWidth:600,	
			idField:'id',	
			textField:'name', 
			mode:'remote',
			columns:[[	
				{field:'name',title:'名称',width:200},	
				{field:'spec',title:'规格',width:100},	
				{field:'price',title:'价格',width:100},
				{field:'unit',title:'单位',width:100},
			]],
			onClickRow:function(index,row){
				var lastIndex = $('#auxAddDgListId').datagrid('appendRow',{
					code:row.code,
					name:row.name,
					spec:row.spec,
					num:1,
					price:row.price,
					unit:row.unit,
				}).datagrid('getRows').length-1;
				$('#auxAddDgListId').datagrid('selectRow',lastIndex);
			}
		});
		$('#auxAddFreId').combobox({
			url:"<%=basePath%>baseinfo/frequency/queryFrequencyGroup.action",
			valueField:'code',	
			textField:'name',
			groupField:'organize',
			filter: function(q, row){
				 var keys = new Array();
				 keys[keys.length] = 'name';
				 keys[keys.length] = 'pinyin';
				 keys[keys.length] = 'wb';
				 keys[keys.length] = 'inputCode';
				 return filterLocalCombobox(q, row, keys);
			}
		});
		$('#auxAddUseaId').combobox({
			url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
			queryParams:{type:'useage'},
			valueField:'encode',	
			textField:'name',
			filter: function(q, row){
				 var keys = new Array();
				 keys[keys.length] = 'name';
				 keys[keys.length] = 'pinyin';
				 keys[keys.length] = 'wb';
				 keys[keys.length] = 'inputCode';
				 return filterLocalCombobox(q, row, keys);
			}
		});
		$('#auxAddDeptId').combobox({
			url:"<%=basePath%>baseinfo/department/departmentCombobox.action",
			valueField:'deptCode',	
			textField:'deptName',
			filter: function(q, row){
				 var keys = new Array();
				 keys[keys.length] = 'deptName';
				 keys[keys.length] = 'deptPinyin';
				 keys[keys.length] = 'deptWb';
				 keys[keys.length] = 'deptInputcode';
				 return filterLocalCombobox(q, row, keys);
			}
		});
		$('#auxAddNumId').numberbox({
			min:1,
			max:999,
			precision:0
		});
		$('#auxAddNumId').numberbox('textbox').bind('keyup',function(event){
			var row = $('#auxAddDgListId').datagrid('getSelected');
			if(row!=null){
				var index = $('#auxAddDgListId').datagrid('getRowIndex',row);
				var curVal = parseInt($('#auxAddNumId').numberbox('getText'));
				if(curVal<=0||curVal==null||curVal==''){
					curVal = 1;
				}
				if(curVal>999){
					curVal=999;
				}
				$('#auxAddNumId').numberbox('setValue',curVal);
				$('#auxAddDgListId').datagrid('updateRow',{
					index:index,
					row: {
						num:curVal
					}
				});
			}
		});
	function delauxAddName(){
		$('#auxAddNameId').combogrid('clear');
	}
	function filterLocalCombobox(q, row, keys){
		if(keys!=null && keys.length > 0){
			for(var i=0;i<keys.length;i++){
				 if(row[keys[i]]!=null&&row[keys[i]]!=''){
					 var istrue = row[keys[i]].indexOf(q.toUpperCase())>-1||row[keys[i]].indexOf(q)>-1;
					 if(istrue==true){
						 return true;
					 }
				 }
			 }
		}else{
			var opts = $(this).combobox('options');
			return row[opts.textField].indexOf(q.toUpperCase())>-1||row[opts.textField].indexOf(q)>-1;
		}
	}
	</script>
</body>
</html>