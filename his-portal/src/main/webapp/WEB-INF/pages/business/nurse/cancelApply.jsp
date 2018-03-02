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
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'center',fit:true"  align="center" style="padding-top: 5%">
	    	<form id="form1" method="post">
				<div id="main">
					<fieldset style="width: 350px;">
						<legend><font style="font-weight: bold;font-size: 14px;">患者信息和转入科室、病床</font></legend>
						<table style="padding: 10px; width: 350px">
							<tr hidden>
								<td>住院流水号：</td>
								<td><input id="patientNo"class="easyui-textbox"  readonly="readonly"/></td>
							</tr>
							<tr>
								<td>病历号 ：</td>
								<td><input id="medicalrecordIdID" value="${medicalreId }" class="easyui-textbox"  readonly="readonly" name="medicalrecordId" />
										<input id="inpatientNo" value="${inpatientNo }" type="hidden" />
								</td>
							</tr>
							<tr style="height: 20px"></tr>
							<tr>
								<td>姓 名：</td>
								<td><input id="patientNameId" class="easyui-textbox" readonly="readonly"/></td>
							</tr>
							<tr style="height: 20px"></tr>
							<tr>
								<td>性 别：</td>
								<td><input id="reportSexID" class="easyui-textbox" readonly="readonly" data-options="iconCls:'icon-user'"/></td>
							</tr>
							<tr style="height: 20px"></tr>
							<tr>
								<td>病区：</td>
								<td><input id="bq"  type="hidden" />
								<input id="bqName" class="easyui-textbox" readonly="readonly" />
								</td>
							</tr>
							<tr style="height: 20px"></tr>
							<tr>
								<td>科室：</td>
								<td><input id="ks"  type="hidden" />
								<input id="ksName" class="easyui-textbox" readonly="readonly" />
								</td>
							</tr>
							<tr style="height: 20px"></tr>
							<tr>
								<td>病床号 ：</td>
								<td>
									<input id="newBedID"  type="hidden"/>
									<input id="newBedIDName" class="easyui-textbox" readonly="readonly" />
								</td>
							</tr>
							<tr style="height: 20px"></tr>
							<tr>
								<td></td>
								<td style="padding-left: 10%">
									<a id="btn" onClick="getINfooooo()" data-options="iconCls:'icon-save'" class="easyui-linkbutton">取消转科申请</a>
								</td>
							</tr>
						</table>
					</fieldset>
				</div>
			</form>
	    </div>   
	</div>
</body>
<script type="text/javascript">
	$(function(){
		$.ajax({
			type:'post',
			url:'<%=basePath%>nursestation/nurse/getInfobyId.action',
			data:{inpatientNo:$('#inpatientNo').val()},
			success:function(data){
				$('#patientNameId').textbox('setValue',data.patientName);
				$('#reportSexID').textbox('setValue',data.reportSexName);
				$('#patientNo').textbox('setValue',data.inpatientNo);
				$.ajax({
					type:'post',
					url:'<%=basePath%>nursestation/nurse/getShiftApply.action',
					data:{inpatientNo:data.inpatientNo},
					success:function(Data){
						$('#bq').val(Data[0].newNurseCellCode);
						$('#ks').val(Data[0].newDeptCode);
						$('#newBedID').val(Data[0].newBenCode);
						
						$('#bqName').textbox('setValue',Data[0].newNurseCellName);
						$('#ksName').textbox('setValue',Data[0].newDeptName);
						$('#newBedIDName').textbox('setValue',Data[0].newBenName);
					}
				}); 
			}
		});
	});
	//保存
	function  getINfooooo(){
		if($('#ks').val()==''){
			$.messager.alert('提示','请选择科室！');
			setTimeout(function(){
					$(".messager-body").window('close');
			},3500);
		}else{
			if($('#bq').val()==''){
				$.messager.alert('提示','请选择病区！');
				setTimeout(function(){
	   					$(".messager-body").window('close');
	   			},3500);
			}else{
				if($('#newBedID').val()==''){
					$.messager.alert('提示','请选择病床！');
					setTimeout(function(){
	   					$(".messager-body").window('close');
	   				},3500);
				}else{
					$('#form1').form('submit',{
						url:'<%=basePath %>nursestation/nurse/cancelShiftApply.action', 
						queryParams:{inpatientNo:$('#patientNo').textbox('getValue'),bc:$('#newBedID').val(),bcName:$('#newBedIDName').textbox('getValue')},
						onSubmit:function(data){
							if (!$('#form1').form('validate')) {
								$.messager.show({
									title : '提示信息',
									msg : '验证没有通过,不能提交表单!'
								});
								return ;
							}
							$.messager.progress({text:'转科中,请稍等...',modal:true});
						},
						success:function(data){
							if(data=="success"){
								$.messager.progress('close');
								$.messager.alert('提示','保存成功!');
								setTimeout(function(){
				   					$(".messager-body").window('close');
				   				},3500);
								$('#form1').form('clear');
								//实现刷新栏目中的数据
								window.parent.$("#treePatientInfo").tree("reload");
							}
						},
						error : function(data) {
							$.messager.alert('提示','保存失败！');
						}
					}); 
				}
			}
		}
	}
	
</script>
</html>