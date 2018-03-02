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
<div class="easyui-panel" id="panelEast">
			<div style="padding:10px">
	    		<form id="editForm" method="post">
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
		    			<tr>
									<td class="honry-lable">
										就诊卡号 ：
									</td>
									<td>
									    <input type="hidden" name="t.scheduleId"  id="scheduleIdhidden">
									    <input type="hidden" name="t.preregisterDeptname"  id="regDate">
										<input id="idcardNo" name="t.idcardId" class="easyui-textbox" />
									</td>
								</tr>
								<tr>
									<td  class="honry-lable">
										病历号 ：
									</td>
									<td>
										<input id="blh" class="easyui-textbox" name="t.medicalrecordId">
									</td>
								</tr>
								<tr>
									<td   class="honry-lable">
										姓名 ：
									</td>
									<td  >
										<input id="xingming" class="easyui-textbox"
											name="t.preregisterName" data-options="required:true,validType:'xingming'"
											missingMessage="请输入拼姓名"  />
									</td>
								</tr>
								<tr>
									<td  class="honry-lable">
										性别 ：
									</td>
									<td  >
										<input type="hidden" id="sexHid" name="t.preregisterSex">
										<input id="preregisterSex"  class="easyui-combobox"name="t.sexS">
									</td>
								</tr>
								<tr>
									<td   class="honry-lable">
										年龄 ：
									</td>
									<td  >
										<input style="width:100px"id="nianling" class="easyui-numberbox"
											name="t.preregisterAge">
										<input style="width:100px;"id="ageUnits" class="easyui-combobox"
											name="t.preregisterAgeunit">
									</td>
								</tr>
								<tr>
									<td   class="honry-lable">
										联系电话 ：
									</td>
									<td  >
										<input id="lxdh" class="easyui-numberbox" data-options="required:true,validType:'lxdh'" name="t.preregisterPhone">
									</td>
								</tr>
								<tr>
									<td   class="honry-lable">
										通讯地址 ：
									</td>
									<td  >
										<input id="dizhi" class="easyui-textbox"
											name="t.preregisterAddress">
									</td>
								</tr>
								<tr>
									<td   class="honry-lable">
										证件类型 ：
									</td>
									<td  >
										<input id="zjlxHidden" name="t.preregisterCertificatesname" type="hidden">
										<input id="zjlx" class="easyui-combobox"
											name="t.preregisterCertificatestype">
									</td>
								</tr>
								<tr  >
									<td class="honry-lable">
										证件号码 ：
									</td>
									<td  >
										<input id="zjhm" class="easyui-textbox" style="width:164px"
										    data-options="required:true,validType:'zjhm'" name="t.preregisterCertificatesno">
									</td>
								</tr>
								<tr>
									<td   class="honry-lable">
										预约时间 ：
									</td>
									<td  >
									<input id="time"  name="t.preregisterDate" readOnly="readonly" class="Wdate" type="text"  style="width:151px;border: 1px solid #95b8e7;border-radius: 5px;"/>
<!-- 										<input id="time"  class="easyui-datebox" name="t.preregisterDate" data-options="readonly:true"> -->
									</td>
								</tr>
								<tr>
									<td   class="honry-lable">
										挂号级别 ：
									</td>
									<td>
										<input id="jbHidden" name="t.preregisterGrade" type="hidden">
										<input id="jb" name="t.preregisterGradeName" class="easyui-textbox" data-options="readonly:true">
									</td>
								</tr>
								<tr>
									<td   class="honry-lable">
										挂号科室 ：
									</td>
									<td>
									   
										<input id="ksHidden" name="t.preregisterDept" type="hidden">
										<input id="ksName" name="t.preregisterDeptName" class="easyui-textbox" data-options="readonly:true">
									</td>
								</tr>
								<tr>
									<td   class="honry-lable">
										挂号专家 ：
									</td>
									<td  >
										<input id="zjHidden" type="hidden" name="t.preregisterExpert">
										<input id="zj"  name="t.preregisterExpertName" data-options="readonly:true" class="easyui-textbox">
									</td>
								</tr>
								
								<tr>
									<td   class="honry-lable">
										午别 ：
									</td>
									<td  >
										<input type="hidden" name="t.midday"  id="middayhidden">
										<input id="middayName"  name="t.preregisterMiddayname" data-options="readonly:true" class="easyui-textbox">
										<!-- 开始时间和结束时间 -->
										<input id="kssj" type="hidden" name="t.preregisterStarttime"  >
										<input id="jssj" type="hidden" name="t.preregisterEndtime">
									</td>
								</tr>
								
								
<!-- 								<tr> -->
<!-- 									<td   class="honry-lable"> -->
<!-- 										开始时间 ： -->
<!-- 									</td> -->
<!-- 									<td   > -->
<!-- 										<input id="kssj" class="easyui-timespinner" data-options="readonly:true" name="t.preregisterStarttime"  > -->
<!-- 									</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td   class="honry-lable"> -->
<!-- 										结束时间 ： -->
<!-- 									</td> -->
<!-- 									<td  > -->
<!-- 										<input id="jssj"  class="easyui-timespinner" data-options="readonly:true" name="t.preregisterEndtime"> -->
<!-- 									</td> -->
<!-- 								</tr> -->
		    	</table>
			    <div style="text-align:center;padding:5px">
			    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>
<script type="text/javascript">
$(function(){
	$('#scheduleIdhidden').val(row_data.id);//排班编号
	$('#middayhidden').val(row_data.midday);//午别
	$('#time').val(rq);//预约时间
	$('#regDate').val(rq);//预约时间 用于从缓存中获取key
	$('#ksHidden').val(row_data.scheduleWorkdept);//挂号科室(编号)
	$('#ksName').val(deptName);//科室名
	$('#zjHidden').val(row_data.doctor);//挂号专家(编号)
	if(row_data.scheduleDoctorname!=null){
		$('#zj').val(row_data.scheduleDoctorname);//专家名称
	}else{
		$('#zj').val(empMap[row_data.doctor]);
	}
	$('#jbHidden').val(row_data.reggrade);//挂号级别
	$('#jb').val(gradeMap[row_data.reggrade]);//级别名称
	var mid="";
	if(row_data.midday==1){
		mid="上午";
	}else if(row_data.midday==2){
		mid="下午";
	}else if(row_data.midday==3){
		mid="晚上";
	}
	$('#middayName').val(mid);
	$('#kssj').val(row_data.startTime);//开始时间
	$('#jssj').val(row_data.endTime);//结束时间
	
	//适用性别
	$('#preregisterSex').combobox({
		url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=sex'/>",
	    valueField:'encode',    
	    textField:'name',
	    editable:false,
	    onSelect:function(record){
	    	$('#sexHid').val(record.name);
	    }
	});
	
	//证件类型
	$('#zjlx') .combobox({ 
	    url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=certificate'/>",      
	    valueField:'encode',    
	    textField:'name',
	    multiple:false,
	    editable:false,
	   onSelect:function(record){
		   $('#zjlxHidden').val(record.name);
	   }
	    
	});
	
	//年龄单位
	$('#ageUnits') .combobox({  
		url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=ageunits",
	    valueField:'encode',    
	    textField:'name',
	    multiple:false,
	    editable:false,
	    width:'80px',
	    onChange:function(post){
    		$('#ageUnits').combobox('reload');
    	}
	    
	});
	
	$.extend($.fn.validatebox.defaults.rules, {     
	     xingming: {     
	         validator: function(value, param){  //姓名验证  
	        	 var pattern = /^[\u4E00-\u9FA5]{2,4}$/;
	        	 return pattern.test(value);
	         },     
	         message: '姓名须为2~4字符之间'    
	     },
	     lxdh: {// 验证电话号码
	        validator: function (value) {
	        	var t= /^0{0,1}(13[0-9]|15[7-9]|153|156|18[0-9])[0-9]{8}$/;
	            return t.test(value);
	        },
	        message: '请输入正确电话号码'
	    },
	      zjhm: {
			    validator: function(value){
			    	var t= $('#zjlx').combobox('getValue');
			    	if(t==3){
				    	// 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X  
				   		var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;  
					    if(reg.test(value))
					    {
					      return true;
					    }else{
					       return false;
					    }
			    	}else{
			    		return true;
			    	}
			    },
			    message: '请输入正确身份证格式'
		  }
	});
});
//关闭编辑窗口
function closeLayout(){
	$('#divLayout').layout('remove','east');
}

function submit(){
	var formdata=getFormData('editForm');
	$('#editForm').form('submit', {
		url : "<%=basePath%>outpatient/webPreregister/savePreregister.action",
		data:formdata,
        dataType:'json',
		onSubmit : function() {
			if(!$('#editForm').form('validate')){
				$.messager.show({  
			         title:'提示信息' ,   
			         msg:'验证没有通过,不能提交表单!'  
			    }); 
			       return false ;
			}
		},
		success:function(data){
			if(data=='1'){//成功
				$('#divLayout').layout('remove','east');
				$.messager.alert('提示','预约成功!');
				$('#'+rq).datagrid('reload');
			}
			if(data=='2'){//已预约过
				$('#divLayout').layout('remove','east');
				$.messager.alert('提示','您已预约过该专家!');
			}
			if(data=='0'){
				$('#divLayout').layout('remove','east');
				$.messager.alert('提示','该专家号源已满,请选择其他专家!');
				$('#'+rq).datagrid('reload');
			}
			if(data=='-1'){
				$.messager.alert('提示','数据错误,请重新填写!');
			}
		}
	});
}

function conveterParamsToJson(paramsAndValues) {
	var jsonObj = {};

	var param = paramsAndValues.split("&");
	for (var i = 0; param != null && i < param.length; i++) {
		var para = param[i].split("=");
		jsonObj[para[0]] = para[1];
	}

	return jsonObj;
}

/**
 * 将表单数据封装为json
 * @param form
 * @returns
 */
function getFormData(form) {
	var formValues = $("#" + form).serialize();

	//关于jquery的serialize方法转换空格为+号的解决方法  
	formValues = formValues.replace(/\+/g, " "); // g表示对整个字符串中符合条件的都进行替换  
	var temp = decodeURIComponent(JSON
			.stringify(conveterParamsToJson(formValues)));
	var queryParam = JSON.parse(temp);
	return queryParam;
}
</script>
</body>
</html>