<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>">
	<title>添加页面</title>
</head>
	<body style="margin: 0px; padding:0px">
		<div id="p" class="easyui-panel" title="添加" style="padding: 5px; background: #fafafa;" data-options="fit:'true',border:true">
			<form id="hospitalForm" action="" method="post">
				<input type="hidden" id="id" name="cpVariation.id" value="${cpVariation.id }">
				<input type="hidden" id="createUser" name="cpVariation.createUser" value="${cpVariation.createUser }">
				<input type="hidden" id="createTime" name="cpVariation.createTime" value="${cpVariation.createTime }">
				<input type="hidden" id="inpatientNo" name="cpVariation.inpatientNo" value="${cpVariation.inpatientNo }">
				<input type="hidden" id="medicalrecordId" name="cpVariation.medicalrecordId" value="${cpVariation.medicalrecordId }">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
					<tr>
						<td class="honry-lable">
							<span>变异阶段:</span>
						</td>
						<td class="honry-view">
							<input class="easyui-combobox" id="stageId"
									name="cpVariation.stageId"
									missingMessage="请选择变异阶段" 
									editable="false"
									style="width: 200px" 
									value="${cpVariation.stageId }"
									/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>变异名称:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-combobox" id="variationName"
									missingMessage="请选择变异名称" 
									editable="false"
									data-options="required:true"
									value="${cpVariation.variationCode }"
									style="width: 200px" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>变异代码:</span>&nbsp;&nbsp;
						</td>
						<td class="honry-view">
							<input id="variationCode" class="easyui-textbox" 
							data-options="editable:false" 
							style="width: 200px" 
							value="${cpVariation.variationCode }" name="cpVariation.variationCode">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>变异因素:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-combobox" id="variationFactorCode"
									name="cpVariation.variationFactorCode"
									missingMessage="请选择床位编制" 
									editable="false"
									data-options="required:true"
									value="${cpVariation.variationFactorCode }"
									style="width: 200px" />
						</td>
					</tr>
					<tr >
						<td class="honry-lable">
							<span>变异时间:</span>&nbsp;&nbsp;
						</td>
						<td >
							<input  id="variationDate" name="cpVariation.variationDate" 
							class="Wdate" type="text" value="${Etime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" 			
							data-options="required:true"
							value="${cpVariation.variationDate }"
							style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;">  
						</td>
					</tr>
					
					<tr>
						<td class="honry-lable">
							<span>变异方向:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-combobox" id="variationDirection"
									name="cpVariation.variationDirection"
									missingMessage="请选择变异方向" 
									data-options="valueField: 'value',textField: 'label',data: [{
						                   label: '正变异',
						                   value: '1',selected:true},
						                   {label: '反变异',
						                   value: '2'},]" 
									editable="false"
									value="${cpVariation.variationDirection }"
									style="width: 200px"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>变异原因:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input class="easyui-textbox" id="variationReason"
									name="cpVariation.variationReason"
									data-options="multiline:true,height:100,width:300"
									value="${cpVariation.variationReason }"
									style="width: 200px"/>
						</td>
					</tr>
				</table>
			</form>
			<div style="text-align: center; padding: 5px">
				<a id="hospitalOKbtn" href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="onClickOKbtn()" class="easyui-linkbutton">确定</a>
				<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeLayout()" class="easyui-linkbutton">关闭</a>
			</div>
		</div>
	<script type="text/javascript">
    var cpId = '${cpId}';
    var versionNo = '${versionNo}';
	$(function(){
		var time ="${cpVariation.variationDate }";
		var Ctime = "${cpVariation.createTime }";
		$('#createTime').val(Ctime.substring(0,19))
		$('#variationDate').val(time.substring(0,19))
		//床位状态
		$('#variationName').combobox({    
			url:  "<%=basePath%>inpatient/variationInfo/queryDictionary.action?type=variationName",
			valueField : 'code',
			textField : 'name',
			onSelect:function(record){
				$('#variationCode').textbox('setValue',record.code);
			}
		});
		//床位编制
		$('#variationFactorCode').combobox({    
			url:"<%=basePath%>inpatient/variationInfo/queryDictionary.action?type=variationFactorCode",
			valueField:'code',    
			textField:'name'
		});
		//床位编制
		$('#stageId').combobox({    
			url:"<%=basePath%>inpatient/variationInfo/queryStageId.action?cpId="+cpId+"&versionNo="+versionNo,
			valueField:'code',    
			textField:'name'
		});
	});
	
	function onClickOKbtn() {
		var variationDate = $('#variationDate').val();
		if(variationDate!=''&&variationDate!=null){
			$('#hospitalForm').form('submit', {
				url : "<%=basePath%>inpatient/variationInfo/saveOrUpdate.action",
				data : $('#hospitalForm').serialize(),
				dataType : 'json',
				onSubmit : function() {
					if (!$('#hospitalForm').form('validate')) {
						$.messager.progress('close');
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						$.messager.alert('提示信息','验证没有通过,不能提交表单!','warning');
						close_alert();
						return false;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});
				},
				success : function(data) {
					$.messager.progress('close');
					if(data=='true'){
						$.messager.alert("提示","保存成功!");
 						setTimeout(function(){
	  						$(".messager-body").window('close');
	  					},3500);
						closeLayout()
 						reload();
					}else{
						$.messager.alert('提示','保存失败');
					}
				},
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert('提示','保存失败！');
				}
			});
		}else{
			$.messager.alert('提示信息','请选择变异时间!','warning');
			close_alert();
		}
	}
	//清除所填信息
	function clear() {
		$('#hospitalForm').form('clear');
	}
</script>
	</body>
</html>
