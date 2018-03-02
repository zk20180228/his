<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>手术安排</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/themes/system/css/loader.css">
	<%@ include file="/common/metas.jsp"%>
	
	<script type="text/javascript">
	 var menuAlias="${menuAlias}";
		//定义全局变量
		var opNameMap = "";//手术名称
		var xunHuiMap = "";//巡回
		var xiShouMap = "";//洗手
		var zhuShouMap = "";//助手
		var aneList = "";//麻醉方式
		var opDocMap = new Map();//渲染医生
		var deptMap="";//渲染科室
		var opjianMap=new Map();//手术间
		var optainMap=new Map();//手术台
		var roomMap= new Map();//渲染手术室
		var userhs=null;//护士
		var user=null;//医生
		var sqzday=null;//诊断信息
		var deptCode="${deptCode}";//当前登录科室code
		var sexMap=new Map();//性别
		var indexfz=1;//护士数量
		var nurseMap=new Map();//护士翻页渲染
		var operatNameMap=new Map();//手术名称翻页渲染
		$(function(){
			/**
			 * 渲染医生
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
				url:"<%=basePath %>operation/operationList/ssComboboxList.action",
				type:"post",
				success : function(data){
					user=data;
					if(data!=null&&data!=""){
						for(var i=0;i<data.length;i++){
							opDocMap.put(data[i].jobNo,data[i].name);
						}
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
			
			/**
			 * 渲染手术室
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
				url : '<%=basePath %>operation/operationList/querysysDeptmentShi.action',
				type:"post",
				success:function(data){
					if(data!=null&&data!=""){
						for(var i=0;i<data.length;i++){
							roomMap.put(data[i].deptCode,data[i].deptName);
						}
					}
				}
			});
			/**
			 * 手术间
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
				url:'<%=basePath%>operation/arrangement/queryOproom.action',
				success:function(data){
					var opjianlist = data;
					for(var i=0;i<opjianlist.length;i++){
						opjianMap.put(opjianlist[i].id,opjianlist[i].roomName);
					}
				}
			});
			/**
			 * 手术台
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
				url:'<%=basePath%>operation/arrangement/queryconsole.action',
				success:function(data){
					var optaiList = data;
					for(var i=0;i<optaiList.length;i++){
						optainMap.put(optaiList[i].consoleCode,optaiList[i].consoleName);
					}
				}
			});
			/**
			 * 麻醉方式
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
				url : '<%=basePath %>operation/operationList/likeAneway.action',
				success: function(aneData) {
					aneList = aneData;
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
			//护士渲染
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
				
			//手术名称翻页渲染
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
			 * 手术房间
			 * @Author: zhangjin
			 * @CreateDate: 2017年2月17日
			 * @param:
			 * @return:
			 * @Modifier:
			 * @ModifyDate:
			 * @ModifyRmk:
			 * @version: 1.0
			 */
			  $("#roomId").combobox({
				url:"<%=basePath%>operation/arrangement/queryroomId.action?deptCode="+deptCode,
				editable : false,
			    valueField:'id',    
			    textField:'roomName',
			    onSelect:function(){
			    	var val=$(this).combobox("getValue");
			    	if(val){
			    		$('#console').combobox("clear");
			    		$('#console').combobox("reload",
								"<%=basePath%>operation/arrangement/getConsoleValid.action?deptCode="+val);
			    	}
			    }
			});
			/**
			 * 手术类别
			 * @Author: zhangjin
			 * @CreateDate: 2017年2月17日
			 * @param:
			 * @return:
			 * @Modifier:
			 * @ModifyDate:
			 * @ModifyRmk:
			 * @version: 1.0
			 */
			$('#opType').combobox({
				url: '<%=basePath%>operation/operationList/queryCodeOperatetype.action',
				editable : false,
			    valueField:'encode',    
			    textField:'name'
			});
			/**
			 * 手术台类别
			 * @Author: zhangjin
			 * @CreateDate: 2017年2月17日
			 * @param:
			 * @return:
			 * @Modifier:
			 * @ModifyDate:
			 * @ModifyRmk:
			 * @version: 1.0
			 */
			$('#consoleType').combobox({
				url : '<%=basePath %>operation/operationList/CodeconsoleType.action',
				editable : false,
				valueField:'encode',    
			    textField:'name'
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
			 * 麻醉类别
			 * @Author: zhangjin
			 * @CreateDate: 2017年2月17日
			 * @param:
			 * @return:
			 * @Modifier:
			 * @ModifyDate:
			 * @ModifyRmk:
			 * @version: 1.0
			 */
			$('#anesType').combobox({
				url:"<%=basePath%>operation/operationList/queryCodeanesType.action",
				valueField:'encode',    
			    textField:'name',
				editable : true,
			 	filter:function(q,row){//hedong 20170309 增加科室过滤
					 var keys = new Array();
					 keys[keys.length] = 'name';//部门名称
					 keys[keys.length] = 'encode';//系统编号
					 keys[keys.length] = 'pinyin';//部门拼音
					 keys[keys.length] = 'wb';//部门五笔
				     keys[keys.length] = 'inputCode';//自定义码
					return filterLocalCombobox(q, row, keys);
				}
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
			 * 麻醉方式
			 * @Author: zhangjin
			 * @CreateDate: 2017年2月17日
			 * @param:
			 * @return:
			 * @Modifier:
			 * @ModifyDate:
			 * @ModifyRmk:
			 * @version: 1.0
			 */
			$('#aneWay').combobox({
				url : '<%=basePath %>operation/operationList/likeAneway.action',
				valueField : 'encode',
				textField : 'name',
				editable : false,
			});
			
			
			/**
			 * 手术名称
			 * @Author: zhangjin
			 * @CreateDate: 2017年2月17日
			 * @param:
			 * @return:
			 * @Modifier:
			 * @ModifyDate:
			 * @ModifyRmk:
			 * @version: 1.0
			 */
			$('#itemName0_').combogrid({
				 url : '<%=basePath%>operation/operationList/undrugComboboxfy.action',
				    idField:'code',    
				    textField:'name',
				    mode:"remote",
					pageList:[10,20,30,40,50],
				 	pageSize:"10",
				 	panelWidth:325,
				 	pagination:true,
					columns:[[    
					         {field:'code',title:'编码',width:'120'},    
					         {field:'name',title:'名称',width:'160'}    
					     ]],  
					 onHidePanel:function(none){
					 	var val = $(this).combogrid('getText');
				    	//校验手术名称是否存在
					 	if(nssmcName1(val)){
				    		$(this).combogrid('clear');
					    	$.messager.alert("提示","手术名称不能重复！","info");
					    	setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
					    }
				    	//判断该手术是否为自定义手术
				    	var mc = $('[id^=itemName]');
				 	    var b=0;
				 	    var c=0;
					 	mc.each(function(index,obj){
					 		if($(obj).combogrid('getText') != $(obj).combogrid('getValue')&&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
					 			b++;
					 		}
					 		if($(obj).combogrid('getText') == $(obj).combogrid('getValue')&&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
					 			c++;
					 		}
					 	});
					 	var value = $(this).combogrid('getValue');
				 	     if(val==value&&value!=null&&value!=""){
				 	    	 if(b>0){
				 	    		$.messager.alert("提示","该手术为非自定义手术，请选择下拉列表里的数据！","info");
				 	    		setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
				 	    		$(this).combogrid('clear');
				 	    	 }
				 	     }else{
				 	    	if(c>0){
				 	    		$.messager.alert("提示","该手术为自定义手术，请输入下拉列表不存在的数据！","info");
				 	    		setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
				 	    		$(this).combogrid('clear');
				 	    	 }
				 	     }
				    	
				 	},
				    onLoadSuccess: function () {
				    		//翻页渲染
				    	    var id=$(this).prop("id");
				            if ($("#"+id).combogrid('getValue')&&operatNameMap.get($("#"+id).combogrid('getValue'))) {
			            	$("#"+id).combogrid('setText',operatNameMap.get($("#"+id).combogrid('getValue')));
			            }
				     //分页工具栏作用提示
						var p = $(this).combogrid('grid');
						var pager = p.datagrid('getPager');
						var aArr = $(pager).find('a');
						var iArr = $(pager).find('input');
						$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1,position:'top',deltaX:1000});
						for(var i=0;i<aArr.length;i++){
							$(aArr[i]).tooltip({
								content:toolArr[i],
								hideDelay:1
							});
							$(aArr[i]).tooltip('hide');
						}

						
			        }  
			});
		
			/**
			 * 巡回护士
			 * @Author: zhangjin
			 * @CreateDate: 2017年2月17日
			 * @param:
			 * @return:
			 * @Modifier:
			 * @ModifyDate:
			 * @ModifyRmk:
			 * @version: 1.0
			 */
			$('#xunHui0_').combogrid({
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
						//校验护士信息是否重复
				    	if(xunhuiName1(val)){
				    		$(this).combogrid('clear');
					    	$.messager.alert("提示","护士信息不能重复！","info");
					    	setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
					    }
			     },
			     onLoadSuccess: function () {
			    	 //翻页渲染
						    var id=$(this).prop("id");
						    if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
			            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
			           } 
				      //分页工具栏作用提示
						var p = $(this).combogrid('grid');
						var pager = p.datagrid('getPager');
						var aArr = $(pager).find('a');
						var iArr = $(pager).find('input');
						$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1,position:'top',deltaX:1000});
						for(var i=0;i<aArr.length;i++){
							$(aArr[i]).tooltip({
								content:toolArr[i],
								hideDelay:1
							});
							$(aArr[i]).tooltip('hide');
						}
			      } 
			});
			/**
			 * 洗手护士
			 * @Author: zhangjin
			 * @CreateDate: 2017年2月17日
			 * @param:
			 * @return:
			 * @Modifier:
			 * @ModifyDate:
			 * @ModifyRmk:
			 * @version: 1.0
			 */
			$('#xiShou0_').combogrid({
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
				    	 //校验护士信息是否重复
						var val = $(this).combogrid('getValue');
				    	if(xishouName1(val)){
				    		$(this).combogrid('clear');
					    	$.messager.alert("提示","护士信息不能重复！","info");
					    	setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
					    	
					    }
			     },
			     onLoadSuccess: function () {
			    	 //翻页渲染
					    var id=$(this).prop("id");
				        if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
			            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
			             } 
					      
					      //分页工具栏作用提示
							var p = $(this).combogrid('grid');
							var pager = p.datagrid('getPager');
							var aArr = $(pager).find('a');
							var iArr = $(pager).find('input');
							$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1,position:'top',deltaX:1000});
							for(var i=0;i<aArr.length;i++){
								$(aArr[i]).tooltip({
									content:toolArr[i],
									hideDelay:1
								});
								$(aArr[i]).tooltip('hide');
							}
				  }  
			});
			
			/**
			 * 手术台
			 * @Author: huangbiao
			 * @CreateDate: 2016年4月16日
			 * @param:
			 * @return:
			 * @Modifier:
			 * @ModifyDate:
			 * @ModifyRmk:
			 * @version: 1.0
			 */
			 if(deptCode!=null&&deptCode!=''){
				 $('#console').combobox({
						url:"<%=basePath%>operation/arrangement/getConsoleValid.action",
						editable : false,
						valueField:'consoleCode',    
					    textField:'consoleName',
					    onSelect : function() {
					    	var room=$("#roomId").combobox("getValue");
					    }
					});
			 }
			 
			setTimeout(function(){
				/**
				 * 手术医生
				 * @Author: huangbiao
				 * @CreateDate: 2016年4月16日
				 * @param:
				 * @return:
				 * @Modifier:
				 * @ModifyDate:
				 * @ModifyRmk:
				 * @version: 1.0
				 */
				$('#opDoctor').combobox({
					 	valueField : 'jobNo',
					 	textField : 'name',
						editable : true,
					    data:user,
					    onSelect : function() {
					    	var id=$(this).prop("id");
				    		var value=$("#"+id).combobox("getValue");
				    		var zs=$('[id^=zsDoc]');
				    		//校验手术医生与助手是否重复
				    		zs.each(function(){
				    			var zsval=$(this).combobox("getValue");
				 	 			if(zsval!=null&&zsval!=""){
				 	 				if(value==zsval){
				 	 					$("#"+id).combobox("clear");
				 	 	 				$.messager.alert("提示","手术医生与助手医生不能相同！","info");
				 	 	 				setTimeout(function(){
											$(".messager-body").window('close');
										},2500);
				 	 	 				return ;
				 	 	 			}
				 	 			}
				    		});
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
						},
						onLoadSuccess:function(){
					    
					    }

					});
				/**
				 * 助手医生渲染
				 * @Author: huangbiao
				 * @CreateDate: 2016年4月16日
				 * @param:
				 * @return:
				 * @Modifier:
				 * @ModifyDate:
				 * @ModifyRmk:
				 * @version: 1.0
				 */
				$('#zsDoc0_').combobox({
					editable : true,
					valueField:'jobNo',    
				    textField:'name',
				    data:user,
				    onSelect : function() {
				    	//校验助手之间是否重复
				    	var zsDoc=$('[id^=zsDoc]');
					    var id=$(this).prop("id");
					    var ssysbms=$('#opDoctor').combobox("getValue");
					  	var b = 0;
					  	var zsys=$(this).combobox("getValue");
					  	zsDoc.each(function(index,obj){
					 		if($(obj).combogrid('getValue') == zsys){
					 			b++;
					 		}
					 		
					 	});
				    	if(b>1){
				    		$('#'+id).combobox('clear');
	    					$.messager.alert("提示","助手重复!","info");
	    					setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
				    	}
				    	//校验手术医生与助手之间是否重复
				    	if(ssysbms!=null&&ssysbms!=""){
		 	 				if(zsys==ssysbms){
		 	 					$("#"+id).combobox("clear");
		 	 	 				$.messager.alert("提示","手术医生与助手医生不能相同");
		 	 	 				setTimeout(function(){
									$(".messager-body").window('close');
								},2500);
		 	 	 				return
		 	 	 			}
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
				 * @Description:页面加载
				 * @Author: zhangjin
				 * @CreateDate: 2016年5月23日
				 * @param:
				 * @return:
				 * @Modifier:
				 * @ModifyDate:
				 * @ModifyRmk:
				 * @version: 1.0
				 */
				$('#list').datagrid({
					method:'post',
					url:"<%=basePath%>operation/arrangement/queryOperationInfoList.action",
					queryParams:{dt:"1",fore:3},
					pagination : true,
					pageSize : 20,
					pageList : [ 20, 30, 50, 100 ],
					styler:function(index,row){
						if (row.status=="3"){   
				            return 'background-color:#ffee00;color:black;';
				        } 
					 } ,
		 			onCheck:function(rowIndex,rowData){
		 				$('#patientNo1').val(rowData.patientNo);
		 			},
		 			 onLoadSuccess:function(row, data){
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
		 				}},
					onDblClickRow:function(rowIndex,rowData){
						xinxi(rowData);
					}
				});
			},350)
		});
		
		/**
		 * @Description:查询手术安排信息
		 * @Author: huangbiao
		 * @CreateDate: 2016年4月16日
		 * @param:
		 * @return:
		 * @Modifier:
		 * @ModifyDate:
		 * @ModifyRmk:
		 * @version: 1.0
		 */
		function searchFrom(){
			 //列表信息取消选中
			$('#list').datagrid('unselectAll');
			var queryTime=$('#queryStime').val().trim();
			var endtime=$("#endtime").val().trim();
			if(!queryTime&&!endtime){
				$.messager.alert('提示','请选择日期！',"info");
				setTimeout(function(){
					$(".messager-body").window('close');
				},2000);
				return ;
			}
			$('#list').datagrid({
				method:'post',
				url:"<%=basePath%>operation/arrangement/queryOperationInfoList.action",
				queryParams:{dt:"1",date:queryTime,endtime:endtime,fore:3},
				pagination : true,
				pageSize : 20,
				pageList : [ 20, 30, 50, 100 ],
				styler:function(index,row){
					if (row.status=="3"){   
			            return 'background-color:#ffee00;color:black;';
			        } 
				 } ,
				    onCheck:function(rowIndex,rowData){
						$('#patientNo1').val(rowData.patientNo);
					},
					onDblClickRow:function(rowIndex,rowData){
						xinxi(rowData);
					}
			});
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

		//加载信息
		function xinxi(rowData){
			//清空上面表单数据
			del();
			opNameMap = new Map();//手术名称
			xunHuiMap = new Map();//巡回
			xiShouMap = new Map();//洗手
			zhuShouMap = new Map();//助手
			//给表单进行赋值
			$("#foreFlag").val(rowData.foreFlag);
			$('#operationId').val(rowData.id);
			$('#patientNo1').val(rowData.patientNo);
			$("#relaCode").combobox("setValue",rowData.relaCode);
			$("#folk").text(rowData.folk);
			$("#roomId").combobox("select",rowData.roomId);
			$("#degree").combobox("select",rowData.degree);
			$('#name').empty() && $('#name').append(rowData.name);
			$('#sex').empty() && $('#sex').append(sexMap.get(rowData.sex));
			$('#age').empty() && $('#age').append(rowData.age+rowData.ageUnit);
			$('#patientNo').empty() && $('#patientNo').append(rowData.patientNo);
			$("#anesType").combobox("select",rowData.anesType);
			$("#console").combobox("select",rowData.console);//手术台
			$('#console').combobox("reload", "<%=basePath%>operation/arrangement/getConsoleValid.action?deptCode="+rowData.roomId+"&console="+rowData.console);
			var wardNo=!rowData.wardNo?"未知":rowData.wardNo;
			var bedNo=!rowData.bedNo?"未知":rowData.bedNo;
			$('#bingqubingfang').empty() && $('#bingqubingfang').append(wardNo+"/"+bedNo);
			
			var diag = "";
			for(var i=0;i<rowData.diagnoseList.length;i++){
				if(i==rowData.diagnoseList.length-1){
					diag+=rowData.diagnoseList[i].diagName||"";
				}else{
					var diag1=rowData.diagnoseList[i].diagName||"";
					diag+=diag1+",";
				}
			}
			$('#diagnose1').empty() && $('#diagnose1').append(diag);
			$('#applyDoctor').empty() && $('#applyDoctor').append(opDocMap.get(rowData.applyDoctor));
			(((rowData.isspecial)==1) && $('#isspecial').empty() && $('#isspecial').append("是")) || ($('#isspecial').empty() && $('#isspecial').append("否"));
			$('#consoleType').combobox('select',rowData.consoleType);
			$('#opType').combobox('select',rowData.opType);
			$('#opDoctor').combobox('select',rowData.opDoctor);
			$('#aneWay').combobox('select',rowData.aneWay);
			$('#preDate').val(rowData.preDate);
			$('#remark').textbox('setValue',rowData.applyRemark);
			var opName = rowData.opNameList.length||0;
			var xunHui = rowData.xunHuiList.length||0;
			var xiShou = rowData.xiShouList.length||0;
			var zs = rowData.zsDocList.length||0;
			var max1 = ((opName>=xunHui)&&opName)||xunHui;
			var max2 = ((max1>=xiShou)&&max1)||xiShou;
			var maxLength = ((max2>=zs)&&max2)||zs;
			//动态添加行
			if(maxLength>0){
				for(var i=0;i<maxLength;i++){
					var itemName = (rowData.opNameList[i]&&(rowData.opNameList[i].itemName||""))||"";
					var xunHuiEmplCode = (rowData.xunHuiList[i]&&(rowData.xunHuiList[i].emplCode||""))||"";
					var zsEmplCode = (rowData.zsDocList[i]&&(rowData.zsDocList[i].emplCode||""))||"";
					var xiShouEmplCode = (rowData.xiShouList[i]&&(rowData.xiShouList[i].emplCode||""))||"";
					var itemId = (rowData.opNameList[i]&&(rowData.opNameList[i].id||""))||"";
					var xunHuiId = (rowData.xunHuiList[i]&&(rowData.xunHuiList[i].id||""))||"";
					var zsId = (rowData.zsDocList[i]&&(rowData.zsDocList[i].id||""))||"";
					var xiShouId = (rowData.xiShouList[i]&&(rowData.xiShouList[i].id||""))||"";
					if(i!=0){
						$("#trId"+(i-1)).after("<tr id=\"trId"+i+"\">"+
								 "<td class='TDlabel' >手术名称"+(i+1)+"：</td>"+
			    					"<td class='Input'><input id=\"itemName"+i+"_"+itemId+"\" class=\"easyui-combogrid\" name=\"itemName"+i+"\" >"+
			    					"<a id='ite"+i+"' href=\"javascript:delSelectedData('itemName"+i+"_"+itemId+"')\";></a>"+
			    					"</td>"+
			    					"<td class='TDlabel'>助手医生"+(i+1)+"：</td>"+
			    					"<td class='Input' ><input id=\"zsDoc"+i+"_"+zsId+"\" class=\"easyui-combogrid\"  name=\"zsDoc"+i+"\">"+
			    					"<a id='zsd"+i+"' href=\"javascript:delSelectedData('zsDoc"+i+"_"+zsId+"')\";></a>"+
			    					"</td>"+
			    				    "<td class='TDlabel' >巡回护士"+(i+1)+"：</td>"+
			    					"<td class='Input' ><input id=\"xunHui"+i+"_"+xunHuiId+"\" class=\"easyui-combogrid\"  name=\"xunHui"+i+"\">"+
			    					"<a id='xun"+i+"' href=\"javascript:delSelectedData('xunHui"+i+"_"+xunHuiId+"')\";></a>"+
			    					"</td>"+
			    				    "<td  class='TDlabel' >洗手护士"+(i+1)+"：</td>"+
			    					"<td  class='Input' ><input id=\"xiShou"+i+"_"+xiShouId+"\" class=\"easyui-combogrid\" name=\"xiShou"+i+"\">"+
			    					"<a id='xis"+i+"' href=\"javascript:delSelectedData('xiShou"+i+"_"+xiShouId+"')\";></a>"+
			    					"<a id=\"remove"+i+"\" href=\"javascript:void(0)\" onclick=\"removeTr('trId','"+(i-1)+"')\"></a>"+
			    					"<a id=\"add"+i+"\" href=\"javascript:void(0)\" onclick=\"addTr('trId','"+i+"')\"></a></td>"+
								"</tr>");
						
						/**
						 * 手术名称
						 * @Author: huangbiao
						 * @CreateDate: 2016年4月16日
						 * @param:
						 * @return:
						 * @Modifier:
						 * @ModifyDate:
						 * @ModifyRmk:
						 * @version: 1.0
						 */
						$('#itemName'+i+'_'+itemId).combogrid({
							 url : '<%=basePath%>operation/operationList/undrugComboboxfy.action',
								    idField:'code',    
								    textField:'name',
								    mode:"remote",
								    panelWidth:325,
									pageList:[10,20,30,40,50],
								 	pageSize:"10",
								 	pagination:true,
									columns:[[    
									         {field:'code',title:'编码',width:120},    
									         {field:'name',title:'名称',width:160},    
									       
									     ]],  
									onHidePanel:function(none){
								 	      var val = $(this).combogrid('getText');
									 	     if(nssmcName1(val)){
									 	    	$(this).combogrid('clear');
										    	$.messager.alert("提示","手术名称不能重复！","info");
										    	setTimeout(function(){
													$(".messager-body").window('close');
												},2000);
										    }
									 	    var mc = $('[id^=itemName]');
									 	    var b=0;
									 	    var c=0;
										 	mc.each(function(index,obj){
										 		if($(obj).combogrid('getText') != $(obj).combogrid('getValue')&&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
										 			b++;
										 		}
										 		if($(obj).combogrid('getText') == $(obj).combogrid('getValue')&&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
										 			c++;
										 		}
										 	});
										 	var value = $(this).combogrid('getValue');
									 	     if(val==value&&value!=null&&value!=""){
									 	    	 if(b>0){
									 	    		$.messager.alert("提示","该手术为非自定义手术，请选择下拉列表的数据！","info");
									 	    		setTimeout(function(){
														$(".messager-body").window('close');
													},3500);
									 	    		$(this).combogrid('clear');
									 	    	 }
									 	     }else{
									 	    	if(c>0){
									 	    		$.messager.alert("提示","该手术为自定义手术，请输入下拉列表不存在的数据！","info");
									 	    		setTimeout(function(){
														$(".messager-body").window('close');
													},3500);
									 	    		$(this).combogrid('clear');
									 	    	 }
									 	     }
								 	 },
								    onLoadSuccess: function () {
								    	var id=$(this).prop("id");
								        if ($("#"+id).combogrid('getValue')&&operatNameMap.get($("#"+id).combogrid('getValue'))) {
							            	$("#"+id).combogrid('setText',operatNameMap.get($("#"+id).combogrid('getValue')));
							            }
								      //分页工具栏作用提示
										var p = $(this).combogrid('grid');
										var pager = p.datagrid('getPager');
										var aArr = $(pager).find('a');
										var iArr = $(pager).find('input');
										$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1,position:'top',deltaX:1000});
										for(var i=0;i<aArr.length;i++){
											$(aArr[i]).tooltip({
												content:toolArr[i],
												hideDelay:1
											});
											$(aArr[i]).tooltip('hide');
										}
							        }  
							});
						/**
						 * 助手医生
						 * @Author: huangbiao
						 * @CreateDate: 2016年4月16日
						 * @param:
						 * @return:
						 * @Modifier:
						 * @ModifyDate:
						 * @ModifyRmk:
						 * @version: 1.0
						 */
						$('#zsDoc'+i+'_'+zsId).combobox({
							editable : true,
							valueField:'jobNo',    
						    textField:'name',
						    data:user,
							onSelect : function() {
								var zsDoc=$('[id^=zsDoc]');
							    var id=$(this).prop("id");
							    var ssysbms=$('#opDoctor').combobox("getValue");
							  	var b = 0;
							  	var zsys=$(this).combobox("getValue");
							  	zsDoc.each(function(index,obj){
							 		if($(obj).combogrid('getValue') == zsys){
							 			b++;
							 		}
							 		
							 	});
						    	if(b>1){
						    		$('#'+id).combobox('clear');
			    					$.messager.alert("提示","助手重复!","info");
			    					setTimeout(function(){
										$(".messager-body").window('close');
									},2000);
						    	}
						    	
						    	if(ssysbms!=null&&ssysbms!=""){
				 	 				if(zsys==ssysbms){
				 	 					$("#"+id).combobox("clear");
				 	 	 				$.messager.alert("提示","手术医生与助手医生不能相同");
				 	 	 				setTimeout(function(){
											$(".messager-body").window('close');
										},2500);
				 	 	 				return
				 	 	 			}
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
						 * 巡回护士
						 * @Author: huangbiao
						 * @CreateDate: 2016年4月16日
						 * @param:
						 * @return:
						 * @Modifier:
						 * @ModifyDate:
						 * @ModifyRmk:
						 * @version: 1.0
						 */
						$('#xunHui'+i+'_'+xunHuiId).combogrid({
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
						 	     if(xunhuiName1(val)){
						 	    	$(this).combogrid('clear');
							    	$.messager.alert("提示","护士信息不能重复！","info");
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
								  //分页工具栏作用提示
									var p = $(this).combogrid('grid');
									var pager = p.datagrid('getPager');
									var aArr = $(pager).find('a');
									var iArr = $(pager).find('input');
									$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1,position:'top',deltaX:1000});
									for(var i=0;i<aArr.length;i++){
										$(aArr[i]).tooltip({
											content:toolArr[i],
											hideDelay:1
										});
										$(aArr[i]).tooltip('hide');
									}
							  } 
					 	    
						});
						/**
						 * 洗手护士
						 * @Author: huangbiao
						 * @CreateDate: 2016年4月16日
						 * @param:
						 * @return:
						 * @Modifier:
						 * @ModifyDate:
						 * @ModifyRmk:
						 * @version: 1.0
						 */
						$('#xiShou'+i+'_'+xiShouId).combogrid({
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
					 	     	if(xishouName1(val)){
						 	    	$(this).combogrid('clear');
							    	$.messager.alert("提示","护士信息不能重复！","info");
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
								  //分页工具栏作用提示
									var p = $(this).combogrid('grid');
									var pager = p.datagrid('getPager');
									var aArr = $(pager).find('a');
									var iArr = $(pager).find('input');
									$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1,position:'top',deltaX:1000});
									for(var i=0;i<aArr.length;i++){
										$(aArr[i]).tooltip({
											content:toolArr[i],
											hideDelay:1
										});
										$(aArr[i]).tooltip('hide');
									}
							  } 
						});
						
						$('#add'+i).linkbutton({    
		    			    iconCls: 'icon-add'   
		    			});  
		    			$('#remove'+i).linkbutton({    
		    			    iconCls: 'icon-remove'   
		    			});  
		    			$('#add'+(i-1)).hide();
		    			$('#remove'+(i-1)).hide();
						 
					}else{
						$("#in0").val(itemId);
						$("#xh0").val(xunHuiId);
						$("#xs0").val(xiShouId);
						$('#itemName0_').prop("id",'itemName0_'+itemId);
			    		$('#zsDoc0_').prop("id",'zsDoc0_'+zsId);
			    		$('#xunHui0_').prop("id",'xunHui0_'+xunHuiId);
			    		$('#xiShou0_').prop("id",'xiShou0_'+xiShouId);
					}
				
					$('#ite'+i).linkbutton({
						iconCls:'icon-opera_clear',
						plain:true
					});
					$('#zsd'+i).linkbutton({
						iconCls:'icon-opera_clear',
						plain:true
					});
					$('#xun'+i).linkbutton({
						iconCls:'icon-opera_clear',
						plain:true
					});
					$('#xis'+i).linkbutton({
						iconCls:'icon-opera_clear',
						plain:true
					});
					//手术名称赋值
					if(rowData.opNameList[i]&&rowData.opNameList[i].itemId){
						$('#itemName'+i+'_'+itemId).combogrid('grid').datagrid('load',{q:rowData.opNameList[i].itemId});
						$('#itemName'+i+'_'+itemId).combogrid('setValue',rowData.opNameList[i].itemId);
						$('#itemName'+i+'_'+itemId).combogrid('setText',rowData.opNameList[i].itemName);
					}else{
						$('#itemName'+i+'_'+itemId).combogrid('setValue',itemName);
						$('#itemName'+i+'_'+itemId).combogrid('setText',itemName);
					}
					//助手医生赋值
					if(zsEmplCode){
						$('#zsDoc'+i+'_'+zsId).combobox("setValue",zsEmplCode);
					}
					//巡回护士赋值
					$('#xunHui'+i+'_'+xunHuiId).combogrid('grid').datagrid('load',{q:xunHuiEmplCode});
					$('#xunHui'+i+'_'+xunHuiId).combogrid('setValue',xunHuiEmplCode);
					//洗手护士赋值
					$('#xiShou'+i+'_'+xiShouId).combogrid('grid').datagrid('load',{q:xiShouEmplCode});
					$('#xiShou'+i+'_'+xiShouId).combogrid('setValue',xiShouEmplCode);
					 opNameMap.put(""+itemId,itemName);
					zhuShouMap.put(""+zsId,zsEmplCode);
					xunHuiMap.put(""+xunHuiId,xunHuiEmplCode);
					xiShouMap.put(""+xiShouId,xiShouEmplCode); 
					indexfz=maxLength;
				}
			}
		}
		/**
		 * @Description:保存手术安排信息
		 * @Author: huangbiao
		 * @CreateDate: 2016年4月16日
		 * @param:
		 * @return:
		 * @Modifier:
		 * @ModifyDate:
		 * @ModifyRmk:
		 * @version: 1.0
		 */
		function saveDatagrid(){
			var itemNameStr = "";//手术名称增删改查信息
			var zsDocStr = "";//助手增删改查信息
			var xunHuiStr = "";//巡回增删改查信息
			var xiShouStr = "";//洗手增删改查信息
			var rowsdates=$('#list').datagrid('getRows');
			var selectObj=$('#list').datagrid('getSelected');
			if(!(rowsdates.length>0)){
				$.messager.alert("提示","无手术申请记录请重新查询！","info");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3000);
				return false;
			}else if(!selectObj){
				$.messager.alert("提示","请选中一条记录！","info");
				setTimeout(function(){
					$(".messager-body").window('close');
				},2000);
				return false;
			}else{
				/**
				 *手术名称
				 */
				var idArr = $('[id^=itemName]');
				var itMap = new Map();
				var itemNewMap = new Map();
				idArr.each(function(){
					var val = $(this).combogrid('getValue');
					var idArrValue = $(this).prop('id');
					var id = idArrValue.substring(idArrValue.indexOf("_")+1);
					var newValue = $('#'+idArrValue).combogrid('getValue');
					var newText=$('#'+idArrValue).combogrid('getText');
					itemNewMap.put(id,newValue);
					var oldValue=opNameMap.get(id);
					if(!oldValue){
						if(newValue){
							itemNameStr+= id+","+newValue+","+newText+"_add#"//添加
						}
					}else{
						if(!newValue){
							itemNameStr+= id+",_del#";//删除
						}else{
							if(oldValue!=newValue){
								itemNameStr+= id+","+newValue+","+newText+"_upd#";//更新
							}
						}
					}
				});
				opNameMap.each(function(key,value,index){
						if(itemNewMap.get(key)==null||itemNewMap.get(key)==""){
							itemNameStr+= key+",_del#"//删除
						}
				});
				var foreflag=$("#foreFlag").val();//人员角色信息2为手术安排状态
				//判断是否为手术安排的人员信息
				 if(foreflag==2){
					 /**
						 *助手医生
						 */
					 var idArr1 = $('[id^=zsDoc]');
						var zdoctor = $('#opDoctor').combobox('getValue');//手术医生
						var zcMap = new Map();
						var zcNewMap = new Map();
						idArr1.each(function(){
							var idArrValue = $(this).prop('id');
							var zsnum =idArrValue.substring(5,6);
							var id = idArrValue.substring(idArrValue.indexOf("_")+1);
							var newValue = $('#'+idArrValue).combobox('getValue');
							var newText=$('#'+idArrValue).combobox('getText');
							zcNewMap.put(id, newValue);
							var oldValue=zhuShouMap.get(id);
							if(!oldValue){
								if(newValue){
									if(newValue!=newText){
										zsDocStr+= id+","+zsnum+","+newValue+","+newText+"_add#"//添加
									}else{
										$.messager.alert("提示","没有这个医生！","info");
										setTimeout(function(){
											$(".messager-body").window('close');
										},2000);
										$('#'+idArrValue).combobox("clear");
									}
								}
							}else{
								if(!newValue){
									zsDocStr+= id+",_del#";//删除
								}else if(newValue!=oldValue){
									if(oldValue!=newValue){
										if(newValue!=newText){
											zsDocStr+= id+","+zsnum+","+newValue+","+newText+"_upd#";//更新
										}else{
											$.messager.alert("提示","没有这个医生！","info");
											setTimeout(function(){
												$(".messager-body").window('close');
											},2000);
											$('#'+idArrValue).combobox("clear");
										}
									}
								}
							}
						});
						zhuShouMap.each(function(key,value,index){
								if(zcNewMap.get(key)==null||zcNewMap.get(key)==""){
									zsDocStr+= key+",_del#";//删除
								}
						});
						/**
						 *巡回
						 */
						var idArr2 = $('[id^=xunHui]');
						var xhMap = new Map();
						var xhNewMap = new Map();
							var count = 0;
							idArr2.each(function(){
								var idArrValue = $(this).prop('id');
								var xhnum=idArrValue.substring(6,7);
								var id = idArrValue.substring(idArrValue.indexOf("_")+1);
								var newValue = $('#'+idArrValue).combogrid('getValue');
								var newText=$('#'+idArrValue).combogrid('getText');
								xhNewMap.put(id,newValue);
								var oldValue=xunHuiMap.get(id);
								if(!oldValue){
									if(newValue){
										if(newValue!=newText){
											xunHuiStr+= id+","+xhnum+","+newValue+","+newText+"_add#";//添加
										}else{
											$.messager.alert("提示","没有这个护士！","info");
											setTimeout(function(){
												$(".messager-body").window('close');
											},2000);
											$('#'+idArrValue).combogrid("clear");
										}
									}
								}else{
									if(!newValue){
										xunHuiStr+= id+",_del#";//删除
									}else if(newValue!=oldValue){
										if(newValue!=newText){
											xunHuiStr+= id+","+xhnum+","+newValue+","+newText+"_upd#"//修改
										}else{
											$.messager.alert("提示","没有这个护士！","info");
											setTimeout(function(){
												$(".messager-body").window('close');
											},2000);
											$('#'+idArrValue).combogrid("clear");
										}
										
									}
								}
							});
							xunHuiMap.each(function(key,value,index){
									if(xhNewMap.get(key)==null||xhNewMap.get(key)==""){
										xunHuiStr+= key+",_del#"//删除
									}
							});
						/**
						 *洗手
						 */
						var idArr3 = $('[id^=xiShou]');
						var xsMap = new Map();
						var xsNewMap = new Map();
						idArr3.each(function(){
							var idArrValue = $(this).prop('id');
							var xsnum=idArrValue.substring(6,7);
							var id = idArrValue.substring(idArrValue.indexOf("_")+1);
							var newValue = $('#'+idArrValue).combogrid('getValue');
							var newText=$('#'+idArrValue).combogrid('getText');
							xsNewMap.put(id,newValue);
							var oldValue=xiShouMap.get(id);
							if(!oldValue){
								if(newValue){
									if(newValue!=newText){
										xiShouStr+= id+","+xsnum+","+newValue+","+newText+"_add#";//添加
									}else{
										$.messager.alert("提示","没有这个护士！","info");
										setTimeout(function(){
											$(".messager-body").window('close');
										},2000);
										$('#'+idArrValue).combogrid("clear");
									}
								}
							}else{
								if(!newValue){
									xiShouStr+= id+",_del#";//删除
								}else if(newValue!=oldValue){
									if(newValue!=newText){
										xiShouStr+= id+","+xsnum+","+newValue+","+newText+"_upd#"//修改
									}else{
										$.messager.alert("提示","没有这个护士！","info");
										setTimeout(function(){
											$(".messager-body").window('close');
										},2000);
										$('#'+idArrValue).combogrid("clear");
									}
								}
							}
						});
						xiShouMap.each(function(key,value,index){
								if(xsNewMap.get(key)==null||xsNewMap.get(key)==""){
									xiShouStr+= key+",_del#"//删除
								}
						});
				 }else{
					 var idArr1 = $('[id^=zsDoc]');
						var zdoctor = $('#opDoctor').combobox('getValue');//手术医生
						var zcMap = new Map();
						var zcNewMap = new Map();
						idArr1.each(function(){
							var val = $(this).combobox('getValue');
							if(val){
								zcMap.put(val,"1");
							}
							var fdoctor = $(this).combobox('getValue');
							var idArrValue = $(this).prop('id');
							var zsnum =idArrValue.substring(5,6);
							var id = "";
							var newValue = $('#'+idArrValue).combobox('getValue');
							var newText=$('#'+idArrValue).combobox('getText');
							if(id){
								zcNewMap.put(id,newValue);
							}else{
								if(newValue!=null&&newValue!=""){
									if(newValue!=newText){
											zsDocStr+= id+","+zsnum+","+newValue+","+newText+"_add#"//添加
									}else{
										$.messager.alert("提示","没有这个医生！","info");
										setTimeout(function(){
											$(".messager-body").window('close');
										},2000);
										$('#'+idArrValue).combobox("clear");
									}
								}
							}
						});
						//巡回
						var idArr2 = $('[id^=xunHui]');
						var xhMap = new Map();
						var xhNewMap = new Map();
							var count = 0;
							idArr2.each(function(){
								var val = $(this).combogrid('getValue');
								if(val){
									xhMap.put(val,"1");
								}
								var idArrValue = $(this).prop('id');
								var xhnum=idArrValue.substring(6,7);
								var id = "";
								var newValue = $('#'+idArrValue).combogrid('getValue');
								var newText =$('#'+idArrValue).combogrid('getText');
								if(id){
									xhNewMap.put(id,newValue);
								}else{
									if(newValue!=null&&newValue!=""){
										if(newValue!=newText){
											xunHuiStr+= id+","+xhnum+","+newValue+","+newText+"_add#"//添加
										}else{
											$.messager.alert("提示","没有这个护士！","info");
											setTimeout(function(){
												$(".messager-body").window('close');
											},2000);
											$('#'+idArrValue).combogrid("clear");
										}
									}
								}
							});
							/**
							 *洗手
							 */
							var idArr3 = $('[id^=xiShou]');
							var xsMap = new Map();
							var xsNewMap = new Map();
							idArr3.each(function(){
								var val = $(this).combogrid('getValue');
								if(val){
									xsMap.put(val,"1");
								}
								var idArrValue = $(this).prop('id');
								var xsnum=idArrValue.substring(6,7);
								var id = "";
								var newValue = $('#'+idArrValue).combogrid('getValue');
								var newText =$('#'+idArrValue).combogrid('getText');
								if(id){
									xsNewMap.put(id,newValue);
								}else{
									if(newValue!=null&&newValue!=""){
										if(newValue!=newText){
											xiShouStr+= id+","+xsnum+","+newValue+","+newText+"_add#"//添加
										}else{
											$.messager.alert("提示","没有这个护士！","info");
											setTimeout(function(){
												$(".messager-body").window('close');
											},2000);
											$('#'+idArrValue).combogrid("clear");
										}
									}
								}
							});
				 }
				$('#applyDataForm').form('submit', {    
				    url:"<%=basePath%>operation/arrangement/saveOperationApplyInfo.action",
				    onSubmit: function(param){
				    	if (!$('#applyDataForm').form('validate')) {
				    		$.messager.progress('close');
							$.messager.show({
								title : '提示信息',
								msg : '验证没有通过,不能提交表单!'
							});
							return false;
						}
						var tempPreDate = $('#preDate').val();
						if(!tempPreDate){
						    $.messager.show({
								title : '提示信息',
								msg : '手术时间不能为空,不能提交表单!'
							});
						    return false;
						}
				    	$.messager.progress({text:'保存中，请稍后...',modal:true});
				    	param.itemNameStr=itemNameStr,
				    	param.zsDocStr=zsDocStr,
				    	param.xunHuiStr=xunHuiStr,
				    	param.xiShouStr=xiShouStr
				    },    
				    success:function(data){
				    	$.messager.progress('close');
				    	if(data=="error"){
				    		$.messager.alert("提示","保存失败！","info");
				    		return false;
				    	}
				    	if(data=="errorToPreDate"){
				    		$.messager.alert("提示","当前手术房间的手术台已存在相同时间段的手术安排！请修改手术时间或者手术房间或者手术台信息 ！","info");
				    		return false;
				    	}
				    	if(data=="success"){
				    		$.messager.alert("提示","保存成功！","info");
				    		 $('#list').datagrid('reload');
				    		 del();
				    	}
				        $('#list').datagrid('reload');
				        opNameMap = new Map();
				        zhuShouMap = new Map();
				        xunHuiMap = new Map();
				        xiShouMap = new Map();
				        setTimeout(function(){
				        	 var rowData = $('#list').datagrid('getSelected');
					         var opName = rowData.opNameList.length||0;
							 var xunHui = rowData.xunHuiList.length||0;
							 var xiShou = rowData.xiShouList.length||0;
							 var zs = rowData.zsDocList.length||0;
							 var max1 = ((opName>=xunHui)&&opName)||xunHui;
							 var max2 = ((max1>=xiShou)&&max1)||xiShou;
							 var maxLength = ((max2>=zs)&&max2)||zs;
							 if(maxLength>0){
								for(var i=0;i<maxLength;i++){
									var itemName = (rowData.opNameList[i]&&(rowData.opNameList[i].itemId||rowData.opNameList[i].itemName||""))||"";
									var xunHuiEmplCode = (rowData.xunHuiList[i]&&(rowData.xunHuiList[i].emplCode||""))||"";
									var zsEmplCode = (rowData.zsDocList[i]&&(rowData.zsDocList[i].emplCode||""))||"";
									var xiShouEmplCode = (rowData.xiShouList[i]&&(rowData.xiShouList[i].emplCode||""))||"";
									var itemId = (rowData.opNameList[i]&&(rowData.opNameList[i].id||""))||"";
									var xunHuiId = (rowData.xunHuiList[i]&&(rowData.xunHuiList[i].id||""))||"";
									var zsId = (rowData.zsDocList[i]&&(rowData.zsDocList[i].id||""))||"";
									var xiShouId = (rowData.xiShouList[i]&&(rowData.xiShouList[i].id||""))||"";
									opNameMap.put(""+itemId,itemName);
									zhuShouMap.put(""+zsId,zsEmplCode);
									xunHuiMap.put(""+xunHuiId,xunHuiEmplCode);
									xiShouMap.put(""+xiShouId,xiShouEmplCode);
							    }    
							 }
				        },100);
					}
			});
		}
	}
		
		//渲染急诊手术
		function functionRowNum(value,row,index){
			if(value==1){
				return '<span style=\'color:red;\'>急</span>'
			}else {
				return "";
			}
		}
		//麻醉方式为空的标识及渲染
		function functionaneway(value,row,index){
			if(value==null||value==""){
				return '<span style=\'position: relative; right:-60px;top:-6px;color:red;\'></span>';
			}else{
				for(var i=0;i<aneList.length;i++){
					if(value==aneList[i].encode){
						return aneList[i].name;
					}
				}
			}
		}
		//手术状态的姓名标识
		function functionName(value,row,index){
			var name = value||"";
			return name;
		}
	
		//医生渲染
		function functiondoc(value,row,index){
			if(value!=null&&value!=''){
				return opDocMap.get(value);
			}
		}
		//手术台渲染
		function functiontai(value,row,index){
			if(value!=null&&value!=''){
				return optainMap.get(value);
			}
		}
		//手术间渲染
		function functionjian(value,row,index){
			if(value!=null&&value!=''){
				return opjianMap.get(value);
			}
		}
		//渲染人员
		function functionEmp(value,row,index){
			if(value!=null&&value!=''){
				return empMap[value];
			}
		}
		
		/**
		 * @Description:渲染手术名称
		 * @Author: huangbiao
		 * @CreateDate: 2016年4月15日
		 * @param:valueList-单元格的值
		 * @param:row-行数据
		 * @param:index-索引
		 * @return:渲染的效果
		 * @Modifier:
		 * @ModifyDate:
		 * @ModifyRmk:
		 * @version: 1.0
		 */
		function functionOpname(valueList,row,index){
			if(valueList!=null){
				var result = "";
				$.each(valueList,function(index,value){
					if(index == valueList.length-1){
						result+=value.itemName||"";
					}else{
						var result1=value.itemName||"";
						result+=result1+",";
						
					}
				});
					return result;
			}
		}
		
		/**
		 * @Description:渲染洗手
		 * @Author: huangbiao
		 * @CreateDate: 2016年4月15日
		 * @param:valueList-单元格的值
		 * @param:row-行数据
		 * @param:index-索引
		 * @return:渲染的效果
		 * @Modifier:
		 * @ModifyDate:
		 * @ModifyRmk:
		 * @version: 1.0
		 */
		function functionXiShou(valueList,row,index){
			if(valueList!=null){
				var result = "";
				$.each(valueList,function(index,value){
					if(index == valueList.length-1){
						result+=value.emplName||"";
					}else{
						result1=value.emplName||"";
						result+=result1+",";
					}
				});
				return result;
			}
		}
		
		/**
		 * @Description:渲染临时助手
		 * @Author: huangbiao
		 * @CreateDate: 2016年4月15日
		 * @param:valueList-单元格的值
		 * @param:row-行数据
		 * @param:index-索引
		 * @return:渲染的效果
		 * @Modifier:
		 * @ModifyDate:
		 * @ModifyRmk:
		 * @version: 1.0
		 */
		function functionLinShi(valueList,row,index){
			if(valueList!=null){
				var result = "";
				$.each(valueList,function(index,value){
					if(index == valueList.length-1){
						result+=opDocMap.get(value.emplCode)||"";
					}else{
						result1=opDocMap.get(value.emplCode)||"";
						result+=result1+",";
					}
				});
				return result;
			}
		}
		
		/**
		 * @Description:渲染患者科室
		 * @Author: huangbiao
		 * @CreateDate: 2016年4月15日
		 * @param:valueList-单元格的值
		 * @param:row-行数据
		 * @param:index-索引
		 * @return:渲染的效果
		 * @Modifier:
		 * @ModifyDate:
		 * @ModifyRmk:
		 * @version: 1.0
		 */
		 function functionDept(value,row,index){
			 if(value!=null&&value!=''){
					return deptMap[value];
				}
		 }
		
		    /**
			 * @Description:手术合并弹窗
			 * @Author: huangbiao
			 * @CreateDate: 2016年4月16日
			 * @param:
			 * @return:渲染的效果
			 * @Modifier:
			 * @ModifyDate:
			 * @ModifyRmk:
			 * @version: 1.0
			 */
		 	function popWinToOpCombine(){
				 var checkFlag = false;
				 var pasourceFlag = false;
				 var sel = $('#list').datagrid('getChecked');
				 if(sel==null||!(sel.length>1)){
					 $.messager.alert('提示','请至少选择两条记录！','info');
					 setTimeout(function(){
							$(".messager-body").window('close');
						},2500);
					 return false;
				 }
				 for(var i=0;i<sel.length;i++){
					 if(sel[0].patientNo!=sel[i].patientNo){
						 checkFlag = true;
					 }
					 
					 if(sel[0].pasource!=sel[i].pasource){
						 pasourceFlag=true;
					}
				 }
				 if(checkFlag){
					 $.messager.alert('提示','请选择相同的患者！','info');
					 setTimeout(function(){
							$(".messager-body").window('close');
						},2500);
					 return false;
				 }
				 if(pasourceFlag){
					 $.messager.alert('提示','门诊手术和住院手术不可以合并！','info');
					 setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					 return false;
				 } 
				var ids = "";
				for(var i=0;i<sel.length;i++){
					if(i==sel.length-1){
						ids+=sel[i].id;
					}else{
						ids+=sel[i].id+",";
					}
				}
				
				AdddilogModel("shousap","手术安排单","<%=basePath%>operation/arrangement/OperationCombineview.action?menuAlias="+menuAlias+"&patientNo="+$('#patientNo1').val()+"&ids="+ids,'80%','95%');
			}
		 
		 	/**
			 * @Description:添加一行
			 * @Author: huangbiao
			 * @CreateDate: 2016年4月20日
			 * @param1:trId:行ID
			 * @param2:index：下标
			 * @return:
			 * @Modifier:
			 * @ModifyDate:
			 * @ModifyRmk:
			 * @version: 1.0
			 */
		 function addTr(trId,index){
			 var nu = parseInt(index)+1;
			 var nu2 = nu+1;
			 $("#"+trId+index).after("<tr id=\""+trId+nu+"\">"+
											"<td class='TDlabel'>手术名称"+nu2+"：</td>"+
											"<td class='Input'><input id='itemName"+nu+"_"+"' name=\"itemName"+nu+"\">"+
												"<a id='ite"+nu+"' href=\"javascript:delSelectedData('itemName"+nu+"_')\";></a>"+
											"</td>"+
											"<td class='TDlabel'>助手医生"+nu2+"：</td>"+
											"<td class='Input'><input id='zsDoc"+nu+"_"+"'  name=\"zsDoc"+nu+"\" >"+
												"<a id='zsd"+nu+"' href=\"javascript:delSelectedData('zsDoc"+nu+"_')\";></a>"+
											"</td>"+
											"<td class='TDlabel'>巡回护士"+nu2+"：</td>"+
											"<td class='Input'><input id='xunHui"+nu+"_"+"' name=\"xunHui"+nu+"\">"+
												"<a id='xun"+nu+"' href=\"javascript:delSelectedData('xunHui"+nu+"_')\";></a>"+
											"</td>"+
											"<td class='TDlabel'>洗手护士"+nu2+"：</td>"+
											"<td class='Input'><input id='xiShou"+nu+"_"+"'  name=\"xiShou"+nu+"\" >"+
											"<a id='xis"+nu+"' href=\"javascript:delSelectedData('xiShou"+nu+"_')\";></a>"+
											"<a id=\"add"+nu+"\" href=\"javascript:void(0)\" onclick=\"addTr('"+trId+"','"+nu+"')\"></a>"+
											"<a id=\"remove"+nu+"\" href=\"javascript:void(0)\" onclick=\"removeTr('"+trId+"','"+index+"')\"></a>"+
											"</td>"+
										"</tr>");
			//渲染添加的行的样式
			/**
			 * @Description:手术名称
			 * @Author: huangbiao
			 * @CreateDate: 2016年4月20日
			 * @return:
			 * @Modifier:
			 * @ModifyDate:
			 * @ModifyRmk:
			 * @version: 1.0
			 */
				$('#itemName'+nu+"_").combogrid({
					 url : '<%=basePath%>operation/operationList/undrugComboboxfy.action',
					    idField:'code',    
					    textField:'name',
					    mode:"remote",
						pageList:[10,20,30,40,50],
					 	pageSize:"10",
					 	panelWidth:325,
					 	pagination:true,
						columns:[[    
						         {field:'code',title:'编码',width:'120'},    
						         {field:'name',title:'名称',width:'160'},    
						     ]],  
					 	onHidePanel:function(none){
					 		var val = $(this).combogrid('getText');
					 		if(nssmcName1(val)){
					 			$(this).combogrid('clear');
						    	$.messager.alert("提示","手术名称不能重复!");
						    	setTimeout(function(){
									$(".messager-body").window('close');
								},2000);
						    }
					 		var mc = $('[id^=itemName]');
					 	    var b=0;
					 	    var c=0;
						 	mc.each(function(index,obj){
						 		if($(obj).combogrid('getText') != $(obj).combogrid('getValue')&&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
						 			b++;
						 		}
						 		if($(obj).combogrid('getText') == $(obj).combogrid('getValue')&&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
						 			c++;
						 		}
						 	});
						 	
						 	var value = $(this).combogrid('getValue');
					 	     if(val==value&&value!=null&&value!=""){
					 	    	 if(b>0){
					 	    		$.messager.alert("提示","该手术为非自定义手术，请选择下拉列表里的数据！","info");
					 	    		setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
					 	    		$(this).combogrid('clear');
					 	    	 }
					 	     }else{
					 	    	if(c>0){
					 	    		$.messager.alert("提示","该手术为自定义手术，请输入下拉列表不存在的数据！","info");
					 	    		setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
					 	    		$(this).combogrid('clear');
					 	    	 }
					 	     }
					 	},
					    onLoadSuccess: function () {
					    	var id=$(this).prop("id");
					        if ($("#"+id).combogrid('getValue')&&operatNameMap.get($("#"+id).combogrid('getValue'))) {
				            	$("#"+id).combogrid('setText',operatNameMap.get($("#"+id).combogrid('getValue')));
				            } 
					      //分页工具栏作用提示
							var p = $(this).combogrid('grid');
							var pager = p.datagrid('getPager');
							var aArr = $(pager).find('a');
							var iArr = $(pager).find('input');
							$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1,position:'top',deltaX:1000});
							for(var i=0;i<aArr.length;i++){
								$(aArr[i]).tooltip({
									content:toolArr[i],
									hideDelay:1
								});
								$(aArr[i]).tooltip('hide');
							}
				        }   
				});
				/**
				 * @Description:助手医生
				 * @Author: huangbiao
				 * @CreateDate: 2016年4月20日
				 * @return:
				 * @Modifier:
				 * @ModifyDate:
				 * @ModifyRmk:
				 * @version: 1.0
				 */
				$('#zsDoc'+nu+"_").combobox({
					editable : true,
					valueField:'jobNo',    
				    textField:'name',
				    data:user,
				    onSelect : function() {
				    	var zsDoc=$('[id^=zsDoc]');
					    var id=$(this).prop("id");
					    var ssysbms=$('#opDoctor').combobox("getValue");
					  	var b = 0;
					  	var zsys=$(this).combobox("getValue");
					  	zsDoc.each(function(index,obj){
					 		if($(obj).combogrid('getValue') == zsys){
					 			b++;
					 		}
					 		
					 	});
				    	if(b>1){
				    		$('#'+id).combobox('clear');
	    					$.messager.alert("提示","助手重复!","info");
	    					setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
				    	}
				    	
				    	if(ssysbms!=null&&ssysbms!=""){
		 	 				if(zsys==ssysbms){
		 	 					$("#"+id).combobox("clear");
		 	 	 				$.messager.alert("提示","手术医生与助手医生不能相同");
		 	 	 				setTimeout(function(){
									$(".messager-body").window('close');
								},3000);
		 	 	 				return
		 	 	 			}
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
				 * @Description:巡回护士
				 * @Author: huangbiao
				 * @CreateDate: 2016年4月20日
				 * @return:
				 * @Modifier:
				 * @ModifyDate:
				 * @ModifyRmk:
				 * @version: 1.0
				 */
				$('#xunHui'+nu+"_").combogrid({
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
				    	if(xunhuiName1(val)){
				    		$(this).combogrid('clear');
					    	$.messager.alert("提示","护士信息不能重复!","info");
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
						  //分页工具栏作用提示
							var p = $(this).combogrid('grid');
							var pager = p.datagrid('getPager');
							var aArr = $(pager).find('a');
							var iArr = $(pager).find('input');
							$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1,position:'top',deltaX:1000});
							for(var i=0;i<aArr.length;i++){
								$(aArr[i]).tooltip({
									content:toolArr[i],
									hideDelay:1
								});
								$(aArr[i]).tooltip('hide');
							}
					  } 
				});
				/**
				 * @Description:洗手护士
				 * @Author: huangbiao
				 * @CreateDate: 2016年4月20日
				 * @return:
				 * @Modifier:
				 * @ModifyDate:
				 * @ModifyRmk:
				 * @version: 1.0
				 */
				$('#xiShou'+nu+"_").combogrid({
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
			 	    	var ss=xishouName1(val);
				    	if(ss){
				    		$(this).combogrid('clear');
					    	$.messager.alert("提示","护士信息不能重复!","info");
					    	setTimeout(function(){
								$(".messager-body").window('close');
							},2000);
					    }
			 	    },onLoadSuccess: function () {
					    var id=$(this).prop("id");
					    if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
				            $("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
				        } 
					  //分页工具栏作用提示
						var p = $(this).combogrid('grid');
						var pager = p.datagrid('getPager');
						var aArr = $(pager).find('a');
						var iArr = $(pager).find('input');
						$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1,position:'top',deltaX:1000});
						for(var i=0;i<aArr.length;i++){
							$(aArr[i]).tooltip({
								content:toolArr[i],
								hideDelay:1
							});
							$(aArr[i]).tooltip('hide');
						}
				  } 
				});
				
				$('#add'+nu).linkbutton({
					iconCls:'icon-add'
				});
				$('#remove'+nu).linkbutton({
					iconCls:'icon-remove'
				});
				 $('#ite'+nu).linkbutton({
						iconCls:'icon-opera_clear',
						plain:true
				});
				$('#zsd'+nu).linkbutton({
					iconCls:'icon-opera_clear',
					plain:true
				});
				$('#xun'+nu).linkbutton({
					iconCls:'icon-opera_clear',
					plain:true
				});
				$('#xis'+nu).linkbutton({
					iconCls:'icon-opera_clear',
					plain:true
				}); 
				
				$('#remove'+index).hide();
				$('#add'+index).hide();
				indexfz=nu2;
		 }
		 
		 /**
			 * @Description:删除一行
			 * @Author: huangbiao
			 * @CreateDate: 2016年4月20日
			 * @param:
			 * @return:
			 * @Modifier:
			 * @ModifyDate:
			 * @ModifyRmk:
			 * @version: 1.0
			 */
		 function removeTr(trId,index){
			 var nu = parseInt(index)+1;
			 $('#'+trId+nu).remove();
			 $('#add'+index).show();
			 $('#remove'+index).show();
		 }
		 /**
			 * @Description:清空
			 * @Author: zhangjin
			 * @CreateDate: 2016年9月26日
			 * @param:
			 * @return:
			 * @Modifier:
			 * @ModifyDate:
			 * @ModifyRmk:
			 * @version: 1.0
			 */
		 function del(){
			 $("#applyDataForm").form("reset");
			 $("#patientNo").text("");
			 $("#name ").text("");
			 $("#sex").text("");
			 $("#age").text("");
			 $("#bingqubingfang ").text("");
			 $("#applyDoctor ").text("");
			 $("#diagnose1").text("");
			 $("#isspecial").text("");
			 $("#folk").text("");
			
				//删除助手护士信息
				for(var i=0;i<indexfz;i++){
					if(i!=0){
						$("#trId"+i).remove();
						$('#add'+(i-1)).show();
					}else{
						var zhushou=$('[id^=zsDoc]');
						var xishou=$('[id^=xiShou]');
						var xunhui=$('[id^=xunHui]');
						var nssmc=$('[id^=itemName]');
						zhushou.each(function(){
							var id=$(this).prop("id","zsDoc0_");
						});
						xishou.each(function(){
							$(this).prop("id","xiShou0_");
							
						});
						xunhui.each(function(){
							$(this).prop("id","xunHui0_");
							
						});
						nssmc.each(function(){
							$(this).prop("id","itemName0_");
							
						});
					}
					
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
		 * @Description:验证重复
		 * @Author：zhangjin
		 * @CreateDate：2016-11-1
		 * @version 1.0
		 * @throws IOException 
		 *
		 */
		$.extend($.fn.validatebox.defaults.rules, {  
		         zs: { // 助手名称
		             validator : function(value, param) { 
		             	var mc = $('[id^=zsDoc]');
		  	        	var b = 0
		  	 			mc.each(function(index,obj){
		  	 				if($(obj).combobox('getValue') == value){
		  	 					b++;
		  	 				}
		   				});
		  	            return b>1?false:true;
		              },  
		          },
		}); 
		 
		 /**
			 * @Description:渲染手术室
			 * @Author: huangbiao
			 * @CreateDate: 2016年4月20日
			 * @param:
			 * @return:
			 * @Modifier:
			 * @ModifyDate:
			 * @ModifyRmk:
			 * @version: 1.0
			 */
		 function functionexecDept(value,row,index){
			 if(value!=null&&value!=""){
				 return roomMap.get(value);
			 }
		 }
		//手术名称信息的验证
		 function nssmcName1(name){
		 	var mc = $('[id^=itemName]');
		  	var b = 0
		 	mc.each(function(index,obj){
		 		if($(obj).combogrid('getText') == name&&$(obj).combogrid('getText')!=""&&$(obj).combogrid('getText')!=null){
		 			b++;
		 		}
		 	});
		  	return b>1?true:false;
		 }
		//巡回护士信息的验证
		 function xunhuiName1(name){
		 	var mc = $('[id^=xunHui]');
		  	var b = 0;
		 	mc.each(function(index,obj){
		 		if($(obj).combogrid('getValue') == name&&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
		 			b++;
		 		}
		 	});
		  	var c=b>1?true:false;
		         if(!c){
		      		var xs = $('[id^=xiShou]');
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
		 function xishouName1(name){
		 	var mc = $('[id^=xiShou]');
		  	var b = 0;
		 	mc.each(function(index,obj){
		 		if($(obj).combogrid('getValue') == name&&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
		 			b++;
		 		}
		 	});
		  	var c=b>1?true:false;
		        if(!c){
		     		var xs = $('[id^=xunHui]');
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
		//加载模式窗口
		function AdddilogModel(id,title,url,width,height){
			$('#'+id).dialog({
				title:title,
				width:width,
				height:height,
				closed:false,
				cache:false,
				href:url,
				resizable:true,
				modal:true
			});
		}		
		//重置查询时间条件
		function clearTime(){
			$("#queryStime").val($("#tmpDefaultDay").val());
			$("#endtime").val("");
			del();
			searchFrom();
		 }	
		
		//添加颜色标识
		function functionColour(value,row,index){
			if (row.status=="3"){   
	            return 'background-color:#ffee00;color:black;';
	        }  
		}
	</script>	
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body style="margin: 0px;padding: 0px;">
	<div id="divLayout" class="easyui-layout" data-options="fit:true" style="width: 100%;height:100%">
		<div data-options="region:'north',split:false,border:false" style="padding:5px 5px 5px 5px;width: 700px;height:280px;">
				<div id="divLayout" class="easyui-layout" data-options="fit:true" style="width: 700px;height:260px;">
					<div data-options="region:'north',split:false,border:false" style="padding:0px 5px 2px 5px;width: 700px;height: 30px">		
						<shiro:hasPermission name="${menuAlias }:function:save">
								<a  href="javascript:void(0)" onclick="saveDatagrid()" class="easyui-linkbutton" iconCls="icon-save">保存</a>
							</shiro:hasPermission>
								<a href="javascript:void(0)"  onclick="clearTime()" class="easyui-linkbutton"  iconCls="reset">重置</a>
					</div>
					<div data-options="region:'center',border:false"  style="width:100%;height:260px;" class="arrangementList">
						<form id="applyDataForm" method = "post" style="width: 100%;">
							<table id="table1" class="tableCss" style="width: 100%;height: 220px">
								<tr>
									<td class="TDlabel">姓名：<input type="hidden" id="operationId" name="opVo.id" /></td>
									<td id="name" class="Input"></td>
									<td class="TDlabel">性别：</td>
									<td id="sex" class="Input"></td>
									<td class="TDlabel">年龄：</td>
									<td id="age" class="Input"></td>
									<td class="TDlabel">病历号：</td>
									<td id="patientNo" class="Input"></td>
								</tr>
								<tr>
									<td class="TDlabel">病区/床号：<input type="hidden" id="foreFlag" name="foreFlag" /></td>
									<td id="bingqubingfang" class="Input">
									</td>
									<td class="TDlabel">手术台类型：</td>
									<td class="Input">
										<input class="easyui-combobox" id="consoleType" name="opVo.consoleType" />
									</td>
									<td class="TDlabel">手术时间：</td>
									<td class="Input">
									    <input id="preDate" name="opVo.preDate" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:00',minDate:'%y-%M-%d',maxDate:'{%y+1}-%M-%d'})" style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
									</td>
									<td class="TDlabel">手术类型：</td>
									<td class="Input">
										<input class="easyui-combobox" id="opType" name="opVo.opType" />
									</td>
								</tr>
								<tr>
									<td class="TDlabel">术前诊断：</td>
									<td id="diagnose1" class="Input"></td>
									<td class="TDlabel">申请医生：</td>
									<td id="applyDoctor" class="Input"></td>
									<td class="TDlabel">手术医生：</td>
									<td class="Input">
										<input class="easyui-combobox" id="opDoctor" data-options="required:true" name="opVo.opDoctor" />
									</td>
									<td class="TDlabel">是否特殊手术：</td>
									<td id="isspecial" class="Input"></td>
								</tr>
								<tr>
									<td class="TDlabel">签字家属：</td>
									<td class="Input" id="folk">
									</td>
									<td class="TDlabel">家属关系：</td>
									<td class="Input" >
									<input class="easyui-textbox" id="relaCode" name="opVo.relaCode" /></td>
									<td class="TDlabel">手术房间：</td>
									<td class="Input">
									<input class="easyui-combobox" id="roomId" name="opVo.roomId" data-options="required:true"/></td>
									<td class="TDlabel">手术规模：</td>
									<td class="Input">
									<input class="easyui-combobox" id="degree" name="opVo.degree" /></td>
								</tr>
								<tr id="af">
									<td class="TDlabel">麻醉类别：</td>
									<td class="Input">
										<input class="easyui-combobox" id="anesType" name="opVo.anesType" />
									</td>
									<td class="TDlabel">麻醉方式：</td>
									<td class="Input">
										<input class="easyui-combobox" id="aneWay" name="opVo.aneWay" />
									</td>
									<td class="TDlabel">手术台：</td>
									<td class="Input" >
										<input class="easyui-combobox" id="console" name="opVo.console" data-options="required:true"/>
									</td>
									<td class="TDlabel">备注：</td>
									<td class="Input" >
										<input class="easyui-textbox" id="remark" name="opVo.applyRemark" />
										<input type="hidden" id="patientNo1" />
									</td>
								</tr>
								<tr id='trId0' >
									<td class="TDlabel">手术名称1： <input id="in0" type="hidden" style="z-index: 110"></td>
									<td class="Input"><input id="itemName0_" name="itemName" class="easyui-combogrid" data-options="required:true"/>
									</td>
								    <td class="TDlabel">助手医生1：</td>
									<td ><input id="zsDoc0_" name="zsDoc" class="easyui-combobox"/>
									</td>
									 <td class="TDlabel">巡回护士1： <input id="xh0" type="hidden"></td>
									<td class="Input"><input id="xunHui0_" name="xunHui" class="easyui-combogrid"/>
									</td>
									<td class="TDlabel">洗手护士1： <input id="xs0" type="hidden"></td>
									<td class="Input"><input id="xiShou0_" name="xiShou" class="easyui-combogrid"/>
									<a href="javascript:void(0)" id="add0" onclick="addTr('trId','0')" class="easyui-linkbutton" data-options="iconCls:'icon-add'"></a>
									</td>
								</tr>
							</table>
						</form>
					</div>
				</div>
		</div>
		<div data-options="region:'center',border:false" style="width:100%;height:60%;" class="arrangementList1">
			<div id="toolbarId" style=" width:100%;height:33px; ">
					<table style="width:100%;">
							<tr>
								<td style="padding: 0px 5px;">手术时间：
									<input id="queryStime" value="${day}" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'{%y+1}-%M-%d'})" style="width:120px;height:24px;border: 1px solid #95b8e7;border-radius: 5px;"/>
									<input type="hidden" id="tmpDefaultDay" value="${day}">
									<a href="javascript:delSelectedData('queryStime');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
									&nbsp;至 &nbsp;
									<input id="endtime" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'{%y+1}-%M-%d'})" style="width:120px;height:24px;border: 1px solid #95b8e7;border-radius: 5px;"/>
									<a href="javascript:delSelectedData('endtime');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
									<input type="hidden" id="vas">
									<shiro:hasPermission name="${menuAlias}:function:query">
										&nbsp;&nbsp;<a href="javascript:void(0)" style="margin-top: -4px" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									</shiro:hasPermission>
									<a style="margin-top: -4px" href="javascript:void(0)" onclick="popWinToOpCombine()" class="easyui-linkbutton" iconCls="icon-add">合并手术</a>
									&nbsp;&nbsp;<span style="height:14px;line-height:10px;margin-top: 10px;display:inline-block;background-color:#ffee01">&nbsp;&nbsp;</span>
									<span style="font-size:14px">表示已安排</span>
								</td>
							</tr>
						</table>
					
					
		    </div>	
			    <table id="list" class="easyui-edatagrid" data-options="toolbar:'#toolbarId',rownumbers:false,idField:'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true,pagination:true">
					<thead>
						<tr> 
							<th field="isurgent" width="3%" align="center" formatter="functionRowNum"  data-options="styler:functionColour"></th>	
							<th field="Id" hidden="true" ></th>
							<th data-options="field:'checkId',checkbox:true"  ></th>
							<th field="inDept" align="center" width="8%" formatter="functionDept">患者科室</th>
							<th field="name" align="center" width="8%" formatter="functionName" >姓名</th>
							<th field="opNameList" align="center" width="8%" formatter="functionOpname">手术名称</th>
							<th field="aneWay" align="center" width="9%" formatter="functionaneway">麻醉方式</th>
							<th field="opDoctor" align="center" width="9%" formatter="functiondoc">手术医生</th>
							<th field="preDate" align="center" width="9%" >手术时间</th>
							<th field="execDept" align="center" width="9%" formatter="functionexecDept">手术室</th>
							<th field="console" align="center" width="9%" formatter="functiontai">手术台</th>
							<th field="xunHuiList" align="center" width="9%" formatter="functionXiShou">巡回</th>
							<th field="xiShouList" align="center" width="9%" formatter="functionXiShou">洗手</th>
							<th field="zsDocList" align="center" width="9%" formatter="functionLinShi">临时助手医生</th>
							<th field="foreFlag" hidden="true" width="9%" >助手信息的状态</th>
							<th field="pasource" hidden="true" width="9%" >来源</th>
						</tr>
					</thead>
				</table>    
		</div>
	</div>
	<div id="shousap" ></div>
	<script type="text/javascript">
		if(deptCode==null||deptCode==''){
			 $("body").setLoading({
					id:"body",
					isImg:false,
					text:"请选择登录科室"
			});
		}
	
	</script>
<style type="text/css">
		.tableCss{
			border-collapse: collapse;border-spacing: 0;border: 1px solid #95b8e7;width:99.5%;
		}
		.tableCss .TDlabel{
			text-align: right;
			font-size:14px;
			width:40px;
		}
		.tableCss td{
			border: 1px solid #95b8e7;
			padding: 5px 5px;
			word-break: keep-all;
			white-space:nowrap;
		}
		.easyuiInput{
			width:100px;
		}
		.Input{
			width:150px;
		}
		.tooltip-bottom{
			z-index: 999999 !important;
		}
		.window .panel-header .panel-tool .panel-tool-close{
  	  		background-color: red;
  	  	}
</style>
</body>