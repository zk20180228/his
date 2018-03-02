<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>患者生命体征维护</title>
	<%@ include file="/common/metas.jsp" %>
<script type="text/javascript">
	var account="";
	var empList="";
	
	$(function(){
		
		$.ajax({
			url:"<%=basePath %>baseinfo/employee/getEmplMap.action",
			type:'post',
			success: function(Data) {
				empList =Data;
			}
		});
		//签名人
		$("#operCode").combobox({
			url:"<%=basePath %>nursestation/nuilfch/getSysEmployeeInfo.action",
			valueField:'jobNo',    
		    textField:'name',
		    mode:'local',
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
		//回车时间
		bindEnterEvent('patientNo',query,'easyui');
		
		//加载数据表格
		 $("#nuilfchListData").datagrid({
				idField:'id',
				border:true,
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				fitColumns:false,
				pagination:true,
				columns:[[
						 {field:'lfchDate1',title:'日期', width:'5%',halign:"center",rowspan:2,formatter:funlfchDateD},
						 {field:'lfchDate2',title:'时间', width:'5%',halign:"center",rowspan:2,formatter:funlfchDateT},
						 {title:'生命体征', width:'40%',colspan:4},
					  	 {field:'mind',title:'神志', halign:"center",width:'10%',rowspan:2},
						 {field:'pupil',title:'瞳孔', halign:"center",width:'10%',rowspan:2},
						 {field:'lfchNote',title:'病情、护理措施及效果', halign:"center",width:'19%',rowspan:2},
						 {field:'operCode',title:'签名', width:'10%',halign:"center",rowspan:2,formatter:funOerCode},
						 ],[
						 {field:'lfchHeat',title:'体温 ℃', width:'10%',halign:"center",rowspan:1},
						 {field:'lfchPuls',title:'脉搏 次/分', width:'10%',halign:"center",rowspan:1},
						 {field:'lfchBrth',title:'呼吸 次/分', width:'10%',halign:"center",rowspan:1},
						 {field:'lfchBldh',title:'血压 mmhg', width:'10%',halign:"center",rowspan:1,formatter:funlfchBldh},
						 ]],
				//双击事件
				 onDblClickRow:function(rowIndex, rowData){
					   $('#lfchDate').textbox('setValue',rowData.lfchDate);
					   $('#lfchHeat').textbox('setValue',rowData.lfchHeat);
					   $('#lfchPuls').textbox('setValue',rowData.lfchPuls);
					   $('#lfchBldl').textbox('setValue',rowData.lfchBldl);
					   $('#lfchBrth').textbox('setValue',rowData.lfchBrth);
					   $('#lfchBldh').textbox('setValue',rowData.lfchBldh);
					   $('#lfchStyle').combobox('setValue',rowData.lfchStyle);
					   $('#mind').textbox('setValue',rowData.mind);
					   $('#pupil').textbox('setValue',rowData.pupil);
					   
					   $('#lfchHeatDown').textbox('setValue',rowData.lfchHeatDown);
					   $('#lfchFlag').textbox('setValue',rowData.lfchFlag);
					   $('#operCode').combobox('setValue',rowData.operCode);
					   $('#lfchNote').val(rowData.lfchNote);
					   $('#id').val(rowData.id);
					  //复选框回显 
					   if(rowData.dropHeat==1){
							$('#dropHeat').prop("checked", true); 
							$('#dropHeatHidden').val(1); 
						}else{
							$('#dropHeat').prop("checked", false); 
							$('#dropHeatHidden').val(0); 
						}
				 },onLoadSuccess : function(data){
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
					}
		})
		 $("#nuilfchListData").datagrid('loadData', { total: 0, rows: [] });
		//加载部门树
		 $("#logindeptcodetree").tree({
				url:'<%=basePath%>nursestation/nurse/getInpatientTree.action',
				lines:true,
				formatter : function(node) {//统计节点总数
					var s = node.text;
					if (node.children.length>0) {					 						
						s += '<span style=\'color:blue\'>('
								+ node.children.length + ')</span>';					
					}
					return s;
				},
				onClick:function(node){
						 var name=node.attributes.inpatientNo;
						    $('#deptCode').val(node.attributes.deptName);
						    $('#name').val(node.attributes.name);
						    $('#operCode').combobox('setValue',node.attributes.operCode);
						    $('#patientNo').textbox('setValue',node.attributes.medicalrecordId);
							 $('#bedNo').val(node.attributes.bedName);
						    $('#medicalrecordId').val(node.attributes.medicalrecordId);
						    $('#inpatientNo').val(node.attributes.inpatientNo);
						    $('#inDate').val(node.attributes.inDate);
						    $("#nuilfchListData").datagrid({
						    	url:'<%=basePath %>nursestation/nuilfch/getNuiLfchByPaiName.action?menuAlias=${menuAlias}',
								queryParams:{namet:name}
						    });
						    clear();
				},onLoadSuccess : function(node, data) {
					if(data.resCode=='error'){
						   $("body").setLoading({
								id:"body",
								isImg:false,
								text:data.resMsg
							});
					   }
				}
			});
	});
	
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
				$('#patientNo').textbox('setValue',data);
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
						$('#patientNo').textbox('setValue',data);
						query();
					}
				});
			};
	/*******************************结束读身份证***********************************************/
	//病历号框回车事件
	function query(){
		var medicalrecordId= $('#patientNo').textbox('getValue');
		if(medicalrecordId == ''){
			$.messager.alert('提示','请输入病历号！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		else{
				//ajax 请求后台数据
				$.ajax({
					url:'<%=basePath %>nursestation/nurse/getInpatientTreeByMid.action?menuAlias=${menuAlias}&medId='+medicalrecordId,
					type:'post',
					success: function(date) {
						if(date!=null&&date!=""){
							var user = $("#user").val();
							    var name=date[0].inpatientNo;
							    $('#deptCode').val(date[0].deptName);
							    $('#name').val(date[0].patientName);
							    $('#operCode').combobox('setValue',user);
							    $('#patientNo').textbox('setValue',date[0].medicalrecordId);
								 $('#bedNo').val(date[0].bedName);
							    $('#medicalrecordId').val(date[0].medicalrecordId);
							    $('#inpatientNo').val(date[0].inpatientNo);
							    $('#inDate').val(date[0].inDate);
							    $("#nuilfchListData").datagrid({
							    	url:'<%=basePath %>nursestation/nuilfch/getNuiLfchByPaiName.action?menuAlias=${menuAlias}',
									queryParams:{namet:name}
							    });
							    clear();
							
						}else{
							$.messager.alert("提示","本病区没有该患者");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}
						
					}
				});
		}
	
	}
	//血压
	function funlfchBldh(value,row,index){
		return value+"-"+row.lfchBldl;
	}
	//日期
	function funlfchDateD(value,row,index){
		if(row.lfchDate!=null&&row.lfchDate!=""){
			var v = row.lfchDate.substring(5,10);
			v = v.replace('-','.');
			return v;
		}
	}
	//时间
	function funlfchDateT(value,row,index){
		if(row.lfchDate!=null&&row.lfchDate!=""){
			var v = row.lfchDate.substring(11,16);
			return v;
		}
	}
	//签名人 empList
	function funOerCode(value){
		var emp = "";
		if(value!=null){
			return empList[value];
		}
		return emp;
	}
	//修改
	function edit(){
		 var rows = $('#nuilfchListData').datagrid('getSelections');
	 	  if (rows.length == 0) {
	 		  $.messager.alert("提示","请双击一行信息修改");
	 		 setTimeout(function(){
					$(".messager-body").window('close');
			 },3500);
	 	  }else{
	 		 $('#nuilfcheditForm').form('submit',{ 
	 	    	url:"<%=basePath %>nursestation/nuilfch/saveNuiLfch.action",  
	 	    	onSubmit:function(){
	 				if (!$('#nuilfcheditForm').form('validate')) {
	 					 $.messager.progress('close');
	 					$.messager.alert("提示","请输入必填信息！");
	 					setTimeout(function(){
	 						$(".messager-body").window('close');
	 					},3500);
	 					return false;
	 				}
	 				if ($('#lfchBldh').val()>200) {
						$.messager.progress('close');
						$.messager.alert("提示","请输入正确的低压！");
						setTimeout(function(){
	 	   					$(".messager-body").window('close');
	 	   				},3500);
						return false;
					}
					if ($('#lfchBldl').val()>200) {
						$.messager.progress('close');
						$.messager.alert("提示","请输入正确的高压！");
						setTimeout(function(){
	 	   					$(".messager-body").window('close');
	 	   				},3500);
						return false;
					}
					if ($('#lfchBrth').val()>200) {
						$.messager.progress('close');
						$.messager.alert("提示","请输入正确的呼吸频次！");
						setTimeout(function(){
	 	   					$(".messager-body").window('close');
	 	   				},3500);
						return false;
					}
					if ($('#lfchHeat').val()>50) {
						$.messager.progress('close');
						$.messager.alert("提示","请输入正确的体温！");
						setTimeout(function(){
	 	   					$(".messager-body").window('close');
	 	   				},3500);
						return false;
					}
					if ($('#lfchHeatDown').val()>50) {
						$.messager.progress('close');
						$.messager.alert("提示","请输入正确的目标体温！");
						setTimeout(function(){
	 	   					$(".messager-body").window('close');
	 	   				},3500);
						return false;
					}
					if ($('#lfchPuls').val()>200) {
						$.messager.progress('close');
						$.messager.alert("提示","请输入正确的脉搏频次！");
						setTimeout(function(){
	 	   					$(".messager-body").window('close');
	 	   				},3500);
						return false;
					}
	 				 $.messager.progress({text:'修改中，请稍后...',modal:true});
	 	    	},  
	 	        success:function(data){
	 	      	    $.messager.progress('close');
	 	        	if(data=="success"){
	 	        		var namet= $('#inpatientNo').val();
	 	        		$.messager.alert('提示','修改成功');
	 	        		setTimeout(function(){
	 	   					$(".messager-body").window('close');
	 	   				},3500);
	 	        		$("#nuilfchListData").datagrid({
					    	url:'<%=basePath %>nursestation/nuilfch/getNuiLfchByPaiName.action?menuAlias=${menuAlias}',
							queryParams:{namet:namet}
					    });
	 	        		add();
	 	        	}else{
	 	        		$.messager.alert('提示','修改失败');
	 	        		setTimeout(function(){
	 	   					$(".messager-body").window('close');
	 	   				},3500);
	 	        	}
	 	        },
	 			error : function(data) {
	 				$.messager.progress('close');
	 				$.messager.alert('提示','修改失败！');
	 				setTimeout(function(){
 	   					$(".messager-body").window('close');
 	   				},3500);
	 			}							         
	 		}); 
	 	}
	}
	//添加 将数据框清空 不清空签名人
	function clear(){
		$('#id').val("");
	    $('#lfchDate').textbox('clear');
	    $('#lfchHeat').textbox('clear');
	    $('#lfchPuls').textbox('clear');
	    $('#lfchBldl').textbox('clear');
	    $('#lfchBrth').textbox('clear');
		$('#lfchBldh').textbox('clear');
		$('#mind').textbox('clear');
		$('#pupil').textbox('clear');
		$('#dropHeat').prop("checked", false); 
		$('#lfchStyle').combobox('clear');
		$('#lfchFlag').textbox('clear');
		$('#lfchNote').val("");
		$('#lfchHeatDown').textbox('clear');
	}
	//添加 将数据框清空 
	function add(){
		$('#id').val("");
	    $('#lfchDate').textbox('clear');
	    $('#lfchHeat').textbox('clear');
	    $('#lfchPuls').textbox('clear');
	    $('#lfchBldl').textbox('clear');
	    $('#lfchBrth').textbox('clear');
		$('#lfchBldh').textbox('clear');
		$('#mind').textbox('clear');
		$('#pupil').textbox('clear');
		$('#dropHeat').prop("checked", false); 
		$('#lfchStyle').combobox('clear');
		$('#lfchFlag').textbox('clear');
		$('#lfchNote').val("");
		$('#lfchHeatDown').textbox('clear');
	}
	
	//表单提交
	function save(){
		if($('#dropHeat').is(':checked')){
			$('#dropHeatHidden').val(1);
		}else{
			$('#dropHeatHidden').val(0);
		}
		$('#nuilfcheditForm').form('submit',{ 
	    	url:"<%=basePath %>nursestation/nuilfch/saveNuiLfch.action",  
	    	onSubmit:function(){
				if (!$('#nuilfcheditForm').form('validate')) {
					$.messager.progress('close');
					$.messager.alert("提示","请输入必填信息！");
					setTimeout(function(){
 	   					$(".messager-body").window('close');
 	   				},3500);
					return false;
				}
				var p= $("#logindeptcodetree").tree("getSelected");
				if (p==null||p=="") {
					$.messager.progress('close');
					$.messager.alert("提示","请选择患者！");
					setTimeout(function(){
 	   					$(".messager-body").window('close');
 	   				},3500);
					return false;
				}
				if ($('#lfchBldh').val()>200) {
					$.messager.progress('close');
					$.messager.alert("提示","请输入正确的低压！");
					setTimeout(function(){
 	   					$(".messager-body").window('close');
 	   				},3500);
					return false;
				}
				if ($('#lfchBldl').val()>200) {
					$.messager.progress('close');
					$.messager.alert("提示","请输入正确的高压！");
					setTimeout(function(){
 	   					$(".messager-body").window('close');
 	   				},3500);
					return false;
				}
				if ($('#lfchBrth').val()>200) {
					$.messager.progress('close');
					$.messager.alert("提示","请输入正确的呼吸频次！");
					setTimeout(function(){
 	   					$(".messager-body").window('close');
 	   				},3500);
					return false;
				}
				if ($('#lfchHeat').val()>50) {
					$.messager.progress('close');
					$.messager.alert("提示","请输入正确的体温！");
					setTimeout(function(){
 	   					$(".messager-body").window('close');
 	   				},3500);
					return false;
				}
				if ($('#lfchHeatDown').val()>50) {
					$.messager.progress('close');
					$.messager.alert("提示","请输入正确的目标体温！");
					setTimeout(function(){
 	   					$(".messager-body").window('close');
 	   				},3500);
					return false;
				}
				if ($('#lfchPuls').val()>200) {
					$.messager.progress('close');
					$.messager.alert("提示","请输入正确的脉搏频次！");
					setTimeout(function(){
 	   					$(".messager-body").window('close');
 	   				},3500);
					return false;
				}
				$.messager.progress({text:'保存中，请稍后...',modal:true});
	    	},  
	        success:function(data){
	        	$.messager.progress('close');
	        	if(data=="success"){
	        		var namet= $('#inpatientNo').val();
	        		$.messager.alert('提示','保存成功');
	        		setTimeout(function(){
 	   					$(".messager-body").window('close');
 	   				},3500);
	        		$("#nuilfchListData").datagrid({
				    	url:'<%=basePath %>nursestation/nuilfch/getNuiLfchByPaiName.action?menuAlias=${menuAlias}',
						queryParams:{namet:namet}
				    });
	        		add();
	        	}else{
	        		$.messager.alert('提示','保存失败');
	        		setTimeout(function(){
 	   					$(".messager-body").window('close');
 	   				},3500);
	        	}
	        },
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示','保存失败！');	
				setTimeout(function(){
	   					$(".messager-body").window('close');
	   			},3500);
			}							         
			}); 
		 
		}
		/**  
		 *  
		 * @Description：过滤	
		 * @Author：zhuxiaolu
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
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
	<body style="margin: 0px;padding: 0px;">
		<div id="cc" class="easyui-layout" data-options="fit:true" > 
			<form id="nuilfcheditForm" method="post" > 
				<div data-options="region:'west',split:true" style="width:16%;padding:5px;border-width:0 1px 0 0;">
					<div>
						<input type="hidden"  id="inpatientNo" name="inpatientNo" />
						<input type="hidden"  id="user" name="user" value="${user}" />
					</div>
					<div>
		        		<ul id="logindeptcodetree" ></ul>	
		        	</div>
		        </div>
		        
		        <div data-options="region:'center',border:false" style="width:80%;height:100%;"> 
		        	<div id="divLayout" class="easyui-layout" data-options="fit:true">
						<div data-options="region:'north'" style="padding:14px 5px 2px 5px;height:50%;border-top:0">
								<table style="width:100%;height: 20px;">
									<tr>
										<td style="white-space: nowrap;">
											病历号：<input class="easyui-textbox" id="patientNo" name="patientNo" data-options="prompt:'输入病历号回车查询'" style="width:130px"   />
											<shiro:hasPermission name="${menuAlias}:function:query">
											&nbsp;&nbsp;<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
											</shiro:hasPermission>
											<shiro:hasPermission name="${menuAlias}:function:readCard">
			       								<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
			       							</shiro:hasPermission>
								        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
								        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
			       							</shiro:hasPermission>
											&nbsp;姓名:&nbsp;
											<input id="name" name="name"  readonly type="text" style="border: 0; width: 110px" ></input>
										 	&nbsp;科室:&nbsp;
											<input  id="deptCode" name="deptCode"  readonly type="text" style="border: 0; width: 110px"/>
											 &nbsp;床号:&nbsp;
											 <input  id="bedNo" name="bedNo"  readonly type="text" style="border: 0; width: 110px"/>
											 &nbsp;病历号:&nbsp;
											 <input  id="medicalrecordId" name="medicalrecordId"  readonly type="text" style="border: 0;width: 110px" />
										</td>
										
									</tr>
								</table>
								<br>
								<input   type="hidden" id="inDate" name="inDate" />
							<input  type="hidden" id="id"  name="id" />
								<table  class="honry-table" id="nuilfcheditTable" cellpadding="1" style="width:100%;height: 87%;" cellspacing="1" border="1px solid black">
									<tr>
										<td class="honry-lable">体温:</td>
						    			<td  ><input class="easyui-numberbox" id="lfchHeat" name="lfchHeat" validtype="lfchHeat" missingMessage="请输入体温  ℃ " data-options="required:true,min:30,max:50,precision:1" style="width:195px"/> ℃</td>
						    			<td class="honry-lable">目标体温:</td>
						    			<td  ><input class="easyui-numberbox" id="lfchHeatDown" name="lfchHeatDown" data-options="required:true,precision:1" missingMessage="请输入目标体温  ℃ " style="width:195px"/> ℃ </td>
										<td class="honry-lable">脉搏:</td>
						    			<td  ><input class="easyui-numberbox" id="lfchPuls" name="lfchPuls"  data-options="required:true" missingMessage="请输入脉搏  次/分" style="width:195px" /> 次/分</td>
				    				</tr>
									<tr>
										<td class="honry-lable">呼吸:</td>
						    			<td  ><input class="easyui-numberbox"  id="lfchBrth"  name="lfchBrth" data-options="required:true" missingMessage="请输入呼吸 次/分" style="width:195px"/> 次/分 </td>
										<td class="honry-lable" >血压:</td>
						    			<td  colspan="3"><input class="easyui-numberbox" id="lfchBldh" name="lfchBldh" data-options="required:true"  missingMessage="请输入血压  mmhg" style="width:195px"/>  至  <input class="easyui-numberbox" id="lfchBldl" name="lfchBldl" data-options="required:true" missingMessage="请输入血压  mmhg" style="width:195px"/> mmhg </td>
				    				</tr>
									<tr>
										<td class="honry-lable">神志:</td>
						    			<td ><input class="easyui-textbox"  id="mind"  name="mind" missingMessage="请输入神志" data-options="required:true" style="width:195px"/> </td>
										<td class="honry-lable">瞳孔:</td>
						    			<td ><input class="easyui-textbox" id="pupil" name="pupil" missingMessage="请输入瞳孔" data-options="required:true" style="width:195px"/> </td>
						    			<td class="honry-lable">强行降温:</td>
						    			<td >
						    				<input type="checkbox" id="dropHeat"/>
						    				<input type="hidden" id="dropHeatHidden"  name="dropHeat"/>
						    			</td>
				    				</tr>
									<tr>
										<td class="honry-lable">体温类型:</td>
						    			<td  colspan="5"><input class="easyui-combobox"  id="lfchStyle"  name="lfchStyle" data-options="valueField: 'id', textField: 'value',data: [{ id: '1', value: '耳温'},{ id: '2', value: '体温'}]" style="width:15%"/> </td>
				    				</tr>
									<tr>
										<td class="honry-lable">病情、护理 <br>措施及效果:</td>
						    			<td  colspan="5"><textarea id="lfchNote" name="lfchNote" data-options="required:true" style="width:80%;height:50px;font-size: 14px;"></textarea> </td>
				    				</tr>
									<tr>
										<td class="honry-lable">签名:</td>
						    			<td  colspan="5"><input class="easyui-combobox" id="operCode" name="operCode" data-options="required:true" style="width:15%"/></td>
				    				</tr>
								</table>
						</div>
						<div data-options="region:'center',border:false" style="height:45%;width:100%;">
							<div id="toolbarId">
							<shiro:hasPermission name="${menuAlias}:function:add">
								<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">新增</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:edit">
								<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
							</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:save">	
								<a href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">保存</a>
						    </shiro:hasPermission>
						    </div>
						    <div style="width: 100%; height: 100%">
						        <table id="nuilfchListData" class="easyui-datagrid" data-options="toolbar:'#toolbarId',fit:true" style="width:99%;padding:5px 5px 5px 5px;">
								</table>
							</div>
						</div>
					</div>	
				</div>	
			</form>  
		</div>
	</body>
</html>