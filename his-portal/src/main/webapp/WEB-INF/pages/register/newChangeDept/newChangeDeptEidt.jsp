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
<body style="margin: 0px; padding: 0px">
<div class="easyui-panel" id = "panelEast" data-options="title:'换科编辑',iconCls:'icon-form',fit:true,border:false">
	<div style="padding: 5px 5px 5px 5px;">
    	<table style="border:0px solid #95b8e7;width:100%"">
    		<tr>
    			<td>
    				<span style="background-color: FF0000">&nbsp;&nbsp;</span>
    				<span>号源已满，不可加号</span>&nbsp;&nbsp;
    				<span style="background-color: #9400D3">&nbsp;&nbsp;</span>
    				<span>号源已满,但可加号</span>&nbsp;&nbsp;
    				<span style="background-color: #98FB98">&nbsp;&nbsp;</span>
    				<span>医生停诊</span>&nbsp;&nbsp;
    			</td>
    		</tr>
    	</table>
   	</div>
   	<div style="padding: 0px 5px 5px 5px;">
	<form id="EditForm" method="post" >
		<input type="hidden" id="newMidday" name="changeDeptLog.newMidday" >
		<input type="hidden" value="${changeDeptLog.rigisterId }" name="changeDeptLog.rigisterId" >
		<input type="hidden" id="gradeX" value="${changeDeptLog.gradeX }" name="changeDeptLog.gradeX" >
		<input type="hidden" id="gradeName" value="${changeDeptLog.gradeName }" name="changeDeptLog.gradeName" >
		<input type="hidden" value="${changeDeptLog.oldDept }" name="changeDeptLog.oldDept">
		<input type="hidden" id="oldDoc" value="${changeDeptLog.oldDoc }" name="changeDeptLog.oldDoc">
		<input type="hidden" id="oldAllDoc" value="${changeDeptLog.newDoc }">
		<input type="hidden" id="oldMidday" value="${changeDeptLog.newMidday }">
		<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="border:1px solid #95b8e7;padding:5px;">
    		<tr align="center">	
				<td style="text-align: right;" class="honry-lable">原科室：</td>
				<td>${changeDeptLog.oldDeptName }</td>
				<td style="text-align: right;" class="honry-lable">原医生：</td>
		    	<td>${changeDeptLog.oldDocName }</td>
	    	</tr>
	    	<tr align="center">	
				<td style="text-align: right;" class="honry-lable">现科室：</td>
				<td><input id="editDeptX"  data-options="required:true">
<!-- 				<a href="javascript:delSelectedData('editDeptX');delSelectedData('editExpxrtX');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a> -->
				<a onclick="delDeptX()"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
					<input type="hidden" id="newDeptName" name="changeDeptLog.newDeptName">
					<input type="hidden" id="newDept" name="changeDeptLog.newDept">
				</td>
				<td style="text-align: right;" class="honry-lable">现医生：</td>
		    	<td><input id="editExpxrtX" name="changeDeptLog.newDoc" data-options="required:true">
		    	<a href="javascript:delSelectedData('editExpxrtX');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
		    		<input type="hidden" id="newDocName" name="changeDeptLog.newDocName">
		    	</td>
	    	</tr>
	    	<tr align="center">	
				<td style="text-align: right;" class="honry-lable">换科原因：</td>
				<td colspan="3"><textarea class="easyui-validatebox" rows="2" cols="75" id="reason" name="changeDeptLog.reason" data-options="multiline:true,required:true"></textarea></td>
	    	</tr>
	    	<tr align="center">	
				<td style="text-align: right;" class="honry-lable">备注：</td>
				<td colspan="3"><textarea class="easyui-validatebox" rows="2" cols="75" id="remark" name="changeDeptLog.remark" data-options="multiline:true"></textarea></td>
	    	</tr>
			<tr>
				<td colspan="4" align="center">
					<a href="javascript:void(0)" data-options="iconCls:'icon-tick'" class="easyui-linkbutton" onclick="submitForm()">提交</a>
					<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" class="easyui-linkbutton" onclick="closeLayout()">关闭</a>
				</td>
			</tr>
		</table>	
	</form>
	</div>
</div>
<script type="text/javascript">
var easonMap=new Map();//停诊原因
	$(function(){
	//渲染停诊原因
	$.ajax({
		url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action',
		data:{"type":"stopReason"},
		type:'post',
		success: function(data) {
			for(var i=0;i<data.length;i++){
				easonMap.put(data[i].encode,data[i].name);
			}
		}
	});
		//下拉框显示更改成的医生和科室
		showDoc();
	});
//提交保存
function submitForm(){
	var oldMiddy = $('#oldMidday').val();
	$('#newDeptName').val($('#editDeptX').combobox('getText'));
	$('#newDocName').val($('#editExpxrtX').combogrid('getText'));
	var id = $('#id').val();
	$('#EditForm').form('submit', {
		url: "<%=basePath%>outpatient/newChangeDeptLog/registerChangeSave.action?middayOld="+oldMiddy,
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
			var dataMap = eval("("+data+")");
			if(dataMap.resMsg=="error"){
				$.messager.progress('close');
        		$.messager.alert("操作提示",dataMap.resCode);
				window.location.reload();
        	}else if(dataMap.resMsg=="success"){
				$.messager.progress('close');
				$.messager.alert('提示','换科成功');
				window.location.reload();
        	}
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


function delDeptX(){
	delSelectedData('editDeptX');
	delSelectedData('editExpxrtX');
	showDoc();
}

function showDoc(){
	var oldDoc = $('#oldDoc').val(); 
	var oldAllDoc = $('#oldAllDoc').val(); 
	var gradeIdX = $('#gradeX').val(); 
	//下拉框显示更改成的医生和科室
	$('#editDeptX').combobox({   
		url: "<%=basePath%>outpatient/newChangeDeptLog/changeDepartmentCombobox.action",
	    valueField:'deptCode',    
	    textField:'deptName',
	    multiple:false,
	    onSelect:function(record){
	    	$('#editExpxrtX').combogrid('grid').datagrid('load',{
	    		"changeDeptLog.newDept":record.deptCode,
    			"changeDeptLog.gradeX":gradeIdX,
	    	});
    		$('#editExpxrtX').combogrid('setValue', '');
    	}
	});
		bindEnterEvent('editDeptX',popWinToDept,'easyui');//绑定回车事件
	$('#editExpxrtX').combogrid({    
		panelWidth:650,    
	    mode: 'remote', 
	    url: "<%=basePath%>outpatient/newChangeDeptLog/changeEmployeeCombobox.action",
// 	    queryParams:{"changeDeptLog.gradeX":"","changeDeptLog.newDept":"","changeDeptLog.oldDoc":oldDoc},
	    queryParams:{"changeDeptLog.gradeX":gradeIdX,"changeDeptLog.newDept":""},
	    idField: 'empId',    
	    textField: 'empName',    
	    columns: [[    
	        {field:'empName',title:'专家名',width:70,sortable:true},    
	        {field:'titleName',title:'级别',width:100,sortable:true}, 
	        {field:'deptName',title:'科室',width:120,sortable:true}, 
	        {field:'midday',title:'午别',
	        	formatter: function(value,row,index){
	        		if(value==1){
	        			return '上午'
	        		}else if(value==2){
	        			return '下午'
	        		}else{
	        			return '晚上'
	        		}
				    },width:50,sortable:true}, 
	        {field:'limit',title:'挂号限额',width:80,sortable:true}, 
	        {field:'infoAlready',title:'已挂人数',width:80,sortable:true}, 
	        {field:'empId',title:'专家ID',hidden:true,sortable:true}, 
	        {field:'infoSurplus',title:'剩余人数',
	        	 formatter: function(value,row,index){
			        	infoSurplu = (row.limit)-(row.infoAlready);
			    		if(infoSurplu>0){
			    			return infoSurplu;
			    		}else{
			    			return 0;
			    		}
				  },width:70,sortable:true},
	        {field:'stoprEason',title:'停诊原因',width:70,formatter:function(value,row,index){
	        	if(value!=null&&value!=''){
	        		return easonMap.get(value);
	        	}
	          },sortable:true}
	    ]],
	    onClickRow: function (rowIndex, rowData) {//双击查看
	    	var newEmpId = rowData.empId;
	    	var newMiddayCode = rowData.midday;
	    	var oldMiddy = $('#oldMidday').val();
	    	$('#newMidday').val(rowData.midday);
	    	if(Number(oldAllDoc.indexOf(newEmpId+newMiddayCode))+1!=0){
	    		delSelectedData('editDeptX');
	    		delSelectedData('editExpxrtX');
	    		$.messager.alert('提示',"该午别下，患者已挂此医生，无需换科！");
	    		return false;
	    	}
	    	var isstop = rowData.stoprEason;
	    	
	    	if(isstop != null && isstop != undefined && isstop != ''){
	    		delSelectedData('editDeptX');
	    		delSelectedData('editExpxrtX');
	    		$.messager.alert('提示','所选医生已经停诊，不能换给此医生');
	    		return false;
	    	}
	    	var oldName =  $('#editDeptX').combobox('getText');
// 	    	if(oldName.length==0){
    		var newdeptId = rowData.deptId;
    		var newdeptName = rowData.deptName;
    		var newGrade = rowData.grade;
    		var newGradeName = rowData.titleName;
 	    	$('#editDeptX').combobox('setText',newdeptName);
 	    	$('#editDeptX').combobox('setValue',newdeptName);
 	    	$('#editDeptX').val(newdeptName);
 	    	$('#newDeptName').val(newdeptName);
 	    	$('#newDept').val(newdeptId);
 	    	$('#gradeX').val(newGrade);
 	    	$('#gradeName').val(newGradeName);
// 	    	}
	    	
	    },
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
}

</script>
</body>
</html>
