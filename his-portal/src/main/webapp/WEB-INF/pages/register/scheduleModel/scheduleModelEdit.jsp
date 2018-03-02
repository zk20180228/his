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
		<div class="easyui-panel" id="panelEast" style="border:0px">
			<div style="padding:10px">
	    		<form id="editForm" method="post">
					<input type="hidden" id="id" name="id" value="${model.id }">
					<input type="hidden" name="createUser" value="${model.createUser }">
					<input type="hidden" name="createDept" value="${model.createDept }">
					<input type="hidden" name="createTime" value="${model.createTime }">
					<input type="hidden" name="stop_flg" value="${model.stop_flg }">
					<input type="hidden" name="del_flg" value="${model.del_flg }">
					<input type="hidden" id="modeType" name="modeType" value="2">
					<input type="hidden" id="deptId" name="department" value="${dept.id }">
					<input type="hidden" id="deptIsforregister" value="${workdept.deptIsforregister }">
					<input type="hidden" id="deptName" value="${dept.deptName }">
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td class="honry-lable">排班分类：</td>
			    			<td class="honry-info"><input type="hidden" id="modelClassHidden" value="${model.modelClass }"/><input id="modelClass" name="modelClass" data-options="required:true" missingMessage="请选择分类" style="width: 200"/></td>
		    			</tr>
		    			<tr id="deptTrId" style="display: none">
							<td class="honry-lable">工作科室：</td>
			    			<td class="honry-info"><input type="hidden" id="modelWorkdeptHidden" value="${workdept.id }"/>
			    			<input id="modelWorkdept" type="hidden" name="modelWorkdept" value="${workdept.id }" style="width: 200"/>
			    			<input id="modelWorkdeptName" type="hidden" value="${workdept.deptName }"/>
			    			<input id="modelWorkdeptText" type="text"   style="width: 200"/>
			    			<a href="javascript:delSelectedData('modelWorkdept,modelWorkdept,modelWorkdeptText,clinicHidden,clinic');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
			    			</td>
		    			</tr>
						<tr id="clinicTrId" style="display: none">
							<td class="honry-lable">工作诊室：</td>
			    			<td class="honry-info"><input type="hidden" id="clinicHidden" value="${model.clinic }"/><input id="clinic" name="clinic" class="easyui-combobox" data-options="required:true" missingMessage="请选择诊室" style="width: 200"/></td>
		    			</tr>
		    			<tr id="doctorTrId" style="display: none">
							<td class="honry-lable">排班人员：</td>
			    			<td class="honry-info"><input type="hidden" id="modelDoctorHidden" value="${model.modelDoctor }"/><input id="modelDoctor" class="easyui-combobox" name="modelDoctor" data-options="required:true" missingMessage="请选择人员" style="width: 200"/>
							<a href="javascript:delSelectedData('modelDoctor');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
			    			</td>
		    			</tr>
						<tr id="weekTrId" style="display: none">
							<td class="honry-lable">星期：</td>
			    			<td class="honry-info"><input id="modelWeek" name="modelWeek" value="${model.modelWeek }" data-options="required:true" missingMessage="请选择星期" style="width: 200"/></td>
		    			</tr>
						<tr id="middayTrId" style="display: none">
							<td class="honry-lable">午别：</td>
			    			<td class="honry-info"><input id="modelMidday" name="modelMidday" value="${model.modelMidday }" data-options="required:true" missingMessage="请选择午别" style="width: 200"/></td>
		    			</tr>
		    			<tr id="startTimet" style="display: none">
							<td class="honry-lable" style="font-size: 16">开始时间：</td>
			    			<td class="honry-info"><input id="modelStartTime" name="modelStartTime" value="${model.modelStartTime }" class="easyui-timespinner"  style="width:150px;"   required="required"/>  </td>
		    			</tr>
		    			<tr id="endTimet" style="display: none">
							<td class="honry-lable" style="font-size: 16">结束时间：</td>
			    			<td class="honry-info"><input id="modelEndTime" name="modelEndTime" value="${model.modelEndTime }" class="easyui-timespinner"  style="width:150px;"   required="required"/>  </td>
		    			</tr>
		    			<tr id="gradeTrId" style="display: none">
							<td class="honry-lable">挂号级别：</td>
			    			<td class="honry-info"><input type="hidden" id="modelReggradeHid" name="modelReggrade" value="${model.modelReggrade }"/><input id="modelReggrade"  data-options="readonly:true" class="easyui-textbox" style="width: 200"/></td>
		    			</tr>
						<tr id="limitTrId" style="display: none">
							<td class="honry-lable">挂号限额：</td>
			    			<td class="honry-info"><input type="text" class="easyui-numberbox" id="modelLimit" name="modelLimit" missingMessage="挂号限额和特诊限额，只能是一个大于0，一个等于0！<br>最大值：999" value="" data-options="max:999,min:0,precision:0,validType:['modelLimit','maxValue[999]']" style="width: 200" /></td>
		    			</tr>
						<tr id="prelimitTrId" style="display: none">
							<td class="honry-lable">预约限额：</td>
			    			<td class="honry-info"><input type="text" class="easyui-numberbox" id="modelPrelimit" name="modelPrelimit" missingMessage="预约限额应小于等于挂号限额！<br>最大值：999" value="" data-options="min:0,max:999,precision:0,validType:['modelPrelimit','modelPreUpperlimit','maxValue[999]']" style="width: 200"/></td>
		    			</tr>
						<tr id="phonelimitTrId" style="display: none">
							<td class="honry-lable">电话限额：</td>
			    			<td class="honry-info"><input type="text" class="easyui-numberbox" id="modelPhonelimit" name="modelPhonelimit" missingMessage="电话限额和网络限额相加应该小于等于预约限额！<br>最大值：999" value="" data-options="min:0,max:999,precision:0,validType:['modelPhonelimit','maxValue[999]']" style="width: 200"/></td>
		    			</tr>
						<tr id="netlimitTrId" style="display: none">
							<td class="honry-lable">网络限额：</td>
			    			<td class="honry-info"><input type="text"  class="easyui-numberbox" id="modelNetlimit" name="modelNetlimit" missingMessage="电话限额和网络限额相加应该小于等于预约限额！<br>最大值：999" value="" data-options="min:0,max:999,precision:0,validType:['modelNetlimit','maxValue[999]']" style="width: 200"/></td>
		    			</tr>
						<tr id="speciallimitTrId" style="display: none">
							<td class="honry-lable">特诊限额：</td>
			    			<td class="honry-info"><input type="text"  class="easyui-numberbox" id="modelSpeciallimit" name="modelSpeciallimit" missingMessage="特诊限额和挂号限额，只能是一个大于0，一个等于0！<br>最大值：999" value="" data-options="min:0,precision:0,validType:['modelSpeciallimit','maxValue[999]']" style="width: 200"/></td>
		    			</tr>
						<tr id="appflagTrId" style="display: none">
							<td class="honry-lable">是否加号：</td>
			    			<td class=""><input type="hidden"" id="modelAppflagHidden" name="modelAppflag" value="${model.modelAppflag }"/>
				    					<input type="checkBox" id="modelAppflag" onclick="javascript:onclickBox('modelAppflag')"/></td>
		    			</tr>
						<tr id="remarkTrId" style="display: none">
							<td class="honry-lable">备注：</td>
			    			<td class="honry-info"><textarea class="easyui-validatebox" rows="4" cols="32" id="modelRemark" name="modelRemark" data-options="multiline:true" style="width: 200">${model.modelRemark }</textarea></td>
		    			</tr>
		    	</table>
			    <div style="text-align:center;padding:5px">
				    <c:if test="${model.id==null }">
				    	<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
				    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	</c:if>
			    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>
	<script type="text/javascript">
		var gradMapIdTitle = null;
		var gradMap = null;
		var popWinDeptCallBackFn = null;
		var weekMap=new Map();
		var weekEnMap=new Map();
		//页面加载
		$(function(){
			$.ajax({
			    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=week",
				type:'post',
				success: function(data) {
					var wtype = data;
					for(var i=0;i<wtype.length;i++){
						weekMap.put(wtype[i].encode,wtype[i].name);
					}
				}
			});
			$.ajax({
			    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=weeken",
				type:'post',
				success: function(data) {
					var wetype = data;
					for(var i=0;i<wetype.length;i++){
						weekEnMap.put(wetype[i].encode,wetype[i].name);
					}
				}
			});
			$.ajax({
				url: "<c:url value='/outpatient/grade/getGradeIdTitleMap.action'/>",
				type:'post',
				success: function(gradData) {
					gradMapIdTitle = gradData;
				}
			});	
			$.ajax({
				url: "<c:url value='/outpatient/grade/getGradeMap.action'/>",
				type:'post',
				success: function(gradData) {
					gradMap = gradData;
				}
			});	
			var idVal = $('#id').val();
			
			$('#modelClass').combobox({//模板分类
			    data:[{id:1,name:"挂号排班模板"},{id:2,name:"工作排班模板"}],
			    valueField:'id',    
			    textField:'name',
			    panelHeight:50,
			    editable:false,
			    multiple:false,
			    required:true,
			    onSelect:function(record){
			    	var modelClass = 1;
			    	if(record.id==1){//挂号排班模板
			    		regiFun();
			    	}else{//工作排班模板
			    		modelClass = 2;
			    		weekFun();
			    	}
			    	/*
					初始化工作科室框
					*/
					$("#modelWorkdeptText").textbox({
						readonly:true,
						required:true,
						missingMessage:"请选择工作科室",
						invalidMessage:"请选择工作科室",
						value:(function(){
							if(idVal){
								$("#modelWorkdept").val($('#modelWorkdeptHidden').val());
								var deptIdTmp = $('#modelWorkdeptHidden').val();
								 $('#clinic').combobox({//查询诊室    
						    			url: "<c:url value='/baseinfo/clinic/getClinicByDeptId.action'/>?deptId="+deptIdTmp,
									    valueField:'id',    
									    textField:'clinicName',
									    editable:false,
									    required:(modelClass==1)?true:false,
							    		onLoadSuccess:function(){
									    	var data = $('#clinic').combobox('getData');
				                            if (data.length > 0) {
				                                $("#clinic").combobox('select', data[0].id);
				                            }
									    }
									});
								return $("#modelWorkdeptName").val();
							}else{
								$("#modelWorkdept").val($('#deptId').val());
								var deptIdTmp = $('#deptId').val();
								 $('#clinic').combobox({//查询诊室    
						    			url: "<c:url value='/baseinfo/clinic/getClinicByDeptId.action'/>?deptId="+deptIdTmp,
									    valueField:'id',    
									    textField:'clinicName',
									    editable:false,
									    required:(modelClass==1)?true:false,
							    		onLoadSuccess:function(){
									    	var data = $('#clinic').combobox('getData');
				                            if (data.length > 0) {
				                                $("#clinic").combobox('select', data[0].id);
				                            }
									    }
									});
								return $('#deptName').val();
							}
						})()
					});
					bindEnterEvent('modelWorkdeptText',popWinToDept,'easyui');//绑定回车事件 
					 /**
					   * 回车弹出工作科室弹框
					   * @author  wanxing
					   * @param deptIsforregister 是否是挂号科室 1是 0否
					   * @param textId 页面上commbox的的id
					   * @date 2016-03-29 18:13   
					   * @version 1.0
					   */
					   function popWinToDept(){
						   var cla = $('#modelClass').combobox('getValue');
						   if(cla!=2){
							   return;
						   }
						   /*
						   	*定义弹窗之后单击行的回调函数
						   */
						   popWinDeptCallBackFn = function(node){
							   $("#modelWorkdept").val(node.id);
							   $("#modelWorkdeptText").textbox('setValue',node.deptName);
							   $('#clinic').combobox({//查询诊室    
					    			url: "<c:url value='/baseinfo/clinic/getClinicByDeptId.action'/>?deptId="+node.id,
								    valueField:'id',    
								    textField:'clinicName',
								    editable:false,
								    required:(modelClass==1)?true:false,
						    		onLoadSuccess:function(){
								    	var data = $('#clinic').combobox('getData');
			                            if (data.length > 0) {
			                                $("#clinic").combobox('select', data[0].id);
			                            }
								    }
								});
						   };
						  var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptIsforregister=1&textId=scheduleWorkdept";
						  window.open(tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
					   }
			    	$('#modelDoctor').combobox({//查询医生       
		    			url: "<c:url value='/outpatient/scheduleModel/getEmployeeByDeptId.action'/>?modelClass="+modelClass+"&search="+$('#deptId').val(),    
					    valueField:'jobNo',    
					    textField:'name',
					    editable:false,
					    required:true,
					    onLoadSuccess:function(none){
		    				if(idVal!=null&&idVal!=''){
		    					if($('#modelClass').combobox('getValue')==2){
		    						$('#modelDoctor').combobox('select',$('#modelDoctorHidden').val());
		    					}
		    				}
		    				bindEnterEvent('modelDoctor',popWinToEmployee,'easyui');//绑定回车事件
		    			},
					    onSelect:function(record){
					    	if($('#modelClass').combobox('getValue')==1){
					    		if(record!=null&&record!=''){
						    		$('#modelReggradeHid').val(gradMapIdTitle[record.title]);
							    	$('#modelReggrade').textbox('setText',gradMap[gradMapIdTitle[record.title]]);
					    		}else{
					    			var grade = $('#modelReggradeHid').val()
							    	$('#modelReggrade').textbox('setText',gradMap[grade]);
					    		}
					    	}else{
					    		$('#modelReggradeHid').val(null);
						    	$('#modelReggrade').textbox('setText',null);
					    	}
					    }
					})
			    }
			});
		
		$('#modelWeek').combobox({//查询星期       
			url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=week',   
		    valueField:'encode',    
		    textField:'name',
		    panelHeight:170,
		    editable:false,
		    required:true
		});
		

			
			$('#modelMidday').combobox({//查询午别       
				url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=midday',
			    valueField:'encode',    
			    textField:'name',
			    panelHeight:75,
			    editable:false,
			    required:true,
			    onSelect:function(record){
			    	if(record.encode=="1"){
			    		$('#modelStartTime').timespinner('setValue',"00:00");
			    		$('#modelEndTime').timespinner('setValue',"12:00");
			    	}else if(record.encode=="2"){
			    		$('#modelStartTime').timespinner('setValue',"12:00");
			    		$('#modelEndTime').timespinner('setValue',"18:00");
			    	}else if(record.encode=="3"){
			    		$('#modelStartTime').timespinner('setValue',"18:00");
			    		$('#modelEndTime').timespinner('setValue',"23:59");
			    	}
			    }
			});
			if($('#modelAppflagHidden').val()==1){
				$('#modelAppflag').attr("checked", true);
			}
			if(idVal!=null&&idVal!=''){
				setTimeout(function(){
					var modelClass = $('#modelClassHidden').val();
					$('#modelClass').combobox('select',modelClass);
					/*
					初始化工作科室框
					*/
					$("#modelWorkdeptText").textbox({
						readonly:true,
						required:true,
						missingMessage:"请选择工作科室",
						invalidMessage:"请选择工作科室",
						value:(function(){
								
								$("#modelWorkdept").val($('#modelWorkdeptHidden').val());
								var deptIdTmp = $('#modelWorkdeptHidden').val();
								 $('#clinic').combobox({//查询诊室    
						    			url: "<c:url value='/baseinfo/clinic/getClinicByDeptId.action'/>?deptId="+deptIdTmp,
									    valueField:'id',    
									    textField:'clinicName',
									    editable:false,
									    required:(modelClass==1)?true:false,
							    		onLoadSuccess:function(){
									    	var data = $('#clinic').combobox('getData');
				                            if (data.length > 0) {
				                                $("#clinic").combobox('select', data[0].id);
				                            }
									    }
									});
								return $("#modelWorkdeptName").val();
						})()
					});
					bindEnterEvent('modelWorkdeptText',popWinToDept,'easyui');//绑定回车事件 
					 /**
					   * 回车弹出工作科室弹框
					   * @author  wanxing
					   * @param deptIsforregister 是否是挂号科室 1是 0否
					   * @param textId 页面上commbox的的id
					   * @date 2016-03-29 18:13   
					   * @version 1.0
					   */
					   function popWinToDept(){
						   var cla = $('#modelClass').combobox('getValue');
						   if(cla!=2){
							   return;
						   }
						   /*
						   	*定义弹窗之后单击行的回调函数
						   */
						   popWinDeptCallBackFn = function(node){
							   $("#modelWorkdept").val(node.id);
							   $("#modelWorkdeptText").textbox('setValue',node.deptName);
							   $('#clinic').combobox({//查询诊室    
					    			url: "<c:url value='/baseinfo/clinic/getClinicByDeptId.action'/>?deptId="+node.id,
								    valueField:'id',    
								    textField:'clinicName',
								    editable:false,
								    required:(modelClass==1)?true:false,
						    		onLoadSuccess:function(){
								    	var data = $('#clinic').combobox('getData');
			                            if (data.length > 0) {
			                                $("#clinic").combobox('select', data[0].id);
			                            }
								    }
								});
						   };
						  var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptIsforregister=1&textId=scheduleWorkdept";
						  window.open(tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -1000) +',height='+ (screen.availHeight-370)+',scrollbars,resizable=yes,toolbar=yes')
					   }
	 				$('#clinic').combobox({//查询诊室    
		    			url: "<c:url value='/baseinfo/clinic/getClinicByDeptId.action'/>?deptId="+$('#modelWorkdeptHidden').val(),
					    valueField:'id',    
					    textField:'clinicName',
					    editable:false,
					    required:(modelClass==1)?true:false,
					    onLoadSuccess:function(none){
					    	$('#clinic').combobox('select',$('#clinicHidden').val());
					    }
					});
	 				$('#modelDoctor').combobox('select',$('#modelDoctorHidden').val());
	 				if($('#modelAppflagHidden').val()==1){
	 					$('#modelAppflag').attr("checked", true);
	 				}
	 				$('#modelLimit').numberbox({value:"${model.modelLimit }"});
	 				$('#modelPrelimit').numberbox({value:"${model.modelPrelimit }"});
	 				$('#modelPhonelimit').numberbox({value:"${model.modelPhonelimit }"});
	 				$('#modelNetlimit').numberbox({value:"${model.modelNetlimit }"});
	 				$('#modelSpeciallimit').numberbox({value:"${model.modelSpeciallimit }"});
				},50);
			}
		});
		//表单提交
		function submit(){ 
			var id = weekEnMap.get($('#modelWeek').combobox('getValue'));
			var name = weekMap.get($('#modelWeek').combobox('getValue'));
		    $('#editForm').form('submit',{ 
		    	url: "<c:url value='/outpatient/scheduleModel/saveSchedulemodel.action'/>",
		        onSubmit:function(){
		        	var deptIs= $('#deptIsforregister').val();
		    		if(deptIs!=1){
		    			$.messager.alert('提示','您选择的科室非挂号科室,不能挂号排班');
		    			return false;
		    		}
		    		var modelClass=$('#modelClass').combobox('getValue');
		    		if(modelClass==1){
		    			var regText = $('#modelReggrade').textbox('getText');
			        	if(regText==null){
			        		$.messager.alert('提示','挂号级别不能为空,请联系管理员添加挂号级别或修改医生信息');
			        		return false;
			        	}
		    		}
		        	var modelStartTime = $('#modelStartTime').timespinner('getValue');
		    		var modelEndTime = $('#modelEndTime').timespinner('getValue');
		    		if(modelStartTime>=modelEndTime){
		    			$.messager.alert('提示','开始时间不能大于等于结束时间');
		        		return false;
		    		}
					if (!$('#editForm').form('validate')) {
						$.messager.progress('close');	
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});
		        },  
		        success:function(data){ 
		        	$.messager.progress('close');	
		        	if(data=="error"){
		        		$.messager.alert("提示","保存失败，您无此操作权限!");
		        	}else if(data=="exist"){
		        		$.messager.alert("提示","该员工已存在此午别排班,请重新填写!");
		        		$('#modelMidday').combobox('clear');
		        	}else{
		        		$.messager.alert("提示","保存成功!");
		        		$('#divLayout').layout('remove','east');
		        		$('#tt').tabs('select',name);
                    	$('#'+id).datagrid('reload');
		        	}
		        },
				error : function(data) {
					$.messager.progress('close');	
					$.messager.alert("提示","保存失败!");	
				}							         
		    }); 
	    }	    
	    
		//连续添加
		function addContinue(){ 
			var id = weekEnMap.get($('#modelWeek').combobox('getValue'));
			var name = weekMap.get($('#modelWeek').combobox('getValue'));
		    $('#editForm').form('submit',{  
		    	url: "<c:url value='/outpatient/scheduleModel/saveSchedulemodel.action'/>",
		        onSubmit:function(){
		        	var regText = $('#modelReggrade').textbox('getText');
		        	if(regText==null){
		        		$.messager.alert('提示','挂号级别不能为空,请联系管理员添加挂号级别或修改医生信息');
		        		return false;
		        	}
					if (!$('#editForm').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});
		        },  
		        success:function(data){  
		        	$.messager.progress('close');
		        	if(data=="error"){
		        		$.messager.alert("提示","保存失败，您无此操作权限!");
		        	}else if(data=="exist"){
		        		$.messager.alert("提示","该医生已存在此午别排班,请重新填写!");
		        		$('#modelDoctor').combobox('clear');
		        		$('#modelMidday').combobox('clear');
		        	}else{
		        		$.messager.alert("提示",'保存成功');
		        		$('#'+id).datagrid('reload');
		        		clear();
		        		$('#modeType').combobox('setValue',2);
                    	$('#tt').tabs('select',name);
                    	
		        	}
		        },
				error : function(data) {
					$.messager.progress('close');
					$.messager.alert("提示",'保存失败！');	
				}							         
		    }); 
	    }

		//清除页面填写信息
		function clear(){
			$('#editForm').form('reset');
		}
		//关闭编辑窗口
		function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
		//复选框赋值
		function onclickBox(id){
			if($('#'+id).is(':checked')){
				$('#'+id+'Hidden').val(1);
			}else{
				$('#'+id+'Hidden').val(0);
			}
		}
		//工作模板调用
		function weekFun(){
			$('#doctorTrId').show();
			$('#deptTrId').show();
			$('#clinicTrId').show();
			$('#weekTrId').show();
			$('#middayTrId').show();
			$('#gradeTrId').hide();
			$('#limitTrId').hide();
			$('#prelimitTrId').hide();
			$('#phonelimitTrId').hide();
			$('#netlimitTrId').hide();
			$('#speciallimitTrId').hide();
			$('#appflagTrId').hide();
			$('#remarkTrId').show();
			$('#modelLimit').numberbox({required:false});
			$('#modelPrelimit').numberbox({required:false});
			$('#modelPhonelimit').numberbox({required:false});
			$('#modelNetlimit').numberbox({required:false});
			$('#modelSpeciallimit').numberbox({required:false});
		}
		//挂号模板调用
		function regiFun(){
			$('#startTimet').show();
			$('#endTimet').show();
			$('#doctorTrId').show();
			$('#deptTrId').show();
			$('#clinicTrId').show();
			$('#weekTrId').show();
			$('#middayTrId').show();
			$('#gradeTrId').show();
			$('#limitTrId').show();
			$('#prelimitTrId').show();
			$('#phonelimitTrId').show();
			$('#netlimitTrId').show();
			$('#speciallimitTrId').show();
			$('#appflagTrId').show();
			$('#remarkTrId').show();
			$('#modelLimit').numberbox({required:true});
			$('#modelPrelimit').numberbox({required:true});
			$('#modelPhonelimit').numberbox({required:true});
			$('#modelNetlimit').numberbox({required:true});
			$('#modelSpeciallimit').numberbox({required:true});
		}
/**
 * 关于numberbox的默认规则，挂号限额和特诊限额，只能是一个大于0，另一个等于0
 *	网络限额+电话限额<预约限额，预约<挂号，
 * @author  huangbiao
 * @date 2016-3-25
 * @version 1.0
 */
$.extend($.fn.numberbox.defaults.rules, {
	//挂号限额规则
	modelLimit: {    
        validator: function(value){ 
        	var modelSpeciallimitValue =$('#modelSpeciallimit').numberbox('getValue');
			if(value>0){
				$('#modelSpeciallimit').numberbox('setValue',0);
				var prelimit = $('#modelPrelimit').numberbox('getValue');
				if(prelimit==null||prelimit==''){
					$('#modelPrelimit').numberbox('setValue',0);
				}
				var Netlimit = $('#modelNetlimit').numberbox('getValue');
				if(Netlimit==null||Netlimit==''){
					$('#modelNetlimit').numberbox('setValue',0);
				}
				var Phonelimit = $('#modelPhonelimit').numberbox('getValue');
				if(prelimit==null||prelimit==''){
					$('#modelPhonelimit').numberbox('setValue',0);
				}
				if(modelSpeciallimitValue>0){
					return false;
				}
			}else if(value==0){
				if(modelSpeciallimitValue==0){
					return false;
				}
			}
        	return true;
        },
        message:"挂号限额和特诊限额，只能是一个大于0，一个等于0！"
	},
	
	maxValue: {   //最大值规则 
        validator: function(value, param){ 
        	return value<=param[0];
        },
        message:"值不能大于999！"
	},
	
	//特诊限额规则
	modelSpeciallimit: {    
        validator: function(value){ 
        	var modelLimitValue =$('#modelLimit').numberbox('getValue');
			if(value>0){
// 				$('#modelLimit').numberbox('setValue',0);
// 				$('#modelPrelimit').numberbox('setValue',0);
// 				$('#modelPhonelimit').numberbox('setValue',0);
// 				$('#modelNetlimit').numberbox('setValue',0);
				if(modelLimitValue>0){
					setTimeout(function(){
						$('#modelSpeciallimit').numberbox('setValue',0);
					},900);
					
					return false;
				}
			}else if(value==0){
				if(modelLimitValue==0){
					return false;
				}
			}
        	return true;
        },
        message:"挂号限额和特诊限额，只能是一个大于0，一个等于0！"
	},
	
	//预约限额规则
	modelPrelimit:{
		validator: function(value){    
			var modelPhonelimitValue = $('#modelPhonelimit').val();
			var modelNetlimitValue = $('#modelNetlimit').val();
			if(value>0){
				if(modelPhonelimitValue!=""&&modelNetlimitValue==""){
					if(Number(modelPhonelimitValue)>Number(value)){
						
						return false;
					}
				}else if(modelPhonelimitValue==""&&modelNetlimitValue!=""){
					if(Number(modelNetlimitValue)>Number(value)){
						return false;
					}
				}else if(modelPhonelimitValue!=""&&modelNetlimitValue!=""){
					if(Number(modelPhonelimitValue)+Number(modelNetlimitValue)>value){
						return false;
					}
				}
			}
			if(value==0||value==""){
				$('#modelPhonelimit').numberbox('setValue',0);
				$('#modelNetlimit').numberbox('setValue',0);
				if(modelPhonelimitValue!=""&&modelPhonelimitValue!=0){
					return false;
				}
				if(modelNetlimitValue!=""&&modelNetlimitValue!=0){
					return false;
				}
			}
			return true;
        },
        message:'电话限额和网络限额相加应该小于等于预约限额！'
	},
	
	modelPreUpperlimit:{//规则：预约限额小于挂号限额
		validator: function(value){    
			var modelLimitValue = $('#modelLimit').val();
			if(value>0){
				if(Number(value)>Number(modelLimitValue)){
					setTimeout(function(){
						$('#modelPrelimit').numberbox('setValue',0);
					},900);
					
					return false;
				}
			}
			return true;			
        },
        message:'预约限额应小于等于挂号限额！'
	},
	
	//电话限额规则
	modelPhonelimit:{
		validator: function(value){    
			var modelNetlimitValue = $('#modelNetlimit').val();
			var modelPrelimitValue = $('#modelPrelimit').val();
			if(value>0){
				if(modelPrelimitValue!=""){
					if(modelNetlimitValue!=""){
						if(Number(value)+Number(modelNetlimitValue)>modelPrelimitValue){
							setTimeout(function(){
								$('#modelPhonelimit').numberbox('setValue',0);
							},900);
							
							//$('#modelNetlimit').numberbox('setValue',0);
							return false;
						}
					}else{
						if(Number(value)>Number(modelPrelimitValue)){
							return false;
						}
					}
				}else{
					return false;
				}
			}
			return true;			
        },
        message:'电话限额和网络限额相加应该小于等于预约限额！'
	},
	
	//网络限额规则
	modelNetlimit:{
		validator: function(value){    
			var modelPhonelimitValue = $('#modelPhonelimit').val();
			var modelPrelimitValue = $('#modelPrelimit').val();
			if(value>0){
				if(modelPrelimitValue!=""){
					if(modelPhonelimitValue!=""){
						if(Number(value)+Number(modelPhonelimitValue)>modelPrelimitValue){
							//$('#modelPhonelimit').numberbox('setValue',0);
							
							setTimeout(function(){
								$('#modelNetlimit').numberbox('setValue',0);
							},900);
							return false;
						}else{
							return true;
						}
					}else{
						if(value>=modelPrelimitValue){
							return false;
						}else{
							return true;
						}
					}
				}else{
					return false;
				}
			}
			return true;			
        },
        message:'电话限额和网络限额相加应该小于等于预约限额！'
	}
	
})


	
		 /**
		   * 回车弹出工作科室弹框
		   * @author  zhuxiaolu
		   * @param deptIsforregister 是否是挂号科室 1是 0否
		   * @param textId 页面上commbox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
		   function popWinToDept(){
				var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptIsforregister=1&textId=modelWorkdept";
				window.open (tempWinPath,'newwindow',' left=300,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
			}

	   /**
		   * 回车弹出排班人员弹框
		   * @author  zhuxiaolu
		   * @param textId 页面上commbox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
		   function popWinToEmployee(){
			   var modelClass=$('#modelClass').combobox('getValue');
			   var deptId=$('#deptId').val();
			   if(modelClass==1){
				   var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=modelDoctor&schedual=2&employeeType=1&deptIds="+deptId;
				   window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-
				   170)+',scrollbars,resizable=yes,toolbar=yes')
			   }else if(modelClass==2){
				   var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=modelDoctor&schedual=1&deptIds="+deptId;
				   window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-
				   170)+',scrollbars,resizable=yes,toolbar=yes')
			   }
		 }
	</script>
	</body>
</html>