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
</head>

	<body>
		<div id="panelEast" class="easyui-layout" fit=true style="min-width:570px;width:auto;">
			<div data-options="region:'north',split:false,title:'财务组信息',iconCls:'icon-book',border:false" style="padding:5px;width:550px;min-height:190px;height:28%;">
				<br>
				<form id="financeGroup" method="post">
					<input type="hidden" id="id" name="id" value="${financeUsergroup.id }">
					<input type="hidden" id="no" name="no" value="${financeUsergroup.no }">
					<input type="hidden" id="ids" name="ids">
					<input type="hidden" id="oldGroupName" name="oldGroupName">
		    		<table  class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width:550px;">
						<tr>
							<td >分组名称:</td>
			    			<td >
			    				<input class="easyui-textbox" id="groupName" name="financeUsergroup.groupName" value="${financeUsergroup.groupName}" onblur="validCheck()"; style="width:200px" data-options="required:true" missingMessage="请输入分组名称"/>
			    				<div id="groupMessage" style="font-size: 12px; color: #FF0000; float: right;width: 200px"></div>
			    			</td>
		    			</tr>
		    			<tr>
							<td >自定义码 :</td>
							<td ><input class="easyui-textbox"  name="financeUsergroup.groupInputcode" value="${financeUsergroup.groupInputcode}" style="width:200px" /></td>
						</tr>
						<tr>					
							<td >备注:</td>
			    			<td ><input class="easyui-textbox"  name="financeUsergroup.stackRemark" value="${financeUsergroup.stackRemark}" style="width:200px;height: 50px"/></td>
		    			</tr>
					</table>
				</form>
			</div>
		<div data-options="region:'center',split:false,iconCls:'icon-book',border:false" style="margin-top:6px;height:70%;">
			<table id="yxygList" class="easyui-datagrid" title="已选员工信息" style="width:97%;height:92%;"
					data-options="
						singleSelect: true,
						toolbar: '#tb',
						method:'post',
						rownumbers:true,
						striped:true,
						border:false,
						checkOnSelect:true,
						selectOnCheck:false,
						fitColumns:false,
						fit:true
					">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true" ></th>
						<th data-options="field:'jobNo',width:100,align:'center',editor:'textbox'">工作号</th>
						<th data-options="field:'deptName',width:100,align:'center',editor:'textbox'">部门</th>
						<th data-options="field:'name',width:100,align:'right',editor:'textbox'">员工姓名</th>
						<th data-options="field:'id',width:100,align:'right',editor:'textbox',hidden:true">员工Id</th>
						<th data-options="field:'flag',width:100,align:'right',hidden:true">员工Id</th>
					</tr>
				</thead>
			</table>
			<div id="tb" style="height:auto">
				<a href="javascript:void(0)" onclick="addEmployee()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="removeit()">删除</a>
			</div>
			 <div style="text-align:center;padding:5px">
			 	<a href="javascript:addFinanceGroup();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保&nbsp;存&nbsp;</a>
			    &nbsp;<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关&nbsp;闭&nbsp;</a>
			</div>
		</div>
		
	</div>
	<script>
		var isClickOk=true;
		//当光标移开焦点的时候进行重复验证  
	    function validCheck(){ 
	    	var groupName = encodeURIComponent(encodeURIComponent($('#groupName').val()));
		    jQuery.ajax({   //使用Ajax异步验证组名是否重复  
		    type : "post",  
		    url : "<%=basePath %>finance/userGroup/queryExistGroupName.action",  
		    data:'financeUsergroup.groupName='+groupName,
		    dataType:'json',  
		    success : function(data){    
		           if($('#groupName').val()==""){//当为空都不显示，因为Easyui的Validate里面已经自动方法限制  
		              
		           }  
		           else if( data == "yes" ){ //当返回值为yes，表示在数据库中没有找到重复的组名  
		        	   $("#groupMessage").empty();
		        	}  
		           else {  
			            if($('#oldGroupName').val()==$('#groupName').val()){
			            	isClickOk=true;
			            	$("#groupMessage").empty(); 
			            }else{
			            	isClickOk=false;
				            $("#groupMessage").empty();  
				            $("#groupMessage").append("组名已被使用");//在id为groupMessage里面加载“组名已被使用”四个字 
			            }
		          }  
		        }  
			});  
		};  
		$(function(){			
			var groupName = $('#groupName').val();
			if(groupName!=""){
		    	$('#yxygList').datagrid({
					url:'<%=basePath %>finance/userGroup/queryEmployeeGroup.action?financeUsergroup.groupName='+encodeURIComponent(encodeURIComponent(groupName))
				});										
				$('#yxygList').datagrid('reload');
				$('#oldGroupName').val(groupName);
			}
		});
		//添加员工
		function addEmployee(){	
			if(isClickOk==false){//当组名重复被设置为false，如果为false就不提交，显示提示框信息不能重复  
                $.messager.alert('操作提示', '组名已被使用，不能添加员工！','error');  
                setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
                return;  
            }
			AddDeptdilogs("员工", "<%=basePath%>finance/userGroup/queryRoleUser.action");		
		}
		function AddDeptdilogs(title, url) {
			$('#roleaddUserdiv').dialog({    
				   title: title,    
				   width: '425px',    
				   height:'85%',
				   left:'650',
				   top:'20',
				   closed: false,    
				   cache: false,    
				   href: url,    
				   modal: true   
			});    
		}		
		//移除员工
		function removeit(index){
			var id=$('#id').val();
			var rows=$('#yxygList').datagrid('getChecked');
			var ids=''
			for(var i=0;i<rows.length;i++){	
				if(rows[i].flag==1){
					$('#yxygList').datagrid('deleteRow',$('#yxygList').datagrid('getRowIndex',rows[i]));
				}else{
					if(ids!=''){
						ids = ids+','+rows[i].id;
					}else{
						ids = rows[i].id;
					}
				}			
			} 
			if(ids!=''){
				$.ajax({
					url: '<%=basePath %>finance/userGroup/deleteGroupEmployee.action?financeUsergroup.id='+id+'&financeUsergroup.employee.id='+ids,
					type:'post',
					success: function() {
						$.messager.alert('操作提示', '删除成功！'); 
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						var groupName=$('#groupName').val();
						$('#yxygList').datagrid('reload');
					}
				});	
			}
			$('#yxygList').datagrid('uncheckAll');
			$('#yxygList').datagrid('unselectAll');
		}	
		
		//关闭页面
		function closeLayout(){			
			$('#divLayout').layout('remove','east');			
		}
		//添加财务组
		function addFinanceGroup(){
			var inserted = $('#yxygList').datagrid('getChanges', "inserted");
			var ids = '';
			for(var i=0; i<inserted.length; i++){
				if(ids!=''){
					ids += ',';
				}
				ids += inserted[i].id;
			};
			$('#ids').val(ids);
			$('#financeGroup').form('submit', {    
			    url:'<%=basePath %>finance/userGroup/saveOrupdateUserGroup.action',   
			    onSubmit: function(){
			    	if(isClickOk==false){//当组名重复被设置为false，如果为false就不提交，显示提示框信息不能重复  
                        $.messager.alert('操作提示', '组名已被使用，不能添加财务组！','error'); 
                        setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
                        return false;  
                    }
			    	if (!$('#financeGroup').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
			    	$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
			    },    
			    success:function(data){ 
			    	$.messager.progress('close');	// 如果提交成功则隐藏进度条
			    	var groupName=$('#groupName').val();
			    	$.messager.alert('操作提示', '保存成功！');
			    	setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
			    	$('#divLayout').layout('remove','east');		    										
					$('#list').datagrid('reload');
			    }    
			});
		}
		
	</script>
	</body>
</html>