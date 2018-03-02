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
<title>麻醉安排</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/themes/system/css/loader.css">
<script type="text/javascript">


var userMap="";//人员
var bedNoMap="";//床号
var aneWayMap="";//麻醉方式
var consoleMap="";//手术台 类型
var soleMap="";//手术台
var opTypeMap="";//手术类型
var deptMap = "";;//科室名称
var oldMap=new Map();//临时助手
var anesTypeMap=new Map();//麻醉类型
var zshs=null;
var sexMap=new Map();
var nurseMap=new Map();//护士翻页渲染
//护士数量
var indexfz=1;
	$(function() {
		/**  
		 *  
		 * @Description：获取人员
		 * @Author：zhangjin
		 * @CreateDate：2017-2-14 
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		$.ajax({
			url : "<%=basePath %>operation/anesthesia/getuserMap.action",
			type:"post",
			success : function(data){
				if(data!=null&&data!=""){
					userMap=data;
				}
			}
		});
	
	/**  
	 *  
	 * @Description：性别渲染
	 * @Author：zhangjin
	 * @CreateDate：2017-2-14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
		$.ajax({
			url : "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action",
			data:{type:"sex"},
			type:'post',
			success: function(data) {
				if(data){
					var v = data;
					for(var i=0;i<v.length;i++){
						sexMap.put(v[i].encode,v[i].name);
					}
				}
			}
		});
		
		
		/**  
		 *  
		 * @Description：手术台
		 * @Author：zhangjin
		 * @CreateDate：2017-2-14 
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		$.ajax({
			url:"<%=basePath%>operation/arrangement/getConsoleValid.action",
			data:{consoleFlg:"1"},
			success:function(data){
				soleMap=data;
			}
		});
		/**  
		 *  
		 * @Description：渲染科室名称
		 * @Author：zhangjin
		 * @CreateDate：2017-2-14 
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		 $.ajax({
				url:"<%=basePath%>operation/arrangement/querydeptComboboxs.action",
				type:"post",
				success : function(data){
					if(data!=null&&data!=""){
						deptMap=data;
					}
				}
			});
		 /**  
			 *  
			 * @Description：渲染手术类型
			 * @Author：zhangjin
			 * @CreateDate：2017-2-14 
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
		 $.ajax({
				url:"<%=basePath %>operation/anesthesia/anesthesopTypeMap.action",
				type:"post",
				success : function(data){
					if(data!=null&&data!=""){
						opTypeMap=data;
					}
				}
			});
		 /**  
			 *  
			 * @Description：渲染麻醉类型
			 * @Author：zhangjin
			 * @CreateDate：2017-2-14 
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
		 $.ajax({
			 url:"<%=basePath%>operation/operationList/queryCodeanesType.action",
				type:"post",
				success : function(data){
					if(data!=null&&data!=""){
						for(var i=0;i<data.length;i++){
							anesTypeMap.put(data[i].encode,data[i].name);
						}
					}
				}
			});
		 /**  
			 *  
			 * @Description：渲染手术台类型
			 * @Author：zhangjin
			 * @CreateDate：2017-2-14 
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
		 $.ajax({
				url:"<%=basePath %>operation/anesthesia/anesthesconsoleType.action",
				type:"post",
				success : function(data){
					if(data!=null&&data!=""){
					consoleMap=data;
					}
				}
			});
		 /**  
			 *  
			 * @Description：渲染麻醉方式
			 * @Author：zhangjin
			 * @CreateDate：2017-2-14 
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			$.ajax({
				url:"<%=basePath %>operation/anesthesia/anesthesaneWay.action",
				type:"post",
				success : function(data){
					if(data!=null&&data!=""){
					aneWayMap=data;
					}
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
			 /**  
			 *  
			 * @Description：未麻醉的datagrid
			 * @Author：zhangjin
			 * @CreateDate：2017-2-14 
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			 var columnsData = [
								[{
									field: 'ck',
									hidden: true
								},{
									field: 'clinicCode',
									title: '住院号',
									width: '6%',
									align: 'center'
								},{
									field: 'inDept',
									title: '患者科室',
									width: '7%',
									align: 'center',
									formatter: funcindept
								},{
									field: 'bedNo',
									title: "床号",
									width: '5%'
								},{
									field: 'name',
									title: "姓名",
									width: '5%'
								},{
									field: 'sex',
									title: '性别',
									width: '4%',
									align: 'center',
									formatter: funcsex
								},{
									field: 'age',
									title: '年龄',
									width: '4%',
									align: 'center',
									formatter: funcage
								},{
									field: 'diagnose1List',
									title: "术前诊断",
									width: '12%',
									align: 'center',
									formatter: funcdiagnose
								},{
									field: 'opName1List',
									title: '手术名称',
									width: '12%',
									align: 'center',
									formatter: funcopname
								},{
									field: 'opDoctor',
									title: '手术人',
									width: '5%',
									align: 'center',
									formatter: funcuser
								},{
									field: 'uniteDisease',
									title: "合并疾病",
									width: '5%'
								},{
									field: 'anesType',
									title: '麻醉类型',
									width: '5%',
									align: 'center',
									formatter: funcanesType
								},{
									field: 'aneWay',
									title: '麻醉方式',
									width: '5%',
									align: 'center',
									formatter: funcaneWay
								},{
									field: 'aneDoctor',
									title: "麻醉医生",
									width: '5%',
									formatter: funcuser
								},{
									field: 'opAssist1',
									title: '麻醉助手',
									width: '5%',
									align: 'center',
									formatter: funcuser
								}, {
									field: 'console',
									title: '手术台',
									width: '5%',
									align: 'center',
									formatter: funcconsole
								}, {
									field: 'opTempassist1List',
									title: "临时麻醉助手",
									width: '8%',
									formatter: funcoptemass
								}, {
									field: 'opType',
									title: "手术类型",
									hidden: true
								}, {
									field: 'preDate',
									title: "手术时间",
									hidden: true
								}, {
									field: 'consoleType',
									title: "手术台类型",
									hidden: true
								}, {
									field: 'isspecial',
									title: "是否特殊手术",
									hidden: true
								}, {
									field: 'applyRemark',
									title: "备注",
									hidden: true
								}, {
									field: 'patientNo',
									title: "病历号",
									hidden: true
								}, {
									field: 'OperationAnaerecord',
									title: "麻醉记录",
									hidden: true
								}
							]
						]
				var daytime=$("#queryStime").val().trim();
				var endtime=$("#endtime").val().trim();
				$("#listgridw").datagrid({
					url: '<%=basePath %>operation/anesthesia/queryBusinessOperationapply.action',
					pageSize:"10",
					pageList:[10,20,30,50,80,100],
					pagination:true,
					queryParams:{ch:'3',fore:'5',daytime:daytime,endtime:daytime},
					method:"post",
					columns: columnsData,
					onClickRow:function(rowIndex, rowData){
						 write(rowData);
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
					}}
				 })
			
			 /**  
			 *  
			 * @Description：拔管人
			 * @Author：zhangjin
			 * @CreateDate：2017-2-14 
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			$("#pulloutOpcd").combogrid({
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
		 	    onSelect:function(rowIndex, rowData){
		 	    	var thelpersia=$('[id^=thelpersia]');
		 	    	var id=$("#pulloutOpcd").combogrid("getValue");
					thelpersia.each(function(index,obj){
		 				if($(obj).combogrid('getValue') == id &&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
		 					$("#pulloutOpcd").combogrid("clear");
							$.messager.alert("提示","拔管人与助手之间不能重复！","info");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3000);
							return;
		 				}
					});
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
			 * @Description：临时麻醉助手
			 * @Author：zhangjin
			 * @CreateDate：2017-2-14 
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			$("#thelpersia0_").combogrid({//麻醉临时助手1
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
		 	    onSelect:function(rowIndex, rowData){
		 	    	var thelpersia=$('[id^=thelpersia]');
					var bg=$("#pulloutOpcd").combogrid("getValue");
					var lmzs=$("#lmzs0").val();
					var theMap=new Map();
					thelpersia.each(function(index,obj){
						var id=$(this).combogrid("getValue");
						if(theMap.get(id)=="1"){
							if(lmzs){
								$("#thelpersia0_"+lmzs).combogrid("clear");
								$.messager.alert("提示","助手之间不能重复！","info");
								setTimeout(function(){
									$(".messager-body").window('close');
								},2000);
								return;
							}else{
								$("#thelpersia0_").combogrid("clear");
								$.messager.alert("提示","助手之间不能重复！","info");
								setTimeout(function(){
									$(".messager-body").window('close');
								},2000);
								return;
							}
						}
						if($(obj).combogrid('getValue') == bg &&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
							$("#thelpersia0_"+lmzs).combogrid("clear");
							$.messager.alert("提示","助手与拔管人之间不能重复！","info");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3000);
							return;
		 				}
						
						if(id!=""&&id!=null){
							theMap.put(id,"1");	
						}
					})
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
			 * @Description：麻醉医生
			 * @Author：zhangjin
			 * @CreateDate：2017-2-14 
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			$("#aneDoctor").combobox({
				 url:"<%=basePath %>operation/anesthesia/anesthesaneDoctor.action",   
				 valueField : 'id',
				 textField : 'empName',
				 onSelect: function(record){
					 var opAss=$("#opAssist1").combobox("getValue");
					if(record.id==opAss){
						$("#aneDoctor").combobox("clear");
						$.messager.alert("提示","麻醉医生与助手医生重复！","info");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3000);
						return false;
					}
					return true;
				},onHidePanel:function(none){
				    var data = $(this).combobox('getData');
				    var val = $(this).combobox('getValue');
				    var result = true;
				    for (var i = 0; i < data.length; i++) {
				        if (val == data[i].id) {
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
					keys[keys.length] = 'id';
					keys[keys.length] = 'code';
					keys[keys.length] = 'empName';
					keys[keys.length] = 'pinyin';
					keys[keys.length] = 'wb';
					keys[keys.length] = 'inputcode';
					return filterLocalCombobox(q, row, keys);
				}
			});
			/**  
			 *  
			 * @Description：麻醉方式
			 * @Author：zhangjin
			 * @CreateDate：2017-2-14 
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			$('#aneWay').combobox({
				url : '<%=basePath %>operation/operationList/likeAneway.action',
					editable : false,
				    valueField:'encode',    
				    textField:'name'
				});
			/**  
			 *  
			 * @Description：助手医生
			 * @Author：zhangjin
			 * @CreateDate：2017-2-14 
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			$("#opAssist1").combobox({
				 url:"<%=basePath %>operation/anesthesia/anesthesaneDoctor.action",   
				    valueField : 'id',
					textField : 'empName',
					multiple : false,
					editable : true,
					onSelect: function(record){
						var aneDoc=$("#aneDoctor").combobox("getValue");
						if(record.id==aneDoc){
							$("#opAssist1").combobox("clear");
							$.messager.alert("提示","助手医生与麻醉医生重复！","info");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3000);
							return false;
						}
						return true;
					},onHidePanel:function(none){
					    var data = $(this).combobox('getData');
					    var val = $(this).combobox('getValue');
					    var result = true;
					    for (var i = 0; i < data.length; i++) {
					        if (val == data[i].id) {
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
						keys[keys.length] = 'id';
						keys[keys.length] = 'code';
						keys[keys.length] = 'empName';
						keys[keys.length] = 'pinyin';
						keys[keys.length] = 'wb';
						keys[keys.length] = 'inputcode';
						return filterLocalCombobox(q, row, keys);
					}
					
			});
			/**  
			 *  
			 * @Description：麻醉类型
			 * @Author：zhangjin
			 * @CreateDate：2017-2-14 
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
			$("#anesType").combobox({
				url:"<%=basePath%>operation/operationList/queryCodeanesType.action",
				valueField:'encode',    
			    textField:'name',
			    required:true,    
			    editable:true,
				filter:function(q,row){
					var keys = new Array();
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
			 * @Description：选项卡
			 * @Author：zhangjin
			 * @CreateDate：2017-2-14 
			 * @ModifyRmk：  
			 * @version 1.0
			 *
			 */
		$('#tt').tabs({
			onSelect: function(title,index){
					if(title=="未麻醉"){
						var daytime=$("#queryStime").val();
						var endtime=$("#endtime").val();
						$("#listgridw").datagrid({
							url: '<%=basePath %>operation/anesthesia/queryBusinessOperationapply.action',
							pageSize:"10",
							pageList:[10,20,30,50,80,100],
							pagination:true,
							queryParams:{ch:'3',fore:'5',daytime:daytime,endtime:endtime},
							method:"post",
							onClickRow:function(rowIndex, rowData){
								 write(rowData);
							}
				       })
					}else if(title=="已麻醉"){
						var daytime=$("#queryStime").val();
						var endtime=$("#endtime").val();
						  $("#listgridy").datagrid({//已麻醉
								url: '<%=basePath %>operation/anesthesia/queryBusinessOperationapply.action?menuAlias=${menuAlias}',
								queryParams:{ch:"2",daytime:daytime,endtime:endtime,fore:"5"},
								columns: columnsData,
								method:"post",
									onClickRow:function(rowIndex, rowData){
										 write(rowData);
									}
							});
					}else{
						var daytime=$("#queryStime").val();
						var endtime=$("#endtime").val();
						 $("#listgridqb").datagrid({//全部
								url: '<%=basePath %>operation/anesthesia/queryBusinessOperationapply.action?menuAlias=${menuAlias}',
								queryParams:{ch:"1",daytime:daytime,endtime:endtime,fore:"5"},
								method:"post",
								columns: columnsData,
								onClickRow:function(rowIndex, rowData){
									write(rowData);
								},
								rowStyler:function(index,row){
									if(row.isane=="1"){
										return "background:#ffee01;";
									}
								}
							});
					}
			  }
			});
	});

	/**  
	 *  
	 * @Description：添加临时助手
	 * @Author：zhangjin
	 * @CreateDate：2017-2-14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
function add(tdId,index,buta,aindex){
		var i=parseInt(index)+1;
		var nu=parseInt(aindex)+1;
		if(!(nu%4)){
			var l=nu/4;
			var m=l-1;
			$("#thsiatr"+m).after("<tr id=\"thsiatr"+l+"\">"+
					"<td align=\"right\" style=\"background-color: #E0ECFF;width=10%\" id=\""+tdId+i+"\"   class=\"medchangeskin\" >临时麻醉助手"+(nu+1)+"：</td>"+
					"<td width=15% id=\""+tdId+(i+1)+"\"><input id=\"thelpersia"+nu+"_\"  class=\"easyui-combogrid\" >"+
					"<a href=\"javascript:void(0)\" id=\""+buta+nu+"\" onclick=\"add('thsia','"+(i+1)+"','athsia','"+nu+"')\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-add'\">"+
					"<a id=\"j"+buta+nu+"\" href=\"javascript:void(0)\" onclick=\"removeTr('thsia','athsia','"+(i+1)+"','"+nu+"')\"></a>"+
					"</td> </tr>");
		}else{
			$("#"+tdId+index).after("<td align=\"right\" style=\"background-color: #E0ECFF;width=10%\" id=\""+tdId+i+"\" class=\"medchangeskin\" >临时麻醉助手"+(nu+1)+"：</td>"+
					"<td width=15% id=\""+tdId+(i+1)+"\"><input id=\"thelpersia"+nu+"_\"  class=\"easyui-combogrid\" >"+
					"<a href=\"javascript:void(0)\" id=\""+buta+nu+"\" onclick=\"add('thsia','"+(i+1)+"','athsia','"+nu+"')\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-add'\">"+
					"<a id=\"j"+buta+nu+"\" href=\"javascript:void(0)\" onclick=\"removeTr('thsia','athsia','"+(i+1)+"','"+nu+"')\"></a>"+
					"</td>");
		}
		
		$("#thelpersia"+nu+"_").combogrid({//麻醉临时助手1
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
	 	    onSelect:function(rowIndex, rowData){
	 	    	var thelpersia=$('[id^=thelpersia]');
				var bg=$("#pulloutOpcd").combogrid("getValue");
				var theMap=new Map();
				thelpersia.each(function(index,obj){
					var id=$(this).combogrid("getValue");
					if(theMap.get(id)=="1"){
						$(this).combogrid("clear");
						$.messager.alert("提示","助手之间不能重复！","info");
						setTimeout(function(){
							$(".messager-body").window('close');
						},2000);
						return;
					}
					if($(obj).combogrid('getValue') == bg &&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
						$("#thelpersia"+nu+"_").combogrid("clear");
						$.messager.alert("提示","助手与拔管人之间不能重复！","info");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3000);
						return;
	 				}
					if(id!=""&&id!=null){
						theMap.put(id,"1");	
					}
					
				});
		     },onLoadSuccess: function () {
		    	var id=$(this).prop("id");
			       if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
		            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
		            } 
		        } 
		});
		$('#'+buta+nu).linkbutton({    
		    iconCls: 'icon-add'   
		});  
		$('#j'+buta+nu).linkbutton({    
		    iconCls: 'icon-remove'   
		}); 
		$('#'+buta+aindex).hide();
		$("#j"+buta+aindex).hide();
		indexfz=nu;
	}
/**  
 *  
 * @Description：删除指定项
 * @Author：zhangjin
 * @CreateDate：2016-6-16
 * @version 1.0
 * @throws IOException 
 *
 */
function removeTr(trId,aId,index,anu){
	var nu = parseInt(index)-1;
	var i=parseInt(anu)-1;
	$("#"+trId+index).remove();
	$("#"+trId+nu).remove();
 	$('#'+aId+i).show();
	$('#j'+aId+i).show();
}
	/**
	 * @Description:渲染科室患者
	 * @Author: huangbiao
	 * @CreateDate: 2016年4月12日
	 * @param1:value:单元格的值
	 * @param2:rowData：行数据
	 * @param3:rowIndex:索引
	 * @return:科室名称
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
function funcindept(value,rowData,rowIndex){
		 if(value!=null&&value!=""){
			 return deptMap[value];
		 }
}
	//渲染患者科室
	function funcsex(value,rowData,rowIndex){
		return sexMap.get(value);
	}
	
	//渲染患者性别
	function funcage(value,rowData,rowIndex){
		var age=!rowData.age?"":rowData.age;
		var ageUnit=!rowData.ageUnit?"":rowData.ageUnit;
		if(age==0){
			return "0"+ageUnit;
		}else{
			return age+ageUnit;
		}
		
	}
	
	/**  
	 *  
	 * @Description：渲染员工
	 * @Author：zhangjin
	 * @CreateDate：2017-2-14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	function funcuser(value,rowData,rowIndex) {
		if(value!=null&&value!=""){
			return userMap[value];
		}
		
	}

	
	/**  
	 *  
	 * @Description：渲染术前诊断
	 * @Author：zhangjin
	 * @CreateDate：2017-2-14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	function funcdiagnose(value,rowData,rowIndex){
		var name;
		if(value!=null&&value!=""){
			for(var i=0;i<value.length;i++){
				if(value[i].diagName!=null&&value[i].diagName!=""){
					if(i==0){
						name=value[i].diagName+",";
					}else{
						name+=value[i].diagName+",";
					}
				}else{
					return "未";
				}
			}
			return name.substring(0,name.length-1);
		}else{
			return "";
		}
	}
	
	/**  
	 *  
	 * @Description：临时麻醉助手
	 * @Author：zhangjin
	 * @CreateDate：2017-2-14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	function  funcoptemass(rowValue,rowData,rowIndex){
		var name="";
		if(rowValue!=null&&rowValue!=""){
			$.each(rowValue,function(index,value){
				if(index == rowValue.length-1){
					name+=value.emplName||"";
				}else{
					result1=value.emplName||"";
					name+=result1+",";
				}
			});
			return name;
		}else{
			return name;
		}
	}
	/**  
	 *  
	 * @Description：麻醉类型
	 * @Author：zhangjin
	 * @CreateDate：2017-2-14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	function funcanesType(value,rowData,rowIndex){
		if(value!=null&&value!=""){
			return anesTypeMap.get(value);
		}
		return "";
	}
	/**  
	 *  
	 * @Description：渲染麻醉方式
	 * @Author：zhangjin
	 * @CreateDate：2017-2-14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	function funcaneWay(value,rowData,rowIndex){
		if(value!=null&&value!=""){
			return aneWayMap[value];
		}
		return value;
	}
	 /**  
	 *  
	 * @Description：渲染手术台
	 * @Author：zhangjin
	 * @CreateDate：2017-2-14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	function funcconsole(value,rowData,rowIndex){
		if(value!=null&&value!=""){
			return soleMap[value];
		}
		return "";
	}
	 /**  
	 *  
	 * @Description：渲染手术名称
	 * @Author：zhangjin
	 * @CreateDate：2017-2-14 
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	function funcopname(value,rowData,rowIndex){
		var name;
		if(value!=null&&value!=""){
			for(var i=0;i<value.length;i++){
				if(value[i].itemName!=null&&value[i].itemName!=""){
					if(i==0){
						name=value[i].itemName+",";
					}else{
						name+=value[i].itemName+",";
					}
					
				}else{
					return "未";
				}
			}
			return name.substring(0,name.length-1);
		}else{
			return "";
		}
	}
	
	/**
	 * @Description:查询按钮
	 * @Author: zhangjin
	 * @CreateDate: 2017年2月14日
	 * @param:
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	function searchFrom(){
		var daytime=$("#queryStime").val().trim();
		var endtime=$("#endtime").val().trim();
		var pp = $('#tt').tabs('getSelected'); 
		var tab = pp.panel('options').title;
		if(tab=="未麻醉"){
			$("#listgridw").datagrid({
				url: '<%=basePath %>operation/anesthesia/queryBusinessOperationapply.action?menuAlias=${menuAlias}',
				queryParams:{ch:"3",daytime:daytime,endtime:endtime,fore:"5"},
				method:"post",
				onClickRow:function(rowIndex, rowData){
					write(rowData);
				}
			});	
		}else if(tab=="已麻醉"){
			 $("#listgridy").datagrid({
					 url: '<%=basePath %>operation/anesthesia/queryBusinessOperationapply.action?menuAlias=${menuAlias}',
						queryParams:{ch:"2",daytime:daytime,endtime:endtime,fore:"5"},
						method:"post",
						onClickRow:function(rowIndex, rowData){
							write(rowData);
						}
					});
		}else if(tab=="全部"){
			 $("#listgridqb").datagrid({
					 url: '<%=basePath %>operation/anesthesia/queryBusinessOperationapply.action?menuAlias=${menuAlias}',
						queryParams:{ch:"1",daytime:daytime,endtime:endtime,fore:"5"},
						method:"post",
						onClickRow:function(rowIndex, rowData){
							write(rowData);
						}
					});
		}
	}	
	/**
	 * @Description:显示详细信息
	 * @Author: zhangjin
	 * @CreateDate: 2017年2月14日
	 * @param:
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
		function write(rowData){
			 del();
				$("#id").val(rowData.id);
				$("#anesType").combobox("select",rowData.anesType);//麻醉类型
				$("#aneWay").combobox("select",rowData.aneWay);//麻醉方式
				$("#console").val(soleMap[rowData.console]);//手术台
				$("#clinicCode").val(rowData.clinicCode);//住院号
				$("#pasource").val(rowData.pasource);//来源
				$("#patientNo").val(rowData.patientNo);//病历号
				$("#inDept").val(deptMap[rowData.inDept]);
				$("#in").val(rowData.inDept);//患者科室			
				if(rowData.OperationAnaerecord!=null){
					$("#anaeDate").val(rowData.OperationAnaerecord.anaeDate);//麻醉时间
					$("#inpacuDate").val(rowData.OperationAnaerecord.inpacuDate);//入室时间
					$("#outpacuDate").val(rowData.OperationAnaerecord.outpacuDate);//岀室时间
					$("#inpacuStatus").textbox("setValue",rowData.OperationAnaerecord.inpacuStatus);//入室状态
					$("#outpacuStatus").textbox("setValue",rowData.OperationAnaerecord.outpacuStatus);//岀室状态
					if(rowData.OperationAnaerecord.pulloutOpcd){
						$("#pulloutOpcd").combogrid('grid').datagrid('load',{q:rowData.OperationAnaerecord.pulloutOpcd});
						$("#pulloutOpcd").combogrid("setValue",rowData.OperationAnaerecord.pulloutOpcd);//拔管人
					}
					$("#demuDays").numberbox("setValue",rowData.OperationAnaerecord.demuDays);//镇痛天数
					$("#pulloutDate").val(rowData.OperationAnaerecord.pulloutDate);//拔管时间
					$("#applyRemark").textbox('setValue',rowData.OperationAnaerecord.remark);//备注
				}else{
					$("#applyRemark").textbox('setValue',rowData.applyRemark);//备注
				}
				for(var a=0;a<rowData.opTempassist1List.length;a++){
					var id=rowData.opTempassist1List[a].id;
					var code=rowData.opTempassist1List[a].emplCode;
					var name=rowData.opTempassist1List[a].emplName;
					if(a!=0){
						if(!(a%4)){
							var l=a/4;
							var m=l-1;
							var i=2*a;
							$("#thsiatr"+m).after("<tr id=\"thsiatr"+l+"\">"+
									"<td align=\"right\" style=\"background-color: #E0ECFF;width=10%\" id=\"thsia"+(i+1)+"\" class=\"medchangeskin\" >临时麻醉助手"+(a+1)+"：</td>"+
									"<td width=15% id=\"thsia"+(i+2)+"\"><input id=\"thelpersia"+a+"_"+id+"\"  class=\"easyui-combogrid\"  >"+
									"<a href=\"javascript:void(0)\" id=\"athsia"+a+"\" onclick=\"add('thsia','"+(i+2)+"','athsia','"+a+"')\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-add'\">"+
									"<a id=\"jathsia"+a+"\" href=\"javascript:void(0)\" onclick=\"removeTr('thsia','athsia','"+(i+2)+"','"+a+"')\"></a>"+
									"</td> </tr>");
						}else{
							var i=2*a;
								$("#thsia"+i).after("<td align=\"right\" style=\"background-color: #E0ECFF;width=10%\" id=\"thsia"+(i+1)+"\" class=\"medchangeskin\" >临时麻醉助手"+(a+1)+"：</td>"+
										"<td width=15% id=\"thsia"+(i+2)+"\"><input id=\"thelpersia"+a+"_"+id+"\" class=\"easyui-combogrid\"   >"+
										"<a href=\"javascript:void(0)\" id=\"athsia"+a+"\" onclick=\"add('thsia','"+(i+2)+"','athsia','"+a+"')\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-add'\">"+
										"<a id=\"jathsia"+a+"\" href=\"javascript:void(0)\" onclick=\"removeTr('thsia','athsia','"+(i+2)+"','"+a+"')\"></a>"+
										"</td>");
						}
						$("#thelpersia"+a+"_"+id).combogrid({//麻醉临时助手1
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
					 	    	var thelpersia=$('[id^=thelpersia]');
					 	    	var bg=$("#pulloutOpcd").combogrid("getValue");
								var theMap=new Map();
								var id=$(this).prop("id");
								thelpersia.each(function(index,obj){
									var value=$(this).combogrid("getValue");
									if(theMap.get(value)!="1"){
										theMap.put(value,"1");
									}else{
										$("#"+id).combogrid("clear");
										$.messager.alert("提示","助手之间不能重复！","info");
										setTimeout(function(){
											$(".messager-body").window('close');
										},2000);
										return;
									}
									if($(obj).combogrid('getValue') == bg &&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
										$(this).combogrid("clear");
										$.messager.alert("提示","助手与拔管人之间不能重复！","info");
										setTimeout(function(){
											$(".messager-body").window('close');
										},3000);
										return;
					 				}
								});
						 	 },onLoadSuccess: function () {
							    	var id=$(this).prop("id");
								       if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
							            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
							            } 
							    }  
						});
						$('#athsia'+a).linkbutton({    
						    iconCls: 'icon-add'   
						});  
						$('#jathsia'+a).linkbutton({    
						    iconCls: 'icon-remove'   
						}); 
						$('#athsia'+(a-1)).hide();
						$("#jathsia"+(a-1)).hide();
						if(code){
							$("#thelpersia"+a+"_"+id).combogrid('grid').datagrid('load',{q:code});
							$("#thelpersia"+a+"_"+id).combogrid("setValue",code);
						}
					}else{
						$("#thelpersia0_").prop("id","thelpersia0_"+id);
						$("#lmzs0").val(id);
						if(code){
							$("#thelpersia0_"+id).combogrid('grid').datagrid('load',{q:code});
							$("#thelpersia0_"+id).combogrid("setValue",code);
						}
					}
					
					oldMap.put(id,name);
					indexfz=rowData.opTempassist1List.length;
				}
	
				$("#inDept").val(deptMap[rowData.inDept]);
				$("#in").val(rowData.inDept);//患者科室
				$("#bedNo").val(rowData.bedNo);//床号
				$("#bed").val(rowData.bedNo);//床号
				$("#name").val(rowData.name);//姓名
				$("#namehi").val(rowData.name);//姓名
				$("#sex").val(funcsex(rowData.sex));//性别
				$("#sexhi").val(rowData.sex);//性别
				var age=!rowData.age?"":rowData.age;
				var ageUnit=!rowData.ageUnit?"":rowData.ageUnit;
				if(age==0){
					$("#age").val("0"+ageUnit);//年龄
				}else{
					$("#age").val(age+ageUnit);//年龄
				}
				var diagname;
				for(var i=0;i<rowData.diagnose1List.length;i++){
					if(i==0){
						$("#diagnose1").val(rowData.diagnose1List[0].diagName);//术前诊断
					}else{
						$("#trsqzd"+(i-1)).after("<tr id=\"trsqzd"+i+"\">"+
                 	 "<td width=10% align=\"right\" style=\"background-color: #E0ECFF;\" class=\"medchangeskin\">术前诊断"+(i+1)+"：</td>"+
                      "<td  width=90% colspan=\"7\"><input id=\"diagnose"+(i+1)+"\" readonly=\"readonly\" style=\"border: 0px;\"></td>"+
			    			"</tr>");
						$("#diagnose"+(i+1)).val(rowData.diagnose1List[i].diagName);
					}
				}
				
				$("#opType").val(opTypeMap[rowData.opType]);//手术类型
				$("#opTypes").val(rowData.opType);//手术类型
				var opname;
				for(var i=0;i<rowData.opName1List.length;i++){
					if(i==0){
						$("#opName1").val(rowData.opName1List[0].itemName);
					}else{
						$("#trndss"+(i-1)).after("<tr id=\"trndss"+i+"\">"+
		                    	 "<td width=10% align=\"right\" style=\"background-color: #E0ECFF;\" class=\"medchangeskin\">手术名称"+(i+1)+"：</td>"+
		                         "<td  width=90% colspan=\"7\"><input id=\"opName"+(i+1)+"\" readonly=\"readonly\" style=\"border: 0px;\"></td>"+
					    			"</tr>");
						$("#opName"+(i+1)).val(rowData.opName1List[i].itemName);
					}
				}
				$("#opDoctor").val(userMap[rowData.opDoctor]);//手术人
				$("#do").val(rowData.opDoctor);//手术人
				$("#preDate").val(rowData.preDate);//手术时间
				if(rowData.aneDoctor!=null&&rowData.aneDoctor!=""){
					$("#aneDoctor").combobox("select",rowData.aneDoctor);//麻醉医生
				}
				if(rowData.opAssist1!=null&&rowData.opAssist1!=""){
					$("#opAssist1").combobox("select",rowData.opAssist1);//助手医生
				}
				$("#consoleType").val(consoleMap[rowData.consoleType]);//手术台类型
				$("#consoletypeh").val(rowData.consoleType);//手术台类型
				if(rowData.isspecial==1||rowData.isspecial=="1"){
					$("#isspecial").val("是");//是否特殊手术
				}else{
					$("#isspecial").val("否");//是否特殊手术
				}
				$("#iss").val(rowData.isspecial);//是否特殊手术
			
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
	
			
	/**
	 * @Description:保存按钮
	 * @Author: zhangjin
	 * @CreateDate: 2017年2月14日
	 * @param:
	 * @return:
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	function save(){
		var op;
		var thelpersia="";
		var isRecord="5";
		var newMap=new Map();
		var lmzs=$('[id^=thelpersia]');
		var opId=$("#id").val();
		lmzs.each(function(){
			var val=$(this).prop("id");
			var id=val.substring(val.indexOf("_")+1);
			var num=val.substring(10,11);
			var newValue=$("#"+val).combogrid("getValue");
			var newText=$("#"+val).combogrid("getText");
			newMap.put(newValue,newText);
			var oldValue=oldMap.get(newValue);
			if(!oldValue){
				if(newValue){
					thelpersia+=newValue+","+num+","+newText+"_add#";
				}
			}else{
				if(!newValue){
					thelpersia+= id+",_del#";//删除
				}else{
					if(oldValue!=newValue){
						thelpersia+= id+","+newValue+","+num+","+newText+"_upd#";//更新
					}
				}
			}
		});
		if(opId!=null&&opId!=""){
			oldMap.each(function(key,value,index){
				if(newMap.get(key)==null||newMap.get(key)==""){
					thelpersia+=key+",_del#";//删除
				}
			});	
		}
		var id = $("#id").val();
		if(id==null||id==""){
			$.messager.alert("提示","请选择一条记录！","info");
			setTimeout(function(){
				$(".messager-body").window('close');
			},2000);
			return false;
		}
		var outval=$('#outpacuDate').val();
		var inp=$("#inpacuDate").val();
		if(inp!=""){
			if(outval!=""){
				if(outval<=inp){
					$.messager.alert("提示","出室时间不能早于入室时间","info");
					setTimeout(function(){
						$(".messager-body").window('close');
					},2500);
					return;
				}
			}else{
				$.messager.alert("提示","请填写出室时间","info");
				setTimeout(function(){
					$(".messager-body").window('close');
				},2000);
				return;
			}
		}else{
			$.messager.alert("提示","请填写入室时间","info");
			setTimeout(function(){
				$(".messager-body").window('close');
			},2000);
			return;
		}
		
		if (!$('#anaeDate').val()) {
			$.messager.alert("提示","请填写麻醉时间","info");
			setTimeout(function(){
				$(".messager-body").window('close');
			},2000);
			return;
		}
		if($("#anaeDate").val().length<19){
			$.messager.alert("提示","请填写麻醉时间","info");
			setTimeout(function(){
				$(".messager-body").window('close');
			},2000);
			return;
		}
		
		var opAssist=$("#opAssist1").combobox("getText");
		var aneDoctor=$("#aneDoctor").combobox("getText");
		$('#pohtoForm').form('submit', {
			url : "<%=basePath %>operation/anesthesia/saveOperationApplyssss.action",
			onSubmit:function(param){
				if (!$('#pohtoForm').form('validate')) {
					$.messager.progress('close');
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					return false;
				}
				$.messager.progress({text:'保存中，请稍后...',modal:true});
				param.thelpersia=thelpersia,
				param.isRecord=isRecord,
				param.opAssist=opAssist,
				param.aneDoctor=aneDoctor
			},
			success : function(data) {
			  $.messager.progress('close');
			  if(data!="error"){
				  $.messager.alert("提示","保存成功");
		            del();
		            var pp = $('#tt').tabs('getSelected'); 
		    		var tab = pp.panel('options').title;
		    		if(tab=="未麻醉"){
		    			 $("#listgridw").datagrid('reload');
		    		}else if(tab=="已麻醉"){
		    			 $("#listgridy").datagrid('reload');
		    		}else if(tab=="全部"){
		    			  $("#listgridqb").datagrid('reload'); 
		    		}
			  }else{
					$.messager.alert("提示","保存失败");
			  }
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert("提示","保存失败");
			}
		 }); 
	}
	
	 /**  
	 *  
	 * @Description：过滤	
	 * @Author：zhangjin
	 * @CreateDate：2016-11-2
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
	
	//清空
	function del(){
		oldMap=new Map();
		$('#pohtoForm').form('reset');
		$("#clinicCode").val("");//住院号
		$("#pasource").val("");//来源
		$("#patientNo").val("");//病历号
		$("#inDept").val("");
		$("#in").val("");//患者科室
		$("#bedNo").val("");//床号
		$("#bed").val("");//床号
		$("#name").val("");//姓名
		$("#sex").val("");//性别
		$("#sexhi").val("");//性别
		$("#age").val("");//年龄
		$("#diagnose1").val("");//术前诊断
		$("#diag").val("");//术前诊断
		$("#opType").val("");//手术类型
		$("#opTypes").val("");//手术类型
		$("#opName1").val("");//手术名称
		$("#op").val("");//手术名称
		$("#opDoctor").val("");//手术人
		$("#do").val("");//手术人
		$("#preDate").val("");//手术时间
		var sszd=$('[id^=trsqzd]');
		sszd.each(function(){
			var id=$(this).prop("id");
			var num=id.substring(6,7);
			if(parseInt(num)>0){
				$(this).remove();
			}
		});
		var ssmc=$('[id^=trndss]');
		ssmc.each(function(){
			var id=$(this).prop("id");
			var num=id.substring(6,7);
			if(parseInt(num)>0){
				$(this).remove();
			}
		});
		 var thsia=$('[id^=thsia]');
		thsia.each(function(){
			var id=$(this).prop("id");
			var num=id.substring(5,6);
			if(parseInt(num)>2){
				$(this).remove();
			}
		});
	
		
		 //删除助手护士信息
		for(var i=0;i<indexfz;i++){
			if(i!=0){
				$("#thsiatr"+i).remove();
				$('#athsia'+(i-1)).show();
				$('#jathsia'+(i-1)).show();
			}else{
				 var tr=$('[id^=thelpersia]');
				 tr.each(function(){
					 var id=$(this).prop('id');
						var num=id.substring(7,8);
					var id=$(this).prop("id","thelpersia0_");
				});
			}
			
		}
		indexfz=1; 
		$('#aneWay1').val("");//麻醉方式
		$("#consoleType").val("");//手术台类型
		$("#consoletypeh").val("");//手术台类型
		
		$("#iss").val("");//是否特殊手术
	}

	//重置
	function reset(){
		oldMap=new Map();
		$('#pohtoForm').form('reset');
		$("#clinicCode").val("");//住院号
		$("#pasource").val("");//来源
		$("#patientNo").val("");//病历号
		$("#inDept").val("");
		$("#in").val("");//患者科室
		$("#bedNo").val("");//床号
		$("#bed").val("");//床号
		$("#name").val("");//姓名
		$("#sex").val("");//性别
		$("#sexhi").val("");//性别
		$("#age").val("");//年龄
		$("#diagnose1").val("");//术前诊断
		$("#diag").val("");//术前诊断
		$("#opType").val("");//手术类型
		$("#opTypes").val("");//手术类型
		$("#opName1").val("");//手术名称
		$("#op").val("");//手术名称
		$("#opDoctor").val("");//手术人
		$("#do").val("");//手术人
		$("#preDate").val("");//手术时间
		var sszd=$('[id^=trsqzd]');
		sszd.each(function(){
			var id=$(this).prop("id");
			var num=id.substring(6,7);
			if(parseInt(num)>0){
				$(this).remove();
			}
		});
		var ssmc=$('[id^=trndss]');
		ssmc.each(function(){
			var id=$(this).prop("id");
			var num=id.substring(6,7);
			if(parseInt(num)>0){
				$(this).remove();
			}
		});
		 var thsia=$('[id^=thsia]');
		thsia.each(function(){
			var id=$(this).prop("id");
			var num=id.substring(5,6);
			if(parseInt(num)>2){
				$(this).remove();
			}
		});
	
		
		 //删除助手护士信息
		for(var i=0;i<indexfz;i++){
			if(i!=0){
				$("#thsiatr"+i).remove();
				$('#athsia'+(i-1)).show();
				$('#jathsia'+(i-1)).show();
			}else{
				 var tr=$('[id^=thelpersia]');
				 tr.each(function(){
					 var id=$(this).prop('id');
						var num=id.substring(7,8);
					var id=$(this).prop("id","thelpersia0_");
				});
			}
			
		}
		indexfz=1; 
		$('#aneWay1').val("");//麻醉方式
		$("#consoleType").val("");//手术台类型
		$("#consoletypeh").val("");//手术台类型
		$("#queryStime").val("${day}");
		$("#endtime").val('');
		$("#iss").val("");//是否特殊手术
		searchFrom();
	}
	 /**  
	 * @Description：麻醉时间  
	 * @Author：zhangjin
	 * @CreateDate：2017-2-14 
	 * @ModifyRmk：  hedong 2017-03-22 更改日期控件后更改事件绑定的方式
	 * @version 1.0
	 */
	function pickedAnaeDateFunc(){
		var value=$("#anaeDate").val();
		var preDate=$("#preDate").val();
		if(value!=""){
			if(value>preDate){
				$("#anaeDate").val("");
				$.messager.alert("提示","麻醉时间不能在手术时间之后！","info");
				setTimeout(function(){
					$(".messager-body").window('close');
				},2000);
			}else{
				var pre=new Date(preDate).getTime();
				var val=new Date(value).getTime();
				var date3=pre-val;  //时间差的毫秒数
				//计算出相差天数
				var days=Math.floor(date3/(24*3600*1000));
				//计算出小时数
				var leave1=date3%(24*3600*1000);//计算天数后剩余的毫秒数
				var hours=Math.floor(leave1/(3600*1000));
				//计算相差分钟数
				var leave2=leave1%(3600*1000);//计算小时数后剩余的毫秒数
				var minutes=Math.floor(leave2/(60*1000));
				//计算相差秒数
				var leave3=leave2%(60*1000);//计算分钟数后剩余的毫秒数
				var seconds=Math.round(leave3/1000);
				$.messager.alert("提示","麻醉时间提前手术时间 "+days+"天 "+hours+"小时 "+minutes+" 分钟"+seconds+" 秒");
			}
		} 
	 }
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
	<div id="cc" class="easyui-layout anesthesiaList" style="width:100%;height:100%;">
	    <div data-options="region:'north',split:false,border:true" style="width:100%;height:357px;border-top:0">
           <table id="searchTab" style="width:800px;height:10%;">
				<tr>
					<td style="padding:5px;" class="anesthesiaList">
					   <shiro:hasPermission name="${menuAlias }:function:save">
						<a  href="javascript:save()"  class="easyui-linkbutton" iconCls="icon-save">保存</a>
					   </shiro:hasPermission>
					    <a href="javascript:void(0)" id="print" 
					       class="easyui-linkbutton"  data-options="iconCls:'icon-clear'" onclick="reset()" >重置</a>
					</td>
				</tr>
			</table>
			
			<form id="pohtoForm" method="post">
		     <table id="list" class="honry-table" style="width: 100%">
                    <tr>
                         <td align="right" style="width:10%;background-color: #E0ECFF; " class="medchangeskin">患者科室：<input id="clinicCode" name="opApplyVo.clinicCode" type="hidden"></td>
                         <td width=15% ><input id="inDept" readonly="readonly" style="border: 0px;" ><input id="in" name="opApplyVo.inDept" type="hidden"></td>
                         <td width=10% align="right"  style="background-color: #E0ECFF;" class="medchangeskin">床号：<input id="pasource" name="pasource" type="hidden"></td>
                         <td width=15% ><input id="bedNo" readonly="readonly" style="border: 0px;"><input id="bed"  type="hidden"></td>
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">姓名<input id="patientNo" name="opApplyVo.patientNo" type="hidden">：</td>
                         <td width=15% ><input id="name"  readonly="readonly" style="border: 0px;">
                         <input id="namehi" name="opApplyVo.name" type="hidden" style="border: 0px;"></td>
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">性别<input id="id" name="opApplyVo.id" type="hidden">：</td>
                         <td width=15% ><input id="sex" readonly="readonly" style="border: 0px;"><input id="sexhi" name="opApplyVo.sex" type="hidden"></td>
                    </tr>
                    <tr>
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">年龄：</td>
                         <td ><input id="age" name="age" readonly="readonly" style="border: 0px;"></td>
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">手术类型：</td>
                         <td width=15% ><input id="opType" readonly="readonly"  style="border: 0px;"><input id="opTypes" name="opType" type="hidden"></td>
                           <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">手术台类型：</td>
                         <td width=15% ><input id="consoleType" readonly="readonly" style="border: 0px;"><input id="consoletypeh" name="consoleType"  type="hidden"></td>
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">手术台：</td>
                         <td width=15% ><input id="console" style="border: 0px;" name="console" readonly="readonly" ></td>
                    </tr>
                    <tr id="trsqzd0">
                    	 <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">术前诊断1：</td>
                         <td  width=90% colspan="7"><input id="diagnose1" readonly="readonly" style="border: 0px;"></td>
                    </tr>
                    <tr id="trndss0">
                    	 <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">手术名称1：</td>
                         <td width=90%  colspan="7"><input id="opName1" readonly="readonly" style="border: 0px;width:80%;size: 1000;"></td>
                    </tr>
                    <tr>
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">手术人：</td>
                         <td width=15% ><input id="opDoctor" readonly="readonly" style="border: 0px;"><input id="do" name="opDoctor" type="hidden"></td>
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">手术时间：</td>
                         <td width=15% ><input id="preDate" name="preDate" readonly="readonly" style="border: 0px;"></td>
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">麻醉医生：</td>
                         <td width=15% >
                         	<input id="aneDoctor" required="true" name="opApplyVo.aneDoctor" class="easyui-combobox">
                         	<a href="javascript:delSelectedData('aneDoctor');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
                         </td>
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">助手医生：</td>
                         <td width=15% ><input id="opAssist1" required="true" name="opApplyVo.opAssist1" class="easyui-combobox">
                         <a href="javascript:delSelectedData('opAssist1');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
                         
                    </tr>
                    <tr>
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">麻醉类型：</td>
                         <td width=15% ><input id="anesType" name="opApplyVo.anesType" class="easyui-combobox" required="true"></td>
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">麻醉方式：</td>
                         <td width=15% ><input id="aneWay" style="border: 0px;" name="opApplyVo.aneWay" class="easyui-combobox" required="true"> </td>
                        <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">是否特殊手术：</td>
                         <td width=15% ><input id="isspecial" readonly="readonly" style="border: 0px;"><input id="iss" name="isspecial" type="hidden"></td>
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">备注：</td>
                         <td width=15%  ><input id="applyRemark" class="easyui-textbox" name="opApplyVo.applyRemark" style="border: 0px;"></td>
                    </tr>
                     <tr class="anesthesiaListDateSize">
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">麻醉时间：</td>
                         <td width=15% >
                         <input id="anaeDate" name="opApplyVo.anaeDate" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:pickedAnaeDateFunc,maxDate:'{%y+1}-%M-%d'})" style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
                         </td>
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">拔管时间：</td>
                         <td width=15% >
                         <input id="pulloutDate" name="opApplyVo.pulloutDate" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'{%y+1}-%M-%d'})" style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
                         </td>
                        <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">拔管人：</td>
                         <td width=15% ><input id="pulloutOpcd" name="opApplyVo.pulloutOpcd"  style="border: 0px;" class="easyui-combogrid"></td>
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">镇痛天数：</td>
                         <td width=15%  ><input id="demuDays" class="easyui-numberbox" name="opApplyVo.demuDays" style="border: 0px;"></td>
                    </tr>
                     <tr class="anesthesiaListDateSize">
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">入室时间：</td>
                         <td width=15% >
                         <input id="inpacuDate" name="opApplyVo.inpacuDate" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'{%y+1}-%M-%d'})" style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
                         </td>
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">入室状态：</td>
                         <td width=15% ><input id="inpacuStatus" style="border: 0px;" name="opApplyVo.inpacuStatus" class="easyui-textbox" > </td>
                        <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">出室时间：</td>
                         <td width=15% >
                         <input id="outpacuDate" name="opApplyVo.outpacuDate" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'{%y+1}-%M-%d'})" style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
                         </td>
                         <td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">出室状态：</td>
                         <td width=15%  ><input id="outpacuStatus" class="easyui-textbox" name="opApplyVo.outpacuStatus" style="border: 0px;"></td>
                    </tr>
                    <tr id="thsiatr0">
                    	<td width=10% align="right" style="background-color: #E0ECFF;" class="medchangeskin">临时麻醉助手1：<input id="lmzs0" type="hidden"></td>
                         <td width=15% id="thsia2"><input id="thelpersia0_"  class="easyui-combogrid" ">
                         <a href="javascript:void(0)" id="athsia0" onclick="add('thsia','2','athsia','0')" class="easyui-linkbutton" data-options="iconCls:'icon-add'"></td>
                    </tr>
             </table>
           </form>
	    </div> 
	    <div data-options="region:'center',split:false,border:false" style="width: 100%;">
		    <div id="tt" class="easyui-tabs" data-options="fit:true" tabPosition='bottom'>
			    <div title="未麻醉" style="width:100%;border: 0px;" >
			         <table id="listgridw" style="width: 100%;" data-options="idField:'id',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:true,fixed:true,fit:true,pagination:true,toolbar:'#toolbarId'"></table>
			    </div>   
			    <div title="已麻醉"  style="width:100%;">   
			        <table id="listgridy" style="width:100%;" data-options="idField:'id',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:true,fixed:true,fit:true,pagination:true,toolbar:'#toolbarId'"></table>   
			    </div>   
			    <div title="全部"  style="width:100%;">   
			       <table id="listgridqb" style="width: 100%;" data-options="idField:'id',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:true,fixed:true,fit:true,pagination:true,toolbar:'#toolbarId'"></table>    
			    </div>   
			</div>  
	    </div>
    </div> 
    <div id="toolbarId">
		<span style="padding-left: 8px;"></span>手术时间 ：<input id="queryStime" value="${day}" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'{%y+1}-%M-%d'})" style="width:120px;height:24px;border: 1px solid #95b8e7;border-radius: 5px;"/>
	    &nbsp;至&nbsp;
		<input id="endtime"  class="Wdate" type="text" value="${day}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'{%y+1}-%M-%d'})" style="width:120px;height:24px;border: 1px solid #95b8e7;border-radius: 5px;"/>
		<shiro:hasPermission name="${menuAlias}:function:query">
			&nbsp;&nbsp; <a href="javascript:void(0)" onclick="searchFrom()"  style="margin-top: -3px;" class="easyui-linkbutton" iconCls="icon-search">查询</a>
		</shiro:hasPermission>
		&nbsp;&nbsp;<span style="height:14px;line-height:10px;display:inline-block;background-color:#ffee01">&nbsp;&nbsp;</span>
		<span style="font-size:14px" class="tip">表示已麻醉</span>
	</div> 
</body>
</html>
</html>
