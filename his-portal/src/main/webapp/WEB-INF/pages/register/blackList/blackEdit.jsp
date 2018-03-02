<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript">
var sexMap=new Map();
//性别渲染
	$.ajax({
	url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
	data:{"type":"sex"},
	type:'post',
	success: function(data) {
		var v = data;
		for(var i=0;i<v.length;i++){
			sexMap.put(v[i].encode,v[i].name);
		}
	}
});


</script>
</head>
<body>
    <div class="easyui-panel"  id="panelEast" data-options="iconCls:'icon-form'" style="width:570px">
		<div style="padding:10px;border:0">
			<form id="editForm" method="post" >
				<input type="hidden" id="id" name="id" value="${blacklist.id }">
				<input type="hidden" id="createUser" name="createUser" value="${blacklist.createUser }">
				<input type="hidden" id="createDept" name="createDept" value="${blacklist.createDept }">
				<input type="hidden" id="createTime" name="createTime" value="${blacklist.createTime }">
		    	<table class="honry-table" cellpadding="1" cellspacing="1" border="0">
					<tr>
						<td class="honry-lable">员工姓名：</td>
		    			<td>
			    	       	<input id="ry" name="dmployeeId.name" data-options="required:true" value="${blacklist.dmployeeId.name}"> 
			    	       	<input id="dmployeeName" type="hidden"  value="${blacklist.dmployeeId.jobNo }">
			    	       	<input id="dmployeeid" type="hidden" name="dmployeeId.id" value="${blacklist.dmployeeId.id }">
			    			<a href="javascript:delSelectedData('ry');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
			    		</td>
			    		
			    	</tr>
					<tr>
						<td class="honry-lable">原因：</td>
		    			<td class="honry-info">
		    			<textarea class="easyui-validatebox" rows="3" cols="30" id="resaon" name="resaon" data-options="multiline:true" >${blacklist.resaon }</textarea>
	    			</tr>
		    	</table>
		    	<div style="text-align:center;padding:5px">
			    	<c:if test="${blacklist.id==null }">
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
	var sexMap=new Map();
	//性别渲染
		$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});

	//黑名单人员下拉选择
	$('#ry').combogrid({ 
		editable:true,
	    panelWidth:450,    
	    idField:'jobNo',    
	    textField:'name', 
	    mode:'remote',
	    url : "<c:url value='/outpatient/blacklist/empCombo.action'/>", 
	    columns:[[    
	        {field:'jobNo',title:'工作号',width:60},    
	        {field:'name',title:'姓名',width:100},
	        {field:'oldName',title:'曾用名',width:100},
	        {field:'deptId',title:'部门名称',width:100,formatter: function(value,row,index){
																			if (row.deptId){
																				return row.deptId.deptName;
																			} else {
																				return value;
																			}
																		}},
	        {field:'sex',title:'性别',width:100,formatter:function (value,row,index){return sexMap.get(value);}} ,
	        {field:'type',title:'员工类型',width:100,formatter:formatEmpType},
	        {field:'post',title:'职务',width:100,formatter:dutiesFamater}, 
	        {field:'title',title:'职称',width:100,formatter:titleFamater},
	          
	    ]],
	    onSelect:function(index,row){
	    	$('#dmployeeid').val(row.id);
	    }
	});  
// 	bindEnterEvent('ry',popWinToEmployee,'easyui');//绑定回车事件
	//员工类型 
	var typeList = null;
		$.ajax({ 
		      url :"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action" ,  
			    type : "post",  
				data:{"type":"empType"},
				success: function(list) { 
					typeList=list;
			    }
		});	
		function formatEmpType(value, row, index){
			if (value != null) {
				for ( var i = 0; i < typeList.length; i++) {
					if (value == typeList[i].encode) {
						return typeList[i].name; 
					}
				}
			}
		}
	//获得职务集合
	var dutiesList=null;
	$.ajax({
		url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
		data:{"type" : "duties"},
		type:'post',
		success: function(dutiesData) {
			dutiesList =  dutiesData ;
		}
	});
	//显示职务格式化
	function dutiesFamater(value){
		if(value!=null){
			for(var i=0;i<dutiesList.length;i++){
				if(value==dutiesList[i].encode){
					return dutiesList[i].name;
				}
			}	
		}
	}
	//获得职称集合
	var titleList=null;
	$.ajax({
		url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
		data:{"type" : "title"},
		type:'post',
		success: function(titleData) {
			titleList =  titleData ;
		}
	});
	//显示职称格式化
	function titleFamater(value){
		var retVal = "";
		if(value!=null){
			for(var i=0;i<titleList.length;i++){
				if(value==titleList[i].encode){
					retVal = titleList[i].name;
					break;
				}
			}	
		}
		return retVal;
	}
	var dmployeeName = $('#dmployeeName').val();
	if (dmployeeName != null && dmployeeName != "") {
		$('#ry').combogrid('setValue', dmployeeName);
	}
	
	//提交验证
	function submit(flg) {
		$('#editForm').form('submit', {
			url :"<c:url value= '/outpatient/blacklist/editInfo.action'/>",
			data : $('#editForm').serialize(),
			dataType : 'json',
			onSubmit : function() {
				if (!$('#editForm').form('validate')) {
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					return false;
				}
				var t='${blacklist.id}';
				if(t=="" || t==null){
					var list=$("#list").datagrid("getRows");
					var id=$("#dmployeeid").val();
					for (var i = 0; i < list.length; i++) {
						if (id==list[i].dmployeeId.id) {
							$.messager.alert('提示',"该用户已存在！");
							return false;
						}
					}
				}
			},
			success : function(data) {
				if(flg==0){
					$.messager.alert('提示',"保存成功");
				    //实现刷新
		          	$("#list").datagrid("reload");
		          	$('#divLayout').layout('remove','east');
			   }else if(flg==1){
					//清除editForm
					$('#editForm').form('reset');
					$('#list').datagrid('reload');
			  	}						
						
			},
			error : function(data) {
				if(flg==1){
					$('#ry').combogrid({    
					    panelWidth:450,    
					    idField:'jobNo',    
					    textField:'name',    
					    url : "<c:url value='/outpatient/blacklist/empCombo.action'/>", 
					    columns:[[    
					        {field:'jobNo',title:'工作号',width:60},    
					        {field:'name',title:'姓名',width:100},
					        {field:'oldName',title:'曾用名',width:100},
					        {field:'deptId',title:'部门名称',width:100,formatter: function(value,row,index){
																							if (row.deptId){
																								return row.deptId.deptName;
																							} else {
																								return value;
																							}
																						}},
					        {field:'sex',title:'性别',width:100,formatter:function (value,row,index){return sexMap.get(value);}} , 
					        {field:'type',title:'员工类型',width:100,formatter:formatEmpType},
					        {field:'post',title:'职务',width:100,formatter:dutiesFamater}, 
					        {field:'title',title:'职称',width:100,formatter:titleFamater},
					          
					    ]],
					onChange:function(str){
		    			$('#ry').combogrid('reload', "<c:url value='/baseinfo/employee/employeeCombobox.action'/>?str="+str);
		    		}
			    
				   });
				}	
		    $.messager.alert('提示',"保存失败");
	     }
	  });
	}
	
	//清除所填信息
	function clear() {
		$('#editForm').form('reset');
	}
	function closeLayout() {
		$('#divLayout').layout('remove', 'east');
	}

	function onclickBox(id) {
		if ($('#' + id).is(':checked')) {
			$('#' + id + 'Hidden').val(1);
		} else {
			$('#' + id + 'Hidden').val(0);
		}
	}
	/**
	   * 回车弹出挂号员黑名单弹框
	   * @author  zhuxiaolu
	   * @param textId 页面上commbox的的id
	   * @date 2016-03-22 14:30   
	   * @version 1.0
	   */
	   function popWinToEmployee(){
		    var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=ry&employeeType=3";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-
			170)+',scrollbars,resizable=yes,toolbar=yes')
		
	 }
</script>
</body>
</html>