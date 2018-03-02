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
<body>
	<div class="easyui-panel" id="panelEast" data-options="iconCls:'icon-form'" style="padding:10px">
		<form id="editForm" method="post">
			<input type="hidden" id="empId" name="preregisterExpert" value="${empId }">
			<input type="hidden" id="deptId" name="preregisterDept" value="${deptId }">
			<input type="hidden" id="gradeId" name="preregisterGrade" value="${gradeId }">
			<input type="hidden" id="preregisterDate" name="preregisterDate" value="${date }">
			<input type="hidden" id="preregisterGradeName" name="preregisterGradeName" >
			<input type="hidden" id="scheduleId" name="scheduleId" >
			<div style="padding:5px 5px 5px 5px;">
				<table id="yyfs" class="honry-table" cellpadding="1" cellspacing="1"
					border="1px solid black" style="width: 100%;border: 1px solid #95b8e7; padding: 5px;">
					<tr>
						<td class="honry-lable">
							卡号 ：
						</td>
						<td >
							<input id="idcardNo" name ="idcardNo" class="easyui-textbox"/>
							&nbsp;&nbsp;
							<a href="javascript:void(0)" onclick="searchFromcx()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							&nbsp;<a href="javascript:submit();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							病例号 ：
						</td>
						<td>
							<input id="blh" class="easyui-textbox" name="medicalrecordId">
						</td>
					<tr>
						<td class="honry-lable">
							建卡时间 ：
						</td>
						<td>
							<input id="jksj" readonly="readonly" class="Wdate" type="text" data-options="showSeconds:true ">
						</td>
					</tr>
					<tr>
				</table>
				</div>
				<div style="padding:5px 5px 5px 5px;">
				<table id="xx" class="honry-table" cellpadding="1" cellspacing="1"
					border="1px solid black" style="width: 100%; border: 1px solid #95b8e7; padding: 5px;">
					<tr>
						<td class="honry-lable">
							姓名 ：
						</td>
						<td>
							<input id="xingming" class="easyui-textbox"
								name="preregisterName" data-options="required:true"
								missingMessage="请输入拼姓名"  />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							性别 ：
						</td>
						<td>
						<input type="hidden" id="sexHid" name="preregisterSex">
							<input id="preregisterSex"  class="easyui-combobox"name="sexS"  data-options="">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							年龄 ：
						</td>
						<td>
							<input id="nianling" class="easyui-numberbox"
								name="preregisterAge">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							年龄单位 ：
						</td>
						<td>
							<input id="ageUnits" class="easyui-comberbox"
								name="ageUnits">
								<input id="ageUnit" type="hidden" name="preregisterAgeunit">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							联系电话 ：
						</td>
						<td>
							<input id="lxdh" class="easyui-numberbox" name="preregisterPhone">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							通讯地址 ：
						</td>
						<td>
							<input id="dizhi" class="easyui-textbox"
								name="preregisterAddress">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							证件类型 ：
						</td>
						<td>
						<input id="zjlxHidden" name="preregisterCertificatesname" type="hidden">
							<input id="zjlx" class="easyui-combobox"
								name="preregisterCertificatestype">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							证件号码 ：
						</td>
						<td>
							<input id="zjhm" class="easyui-numberbox"
								 name="preregisterCertificatesno">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">挂号午别 ：</td>
	    				<td>
	    					<input id="middayHidden" name="preregisterMiddayname" value="${preregisterMiddayname }" type="hidden">
		    	      	 	<input class="easyui-combobox" id="midday" value="${midday }"  name="midday" data-options=" valueField: 'value',textField: 'label',data: [{label: '上午',value: '1'},{label: '下午',value: '2'},{label: '晚上',value: '3'}],required:true,editable:false" /> 
		    			</td>
					</tr>
					</table>					
				</div>
				<div style="padding:5px 5px 5px 5px;">
				<table id="xx" class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width: 100%; border: 1px solid #95b8e7; padding: 5px;">
					<tr>
								<td style="font-size: 14" class="honry-lable">挂号日期 ：</td>
								<td>
									<input id="date" class="easyui-textbox" data-options="required:true" name="date" value="${date }" >
								</td>
					</tr>
					
					<tr>
									<td  style="font-size: 14" class="honry-lable">
										开始时间 ：
									</td>
									<td  style="font-size: 14" >
										<input id="kssj" class="easyui-textbox" value="${preregisterStarttime }" data-options="required:true" name="preregisterStarttime"  >
									</td>
								</tr>
								<tr>
									<td  style="font-size: 14" class="honry-lable">
										结束时间 ：
									</td>
									<td  style="font-size: 14">
										<input id="jssj"  class="easyui-textbox" value="${preregisterEndtime }" data-options="required:true" name="preregisterEndtime">
									</td>
								</tr>
				</table>					
				</div>
		</form>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/keydown.js"></script>
	<script type="text/javascript">
	$(function(){
	       	$('#lxdh').numberbox({required:false});
		    $('#blh').textbox({ readonly:true});
	        $('#xingming').textbox({ readonly:true});
	        $('#preregisterSex').textbox({ readonly:true});
	        $('#nianling').textbox({ readonly:true});
	        $('#ageUnits').textbox({ readonly:true});
	        $('#dizhi').textbox({ readonly:true});
	        $('#zjlx').textbox({ readonly:true});
	        $('#zjhm').textbox({ readonly:true});
	        $('#lxdh').numberbox({ readonly:true});
	       	$('#isnetHidden').val(0);
	   	 	$('#isphoneHidden').val(0);
	   	 var empId=$('#empId').val();
		//医师挂号级别
		$.ajax({
		url : "<c:url value='/outpatient/businesspreregister/searchempId.action'/>?empId="+empId, 
		type:'post',
		success: function(empIdObj) {
			$('#preregisterGradeName').val(empIdObj.gradeName);
		}
		});	
		//证件类型
		$('#zjlx') .combobox({ 
		    url : "<c:url value='/outpatient/businesspreregister/likeCertificate.action'/>",      
		    valueField:'encode',    
		    textField:'name',
		    multiple:false,
		    editable:false
		});
		
		//预约开始时间不能大于预约结束时间
        $('#jssj').textbox({
	        onSelect:function(){
	        var kssj= $('#kssj').textbox('getValue');
	        var jssj= $('#jssj').textbox('getValue');
	        if(jssj<=kssj){
		        $('#jssj').textbox('setValue','');
		        $.messager.alert("操作提示","结束时间不能小于开始时间");
		        setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
	        }
        }
        
        });
         //预约开始时间不能大于预约结束时间
        $('#kssj').textbox({
	        onSelect:function(){
	        var kssj= $('#kssj').textbox('getValue');
	        var jssj= $('#jssj').textbox('getValue');
	        if(jssj<=kssj){
		        $('#kssj').textbox('setValue','');
		       		$.messager.alert("操作提示","结束时间不能小于开始时间");
		       		setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
		       	}
	        }
        });
 		//适用性别
		$('#preregisterSex').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
		    valueField:'encode',    
		    textField:'name',
		    editable:false
		});
		var sexHid = $('#sexHid').val();
		if(sexHid!=null&&sexHid!=""){
			$('#preregisterSex').combobox('setValue',sexHid);
		}
		
		/*//年龄单位
		$('#ageUnits') .combobox({  
			url : "<c:url value='/likeAgeunit.action'/>", 
		    valueField:'id',    
		    textField:'name',
		    multiple:false,
		    editable:false	
		});*/
		setTimeout(function(){
			bindEnterEvent('idcardNo',searchFromcx,'easyui');
		},100);
	});
	//午别带入开始结束时间
		$('#midday').combobox({ 
		    onSelect:function(){
		            var midday=$('#midday').combobox('getValue');
		    		var preregisterDate=$('#preregisterDate').val();
		    		var gradeId=$('#gradeId').val();
		    		var deptId=$('#deptId').val();
			    	$.ajax({
			    	    url : "<c:url value='/outpatient/businesspreregister/getTimeBymiddy.action'/>?preregisterDate="+preregisterDate+"&gradeid="+gradeId+"&deptid="+deptId+"&middy="+midday,
					    type:'post',
			            success: function(timeMap) {
			            if(timeMap.resMsg=="error"){
			            	 $.messager.alert("操作提示","对不起该时间段医生没有排班");
			            	 setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
			            	 $('#midday').combobox('setValue',"");
			            	 $('#kssj').textbox('setValue',"");
			            	 $('#date').textbox('setValue',"");
							 $('#jssj').textbox('setValue',"");	
			            }else{
			            	$('#kssj').textbox('setValue', timeMap.startTime);
						    $('#jssj').textbox('setValue', timeMap.endTime);	
						    $('#date').textbox('setValue', timeMap.date);
						    $('#middayHidden').val($('#midday').combobox('getText'));
						    $('#scheduleId').val(timeMap.id);
						    
			            }
			           }
			    	});
				}
    	});
//就诊卡查询
function searchFromcx(){
	var idcardNo = $('#idcardNo').textbox('getValue');
	if(idcardNo==null||idcardNo==""){
		$.messager.alert('提示','请输入就诊卡号');	
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return false;
	}
	$.ajax({
		url : "<c:url value='/outpatient/businesspreregister/searchIdcard.action'/>?idcardNo="+idcardNo, 
		type:'post',
		success: function(idCardObj) {
			if(idCardObj.voPname==null || idCardObj.voPname==""){
				$.messager.alert('提示','没有患者信息');	
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}
			$('#blh').textbox('setValue',idCardObj.voPmedicalrecordId);
			$('#xingming').textbox('setValue',idCardObj.voPname);
			$('#preregisterSex').combobox('setValue',idCardObj.voPsex);
			$('#lxdh').textbox('setValue',idCardObj.voPphone);
			$('#dizhi').textbox('setValue',idCardObj.voPaddrss);
			$('#zjlx').combobox('setValue',idCardObj.voPtype);
			$('#zjhm').textbox('setValue',idCardObj.voPtypeNo);
			$('#jksj').val(idCardObj.cIdCardNoTime);
			var age=idCardObj.voPdata;
			var ages=DateOfBirth(age);
			$('#nianling').textbox('setValue',ages.get("nianling"));
		    $('#ageUnits').textbox('setValue',ages.get("ageUnits"));
			$('#ageUnit').val(ages.get("ageUnit"));
			$('#zjlxHidden').val($('#zjlx').combobox('getText'));
			$('#sexHid').val($('#preregisterSex').combobox('getText'));
		}
	});	
}

//提交验证
function submit(){
	queryPreInfo();
}

function queryPreInfo(){
	var dates = $('#preregisterDate').val();
	var idCardno = $('#idcardNo').val();
	var midday = $('#midday').combobox('getValue');
	if(idCardno==null||idCardno==""){
		$.messager.alert("操作提示","请先录入就诊卡号");
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return;
	}
	$.ajax({
		url: "<%=basePath%>outpatient/businesspreregister/queryPreInfo.action",
		type:'post',
		data:{"idCardno":idCardno,"dates":dates,"midday":midday},
		success: function(data) {
			if(data.resMsg=="error"){
				$.messager.alert("操作提示","该患者已经挂号");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}else{
				submitSave();
			}
		}
	});	
}

function submitSave(){
	$('#editForm').form('submit', {
		url : "<c:url value='/outpatient/businesspreregister/editPreregisterVo.action'/>",
		data:$('#editForm').serialize(),
	    dataType:'json',
		onSubmit : function() {
			if(!$('#editForm').form('validate')){
				$.messager.show({  
			         title:'提示信息' ,   
			         msg:'验证没有通过,不能提交表单!'  
			    }); 
			       return false ;
			       
			}
			$.messager.progress({text:'保存中，请稍后...',modal:true});
		},
		success:function(data){ 
			var dataObj = eval("("+data+")");
			if(dataObj.resCode=="error"){
				$.messager.progress('close');
				$.messager.alert("操作提示",dataObj.resMsg);
			}
			if(dataObj.resCode=="success"){
				$.messager.progress('close');
				 window.location.href="<c:url value='/outpatient/businesspreregister/listPreregister.action'/>?menuAlias=${menuAlias}";
			}
		 }
		}); 
}
	
</script>
</body>
</html>