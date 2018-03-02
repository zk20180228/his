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
<style type="text/css">
	.tableCss{
		border-collapse: collapse;border-spacing: 0;border: 1px solid #95b8e7;width:100%;
	}
	.tableCss .TDlabel{
		text-align: right;
		font-size:14px;
	}
	.tableCss td{
		border: 1px solid #95b8e7;
		padding: 5px 5px;
		word-break: keep-all;
		white-space:nowrap;
	}
	.easyuiInput{
		width:100px;
	}
</style>
<body>
<div class="easyui-panel" id = "panelEast" data-options="title:'换科编辑',iconCls:'icon-form'" style="width:580px">
	<div style="padding: 5px 5px 5px 5px;">
    	<table style="width:100%;border:1px solid #95b8e7;padding:5px;">
    		<tr>
    			<td>
    				<span style="background-color: FF0000">&nbsp;&nbsp;&nbsp;&nbsp;</span>
    				<span>号源已满，不可加号</span>&nbsp;&nbsp;&nbsp;&nbsp;
    				<span style="background-color: #9400D3">&nbsp;&nbsp;&nbsp;&nbsp;</span>
    				<span>号源已满,但可加号</span>&nbsp;&nbsp;&nbsp;&nbsp;
    				<span style="background-color: #98FB98">&nbsp;&nbsp;&nbsp;&nbsp;</span>
    				<span>医生停诊</span>&nbsp;&nbsp;&nbsp;&nbsp;
    			</td>
    		</tr>
    	</table>
   	</div>
   	<div style="padding: 0px 5px 5px 5px;">
	<form id="EditForm" method="post" >
		<input type="hidden" value="${registerChangeDeptLog.infoId }" name="rigisterIds" >
		<input type="hidden" id="gradeX" value="${registerChangeDeptLog.gradeX }">
		<input type="hidden" value="${registerChangeDeptLog.oldDept }" name="oldDept">
		<input type="hidden" value="${registerChangeDeptLog.oldDoc }" name="oldDoc">
		<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%;border:1px solid #95b8e7;padding:5px;">
    		<tr align="center">	
				<td style="font-size:14px;background: #E0ECFF;text-align: right;">原科室：</td>
				<td style="font-size:14px;">${registerChangeDeptLog.oldDeptName }</td>
				<td style="font-size:14px;background: #E0ECFF;text-align: right;">原医生：</td>
		    	<td style="font-size:14px;">${registerChangeDeptLog.oldDocName }</td>
	    	</tr>
	    	<tr align="center">	
				<td style="font-size:14px;background: #E0ECFF;text-align: right;">现科室：</td>
				<td style="font-size:14px;"><input id="editDeptX" name="newDept" data-options="required:true">
				<a href="javascript:delSelectedData('editDeptX');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
				</td>
				<td style="font-size:14px;background: #E0ECFF;text-align: right;">现医生：</td>
		    	<td style="font-size:14px;"><input id="editExpxrtX" name="newDoc" data-options="required:true"></td>
	    	</tr>
	    	<tr align="center">	
				<td style="font-size:14px;background: #E0ECFF;text-align: right;">换科原因：</td>
				<td  style="font-size:14px;" colspan="3"><textarea class="easyui-validatebox" rows="2" cols="50" id="reason" name="reason" data-options="multiline:true,required:true"></textarea></td>
	    	</tr>
	    	<tr align="center">	
				<td style="font-size:14px;background: #E0ECFF;text-align: right;">备注：</td>
				<td style="font-size:14px;" colspan="3"><textarea class="easyui-validatebox" rows="2" cols="50" id="remark" name="remark" data-options="multiline:true"></textarea></td>
	    	</tr>
			<tr>
				<td colspan="4" align="center">
					<a href="javascript:void(0)" data-options="iconCls:'icon-add'" class="easyui-linkbutton" onclick="submitForm()">提交</a>
					<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" class="easyui-linkbutton" onclick="closeLayout()">关闭</a>
				</td>
			</tr>
		</table>	
	</form>
	</div>
</div>
<script type="text/javascript">
	$(function(){
		//下拉框显示更改成的医生和科室
		$('#editDeptX').combobox({   
		    url: "<c:url value='/register/changeDepartmentCombobox.action'/>",    
		    valueField:'id',    
		    textField:'deptName',
		    multiple:false,
		    onSelect:function(record){
		    	var gradeIdX = $('#gradeX').val(); 
		    	$('#editExpxrtX').combogrid('grid').datagrid('load',{
	    			departmentId:record.id,
	    			grade:gradeIdX
		    	});
	    		$('#editExpxrtX').combogrid('setValue', '');
	    	}
		});
		
		bindEnterEvent('editDeptX',popWinToDept,'easyui');//绑定回车事件
		$('#editExpxrtX').combogrid({    
			panelWidth:450,    
		    mode: 'remote',    
		    url:"<c:url value='/register/changeEmployeeCombobox.action'/>",    
		    idField: 'empId',    
		    textField: 'empName',    
		    columns: [[    
		        {field:'empName',title:'专家名',width:50,sortable:true},    
		        {field:'titleName',title:'级别',width:50,sortable:true}, 
		        {field:'deptName',title:'科室',width:50,sortable:true}, 
		        {field:'clinic',title:'诊室',width:50,sortable:true}, 
		        {field:'limit',title:'挂号限额',width:50,sortable:true}, 
		        {field:'infoAlready',title:'已挂号人数',width:50,sortable:true}, 
		        {field:'empId',title:'专家ID',width:50,hidden:true,sortable:true}, 
		        {field:'infoSurplus',title:'剩余人数',
		        	 formatter: function(value,row,index){
				        	infoSurplu = (row.limit)-(row.infoAlready);
				    		if(infoSurplu>0){
				    			return infoSurplu;
				    		}else{
				    			return 0;
				    		}
						},width:50,sortable:true},
		        {field:'stoprEason',title:'停诊原因',width:50,sortable:true}
		    ]],
		    rowStyler: function(index,row){
				if((row.limit)-(row.infoAlready)==0){
					if(row.appFlag==1){
						return 'background-color:#9400D3;color:black;';
					}else{
						return 'background-color:#FF0000;color:black;';
					}
				}else if(row.isStop==1){
					return 'background-color:#98FB98;color:black;';
				}
	 		}	
		});
	});
	//提交保存
	function submitForm(){
		var id = $('#id').val();
		$('#EditForm').form('submit', {
			url : "<c:url value='/register/registerChangeSave.action'/>",
			data:$('#EditForm').serialize(),
			dataType:'json',
			onSubmit : function() {
				if(!$('#EditForm').form('validate')){
					$.messager.show({  
						title:'提示信息' ,   
						msg:'验证没有通过,不能提交表单!'  
					}); 
					return false ;
				}
				$.messager.progress({text:'保存中，请稍后...',modal:true});
			},
			success : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示','换科成功');
				window.location.reload();
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示',"操作失败！");	
			}
		}); 
	}
	//关闭
	function closeLayout(){
		$('#divLayout').layout('remove','east');
		$("#list").datagrid("reload");
	}
	
	 /**
	   * 回车弹出现科室弹框
	   * @author  zhuxiaolu
	   * @param deptIsforregister 是否是挂号科室 1是 0否
	   * @param textId 页面上commbox的的id
	   * @date 2016-03-22 14:30   
	   * @version 1.0
	   */
	   function popWinToDept(){
			$('#editDeptX').combobox('setValue','');
			var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptIsforregister=1&textId=editDeptX";
			window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -900) +',height='+ (screen.availHeight-370)+',scrollbars,resizable=yes,toolbar=yes')
		}
</script>
</body>
</html>
