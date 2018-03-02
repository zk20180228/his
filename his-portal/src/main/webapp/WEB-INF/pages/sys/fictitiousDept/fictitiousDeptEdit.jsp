<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<body>
	<form id="treeEditForm" method="post" >
		<div title="Tab1" style="padding: 20px;">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<input type="hidden" id="id" name="id" value="${id }">
				<input type="hidden" name="fictCode" value="${fictCode }">
				<input type="hidden" name="fictName" value="${fictName }">
				<tr align="center">
	    			<td class="honry-lable">科室名称:</td>
	    			<td><input class="easyui-combobox" type="text" id="deptCode" name="deptCode" value="${contact.deptCode }" data-options="required:true" style="width:290px" missingMessage="请输入名称"/></td>
	    			<input type="hidden" id="deptName" name="deptName">
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">科室类型:</td>
	    			<td><input class="easyui-combobox" type="text" id="deptType" name="deptType" value="${deptType }" data-options="required:true,readonly:true,disabled:true" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">排序:</td>
	    			<td><input  type="text" id="deptOrder" name="deptOrder" value="${contact.fictOrder }"  style="width:290px" missingMessage="请输入名称" /></td>
	    		</tr>
	    		<tr>
						<td class="honry-lable">
							备注:
						</td>
						<td class="honry-info" >&nbsp;
							<textarea class="easyui-validatebox" rows="4" cols="38" id="deptRemark" name="deptRemark"
								data-options="multiline:true" align="center" style="border-radius:3px;border:1px solid #0aa2a1;">${contact.mark }</textarea>
						</td>
					</tr>
			</table>	
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="submitTreeForm()" class="easyui-linkbutton">确定</a>
				<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeEast()" class="easyui-linkbutton">关闭</a>
			</div>
		</div>		
	</form>
	<script type="text/javascript">
	var type="${deptType}";
	var order="${contact.fictOrder }";
	$(function() {
		$('#deptType').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=depttype",
			valueField : 'encode',
			textField : 'name',
			multiple : false,
		});
		$('#deptCode').combobox({
			url: "<c:url value='/baseinfo/fictitiousDept/queryDeptForType.action'/>?deptType="+type,
			valueField: 'deptCode',
			textField: 'deptName',
			multiple:false,
			onSelect:function(record){
				$('#deptName').val(record.deptName);
			}
		});
	});
	$('#deptOrder').spinner({    
	    required:true,
	    min:1,
	    max:10000,
	    value:1,
	    increment:1,
	    onSpinUp:function(){
	    	var val=parseInt($('#deptOrder').spinner('getValue'));
	    	$('#deptOrder').spinner('setValue',val+1);	
	    },
	    onSpinDown:function(){
	    	var val=parseInt($('#deptOrder').spinner('getValue'));
	    	if(val>1){
	    		$('#deptOrder').spinner('setValue',val-1);
	    	}
	    }
	});
	$('#deptOrder').spinner('setValue',order);
	//提交验证
	function submitTreeForm(){
		$.messager.progress({text:'查询中，请稍后...',modal:true});
		$('#treeEditForm').form('submit', {
			url:"<c:url value='/baseinfo/fictitiousDept/saveOrUpdateFititiousContext.action'/>",
			data:$('#treeEditForm').serialize(),
	        dataType:'json',
			onSubmit : function() {
				if(!$('#treeEditForm').form('validate')){
					$.messager.progress('close');
// 					$.messager.show({  
// 				         title:'提示信息' ,   
// 				         msg:'验证没有通过,不能提交表单!'  
// 				    }); 
					$.messager.alert('提示信息','验证没有通过,不能提交表单!','warning');
					close_alert();
				    return false ;
				}
			},
			success : function(data) {
				$.messager.progress('close');
				if(data=='true'){
					$.messager.alert('提示','保存成功！');
					closeEast();
					$("#list").datagrid("reload");
				}else{
					$.messager.alert('提示','保存失败,重新选择科室');
				}
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示','操作失败！');	
			}
		}); 
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>