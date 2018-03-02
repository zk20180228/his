<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>维度选择设置</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px; padding: 0px">
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'center'" style="">
			<table  style="width: 100%;border-spacing:10px;" id='sq'>
				<tr>
					<td width="20%">维度：</td>
					<td width="25%">
					<input type="hidden" id="dimensionNumber" name="biDimensionSetlist.dimensionNumber" value="${biDimensionSetlist[0].dimensionNumber}">
					<input id="dimensionName" value="${biDimensionSetlist[0].dimensionName}" class="easyui-textbox" data-options="required:true,editable:false"></td>
					<td width="25%">方向：</td>
					<td width="30%">
						<input id="direction" data-options="required:true,valueField: 'value',textField: 'label',
						data: [{label: '',value: ''},{label: '横向',value: '1'},{label: '纵向',value: '2'}]" class="easyui-combobox" >
					</td>
				</tr>
				<tr id="zhibiao" style="display:none">
					<td style="width:50%;height: 20%;padding-top: 0" colspan="2">
						<table id="regInfo" style="width: 100%;height: 100%;" class="easyui-datagrid">
							<thead>
								<tr>
									<th data-options="field:'setGroupid',width : '20%',hidden:true"></th>
									<th data-options="field:'indexName',width : '20%'">指标名称</th>
									<th data-options="field:'indextable', width : '20%'">表名</th>
									<th data-options="field:'indexField', width : '20%'">指标字段</th>
									<th data-options="field:'indexFieldChinese', width : '20%'">中文名</th>
									<th data-options="field:'polymerization', width : '20%'">聚合函数</th>
								</tr>
							</thead>
						</table>
					</td>
					<td colspan="2" style="width:50%;height: 20%">
						<form id="matBaseRegInfoForm" method="post">
		    				<table id="secTable" class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black"  style="width:90%; ">
								<tr><td>指标名称</td><td colspan="3"><input  class="easyui-textbox" id="indexName" name="indexName" data-options="required:true"></td></tr>
								<tr><td>表名</td><td colspan="3"><input  class="easyui-combobox" id="indextable" name="indextable" data-options="valueField:'tableName',textField:'tableName',url:'<%=basePath%>statistics/bi/statisticalSetting/querytableList.action',required:true"></td></tr>
								<tr><td>指标字段</td><td colspan="3"><input  class="easyui-combobox" id="indexField" name="indexField" data-options="required:true"></td></tr>
								<tr><td>中文名</td><td><input id="indexFieldChinese" name="indexFieldChinese" class="easyui-textbox" data-options="required:true"></td></tr>
								<tr><td>聚合函数</td><td><input id="polymerization" name="polymerization" class="easyui-combobox" data-options=" valueField: 'value',textField: 'label',
							data: [{label: '',value: ''},{label: 'count',value: 'count'},{label: 'min',value: 'min'},{label: 'max',value: 'max'},
							{label: 'sum',value: 'sum'},{label: 'avg',value: 'avg'},{label: 'variance',value: 'variance'},{label: 'stddev',value: 'stddev'},
							{label: 'median',value: 'median'}],required:true"></td></tr>
								<tr><td colspan="4" align="center">
									<a  href="javascript:void(0)" onclick="addRegister()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">添加</a>
									<a  href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清空</a>
									<a href="javascript:delRegister();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >删除</a>
								</td></tr>
							</table>
		    			</form>
					</td>
				</tr>
				<tr id='trzs0'>
				    <td style="width:50%;height: 20%;padding-top: 0" colspan="2">
						<table id="regInfo1" style="width: 100%;height: 100%;" class="easyui-datagrid">
							<thead>
								<tr>
									<th data-options="field:'setGroupid',width : '20%',hidden:true">分段名称</th>
									<th data-options="field:'subsectionName',width : '20%'">分段名称</th>
									<th data-options="field:'subsectionField',width : '20%'">分段字段</th>
									<th data-options="field:'subsectionFieldChinese',width : '20%'">中文名</th>
									<th data-options="field:'subsectionUpperLimit', width : '20%'">分段上限</th>
									<th data-options="field:'subsectionLowerLimit', width : '20%'">分段下限</th>
								</tr>
							</thead>
						</table>
					</td>
					<td colspan="2" style="width:50%;height: 20%">
						<form id="fenduanForm" method="post">
		    				<table id="secTable" class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black"  style="width:90%; ">
								<tr><td>分段名称</td><td colspan="3"><input  class="easyui-textbox" id="subsectionName" name="subsectionName" data-options="required:true"></td></tr>
								<tr><td>分段表名</td><td><input id="subsectiontable" name="subsectiontable" class="easyui-combobox" data-options="valueField:'tableName',textField:'tableName',url:'<%=basePath%>statistics/bi/statisticalSetting/querytableList.action',required:true"></td></tr>
								<tr><td>分段字段</td><td><input id="subsectionField" name="subsectionField" class="easyui-combobox" data-options="required:true"></td></tr>
								<tr><td>中文名</td><td><input id="subsectionFieldChinese" name="subsectionFieldChinese" class="easyui-textbox" data-options="required:true"></td></tr>
								<tr><td>分段上限</td><td><input id="subsectionUpperLimit" name="subsectionUpperLimit" class="easyui-textbox" data-options="required:true"></td></tr>
								<tr><td>分段下限</td><td><input id="subsectionLowerLimit" name="subsectionLowerLimit" class="easyui-textbox" data-options="required:true"></td></tr>
								<tr><td colspan="4" align="center">
									<a  href="javascript:void(0)" onclick="addRegister1()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">添加</a>
									<a  href="javascript:clear1();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清空</a>
									<a href="javascript:delRegister1();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >删除</a>
								</td></tr>
							</table>
		    			</form> 
					</td>
				</tr>
				<tr>
					<td align="center" colspan="4">
						<a  href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
						<a  href="javascript:closeLayout1();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-delete'">关闭</a>
					</td>
				</tr>
			</table>
	    </div>   
	</div>
	<script type="text/javascript">
		var dimensionType = "${biDimensionSetlist[0].dimensionType}";
		var setGroupid = "${setGroupid}";
		$(function(){
			if(dimensionType==1){
				$('#trzs0').hide();
			}else if(dimensionType==2){
				$('#trzs0').show();
			}
			$('#direction').combobox({
				onSelect:function(record){
					if(record.value==1){
						$('#zhibiao').show();
						$('#regInfo').datagrid({
							method:'post'
						});
					}else if(record.value==2){
						$('#zhibiao').hide();
					}
				}
			})
			$('#indextable').combobox({
				onSelect:function(record){
					$('#indexField').combobox({
			    		url:"<%=basePath%>statistics/bi/statisticalSetting/querycolumnNameList.action",
			    		queryParams:{columnName:record.tableName},
			    	    valueField:'columnName',
			    	    textField:'columnName',
					});
				}
			})
			$('#subsectiontable').combobox({
				onSelect:function(record){
					$('#subsectionField').combobox({
			    		url:"<%=basePath%>statistics/bi/statisticalSetting/querycolumnNameList.action",
			    		queryParams:{columnName:record.tableName},
			    	    valueField:'columnName',
			    	    textField:'columnName',
					});
				}
			})
		})

		//添加 指标
		function addRegister(){
			$('#matBaseRegInfoForm').form('submit',{
				onSubmit : function() {
					if(!$('#matBaseRegInfoForm').form('validate')){
						$.messager.show({  
					         title:'提示信息' ,   
					         msg:'验证没有通过,不能提交表单!'  
					    }); 
					    return false ;
					}
				},
				success:function(dataMap) {
					$('#regInfo').datagrid('appendRow',{
						indexName:$('#indexName').textbox('getText') ,
						indextable:$('#indextable').combobox('getValue'),
						indexField:$('#indexField').combobox('getValue'),
						indexFieldChinese: $('#indexFieldChinese').textbox('getText'),
						polymerization: $('#polymerization').combobox('getValue'),
						setGroupid: setGroupid
					});
					$('#matBaseRegInfoForm').form('clear');
				}
			});
		}
		//清空 指标
		function clear(){
			$('#matBaseRegInfoForm').form('clear');
		}
		//删除 指标
		function delRegister(){
			var rows = $("#regInfo").datagrid("getSelections");
			if(rows!=null&&rows!=''){
				for(var i=0;i<rows.length;i++){
					var index=$("#regInfo").datagrid('getRowIndex',rows[i]);
					$("#regInfo").datagrid('deleteRow',index);
				 }
			}else{
				$.messager.alert('提示','请选择一条记录进行删除！');
			}
		}
		//添加 分段
		function addRegister1(){
			$('#fenduanForm').form('submit',{
				onSubmit : function() {
					if(!$('#fenduanForm').form('validate')){
						$.messager.show({  
					         title:'提示信息' ,   
					         msg:'验证没有通过,不能提交表单!'  
					    }); 
					    return false ;
					}
				},
				success:function(dataMap) {
					$('#regInfo1').datagrid('appendRow',{
						subsectionName:$('#subsectionName').textbox('getText'),
						subsectionField:$('#subsectionField').combobox('getValue'),
						subsectionFieldChinese:$('#subsectionFieldChinese').textbox('getText'),
						subsectionUpperLimit:$('#subsectionUpperLimit').textbox('getText'),
						subsectionLowerLimit:$('#subsectionLowerLimit').textbox('getText'),
						setGroupid: setGroupid
					});
					$('#fenduanForm').form('clear');
				}
			});
		}
		//清空 分段
		function clear1(){
			$('#fenduanForm').form('clear');
		}
		//删除 分段
		function delRegister1(){
			var rows = $("#regInfo1").datagrid("getSelections");
			if(rows!=null&&rows!=''){
				for(var i=0;i<rows.length;i++){
					var index=$("#regInfo1").datagrid('getRowIndex',rows[i]);
					$("#regInfo1").datagrid('deleteRow',index);
				 }
			}else{
				$.messager.alert('提示','请选择一条记录进行删除！');
			}
		}
		function save(){
			var direction = $('#direction').combobox('getValue');
			if(direction==''||direction==null){
				$.messager.alert('提示','还没有维度方向！');
			}else{
				if(dimensionType==1){
					if(direction==1){
						var zhibiao = $('#regInfo').datagrid('getRows');
						if(zhibiao!=null&&zhibiao!=''){
							savefenduan();
						}else{
							$.messager.alert('提示','还没有进行指标设置！');
						}
					}else if(direction==2){
						savefenduan();
					}
				}else if(dimensionType==2){
					var fenduan = $('#regInfo1').datagrid('getRows');
					if(fenduan!=null&&fenduan!=''){
						if(direction==1){
							var zhibiao = $('#regInfo').datagrid('getRows');
							if(zhibiao!=null&&zhibiao!=''){
								savefenduan();
							}else{
								$.messager.alert('提示','还没有进行指标设置！');
							}
						}else if(direction==2){
							savefenduan();
						}
					}else{
						$.messager.alert('提示','还没有进行分段设置！');
					}
				}
			}
		}
		//保存
		function savefenduan(){
			var dimensionNumber = $('#dimensionNumber').val();
			$.ajax({
				url : '<%=basePath%>statistics/bi/statisticalSetting/saveBiSubsectionSetOrBiIndexSet.action',
				data:{biSubsectionSetJson:JSON.stringify($('#regInfo1').datagrid('getRows')),biIndexSetJson:JSON.stringify($('#regInfo').datagrid('getRows')),dimensionNumber:dimensionNumber},
				type : 'post',
				success : function(data) {
					$.messager.alert("提示", data, "info");
					weidu($('#dimensionName').textbox('getValue'),$('#dimensionNumber').val(),$('#direction').combobox('getValue'));
					$('#menuWin1').dialog('close');
				}
			});
		}
		//关闭
	    function closeLayout1(){
	    	$('#menuWin1').dialog('close');
	    }
	</script>
</body>
</html>