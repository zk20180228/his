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
<title>就诊卡管理</title>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
.panel .panel-header .panel-tool a{
    background-color: #B8432C;	
}
</style>
<script type="text/javascript">
	var sexList = "";
	var countryList = "";	
	var certiList = "";
	var nationList = "";
	var marryList = "";
	var occuList = "";	
	var relationList = "";
	var idcardTypeList = "";
	var codeCertificateList="";	
	var c_type;
	var sexMap=new Map();
	var codeCertificateMap=new Map();
		//加载页面
	$(function(){	
		$('#certificate').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=certificate ",
			valueField : 'encode',
			textField : 'name',
			multiple : false,
			onLoadSuccess:function(){
				var v = $('#certificate').combobox('getData');
			 	for(var i=0;i<v.length;i++){
			 		codeCertificateMap.put(v[i].encode,v[i].name);
				}
	    	},
		});
		$('#sex').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
			valueField:'encode',    
		    textField:'name',
			multiple : false,
			onLoadSuccess:function(){
				var v = $('#sex').combobox('getData');
			 	for(var i=0;i<v.length;i++){
					sexMap.put(v[i].encode,v[i].name);
				}
	    	},
		});
		queryDistrictSJLDOneList();
		queryDistrictSJLDTwoList('');
		queryDistrictSJLDThreeList('');
		queryDistrictSJLDFourList('',false);		     
		
		//解析编码xml文件----性别    "patientCertificatestype"
		
		
             //证件类型 certificate
            $.ajax({
					
					url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=certificate'/>",
					//data:{"str" : "CodeCertificate"},
					type:'post',
					success: function(codeCertificatedata) {
						codeCertificateList =  codeCertificatedata ;
					}
			});
            
            //卡类型 idcardType
            $.ajax({
					url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=idcardType'/>",
					//data:{"str" : "CodeIdcardType"},
					type:'post',
					success: function(idcardTypedata) {
						idcardTypeList = idcardTypedata ;
					}
			});
            //回车查询
            bindEnterEvent('name',isSearchFrom,'easyui');//姓名
            bindEnterEvent('contact',isSearchFrom,'easyui');//联系方式
            bindEnterEvent('number',isSearchFrom,'easyui');//证件号码
            bindEnterEvent('address',isSearchFrom,'easyui');//地址
            bindEnterEvent('idcard',isSearchFrom,'easyui');//就诊卡号
		});
	
		//省市区三级联动
	function queryDistrictSJLDFourList(id,bool) {
		if(id===''){
			$('#homefours').combobox({
				data:[]
			})
			bindEnterEvent('homefours',popWinToDistrictListFours,'easyui');
			return;
		}
			$('#homefours').combobox({  
				url: "<c:url value='/baseinfo/district/queryDistrictTreeFour.action'/>?parId="+id,    
				valueField:'cityCode',    
				    textField:'cityName',
				    required:bool,
				    multiple:false,
				    editable:true,
				    onSelect:function(node) {
				    	$('#patientCitys').val(node.cityCode);
				    }
			});
			bindEnterEvent('homefours',popWinToDistrictListFours,'easyui');
	}
	function queryDistrictSJLDThreeList(id) {
		if(id===''){
			$('#homethrees').combobox({
				data:[]
			})
			bindEnterEvent('homethrees',popWinToDistrictListThrees,'easyui');
			return;
		}
			$('#homethrees').combobox({  
				url: "<c:url value='/baseinfo/district/queryDistrictTreeThree.action'/>?parId="+id,    
					valueField:'cityCode',    
				    textField:'cityName',
				    required:true,
				    multiple:false,
				    editable:true,
				    onSelect:function(node) {
				     	var bool = false;
				     	if(node.cityName=="市辖区"){
				     		bool=true;
				     		$('#patientCitys').val('');
				     		$('#homefours').combobox({
								required: true,
								value:'',
								disabled:false
							});
				     	}else{
				     		$('#patientCitys').val(node.cityCode);
				     		$('#homefours').combobox({
								required: false,
								value:'',
								disabled:true
							});
				     	}
			        	queryDistrictSJLDFourList(node.cityCode,bool);
			        }
			});
			bindEnterEvent('homethrees',popWinToDistrictListThrees,'easyui');
	}
	function queryDistrictSJLDTwoList(id) {
		if(id===''){
			$('#hometwos').combobox({
				data:[]
			})
			bindEnterEvent('hometwos',popWinToDistrictListTwos,'easyui');//绑定回车事件
			return;
		}
		$('#hometwos').combobox({  
			url: "<c:url value='/baseinfo/district/queryDistrictTreeTwo.action'/>?parId="+id,    
				valueField:'cityCode',    
			    textField:'cityName',
			    required:true,
			    multiple:false,
			    editable:true,
			    onSelect:function(node) {
		        	queryDistrictSJLDThreeList(node.cityCode);
		        	$('#patientCitys').val('');
			    	$('#homefours').combobox('setValue','');
		        }
		});
		bindEnterEvent('hometwos',popWinToDistrictListTwos,'easyui');//绑定回车事件
		
	}
		function queryDistrictSJLDOneList(){
			$('#homeones').combobox({
				url: "<c:url value='/baseinfo/district/queryDistrictTreeOne.action'/>?parId=",
				valueField:'cityCode',    
			    textField:'cityName',
			    multiple:false,
			    editable:true,
			    onSelect:function(node) {
					queryDistrictSJLDTwoList(node.cityCode);
					$('#patientCitys').val('');
					$('#homethrees').combobox('setValue','');
					$('#homefours').combobox('setValue','');
		        }
			});
			bindEnterEvent('homeones',popWinToDistrictList,'easyui');//绑定回车事件
		}
		
		function add(){
			//2017年2月18日  GH  岳工让添加时清空页面  
			clearValue(0);
		 window.open("${pageContext.request.contextPath}/patient/idcard/addIdcard.action",'newwindow',' left=273,top=49,width='+ (screen.availWidth -505) +',height='+ (screen.availHeight-152));
		}
		
		function edit(){        
			var idcardId = "";
			var id = "";
			idcardId=$('#idcards').val();
			id=$('#ids').val();
			if(idcardId != "" && idcardId != null  && id != "" && id != null){
				AddOrShowEast('编辑',"<c:url value='/patient/idcard/editInfoIdcardNext.action'/>?id="+idcardId+"&pid="+id);
	   		}else{
	   			$.messager.alert("操作提示","请确定一条记录进行编辑","warning");  
	   		}    
		}
		
		function del(){
						var idcardId = '';
						var ids = '';
						idcardId=$('#idcards').val();
						ids=$('#ids').val();
	                  	if (idcardId != "" && ids!="") {                     
					 	$.messager.confirm('确认', '确定要删除信息吗?', function(res){//提示是否删除
							if (res){
								$.ajax({
									url: "<c:url value='/patient/patinent/delPatient.action'/>?id="+ids+"&idcardId="+idcardId,
									type:'post',
									success: function() {
										$.messager.alert('提示','删除成功');
										clearValue(0);
									}
								});
							}
	                      });
	                  }
		}
		
		function clearValue(type){
			//实现刷新栏目中的数据
			$('#idcards').val('');
			if(type==0){
				$('#idcard').textbox('setValue','');//就诊卡号
				$('#name').textbox('setValue','');//姓名
				$('#sex').textbox('setValue','');//性别
				$('#contact').textbox('setValue','');//联系方式
				$('#birthday').val('');//生日
				$('#certificate').textbox('setValue','');//证件类型
				$('#number').textbox('setValue','');//证件号码
				$('#homeones').combobox({
					required: false,
					value:''
				});
				$('#hometwos').combobox({
					required: false,
					value:''
				});
				$('#homethrees').combobox({
					required: false,
					value:''
				});
				$('#homefours').combobox({
					required: false,
					value:''
				});
				$('#patientCitys').val('');
				$('#address').textbox('setValue','');
			}
			$('#ids').val('');
			$('#medicalrecordId').val('');
			$('#patientNames').text('');
			$('#patientSexs').text('');
			$('#patientBirthdayNext').text('');
			$('#patientPhone').text('');
			$('#patientCertificatestype').text('');
			$('#patientCertificatesno').text('');
			$('#patientCityAndAddress').text('');
			
			$('#idcardNos').text('');
			$('#idcardCreatetimes').text('');
			$('#idcardType').text('');
			$('#idcardOperator').text('');
			$('#idcardRemark').text('');
			$('#medicalrecordIds').text('');
			//清空时禁用按钮
			$('#editlinkbutton').linkbutton('disable');
			$('#backlinkbutton').linkbutton('disable');
			$('#losslinkbutton').linkbutton('disable');
			$('#activatelinkbutton').linkbutton('disable');
			$('#filllinkbutton').linkbutton('disable');
			//挂失退卡提醒清空
			$("#message").empty();
			//2017年2月20日  变更记录的查看  清除
			$("#idcardChange").html('');
			
		}
		/**
		 * 查询条数  如果为多条  则弹窗显示
		 * @author  GH
		 * @date 2016年9月28日15:48:29
		 * @version 1.0
		 */
		 function isSearchFrom(){
			//查询时如果右侧窗口打开,先关闭再查询
			$('#divLayout').layout('remove', 'east');
			$.messager.progress({text:'查询中，请稍后...',modal:true});	
			var idcard = $('#idcard').val();//就诊卡卡号
			var name = $('#name').val();//姓名
			var sex = $('#sex').combobox('getValue');//性别
			var contact = $('#contact').val();//联系方式
			if(sex){
			    if(idcard==null||idcard.length==0){
					if(checkName(name,contact)){
						return;
					};
				}
			}
			var birthday = $('#birthday').val();//生日
			var certificate = $('#certificate').combobox('getValue');//证件类型
			var number = $('#number').val();//证件号码		
			if(checkTypeOrNum(certificate,number)){
				return;
			}
			var patientCitys = $('#patientCitys').val();//省市县的ID
			var address = $('#address').val();//具体地址	
			$.ajax({
				url: "<%=basePath%>patient/patinent/listPatientNext.action",
	   			data : {
					"idcardId":idcard,
					"name" : name,
					"sex" : sex,
					"birthday":birthday,
					"contact":contact,
					"cost":$("#cost").val(),
					"certificate":certificate,
					"number":number,
					"patientCitys":patientCitys,
					"address":address
				},
				type:'post',
				success: function(data) {
					if(data=="noSearch"){
						$.messager.progress('close');	
						$.messager.alert('提示','查询条件不完整或未输入查询条件');	
						return;
					}
					var mapList=eval("("+data+")");
					if(mapList.resSize>0){
						if(mapList.resSize==1){
							var dataObj = mapList.resData;
							if(dataObj != null && dataObj != ""){
								$('#idcards').val(dataObj.idcardId);
								$('#ids').val(dataObj.id);
								$('#medicalrecordIds').text(dataObj.medicalrecordId);
								$('#medicalrecordId').val(dataObj.medicalrecordId);
								$('#patientNames').text(dataObj.patientName);
								$('#patientSexs').text(sexMap.get(dataObj.patientSex));
								$('#patientBirthdayNext').text(dataObj.birthdayStr);
								$('#patientPhone').text(dataObj.patientPhone==null?'':dataObj.patientPhone);
								$('#patientCertificatestype').text(codeCertificateFamater(dataObj.patientCertificatestype));
								$('#patientCertificatesno').text(dataObj.patientCertificatesno==null?'':dataObj.patientCertificatesno);
								$('#patientCityAndAddress').text(dataObj.patientCity+(dataObj.patientAddress==null?'':"-"+dataObj.patientAddress));
								$('#idcardNos').text(dataObj.idcardNo);
								cardNo=dataObj.idcardNo;
								if(dataObj.idcardCreatetime==null){
									$('#idcardCreatetimes').text('');
								}else{
									$('#idcardCreatetimes').text(dataObj.createTimeStr);
								}
								if(dataObj.idcardType==null){
									$('#idcardType').text('');
								}else{
									$('#idcardType').text(idcardTypeFamater(dataObj.idcardType));
								}
								$('#idcardOperator').text(dataObj.idcardOperator);
								$('#idcardRemark').text(dataObj.idcardRemark);
								if(dataObj.idcardStatus=='1'){
									$('#editlinkbutton').linkbutton('enable');
									$('#dellinkbutton').linkbutton('enable');
									$('#backlinkbutton').linkbutton('enable');
									$('#losslinkbutton').linkbutton('enable');
									$('#activatelinkbutton').linkbutton('disable');
									$('#filllinkbutton').linkbutton('enable');
									$("#message").empty();
								}
								if(dataObj.idcardStatus=='2'){
									$('#editlinkbutton').linkbutton('enable');
									$('#dellinkbutton').linkbutton('enable');
									$('#backlinkbutton').linkbutton('enable');
									$('#losslinkbutton').linkbutton('disable');
									$('#activatelinkbutton').linkbutton('enable');
									$('#filllinkbutton').linkbutton('enable');
									$("#message").empty();  
						            $("#message").append("已挂失"); 
								}
								if(dataObj.idcardStatus=='0'){
									$('#editlinkbutton').linkbutton('enable');
									$('#dellinkbutton').linkbutton('enable');
									$('#backlinkbutton').linkbutton('disable');
									$('#losslinkbutton').linkbutton('disable');
									$('#activatelinkbutton').linkbutton('disable');
									$('#filllinkbutton').linkbutton('enable');
									$("#message").empty();  
						            $("#message").append("已退卡"); 
								}
								$.messager.progress('close');
								//查询完成后展示 查看 字样  可查看变更记录
								$('#idcardChange').html("<shiro:hasPermission name='${menuAlias}:function:change'>  <a href='javascript:queryChange();' style=' color:blue;'>变更记录</a> </shiro:hasPermission>");
							}
						}else{
							SearchFromData=mapList.resData;
							$.messager.progress('close');	
							Adddilog('双击选择患者','<%=basePath%>patient/patinent/selectDialogURL.action');
						}
					}else{
						$.messager.progress('close');
						$.messager.alert('友情提示','未查询到患者信息');  
						clearValue(1);
					}
				}
			});
				
		 }
		//加载dialog
		function Adddilog(title, url) {
			$('#dialog').dialog({    
			    title: title,    
			    width: '50%',    
			    height:'50%',    
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
		
		//2016年10月26日  by GH  新添：证件类型和证件号必须共同选择   避免只填一项造成服务崩溃
		function checkTypeOrNum(type,num){
			if(type.length==0||type==null){
				if(num.length!=0&&num!=null){
					$.messager.alert('友情提示','请选择证件类型');
					$.messager.progress('close');
					return true;
				}
			}else{
				if(num.length==0||num==null){
					$.messager.alert('友情提示','请填写证件号');  
					$.messager.progress('close');
					return true;
				}
			}
			return false;
		}
		
		//2016年11月14日  by GH  新添：性别查询在就诊卡号为空的情况下必须有姓名和联系方式的信息，  避免只填一项造成服务崩溃
		function checkName(name,contact){
			if(name.length==0||name==null){
				if(contact.length==0||contact==null){
					$.messager.alert('友情提示','请填写姓名或者联系方式信息!'); 
					$.messager.progress('close');
					return true;
				}
			}
			return false;
		}
		//全局变量   存放就诊卡号
		var cardNo;
		/**
		 * 查询
		 * @author  lt
		 * @date 2015-06-18
		 * @version 1.0
		 */
		function searchFrom(idcard) {
			cardNo='';
			$.ajax({
						url: "<%=basePath%>patient/patinent/listPatientByMedicalrecord.action",
						data : {
							"medicalrecordId":idcard,
						},
						type:'post',
						success: function(data) {
							if(data!=null && data!=""){
								var dataObj = data;
								if(dataObj != null && dataObj != ""){
									$('#idcards').val(dataObj.idcardId);
									$('#ids').val(dataObj.id);
									$('#medicalrecordIds').text(dataObj.medicalrecordId);
									$('#medicalrecordId').val(dataObj.medicalrecordId);
									$('#patientNames').text(dataObj.patientName);
									$('#patientSexs').text(sexMap.get(dataObj.patientSex));
									$('#patientBirthdayNext').text(dataObj.birthdayStr);
									$('#patientPhone').text(dataObj.patientPhone==null?'':dataObj.patientPhone);
									$('#patientCertificatestype').text(codeCertificateFamater(dataObj.patientCertificatestype));
									$('#patientCertificatesno').text(dataObj.patientCertificatesno==null?'':dataObj.patientCertificatesno);
									$('#patientCityAndAddress').text(dataObj.patientCity+(dataObj.patientAddress==null?'':"-"+dataObj.patientAddress));
									$('#idcardNos').text(dataObj.idcardNo);
									cardNo=dataObj.idcardNo;
									if(dataObj.idcardCreatetime==null){
										$('#idcardCreatetimes').text('');
									}else{
										$('#idcardCreatetimes').text(dataObj.createTimeStr);
									}
									if(dataObj.idcardType==null){
										$('#idcardType').text('');
									}else{
										$('#idcardType').text(idcardTypeFamater(dataObj.idcardType));
									}
									$('#idcardOperator').text(dataObj.idcardOperator);
									$('#idcardRemark').text(dataObj.idcardRemark);
									if(dataObj.idcardStatus=='1'){
										$('#editlinkbutton').linkbutton('enable');
										$('#dellinkbutton').linkbutton('enable');
										$('#backlinkbutton').linkbutton('enable');
										$('#losslinkbutton').linkbutton('enable');
										$('#activatelinkbutton').linkbutton('disable');
										$('#filllinkbutton').linkbutton('enable');
										$("#message").empty();
									}
									if(dataObj.idcardStatus=='2'){
										$('#editlinkbutton').linkbutton('enable');
										$('#dellinkbutton').linkbutton('enable');
										$('#backlinkbutton').linkbutton('enable');
										$('#losslinkbutton').linkbutton('disable');
										$('#activatelinkbutton').linkbutton('enable');
										$('#filllinkbutton').linkbutton('enable');
										$("#message").empty();  
							            $("#message").append("已挂失"); 
									}
									if(dataObj.idcardStatus=='0'){
										$('#editlinkbutton').linkbutton('enable');
										$('#dellinkbutton').linkbutton('enable');
										$('#backlinkbutton').linkbutton('disable');
										$('#losslinkbutton').linkbutton('disable');
										$('#activatelinkbutton').linkbutton('disable');
										$('#filllinkbutton').linkbutton('enable');
										$("#message").empty();  
							            $("#message").append("已退卡"); 
									}
									$.messager.progress('close');
									//查询完成后展示 查看 字样  可查看变更记录
									$('#idcardChange').html("<shiro:hasPermission name='${menuAlias}:function:change'>  <a href='javascript:queryChange();' style=' color:blue;'>变更记录</a> </shiro:hasPermission> ");
								}
							}else{
								$.messager.progress('close');
								$.messager.alert('提示',"没有符合条件的患者");
								$('#editlinkbutton').linkbutton('disable');
								$('#dellinkbutton').linkbutton('disable');
								$('#backlinkbutton').linkbutton('disable');
								$('#losslinkbutton').linkbutton('disable');
								$('#activatelinkbutton').linkbutton('disable');
								$('#filllinkbutton').linkbutton('disable');
								clearValue(0);
							}
						}
			});	
		}
	/**
		 * 动态添加标签页
		 * @author  lt
		 * @date 2015-06-19
		 * @version 1.0
		 */
		function AddOrShowEast(title, url) {
			 var eastpanel=$('#panelEast'); //获取右侧收缩面板
			if(eastpanel.length>0){ //判断右侧收缩面板是否存在
				//重新装载右侧面板
		   		$('#divLayout').layout('panel','east').panel({
	                         href:url 
	                  });
			}else{//打开新面板
				// 由于下面的页面导致无法再新的布局中读卡，所以改为弹出框 
				$('#divLayout').layout('add', {
					region : 'east',
					width : 800,
					top:'20%',
					split : true,
					href : url,
					closable : true
				}); 
			}
				
		}
		
		/**
		 * 回车键查询
		 * @author  lt
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
		 * 性别列表页 显示
		 * @author  lt
		 * @date 2015-06-29
		 * @version 1.0
		 */
		function sexFamater(value){
			if(value!=null&&value!=''){
				return sexMap.get(value);
			}
		}
		/** 关系列表页 显示
		 * @author  lt
		 * @date 2015-06-29
		 * @version 1.0
		 */
		function relationFamater(value){
			if(value!=null){
				for(var i=0;i<relationList.length;i++){
					if(value==relationList[i].id){
						return relationList[i].name;
					}
				}
			}	
		}
		/** 卡类型列表页 显示
		 * @author  lt
		 * @date 2015-06-29
		 * @version 1.0
		 */
		function idcardTypeFamater(value){
				for(var i=0;i<idcardTypeList.length;i++){
					if(value==idcardTypeList[i].encode){
						return idcardTypeList[i].name;
					}
				}
			}	
			
		/** 证件类型列表页 显示
		 * @author  zpty
		 * @date 2015-12-23
		 * @version 1.0
		 */
		function codeCertificateFamater(value){
				for(var i=0;i<codeCertificateList.length;i++){
					if(value==codeCertificateList[i].encode){
						return codeCertificateList[i].name;
					}
				}
			}	
		
		/**
		 * 退卡操作方法(新)
		 * @author  zpty
		 * @date 2016-04-08
		 * @version 1.0
		 */
		function backCard(){      
		    $.messager.progress({text:'正在查询患者账户是否可以执行退卡操作，请稍后...',modal:true});	
			var idcardId = ""; 
			idcardId = $('#idcards').val();
	           	if(idcardId!=null&&idcardId!=""){
	           		$.ajax({//这里是通过就诊卡ID查询出门诊账户
	           			url:'<%=basePath%>/patient/idcard/getAccountForcardId.action',
	           			type:'post',
	           			data:{"idcardId":idcardId},
						success: function(data) {
							if(data.accountBalance==0){//这个是判断是否结清
								$.messager.progress('close');
								$.messager.confirm('确认', '此患者卡内账户已结清，可以办理退卡业务，确定执行退卡操作？', function(res){
									if (res){
										$.messager.progress({text:'正在执行退卡操作，请稍后...',modal:true});	
										$.ajax({//退卡操作
										url: "<%=basePath%>patient/idcard/backIdcard.action",
										type:'post',
										data:{'idcardId':idcardId,"idcardNO":cardNo},
										success: function(data) {
											if(data=="yes"){
												clearValue(0);
												$.messager.alert("操作提示","退卡成功！","success");
												$.messager.progress('close');
											}else{
												$.messager.alert("操作提示","退卡失败！","error");
												$.messager.progress('close');
											}
										}
										});	
									}
								});
							}else{
								$.messager.progress('close');
								$.messager.alert('友情提示','该患者卡内账户现有余额'+data.accountBalance+'元，请先结清此卡内账户');   
							}
						}
					});	
	           	}else{
	           		$.messager.progress('close');
	           		$.messager.alert("操作提示","此患者没有就诊卡，不能办理退卡业务！","warning");   
	           	}
		}
		
	//挂失
	function lossCard(){
		var idcardId = ""; 
		idcardId = $('#idcards').val();
        	if(idcardId!=null&&idcardId!=""){
        		$.messager.confirm('操作提示', '确定执行挂失操作？', function(res){
					if (res){
						$.ajax({
						url: "<%=basePath%>patient/idcard/lossIdcard.action",
						type:'post',
						data:{'idcardId':idcardId},
						success: function(data) {
							if(data=="yes"){
								clearValue(0);
								$.messager.alert("操作提示","挂失成功！","success");
								$('#editlinkbutton').linkbutton('disable');
								$('#dellinkbutton').linkbutton('disable');
								$('#backlinkbutton').linkbutton('disable');
								$('#losslinkbutton').linkbutton('disable');
								$('#activatelinkbutton').linkbutton('disable');
								$('#filllinkbutton').linkbutton('disable');
							}else{
								$.messager.alert("操作提示","挂失失败！","error");
							}
						}
						});	
					}
				});
       		}else{
       		$.messager.alert("操作提示","此患者没有就诊卡，不能办理挂失业务！","warning");   
       	}
	}
	//补卡
	function fillCard() {
		var idcardId = ""; 
		var id="";
		idcardId = $('#idcards').val();
		id = $('#ids').val();
		if(idcardId!=null){
			AddOrShowEast('补办就诊卡',"<%=basePath%>patient/idcard/fillInfoIdcard.action?id="+idcardId+"&pid="+id+"&random="+new Date().getTime());
		}else{
			idcardId="";
       		AddOrShowEast('补办就诊卡',"<%=basePath%>patient/idcard/fillInfoIdcard.action?id="+idcardId+"&pid="+id+"&random="+new Date().getTime());
       	}
	}
	//激活
	function activateCard(){
		var idcardId = ""; 
		idcardId = $('#idcards').val();
        	if(idcardId!=null&&idcardId!=""){
        		$.ajax({
					url: "<%=basePath%>patient/idcard/checkIdcard.action",
					type:'post',
					data:{'idcardId':idcardId},
					success: function(data) {
						if(data=="yes"){
							$.messager.confirm('操作提示', '确定执行激活操作？', function(res){
							if (res){
								$.ajax({
									url: "<%=basePath%>patient/idcard/activateIdcard.action",
									type:'post',
									data:{'idcardId':idcardId},
									success: function(data) {
										if(data=="yes"){
											clearValue(0);
											$.messager.alert("操作提示","激活成功！","success");
											$('#editlinkbutton').linkbutton('disable');
											$('#dellinkbutton').linkbutton('disable');
											$('#backlinkbutton').linkbutton('disable');
											$('#losslinkbutton').linkbutton('disable');
											$('#activatelinkbutton').linkbutton('disable');
											$('#filllinkbutton').linkbutton('disable');
										}else{
											$.messager.alert("操作提示","激活失败！","error");
										}
									}
									});	
								}
							});
						}else{
							$.messager.alert("操作提示","此卡没有挂失，无需激活！","error");
						}
					}
				});
       		}else{
       		$.messager.alert("操作提示","此患者没有就诊卡，不能办理激活业务！","warning");   
       	}
	} 
	//读卡
	function queryFrom(){
		var idcardId = $('#idcard').val();
		if(idcardId==""||idcardId==null){
			$.messager.alert('提示',"请输入就诊卡号");
			return false;
		}
		$.ajax({
			url: "<%=basePath%>patient/idcard/queryIdcadAllInfo.action",
			type:'post',
			data:{"idcardNo":idcardId},
			success: function(dataObj) {
				if(dataObj != null && dataObj != ""){
					$('#idcards').val(dataObj.id);
					$('#ids').val(dataObj.patient.id);
					$('#medicalrecordId').val(dataObj.medicalrecordId);
					$('#medicalrecordIds').text(dataObj.patient.medicalrecordId);
					$('#patientNames').text(dataObj.patient.patientName);
					$('#patientSexs').text(sexMap.get(dataObj.patient.patientSex));
					$('#patientBirthdayNext').text(dataObj.patient.patientBirthday);
					$('#patientPhone').text(dataObj.patient.patientPhone);
					$('#patientCertificatestype').text(codeCertificateFamater(dataObj.patient.patientCertificatestype));
					$('#patientCertificatesno').text(dataObj.patient.patientCertificatesno);
					$('#patientCityAndAddress').text(dataObj.patient.patientCity+(dataObj.patient.patientAddress==null?'':"-"+dataObj.patient.patientAddress));
					$('#idcardNos').text(dataObj.idcardNo);
					$('#idcardCreatetimes').text(dataObj.idcardCreatetime);
					$('#idcardType').text(idcardTypeFamater(dataObj.idcardType));
					$('#idcardOperator').text(dataObj.idcardOperator);
					$('#idcardRemark').text(dataObj.idcardRemark);
					if(dataObj.idcardStatus=='1'){
						$('#editlinkbutton').linkbutton('enable');
						$('#dellinkbutton').linkbutton('enable');
						$('#backlinkbutton').linkbutton('enable');
						$('#losslinkbutton').linkbutton('enable');
						$('#activatelinkbutton').linkbutton('disable');
						$('#filllinkbutton').linkbutton('enable');
						$("#message").empty();  
					}
					if(dataObj.idcardStatus=='2'){
						$('#editlinkbutton').linkbutton('enable');
						$('#dellinkbutton').linkbutton('enable');
						$('#backlinkbutton').linkbutton('enable');
						$('#losslinkbutton').linkbutton('disable');
						$('#activatelinkbutton').linkbutton('enable');
						$('#filllinkbutton').linkbutton('enable');
						$("#message").empty();  
			            $("#message").append("已挂失"); 
					}
					
					
				}else{
					$.messager.alert('提示',"此卡号["+idcardId+"]没有对应的患者");
					$('#editlinkbutton').linkbutton('disable');
					$('#dellinkbutton').linkbutton('disable');
					$('#backlinkbutton').linkbutton('disable');
					$('#losslinkbutton').linkbutton('disable');
					$('#activatelinkbutton').linkbutton('disable');
					$('#filllinkbutton').linkbutton('disable');
					clearValue(0);
				}
				
			}
		});
		
	}
	
	/**
	* 回车弹出地址一级选择窗口
	* @author  zhuxiaolu
	* @param textId 页面上commbox的的id
	* @date 2016-03-22 14:30   
	* @version 1.0
	*/

	function popWinToDistrictList(){
		$('#hometwos').textbox('setValue','');
		$('#homethrees').textbox('setValue','');
		$('#homefours').textbox('setValue','');
		$('#patientCitys').val('');
		var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=homeones&level=1&parentId=1";
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

	function popWinToDistrictListTwos(){
		$('#homethrees').textbox('setValue','');
		$('#homefours').textbox('setValue','');
		$('#patientCitys').val('');
		var parentId=$('#homeones').textbox('getValue');
		if(!parentId){
			$.messager.alert('提示','请选择省/直辖市');  
			 setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
		}else{
			var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=hometwos&level=2&parentId="+parentId;
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

	function popWinToDistrictListThrees(){
		$('#homefours').textbox('setValue','');
		$('#patientCitys').val('');
		var parentId=$('#hometwos').textbox('getValue');
		if(!parentId){
			$.messager.alert('提示','请选择市');  
			 setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
		}else{
			var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=homethrees&level=3&parentId="+parentId;
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

	function popWinToDistrictListFours(){
		var parentId=$('#homethrees').textbox('getValue');
		if(!parentId){
			$.messager.alert('提示','请选择县');  
			 setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
		}else{
			var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=homefours&level=4&parentId="+parentId;
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 
			+',scrollbars,resizable=yes,toolbar=yes')
		}
	}
	
	/** 清除查询中的三级联动地址
	 * @author  zpty
	 * @date 2017-02-27
	 * @version 1.0
	 */
	function delSelectedSJLD(){
		$('#homeones').combobox({
			required: false,
			value:''
		});
		$('#hometwos').combobox({
			required: false,
			value:''
		});
		$('#homethrees').combobox({
			required: false,
			value:''
		});
		$('#homefours').combobox({
			required: false,
			value:''
		});
		$('#patientCitys').val('');
		$('#address').textbox('setValue','');
	}	
	
	/* 
		GH 
		2017年2月20日
		查询就诊卡变更记录
	*/
	function queryChange(){
		var ids=$('#ids').val();
		if(ids){
			changedilog('就诊卡变更记录','<%=basePath%>patient/idcardChange/changeSelectDialog.action');
		}else{
			$.messager.alert("提示","无就诊卡信息");
		}
	}
	//加载dialog
	function changedilog(title, url) {
		$('#dialog').dialog({    
		    title: title,    
		    width: '60%',    
		    height:'60%',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
	   });    
	}
	
	
	
	/*******************************开始读卡***********************************************/
	//定义一个事件
	function read_card_ic(){
		var card_value=app.read_ic();
		if(card_value=='0'||card_value==undefined||card_value=='')
		{
			$.messager.alert('提示','此卡号['+card_value+']无效');
			return;
		}
		//alert(card_value);
		
		$("#idcard").textbox("setValue",card_value);
		queryFrom();  
	}

	function read_card_sfz(){

		var card_value=app.read_sfz_all().trim();
		var cards = '';
		if(card_value=='0'||card_value==undefined||card_value=='')
		{
			$.messager.alert('提示','此卡号['+card_value+']无效');
			return;
		}else{
			cards = card_value.split(',');
			card_value = cards[5];
		}
		if(card_value==undefined||card_value==''){
			$.messager.alert('提示','此卡号['+card_value+']无效');
			return;
		}
		$("#number").textbox("setValue",card_value);//身份证
		var certificate = "";//证件类型
		for(var i=0;i<codeCertificateList.length;i++){
			if(codeCertificateList[i].name.indexOf("身份证")>=0){
				certificate=codeCertificateList[i].encode;
			}
		}
		$('#certificate').combobox('setValue',certificate);//证件类型
		isSearchFrom();  
	}
	/*******************************结束读卡***********************************************/
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<style type="text/css">
	.tableCss{
		border-collapse: collapse;border-spacing: 0;border: 1px solid #95b8e7;width:100%;
	}
	.tableCss td{
		border: 1px solid #95b8e7;
		padding: 5px 15px;
		word-break: keep-all;
		white-space:nowrap;
	}
	.tableCss .TDlabel{
		text-align: right;
		width:80px;
	}
	#serchTab td {
		padding: 5px 0;
	}		
</style>
</head>
<body>
	<div style="padding: 5px 7px 5px 7px;">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="add()"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-add'">添加</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="edit()"
				class="easyui-linkbutton" id="editlinkbutton"
				data-options="iconCls:'icon-edit',disabled:'true'">修改</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:backCard">
		<a href="javascript:void(0)" onclick="backCard()"
			class="easyui-linkbutton" id="backlinkbutton"
			data-options="iconCls:'icon-backcard',disabled:'true'">退卡</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:lossCard">
		<a href="javascript:void(0)" onclick="lossCard()"
			class="easyui-linkbutton" id="losslinkbutton"
			data-options="iconCls:'icon-02',disabled:'true'">挂失</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:activateCard">
		<a href="javascript:void(0)" onclick="activateCard()"
			class="easyui-linkbutton" id="activatelinkbutton"
			data-options="iconCls:'icon-2012080412263',disabled:'true'">激活</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:fillCard">	
		<a href="javascript:void(0)" onclick="fillCard()"
			class="easyui-linkbutton" id="filllinkbutton"
			data-options="iconCls:'icon-remakecard',disabled:'true'">补卡</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)" onclick="isSearchFrom()"
							class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
		</shiro:hasPermission>
<!-- 						<a href="javascript:void(0)" class="easyui-linkbutton read_medical_card" type_id="read_medical_cardID" id="read_medical_cardID" type_value="read_medical_card" cardNo="" onclick="read_card_ic()" data-options="iconCls:'icon-bullet_feed'">读卡</a> -->
<!-- 							<input type="hidden" id="id_card_no"> -->
						<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
						<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
						<a href="javascript:void(0)" onclick="clearValue(0)"
							class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清空</a>
		
	</div>
	<div id="divLayout" class="easyui-layout" fit=true
		style="width: 100%; height: 100%; overflow-y: auto;">
		<div data-options="region:'center'" style="border: 0px;">
		<div style="padding: 5px 7px 5px 7px;">
		<fieldset style="border:1px solid #95b8e7;margin-left:auto;margin-right:auto;" class="changeskin">
			<legend><font style="font-weight: bold;font-size: 12px;">查询域</font></legend>
			<table id="serchTab" style="width: 100%; border: 0px solid #95b8e7;margin-left:auto;margin-right:auto;">
				<tr>
					<td>就诊卡号：
					</td>
					<td>
					<input class="easyui-textbox" type="text" ID="idcard"  onkeydown="KeyDown()" />
					</td>
				</tr>
				<tr>
					<td  nowrap="nowrap">
						姓名：
					</td>
					<td  nowrap="nowrap">					
						<input class="easyui-textbox" type="text" ID="name"  onkeydown="KeyDown()" />
					</td>
					<td  nowrap="nowrap">
						性别：
					</td>
					<td  nowrap="nowrap">				
						<input id="sex"  onkeydown="KeyDown()" />
					</td>
					<td  nowrap="nowrap">	
						出生日期：
					</td>
					<td  nowrap="nowrap">				
						<input id="birthday" class="Wdate" type="text" onClick="WdatePicker()"  style="border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
				</tr>
				<tr>
					<td  nowrap="nowrap">
						联系方式：
					</td>
					<td  nowrap="nowrap">				
						<input class="easyui-textbox" type="text" ID="contact"  onkeydown="KeyDown()" />
					</td>
					<td  nowrap="nowrap">	
						证件类型：
					</td>
					<td  nowrap="nowrap">				
						<input id="certificate"  onkeydown="KeyDown()" />			
					</td>
					<td  nowrap="nowrap">				
						证件号码：
					</td>
					<td  nowrap="nowrap">				
						<input class="easyui-textbox" type="text" ID="number"  onkeydown="KeyDown()" />
					</td>
				<tr>
					<td  nowrap="nowrap">
						地址：
					</td>
					<td  nowrap="nowrap" colspan="5">		
						<input   id="homeones"  style="width: 130px"  data-options="prompt:'省/直辖市'"/>
						<input  id="hometwos" style="width: 130px"  data-options="prompt:'市'"/>
						<input   id="homethrees" style="width: 130px"  data-options="prompt:'县'"/>
						<input  id="homefours" style="width: 130px"  data-options="prompt:'区'"/>
						<input type="hidden" id="patientCitys" />	
						<input class="easyui-textbox" id="address" style="width: 250px; "	data-options="missingMessage:'请填写详细地址!',prompt:'社区/乡镇村详细地址'"/>
						<a href="javascript:void(0);" onclick="delSelectedSJLD()" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
					</td>
				</tr>	
			</table>
			</fieldset>
		</div>

		<div style="padding: 10px 5px 5px 5px;">
			<fieldset style="border:1px solid #95b8e7;margin-left:auto;margin-right:auto;" class="changeskin">
			<legend><font style="font-weight: bold;font-size: 12px;">患者基本信息</font></legend>
				<table class="tableCss" cellpadding="1" cellspacing="1" border="1px solid black">
					<input type="hidden" id="idcards"  value="${idcard }">
					<input type="hidden" id="ids"  value="${id }">
					<input type="hidden" id="medicalrecordId"  value="${medicalrecordId }">
					<tr>
					<td class="TDlabel">
					   病历号：
					   </td>
					   <td colspan="3"  class="honry-view"  id="medicalrecordIds"  >
					   </td>
					</tr>
					<tr>
						<td class="TDlabel" >
							患者姓名：
						</td>
						<td class="honry-view"  id="patientNames" >
							&nbsp;
						</td>
						<td class="TDlabel">
							性别：
						</td>
						<td id="patientSexs">
							&nbsp;
						</td>										
					</tr>				
					<tr>
						<td class="TDlabel">
							出生日期：
						</td>
						<td id="patientBirthdayNext">
							&nbsp;
						</td>
						<td class="TDlabel">
							联系方式：
						</td>
						<td class="honry-view" id="patientPhone">
							&nbsp;
						</td>
					</tr>
					<tr>					
						<td class="TDlabel">
							证件类型：
						</td>
						<td class="honry-view" id="patientCertificatestype">
							&nbsp;
						</td>
						<td class="TDlabel">
							证件号码：
						</td>
						<td class="honry-view" id="patientCertificatesno">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="TDlabel">
							家庭地址：
						</td>
						<td class="honry-view" colspan="3" id="patientCityAndAddress">
							&nbsp;
						</td>
					</tr>
				</table>
			</fieldset>
		</div>

		<div style="padding: 10px 5px 5px 5px;">	
			<fieldset style="border:1px solid #95b8e7;margin-left:auto;margin-right:auto;" class="changeskin">
			<legend><font style="font-weight: bold;font-size: 12px;">就诊卡基本信息</font></legend>
				<div id="message" style="font-size: 14px; color: #FF0000; float: left"></div>
				<table class="tableCss" cellpadding="1" cellspacing="1" border="1px solid black">
					<tr>
						<td class="TDlabel" >
							卡号：
						</td>
						<td class="honry-view" id="idcardNos">
							&nbsp;
						</td>
						<td class="TDlabel">
							建卡时间：
						</td>
						<td class="honry-view" id="idcardCreatetimes">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="TDlabel">
							&nbsp;卡类型：
						</td>
						<td class="honry-view" id="idcardType">
							&nbsp;
						</td>
						<td class="TDlabel">
							操作人员：
						</td>
						<td class="honry-view" id="idcardOperator">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="TDlabel">
							备注：
						</td>
						<td class="honry-view" id="idcardRemark"  >
							&nbsp;
						</td>
						<td class="TDlabel">
							变更日志：
						</td>
						<td class="honry-view" id="idcardChange" >
						</td>
					</tr>
				</table>
				<div id="dialog"></div>  
			</fieldset>
		</div>
		</div>
	</div>
	<!-- 弹出框 -->
	<div id="dd"></div>  
	</body>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-lang-zh_CN.js" type="text/javascript"></script>
</html>