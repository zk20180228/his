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
	<div class="easyui-panel" id = "panelEast" data-options="title:'服务管理编辑',iconCls:'icon-form',border:false,fit:true" style="width:580px">
		<div style="padding:10px">
			<form id="editForm" method="post">
				<input type="hidden" name="serviceManagement.id" value="${serviceManagement.id }" >
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
					<tr>
						<td class="honry-lable">服务编码:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="code" name="serviceManagement.code" value="${serviceManagement.code }" data-options="required:true"  style="width:200px" /></td>
	    			</tr>
					<tr>
						<td class="honry-lable">服务名称:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="name" name="serviceManagement.name" value="${serviceManagement.name }" data-options="required:true"  style="width:200px" /></td>
	    			</tr>
					<tr>
						<td class="honry-lable">服务类型:</td>
		    			<td class="honry-info"><input class="easyui-combobox" id="type" name="serviceManagement.type" value="${serviceManagement.type }" data-options="required:true,editable:false" style="width:200px"/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">IP:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="ip" name="serviceManagement.ip" value="${serviceManagement.ip }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">占用端口:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="port" name="serviceManagement.port" value="${serviceManagement.port }" data-options="required:true" style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">心跳频率:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="heartRate" name="serviceManagement.heartRate" value="${serviceManagement.heartRate }" data-options="required:true" style="width:200px;"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">心跳频率单位:</td>
		    			<td class="honry-info"><input class="easyui-combobox" id="heartUnit" name="serviceManagement.heartUnit" value="${serviceManagement.heartUnit }" data-options="required:true,editable:false" style="width:200px;"/></td>
	    			</tr>
	    			<tr >
						<td class="honry-lable">最新心跳时间:</td>
						<td class="honry-info"><input id="heartNewtime" name="serviceManagement.heartNewtime" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-{%d+7}'})"value="${heartNewtime }" data-options="required:true" style="width:200px;"/></td>
					</tr>
	    			<tr>
						<td class="honry-lable">状态:</td>
		    			<td class="honry-info"><input class="easyui-combobox" id="state" name="serviceManagement.state" value="${serviceManagement.state }" data-options="required:true,editable:false" style="width:200px;" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">服务状态:</td>
		    			<td class="honry-info"><input class="easyui-combobox" id="masterprePare" name="serviceManagement.masterprePare" value="${serviceManagement.masterprePare }" data-options="required:true,editable:false" style="width:200px;" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">备注:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="remarks" name="serviceManagement.remarks" value="${serviceManagement.remarks }" style="width:200px;height:60px;" data-options="multiline:true"/></td>
	    			</tr>
				</table>
				</form>
				<div style="text-align:center;padding:5px">
<%-- 				<c:if test="${serviceManagement.id==null }"> --%>
<!-- 			    	<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a> -->
<%-- 			    </c:if> --%>
			    	<a href="javascript:submitForm();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
		</div>
	</div>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<script type="text/javascript">
$(function(){
	//服务类型
	$("#type").combobox({
		valueField: 'id',
		textField: 'value',
		data:[
		{id:'0',value:'同步服务'},
		{id:'1',value:'接口服务'},
		]
	   });
	//心跳频率单位
	$("#heartUnit").combobox({
		valueField: 'id',
		textField: 'value',
		data:[
		{id:'S',value:'秒'},
		{id:'M',value:'分'},
		{id:'H',value:'时'},
		{id:'D',value:'天'},
		{id:'W',value:'周'},
		]
	   });
	//状态
	$("#state").combobox({
		valueField: 'id',
		textField: 'value',
		data:[
		{id:'0',value:'正常'},
		{id:'1',value:'停用'}
		]
	   });
	$("#masterprePare").combobox({
		valueField: 'id',
		textField: 'value',
		data:[
		{id:'1',value:'主'},
		{id:'2',value:'备'}
		]
	   });
});

/**
 * 保存
 * @author wangshujuan
 * @date 2017-9-19 16:53
 * @version 1.0
 */
function submitForm(){ 
      	$('#editForm').form('submit',{  
       	url:"<%=basePath %>migrate/ServiceManagement/saveServiceManagement.action",  
       	onSubmit:function(){
       	 if($(this).form('validate')){
    		 $.messager.progress({text:'保存中，请稍后...',modal:true});
    		 return true;
    	 }else{
    		 $.messager.alert('提示','请填写完整信息');
    		 return false;
    	 	}
		},  
        success:function(data){  
        	$.messager.progress('close');
        	var res = eval('('+data+')');
        	if(res.resCode == 'success'){
        		$.messager.alert('提示','保存成功！');
	        	$('#list').datagrid('reload');
	        	$('#divLayout').layout('remove','east');
        	}else{
				$.messager.alert('提示',res.resMsg);	
        	}
        },
  		}); 
   }
   /**
	 * 连续添加
	 * @author wangshujuan
	 * @date 2017-9-19 16:53
	 * @version 1.0
	 */
//    function addContinue(){
//    	$('#editForm').form('submit',{  
<%--    		url:"<%=basePath %>migrate/ServiceManagement/saveServiceManagement.action",  --%>
//    		onSubmit:function(){
//           	 if($(this).form('validate')){
//        		 $.messager.progress({text:'保存中，请稍后...',modal:true});
//        		 return true;
//        	 }else{
//        		 $.messager.alert('提示','请填写完整信息','info');
//        		 return false;
//        	 	}
//    		},  
//         success:function(data){  
//         	var res = jQuery.parseJSON(data);
//         	$.messager.progress('close');
//         	if(res.resCode == "success"){
//         		//实现刷新栏目中的数据
//                    $('#list').datagrid('reload');
//                    $('#editForm').form('reset');
//         	}else if(res.resCode == "repeat"){
//         		$('#name').val("");
// 				$("#name").focus();
// 				close_alert();
//         	} 
//         	$.messager.alert('提示',res.resMsg);
//         },
// 		error : function(data) {
// 			$.messager.progress('close');
// 			$.messager.alert('提示','保存失败！');	
// 		}							         
//   	  }); 
//    }
   /**
 * 清除页面填写信息
 * @author wangshujuan
 * @date 2017-9-19 16:53
 * @version 1.0
 */
	function clear(){
		$('#editForm').form('clear');
	}
   //关闭页面
	function closeLayout(){
		$('#divLayout').layout('remove','east');
		$("#list").datagrid("reload");
	}
		
	</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>