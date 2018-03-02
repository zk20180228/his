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
		<div class="easyui-panel" id="panelEast" style="border:0">
			<div style="padding:10px">
	    		<form id="editForm" method="post">
					<input type="hidden" id="id" name="model.id" value="${model.id }">
					<input type="hidden" name="model.createUser" value="${model.createUser }">
					<input type="hidden" name="model.createDept" value="${model.createDept }">
					<input type="hidden" name="model.createTime" value="${model.createTime }">
					<input type="hidden" name="model.stop_flg" value="${model.stop_flg }">
					<input type="hidden" name="model.del_flg" value="${model.del_flg }">
					<input type="hidden" id="model.modeType" name="modeType" value="2">
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td class="honry-lable">模板分类：</td>
			    			<td class="honry-info">
			    			<c:if test="${model.modelClass==1}">
									<input id="modelClass" name="modelClass" value="项目排版模板" class="easyui-combobox" style="width: 200"/>
								</c:if>
		    					<c:if test="${model.modelClass==2}">
									<input id="modelClass" name="modelClass" value="设备排版模板" class="easyui-combobox" style="width: 200"/>
								</c:if>
			    			</td>
		    			</tr>
		    			<tr id="deptTrId" >
							<td class="honry-lable">科室：</td>
			    			<td class="honry-info"><input type="hidden" id="modelDeptid" name="model.modelDeptid" value="${model.modelDeptid }"/>
			    			<input id="modelWorkdeptText"  class="easyui-combobox" value="${model.modelDeptid }" data-options="required:true" style="width: 200"/>
			    			</td>
		    			</tr>
						<tr id="clinicTrId" >
							<td class="honry-lable">诊室：</td>
			    			<td class="honry-info"><input type="hidden" id="modelClinicid" name="model.modelClinicid" value="${model.modelClinicid }"/>
			    			<input id="clinic" class="easyui-combobox" value="${model.modelClinicid }" data-options="required:true" style="width: 200"/></td>
		    			</tr>
		    			<tr id="doctorTrId" >
							<td class="honry-lable">设备/项目名称：</td>
			    			<td class="honry-info"><input  id="modelItemCode" name="model.modelItemCode" type="hidden" value="${model.modelItemCode }"/>
			    			<input  id="modelDoctorHidden" name="model.modelItemName" class="easyui-textbox" data-options="editable:false" value="${model.modelItemName }" style="width: 200"/>
			    			</td>
		    			</tr>
						<tr id="weekTrId" >
							<td class="honry-lable">星期：</td>
			    			<td class="honry-info"><input id="modelWeek" name="model.modelWeek" value="${model.modelWeek }" data-options="required:true" missingMessage="请选择星期" style="width: 200"/></td>
		    			</tr>
						<tr id="middayTrId" >
							<td class="honry-lable">午别：</td>
			    			<td class="honry-info"><input id="modelMidday" name="model.modelMidday" value="${model.modelMidday }" data-options="required:true" missingMessage="请选择午别" style="width: 200"/></td>
		    			</tr>
		    			<tr id="modelStarttimeTrId" >
							<td class="honry-lable">开始时间：</td>
			    			<td class="honry-info"><input id="modelStarttime" name="model.modelStarttime" value="${model.modelStarttime }" class="easyui-timeSpinner" missingMessage="请选择时间" style="width: 200"/></td>
		    			</tr>
						<tr id="modelEndtimeTrId" >
							<td class="honry-lable">结束时间：</td>
			    			<td class="honry-info"><input id="modelEndtime" name="model.modelEndtime" value="${model.modelEndtime }" class="easyui-timeSpinner" missingMessage="请选择时间" style="width: 200"/></td>
		    			</tr>
						<tr id="prelimitTrId" >
							<td class="honry-lable">预约限额：</td>
			    			<td class="honry-info">
<%-- 			    			<a href="javascript:void(0)" title="预约限额应大于网络限额限额！<br>最大值：999" data-options="position:'bottom'" class="easyui-tooltip"> --%>
			    				<input type="text" class="easyui-numberbox" id="modelPrelimit" name="model.modelPrelimit" value="${model.modelPrelimit }" data-options="min:0,max:999,precision:0,validType:['modelPrelimit','modelPreUpperlimit','maxValue[999]']" style="width: 200"/>
<!-- 			    			</a> -->
			    			</td>
		    			</tr>
						<tr id="netlimitTrId"  >
							<td class="honry-lable">网络限额：</td>
			    			<td class="honry-info">
<%-- 			    			<a href="javascript:void(0)" title="网络限额和特诊限额相加应该小于预约限额！<br>最大值：999" data-options="position:'bottom'" class="easyui-tooltip"> --%>
			    				<input type="text"  class="easyui-numberbox" id="modelNetlimit" name="model.modelNetlimit" value="${model.modelNetlimit }" data-options="min:0,max:999,precision:0,validType:['modelNetlimit','maxValue[999]']" style="width: 200"/>
<!-- 			    			</a> -->
			    			</td>
		    			</tr>
						<tr id="speciallimitTrId"  >
							<td class="honry-lable">特诊限额：</td>
			    			<td class="honry-info">
<%-- 			    			<a href="javascript:void(0)" title="特诊限额和挂号限额，只能是一个大于0，一个等于0！<br>最大值：999" data-options="position:'bottom'" class="easyui-tooltip"> --%>
			    				<input type="text"  class="easyui-numberbox" id="modelSpeciallimit" name="model.modelSpeciallimit" value="${model.modelSpeciallimit }" data-options="min:0,precision:0,validType:['modelSpeciallimit','maxValue[999]']" style="width: 200"/>
<!-- 			    			</a> -->
			    			</td>
		    			</tr>
						<tr id="appflagTrId"  >
							<td class="honry-lable">是否加号：</td>
			    			<td class=""><input type="hidden" id="modelAppflagHidden" name="model.modelAppflag" value="${model.modelAppflag }" />
				    					<input type="checkBox" id="modelAppflag" onclick="javascript:onclickBox('modelAppflag')"/></td>
		    			</tr>
						<tr id="remarkTrId"  >
							<td class="honry-lable">注意事项：</td>
			    			<td class="honry-info"><textarea class="easyui-validatebox" rows="4" cols="32" id="modelRemark" name="model.modelAttentions" data-options="multiline:true" style="width: 200">${model.modelAttentions }</textarea></td>
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
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
		var gradMapIdTitle = null;
		var gradMap = null;
		var popWinDeptCallBackFn = null;
		var weekEnMap=new Map();
		var weekMap=new Map();
		var wetype = new Map();
		//页面加载
		$(function(){
			$.ajax({
			    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=weeken",
				type:'post',
				success: function(data) {
					wetype = data;
					for(var i=0;i<wetype.length;i++){
						weekEnMap.put(wetype[i].encode,wetype[i].name);
					}
				}
			});
			$.ajax({
			    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=week",
				type:'post',
				success: function(data) {
					var wtype = data;
					for(var i=0;i<wetype.length;i++){
						weekMap.put(wetype[i].encode,wetype[i].name);
					}
				}
			});
			var idVal = $('#id').val();
			$('#modelClass').combobox({//模板分类
			    data:[{id:1,name:"项目排班模板"},{id:2,name:"设备排班模板"}],
			    valueField:'id',    
			    textField:'name',
			    required:true,
			    onSelect:function(record){
			    }
			});
			    	/*
					初始化科室框
					*/
					$("#modelWorkdeptText").combobox({
						url:"<%=basePath%>technical/medicalMineplate/getDept.action",
						valueField:'deptCode',
						textField:'deptName',
						selectOnNavigation:true,
						onHidePanel:function(none){
						    var data = $(this).combobox('getData');
						    var val = $(this).combobox('getValue');
						    var result = true;
						    for (var i = 0; i < data.length; i++) {
						        if (val == data[i].deptCode) {
						            result = false;
						        }
						    }
						    if (result) {
						        $(this).combobox("clear");
						    }else{
						        $(this).combobox('unselect',val);
						        $(this).combobox('select',val);
						    }
						},
						filter:function(q,row){
							var keys = new Array();
							keys[keys.length] = 'deptCode';
							keys[keys.length] = 'deptName';
							keys[keys.length] = 'deptPinyin';
							keys[keys.length] = 'deptWb';
							keys[keys.length] = 'deptInputcode';
							return filterLocalCombobox(q, row, keys);
						},
						onSelect:function(){
							 $('#clinic').combobox('setValue','');
							 $('#modelClinicid').val('');
								var val=$(this).combobox("getValue");
// 								$('#clinic').combobox('setValue','');
								$("#modelDeptid").val(val);
								 $('#clinic').combobox({//查询诊室    
									 url: "<c:url value='/technical/medicalMineplate/getClinic.action'/>?deptId="+val,
									    valueField:'id',    
									    textField:'clinicName',
									    editable:false
									});
						}
						,onLoadSuccess:function(){
							var val=$(this).combobox("getValue");
							$("#modelDeptid").val(val);
// 							 $('#clinic').combobox({//查询诊室    
// 								 url: "<c:url value='/technical/medicalMineplate/getClinic.action'/>?deptId="+val,
// 								    valueField:'id',    
// 								    textField:'clinicName',
// 								    editable:false,
// 								    onLoadSuccess : function(){
// 								    	 $('#clinic').combobox('setValue',$('#modelClinicid').val());
// 								    }
// 								});
						}
					});
					$('#clinic').combobox({//查询诊室    
						url: "<c:url value='/technical/medicalMineplate/getClinic.action'/>?deptId="+$("#modelWorkdeptText").combobox("getValue"),
						valueField:'id',    
					    textField:'clinicName',
					    editable:false,
					    onSelect:function(){
							var val=$(this).combobox("getValue");
							$("#modelClinicid").val(val);
						}
						,onLoadSuccess : function(){
						    $('#clinic').combobox('setValue',$('#modelClinicid').val());
						 }
						
						});
					bindEnterEvent('modelWorkdeptText',popWinToDept,'easyui');//绑定回车事件 
					 /**
					   * 回车弹出科室弹框
					   * @author  zhangjin
					   * @param deptIsforregister 是否是挂号科室 1是 0否
					   * @param textId 页面上commbox的的id
					   * @date 2016-03-29 18:13   
					   * @version 1.0
					   */
// 					   function popWinToDept(){
// 						   /*
// 						   	*定义弹窗之后单击行的回调函数
// 						   */
// 						   popWinDeptCallBackFn = function(node){
// 							   $("#modelWorkdeptText").combobox('setValue',node.deptName);
// 							   $('#clinic').combobox({//查询诊室    
// 					    			url: "<c:url value='/technical/medicalMineplate/getClinic.action'/>?deptId="+node.id,
// 								    valueField:'id',    
// 								    textField:'clinicName',
// 								    editable:false,
// 								    required:(modelClass==1)?true:false,
// 						    		onLoadSuccess:function(){
// 								    	var data = $('#clinic').combobox('getData');
// 			                            if (data.length > 0) {
// 			                                $("#clinic").combobox('select', data[0].id);
// 			                            }
// 								    }
// 								});
// 						   };
//<%-- 						  var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptIsforregister=1&textId=scheduleWorkdept"; --%>
// 						  window.open(tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
// 					   }
			$('#modelWeek').combobox({//查询星期       
			    url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=week",
			    valueField:'encode',    
			    textField:'name',
			    panelHeight:170,
			    editable:false,
			    required:true
			});
			$('#modelMidday').combobox({//查询午别       
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=midday ",
				valueField : 'encode',
				textField : 'name',
			    panelHeight:75,
			    editable:false,
			    required:true,
			    onSelect:function(record){
			    	$('#modelPrelimit').numberbox('setValue',0);
			    	$('#modelNetlimit').numberbox('setValue',0);
			    	$('#modelSpeciallimit').numberbox('setValue',0);
			    	if(record.encode=="1"){
			    		$("#modelStarttime").timespinner("setValue",'9:30');//开始时间
						$("#modelEndtime").timespinner("setValue",'12:30');//结束时间
			    	}else if(record.encode=="2"){
			    		$("#modelStarttime").timespinner("setValue",'13:30');//开始时间
						$("#modelEndtime").timespinner("setValue",'15:30');//结束时间
			    	}else if(record.encode=="3"){
			    		$("#modelStarttime").timespinner("setValue",'19:30');//开始时间
						$("#modelEndtime").timespinner("setValue",'05:30');//结束时间
			    	}
			    }
			});
			if($('#modelAppflagHidden').val()==1){
				$('#modelAppflag').attr("checked", true);
			}
			if(idVal!=null&&idVal!=''){
				setTimeout(function(){
// 					var modelClass = $('#modelClassHidden').val().trim();
// 					$('#modelClass').combobox('select',$.trim($('#modelClassHidden').val()));
					/*
					初始化工作科室框
					*/
// 					$("#modelWorkdeptText").combobox({
// 						readonly:true,
// 						required:true,
// 						missingMessage:"请选择工作科室",
// 						invalidMessage:"请选择工作科室",
// 						value:(function(){
// 								$("#modelWorkdept").val($('#modelWorkdeptHidden').val());
// 								var deptIdTmp = $('#modelWorkdeptHidden').val();
// 								 $('#clinic').combobox({//查询诊室    
// 						    			url: "<c:url value='/sys/getClinicByDeptId.action'/>?deptId="+deptIdTmp,
// 									    valueField:'id',    
// 									    textField:'clinicName',
// 									    editable:false,
// 									    required:(modelClass==1)?true:false,
// 							    		onLoadSuccess:function(){
// 									    	var data = $('#clinic').combobox('getData');
// 				                            if (data.length > 0) {
// 				                                $("#clinic").combobox('select', data[0].id);
// 				                            }
// 									    }
// 									});
// 								return $("#modelWorkdeptName").val();
// 						})()
// 					});
					bindEnterEvent('modelWorkdeptText',popWinToDept,'easyui');//绑定回车事件 
					 /**
					   * 回车弹出科室弹框
					   * @author  wanxing
					   * @param deptIsforregister 是否是挂号科室 1是 0否
					   * @param textId 页面上commbox的的id
					   * @date 2016-03-29 18:13   
					   * @version 1.0
					   */
// 					   function popWinToDept(){
						   /*
						   	*定义弹窗之后单击行的回调函数
						   */
// 						   popWinDeptCallBackFn = function(node){
// 							   $("#modelWorkdept").val(node.id);
// 							   $("#modelWorkdeptText").combobox('setValue',node.deptName);
// 							   $('#clinic').combobox({//查询诊室    
// 					    			url: "<c:url value='/technical/medicalMineplate/getClinic.action'/>?deptId="+node.id,
// 								    valueField:'id',    
// 								    textField:'clinicName',
// 								    editable:false,
// 								    required:(modelClass==1)?true:false,
// 						    		onLoadSuccess:function(){
// 								    	var data = $('#clinic').combobox('getData');
// 			                            if (data.length > 0) {
// 			                                $("#clinic").combobox('select', data[0].id);
// 			                            }
// 								    }
// 								});
// 						   };
//<%-- 						  var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptIsforregister=1&textId=scheduleWorkdept"; --%>
// 						  window.open(tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -1000) +',height='+ (screen.availHeight-370)+',scrollbars,resizable=yes,toolbar=yes')
// // 					   }
// 	 				$('#clinic').combobox({//查询诊室    
// 		    			url: "<c:url value='/technical/medicalMineplate/getClinic.action'/>?deptId="+$('#modelWorkdeptHidden').val(),
// 					    valueField:'id',    
// 					    textField:'clinicName',
// 					    editable:false,
// 					    required:(modelClass==1)?true:false,
// 					    onLoadSuccess:function(none){
// 					    	$('#clinic').combobox('select',$('#clinicHidden').val());
// 					    }
// 					});
				},50);
			}
		});
		//表单提交
		function submit(){ 
			var id = weekEnMap.get($('#modelWeek').combobox('getValue'));
			var Starttime=$("#modelStarttime").spinner("getValue");//开始时间
			var Endtime=$("#modelEndtime").spinner("getValue");//结束时间
			var modelMidday=$("#modelMidday").combobox("getText");
			if(modelMidday=="上午"){
				if(Starttime>Endtime){
	 				$.messager.alert("提示","结束时间必须大于开始时间");
	 				setTimeout(function(){
	 					$(".messager-body").window('close');
	 				},3500);
	 				return ;
	 			}
				if(Starttime>"12:30"||Starttime<"09:30"){
					$.messager.alert("提示","请选择上午的时间[<span  style='color:blue'>09:30-12:30</span>]");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
			}else if(modelMidday=="下午"){
				if(Starttime>Endtime){
	 				$.messager.alert("提示","结束时间必须大于开始时间");
	 				setTimeout(function(){
	 					$(".messager-body").window('close');
	 				},3500);
	 				return ;
	 			}
				if(Starttime>"17:30"){
					$.messager.alert("提示","请选择下午的时间[<span  style='color:blue'>13:30-17:30</span>]");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
				if(Starttime<"13:30"){
					$.messager.alert("提示","请选择下午的时间[<span  style='color:blue'>13:30-17:30</span>]");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
			}else{
				if(Starttime>"05:00"&&Endtime<"19:30"){
					var s = Starttime.split(':');
					var e = Endtime.split(':');
					if(e[0]>"05"||s[0]<"19"){
						$.messager.alert("提示","请选择晚上的时间[<span  style='color:blue'>19:30-05:30</span>]");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return;
					}else if((s[0]==5&&s[1]>0)||(e[0]==19&&e[1]>30)){
						$.messager.alert("提示","请选择晚上的时间[<span  style='color:blue'>19:30-05:30</span>]");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return;
					}
				}
			}
// 			if(Starttime>Endtime){
// 				$.messager.alert("提示","结束时间必须大于开始时间");
// 				return ;
// 			}
		    $('#editForm').form('submit',{ 
		    	url: "<%=basePath%>technical/medicalMineplate/savemedicalMineplate.action",
		        onSubmit:function(){
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
		        		setTimeout(function(){
		        			$(".messager-body").window('close');
		        		},3500);
		        	}else if(data=="exist"){
		        		$.messager.alert("提示","该员工已存在此午别排班,请重新填写!");
		        		setTimeout(function(){
		        			$(".messager-body").window('close');
		        		},3500);
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
			var Starttime=$("#modelStarttime").spinner("getValue");//开始时间
			var Endtime=$("#modelEndtime").spinner("getValue");//结束时间
			if(Starttime>Endtime){
				$.messager.alert("提示","结束时间必须大于开始时间");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return ;
			}
			if(modelMidday=="上午"){
				if(Starttime>"12:30"||Starttime<"09:30"){
					$.messager.alert("提示","请选择上午的时间");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
			}else if(modelMidday=="下午"){
				if(Starttime>"17:30"||Starttime<"13:30"){
					$.messager.alert("提示","请选择下午的时间");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
			}else if(modelMidday=="晚上"){
				if(Starttime>"05:00"&&Starttime<"19:30"){
					$.messager.alert("提示","请选择晚上的时间");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
			}
		    $('#editForm').form('submit',{  
		    	url: "<%=basePath%>/technical/medicalMineplate/savemedicalMineplate.action",
		        onSubmit:function(){
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
		        		setTimeout(function(){
		        			$(".messager-body").window('close');
		        		},3500);
		        	}else if(data=="exist"){
		        		$.messager.alert("提示","该项目已存在此午别排班,请重新填写!");
		        		setTimeout(function(){
		        			$(".messager-body").window('close');
		        		},3500);
		        		$('#modelMidday').combobox('clear');
		        	}else{
		        		$.messager.alert("提示",'保存成功');
		        		clear();
		        		$('#modeType').combobox('setValue',2);
                    	$('#tt').tabs('select',name);
                    	$('#'+id).datagrid('reload');
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
// 		//工作模板调用
// 		function weekFun(){
// 			$('#doctorTrId').show();
// 			$('#deptTrId').show();
// 			$('#clinicTrId').show();
// 			$('#weekTrId').show();
// 			$('#middayTrId').show();
// 			$('#gradeTrId').hide();
// 			$('#limitTrId').hide();
// 			$('#prelimitTrId').hide();
// 			$('#phonelimitTrId').hide();
// 			$('#netlimitTrId').hide();
// 			$('#speciallimitTrId').hide();
// 			$('#appflagTrId').hide();
// 			$('#remarkTrId').show();
// 		}
// 		//挂号模板调用
// 		function regiFun(){
// 			$('#doctorTrId').show();
// 			$('#deptTrId').show();
// 			$('#clinicTrId').show();
// 			$('#weekTrId').show();
// 			$('#middayTrId').show();
// 			$('#gradeTrId').show();
// 			$('#limitTrId').show();
// 			$('#prelimitTrId').show();
// 			$('#phonelimitTrId').show();
// 			$('#netlimitTrId').show();
// 			$('#speciallimitTrId').show();
// 			$('#appflagTrId').show();
// 			$('#remarkTrId').show();
// 		}
/**
 * 关于numberbox的默认规则，挂号限额和特诊限额，只能是一个大于0，另一个等于0
 *	网络限额+电话限额<预约限额，预约<挂号，
 * @author  huangbiao
 * @date 2016-3-25
 * @version 1.0
 */
// $.extend($.fn.numberbox.defaults.rules, {
// 	maxValue: {   //最大值规则 
//         validator: function(value, param){ 
//         	return value<=param[0];
//         },
//         message:"值不能大于999！"
// 	},
// 	//特诊限额规则
// 	modelSpeciallimit: {    
//         validator: function(value){ 
//         	alert(value);
// 			if(value>0){
// 				$('#modelPrelimit').numberbox('setValue',0);
// 				$('#modelPhonelimit').numberbox('setValue',0);
// 				$('#modelNetlimit').numberbox('setValue',0);
// 			}else if(value==0){
// 			}
//         	return true;
//         },
// 	},
	
// 	//预约限额规则
// 	modelPrelimit:{
// 		validator: function(value){    
// 			var modelPhonelimitValue = $('#modelPhonelimit').val();
// 			var modelNetlimitValue = $('#modelNetlimit').val();
// 			if(value>0){
// 				if(modelPhonelimitValue!=""&&modelNetlimitValue==""){
// 					if(Number(modelPhonelimitValue)>Number(value)){
// 						return false;
// 					}
// 				}else if(modelPhonelimitValue==""&&modelNetlimitValue!=""){
// 					if(Number(modelNetlimitValue)>Number(value)){
// 						return false;
// 					}
// 				}else if(modelPhonelimitValue!=""&&modelNetlimitValue!=""){
// 					if(Number(modelPhonelimitValue)+Number(modelNetlimitValue)>=value){
// 						return false;
// 					}
// 				}
// 			}
// 			if(value==0||value==""){
// 				$('#modelPhonelimit').numberbox('setValue',0);
// 				$('#modelNetlimit').numberbox('setValue',0);
// 				if(modelPhonelimitValue!=""&&modelPhonelimitValue!=0){
// 					return false;
// 				}
// 				if(modelNetlimitValue!=""&&modelNetlimitValue!=0){
// 					return false;
// 				}
// 			}
// 			return true;
//         },
//         message:'电话限额和网络限额相加应该小于预约限额！'
// 	},
	
// 	modelPreUpperlimit:{//规则：预约限额小于挂号限额
// 		validator: function(value){    
// // 			var modelLimitValue = $('#modelLimit').val();
// 			if(value>0){
// 				if(Number(value)>=Number(modelLimitValue)){
// 					$('#modelPrelimit').numberbox('setValue',0);
// 					return false;
// 				}
// 			}
// 			return true;			
//         },
//         message:'预约限额应小于挂号限额！'
// 	},
	
	
// 	//网络限额规则
// 	modelNetlimit:{
// 		validator: function(value){    
// 			var modelPrelimitValue = $('#modelPrelimit').val();
// 			if(value>0){
// 				if(modelPrelimitValue!=""){
// 					if(modelPhonelimitValue!=""){
// 						if(Number(value)+Number(modelPhonelimitValue)>=modelPrelimitValue){
// 							$('#modelPhonelimit').numberbox('setValue',0);
// 							$('#modelNetlimit').numberbox('setValue',0);
// 							return false;
// 						}else{
// 							return true;
// 						}
// 					}else{
// 						if(value>=modelPrelimitValue){
// 							return false;
// 						}else{
// 							return true;
// 						}
// 					}
// 				}else{
// 					return false;
// 				}
// 			}
// 			return true;			
//         },
//         message:'电话限额和网络限额相加应该小于预约限额！'
// 	}
	
// })


	
		 /**
		   * 回车弹出科室弹框
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
			 *预约限额规则验证     网络限额不能大于预约限额，特诊限额和预约限额只能是一个为0一个大于0
			 * 
			 *
			 */
			 var premessage = "预约限额大于网络限额！";
				$.extend($.fn.validatebox.defaults.rules, {    
					modelPrelimit: {//预约限额验证
				        validator: function(value,param){
				        	var modelNetlimit = $('#modelNetlimit').val();//网络限额数量
				        	var modelSpeciallimit = $('#modelSpeciallimit').val();//特诊限额
				        	if(value>0){
				        		if(Number(modelNetlimit)!=''&&Number(modelNetlimit)!=0&&value<Number(modelNetlimit)){
				        			premessage = "预约限额大于网络限额！";
				        			return false;
				        		}
				        		if(Number(modelSpeciallimit)!=''&&Number(modelSpeciallimit)!=0){
				        			premessage = "预约限额和特诊限额，只能一个大于0一个等于0";
				        			return false;
				        		}
				        		return true;
				        	}else{
				        		if(Number(modelSpeciallimit)!=''&&Number(modelSpeciallimit)!=0&&Number(modelSpeciallimit)>0){
				        			return true;
				        		}
				        		return false;
				        	}
				        },    
				        message: premessage   
				    },
				    modelNetlimit:{//网络限额
				    	validator:function(value,param){
				    		var modelPrelimit = $('#modelPrelimit').val();//预约限额数量
				        	if(value>0){
				        		if(value>Number(modelPrelimit)){
				        			return false;
				        		}
				        		return true;
				        	}else{
				        		return true;
				        	}
				    	},
				    	message:"网络限额不能大于预约限额！"
				    },
				    modelSpeciallimit:{//特诊限额
				    	validator:function(value,param){
				    		var modelPrelimit = Number($('#modelPrelimit').val());//预约限额数量
				    		if(value>0){
				    			if(modelPrelimit!=''&&modelPrelimit!=0){
				    				return false;
				    			}
				    			return true;
				    		}else{
				    			return true;
				    		}
				    	},
				    	message:"特诊限额和预约限额，只能一个大于0一个等于0"
				    },
				    maxValue: {   //最大值规则 
				        validator: function(value, param){ 
				        	return value<=param[0];
				        },
				        message:"值不能大于999！"
					},
				});  

	</script>
	</body>
</html>