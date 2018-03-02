<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>手术申请</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/themes/system/css/loader.css">
	<%--hedong扩大复选框  20170307--%>
	<style type="text/css">
		input[type=checkbox] {
		  -ms-transform: scale(1.35); /* IE */
		  -moz-transform: scale(1.35); /* FireFox */
		  -webkit-transform: scale(1.35); /* Safari and Chrome */
		  -o-transform: scale(1.35); /* Opera */
	   }
	   body,html{
	   	width: 100%;
	   	height: 100%;
	   }
	   *{
	   	padding: 0;
	   	margin: 0;
	   }
	</style>
	<%@ include file="/common/metas.jsp" %>
	<script type="text/javascript">
	//遮罩层
	$(window).load(function(){
	    $('body').addClass('loaded');
	    $('#loader-wrapper .load_title').remove();
	});
	var nurseMap=new Map();//护士翻页渲染
	var diagMap=new Map();//诊断翻页渲染
	var operatNameMap=new Map();//手术名称翻页渲染
	var payMap=new Map();//结算类别渲染
	$(document).ready(function(){
	
	var name="${cname}";//当前登陆医生
	operApp="${name}";//申请限制时间
	if(operApp){
		$("#tdAppDate").text("要求在"+operApp+"前发送手术申请，否则将使用接台。");
	}
	/**  
	 *  
	 * @Description：患者信息树
	 * @Author：zhangjin
	 * @CreateDate：2016-4-28
	 * @version 1.0
	 * @throws IOException 
	 *
	 */ 
	$('#tDt').tree({
			url : '<%=basePath%>operation/operationapplycation/queryRegister.action',
			method : 'get',
			animate : true,
			lines : true,
			formatter : function(node) {//统计节点总数
				var s = node.text;
				if (node.children) {
					s += '&nbsp;<span style=\'color:blue\'>('
							+ node.children.length + ')</span>';
				}
				return s;
			},
			onBeforeCollapse:function(node){
				if(node.id=="1"){
					return false;
				}
				
			},onDblClick : function(node) {//点击节点
				var id=node.id;
				b=id;
				operId="";
				huanzhexinxi();//患者信息
				qingss();
				shoushuxinxi();//获取手术信息
			}
	});
	//审批按钮
 	$(document).ready(function(e){
 		$("#shenpi").click(function(e){
 	 		$("#spxx").toggle();
	 	});
 	});
	 //助手数量控制
	 $("#helperNum").numberbox("textbox").bind('keyup',function(event){
		 var hnum=$('#helperNum').numberbox("getText");
		var zs=$('[id^=zhushou]');
		 var reg = /^[1-9]\d*$/;
		zs.each(function(){
			var id=$(this).prop('id');
			var num=id.substring(7,8);
			if(parseInt(num)+1>hnum){
				$(this).combobox("readonly",true);
				$(this).combobox('disable');
			}else{
				if(!reg.test(hnum)){
					$(this).combobox("readonly",true);
					$(this).combobox('disable');
				}else{
					$(this).combobox("readonly",false);
					$(this).combobox('enable');
				}
			}
		});
	});
	 //巡回数量控制
	 $("#prepNurse").numberbox("textbox").bind('keyup',function(event){
		 var hnum=$('#prepNurse').numberbox("getText");
		var xunhui=$('[id^=xunhui]');
		 var reg = /^[1-9]\d*$/;
		xunhui.each(function(){
			var id=$(this).prop('id');
			var num=id.substring(6,7);
			if(parseInt(num)+1>hnum){
				$(this).combogrid("readonly",true);
				$(this).combogrid('disable');
			}else{
				if(!reg.test(hnum)){
					$(this).combogrid("readonly",true);
					$(this).combogrid('disable');
				}else{
					$(this).combogrid("readonly",false);
					$(this).combogrid('enable');
				}
				
			}
		});
		
	});
	 //洗手数量控制
	 $("#washNurse").numberbox("textbox").bind('keyup',function(event){
		 var hnum=$('#washNurse').numberbox("getText");
		var zs=$('[id^=xishou]');
		 var reg = /^[1-9]\d*$/;
		zs.each(function(){
			var id=$(this).prop('id');
			var num=id.substring(6,7);
			if(parseInt(num)+1>hnum){
				$(this).combogrid("readonly",true);
				$(this).combogrid('disable');
			}else{
				if(!reg.test(hnum)){
					$(this).combogrid("readonly",true);
					$(this).combogrid('disable');
				}else{
					$(this).combogrid("readonly",false);
					$(this).combogrid('enable');
				}
			}
		});
	});
	 bindEnterEvent('medicalrecord',query,'easyui');
 /**  
	 *  
	 * @Description：获取员工
	 * @Author：zhangjin
	 * @CreateDate：2016-4-28
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	 $.ajax({
	 		url: '<%=basePath %>operation/operationList/getEmployee.action',
	 		type:'post',
	 		success: function(data) {
	 			empMap = data;
	 		}
	 	});
	 /**  
		 *  
		 * @Description：获取科室
		 * @Author：zhangjin
		 * @CreateDate：2016-4-28
		 * @version 1.0
		 * @throws IOException 
		 *
		 */
	 $.ajax({
	 	url: "<%=basePath%>operation/operationList/querydeptComboboxs.action", 
	 	type:'post',
	 	success: function(deptData) {
	 		deptMapq = deptData;
	 	}
	 });
	 /**  
		 *  
		 * @Description：获取医生
		 * @Author：zhangjin
		 * @CreateDate：2016-4-28
		 * @version 1.0
		 * @throws IOException 
		 *
		 */
		$.ajax({
			 url : '<%=basePath %>operation/operationList/ssComboboxList.action',
			 type:"post",
			 async:false,
			 success:function(data){
				 user=data
			 }
		});
		/**  
		 *  
		 * @Description：获取结算类别
		 * @Author：zhangjin
		 * @CreateDate：2016-4-28
		 * @version 1.0
		 * @throws IOException 
		 *
		 */
		$.ajax({
 		    url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=paykind",
 			type:'post',
 			success: function(data) {
 				var type = data;
 				for(var i=0;i<type.length;i++){
 					payMap.put(type[i].encode,type[i].name);
 				}
 			}
 		});
 		sexMap=new Map();
 		/**  
 		 *  
 		 * @Description：获取性别
 		 * @Author：zhangjin
 		 * @CreateDate：2016-4-28
 		 * @version 1.0
 		 * @throws IOException 
 		 *
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
 		
		/**  
 		 *  
 		 * @Description：获取所有护士
 		 * @Author：zhangjin
 		 * @CreateDate：2016-4-28
 		 * @version 1.0
 		 * @throws IOException 
 		 *
 		 */
		$.ajax({
			url : '<%=basePath %>operation/operationList/ssSysEmployeeList.action',
			type:'post',
			success: function(data) {
				var v = data;
				for(var i=0;i<v.length;i++){
					nurseMap.put(v[i].jobNo,v[i].name);
				}
			}
		});
		/**  
 		 *  
 		 * @Description：获取诊断信息
 		 * @Author：zhangjin
 		 * @CreateDate：2016-4-28
 		 * @version 1.0
 		 * @throws IOException 
 		 *
 		 */
		$.ajax({
			url : '<%=basePath %>operation/operationList/icdCombobox2.action',
			type:'post',
			success: function(data) {
				var v = data;
				for(var i=0;i<v.length;i++){
					diagMap.put(v[i].code,v[i].name);
				}
			}
	  	});
 				
		/**  
 		 *  
 		 * @Description：获取手术名称信息
 		 * @Author：zhangjin
 		 * @CreateDate：2016-4-28
 		 * @version 1.0
 		 * @throws IOException 
 		 *
 		 */
		$.ajax({
			url : '<%=basePath %>operation/operationList/undrugCombobox.action',
			type:'post',
			success: function(data) {
				var v = data;
				for(var i=0;i<v.length;i++){
					operatNameMap.put(v[i].code,v[i].name);
				}
			}
	  	});
		 /**  
		  *  
		  * @Description：巡回护士1
		  * @Author：zhangjin
		  * @CreateDate：2016-4-28
		  * @version 1.0
		  * @throws IOException 
		  *
		  */ 
		 $('#xunhui0_').combogrid({
		 		url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
		 		idField : 'jobNo',
		 		textField : 'name',
		 		mode:"remote",
		 		panelAlign:'right',
		 		panelWidth:325,
		 		editable : true,
		 		pageList:[10,20,30,40,50],
				 pageSize:"10",
				 validType:'text',
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
			    },
			    onLoadSuccess: function () {
			    	var id=$(this).prop("id");
			       if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
		            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
		            } 
		        }
		 	});

		 /**  
		  *  
		  * @Description：拟手术名称1
		  * @Author：zhangjin
		  * @CreateDate：2016-4-28
		  * @version 1.0
		  * @throws IOException 
		  *
		  */ 
		 $('#nssmc0_').combogrid({
		 	url : '<%=basePath %>operation/operationList/undrugComboboxfy.action',
		 	idField : 'code',
		 	textField : 'name',
		 	multiple : false,
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
		 	         {field:'undrugInputcode',title:'自定义码',width:'20%'}
		 	     ]],  
		  	onHidePanel:function(none){
		  	   var val = $(this).combogrid('getValue');
		  	   var name = $(this).combogrid('getText');
			   if(nssmcName(name)){
	    		$(this).combogrid("clear");
		    	$.messager.alert("提示","手术名称不能重复!");
		    	setTimeout(function(){
					$(".messager-body").window('close');
				},2000);
		    	
		    }
	     	if(name==val&&name!=null&&name!=""){
	     		if(!$('#operation').is(':checked')){
	     	    	$.messager.confirm('提示','该条信息在手术名称下拉表格中不存在，是否更改为自定义手术？',function(r){
	     	    		if(r){
	     	    			$("#operation").prop("checked",true);
	     	    			ssmcdy();
	     	    		}
	     	    	})
	     		}
	     	}
		  	},
	    onLoadSuccess: function () {
	    	var id=$(this).prop("id");
	       if ($("#"+id).combogrid('getValue')&&operatNameMap.get($("#"+id).combogrid('getValue'))) {
            	$("#"+id).combogrid('setText',operatNameMap.get($("#"+id).combogrid('getValue')));
            } 
        } 
		 });
		 /**  
		  *  
		  * @Description：麻醉方式
		  * @Author：zhangjin
		  * @CreateDate：2016-4-28
		  * @version 1.0
		  * @throws IOException 
		  *
		  */ 
		 $('#aneWay').combobox({
		 	url : '<%=basePath %>operation/operationList/likeAneway.action',
		 	valueField : 'encode',
		 	textField : 'name',
		 	multiple : false,
		 	editable :false
		 });
		 	
		 /**  
		  *  
		  * @Description： 手术台类型
		  * @Author：zhangjin
		  * @CreateDate：2016-4-28
		  * @version 1.0
		  * @throws IOException 
		  *
		  */
		  $('#consoleType').combobox({
		 	url : '<%=basePath %>operation/operationList/CodeconsoleType.action',
		  	valueField : 'encode',
		  	textField : 'name',
		  	multiple : false,
		  	editable : false
		  });

		 /**  
			 *  
			 * @Description：家属关系
			 * @Author：zhangjin
			 * @CreateDate：2016-4-28
			 * @version 1.0
			 * @throws IOException 
			 *
			 */
			$("#relaCode").combobox({
				url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=relation",
				valueField:'encode',    
			    textField:'name',
		    	editable : true,
			});
		 
		 /**  
		  *  
		  * @Description：手术室
		  * @Author：zhangjin
		  * @CreateDate：2016-4-28
		  * @version 1.0
		  * @throws IOException 
		  *
		  */
		  $('#execDept').combobox({
		 	url : '<%=basePath %>operation/operationList/querysysDeptmentShi.action',
		  	valueField : 'deptCode',
		  	textField : 'deptName',
		  	multiple : false,
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
		  	onSelect:function(){
	 		var opem=$(this).combobox("getValue");
			if(opem!=""&&opem!=null){
				var none=$("#nishoushu").val();
				if(none){
					var da=none.substring(0,10);
					 $.ajax({
						 url:"<%=basePath%>operation/operationList/querydate.action",
						 data:{cnow:da,dept:opem},
						 type:"post",
						 success:function(data){
							 if(data=="1"){
								$("#consoleType").combobox("select","02");
							 }else if(data=="2"){
								 $("#consoleType").combobox("select","01");
							 }else{
								 $("#consoleType").combobox('setValue','');
								 $.messager.alert("提示","该手术室没有该科室的手术台");
								 setTimeout(function(){
										$(".messager-body").window('close');
									},3000);
							 }
						 }
					 });
				}
			}
	 	},
		  	filter:function(q,row){//hedong 20170309 增加科室过滤
				 var keys = new Array();
				 keys[keys.length] = 'deptCode';//部门名称
				 keys[keys.length] = 'deptName';//部门名称
				 keys[keys.length] = 'deptPinyin';//部门拼音
				 keys[keys.length] = 'deptWb';//部门五笔
			     keys[keys.length] = 'deptInputCode';//自定义码
				return filterLocalCombobox(q, row, keys);
			}
		  });
		  
		 /**  
		  *  
		  * @Description：术者科室
		  * @Author：zhangjin
		  * @CreateDate：2016-4-28
		  * @version 1.0
		  * @throws IOException 
		  *
		  */ 
		 $('#opDoctordept').combobox({
		 	url : '<%=basePath %>operation/operationList/querysysDeptmentkeshi.action',
		 	valueField : 'deptCode',
		 	textField : 'deptName',
		 	mode:'local',
		 	multiple : false,
		 	editable : true,
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
		 		keys[keys.length] = 'deptName';
		 		keys[keys.length] = 'deptPinyin';
		 		keys[keys.length] = 'deptWb';
		 		keys[keys.length] = 'deptInputcode';
		 		keys[keys.length] = 'deptCode';
		 		return filterLocalCombobox(q, row, keys);
		 	}
		 });
		 /**  
		  *  
		  * @Description：切口类型
		  * @Author：zhangjin
		  * @CreateDate：2016-4-28
		  * @version 1.0
		  * @throws IOException 
		  *
		  */
		 $("#inciType").combobox({
		 	url:"<%=basePath%>operation/operationList/queryCodeIncitype.action",
		 	valueField:'encode',    
		     textField:'name',
		    	editable : false
		 });


		 /**  
		  *  
		  * @Description： 手术分类
		  * @Author：zhangjin
		  * @CreateDate：2016-4-28
		  * @version 1.0
		  * @throws IOException 
		  *
		  */
		 $("#opType").combobox({
		 	url:"<%=basePath%>operation/operationList/queryCodeOperatetype.action",
		 	valueField:'encode',    
		     textField:'name',
		    	editable : false
		 });

		 /**  
		  *  
		  * @Description： 手术体位
		  * @Author：zhangjin
		  * @CreateDate：2016-4-28
		  * @version 1.0
		  * @throws IOException 
		  *
		  */
		 $("#opertionposition").combobox({
		 	url:"<%=basePath%>operation/operationList/queryOperationPosition.action",
		 	valueField:'encode',    
		     textField:'name',
		     editable : false
		 });

		  /**  
		  *  
		  * @Description： 手术规模
		  * @Author：zhangjin
		  * @CreateDate：2016-4-28
		  * @version 1.0
		  * @throws IOException 
		  *
		  */
		 $("#degree").combobox({
		 	url:"<%=basePath%>operation/operationList/queryCodeScaleofoperation.action",
		 	valueField:'encode',    
		     textField:'name',
		    	editable : false
		 });

		  /**  
		  *  
		  * @Description： 感染类型
		  * @Author：zhangjin
		  * @CreateDate：2016-4-28
		  * @version 1.0
		  * @throws IOException 
		  *
		  */
		 $("#infectType").combobox({
		 	url:"<%=basePath%>operation/operationList/queryCodeInfecttype.action",
		 	valueField:'encode',    
		     textField:'name',
		    editable : true
		 }); 
		  
		  /**  
		  *  
		  * @Description：麻醉类型
		  * @Author：zhangjin
		  * @CreateDate：2016-4-28
		  * @version 1.0
		  * @throws IOException 
		  *
		  */
		 $("#anesType").combobox({
		 	url:"<%=basePath%>operation/operationList/queryCodeanesType.action",
		 	valueField:'encode',    
		    textField:'name',
		    editable : true
		 }); 
		 /**  
		  *  
		  * @Description：洗手护士1
		  * @Author：zhangjin
		  * @CreateDate：2016-4-28
		  * @version 1.0
		  * @throws IOException 
		  *
		  */ 
		 $('#xishou0_').combogrid({
				url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
		 		idField : 'jobNo',
		 		textField : 'name',
		 		mode:"remote",
		 		panelAlign:'right',
		 		panelWidth:325,
		 		editable : true,
		 		pageList:[10,20,30,40,50],
				 pageSize:"10",
				 validType:'text',
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
			    },
				onLoadSuccess: function (){
				     var id=$(this).prop("id");
				     if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
			            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
			         } 
			    } 
		 	});
		 /**  
		  *  
		  * @Description：手术诊断1
		  * @Author：zhangjin
		  * @CreateDate：2016-4-28
		  * @version 1.0
		  * @throws IOException 
		  *
		  */	
		 $('#shoushuzd0_').combogrid({
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
			  	   var val = $(this).combogrid('getValue');
			  	   var name = $(this).combogrid('getText');
				   if(validName(name)){
		  		    $(this).combogrid("clear")
		    	$.messager.alert("提示","诊断名称不能重复!");
	    		setTimeout(function(){
					$(".messager-body").window('close');
				},2000);
	    	
	       }
		     	if(name==val&&name!=null&&name!=""){
		     		if(!$('#operationZD').is(':checked')){
		     	    	$.messager.confirm('提示','该条信息在手术诊断信息下拉表格中不存在，是否更改为自定义诊断？',function(r){
		     	    		if(r){
		     	    			$("#operationZD").prop("checked",true);
		     	    			sszd();
		     	    		}
		     	    	})
		     		}
		     	}
			  	},
		 onLoadSuccess: function (){
				      var id=$(this).prop("id");
				      if ($("#"+id).combogrid('getValue')&&diagMap.get($("#"+id).combogrid('getValue'))) {
			            	$("#"+id).combogrid('setText',diagMap.get($("#"+id).combogrid('getValue')));
			            } 
			      } 
		 });
	/**  
	 *  
	 * @Description：加载一些渲染类
	 * @Author：zhangjin
	 * @CreateDate：2016-4-28
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
		

	setTimeout(function(){
		 /**  
		  *  
		  * @Description： 申请医生
		  * @Author：zhangjin
		  * @CreateDate：2016-4-28
		  * @version 1.0
		  * @throws IOException 
		  *
		  */
			 $("#sqysbm").combobox({
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
			
		 	/**  
			  *  
			  * @Description：一级审批人
			  * @Author：zhangjin
			  * @CreateDate：2016-4-28
			  * @version 1.0
			  * @throws IOException 
			  *
			  */
			 $("#apprDoctor").combobox({
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
			 
		 	/**  
			  *  
			  * @Description：二级审批人
			  * @Author：zhangjin
			  * @CreateDate：2016-4-28
			  * @version 1.0
			  * @throws IOException 
			  *
			  */
			 $('#apprDoctor2').combobox({
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
			 
		 	/**  
			  *  
			  * @Description：三级审批人
			  * @Author：zhangjin
			  * @CreateDate：2016-4-28
			  * @version 1.0
			  * @throws IOException 
			  *
			  */
			 $('#apprDoctor3').combobox({
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
			 
		 	/**  
			  *  
			  * @Description：指导医生编码
			  * @Author：zhangjin
			  * @CreateDate：2016-4-28
			  * @version 1.0
			  * @throws IOException 
			  *
			  */
			$('#guiDoctor').combobox({
				valueField : 'jobNo',
				textField : 'name',
				multiple : false,
				editable : true,
				data:user,
				onSelect : function(record) {
					var id=$(this).prop("id");
			 		var value=$("#"+id).combobox("getValue");
			 		var ssysbm=$('#ssysbm').combobox("getValue");
			 		var zs=$('[id^=zhushou]');
			 		if(value!=""&&value!=null){
			 			zs.each(function(){
			 	 			var zsval=$(this).combobox("getValue");
			 	 			if(zsval!=null&&zsval!=""){
			 	 				if(value==zsval){
			 	 					$("#"+id).combobox("clear");
			 	 	 				$.messager.alert("提示","指导医生与助手医生不能相同!");
			 	 	 				setTimeout(function(){
										$(".messager-body").window('close');
									},2500);
			 	 	 				return
			 	 	 			}
			 	 			}
			 	 		});
			 			if(ssysbm!=null&&ssysbm!=""){
			 				if(ssysbm==value){
			 					$("#"+id).combobox("clear");
			 	 				$.messager.alert("提示","指导医生不能与手术医生相同!")
			 	 				setTimeout(function(){
									$(".messager-body").window('close');
								},2500);
			 	 			}
			 			}
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
			 
			/**  
			 *  
			 * @Description：手术医生编码
			 * @Author：zhangjin
			 * @CreateDate：2016-4-28
			 * @version 1.0
			 * @throws IOException 
			 *
			 */
			$('#ssysbm').combobox({
			 	valueField : 'jobNo',
			 	textField : 'name',
			 	multiple : false,
			 	editable : true,
			 	data:user,
			 	onSelect : function(record) {
			 		var id=$(this).prop("id");
			 		var value=$("#"+id).combobox("getValue");
			 		var ssysbm=$('#guiDoctor').combobox("getValue");
			 		var zs=$('[id^=zhushou]');
			 		if(value!=""&&value!=null){
			 			zs.each(function(){
			 	 			var zsval=$(this).combobox("getValue");
			 	 			if(zsval!=null&&zsval!=""){
			 	 				if(value==zsval){
			 	 					$("#"+id).combobox("clear");
			 	 	 				$.messager.alert("提示","手术医生与助手医生不能相同!");
			 	 	 				setTimeout(function(){
										$(".messager-body").window('close');
									},3000);
			 	 	 				return
			 	 	 			}
			 	 			}
			 	 		});
			 			if(ssysbm!=null&&ssysbm!=""){
			 				if(ssysbm==value){
			 					$("#"+id).combobox("clear");
			 	 				$.messager.alert("提示","手术医生不能与指导医生相同!");
			 	 				setTimeout(function(){
									$(".messager-body").window('close');
								},3000);
			 	 			}
			 			}
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
			
			/**  
			 * @Description：助手1
			 * @Author：zhangjin
			 * @CreateDate：2016-4-28
			 * @version 1.0
			 * @throws IOException 
			 *
			 */
			 $('#zhushou0_').combobox({
			 	valueField : 'jobNo',
			 	textField : 'name',
			 	multiple : false,
			 	editable : true,
			 	validType:'zs',
			 	data:user,
			 	onSelect : function() {
			 		var zsDoc=$('[id^=zhushou]');
			    	var id=$(this).prop("id");
			    	var ssysbm=$('#ssysbm').combobox("getValue");
			  		var gui=$('#guiDoctor').combobox("getValue");
			  		var b = 0;
			  		var zsys=$(this).combobox("getValue");
		    		zsDoc.each(function(index,obj){
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
		 },400);
 });
 


	/**  
	 *  
	 * @Description：是否自费
	 * @Author：zhangjin
	 * @CreateDate：2016-4-28
	 * @version 1.0
	 * @throws IOException 
	 *
	 */
	function sfzf(){
		if($("#zifei").is(":checked")){
			$("#zifei1").val(1);
		}else{
			$("#zifei1").val(0);
		}
	}
	

//获取添加的手术名称数量(诊断)
var indexName2=1;
//获取添加的手术诊断数量(诊断)
var indexName=1;
//护士数量
var indexfz=1;
var s="";
var c="";
var ids = "";//id字符串
var empMap=""; //员工信息
var operId;//手术序号
var user= null;//员工
var userhs=null;//员工(护士)
var sqzday=null;//诊断信息
var sqzdaye=null;//存放诊断信息数据
/**
 * 洗手
 */
var xishouMap=new Map();
/**
 * 巡回
 */
var xunhuiMap=new Map();
/**
 * 助手
 */
var zhushouMap=new Map();
/**
 * 手术名称
 */
var ssmcMap=new Map();
/**
 * 诊断名称
 */
var sqzdMap=new Map();

 /**  
 *  
 * @Description：是否有菌
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
 function isgermabc() {
 	if ($('#isgerm').is(':checked')) {
 		$('#isgerm1').val(1);
 	}
 	else {
 		$('#isgerm1').val(0);
 	}
 }
/**  
 *  
 * @Description：是否需要巡回护士
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
 function isneedprepabc(){
		var xunhui=$('[id^=xunhui]');
		if ($('#isneedprep').is(':checked')) {
			$('#isneedprep1').val(1);
			$("#prepNurse").textbox("readonly",false);
		
		}else {
			$('#isneedprep1').val(0);
			$("#prepNurse").textbox("readonly");
			xunhui.each(function(){
				$(this).combogrid("readonly",true);
				$(this).combogrid('clear');
				$("#prepNurse").textbox('clear');
				$(this).combogrid('disable');
			});
		}
	}

/**  
 *  
 * @Description：是否需要随台护士
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function isneedaccoabc(){
	if ($('#isneedacco').is(':checked')) {
		$('#isneedacco1').val(1);
		$("#accoNurse").textbox("readonly",false);
	}else {
		$('#isneedacco1').val(0);
		$("#accoNurse").textbox("readonly");
	}
}

/**  
 *  
 * @Description：删除指定项
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function removeTr(trId,aId,index){
	var nu = parseInt(index)+1;
	$("#"+trId+nu).remove();
	$('#'+aId+index).show();
	$('#j'+aId+index).show();
}
/**  
 *  
 * @Description：判断名字是否重复
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
$.extend($.fn.validatebox.defaults.rules, {  
         ssmc: { // 手术名称
            validator : function(value, param) {  
            	var mc = $('[id^=nssmc]');
 	        	var b = 0
 	 			mc.each(function(index,obj){
 	 				if($(obj).combobox('getText') == value){
 	 					b++;
 	 				}
  				});
 	            return b>1?false:true;
             },  
         },
         zs: { // 助手名称
             validator : function(value, param) { 
             	var mc = $('[id^=zhushou]');
  	        	var b = 0
  	 			mc.each(function(index,obj){
  	 				if($(obj).combobox('getText') == value){
  	 					b++;
  	 				}
   				});
  	            return b>1?false:true;
              },  
          },
}); 

/**  
 *  
 * @Description：添加指定项
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function add(name,trId,index,tdId,tdname,aId){
	var nu = parseInt(index)+1;
	var nu2 = nu+1;
	$("#"+trId+index).after("<tr id=\""+trId+nu+"\">"+
		    "<td  style=\"text-align: right;width: 15%\">"+name+nu2+"：</td>"+   
			"<td colspan=\"5\"><input id=\""+tdId+nu+"_\" name=\""+tdname+nu+"\"  style=\"width: 92%\" data-options=\"required:true\">"+
			"<a id=\""+aId+nu+"\" href=\"javascript:void(0)\" onclick=\"add('"+name+"','"+trId+"','"+nu+"','"+tdId+"','"+tdname+"','"+aId+"')\"></a>"+
			"<a id=\"j"+aId+nu+"\" href=\"javascript:void(0)\" onclick=\"removeTr('"+trId+"','"+aId+"','"+index+"')\"></a></td>"+
		"</tr>"); 
	if(name=='术前诊断'){
		indexName=nu2;
		//自定义诊断选中
		if($("#operationZD").is(':checked')){
			$('#'+tdId+nu+"_").combogrid({
				idField : 'code',
				textField : 'name',
				multiple : false,
				editable : true,
				mode:"remote",
				data:[],
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
  				  	   if(validName(name)){
  				  		    $(this).combogrid("clear")
					    	$.messager.alert("提示","诊断名称不能重复!");
				    		setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
				    	
				     }
  				  } 
			}); 
		}else{
			//自定义诊断未选中
			$('#'+tdId+nu+"_").combogrid({
				url : '<%=basePath%>operation/operationList/icdCombobox2fy.action',
				idField : 'code',
				textField : 'name',
				multiple : false,
				editable : true,
				mode:"remote",
				data:sqzdaye,
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
				  	   var val = $(this).combogrid('getValue');
				  	   var name = $(this).combogrid('getText');
				  	   if(validName(name)){
				  		    $(this).combogrid("clear")
					    	$.messager.alert("提示","诊断名称不能重复!");
				    		setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
				       }
				     	if(name==val&&name!=null&&name!=""){
				     		if(!$('#operationZD').is(':checked')){
				     	    	$.messager.confirm('提示','该条信息在手术诊断信息下拉表格中不存在，是否更改为自定义诊断？',function(r){
				     	    		if(r){
				     	    			$("#operationZD").prop("checked",true);
				     	    			sszd();
				     	    		}
				     	    	})
				     		}
				     	}
				  	},
				 onLoadSuccess: function (){
 				      var id=$(this).prop("id");
 				      if ($("#"+id).combogrid('getValue')&&diagMap.get($("#"+id).combogrid('getValue'))) {
 			            	$("#"+id).combogrid('setText',diagMap.get($("#"+id).combogrid('getValue')));
 			            } 
 			      }  
			}); 
		}
		
	}else{
		indexName2=nu2;
		//自定义手术被选中
		if($("#operation").is(':checked')){
			$('#'+tdId+nu+"_").combogrid({
				data : [],
			    valueField:'code',    
			    textField:'name',
			    mode:"remote",
				pageList:[10,20,30,40,50],
			 	pageSize:"10",
			 	pagination:true,
			 	multiple : false,
				columns:[[    
				         {field:'code',title:'编码',width:'18%'},    
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
			//自定义手术未被选中
			$('#'+tdId+nu+"_").combogrid({
				 url : '<%=basePath%>operation/operationList/undrugComboboxfy.action',
			    idField:'code',    
			    textField:'name',
			    mode:"remote",
				pageList:[10,20,30,40,50],
			 	pageSize:"10",
			 	pagination:true,
			 	multiple : false,
				columns:[[    
				         {field:'code',title:'编码',width:'18%'},    
				         {field:'name',title:'名称',width:'20%'},    
				         {field:'undrugPinyin',title:'拼音码',width:'20%'},
				         {field:'undrugWb',title:'五笔码',width:'20%'},    
				         {field:'undrugInputcode',title:'自定义码',width:'20%'},
				     ]],  
			 	onHidePanel:function(none){
				  	   var val = $(this).combogrid('getValue');
				  	   var name = $(this).combogrid('getText');
				  	   if(nssmcName(name)){
				    		$(this).combogrid("clear");
					    	$.messager.alert("提示","手术名称不能重复!");
					    	setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
					    	
					    }
				     	if(name==val&&name!=null&&name!=""){
				     		if(!$('#operation').is(':checked')){
				     	    	$.messager.confirm('提示','该条信息在手术名称信息下拉表格中不存在，是否更改为自定义手术？',function(r){
				     	    		if(r){
				     	    			$("#operation").prop("checked",true);
				     	    			ssmcdy();
				     	    		}
				     	    	})
				     		}
				     	}
				  	},
			    onLoadSuccess: function () {
			    	var id=$(this).prop("id");
			       if ($("#"+id).combogrid('getValue')&&operatNameMap.get($("#"+id).combogrid('getValue'))) {
		            	$("#"+id).combogrid('setText',operatNameMap.get($("#"+id).combogrid('getValue')));
		            } 
		        }  
			}); 
		}
			
	}
	//添加按钮
	$('#'+aId+nu).linkbutton({    
	    iconCls: 'icon-add'   
	});  
	//移除按钮
	$('#j'+aId+nu).linkbutton({    
	    iconCls: 'icon-remove'   
	});  
	$('#'+aId+index).hide();
	$('#j'+aId+index).hide();
}

/**  
 * @Description：添加护士和助手
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 */
function addfz(trId,index,aId){
	var nu = parseInt(index)+1;
	var max;
	var maxhel=$("#helperNum").numberbox("getValue");
	var maxpre=$("#prepNurse").numberbox("getValue");
	var maxwash=$("#washNurse").numberbox("getValue");
	max = maxhel>maxpre?maxhel:maxpre;
	max = max>maxwash?max:maxwash;
	var nu2 = nu+1;
	if(max>=nu2){
		$("#"+trId+index).after("<tr id=\""+trId+nu+"\">"+
			    "<td  style=\"text-align: right;width: 15%\">助手"+nu2+"：</td>"+
				"<td style=\"width: 15%\"><input id=\"zhushou"+nu+"_\" name=\"thelper"+nu+"\" >"+
				"</td>"+
				"<td  style=\"text-align: right;width: 15%\">洗手护士"+nu2+"：</td>"+
				"<td style=\"width: 15%\"><input id=\"xishou"+nu+"_\" name=\"wash"+nu+"\">"+
				"</td>"+
			    "<td  style=\"text-align: right;width: 15%\">巡回护士"+nu2+"：</td>"+
				"<td style=\"width: 15%\"><input id=\"xunhui"+nu+"_\" name=\"tour"+nu+"\" readonly=\"readonly\">"+
				"<a id=\""+aId+nu+"\" href=\"javascript:void(0)\" onclick=\"addfz('"+trId+"','"+nu+"','"+aId+"')\"></a>"+
				"<a id=\"j"+aId+nu+"\" href=\"javascript:void(0)\" onclick=\"removeTr('"+trId+"','"+aId+"','"+index+"')\"></a></td>"+
			"</tr>"); 
		if(maxhel>=nu2){
			//助手医生
			$('#zhushou'+nu+"_").combobox({    
			    valueField:'jobNo',    
			    textField:'name',
			    readonly:false,
			    validType:'zs',
			    data:user,
		    	onSelect : function() {
		    		var zsDoc=$('[id^=zhushou]');
			    	var id=$(this).prop("id");
			    	var ssysbm=$('#ssysbm').combobox("getValue");
			  		var gui=$('#guiDoctor').combobox("getValue");
			  		var zsys=$(this).combobox("getValue");
		     		var b = 0;
		    		zsDoc.each(function(index,obj){
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
		}else{
			$('#zhushou'+nu+"_").combobox({    
			    valueField:'jobNo',    
			    textField:'name',
			    readonly:true,
			    disabled:true,
			    validType:'zs',
			    data:user,
		    	onSelect : function() {
		    		var zsDoc=$('[id^=zhushou]');
			    	var id=$(this).prop("id");
			    	var ssysbm=$('#ssysbm').combobox("getValue");
			  		var gui=$('#guiDoctor').combobox("getValue");
			  		var zsys=$(this).combobox("getValue");
		     		var b = 0;
		    		zsDoc.each(function(index,obj){
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
		}
		//洗手护士
		if(maxwash>=nu2){
			$('#xishou'+nu+"_").combogrid({    
				url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
		 		idField : 'jobNo',
		 		textField : 'name',
		 		mode:"remote",
		 		panelAlign:'right',
		 		panelWidth:325,
		 		editable : true,
		 		pageList:[10,20,30,40,50],
				 pageSize:"10",
				 validType:'text',
				 pagination:true,
			 	columns:[[   
						{field:'jobNo',title:'工作号',width:'130'},
			 	         {field:'name',title:'名称',width:'160'} 
			 	        
		 	     ]],  
		 	    onSelect:function(rowIndex, rowData){
			    	if(xishouName(rowData.jobNo)){
			    		$('#xishou'+nu+"_").combogrid("clear");
				    	$.messager.alert("提示","护士信息不能重复!");
				    	setTimeout(function(){
							$(".messager-body").window('close');
						},3000);
				    	
				    }
			     },
				 onLoadSuccess: function (){
  				    	var id=$(this).prop("id");
  				      if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
  			            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
  			            } 
  			        }  
			});  
		}else{
			$('#xishou'+nu+"_").combogrid({ 
				url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
		 		idField : 'jobNo',
		 		textField : 'name',
		 		mode:"remote",
		 		panelAlign:'right',
		 		readonly:true,
		 		disabled:true,
		 		panelWidth:325,
		 		editable : true,
		 		pageList:[10,20,30,40,50],
				 pageSize:"10",
				 validType:'text',
				 pagination:true,
			 	columns:[[   
						{field:'jobNo',title:'工作号',width:'130'},
			 	         {field:'name',title:'名称',width:'160'} 
			 	        
		 	     ]],  
		 	    onSelect:function(rowIndex, rowData){
			    	if(xishouName(rowData.jobNo)){
			    		$('#xishou'+nu+"_").combogrid("clear");
				    	$.messager.alert("提示","护士信息不能重复!");
				    	setTimeout(function(){
							$(".messager-body").window('close');
						},3000);
				    	
				    }
			     },
				 onLoadSuccess: function (){
  				    	var id=$(this).prop("id");
  				      if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
  			            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
  			            } 
  			        }  
			});  
		}
		//巡回护士
		if ($('#isneedprep').is(':checked')) {
			if(maxpre>=nu2){
				$('#xunhui'+nu+"_").combogrid({   
					url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
			 		idField : 'jobNo',
			 		textField : 'name',
			 		mode:"remote",
			 		panelAlign:'right',
			 		readonly:false,
			 		panelWidth:325,
			 		editable : true,
			 		pageList:[10,20,30,40,50],
					 pageSize:"10",
					 validType:'text',
					 pagination:true,
				 	columns:[[   
							{field:'jobNo',title:'工作号',width:'130'},
				 	         {field:'name',title:'名称',width:'160'} 
				 	        
			 	     ]],  
			 	    onSelect:function(rowIndex, rowData){
			 	    	if(xunhuiName(rowData.jobNo)){
				    		$('#xunhui'+nu+"_").combogrid("clear");
					    	$.messager.alert("提示","护士信息不能重复!");
					    	setTimeout(function(){
								$(".messager-body").window('close');
							},3000);
					    }
				     },
 					 onLoadSuccess: function () {
				    	var id=$(this).prop("id");
				    	   if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
			            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
			            } 
			        } 
				});
			}else{
				$('#xunhui'+nu+"_").combogrid({  
					url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
			 		idField : 'jobNo',
			 		textField : 'name',
			 		mode:"remote",
			 		panelAlign:'right',
			 		readonly:true,
			 		disabled:true,
			 		panelWidth:325,
			 		editable : true,
			 		pageList:[10,20,30,40,50],
					 pageSize:"10",
					 validType:'text',
					 pagination:true,
				 	columns:[[   
							{field:'jobNo',title:'工作号',width:'130'},
				 	         {field:'name',title:'名称',width:'160'} 
				 	        
			 	     ]],  
			 	    onSelect:function(rowIndex, rowData){
			 	    	if(xunhuiName(rowData.jobNo)){
				    		$('#xunhui'+nu+"_").combogrid("clear");
					    	$.messager.alert("提示","护士信息不能重复!");
					    	setTimeout(function(){
								$(".messager-body").window('close');
							},3000);
					    }
				     },
 					 onLoadSuccess: function (){
  				    	var id=$(this).prop("id");
  				      if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
  			            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
  			            } 
  			        }   
				});
			}
		}else{
			$('#xunhui'+nu+"_").combogrid({    
				url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
		 		idField : 'jobNo',
		 		textField : 'name',
		 		mode:"remote",
		 		panelAlign:'right',
		 		readonly:true,
		 		panelWidth:325,
		 		editable : true,
		 		pageList:[10,20,30,40,50],
				 pageSize:"10",
				 validType:'text',
				 pagination:true,
			 	columns:[[   
						{field:'jobNo',title:'工作号',width:'130'},
			 	         {field:'name',title:'名称',width:'160'}
			 	        
		 	     ]],  
		 	    onSelect:function(rowIndex, rowData){
		 	    	if(xunhuiName(rowData.jobNo)){
			    		$('#xunhui'+nu+"_").combogrid("clear");
				    	$.messager.alert("提示","护士信息不能重复!");
				    	setTimeout(function(){
							$(".messager-body").window('close');
						},3000);
				    }
			     },
				 onLoadSuccess: function (){
  				    	var id=$(this).prop("id");
  				      if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
  			            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
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
		indexfz=nu2;
	}else{
		$.messager.alert("提示","超过安排人数");
		setTimeout(function(){
			$(".messager-body").window('close');
		},2000);
	}
}
var deptMapq="";//渲染科室
var a = '';//患者信息查询使用的参数
var b = '';//手术信息查询使用的参数


/**  
 *  
 * @Description：是否加急
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function isurgentabc(){
	if ($('#isurgent').is(':checked')) {
		$('#isurgent1').val(1);
	}else {
		$('#isurgent1').val(0);
	}
}


/**  
 *  
 * @Description：是否重症
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function isheavyabc(){
	if ($('#isheavy').is(':checked')) {
		$('#isheavy1').val(1);
	}else{
		$('#isheavy1').val(0);
	}
}

/**  
 *  
 * @Description：单选按钮是否特殊手术:
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
 function isspecialabc() {
 	if ($('#isspecial').is(':checked')) {
 		$('#isspecial1').val(1);
 	}else {
 		$('#isspecial1').val(0);
 	}
 }

/**  
 *  
 * @Description：是否需要病理检查
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
 function isneedpathologyabc() {
 	if ($('#isneedpathology').is(':checked')) {
 		$('#isneedpathology1').val(1);
 	}else {
 		$('#isneedpathology1').val(0);
 	}
 }

/**  
 *  
 * @Description：是否自体血回输
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
 function isautobloodabc() {
 	if ($('#isautoblood').is(':checked')) {
 		$('#isautoblood1').val(1);
 	}else {
 		$('#isautoblood1').val(0);
 	}
 }

/**  
 *  
 * @Description：是否需要审批
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function apprabc(){
	if ($('#appr').is(':checked')) {
 		$("#spdan").window("open");
 	}else {
 		$("#spdan").window("close");
 	}
}



/**  
 *  
 * @Description：渲染科室
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function functionDept(value,row,index){
	if(value!=null&&value!=''){
		return deptMapq[value]; 
	}
}
	
/**  
 *  
 * @Description：渲染表单中的挂号结算类别
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function functionPay(value,row,index){
	if(value!=null&&value!=''){
		return payMap.get(value);
	}
	return "";
}
	
/**  
 *  
 * @Description：渲染医生
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */
function functiondoctor(value,row,index){
	if(value!=""&&value!=null){
		return empMap[value];
	}else{
		return "";
	}
}
	
/**  
 *  
 * @Description：渲染手术名称
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */		
 function functionopNameList(value,row,index){
		if(value!=""&&value!=null){
			var name="";
			for(var i=0;i<value.length;i++){
				name+=value[i].itemName+",";
			}
			if(name!=""){
				return name.substring(0,name.length-1);
			}else{
				return name;
			}
			
			
		}else{
			return "";
		}
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





/**  
 *  
 * @Description：手术名称是否自定义
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException operationZD
 *
 */ 
function ssmcdy(){
	var en=$('[id^=nssmc]');
	if ($('#operation').is(':checked')) {
		en.each(function(index,obj){
			$(obj).combogrid("clear")
		    $(obj).combogrid({
		    	data:[],
			    mode:"local",
			    onBeforeLoad:function(){
		        	return false;
		        },
		        onHidePanel:function(none){
			  	    var name = $(this).combogrid('getText');
			  	    if(nssmcName(name)){
			    		 $(this).combogrid("clear")
					    	$.messager.alert("提示","手术名称不能重复!");
				    		setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
					    	
					    }
				  }
			});  
			$(obj).combogrid('grid').datagrid('loadData',{total:0,rows:[]}); 
			 
		})
 	}else {
 		//拟手术名称1
 		en.each(function(){
 			$(this).combogrid({
				url : '<%=basePath %>operation/operationList/undrugComboboxfy.action',
				idField : 'code',
				textField : 'name',
				multiple : false,
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
			    onLoadSuccess: function () {
			    	var id=$(this).prop("id");
			       if ($("#"+id).combogrid('getValue')&&operatNameMap.get($("#"+id).combogrid('getValue'))) {
		            	$("#"+id).combogrid('setText',operatNameMap.get($("#"+id).combogrid('getValue')));
		            } 
		        },
		        onHidePanel:function(none){
				  	   var val = $(this).combogrid('getValue');
				  	   var name = $(this).combogrid('getText');
				  	   if(nssmcName(name)){
				    		$(this).combogrid("clear");
					    	$.messager.alert("提示","手术名称不能重复!");
					    	setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
					    	
					    }
				     	if(name==val&&name!=null&&name!=""){
				     		if(!$('#operation').is(':checked')){
				     	    	$.messager.confirm('提示','该条信息在手术名称下拉表格中不存在，是否更改为自定义手术？',function(r){
				     	    		if(r){
				     	    			$("#operation").prop("checked",true);
				     	    			ssmcdy();
				     	    		}
				     	    	})
				     		}
				     	}
				  	},
		        onBeforeLoad:function(){
		        	return true;
		        } 
		 	});
 		})
 	}
}
/**  
 *  
 * @Description：手术诊断是否自定义
 * @Author：zhangjin
 * @CreateDate：2016-11-2
 * @version 1.0
 * @throws IOException 
 *
 */ 
function sszd(){
	var en=$('[id^=shoushuzd]');
	if ($('#operationZD').is(':checked')) {
		en.each(function(index,obj){
			$(obj).combogrid("clear")
		    $(obj).combogrid({
		    	data:[],
			    mode:"local",
			    onBeforeLoad:function(){
		        	return false;
		        },
		        onHidePanel:function(none){
		        	var name = $(this).combogrid('getText');
			        if(validName(name)){
				  		    $(this).combogrid("clear")
				    	    $.messager.alert("提示","诊断名称不能重复!");
			    		    setTimeout(function(){
							   $(".messager-body").window('close');
						    },2000);
			    	
			       }
		        }
			});  
			$(obj).combogrid('grid').datagrid('loadData',{total:0,rows:[]}); 
		});
 	}else {
 		//术前诊断1
 		en.each(function(){
 			$(this).combogrid({
 				url : '<%=basePath%>operation/operationList/icdCombobox2fy.action',
				idField : 'code',
				textField : 'name',
				multiple : false,
				editable : true,
				mode:"remote",
				data:sqzdaye,
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
				  	   var val = $(this).combogrid('getValue');
				  	   var name = $(this).combogrid('getText');
				  	   if(validName(name)){
  				  		    $(this).combogrid("clear")
					    	$.messager.alert("提示","诊断名称不能重复!");
				    		setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
				    	
				       }
				     	if(name==val&&name!=null&&name!=""){
				     		if(!$('#operationZD').is(':checked')){
				     	    	$.messager.confirm('提示','该条信息在手术诊断信息下拉表格中不存在，是否更改为自定义诊断？',function(r){
				     	    		if(r){
				     	    			$("#operationZD").prop("checked",true);
				     	    			sszd();
				     	    		}
				     	    	})
				     		}
				     	}
				  	},
				 onLoadSuccess: function (){
  				      var id=$(this).prop("id");
  				      if ($("#"+id).combogrid('getValue')&&diagMap.get($("#"+id).combogrid('getValue'))) {
  			            	$("#"+id).combogrid('setText',diagMap.get($("#"+id).combogrid('getValue')));
  			            } 
  			      },
  			    onBeforeLoad:function(){
		        	return true;
		        } 
		 	});
 		})
 	}
}



/**  
 *  
 * @Description：提交验证
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */ 
function submitop() {
	var blh=$("#blh").text();
	if(blh==""||blh==null){
		$.messager.alert("提示","请选择患者");
		return ;
	}
	var operationId=$("#operationId").val();
	// 获取日历对象
	//hedong 20170323增加对预约时间 申请日期的校验
	var sho=$("#nishoushu").val();
	if(!sho){
		$.messager.alert("提示","请选择预约时间");
		return;
	}
	if($("#nishoushu").val().length<19){
		$.messager.alert("提示","日期格式不正确 ，正确格式：2010-03-22 11:31:00");
		return;
	}
	var riqqiVal = $("#riqqi").val();
	if(!riqqiVal){
		$.messager.alert("提示","请选择申请日期");
		return;
	}
	if($("#riqqi").val().length<19){
		$.messager.alert("提示","日期格式不正确 ，正确格式：2010-03-22 11:31:00");
		return;
	}
	
 	var na=sho.substring(sho.indexOf(" ")+1);
	var e=parseInt(na.replace(":",""));
	var clinicCode=$("#inp").val();//住院流水号
	var med=$("#medicalrecord1").val();//病历号
	var checkFlag=true;
	/**
	 *手术名称
	 */
	
	var idArr = $('[id^=nssmc]');
	var itemNameStr = "";
	var itMap = new Map();
	var nowsqzdMap=new Map();
	idArr.each(function(){
		var idArrValue = $(this).prop('id');
		var id = idArrValue.substring(idArrValue.indexOf("_")+1);
		var newValue=$('#'+idArrValue).combogrid('getValue');
		var newText= $('#'+idArrValue).combogrid('getText');
		nowsqzdMap.put(id,newValue);
		 var oldValue = ssmcMap.get(id);
			if(!oldValue){
				if(newValue){
						itemNameStr+=newText+","+newValue+"_add#";//添加
				}
			}else{
				if(!newValue){
					itemNameStr+=id+"_del#";//删除
				}else{
					if(oldValue!=newValue){
							itemNameStr+=id+","+newText+","+newValue+"_upd#";//更新
					}
				}
			}
	});
	if(operationId!=null&&operationId!=""){
		ssmcMap.each(function(key,value,index){
			if(nowsqzdMap.get(key)==null||nowsqzdMap.get(key)==""){
				itemNameStr+=key+"_del#";//删除
			}
		});
	}
	
	/**
	 *诊断名称
	 */
	
	var zdArr = $('[id^=shoushuzd]');
	var diagNameStr = "";
	var zdMap = new Map();
	var newzdmap=new Map();
	zdArr.each(function(){
		var zdArrValue = $(this).prop('id');
		var zdId = zdArrValue.substring(zdArrValue.indexOf("_")+1);
		var newValue = $('#'+zdArrValue).combogrid('getValue');
		var newText = $('#'+zdArrValue).combogrid('getText');
		newzdmap.put(zdId,newText);
		var oldValue = sqzdMap.get(zdId);
		if(!oldValue){
			if(newText){
					diagNameStr+=newText+","+newValue+"_add#";//添加
			}
		}else{
			if(!newText){
				diagNameStr+=zdId+"_del#";//删除
			}else{
				if(oldValue!=newText){
						diagNameStr+=zdId+","+newText+","+newValue+"_upd#";//更新
				}
			}
		}
	});
	if(operationId!=null&&operationId!=""){
		sqzdMap.each(function(key,value,index){
			if(newzdmap.get(key)==null||newzdmap.get(key)==""){
				diagNameStr+=key+"_del#";//删除
			}
		});
	}
	/**
	 *洗手护士
	 */
	var xsArr = $('[id^=xishou]');
	var washStr = "";
	var xsMap = new Map();
	var newxsMap=new Map();
	xsArr.each(function(){
		var xsArrValue = $(this).prop('id');
		var xsnum=xsArrValue.substring(6,7);
		var xsId = xsArrValue.substring(xsArrValue.indexOf("_")+1);
		var newValue = $('#'+xsArrValue).combogrid('getValue');
		var newText = $('#'+xsArrValue).combogrid('getText');
		var oldValue = xishouMap.get(xsId);
		newxsMap.put(xsId,newValue);
		if(!oldValue){
			if(newValue){
				if(newValue==newText){
					$.messager.alert("提示","该护士信息有误！");
					 $('#'+xsArrValue).combogrid('clear');
				}else{
					washStr+= newText+","+xsnum+","+newValue+"_add#";//添加
				}
			}
		}else{
			if(!newValue){
				washStr+= xsId+"_del#";//删除
			}else{
				if(oldValue!=newValue){
					if(newValue==newText){
						$.messager.alert("提示","该护士信息有误！");
						 $('#'+xsArrValue).combogrid('clear');
					}else{
						washStr+= xsId+","+newText+","+xsnum+","+newValue+"_upd#";//更新
					}
				}
			}
		}
	});
	if(operationId!=null&&operationId!=""){
		xishouMap.each(function(key,value,index){
			if(newxsMap.get(key)==null||newxsMap.get(key)==""){
				washStr+=key+"_del#";//删除
			}
		});
	}
	/**
	 *巡回护士
	 */
	var xhArr = $('[id^=xunhui]');
	var tourStr = "";
	var xhMap = new Map();
	var newxhMap=new Map();
	xhArr.each(function(){
		var xhArrValue = $(this).prop('id');
		var xhnum=xhArrValue.substring(6,7);
		var xhId = xhArrValue.substring(xhArrValue.indexOf("_")+1);
		var newValue = $('#'+xhArrValue).combogrid('getValue');
		var newText = $('#'+xhArrValue).combogrid('getText');
		newxhMap.put(xhId,newValue);
		var oldValue = xunhuiMap.get(xhId);
		if(!oldValue){
			if(newValue){
				if(newValue==newText){
					$.messager.alert("提示","该护士信息有误！");
					 $('#'+xhArrValue).combogrid('clear');
				}else{
					tourStr+= newText+","+xhnum+","+newValue+"_add#";//添加
				}
			}
		}else{
			if(!newValue){
				tourStr+= xhId+",del#";//删除
			}else{
				if(oldValue!=newValue){
					if(newValue==newText){
						$.messager.alert("提示","该护士信息有误！");
						 $('#'+xhArrValue).combogrid('clear');
					}else{
						tourStr+= xhId+","+newText+","+xhnum+","+newValue+"_upd#";//更新
					}
				}
			}
		}
	});
	if(operationId!=null&&operationId!=""){
		xunhuiMap.each(function(key,value,index){
			if(newxhMap.get(key)==null||newxhMap.get(key)==""){
				tourStr+=key+"_del#";//删除
			}
		});
	}
	/**
	 *助手
	 */
	var zsArr = $('[id^=zhushou]');
	var thelperStr = "";
	var zsMap = new Map();
	var newzsMap=new Map();
	zsArr.each(function(){
		var zsArrValue = $(this).prop('id');
		var zsnum=zsArrValue.substring(7,8);
		var zsId = zsArrValue.substring(zsArrValue.indexOf("_")+1);
		var newValue = $('#'+zsArrValue).combobox('getValue');
		var newText = $('#'+zsArrValue).combobox('getText');
		newzsMap.put(zsId,newValue);
		var oldValue = zhushouMap.get(zsId);
		if(!oldValue){
			if(newValue){
				if(newValue==newText){
					$.messager.alert("提示","该助手信息有误！");
					 $('#'+zsArrValue).combobox('clear');
				}else{
					thelperStr+=newText+","+zsnum+","+newValue+"_add#";//添加
				}
			}
		}else{
			if(!newValue){
				thelperStr+= zsId+"_del#";//删除
			}else{
				if(oldValue!=newValue){
					if(newValue==newText){
						$.messager.alert("提示","该助手信息有误！");
						 $('#'+zsArrValue).combobox('clear');
					}else{
						thelperStr+= zsId+","+newText+","+zsnum+","+newValue+"_upd#";//更新
					}
				}
			}
		}
	});
	if(operationId!=null&&operationId!=""){
		zhushouMap.each(function(key,value,index){
			if(newzsMap.get(key)==null||newzsMap.get(key)==""){
				thelperStr+=key+"_del#"//删除
			}
		});
	}
	var nss="";
	if(operationId!=null&&operationId!=""){
		var nssname=$('[id^=nssmc]');
		nssname.each(function(){
		 nss+=$(this).combobox("getText")+",";
		});
	}else{
		var nssname=$('[id^=nssmc]');
		nssname.each(function(){
		 nss+=$(this).combobox("getText")+",";
		});
		
	}
	$.ajax({
		url : '<%=basePath%>operation/operationList/querynuss.action',
		data:{itemNameStr:nss,medicalrecordId:blh,opId:operationId},
		type:"post",
		success:function(data){
			if(data=="1"){
				$.messager.alert("提示","已申请有该手术");
			}else{
				var appDate=2359;
				if(operApp){
					appDate=parseInt(operApp.replace(":","")); 
				}
				if(e>appDate){
					$.messager.alert("提示","手术预约时间请在"+operApp+"前！");
				}else{
					var date=new Date();
				    var seperator1 = "-";
				    var seperator2 = ":";
				    var month = date.getMonth() + 1;
				    var strDate = date.getDate();
				    if (month >= 1 && month <= 9) {
				        month = "0" + month;
				    }
				    if (strDate >= 0 && strDate <= 9) {
				        strDate = "0" + strDate;
				    }
				    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
				            + " " + date.getHours() + seperator2 + date.getMinutes()
				            + seperator2 + date.getSeconds();
				    
					if(currentdate>sho){
						$.messager.confirm('提示','当前预约时间小于申请时间，确定要申请吗？',function(r){
							if(!r){
								return ;
							}else{
								if (!$('#editform').form('validate')) {
									$.messager.progress('close');
									$.messager.show({
										title : '提示信息',
										msg : '验证没有通过,不能提交表单!'
									});
									return false;
								}else{
									$.messager.progress({text:'保存中，请稍后...',modal:true});	//显示进度条
									$("#editform").form('submit',{
										url : "<%=basePath%>operation/operationList/operationapplySaveList.action",
										onSubmit:function(param){
											if (!$('#editform').form('validate')) {
												$.messager.progress('close');
												$.messager.show({
													title : '提示信息',
													msg : '验证没有通过,不能提交表单!',
												});
												return false;
											}
											param.itemNameStr=itemNameStr,
											param.diagNameStr=diagNameStr,
											param.washStr=washStr,
											param.tourStr=tourStr,
											param.thelperStr=thelperStr,
											param.isRecord="1",
											param.flog='mz'
										},
										success:function(data){
											var res = eval("(" + data + ")");
											$.messager.progress('close');// 如果提交成功则隐藏进度条
											$.messager.alert('提示',res.resMsg);
											if(data.resCode=='success'){
												clear();
											}
											setTimeout(function(){
						       					$(".messager-body").window('close');
						       				},3500);
										}
									}); 
								}
							}
						});
					}else{
						if (!$('#editform').form('validate')) {
							$.messager.progress('close');
							$.messager.show({
								title : '提示信息',
								msg : '验证没有通过,不能提交表单!'
							});
							return false;
						}else{
							$.messager.progress({text:'保存中，请稍后...',modal:true});	//显示进度条
							$("#editform").form('submit',{
								url : "<%=basePath%>operation/operationList/operationapplySaveList.action",
								onSubmit:function(param){
									if (!$('#editform').form('validate')) {
										$.messager.progress('close');
										$.messager.show({
											title : '提示信息',
											msg : '验证没有通过,不能提交表单!',
										});
										return false;
									}
									param.itemNameStr=itemNameStr,
									param.diagNameStr=diagNameStr,
									param.washStr=washStr,
									param.tourStr=tourStr,
									param.thelperStr=thelperStr,
									param.isRecord="1",
									param.flog='mz'
								},
								success:function(data){
									var res = eval("(" + data + ")");
									$.messager.progress('close');// 如果提交成功则隐藏进度条
									$.messager.alert('提示',res.resMsg);
									if(data.resCode=='success'){
										clear();
									}
									setTimeout(function(){
				       					$(".messager-body").window('close');
				       				},3500);
									
								}
							}); 
						}
					}
					
				}
			}
		}
	});
	
}

/**  
 * @Description：打印知情同意书
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */ 
function printZQTYS(){
	var opId = $("#operationId").val();
	var mid = $("#patientNo1").val();//注掉的信息('系统消息','无病历号不能打印!')
	if(!opId){
		$.messager.alert('系统消息','未申请的手术不能打印!');
		return;
	}
	var timerStr = Math.random();
 	 window.open ("<c:url value='/iReport/iReportPrint/iReportToZQTYS.action?randomId='/>"+timerStr+"&mid="+opId+"&fileName=shoushuzhiqingtongyishu",'newwindow'+mid,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
	
}
/**  
 *  
 * @Description：作废按钮
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */ 
function del(){
	var med =$("#blh").text();
	if(med==""){
		$.messager.alert("提示","请选择要作废的信息");
	}else{
		var opId=$("#operationId").val();
		if(opId!=null&&opId!=""){
			$.messager.confirm('确认', '确定要作废当前信息吗?', function(res){
				if(res){
					$.ajax({
						url:'<%=basePath%>operation/operationList/delopertionApply.action',
						data:{opId:opId},
						type:"post",
						success:function(data){
							$.messager.alert("提示",data.resMsg);
							setTimeout(function(){
		       					$(".messager-body").window('close');
		       				},3500);
						}
					});
				}
			});
		}else{
			$.messager.alert("提示","该手术还未申请");
			setTimeout(function(){
					$(".messager-body").window('close');
			},3500);
		}
	}
} 

/**  
 *  
 * @Description：手术申请单
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */ 
function printSSSQD(){
	var opId = $("#operationId").val();
	if(opId!=""&&opId!=null){
		var timerStr = Math.random();
	 	window.open ("<c:url value='/iReport/iReportPrint/iReportToOpperactionApply.action?randomId='/>"+timerStr+"&opId="+opId+"&fileName=SSSQD",'newwindow'+opId,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
	}else{
		$.messager.alert("提示","请选择已申请的手术信息");
	}
	
}
/**  
 *  
 * @Description：病历号查询
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */ 
function query(){
	var medicalrecordId = $('#medicalrecord').textbox('getValue');
	if(medicalrecordId!=null&&medicalrecordId!=""){
		//根据病历号查询患者信息
		$.ajax({
			url:'<%=basePath%>operation/operationapplycation/getPatientinfoMed.action',
			data:{id:medicalrecordId},
			type:"post",
			success:function(data){
				clear();
				var Plist = data;
				if(Plist.length==0){
					$.messager.alert('提示',"请正确输入正确的病历号！");
				}else if(Plist.length==1){
					a = Plist[0].medicalrecordId;
					b=Plist[0].no;
					operId="";
					huanzhexinxi();//患者信息
					shoushuxinxi();//手术信息
				}else if(Plist.length>1){
					$("#diaInpatient").window('open');
					$("#infoDatagrid").datagrid({
						data:Plist,
					    columns:[[    
					        {field:'medicalrecordId',title:'病历号',width:'25%',align:'center'} ,  
					        {field:'patientSex',title:'性别',width:'10%',align:'center',formatter:function(value,row,index){
					        	return sexMap.get(value);
					        }} ,
					        {field:'patientName',title:'姓名',width:'20%',align:'center'} ,   
					        {field:'patientCertificatesno',title:'身份证号',width:'20%',align:'center'},
					        {field:'expxrt',title:'挂号医生',width:'20%',align:'center',formatter:function(value,row,index){
					        	if(value!=""){
					        		return empMap[value];
					        	}
					        }}
					    ]] ,
					    onDblClickRow:function(rowIndex, rowData){
					    	a = rowData.medicalrecordId;
					    	b=rowData.no;
					    	operId="";
					    	huanzhexinxi();//患者信息
							shoushuxinxi();//手术信息
							$("#diaInpatient").window('close');
					    }
					});
				}
			}
		});	
	}
}

/**  
 *  
 * @Description：患者信息
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */ 
function huanzhexinxi(){
	//根据住院流水号查询患者住院信息
	$.ajax({
		url:'<%=basePath%>operation/operationapplycation/getPatientinfo.action',
		type:"post",
		data:{id:b},
		success:function(data){
			clear();
			var info=data;
			$("#blh").text(info[0].medicalrecordId);
			$("#xm").text(info[0].patientName);
			if(info[0].patientAge&&info[0].patientAgeunit){
				$("#nl").text(info[0].patientAge+info[0].patientAgeunit);
			}
			$("#zzys").text(empMap[info[0].expxrt]);//主治医生
			$("#inp").val(info[0].no);
			$("#deptCode").val(info[0].dept);
			$("#medicalrecord1").val(info[0].medicalrecordId);
			$("#sex").val(info[0].patientSex);
			$("#reportAge").val(info[0].patientAge);
			$("#patientName").val(info[0].patientName);
			$("#birthday").val(info[0].patientBirthday);
		  	$("#xb").text(sexMap.get(info[0].patientSex));
			$("#ks").text(deptMapq[info[0].dept]);
		  	$("#lb").text(payMap.get(info[0].patientPaykind));//需要改为合同单位
		  	$("#csrq").text(info[0].patientBirthday);
	  		if(info[0].patientIshepatitis==1){
		  		$("#zdjb").text("有");
		  	}else{
				$("#zdjb").text("无");
			}
			if(info[0].patientIsanaphy==1){
				$("#gm").text('有');
			}
			else{
				$("#gm").text("无");
			}
		}
	});	
}
/**  
 *  
 * @Description：手术信息
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */ 
function shoushuxinxi(){
	/**  
	 *  
	 * @Description：查询手术单
	 * @Author：zhangjin
	 * @CreateDate：2016-4-28
	 * @version 1.0
	 * @throws IOException 
	 *
	 */ 
	 setTimeout(function(){
		 $.ajax({
			url:"<%=basePath%>operation/operationList/queryOperationShoushu.action",
			data:{medicalrecordId:b,opId:operId,fore:"1"},
			type:'post',
			success: function(date) {
				qingss();
				var plist=date;
			    if(plist.length==1){
			    	xinxi(plist[0]);
			    	
				}else if(plist.length>1){
					$('#ssxxxx').window('open');
					$("#list").datagrid({
						data:date,
						queryParams:{medicalrecordId:b,opId:"",fore:"1"},
						method:'post',
						onSelect:function(rowIndex, rowData){
							a=rowData.patientNo;
							operId=rowData.id;
							xinxi(rowData);
							$('#ssxxxx').window('close');
					}
				});
			}else{
				qingss();
			}
		}		
	})
	},300)
};

//给表单赋值
function xinxi(plist){
	$("#operationId").val(plist.id);//手术序号
	$("#execDept").combobox("select",plist.execDept);//手术间
	$("#console").textbox("setValue",plist.console);//手术台
	$("#consoleType").combobox("select",plist.consoleType);//手术台类型
	$("#opType").combobox("select",plist.opType);//手术分类
	$("#nishoushu").val(plist.preDate);//预约时间
	$("#duration").numberbox("setValue",plist.duration);//预定用时
	$("#riqqi").val(plist.applyDate);//申请时间
	$("#opertionposition").combobox("select",plist.opertionposition);//手术体位
	if(plist.opDoctordept!=""&&plist.opDoctordept!=null){
		$("#opDoctordept").combobox("select",plist.opDoctordept);//手术医生科室
	}
	$("#ssysbm").combobox("select",plist.opDoctor);//手术医生
	$("#guiDoctor").combobox("select",plist.guiDoctor);//指导医生
	$("#isneedprep1").val(plist.isneedprep);//是否需要巡回护士
	$("#isneedacco1").val(plist.isneedacco);//是否需要随台护士
	if(plist.helperNum!=null&&plist.helperNum!=""){
		$("#helperNum").textbox("setValue",plist.helperNum);//助手数
		var zs=$('[id^=zhushou]');
		zs.each(function(){
			$(this).combobox('enable');
		});
	}
	if(plist.washNurse!=null&&plist.washNurse!=""){
		$("#washNurse").textbox("setValue",plist.washNurse);//洗手护士数
		var zs=$('[id^=xishou]');
		zs.each(function(){
			$(this).combogrid('enable');
		});
	}
	if(plist.prepNurse!=null&&plist.prepNurse!=""){
		$("#prepNurse").textbox("setValue",plist.prepNurse);//巡回护士数	
		var zs=$('[id^=xunhui]');
		zs.each(function(){
			$(this).combogrid('enable');
		});
	}
	
	$("#accoNurse").textbox("setValue",plist.accoNurse);//随台护士数
	$("#inciType").combobox("select",plist.inciType);//切口类型
	$("#infectType").combobox("select",plist.infectType);//感染类型
	$("#opsNote").textbox("setValue",plist.opsNote);//手术注意事项
	$("#clinical").textbox("setValue",plist.clinical);//临床表现
	$("#contraindication").textbox("setValue",plist.contraindication);//手术禁忌
	$("#indication").textbox("setValue",plist.indication);//手术适应症
	$("#stitution").textbox("setValue",plist.stitution);//病人情况
	$("#preparation").textbox("setValue",plist.preparation);//术前准备情况
	$("#complication").textbox("setValue",plist.complication);//可能的并发症
	$("#folk").textbox("setValue",plist.folk);//签字家属
	$("#relaCode").combobox("setValue",plist.relaCode);//家属关系
	$("#folkComment").textbox("setValue",plist.folkComment);//家属意见
	$("#degree").combobox("select",plist.degree);//手术规模
	$("#anesType").combobox("select",plist.anesType);//麻醉类型
	$("#aneWay").combobox("select",plist.aneWay);//麻醉方式
	$("#isurgent1").val(plist.isurgent);//是否加急
	$("#isheavy1").val(plist.isheavy);//是否重症
	$("#isspecial1").val(plist.isspecial);//是否特殊
	$("#isneedpathology1").val(plist.isneedpathology);//是否需要检查
	$("#isautoblood1").val(plist.isautoblood);//是否自体血回输
	$("#isgerm1").val(plist.isgerm);//是否有菌
	$("#getdate").textbox("setValue",plist.applyRemark);//备注
	if(plist.applyDoctor!=""&&plist.applyDoctor!=null){
		$("#sqysbm").combobox("setValue",plist.applyDoctor);//申请医生
	}
	$("#apprDoctor").combobox("select",plist.apprDoctor);
   	$("#apprDate").val(plist.apprDate);
   	$("#apprRemark").val(plist.apprRemark);
   	$("#apprDoctor2").combobox("select",plist.apprDoctor2);
   	$("#apprDate2").val(plist.apprDate2);
   	$("#apprRemark2").val(plist.apprRemark2);
   	$("#apprDoctor3").combobox("select",plist.apprDoctor3);
   	$("#apprDate3").val(plist.apprDate3);
   	$("#apprRemark3").val(plist.apprRemark3);
	$("#zifei1").val(plist.isownexpense);
	//是否自费
	if(plist.isownexpense!=null&&plist.isownexpense!=0){
		$("#zifei").attr('checked', 'true');
	}
	
	//是否需要检查
	if(plist.isneedpathology!=0&&plist.isneedpathology!=null){
		$("#isneedpathology").attr('checked', 'true');
	}
	//是否重症
	if(plist.isheavy!=0&&plist.isheavy!=null){
		$("#isheavy").attr('checked', 'true');
	}
	//是否加急
	if(plist.isurgent!=0&&plist.isurgent!=null){
		$("#isurgent").attr('checked', 'true');
	}
	//是否特殊
	if(plist.isspecial!=0&&plist.isspecial!=null){
		$("#isspecial").attr('checked', 'true');
	}
	//是否自体血回输
	if(plist.isautoblood!=0&&plist.isautoblood!=null){
		$("#isautoblood").attr('checked', 'true');
	}
	//是否需要巡回护士
	if(plist.isneedprep!=0&&plist.isneedprep!=null){
		$("#isneedprep").attr('checked', 'true');
		$("#prepNurse").textbox("readonly",false);//hedong 20170310 巡回护士选中后,巡回护士数可编辑
	    $("#xunhui0_").combobox("readonly",false);
	}
	
	//是否需要随台护士
	if(plist.isneedacco!=0&&plist.isneedacco!=null){
		$("#isneedacco").attr('checked', 'true');
		$("#accoNurse").textbox("readonly",false);
	}
	//是否有菌
	if(plist.isgerm!=0&&plist.isgerm!=null){
		$("#isgerm").attr('checked', 'true');
	}
	if(plist.isgerm!=0&&plist.isgerm!=null){
		$("#isgerm").attr('checked', 'true');
	}
	var num;
	var xsleng=plist.xiShouList.length;
	var xhleng=plist.xunHuiList.length;
	var zsleng=plist.zsDocList.length;
	if(xsleng>xhleng){
		num=xsleng;
	}else{
		num=xhleng;
	}
	if(zsleng>num){
		num=zsleng;
	}
	
	if(plist.helperNum!=null&&plist.helperNum!=""&&num<=0){
		$("#zhushou0_").combobox("readonly",false);
	}
	if(plist.washNurse!=null&&plist.washNurse!=""&&num<=0){
		$("#xishou0_").combobox("readonly",false);
	}
	if(plist.prepNurse!=null&&plist.prepNurse!=""&&num<=0){
		$("#xunhun0_").combobox("readonly",false);
	}
	//助手，护士
	for(var i=0;i<num;i++){
		
		var xunHuiEmplCode = (plist.xunHuiList[i]&&(plist.xunHuiList[i].emplCode||""))||"";
		var zsEmplCode = (plist.zsDocList[i]&&(plist.zsDocList[i].emplCode||""))||"";
		var xiShouEmplCode = (plist.xiShouList[i]&&(plist.xiShouList[i].emplCode||""))||"";
		var xunHuiId = (plist.xunHuiList[i]&&(plist.xunHuiList[i].id||""))||"";
		var zsId = (plist.zsDocList[i]&&(plist.zsDocList[i].id||""))||"";
		var xiShouId = (plist.xiShouList[i]&&(plist.xiShouList[i].id||""))||"";
		if(i!=0){
			var nu=i+1;
			$("#trzs"+(i-1)).after("<tr id=\"trzs"+i+"\">"+
				    "<td  style=\"text-align: right;width: 15%\">助手"+(i+1)+"：</td>"+
					"<td style=\"width: 15%\"><input id=\"zhushou"+i+"_"+zsId+"\" name=\"thelper"+i+"\" >"+
					"</td>"+
					"<td  style=\"text-align: right;width: 15%\">洗手护士"+(i+1)+"：</td>"+
					"<td style=\"width: 15%\"><input id=\"xishou"+i+"_"+xiShouId+"\" name=\"wash"+i+"\">"+
					"</td>"+
				    "<td  style=\"text-align: right;width: 15%\">巡回护士"+(i+1)+"：</td>"+
					"<td style=\"width: 15%\"><input id=\"xunhui"+i+"_"+xunHuiId+"\" name=\"tour"+i+"\" readonly=\"readonly\">"+
					"<a id=\"afz"+i+"\" href=\"javascript:void(0)\" onclick=\"addfz('trzs','"+i+"','afz')\"></a>"+
					"<a id=\"jafz"+i+"\" href=\"javascript:void(0)\" onclick=\"removeTr('trzs','jafz','"+(i-1)+"')\"></a></td>"+
				"</tr>"); 
			if(nu<=plist.helperNum){
				$('#zhushou'+i+"_"+zsId).combobox({    
    			    valueField:'jobNo',    
    			    textField:'name', 
    			    readonly:false,
    			    validType:'zs',
    			    data:user,
    			 	onSelect : function() {
    			 		var zsDoc=$('[id^=zhushou]');
    			    	var id=$(this).prop("id");
    			    	var ssysbm=$('#ssysbm').combobox("getValue");
    			  		var gui=$('#guiDoctor').combobox("getValue");
    			  		var zsys=$(this).combobox("getValue");
			     		var b = 0;
    		    		zsDoc.each(function(index,obj){
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
							},3000);
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
			}else{
				$('#zhushou'+i+"_"+zsId).combobox({ 
    			    valueField:'jobNo',    
    			    textField:'name', 
    			    readonly:true,
    			    validType:'zs',
    			    disabled:true,
    			    data:user,
    			 	onSelect : function() {
    			 		var zsDoc=$('[id^=zhushou]');
				    	var id=$(this).prop("id");
				    	var ssysbm=$('#ssysbm').combobox("getValue");
    			  		var gui=$('#guiDoctor').combobox("getValue");
    			  		var zsys=$(this).combobox("getValue");
			     		var b = 0;
			    		zsDoc.each(function(index,obj){
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
			}
			
			if(nu<=plist.washNurse){
				$('#xishou'+i+"_"+xiShouId).combogrid({
					url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
			 		idField : 'jobNo',
			 		textField : 'name',
			 		mode:"remote",
			 		panelAlign:'right',
			 		panelWidth:325,
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
				 	 }, 
 					 onLoadSuccess: function (){
    				    	var id=$(this).prop("id");
    				    	   if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
    			            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
    			            } 
    			        }  
    			});  
			}else{
				$('#xishou'+i+"_"+xiShouId).combogrid({
			 		idField : 'jobNo',
			 		textField : 'name',
			 		mode:"remote",
			 		panelAlign:'right',
			 		panelWidth:325,
			 		editable : true,
			 		readonly:true,
			 		disabled:true,
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
				 	 }, 
 					 onLoadSuccess: function (){
    				    	var id=$(this).prop("id");
    				    	   if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
    			            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
    			            } 
    			        }   
    			});  
			}
			
			if(nu<=plist.prepNurse){
				$('#xunhui'+i+"_"+xunHuiId).combogrid({ 
					url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
			 		idField : 'jobNo',
			 		textField : 'name',
			 		mode:"remote",
			 		panelAlign:'left',
			 		panelWidth:325,
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
				 	 }, 
 					 onLoadSuccess: function (){
    				    	var id=$(this).prop("id");
    				    	   if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
    			            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
    			            } 
    			      }  
    			}); 
			}else{
				$('#xunhui'+i+"_"+xunHuiId).combogrid({ 
					url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
			 		idField : 'jobNo',
			 		textField : 'name',
			 		mode:"remote",
			 		panelAlign:'left',
			 		panelWidth:325,
			 		editable : true,
			 		 readonly:true,
			 		disabled:true,
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
				 	 },
 					 onLoadSuccess: function (){
    				    	var id=$(this).prop("id");
    				    	   if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
    			            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
    			            } 
    			      }  
    			    
    			}); 
			}
			 if ($('#isneedprep').is(':checked')) {
    			var xunhui =$('[id^=xunhui]');
    			xunhui.each(function(){
    				$(this).combobox('readonly',false);
    			});
			} 
			$('#afz'+i).linkbutton({    
			    iconCls: 'icon-add'   
			});  
			$('#jafz'+i).linkbutton({    
			    iconCls: 'icon-remove'   
			});  
			$('#afz'+(i-1)).hide();
			$('#jafz'+(i-1)).hide();
		}else{
			$('#xunhui0_').combobox('readonly', false);
			$('#zhushou0_').prop("id",'zhushou0_'+zsId);
    		$('#xishou0_').prop("id",'xishou0_'+xiShouId);
    		$("#xs0").val(xiShouId);
    		$("#xh0").val(xunHuiId);
    		$('#xunhui0_').prop("id",'xunhui0_'+xunHuiId);
		}
		if(plist.helperNum!=null&&plist.helperNum!=""&&plist.helperNum!='0'){
			$('#zhushou'+i+'_'+zsId).combobox("readonly",false);
		}
		if(plist.washNurse!=null&&plist.washNurse!=""&&plist.helperNum!='0'){
			$('#xishou'+i+'_'+xiShouId).combogrid("readonly",false);
		}
		$('#zhushou'+i+'_'+zsId).combobox('select',(plist.zsDocList[i]&&(plist.zsDocList[i].emplCode||""))||"");
		if(plist.xiShouList[i]&&plist.xiShouList[i].emplCode){
			$('#xishou'+i+'_'+xiShouId).combogrid('grid').datagrid('load',{q:plist.xiShouList[i].emplCode});
			$('#xishou'+i+'_'+xiShouId).combogrid('setValue',(plist.xiShouList[i]&&(plist.xiShouList[i].emplCode||""))||""); 
		}
		if(plist.xunHuiList[i]&&plist.xunHuiList[i].emplCode){
			$('#xunhui'+i+'_'+xunHuiId).combogrid('grid').datagrid('load',{q:plist.xunHuiList[i].emplCode});
			$('#xunhui'+i+'_'+xunHuiId).combogrid('setValue',(plist.xunHuiList[i]&&(plist.xunHuiList[i].emplCode||""))||""); 
		}
		zhushouMap.put(""+zsId,zsEmplCode);
		xunhuiMap.put(""+xunHuiId,xunHuiEmplCode);
		xishouMap.put(""+xiShouId,xiShouEmplCode);
		indexfz=num;
		
	}
	//诊断名称
	 for(var i=0;i<plist.diagNameList.length;i++){
		var zdEmplCode = (plist.diagNameList[i]&&(plist.diagNameList[i].icdCode||""))||"";
		var zdId = (plist.diagNameList[i]&&(plist.diagNameList[i].id||""))||"";
		if(i!=0){
			$("#trsqzd"+(i-1)).after("<tr id=\"trsqzd"+i+"\">"+
	    			    "<td  style=\"text-align: right;width: 15%\">术前诊断"+(i+1)+"：</td>"+
	    				"<td colspan=\"5\"><input id=\"shoushuzd"+i+"_"+zdId+"\" name=\"diagName"+i+"\" style=\"width: 92%\" data-options=\"required:true\">"+
	    				"<a id=\"ashoushuzd"+i+"\" href=\"javascript:void(0)\" onclick=\"add('术前诊断','trsqzd','"+i+"','shoushuzd','diagName','ashoushuzd')\"></a>"+
	    				"<a id=\"jashoushuzd"+i+"\" href=\"javascript:void(0)\" onclick=\"removeTr('trsqzd','ashoushuzd','"+(i-1)+"')\"></a></td>"+
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
   				     onHidePanel:function(none){
  				 	         var val = $(this).combogrid('getText');
  				 	  	     var value = $(this).combogrid('getValue');
  					 	     if(validName(val)){
   					 	    	$(this).combogrid('clear');
	   					 	    $.messager.alert("提示","诊断名称不能重复!");
		   					 	setTimeout(function(){
									$(".messager-body").window('close');
								},2000);
  						     }
  					 		 if(value==val&&val!=null&&val!=""){
  					     		if(!$('#operationZD').is(':checked')){
  					     	    	$.messager.confirm('提示','该条信息在手术诊断信息下拉表格中不存在，是否更改为自定义诊断？',function(r){
  					     	    		if(r){
  					     	    			$("#operationZD").prop("checked",true);
  					     	    			sszd();
  					     	    		}
  					     	    	})
  					     		}
  					     	}
  				 	},
					onLoadSuccess: function (){
	  				      var id=$(this).prop("id");
	  				      if ($("#"+id).combogrid('getValue')&&diagMap.get($("#"+id).combogrid('getValue'))) {
	  			            	$("#"+id).combogrid('setText',diagMap.get($("#"+id).combogrid('getValue')));
	  			            } 
	  			      } 
    			}); 
    		$('#ashoushuzd'+i).linkbutton({    
			    iconCls: 'icon-add'   
			});  
			$('#jashoushuzd'+i).linkbutton({    
			    iconCls: 'icon-remove'   
			}); 
			$('#ashoushuzd'+(i-1)).hide();
			$('#jashoushuzd'+(i-1)).hide();
		 }else{
			 $('#shoushuzd0_').prop("id",'shoushuzd0_'+zdId);
			 $("#zd0").val(zdId);
		 }
		
		if(!plist.diagNameList[i].icdCode){
			$("#operationZD").attr("checked",'true');
			$('#shoushuzd'+i+'_'+zdId).combogrid('setValue',plist.diagNameList[i].diagName);
			$('#shoushuzd'+i+'_'+zdId).combogrid('setText',plist.diagNameList[i].diagName);
		}else{
			$('#shoushuzd'+i+'_'+zdId).combogrid('grid').datagrid('load',{q:plist.diagNameList[i].icdCode});
			$('#shoushuzd'+i+'_'+zdId).combogrid('setValue',plist.diagNameList[i].icdCode);
			$('#shoushuzd'+i+'_'+zdId).combogrid('setText',plist.diagNameList[i].diagName);
		}
		sqzdMap.put(""+zdId,plist.diagNameList[i].diagName);
		indexName=plist.diagNameList.length;
	}
	//手术名称
	for(var i=0;i<plist.opNameList.length;i++){
		var ssitemName = (plist.opNameList[i]&&(plist.opNameList[i].itemName||""))||"";
		var ssitemNameId=(plist.opNameList[i]&&(plist.opNameList[i].itemId||""))||"";
		var ssitemId = (plist.opNameList[i]&&(plist.opNameList[i].id||""))||"";
		if(i!=0){
			$("#trndss"+(i-1)).after("<tr id=\"trndss"+i+"\">"+
    			    "<td  style=\"text-align: right;width: 15%\">拟手术名称"+(i+1)+"：</td>"+
    				"<td colspan=\"5\"><input id=\"nssmc"+i+"_"+ssitemId+"\" name=\"itemName"+i+"\" style=\"width: 92%\" data-options=\"required:true\">"+
    				"<a id=\"anssmc"+i+"\" href=\"javascript:void(0)\" onclick=\"add('拟手术名称','trndss','"+i+"','nssmc','itemName','anssmc')\"></a>"+
    				"<a id=\"janssmc"+i+"\" href=\"javascript:void(0)\" onclick=\"removeTr('trndss','anssmc','"+(i-1)+"')\"></a></td>"+
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
  				 	      var value = $(this).combogrid('getValue');
  					 	     if(nssmcName(val)){
  					 	    	$(this).combogrid('clear');
  					 	    	$.messager.alert("提示","手术名称不能重复!");
  					 	    	setTimeout(function(){
									$(".messager-body").window('close');
								},2000);
  						    }
  					 	  if(value==val&&val!=null&&val!=""){
   				     		if(!$('#operation').is(':checked')){
   				     	    	$.messager.confirm('提示','该条信息在手术名称下拉表格中不存在，是否更改为自定义手术？',function(r){
   				     	    		if(r){
   				     	    			$("#operation").prop("checked",true);
   				     	    			ssmcdy();
   				     	    		}
   				     	    	})
   				     		}
   				     	 }
  				 	},
				    onLoadSuccess: function () {
				    	var id=$(this).prop("id");
				       if ($("#"+id).combogrid('getValue')&&operatNameMap.get($("#"+id).combogrid('getValue'))) {
			            	$("#"+id).combogrid('setText',operatNameMap.get($("#"+id).combogrid('getValue')));
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
		if(!plist.opNameList[i].itemId){
			$("#operation").attr("checked",'true');
			$('#nssmc'+i+'_'+ssitemId).combogrid("setValue",plist.opNameList[i].itemName);
			$('#nssmc'+i+'_'+ssitemId).combogrid("setText",plist.opNameList[i].itemName);
		}else{
			$('#nssmc'+i+'_'+ssitemId).combogrid('grid').datagrid('load',{q:plist.opNameList[i].itemId});
			$('#nssmc'+i+'_'+ssitemId).combogrid("setValue",plist.opNameList[i].itemId);
			$('#nssmc'+i+'_'+ssitemId).combogrid("setText",plist.opNameList[i].itemName);
		}
			
		ssmcMap.put(""+ssitemId,ssitemName);
		indexName2=plist.opNameList.length;
	} 
}
/**  
 *  
 * @Description：关闭手术信息窗口
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */ 
function guan(){
	$("#ssxxxx").window('close');
}
/**  
 *  
 * @Description：清除
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */ 
function clear(){
	 qingss();
	 $("#blh").text("");
	 $("#xm").text("");
	 $("#nl").text("");
	 $("#zzys").text("");
	 $("#csrq").text("");
	 $("#gm").text("");
	 $("#lb").text("");
	 $("#ks").text("");
	 $("#zdjb").text("");
	 $("#xb").text("");
	 $("#patientName").val("");
	$("#sex").val("");
	$("#reportAge").val("");
	$("#deptCode").val("");
	$("#bedNo").val("");
	$("#inp").val("");
	$("#medicalrecord1").val("");
	$("#birthday").val("");
}
/**  
 *  
 * @Description：清空手术申请信息	
 * @Author：zhangjin
 * @CreateDate：2016-4-28
 * @version 1.0
 * @throws IOException 
 *
 */ 
function qingss(){
	
	var patientName=$("#patientName").val();
	var sex=$("#sex").val();
	var reportAge=$("#reportAge").val();
	var deptCode=$("#deptCode").val();
	var bedNo=$("#bedNo").val();
	var inp=$("#inp").val();
	var medicalrecord1=$("#medicalrecord1").val();
	var birthday=$("#birthday").val();
	$("#editform").form("reset");
	$("#operationId").val("");
	$("#isneedacco").attr('checked', false);
	$("#isneedprep").attr('checked', false);
	$("#isautoblood").attr('checked', false);
	$("#isurgent").attr('checked', false)
	$("#isspecial").attr('checked', false);
	$("#isneedpathology").attr('checked', false);
	$("#isheavy").attr('checked',false);
	$("#isgerm").attr('checked', false);
	$("#zifei").attr('checked',false);
	$("#operation").attr('checked',false);
	$("#operationZD").attr('checked',false);
	
	$("#isneedacco1").val("");
	$("#isneedprep1").val("");
	$("#isautoblood1").val("");
	$("#isurgent1").val("")
	$("#isspecial1").val("");
	$("#isneedpathology1").val("");
	$("#isheavy1").val("");
	$("#isgerm1").val("");
	$("#zifei1").val("");
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
			zhushou.each(function(){
				var id=$(this).prop("id","zhushou0_");
				$(this).combogrid("readonly",true);
				$(this).combobox('disable');
			});
			xishou.each(function(){
				$(this).prop("id","xishou0_");
				$(this).combogrid("readonly",true);
				$(this).combogrid('disable');
			});
			xunhui.each(function(){
				$(this).prop("id","xunhui0_");
				$(this).combogrid("readonly",true);
				$(this).combogrid('disable');
			});
		}
		
	}
	//删除手术诊断信息
	for(var i=0;i<indexName;i++){
		if(i!=0){
			$("#trsqzd"+i).remove();
			$('#ashoushuzd'+(i-1)).show();
			$('#jashoushuzd'+(i-1)).show();
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
	$("#xunhui0_").combogrid('grid').datagrid('load',{q:" "});
	$("#xishou0_").combogrid('grid').datagrid('load',{q:" "});
	$("#shoushuzd0_").combogrid('grid').datagrid('load',{q:" "});
	$("#nssmc0_").combogrid('grid').datagrid('load',{q:" "});
	$("#accoNurse").numberbox('readonly',true);
	$("#prepNurse").numberbox('readonly',true);
	var patientName=$("#patientName").val(patientName);
	$("#sex").val(sex);
	$("#reportAge").val(reportAge);
	$("#deptCode").val(deptCode);
	$("#bedNo").val(bedNo);
	$("#inp").val(inp);
	$("#medicalrecord1").val(medicalrecord1);
	$("#birthday").val(birthday);
	sqzdMap=new Map();
	ssmcMap=new Map();
	
}

//巡回护士信息的验证
function xunhuiName(name){
	var mc = $('[id^=xunhui]');
 	var b = 0;
	mc.each(function(index,obj){
		if($(obj).combogrid('getValue') == name &&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
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
          	return a==1?true:false;
        }else{
     	   return true;
        }
}
//洗手护士信息的验证
function xishouName(name){
	var mc = $('[id^=xishou]');
 	var b = 0;
	mc.each(function(index,obj){
		if($(obj).combogrid('getValue') == name &&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
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
         	return a==1?true:false;
       }else{
    	   return true;
       }
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
//诊断信息的验证
function validName(name){
	var mc = $('[id^=shoushuzd]');
 	var b = 0;
	mc.each(function(index,obj){
		if($(obj).combogrid('getText') == name&&$(obj).combogrid('getText')!=""&&$(obj).combogrid('getText')!=null){
			b++;
		}
	});
 	return b>1?true:false;
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
	
	/*******************************开始读卡***********************************************/
	//定义一个事件（读卡）
	function read_card_ic(){
		var card_value = app.read_ic();
		if(card_value=='0'||card_value==undefined||card_value==''){
			$.messager.alert('提示','此卡号['+card_value+']无效');
			return;
		}
		$.ajax({
			url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
			data:{idcardOrRe:card_value},
			type:'post',
			async:false,
			success: function(data) { 
				if(data==null||data==''){
					$.messager.alert('提示','此卡号无效');
					return;
				}
				//填写病历号
				$("#medicalrecord").textbox("setValue",data);
				query();
			}
		});
	};
	/*******************************结束读卡***********************************************/
	/*******************************开始读身份证***********************************************/
	//定义一个事件（读身份证）
	function read_card_sfz(){
		var card_value = app.read_sfz();
		if(card_value=='0'||card_value==undefined||card_value==''){
			$.messager.alert('提示','此卡号['+card_value+']无效');
			return;
		}
		$.ajax({
			url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
			data:{idcardOrRe:card_value},
			type:'post',
			async:false,
			success: function(data) {
				if(data==null||data==''){
					$.messager.alert('提示','此卡号无效');
					return;
				}
				$('#medicalrecord').textbox('setValue',data);
				query();
			}
		});
	};
	/*******************************结束读身份证***********************************************/
	
	</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
<div id="loader-wrapper">
		<div id="loader"></div>
		<div class="loader-section section-left"></div>
		<div class="loader-section section-right"></div>
		<div class="load_title"><br><span>Loading...</span></div>
	</div>
	<div id="cc" class="easyui-layout" style="width: 100%; height: 100%;">
		<div data-options="region:'west',split:true,border:false" style="height:100%;width: 360px">
			<div id="ee" class="easyui-layout" style="width:100%;height:100%;"  data-options="fit:true">
			    <div data-options="region:'north',iconCls:'icon-reload',split:false" style="width:360px;height:55px;overflow-y:hidden;border-top:0">
			    	<div style="padding: 3px 5px 4px 5px;">
			    		<table style="width:340px;padding:5px;">
			    			<tr>
			    				<td>
									<input id="medicalrecord" class="easyui-textbox" style="width:150PX" data-options="prompt:'病历号'"/>
									  <shiro:hasPermission name="${menuAlias}:function:readCard"> 
									  	<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
									 </shiro:hasPermission>
									 <shiro:hasPermission name="${menuAlias}:function:readIdCard">
									 	<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
									 </shiro:hasPermission> 
									</td>
			    			</tr>
			    			
			    		</table>
			    	</div>
			    </div> 
			    <div data-options="region:'center',split:false,doSize:false"
					style="width: 100%;height:84%;">
						<ul id="tDt"></ul>
				</div>
			</div>  
		</div>  
		<div data-options="region:'center',split:false,border:true" style="height:100%;width: 87%;border-top:0">
			<div id="dd" class="easyui-layout"  style="width:100%;height:100%;">   
				<div data-options="region:'north',split:false,collapsible:false,border:false" style="width:100%;height:94px;padding:5px 5px 0px 5px;" >
					<table class="honry-table" style="width: 100%;height: 80px">
					  <tr>
					    <td class="honry-lable"style="width:10%">病历号：</td><td id="blh" style="width:10%"/></td>
					    <td class="honry-lable"style="width:10%">姓名：</td><td id="xm" style="width:10%"/></td>
					    <td class="honry-lable"style="width:10%">年龄：</td><td id="nl" style="width:10%"/></td>
					     <td class="honry-lable"style="width:10%">主治医生：</td><td id="zzys" style="width:10%"/></td>
					    <td class="honry-lable"style="width:10%">出生日期：</td><td id="csrq" style="width:10%"/></td>
					  </tr>
					  <tr>
					  	<td class="honry-lable"style="width:10%">过敏史：</td><td id="gm" style="width:10%"/></td>
					  	<td class="honry-lable"style="width:10%">类别：</td><td id="lb" style="width:10%"/></td>
					  	<td class="honry-lable"style="width:10%">科室：</td><td id="ks" style="width:10%"/></td>
					  	<td class="honry-lable"style="width:10%">是否重大疾病：</td><td id="zdjb" style="width:10%"/></td>
					  	<td class="honry-lable"style="width:10%">性别：</td><td id="xb" style="width:10%"/></td>
					  </tr>
					</table>
			    </div>   
				<div class="OperationAppliDateSize" data-options="region:'center',split:false" style="padding: 3px; width: 100%;height:85%;border-left:0" >
					<form id="editform" method="post">
						<div id="addData-window"></div><input type="hidden" id="operationId" name="operationApply.id">
						<table  style="width: 100%; border-collapse:separate;border-spacing:10px;" id='sq' >
							<tr>
							    <td  style="text-align: right;width: 15%width: 15%" nowrap="nowrap">手术室：<input type="hidden" name="operationApply.pasource" value="1"></td>
								<td style="width: 15%"><input class="easyui-combobox" id="execDept"
									name="operationApply.execDept" data-options="required:true"></td> 
								<td  style="text-align: right;width: 15%" nowrap="nowrap">是否自定义手术：</td>
							    <td style="width: 15%">
							  		<div  style="float:left;width: 5%;height: 22px;padding-top: 4px" >
								  		<input type="checkbox" id="operation"name="operation" onclick="ssmcdy()" >
									</div>
									<div style="float:left;width:95%;height: 22px" onclick="kdfw('operation')"></div>
							    </td>
		                        <td  style="text-align: right;width: 15%" nowrap="nowrap">是否自定义诊断：</td>
							    <td style="width: 15%">
							   		<div  style="float:left;width: 5%;height: 22px;padding-top: 4px" >
								   		<input type="checkbox" id="operationZD"name="operationZD" onclick="sszd()" >
									</div>
									<div style="float:left;width:95%;height: 22px" onclick="kdfw('operationZD')"></div>
								</td>
							</tr>
							<tr id='trsqzd0'>
							    <td  style="text-align: right;width: 15%" nowrap="nowrap">术前诊断1：<input type="hidden" id="zd0"></td>
								<td colspan="5" style="width: 75%"><input class="easyui-combogrid" data-options="required:true" id="shoushuzd0_"name="diagName0"  style="width: 92%" >
								<a href="javascript:void(0)" id="ashoushuzd0" onclick="add('术前诊断','trsqzd','0','shoushuzd','diagName','ashoushuzd')" class="easyui-linkbutton" data-options="iconCls:'icon-add'"></a></td>
							</tr>
							<tr id='trndss0'>
								<td  style="text-align: right;width: 15%" nowrap="nowrap">拟手术名称1：<input type="hidden" id="mc0"></td>
								<td colspan="5" style="width: 75%"><input class="easyui-combogrid" id="nssmc0_" name="itemName0"  data-options="required:true" style="width: 92%"> 
								  <a href="javascript:void(0)" id="anssmc0" onclick="add('拟手术名称','trndss','0','nssmc','itemName','anssmc')" class="easyui-linkbutton" data-options="iconCls:'icon-add'"></td>
							</tr>
							<tr>
								<td  style="text-align: right;width: 15%" nowrap="nowrap">预约时间：</td>
								<td style="width: 15%">
								<input id="nishoushu" name="operationApply.preDate" class="Wdate" type="text"  
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:00',onpicked:preDatePicked,minDate:'%y-%M-%d',maxDate:'{%y+1}-%M-%d'})"
								style="width:175px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								<td  style="text-align: right;width: 15%" nowrap="nowrap">预定用时：</td>
								<td style="width: 15%"><input id="duration" name="operationApply.duration" class="easyui-numberbox" 
								data-options="required:true,showSeconds:false,min:0,precision:1"></td>         
							   <td style="text-align: right;width: 15%" nowrap="nowrap">手术体位：</td>
								<td style="width: 15%"><input id="opertionposition" name="operationApply.opertionposition" class="easyui-combobox" ></td>
							</tr>
							<tr>
							    <td  style="text-align: right;width: 15%" nowrap="nowrap">手术医生科室：</td>
								<td style="width: 15%"><input id="opDoctordept" data-options="required:true" name="operationApply.opDoctordept" class="easyui-combobox" >
								</td>
							    <td  style="text-align: right;width: 15%" nowrap="nowrap">手术医生 ：</td>
								<td style="width: 15%"><input id="ssysbm" data-options="required:true" name="operationApply.opDoctor" class="easyui-combobox" ></td>
							    <td  style="text-align: right;width: 15%" nowrap="nowrap">指导医生 ：</td>
								<td style="width: 15%"><input id="guiDoctor" name="operationApply.guiDoctor" class="easyui-combobox" ></td>
							</tr>
							<tr>
								<input type="hidden" id="isneedprep1" name="operationApply.isneedprep"></td>
								<td  style="text-align: right;width: 15%" nowrap="nowrap">是否需要随台护士：</td>
								<td style="width: 15%">
								<div  style="float:left;width: 5%;height: 22px;padding-top: 4px" >
									<input id="isneedacco"  type="checkbox" onclick="isneedaccoabc()">
								</div>
								<div style="float:left;width:95%;height: 22px" onclick="kdfw('isneedacco')"></div>
								<input type="hidden" id="isneedacco1" name="operationApply.isneedacco">
								</td>
						    	<td  style="text-align: right;width: 15%">随台护士数：</td>
								<td style="width: 15%"> <input id="accoNurse" name="operationApply.accoNurse" class="easyui-numberbox" readonly="readonly"></td>
								<td  style="text-align: right;width: 15%" nowrap="nowrap">是否需要巡回护士：</td>
								<td style="width: 15%">
								<div  style="float:left;width: 5%;height: 22px;padding-top: 4px" >
									<input id="isneedprep"  type="checkbox" onclick="isneedprepabc()">
								</div>
								<div style="float:left;width:95%;height: 22px" onclick="kdfw('isneedprep')"></div>
								</td>
							</tr>
							<tr>
								<td  style="text-align: right;width: 15%">助手数：</td>
								<td style="width: 15%"><input id="helperNum" name="operationApply.helperNum" class="easyui-numberbox" ></td>
								<td  style="text-align: right;width: 15%" nowrap="nowrap">洗手护士数：</td>
								<td style="width: 15%"><input id="washNurse" name="operationApply.washNurse" class="easyui-numberbox" ></td>
								<td  style="text-align: right;width: 15%">巡回护士数：</td>
								<td style="width: 15%"><input id="prepNurse" name="operationApply.prepNurse" class="easyui-numberbox" readonly="readonly"></td>
							</tr>
							<tr id='trzs0'>
								<td  style="text-align: right;width: 15%" nowrap="nowrap">助手1：</td>
								<td style="width: 15%"><input id="zhushou0_" name="thelper" class="easyui-combobox" readonly="readonly" disabled="disabled">
								</td>
								<td  style="text-align: right;width: 15%" nowrap="nowrap">洗手护士1：<input type="hidden" id="xs0"></td>
								<td style="width: 15%"><input id="xishou0_" name="wash" class="easyui-combogrid" readonly="readonly" disabled="disabled" >
								</td>
								<td  style="text-align: right;width: 15%" nowrap="nowrap">巡回护士1：<input type="hidden" id="xh0"></td>
								<td style="width: 15%"><input id="xunhui0_" name="tour" class="easyui-combogrid" readonly="readonly" disabled="disabled" >
								<a href="javascript:void(0)" id="afz0" onclick="addfz('trzs','0','afz')" class="easyui-linkbutton" data-options="iconCls:'icon-add'">
								</td>
							</tr>
							<tr>
							    <td  style="text-align: right;width: 15%" nowrap="nowrap">切口类型：</td>
								<td style="width: 15%"><input id="inciType" name="operationApply.inciType" class="easyui-textbox" ></td>
							    <td  style="text-align: right;width: 15%" nowrap="nowrap">感染类型：</td>
								<td style="width: 15%"><input id="infectType" name="operationApply.infectType" class="easyui-combobox" ></td>
								<td  style="text-align: right;width: 15%" nowrap="nowrap">手术注意事项：</td>
								<td style="width: 15%"><input id="opsNote" name="operationApply.opsNote" class="easyui-textbox" ></td>
							</tr>
							<tr>
							    <td  style="text-align: right;width: 15%">临床表现：</td>
								<td style="width: 15%"><input id="clinical" name="operationApply.clinical" class="easyui-textbox" ></td>
							    <td  style="text-align: right;width: 15%">手术禁忌症：</td>
								<td style="width: 15%"><input id="contraindication" name="operationApply.contraindication" class="easyui-textbox" ></td>
								<td  style="text-align: right;width: 15%">手术适应症：</td>
								<td style="width: 15%"><input id="indication" name="operationApply.indication" class="easyui-textbox" ></td>
							</tr>
							<tr>
							    <td  style="text-align: right;width: 15%">目前病人情况：</td>
								<td style="width: 15%"><input id="stitution" name="operationApply.stitution" class="easyui-textbox" ></td>
							    <td  style="text-align: right;width: 15%">术前准备情况：</td>
								<td style="width: 15%"><input id="preparation" name="operationApply.preparation" class="easyui-textbox" ></td>
								<td  style="text-align: right;width: 15%">可能的并发症：</td>
								<td style="width: 15%"><input id="complication" name="operationApply.complication" class="easyui-textbox" ></td>
							</tr>
							<tr>
							    <td  style="text-align: right;width: 15%">签字家属：</td>
								<td style="width: 15%"><input id="folk" name="operationApply.folk"class="easyui-textbox" data-options="required:true"></td>
							    <td  style="text-align: right;width: 15%">家属关系：</td>
								<td style="width: 15%"><input id="relaCode" name="operationApply.relaCode" class="easyui-combobox" ></td>
								<td  style="text-align: right;width: 15%">家属意见：</td>
								<td style="width: 15%"><input id="folkComment" name="operationApply.folkComment" class="easyui-textbox" ></td>
							</tr>
							<tr>
							    <td  style="text-align: right;width: 15%" nowrap="nowrap">手术规模：</td>
								<td style="width: 15%"><input id="degree" name="operationApply.degree" class="easyui-combobox" ></td>
								<td style="text-align: right;width: 15%" nowrap="nowrap">手术分类：</td>
								<td style="width: 15%"><input id="opType" data-options="required:true" name="operationApply.opType"  class="easyui-combobox" ></td>
								<td  style="text-align: right;width: 15%" nowrap="nowrap">台类型：</td>
								<td style="width: 15%"><input id="consoleType" name="operationApply.consoleType" class="easyui-combobox">
								    <input id="patientName" name="operationApply.name" type="hidden" >
									<input id="sex" name="operationApply.sex" type="hidden">
									<input id="reportAge" name="operationApply.age" type="hidden">
									<input id="paykindCode" name="paykindCode" type="hidden">
									<input id="deptCode" name="operationApply.inDept" type="hidden">
									<input id="inp" name="operationApply.clinicCode" type="hidden">
									<input id="medicalrecord1" name="operationApply.patientNo" type="hidden">
									<input id="inSource" name="inSource" type="hidden">
									<input id="birthday" name="operationApply.birthday" type="hidden">
								</td>
							</tr>
							    <td  style="text-align: right;width: 15%" nowrap="nowrap">麻醉类型：</td>
								<td style="width: 15%"><input id="anesType" name="operationApply.anesType" class="easyui-combobox"></td>
								<td  style="text-align: right;width: 15%" nowrap="nowrap">麻醉方式：</td>
								<td style="width: 15%"><input id="aneWay" name="operationApply.aneWay" class="easyui-combobox"
									></td>
							    <td  style="text-align: right;width: 15%" nowrap="nowrap">是否加急：</td>
								<td style="width: 15%">
									<div  style="float:left;width: 5%;height: 22px;padding-top: 4px" >
										<input id="isurgent"  type="checkbox" onclick="isurgentabc()">
									</div>
									<div style="float:left;width:95%;height: 22px" onclick="kdfw('isurgent')"></div>
								<input type="hidden" name="operationApply.isurgent" id="isurgent1" >
								</td>
							</tr>
							<tr>
							    <td  style="text-align: right;width: 15%" nowrap="nowrap">特殊手术：</td>
							    <td style="width: 15%">
							    <div  style="float:left;width: 5%;height: 22px;padding-top: 4px" >
								    <input type="checkbox" id="isspecial"  onclick="isspecialabc()">
								</div>
								<div style="float:left;width:95%;height: 22px" onclick="kdfw('isspecial')"></div>
							    <input type="hidden" name="operationApply.isspecial" id="isspecial1">
								</td>
							    <td style="text-align: right;width: 15%" nowrap="nowrap">是否需要病理检查：</td>
								<td style="width: 15%">
									<div  style="float:left;width: 5%;height: 22px;padding-top: 4px" >
										<input id="isneedpathology"  type="checkbox" onclick="isneedpathologyabc()">
									</div>
									<div style="float:left;width:95%;height: 22px" onclick="kdfw('isneedpathology')"></div>
									<input type="hidden" name="operationApply.isneedpathology" id="isneedpathology1">
								</td>
								<td style="text-align: right;width: 15%" nowrap="nowrap">是否自体血回输：</td>
								<td style="width: 15%">
									<div  style="float:left;width: 5%;height: 22px;padding-top: 4px" >
										<input id="isautoblood"  type="checkbox" onclick="isautobloodabc()">
									</div>
									<div style="float:left;width:95%;height: 22px" onclick="kdfw('isautoblood')"></div>
									<input type="hidden" name="operationApply.isautoblood" id="isautoblood1">
								</td>
							</tr>
							<tr>
							    <td  style="text-align: right;width: 15%" nowrap="nowrap">是否有菌：</td>
							    <td style="width: 15%">
							    	<div  style="float:left;width: 5%;height: 22px;padding-top: 4px" >
								    	<input type="checkbox" id="isgerm"  onclick="isgermabc()">
									</div>
									<div style="float:left;width:95%;height: 22px" onclick="kdfw('isgerm')"></div>	
							    	<input type="hidden" name="operationApply.isgerm" id="isgerm1">
								</td>
								<td  style="text-align: right;width: 15%" nowrap="nowrap">是否重症：</td>
								<td style="width: 15%">
								<div  style="float:left;width: 5%;height: 22px;padding-top: 4px" >
									<input id="isheavy"  type="checkbox" onclick="isheavyabc()">
								</div>
								<div style="float:left;width:95%;height: 22px" onclick="kdfw('isheavy')"></div>
								<input type="hidden" name="operationApply.isheavy" id="isheavy1">
								</td>
							</tr>
							
						</table>
						<div >
							<a id="shenpi"  class="easyui-linkbutton" data-options="iconCls:'icon-add'">审批</a>
						</div>
						<div id="spxx" data-options="title:'审批信息'," style="width: 100%;display: none;" >
								 <table style="width: 100%;" class="honry-table">
										<tr >
											<td class="honry-lable"style="width: 15%;">一级审批人：</td>
											<td style="width: 17%"><input class="easyui-combobox" id="apprDoctor" name="operationApply.apprDoctor" ></td>
											<td class="honry-lable"style="width: 15%;">二级审批人：</td>
											<td style="width: 17%"><input class="easyui-combobox" id="apprDoctor2" name="operationApply.apprDoctor2" ></td>
											<td class="honry-lable"style="width: 15%;">三级审批人：</td>
											<td style="width: 17%"><input class="easyui-combobox" id="apprDoctor3" name="operationApply.apprDoctor3" ></td>
										</tr>
										<tr>
											<td class="honry-lable"style="width: 15%;">一级审批时间：</td>
											<td style="width: 17%">
											<input id="apprDate" name="operationApply.apprDate" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width: 67%;border: 1px solid #95b8e7;border-radius: 5px;"/>
											</td>
											<td class="honry-lable"style="width: 15%;">二级审批时间：</td>
											<td style="width: 17%">
											<input id="apprDate2" name="operationApply.apprDate2" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width: 67%;border: 1px solid #95b8e7;border-radius: 5px;"/>
											</td>
											<td class="honry-lable"style="width: 15%;">三级审批时间：</td>
											<td style="width: 17%">
											<input id="apprDate3" name="operationApply.apprDate3" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width: 67%;border: 1px solid #95b8e7;border-radius: 5px;"/>
											</td>
										</tr>
										<tr>
											<td class="honry-lable"style="width: 15%;">一级审批备注：</td>
											<td style="width: 17%"><input  id="apprRemark" name="operationApply.apprRemark" style="height: 30px;width: 67%"></td>
											<td class="honry-lable"style="width: 15%;">二级审批备注：</td>
											<td style="width: 17%"><input  id="apprRemark2" name="operationApply.apprRemark2" style="height: 30px;width: 67%"></td>
											<td class="honry-lable"style="width: 15%;">三级审批备注：</td>
											<td style="width: 17%;"><input  id="apprRemark3" name="operationApply.apprRemark3" style="height: 30px;width: 67% "></td>
										</tr>
								</table>
						</div>
						<div >
							<table>
								<tr>
									<td nowrap="nowrap">特殊说明：</td>
								</tr>
							</table>
						</div>
							<input id="getdate" name="operationApply.applyRemark" class="easyui-textbox"
								 style="height: 50px; width: 100%;">
						
						<div>
						    <table>
							    <tr>
								    <td style="color: red;" nowrap="nowrap" id="tdAppDate"></td>
								</tr>
							</table>
						</div>
		                <div style="text-align: right;">
							 同意使用自费项目：<input type="checkbox" id="zifei"onclick="sfzf()">
								 <input type="hidden" id="zifei1" name="operationApply.isownexpense">
						</div>
						<div style="text-align: right;">
							申请医生： <input id="sqysbm" value="${cname}" style="width: 100px;"
								data-options="required:true,showSeconds:false" name="operationApply.applyDoctor" class="easyui-combobox" >
								<input type="hidden"  id="applyDoctor">
								<a href="javascript:delSelectedData('sqysbm');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							申请日期：<input id="riqqi" name="operationApply.applyDate" class="Wdate" type="text" value="${cnow}"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:00',required:true})" style="width:155px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						</div>
						<div style="text-align: center;padding:10px;fit:true" >
							<shiro:hasPermission name="${menuAlias}:function:save">
								<a href="javascript:void(0)" class="easyui-linkbutton"
									onclick="submitop()" style="text-align: right;">保存</a>
							</shiro:hasPermission>
							<a href="javascript:void(0)" class="easyui-linkbutton"
									onclick="qingss()" style="text-align: right;">重置</a>
							<shiro:hasPermission name="${menuAlias}:function:delete">
								<a href="javascript:void(0)" id="del" onclick="del()"
									class="easyui-linkbutton" style="text-align: right;">作废</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:print">
							<a href="javascript:void(0)" onclick="printZQTYS()"
									class="easyui-linkbutton" style="text-align: right;" disabled="true">打印知情同意书</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:print">
							<a href="javascript:void(0)" id="print" onclick="printSSSQD()"
								 class="easyui-linkbutton"  style="text-align: right;" >打印</a>
							</shiro:hasPermission>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
		<div id="ssxxxx" class="easyui-dialog" title="手术信息" style="width:670;height:40%" data-options="modal:true, closed:true">
		<table id="list" class="easyui-datagrid" data-options="fit:true"  >
			<thead>
				<th data-options="field:'id',width:100,hidden:true,align:'center'">手术序号</th>
				<th data-options="field:'patientNo',width:100,align:'center'">病历号</th>
				<th data-options="field:'name',width:100,align:'center'">患者姓名</th>
				<th data-options="field:'opNameList',width:200,align:'center',formatter:functionopNameList">手术名称</th> 
				<th data-options="field:'preDate',width:150,align:'center'">手术时间</th>   
				<th data-options="field:'opDoctor',width:100,align:'center',formatter:functiondoctor">手术医生</th>
			</thead>
		</table>
	</div>
	
	<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:500;height:500;padding:30 60 40 60" data-options="modal:true, closed:true">   
		     <table id="infoDatagrid"  style="width:400px;height:400" data-options="fitColumns:true,singleSelect:true">   
		</table>  
    </div>
</body>
<script type="text/javascript">
/**
 * @Description：预约时间 查询当天是否还有手术台
 * @Author：hedong
 * @CreateDate：2017-3-23
 */
function queryIfHaveConsole(){
	var opem=$("#execDept").combobox("getValue");
	if(opem!="" && opem!=null){
		var none=$("#nishoushu").val();
		var da=none.substring(0,10);
		 $.ajax({
			 url:"<%=basePath%>operation/operationList/querydate.action",
			 data:{cnow:da,dept:opem},
			 type:"post",
			 success:function(data){
				 if(data=="1"){
					$("#consoleType").combobox("select","02");
				 }else if(data=="2"){
					 $("#consoleType").combobox("select","01");
				 }else{
					 $("#consoleType").combobox('setValue','');
					 $.messager.alert("提示","该手术室没有该科室的手术台");
					 setTimeout(function(){
							$(".messager-body").window('close');
					 },3000);
				 }
			 }
		 });
	}else{
		$.messager.alert("提示","请先选择手术室");
		setTimeout(function(){
			$(".messager-body").window('close');
		},2000);
		$("#nishoushu").val("");
		return false;
	}
}
/**
 * @Description：预约时间 增加新日历空间选中事件
 * @Author：hedong
 * @CreateDate：2017-3-23
 */
function preDatePicked(){
	queryIfHaveConsole();
}

/**
 * @Description：预约时间手动输入时触发
 * @Author：hedong
 * @CreateDate：2017-3-23
 */
$("#nishoushu").change(function() { 
	 if($("#nishoushu").val()){
		if($("#nishoushu").val().length<19){
			return;
		}
		queryIfHaveConsole();
	 }
   }
);

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
</html>