<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
.panel-header,.panel-body{
	border-top:0
}
.tree-title {
    font-size: 14px;
    }

</style>
<title>临床路径申请</title>
</head>
<body>
<div  class="easyui-layout" fit="true">
		<form id="ft" >
			<div style="padding: 5px 5px 5px 5px;margin-left:auto;margin-right:auto;">
							<table id="list" class="honry-table" cellpadding="0" cellspacing="0"border="0" style="width:100% ;margin-left:auto;margin-right:auto;" > 
								<tr >
										<td style="font-size: 22" align="center" colspan="2">临床路径申请</td>
										<input  name="cPWayVo.apply_code" value="${apply_code}" type="hidden" >
								</tr>
								<tr>
									<td  class="honry-lable" style="font-size: 14">
										住院流水号：
									</td>
									<td style="font-size: 14">
										<input id="inpatient_no" name="cPWayVo.inpatient_no"   data-options="required:true,prompt: '请输入住院流水号'" class="easyui-textbox" style="width:240px;height:24px;" >
									</td>
								</tr>
								<tr>
									<td  class="honry-lable" style="font-size: 14">
										病历号：
									</td>
									<td style="font-size: 14">
										<input id="medicalrecord_id" name="cPWayVo.medicalrecord_id"  readonly="readonly"  class="easyui-textbox" style="width:240px;height:24px;" >
									</td>
								</tr>
								<tr>
									<td  class="honry-lable" style="font-size: 14">
										姓名：
									</td>
									<td style="font-size: 14">
										<input id="patientName" name="patientName"  readonly="readonly"  class="easyui-textbox" style="width:240px;height:24px;" >
									</td>
								</tr>
								<tr>
									<td  class="honry-lable" style="font-size: 14">
										临床路径名称：
									</td>
									<td style="font-size: 14">
										<input id="cp_id" class="easyui-combobox" name="cPWayVo.cp_id"  style="width:240px;height:24px;" data-options="required:true,prompt: '请选择临床路径名称'"/>									
									</td>
								</tr>
								
								<tr>
									<td  class="honry-lable" style="font-size: 14">
										临床路径版本号：
									</td>
									<td style="font-size: 14">
										<input id="version_no" class="easyui-combobox" name="cPWayVo.version_no"  style="width:240px;height:24px;" data-options="required:true,prompt: '请选择临床路径版本号'"/>		 
									</td>
								</tr>
								<tr>
									<td  class="honry-lable" style="font-size: 14">
										申请类别：
									</td>
									<td style="font-size: 14">
										<select id="apply_type" class="easyui-combobox" name="cPWayVo.apply_type" style="width:240px;height:24px;" data-options="required:true,prompt: '请选择申请类别'">   
										    <option value="1">入径申请</option> 
										    <option value="2">出径申请</option>  
										</select> 
									</td>
								</tr>
								<tr>
									<td  class="honry-lable" style="font-size: 14">
										申请说明：
									</td>
									<td style="font-size: 14">
										<input id="apply_memo"  name="cPWayVo.apply_memo" class="easyui-textbox" data-options="multiline:true"  style="width:240px;height:50px;">									
									</td>
								</tr>
								<tr>
									<td  class="honry-lable" style="font-size: 14;text-align: center;" colspan="2"  align="center" >
										<div>
											<a href="javascript:void(0)"  onclick="saveForm()" class="easyui-linkbutton" data-options="iconCls:'icon-save'"  >保存</a>
											<a href="javascript:void(0)"  onclick="closeWin()" class="easyui-linkbutton" data-options="iconCls:'icon-close'"  >关闭</a>
										</div>
									</td>
								</tr>
							</table>
			</div>
			</form>
		</div>
</body>
</html>
<script type="text/javascript">

	
		$(function(){
			//下拉框加载
			$('#cp_id').combobox({    
			    url:"${pageContext.request.contextPath}/outpatient/CPWay/cPWList.action",    
			    valueField:'id',    
			    textField:'text',
			    onSelect:function(record){
			    	//加载对应的版本号列表
			    	$('#version_no').combobox({    
					    url:"${pageContext.request.contextPath}/outpatient/CPWay/findVersionList.action?cPWId="+record.id,    
					    valueField:'id',    
					    textField:'text'
					});  
			    }
			});  
		})

		//给住院流水号添加失去焦点事件
		$('#inpatient_no').textbox({onChange:function(){
			var inpatient_no = $('#inpatient_no').textbox("getText");
			//校验该住院流水号是否已经申请过
			$.ajax({
				type:"post",
				url:"${pageContext.request.contextPath}/outpatient/CPWay/checkIsAdd.action",
				data:"inpatient_no="+inpatient_no,
				success:function(backData){
					if(backData.data=="true"){
						$.messager.alert('提示','该患者已申请，请勿重复申请！'); 
						$('#inpatient_no').textbox("setText","");
					}else{
							//根据住院流水号查询患者信息
							$.ajax({
								type:"post",
								url:"${pageContext.request.contextPath}/outpatient/CPWay/findPatient.action",
								data:"inpatient_no="+inpatient_no,
								success:function(backData){
									if(backData.data=="error"){
										$.messager.alert('提示','网络故障，请稍后重试!'); 
										$('#inpatient_no').textbox("setText","");
										$('#medicalrecord_id').textbox("setText","");
										$('#patientName').textbox("setText","");
									}else if(backData.data=="empty"){
										//根据住院流水号查询患者信息
										$.messager.alert('提示','没有查到对应的患者，请重新输入住院流水号！');
										$('#inpatient_no').textbox("setText","");
										$('#medicalrecord_id').textbox("setText","");
										$('#patientName').textbox("setText","");
									}else{
										
										$('#medicalrecord_id').textbox("setText",backData.data.medicalrecord_id);
										$('#medicalrecord_id').textbox("setValue",backData.data.medicalrecord_id);
										$('#patientName').textbox("setText",backData.data.patientName);
										
									}
								}
							})
					}
				}
			})
			
		}})
		
		function saveForm(){
			var inpatient_no = $('#inpatient_no').textbox("getText");
			var cp_id = $('#cp_id').combobox("getValue");
			var version_no = $('#version_no').combobox("getValue");
			var apply_type = $('#apply_type').combobox("getValue");
			
			if(inpatient_no==null||inpatient_no==''||inpatient_no==undefined){
				$.messager.alert('提示','住院流水号不能为空!');
				return ;
			}
			if(cp_id==null||cp_id==''||cp_id==undefined){
				$.messager.alert('提示','临床路径名称不能为空!');
				return ;
			}
			if(version_no==null||version_no==''||version_no==undefined){
				$.messager.alert('提示','临床路径版本号不能为空!');
				return ;
			}
			if(apply_type==null||apply_type==''||apply_type==undefined){
				$.messager.alert('提示','申请类别不能为空!');
				return ;
			}
			
			$.ajax({
				url:"${pageContext.request.contextPath}/outpatient/CPWay/addCPWayPatient.action",
				data:$("#ft").serialize(),
				type:"post",
				success:function(backData){
					if(backData.data=="success"){
						$.messager.alert('提示','添加成功！');
						//刷新父列表
						window.parent.refreshApply();
						//关闭当前窗口
						window.parent.$("#win").window("close");
						
					}else{
						$.messager.alert('提示','添加失败，请稍后重试！');
					}
				}
			})
			
			
		}
		
		function closeWin(){
			
			window.parent.$("#win").window("close");
		}
		
		




</script>

