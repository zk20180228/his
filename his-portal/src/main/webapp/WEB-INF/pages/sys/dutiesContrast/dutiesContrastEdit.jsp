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
	<div class="easyui-panel" id = "panelEast" data-options="title:'编辑',iconCls:'icon-form',border: false" >
		<div style="padding:5px">
			<form id="editForm" method="post">
			<input type="hidden" id="id" name="dutiesContrast.id" value="${dutiesContrast.id}">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;">
	    			<tr>
						<td class="honry-lable">职务类型名称：</td>
		    			<td class="honry-info"><input  id="belongLevelName" name="dutiesContrast.belongLevelName" value="${dutiesContrast.belongLevelName}" data-options="required:true" style="width:200px"/>
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">职务类型代码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="belongLevel" name="dutiesContrast.belongLevel" value="${dutiesContrast.belongLevel}" readonly="readonly" data-options="required:true" style="width:200px" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">职务名称：</td>
		    			<td class="honry-info"><input  id="dutiesName" name="dutiesContrast.dutiesName" value="${dutiesContrast.dutiesName }" data-options="required:true" style="width:200px" />
	    			</tr>
	    			<tr>
						<td class="honry-lable">职务代码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="dutiesCode" name="dutiesContrast.dutiesCode" value="${dutiesContrast.dutiesCode}" readonly="readonly" data-options="required:true" style="width:200px" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">职务等级：</td>
		    			<td class="honry-info"><input class="easyui-combobox" id="dutiesLevel" name="dutiesContrast.dutiesLevel" value="${dutiesContrast.dutiesLevel }" data-options="required:true,
						valueField: 'id',
						textField: 'value',
						data: [
						{id: '1',value: '一级'},
						{id: '2',value: '二级'},
						{id: '3',value: '三级'},
						{id: '4',value: '四级'}
						]"  style="width:200px" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">自定义码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="dutiesInputCode" name="dutiesContrast.dutiesInputCode" value="${dutiesContrast.dutiesInputCode}"  style="width:200px" /></td>
	    			</tr>
				</table>
				<div style="text-align:center;padding:5px">
					<c:if test="${dutiesContrast.id==null }">
			    		<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
			    	</c:if>
			    	<a href="javascript:submit(2);void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout('edit')">关闭</a>
			    </div>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<script type="text/javascript">

//职务类型
$('#belongLevelName').combobox({    
	url:"<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action", 
	queryParams:{type:'dutiesType'},
    valueField:'name',    
    textField:'name',
    onHidePanel:function(none){
  	    var data = $(this).combobox('getData');
  	    var val = $(this).combobox('getValue');
  	    var result = true;
  	    for (var i = 0; i < data.length; i++) {
  	        if (val == data[i].name) {
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
  	onSelect:function(record){
    	$('#belongLevel').textbox('setValue',record.encode);
    },
}); 


//职务名称
$('#dutiesName').combobox({    
	url:"<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action", 
	queryParams:{type:'duties'},
    valueField:'name',    
    textField:'name',
    onSelect:function(record){
    	$('#dutiesCode').textbox('setValue',record.encode);
    },
    onHidePanel:function(none){
	  	    var data = $(this).combobox('getData');
	  	    var val = $(this).combobox('getValue');
	  	    var result = true;
	  	    for (var i = 0; i < data.length; i++) {
	  	        if (val == data[i].name) {
	  	            result = false;
	  	        }
	  	    }
	  	    if (result) {
	  	        $(this).combobox("clear");
	  	    }else{
	  	        $(this).combobox('unselect',val);
	  	        $(this).combobox('select',val);
	  	    }
	  	}
});

function submit(flag){ 
	$('#editForm').form('submit',{  
    	url:"<%=basePath %>baseinfo/dutiesContrast/saveDutiesContrast.action",  
    	onSubmit:function(){
			if (!$('#editForm').form('validate')) {
				$.messager.show({
					title : '提示信息',
					msg : '验证没有通过,不能提交表单!'
				});
				return false;
			}
			$.messager.progress({text:'保存中，请稍后...',modal:true});
    	},  
        success:function(data){ 
        	$.messager.progress('close');
        	var res = eval("(" + data + ")");
        	if(flag == 1){
				clear();
				$('#list').datagrid('reload');
			}else if(flag == 2){
				closeLayout('edit');
				$.messager.alert('提示',res.resMsg);
				close_alert();
			}
        }
	}); 
}
//清除所填信息
function clear(){
	var id = '${dutiesContrast.id}';
	$('#editForm').form('reset');
	if(id){
		$('#id').val(id);
	}
}
/* 
* 关闭界面
*/
function closeLayout(flag){
	$('#divLayout').layout('remove','east');
	if(flag == 'edit'){
		$('#list').datagrid('reload');
	}
}
</script>
</body>
</html>