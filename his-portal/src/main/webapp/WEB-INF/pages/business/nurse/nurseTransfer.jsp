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
								<td>主键 ：</td>
								<td><input id="inpatientId" class="easyui-textbox" /></td>
							</tr>
							<tr hidden>
								<td>病历号 ：</td>
								<td><input id="inpatientNo" class="easyui-textbox" /></td>
							</tr>
							<tr>
								<td>病历号 ：</td>
								<td><input id="medicalrecordIdID" value="${medicalreId }" class="easyui-textbox"  readonly="readonly" name="medicalrecordId" />
									<input id="inpatientNO" type="hidden" value="${inpatientNo }"/>
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
								<td><input id="reportSexID" class="easyui-textbox"  readonly="readonly" data-options="iconCls:'icon-user'"/></td>
							</tr>
							<tr style="height: 20px"></tr>
							<tr>
								<td>病区：</td>
								<td><input id="bq"  data-options="prompt:'下拉查询病区',iconCls:'icon-user_home'" />
									<a href="javascript:delSelectedData('bq');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
								</td>
							</tr>
							<tr style="height: 20px"></tr>
							<tr>
								<td>科室：</td>
								<td><input id="ks" class="easyui-combobox" data-options="disabled:true,prompt:'下拉查询科室',iconCls:'icon-user_gray_cool',required:true" />
									<a href="javascript:delSelectedData('ks');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
									<input id="oldDept" type="hidden"  />
								</td>
							</tr>
							<tr style="height: 20px"></tr>
							<tr>
								<td>病床号 ：</td>
								<td>
									<input id="newBedID1" class="easyui-combobox"  data-options="disabled:true,prompt:'下拉查询病床号',iconCls:'icon-new_blue',required:true"/>
									<a href="javascript:delSelectedData('newBedID1');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
									<input id="oldBed" type="hidden"  />
								</td>
							</tr>
							<tr style="height: 20px"></tr>
							<tr>
								<td></td>
								<td style="padding-left: 10%">
									<shiro:hasPermission name="${menuAlias}:function:save">
									<a id="btn" onClick="getINfooooo()" data-options="iconCls:'icon-save'" class="easyui-linkbutton">保&nbsp;存&nbsp;</a>
									</shiro:hasPermission>
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
		$("#bq").combobox({
			url:'<%=basePath%>nursestation/nurse/getDeptment.action?flg=1',
			required:true,
			mode:'remote',
			valueField:'deptCode',
			textField:'deptName',
			onChange:function(newValue, oldValue){
				$("#ks").combobox({
					disabled:false,
					mode:'remote',
					url:"<%=basePath%>nursestation/nurse/getDeptmentks.action?deptBqId="+newValue,
					valueField:'deptCode',
					textField:'deptName'
					
				});
				$("#newBedID1").combobox({
					disabled:false,
					url:"<%=basePath%>inpatient/recall/getcomboboxBystate.action?deptBqId="+newValue,
					valueField:'id',
					textField:'bedName'
					
				});
			}
		});
		bindEnterEvent('newBedID1',popWinToHospitalBed,'easyui');
		$.ajax({
			type:'post',
			url:'<%=basePath%>nursestation/nurse/getInfobyId.action',
			data:{inpatientNo:$('#inpatientNO').val()},
			success:function(data){
				$('#patientNameId').textbox('setValue',data.patientName);
				$('#reportSexID').textbox('setValue',data.reportSexName);
				$('#bq').combobox('setValue',data.nurseCellCode);
				$('#ks').combobox('setValue',data.deptCode);
				$('#newBedID1').combobox('setValue',data.bedName);
				$('#oldDept').val(data.deptCode);
				$('#oldBed').val(data.bedName);
				
			}
		});
	});
	//保存
	function  getINfooooo(){
		if($('#ks').combobox('getValue')==''){
			$.messager.alert('提示','请选择科室！');  
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}else{
			if($('#bq').combobox('getValue')==''){
				$.messager.alert('提示','请选择病区！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}else{
				if($('#newBedID1').combobox('getValue')==''){
					$.messager.alert('提示','请选择病床!');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else{
					if($('#ks').combobox('getValue')==$('#oldDept').val()){
						if($('#newBedID1').combobox('getText')==$('#oldBed').val()){
							$.messager.alert('提示',"该患者科室病床都相同，不能转科");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return;
						}
					}
					
					$('#form1').form('submit',{
						url:'<%=basePath %>nursestation/nurse/saveShiftApply.action', 
						queryParams:{deptCode1:$('#ks').combobox('getValue'),hs:$('#bq').combobox('getValue'),bl:$('#inpatientNO').val(),bc:$('#newBedID1').combobox('getValue'),bcName:$('#newBedID1').combobox('getText')},
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
							}else if(data=="1"){
								$.messager.progress('close');
								$.messager.alert('提示','该患者已存在转科申请，请先取消转科申请再进行转科！');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								$('#form1').form('clear');
								return;
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
	 /**
	 * 回车弹出病床弹框
	 * @author  donghe
	 * @param textId 页面上commbox的的id
	 * @date 2016-05-13 14:30   
	 * @version 1.0
	 */
	 function popWinToHospitalBed(){
		 popWinBedCallBackFn = function(node){
				$("#newBedIDHiddden").val(node.id);
				$("#newBedID1").combobox('setValue',node.bedName);
			};
			var tempWinPath = "<%=basePath%>popWin/popWinBusinessHospitalbed/toBusinessHospitalbedPopWin.action?textId=newBedID1&type=2";
			window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -1000) +',height='+ (screen.availHeight-370) 
			+',scrollbars,resizable=yes,toolbar=yes')
	  }
</script>
</html>