<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
	<div id="divLayout" class="easyui-layout" class="easyui-layout" data-options="fit:true" style="width: 100%; height: 100%; overflow-y: auto;">
		<div  class="easyui-layout" fit="true">
		<form id="editForm" method="post">
			<div style="padding: 5px 5px 5px 5px;margin-left:auto;margin-right:auto;">
				<input type="hidden" name="type" id="type"/>
							<table id="list" class="honry-table" cellpadding="0" cellspacing="0"
								border="0" style="width: 60% ;margin-left:auto;margin-right:auto;" >
								<tr >
								<td style="font-size: 14" colspan="4">&nbsp;&nbsp;&nbsp;						
								<input class="easyui-textbox" id="idcardNode" data-options="prompt:'就诊卡号'" /><!--style="width: 200px;"  -->
<!-- 								<a href="javascript:void(0)"  class="easyui-linkbutton read_medical_card" type_id="read_medical_cardID" id="read_medical_cardID" type_value="read_medical_card" cardNo="" data-options="iconCls:'icon-bullet_feed'">读就诊卡</a> -->
									<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读就诊卡</a>
								<input type="hidden" id="hiddenIdcardNode" name="cardNo" >
<!-- 								<a href="javascript:void(0)"  class="easyui-linkbutton  read_identity" type_id="read_identityID" id="read_identityID" type_value="read_identity" cardNo="" data-options="iconCls:'icon-bullet_feed'">读身份证</a> -->
									<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
								</td>
							</tr>
								<tr>
									<td  class="honry-lable" style="font-size: 14"><!-- width:165px; -->
										姓名：
										</td>
									<td style="font-size: 14"><!-- width:225px; -->
										<input type="hidden" id="id" name="patient.id" >
										<input id="patientName" name="patient.patientName"  readonly="readonly" data-options="required:true" class="easyui-textbox" >
									</td>
									<td class="honry-lable" style="font-size: 14"> <!--width:165px; -->
									<input type="hidden" id="sexHid" >
										性别：
										</td>
									<td style="font-size: 14"><!-- width:225px; -->
										<input id="patientSex" name="patient.patientSex" class="easyui-combobox" >
									</td>
									
								</tr>
								<tr>
									<td class="honry-lable" ><!--style="width:165px"  -->
										出生日期：
										</td>
									<td style="font-size: 14"><!--width:225px;  -->
										<input id="patientBirthday" name="patient.patientBirthday" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
									</td>
									<td class="honry-lable" style="font-size: 14"><!-- width:165px; -->
										婚姻情况：
										</td>
									<td style="font-size: 14"><!--width:225px;  -->
										<input id="patientWarriage" name="patient.patientWarriage"  class="easyui-combobox" >
									</td>
								</tr>
								<tr>
									<td class="honry-lable" style="font-size: 14"><!--width:165px;  -->
										联系电话：
										</td>
									<td style="font-size: 14"><!-- width:225px; -->
										<input id="patientPhone" name="patient.patientPhone"  class="easyui-textbox"  >
									</td>
									<td class="honry-lable" style="font-size: 14"><!--width:165px;  -->
										职业：
										</td>
									<td style="font-size: 14"><!-- width:225px; -->
										<input id="patientOccupation" class="easyui-combobox" name="patient.patientOccupation"  class="easyui-textbox" >
									</td>
								</tr>
								</tr>
								<tr>
									<td class="honry-lable" style="font-size: 14"><!--width:165px;  -->
										证件类型：
										</td>
									<td style="font-size: 14"><!--width:225px;  -->
										<input id="patientCertificatestype" name="patient.patientCertificatestype" class="easyui-combobox">
									</td>
									<td class="honry-lable" style="font-size: 14"><!-- width:165px; -->
										证件号码：
										</td>
									<td style="font-size: 14"><!-- width:225px; -->
										<input id="patientCertificatesno" name="patient.patientCertificatesno" class="easyui-textbox" data-options="missingMessage:'请填写证件号码!'">
									</td>
								</tr>
								<tr>
									<td class="honry-lable" style="font-size: 14"><!--width:165px;  -->
										民族：
										</td>
									<td style="font-size: 14"><!--width:225px;  -->
										<input id="patientNation" name="patient.patientNation" class="easyui-combobox" >
										<a href="javascript:delSelectedData('patientNation');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
									</td>
									<td class="honry-lable" style="font-size: 14"><!-- width:165px; -->
										国籍：
										</td>
									<td style="font-size: 14"><!--width:225px;  -->
										<input id="patientNationality" name="patient.patientNationality">
										<a href="javascript:delSelectedData('patientNationality');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
									</td>
								</tr>
								<tr>
									<td class="honry-lable" style="font-size: 14"><!-- width:165px; -->
										联系人：
										</td>
									<td style="font-size: 14"><!--width:225px;  -->
										<input id="patientLinkman" name="patient.patientLinkman"  class="easyui-textbox" >
										<td class="honry-lable" style="font-size: 14"><!--width:165px;  -->
										联系人电话：
										</td>
									<td style="font-size: 14"><!--width:225px;  -->
										<input id="linkphone" name="patient.patientLinkphone"  class="easyui-textbox" >
									</td>
								</tr>
								<tr>
									<td class="honry-lable" style="font-size: 14"><!-- width:165px; -->
										单位电话：
										</td>
									<td style="font-size: 14" ><!-- width:225px; -->
										<input id="patientWorkphone" name="patient.patientWorkphone"  class="easyui-textbox"  >
									</td>
									<td class="honry-lable" style="font-size: 14"><!-- width:165px; -->
										</td>
									<td style="font-size: 14" ><!--width:225px;  -->
									</td>
								</tr>
								<tr>
									<td class="honry-lable" style="font-size: 14" ><!--width:165px;  -->
										工作单位：
										</td>
									<td style="font-size: 14" colspan="3"><!--width:225px;  -->
										<input id="patientWorkunit" name="patient.patientWorkunit"  class="easyui-textbox"><!--style="width:730px;"   -->
									</td>
								</tr>
								<tr>
									<td class="honry-lable" style="font-size: 14"><!--width:165px;  -->
										家庭地址：
									</td>
									<td style="font-size: 14" colspan="3"><!-- width:225px; -->
										  <div style="text-align: 25px; ">
											<input  id="homeone"  style="width: 90px"  value="${oneName }" data-options="prompt:'省/直辖市'"/>
											<input  id="hometwo" style="width: 90px"  value="${twoName }" data-options="prompt:'市'"/>
											<input  id="homethree" style="width: 90px"  value="${threeName }" data-options="prompt:'县'"/>
											<input  id="homefour" style="width: 90px"  value="${fourName }" data-options="prompt:'区'"/>
											<input type="hidden" id="patientCity" name="patient.patientCity" value="${patient.patientCity }" >
											<!-- style="width: 300px; " --><input class="easyui-textbox" 
													 id ="patientAddress" value="${patient.patientAddress }"
													name="patient.patientAddress"
													data-options="required:true,missingMessage:'请填写详细地址!',prompt:'社区/乡镇村详细地址'"/>
										</div>
									</td>
								</tr>
							</table>
					 	<div id="dialog"></div>  
					  <div id="toolbarId" style="text-align:center;width:70%;padding:5px;margin-left:auto;margin-right:auto;">
					 <shiro:hasPermission name="${menuAlias}:function:save">	
					 <a href="javascript:submit();void(0)" class="easyui-linkbutton" 
								data-options="iconCls:'icon-save'">保&nbsp;存&nbsp;</a>
			    	</shiro:hasPermission>
			    </div>
			</div>
			</form>
		</div>
		</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<style type="text/css">
.easyui-dialog .panel-header .panel-tool a{
    background-color: red;	
}
</style>
	<script type="text/javascript">
		var SearchFromData=null; //病历号-->患者json信息  
		var district;//获得性别编码Map
		var patterms = new Object();
		//验证日期格式2009-07-13
		/* 修改之前的格式：patterms.date =/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/; */
		patterms.date =/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/;

		patterms.dateValue=/^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)$/;
		
		$(function() {
			
			//根据身份证回车查询
			//bindEnterEvent('queryName', searchCerNoFrom, 'easyui');
			//根据就诊卡号回车查询	
			bindEnterEvent('idcardNode', searchIdcardFrom, 'easyui');
			
			/**
			 * 回车弹出国籍选择窗口
			 * @author  wanxing
			 * @date 2016-03-22 18:40 
			 * @version 1.0
			 */
			 bindEnterEvent('patientNationality',popWinToCountry,'easyui');//绑定回车事件
			 function popWinToCountry(){
				 	$('#patientNationality').combobox('setValue','');
					var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=country&textId=patientNationality";
					window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
			}
			 $.ajax({
					url : "<%=basePath%>baseinfo/district/getDistrictTree.action",
					type : 'post',
					success : function(result) {
						district=result;
					}
				});
					 
			/**
			 * 回车弹出民族选择窗口
			 * @author  wanxing
			 * @date 2016-03-22  18:50
			 * @version 1.0
			 */
			 bindEnterEvent('patientNation',popWinToNationality,'easyui');//绑定回车事件
			 function popWinToNationality(){
					$('#patientNation').combobox('setValue','');
					var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=nationality&textId=patientNation";
					window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
			 }
			 
			 queryDistrictSJLDOne();
				queryDistrictSJLDTwo('');
				queryDistrictSJLDThree('');
				queryDistrictSJLDFour('',false);
		});
		
		function verify(birthDay,pat)
		{
		    var thePat;
		    thePat = patterms[pat];
		    if(thePat.test(birthDay))
		    {
		        return 1;
		    }
		    else
		    {
		        return 0;
		    }
		}
		//提交验证
		function submit() {
			var birthDay=$('#patientBirthday').val(); 
			var aa=verify(birthDay,"date");
			if(aa==0){
				 $.messager.alert('提示',"出生日期格式不正确,格式如1990-01-01");
				return ;
			}else{
				var value=verify(birthDay,"dateValue");
				if(value==0){
					 $.messager.alert('提示',"出生日期不正确");
						return ;
				}
			
			}
			
			//下面这行代码，在不输入身份证时，验证没通过，会一直执行，因此我把他注释了，以后要用，请改善后用2017-04-14
			 $.messager.progress({text:'保存中，请稍后...',modal:true});
			 if (!$('#editForm').form('validate')) {
				    $.messager.progress('close');	
					$.messager.alert('提示',"验证没有通过,不能提交表单!");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}else{
					var ss=$("#patientCertificatesno").val();
					if(ss!=null&&ss!=""){
						if($("#patientCertificatestype").combobox('getText')=='身份证'&& !isIdCardNo($("#patientCertificatesno").val())){
							 $.messager.progress('close');
							return false;
						}else{
							validCheckName();
						}
					}else{
						//2017-04-14：下面这行代码导致没身份证，也能保存，因此我改进了一下
						//submits();
						$.messager.progress('close');
						$.messager.alert('提示',"请输入证件号码!");
						 setTimeout(function(){
							$(".messager-body").window('close');
						},3500); 
						return false;
					}
				}
	 	}
		//验证此身份证号是否已存在
		function validCheckName(){
				$.ajax({
					url : "<%=basePath%>patient/register/checkIdcardName.action",
					data : {
						"name":$("#patientName").textbox('getValue'),
						"certificate":$("#patientCertificatestype").combobox('getValue'),
						"certificatesno":$("#patientCertificatesno").val()
					},
					type : 'post',
					success : function(result) {
						if (result == "no1") {
							check = "nameNo";
						}else{
							check = "";
						}
						if(check==""){
							//2017-04-14加下面的代码为了解决读身份证提交表单时，省市区县数据是""的问题
							var rs =$('#homefour').combobox("getValue")==""?$('#homethree').combobox("getValue"):$('#homefour').combobox("getValue");
							$("#patientCity").val(rs);
							submits();
							//成功后，就诊卡隐藏域设置为""
							$("#hiddenIdcardNode").val("");
						}else{
							 $.messager.progress('close');
							$.messager.alert('提示',"此身份证号已存在,不能提交表单,请修改后提交!");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return;
						}
					}
				});
			}
	
		//提交
		function submits() {
			if ($('#id').val() != "") {
				$('#editForm').form('submit',{
									url : "<%=basePath%>patient/register/patientInfoCollectionSave.action",
									onSubmit : function() {
										if($("#patientPhone").val()&&$("#patientPhone").val()!=""&&!isTelphoneNum($("#patientPhone").val())&&!isMobilephoneNum($("#patientPhone").val())){
											 $.messager.progress('close');	
											 $.messager.alert('提示',"联系电话格式不正确,格式如01088888888,010-88888888,0955-7777777或13800571506!");
											 setTimeout(function(){
													$(".messager-body").window('close');
												},3500);	
											 	return false;
											}
										
										if($("#patientWorkphone").val()!="" && $("#patientWorkphone").val()!=null && !isTelphoneNum($("#patientWorkphone").val())&&!isMobilephoneNum($("#patientWorkphone").val())){
											 $.messager.progress('close');	
											$.messager.alert('提示',"单位电话格式不正确,格式如01088888888,010-88888888,0955-7777777或13800571506!");
											 setTimeout(function(){
													$(".messager-body").window('close');
												},3500);
											return false;
										}
										if($("#linkphone").val()!="" && $("#linkphone").val()!=null && !isTelphoneNum($("#linkphone").val())&&!isMobilephoneNum($("#linkphone").val())){
											 $.messager.progress('close');	
											$.messager.alert('提示',"联系人电话格式不正确,格式如01088888888,010-88888888,0955-7777777或13800571506!");
											 setTimeout(function(){
													$(".messager-body").window('close');
												},3500);
											return false;
										}
										
										if (!$('#editForm').form('validate')) {
											$.messager.show({
												title : '提示信息',
												msg : '验证没有通过,不能提交表单!'
											});
											return false;
										}
									},
									success : function(data) {
										 $.messager.progress('close');
										//成功后，就诊卡隐藏域设置为""
										$("#hiddenIdcardNode").val(""); 
										$.messager.alert('提示','保存成功');
										 setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
										//实现刷新栏目中的数据
										window.location.href = "<%=basePath%>patient/register/listCollection.action?menuAlias=${menuAlias}";
									},
									error : function(data) {
										 $.messager.progress('close');	
										$.messager.alert('提示','保存失败！');
										//实现刷新
										window.location.href = "<%=basePath%>patient/register/listCollection.action?menuAlias=${menuAlias}";
									},
									error : function(data) {
									}
								});
			} else {
				$.messager.alert('提示',"请输入就诊卡号");
				 setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				 $.messager.progress('close');	
				window.location.href = "<%=basePath%>patient/register/listCollection.action?menuAlias=${menuAlias}";
			}
		}
		
		
		
		/**
		 * 查询
		 * @author  wj
		 * @date 2015-7-2 14:52
		 * @version 1.0
		 */
		 
		 function isSearchFrom(){
				var id =$('#idcardNode').val();	
				
				if(id.length<0){
					$.messager.alert('提示','请输入就诊卡号');
					 setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					return;
				}
				$.messager.progress({text:'查询中，请稍后...',modal:true});
				$.ajax({
		   			url: '<%=basePath%>patient/register/isSearchFrom.action?idNo='+id + "&menuAlias=${menuAlias}",
					type:'post',
					success: function(data) {
						var mapList=eval("("+data+")");
						 $.messager.progress('close');	
						if(mapList.resSize>0){
							if(mapList.resSize==1){
								searchFrom(mapList.resData[0].medicalrecordId);
							}else{
								SearchFromData=mapList.resData;
								Adddilog('双击选择患者','<%=basePath%>patient/register/selectDialogURL.action');
							}
						}else{
							$.messager.alert('友情提示','没有该患者或该患者没有挂号');  
							 setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
						}
					}
				});
					
			 }
		 
		function searchFrom(idNode) {
				$.ajax({
						url : '<%=basePath%>patient/register/queryCollectionById.action?idNo='+idNode + "&menuAlias=${menuAlias}",
						type : 'post',
						success : function(data) {
							if (data.flg!=0) {//没有数据默认为0
								var idCardObj = data.collection;
								$('#patientName').textbox('readonly',false);
								/*zhangkui 2017-04-12*/
									$('#id').val(idCardObj.id);
									$('#patientName').textbox('setValue',idCardObj.name);
									$('#patientSex').combobox('setValue',idCardObj.sex);
									//时间格式为2017-04-12
									 var btTime=idCardObj.birthday
									 var birthday=btTime.substring(0,btTime.indexOf(" "));
									$('#patientBirthday').val(birthday);
									$('#patientAddress').textbox('setValue',
											idCardObj.address);
									$('#homeone').combobox('setValue',
											idCardObj.oneCode);							
									queryDistrictSJLDTwo(idCardObj.oneCode);
									$('#hometwo').combobox('setValue',
											idCardObj.twoCode);
									queryDistrictSJLDThree(idCardObj.twoCode);
									$('#homethree').combobox('setValue',
									idCardObj.threeCode);
									if(idCardObj.fourCode==null||idCardObj.fourCode==""){
										$('#homefour').combobox({
											disabled:true
										});
									}else{
										queryDistrictSJLDFour(idCardObj.threeCode,true);
									}
									
									$('#homefour').combobox('setValue',
											idCardObj.fourCode);
									
									$('#patientCity').val(idCardObj.patientCity);
									$('#patientPhone').textbox('setValue',
											idCardObj.phone);
									$('#patientCertificatestype').combobox(
											'setValue', idCardObj.certificatestype);
									$('#patientCertificatesno').textbox('setValue',
											idCardObj.certificatesno);
									$('#patientNationality').combobox('setValue',
											idCardObj.nationality);
									$('#patientNation').combobox('setValue',
											idCardObj.nation);
									$('#patientWorkunit').textbox('setValue',
											idCardObj.workunit);
									$('#patientWarriage').combobox('setValue',
											idCardObj.warriage);
									$('#patientWorkphone').textbox('setValue',
											idCardObj.workphone);
									$('#patientOccupation').combobox('setValue',
											idCardObj.occupation);
									$('#patientLinkman').textbox('setValue',
											idCardObj.linkman);
									$('#linkphone').textbox('setValue',
											idCardObj.linkphone);
							} else {
								$.messager.alert('提示','没有该患者或该患者没有挂号');
								 setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
								$('#queryName').val('');
								$('#patientName').textbox('setValue', '');
								$('#patientSex').combobox('setValue', '');
								$('#patientBirthday').val('');
								$('#patientAddress').textbox('setValue', '');
								$('#patientPhone').textbox('setValue', '');
								$('#patientCertificatestype').combobox(
										'setValue', '');
								$('#patientCertificatesno').textbox('setValue',
										'');
								$('#patientNationality').combobox('setValue',
										'');
								$('#patientNation').combobox('setValue', '');
								$('#patientWorkunit').textbox('setValue', '');
								$('#patientWarriage').textbox('setValue', '');
								$('#patientWorkphone')
										.textbox('setValue', '');
								$('#patientOccupation')
										.combobox('setValue', '');
								$('#patientLinkman').textbox('setValue', '');
								$('#linkphone').textbox('setValue', '');
							}
						}
					});

		}

		//就诊卡号读卡查询zpty20160602
		function searchIdcardFrom() {
			var idcardNode =$('#idcardNode').val();	
			$.ajax({
						url : "<%=basePath%>patient/register/queryCollectionByIdcard.action?idcardNo="+idcardNode + "&menuAlias=${menuAlias}",
						type : 'post',
						success : function(data) {
							if (data.flg!=0) {//没有数据默认为0
								var idCardObj = data.collection;
								$('#id').val(idCardObj.id);
								$('#patientName').textbox('setValue',
										idCardObj.name);
								$('#patientSex').combobox('setValue',
										idCardObj.sex);
								$('#patientBirthday').val(idCardObj.birthday.substring(0, 10));
								$('#patientAddress').textbox('setValue',
										idCardObj.address);
								
								$('#homeone').combobox('setValue',
										idCardObj.oneCode);							
								queryDistrictSJLDTwo(idCardObj.oneCode);
								$('#hometwo').combobox('setValue',
										idCardObj.twoCode);
								queryDistrictSJLDThree(idCardObj.twoCode);
								$('#homethree').combobox('setValue',
								idCardObj.threeCode);
								if(idCardObj.fourCode==null||idCardObj.fourCode==""){
									$('#homefour').combobox({
										disabled:true
									});
								}else{
									queryDistrictSJLDFour(idCardObj.threeCode,true);
									$('#homefour').combobox({
										disabled:false
									});
								}
								
								$('#homefour').combobox('setValue',
										idCardObj.fourCode);
								
								$('#patientCity').val(idCardObj.patientCity);
								$('#patientPhone').textbox('setValue',
										idCardObj.phone);
								$('#patientCertificatestype').combobox(
										'setValue', idCardObj.certificatestype);
								$('#patientCertificatesno').textbox('setValue',
										idCardObj.certificatesno);
								$('#patientNationality').combobox('setValue',
										idCardObj.nationality);
								$('#patientNation').combobox('setValue',
										idCardObj.nation);
								$('#patientWorkunit').textbox('setValue',
										idCardObj.workunit);
								$('#patientWarriage').combobox('setValue',
										idCardObj.warriage);
								$('#patientWorkphone').textbox('setValue',
										idCardObj.workphone);
								$('#patientOccupation').combobox('setValue',
										idCardObj.occupation);
								$('#patientLinkman').textbox('setValue',
										idCardObj.linkman);
								$('#linkphone').textbox('setValue',
										idCardObj.linkphone);
							} else {
								$.messager.alert('提示','没有该患者或该患者没有挂号');
								 setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
								$('#queryName').val('');
								$('#patientName').textbox('setValue', '');
								$('#patientSex').combobox('setValue', '');
								$('#patientBirthday').val('');
								$('#patientAddress').textbox('setValue', '');
								$('#patientPhone').textbox('setValue', '');
								$('#patientCertificatestype').combobox(
										'setValue', '');
								$('#patientCertificatesno').textbox('setValue',
										'');
								$('#patientNationality').combobox('setValue',
										'');
								$('#patientNation').combobox('setValue', '');
								$('#patientWorkunit').textbox('setValue', '');
								$('#patientWarriage').textbox('setValue', '');
								$('#patientWorkphone')
										.textbox('setValue', '');
								$('#patientOccupation')
										.combobox('setValue', '');
								$('#patientLinkman').textbox('setValue', '');
								$('#linkphone').textbox('setValue', '');
							}
						}
					});

		}
		
		//身份证号读卡查询zpty20160602
		function searchCerNoFrom(cerNoNode) {
			var cerNoNode =$('#queryName').val();	
			$.ajax({
						url : "<%=basePath%>patient/register/queryCollectionByCerNo.action?cerNo="+cerNoNode + "&menuAlias=${menuAlias}",
						type : 'post',
						success : function(data) {
							if (data.flg!=0) {//没有数据默认为0
								var idCardObj = data.collection;
								$('#id').val(idCardObj.id);
								$('#patientName').textbox('setValue',
										idCardObj.name);
								$('#patientSex').combobox('setValue',
										idCardObj.sex);
								$('#patientBirthday').val(idCardObj.birthday);
								$('#patientAddress').textbox('setValue',
										idCardObj.address);
								$('#patientAddress').textbox('setValue',
										idCardObj.address);
								$('#patientAddress').textbox('setValue',
										idCardObj.address);
								$('#patientAddress').textbox('setValue',
										idCardObj.address);
								$('#patientAddress').textbox('setValue',
										idCardObj.address);
								
								$('#patientPhone').textbox('setValue',
										idCardObj.phone);
								$('#patientCertificatestype').combobox(
										'setValue', idCardObj.certificatestype);
								$('#patientCertificatesno').textbox('setValue',
										idCardObj.certificatesno);
								$('#patientNationality').combobox('setValue',
										idCardObj.nationality);
								$('#patientNation').combobox('setValue',
										idCardObj.nation);
								$('#patientWorkunit').textbox('setValue',
										idCardObj.workunit);
								$('#patientWarriage').combobox('setValue',
										idCardObj.warriage);
								$('#patientWorkphone').textbox('setValue',
										idCardObj.workphone);
								$('#patientOccupation').combobox('setValue',
										idCardObj.occupation);
								$('#patientLinkman').textbox('setValue',
										idCardObj.linkman);
								$('#linkphone').textbox('setValue',
										idCardObj.linkphone);
							} else {
								$.messager.alert('提示','没有该患者或该患者没有挂号');
								 setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
								$('#queryName').val('');
								$('#patientName').textbox('setValue', '');
								$('#patientSex').combobox('setValue', '');
								$('#patientBirthday').val('');
								$('#patientAddress').textbox('setValue', '');
								$('#patientPhone').textbox('setValue', '');
								$('#patientCertificatestype').combobox(
										'setValue', '');
								$('#patientCertificatesno').textbox('setValue',
										'');
								$('#patientNationality').combobox('setValue',
										'');
								$('#patientNation').combobox('setValue', '');
								$('#patientWorkunit').textbox('setValue', '');
								$('#patientWarriage').textbox('setValue', '');
								$('#patientWorkphone')
										.textbox('setValue', '');
								$('#patientOccupation')
										.combobox('setValue', '');
								$('#patientLinkman').textbox('setValue', '');
								$('#linkphone').textbox('setValue', '');
							}
						}
					});

		}
		
		
		//适用性别
		
		$('#patientSex').combobox({
		 url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=sex'/>",
	        valueField : 'encode',
            textField : 'name',
			multiple : false
		});
		//证件类型
		$('#patientCertificatestype').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=certificate'/>",
			valueField : 'encode',
			textField : 'name',
			multiple : false,
		});

		//国籍
		$('#patientNationality').combobox(
				{
					url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=country'/>",
					valueField : 'encode',
					textField : 'name',
					filter:function(q,row){
						var keys = new Array();
						keys[keys.length] = 'encode';
						keys[keys.length] = 'name';
						keys[keys.length] = 'pinyin';
						keys[keys.length] = 'wb';
						return filterLocalCombobox(q, row, keys);
				    },
				    multiple : false
				});

		//民族
		$('#patientNation').combobox(
				{
					url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=nationality'/>",
					valueField : 'encode',
					textField : 'name',
					filter:function(q,row){
						var keys = new Array();
						keys[keys.length] = 'encode';
						keys[keys.length] = 'name';
						keys[keys.length] = 'pinyin';
						keys[keys.length] = 'wb';
						return filterLocalCombobox(q, row, keys);
				    },
					multiple : false

				});

		//婚姻情况 
		$('#patientWarriage').combobox(
				{
					url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=marry'/>",
					valueField : 'encode',
					textField : 'name',
					multiple : false,
					onChange : function(post) {
						$('#patientWarriage').combobox('reload', "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=marry'/>");
					}

				});
		//职业
		$('#patientOccupation').combobox(
				{
					url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=occupation'/>",
					valueField : 'encode',
					textField : 'name',
					filter:function(q,row){
						var keys = new Array();
						keys[keys.length] = 'encode';
						keys[keys.length] = 'name';
						keys[keys.length] = 'pinyin';
						keys[keys.length] = 'wb';
						return filterLocalCombobox(q, row, keys);
				    },
					multiple : false

				});
		//加载dialog
		function Adddilog(title, url) {
			$('#dialog').dialog({    
			    title: title,    
			    width: '40%',    
			    height:'40%',    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true   
		   });    
		}
		//打开dialog
		function openDialog() {
			$('#dialog').dialog('open'); 
		}
		//关闭dialog
		function closeDialog() {
			$('#dialog').dialog('close');  
		}
		 /**
		   * 回车弹出看诊科室弹框
		   * @author  zhuxiaolu
		   * @param deptIsforregister 是否是挂号科室 1是 0否
		   * @param textId 页面上commbox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
		  
	 function popWinToDept(){
			var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?textId=dept&deptIsforregister=1&deptType=C";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
		}
	 

	
	 /**
	   * 回车弹出看诊医师弹框
	   * @author  zhuxiaolu
	   * @param deptIsforregister 是否是挂号科室 1是 0否
	   * @param textId 页面上commbox的的id
	   * @date 2016-03-22 14:30   
	   * @version 1.0
	   */
	   function popWinToEmployee(){
			var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=empDoctor&employeeType=1";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
			
	 }
	 
	   function District(value){
			for (var i = 0; i < district.length; i++) {
				if(district[i].cityCode==value){
					return district[i].cityName;
				}
			}
		}
		
		
  //省市区三级联动
	function queryDistrictSJLDFour(id,bool) {
		if(id===''){
			$('#homefour').combobox({
				data:[]
			})
			bindEnterEvent('homefour',popWinToDistrictFours,'easyui');
			return;
		}
		$('#homefour').combobox({  
			url: "<%=basePath%>baseinfo/district/queryDistrictTreeFour.action?parId="+id,    
			valueField:'cityCode',    
			    textField:'cityName',
				required:bool,    
			    multiple:false,
			    editable:true,
			    disabled:bool,
			    onSelect:function(node) {
			    	$('#patientCity').val(node.cityCode);
			    }
		});
			bindEnterEvent('homefour',popWinToDistrictFours,'easyui');
	}
	function queryDistrictSJLDThree(id) {
		if(id===''){
			$('#homethree').combobox({
				data:[]
			})
			bindEnterEvent('homethree',popWinToDistrictThrees,'easyui');
			return;
		}
		$('#homethree').combobox({  
			url: "<%=basePath%>baseinfo/district/queryDistrictTreeThree.action?parId="+id,    
				valueField:'cityCode',    
			    textField:'cityName',
				required:true,    
			    multiple:false,
			    editable:true,
			     onSelect:function(node) {
			     	var bool = true;
			     	if(node.cityName=="市辖区"){
			     		bool=false;
			     		$('#homefour').combobox({
							required: true,
							value:'',
							disabled:bool
						});
			     	}else{
			     		$('#patientCity').val(node.cityCode);
			     		$('#homefour').combobox({
							required: false,
							value:'',
							disabled:true
						});
			     	}				     	
		        	queryDistrictSJLDFour(node.cityCode,bool);
		        }
		});
		bindEnterEvent('homethree',popWinToDistrictThrees,'easyui');
	}
	function queryDistrictSJLDTwo(id) {
		if(id===''){
			$('#hometwo').combobox({
				data:[]
			})
			bindEnterEvent('hometwo',popWinToDistrictTwos,'easyui');//绑定回车事件
			return;
		}
		$('#hometwo').combobox({  
			url: "<%=basePath%>baseinfo/district/queryDistrictTreeTwo.action?parId="+id,    
				valueField:'cityCode',    
			    textField:'cityName',
				required:true,    
			    multiple:false,
			    editable:true,
			    onSelect:function(node) {
		        	queryDistrictSJLDThree(node.cityCode);
					$('#homefour').combobox('setValue','');
		        }
		});
		bindEnterEvent('hometwo',popWinToDistrictTwos,'easyui');//绑定回车事件
	}
		function queryDistrictSJLDOne() {
			$('#homeone').combobox({  
				url: "<%=basePath%>baseinfo/district/queryDistrictTreeOne.action?parId=",
				valueField:'cityCode',    
			    textField:'cityName',
				required:true,    
			    multiple:false,
			    editable:true,
			    onSelect:function(node) {
					queryDistrictSJLDTwo(node.cityCode);
					$('#homethree').combobox('setValue','');
					$('#homefour').combobox('setValue','');
		        }
			});
			bindEnterEvent('homeone',popWinToDistrict,'easyui');//绑定回车事件
		}
		
		 /**
		* 回车弹出地址一级选择窗口
		* @author  zhuxiaolu
		* @param textId 页面上commbox的的id
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/

		function popWinToDistrict(){
			$('#hometwo').textbox('setValue','');
			$('#homethree').textbox('setValue','');
			$('#homefour').textbox('setValue','');
			var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=homeone&level=1&parentId=1";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 

		+',scrollbars,resizable=yes,toolbar=yes')
		}
		/**
		* 回车弹出地址二级选择窗口
		* @author  zhuxiaolu
		* @param textId 页面上commbox的的id
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/

		function popWinToDistrictTwos(){
			$('#homethree').textbox('setValue','');
			$('#homefour').textbox('setValue','');
			var parentId=$('#homeone').textbox('getValue');
			if(!parentId){
				$.messager.alert('提示','请选择省/直辖市');  
				 setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				
			}else{
				var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=hometwo&level=2&parentId="+parentId;
				window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 
				+',scrollbars,resizable=yes,toolbar=yes')
			}
		}
		/**
		* 回车弹出地址三级选择窗口
		* @author  zhuxiaolu
		* @param textId 页面上commbox的的id
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/

		function popWinToDistrictThrees(){
			$('#homefour').textbox('setValue','');
			var parentId=$('#hometwo').textbox('getValue');
			if(!parentId){
				$.messager.alert('提示','请选择市');  
				 setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				
			}else{
				var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=homethree&level=3&parentId="+parentId;
				window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 
				+',scrollbars,resizable=yes,toolbar=yes')
			}
		}
		/**
		* 回车弹出地址四级选择窗口
		* @author  zhuxiaolu
		* @param textId 页面上commbox的的id
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/

		function popWinToDistrictFours(){
			var parentId=$('#homethree').textbox('getValue');
			if(!parentId){
				$.messager.alert('提示','请选择县');  
				 setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				
			}else{
				var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=homefour&level=4&parentId="+parentId;
				window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 
				+',scrollbars,resizable=yes,toolbar=yes')
			}
		}
		
		
		
		/*******************************开始读卡***********************************************/
		//2017-09-09读就诊卡
		function read_card_ic(){
			var card_value=app.read_ic();
			if(card_value=='0'||card_value==undefined||card_value=='')
			{
				$.messager.alert('提示','此卡号['+card_value+']无效');
				return;
			}
			
			$("#idcardNode").textbox("setValue",card_value);
			$("#hiddenIdcardNode").val(card_value);
			//去后台查询
			isSearchFrom();
		}
 	/*******************************开始读身份证***********************************************/
		
 		//2017-09-09读身份证
		function read_card_sfz(){
			//读身份证前，先让其读就诊卡,保存完毕让就诊卡号隐藏域的值为""
			if($("#hiddenIdcardNode").val()!=null&&$("#hiddenIdcardNode").val()!=""){
					
					var card_value=app.read_sfz_all().trim();//朱x             ,男,汉,1963年06月15日,重庆市沙坪坝区建工东村83号1-3                  ,510211196306151xxx,重庆市公安局沙坪坝分局    ,2008年03月05日,2028年03月05日,D:\cef\dcef3-2623\bin\Win32\510211196306151256.Bmp
					var cards = '';
					var patientCity='';//身份证前六位
					
					if(card_value=='0'||card_value==undefined||card_value=='')
					{
						$.messager.alert('提示','此卡号['+card_value+']无效');
						return;
					}else{
						cards = card_value.split(',');
						patientCity = cards[5].trim().substring(0,6);
					}
					if(cards==undefined||cards==''){
						$.messager.alert('提示','此卡号['+card_value+']无效');
						return;
					}
					
					$.ajax({
						url: "<%=basePath%>patient/idcard/readInfoIdcard.action",
						data:"patientCity="+patientCity,
						type:'post',
						success: function(msgData) {
							//设置其余的信息
							//姓名
							$("#patientName").textbox("setValue",cards[0].trim());
							//性别
							if(cards[1].trim()=="男"){
			  					$('#patientSex').combobox('select','1');
			  					//alert("男");
			 				}else if(cards[1].trim()=="女"){
								$('#patientSex').combobox('select','2');
			 				}else{
			 					$('#patientSex').combobox('select','3');
			 				} 
//			 				//年龄
							var bt1=cards[3].trim().replace(/(\D+)/g,"-");
							var birthday=bt1.substring(0,bt1.length-1);
							$('#patientBirthday').val(birthday);
//			 				//证件类型
			  				$("#patientCertificatestype").combobox("select","3");
//			 				//证件号码
			 				$("#patientCertificatesno").textbox("setValue",cards[5].trim());
							//3.把身份证前六位代码设置为后台病人的所在城市，处理完毕返回省市区代码
							//4.请求成功得到oneCode,twoCode,threeCode,fourCode,进行设置，回显
							var cty_nodes=msgData.split(",");
							//省市区
							queryDistrictSJLDOne();
							$('#homeone').combobox('select',cty_nodes[0]);
							queryDistrictSJLDTwo(cty_nodes[0]);
							$('#hometwo').combobox('setValue',cty_nodes[1]);
							queryDistrictSJLDThree(cty_nodes[1]);
							$('#homethree').combobox('setValue',cty_nodes[2]);
							//如果为空禁用第四级
							var flag=cty_nodes[3]==""?true:false;
							queryDistrictSJLDFour(cty_nodes[2],flag);
							$('#homefour').combobox('setValue',cty_nodes[3]);
							//5.请求成功，发送ajax请求把身份证上民族"字符"发送到后台得到民资代码，进行回显
							//如果是汉就不用读表了
							if(cards[2].trim()=="汉"){
								$('#patientNation').combobox('setValue',"01");
							}else{
								//否则读表，查民族的代码
								$.ajax({
									url: "<%=basePath%>baseinfo/pubCodeMaintain/queryPubCode.action",
									data:"dictionary.name="+cards[2].trim(),
									type:'post',
									async:false,
									success:function(msgData){
										//得到民族代码
										var nations=msgData.rows;
										//民族
										$('#patientNation').combobox('select',nations[0].encode);
										}
								});
							}
							//6.国籍,如果市不为"",说明是中国人
							if($("#hometwo").combobox("getValue")!=""){
								$('#patientNationality').combobox('setValue',"1");
							}
						}
					  
					});
			}else{
				 $.messager.alert('提示',"请先读就诊卡号！");
			 }
			
		}
 		
		/*******************************结束读身份证***********************************************/	 
	
	 
	 
		
		
			
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>