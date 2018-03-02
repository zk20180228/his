<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
		<div style="padding:5px 5px 5px 5px;">
			<table style="padding:0px 5px 5px 5px;">
				<tr>
					<td style="width:70">组套类型:</td>
			 		<td style="width:130"><input class="easyui-combobox" type="text" id="adStackInfoSaveModelStacktype" data-options="valueField:'id',textField:'text',data:stackType,required:true" style="width:110px" missingMessage="请输入组套类型" /></td>
					<td style="width:70">组套名称:</td>
			 		<td style="width:130"><input class="easyui-textbox" type="text" id="adStackInfoSaveModelName" data-options="required:true" style="width:110px" missingMessage="请输入名称" /></td>
					<td style="width:70">组套来源:</td>
			 		<td style="width:130"><input class="easyui-combobox" type="text" id="adStackInfoSaveModelSource" data-options="valueField:'id',textField:'text',data:stackSource,required:true" style="width: 110px" missingMessage="请输入组套来源" /></td>
			 		<td style="width:70">自定义码:</td>
			 		<td style="width:130"><input class="easyui-textbox" type="text" id="adStackInfoSaveModelInputCode" data-options="" style="width:110px" missingMessage="请输入名称" /></td>
					<td style="width:70">备注:</td>
			 		<td style="width:130"><input class="easyui-textbox" type="text" id="adStackInfoSaveModelRemark" data-options="" style="width:110px" missingMessage="请输入名称" /></td>
					<td style="width:70">是否共享:</td>
			 		<td style="width:130"><input type="checkbox" id="adStackInfoSaveModelShareFlag"></td>
			 	</tr>
			</table>
			<table class="easyui-datagrid"  style="padding:5px 5px 5px 5px;" id="adStackInfoSaveModelDgId"data-options="rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,width:'100%',height:335"> 
				 <thead>   
			        <tr>   
			            <th data-options="field:'id',width:100,hidden:true">名称</th>   
			            <th data-options="field:'name',width:100">名称</th>   
			            <th data-options="field:'spec',width:100">规格</th>   
			            <th data-options="field:'packagingnum',width:100">包装数量</th>   
			            <th data-options="field:'unit',width:100,hidden:true">单位</th>   
			            <th data-options="field:'unitView',width:100">单位</th>   
			            <th data-options="field:'defaultprice',width:100">默认价</th>   
			            <th data-options="field:'childrenprice',width:100">儿童价</th>   
			            <th data-options="field:'specialprice',width:100">特诊价</th>   
			            <th data-options="field:'frequencyCode',width:100,hidden:true">频次编码</th>   
			            <th data-options="field:'frequencyCodeView',width:100">频次编码</th>   
			            <th data-options="field:'usageCode',width:100,hidden:true">用法名称</th>   
			            <th data-options="field:'usageCodeView',width:100">用法名称</th>   
			            <th data-options="field:'onceDose',width:100">每次服用剂量</th>   
			            <th data-options="field:'doseUnit',width:100,hidden:true">剂量单位</th>   
			            <th data-options="field:'doseUnitView',width:100">剂量单位</th>   
			            <th data-options="field:'mainDrugshow',width:100">主药标记</th>   
			            <th data-options="field:'dateBgn',width:100">医嘱开始时间</th>   
			            <th data-options="field:'dateEnd',width:100">医嘱结束时间</th>   
			            <th data-options="field:'itemNote',width:100">检查部位</th>   
			            <th data-options="field:'days',width:100">草药付数</th>   
			            <th data-options="field:'intervaldays',width:100">间隔天数</th>   
			            <th data-options="field:'remark',width:100">备注</th>   
			            <th data-options="field:'isDrugShow',width:100,hidden:true">是否药品</th>   
			            <th data-options="field:'isDrugShowView',width:100">是否药品</th>   
			            <th data-options="field:'stackInfoNum',width:100">开立数量</th>   
			        </tr>   
			    </thead>   
			</table>
			<div style="text-align:center;padding:5px">
		    	<a href="javascript:adStackInfoSaveModelsubmit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
		    	<a href="javascript:adStackInfoSaveModelClose();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
		    </div>
		</div>
		<script type="text/javascript">
			var stackType = [{"id":1,"text":"药品组套","selected":true},{"id":2,"text":"非药品组套"},{"id":3,"text":"复合组套"}];//组套类型
			var stackSource = [{"id":1,"text":"全院"},{"id":2,"text":"科室"},{"id":3,"text":"医生"}];//组套来源
			/**
			* 判断选择的组套类来源
			**/
			function StackInfoSource(){
				var node = $('#adStackTree').tree('getSelected');
				if(node==null){
					$('#adStackInfoSaveModelSource').combobox('select',1);
				}else{
					var node1 = $('#adStackTree').tree('find', node.id);
					var nodeP = $('#adStackTree').tree('getRoot',node1.target);
					if(nodeP==null){
						$('#adStackInfoSaveModelSource').combobox('select',node.id);
					}else{
						$('#adStackInfoSaveModelSource').combobox('select',nodeP.id);
					}
				}
			}
			function adStackInfoSaveModelsubmit(){
				var stackType = $('#adStackInfoSaveModelStacktype').combobox('getValue');
				if(stackType==null||stackType==''){
					$.messager.alert('提示',"请选择组套类型!");
					return;
				}
				var stackName = $('#adStackInfoSaveModelName').textbox('getText');
				if(stackName==null||stackName==''){
					$.messager.alert('提示',"请填写组套名称!");
					return;
				}
				var stackSource = $('#adStackInfoSaveModelSource').combobox('getValue');
				if(stackSource==null||stackSource==''){
					$.messager.alert('提示',"请选择组套来源!");
					return;
				}
				var rows = $('#adStackInfoSaveModelDgId').datagrid('getRows');
				if(rows!=null&&rows.length>0){
					$.messager.confirm('提示信息', '确定保存组套信息？', function(r){
						if (r){							
							var stackInputCode = $('#adStackInfoSaveModelInputCode').textbox('getText');
							var stackRemark = $('#adStackInfoSaveModelRemark').textbox('getText');
							var stackFlag = 0;
							var node = $('#adStackTree').tree('getSelected');
							var parent = "";
							if(node==null){
								parent="ROOT";
							}else{
								var node1 = $('#adStackTree').tree('find', node.id);
								if(node1==null){
									parent="ROOT";
								}else{
									if(node1.id=="1"||node1.id=="2"||node1.id=="3"){
										parent="ROOT";
									}else{
										var nodeP = $('#adStackTree').tree('getParent',node1.target);
										if(nodeP.id=="1"||nodeP.id=="2"||nodeP.id=="3"){
											parent="ROOT";
										}else{
											parent=nodeP.id;
										}
									}
								}
							}
							if($('adStackInfoSaveModelShareFlag').is(':checked')){
								stackFlag = 1;
							}
							$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
					        $.post("<c:url value='/outpatient/advice/savaStackInfo.action'/>",
					        {"parent":parent,"stackInpmertype":1,"stackType":stackType,"stackName":stackName,"stackSource":stackSource,"stackInputCode":stackInputCode,"stackRemark":stackRemark,"stackFlag":stackFlag,"jsonData":JSON.stringify(rows)},
					        function(dataMap){
					        	$.messager.progress('close');	// 如果提交成功则隐藏进度条
					   			if(dataMap.resMsg=="error"){
					   				$.messager.alert('提示',dataMap.resCode);
					        	}else if(dataMap.resMsg=="success"){
					        		$.messager.alert('提示',dataMap.resCode);
					        		$('#adStackTree').tree('reload');
					        		adStackInfoSaveModelClose();
					        	}else{
					        		$.messager.alert('提示','未知错误,请联系管理员!');
					        	}
					   		});
						}else{
				        	return;
				        }
					});
				}else{
					$.messager.alert('提示',"组套信息为空,无法保存!");
					return;
				}
			}
			function adStackInfoSaveModelClose(){
				$('#adStackInfoSaveModelStacktype').combobox('select',1);
				$('#adStackInfoSaveModelName').textbox('clear');
				var node = $('#adStackTree').tree('getSelected');
				if(node==null){
					$('#adStackInfoSaveModelSource').combobox('select',1);
				}else{
					var node1 = $('#adStackTree').tree('find', node.id);
					var nodeP = $('#adStackTree').tree('getParent',node1.target);
				}
				$('#adStackInfoSaveModelInputCode').textbox('clear');
				$('#adStackInfoSaveModelRemark').textbox('clear');
				$('#adStackInfoSaveModelShareFlag').attr("checked",false);
				var rows = $('#adStackInfoSaveModelDgId').datagrid('getRows');
				var leng = rows.length;
				for(var i=0;i<leng;i++){
					$('#adStackInfoSaveModelDgId').datagrid('deleteRow',$('#adStackInfoSaveModelDgId').datagrid('getRowIndex',rows[0]));
				}
				$('#stackSaveModleDivId').dialog('close');
			}
		</script>
	</body>
</html>
