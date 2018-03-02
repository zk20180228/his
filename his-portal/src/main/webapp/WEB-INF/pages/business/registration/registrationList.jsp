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
	<title>手术登记</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/themes/system/css/loader.css">
	<%@ include file="/common/metas.jsp" %>
<%--hedong扩大复选框  20170306--%>
<style type="text/css">
	* {
		padding: 0;
		margin: 0;
	}
	body,html{
		width: 100%;
		height: 100%;
		overflow: hidden;
	}
	input[type=checkbox] {
	  -ms-transform: scale(1.35); /* IE */
	  -moz-transform: scale(1.35); /* FireFox */
	  -webkit-transform: scale(1.35); /* Safari and Chrome */
	  -o-transform: scale(1.35); /* Opera */
   }
     .window .panel-header .panel-tool .panel-tool-close{
  	  	background-color: red;
  	}
</style> 
</head>
<script type="text/javascript">
var patterms = new Object();
//验证血压格式
patterms.forMat =/^\d{1,3}-\d{1,3}$/;
//全局变量
var employeMap=null;   //员工信息map
var deptMap="";	   //科室信息map
var payMap="";//渲染表单中的挂号结算类别
var a = '';//患者信息查询使用的参数
var b = '';//手术信息查询使用的参数
var s="";
var c="";
var ids = "";
var empMap=""; //员工信息
var ageMap="";//年龄map
var freeCostMap="";//住院余额map
var freeCostMap2="";//门诊余额map
var xishouMap=new Map();
var xunhuiMap=new Map();
var zhushouMap=new Map();
var jinxiuMap=new Map();
var ssmcMap=new Map();
var sqzdMap=new Map();
var zshs=null;//加载护士
var user=null;//加载医生
var payCodeMap=new Map();//结算类别map
var sexMap=new Map();//性别
//获取添加的手术名称数量(诊断)
var indexName2=1;
//获取添加的手术诊断数量(诊断)
var indexName=1;
//护士数量
var indexfz=1;
$('#operationRegistration').window({
	onBeforeClose: function () { //当面板关闭之前触发的事件
		$('#typeNo').combobox('setValue','0');
		$('#questionName').textbox('setValue','');
	}
});
//校验血压格式
function verify(birthDay,pat)
{
	var thePat;
	thePat = patterms[pat];
	if(thePat.test(birthDay)){
	    return 1;
	}else{
	    return 0;
	}
}
$(function(){
	$("input",$("#forepress").next("span")).blur(function()  
			{  
				if($('#forepress').val()){
					var pre=$('#forepress').val().split("-");
					if(pre[0]==null||pre[0]==""||pre[1]==null||pre[1]==""){
						$.messager.alert('提示','请按格式输入血压范围，例如100-200');
						$('#forepress').textbox('setValue','');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}else{
						var value=verify($('#forepress').val(),"forMat");
						if(value==0){
							$('#forepress').textbox('setValue','');
							$.messager.alert('提示','请按格式输入血压范围，例如100-200');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return false;
						}
					}
				}
			});
	 $("input",$("#steppress").next("span")).blur(function()  
				{  
					if($('#steppress').val()){
						var pre=$('#steppress').val().split("-");
						if(pre[0]==null||pre[0]==""||pre[1]==null||pre[1]==""){
							$.messager.alert('提示','请按格式输入血压范围，例如100-200');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return false;
						}else{
							var value=verify($('#steppress').val(),"forMat");
							if(value==0){
								$('#steppress').textbox('setValue','');
								$.messager.alert('提示','请按格式输入血压范围，例如100-200');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								return false;
							}
						}
					}
			    });
	 $.ajax({
			url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=paykind",
			success: function(data) {
				var deptType = data;
				for(var i=0;i<deptType.length;i++){
					payCodeMap.put(deptType[i].encode,deptType[i].name);
				}
			}
		});
	 /**
		 * 渲染性别
		 * @Author: zhangjin
		 * @CreateDate: 2017年2月17日
		 * @param:
		 * @return:
		 * @Modifier:
		 * @ModifyDate:
		 * @ModifyRmk:
		 * @version: 1.0
		 */
		$.ajax({
			url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=sex ",
			type:'post',
			success: function(data) {
				var v = data;
				for(var i=0;i<v.length;i++){
					sexMap.put(v[i].encode,v[i].name);
				}
			}
		});
		//加载医生
		 $.ajax({
			 url : '<%=basePath %>operation/operationList/ssComboboxList.action',
			 type:"post",
			 success:function(data){
				 user=data
			 }
		});
		
		/**
			 * 科室
			 * @Author: zhangjin
			 * @CreateDate: 2017年2月17日
			 * @param:
			 * @return:
			 * @Modifier:
			 * @ModifyDate:
			 * @ModifyRmk:
			 * @version: 1.0
			 */
			$.ajax({
				url:"<%=basePath%>operation/arrangement/querydeptComboboxs.action",
				success:function(data){
					deptMap=data;
				}
			});
		//住院号绑定回车时间 medicalrecordId
		bindEnterEvent('medicalrecordId',queryPatient,'easyui');
		//手术名称绑定回车时间 medicalrecordId
		bindEnterEvent('questionName',queryPatient,'easyui');


	/**
	更改获取员工的路
	*/
	//获取员工
	$.ajax({
		url: '<%=basePath %>operation/operationList/getEmployee.action',
		type:'post',
		success: function(data) {
			empMap = data;
		}
	});

		
	//渲染表单中的挂号结算类别
	$.ajax({
		url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=paykind",
		type:'post',
		success: function(date){
				payMap=date;
		}
	});
	
	//手术分类
	$("#opType").combobox({
		url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=operatetype",
		valueField:'encode',    
		textField:'name',
		editable : false
	});
	//血型
	$("#bloodCode").combobox({
		url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=bloodType",
		valueField:'encode',    
		textField:'name',
		editable : true
	});

	//手术台类型
	$('#consoleType').combobox({
		url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=tabletype",
		valueField : 'encode',
		textField : 'name',
		multiple : false,
		editable : false
	});


	//手术室
	$('#opRoom').combobox({
		url:"<%=basePath%>operation/arrangement/queryroomId.action",
	    valueField:'id',    
	    textField:'roomName',
	    onChange:function(newValue, oldValue){
	    	if(newValue){//hedong 20170306 增加判断 如果没值则不进入判断
	    		var exec=$('#execDept').combobox("getValue");
		    	if(!exec){
		    		$.messager.alert("提示","请先选择执行科室！");
		    		setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
		    		delSelectedData('opRoom');//hedong 20170306 为防止产生无效数据， 在没选择有效的执行科室前选择手术室时清空下拉数据。
		    		return ;
		    	}
	    	}
	    },
	    filter: function(q, row){
			var keys = new Array();
			keys[keys.length] = 'roomId';
			keys[keys.length] = 'roomName';
			keys[keys.length] = 'inputCode';
			return filterLocalCombobox(q, row, keys);
		}
	});

 	
	//手术者科室
	$('#docDpcd').combobox({
		url : '<%=basePath %>operation/operationList/querysysDeptmentkeshi.action',
		valueField : 'deptCode',
		textField : 'deptName',
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
		filter: function(q, row){
			var keys = new Array();
			keys[keys.length] = 'deptCode';
			keys[keys.length] = 'deptName';
			keys[keys.length] = 'deptPinyin';
			keys[keys.length] = 'deptWb';
			keys[keys.length] = 'deptInputcode';
			return filterLocalCombobox(q, row, keys);
		}
	});
	//执行科室 execDept
	$('#execDept').combobox({
		url : '<%=basePath %>operation/operationList/querysysDeptmentShi.action',
		valueField : 'deptCode',
		textField : 'deptName',
		multiple : false,
		onChange:function(newValue, oldValue){/* 每次变更科室会触发三次onchange事件,所以需要加非undefined判断 (hzq)*/
			if(typeof(newValue) != "undefined"&&typeof(oldValue) != "undefined"){
		    	var exec=$('#execDept').combobox("getValue");
		    	if(newValue&&newValue!=oldValue){
		    		//$("#opRoom").combobox('clear');
					$('#opRoom').combobox("reload",
							"<%=basePath%>operation/arrangement/queryroomId.action?deptCode="+exec);
		    	}else{
		    		$('#opRoom').combobox("reload",
							"<%=basePath%>operation/arrangement/queryroomId.action");
		    	}
		    	if(newValue!=null&&newValue!=""){
		    		$('#opRoom').combobox({
		    			required:true,
			    		readonly:false
			    	})
		    	}else{
		    		$('#opRoom').combobox({
		    			required:true,
			    		readonly:true
			    	})
		    	}
			}	    	
	    },
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
	    filter:function(q,row){//hedong 20170310 增加过滤
			 var keys = new Array();
			 keys[keys.length] = 'deptCode';//科室code
			 keys[keys.length] = 'deptName';//部门名称
			 keys[keys.length] = 'deptPinyin';//部门拼音
			 keys[keys.length] = 'deptWb';//部门五笔
		     keys[keys.length] = 'deptInputCode';//自定义码
			return filterLocalCombobox(q, row, keys);
		}

	});
	//麻醉方式
	$('#aneWay').combobox({
		url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=aneWay",
		valueField : 'encode',
		textField : 'name',
		multiple : false,
		editable :false
	});

	//拟手术名称1  存国家编码
	$('#nssmc0_').combogrid({
		url : '<%=basePath%>operation/operationList/undrugComboboxfy.action',
	    idField:'code',    
	    textField:'name',
	    mode:"remote",
		pageList:[10,20,30,40,50],
	 	pageSize:"10",
	 	pagination:true,
		columns:[[
		         {field:'code',title:'编码',width:'18%'},
		         {field:'name',title:'名称',width:'20%'},
		         {field:'undrugPinyin',title:'拼音',width:'20%'}, 
		         {field:'undrugWb',title:'五笔',width:'18%'},
	     	 	 {field:'undrugInputcode',title:'自定义码',width:'20%'}
		     ]], 
	     onHidePanel:function(none){
	    	 var name = $(this).combogrid('getText');
	    	 if(nssmcName(name)){
	    		 $(this).combogrid("clear");
	    		 $.messager.alert("提示","手术名称不能重复!");
	    		 setTimeout(function(){
						$(".messager-body").window('close');
					},2000);
	    	 }
	    	var val = $(this).combogrid('getValue');
    	 	if(name==val){
    	 		if(!$('#operation').is(':checked')){
    	 			$(this).combogrid("clear");
    	 			$.messager.alert("提示","该手术为非自定义手术，请选择下拉列表中的数据！","info");
    	 			setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
    	 		}
    	 	} 
		 }
	
	});
	//手术诊断1
	$('#shoushuzd0_').combogrid({
		url : '<%=basePath%>operation/operationList/icdCombobox2fy.action',
		idField : 'code',
		textField : 'name',
		multiple : false,
		editable : true,
		readonly:true,
		mode:"remote",
		pageList:[10,20,30,40,50],
	 	pageSize:"10",
	 	pagination:true,
		columns:[[    
		         {field:'code',title:'编码',width:'18%'},    
		         {field:'name',title:'名称',width:'20%'},    
		         {field:'pinyin',title:'拼音',width:'20%'},    
		         {field:'wb',title:'五笔',width:'18%'},
	     	 	 {field:'inputcode',title:'自定义码',width:'20%'}
		     ]],  
	});

	//切口类型
	$("#inciType").combobox({
		url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=inciType",
		valueField:'encode',    
		textField:'name',
		editable : false
	});

	//手术规模
	$("#degree").combobox({
		url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=scaleofoperation",
		valueField:'encode',    
		textField:'name',
		editable : false
	});


	//麻醉类别
	$('#anesType').combobox({
		url:"<%=basePath%>operation/operationList/queryCodeanesType.action",
		valueField:'encode',    
		textField:'name',
		editable : true
	});
	//加载树
	setTimeout(function(){
		tree();
	},100);
	//巡回
	$("#xunhui0").combogrid({
		url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
		idField : 'jobNo',
		textField : 'name',
		mode:"remote",
		panelAlign:'right',
		panelWidth:320,
		editable : true,
		pageList:[10,20,30,40,50],
		pageSize:"10",
		pagination:true,
	 	columns:[[   
			{field:'jobNo',title:'工作号',width:'130'},
 	         {field:'name',title:'名称',width:'160'} 
 	        
	    ]], 
		onSelect:function(rowIndex, rowData){
	    	if(xunhuiName(rowData.jobNo)){
	    	
	    			$("#xunhui0"+xhId).combogrid("clear");
	    			$("#xunhui0").combogrid("clear");
			    	$.messager.alert("提示","护士信息不能重复!","info");
			    	setTimeout(function(){
						$(".messager-body").window('close');
					},2000);
	    		
		   	 }
		  }
	});
	$("#xishou0").combogrid({
		url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
		idField : 'jobNo',
		textField : 'name',
		mode:"remote",
		panelAlign:'right',
		panelWidth:320,
		editable : true,
		pageList:[10,20,30,40,50],
		pageSize:"10",
		pagination:true,
		columns:[[   
			{field:'jobNo',title:'工作号',width:'130'},
		         {field:'name',title:'名称',width:'160'} 
		        
	     ]],  
	    onSelect:function(rowIndex, rowData){
			if(xishouName(rowData.jobNo)){
				 $("#xishou0").combogrid("clear");
					$("#xishou0_").combogrid("clear");
		  		$.messager.alert("提示","护士信息不能重复!","info");
		  		setTimeout(function(){
					$(".messager-body").window('close');
				},2000);
				
		 	}
	    }
	});
	//进修
	$("#jinxiu0").combogrid({
		url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
		idField : 'jobNo',
		textField : 'name',
		mode:"remote",
		panelAlign:'right',
		panelWidth:320,
		editable : true,
		pageList:[10,20,30,40,50],
		pageSize:"10",
		pagination:true,
	 	columns:[[   
			{field:'jobNo',title:'工作号',width:'130'},
 	         {field:'name',title:'名称',width:'160'} 
 	        
	     ]],  
	    onSelect:function(rowIndex, rowData){
		    if(jinxiuName(rowData.jobNo)){
    			$("#jinxiu0_").combogrid("clear");
    			$("#jinxiu0").combogrid("clear");
		    	$.messager.alert("提示","护士信息不能重复!","info");
		    	setTimeout(function(){
					$(".messager-body").window('close');
				},2000);
			}
		}
	});
	setTimeout(function(){
		
		//手术医生编码
		$('#ssysbm').combobox({
			valueField : 'jobNo',
			textField : 'name',
			multiple : false,
			editable : true,
			data:user,
			onHidePanel:function(none){
				    var data = $(this).combobox('getData');
				    var val = $(this).combobox('getValue');
				    var result = true;
				    for (var i = 0; i < data.length; i++) {
				        if (val == data[i].jobNo) {
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
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
		//指导医生编码
		$('#guiDocd').combobox({
			valueField : 'jobNo',
			textField : 'name',
			multiple : false,
			editable : true,
			data:user,
			onHidePanel:function(none){
				    var data = $(this).combobox('getData');
				    var val = $(this).combobox('getValue');
				    var result = true;
				    for (var i = 0; i < data.length; i++) {
				        if (val == data[i].jobNo) {
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
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		
		});
		//审批医生编码
		$('#approveDocd').combobox({
			valueField : 'jobNo',
			textField : 'name',
			multiple : false,
			editable : true,
			data:user,
			onHidePanel:function(none){
			    var data = $(this).combobox('getData');
			    var val = $(this).combobox('getValue');
			    var result = true;
			    for (var i = 0; i < data.length; i++) {
			        if (val == data[i].jobNo) {
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
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
		//助手
		$("#zhushou0").combobox({
			valueField:'jobNo',    
		    textField:'name',
		    data:user,
		    onSelect:function(rowIndex, rowData){
		    	var m=new Map();
	     		var id=$(this).prop("id");
	    		var zs = $('[id^=zhushou]');
	     		var ssysbm=$('#ssysbm').combobox("getValue");
	     		var gui=$('#guiDocd').combobox("getValue");
	     		var zsys=$(this).combobox("getValue");
	     		var b = 0;
	     		zs.each(function(index,obj){
	     			if($(obj).combogrid('getValue') == zsys){
			 			b++;
			 		}
	     			var zsys2=$(this).combobox("getValue");
	     			if(ssysbm!=null&&ssysbm!=""){
	 	 				if(zsys2==ssysbm){
	 	 					$("#"+id).combobox("clear");
	 	 					$.messager.alert("提示","助手与手术医生相同！");
	 	 					setTimeout(function(){
								$(".messager-body").window('close');
							},3000);
	 	 	 				return
	 	 	 			}
	 	 			}
	    			if(gui!=null&&gui!=""){
		 				if(gui==zsys2){
		 					$("#"+id).combobox("clear");
		 					$.messager.alert("提示","助手与指导医生相同！");
		 					setTimeout(function(){
								$(".messager-body").window('close');
							},3000);
		 	 			}
		 			}
	     		});
	     		if(b>1){
		    		$('#'+id).combobox('clear');
					$.messager.alert("提示","助手重复!","info");
					setTimeout(function(){
						$(".messager-body").window('close');
					},2000);
		    	}
		    },
		    onHidePanel:function(none){
			    var data = $(this).combobox('getData');
			    var val = $(this).combobox('getValue');
			    var result = true;
			    for (var i = 0; i < data.length; i++) {
			        if (val == data[i].jobNo) {
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
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
	},800);	
	//术者科室过滤 与上边定义的过滤想从导无法过滤 hedong 20170310 移除
	//手术诊断1
	$('#typeNo').combobox({
		valueField: 'id',
		textField: 'value',
		data: [{
			id: '0',value: '手术医生'
		},{
			id: '1',value: '手术名称'
		}]
	});


});
//查询按钮
function queryPatient(){
	var typeNo=$('#typeNo').combobox('getValue');
	var questionName=$('#questionName').textbox('getValue').trim();
	var inputmessage = "";
	var patientNo=$('#medicalrecordId').textbox('getText').trim();
	var beganTime = $('#beganTime').val().trim();
	var endTime = $('#endTime').val().trim();
	if(!patientNo){
		$.messager.alert("提示", "请输入正确的就诊卡号");
		return ;
	}
	if(beganTime!=""&&endTime!=""&&endTime<beganTime){
		$.messager.alert("提示","结束时间不能小于开始时间","info");
		setTimeout(function(){
				$(".messager-body").window('close');
			},3000);
		return ;
	}
	$('#dg').datagrid({
		pageSize:10,
		pageList:[10,20,30,50,80,100],
		pagination:true,
		url:'<%=basePath%>operation/registration/queryPatient.action?', 
		queryParams: {
		patientNo: patientNo,
		typeNo: typeNo,
		queryPatient:questionName,
		beganTime:beganTime,
		endTime:endTime
		},
   		onDblClickRow:function(index,row){
	    	if(row.status=='未登记'){//未登记回显
	    		applyData2Clear();
	    		$('#operationRegistration').window('close');
	    		$("#dengji").val("noregister");
	    		$("#operationno").val(row.opID);
	    		setTimeout(function(){
	    			OperationapplyData(row.opID,row.pasource,row.clinicCode);
	    		},500);
	    		var id=row.opID;
	    		var deptCode=row.deptCode;
	    		var node = $('#tDt').tree('find',"noregister");
	    		$("#tDt").tree("expand",node.target);
	    		setTimeout(function(){
		    		if(deptCode!=null){
		    			var node2 = $('#tDt').tree('find',deptCode);
 		    		$("#tDt").tree("expand",node2.target);
		    		}
	    		},1500);
	    		setTimeout(function(){
		    		var node3=$("#tDt").tree('find',id);
 		    		$("#tDt").tree("scrollTo",node3.target);
		    		$("#tDt").tree('select',node3.target);
	    		},3000);
	    	}else if(row.status=='已登记'){//已作废或已登记回显
	    		applyData2Clear();
	    	
	    		$('#operationRegistration').window('close');
	    		$("#dengji").val("register");
	    		 setTimeout(function(){
	    			ReCordData(row.opID,row.pasource,row.clinicCode);
	    		},500); 
	    		$("#operationno").val(row.operationId);
	    		var id=row.opID;
	    		var deptCode=row.deptCode;
	    		var node = $('#tDt').tree('find',"register");
	    		$("#tDt").tree("expand",node.target);
	    		setTimeout(function(){
		    		if(deptCode!=null){
		    			var node2 = $('#tDt').tree('find',deptCode);
			    		$("#tDt").tree("expand",node2.target);
		    		}
	    		},1000);
	    		setTimeout(function(){
		    		var node3=$("#tDt").tree('find',id);
		    		if(node3!=null){
		    			$("#tDt").tree("scrollTo",node3.target);
			    		$("#tDt").tree('select',node3.target);
		    		}else{
		    			node3=$("#tDt").tree('find',id);
		    		}
 		    		
	    		},1500); 
	    		
	    	}else if(row.status=='已作废'){
	    		applyData2Clear();
	    		$('#operationRegistration').window('close');
	    		$("#dengji").val("cancel");
	    		setTimeout(function(){
	    			ReCordData(row.opID,row.pasource,row.clinicCode);
	    		},500);
	    		$("#operationno").val(row.opID);
	    		var id=row.opID;
	    		var deptCode=row.deptCode;
	    		var node = $('#tDt').tree('find',"cancel");
	    		$("#tDt").tree("expand",node.target);
	    		setTimeout(function(){
		    		if(deptCode!=null){
		    			var node2 = $('#tDt').tree('find',deptCode);
 		    			$("#tDt").tree("expand",node2.target);
		    		}
	    		},1500);
	    		setTimeout(function(){
		    		var node3=$("#tDt").tree('find',id);
 		    		$("#tDt").tree("scrollTo",node3.target);
		    		$("#tDt").tree('select',node3.target);
	    		},3000);
	    	}
	    },onLoadSuccess:function(data){
	    	 $('#operationRegistration').window('open');
	    	//分页工具栏作用提示
			var pager = $(this).datagrid('getPager');
			var aArr = $(pager).find('a');
			var iArr = $(pager).find('input');
			$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
			for(var i=0;i<aArr.length;i++){
				$(aArr[i]).tooltip({
					content:toolArr[i],
					hideDelay:1
				});
				$(aArr[i]).tooltip('hide');
			}
	    },onLoadError:function(none){
	    	$.messager.alert("提示","未查到该患者信息");
	    }
   	});
}

//删除指定项
function removeTr(trId,aId,index){
	var nu = parseInt(index)+1;
	$("#"+trId+nu).remove();
	$('#'+aId+index).show();
	$('#j'+aId+index).show();
}
//添加指定项 
function add(name,trId,index,tdId,tdname,aId){
	var nu = parseInt(index)+1;
	var nu2 = nu+1;
	$("#"+trId+index).after("<tr id=\""+trId+nu+"\">"+
		    "<td> "+name+nu2+"：</td>"+
			"<td colspan=\"7\"><input id=\""+tdId+nu+"_\" name=\""+tdname+nu+"\" style=\"width: 90%\" data-options=\"required:true\">"+
			"<a id=\"j"+aId+nu+"\" href=\"javascript:void(0)\" onclick=\"removeTr('"+trId+"','"+aId+"','"+index+"')\"></a>"+
			"<a id=\""+aId+nu+"\" href=\"javascript:void(0)\" onclick=\"add('"+name+"','"+trId+"','"+nu+"','"+tdId+"','"+tdname+"','"+aId+"')\"></a></td>"+
		"</tr>"); 
	if(name=='手术名称'){
		indexName=nu2;
		$('#'+tdId+nu+"_").combogrid({
			url : '<%=basePath %>operation/operationList/undrugComboboxfy.action',
			idField : 'code',
			textField : 'name',
			editable : true,
			mode:"remote",
			pageList:[10,20,30,40,50],
		 	pageSize:"10",
		 	pagination:true,
		 	multiple : false,
			columns:[[    
			         {field:'code',title:'编码',width:'20%'},    
			         {field:'name',title:'名称',width:'20%'},    
			         {field:'undrugPinyin',title:'拼音码',width:'20%'},
			         {field:'undrugWb',title:'五笔码',width:'20%'},    
			         {field:'undrugInputcode',title:'自定义码',width:'20%'},
			     ]],  
			onHidePanel:function(none){
		    	 var name = $(this).combogrid('getText');
		    	 if(nssmcName(name)){
		    		 $(this).combogrid("clear");
		    		 $.messager.alert("提示","手术名称不能重复!");
		    		 setTimeout(function(){
							$(".messager-body").window('close');
						},2000);
		    	 }
		    
			 }
		}); 
	}else{
		indexName2=nu2;
		$('#'+tdId+nu+"_").combogrid({
			url : '<%=basePath%>operation/operationList/icdCombobox2fy.action',
			idField : 'code',
			textField : 'name',
			multiple : false,
			editable : true,
			mode:"remote",
			pageList:[10,20,30,40,50],
		 	pageSize:"10",
		 	pagination:true,
			columns:[[    
			         {field:'code',title:'编码',width:'18%'},    
			         {field:'name',title:'名称',width:'20%'},    
			         {field:'pinyin',title:'拼音',width:'20%'},    
			         {field:'wb',title:'五笔',width:'18%'},
		     	 {field:'inputcode',title:'自定义码',width:'20%'}
			     ]],  
		     onHidePanel:function(none){
		    	 var name = $(this).combogrid('getText');
		    	 if(nssmcName(name)){
		    		 $(this).combogrid("clear");
		    		 $.messager.alert("提示","手术名称不能重复!");
		    		 setTimeout(function(){
							$(".messager-body").window('close');
						},2000);
		    	 }
		    	var val = $(this).combogrid('getValue');
	    	 	if(name==val){
	    	 		if(!$('#operation').is(':checked')){
	    	 			$(this).combogrid("clear");
	    	 			$.messager.alert("提示","该手术为非自定义手术，请选择下拉列表中的数据！","info");
	    	 			setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
	    	 		}
	    	 	} 
			 }
		}); 
	}
	 
	$('#'+aId+nu).linkbutton({    
	    iconCls: 'icon-add'   
	});  
	$('#j'+aId+nu).linkbutton({    
	    iconCls: 'icon-remove'   
	});  
	$('#'+aId+index).hide();
	$('#j'+aId+index).hide();
	
	
}
//添加护士和助手 
function addfz(trId,index,aId){
	var nu = parseInt(index)+1;
	var max;
	var nu2 = nu+1;
	$("#"+trId+index).after("<tr id=\""+trId+nu+"\">"+
		    "<td  style=\"white-space: nowrap;\">助手"+nu2+"：</td>"+
			"<td style=\"width: 15%\"><input id=\"zhushou"+nu+"_\" name=\"thelper"+nu+"\" >"+
			"</td>"+
			"<td  style=\"white-space: nowrap;\">洗手护士"+nu2+"：</td>"+
			"<td style=\"width: 15%\"><input id=\"xishou"+nu+"_\" name=\"wash"+nu+"\">"+
			"</td>"+
		    "<td  style=\"white-space: nowrap;\">巡回护士"+nu2+"：</td>"+
			"<td style=\"width: 15%\"><input id=\"xunhui"+nu+"_\" name=\"tour"+nu+"\" >"+
			"</td>"+
		    "<td  style=\"white-space: nowrap;\">进修护士"+nu2+"：</td>"+
			"<td style=\"width: 15%\"><input id=\"jinxiu"+nu+"_\" name=\"engage"+nu+"\" >"+
			"<a id=\""+aId+nu+"\" href=\"javascript:void(0)\" onclick=\"addfz('"+trId+"','"+nu+"','"+aId+"')\"></a>"+
			"<a id=\"j"+aId+nu+"\" href=\"javascript:void(0)\" onclick=\"removeTr('"+trId+"','"+aId+"','"+index+"')\"></a></td>"+
		"</tr>"); 
		$('#zhushou'+nu+"_").combobox({    
		    valueField:'jobNo',    
		    textField:'name',
		    validType:'zs',
		    data:user,
		    editable : true,
	    	onSelect : function() {
	    		var m=new Map();
	     		var id=$(this).prop("id");
	    		var zs = $('[id^=zhushou]');
	     		var ssysbm=$('#ssysbm').combobox("getValue");
	     		var gui=$('#guiDocd').combobox("getValue");
	     		var zsys=$(this).combobox("getValue");
	     		var b = 0;
	     		zs.each(function(index,obj){
	     			var zsys2=$(this).combobox("getValue");
	     			if($(obj).combogrid('getValue') == zsys){
			 			b++;
			 		}
	    			if(ssysbm!=null&&ssysbm!=""){
	 	 				if(zsys2==ssysbm){
	 	 					$("#"+id).combobox("clear");
	 	 					$.messager.alert("提示","助手与手术医生相同！");
	 	 					setTimeout(function(){
								$(".messager-body").window('close');
							},3000);
	 	 	 				return
	 	 	 			}
	 	 			}
	    			if(gui!=null&&gui!=""){
		 				if(gui==zsys2){
		 					$("#"+id).combobox("clear");
		 					$.messager.alert("提示","助手与指导医生相同！");
		 					setTimeout(function(){
								$(".messager-body").window('close');
							},3000);
		 	 			}
		 			}
	     		});
	     		if(b>1){
		    		$('#'+id).combobox('clear');
					$.messager.alert("提示","助手重复!","info");
					setTimeout(function(){
						$(".messager-body").window('close');
					},2000);
		    	}
	     		
		    },onHidePanel:function(none){
			    var data = $(this).combobox('getData');
			    var val = $(this).combobox('getValue');
			    var result = true;
			    for (var i = 0; i < data.length; i++) {
			        if (val == data[i].jobNo) {
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
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});  
		$('#xishou'+nu+"_").combogrid({    
			url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
	 		idField : 'jobNo',
	 		textField : 'name',
	 		mode:"remote",
	 		panelAlign:'right',
	 		panelWidth:320,
	 		editable : true,
	 		pageList:[10,20,30,40,50],
			pageSize:"10",
			pagination:true,
		 	columns:[[   
					{field:'jobNo',title:'工作号',width:'130'},
		 	         {field:'name',title:'名称',width:'160'} 
		 	        
	 	     ]],  
	 	    onSelect:function(rowIndex, rowData){
		    	if(xishouName(rowData.jobNo)){
		    		$('#xishou'+nu+"_").combogrid("clear");
			    	$.messager.alert("提示","护士信息不能重复!","info");
			    	setTimeout(function(){
						$(".messager-body").window('close');
					},2000);
			    }
	 	    }
		});  

		$('#xunhui'+nu+"_").combogrid({    
			url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
	 		idField : 'jobNo',
	 		textField : 'name',
	 		mode:"remote",
	 		panelAlign:'right',
	 		panelWidth:320,
	 		editable : true,
	 		pageList:[10,20,30,40,50],
			pageSize:"10",
			pagination:true,
		 	columns:[[   
					{field:'jobNo',title:'工作号',width:'130'},
		 	         {field:'name',title:'名称',width:'160'} 
		 	        
	 	     ]],  
	 	    onSelect:function(rowIndex, rowData){
		    	if(xunhuiName(rowData.jobNo)){
		    		$('#xunhui'+nu+"_").combogrid("clear");
			    	$.messager.alert("提示","护士信息不能重复!","info");
			    	setTimeout(function(){
						$(".messager-body").window('close');
					},2000);
			    }
	 	    }
		});
		
		$('#jinxiu'+nu+"_").combogrid({    
			url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
	 		idField : 'jobNo',
	 		textField : 'name',
	 		mode:"remote",
	 		panelAlign:'right',
	 		panelWidth:320,
	 		editable : true,
	 		pageList:[10,20,30,40,50],
			pageSize:"10",
			pagination:true,
		 	columns:[[   
					{field:'jobNo',title:'工作号',width:'130'},
		 	         {field:'name',title:'名称',width:'160'} 
		 	        
	 	     ]],  
	 	    onSelect:function(rowIndex, rowData){
		    	if(jinxiuName(rowData.jobNo)){
		    		$('#jinxiu'+nu+"_").combogrid("clear");
			    	$.messager.alert("提示","护士信息不能重复!","info");
			    	setTimeout(function(){
						$(".messager-body").window('close');
					},2000);
			    }
	 	    }
		});
	
		$('#'+aId+nu).linkbutton({    
		    iconCls: 'icon-add'   
		});  
		$('#j'+aId+nu).linkbutton({    
		    iconCls: 'icon-remove'   
		});  
		$('#'+aId+index).hide();
		$('#j'+aId+index).hide();
		indexfz=nu2;

}
	//手术名称是否自定义  
function ssmcdy(){
	if($("#operation").is(':checked')){
		var idArr = $('[id^=nssmc]');
		idArr.each(function(index,obj){
			$(obj).combogrid("clear")
		    $(obj).combogrid({
		    	data:[],
			    mode:"local",
			    onBeforeLoad:function(){
		        	return false;
		        } 
			});  
			$(obj).combogrid('grid').datagrid('loadData',{total:0,rows:[]}); 
			 
		})
	}else{
		var idArr = $('[id^=nssmc]');
		idArr.each(function(){
			 $(this).combogrid({
				url : '<%=basePath%>operation/operationList/undrugComboboxfy.action',
			    idField:'code',    
			    textField:'name',
			    mode:"remote",
				pageList:[10,20,30,40,50],
			 	pageSize:"10",
			 	pagination:true,
				columns:[[    
				         {field:'code',title:'编码',width:'18%'},    
				         {field:'name',title:'名称',width:'20%'},    
				         {field:'undrugPinyin',title:'拼音',width:'20%'},    
				         {field:'undrugWb',title:'五笔',width:'18%'},
			     	 {field:'undrugInputcode',title:'自定义码',width:'20%'}
				     ]],  
			 	 onHidePanel:function(none){
				  	   var name = $(this).combogrid('getText');
				  	   if(nssmcName(name)){
				    		$(this).combogrid("clear");
					    	$.messager.alert("提示","手术名称不能重复!","info");
					    	setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
					    }
				  	    var val = $(this).combogrid('getValue');
				     	if(name==val){
				     		if(!$('#operation').is(':checked')){
				     			$(this).combogrid("clear");
				     			$.messager.alert("提示","该手术为非自定义手术，请选择下拉列表中的数据！","info");
				     			setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
				     		}
				     	}
				  	},
				  	onBeforeLoad:function(){
			        	return true;
			        },
				    onLoadSuccess: function () {
				    	var id=$(this).prop("id");
				       if ($("#"+id).combogrid('getValue')&&operatNameMap.get($("#"+id).combogrid('getValue'))) {
			            	$("#"+id).combogrid('setText',operatNameMap.get($("#"+id).combogrid('getValue')));
			            } 
			        } 
	 		});
		});
		
	}
}

//渲染表单中的挂号结算类别	
function functionPay(value,row,index){
	if(value!=null&&value!=''){
		return payMap[value];
	}
}
//是否有菌回显
function isgerm(){
	if($("#germ").is(':checked')){
		$("#germ2").val(1);
	}else{
		$("#germ2").val(0);
	}
}
//是否感染回显
function isquestion(){
	if($("#question").is(':checked')){
		$("#question2").val(1);
	}else{
		$("#question2").val(0);
	}
}
//术前是否清醒
function isforeynsober(){
	if($("#foreynsober").is(':checked')){
		$("#foreynsober2").val(1);
	}else{
		$("#foreynsober2").val(0);
	}
}
//术后是否清醒     
function isstepynsober(){
	if($("#stepynsober").is(':checked')){
		$("#stepynsober2").val(1);
	}else{
		$("#stepynsober2").val(0);
	}
}
//是否危重
function isdanger(){
	if($("#danger").is(':checked')){
		$("#danger2").val(1);
	}else{
		$("#danger2").val(0);
	}
}
//是否隔离
function isseperate(){
	if($("#seperate").is(':checked')){
		$("#seperate2").val(1);
	}else{
		$("#seperate2").val(0);
	}
}
//是否付费
function isynfee(){
	if($("#ynfee").is(':checked')){
		$("#ynfee2").val(1);
	}else{
		$("#ynfee2").val(0);
	}
}
/**
 * @Description:加载手术登记树
 * @Author: tangfeishuai
 * @CreateDate: 2016年4月20日
 * @param:
 * @return:
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */	
function tree(){
	$('#tDt').tree({ 
		url:"<%=basePath%>operation/registration/treeListNew.action",
		queryParams:{inde:2},
	    method:'post',
	    animate:true,  //点在展开或折叠的时候是否显示动画效果
	    lines:true,    //是否显示树控件上的虚线
	    state:'closed',//节点不展开
		    onBeforeLoad:function(node,param){
		    	if(node!=null){
		    		var node1=$('#tDt').tree('getParent',node.target);
			    	param.pid=node1.id
		    	}
            },
            onBeforeCollapse:function(node){
           	 if(node.id==1){
           		 return false;
           	 }
            },
	    formatter:function(node){//统计节点总数
			var s = node.text;
			  if (node.children){
				  if(node.children.length>0){
					  s += '<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				  }
			}  
			return s;
		},onDblClick: function(node){//点击节点
			var parentNode=$('#tDt').tree('getParent',node.target);
			if(parentNode.id!=1){
				if(parentNode.attributes.pid=='noregister'){//未登记
					$('#operationno').val(node.id);
					$('#clinicCode').val(node.attributes.clinicCode);
					$('#patientNo').val(node.attributes.patientNo);
					applyDataClear();
					//param1 （id）param2(来源) param3 (门诊号/住院流水号)
					setTimeout(function(){
						OperationapplyData(node.id,node.attributes.pasource,node.attributes.clinicCode);
					},1000);	
				}else if(parentNode.attributes.pid=='cancel'||parentNode.attributes.pid=='register'){ //已登记或作废 clinicCode patientNo
					$('#id').val(node.id);
					$('#operationno').val(node.attributes.operationId);
					$('#clinicCode').val(node.attributes.clinicCode);
					$('#patientNo').val(node.attributes.patientNo);
					applyDataClear();
					//param1 （id）param2(来源) param3 (门诊号/住院流水号)
					setTimeout(function(){
						ReCordData(node.id,node.attributes.pasource,node.attributes.clinicCode);
					},1000);
						
				}
			}
		},onClick: function(node){
				$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
		}
	}); 
}

/**
 * @Description:未登记信息查询 node.attributes.pid=='noregister' 未登记
 * @Author: tangfeishuai
 * @CreateDate: 2016年4月20日
 * @param:
 * @return:
 * @Modifier:zhangjin
 * @ModifyDate:2016年10月11日
 * @ModifyRmk:
 * @version: 1.0
 */
function OperationapplyData(id,pasource,clinicCode){
	 if(pasource!=""&&pasource!=null){
		 huanzhexinxi(clinicCode,pasource);
		shoushuxinxi(id,"OperationapplyData");
	 }else{
		 $.messager.alert("提示","该患者数据有问题");
		 setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
	 }
}
//手术信息
function shoushuxinxi(id,pas){
	if(pas=="OperationapplyData"){
		$.ajax({
			url:"<%=basePath%>operation/arrangement/queryOperationInfoList2.action",
			type:'post',
			data:{'opapId':id,fore:"3"},
			success:function(data){
				var obj = data;
				applyDataFill(obj);
			}
		});
	}else{//已登记
		$.ajax({
			url:"<%=basePath%>operation/registration/queryOperationRecordInfoList.action",
			type:'post',
			data:{'opapId':id,fore:"3"},
			success:function(data){
				var obj = data;
				ReCordFill(obj);
			}
		});
	}
}
//患者信息
function huanzhexinxi(clinicCode,pasource){
	//根据住院流水号查询患者住院信息
	$.ajax({
		url:'<%=basePath%>operation/registration/gethuanzhexinxiNew.action',
		type:"post",
		data:{clinicCode:clinicCode,pasource:pasource},
		async: false,
		success:function(data){
			if(data!="error"){
				if(data[0].patientName){
					$('#name').text(data[0].patientName);
				}
				var age="";
				var unit="";
				if(data[0].patientAge){
					age=data[0].patientAge;
				}
				if(data[0].patientAgeunit){
					unit=data[0].patientAgeunit;
				}
				$('#age').text(age+unit);
				//出生日期
				$('#birthDay').text(data[0].patientBirthday);
				//科室
				if(data[0].patientPaykind){
					$('#paykindCode').text(payCodeMap.get(data[0].patientPaykind));
				}
				
				//余额 1门诊 2住院
				if(pasource==1){
					$('#sex').text(sexMap.get(data[0].patientSex));
					$('#inDept').text(deptMap[data[0].dept]);
					if(data[0].freeCost){
						$('#prepayCost').text(data[0].freeCost);
					}else{
						$('#prepayCost').text("0");
					}
				}else if(pasource==2){
					if(data[0].patientSex){
						$('#sex').text(data[0].sexName);
					}
					$('#inDept').text(data[0].deptName);
					if(data[0].freeCost){
						$('#prepayCost').text(data[0].freeCost);
					}else{
						$('#prepayCost').text("0");
					}
					if(data[0].bedName){
						//病床
						$('#bedNo').text(data[0].bedName);
					}
					if(data[0].bedwardName){
						//房号
						$('#bedhouse').textbox('setValue',data[0].bedwardName+"病房");
					}
				}
				
				//住院号
				$('#patientNo').text(data[0].medicalrecordId);
			}
		}
	});
}
/**
 * @Description:已登记信息查询
 * @Author: tangfeishuai
 * @CreateDate: 2016年4月20日
 * @param:
 * @return:
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */	
function ReCordData(id,pasource,clinicCode){
	if(pasource!=""&&pasource!=null){
		 huanzhexinxi(clinicCode,pasource);
		shoushuxinxi(id,"ReCordData");
	 }else{
		 $.messager.alert("提示","该患者数据有问题");
		 setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
	 }
}

/**
 * @Description:未登记信息填充
 * @Author: tangfeishuai
 * @CreateDate: 2016年4月20日
 * @param:
 * @return:
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */
function applyDataFill(data){
	//显示数据
	//申请医生
	$('#applyDoctor').text(empMap[data.applyDoctor]); 
	//门诊号
	$('#clinicCode').text(data.clinicCode);
	//预约日期
	$('#preDate').text(data.preDate);
	//执行科室
	$('#execDept').combobox('setValue',data.execDept);
	//余额，即预交金
	//$('#prepayCost').text(data.prepayCost);
	//手术分类			
	$('#opType').combobox('setValue',data.opType);
	//切口类型
	$('#inciType').combobox('setValue',data.inciType);
	//麻醉类型
	$("#anesType").combobox('setValue',data.anesType);
	//麻醉方式
	$("#aneWay").combobox('setValue',data.aneWay);
	//备注
	$("#remark").val(data.applyRemark);
	//手术台类型
	$("#consoleType").combobox('setValue',data.consoleType);
	//手术医生
	$("#ssysbm").combobox('setValue',data.opDoctor);
	//指导医生
	$("#guiDocd").combobox('setValue',data.guiDoctor);
	//审批医生
	$("#approveDocd").combobox('setValue',data.apprDoctor);
	//手术医生科室
	$("#docDpcd").combobox('setValue',data.opDoctordept);
	//手术规模
	$("#degree").combobox('setValue',data.degree);
	//手术室
	$('#opRoom').combobox('setValue',data.roomId);
	//患者血型
	$('#bloodCode').combobox('setValue',data.bloodCode);
	//是否有菌
	if(data.isgerm==1){
		$('#germ').prop("checked", true);
		$('#germ2').val(1); 
	}else{
		$('#germ').prop("checked", false); 
		$('#germ2').val(0); 
	}
	//是否收费
	if(data.ynfee==1){
		$('#ynfee').prop("checked", true); 
		document.getElementById("ynfee").disabled=true;
		$('#ynfee2').val(1); 
	}else{
		$('#ynfee').prop("checked", false); 
		$('#ynfee2').val(0); 
	} 
	var opName = data.opNameList.length;
	var xunHui = data.xunHuiList.length;
	var xiShou = data.xiShouList.length;
	var jinXiu = data.jinXiuList.length;
	var zs = data.zsDocList.length;
	var zd = data.opDiagList.length;
	var max1 = ((opName>=xunHui)&&opName)||xunHui;
	var max2 = ((max1>=xiShou)&&max1)||xiShou;
	var maxLength = ((max2>=zs)&&max2)||zs;
	var num;
	if(xiShou>xunHui){
		num=xiShou;
	}else{
		num=xunHui;
	}
	if(zs>num){
		num=zs;
	}
	if(jinXiu>num){
		num=jinXiu;
	}
	//助手，护士  
	if(maxLength>0){
		for(var i=0;i<num;i++){
    		var xunHuiEmplCode = (data.xunHuiList[i]&&(data.xunHuiList[i].emplCode||""))||"";
			var zsEmplCode = (data.zsDocList[i]&&(data.zsDocList[i].emplCode||""))||"";
			var xiShouEmplCode = (data.xiShouList[i]&&(data.xiShouList[i].emplCode||""))||"";
			var jinXiuEmplCode = (data.jinXiuList[i]&&(data.jinXiuList[i].emplCode||""))||"";
			var xunHuiId = (data.xunHuiList[i]&&(data.xunHuiList[i].id||""))||"";
			var zsId = (data.zsDocList[i]&&(data.zsDocList[i].id||""))||"";
			var xiShouId = (data.xiShouList[i]&&(data.xiShouList[i].id||""))||"";
			var jinXiuId = (data.jinXiuList[i]&&(data.jinXiuList[i].id||""))||"";
    		if(i!=0){
    			$("#trzs"+(i-1)).after("<tr id=\"trzs"+i+"\">"+
    				    "<td >助手"+(i+1)+"：</td>"+
    					"<td ><input id=\"zhushou"+i+"_"+zsId+"\" name=\"thelper"+i+"\" >"+
    					"</td>"+
    					"<td >洗手护士"+(i+1)+"：</td>"+
    					"<td ><input id=\"xishou"+i+"_"+xiShouId+"\" name=\"wash"+i+"\">"+
    					"</td>"+
    				    "<td  >巡回护士"+(i+1)+"：</td>"+
    					"<td ><input id=\"xunhui"+i+"_"+xunHuiId+"\" name=\"tour"+i+"\">"+
    					"</td>"+
    				    "<td  >进修人员"+(i+1)+"：</td>"+
    					"<td ><input id=\"jinxiu"+i+"_"+jinXiuId+"\" name=\"engage"+i+"\">"+
    					"<a id=\"jafz"+i+"\" href=\"javascript:void(0)\" onclick=\"removeTr('trzs','afz','"+(i-1)+"')\"></a>"+
    					"<a id=\"afz"+i+"\" href=\"javascript:void(0)\" onclick=\"addfz('trzs','"+i+"','afz')\"></a></td>"+
    				"</tr>"); 
    			$('#zhushou'+i+"_"+zsId).combobox({    
    			    valueField:'jobNo',    
    			    textField:'name', 
    			    data:user,
    			    onHidePanel:function(none){
    				    var data = $(this).combobox('getData');
    				    var val = $(this).combobox('getValue');
    				    var result = true;
    				    for (var i = 0; i < data.length; i++) {
    				        if (val == data[i].jobNo) {
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
    					keys[keys.length] = 'jobNo';
    					keys[keys.length] = 'code';
    					keys[keys.length] = 'name';
    					keys[keys.length] = 'pinyin';
    					keys[keys.length] = 'wb';
    					keys[keys.length] = 'inputCode';
    					return filterLocalCombobox(q, row, keys);
    				},onSelect : function() {
    		    		var m=new Map();
    		     		var id=$(this).prop("id");
    		    		var zs = $('[id^=zhushou]');
    		     		var ssysbm=$('#ssysbm').combobox("getValue");
    		     		var gui=$('#guiDocd').combobox("getValue");
    		     		var b = 0;
					  	var zsys=$(this).combobox("getValue");
    		     		zs.each(function(index,obj){
    		     			var zsys2=$(this).combobox("getValue");
    		     			if($(obj).combogrid('getValue') == zsys){
					 			b++;
					 		}
    		    			if(ssysbm!=null&&ssysbm!=""){
    		 	 				if(zsys2==ssysbm){
    		 	 					$("#"+id).combobox("clear");
    		 	 					$.messager.alert("提示","助手与手术医生相同！");
    		 	 					setTimeout(function(){
										$(".messager-body").window('close');
									},3000);
    		 	 	 				return
    		 	 	 			}
    		 	 			}
    		    			if(gui!=null&&gui!=""){
    			 				if(gui==zsys2){
    			 					$("#"+id).combobox("clear");
    			 					$.messager.alert("提示","助手与指导医生相同！");
    			 					setTimeout(function(){
										$(".messager-body").window('close');
									},3000);
    			 	 			}
    			 			}
    		     		});
    		     		if(b>1){
    			    		$('#'+id).combobox('clear');
    						$.messager.alert("提示","助手重复!","info");
    						setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
    			    	}
    			    }
    			   
    			});  
    			$('#xishou'+i+"_"+xiShouId).combogrid({    
    				url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
			 		idField : 'jobNo',
			 		textField : 'name',
			 		mode:"remote",
			 		panelAlign:'right',
			 		panelWidth:320,
			 		editable : true,
			 		pageList:[10,20,30,40,50],
					pageSize:"10",
					pagination:true,
				 	columns:[[   
							{field:'jobNo',title:'工作号',width:'130'},
				 	         {field:'name',title:'名称',width:'160'} 
				 	        
			 	     ]],  
			 	   onHidePanel:function(none){
				 	     var val = $(this).combogrid('getValue');
				 	     if(xishouName(val)){
					 	    	$(this).combogrid('clear');
					 	    	$.messager.alert("提示","护士信息不能重复!");
					 	    	setTimeout(function(){
									$(".messager-body").window('close');
								},2000);
						    }
				 	 }
    			});  
    			$('#xunhui'+i+"_"+xunHuiId).combogrid({    
    				url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
			 		idField : 'jobNo',
			 		textField : 'name',
			 		mode:"remote",
			 		panelAlign:'right',
			 		panelWidth:320,
			 		editable : true,
			 		pageList:[10,20,30,40,50],
					pageSize:"10",
					pagination:true,
				 	columns:[[   
							{field:'jobNo',title:'工作号',width:'130'},
				 	         {field:'name',title:'名称',width:'160'} 
				 	        
			 	     ]],  
			 	   onHidePanel:function(none){
				 	     var val = $(this).combogrid('getValue');
				 	    if(xunhuiName(val)){
					 	    	$(this).combogrid('clear');
					 	    	$.messager.alert("提示","护士信息不能重复!");
					 	    	setTimeout(function(){
									$(".messager-body").window('close');
								},2000);
						    }
				 	 }
    			});  
    			$('#jinxiu'+i+"_"+jinXiuId).combogrid({    
    				url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
    		 		idField : 'jobNo',
    		 		textField : 'name',
    		 		mode:"remote",
    		 		panelAlign:'right',
    		 		panelWidth:320,
    		 		editable : true,
    		 		pageList:[10,20,30,40,50],
    				pageSize:"10",
    				pagination:true,
    			 	columns:[[   
    						{field:'jobNo',title:'工作号',width:'130'},
    			 	         {field:'name',title:'名称',width:'160'} 
    			 	        
    		 	     ]],  
    			     onHidePanel:function(none){
				 	     var val = $(this).combogrid('getValue');
				 	    if(jinxiuName(val)){
					 	    	$(this).combogrid('clear');
					 	    	$.messager.alert("提示","护士信息不能重复!");
					 	    	setTimeout(function(){
									$(".messager-body").window('close');
								},2000);
						    }
				 	 }
    			});  
    			$('#afz'+i).linkbutton({    
    			    iconCls: 'icon-add'   
    			});  
    			$('#jafz'+i).linkbutton({    
    			    iconCls: 'icon-remove'   
    			});  
    			$('#afz'+(i-1)).hide();
    			$('#jafz'+(i-1)).hide();
    		}else{
    			$('#zhushou0').prop("id",'zhushou0_'+zsId);
    			$("#xs0").val(xiShouId);
	    		$('#xishou0').prop("id",'xishou0_'+xiShouId);
	    		$("#xh0").val(xunHuiId);
	    		$('#xunhui0').prop("id",'xunhui0_'+xunHuiId);
	    		$("#jx0").val(jinXiuId);
	    		$('#jinxiu0').prop("id",'jinxiu0_'+jinXiuId);
    		}
    		if(data.zsDocList[i]&&data.zsDocList[i].emplCode){
    			$('#zhushou'+i+'_'+zsId).combobox('select',data.zsDocList[i].emplCode);
    		}
    		if(data.xiShouList[i]&&data.xiShouList[i].emplCode){
    			$('#xishou'+i+'_'+xiShouId).combogrid('grid').datagrid('load',{q:data.xiShouList[i].emplCode});
    			$('#xishou'+i+'_'+xiShouId).combogrid('setValue',(data.xiShouList[i]&&(data.xiShouList[i].emplCode||""))||"");
    		}
    		if(data.xunHuiList[i]&&data.xunHuiList[i].emplCode){
    			$('#xunhui'+i+'_'+xunHuiId).combogrid('grid').datagrid('load',{q:data.xunHuiList[i].emplCode});
    			$('#xunhui'+i+'_'+xunHuiId).combogrid('setValue',data.xunHuiList[i].emplCode);
    		}
    		if(data.jinXiuList[i]&&data.jinXiuList[i].emplCode){
    			$('#jinxiu'+i+'_'+jinXiuId).combogrid('grid').datagrid('load',{q:data.jinXiuList[i].emplCode});
    			$('#jinxiu'+i+'_'+jinXiuId).combogrid('setValue',(data.jinXiuList[i]&&(data.jinXiuList[i].emplCode||""))||"");
    		} 
    		zhushouMap.put(""+zsId,zsEmplCode);
			xunhuiMap.put(""+xunHuiId,xunHuiEmplCode);
			xishouMap.put(""+xiShouId,xiShouEmplCode);
			jinxiuMap.put(""+jinXiuId,jinXiuEmplCode);
			indexfz=num;
			

			$('#xishou'+i+'_'+xiShouId).combogrid("readonly",false);
			$('#xunhui'+i+'_'+xunHuiId).combogrid("readonly",false);
			$('#jinxiu'+i+'_'+jinXiuId).combogrid("readonly",false);
    	}
	}
   	//手术名称 
   	for(var i=0;i<data.opNameList.length;i++){
   		var ssitemName = (data.opNameList[i]&&(data.opNameList[i].itemName||""))||"";
   		var ssitemId2 = (data.opNameList[i]&&(data.opNameList[i].itemId||""))||"";
   		var ssitemId = (data.opNameList[i]&&(data.opNameList[i].id||""))||"";
   		
   		if(i!=0){
   			
   			$("#trndss"+(i-1)).after("<tr id=\"trndss"+i+"\">"+
	    			    "<td >手术名称"+(i+1)+"：</td>"+
	    				"<td colspan=\"7\"><input id=\"nssmc"+i+"_"+ssitemId+"\" name=\"itemName"+i+"\" class=\"easyui-combogrid\" style=\"width: 90%\" data-options=\"required:true\">"+
	    				"<a id=\"janssmc"+i+"\" href=\"javascript:void(0)\" onclick=\"removeTr('trndss','anssmc','"+(i-1)+"')\"></a>"+
	    				"<a id=\"anssmc"+i+"\" href=\"javascript:void(0)\" onclick=\"add('拟手术名称','trndss','"+i+"','nssmc','itemName','anssmc')\"></a></td>"+
	    			"</tr>"); 
   			$('#nssmc'+i+"_"+ssitemId).combogrid({
   			    url : '<%=basePath%>operation/operationList/undrugComboboxfy.action',
			    idField:'code',    
			    textField:'name',
			    mode:"remote",
				pageList:[10,20,30,40,50],
			 	pageSize:"10",
			 	pagination:true,
				columns:[[    
				         {field:'code',title:'编码',width:'18%'},    
				         {field:'name',title:'名称',width:'20%'},    
				         {field:'undrugPinyin',title:'拼音',width:'20%'},    
				         {field:'undrugWb',title:'五笔',width:'18%'},
			     	 {field:'undrugInputcode',title:'自定义码',width:'20%'}
				     ]],  
			 	 onHidePanel:function(none){
			 	     var val = $(this).combogrid('getText');
			 	     if(nssmcName(val)){
				 	    	$(this).combogrid('clear');
				 	    	$.messager.alert("提示","手术名称不能重复!","info");
				 	    	setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
					 }
			 	    var name = $(this).combogrid('getValue');
		    	 	if(name==val){
		    	 		if(!$('#operation').is(':checked')){
		    	 			$(this).combogrid("clear");
		    	 			$.messager.alert("提示","该手术为非自定义手术，请选择下拉列表中的数据！","info");
		    	 			setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
		    	 		}
		    	 	} 
			 	 } 
   			
   			}); 
   			$('#anssmc'+i).linkbutton({    
   			    iconCls: 'icon-add'   
   			});  
   			$('#janssmc'+i).linkbutton({    
   			    iconCls: 'icon-remove'   
   			});  
   			$('#anssmc'+(i-1)).hide();
	    		$('#janssmc'+(i-1)).hide();
   		}else{
   			$("#nssmc0_").prop("id",'nssmc0_'+ssitemId);
   			$("#mc0").val(ssitemId);
   		}
   		
   		if(data.opNameList[i]&&!data.opNameList[i].itemId&&data.opNameList[i].itemName){
   			$('#nssmc'+i+'_'+ssitemId).combogrid("setValue",data.opNameList[i].itemName);
   			$('#nssmc'+i+'_'+ssitemId).combogrid('setText',data.opNameList[i].itemName);
   			$("#operation").prop('checked',true);
   		}else{
   			$('#nssmc'+i+'_'+ssitemId).combogrid("grid").datagrid('load',{q:data.opNameList[i].itemId});
   			$('#nssmc'+i+'_'+ssitemId).combogrid("setValue",data.opNameList[i].itemId);
   			$('#nssmc'+i+'_'+ssitemId).combogrid('setText',data.opNameList[i].itemName);
   		}
   		ssmcMap.put(""+ssitemId,ssitemName);
   		indexName2=data.opNameList.length;
   	}
	//诊断名称 diagName
  	 for(var i=0;i<data.opDiagList.length;i++){
  		var zdEmplCode = (data.opDiagList[i]&&(data.opDiagList[i].diagName||""))||"";
		var zdId = (data.opDiagList[i]&&(data.opDiagList[i].id||""))||"";
  		if(i!=0){
  			$("#trsqzd"+(i-1)).after("<tr id=\"trsqzd"+i+"\">"+
		    			    "<td >术前诊断"+(i+1)+"：</td>"+
		    				"<td colspan=\"7\"><input id=\"shoushuzd"+i+"_"+zdId+"\" class=\"easyui-combogrid\" name=\"diagName"+i+"\"  style=\"width: 90%\" ></td>"+
		    			"</tr>"); 
  			$('#shoushuzd'+i+"_"+zdId).combogrid({
  				url : '<%=basePath%>operation/operationList/icdCombobox2fy.action',
				idField : 'code',
				textField : 'name',
				multiple : false,
				editable : true,
				mode:"remote",
				pageList:[10,20,30,40,50],
			 	pageSize:"10",
			 	pagination:true,
				columns:[[    
				         {field:'code',title:'编码',width:'18%'},    
				         {field:'name',title:'名称',width:'20%'},    
				         {field:'pinyin',title:'拼音',width:'20%'},    
				         {field:'wb',title:'五笔',width:'18%'},
			     	 {field:'inputcode',title:'自定义码',width:'20%'}
				     ]],  
				    onSelect:function(rowIndex, rowData){
			 	} 
	    	}); 
  		 }else{
  			 $('#shoushuzd0_').prop("id",'shoushuzd0_'+zdId);
  		 }
  		if(data.opDiagList[i]&&data.opDiagList[i].icdCode&&!data.opDiagList[i].diagName){
  			$('#shoushuzd'+i+'_'+zdId).combogrid('setText',"未命名"); 
  		}else{
  			$('#shoushuzd'+i+'_'+zdId).combogrid('setText',data.opDiagList[i].diagName); 
  			$('#shoushuzd'+i+'_'+zdId).combogrid('setValue',data.opDiagList[i].diagName); 
  		}
  		sqzdMap.put(""+zdId,zdEmplCode);
  		indexName=data.opDiagList.length;
  	}
	
}

/**
 * @Description:填充已登记信息
 * @Author: tangfeishuai
 * @CreateDate: 2016年4月20日
 * @param:
 * @return:
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */
function ReCordFill(data){
	//显示数据
	//执行科室
	$('#execDept').combobox('setValue',data.execDept);
	//门诊号
	$('#clinicCode').text(data.clinicCode);
	//预约日期
	$('#preDate').text(data.preDate);
	//麻醉类型
	$("#anesType").combobox('setValue',data.anesType);
	//申请医生
	$('#applyDoctor').text(empMap[data.applyDoctor]); 
	//手术规模
	$('#degree').textbox('setValue',data.degree); 
	 //开始时间
	$('#preDate2').val(data.enterDate);		
	//结束时间
	$('#duration').val(data.outDate); 
	//手术台类型
	$("#consoleType").combobox('setValue',data.consoleType);
	//手术医生
	$("#ssysbm").combobox('setValue',data.opDoctor);
	//手术医生科室
	$("#docDpcd").combobox('setValue',data.docDpcd);
	//手术规模
	$("#degree").combobox('setValue',data.degree);
	//切口类型
	$("#inciType").combobox('setValue',data.inciType);
	//手术分类
	$("#opType").combobox('setValue',data.opsKind);
	//备注
	$("#remark").val(data.remark);
	//指导医生
	$('#guiDocd').combobox('setValue',data.guiDocd);
	//审批医生
	$('#approveDocd').combobox('setValue',data.approveDocd);
		//术前血压
	$("#forepress").textbox('setValue',data.forepress);
	//术后血压
	$("#steppress").textbox('setValue',data.steppress);
	//术前脉搏
	$("#forepulse").textbox('setValue',data.forepulse);
	//术后脉搏
	$("#steppulse").textbox('setValue',data.steppulse);
	//褥疮数量
	$("#scarNum").textbox('setValue',data.scarNum);
	//输液量
	$("#transfusionQty").textbox('setValue',data.transfusionQty);
	//标本数量
	$("#sampleQty").textbox('setValue',data.sampleQty);
	//引流管次数
	$("#guidtubeNum").textbox('setValue',data.guidtubeNum);
	//抽血次数
	$("#letBlood").textbox('setValue',data.letBlood);
	//肌注次数
	$("#muscleLine").textbox('setValue',data.muscleLine);
	//静注次数
	$("#mainLine").textbox('setValue',data.mainLine);
	//输液次数
	$("#transfusion").textbox('setValue',data.transfusion);
	//输氧次数
	$("#transoxyen").textbox('setValue',data.transoxyen);
	//导尿次数
	$("#stale").textbox('setValue',data.stale);
	/* //特殊说明
	$("#specialComment").val(data.specialComment); */
	//血型
	$("#bloodCode").combobox('setValue',data.bloodCode);
	//血液类型
	$("#bloodType").textbox('setValue',data.bloodType);
	//用血量
	$("#bloodNum").textbox('setValue',data.bloodNum);
	//单位
	$("#bloodUnit").textbox('setValue',data.bloodUnit); 
	//手术室
	$('#opRoom').combobox('setValue',data.roomId);
	//术前是否清醒
	if(data.foreynsober==1){
		$('#foreynsober').prop("checked", true); 
		$('#foreynsober2').val(1); 
	}else{
		$('#foreynsober').prop("checked", false); 
		$('#foreynsober2').val(0); 
	}
	//术后是否清醒
	if(data.stepynsober==1){
		$('#stepynsober').prop("checked", true); 
		$('#stepynsober2').val(1); 
	}else{
		$('#stepynsober').prop("checked", false); 
		$('#stepynsober2').val(0); 
	}
	//是否危重
	if(data.danger==1){
		$('#danger').prop("checked", true); 
		$('#danger2').val(1); 
	}else{
		$('#danger').prop("checked", false); 
		$('#danger2').val(0); 
	}
	//是否隔离
	if(data.seperate==1){
		$('#seperate').prop("checked", true); 
		$('#seperate2').val(1); 
	}else{
		$('#seperate').prop("checked", false); 
		$('#seperate2').val(0); 
	}
	//是否收费
	if(data.ynfee==1){
		$('#ynfee').prop("checked", true); 
		document.getElementById("ynfee").disabled=true;
		$('#ynfee2').val(1); 
	}else{
		$('#ynfee').prop("checked", false); 
		$('#ynfee2').val(0); 
	} 
	//是否有菌
	if(data.yngerm==1){
		$('#germ').prop("checked", true); 
		$('#germ2').val(1); 
	}else{
		$('#germ').prop("checked", false); 
		$('#germ2').val(0); 
	}
	//是否感染
	if(data.question==1){
		$('#question').prop("checked", true); 
		$('#question2').val(1); 
	}else{
		$('#question').prop("checked", false); 
		$('#question2').val(0); 
	}
	
	
	var opName = data.opNameList.length;
	var xunHui = data.xunHuiList.length;
	var xiShou = data.xiShouList.length;
	var jinXiu = data.jinXiuList.length;
	var zs = data.zsDocList.length;
	var zd = data.opDiagList.length;
	
	var num;
	if(xiShou>xunHui){
		num=xiShou;
	}else{
		num=xunHui;
	}
	if(zs>num){
		num=zs;
	}
	if(jinXiu>num){
		num=jinXiu;
	}
	//助手，护士  
	for(var i=0;i<num;i++){
		var xunHuiEmplCode = (data.xunHuiList[i]&&(data.xunHuiList[i].emplCode||""))||"";
		var zsEmplCode = (data.zsDocList[i]&&(data.zsDocList[i].emplCode||""))||"";
		var xiShouEmplCode = (data.xiShouList[i]&&(data.xiShouList[i].emplCode||""))||"";
		var jinXiuEmplCode = (data.jinXiuList[i]&&(data.jinXiuList[i].emplCode||""))||"";
		var xunHuiId = (data.xunHuiList[i]&&(data.xunHuiList[i].id||""))||"";
		var zsId = (data.zsDocList[i]&&(data.zsDocList[i].id||""))||"";
		var xiShouId = (data.xiShouList[i]&&(data.xiShouList[i].id||""))||"";
		var jinXiuId = (data.jinXiuList[i]&&(data.jinXiuList[i].id||""))||"";
		if(i!=0){
			$("#trzs"+(i-1)).after("<tr id=\"trzs"+i+"\">"+
				    "<td >助手"+(i+1)+"：</td>"+
					"<td ><input id=\"zhushou"+i+"_"+zsId+"\" name=\"thelper"+i+"\" >"+
					"</td>"+
					"<td  >洗手护士"+(i+1)+"：</td>"+
					"<td ><input id=\"xishou"+i+"_"+xiShouId+"\" name=\"wash"+i+"\">"+
					"</td>"+
				    "<td >巡回护士"+(i+1)+"：</td>"+
					"<td ><input id=\"xunhui"+i+"_"+xunHuiId+"\" name=\"tour"+i+"\">"+
					"</td>"+
				    "<td >进修人员"+(i+1)+"：</td>"+
					"<td ><input id=\"jinxiu"+i+"_"+jinXiuId+"\" name=\"engage"+i+"\">"+
					"<a id=\"jafz"+i+"\" href=\"javascript:void(0)\" onclick=\"removeTr('trzs','afz','"+(i-1)+"')\"></a>"+
					"<a id=\"afz"+i+"\" href=\"javascript:void(0)\" onclick=\"addfz('trzs','"+i+"','afz')\"></a></td>"+
				"</tr>"); 
			$('#zhushou'+i+"_"+zsId).combobox({    
			    valueField:'jobNo',    
			    textField:'name', 
			    data:user,
			    onHidePanel:function(none){
				    var data = $(this).combobox('getData');
				    var val = $(this).combobox('getValue');
				    var result = true;
				    for (var i = 0; i < data.length; i++) {
				        if (val == data[i].jobNo) {
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
					keys[keys.length] = 'jobNo';
					keys[keys.length] = 'code';
					keys[keys.length] = 'name';
					keys[keys.length] = 'pinyin';
					keys[keys.length] = 'wb';
					keys[keys.length] = 'inputCode';
					return filterLocalCombobox(q, row, keys);
				},onSelect : function() {
		    		var m=new Map();
		     		var id=$(this).prop("id");
		    		var zs = $('[id^=zhushou]');
		     		var ssysbm=$('#ssysbm').combobox("getValue");
		     		var gui=$('#guiDocd').combobox("getValue");
		     		var b = 0;
		     		var zsys=$(this).combobox("getValue");
		     		zs.each(function(index,obj){
		     			if($(obj).combogrid('getValue') == zsys){
				 			b++;
				 		}
		     			var zsys2=$(this).combobox("getValue");
		    			if(ssysbm!=null&&ssysbm!=""){
		 	 				if(zsys2==ssysbm){
		 	 					$("#"+id).combobox("clear");
		 	 					$.messager.alert("提示","助手与手术医生相同！");
		 	 					setTimeout(function(){
									$(".messager-body").window('close');
								},3000);
		 	 	 				return
		 	 	 			}
		 	 			}
		    			if(gui!=null&&gui!=""){
			 				if(gui==zsys2){
			 					$("#"+id).combobox("clear");
			 					$.messager.alert("提示","助手与指导医生相同！");
			 					setTimeout(function(){
									$(".messager-body").window('close');
								},3000);
			 	 			}
			 			}
		     		});
		     		if(b>1){
			    		$('#'+id).combobox('clear');
						$.messager.alert("提示","助手重复!","info");
						setTimeout(function(){
							$(".messager-body").window('close');
						},2000);
			    	}
			    }
			   
			});  
			$('#xishou'+i+"_"+xiShouId).combogrid({    
				url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
		 		idField : 'jobNo',
		 		textField : 'name',
		 		mode:"remote",
		 		panelAlign:'left',
		 		panelWidth:320,
		 		editable : true,
		 		pageList:[10,20,30,40,50],
				pageSize:"10",
				pagination:true,
			 	columns:[[   
						{field:'jobNo',title:'工作号',width:'130'},
			 	         {field:'name',title:'名称',width:'160'} 
			 	        
		 	     ]],  
		 	     onHidePanel:function(none){
			 	     var val = $(this).combogrid('getValue');
			 	     if(xishouName(val)){
				 	    	$(this).combogrid('clear');
				 	    	$.messager.alert("提示","护士信息不能重复!");
				 	    	setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
					    }
			 	 }
			});  
			$('#xunhui'+i+"_"+xunHuiId).combogrid({    
				url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
		 		idField : 'jobNo',
		 		textField : 'name',
		 		mode:"remote",
		 		panelAlign:'right',
		 		panelWidth:320,
		 		editable : true,
		 		pageList:[10,20,30,40,50],
				pageSize:"10",
				pagination:true,
			 	columns:[[   
						{field:'jobNo',title:'工作号',width:'130'},
			 	         {field:'name',title:'名称',width:'160'} 
			 	        
		 	     ]],  
			     onHidePanel:function(none){
			 	      var val = $(this).combogrid('getValue');
			 	      if(xunhuiName(val)){
				 	    	$(this).combogrid('clear');
				 	    	$.messager.alert("提示","护士信息不能重复!");
				 	    	setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
					    }
			 	 } 
			});  
			$('#jinxiu'+i+"_"+jinXiuId).combogrid({    
				url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
		 		idField : 'jobNo',
		 		textField : 'name',
		 		mode:"remote",
		 		panelAlign:'right',
		 		panelWidth:320,
		 		editable : true,
		 		pageList:[10,20,30,40,50],
				pageSize:"10",
				pagination:true,
			 	columns:[[   
						{field:'jobNo',title:'工作号',width:'130'},
			 	         {field:'name',title:'名称',width:'160'} 
			 	        
		 	     ]],  
			     onHidePanel:function(none){
			 	     var val = $(this).combogrid('getValue');
			 	     if(jinxiuName(val)){
				 	    	$(this).combogrid('clear');
				 	    	$.messager.alert("提示","护士信息不能重复!");
				 	    	setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
					    }
			 	 } 
			});  
			$('#afz'+i).linkbutton({    
			    iconCls: 'icon-add'   
			});  
			$('#jafz'+i).linkbutton({    
			    iconCls: 'icon-remove'   
			});  
			$('#afz'+(i-1)).hide();
			$('#jafz'+(i-1)).hide();
		}else{
			$('#zhushou0').prop("id",'zhushou0_'+zsId);
			$("#xs0").val(xiShouId);
			$("#xh0").val(xunHuiId);
			$("#jx0").val(jinXiuId);
    		$('#xishou0').prop("id",'xishou0_'+xiShouId);
    		$('#xunhui0').prop("id",'xunhui0_'+xunHuiId);
    		$('#jinxiu0').prop("id",'jinxiu0_'+jinXiuId);
		}
		
		$('#xishou'+i+'_'+xiShouId).combogrid("readonly",false);
		$('#xunhui'+i+'_'+xunHuiId).combogrid("readonly",false);
		$('#jinxiu'+i+'_'+jinXiuId).combogrid("readonly",false);
		if(data.zsDocList[i]&&data.zsDocList[i].emplCode){
			$('#zhushou'+i+'_'+zsId).combobox('select',(data.zsDocList[i]&&(data.zsDocList[i].emplCode||""))||"");
		}
		if(data.xiShouList[i]&&data.xiShouList[i].emplCode){
			$('#xishou'+i+'_'+xiShouId).combogrid('grid').datagrid('load',{q:data.xiShouList[i].emplCode});
			$('#xishou'+i+'_'+xiShouId).combogrid('setValue',(data.xiShouList[i]&&(data.xiShouList[i].emplCode||""))||"");
		}
		if(data.xunHuiList[i]&&data.xunHuiList[i].emplCode){
			$('#xunhui'+i+'_'+xunHuiId).combogrid('grid').datagrid('load',{q:data.xunHuiList[i].emplCode});
			$('#xunhui'+i+'_'+xunHuiId).combogrid('setValue',(data.xunHuiList[i]&&(data.xunHuiList[i].emplCode||""))||"");
			
		}
		if(data.jinXiuList[i]&&data.jinXiuList[i].emplCode){
			if(data.jinXiuList[i].emplCode!=null&&data.jinXiuList[i].emplCode!=""){
				$('#jinxiu'+i+'_'+jinXiuId).combogrid('grid').datagrid('load',{q:data.jinXiuList[i].emplCode});
				$('#jinxiu'+i+'_'+jinXiuId).combogrid('setValue',(data.jinXiuList[i]&&(data.jinXiuList[i].emplCode||""))||"");
			}else{
				$('#jinxiu'+i+'_'+jinXiuId).combogrid('grid').datagrid('load',{q:" "});
			}
			
			
		}
		
		zhushouMap.put(""+zsId,zsEmplCode);
		xunhuiMap.put(""+xunHuiId,xunHuiEmplCode);
		xishouMap.put(""+xiShouId,xiShouEmplCode);
		jinxiuMap.put(""+jinXiuId,jinXiuEmplCode);
	
		indexfz=num;
	}
   	//手术名称 
   	for(var i=0;i<data.opNameList.length;i++){
   		var ssitemName = (data.opNameList[i]&&(data.opNameList[i].itemName||""))||"";
   		var ssitemId2 = (data.opNameList[i]&&(data.opNameList[i].itemId||""))||"";
   		var ssitemId = (data.opNameList[i]&&(data.opNameList[i].id||""))||"";
   		if(i!=0){
   			$("#trndss"+(i-1)).after("<tr id=\"trndss"+i+"\">"+
	    			    "<td >手术名称"+(i+1)+"：</td>"+
	    				"<td colspan=\"7\"><input id=\"nssmc"+i+"_"+ssitemId+"\" class=\"easyui-combogrid\" name=\"itemName"+i+"\"  style=\"width: 90%\" data-options=\"required:true\">"+
	    				"<a id=\"janssmc"+i+"\" href=\"javascript:void(0)\" onclick=\"removeTr('trndss','anssmc','"+(i-1)+"')\"></a>"+
	    				"<a id=\"anssmc"+i+"\" href=\"javascript:void(0)\" onclick=\"add('拟手术名称','trndss','"+i+"','nssmc','itemName','anssmc')\"></a></td>"+
	    			"</tr>"); 
   			$('#nssmc'+i+"_"+ssitemId).combogrid({
   				url : '<%=basePath %>operation/operationList/undrugComboboxfy.action',
   				idField : 'code',
   				textField : 'name',
   				editable : true,
   				mode:"remote",
   				pageList:[10,20,30,40,50],
   			 	pageSize:"10",
   			 	pagination:true,
   			 	multiple : false,
   				columns:[[    
   				         {field:'code',title:'编码',width:'20%'},    
   				         {field:'name',title:'名称',width:'20%'},    
   				         {field:'undrugPinyin',title:'拼音码',width:'20%'},
   				         {field:'undrugWb',title:'五笔码',width:'20%'},    
   				         {field:'undrugInputcode',title:'自定义码',width:'20%'},
   				     ]],  
	   			 onHidePanel:function(none){
			 	      var val = $(this).combogrid('getText');
			 	      if(nssmcName(val)){
				 	    	$(this).combogrid('clear');
				 	    	$.messager.alert("提示","手术名称不能重复!","info");
				 	    	setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
					   }
			 	      var name = $(this).combogrid('getValue');
			    	 	if(name==val){
			    	 		if(!$('#operation').is(':checked')){
			    	 			$(this).combogrid("clear");
			    	 			$.messager.alert("提示","该手术为非自定义手术，请选择下拉列表中的数据！","info");
			    	 			setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
			    	 		}
			    	 	} 
			 	 }
   			}); 
   			$('#anssmc'+i).linkbutton({    
   			    iconCls: 'icon-add'   
   			});  
   			$('#janssmc'+i).linkbutton({    
   			    iconCls: 'icon-remove'   
   			});  
   			$('#anssmc'+(i-1)).hide();
   			if(i>1){
   				$('#janssmc'+(i-1)).hide();
   			}
	    	
   		}else{
   			$("#nssmc0_").prop("id",'nssmc0_'+ssitemId);
   			$("#mc0").val(ssitemId);
   		}
   		 if(data.opNameList[i]&&!data.opNameList[i].itemId&& data.opNameList[i].itemName){
   			$('#nssmc'+i+'_'+ssitemId).combogrid('setValue',data.opNameList[i].itemName);
   			$('#nssmc'+i+'_'+ssitemId).combogrid('setText',data.opNameList[i].itemName);
   			$("#operation").prop('checked',true);
   		}else{
   			$('#nssmc'+i+'_'+ssitemId).combogrid("grid").datagrid('reload',{q:data.opNameList[i].itemId});
   			$('#nssmc'+i+'_'+ssitemId).combogrid("setValue",data.opNameList[i].itemId);
   			$('#nssmc'+i+'_'+ssitemId).combogrid('setText',data.opNameList[i].itemName);
   		} 
   		
   		ssmcMap.put(""+ssitemId,ssitemName);
   		indexName2=data.opNameList.length;
   	}
		
	//诊断名称 
  	 for(var i=0;i<data.opDiagList.length;i++){
  		var zdEmplCode = (data.opDiagList[i]&&(data.opDiagList[i].diagName||""))||"";
		var zdId = (data.opDiagList[i]&&(data.opDiagList[i].id||""))||"";
  		if(i!=0){
  			$("#trsqzd"+(i-1)).after("<tr id=\"trsqzd"+i+"\">"+
		    			    "<td >术前诊断"+(i+1)+"：</td>"+
		    				"<td colspan=\"7\"><input id=\"shoushuzd"+i+"_"+zdId+"\"     name=\"diagName"+i+"\"  style=\"width: 90%\" </td>"+
		    			"</tr>"); 
  			$('#shoushuzd'+i+"_"+zdId).combogrid({
  				url : '<%=basePath%>operation/operationList/icdCombobox2fy.action',
				idField : 'code',
				textField : 'name',
				multiple : false,
				editable : true,
				mode:"remote",
				pageList:[10,20,30,40,50],
			 	pageSize:"10",
			 	pagination:true,
				columns:[[    
				         {field:'code',title:'编码',width:'18%'},    
				         {field:'name',title:'名称',width:'20%'},    
				         {field:'pinyin',title:'拼音',width:'20%'},    
				         {field:'wb',title:'五笔',width:'18%'},
			     	 {field:'inputcode',title:'自定义码',width:'20%'}
				     ]],  
				    onSelect:function(rowIndex, rowData){
			 	} 
	    	}); 
  		 }else{
  			 $('#shoushuzd0_').prop("id",'shoushuzd0_'+zdId);
  		 }
  		if(data.opDiagList[i]&&data.opDiagList[i].icdCode&&!data.opDiagList[i].diagName){
  			$('#shoushuzd'+i+'_'+zdId).combogrid('setText',"未命名"); 
  		}else{
  			$('#shoushuzd'+i+'_'+zdId).combogrid('setValue',data.opDiagList[i].diagName); 
  			$('#shoushuzd'+i+'_'+zdId).combogrid('setText',data.opDiagList[i].diagName); 
  		}
  		sqzdMap.put(""+zdId,zdEmplCode);
  		indexName=data.opDiagList.length;
  	}
	
}

/**
 * @Description:公用申请信息清除
 * @Author: tangfeishuai
 * @CreateDate: 2016年4月20日
 * @param:
 * @return:
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */
function applyDataClear(){
	 $('#typeNo').combobox('setValue','0');
	 $('#questionName').textbox('setValue','');
	 $('#opRoom').combobox({
		required:false
 	})
	$('#Form1').form('reset');
	$("#question").attr("checked",false);
	$("#germ").attr("checked",false);
	$("#foreynsober").attr("checked",false);
	$("#stepynsober").attr("checked",false);
	$("#danger").attr("checked",false);
	$("#seperate").attr("checked",false);
	$("#ynfee").attr("checked",false);
	$("#operation").prop("checked",false);
	//清除显示数据
	$('#name').text('');
	$('#sex').text('');
	$('#age').text('');
	$('#inDept').text('');
	$('#clinicCode').text('');
	$('#preDate').text('');
	$('#applyDoctor').text('');
	$('#bedNo').text('');
	$('#patientNo').text('');
	$('#birthDay').text('');
	$('#paykindCode').text('');
	$('#prepayCost').text('');
	//删除助手护士信息
	for(var i=0;i<indexfz;i++){
		if(i!=0){
			$("#trzs"+i).remove();
			$('#afz'+(i-1)).show();
			$('#jafz'+(i-1)).show();
		}else{
			var zhushou=$('[id^=zhushou]');
			var xishou=$('[id^=xishou]');
			var xunhui=$('[id^=xunhui]');
			var jinxiu=$('[id^=jinxiu]');
			zhushou.each(function(){
				var id=$(this).prop("id","zhushou0");
			});
			xishou.each(function(){
				$(this).prop("id","xishou0");
				$(this).combogrid("readonly",true);
			});
			xunhui.each(function(){
				$(this).prop("id","xunhui0");
				$(this).combogrid("readonly",true);
			});
			jinxiu.each(function(){
				$(this).prop("id","jinxiu0");
				$(this).combogrid("readonly",true);
			});
		}
		
	}
	//删除手术诊断信息
	for(var i=0;i<indexName;i++){
		if(i!=0){
			$("#trsqzd"+i).remove();
		}else{
			var shoushuzd=$('[id^=shoushuzd]');
			shoushuzd.each(function(){
				$(this).prop("id","shoushuzd0_");
			});
		}
		
	}
	//删除手术名称信息
	for(var i=0;i<indexName2;i++){
		if(i!=0){
			$("#trndss"+i).remove();
			$('#anssmc'+(i-1)).show();
			$('#janssmc'+(i-1)).show();
		}else{
			var nssmc=$('[id^=nssmc]');
			nssmc.each(function(){
				$(this).prop("id","nssmc0_");
			});
		}
		
	}
	ssmcMap=new Map();
}

/**
 * @Description:只清除表单信息
 * @Author: tangfeishuai
 * @CreateDate: 2016年4月20日
 * @param:
 * @return:
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */
function applyData2Clear(){
	$('#typeNo').combobox('setValue','0');
	$('#questionName').textbox('setValue','');
	$('#opRoom').combobox({
		required:false
	})
	$('#Form1').form('reset');
	$("#question").attr("checked",false);
	$("#germ").attr("checked",false);
	$("#foreynsober").attr("checked",false);
	$("#stepynsober").attr("checked",false);
	$("#danger").attr("checked",false);
	$("#seperate").attr("checked",false);
	$("#ynfee").attr("checked",false);
	$("#operation").prop("checked",false);
	$('#bedNo').text('');
	//删除助手护士信息
	for(var i=0;i<indexfz;i++){
		if(i!=0){
			$("#trzs"+i).remove();
			$('#afz'+(i-1)).show();
			$('#jafz'+(i-1)).show();
		}else{
			var zhushou=$('[id^=zhushou]');
			var xishou=$('[id^=xishou]');
			var xunhui=$('[id^=xunhui]');
			var jinxiu=$('[id^=jinxiu]');
			zhushou.each(function(){
				var id=$(this).prop("id","zhushou0");
			});
			xishou.each(function(){
				$(this).prop("id","xishou0");
				$(this).combogrid("readonly",true);
			});
			xunhui.each(function(){
				$(this).prop("id","xunhui0");
				$(this).combogrid("readonly",true);
			});
			jinxiu.each(function(){
				$(this).prop("id","jinxiu0");
				$(this).combogrid("readonly",true);
			});
		}
		
	}
	
	
	//删除手术诊断信息
	for(var i=0;i<indexName;i++){
		if(i!=0){
			$("#trsqzd"+i).remove();
		}else{
			var shoushuzd=$('[id^=shoushuzd]');
			shoushuzd.each(function(){
				$(this).prop("id","shoushuzd0_");
			});
		}
		
	}
	//删除手术名称信息
	for(var i=0;i<indexName2;i++){
		if(i!=0){
			$("#trndss"+i).remove();
			$('#anssmc'+(i-1)).show();
			$('#janssmc'+(i-1)).show();
		}else{
			var nssmc=$('[id^=nssmc]');
			nssmc.each(function(){
				$(this).prop("id","nssmc0_");
			});
		}
		
	}
	ssmcMap=new Map();
}

/**
 * 保存判断  获取系统参数，是否允许修改手术登记和麻醉登记记录。否的情况下，提示用户，终止操作，
 * 是的情况下，更新手术登记信息。
 * @Author: tangfeishuai
 * @CreateDate: 2016年4月20日
 * @param:
 * @return:
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */
function issave(){
	var isupdate=false;
	$.ajax({
		url : '<%=basePath%>operation/registration/registrationForisupdate.action',
		type:'post',
		async:false, 
		success:function(data){
			if(data=='T'){//可修改
				isupdate=true;
			}
		}
	});
	var node = $('#tDt').tree('getSelected');//获取所选节点
	if(node!=null&&node!=""){
		var parentNode=$('#tDt').tree('getParent',node.target);
		if(parentNode.attributes.pid=='noregister'){//未登记
			save();
		}else if(parentNode.attributes.pid=='register'){//已登记
			if(isupdate){
				save();
			}else{
				$.messager.alert('友情提示','该手术状态没有保存权限！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		}else{//已作废
			$.messager.alert('友情提示','该手术已作废！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}else{
		save();
	}
	
}

/**
 * @Description:保存手术登记信息
 * @Author: tangfeishuai
 * @CreateDate: 2016年4月20日
 * @param:
 * @return:
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */

//提交验证
function save() {
	var begin= $("#preDate2").val();
	var end=$("#duration").val();
	if(begin>end){
		$.messager.alert("提示","开始时间不能晚于结束时间！");
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return ;
	}
	var clinicCode=$("#clinicCode").val();//住院流水号
	var med=$("#patientNo").val();//病历号
	var opid=$("#operationno").val();//手术序号
	var checkFlag=true;
	
	/**
	 *手术名称
	 */
	var idArr = $('[id^=nssmc]');
	var itemNameStr = "";
	var itMap = new Map();
	var ssmcMap2=new Map();
	idArr.each(function(){
		
		var val = $(this).combogrid('getText');
		if(itMap.get(val)!=null&&val!=""){
			$.messager.alert("提示","手术名称不能重复！","info");
			setTimeout(function(){
				$(".messager-body").window('close');
			},2000);
			checkFlag = false;
		}
		itMap.put(val,"1");
		var idArrValue = $(this).prop('id');
		var id = idArrValue.substring(idArrValue.indexOf("_")+1);
		var newValue = $('#'+idArrValue).combogrid('getValue');
		var newText = $('#'+idArrValue).combogrid('getText');
		ssmcMap2.put(id,newValue);
		var oldValue = ssmcMap.get(id);
		if(!oldValue){
			if(newValue){
				itemNameStr+=newText+","+newValue+"_add#"//添加
			}
		}else{
			if(!newValue){
				itemNameStr+= id+"_del#"//删除
			}else{
				if(oldValue!=newValue){
					itemNameStr+= id+","+newText+","+newValue+"_upd#"//更新
				}
			}
			
		}
		
	});
	if(opid!=null&&opid!=""){
		ssmcMap.each(function(key,value,index){
			if(ssmcMap.get(key)!=null){
				if(ssmcMap2.get(key)==null){
					itemNameStr+= key+"_del#"//删除
				}
			}
		});
	}  
	/**
	 *诊断名称 诊断不能修改，故不需比较
	 */
	var dengji="";//当前状态
	var node = $('#tDt').tree('getSelected');//获取所选节点
	if(node!=null&&node!=""){
		var parentNode=$('#tDt').tree('getParent',node.target);
		dengji=parentNode.attributes.pid;
	}else{
		dengji=$("#dengji").val();
	}
		if(dengji=='noregister'){//未登记
			/**
			 *洗手护士
			 */
			var xsArr = $('[id^=xishou]');
			var washStr = "";
			var xsMap = new Map();
			xsArr.each(function(){
				var newText=$(this).combogrid("getText");
				var newValue=$(this).combogrid("getValue");
				var id=$(this).prop("id");
				var xsnum=id.substring(6,7);
				if(newValue){
					washStr+= newText+","+xsnum+","+newValue+"_add#"//添加
				}
				
			});
			/**
			 *巡回护士
			 */
			var xhArr = $('[id^=xunhui]');
			var tourStr = "";
			xhArr.each(function(){
				var newText=$(this).combogrid("getText");
				var newValue=$(this).combogrid("getValue");
				var id=$(this).prop("id");
				var xhnum=id.substring(6,7);
				if(newValue){
					tourStr+= newText+","+xhnum+","+newValue+"_add#"//添加
				}
			});
			/**
			 *助手
			 */
			var zsArr = $('[id^=zhushou]');
			var thelperStr = "";
			zsArr.each(function(){
				var newText=$(this).combobox("getText");
				var newValue=$(this).combobox("getValue");
				var id=$(this).prop("id");
				var zsnum=id.substring(7,8);
				if(newValue){
					thelperStr+=newText+","+zsnum+","+newValue+"_add#";//添加
				}
			});
			/**
			 *进修人员
			 */
			var jxArr = $('[id^=jinxiu]');
			var engageStr = "";
			jxArr.each(function(){
				var newText=$(this).combogrid("getText");
				var newValue=$(this).combogrid("getValue");
				var id=$(this).prop("id");
				var jxnum=id.substring(6,7);
				if(newValue){
					engageStr+=newText+","+jxnum+","+newValue+"_add#"//添加
				}
			});
		}else if(dengji=='register'){//已登记
			/**
			 *洗手护士
			 */
			var xsArr = $('[id^=xishou]');
			var washStr = "";
			var xsMap = new Map();
			var xishouMap2 = new Map();
			xsArr.each(function(){
				var val = $(this).combogrid('getText');
				if(xsMap.get(val)!=null&&val!=""){
					$.messager.alert("提示","洗手护士不能重复！","info");
					setTimeout(function(){
						$(".messager-body").window('close');
					},2000);
					checkFlag = false;
				}
				xsMap.put(val,"1");
				var xsArrValue = $(this).prop('id');
				var xsnum=xsArrValue.substring(6,7);
				var xsId = xsArrValue.substring(xsArrValue.indexOf("_")+1);
				var newValue = $('#'+xsArrValue).combogrid('getValue');
				var newText = $('#'+xsArrValue).combogrid('getText');
				var oldValue = xishouMap.get(xsId);
				xishouMap2.put(xsId,newValue);
				if(!oldValue){
					if(newValue){
						washStr+= newText+","+xsnum+","+newValue+"_add#"//添加
					}
				}else{
					if(!newValue){
						washStr+= xsId+"_del#"//删除
					}else{
						if(oldValue!=newValue){
							washStr+= xsId+","+newText+","+xsnum+","+newValue+"_upd#"//更新
						}
					}
				}
			});
			xishouMap.each(function(key,value,index){
				if(xishouMap.get(key)!=null){
					if(xishouMap2.get(key)==null){
						washStr+= key+"_del#"//删除
					}
				}
			})
			/**
			 *巡回护士
			 */
			var xhArr = $('[id^=xunhui]');
			var tourStr = "";
			var xhMap = new Map();
			var xunhuiMap2 = new Map();
			xhArr.each(function(){
				var val = $(this).combogrid('getText');
				
				if(xhMap.get(val)!=null&&val!=""){
					$.messager.alert("提示","巡回护士不能重复！","info");
					setTimeout(function(){
						$(".messager-body").window('close');
					},2000);
					checkFlag = false;
				}
				xhMap.put(val,"1");
				var xhArrValue = $(this).prop('id');
				var xhnum=xhArrValue.substring(6,7);
				var xhId = xhArrValue.substring(xhArrValue.indexOf("_")+1);
				var newValue = $('#'+xhArrValue).combogrid('getValue');
				var newText = $('#'+xhArrValue).combogrid('getText');
				var oldValue = xunhuiMap.get(xhId);
				xunhuiMap2.put(xhId,newValue);
				if(!oldValue){
					if(newValue){
						tourStr+= newText+","+xhnum+","+newValue+"_add#"//添加
					}
				}else{
					if(!newValue){
						tourStr+= xhId+"_del#"//删除
					}else{
						if(oldValue!=newValue){
							tourStr+= xhId+","+newText+","+xhnum+","+newValue+"_upd#"//更新
						}
					}
				}
			});
			xunhuiMap.each(function(key,value,index){
				if(xunhuiMap.get(key)!=null){
					if(xunhuiMap2.get(key)==null){
						tourStr+= key+"_del#"//删除
					}
				}
			})
			/**
			 *助手
			 */
			var zsArr = $('[id^=zhushou]');
			var thelperStr = "";
			var zsMap = new Map();
			var zhushouMap2 = new Map();
			zsArr.each(function(){
				var val = $(this).combobox('getText');
				if(zsMap.get(val)!=null&&val!=""){
					$.messager.alert("提示","医生助手不能重复！","info");
					setTimeout(function(){
						$(".messager-body").window('close');
					},2000);
					checkFlag = false;
				}
				zsMap.put(val,"1");
				var zsArrValue = $(this).prop('id');
				var zsnum=zsArrValue.substring(7,8);
				var zsId = zsArrValue.substring(zsArrValue.indexOf("_")+1);
				var newValue = $('#'+zsArrValue).combobox('getValue');
				var newText = $('#'+zsArrValue).combobox('getText');
				var oldValue = zhushouMap.get(zsId);
				zhushouMap2.put(zsId,newValue);
				if(!oldValue){
					if(newValue){
						thelperStr+=newText+","+zsnum+","+newValue+"_add#";//添加
					}
				}else{
					if(!newValue){
						thelperStr+= zsId+"_del#"//删除
					}else{
						if(oldValue!=newValue){
							thelperStr+= zsId+","+newText+","+zsnum+","+newValue+"_upd#";//更新
						}
					}
				}
			});
			zhushouMap.each(function(key,value,index){
				if(zhushouMap.get(key)!=null){
					if(zhushouMap2.get(key)==null){
						thelperStr+= key+"_del#"; //删除
					}
				}
			})
			/**
			 *进修人员
			 */
			var jxArr = $('[id^=jinxiu]');
			var engageStr = "";
			var jxMap = new Map();
			var jinxiuMap2 = new Map();
			jxArr.each(function(){
				var jxArrValue = $(this).prop('id');
				var jxnum=jxArrValue.substring(6,7);
				var val = $(this).combogrid('getText');
				var jxId = jxArrValue.substring(jxArrValue.indexOf("_")+1);
				var newValue = $('#'+jxArrValue).combogrid('getValue');
				var newText = $('#'+jxArrValue).combogrid('getText');
				var oldValue = jinxiuMap.get(jxId);
				jinxiuMap2.put(jxId,newValue);
				if(!oldValue){
					if(newValue==newText){
						if(newValue!=null&&newValue!=""){
							$.messager.alert("提示","该护士信息有误！");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							 $('#'+xsArrValue).combogrid('clear');
						}
					}else{
						engageStr+=newText+","+zsnum+","+newValue+"_add#"//添加
					}
				}else{
					if(!newValue){
						engageStr+= jxId+"_del#"//删除
					}else{
						if(newValue==newText){
							if(newValue!=null&&newValue!=""){
								$.messager.alert("提示","该护士信息有误！");
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								 $('#'+xsArrValue).combogrid('clear');
							}
						}else{
							engageStr+= jxId+","+newText+","+zsnum+","+newValue+"_upd#"//更新
						}
					}
				}
			});
			jinxiuMap.each(function(key,value,index){
				if(jinxiuMap.get(key)!=null){
					if(jinxiuMap2.get(key)==null){
						engageStr+= key+"_del#" //删除
					}
				}
			})
		}else if(dengji=="cancel"){
			$.messager.alert('友情提示','该手术已作废！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return ;
		}
	
	if(checkFlag){
		$('#Form1').form('submit',{
			url : '<%=basePath%>operation/operationrecord/saveOperationRecordInfo.action',
			onSubmit : function(param) {
				if (!$('#Form1').form('validate')) {
					$.messager.progress('close');
					$.messager.alert('提示','有信息未填写，不能保存！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}else{
					param.opapId=opid,
					param.itemNameStr=itemNameStr,
					param.washStr=washStr,
					param.tourStr=tourStr,
					param.thelperStr=thelperStr,
					param.engageStr=engageStr,
					param.isRecord='3'
					
				}
				$.messager.progress({text:'保存中，请稍后...',modal:true});
				
			},
			success:function(data){
				$.messager.progress('close');
				if(data=="error"){
					$.messager.alert('提示','保存失败，请重新填写信息！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else{
					 $.messager.alert('提示','保存成功！');
						applyDataClear();
						$('#tDt').tree('reload');
				}
				
			},
			error:function(data){
				$.messager.progress('close');
				$.messager.alert('提示','保存失败，请重新填写信息！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		});
	}
	
}	

//取消
function cancel(){
	var node = $('#tDt').tree('getSelected');//获取所选节点
	var getParent=$("#tDt").tree("getParent",node.target);
	if(node.id.length==32){
		if(getParent.attributes.pid!='register'){//未登记或作废
			$.messager.alert('友情提示','该手术状态不可取消！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}else{//已登记
			$.messager.confirm('继续','取消操作将把该手术恢复到未登记状态，是否继续？',function(r){    
			    if (r){ 
			    	$.messager.progress({text:'取消中，请稍后...',modal:true});
			    	
			    	$.ajax({
			    		url : '<%=basePath%>operation/operationrecord/cancelOperationRecordInfo.action',
			    		type:'post',
			    		data:{'opreId':node.id,'opapId':node.attributes.operationId},
			    		success:function(data){
			    			$.messager.progress('close');	
			    			if(data=="success"){
			    				$.messager.alert("提示",'操作成功！');
			    				applyDataClear();
								$('#tDt').tree('reload');
			    			}
			    		}
			    	});
			    }    
			}); 
		}
	}
}

//作废
function fundel(){
	var node = $('#tDt').tree('getSelected');//获取所选节点
	var getParent=$("#tDt").tree("getParent",node.target);
		if(getParent.attributes.pid!='register'){//未登记或作废
			$.messager.alert('友情提示','该手术状态不可作废！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}else{//已登记
			$.messager.confirm('继续','作废操作将把手术置为“作废”状态，该状态不可恢复，是否继续？',function(r){    
			    if (r){  
			    	$.messager.progress({text:'作废中，请稍后...',modal:true});
			    		
			    	$.ajax({
			    		url : '<%=basePath%>operation/operationrecord/delOperationRecordInfo.action',
			    		type:'post',
			    		data:{'opreId':node.id,'opapId':node.attributes.operationId},
			    		success:function(data){
			    			$.messager.progress('close');
			    			if(data=="success"){
			    				$.messager.alert("提示",'操作成功！');
			    				applyDataClear();
								$('#tDt').tree('reload');
			    			}
			    		}
			    	});
			    }    
			}); 
		}
}
//巡回护士信息的验证
function xunhuiName(name){
	var mc = $('[id^=xunhui]');
 	var b = 0;
	mc.each(function(index,obj){
		if($(obj).combogrid('getValue') == name&&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
			b++;
		}
	});
 	var c=b>1?true:false;
        if(!c){
     		var xs = $('[id^=xishou]');
        	var a = 0
 			xs.each(function(index,obj){
 				if($(obj).combogrid('getValue') == name &&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
 					a++;
 				}
			});
        	var  d=(a>=1)?true:false;
     	  if(!d){
	    		var xs = $('[id^=jinxiu]');
	       		var a = 0
				xs.each(function(index,obj){
					if($(obj).combogrid('getValue') ==name &&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
						a++;
					}
				});
	         	return  (a==1)?true:false;
  	       }else{
  	    	 return true;
  	       }
        }else{
  	    	   return true;
  		}
}
//洗手护士信息的验证
function xishouName(name){
	var mc = $('[id^=xishou]');
 	var b = 0;
	mc.each(function(index,obj){
		if($(obj).combogrid('getValue') == name&&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
			b++;
		}
	});
 	var c=b>1?true:false;
       if(!c){
    		var xs = $('[id^=xunhui]');
       		var a = 0
			xs.each(function(index,obj){
				if($(obj).combogrid('getValue') ==name &&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
					a++;
				}
			});
         	var d=(a>=1)?true:false;
     	  if(!d){
  	    		var xs = $('[id^=jinxiu]');
  	       		var a = 0
  				xs.each(function(index,obj){
  					if($(obj).combogrid('getValue') ==name &&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
  						a++;
  					}
  				});
  	       	   return  (a==1)?true:false;
  	         	
  	       }else{
  	    	 return true;
  	       }
       }else{
	    	   return true;
	       }
     
}
//洗手护士信息的验证
function jinxiuName(name){
	var mc = $('[id^=jinxiu]');
 	var b = 0;
	mc.each(function(index,obj){
		if($(obj).combogrid('getValue') == name&&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
			b++;
		}
	});
 	var c=b>1?true:false;
       if(!c){
    		var xs = $('[id^=xunhui]');
       		var a = 0
			xs.each(function(index,obj){
				if($(obj).combogrid('getValue') ==name &&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
					a++;
				}
			});
         	var d=(a>=1)?true:false;
     	  if(!d){
  	    		var xs = $('[id^=xishou]');
  	       		var a = 0
  				xs.each(function(index,obj){
  					if($(obj).combogrid('getValue') ==name &&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
  						a++;
  					}
  				});
  	         	return  (a==1)?true:false;
  	       }else{
  	    	 return true;
  	       }
       }else{
	    	   return true;
	       }
     
}
/**
 * @Description:提示框自动消失
 * @Author: zhangjin
 * @CreateDate: 2017年2月10日
 * @param:
 * @return:
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */
function alert_autoClose(title,msg,icon){  
	var interval;  
	var time=3500;  
	var x=1;    //设置时间2s
	$.messager.alert(title,msg,icon,function(){});  
	interval=setInterval(fun,time);  
	function fun(){  
		--x;  
		if(x==0){  
			clearInterval(interval);  
			$(".messager-body").window('close');    
		}  
	}; 
}
//手术名称信息的验证
function nssmcName(name){
	var mc = $('[id^=nssmc]');
 	var b = 0
	mc.each(function(index,obj){
		if($(obj).combogrid('getText') == name&&$(obj).combogrid('getText')!=""&&$(obj).combogrid('getText')!=null){
			b++;
		}
	});
 	return b>1?true:false;
}
function dayin(){
	var opId=$("#operationno").val().trim();
	var timerStr = Math.random();
	var node=$('#tDt').tree('getSelected');
	if(node==null){
		$.messager.alert("提示","请选择要打印的手术登记信息！");
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return ;
	}
	var tar=$('#tDt').tree('getParent',node.target);
	var target=$('#tDt').tree('getParent',tar.target);
	if(target.text!='已登记手术'){
		$.messager.alert("提示","请选择已登记的手术信息！");
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return ;
	}
	window.open("<%=basePath%>iReport/iReportPrint/iReportsstzb.action?randomId="+timerStr+"&opId="+opId+"&fileName=ssdengjibiao",'newwindow'+opId,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
}
/**  
 *  
 * @Description：过滤	
 * @Author：zhangjin
 * @CreateDate：2016-11-1
 * @version 1.0
 * @throws IOException 
 *
 */ 
function filterLocalCombobox(q, row, keys){
	if(keys!=null && keys.length > 0){//
		for(var i=0;i<keys.length;i++){ 
			if(row[keys[i]]!=null&&row[keys[i]]!=''){
					var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1;
					if(istrue==true){
						return true;
					}
			}
		}
	}else{
		var opts = $(this).combobox('options');
		return row[opts.textField].indexOf(q.toUpperCase()) > -1;
	}
}

function funcStatus(value,row,index){//状态显示值 -->
	if (value){ 
		return value; 
	} else {
		return "无状态"; 
	} 
} 
/**
 * @Description:清空查询条件
 */	
	function clearQuery(){
		$("#medicalrecordId").textbox("clear");
		$("#beganTime").val("");
		$("#endTime").val("");
		applyDataClear();
		$('#tDt').tree('reload');
 	}
 
</script>
<script type="text/javascript">
//hedong 20170322  开始时间  结束时间 更改日期控件后重新绑定事件
function preDate2Fun(){
	var beg=$('#preDate2').val().trim();
	var end=$('#duration').val().trim();
	if(beg!=null&&beg!=""){
		if(end!=null&&end!=""){
			if(end<beg){
				$.messager.alert("提示","开始时间不能大于结束时间！","info");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3000);
				$('#preDate2').val("");
				return ;
			}
		}
	}
}
//hedong 20170322  开始时间  结束时间 更改日期控件后重新绑定事件
function durationFun(){
	var beg=$('#preDate2').val().trim();
	var end=$('#duration').val().trim();
	if(end!=null&&end!=""){
		if(beg!=null&&end!=""){
			if(end<beg){
				$.messager.alert("提示","结束时间不能小于开始时间！","info");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3000);
				$('#duration').val("");
				return ;
			}
		}
	}
}

/**
 * @Description：提取扩大复选框范围的方法
 * @Author：houzq
 * @CreateDate：2017-4-5
 */
var i = '';
function kdfw(id){
	$('#'+id).prop('checked')==true?$('#'+id).prop('checked',false):$('#'+id).prop('checked',true);
	i = id;
	setTimeout('triggerclick()',500);
}
function triggerclick(){
	var a = $('#'+i).attr('onclick');
	eval(a);
}



</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>	

<body style="margin: 0px;padding: 0px;"> 
   <div id="cc" class="easyui-layout" style="width:100%;height:100%">  
	   <div id="buth" data-options="region:'north',title:'',split:false" style="height:40px;padding-top: 5px;padding-left:5px;border-top:0">
			 <div style="text-align: left;width: 600px"  data-options="split:true">
				<shiro:hasPermission name="${menuAlias}:function:save">
					<a href="javascript:issave()" class="easyui-linkbutton"  data-options="iconCls:'icon-save'">保存</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:cancel">
					<a href="javascript:cancel()" class="easyui-linkbutton"  data-options="iconCls:'icon-remove'">取消</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:nullify">
					<a href="javascript:fundel()" class="easyui-linkbutton"  data-options="iconCls:'icon-cancellation'">作废</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:print">
					<a href="javascript:dayin()" class="easyui-linkbutton"  data-options="iconCls:'icon-2012081511202'" >打印</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:queryPatient()" class="easyui-linkbutton"  data-options="iconCls:'icon-search'">查询</a>
				</shiro:hasPermission>
				 <shiro:hasPermission name="${menuAlias}:function:readCard"> 
				<a href="javascript:void(0)"  class="easyui-linkbutton read_medical_card" type_id="read_medical_cardID" id="read_medical_cardID" type_value="read_medical_card" cardNo="" data-options="iconCls:'icon-bullet_feed'">读卡</a>
				</shiro:hasPermission>
				   <shiro:hasPermission name="${menuAlias}:function:readIdCard"> 
				   		<a href="javascript:void(0)"  class="easyui-linkbutton read_identity" type_id="read_identityID" id="read_identityID" type_value="read_identity" cardNo="" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
				</shiro:hasPermission> 
				<a href="javascript:clearQuery();"  class="easyui-linkbutton" iconCls="reset">重置</a>
			</div>
	</div>
	<div data-options="region:'west',split:true" style="width:300px; padding: 10px">
		<div id="mm" class="easyui-layout" style="width:100%;height:100%" data-options="fit:true">
			<div data-options="region:'north',split:false,border:false" style="height:110px;width: 200px;overflow-y:hidden">
				<div style="padding: 5px 5px 5px 5px;">
					<input type="hidden" id="dengji">
						<table style="width:100%;border:1px solid #95b8e7;padding:5px;" class="changeskin registrationListSize">
							<tr>
								<td style="text-align: right;" nowrap="true" >病 历 号：</td>
								<td><input id="medicalrecordId" class="easyui-textbox" style="width: 145px" data-options="prompt:'输入病历号回车查询'"/></td>
							</tr>
							<tr>
								<td style="text-align: right;" nowrap="true" >开始时间：</td>
								<td>
								<input id="beganTime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:145px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
							</tr>
							<tr>
								<td style="text-align: right;" nowrap="true" >结束时间：</td>
								<td>
								<input id="endTime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:145px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
							</tr>
						</table>
				</div>
			</div>
			<div data-options="region:'center',border:false" >
				<div id="treeDiv" style="width: 100%;">
					<ul id="tDt">加载中，请稍等...</ul>
				</div>
			</div> 
		</div>
	</div> 
	  
	<div data-options="region:'center',split:false" title="手术信息" >	
		<div id="ll" class="easyui-layout" style="width:100%;height:100%;" >   
			<div data-options="region:'north',split:false,border:false" style="height:110px;width: 100%">
				<div style="padding-left: 5px" >
					<table id="table" style="width:100%;">
						<tr height="30px">
							<td style="white-space: nowrap;">
								住院号：
							</td>
							<td id='patientNo' style="width:19%">
							<input type='hidden' id='reCordpatientNo' name='operRecord.patientNo'/>
							</td>
							<td style="white-space: nowrap;">
								<input type="hidden" id="id" name="id" />
								<input type='hidden' id='operationno' name='operationno'/>
								<input type='hidden' id='clinicCode' name='clinicCode'/>
								姓名：
							</td>
							<td id='name' style="width:19%">
								<input type='hidden' id='reCordname' name='operRecord.name'/>
							</td>
							<td style="white-space: nowrap;">
								性别：
							</td>
							<td id='sex' style="width:19%">
								<input type='hidden' id='reCordsexCode' name='operRecord.sexCode'/>
							</td>
							<td style="white-space: nowrap;">
								出生日期：
							</td>
							<td id='birthDay' style="width:19%">
								<input type='hidden' id='reCordbirthDay' name='operRecord.birthDay'/>
							</td>
						</tr>
						<tr height="30px">
							<td style="white-space: nowrap;">
								年龄：
							</td>
							<td id='age' ></td>
							<td style="white-space: nowrap;">
								科室：
							</td>
							<td id='inDept'>
								<input type='hidden' id='reCorddeptCode' name='operRecord.deptCode'/>
							</td>
							<td style="white-space: nowrap;">
								病床：
							</td>
							<td id='bedNo' >
								<input type='hidden' id='reCordbedNo' name='operRecord.bedNo' />
							</td>
							<td style="white-space: nowrap;">
								申请医生：
							</td>
							<td id='applyDoctor' >
								<input type='hidden' id='reCordapplyDocd' name='operRecord.applyDocd'/>
							</td>
						</tr>
						<tr height="30px">
							<td style="white-space: nowrap;">
								类别：
							</td>
							<td id='paykindCode' ></td>
							<td style="white-space: nowrap;">
								余额：
							</td>
							<td id='prepayCost' ></td>
							<td >
								
							</td>
							<td ></td>
							<td >
								
							</td>
							<td ></td>
						</tr>
					</table>
					<hr/>
				</div>
    		</div>
  			<div data-options="region:'center',split:false,border:false" >
				<form id="Form1" method="post" style="padding-left: 5px">
					<table   id='sq' style="width: 100%;height: 100%" >
						<tr id="trRoom">
							<td style="white-space: nowrap;">手术房间：</td>
							<td style="width:19%">
								<a href="javascript:void(0)" title="选择手术房间前请先选择执行科室" data-options="position:'left'" class="easyui-tooltip">
								<input class="easyui-combobox" id="opRoom" 
								name="operRecord.roomId" readonly="readonly" /> </a></td>
							<td style="white-space: nowrap;">是否自定义手术：</td>
							<td >
								<div align="center" style="float:left;width: 7%;height: 100%;padding-top: 5px" >
								<input type="checkbox" id="operation" onclick="ssmcdy()"/>
							</div>
							<div style="float:left;width:93%;height: 100%" onclick="kdfw('operation')"></div>
							</td>
						</tr>
						<tr id='trsqzd0'>
							<td style="white-space: nowrap;">术前诊断1：</td>
							<td  colspan="7"><input class="easyui-combogrid" style="width: 90%;" id="shoushuzd0_" name="diagName0" data-options='required:true' readonly="readonly"/>
							</td>
						</tr>
						<tr id='trndss0'>
							<td style="white-space: nowrap;">手术名称1：<input id="mc0" type="hidden"></td>
							<td  colspan="7"><input class="easyui-combogrid" id="nssmc0_" style="width: 90%"
								name="itemName0" data-options="required:true"/> 
								<a href="javascript:void(0)" id="anssmc0" onclick="add('手术名称','trndss','0','nssmc','itemName','anssmc')" class="easyui-linkbutton" data-options="iconCls:'icon-add'"></a>
								</td>
						</tr>
						<tr>
							<td style="white-space: nowrap;">麻醉类型：</td>
							<td ><input id="anesType" name="operRecord.anesType" class="easyui-combobox"/></td>
							<!-- <td  style="text-align: right;width: 15%">麻醉方式：</td>
							<td style="width: 15%" ><input id="aneWay" name="aneWay" class="easyui-combobox"/></td> -->
							<td style="white-space: nowrap;">手术规模：</td>
							<td ><input id="degree" name="operRecord.degree" class="easyui-combobox"/></td>
							<td style="white-space: nowrap;">手术分类：</td>
							<td ><input id="opType" name="operRecord.opsKind"  class="easyui-combobox" size="20"/></td>
							<td style="width:white-space: nowrap;">手术台类型：</td>
							<td ><input id="consoleType" name="operRecord.consoleType" class="easyui-combobox"/>
							</td>
						</tr>
						<tr>
							<td style="white-space: nowrap;">指导医生：</td>
							<td ><input id="guiDocd" name="operRecord.guiDocd" class="easyui-combobox" size="20"/></td>
							<td style="white-space: nowrap;">审批医生：</td>
							<td ><input id="approveDocd" name="operRecord.approveDocd" class="easyui-textbox"/></td> 
							<td style="white-space: nowrap;">手术医生：</td>
							<td ><input id="ssysbm" name="operRecord.opsDocd" class="easyui-combobox" data-options="required:true"/></td>
							<td style="white-space: nowrap;">手术医生科室：</td>
							<td ><input id="docDpcd" name="operRecord.docDpcd" class="easyui-combobox" data-options="required:true"/>
							</td>
						</tr>
						<tr class="registrationListSize1">
							<td style="white-space: nowrap;">开始时间：</td>
							<td>
							<input id="preDate2" name="operRecord.enterDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:preDate2Fun})"  style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td style="white-space: nowrap;">结束时间：</td>
							<td>
							<input id="duration" name="operRecord.outDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:durationFun})"  style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td> 
							<td style="white-space: nowrap;">执行科室：</td>
							<td >
								<input id="execDept" name="operRecord.execDept" class="easyui-textbox"/>
							</td>
							<td style="white-space: nowrap;">房号：</td>
							<td ><input id="bedhouse" name="bedhouse" class="easyui-textbox"/></td> 
						</tr>
					
						<tr>
							<td style="white-space: nowrap;">血型：</td>
							<td ><input id="bloodCode" name="operRecord.bloodCode"  class="easyui-textbox" /></td>
							<td style="white-space: nowrap;">血液成分：</td>
							<td ><input id="bloodType" name="operRecord.bloodType"  class="easyui-textbox"/></td> 
							<td style="white-space: nowrap;">用血量：</td>
							<td ><input id="bloodNum" name="operRecord.bloodNum" class="easyui-numberbox" /></td>
							<td style="white-space: nowrap;">单位：</td>
							<td ><input id="bloodUnit" name="operRecord.bloodUnit" class="easyui-textbox" /></td>
						</tr>
						<tr>
							<td style="white-space: nowrap;">术前血压：</td>
							<td ><input id="forepress" name="operRecord.forepress"  data-options="prompt:'例如100-120'" class="easyui-textbox" /></td>
							<td style="white-space: nowrap;">术后血压：</td>
							<td ><input id="steppress" name="operRecord.steppress"   data-options="prompt:'例如100-120'" class="easyui-textbox"/></td> 
							<td style="white-space: nowrap;">术前脉搏：</td>
							<td ><input id="forepulse" name="operRecord.forepulse" class="easyui-numberbox" /></td>
							<td style="white-space: nowrap;">术后脉搏：</td>
							<td ><input id="steppulse" name="operRecord.steppulse" class="easyui-numberbox" /></td>
						</tr>
						<tr>
							<td style="white-space: nowrap;">褥疮数量：</td>
							<td ><input id="scarNum" name="operRecord.scarNum" class="easyui-numberbox" size="20"/></td>
							<td style="white-space: nowrap;">输液量：</td>
							<td ><input id="transfusionQty" name="operRecord.transfusionQty" class="easyui-numberbox"/></td> 
							<td style="white-space: nowrap;">标本数量：</td>
							<td ><input id="sampleQty" name="operRecord.sampleQty" class="easyui-numberbox" /></td>
							<td style="white-space: nowrap;">引流管个数：</td>
							<td ><input id="guidtubeNum" name="operRecord.guidtubeNum" class="easyui-numberbox" /></td>
						</tr>
						<tr>
						<td style="white-space: nowrap;">抽血次数：</td>
							<td ><input id="letBlood" name="operRecord.letBlood" class="easyui-numberbox" size="20"/></td>
							<td style="white-space: nowrap;">静注次数：</td>
							<td ><input id="mainLine" name="operRecord.mainLine" class="easyui-numberbox"/></td> 
							<td style="white-space: nowrap;">肌注次数：</td>
							<td ><input id="muscleLine" name="operRecord.muscleLine" class="easyui-numberbox" /></td>
							<td style="white-space: nowrap;">输液次数：</td>
							<td ><input id="transfusion" name="operRecord.transfusion" class="easyui-numberbox" /></td>
						</tr>
						<tr id='tr'>
							<td style="white-space: nowrap;">输氧次数：</td>
							<td ><input id="transoxyen" name="operRecord.transoxyen" class="easyui-numberbox" size="20"/></td>
							<td style="white-space: nowrap;">导尿次数：</td>
							<td ><input id="stale" name="operRecord.stale" class="easyui-numberbox"/></td> 
							<td style="white-space: nowrap;">切口类型：</td>
							<td  colspan="3"><input id="inciType" name="operRecord.inciType" class="easyui-textbox"/></td>
						</tr>
						<tr id='trzs0' >
							<td style="white-space: nowrap;">助手1：</td>
							<td ><input id="zhushou0" name="thelper" class="easyui-combobox"/>
							</td>
							<td style="white-space: nowrap;">洗手护士1： <input id="xs0" type="hidden"></td>
							<td ><input id="xishou0" name="wash" class="easyui-combogrid"/>
							</td>
						    <td style="white-space: nowrap;">巡回护士1： <input id="xh0" type="hidden"></td>
							<td ><input id="xunhui0" name="tour" class="easyui-combogrid"/>
							</td>
						    <td style="white-space: nowrap;">进修人员1： <input id="jx0" type="hidden"></td>
							<td ><input id="jinxiu0" name="engage" class="easyui-combogrid"/>
							<a href="javascript:void(0)" id="afz0" onclick="addfz('trzs','0','afz')" class="easyui-linkbutton" data-options="iconCls:'icon-add'"></a>
							</td>
						</tr>
						<tr >
							<td style="white-space: nowrap;">术前是否清醒：</td>
							<td >
								<div align="center" style="float:left;width: 7%;height: 100%;padding-top: 8px" >
									<input type="checkbox" id="foreynsober"  onclick="isforeynsober()"/>
							    	<input type="hidden" name="operRecord.foreynsober" id="foreynsober2"/>
								</div>
								<div style="float:left;width:93%;height: 100%" onclick="kdfw('foreynsober')"></div>
						    </td>
							<td style="white-space: nowrap;">术后是否清醒：</td>
							<td >
								<div align="center" style="float:left;width: 7%;height: 100%;padding-top: 8px" >
									<input type="checkbox" id="stepynsober"  onclick="isstepynsober()"/>
							    	<input type="hidden" name="operRecord.stepynsober" id="stepynsober2"/>
								</div>
								<div style="float:left;width:93%;height: 100%" onclick="kdfw('stepynsober')"></div>
						    </td> 
							<td style="white-space: nowrap;">是否危重：</td>
							<td >
								<div align="center" style="float:left;width: 7%;height: 100%;padding-top: 8px" >
									<input type="checkbox" id="danger"  onclick="isdanger()"/>
							    	<input type="hidden" name="operRecord.danger" id="danger2"/>
								</div>
								<div style="float:left;width:93%;height: 100%" onclick="kdfw('danger')"></div>
						    </td>
							<td style="white-space: nowrap;">是否隔离：</td>
							<td >
								<div align="center" style="float:left;width: 7%;height: 100%;padding-top: 8px" >
									<input type="checkbox" id="seperate"  onclick="isseperate()"/>
							    	<input type="hidden" name="operRecord.seperate" id="seperate2"/>
								</div>
								<div style="float:left;width:93%;height: 100%" onclick="kdfw('seperate')"></div>
							</td>
						</tr>
						<tr >
							<td style="white-space: nowrap;">是否收费：</td>
							<td >
								<div align="center" style="float:left;width: 7%;height: 100%;padding-top: 5px" >
									<input type="checkbox" id="ynfee"  onclick="isynfee()"/>
							    	<input type="hidden" name="operRecord.ynfee" id="ynfee2"/>
								</div>
								<div style="float:left;width:93%;height: 100%" onclick="kdfw('ynfee')"></div>
						    </td>	
							<td style="white-space: nowrap;">是否感染：</td>
							<td >
								<div align="center" style="float:left;width: 7%;height: 100%;padding-top: 5px" >
									<input type="checkbox" id="question"  onclick="isquestion()"/>
							    	<input type="hidden" name="operRecord.question" id="question2"/>
								</div>
								<div style="float:left;width:93%;height: 100%" onclick="kdfw('question')"></div>
							</td>
							<td style="white-space: nowrap;">是否有菌：</td>
						    <td  colspan="3">
						    <div align="center" style="float:left;width: 2%;height: 100%;padding-top: 5px" >
							    <input type="checkbox" id="germ"  onclick="isgerm()"/>
						    	<input type="hidden" name="operRecord.yngerm" id="germ2"/>
							</div>
							<div style="float:left;width:93%;height: 100%" onclick="kdfw('germ')"></div>
							</td>
						</tr>
					
						<tr>
							<td style="white-space: nowrap;">备注：</td>
							<td colspan="7">
								<textarea rows="4" cols="32" id="remark" name="operRecord.remark"
									data-options="multiline:true" maxlength="50" style="width:92%;font-size:14px;">
								</textarea>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="8">
							<shiro:hasPermission name="${menuAlias}:function:save">
							<a href="javascript:issave()" class="easyui-linkbutton" style="margin:0 0 0 1% " data-options="iconCls:'icon-save'">保存</a>
							</shiro:hasPermission>
							<a href="javascript:applyData2Clear()" class="easyui-linkbutton" style="margin:0 0 0 1% " data-options="iconCls:'icon-clear'">清空</a>
							</td>
						</tr>
					</table>
				</form> 
  		 	</div>
    	</div>
	</div>
	</div> 
	<div id="operationRegistration" title='患者信息' class="easyui-window" align="center" data-options="modal:true,collapsible:false,minimizable:false,maximizable:false,closed:true" style="width:1000px;height:600px">
		<div style="height: 30px;padding:3px 5px 3px 5px;text-align: left;" data-options="region:'north',split:false" >
			<input class="easyui-combobox"  id="typeNo"  value="0"  />
			<input class="easyui-textbox"  id="questionName"  type="text" style="width: 500px;" />
			<a href="javascript:queryPatient()" class="easyui-linkbutton"  data-options="iconCls:'icon-search'">查询</a>
		</div>
		<div data-options="region:'center'"  style="height: 528px;">
			<table id="dg" class="easyui-datagrid"  data-options="fitColumns:true,fit:true" >   
				<thead>
					<tr>
						<th data-options="field:'opID',width:30,align:'center',hidden:true">手术序列号</th>
						<th data-options="field:'name',width:150,align:'center',resizable:true">姓名</th>
						<th data-options="field:'itemName',width:300,align:'center',resizable:true">手术名称</th>
						<th data-options="field:'employeeName',width:100,align:'center',resizable:true">手术医生</th>
						<th data-options="field:'predate',width:150,align:'center',resizable:true,sortable:true">预约时间</th>
						<th data-options="field:'status',width:80,align:'center',resizable:true," formatter="funcStatus">状态</th>
						<th data-options="field:'patientNO',width:50,align:'center',hidden:true"></th>
						<th data-options="field:'pasource',width:30,align:'center',hidden:true"></th>
						<th data-options="field:'clinicCode',width:50,align:'center',hidden:true"></th>
					</tr>
				</thead>
			</table>  
		</div>   
	</div>   
</body>

</html>