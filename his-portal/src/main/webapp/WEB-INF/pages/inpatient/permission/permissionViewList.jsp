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
<body class="easyui-layout" >
	<div id="panel" data-options="region:'center',border:false" style="width:100%;height:100%;text-align: center;">
		<form method="post" id="tanchuang" style="width:100%;border:none">
			<table class="honry-table" data-optios="border:false" style="width: 100%;height:50%;">
				<tr>
						<input id="listId" type="hidden" name="id"/>
						<input id="inpatientNONO" type="hidden" name="inpatientNo"/>
						<input id="permissionNo" type="hidden" name="permissionNo"/>
					<td class="honry-lable" >授权科室：</td>
					<td>
						<input id="deptCodedept" class="easyui-combobox" name="deptCode" data-options="required:true"/>
						<a href="javascript:delSelectedData('deptCodedept');" class="easyui-linkbutton" 
						   data-options="iconCls:'icon-opera_clear',plain:true"></a>
					</td>
					<td class="honry-lable" >授权医师：</td>
					<td>
						<input id="docCode" class="easyui-combobox" name="docCode" data-options="required:true"/>
						<a href="javascript:delSelectedData('docCode');" class="easyui-linkbutton" 
						   data-options="iconCls:'icon-opera_clear',plain:true"></a>
					</td>
				</tr>
				<tr class="permissionDateSize">
					<td class="honry-lable" >处方授权时间从：</td>
					<td colspan="3">
						<input id="moStdt" name="moStdt" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d-%H-%m-%s',maxDate:'{%y+1}-{%M+1}-{%d+1}-%H-%m-%s'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						&nbsp;&nbsp;&nbsp;&nbsp;到&nbsp;&nbsp;&nbsp;&nbsp;
						<input id="moEddt" name="moEddt" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'moStdt\')}',maxDate:'{%y+1}-{%M+1}-{%d+1}-%H-%m-%s'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable" >授权说明：</td>
					<td colspan="3">
						<textarea class="easyui-textbox" style="width:88%;height:60px"id="remark" name="remark" data-options="multiline:true"></textarea>
					</td>
				</tr>
			</table>
			<br>
			<a href="javascript:void(0)" id="savesave" class="easyui-linkbutton" onclick="submit()" data-options="iconCls:'icon-save'" style="margin-left:auto;margin-right:auto;display:none;" >保&nbsp;存&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:void(0)" id="editedit" class="easyui-linkbutton" onclick="update()" data-options="iconCls:'icon-edit'" style="margin-left:auto;margin-right:auto;display:none;" >修&nbsp;改&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="closeDialog()" data-options="iconCls:'icon-cancel'"  style="margin-left:auto;margin-right:auto;" >退&nbsp;出&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
		</form>
		</div>
	
	<script type="text/javascript"> 
 	var deptMap="";
 	var empMap="";
 	var inpatientNo="";
 	var mainId="";
 	var state="${state}";
 	$(function(){
 		
 		 //授权医师(下拉框)
		$('#docCode') .combobox({    
		    url:"<%=basePath%>inpatient/permission/employeeCombo.action",    
		    valueField:'jobNo',    
		    textField:'name',
		    multiple:false,
		    mode:'remote'
		    
	    });	
 		//住院流水号
		inpatientNo="${inpatientNo}";
		$('#inpatientNONO').val(inpatientNo);
		//授权记录表中的主键ID
		mainId="${manId}";
		$('#listId').val(mainId);
		//授权科室(下拉框)
		$('#deptCodedept').combobox({   
			url: "<%=basePath%>inpatient/permission/queryDeptByType.action?state="+state,  
			mode:'remote',
			valueField:'deptCode',    
		    textField:'deptName',
		    multiple:false,
		    onSelect:function(data){
	    		$('#docCode').combobox('setValue',"");
		    	$('#docCode').combobox('reload',"<%=basePath%>inpatient/permission/employeeCombo.action?departmentId="+data.deptCode );
		    	
		    }
		    
		});
	
 		//渲染表单中的挂号科室
		$.ajax({
			url: '<%=basePath%>inpatient/permission/queryDept.action',
			success: function(deptData) {
				deptMap = deptData;
			}
		});
		//渲染表单中的挂号专家
		$.ajax({
			url: "<%=basePath%>inpatient/permission/queryUser.action",
			success: function(empData) {
				empMap = empData;
			}
		});
		if(mainId!=null&&mainId!=""){
			$('#savesave').hide();
			$('#editedit').show();
			$.ajax({
				url:"<%=basePath%>inpatient/permission/queryPermissionById.action",
				data:{"manId":mainId},
				success:function(dataqwer){
					var dataform = dataqwer;
					$('#listId').val(dataform.id);
					$('#permissionNo').val(dataform.permissionNo);
					$('#inpatientNONO').val(dataform.inpatientNo);
					$('#deptCodedept').combobox('setValue',dataform.deptCode);
					$('#docCode').combobox('reload',"<%=basePath%>inpatient/InpatientProof/employeeComboboxProof.action?departmentCode="+dataform.deptCode);
					$('#docCode').combobox('setValue',dataform.docCode);
					$('#moEddt').val(dataform.moEddt);
					$('#moStdt').val(dataform.moStdt);
					$('#remark').textbox('setValue',dataform.remark);
				}
			});
		}else{
			$('#savesave').show();
			$('#editedit').hide();
		}
	});

	//渲染科室		
	function functionDept(value,row,index){
		if(value!=null&&value!=''){
			return deptMap[value];
		}
	}
	//渲染人员
	function functionEmp(value,row,index){
		if(value!=null&&value!=''){
			return empMap[value];
		}
	}
	function submit(){
		if($('#deptCodedept').combobox('getValue')==$('#deptCodedept').combobox('getText')){
			$.messager.alert('操作提示','请选择科室下拉框里存在的科室');
			return;
		}
		var deptCodedept =$('#deptCodedept').combobox('getValue');
		var docCode = $('#docCode').combobox('getValue');
		var start =$('#moStdt').val();
		var end =$('#moEddt').val();
		if(deptCodedept ==null || deptCodedept==""){
			$.messager.alert('操作提示','请填写授权科室');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		if(docCode ==null || docCode==""){
			$.messager.alert('操作提示','请填写授权医师');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		if(start ==null || start==""){
			$.messager.alert('操作提示','请填写处方授权起始日');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		if(end ==null || end==""){
			$.messager.alert('操作提示','请填写处方授权结束日');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		if(compareTime(end,start)){
			$.messager.alert('操作提示','起始日期应早于结束日期');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		$.messager.progress({text:'保存中，请稍后...',modal:true});
		$('#tanchuang').form('submit',{
			url:"<%=basePath%>inpatient/permission/saveOrUpdateList.action",
			data: $('#tanchuang').serialize(),
			dataType:'json',
			success:function(){
				$.messager.progress('close');
				$.messager.alert('提示','保存成功');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				closeDialog();
				queryBymed();
			},
			error:function(){
				$.messager.progress('close');
				$.messager.alert('提示','保存失败');
			}
		});
	}
	 //比较时间  start 早于end   返回false
	function compareTime(start,end){
		var s = new Date(start.replace(/\-/g, "\/")); 
		var e = new Date(end.replace(/\-/g, "\/"));
		if(s<e){
			return true;
		}else{
			return false;
		}
	}
	 /**
	   * 回车弹出授权科室弹框
	   * @author  zhuxiaolu
	   * @param deptIsforregister 是否是挂号科室 1是 0否
	   * @param textId 页面上commbox的的id
	   * @date 2016-03-22 14:30   
	   * @version 1.0
	   */
	  
	 	function popWinToDept(){
		   var state="${state}";
		   if (state == 1) {
			    var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptType=C&textId=deptCodedept";
			    window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='
				+ (screen.availHeight-370)+',scrollbars,resizable=yes,toolbar=yes')
		   }else if (state == 2) {
			    var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptType=I&textId=deptCodedept";
			    window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='
				+ (screen.availHeight-370)+',scrollbars,resizable=yes,toolbar=yes')
		   }else if(state == 3){
			   var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptType=C,I&textId=deptCodedept";
			    window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='
				+ (screen.availHeight-370)+',scrollbars,resizable=yes,toolbar=yes')
		   }
		}
		 /**
		  * 回车弹出授权医师弹框
		  * @author  zhuxiaolu
		  * @param textId 页面上commbox的的id
		  * @date 2016-03-22 14:30   
		  * @version 1.0
		  */
	   function popWinToEmployees(){
		   var deptid=$('#deptCodedept').textbox('getValue');
		   if(deptid){
				var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=docCode&employeeType=1&deptIds="+deptid;
				window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='
				+ (screen.availHeight-370)+',scrollbars,resizable=yes,toolbar=yes')
			}else{
				$.messager.alert('提示',"请选择选择科室");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
	   } 
	   function update(){
		 $('#tanchuang').form('submit',{
			onSubmit:function(){
				var start =$('#moEddt').val();
				var end =$('#moStdt').val();
				if(start ==null || start==""){
					$.messager.alert('操作提示','请填写处方授权起始日');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				if(end ==null || end==""){
					$.messager.alert('操作提示','请填写处方授权结束日');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				if(compareTime(start,end)){
					$.messager.alert('操作提示','起始日期应早于结束日期');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				
			},
			url:"<%=basePath%>inpatient/permission/saveOrUpdateList.action?mainId="+mainId,
			success:function(){
				$.messager.alert('提示','修改成功');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				closeDialog();
				queryBymedicalrecordId();
			},
			error:function(){
				$.messager.alert('提示','修改失败');
			}
		});
	}
</script> 
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>