<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>医嘱类型维护编辑</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px; padding: 0px">
	<div class="easyui-panel" id = "panelEast" data-options="title:'编辑',iconCls:'icon-form',fit:true">
		<div style="padding:5px">
			<form id="editForm" method="post">
			<input type="hidden" name="kindInfo.id" value="${kindInfo.id }">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
					<tr>
						<td class="honry-lable">医嘱类别名称：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="typeName" name="kindInfo.typeName" value="${kindInfo.typeName }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">适用范围：</td>
		    			<td class="honry-info"><input id="fitExtent" name="kindInfo.fitExtent" style="width:200px"/>
		    			                       <input id="fitExtent1" value="${kindInfo.fitExtent }"  type="hidden" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">医嘱状态：</td>
		    			<td class="honry-info"><input id="decmpsState" name="kindInfo.decmpsState" style="width:200px"  />
		    			                       <input id="decmpsState1" value="${kindInfo.decmpsState }"  type="hidden" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">是否计费：</td>
		    			<td class="honry-info"><input id="chargeState" name="kindInfo.chargeState" style="width:200px;" />
									           <input id="chargeState1" value="${kindInfo.chargeState }" type="hidden" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">药房是否配药：</td>
		    			<td class="honry-info"><input id="needDrug" name="kindInfo.needDrug" style="width:200px" />
		    			                       <input id="needDrug1" value="${kindInfo.needDrug }"  type="hidden" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">是否打印执行单：</td>
		    			<td class="honry-info"><input id="prnExelist" name="kindInfo.prnExelist" style="width:200px" />
		    			                       <input id="prnExelist1" value="${kindInfo.prnExelist }"  type="hidden" /></td>
	    			</tr>
	    			<tr>
	    			<tr>
						<td class="honry-lable">是否需要确认：</td>
		    			<td class="honry-info"><input id="needConfirm" name="kindInfo.needConfirm" style="width:200px" />
		    			                       <input id="needConfirm1" value="${kindInfo.needConfirm }"  type="hidden" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">是否能开总量：</td>
		    			<td class="honry-info"><input id="totqtyFlag" name="kindInfo.totqtyFlag" style="width:200px" />
		    			                       <input id="totqtyFlag1" value="${kindInfo.totqtyFlag }"  type="hidden" /></td>
	    			</tr>
	    			
						<td class="honry-lable">是否打印医嘱单：</td>
		    			<td class="honry-info"><input id="prnMorlist" name="kindInfo.prnMorlist" style="width:200px" />
		    			                       <input id="prnMorlist1" value="${kindInfo.prnMorlist }"  type="hidden" /></td>
	    			</tr>
	    		
				</table>
				<div style="text-align:center;padding:5px">
					<c:if test="${empty kindInfo.id}">	
			    		<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
			    	</c:if>
			    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
			</form>
		</div>
	</div>
<script type="text/javascript">
//清除
function clear(){
	$('#editForm').form('clear');
}
//关闭
function closeLayout(){
	$('#divLayout').layout('remove','east');
}
$(function(){
	$('#chargeState').combobox({
		data:([{id: '1',value: '是'},{id: '0',value: '否'}]),
	    valueField:'id',    
	    textField:'value',
	    editable:false
	});
	var a= $('#chargeState1').val();
	$('#chargeState').combobox('setValue',a);
});


$(function(){
	$('#decmpsState').combobox({
		data:([{id: '1',value: '可以分解'},{id: '0',value: '不可以分解'}]),
	    valueField:'id',    
	    textField:'value',
	    editable:false
	});
	var a= $('#decmpsState1').val();
	$('#decmpsState').combobox('setValue',a);
});

$(function(){
	$('#needDrug').combobox({
		data:([{id: '1',value: '是'},{id: '0',value: '否'}]),
	    valueField:'id',    
	    textField:'value',
	    editable:false
	});
	var a= $('#needDrug1').val();
	$('#needDrug').combobox('setValue',a);
});

$(function(){
	$('#prnExelist').combobox({
		data:([{id: '1',value: '是'},{id: '0',value: '否'}]),
	    valueField:'id',    
	    textField:'value',
	    editable:false
	});
	var a= $('#prnExelist1').val();
	$('#prnExelist').combobox('setValue',a);
});

$(function(){
	$('#needConfirm').combobox({
		data:([{id: '1',value: '是'},{id: '0',value: '否'}]),
	    valueField:'id',    
	    textField:'value',
	    editable:false
	});
	var a= $('#needConfirm1').val();
	$('#needConfirm').combobox('setValue',a);
});

$(function(){
	$('#totqtyFlag').combobox({
		data:([{id: '1',value: '是'},{id: '0',value: '否'}]),
	    valueField:'id',    
	    textField:'value',
	    editable:false
	});
	var a= $('#totqtyFlag1').val();
	$('#totqtyFlag').combobox('setValue',a);
});



$(function(){
	$('#prnMorlist').combobox({
		data:([{id: '1',value: '是'},{id: '0',value: '否'}]),
	    valueField:'id',    
	    textField:'value',
	    editable:false
	});
	var a= $('#prnMorlist1').val();
	$('#prnMorlist').combobox('setValue',a);
});

$(function(){
	$('#fitExtent').combobox({
		data:([{id: '1',value: '门诊'},{id: '2',value: '住院'},{id: '3',value: '全院'}]),
	    valueField:'id',    
	    textField:'value',
	    editable:false
	});
	var a= $('#fitExtent1').val();
	$('#fitExtent').combobox('setValue',a);
});


function submit(flg) {
	$('#editForm').form('submit', {
		url:"<%=basePath %>baseinfo/kindInfo/saveInpatientKind.action",
		onSubmit : function() {
			return $(this).form('validate');
		},
		success : function(data) {
			if (flg == 0) {
				$.messager.alert('提示',"保存成功");
				$('#divLayout').layout('remove', 'east');
				//实现刷新
				$("#list").datagrid("reload");
			} else if (flg == 1) {
				//清除editForm
				$('#editForm').form('reset');
				//实现刷新
				$("#list").datagrid("reload");	
			}
		},
		error : function(date) {
			$.messager.alert('提示',"保存失败");
		}
	});
}
</script>
</body>
</html>