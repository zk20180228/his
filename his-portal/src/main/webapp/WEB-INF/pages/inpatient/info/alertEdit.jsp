<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/javascript/default.jsp"></jsp:include>
	<body>
		<div id="p" class="easyui-panel"  style="padding: 10px; background: #fafafa;" data-options="fit:'true',border:'false'">
				<form id="editForm">
					<input type="hidden" id="id" name="id" value="${inpatientInfo.id}">
					<div id="tt" class="easyui-tabs" data-options="" style="">
						<div title="第一页" style="padding:10px">
							<jsp:include page="firstAlertTab.jsp"></jsp:include>
						</div>
						<div title="第二页" style="padding:10px">
							<jsp:include page="alertSecTab.jsp"></jsp:include>
						</div>
					</div>
				</form>	
				<div id="addCombox"></div>	
			</div>
			<div id="addBed"></div>
	<script>	
		$(function(){
			//初始化下拉框
			idCombobox("CodeMedicaltype");
			idCombobox("CodeCertificate");
			idCombobox("CodeSex");
			idCombobox("CodeOccupation");
			idCombobox("CodeNationality");
			idCombobox("CodeRelation");
			idCombobox("CodeMarry");
			idCombobox("CodeBloodtype");
			idCombobox("CodeSettlement");
			idCombobox("CodeSourse");
			idCombobox("CodeNurselevel");
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
		//下拉框的keydown事件   调用弹出窗口
		var paramkeydown = $('#CodeMedicaltype').combobox('textbox'); 
		paramkeydown.keyup(function(){
			KeyDown(0,"CodeMedicaltype");
		});
		var certificatekeydown = $('#CodeCertificate').combobox('textbox'); 
		certificatekeydown.keyup(function(){
			KeyDown(0,"CodeCertificate");
		});
		var occupationkeydown = $('#CodeOccupation').combobox('textbox'); 
		occupationkeydown.keyup(function(){
			KeyDown(0,"CodeOccupation");
		});
		var nationalitykeydown = $('#CodeNationality').combobox('textbox'); 
		nationalitykeydown.keyup(function(){
			KeyDown(0,"CodeNationality");
		});
		var relationkeydown = $('#CodeRelation').combobox('textbox'); 
		relationkeydown.keyup(function(){
			KeyDown(0,"CodeRelation");
		});
		var marrykeydown = $('#CodeMarry').combobox('textbox'); 
		marrykeydown.keyup(function(){
			KeyDown(0,"CodeMarry");
		});
		var bloodtypekeydown = $('#CodeBloodtype').combobox('textbox'); 
		bloodtypekeydown.keyup(function(){
			KeyDown(0,"CodeBloodtype");
		});
		var settlementkeydown = $('#CodeSettlement').combobox('textbox'); 
		settlementkeydown.keyup(function(){
			KeyDown(0,"CodeSettlement");
		});
		var soursekeydown = $('#CodeSourse').combobox('textbox'); 
		soursekeydown.keyup(function(){
			KeyDown(0,"CodeSourse");
		});
		var nurselevelkeydown = $('#CodeNurselevel').combobox('textbox'); 
		nurselevelkeydown.keyup(function(){
			KeyDown(0,"CodeNurselevel");
		});
		var sexkeydown = $('#CodeSex').combobox('textbox'); 
		sexkeydown.keyup(function(){
			KeyDown(0,"CodeSex");
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
		 * @author  lt
		 * @date 2015-6-1
		 * @version 1.0
		 */
		  	function submit(flg){
		  	$('#editForm').form('submit',{
		  		url:'inpatient/info/editInpatientInfo.action',
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
					if(flg==0){
						$.messager.alert('提示',"保存成功");
						closeDialog();
			          	$("#infolist").datagrid("reload");
				   }else if(flg==1){
						//清除editForm
						$('#editForm').form('reset');
				  	}
				 },
				error:function(date){
					$.messager.alert('提示',"保存失败");
				}
		  	});
		  	}
		/**
		 * 清除页面填写信息
		 * @author  lt
		 * @date 2015-6-19 10:53
		 * @version 1.0
		 */
		function clear(){
			$('#editForm').form('reset');
		}
	/**
	 * 添加弹出病床框
	 * @author  lt
	 * @param title 标签名称
	 * @param url 跳转路径
	 * @date 2015-06-25
	 * @version 1.0
	 */
	function AddBeddilog(title, url) {
		$('#addBed').dialog({    
		    title: title,    
		    width: '70%',    
		    height:'90%',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		   });    
	}
	/**
	 * 打开病床框
	 * @author  lt
	 * @date 2015-06-26
	 * @version 1.0
	 */
	function openBedDialog() {
		$('#addBed').dialog('open'); 
	}
	/**
	 * 加载病床框及信息
	 * @author  lt
	 * @date 2015-06-26
	 * @version 1.0
	 */
	function openBed(){
		AddBeddilog("病床", "<c:url value='listHospitalbed.action'/>");
	}
	
	/**
	 * 回车键查询
	 * @author  lt
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
	 * @author  lt
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
								$.messager.alert('提示',"没有此就诊卡，请输入正确就诊卡号。");
								return;
								
							}
						}
			});	
	} 
	/**
	 * 赋值填充数据
	 * @author  lt
	 * @date 2015-06-26
	 * @version 1.0
	 */
	function valData(obj){
		$("#medicalrecordId").val(obj.medicalrecordId);
		$("#idcardNo").val(obj.idcardNo);
		$("#mcardNo").textbox('setValue',obj.patient.patientHandbook);
		$("#patientName").textbox('setValue',obj.patient.patientName);
		$("#certificatesType").textbox('setValue',obj.patient.patientCertificatestype);
		$("#certificatesNo").textbox('setValue',obj.patient.patientCertificatesno);
		$("#reportSex").textbox('setValue',obj.patient.patientSex);
		$("#reportBirthday").datebox('setValue',obj.patient.patientBirthday);
		$("#reportAge").textbox('setValue',obj.patient.patientAge);
		$("#profCode").textbox('setValue',obj.patient.patientOccupation);
		$("#workName").textbox('setValue',obj.patient.patientWorkunit);
		$("#workTel").textbox('setValue',obj.patient.patientWorkphone);
		$("#workZip").textbox('setValue',obj.patient.patientWorkzip);
		$("#home").textbox('setValue',obj.patient.patientAddress);
		$("#homeTel").textbox('setValue',obj.patient.patientPhone);
		$("#homeZip").textbox('setValue',obj.patient.patientHomezip);
		$("#dist").textbox('setValue',obj.patient.patientNativeplace);
		$("#birthArea").textbox('setValue',obj.patient.patientBirthplace);
		$("#nationCode").textbox('setValue',obj.patient.patientNation);
		$("#linkmanName").textbox('setValue',obj.patient.patientLinkman);
		$("#linkmanTel").textbox('setValue',obj.patient.patientLinkphone);
		$("#linkmanAddress").textbox('setValue',obj.patient.patientLinkaddress);
		$("#relaCode").textbox('setValue',obj.patient.patientLinkrelation);
		$("#mari").textbox('setValue',obj.patient.patientWarriage);
		$("#counCode").textbox('setValue',obj.patient.patientNationality);
	}
	/**
	 * 弹出框
	 * @author  lt
	 * @date 2015-06-26
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