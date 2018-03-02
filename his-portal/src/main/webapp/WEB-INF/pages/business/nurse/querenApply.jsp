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
	    <div data-options="region:'center',fit:true" align="center">
	    	<form id="form1" method="post">
				<div id="main" style="padding-top:5%;">
					<div>
						<input type="hidden" id="pid"  name="inpatientInfo.id">
						<input type="hidden"  id="inpatientNo" name="inpatientInfo.inpatientNo"/>
						<input type="hidden" id="medicalrecordId" name="inpatientInfo.medicalrecordId"/>
						<input type="hidden" id="names" name="inpatientInfo.patientName"/>
						<input type="hidden" id="inState" name="inpatientInfo.inState"/>
						<input type="hidden" id="babyFlag" name="inpatientInfo.babyFlag"/>
						<input type="hidden" id="bedState" />
						<input id="dutyNurse" type="hidden"" name="inpatientInfo.dutyNurseCode"/>
						<input id="sexs" type="hidden" name="inpatientInfo.reportSex"/>
						<input id="docCode" type="hidden"  name="inpatientInfo.houseDocCode"/>
						<input id="inDates" type="hidden" name="inpatientInfo.inDate"/>
						<input id="chargeDoc" type="hidden" name="inpatientInfo.chargeDocCode"/>
						<input id="paykind" type="hidden" name="inpatientInfo.paykindCode" />
						<input id="chiefDoc" type="hidden" name="inpatientInfo.chiefDocCode"/>
						<input id="deptNames" type="hidden" />
						<input id="bedNames" type="hidden"/>
					</div>
					<fieldset style="width: 350px;">
						<legend><font style="font-weight: bold;font-size: 14px;">患者信息和转入科室、病床</font></legend>
						<table style="padding: 10px; width: 350px">
							<tr hidden>
								<td>住院流水号：</td>
								<td><input id="patientNo"class="easyui-textbox"  readonly="readonly"/>
								<input type="hidden"  id="inpatientNO" value="${inpatientNo }"/>
								</td>
							</tr>
							<tr>
								<td>病历号 ：</td>
								<td><input id="medicalrecordIdID" value="${medicalreId }" class="easyui-textbox"  readonly="readonly" name="medicalrecordId" /></td>
							</tr>
							<tr style="height: 20px"></tr>
							<tr>
								<td>姓 名：</td>
								<td><input id="patientNameId" class="easyui-textbox" readonly="readonly"/></td>
							</tr>
							<tr style="height: 20px"></tr>
							<tr>
								<td>性 别：</td>
								<td><input id="reportSexID" class="easyui-combobox"  data-options="iconCls:'icon-user'"/></td>
							</tr>
							<tr style="height: 20px"></tr>
							<tr>
								<td>病区：</td>
								<td><input id="bq" class="easyui-combobox" name="inpatientInfo.nurseCellCode" data-options="prompt:'下拉或回车查询',iconCls:'icon-user_home'" />
									<a href="javascript:delSelectedData('bq');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
									<input id="bqName" type="hidden" name="inpatientInfo.nurseCellName"/>
								</td>
							</tr>
							<tr style="height: 20px"></tr>
							<tr>
								<td>科室：</td>
								<td><input id="ks" class="easyui-combobox" name="inpatientInfo.deptCode"   data-options="prompt:'下拉或回车查询',iconCls:'icon-user_gray_cool',required:true" />
									<a href="javascript:delSelectedData('ks');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
									<input id="ksName" type="hidden"  name="inpatientInfo.deptName"/>
								</td>
							</tr>
							<tr style="height: 20px"></tr>
							<tr>
								<td>病床号 ：</td>
								<td>
									<input id="newBedID" class="easyui-combobox" name="inpatientInfo.bedNo" data-options="prompt:'下拉或回车查询',iconCls:'icon-new_blue',required:true"/>
									<a href="javascript:delSelectedData('newBedID');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
									<input id="newBedIDName" type="hidden"  name="inpatientInfo.bedName"/>
								</td>
							</tr>
							<tr style="height: 20px"></tr>
							<tr>
								<td></td>
								<td style="padding-left: 10%">
								<shiro:hasPermission name="${menuAlias}:function:save">
									<a id="btn" onClick="getINfooooo()" data-options="iconCls:'icon-save'" class="easyui-linkbutton">确认转科申请</a>
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
	//获取性别下拉列表  
		$('#reportSexID').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
			valueField : 'encode',
			textField : 'name',
			readonly : true
		});
		//根据登录科室获取病床号
		$("#newBedID").combobox({
			url:'<%=basePath%>inpatient/recall/getcomboboxBystate.action',    
			valueField:'id',    
			textField:'bedName'
			
		});
		$("#bq").combobox({
			url:'<%=basePath%>nursestation/nurse/getDeptment.action?flg=1',
			mode:'remote',
			required:true,
			valueField:'deptCode',
			textField:'deptName',
			onChange:function(newValue, oldValue){
				$("#ks").combobox({
					url:"<%=basePath%>nursestation/nurse/getDeptmentks.action?deptBqId="+newValue,
					valueField:'deptCode',
					textField:'deptName',
					mode:'remote'
				});
			}
			
		});
		bindEnterEvent('newBedID',popWinToHospitalBed,'easyui');
		$.ajax({
			type:'post',
			url:'<%=basePath%>nursestation/nurse/getInfobyId.action',
			data:{inpatientNo:$('#inpatientNO').val()},
			success:function(data){
				$('#patientNameId').textbox('setValue',data.patientName);
				$('#reportSexID').combobox('setValue',data.reportSex);
				$('#patientNo').textbox('setValue',data.inpatientNo);
				$.ajax({
					type:'post',
					url:'<%=basePath%>nursestation/nurse/getShiftApply.action',
					data:{inpatientNo:data.inpatientNo},
					success:function(Data){
						$('#bq').combobox('setValue',Data[0].newNurseCellCode);
						$('#ks').combobox('setValue',Data[0].newDeptCode);
						$('#newBedID').combobox('setValue',Data[0].newBenCode);
					}
				});
			}
		});
		$.ajax({
			type:'post',
			url:'<%=basePath%>nursestation/nurse/searchByInpatient.action',
			data:{inpatientNo:$('#inpatientNO').val()},
			success:function(Data){
				if(Data.id==null){
					$.messager.alert('提示信息','该患者还未住院!');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else{
					obtainValue(Data);
				}
			}
		});
	});
	
	/**
	 * 患者获得值给td复值
	 */
	function obtainValue(Data){
		 $('#pid').val(Data.id);     //获得id
		 $('#inState').val(Data.inState);   //接诊状态
		 $('#babyFlag').val(Data.babyFlag);   //是否是婴儿
		 $('#bedState').val(Data.bedState);  //病床状态
		 $('#medicalrecordId').val(Data.medicalrecordId);  //病历号
		 $('#inpatientNo').val(Data.inpatientNo);       //流水号
		 $('#names').val(Data.patientName);              //获取姓名
		 $('#dutyNurse').val(Data.dutyNurseCode);    //获取责任护士
		 $('#sexs').val(Data.reportSex);		//获取性别
		 $('#docCode').val(Data.houseDocCode);  //获取住院医师
		 $('#inDates').val(Data.inDate);		//获取入院日期
		 $('#chargeDoc').val(Data.chargeDocCode);  //获取主治医师
		 $('#paykind').val(Data.paykindCode);		//获取结算类别
		 $('#chiefDoc').val(Data.chiefDocCode);  //获取主任医师
		 $('#deptNames').val(Data.deptCode);		//获取住院科室
		 $('#bedNames').val(Data.bedId);  //获取病床号
	}
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
				if($('#newBedID').combobox('getValue')==''){
					$.messager.alert('提示','请选择病床！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else{
					$('#ksName').val($('#ks').combobox('getText'));
					$('#bqName').val($('#bq').combobox('getText'));
					$('#newBedIDName').val($('#newBedID').combobox('getText'));
					$('#form1').form('submit',{
						url:'<%=basePath %>nursestation/nurse/confirmApply.action', 
						queryParams:{bedId:$('#bedNames').val(),deptCode12:$('#deptNames').val()},
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
								$.messager.alert('提示','保存成功！');
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
				$("#newBedID").combobox('setValue',node.bedName);
			};
			var tempWinPath = "<%=basePath%>popWin/popWinBusinessHospitalbed/toBusinessHospitalbedPopWin.action?textId=newBedID&type=2";
			window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -1000) +',height='+ (screen.availHeight-370) 
			+',scrollbars,resizable=yes,toolbar=yes')
	  }
</script>
</html>