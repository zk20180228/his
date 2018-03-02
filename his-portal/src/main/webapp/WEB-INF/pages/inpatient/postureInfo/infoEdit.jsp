<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<body>
		<div class="easyui-layout" style="width:100%;height:100%;">
		<div data-options="region:'center'" >
			<div id="divLayout" class="easyui-layout" fit=true>
				<div data-options="region:'center',split:false,title:'患者接诊',iconCls:'icon-book'" style="padding:5px;">
					 <fieldset>
					<table class="honry-table" cellpadding="1" cellspacing="1"	border="1px solid black">
						<tr>
								<td  style="width: 15%;" class="honry-lable" >
									病历号：
								</td>
									<input class="easyui-textbox" id="queryInpatientNo" name="queryInpatientNo"  onkeydown="KeyDown()"/>&nbsp;&nbsp;
									<shiro:hasPermission name="HZJZ:function:query">
									<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								    </shiro:hasPermission>
								</td>
							</tr>
					</table>
					</fieldset>
					<br/>
				<form id="editForm">
				    <fieldset>
				    <legend><font style="font-weight: bold;font-size: 12px;">基本信息</font></legend>
					<table class="honry-table" cellpadding="1" cellspacing="1"	border="1px solid black">
						<tr>
							<td>
							<shiro:hasPermission name="HZJZ:function:edit">
							<a href="javascript:submit();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>&nbsp;&nbsp;
							</shiro:hasPermission>
							<a href="javascript:clear();void(0)" data-options="iconCls:'icon-clear'" class="easyui-linkbutton">清空</a></td>
						</tr>
					</table>
					<table  class="honry-table" cellpadding="1" cellspacing="1"	border="1px solid black" id="list">
						   <tr>					
								<td class="honry-lable">姓名:</td>
				    			<td>
				    			<input type="hidden" name="inPatientInfoId" id="inPatientInfoId" value=""/>
				    			<input class="easyui-textbox" id="patientName" name="patientName" value="" data-options="required:true"  missingMessage="请输入姓名"/></td>
							    <td class="honry-lable">性别:</td>
			    				<td><input id="CodeSex" name="reportSex" value="" onkeydown="KeyDown(0,'CodeSex')" data-options="required:true"   missingMessage="请输入性别"/></td>
								 <td class="honry-lable">就诊卡号：</td>
							   <td><input class="easyui-textbox" id="idcardNo" name="idcardNo" value="" data-options="required:true"  missingMessage="请输入就诊卡号" onkeydown="KeyDownByp('idcardNo')"/></td>
						       <td class="honry-lable">结算方式:</td><%--结算类别 --%>
    							<td><input id="CodeSettlement" name="paykindCode" value="" onkeydown="KeyDown(0,'CodeSettlement')" data-options="required:true"  missingMessage="请输入结算方式"/></td>
							</tr>
							<tr>
								<td class="honry-lable">出生地:</td><%--出生地代码 --%>
				    			<td><input class="easyui-textbox" id="birthArea" name="birthArea" value="" data-options="required:true"  missingMessage="请输入出生地"/></td>
								<td class="honry-lable">国籍:</td>
				    			<td><input id="CodeCountry" name="counCode" value="" onkeydown="KeyDown(0,'CodeCountry')" data-options="required:true"  missingMessage="请输入国籍"/></td>
								  <td class="honry-lable">民族:</td>
			    				 <td colspan="3"><input id="CodeNationality" name="nationCode" value="" onkeydown="KeyDown(0,'CodeNationality')" data-options="required:true"  missingMessage="请输入民族"/></td>
		    			    </tr>
							<tr>
							<td class="honry-lable">出生日期:</td>
    					    <td><input class="easyui-datebox" id="reportBirthday" name="reportBirthday" value="" data-options="required:true"  missingMessage="请输入出生日期"/></td>					
							 <td class="honry-lable">年龄:</td>
			    			 <td><input class="easyui-numberbox" id="reportAge" name="reportAge" value="" data-options="required:true"  missingMessage="请输入年龄"/></td>
							 <td class="honry-lable">籍贯:</td>
			    			 <td colspan="3"><input class="easyui-textbox" id="dist" name="dist" value="" data-options="required:true"  missingMessage="请输入籍贯"/></td>
							</tr>
							<tr>
							   <td class="honry-lable">职业:</td>
			    			   <td ><input  id="CodeOccupation" name="profCode" value="" onkeydown="KeyDown(0,'CodeOccupation')" data-options="required:true"   missingMessage="请输入职业"/></td>
					           <td class="honry-lable">身份证号码:</td><%--证件号码 --%>
			    			   <td><input class="easyui-numberbox" id="certificatesNo" name="certificatesNo" value="" data-options="required:true"  missingMessage="请输入身份证号码"/></td>
						       <td class="honry-lable">工作单位:</td>
			    			   <td colspan="3"><input class="easyui-textbox" id="workName" name="workName" value="" data-options="required:true"  missingMessage="请输入工作单位"/></td>
							</tr>
							<tr>
							   <td class="honry-lable">单位电话:</td><%--工作单位电话--%>
			    			   <td><input class="easyui-numberbox" id="workTel" name="workTel" value="" data-options="required:true"  missingMessage="请输入单位电话"/></td>
							   <td class="honry-lable">家庭地址:</td><%--户口家庭住址--%>
			    			   <td><input class="easyui-textbox" id="home" name="home" value="" data-options="required:true"   missingMessage="请输入家庭地址"/></td>
								<td class="honry-lable">家庭电话:</td>
			    			   <td colspan="3"><input class="easyui-numberbox" id="homeTel" name="homeTel" value=""   data-options="required:true" missingMessage="请输入家庭电话"/></td>
							</tr>
					        <tr>					
								<td class="honry-lable">婚姻状况:</td>
				    			<td><input id="CodeMarry" name="mari" value="" onkeydown="KeyDown(0,'CodeMarry')" data-options="required:true"  missingMessage="请输入婚姻状况"/></td>
								<td class="honry-lable">联系人:</td><%--联系人姓名--%>
				    			<td><input class="easyui-textbox" id="linkmanName" name="linkmanName" value="" data-options="required:true"  missingMessage="请输入联系人"/></td>
								<td class="honry-lable">关系:</td>
				    			<td><input  id="CodeRelation" name="relaCode" value="" onkeydown="KeyDown(0,'CodeRelation')" data-options="required:true"   missingMessage="请输入关系"/></td>
								<td class="honry-lable">联系人电话:</td>
			    			    <td><input class="easyui-numberbox" id="linkmanTel" name="linkmanTel" value="" data-options="required:true"  missingMessage="请输入联系人电话"/></td>
							</tr>
							<tr>
								<td class="honry-lable">联系人地址:</td>
				    			<td colspan="8"><input class="easyui-textbox" id="linkmanAddress" name="linkmanAddress" value="" style="width:60%;"  data-options="required:true"  missingMessage="请输入联系人地址"/></td>
		    			    </tr>
					</table>
					</fieldset><br/>
					<fieldset>
					<legend><font style="font-weight: bold;font-size: 12px;">登记信息</font></legend>
					<table class="honry-table" cellpadding="1" cellspacing="1"	border="1px solid black">
						</table>
						<table  class="honry-table" cellpadding="1" cellspacing="1"	border="1px solid black" id="list">
						 <tr>
					            <td class="honry-lable">入院来源:</td>
				    			<td ><input  id="CodeSourse" name="inSource" value="" onkeydown="KeyDown(0,'CodeSourse')" data-options="required:true"  missingMessage="请输入入院来源"/></td>
								<td class="honry-lable">入院途径:</td>
				    			<td><input class="easyui-textbox" id="inAvenue" name="inAvenue" value="" data-options="required:true"  missingMessage="请输入入院途径"/></td>
   								<td class="honry-lable">入院情况:</td>
				    			<td><input class="easyui-textbox" id="inCircs" name="inCircs" value="" data-options="required:true"   missingMessage="请输入入院情况"/></td>
								<td class="honry-lable">床费间隔:</td>
				    			<td>&nbsp;</td>
								</tr>
						<tr>
						        <td class="honry-lable">入院日期:</td>
    							<td><input class="easyui-datebox" id="inDate" name="inDate" value="" data-options="required:true"  missingMessage="请输入入院日期"/></td>
								<td class="honry-lable">管床医生:</td>
				    			<td>&nbsp;</td>
				    			<td class="honry-lable">备注:</td>
    							<td colspan="3"><input class="easyui-textbox" id="remark" name="remark" value="" data-options="multiline:true" style="width:70%;height:60px;" missingMessage="请输入备注"/></td>
						</tr>
						<tr>
						        <td class="honry-lable">入住科室:</td><%--科室代码--%>
    			                <td><input class="easyui-textbox" id="deptCode" name="deptCode" value="" data-options="required:true"   missingMessage="请输入入住科室"/></td>
								<td class="honry-lable">开立住院证医生:</td><%--开据医师--%>
    			                <td colspan="5"><input class="easyui-textbox" id="emplCode" name="emplCode" value="" data-options="required:true"  missingMessage="请输入开立住院证医生"/></td>
						</tr>
						<tr>
						         <td class="honry-lable">入院诊断:</td><%--诊断名称--%>
    			                 <td colspan="7"><input class="easyui-textbox" id="diagName" name="diagName" value="" data-options="required:true" style="width:60%"   missingMessage="请输入入院诊断"/></td>
						</tr>		
					</table>
					</fieldset><br/>
					<fieldset>
					 <legend><font style="font-weight: bold;font-size: 12px;">生命体征</font></legend>
				   <table  class="honry-table" cellpadding="1" cellspacing="1"	border="1px solid black" id="list">
						 <tr>
					            <td class="honry-lable">体温:</td>
				    			<td>
				    			<input type="hidden" id="postrueInfoId" name="postrueInfoId" value=""/>
				    			<input type="hidden" id="patientNo" name="patientNo" value=""/>
				    			<input class="easyui-textbox" id="temperature" name="temperature" value="" data-options="required:true"   missingMessage="请输入体温"/></td>
								<td class="honry-lable">脉搏:</td>
				    			<td><input class="easyui-textbox" id="pulse" name="pulse" value="" data-options="required:true"  missingMessage="请输入脉搏"/></td>
   								<td class="honry-lable">呼吸:</td>
				    			<td><input class="easyui-textbox" id="breath" name="breath" value="" data-options="required:true"   missingMessage="请输入呼吸"/></td>
   						</tr>
   						 <tr>
					            <td class="honry-lable">血压:</td>
				    			<td><input class="easyui-textbox" id="pressure" name="pressure" value="" data-options="required:true"  missingMessage="请输入血压"/></td>
								<td class="honry-lable">体重（kg）:</td>
				    			<td><input class="easyui-numberbox" id="weight" name="weight" value="" data-options="required:true"  missingMessage="请输入体重"/></td>
   								<td class="honry-lable">录入时间:</td>
				    			<td ><input class="easyui-datebox" id="postureDate" name="postureDate" value="" data-options="required:true" s missingMessage="请输入录入时间"/></td>
   						</tr>
					</table>
					</fieldset>
				</form>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	    
		 $(function(){
			 //初始化下拉框
			idCombobox("CodeSex");
	        idCombobox("CodeOccupation");
			idCombobox("CodeNationality");
			idCombobox("CodeRelation");
			idCombobox("CodeMarry");
			idCombobox("CodeSettlement");
			idCombobox("CodeSourse");
			idCombobox("CodeCountry");
		});
	   //从xml文件中解析，读到下拉框
		function idCombobox(param){
			$('#'+param).combobox({
			    url:'comboBox.action?str='+param,    
			    valueField:'id',    
			    textField:'name',
			    multiple:false
			});
		}
		var settlementkeydown = $('#CodeSettlement').combobox('textbox'); 
		settlementkeydown.keyup(function(){
			KeyDown(0,"CodeSettlement");
		});
		var nationalitykeydown = $('#CodeNationality').combobox('textbox'); 
		nationalitykeydown.keyup(function(){
			KeyDown(0,"CodeNationality");
		});
		var occupationkeydown = $('#CodeOccupation').combobox('textbox'); 
		occupationkeydown.keyup(function(){
			KeyDown(0,"CodeOccupation");
		});
		var soursekeydown = $('#CodeSourse').combobox('textbox'); 
		soursekeydown.keyup(function(){
			KeyDown(0,"CodeSourse");
		});
		var marrykeydown = $('#CodeMarry').combobox('textbox'); 
		marrykeydown.keyup(function(){
			KeyDown(0,"CodeMarry");
		});
		var relationkeydown = $('#CodeRelation').combobox('textbox'); 
		relationkeydown.keyup(function(){
			KeyDown(0,"CodeRelation");
		});
	    var sexkeydown = $('#CodeSex').combobox('textbox'); 
		sexkeydown.keyup(function(){
		});
		
		var countrydown = $('#CodeCountry').combobox('textbox'); 
		countrydown.keyup(function(){
			KeyDown(0,"CodeCountry");
		});
		function KeyDown(flg,tag){ 	    	
	    	if(flg==1){//回车键光标移动到下一个输入框
		    	if(event.keyCode==13){	
		    		event.keyCode=9;
		    	}
		    } 
		    if(flg==0){	//空格键打开弹出窗口
			    if (event.keyCode == 32)  
			    { 
			        event.returnValue=false;  
			        event.cancel = true; 
			        if(tag=="CodeMedicaltype"){
			        	showWin("请医疗类型","<c:url value='/ComboxOut.action'/>?xml="+"CodeMedicaltype,0","50%","80%");
			        }
			        if(tag=="CodeCertificate"){
			        	showWin("请写证件类型","<c:url value='/ComboxOut.action'/>?xml="+"CodeCertificate,0","50%","80%");
			        }
			        if(tag=="CodeOccupation"){
			        	showWin("请写职业代码","<c:url value='/ComboxOut.action'/>?xml="+"CodeOccupation,0","50%","80%");
			        }
			        if(tag=="CodeNationality"){
			        	showWin("请写民族","<c:url value='/ComboxOut.action'/>?xml="+"CodeNationality,0","50%","80%");
			        }
			        if(tag=="CodeRelation"){
			        	showWin("请写关系","<c:url value='/ComboxOut.action'/>?xml="+"CodeRelation,0","50%","80%");
			        }
			        if(tag=="CodeMarry"){
			        	showWin("请写婚姻状况","<c:url value='/ComboxOut.action'/>?xml="+"CodeMarry,0","50%","80%");
			        }
			        if(tag=="CodeBloodtype"){
			        	showWin("请写血型","<c:url value='/ComboxOut.action'/>?xml="+"CodeBloodtype,0","50%","80%");
			        }
			        if(tag=="CodeSettlement"){
			        	showWin("请写结算方式","<c:url value='/ComboxOut.action'/>?xml="+"CodeSettlement,0","50%","80%");
			        }
			        if(tag=="CodeSourse"){
			        	showWin("请写入院来源","<c:url value='/ComboxOut.action'/>?xml="+"CodeSourse,0","50%","80%");
			        }
			        if(tag=="CodeNurselevel"){
			        	showWin("请写护理级别","<c:url value='/ComboxOut.action'/>?xml="+"CodeNurselevel,0","50%","80%");
			        }
			        if(tag=="CodeSex"){
			        	showWin("请写性别","<c:url value='/ComboxOut.action'/>?xml="+"CodeSex,0","50%","80%");
			        }
			        if(tag=="CodeCountry"){
			        	showWin("请写国籍","<c:url value='/ComboxOut.action'/>?xml="+"CodeCountry,0","50%","80%");
			        }
			    }
		    }
		} 
				
		/**
		 * 表单提交
		 * @author  hedong
		 * @date 2015-08-12
		 * @version 1.0
		 */
		  	function submit(){
				var inPatientInfoId = $('#inPatientInfoId').val();
				if(inPatientInfoId){
				 	$('#editForm').form('submit',{
				  		url:'editInpatientPostureInfo.action',
				  		 onSubmit:function(){ 
				  		 	if(!$('#editForm').form('validate')){
								$.messager.show({  
								     title:'提示信息' ,   
								     msg:'验证没有通过,不能提交表单!'  
								}); 
								   return false ;
						     }
						 },  
						success:function(data){
							$.messager.alert('提示','保存成功！','info');
							$('#queryInpatientNo').textbox('setValue','');
						    $('#patientName').textbox('setValue','');
						    $('#CodeSex').combobox('setValue','');
						    $('#inDate').textbox('setValue','');
						    $('#idcardNo').textbox('setValue','');
						    $('#CodeSettlement').combobox('setValue','');
						    $('#birthArea').textbox('setValue','');
						    $('#CodeCountry').combobox('setValue','');
						    $('#CodeNationality').combobox('setValue','');
						    $('#dist').textbox('setValue','');
						    $('#CodeOccupation').combobox('setValue','');
						    $('#CodeSourse').combobox('setValue','');
						    $('#inAvenue').textbox('setValue','');
						    $('#inCircs').textbox('setValue','');
						    $('#postrueInfoId').val('');
						    $('#patientNo').val('');
						    $('#temperature').textbox('setValue','');
						    $('#pulse').textbox('setValue','');
						    $('#breath').textbox('setValue','');
						    $('#pressure').textbox('setValue','');
						    $('#weight').textbox('setValue','');
						    $('#postureDate').textbox('setValue','');
						    $('#reportBirthday').textbox('setValue','');
						    $('#reportAge').textbox('setValue','');
						    $('#certificatesNo').textbox('setValue','');
						    $('#workName').textbox('setValue','');
						    $('#workTel').textbox('setValue','');
						    $('#home').textbox('setValue','');
						    $('#homeTel').textbox('setValue','');
						    $('#CodeMarry').combobox('setValue','');
						    $('#linkmanName').textbox('setValue','');
						    $('#CodeRelation').combobox('setValue','');
						    $('#linkmanAddress').textbox('setValue','');
						    $('#linkmanTel').textbox('setValue','');
						    $('#remark').textbox('setValue','');
						    $('#deptCode').textbox('setValue','');
						    $('#emplCode').textbox('setValue','');
						    $('#diagName').textbox('setValue','');
						 },
						error:function(date){
							$.messager.alert('提示','保存失败！','error');
						}
				  	});
				}else{
					$.messager.alert('提示','无法进行保存操作！请先填写住院登记信息！','error');
				}
		 
		  	}
		/**
		 * 清除页面填写信息
		 * @author  hedong
		 * @date 2015-08-12
		 * @version 1.0
		 */
		function clear(){
		    $.messager.confirm('确认','清空当前患者接诊信息？',function(r){
		    	 if(r){
					    $('#patientName').textbox('setValue','');
					    $('#CodeSex').combobox('setValue','');
					    $('#inDate').textbox('setValue','');
					    $('#idcardNo').textbox('setValue','');
					    $('#CodeSettlement').combobox('setValue','');
					    $('#birthArea').textbox('setValue','');
					    $('#CodeCountry').combobox('setValue','');
					    $('#CodeNationality').combobox('setValue','');
					    $('#dist').textbox('setValue','');
					    $('#CodeOccupation').combobox('setValue','');
					    $('#CodeSourse').combobox('setValue','');
					    $('#inAvenue').textbox('setValue','');
					    $('#inCircs').textbox('setValue','');
					    $('#temperature').textbox('setValue','');
					    $('#pulse').textbox('setValue','');
					    $('#breath').textbox('setValue','');
					    $('#pressure').textbox('setValue','');
					    $('#weight').textbox('setValue','');
					    $('#postureDate').textbox('setValue','');
					    $('#reportBirthday').textbox('setValue','');
					    $('#reportAge').textbox('setValue','');
					    $('#certificatesNo').textbox('setValue','');
					    $('#workName').textbox('setValue','');
					    $('#workTel').textbox('setValue','');
					    $('#home').textbox('setValue','');
					    $('#homeTel').textbox('setValue','');
					    $('#CodeMarry').combobox('setValue','');
					    $('#linkmanName').textbox('setValue','');
					    $('#CodeRelation').combobox('setValue','');
					    $('#linkmanAddress').textbox('setValue','');
					    $('#linkmanTel').textbox('setValue','');
					    $('#remark').textbox('setValue','');
					    $('#deptCode').textbox('setValue','');
					    $('#emplCode').textbox('setValue','');
					    $('#diagName').textbox('setValue','');
		    	 }
		    });
		}
		/**
		 * 表单查询
		 * @author  hedong
		 * @date 2015-08-12
		 * @version 1.0
		 */
		function searchFrom() {
				var queryInpatientNo = $('#queryInpatientNo').val();
				if(queryInpatientNo){
					$.ajax({
						url: 'ajaxQueryPostureInfo.action?inpatientNoSearch='+queryInpatientNo,
						type:'post',
						success: function(data) {
							var dataObj = eval("("+data+")");
							$('#inPatientInfoId').val(dataObj.inPatientInfoId);
						    $('#patientName').textbox('setValue',dataObj.patientName);
						    $('#CodeSex').combobox('setValue',dataObj.reportSex);
						    $('#inDate').textbox('setValue',dataObj.inDate);
						    $('#idcardNo').textbox('setValue',dataObj.idcardNo);
						    $('#CodeSettlement').combobox('setValue',dataObj.paykindCode);
						    $('#birthArea').textbox('setValue',dataObj.birthArea);
						    $('#CodeCountry').combobox('setValue',dataObj.counCode);
						    $('#CodeNationality').combobox('setValue',dataObj.nationCode);
						    $('#dist').textbox('setValue',dataObj.dist);
						    $('#CodeOccupation').combobox('setValue',dataObj.profCode);
						    $('#CodeSourse').combobox('setValue',dataObj.inSource);
						    $('#inAvenue').textbox('setValue',dataObj.inAvenue);
						    $('#inCircs').textbox('setValue',dataObj.inCircs);
						    $('#postrueInfoId').val(dataObj.postrueInfoId);
						    $('#patientNo').val(dataObj.patientNo);
						    $('#temperature').textbox('setValue',dataObj.temperature);
						    $('#pulse').textbox('setValue',dataObj.pulse);
						    $('#breath').textbox('setValue',dataObj.breath);
						    $('#pressure').textbox('setValue',dataObj.pressure);
						    $('#weight').textbox('setValue',dataObj.weight);
						    $('#postureDate').textbox('setValue',dataObj.postureDate);
						    $('#reportBirthday').textbox('setValue',dataObj.reportBirthday);
						    $('#reportAge').textbox('setValue',dataObj.reportAge);
						    $('#certificatesNo').textbox('setValue',dataObj.certificatesNo);
						    $('#workName').textbox('setValue',dataObj.workName);
						    $('#workTel').textbox('setValue',dataObj.workTel);
						    $('#home').textbox('setValue',dataObj.home);
						    $('#homeTel').textbox('setValue',dataObj.homeTel);
						    $('#CodeMarry').combobox('setValue',dataObj.mari);
						    $('#linkmanName').textbox('setValue',dataObj.linkmanName);
						    $('#CodeRelation').combobox('setValue',dataObj.relaCode);
						    $('#linkmanAddress').textbox('setValue',dataObj.linkmanAddress);
						    $('#linkmanTel').textbox('setValue',dataObj.linkmanTel);
						    $('#remark').textbox('setValue',dataObj.remark);
						    $('#deptCode').textbox('setValue',dataObj.deptCode);
						    $('#emplCode').textbox('setValue',dataObj.emplCode);
						    $('#diagName').textbox('setValue',dataObj.diagName);
						}
					});
				}else{
					$.messager.alert('提示','查询条件不能为空！','error');
				}
				 
		};

		/**
		 * 回车键查询
		 * @author  hedong
		 * @param title 标签名称
		 * @param title 跳转路径
		 * @date 2015-06-19
		 * @version 1.0
		 */
		function KeyDown()  
		{  
		    if (event.keyCode == 13)  
		    {  
		        event.returnValue=false;  
		        event.cancel = true;  
		        searchFrom();  
		    }  
		} 
		
		/**
		 * 回车键查询
		 * @author  hedong
		 * @date 2015-06-26
		 * @version 1.0
		 */
		function KeyDownByp(flg) {  
		    if (event.keyCode == 13)  
		    {  
		        event.returnValue=false;  
		        event.cancel = true;
		        if(flg=="medicalrecordId"){
		        	getPatient($('#medicalrecordId').val());  
		        }else if(flg=="idcardNo"){
		       	 	getPatient($('#idcardNo').val()); 
		        }  
		    }  
		} 
		/**
		 * 通过就诊卡编号或病历号获取病人信息
		 * @author heodng
		 * @date 2015-06-26
		 * @version 1.0
		 */
		function getPatient(value) {
				$.ajax({
							url: 'getPatientByIdcard.action?id='+value,
							type:'get',
							success: function(data) {
								var dataObj = eval("("+data+")");
								if(dataObj != null&&dataObj != ""){
									valData(dataObj);
								}else{
									$.messager.alert('提示','有此就诊卡，请输入正确就诊卡号！','error');
									return;
									
								}
							}
				});	
		} 
		
		/**
		 * 赋值填充数据
		 * @author  hedong  待调整 通过就诊卡号赋值
		 * @date 2015-06-26
		 * @version 1.0
		 */
		function valData(obj){
			
		}
		
		/**
		 * 弹出框
		 * @author  hedong
		 * @date 2015-08-12
		 * @version 1.0
		 */
			var win;	
			function showWin (title,url, width, height) {
			   	var content = '<iframe id="myiframe" src="' + url + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
			    var divContent = '<div id="treeDeparWin">';
			    win = $('<div id="treeDeparWin"><div/>').dialog({
			        content: content,
			        width: width,
			        height: height,
			        modal: true,
			        minimizable:false,
			        maximizable:true,
			        resizable:true,
			        shadow:true,
			        center:true,
			        title: title
			    });
			    win.dialog('open');
			}
		</script>
</body>
</html>