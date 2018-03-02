<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String mpMap =request.getAttribute("mpMap").toString();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
		<div class="easyui-panel" id="panelEast" style="border:0px">
			<div style="padding:10px">
	    		<form id="editForm" method="post">
					<input type="hidden" id="id" name="id" value="${schedule.id }">
					<input type="hidden" name="createUser" value="${model.createUser }">
					<input type="hidden" name="createDept" value="${model.createDept }">
					<input type="hidden" name="createTime" value="${model.createTime }">
					<input type="hidden" name="stop_flg" value="${model.stop_flg }">
					<input type="hidden" name="del_flg" value="${model.del_flg }">
					<input type="hidden" id="type" name="type" value="2">
					<input type="hidden" id="deptId" name="department" value="${dept.id }">
					<input type="hidden" id="deptName" value="${dept.deptName }">
					<input type="hidden" id="deptIsforregister" value="${workdept.deptIsforregister }">
					<input type="hidden" id="dayParam" value="${dayParam }">
					<input type="hidden" id="tabId" value="<fmt:formatDate value="${schedule.date }" pattern="yyyy-MM-dd"/>">
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
		    			<tr>
							<td class="honry-lable">排班分类：</td>
			    			<td class="honry-info"><input type="hidden" id="scheduleClassHidden" value="${schedule.scheduleClass}"/><input id="scheduleClass" name="scheduleClass" data-options="required:true" missingMessage="请选择分类" style="width: 200"/></td>
		    			</tr>
		    			<tr id="deptTrId" style="display: none">
							<td class="honry-lable">工作科室：</td>
			    			<td class="honry-info"><input type="hidden" id="scheduleWorkdeptHidden" value="${workdept.id }"/>
			    			<input id="scheduleWorkdept" type="hidden"  name="scheduleWorkdept" value="${workdept.id }" style="width: 200"/>
			    			<input id="scheduleWorkdeptName" type="hidden" value="${workdept.deptName }"/>
			    			<input id="scheduleWorkdeptText" type="text"   style="width: 200"/>
							<a href="javascript:delSelectedData('scheduleWorkdept,scheduleWorkdeptText,scheduleWorkdept,clinicHidden,clinic');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
			    			</td>
		    			</tr>
		    			<tr id="clinicTrId" style="display: none">
							<td class="honry-lable">工作诊室：</td>
			    			<td class="honry-info"><input type="hidden" id="clinicHidden" value="${schedule.clinic }"/><input id="clinic" name="clinic" class="easyui-combobox" data-options="required:true" missingMessage="请选择诊室" style="width: 200"/></td>
		    			</tr>
		    			<tr id="doctorTrId" style="display: none">
							<td class="honry-lable" style="font-size: 14">排班人员：</td>
			    			<td class="honry-info"><input type="hidden" id="doctorHidden" value="${schedule.doctor }"/><input id="doctor" name="doctor" class="easyui-combobox" data-options="required:true" missingMessage="请选择医生" style="width: 200"/>
			    			<a href="javascript:delSelectedData('doctor');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
			    			</td>
		    			</tr>
						<tr id="dateTrId" style="display: none">
							<td class="honry-lable" style="font-size: 14">日期：</td>
			    			<td class="honry-info">
			    			<input id="date" name="date" class="Wdate" value="<fmt:formatDate value="${schedule.date }" pattern="yyyy-MM-dd"/>" type="text" readonly="readonly" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-{%d}',maxDate:'%y-%M-{%d+6}'})"style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>
			    			</input></td>
		    			</tr>
						<tr id="middayTrId" style="display: none">
							<td class="honry-lable" style="font-size: 14">午别：</td>
			    			<td class="honry-info"><input id="midday" name="midday" value="${schedule.midday }" data-options="required:true" missingMessage="请选择午别" style="width: 200"/></td>
		    				
		    			</tr>
		    			
		    			<tr id="startTimet" style="display: none">
							<td class="honry-lable" style="font-size: 16">开始时间：</td>
			    			<td class="honry-info"><input id="startTime" name="startTime" value="${schedule.startTime }" class="easyui-timespinner"  style="width:150px;"   required="required"/>  </td>
		    			</tr>
		    			<tr id="endTimet" style="display: none">
							<td class="honry-lable" style="font-size: 16">结束时间：</td>
			    			<td class="honry-info"><input id="endTime" name="endTime" value="${schedule.endTime }" class="easyui-timespinner"  style="width:150px;"   required="required"/>  </td>
		    			</tr>
		    			<tr id="gradeTrId" style="display: none">
							<td class="honry-lable" style="font-size: 14">挂号级别：</td>
			    			<td class="honry-info"><input type="hidden" id="reggradeHid" name="reggrade" value="${schedule.reggrade }"/><input id="reggrade" class="easyui-textbox" data-options="readonly:true" missingMessage="请填写挂号级别" style="width: 200"/></td>
		    			</tr>
		    			
						<tr id="limitTrId" style="display: none">
							<td class="honry-lable" style="font-size: 14">挂号限额：</td>
			    			<td class="honry-info"><input type="text" class="easyui-numberbox" id="limit" name="limit" missingMessage="挂号限额和特诊限额，只能是一个大于0，一个等于0！<br>最大值：999" data-options="min:0,max:999,precision:0,validType:['modelLimit','maxValue[999]']" style="width: 200" ></input></td>
		    			</tr>
						<tr id="prelimitTrId" style="display: none">
							<td class="honry-lable" style="font-size: 14">预约限额：</td>
			    			<td class="honry-info"><input type="text" class="easyui-numberbox" id="preLimit" name="preLimit"  missingMessage="挂号限额和特诊限额，只能是一个大于0，一个等于0！<br>最大值：999"  value="" data-options="min:0,max:999,precision:0,validType:['modelPrelimit','modelPreUpperlimit','maxValue[999]']" style="width: 200"/></td>
		    			</tr>
						<tr id="phonelimitTrId" style="display: none">
							<td class="honry-lable" style="font-size: 14">电话限额：</td>
			    			<td class="honry-info"><input type="text" class="easyui-numberbox" id="phoneLimit" name="phoneLimit"  missingMessage="电话限额和网络限额相加应该小于等于预约限额！<br>最大值：999"  value="" data-options="min:0,max:999,precision:0,validType:['modelPhonelimit','maxValue[999]']" style="width: 200"/></td>
		    			</tr>
						<tr id="netlimitTrId" style="display: none">
							<td class="honry-lable" style="font-size: 14">网络限额：</td>
			    			<td class="honry-info"><input type="text" class="easyui-numberbox" id="netLimit" name="netLimit"  missingMessage="电话限额和网络限额相加应该小于等于预约限额！<br>最大值：999"  value="" data-options="min:0,max:999,precision:0,validType:['modelNetlimit','maxValue[999]']" style="width: 200"/></td>
		    			</tr>
		    			<tr id="speciallimitTrId" style="display: none">
							<td class="honry-lable" style="font-size: 14">特诊限额：</td>
			    			<td class="honry-info"><input type="text" class="easyui-numberbox" id="speciallimit" name="speciallimit"  missingMessage="特诊限额和挂号限额，只能是一个大于0，一个等于0！<br>最大值：999"  value="" data-options="min:0,max:999,precision:0,validType:['modelSpeciallimit','maxValue[999]']" style="width: 200"/></td>
		    			</tr>
						<tr id="appflagTrId" style="display: none">
							<td class="honry-lable" style="font-size: 14">是否加号：</td>
			    			<td class=""><input type="hidden" id="appFlagHidden" name="appFlag" value="${schedule.appFlag }"/>
				    					<input type="checkBox" id="appFlag" onclick="javascript:onclickBox('appFlag')"/></td>
		    			</tr>
						<tr  id="isStopTrId" style="display: none">
							<td class="honry-lable" style="font-size: 14">是否停诊：</td>
			    			<td class=""><input type="hidden" id="isStopHidden" name="isStop" value="${schedule.isStop }"/>
				    					<input type="checkBox" id="isStop" onclick="javascript:onclickBox('isStop')"/></td>
		    			</tr>
<!-- 		    			<tr id="stopDoctorTrId" style="display: none"> -->
<!-- 							<td class="honry-lable" style="font-size: 14">停诊人：</td> -->
<%-- 			    			<td class="honry-info"><input id="stopDoctor" class="easyui-textbox" name="stopDoctor.id" value="${schedule.stopDoctor.id }" style="width: 200"/></td> --%>
<!-- 		    			</tr> -->
<!-- 						<tr id="stoprEasonTrId" style="display: none"> -->
<!-- 							<td class="honry-lable" style="font-size: 14">停诊原因：</td> -->
<%-- 			    			<td class="honry-info"><textarea class="easyui-validatebox" rows="4" cols="32" id="stoprEason" name="stoprEason" data-options="multiline:true" style="width: 200">${schedule.stoprEason }</textarea></td> --%>
<!-- 		    			</tr> -->
						<tr id="stoprEasonTrId" style="display: none">
							<td class="honry-lable" style="font-size: 14">停诊原因：</td>
			    			<td class="honry-info">
			    				<input id="stoprEason" class="easyui-combobox" name="stoprEason" style="width:200px;" value="${schedule.stoprEason }"/>   
			    			</td>
		    			</tr>
						<tr id="remarkTrId" style="display: none">
							<td class="honry-lable" style="font-size: 14">备注：</td>
			    			<td class="honry-info"><textarea class="easyui-validatebox" rows="4" cols="32" id="remark" name="remark" data-options="multiline:true" style="width: 200">${schedule.remark }</textarea></td>
		    			</tr>
		    	</table>
			    <div style="text-align:center;padding:5px">
				    <c:if test="${schedule.id==null }">
				    	<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'"  style="font-size: 70">连续添加</a>
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
		//页面加载
		$(function(){
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
			
			$('#stoprEason').combobox({
				url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=stopReason',
			    valueField:'encode',    
			    textField:'name'
			});
			
			var idVal = $('#id').val();
			$('#scheduleClass').combobox({//模板分类
			    data:[{id:1,name:"挂号排班"},{id:2,name:"工作排班"}],
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
					$("#scheduleWorkdeptText").textbox({
						readonly:true,
						required:true,
						missingMessage:"请选择工作科室",
						invalidMessage:"请选择工作科室",
						value:(function(){
							if(idVal){
								$("#scheduleWorkdept").val($('#scheduleWorkdeptHidden').val());
								var deptIdTmp = $('#scheduleWorkdeptHidden').val();
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
								return $("#scheduleWorkdeptName").val();
							}else{
								$("#scheduleWorkdept").val($('#deptId').val());
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
					bindEnterEvent('scheduleWorkdeptText',popWinToDept,'easyui');//绑定回车事件 
					 /**
					   * 回车弹出工作科室弹框
					   * @author  wanxing
					   * @param deptIsforregister 是否是挂号科室 1是 0否
					   * @param textId 页面上commbox的的id
					   * @date 2016-03-29 18:13   
					   * @version 1.0
					   */
					   function popWinToDept(){
						   var cla = $('#scheduleClass').combobox('getValue');
						   if(cla!=2){
							   return;
						   }
						   /*
						   	*定义弹窗之后单击行的回调函数
						   */
						   popWinDeptCallBackFn = function(node){
							   $("#scheduleWorkdept").val(node.id);
							   $("#scheduleWorkdeptText").textbox('setValue',node.deptName);
							   $('#clinic').combobox({//查询诊室    
					    			url: "<c:url value='/baseinfo/clinic/getClinicByDeptId.action'/>?deptId="+node.id,
								    valueField:'id',    
								    textField:'clinicName',
								    editable:false,
								    required:(modelClass==1)?true:false
								});
						   };
						  var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptIsforregister=1&textId=scheduleWorkdept";
						  window.open(tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -1000) +',height='+ (screen.availHeight-370)+',scrollbars,resizable=yes,toolbar=yes')
					   }
			    	$('#doctor').combobox({//查询医生       
		    			url: "<c:url value='/outpatient/scheduleModel/getEmployeeByDeptId.action'/>?modelClass="+modelClass+"&search="+$('#deptId').val(),    
					    valueField:'jobNo',    
					    textField:'name',
					    editable:false,
					    required:true,
					    onLoadSuccess:function(none){
		    				if(idVal!=null&&idVal!=''){
		    					if($('#scheduleClass').combobox('getValue')==2){
		    						$('#doctor').combobox('select',$('#doctorHidden').val());
		    					}
		    				}
		    				bindEnterEvent('doctor',popWinToEmployee,'easyui');//绑定回车事件
		    			},
					    onSelect:function(record){
					    	if($('#scheduleClass').combobox('getValue')==1){
					    		if(record!=null&&record!=''){
					    			$('#reggradeHid').val(gradMapIdTitle[record.title]);
							    	$('#reggrade').textbox('setText',gradMap[gradMapIdTitle[record.title]]);
					    		}else{
					    			var grade = $('#reggradeHid').val()
							    	$('#reggrade').textbox('setText',gradMap[grade]);
					    		}
					    	}else{
					    		$('#reggradeHid').val(null);
						    	$('#reggrade').textbox('setText',null);
					    	}
					    }
					})
			    }
			});
			var dayParam = $('#dayParam').val();
			middyPMap = '<%=mpMap%>';
			middyPJson = $.parseJSON(middyPMap); 
			$('#midday').combobox({//查询午别       
				url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=midday',
			    valueField:'encode',    
			    textField:'name',
			    panelHeight:75,
			    editable:false,
			    required:true,
			    onSelect:function(record){
			    	if(record.encode=="1"){
			    		$('#startTime').timespinner('setValue',middyPJson['1'].split(",")[0]);
			    		$('#endTime').timespinner('setValue',middyPJson['1'].split(",")[1]);
			    	}else if(record.encode=="2"){
			    		$('#startTime').timespinner('setValue',middyPJson['2'].split(",")[0]);
			    		$('#endTime').timespinner('setValue',middyPJson['2'].split(",")[1]);
			    	}else if(record.encode=="3"){
			    		$('#startTime').timespinner('setValue',middyPJson['3'].split(",")[0]);
			    		$('#endTime').timespinner('setValue',middyPJson['3'].split(",")[1]);
			    	}
			    }
			});
			if($('#appFlagHidden').val()==1){
				$('#appFlag').attr("checked", true);
			}
			if($('#isStopHidden').val()==1){
				$('#isStop').attr("checked", true);
				$('#stoprEasonTrId').show();
				
			}else{
				$('#stoprEasonTrId').hide();
			}
			if(idVal!=null&&idVal!=''){
				setTimeout(function(){
					var modelClass = $('#scheduleClassHidden').val();
					$('#scheduleClass').combobox('select',modelClass);
					/*
					初始化工作科室框
					*/
					$("#scheduleWorkdeptText").textbox({
						required:true,
						missingMessage:"请选择工作科室",
						invalidMessage:"请选择工作科室",
						readonly:true,
						value:(function(){
								$("#scheduleWorkdept").val($('#scheduleWorkdeptHidden').val());
								return $('#scheduleWorkdeptName').val();
							})()
					});
					bindEnterEvent('scheduleWorkdeptText',popWinToDept,'easyui');//绑定回车事件 
					 /**
					   * 回车弹出工作科室弹框
					   * @author  wanxing
					   * @param deptIsforregister 是否是挂号科室 1是 0否
					   * @param textId 页面上commbox的的id
					   * @date 2016-03-29 18:13   
					   * @version 1.0
					   */
					   function popWinToDept(){
						   var cla = $('#scheduleClass').combobox('getValue');
						   if(cla!=2){
							   return;
						   }
						   /*
						   	*定义弹窗之后单击行的回调函数
						   */
						   popWinDeptCallBackFn = function(node){
							   $("#scheduleWorkdept").val(node.id);
							   $("#scheduleWorkdeptText").textbox('setValue',node.deptName);
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
							window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
					   }
	 				$('#clinic').combobox({//查询诊室    
		    			url: "<c:url value='/baseinfo/clinic/getClinicByDeptId.action'/>?deptId="+$('#scheduleWorkdeptHidden').val(),
					    valueField:'id',    
					    textField:'clinicName',
					    editable:false,
					    required:(modelClass==1)?true:false,
					    onLoadSuccess:function(none){
					    	$('#clinic').combobox('select',$('#clinicHidden').val());
					    }
					});
	 				$('#limit').numberbox({value:"${schedule.limit }"});
	 				$('#preLimit').numberbox({value:"${schedule.preLimit }"});
	 				$('#phoneLimit').numberbox({value:"${schedule.phoneLimit }"});
	 				$('#netLimit').numberbox({value:"${schedule.netLimit }"});
	 				$('#speciallimit').numberbox({value:"${schedule.speciallimit }"});
	 				$('#doctor').combobox('select',$('#doctorHidden').val());
	 				if($('#appFlagHidden').val()==1){
	 					$('#appFlag').attr("checked", true);
	 				}
	 				if($('#isStopHidden').val()==1){
	 					$('#isStop').attr("checked", true);
	 					$('#stoprEasonTrId').show();
	 				}else{
	 					$('#stoprEasonTrId').hide();
	 				}
				},50);
				
			}
		});
		//表单提交
		function submit(){ 
			var id = $('#date').val();
			var tabName = getTabName(id);
		    $('#editForm').form('submit',{  
		    	url: "<c:url value='/outpatient/schedule/scheduleSave.action'/>",
		        onSubmit:function(){
		        	var deptIs= $('#deptIsforregister').val();
		    		if(deptIs!=1){
		    			$.messager.alert('提示','您选择的科室非挂号科室,不能挂号');
		    			return false;
		    		}
		    		var scheduleClass=$('#scheduleClass').combobox('getValue');
		    		if(scheduleClass==1){
		    			var regText = $('#reggrade').textbox('getText');
			        	if(regText==null){
			        		$.messager.alert('提示','挂号级别不能为空,请联系管理员添加挂号级别或修改医生信息');
			        		return false;
			        	}
		    		}
		        	var startTime = $('#startTime').timespinner('getValue');
		    		var endTime = $('#endTime').timespinner('getValue');
		        	if(startTime>=endTime){
		        		$.messager.alert('提示','开始时间不能大于等于结束时间');
		        		return false;
		        	}
		        	if($('#isStop').is(':checked')){
		        		var stoprEason1 = $('#stoprEason').combobox('getValue');
		        		if(stoprEason1==""){
			        		$.messager.alert('提示','请选择停诊原因');
			        		return false;
		        		}
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
		        		$.messager.alert("提示","该医生已存在此午别排班,请重新填写!");
		        		$('#doctor').combobox('clear');
		        		$('#midday').combobox('clear');
		        	}else{
		        		$.messager.alert("提示","保存成功!");
		        		$('#divLayout').layout('remove','east');
                    	$('#tt').tabs('select',tabName);
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
			var id = $('#tabId').val();
			var tabName = getTabName(id);
		    $('#editForm').form('submit',{  
		    	url: "<c:url value='/outpatient/schedule/scheduleSave.action'/>",
		        onSubmit:function(){
		        	var regText = $('#reggrade').textbox('getText');
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
		        		$.messager.alert("提示","该员工已存在此午别排班,请重新填写!");
		        		$('#doctor').combobox('clear');
		        	}else{
		        		$.messager.alert("提示",'保存成功');
		        		$('#'+id).datagrid('reload');
		        		clear();
// 		        		$('#type').combobox('setValue',2);
		        		$('#type').val(2);
                    	$('#tt').tabs('select',tabName);
						
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
				if(id=="isStop"){
					$('#stoprEasonTrId').show();
				}
			}else{
				if(id=="isStop"){
					$('#'+id+'Hidden').val(2);
					$('#stoprEasonTrId').hide();
				}else{
					$('#'+id+'Hidden').val(0);
				}
			}
		}
		//获得TabName
		function getTabName(param){
			return param+"<br>"+getWeekByString(param);
		}
		//工作模板调用
		function weekFun(){
			$('#doctorTrId').show();
			$('#deptTrId').show();
			$('#clinicTrId').show();
			$('#weekTrId').show();
			$('#dateTrId').show();
			$('#middayTrId').show();
			$('#startTimet').show();
			$('#endTimet').show();
			$('#gradeTrId').hide();
			$('#limitTrId').hide();
			$('#prelimitTrId').hide();
			$('#phonelimitTrId').hide();
			$('#netlimitTrId').hide();
			$('#speciallimitTrId').hide();
			$('#appflagTrId').hide();
			$('#isStopTrId').hide();
			$('#stoprEasonTrId').hide();
			$('#remarkTrId').show();
			$('#limit').numberbox({required:false});
			$('#preLimit').numberbox({required:false});
			$('#phoneLimit').numberbox({required:false});
			$('#netLimit').numberbox({required:false});
			$('#speciallimit').numberbox({required:false});
		}
		//挂号模板调用
		function regiFun(){
			$('#doctorTrId').show();
			$('#deptTrId').show();
			$('#clinicTrId').show();
			$('#weekTrId').show();
			$('#dateTrId').show();
			$('#middayTrId').show();
			$('#startTimet').show();
			$('#endTimet').show();
			$('#gradeTrId').show();
			$('#limitTrId').show();
			$('#prelimitTrId').show();
			$('#phonelimitTrId').show();
			$('#netlimitTrId').show();
			$('#speciallimitTrId').show();
			$('#appflagTrId').show();
			$('#isStopTrId').show();
			$('#stoprEasonTrId').hide();
			$('#remarkTrId').show();
			$('#limit').numberbox({required:true});
			$('#preLimit').numberbox({required:true});
			$('#phoneLimit').numberbox({required:true});
			$('#netLimit').numberbox({required:true});
			$('#speciallimit').numberbox({required:true});
		}
		
		/**
		 * 关于numberbox的默认规则，挂号限额和特诊限额，只能是一个大于0，另一个等于0
		 *	网络限额+电话限额<预约限额，预约<挂号，
		 * @author  huangbiao
		 * @date 2016-3-25
		 * @version 1.0											
		 */
		$.extend($.fn.numberbox.defaults.rules, { 
			modelLimit: {    //挂号限额
		        validator: function(value){ 
		        	var modelSpeciallimitValue =$('#speciallimit').numberbox('getValue');
					if(value>0){
						$('#speciallimit').numberbox('setValue',0);
						var prelimit = $('#preLimit').numberbox('getValue');
						if(prelimit==null||prelimit==''){
							$('#preLimit').numberbox('setValue',0);
						}
						var Netlimit = $('#netLimit').numberbox('getValue');
						if(Netlimit==null||Netlimit==''){
							$('#netLimit').numberbox('setValue',0);
						}
						var Phonelimit = $('#phoneLimit').numberbox('getValue');
						if(prelimit==null||prelimit==''){
							$('#phoneLimit').numberbox('setValue',0);
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
			
			modelSpeciallimit: {    //特诊限额
		        validator: function(value){ 
		        	var modelLimitValue =$('#limit').numberbox('getValue');
					if(value>0){
// 						$('#limit').numberbox('setValue',0);
// 						$('#preLimit').numberbox('setValue',0);
// 						$('#phoneLimit').numberbox('setValue',0);
// 						$('#netLimit').numberbox('setValue',0);
						if(modelLimitValue>0){
							$('#speciallimit').numberbox('setValue',0);
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
			
			modelPrelimit:{
				validator: function(value){    
					var modelPhonelimitValue = $('#phoneLimit').val();
					var modelNetlimitValue = $('#netLimit').val();
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
						$('#phoneLimit').numberbox('setValue',0);
						$('#netLimit').numberbox('setValue',0);
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
			
			modelPreUpperlimit:{//预约限额
				validator: function(value){    
					var modelLimitValue = $('#limit').val();
					if(value>0){
						if(Number(value)>Number(modelLimitValue)){
							$('#preLimit').numberbox('setValue',0);
							return false;
						}
					}
					return true;			
		        },
		        message:'预约限额应小于等于挂号限额！'
			},
			
			modelPhonelimit:{//电话限额
				validator: function(value){    
					var modelNetlimitValue = $('#netLimit').val();
					var modelPrelimitValue = $('#preLimit').val();
					if(value>0){
						if(modelPrelimitValue!=""){
							if(modelNetlimitValue!=""){
								if(Number(value)+Number(modelNetlimitValue)>modelPrelimitValue){
									$('#phoneLimit').numberbox('setValue',0);
									//$('#netLimit').numberbox('setValue',0);
									return false;
								}
							}else{
								if(Number(value)>=Number(modelPrelimitValue)){
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
			
			modelNetlimit:{//网络限额
				validator: function(value){    
					var modelPhonelimitValue = $('#phoneLimit').val();
					var modelPrelimitValue = $('#preLimit').val();
					if(value>0){
						if(modelPrelimitValue!=""){
							if(modelPhonelimitValue!=""){
								if(Number(value)+Number(modelPhonelimitValue)>modelPrelimitValue){
									//$('#phoneLimit').numberbox('setValue',0);
									$('#netLimit').numberbox('setValue',0);
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
		   * 回车弹出排班人员弹框
		   * @author  zhuxiaolu
		   * @param textId 页面上commbox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
		   function popWinToEmployee(){
			   var scheduleClass=$('#scheduleClass').combobox('getValue');
			   var deptId=$('#scheduleWorkdept').val();
			   if(scheduleClass==1){
				   var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=doctor&schedual=2&employeeType=1&deptIds="+deptId;
				   window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-
				   170)+',scrollbars,resizable=yes,toolbar=yes')
			   }else if(scheduleClass==2){
				   var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=doctor&schedual=1&deptIds="+deptId;
				   window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-
				   170)+',scrollbars,resizable=yes,toolbar=yes')
			   }s
		 }
		
	</script>
	</body>
</html>