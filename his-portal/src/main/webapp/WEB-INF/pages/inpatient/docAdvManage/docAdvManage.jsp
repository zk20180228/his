<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>医嘱管理医嘱</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body class="easyui-layout" data-options="fit:true">   
    <div id='Loading' style="position:absolute;z-index:1000;top:0px;left:0px;width:100%;height:100%;background:#DDDDDB;text-align:center;padding-top: 20%;"><h1><font color="#15428B">加载中，请稍后······</font></h1></div>
	<div data-options="region:'north',split:true" style="padding:5px 10px 5px 5px;height:53px;">
		<input type="hidden" id="pharmacyInputId" value="${pharmacyInputId }">
			<input type="hidden" id="pharmacyInputName" value="${pharmacyInputName }">
				<div style="padding:5px;float:left;width:140px;height:25px;margin-left:10px;">
					<input id="pharmacyCombobox" class="easyui-combobox" name="dept" style="width:120px;height:25px;">					
				</div>
			<shiro:hasPermission name="${menuAlias}:function:open">
				&nbsp;<a id="adStartAdviceBtn" href="javascript:adStartAdvice();" class="easyui-linkbutton" data-options="iconCls:'icon-application_edit',disabled:false" style="width: 103px;margin-top:5px">开立医嘱</a>
			</shiro:hasPermission>
				<a id="adEndAdviceBtn" href="javascript:adEndAdvice();" class="easyui-linkbutton" data-options="iconCls:'icon-application_edit',disabled:true" style="width: 103px;margin-top:5px">退出开立</a>
				<a id="adSaveAdviceBtn" href="javascript:adSaveAdvice();" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_add',disabled:true" style="width: 103px;margin-top:5px">保存医嘱</a>
				<a id="adDelAdviceBtn" href="javascript:adDelAdvice();" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_delete',disabled:true" style="width: 103px;margin-top:5px">删除医嘱</a>
				<a id="adStopAdviceBtn" href="javascript:adStopAdvice();" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_delete',disabled:true" style="width: 103px;margin-top:5px">停止医嘱</a>
				<a id="adHerbMedicineBtn" href="javascript:adHerbMedicine();" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_magnify',disabled:true" style="width:103px;margin-top:5px;">草药医嘱</a>
				<a id="adAuditAdviceBtn" href="javascript:adAuditAdvice();" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_edit',disabled:true" style="width: 90px;margin-top:5px">审核</a>
				<a id="adAddGroupBtn" href="javascript:adAddGroup();" class="easyui-linkbutton" data-options="iconCls:'icon-application_link',disabled:true" style="width: 90px;margin-top:5px">组合</a>
				<a id="adCancelGroupBtn" href="javascript:adCancelGroup();" class="easyui-linkbutton" data-options="iconCls:'icon-application_double',disabled:true" style="width: 103px;margin-top:5px">取消组合</a>
				<a id="adSaveGroupBtn" href="javascript:adSaveGroup();" class="easyui-linkbutton" data-options="iconCls:'icon-application_form_edit',disabled:true" style="width: 103px;margin-top:5px">保存组套</a>
				<a id="adPrintAdviceBtn" href="javascript:adPrintAdvice();" class="easyui-linkbutton" data-options="iconCls:'icon-2012081511202',disabled:true" style="width: 103px;margin-top:5px">打印医嘱</a>&nbsp;
				<input id="docAdvTypeFilt" class="easyui-combobox" style="width:90px;" data-options="    
											       valueField: 'id',    
											       textField: 'text',    									      
											       data: [{
														id: '01',
														text: '全部医嘱'
													},{
														id: '02',
														text: '有效医嘱'
													},{
														id: '03',
														text: '作废医嘱'
													},{
														id: '04',
														text: '当天医嘱'
													},{
														id: '05',
														text: '未审医嘱'
													}]     
											       " /> 			
	</div>	 
    <div id="p" data-options="region:'west',tools:'#toolSMId',split:true" title="患者管理" style="width: 10%; padding: 10px">
    	<div id="toolSMId">
					<a href="javascript:void(0)" class="icon-reload" onclick="refresh()" ></a>
					<a href="javascript:void(0)" class="icon-fold" onclick="collapseAll()"></a>
					<a href="javascript:void(0)" class="icon-open" onclick="expandAll()" ></a>
		</div> 
		<div id="treeDiv" style="width: 100%; height: 100%; overflow-y: auto;">
    		<ul id="tDt"></ul>
    	</div>
	</div>
	<div id="mm" class="easyui-menu" data-options="" style="width: 120px;">						
		<div id="patientinfoTree" onclick="patientinfoTree()" data-options="iconCls:'icon-edit'">
				查看患者信息
		</div>
	</div>	   
    <div data-options="region:'center'">
    	<div id="tt1" class="easyui-tabs" data-options="fit:true">   
			<div title="医嘱" id="" style="padding: 5px 5px 5px 5px;" data-options="fit:true">   
				<jsp:include page="docAdvDetail.jsp"></jsp:include>				
		    </div>
		    <div title="历史医嘱" style="padding:10px">
				<jsp:include page="hisDocAdvDetail.jsp"></jsp:include>
			</div>
			<div title="药品" style="padding:10px">
				<iframe src="<%=basePath%>inpatient/docAdvManage/queryDrugInfo.action"
				 style="width:100%;height:100%;" frameborder="0" scrolling="auto"></iframe>
			</div> 
    	</div>
    </div> 
    <div id="adviceListMenu" class="easyui-menu" style="width:100px; display: none;">
		<div data-options="iconCls:'icon-application_double'" onclick="adviceCopy()">复制医嘱</div>
		<div data-options="iconCls:'icon-application_xp'" onclick="advicePaste()">粘贴医嘱</div>		   
	</div>
    <div id="addData-window" style="position:relative;"></div> 
    <div id="adWestMediModlDivId"></div> 
    <div id="adExeDeptModlDivId"></div>
    <div id="adAuditData-window"></div> 
    <div id="adStopData-window"></div>
    <div id="adSpeFreData-window"></div>
    <div id="stackModleDivId"></div>
    <div id="stackSaveModleDivId" style="display: none;"><jsp:include page="stackInfoSave.jsp"></jsp:include></div>
    <div id="chinMediModleDivId"></div>   
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>    
<script type="text/javascript">
        function closes(){
        	
        	    $("#Loading").fadeOut("normal",function(){
        	
        	        $(this).remove();
        	
        	    });
        	
        	}
        	
        	var pc;
        	
        	$.parser.onComplete = function(){
        	
        	    if(pc) clearTimeout(pc);
        	
        	    pc = setTimeout(closes, 1000);
        	
        	}
        
		var decmpsState="";//长期医嘱、临时医嘱标志
		var inpatientNo="";//住院流水号
		var recordId="";
		var adDrugUnitUsemMap = new Map();//使用方法Map
		var adDrugUnitUsemList = null;//使用方法List
		var colorInfoCheckpointMap = new Map();//检查部位Map
		var colorInfoCheckpointList = null;//检查部位List
		var colorInfoSampleTeptMap = new Map();//样本类型Map
		var colorInfoSampleTeptList = null;//样本类型List	
		var drugpackagingunitMaps = "";//包装单位Map
		var nonmedicineencodingMaps="";//非药品单位	Map
		var drugdoseunitMaps = "";//计量单位Map		
		var frequencyMap = "";//频次map
		var implDepartmentMap = "";//执行科室Map
		var usemodeMap = "";//用法Map
		var colorInfoUnitsList = null;//单位List
		var sexMap=new Map();
		function loadDatagridA(decmpsState,inpatientNo,recordId){			
			$("#infolistA").datagrid({ 
				url:'<%=basePath%>inpatient/docAdvManage/queryInpatientOrder.action?menuAlias=${menuAlias}&inpatientOrder.decmpsState='+decmpsState+'&inpatientOrder.inpatientNo='+inpatientNo+'&recordId='+recordId,
				fit:true,
				singleSelect:true,
				method:'post',
				rownumbers:false,		
				striped:true,
				border:true,
				selectOnCheck:false,
				checkOnSelect:true,
				fitColumns:false, 
				onLoadSuccess:function(data){
					var row = $('#infolistA').datagrid('getRows');
					var rows = $('#infolistA').datagrid('getRows');
					if(row.length>0){
						for(var q=0;q<row.length;q++){
							var a=0;
							for(var i=0;i<rows.length;i++){
								if(row[q].combNo==rows[i].combNo && row[q].combNo!=null && row[q].combNo!=''){
									a++;
								}
							}
							if(a==1){
								$('#infolistA').datagrid('updateRow',{
									index: q,
									row: {
										combNoFlag:''
									}
								});	
							}else if(a==2){
								for(var j=0;j<rows.length;j++){
									if(row[q].combNo==rows[j].combNo){
										if(row[q].sortId==rows[j].sortId){
											if(j==0){
												$('#infolistA').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┓'
													}
												});									
											}else if(j!=0 && (row[q].combNo!=rows[j-1].combNo)){
												$('#infolistA').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┓'
													}
												});
											}else if(j==rows.length-1){
												$('#infolistA').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┛'
													}
												});												
											}else if((j!=rows.length-1) && (row[q].combNo!=rows[j+1].combNo)){
												$('#infolistA').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┛'
													}
												});	
											}									
										}								
									}
								}						
							}else if(a>2){
								for(var j=0;j<rows.length;j++){
									if(row[q].combNo==rows[j].combNo){
										if(row[q].sortId==rows[j].sortId){
											if(j==0){
												$('#infolistA').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┓'
													}
												});
											}else if(j!=0 && row[q].combNo!=rows[j-1].combNo){
												$('#infolistA').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┓'
													}
												});
											}else if(j==rows.length-1){
												$('#infolistA').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┛'
													}
												});	
											}else if(j!=rows.length-1 && row[q].combNo!=rows[j+1].combNo){
												$('#infolistA').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┛'
													}
												});	
											}else{
												$('#infolistA').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┃'
													}
												});	
											}																		
										}								
									}
								}
							}
						}	
					}					
				},				
			});
		}
		function loadDatagridB(decmpsState,inpatientNo,recordId){
			$("#infolistB").datagrid({
				url:'<%=basePath%>inpatient/docAdvManage/queryInpatientOrder.action?menuAlias=${menuAlias}&inpatientOrder.decmpsState='+decmpsState+'&inpatientOrder.inpatientNo='+inpatientNo+'&recordId='+recordId,
				fit:true,
				singleSelect:true,
				method:'post',
				rownumbers:false,
				striped:true,
				border:true,
				selectOnCheck:false,
				checkOnSelect:true,
				fitColumns:false, 
				onLoadSuccess:function(data){
					var row = $('#infolistB').datagrid('getRows');
					var rows = $('#infolistB').datagrid('getRows');
					if(row.length>0){
						for(var q=0;q<row.length;q++){
							var a=0;
							for(var i=0;i<rows.length;i++){
								if(row[q].combNo==rows[i].combNo && row[q].combNo!=null && row[q].combNo!=''){
									a++;
								}
							}
							if(a==1){
								$('#infolistB').datagrid('updateRow',{
									index: q,
									row: {
										combNoFlag:''
									}
								});	 
							}else if(a==2){
								for(var j=0;j<rows.length;j++){
									if(row[q].combNo==rows[j].combNo){
										if(row[q].sortId==rows[j].sortId){
											if(j==0){
												$('#infolistB').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┓'
													}
												});									
											}else if(j!=0 && (row[q].combNo!=rows[j-1].combNo)){
												$('#infolistB').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┓'
													}
												});
											}else if(j==rows.length-1){
												$('#infolistB').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┛'
													}
												});												
											}else if((j!=rows.length-1) && (row[q].combNo!=rows[j+1].combNo)){
												$('#infolistB').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┛'
													}
												});	
											}									
										}								
									}
								}						
							}else if(a>2){
								for(var j=0;j<rows.length;j++){
									if(row[q].combNo==rows[j].combNo){
										if(row[q].sortId==rows[j].sortId){
											if(j==0){
												$('#infolistB').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┓'
													}
												});
											}else if(j!=0 && row[q].combNo!=rows[j-1].combNo){
												$('#infolistB').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┓'
													}
												});
											}else if(j==rows.length-1){
												$('#infolistB').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┛'
													}
												});	
											}else if(j!=rows.length-1 && row[q].combNo!=rows[j+1].combNo){
												$('#infolistB').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┛'
													}
												});	
											}else{
												$('#infolistB').datagrid('updateRow',{
													index: q,
													row: {
														combNoFlag:'┃'
													}
												});	
											}																		
										}								
									}
								}
							}
						}	
					}					
				}		 				
			});
		}
		//加载标签页
		function loadTabs(inpatientNo,recordId){			
			if($("#inpatientId").val()!=""){
				var qq = $('#ttta').tabs('getSelected');				
				var tab = qq.panel('options');
				if(tab.title=='长期医嘱'){
					loadDatagridA(1,inpatientNo,recordId);
				}else{
					loadDatagridB(0,inpatientNo,recordId);
				}
			}
			$('#ttta').tabs({
			    border:false,
			    onSelect:function(title,index){				    				    	
			    	var qq = $('#ttta').tabs('getSelected');				
					var tab = qq.panel('options');
					if(tab.title=='长期医嘱'){	
						var rows = $('#infolistB').datagrid('getRows');
						var array = new Array();
						for(var i=0;i<rows.length;i++){
							if(rows[i].changeNo==1){
								array.push(rows[i]);
							}
						}
						if(array.length>0){
							$.extend($.messager.defaults,{  
						        ok:"是",  
						        cancel:"否"  
						    });  
							jQuery.messager.confirm('提示','有尚未保存的医嘱，是否进行保存?',function(e){   
								if(e){ 
									adSaveAdvice(array);
								}
							});							
						} 
						$("#long").show();
						$("#tem").hide();
						$("#endTimeTitle").show();
						$("#endTimeText").show();
						if($("#comboboxId").val()==1){
							loadDatagridA(1,inpatientNo,'02');
						}else{
							loadDatagridA(1,inpatientNo,recordId);
						}						
						loadDatagridC(1,inpatientNo);
					}else{
						var rowsA = $('#infolistA').datagrid('getRows');
						var arrayA = new Array();
						for(var i=0;i<rowsA.length;i++){
							if(rowsA[i].changeNo==1){								
								arrayA.push(rowsA[i]);							
							}
						}
						if(arrayA.length>0){
							$.extend($.messager.defaults,{  
						        ok:"是",  
						        cancel:"否"  
						    });  
							jQuery.messager.confirm('提示','有尚未保存的医嘱，是否进行保存?',function(e){   
								if(e){ 
									adSaveAdvice(arrayA);
								}
							});							
						} 
						$("#long").hide();
						$("#tem").show();
						$("#endTimeTitle").hide();
						$("#endTimeText").hide();
						if($("#comboboxId").val()==1){
							loadDatagridB(0,inpatientNo,'06');
						}else{
							loadDatagridB(0,inpatientNo,recordId);
						}
						loadDatagridD(0,inpatientNo);
					}
					$('#longDocAdvType').combobox('setValue','');
			    	$('#temDocAdvType').combobox('setValue','');
			    	$('#adProjectTdId').combobox('setValue','');	
					clearProjectTextbox();//清空项目类别区域的textbox
			    	if($("#docAdvTypeFilt").combobox('getValue')!=null){
			    		recordId = $("#docAdvTypeFilt").combobox('getValue');
			    	}
			    }
			});
		}	
		function loadAjax(){
			//性别渲染
			$.ajax({
				url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",	
				data:{type:'sex'},
				type:'post',
				success: function(data) {
					var v = data;
					for(var i=0;i<v.length;i++){
						sexMap.put(v[i].encode,v[i].name);
					}
				}
			});
			//频次
			$.ajax({
				url: "<%=basePath%>inpatient/docAdvManage/queryFrequency.action",				
				type:'post',
				success: function(frequencydata) {					
					frequencyMap = frequencydata;		
				}
			});	
			//执行科室
			$.ajax({
				url: "<%=basePath%>inpatient/docAdvManage/queryImplDepartment.action",				
				type:'post',
				success: function(implDepartmentdata) {					
					implDepartmentMap = implDepartmentdata;					
				}
			});
			//用法
			$.ajax({
				url: "<%=basePath%>inpatient/docAdvManage/queryDrugUsemode.action",				
				type:'post',
				success: function(usemodedata) {					
					usemodeMap = usemodedata;					
				}
			});
			//获得检查部位
			$.ajax({
				url: "<%=basePath%>inpatient/docAdvManage/queryCheckpointMap.action",	
				type:'post',
				success: function(data) {
					colorInfoCheckpointMap = data;				
				}
			});
			//获得样本类型
			$.ajax({
				url: "<%=basePath%>inpatient/docAdvManage/querySampleTeptMap.action",
				type:'post',
				success: function(data) {					
					colorInfoSampleTeptMap = data;					
				}
			}); 
			//查询包装单位
			$.ajax({
				url: "<%=basePath%>inpatient/docAdvManage/queryDrugpackagingunit.action",				
				type:'post',
				success: function(drugpackagingunitdata) {					
					drugpackagingunitMaps = drugpackagingunitdata;										
				}
			});
			//查询非药品单位
			$.ajax({
				url: "<%=basePath%>inpatient/docAdvManage/queryNonmedicineencoding.action",				
				type:'post',
				success: function(nonmedicineencodingdata) {					
					nonmedicineencodingMaps = nonmedicineencodingdata;										
				}
			});
			//查询计量单位
			$.ajax({
				url: "<%=basePath%>inpatient/docAdvManage/queryDrugpackagingunit.action",				
				type:'post',
				success: function(drugdoseunitdata) {					
					drugdoseunitMaps = drugdoseunitdata;										
				}
			});
		}
		$(function(){
			bindEnterEvent('adStackTreeSearch',searchStackTreeNodes,'easyui');
			$('#adProjectNameTdId').combogrid('disable');
			$('#adStopAdviceBtn').hide();
			$('#cc').layout('collapse','west'); 
			$('#pharmacyCombobox').combobox({    
			    url:'<%=basePath%>publics/putGetDrug/PGDjsonForPutDeptID.action',
			    valueField:'id',    
			    textField:'deptName',
			    multiple:false,
			    editable:false,
			    onLoadSuccess: function (data) {
			    	if($('#pharmacyInputId').val()!=null&&$('#pharmacyInputId').val()!=''){
			    		$('#pharmacyCombobox').combobox('select', $('#pharmacyInputId').val());
			    	}else if (data.length > 0) {
	                    $('#pharmacyCombobox').combobox('select', data[0].id);
	                }
		        },
		        onSelect:function(record){
		        	$.post("<%=basePath%>inpatient/docAdvManage/savePharmacyInfo.action",
					        {"pharmacyId":record.id},
					        function(data){
					        	var dataMap = data;
					   			if(dataMap.resMsg=="error"){
					   				$.messager.alert('提示',dataMap.resCode);
					        	}else if(dataMap.resMsg=="success"){
					        		$('#pharmacyInputId').val(record.id);
					        		$('#pharmacyInputName').val(record.deptName);
					        	}else{
					        		$.messager.alert('提示',"未知错误,请联系管理员！");					     
					        	}
					   		}
					);
		        }
			});
			$('#docAdvTypeFilt').combobox({  
				onSelect:function(record){
					recordId=record.id;					
					var qq = $('#ttta').tabs('getSelected');				
					var tab = qq.panel('options');
					if(tab.title=='长期医嘱'){
						loadDatagridA(1,inpatientNo,recordId);
					}else if(tab.title=='临时医嘱'){
						loadDatagridB(0,inpatientNo,recordId);
					}					
				}
			}); 
			bindEnterEvent('adProjectNameTdId',searchInfo,'easyui');//为项目名称添加事件						
			bindEnterEvent('adExeDeptTdId',searchDeptInfo,'easyui');//为执行科室添加事件
			$('#infolistA').datagrid({//点击选择行，相应数据反显到列表上方编辑区域
				height:$("body").height()-365,
				onRowContextMenu: function(e, rowIndex, rowData) { //右键时触发事件
	                e.preventDefault(); //阻止浏览器捕获右键事件
	                //$(this).datagrid("clearSelections"); //取消所有选中项
	                $(this).datagrid("selectRow", rowIndex); //根据索引选中该行	                
	                $('#adviceListMenu').menu('show', {//显示右键菜单
	                    left: e.pageX,//在鼠标点击处显示菜单
	                    top: e.pageY
	                });
	            },
				onSelect:function(rowIndex,rowData){	
					if(rowData.moStat==1){
						$('#adDelAdviceBtn').hide();
						$('#adStopAdviceBtn').show();
						return;
					}else if(rowData.moStat==2){
						$('#adDelAdviceBtn').hide();
						$('#adStopAdviceBtn').show();
						return;
					}else if(rowData.moStat==3){
						$('#adDelAdviceBtn').show();
						$('#adStopAdviceBtn').hide();
						return;
					}else{
						$('#adDelAdviceBtn').show();
						$('#adStopAdviceBtn').hide();
						if(rowData.itemType=='1'){
							initCombobox(false,colorInfoUnitsMap,rowData.drugpackagingUnit,rowData.minUnit);
						}else{
							$('#adProjectUnitTdId').combobox('select',rowData.drugpackagingUnit);
						}		
						if(rowData.typeCode!=null&&rowData.typeCode!=''){
							$('#longDocAdvType').combobox('select',rowData.typeCode);	
						}else{
							$('#longDocAdvType').combobox('clear');
						}
						if(rowData.classCode!=null&&rowData.classCode!=''){
							$('#adProjectTdId').combobox('select',"09");
						}else{
							$('#adProjectTdId').combobox('clear');
						}
						if(rowData.frequencyCode!=null&&rowData.frequencyCode!=''){//中、西药品频次
							$('#adWestMediFreTdId').combobox('select',rowData.frequencyCode);	
						}else{
							$('#adWestMediFreTdId').combobox('clear');
						}
						if(rowData.usageCode!=null&&rowData.usageCode!=''){//中、西药品用法
							$('#adWestMediUsaTdId').combobox('select',rowData.usageCode);	
						}else{
							$('#adWestMediUsaTdId').combobox('clear');
						}
						$('#adProjectNumTdId').numberspinner('setValue',rowData.qtyTot);
						$('#adProjectNameTdId').textbox('setValue',rowData.itemName);
						if(rowData.priceUnit!=null&&rowData.priceUnit!=''){
							$('#adProjectUnitTdId').combobox('setValue',rowData.priceUnit);
						}else{
							$('#adProjectUnitTdId').combobox('clear');
						}
						$('#adWestMediDosMaxTdId').numberbox('setValue',(rowData.doseOnce/parseFloat(rowData.baseDose)).toFixed(2));
						$('#adWestMediMinUnitTdId').text(drugpackagingunitMaps[rowData.minUnit]);
						$('#adWestMediDosMinTdId').numberbox('setValue',rowData.doseOnce);
						$('#adWestMediDosDosaTdId').text(drugdoseunitMaps[rowData.doseUnit]);
						$('#adExeDeptTdId').textbox('setValue',rowData.execDpnm);//执行科室
						if(rowData.moNote2!=null&&rowData.moNote2!=''){
							$('#adWestMediRemTdId').combobox('setText',rowData.moNote2);//西药中成药备注
						}else{
							$('#adWestMediRemTdId').combobox('clear');
						}					
						$('#adChinMediNumTdId').numberbox('setValue',rowData.useDays);//草药付数					
						if(rowData.frequencyCode!=null&&rowData.frequencyCode!=''){//中、西药品频次
							$('#adChinMediFreTdId').combobox('select',rowData.frequencyCode);	
						}else{
							$('#adChinMediFreTdId').combobox('clear');
						}
						if(rowData.usageCode!=null&&rowData.usageCode!=''){
							$('#adChinMediUsaTdId').combobox('select',rowData.usageCode);//中草药用法	
						}else{
							$('#adChinMediUsaTdId').combobox('clear');
						}
						if(rowData.moNote2!=null&&rowData.moNote2!=''){
							$('#adChinMediRemTdId').combobox('setText',rowData.moNote2);//中草药备注 
						}else{
							$('#adChinMediRemTdId').combobox('clear');
						}	
						if(rowData.itemNote!=null&&rowData.itemNote!=''){
							$('#adNotDrugInsTdId').combobox('select',rowData.itemNote);//部位
						}else{
							$('#adNotDrugInsTdId').combobox('clear');
						}
						if(rowData.labCode!=null&&rowData.labCode!=''){
							$('#adNotDrugSamTdId').combobox('select',rowData.labCode);//样本类型
						}else{
							$('#adNotDrugSamTdId').combobox('clear');
						}	
						if(rowData.moNote2!=null&&rowData.moNote2!=''){
							$('#adNotDrugRemTdId').combobox('setText',rowData.moNote2);//非药品备注
						}else{
							$('#adNotDrugRemTdId').combobox('clear');
						}
						if(rowData.emcFlag==1){
							$('#adWestMediUrgTdId').prop("checked", true);
							$('#adNotDrugUrgTdId').prop("checked", true); 
						}else{
							$('#adWestMediUrgTdId').prop("checked", false);
							$('#adNotDrugUrgTdId').prop("checked", false); 
						}
						if(rowData.dateBgn!=null&&rowData.dateBgn!=''){
							$('#startTime').datetimebox('setValue',rowData.dateBgn);//医嘱开始时间
						}else{
							$('#startTime').datetimebox('setValue','');
						}
						if(rowData.dateEnd!=null&&rowData.dateEnd!=''){
							$('#endTime').datetimebox('setValue',rowData.dateEnd);//医嘱结束时间
						}else{
							$('#endTime').datetimebox('setValue','');
						}
						if(rowData.classCode=='07'){//类型为检查	
							$('#bodyParts').show();	
					    	$('#bodyPartss').show();
					    	$('#sampleType').hide();	
					    	$('#sampleTypes').hide();					    	
					    }else{
					    	$('#bodyParts').hide();	
					    	$('#bodyPartss').hide();	
					    	$('#sampleType').show();	
					    	$('#sampleTypes').show();
					    }			    	
				    	if(rowData.classCode=='12'){//类型为转科				    		
				    		$('#adNotDrugInsTdId').combobox('disable');
				    		$('#adNotDrugSamTdId').combobox('disable');
					    }		
				    	if(rowData.classCode!='12'){		    		
				    		$('#adNotDrugInsTdId').combobox('enable');
				    		$('#adNotDrugSamTdId').combobox('enable');
					    }
				    	if(rowData.classCode=='17'||rowData.classCode=='18'){//中药/西药
				    		$('#adNotDrugDivId').hide();
				    		$('#adChinMediDivId').hide();
				    		$('#adWestMediDivId').show();
				    	}else if(rowData.classCode=='16'){
				    		$('#adNotDrugDivId').hide();
				    		$('#adWestMediDivId').hide();
				    		$('#adChinMediDivId').show();
				    		$('#adProjectNumTdId').numberbox('readonly',true);
				    	}else{
				    		$('#adChinMediDivId').hide();
				    		$('#adWestMediDivId').hide();
				    		$('#adNotDrugDivId').show();
				    	}
						if(rowData.hypotest==2 ||rowData.hypotest==3||rowData.hypotest==4){
							$('#skiTdId').show();
							$('#skiTdName').show();
							$('#adWestMediSkiTdId').combobox('setValue',rowData.hypotest);	
						}else{
							$('#skiTdId').hide();
							$('#skiTdName').hide();
						}
					}
				}
			});
			$('#infolistB').datagrid({
				height:$("body").height()-365,
				onRowContextMenu: function(e, rowIndex, rowData) { //右键时触发事件
	                e.preventDefault(); //阻止浏览器捕获右键事件
	                $(this).datagrid("selectRow", rowIndex); //根据索引选中该行	                
	                $('#adviceListMenu').menu('show', {//显示右键菜单
	                    left: e.pageX,//在鼠标点击处显示菜单
	                    top: e.pageY
	                });
	            },
				onSelect:function(rowIndex,rowData){
					if(rowData.moStat==1){
						$('#adDelAdviceBtn').hide();
						$('#adStopAdviceBtn').show();
						return;
					}else if(rowData.moStat==2){
						$('#adDelAdviceBtn').hide();
						$('#adStopAdviceBtn').show();
						return;
					}else if(rowData.moStat==3){
						$('#adDelAdviceBtn').show();
						$('#adStopAdviceBtn').hide();
						return;
					}else{
						$('#adDelAdviceBtn').show();
						$('#adStopAdviceBtn').hide();
						if(rowData.itemType=='1'){
							initCombobox(false,colorInfoUnitsMap,rowData.drugpackagingUnit,rowData.minUnit);
						}else{
							$('#adProjectUnitTdId').combobox('select',rowData.drugpackagingUnit);
						}
						if(rowData.typeCode!=null&&rowData.typeCode!=''){
							$('#temDocAdvType').combobox('select',rowData.typeCode);	
						}else{
							$('#temDocAdvType').combobox('clear');
						}
						if(rowData.classCode!=null&&rowData.classCode!=''){
							$('#adProjectTdId').combobox('select',rowData.classCode);	
						}else{
							$('#adProjectTdId').combobox('clear');
						}
						if(rowData.frequencyCode!=null&&rowData.frequencyCode!=''){//中、西药品频次
							$('#adWestMediFreTdId').combobox('select',rowData.frequencyCode);	
						}else{
							$('#adWestMediFreTdId').combobox('clear');
						}
						if(rowData.usageCode!=null&&rowData.usageCode!=''){//中、西药品用法
							$('#adWestMediUsaTdId').combobox('select',rowData.usageCode);	
						}else{
							$('#adWestMediUsaTdId').combobox('clear');
						}
						$('#adProjectNumTdId').numberspinner('setValue',rowData.qtyTot);
						$('#adProjectNameTdId').textbox('setValue',rowData.itemName);
						if(rowData.priceUnit!=null&&rowData.priceUnit!=''){
							$('#adProjectUnitTdId').combobox('select',rowData.priceUnit);	
						}else{
							$('#adProjectUnitTdId').combobox('clear');
						}
						$('#adWestMediDosMaxTdId').numberbox('setValue',(rowData.doseOnce/parseFloat(rowData.baseDose)).toFixed(2));
						$('#adWestMediMinUnitTdId').text(drugpackagingunitMaps[rowData.minUnit]);
						$('#adWestMediDosMinTdId').numberbox('setValue',rowData.doseOnce);
						$('#adWestMediDosDosaTdId').text(drugdoseunitMaps[rowData.doseUnit]);
						$('#adExeDeptTdId').textbox('setValue',rowData.execDpnm);//执行科室
						if(rowData.moNote2!=null&&rowData.moNote2!=''){
							$('#adWestMediRemTdId').combobox('setText',rowData.moNote2);//西药中成药备注
						}else{
							$('#adWestMediRemTdId').combobox('clear');
						}					
						$('#adChinMediNumTdId').numberbox('setValue',rowData.useDays);//草药付数					
						if(rowData.frequencyCode!=null&&rowData.frequencyCode!=''){//中、西药品频次
							$('#adChinMediFreTdId').combobox('select',rowData.frequencyCode);	
						}else{
							$('#adChinMediFreTdId').combobox('clear');
						}
						if(rowData.usageCode!=null&&rowData.usageCode!=''){
							$('#adChinMediUsaTdId').combobox('select',rowData.usageCode);//中草药用法	
						}else{
							$('#adChinMediUsaTdId').combobox('clear');
						}
						if(rowData.moNote2!=null&&rowData.moNote2!=''){
							$('#adChinMediRemTdId').combobox('setText',rowData.moNote2);//中草药备注 
						}else{
							$('#adChinMediRemTdId').combobox('clear');
						}	
						if(rowData.itemNote!=null&&rowData.itemNote!=''){
							$('#adNotDrugInsTdId').combobox('select',rowData.itemNote);//部位
						}else{
							$('#adNotDrugInsTdId').combobox('clear');
						}
						if(rowData.labCode!=null&&rowData.labCode!=''){
							$('#adNotDrugSamTdId').combobox('select',rowData.labCode);//样本类型
						}else{
							$('#adNotDrugSamTdId').combobox('clear');
						}	
						if(rowData.moNote2!=null&&rowData.moNote2!=''){
							$('#adNotDrugRemTdId').combobox('setText',rowData.moNote2);//非药品备注
						}else{
							$('#adNotDrugRemTdId').combobox('clear');
						}
						if(rowData.emcFlag==1){
							$('#adWestMediUrgTdId').prop("checked", true);
							$('#adNotDrugUrgTdId').prop("checked", true); 
						}else{
							$('#adWestMediUrgTdId').prop("checked", false);
							$('#adNotDrugUrgTdId').prop("checked", false); 
						}
						if(rowData.dateBgn!=null&&rowData.dateBgn!=''){
							$('#startTime').datetimebox('setValue',rowData.dateBgn);//医嘱开始时间
						}else{
							$('#startTime').datetimebox('setValue','');
						}
						if(rowData.dateEnd!=null&&rowData.dateEnd!=''){
							$('#endTime').datetimebox('setValue',rowData.dateEnd);//医嘱结束时间
						}else{
							$('#endTime').datetimebox('setValue','');
						}
						if(rowData.classCode=='07'){//类型为检查	
							$('#bodyParts').show();	
					    	$('#bodyPartss').show();
					    	$('#sampleType').hide();	
					    	$('#sampleTypes').hide();					    	
					    }else{
					    	$('#bodyParts').hide();	
					    	$('#bodyPartss').hide();	
					    	$('#sampleType').show();	
					    	$('#sampleTypes').show();
					    }			    	
				    	if(rowData.classCode=='12'){//类型为转科				    		
				    		$('#adNotDrugInsTdId').combobox('disable');
				    		$('#adNotDrugSamTdId').combobox('disable');
					    }		
				    	if(rowData.classCode!='12'){		    		
				    		$('#adNotDrugInsTdId').combobox('enable');
				    		$('#adNotDrugSamTdId').combobox('enable');
					    }
				    	if(rowData.classCode=='17'||rowData.classCode=='18'){//中药/西药
				    		$('#adNotDrugDivId').hide();
				    		$('#adChinMediDivId').hide();
				    		$('#adWestMediDivId').show();
				    	}else if(rowData.classCode=='16'){
				    		$('#adNotDrugDivId').hide();
				    		$('#adWestMediDivId').hide();
				    		$('#adChinMediDivId').show();
				    		$('#adProjectNumTdId').numberbox('readonly',true);
				    	}else{
				    		$('#adChinMediDivId').hide();
				    		$('#adWestMediDivId').hide();
				    		$('#adNotDrugDivId').show();
				    	}
						if(rowData.hypotest==2 ||rowData.hypotest==3||rowData.hypotest==4){
							$('#skiTdId').show();
							$('#skiTdName').show();
							$('#adWestMediSkiTdId').combobox('setValue',rowData.hypotest);	
						}else{
							$('#skiTdId').hide();
							$('#skiTdName').hide();
						}
					}
				}
			});	
			if(!$('#pharmacyCombobox').combobox().next('span').find('input').focus().is('1')){
				$('#pharmacyCombobox').combobox().next('span').find('input').blur();
				return false;
			}
		});
		var index = 0;	
		var index1 = 0;
		var flag = 1;
		$(document).keydown(function(e){
			if(e.which == 113) {//113-F2
				adPrintAdvice();
			}
			if(e.which == 115) {//115-F4
					$('#longDocAdvType').combobox().next('span').find('input').focus();
			}
			if(e.which == 8){				
				if(!$('#adProjectNameTdId').combogrid('combogrid').focus().is('1')){
					$('#adProjectNameTdId').combogrid('combogrid').blur();
					return false;
				}
			}
			if(event.ctrlKey && e.which ==112){
				adStartAdvice();
			}
			if(e.which == 37){
				if(flag==1){
					if(index1>0 && index1<=3){
						index1 = index1-1;
					}				
					$('#tt1').tabs('select',index1);
				}
				if(flag==2){
					if(index1>0 && index1<=1){
						index1 = index1-1;
					}				
					$('#tt').tabs('select',index1);
				}
			}
			if(e.which == 39){
				if(flag==1){
					if(index1>=0 && index1<3){
						index1 = index1+1;
					}					
					$('#tt1').tabs('select',index1);
				}				
				if(flag==2){
					if(index1>=0 && index1<1){
						index1 = index1-1;
					}				
					$('#tt').tabs('select',index1);
				}
			}
			if(e.which == 38){
				index1 = 0;
				flag = 1;
				$('#tt1').tabs('select',index1);
			}
			if(e.which == 40){
				index1 = 0;
				flag = 2;
				$('#tt').tabs('select',index1);
			}
		});
					
		var colorInfoUnitsMap = new Map();//单位Map
		
		//获得单位
		$.ajax({
			url: '<%=basePath%>inpatient/docAdvManage/queryUnitsList.action',
			type:'post',
			async:false,
			success: function(data) {						
				colorInfoUnitsList = data;
				if(colorInfoUnitsList!=null&&colorInfoUnitsList.length>0){
					for(var i=0;i<colorInfoUnitsList.length;i++){
						colorInfoUnitsMap.put(colorInfoUnitsList[i].encode,colorInfoUnitsList[i].name);
					}
				}
			}
		});

		//初始化单位下拉框
		function initCombobox(bool,map,unit,minUnit){
			var dataint = null;
			if(bool){		
				var retVal = "[";
				if(map!=null){			
					map.each(function(key,value,index){
						if(retVal.length>1){
							retVal+=",";
						}
						retVal+="{\"encode\":\""+key+"\",\"name\":\""+value+"\"}";
					})
				}
				retVal +="]";
				dataint = eval("("+retVal+")");
			}else{
				var retVal = "[";
				if(unit==minUnit && unit!=null && unit!=''){
					retVal+="{\"encode\":\""+unit+"\",\"name\":\""+map.get(unit)+"\"}";
				}else{
					if(unit!=null && unit!=''){
						retVal+="{\"encode\":\""+unit+"\",\"name\":\""+map.get(unit)+"\"}";
					}					
					if(minUnit!=null){
						retVal+=",{\"encode\":\""+minUnit+"\",\"name\":\""+map.get(minUnit)+"\"}";
					}
				}
				if(retVal=="["){
					map.each(function(key,value,index){
						if(value=="次"){
							if(retVal.length==1){
								retVal+="{\"encode\":\""+key+"\",\"name\":\""+value+"\"}";
							}
						}
					});
				}
				retVal +="]";
				dataint = eval("("+retVal+")");
			}			
			$('#adProjectUnitTdId').combobox({
    			data:dataint,    
    		    valueField:'encode',    
    		    textField:'name',
    		    editable:false,
    		    disabled:false,
    		    onSelect:function(record){
    		    	if(record==''||record==null||record=='undefined'){
    		    	}else{
    		    		var index = getIndexForAdDgList();
    	   		    	var indexA = getIndexForAdDgListA();
   						if(indexA>=0){
   							$('#infolistA').datagrid('updateRow',{
   								index: indexA,
   								row: {									
   									priceUnit:record.encode,
   									changeNo:1
   								}
   							});
   						}
   						if(index>=0){
   							$('#infolistB').datagrid('updateRow',{
   								index: index,
   								row: {									
   									priceUnit:record.encode,
   									changeNo:1
   								}
   							});
   						}
    		    	}
    		    }
    		});						
		} 	
		
		//添加功能事件
		function functionInfo(){
			initCombobox(true,colorInfoUnitsMap,null,null);//单位
		}
		//单位 列表页 显示	
		function drugpackagingunitFamaters(value,row,index){	
			if(value!=null&&value!=""){
				if(row.itemType=='1'){					
					if(drugpackagingunitMaps[value]!=null&&drugpackagingunitMaps[value]!=""){
						return drugpackagingunitMaps[value];
					}
					return value;
				}
				return value;
			}			
		}
		//剂量单位 列表页 显示	
 		function drugpdoseunitFamaters(value,row,index){	
			if(value!=null&&value!=""){	
				if(drugdoseunitMaps[value]!=null&&drugdoseunitMaps[value]!=""){
					return drugdoseunitMaps[value];
				}
				return value;
			}			
		} 
 		//扣库科室 列表页 显示		
		function implDepartmentFamaters(value,row,index){			
			if(value!=null&&value!=""){					
				return implDepartmentMap[value];									
			}			
		}
 		//组合号标志
		function combNoFlagFamater(value,row,index){
				var rows = $('#infolistA').datagrid('getRows');
				if(rows.length>0){
					var a=0;
					for(var i=0;i<rows.length;i++){
						if(row.combNo==rows[i].combNo && row.combNo!=null && row.combNo!=''){
							a++;
						}
					}
					if(a==1){
						return '';
					}else if(a==2){
						for(var j=0;j<rows.length;j++){
							if(row.combNo==rows[j].combNo){
								if(row.sortId==rows[j].sortId){
									if(j==0){
										return '┓';										
									}else if(j!=0 && (row.combNo!=rows[j-1].combNo)){
										return '┓';
									}else if(j==rows.length-1){
										return '┛';
									}else if((j!=rows.length-1) && (row.combNo!=rows[j+1].combNo)){
										return '┛';
									}									
								}								
							}
						}						
					}else if(a>2){
						for(var j=0;j<rows.length;j++){
							if(row.combNo==rows[j].combNo){
								if(row.sortId==rows[j].sortId){
									if(j==0){
										return '┓';
									}else if(j!=0 && row.combNo!=rows[j-1].combNo){
										return '┓';
									}else if(j==rows.length-1){
										return '┛';
									}else if(j!=rows.length-1 && row.combNo!=rows[j+1].combNo){
										return '┛';
									}else{
										return '┃';
									}																		
								}								
							}
						}
					}
				}				
			
		}
		//项目弹出窗口
		function searchInfo(){
			if($('#adProjectTdId').combobox('getValue')==''){
				$.messager.alert('提示',"请先选择一个系统类别!");
				return;
			}
			if($('#adProjectTdId').combobox('getValue')=='12'||$('#adProjectTdId').combobox('getValue')=='10'||$('#adProjectTdId').combobox('getValue')=='04'||
					$('#adProjectTdId').combobox('getValue')=='11'||$('#adProjectTdId').combobox('getValue')=='13'||$('#adProjectTdId').combobox('getValue')=='15'){
				if($('#adProjectTdId').combobox('getValue')=='12' && $('#adExeDeptTdId').textbox('getValue')==''){
					$.messager.alert('提示',"请先填写执行科室!");
				}else{
					var mytime = new Date().format("yyyy-MM-dd hh:mm:ss");
					if($('#adProjectTdId').combobox('getValue')=='15'){
						if($('#adProjectNameTdId').textbox('getValue')==''){
							$.messager.alert('提示',"项目名称不能为空!");
							return;
						}						
						var qq = $('#ttta').tabs('getSelected');				
						var tab = qq.panel('options');
						if(tab.title=='长期医嘱'){
							var lastIndex = $('#infolistA').datagrid('appendRow',{
								id:'',
								itemCode:'',//特殊标识
								typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
								typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
								classCode:$('#adProjectTdId').combobox('getValue'),
								className:$('#adProjectTdId').combobox('getText'),
								itemName:$('#adProjectNameTdId').textbox('getValue'),//医嘱名称
								combNo:'',//组
								qtyTot:1,//总量
								priceUnit:'2',//单位（总量单位）
								drugpackagingUnit:'',//包装单位
								packQty:'',//包装数量
			    				minUnit:'',//最小单位
								doseOnce:'',//每次量		
								doseOnces:'',//每次量计算											
								doseUnit:'',//单位（剂量单位）
								specs:'',//规格 
								doseModelCode:'',
								drugType:'',//药品类型
								drugQuality:'',//药品性质
								itemPrice:'',
								useDays:0,//付数
								frequencyCode:'QD1',//频次代码
								frequencyName:'1/日（8:00）',//频次名称
								usageCode:'',//用法Id		
								useName:'',//名称
								dateBgn:mytime,//开始时间 
								dateEnd:'',//停止时间
								moDate:mytime,//开立时间 
								docName:$('#username').val(),//开立医生
								execDpcd:$('#adExeDeptTd').val(),//执行科室 id
								execDpnm:$('#adExeDeptTdId').textbox('getValue'),//执行科室 
								emcFlag:0,//加急标记
								isUrgent:'',//急
								labCode:'',//样本类型
								itemNote:'',//检查部位
								pharmacyCode:'',//扣库科室
								moNote2:$('#adWestMediRemTdId').combobox('getText'),//备注 
								recUsernm:$('#username').val(),//录入人
								listDpcd:$('#deptId').val(),//开立科室 
								updateUser:'',//停止人 
								baseDose:'',//基本剂量
								permission:'',//患者是否同意
								hypotest:'',//皮试Id
								hypotestName:'',//皮试
								itemType:'0',//药品非药品标识
								moStat:0,//医嘱状态
								sortId:''//顺序号								
							}).datagrid('getRows').length-1;
							$('#infolistA').datagrid('selectRow',lastIndex);
						}else if(tab.title=='临时医嘱'){
							var lastIndex = $('#infolistB').datagrid('appendRow',{
								id:'',
								itemCode:'',//特殊标识
								typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
								typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
								classCode:$('#adProjectTdId').combobox('getValue'),
								className:$('#adProjectTdId').combobox('getText'),
								itemName:$('#adProjectNameTdId').textbox('getValue'),//医嘱名称
								combNo:'',//组
								qtyTot:1,//总量
								priceUnit:'2',//单位（总量单位）
								drugpackagingUnit:'',//包装单位
								packQty:'',//包装数量
			    				minUnit:'',//最小单位
								doseOnce:'',//每次量		
								doseOnces:'',//每次量计算											
								doseUnit:'',//单位（剂量单位）
								specs:'',//规格 
								doseModelCode:'',
								drugType:'',//药品类型
								drugQuality:'',//药品性质
								itemPrice:'',
								useDays:0,//付数
								frequencyCode:'QD1',//频次代码
								frequencyName:'1/日（8:00）',//频次名称
								usageCode:'',//用法Id		
								useName:'',//名称
								dateBgn:mytime,//开始时间 
								dateEnd:'',//停止时间
								moDate:mytime,//开立时间 
								docName:$('#username').val(),//开立医生
								execDpcd:$('#adExeDeptTd').val(),//执行科室 id
								execDpnm:$('#adExeDeptTdId').textbox('getValue'),//执行科室 
								emcFlag:0,//加急标记
								isUrgent:'',//急
								labCode:'',//样本类型
								itemNote:'',//检查部位
								pharmacyCode:'',//扣库科室
								moNote2:$('#adWestMediRemTdId').combobox('getText'),//备注 
								recUsernm:$('#username').val(),//录入人
								listDpcd:$('#deptId').val(),//开立科室 
								updateUser:'',//停止人 
								baseDose:'',//基本剂量
								permission:'',//患者是否同意
								hypotest:'',//皮试Id
								hypotestName:'',//皮试
								itemType:'0',//药品非药品标识
								moStat:0,//医嘱状态
								sortId:''//顺序号								
							}).datagrid('getRows').length-1;
							$('#infolistB').datagrid('selectRow',lastIndex);
						}							
					}else{
						var qq = $('#ttta').tabs('getSelected');				
						var tab = qq.panel('options');
						if(tab.title=='长期医嘱'){
							var lastIndex = $('#infolistA').datagrid('appendRow',{
								id:'',
								itemCode:'',//特殊标识
								typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
								typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
								classCode:$('#adProjectTdId').combobox('getValue'),
								className:$('#adProjectTdId').combobox('getText'),
								itemName:$('#adProjectNameTdId').textbox('getValue'),//医嘱名称
								combNo:'',//组
								packQty:1,//总量
								priceUnit:'2',//单位（总量单位）
								drugpackagingUnit:'', 
			    				minUnit:'',
								doseOnce:'',//每次量		
								doseOnces:'',//每次量计算											
								doseUnit:'',//单位（剂量单位）
								specs:'',//规格 
								doseModelCode:'',
								drugType:'',
								drugQuality:'',
								itemPrice:'',
								useDays:0,//付数
								frequencyCode:'ONCE',//频次代码
								frequencyName:'ONCE',//频次名称
								usageCode:'',//用法Id		
								useName:'',//名称
								dateBgn:mytime,//开始时间 
								dateEnd:'',//停止时间
								moDate:mytime,//开立时间 
								docName:$('#username').val(),//开立医生
								execDpcd:$('#adExeDeptTd').val(),//执行科室 id
								execDpnm:$('#adExeDeptTdId').textbox('getValue'),//执行科室 
								emcFlag:0,//加急标记
								isUrgent:'',//急
								labCode:'',//样本类型
								itemNote:'',//检查部位
								//openDoctor:'当前医生',//
								pharmacyCode:'',//扣库科室
								moNote2:$('#adWestMediRemTdId').combobox('getText'),//备注 
								recUsernm:$('#username').val(),//录入人
								listDpcd:$('#deptId').val(),//开立科室 
								updateUser:'',//停止人 
								baseDose:'',//基本剂量
								permission:'',//患者是否同意
								hypotest:'',//皮试Id
								hypotestName:'',//皮试
								itemType:'0',//药品非药品标识
								moStat:0,//医嘱状态
								sortId:''//顺序号								
							}).datagrid('getRows').length-1;
							$('#infolistB').datagrid('selectRow',lastIndex);
						}else if(tab.title=='临时医嘱'){
							var lastIndex = $('#infolistB').datagrid('appendRow',{
								id:'',
								itemCode:'',//特殊标识
								typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
								typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
								classCode:$('#adProjectTdId').combobox('getValue'),
								className:$('#adProjectTdId').combobox('getText'),
								itemName:$('#adProjectNameTdId').textbox('getValue'),//医嘱名称
								combNo:'',//组
								packQty:1,//总量
								priceUnit:'2',//单位（总量单位）
								drugpackagingUnit:'', 
			    				minUnit:'',
								doseOnce:'',//每次量		
								doseOnces:'',//每次量计算											
								doseUnit:'',//单位（剂量单位）
								specs:'',//规格 
								doseModelCode:'',
								drugType:'',
								drugQuality:'',
								itemPrice:'',
								useDays:0,//付数
								frequencyCode:'ONCE',//频次代码
								frequencyName:'ONCE',//频次名称
								usageCode:'',//用法Id		
								useName:'',//名称
								dateBgn:mytime,//开始时间 
								dateEnd:'',//停止时间
								moDate:mytime,//开立时间 
								docName:$('#username').val(),//开立医生
								execDpcd:$('#adExeDeptTd').val(),//执行科室 id
								execDpnm:$('#adExeDeptTdId').textbox('getValue'),//执行科室 
								emcFlag:0,//加急标记
								isUrgent:'',//急
								labCode:'',//样本类型
								itemNote:'',//检查部位
								pharmacyCode:'',//扣库科室
								moNote2:$('#adWestMediRemTdId').combobox('getText'),//备注 
								recUsernm:$('#username').val(),//录入人
								listDpcd:$('#deptId').val(),//开立科室 
								updateUser:'',//停止人 
								baseDose:'',//基本剂量
								permission:'',//患者是否同意
								hypotest:'',//皮试Id
								hypotestName:'',//皮试
								itemType:'0',//药品非药品标识
								moStat:0,//医嘱状态
								sortId:''//顺序号								
							}).datagrid('getRows').length-1;
							$('#infolistB').datagrid('selectRow',lastIndex);
						}						
					}						
				}													
			}else{
				var index = getIndexForAdDgList();
				if(index>=0){
					$('#infolistB').datagrid('unselectRow',index);
				} 	
				var sysType ='';
				if($('#adProjectTdId').combobox('getValue')!='0'){
					sysType = $('#adProjectTdId').combobox('getValue');
				}else{					
					var types= $('#adProjectTdId').combobox('getData');
					for(var i=0;i<types.length;i++){
						if(sysType==''){
							sysType = "'"+types[i].id+"'";
						}else{
							sysType = sysType+','+"'"+types[i].id+"'";
						}						
					}					
				}								
				var sysTypeName = encodeURIComponent(encodeURIComponent($('#adProjectTdId').combobox('getText')));
				var proName = encodeURIComponent(encodeURIComponent($('#adProjectNameTdId').textbox('getValue').trim()));
				AdddilogModel("adWestMediModlDivId","项目信息",'<%=basePath%>inpatient/docAdvManage/queryDrugOrUndrugInfo.action?inpatientOrder.itemName='+proName+'&inpatientOrder.classCode='+sysType+'&inpatientOrder.className='+sysTypeName,'70%','75%');	
			}	 				
		}				
		//执行科室弹出窗口
		function searchDeptInfo(){
			var row=null;
			var qq = $('#ttta').tabs('getSelected');				
			var tab = qq.panel('options');
			if(tab.title=='长期医嘱'){
				row = $('#infolistA').datagrid('getSelected');
			}else if(tab.title=='临时医嘱'){
				row = $('#infolistB').datagrid('getSelected');
			}
			if(row!=null||$('#adProjectTdId').combobox('getValue')=='402880b751f104d10151f19402800008'){
				var deptName = encodeURI($('#adExeDeptTdId').textbox('getValue').trim());
				AdddilogModel("adExeDeptModlDivId","科室信息",'<%=basePath%>inpatient/docAdvManage/queryExeDept.action?inpatientOrder.execDpnm='+deptName,'45%','63%');
			}else{
				$.messager.alert('提示',"必须选择一条非药品医嘱！");
			}						
		}
		//获得infolistA索引
		function getIndexForAdDgListA(){
			var row = $('#infolistA').datagrid('getSelected');
			if(row!=null && row.moStat<=0){
				return $('#infolistA').datagrid('getRowIndex',row);
			}else{
				return -1;
			}
		}
		//获得infolistB索引
		function getIndexForAdDgList(){
			var row = $('#infolistB').datagrid('getSelected');
			if(row!=null && row.moStat<=0){
				return $('#infolistB').datagrid('getRowIndex',row);
			}else{
				return -1;
			}
		}
		//加载患者树(左侧患者树)
		$('#tDt').tree({
				url : '<%=basePath%>inpatient/doctorAdvice/treeDoctorAdvice.action',
				method : 'get',
				animate : true,
				lines : true,
				onlyLeafCheck:true,
				formatter : function(node) {//统计节点总数
					var s = node.text;
					if(node.attributes.dateState!=null&&node.attributes.dateState!=''){
						s += node.attributes.dateState;
					}
					if(node.attributes.leaveFlag!=null&&node.attributes.leaveFlag!=''){
						s += node.attributes.leaveFlag;
					} 	
					if (node.children.length>0) {					 						
						s += '<span style=\'color:blue\'>('
								+ node.children.length + ')</span>';					
					}					
					return s;
				},onClick : function(node){	
					if(node.attributes.pid=='root'){
						return;
					}
					$("#adNameId").text('');	
					$("#adSexId").text('');
					$("#adFreeCostId").text('');
					$("#inpatientNo").val('');
					$("#patientNo").val('');
					$("#deptCode").val('');
					$("#nurseCellCode").val('');
					$("#babyFlag").val('');
					$("#pid").val('');
					$("#createOrderFlag").val('');
					$("#adPactId").text('');
					$("#adNameId").text(node.text);	
					$("#adSexId").text(sexMap.get(node.attributes.reportSex));
					$("#adFreeCostId").text(node.attributes.freeCost);
					$("#inpatientNo").val(node.attributes.inpatientNo);
					$("#patientNo").val(node.attributes.medicalrecordId);
					$("#deptCode").val(node.attributes.deptCode);
					$("#nurseCellCode").val(node.attributes.nurseCellCode);
					$("#babyFlag").val(node.attributes.babyFlag);
					$("#pid").val(node.attributes.pid);
					$("#createOrderFlag").val(node.attributes.createOrderFlag);
					$('#adPrintAdviceBtn').menubutton('enable');//打印医嘱
					$.ajax({
						url: '<%=basePath%>inpatient/docAdvManage/queryReglist.action',
						type:'post',
						success: function(regdata) {
							var regjson=regdata;										
							for(var i=0;i<regjson.length;i++){
								if(node.attributes.pactCode==regjson[i].encode){//合同单位颜色
									if(regjson[i].encode=='01'){
										$("#adPactId").text(regjson[i].name);
										$("#adPactId").css('color','red');
									}
									if(regjson[i].encode=='02'){
										$("#adPactId").text(regjson[i].name);
										$("#adPactId").css('color','green');
									}
									if(regjson[i].encode=='03'){
										$("#adPactId").text(regjson[i].name);
										$("#adPactId").css('color','blue');
									}
									if(regjson[i].encode=='05'){
										$("#adPactId").text(regjson[i].name);
										$("#adPactId").css('color','#CDCD00');
									}
									if(regjson[i].encode=='07'){
										$("#adPactId").text(regjson[i].name);
										$("#adPactId").css('color','orange');
									}
									if(regjson[i].encode=='08'){
										$("#adPactId").text(regjson[i].name);
										$("#adPactId").css('color','grey');
									}
									if(regjson[i].encode=='10'){
										$("#adPactId").text(regjson[i].name);
										$("#adPactId").css('color','pink');
									}
									if(regjson[i].encode=='13'){
										$("#adPactId").text(regjson[i].name);
										$("#adPactId").css('color','purple');
									}
									if(regjson[i].encode=='15'){
										$("#adPactId").text(regjson[i].name);
										$("#adPactId").css('color','brown  ');
									}
									if(regjson[i].encode=='20'){
										$("#adPactId").text(regjson[i].name);
										$("#adPactId").css('color','maroon');
									}
									if(regjson[i].encode=='90'){
										$("#adPactId").text(regjson[i].name);
										$("#adPactId").css('color','lavender');
									}
								}
							}
						}
					});		
					loadAjax();
					$("#inpatientId").val(node.id);
					inpatientNo=node.attributes.inpatientNo;					
					var qqs = $('#tt1').tabs('getSelected');				
					var tabs = qqs.panel('options');
					if(tabs.title=='医嘱'){						
						if($("#comboboxId").val()==1){//开立状态
							var qq = $('#ttta').tabs('getSelected');				
							var tab = qq.panel('options');
							if(tab.title=='长期医嘱'){
								recordId = '02';
								loadTabs(inpatientNo,recordId);
							}else{
								recordId = '06';
								loadTabs(inpatientNo,recordId);
							}								
						}else{
							recordId = null;				
							loadTabs(inpatientNo,recordId);
						}
					}else{//历史医嘱
						var qqb = $('#tttb').tabs('getSelected');				
						var tabb = qqb.panel('options');
						if(tabb.title=='长期医嘱'){							
							loadTabss(inpatientNo);
						}else{							
							loadTabss(inpatientNo);
						}
					}
				},onContextMenu : function(e, node) {
					e.preventDefault();
					$(this).tree('select',node.target);					
					var id = $('#tDt').tree('getSelected');
					if(id!=null){
						$('#patientinfoTree').css("display","block");
						$('#mm').menu('show',{
							left: e.pageX,
							top: e.pageY
						});						
					}
				}
		});
		/**
	 	* 刷新树
	    * @author  yeguanqun
	    * @date 2016-4-12 10:53
	    * @version 1.0
	    */
	   	function refresh(){
			$('#tDt').tree('reload'); 
		}
		/**
	 	* 展开树
	    * @author  yeguanqun
	    * @date 2016-4-12 10:53
	    * @version 1.0
	    */
		function expandAll(){
			$('#tDt').tree('expandAll');
		}
		/**
	 	* 关闭树
	    * @author  yeguanqun
	    * @date 2016-4-12 10:53
	    * @version 1.0
	    */
	   	function collapseAll(){
			$('#tDt').tree('collapseAll');
		}
		//查看患者信息弹出窗口
		function patientinfoTree() {
			var node = $('#tDt').tree('getSelected');
			AdddilogModel("addData-window","患者信息","<%=basePath%>inpatient/doctorAdvice/inpatientInfos.action?id="+node.attributes.inpatientNo,'43%','58%');
			$('#tDt').tree('reload');
		}
		//加载模式窗口
		function AdddilogModel(id,title,url,width,height) {
			$('#'+id).dialog({    
			    title: title,    
			    width: width,    
			    height: height,    
			    closed: false,    
			    cache: false,
			    href: url,    
			    modal: true   
			});    
		}
		
		var groupVal = 1;
		var adProjectTdId='';
		function searchStackTreeNodes(){
			var searchParam = $('#adStackTreeSearch').textbox('getValue');
			$("#adStackTree").tree("collapseAll");
			$.post("<%=basePath%>nursestation/nurseCharge/getstackNameParam.action",{infoId:searchParam},function(data){
	    		if(data!=null&&data.length>0){
	    			for(var i=0;i<data.length;i++){
	    				//展开一级节点
	    				var source = data[i].source;
	    				var node1 = $('#adStackTree').tree('find',source);
	    				$('#adStackTree').tree('expand',node1.target);
	    			}
	    			setTimeout(function(){
	    				for(var i=0;i<data.length;i++){
		    				var id = data[i].id;
	    					var node2 = $('#adStackTree').tree('find',id);
		    				$('#adStackTree').tree('expand',node2.target);
		    			}
	    			},1000);
	    		}
	    	});
		}
		//开立医嘱
		function adStartAdvice(){			
			var pid = $("#pid").val();
			var createOrderFlag = $("#createOrderFlag").val();
			if(pid=='3'&&createOrderFlag=='0'){
				$.messager.alert('提示',"该患者会诊申请未允许开立医嘱!");
				return;
			}
			var inpatientId = $("#inpatientId").val();
			if(inpatientId==null||inpatientId==''){
				$.messager.alert('提示',"患者信息不存在,请点击获取患者开立医嘱!");
				return;
			}
			var pharmacyInputId = $('#pharmacyInputId').val();
			if(pharmacyInputId==null||pharmacyInputId==''){
				$.messager.alert('提示',"请选择药房!");
				return;
			}						
			var qq = $('#ttta').tabs('getSelected');				
			var tab = qq.panel('options');
			if(tab.title=='长期医嘱'){				
				loadDatagridA(1,inpatientNo,'02');
				$("#long").show();
				$("#tem").hide();
				$('#docAdvTypeFilt').combobox('disable');
				$('#comboboxId').val(1);
				var rowsA = $('#infolistA').datagrid('getRows');
				if(rowsA!=null&&rowsA.length>0){
					var leng = rowsA.length;
					for(var i=0;i<leng;i++){
						$('#infolistA').datagrid('deleteRow',$('#infolistA').datagrid('getRowIndex',rowsA[0]));
					}
				}	
				$('#longDocAdvType').combobox('disable');
			}else if(tab.title=='临时医嘱'){
				$('#temDocAdvType').combobox().next('span').find('input').focus();
				loadDatagridB(0,inpatientNo,'06');
				$("#tem").show();
				$("#long").hide();
				$('#docAdvTypeFilt').combobox('disable');
				$('#comboboxId').val(1);
				var rows = $('#infolistB').datagrid('getRows');
				if(rows!=null&&rows.length>0){
					var leng = rows.length;
					for(var i=0;i<leng;i++){
						$('#infolistB').datagrid('deleteRow',$('#infolistB').datagrid('getRowIndex',rows[0]));
					}
				}	
			}								 					 
			$('#adEndAdviceBtn').menubutton('enable');//退出开立
			$('#adSaveAdviceBtn').menubutton('enable');//保存医嘱
			$('#adDelAdviceBtn').menubutton('enable');//删除医嘱
			$('#adStopAdviceBtn').menubutton('enable');//停止医嘱
			var userid=$("#userid").val();
			$.ajax({
				url: "<%=basePath%>inpatient/docAdvManage/queryAuditInfo.action",		
				data:'userId='+userid+'&parameterCode='+'yzshzj',
				type:'post',
				success: function(auditdata) {
					if(auditdata>0){
						$('#adAuditAdviceBtn').menubutton('enable');//审核医嘱
					}
				}
			});
			$('#adHerbMedicineBtn').menubutton('enable');//草药查询
			$('#adAddGroupBtn').menubutton('enable');//添加组套
			$('#adCancelGroupBtn').menubutton('enable');//取消组套
			$('#adSaveGroupBtn').menubutton('enable');//保存组套
			
			
			$('#adStackTree').tree({    
				url:"<%=basePath%>nursestation/nurseCharge/stackAndStackInfoForTree.action",
			 	queryParams:{drugType:"2",type:"1"},
			    onDblClick:function(node){
			    	var qq = $('#ttta').tabs('getSelected');				
					var tab = qq.panel('options');
					if(tab.title=='长期医嘱'){
						if($('#longDocAdvType').combobox('getValue')==''){
							$.messager.alert('提示',"请先选择医嘱类型！");
							return;
						}
					}else if(tab.title=='临时医嘱'){
						if($('#temDocAdvType').combobox('getValue')==''){
							$.messager.alert('提示',"请先选择医嘱类型！");
							return;
						}
					}
			    	if(node.attributes.isOPen=="1"){
			    		AdddilogModel("stackModleDivId","【"+node.text+"】详情信息","<%=basePath%>inpatient/docAdvManage/viewStackInfo.action?inpatientOrder.id="+node.id,'80%','50%');
			    	}else{
			    		$(this).tree(node.state==='closed'?'expand':'collapse',node.target);
			    	}
			    }
			});
			$('#cc').layout('expand','west');//展开组套信息
    		$('#adProjectDivId').show();
    		$('#adWestMediDivId').show();
    		//长期医嘱类型下拉框
    		$('#longDocAdvType').combobox({
    			url:'<%=basePath%>inpatient/docAdvManage/queryDocAdvType.action', 
    			queryParams:{
    				'inpatientKind.fitExtent':2,
    				'inpatientKind.decmpsState':1
    			},
			    valueField:'typeCode',    
			    textField:'typeName',
			    multiple:false,
			    editable:false,
			    onSelect:function(data){
			    	$('#adProjectTdId').combobox('clear');
			    	$('#adProjectTdId').combobox('reload',"<%=basePath%>baseinfo/advice/querySystemTypesByTypeId.action?typeId="+data.typeCode);
			    }
    		});
    		//临时医嘱类型下拉框
    		$('#temDocAdvType').combobox({
    			url:'<%=basePath%>inpatient/docAdvManage/queryDocAdvType.action', 
    			queryParams:{
    				'inpatientKind.fitExtent':2,
    				'inpatientKind.decmpsState':0
    			},
			    valueField:'typeCode',    
			    textField:'typeName',
			    multiple:false,
			    editable:false,
			    onSelect:function(data){
			    	$('#adProjectTdId').combobox('clear');
			    	$('#adProjectTdId').combobox('reload',"<%=basePath%>baseinfo/advice/querySystemTypesByTypeId.action?typeId="+data.typeCode);
			    }
    		});
    		//系统类型下拉框
			$('#adProjectTdId') .combobox({ 
				url:'<%=basePath%>inpatient/doctorAdvice/likeSystemtype.action', 
			    valueField:'code',    
			    textField:'name',
			    multiple:false,
			    editable:false,
			    onSelect:function(data){
			    	var qq = $('#ttta').tabs('getSelected');				
					var tab = qq.panel('options');
					if(tab.title=='长期医嘱'){	
						if($('#longDocAdvType').combobox('getValue')==''){
							$.messager.alert('提示',"请先选择医嘱类型！");
							$('#adProjectTdId') .combobox('setValue','');
							return;
						}
					}else if(tab.title=='临时医嘱'){
						if($('#temDocAdvType').combobox('getValue')==''){
							$.messager.alert('提示',"请先选择医嘱类型！");
							$('#adProjectTdId') .combobox('setValue','');
							return;
						}
					}			
					var record = $('#adProjectTdId') .combobox('getValue');
					var recordName = $('#adProjectTdId') .combobox('getText');
			    	if(record=='07'){//类型为检查			    		
				    	$('#bodyParts').show();	
				    	$('#bodyPartss').show();	
				    	$('#sampleType').hide();	
				    	$('#sampleTypes').hide();
				    }else{
				    	$('#bodyParts').hide();	
				    	$('#bodyPartss').hide();
				    	$('#sampleType').show();	
				    	$('#sampleTypes').show();
				    }			    	
			    	if(record=='12'){//类型为转科				    		
			    		$('#adNotDrugInsTdId').combobox('disable');
			    		$('#adNotDrugSamTdId').combobox('disable');
				    }		
			    	if(record!='12'){		    		
			    		$('#adNotDrugInsTdId').combobox('enable');
			    		$('#adNotDrugSamTdId').combobox('enable');
				    }
			    	if(record=='17'||record=='18'){//中药/西药
			    		$('#adNotDrugDivId').hide();
			    		$('#adChinMediDivId').hide();
			    		$('#adWestMediDivId').show();
			    	}else if(record=='16'){
			    		$('#adNotDrugDivId').hide();
			    		$('#adWestMediDivId').hide();
			    		$('#adChinMediDivId').show();
			    	}else{
			    		$('#adChinMediDivId').hide();
			    		$('#adWestMediDivId').hide();
			    		$('#adNotDrugDivId').show();
			    	}	
			    	if($('#adProjectTdId').combobox('getValue')==''){
						$('#adProjectNameTdId').combogrid('disable');
					}else{
						$('#adProjectNameTdId').combogrid('enable');
					}
			    	var adProjectTdId = record;
			    	var projectName = recordName;
			    	ProjectName(adProjectTdId,projectName);
			    	clearProjectTextbox();//清空项目类别区域的textbox
			    }			    
		    });	
			enableProject();//启用项目类别区域的textbox
			clearProjectTextbox();//清空项目类别区域的textbox
	    	functionInfo();//添加功能事件	    	
			//数量数字输入框事件
			$('#adProjectNumTdId').numberspinner({
				onChange:function(newValue,oldValue){
					var qq = $('#ttta').tabs('getSelected');				
					var tab = qq.panel('options');
					if(tab.title=='长期医嘱'){	
						var indexA = getIndexForAdDgListA();
						if(indexA>=0){
							var retVal = newValue;
							if(retVal==null||retVal==''){
								return;
							}
							var row = $('#infolistA').datagrid('getSelected');
							if(row!=null&&row.combNo!=null&&row.combNo!=''){
								var rows = $('#infolistA').datagrid('getRows');				
								for(var i=0;i<rows.length;i++){
									if(rows[i].combNo==row.combNo){
										var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
										$('#infolistA').datagrid('updateRow',{
											index: indexRow,
											row: {
												qtyTot:parseInt(retVal),
												changeNo:1
											}
										});
									}
								}
							}else{	
								$('#infolistA').datagrid('updateRow',{
									index: indexA,
									row: {
										qtyTot:parseInt(retVal),
										changeNo:1
									}
								});								
							}							
						}
					}else if(tab.title=='临时医嘱'){
						var index = getIndexForAdDgList();
						if(index>=0){
							var retVal = newValue;
							if(retVal==null||retVal==''){
								return;
							}
							var row = $('#infolistB').datagrid('getSelected');
							if(row!=null&&row.combNo!=null&&row.combNo!=''){
								var rows = $('#infolistB').datagrid('getRows');
								for(var i=0;i<rows.length;i++){
									if(rows[i].combNo==row.combNo){
										var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
										$('#infolistB').datagrid('updateRow',{
											index: indexRow,
											row: {
												qtyTot:parseInt(retVal),
												changeNo:1
											}
										});
									}
								}
							}else{							
									$('#infolistB').datagrid('updateRow',{
										index: index,
										row: {
											qtyTot:parseInt(retVal),
											changeNo:1
										}
									});							
							}							
						}
					}					
				}
			});
			//每次用量
			$('#adWestMediDosMaxTdId').numberbox('textbox').bind('keyup',function(event){//西药,中成药-每次用量-单位
				var indexA = getIndexForAdDgListA();
    			var index = getIndexForAdDgList();
    			if(indexA>=0){
    				var curVal = $('#adWestMediDosMaxTdId').numberbox('getText');
	    			if(curVal>999){
	    				curVal=999;
	    			}
	    			if(curVal==null||curVal==''){
	    				curVal=0;
	    			}
	    			var row = $('#infolistA').datagrid('getSelected');
	    			var dosage = (curVal*parseFloat(row.baseDose)).toFixed(2);;
	    			var retVal = curVal+row.minUnit+"="+dosage+row.doseUnit;
	    			$('#adWestMediDosMinTdId').numberbox('setText',dosage);
	    			$('#infolistA').datagrid('updateRow',{
						index: indexA,
						row: {
							doseOnce:dosage,
							doseOnces:retVal,
							changeNo:1
						}
					});
    			}
    			if(index>=0){
    				var curVal = $('#adWestMediDosMaxTdId').numberbox('getText');
	    			if(curVal>999){
	    				curVal=999;
	    			}
	    			if(curVal==null||curVal==''){
	    				curVal=0;
	    			}
	    			var row = $('#infolistB').datagrid('getSelected');
	    			var dosage = (curVal*parseFloat(row.baseDose)).toFixed(2);;
	    			var retVal = curVal+row.minUnit+"="+dosage+row.doseUnit;
	    			$('#adWestMediDosMinTdId').numberbox('setText',dosage);
	    			$('#infolistB').datagrid('updateRow',{
						index: index,
						row: {
							doseOnce:dosage,
							doseOnces:retVal,
							changeNo:1
						}
					});
    			}
    		}); 
			//频次下拉框adChinMediFreTdId  (长期医嘱频次下拉)
			$('#adWestMediFreTdId') .combobox({    
			    url:'<%=basePath%>baseinfo/frequency/queryFrequencyGroup.action',    
			    valueField:'code',    
			    textField:'name',
			    multiple:false,
			    editable:false,
			    onSelect:function(record){
			    	if(record!=null&&record!=''){
				    	var qq = $('#ttta').tabs('getSelected');				
						var tab = qq.panel('options');
						if(tab.title=='长期医嘱'){
							var index = getIndexForAdDgListA();
							if(index>=0){
								var row = $('#infolistA').datagrid('getSelected');
								if(row!=null&&row.combNo!=null&&row.combNo!=''){
									var rows = $('#infolistA').datagrid('getRows');
									for(var i=0;i<rows.length;i++){
										if(rows[i].combNo==row.combNo){
											var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
											$('#infolistA').datagrid('updateRow',{
												index: indexRow,
												row: {
													frequencyCode:record.code,
													frequencyName:record.name,
													changeNo:1
												}
											});
										}
									}
								}else{								
									$('#infolistA').datagrid('updateRow',{
										index: index,
										row: {
											frequencyCode:record.code,
											frequencyName:record.name,
											changeNo:1
										}
									});
								}
							}
						}else if(tab.title=='临时医嘱'){
							var index = getIndexForAdDgList();
							if(index>=0){
								var row = $('#infolistB').datagrid('getSelected');
								if(row!=null&&row.combNo!=null&&row.combNo!=''){
									var rows = $('#infolistB').datagrid('getRows');
									for(var i=0;i<rows.length;i++){
										if(rows[i].combNo==row.combNo){
											var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
											$('#infolistB').datagrid('updateRow',{
												index: indexRow,
												row: {
													frequencyCode:record.code,
													frequencyName:record.name,
													changeNo:1
												}
											});
										}
									}
								}else{								
									$('#infolistB').datagrid('updateRow',{
										index: index,
										row: {
											frequencyCode:record.code,
											frequencyName:record.name,
											changeNo:1
										}
									});
								}
							}
						}
			    	}
	   		    }
		    });
			
			/**
			* 绑定频次回车事件
			* @author  zhuxiaolu
			* @date 2016-03-22 14:30   
			* @version 1.0
			*/
			bindEnterEvent('adWestMediFreTdId',popWinToBusinessFrequency,'easyui');
			//用法下拉框
			$('#adWestMediUsaTdId') .combobox({    
			    url:'<%=basePath%>inpatient/doctorAdvice/likeUseage.action',    
			    valueField:'encode',    
			    textField:'name',
			    multiple:false,
			    editable:false,
			    onSelect:function(record){
					if(record!=null&&record!=''){
				    	var qq = $('#ttta').tabs('getSelected');				
						var tab = qq.panel('options');
						if(tab.title=='长期医嘱'){
							var index = getIndexForAdDgListA();
							if(index>=0){
								var row = $('#infolistA').datagrid('getSelected');
								if(row!=null&&row.combNo!=null&&row.combNo!=''){
									var rows = $('#infolistA').datagrid('getRows');
									for(var i=0;i<rows.length;i++){
										if(rows[i].combNo==row.combNo){
											var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
											$('#infolistA').datagrid('updateRow',{
												index: indexRow,
												row: {
													usageCode:record.encode,
													useName:record.name,
													changeNo:1
												}
											});
										}
									}
								}else{								
									$('#infolistA').datagrid('updateRow',{
										index: index,
										row: {
											usageCode:record.encode,
											useName:record.name,
											changeNo:1
										}
									});
								}
							}
						}else if(tab.title=='临时医嘱'){
							var index = getIndexForAdDgList();
							if(index>=0){
								var row = $('#infolistB').datagrid('getSelected');
								if(row!=null&&row.combNo!=null&&row.combNo!=''){
									var rows = $('#infolistB').datagrid('getRows');
									for(var i=0;i<rows.length;i++){
										if(rows[i].combNo==row.combNo){
											var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
											$('#infolistB').datagrid('updateRow',{
												index: indexRow,
												row: {
													usageCode:record.encode,
													useName:record.name,
													changeNo:1
												}
											});
										}
									}
								}else{								
									$('#infolistB').datagrid('updateRow',{
										index: index,
										row: {
											usageCode:record.encode,
											useName:record.name,
											changeNo:1
										}
									});
								}
							}
						}
			    	}
	   		    }
		    });
			
			/**
			* 绑定用法回车事件
			* @author  zhuxiaolu
			* @date 2016-03-22 14:30   
			* @version 1.0
			*/
			bindEnterEvent('adWestMediUsaTdId',popWinToUsa,'easyui');
			//西药中药备注下拉框
			$('#adWestMediRemTdId') .combobox({    
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=opendocadvmark'/>",
			    valueField:'encode',    
			    textField:'name',
			    multiple:false,
			    editable:true,
			    onSelect:function(record){		    	
			    	if(record!=null&&record!=''){
				    	var qq = $('#ttta').tabs('getSelected');				
						var tab = qq.panel('options');
						if(tab.title=='长期医嘱'){
							var index=getIndexForAdDgListA();
		   		    		if(index>=0){	
								$('#infolistA').datagrid('updateRow',{
								index: index,
									row: {								
										moNote2:record.name,
										changeNo:1
									}
								});
							}
						}else if(tab.title=='临时医嘱'){
							var index = getIndexForAdDgList();			   		    	
							if(index>=0){							
								$('#infolistB').datagrid('updateRow',{
								index: index,
									row: {									
										moNote2:record.name,
										changeNo:1
									}
								});
							}
						}
			    	}	   		    	
	   		    }
		    });	
			
			/**
			* 绑定西药中药备注回车事件
			* @author  zhuxiaolu
			* @date 2016-03-22 14:30   
			* @version 1.0
			*/
			bindEnterEvent('adWestMediRemTdId',popWinToCodeOpendocadvmark,'easyui');
			//西药中药备注可编辑
	    	$('#adWestMediRemTdId').combobox('textbox').bind('keyup',function(event){
	    		var retval = $('#adWestMediRemTdId').combobox('getText');
	    		var qq = $('#ttta').tabs('getSelected');				
				var tab = qq.panel('options');
				if(tab.title=='长期医嘱'){
					var indexA = getIndexForAdDgListA();
					if(indexA>=0){
						$('#infolistA').datagrid('updateRow',{
							index: indexA,
							row: {
								moNote2:retval,
								changeNo:1
							}
						});
					}
				}else if(tab.title=='临时医嘱'){
					var index = getIndexForAdDgList();	    		
		    		if(index>=0){
						$('#infolistB').datagrid('updateRow',{
							index: index,
							row: {
								moNote2:retval,
								changeNo:1
							}
						});
					}
				}	    			    		
			});
			//非药品-部位检体
			$('#adNotDrugInsTdId').combobox({
				url: "<%=basePath%>inpatient/docAdvManage/queryCheckpoint.action",	
	    		disabled:false,
	    		editable:false,
	    		valueField:'id',    
				textField:'name',
				onSelect:function(record){
					if(record!=null&&record!=''){
				    	var qq = $('#ttta').tabs('getSelected');				
						var tab = qq.panel('options');
						if(tab.title=='长期医嘱'){
							var indexA = getIndexForAdDgListA();
							if(indexA>=0){
								$('#infolistA').datagrid('updateRow',{
									index: indexA,
									row: {
										itemNote:record.id,
										changeNo:1
									}
								});
							}  
						}else if(tab.title=='临时医嘱'){
							var index = getIndexForAdDgList();		  		    	
							if(index>=0){
								$('#infolistB').datagrid('updateRow',{
									index: index,
									row: {
										itemNote:record.id,
										changeNo:1
									}
								});
							}
						}
					}					
    		    }
	    	});
			 //检验-样本类型 
			   $('#adNotDrugSamTdId').combobox({
				    url: "<%=basePath%>inpatient/docAdvManage/querySampleTept.action", 
		    		disabled:false,
		    		editable:false,
		    		valueField:'encode',    
					textField:'name',
					onSelect:function(record){
						if(record!=null&&record!=''){
					    	var qq = $('#ttta').tabs('getSelected');				
							var tab = qq.panel('options');
							if(tab.title=='长期医嘱'){
								var index = getIndexForAdDgListA();
								if(index>=0){
									var row = $('#infolistA').datagrid('getSelected');
									if(row!=null&&row.combNo!=null&&row.combNo!=''){
										var rows = $('#infolistA').datagrid('getRows');
										for(var i=0;i<rows.length;i++){
											if(rows[i].combNo==row.combNo){
												var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
												$('#infolistA').datagrid('updateRow',{
													index: indexRow,
													row: {
														labCode:record.encode,
														changeNo:1
													}
												});
											}
										}
									}else{									
										$('#infolistA').datagrid('updateRow',{
											index: index,
											row: {
												labCode:record.encode,
												changeNo:1
											}
										});
									}
								}
							}else if(tab.title=='临时医嘱'){
								var index = getIndexForAdDgList();
								if(index>=0){
									var row = $('#infolistB').datagrid('getSelected');
									if(row!=null&&row.combNo!=null&&row.combNo!=''){
										var rows = $('#infolistB').datagrid('getRows');
										for(var i=0;i<rows.length;i++){
											if(rows[i].combNo==row.combNo){
												var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
												$('#infolistB').datagrid('updateRow',{
													index: indexRow,
													row: {
														labCode:record.encode,
														changeNo:1
													}
												});
											}
										}
									}else{									
										$('#infolistB').datagrid('updateRow',{
											index: index,
											row: {
												labCode:record.encode,
												changeNo:1
											}
										});
									}
								}
							}
				    	}
		   		    }
		    	});
				//非药品备注下拉框
				$('#adNotDrugRemTdId') .combobox({    
					url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=opendocadvmark'/>",
				    valueField:'encode',    
				    textField:'name',
				    multiple:false,
				    editable:true,
				    onSelect:function(record){
				    	if(record!=null&&record!=''){
					    	var qq = $('#ttta').tabs('getSelected');				
							var tab = qq.panel('options');
							if(tab.title=='长期医嘱'){
								var indexA=getIndexForAdDgListA();
			   		    		if(indexA>=0){							
									$('#infolistA').datagrid('updateRow',{
									index: indexA,
										row: {								
											moNote2:record.name,
											changeNo:1
										}
									});
								}
							}else if(tab.title=='临时医嘱'){
								var index = getIndexForAdDgList();				   		    	
								if(index>=0){							
									$('#infolistB').datagrid('updateRow',{
									index: index,
										row: {									
											moNote2:record.name,
											changeNo:1
										}
									});
								}
							}
				    	}			   		    	
		   		    }
			    });	
				
				/**
				* 绑定非药品备注回车事件
				* @author  zhuxiaolu
				* @date 2016-03-22 14:30   
				* @version 1.0
				*/
				bindEnterEvent('adNotDrugRemTdId',popWinToNotDrugRem,'easyui');//绑定回车事件
				//非药品备注可编辑
		    	$('#adNotDrugRemTdId').combobox('textbox').bind('keyup',function(event){
		    		var retval = $('#adNotDrugRemTdId').combobox('getText');
		    		var qq = $('#ttta').tabs('getSelected');				
					var tab = qq.panel('options');
					if(tab.title=='长期医嘱'){
						var indexA = getIndexForAdDgListA();
						if(indexA>=0){
							$('#infolistA').datagrid('updateRow',{
								index: indexA,
								row: {
									moNote2:retval,
									changeNo:1
								}
							});
						}
					}else if(tab.title=='临时医嘱'){
						var index = getIndexForAdDgList();			    		
			    		if(index>=0){
							$('#infolistB').datagrid('updateRow',{
								index: index,
								row: {
									moNote2:retval,
									changeNo:1
								}
							});
						}
					}		    				    		
				});
		    	//频次下拉框
				$('#adChinMediFreTdId') .combobox({    
					url:'<%=basePath%>baseinfo/frequency/queryFrequencyGroup.action',    
				    valueField:'code',    
				    textField:'name',
				    multiple:false,
				    editable:false,
				    onSelect:function(record){
				    	if(record!=null&&record!=''){
					    	var qq = $('#ttta').tabs('getSelected');				
							var tab = qq.panel('options');
							if(tab.title=='长期医嘱'){
								var index = getIndexForAdDgListA();
								if(index>=0){
									var row = $('#infolistA').datagrid('getSelected');
									if(row!=null&&row.combNo!=null&&row.combNo!=''){
										var rows = $('#infolistA').datagrid('getRows');
										for(var i=0;i<rows.length;i++){
											if(rows[i].combNo==row.combNo){
												var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
												$('#infolistA').datagrid('updateRow',{
													index: indexRow,
													row: {
														frequencyCode:record.code,
														frequencyName:record.name,
														changeNo:1
													}
												});
											}
										}
									}else{								
										$('#infolistA').datagrid('updateRow',{
											index: index,
											row: {
												frequencyCode:record.code,
												frequencyName:record.name,
												changeNo:1
											}
										});
									}
								}
							}else if(tab.title=='临时医嘱'){
								var index = getIndexForAdDgList();
								if(index>=0){
									var row = $('#infolistB').datagrid('getSelected');
									if(row!=null&&row.combNo!=null&&row.combNo!=''){
										var rows = $('#infolistB').datagrid('getRows');
										for(var i=0;i<rows.length;i++){
											if(rows[i].combNo==row.combNo){
												var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
												$('#infolistB').datagrid('updateRow',{
													index: indexRow,
													row: {
														frequencyCode:record.code,
														frequencyName:record.name,
														changeNo:1
													}
												});
											}
										}
									}else{								
										$('#infolistB').datagrid('updateRow',{
											index: index,
											row: {
												frequencyCode:record.code,
												frequencyName:record.name,
												changeNo:1
											}
										});
									}
								}
							}
				    	}
		   		    }
			    });
		    	$('#adChinMediNumTdId').numberbox('textbox').bind('keyup',function(event){//草药-付数
		    		var qq = $('#ttta').tabs('getSelected');				
					var tab = qq.panel('options');
					if(tab.title=='长期医嘱'){	
						var index = getIndexForAdDgListA();
		    			if(index>=0){
		    				var retVal = $('#adChinMediNumTdId').numberbox('getText');
			    			if(retVal>999){
			    				retVal=999;
			    			}
			    			if(retVal==null||retVal==''){
			    				retVal=0;
			    			}
			    			var row = $('#infolistA').datagrid('getSelected');
			    			var doseOnce = row.doseOnce;			    			
			    			if(row.doseOnce==''||row.doseOnce==null||row.doseOnce=='undefined'){			    				
			    				doseOnce = 1;
			    			}
			    			var tot = parseInt(retVal)*parseInt(doseOnce);
							if(row!=null&&row.combNo!=null&&row.combNo!=''){
								var rows = $('#infolistA').datagrid('getRows');
								for(var i=0;i<rows.length;i++){
									if(rows[i].combNo==row.combNo){
										var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
										$('#infolistA').datagrid('updateRow',{
											index: indexRow,
											row: {
												useDays:parseInt(retVal),
												qtyTot:parseInt(tot),
												changeNo:1
											}
										});
									}
								}
							}else{								
									$('#infolistA').datagrid('updateRow',{
										index: index,
										row: {
											useDays:parseInt(retVal),
											qtyTot:parseInt(tot),
											changeNo:1
										}
									});								
							}
			    			$('#adChinMediNumTdId').numberbox('setText',parseInt(retVal));
		    			}
					}else if(tab.title=='临时医嘱'){
						var index = getIndexForAdDgList();
		    			if(index>=0){
		    				var retVal = $('#adChinMediNumTdId').numberbox('getText');
			    			if(retVal>999){
			    				retVal=999;
			    			}
			    			if(retVal==null||retVal==''){
			    				retVal=0;
			    			}
			    			var row = $('#infolistB').datagrid('getSelected');
			    			var doseOnce = row.doseOnce;			    			
			    			if(row.doseOnce==''||row.doseOnce==null||row.doseOnce=='undefined'){			    				
			    				doseOnce = 1;
			    			}
			    			var tot = parseInt(retVal)*parseInt(doseOnce);
							if(row!=null&&row.combNo!=null&&row.combNo!=''){
								var rows = $('#infolistB').datagrid('getRows');
								for(var i=0;i<rows.length;i++){
									if(rows[i].combNo==row.combNo){
										var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
										$('#infolistB').datagrid('updateRow',{
											index: indexRow,
											row: {
												useDays:parseInt(retVal),
												qtyTot:parseInt(tot),
												changeNo:1
											}
										});
									}
								}
							}else{
								
									$('#infolistB').datagrid('updateRow',{
										index: index,
										row: {
											useDays:parseInt(retVal),
											qtyTot:parseInt(tot),
											changeNo:1
										}
									});
								
							}
			    			$('#adChinMediNumTdId').numberbox('setText',parseInt(retVal));
		    			}
					}	    			
	    		});
		    	$('#adChinMediUsaTdId').combobox({//中草药-用法
		    		url:'<%=basePath%>inpatient/doctorAdvice/likeUseage.action',  
		    		disabled:false,
		    		editable:false,
		    		valueField:'encode',    
					textField:'name',
					onSelect:function(record){
						if(record!=null&&record!=''){
							var qq = $('#ttta').tabs('getSelected');				
							var tab = qq.panel('options');
							if(tab.title=='长期医嘱'){	
								var index = getIndexForAdDgListA();
								if(index>=0){
				    		    	var row = $('#infolistA').datagrid('getSelected');
									if(row!=null&&row.combNo!=null&&row.combNo!=''){
										var rows = $('#infolistA').datagrid('getRows');
										for(var i=0;i<rows.length;i++){
											if(rows[i].combNo==row.combNo){
												var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
												$('#infolistA').datagrid('updateRow',{
													index: indexRow,
													row: {
														usageCode:record.encode,
														useName:record.name,
														changeNo:1
													}
												});
											}
										}
									}else{									
										$('#infolistA').datagrid('updateRow',{
											index: index,
											row: {
												usageCode:record.encode,
												useName:record.name,
												changeNo:1
											}
										});
									}
								}
							}else if(tab.title=='临时医嘱'){
								var index = getIndexForAdDgList();
								if(index>=0){
				    		    	var row = $('#infolistB').datagrid('getSelected');
									if(row!=null&&row.combNo!=null&&row.combNo!=''){
										var rows = $('#infolistB').datagrid('getRows');
										for(var i=0;i<rows.length;i++){
											if(rows[i].combNo==row.combNo){
												var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
												$('#infolistB').datagrid('updateRow',{
													index: indexRow,
													row: {
														usageCode:record.encode,
														useName:record.name,
														changeNo:1
													}
												});
											}
										}
									}else{									
										$('#infolistB').datagrid('updateRow',{
											index: index,
											row: {
												usageCode:record.encode,
												useName:record.name,
												changeNo:1
											}
										});
									}
								}
							}							
						}
	    		    }
		    	});
		    	
		    	/**
				* 绑定中草药-用法回车事件
				* @author  zhuxiaolu
				* @date 2016-03-22 14:30   
				* @version 1.0
				*/
		    	bindEnterEvent('adChinMediUsaTdId',popWinToUsaChin,'easyui');
		    	$('#adChinMediRemTdId').combobox({//中草药-备注
		    		url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=opendocadvmark'/>",  
		    		editable:true,
		    		valueField:'encode',    
					textField:'name',
					onSelect:function(record){
						if(record!=null&&record!=''){
							var qq = $('#ttta').tabs('getSelected');				
							var tab = qq.panel('options');
							if(tab.title=='长期医嘱'){	
								var index = getIndexForAdDgListA();			    		    
								if(index>=0){
									$('#infolistA').datagrid('updateRow',{
										index: index,
										row: {
											moNote2:record.name,
											changeNo:1
										}
									});
								}							
							}else if(tab.title=='临时医嘱'){
								var index = getIndexForAdDgList();			    		    	
								if(index>=0){
									$('#infolistB').datagrid('updateRow',{
										index: index,
										row: {
											moNote2:record.name,
											changeNo:1
										}
									});
								}								
							}							
						}
	    		    }
		    	});
		    	/**
				* 绑定中草药-备注回车事件
				* @author  zhuxiaolu
				* @date 2016-03-22 14:30   
				* @version 1.0
				*/
		    	bindEnterEvent('adChinMediRemTdId',popWinToChinMediRemTd,'easyui');
		    	$('#adChinMediRemTdId').combobox('textbox').bind('keyup',function(event){//中草药-备注
		    		var retval = $('#adChinMediRemTdId').combobox('getText');
		    		var qq = $('#ttta').tabs('getSelected');				
					var tab = qq.panel('options');
					if(tab.title=='长期医嘱'){	
						var index = getIndexForAdDgListA();			    		
						if(index>=0){
							$('#infolistA').datagrid('updateRow',{
								index: index,
								row: {
									moNote2:retval,
									changeNo:1
								}
							});
						}						
					}else if(tab.title=='临时医嘱'){
						var index = getIndexForAdDgList();
						if(index>=0){
							$('#infolistB').datagrid('updateRow',{
								index: index,
								row: {
									moNote2:retval,
									changeNo:1
								}
							});
						}						
					}		    		
				});
			//获取当前时间
			function nowTime()
				{
				   var day=new Date();
				   var Year=0;
				   var Month=0;
				   var Day=0;
				   var Hour = 0;
				   var Minute = 0;
				   var Second = 0;
				   var CurrentDate="";				
				   //初始化时间
				   Year       = day.getFullYear();
				   Month      = day.getMonth()+1;
				   Day        = day.getDate();
				   Hour       = day.getHours();
				   Minute     = day.getMinutes();
				   Second     = day.getSeconds();
				   CurrentDate =  Year + "-";
				   if (Month >= 10 )
				   {
				    CurrentDate = CurrentDate + Month + "-";
				   }else{
				    CurrentDate = CurrentDate + "0" + Month + "-";
				   }
				   if (Day >= 10 )
				   {
				    CurrentDate = CurrentDate + Day ;
				   }else{
				    CurrentDate = CurrentDate + "0" + Day ;
				   }
				   if(Hour >=10)
				   {
				    CurrentDate = CurrentDate + " " + Hour ;
				   }else{
				    CurrentDate = CurrentDate + " " + "0" + Hour ;
				   }
				   if(Minute >=10)
				   {
				    CurrentDate = CurrentDate + ":" + Minute ;
				   }else{
				    CurrentDate = CurrentDate + ":0" + Minute ;
				   }      
				   if(Second>=10)
				   {
				    CurrentDate = CurrentDate + ":" + Second;
				   }else{
				    CurrentDate = CurrentDate + ":0" + Second;
				   }
				  
				   return CurrentDate;
				}
			//开始时间
			$("#startTime").datetimebox({			
				onChange:function(newdate,olddate){
					var endTime=$('#endTime').datetimebox('getValue');
					if(newdate!='' && endTime!=''){
						var sDate = new Date(newdate.replace(/\-/g, "\/"));  
						var eDate = new Date(endTime.replace(/\-/g, "\/"));  
						if(sDate>eDate){						
							$.messager.alert('操作提示', '开始时间不能大于结束时间！');
							return;
						}
					}
					if(newdate==null ||newdate ==''){
						var qq = $('#ttta').tabs('getSelected');				
						var tab = qq.panel('options');
						if(tab.title=='长期医嘱'){
							var row = $('#infolistA').datagrid('getSelected')
							newdate=row.dateBgn;
						}else if(tab.title=='临时医嘱'){
							var row = $('#infolistB').datagrid('getSelected')
							newdate=row.dateBgn;
						}						
					}					
	   		    	var index = getIndexForAdDgList();
	   		    	var indexA=getIndexForAdDgListA();
	   		    		if(indexA>=0){							
							$('#infolistA').datagrid('updateRow',{
								index: indexA,
								row: {									
									dateBgn:newdate,
									changeNo:1
								}
							});
						}
						if(index>=0){
							$('#infolistB').datagrid('updateRow',{
								index: index,
								row: {									
									dateBgn:newdate,
									changeNo:1
								}
							});
						}
	   		    }
			});
			//结束时间
			$("#endTime").datetimebox({			
				onChange:function(newdate,olddate){
					var beginTime=$('#startTime').datetimebox('getValue');
					if(beginTime!='' && newdate!=''){
						var sDate = new Date(beginTime.replace(/\-/g, "\/"));  
						var eDate = new Date(newdate.replace(/\-/g, "\/"));  
						if(sDate>eDate){
							$.messager.alert('操作提示', '开始时间不能大于结束时间！');							
							return;
						}
					}
					if(newdate==null ||newdate ==''){
						var qq = $('#ttta').tabs('getSelected');				
						var tab = qq.panel('options');
						if(tab.title=='长期医嘱'){
							var row = $('#infolistA').datagrid('getSelected')
							newdate=row.dateEnd;
						}else if(tab.title=='临时医嘱'){
							var row = $('#infolistB').datagrid('getSelected')
							newdate=row.dateEnd;
						}						
					}
	   		    	var index = getIndexForAdDgList();
	   		    	var indexA=getIndexForAdDgListA();
	   		    		if(indexA>=0){							
							$('#infolistA').datagrid('updateRow',{
								index: indexA,
								row: {									
									dateEnd:newdate,
									changeNo:1
								}
							});
						}
						if(index>=0){
							$('#infolistB').datagrid('updateRow',{
								index: index,
								row: {									
									dateEnd:newdate,
									changeNo:1
								}
							});
						}
	   		    }
			});			
		}
		//启用项目类别区域的textbox
		function enableProject(){
			$('#adProjectNameTdId').combogrid('disable');						
			$('#adProjectNumTdId').numberspinner('enable');
		}
		//清空项目类别区域的textbox
		function clearProjectTextbox(){
			$('#adProjectNameTdId').textbox('clear');
			$('#adProjectNumTdId').numberspinner('clear');
			$('#adProjectUnitTdId').combobox('clear');
			$('#adNotDrugInsTdId').combobox('clear');
			$('#adNotDrugSamTdId').combobox('clear');
			$('#adExeDeptTdId').textbox('clear');
			$('#startTime').datetimebox('clear');
			$('#endTime').datetimebox('clear');		
			$('#adWestMediFreTdId').combobox('clear');
			$('#adWestMediUsaTdId').combobox('clear'); 
			$('#adWestMediDosMaxTdId').numberbox('clear');
			$('#adWestMediDosMinTdId').numberbox('clear');
			$('#adWestMediSkiTdId').combobox('clear');
			$('#adChinMediFreTdId').combobox('clear');
			$('#adChinMediUsaTdId').combobox('clear');
			$('#adChinMediNumTdId').numberbox('clear');
			$('#adChinMediRemTdId').combobox('clear');
		}
		
		

		/**
		 * 保存表格新增医嘱数据
		 * @author  ygq
		 * @param title 标签名称
		 * @param title 跳转路径
		 * @date 2015-12-29
		 * @version 1.0
		 */		 
		function adSaveAdvice(data){				
			
			var qq = $('#ttta').tabs('getSelected');				
			var tab = qq.panel('options');
			if(tab.title=='长期医嘱'){		
				 var rows = '';
				 if(data!=null&&data.length>0){
					 rows = data;
				 }else{
					 rows = $('#infolistA').datagrid('getRows');
					 if(rows.length == 0){
						 $.messager.alert('提示','尚未添加任何医嘱内容，请添加后保存');
						 return ;
					 }
					 var array = new Array();
					 for(var i=0;i<rows.length;i++){
						 if(rows[i].changeNo==1){
							 array.push(rows[i]);
						 }
					 }
					rows = array;
				 }	
				 var name='';
				 for(var i =0;i<rows.length;i++){
					 if((rows[i].doseOnce/rows[i].baseDose).toFixed(1)==0.0){
						 if(name==''){
							 name = rows[i].itemName;
						 }else{
							 name = name+','+rows[i].itemName;
						 }						 
					 }					 				 
						 if(rows[i].id==null){
							 rows[i].id='';
						 }
						 if(rows[i].className==null){
							 rows[i].className='';
						 }
						 if(rows[i].combNo== null){
							 rows[i].combNo='';						 						 
						 }						 
						 if(rows[i].qtyTot== null){
							 rows[i].qtyTot='';						 						 
						 }
						 if(rows[i].packQty== null){
							 rows[i].packQty='';						 						 
						 }
						 if(rows[i].specs== null){
							 rows[i].specs='';						 						 
						 }
						 if(rows[i].drugpackagingUnit== null){
							 rows[i].drugpackagingUnit='';						 						 
						 }						 
						 if(rows[i].doseOnce== null){
							 rows[i].doseOnce='';						 						 
						 }					 
						 if(rows[i].priceUnit== null){
							 rows[i].priceUnit='';						 						 
						 }						 
						 if(rows[i].doseOnces== null){
							 rows[i].doseOnces='';						 						 
						 }
						 if(rows[i].doseUnit== null){
							 rows[i].doseUnit='';						 						 
						 }
						 if(rows[i].useDays== null){
							 rows[i].useDays='';						 						 
						 }						 
						 if(rows[i].itemType== '1'){
							 if(rows[i].priceUnit==rows[i].drugpackagingUnit&&rows[i].priceUnit!=''){
								 if(rows[i].doseOnce!=''&&rows[i].baseDose!=''&&rows[i].qtyTot!=''&&rows[i].packQty){
									 if((rows[i].doseOnce/rows[i].baseDose).toFixed(1)>rows[i].qtyTot*rows[i].packQty){
										 $.messager.alert('提示',rows[i].itemName+'开立数量小于每次剂量，不能开立医嘱!');		
										 return;
									 }
								 }								 
							 }else{
								 if(rows[i].doseOnce!=''&&rows[i].baseDose!=''&&rows[i].qtyTot!=''&&rows[i].packQty){
									 if((rows[i].doseOnce/rows[i].baseDose).toFixed(1)>rows[i].qtyTot){
										 $.messager.alert('提示',rows[i].itemName+'开立数量小于每次剂量，不能开立医嘱!');		
										 return;
									 }
								 }								 
							 }
						    if(rows[i].classCode=='16'||rows[i].classCode=='17'||rows[i].classCode=='18'){//非药品不校验频次 hedong  2016-09-09  页面都无此输入项
							  if(rows[i].frequencyCode== null||rows[i].frequencyCode==''){
								 $.messager.alert('提示',rows[i].itemName+'频次为空，不能开立医嘱!');		
								 return;						 						 
							   } 
						     }
							 
							 if(rows[i].frequencyName== null){
								 rows[i].frequencyName='';						 						 
							 }
							 if(rows[i].classCode=='16'||rows[i].classCode=='17'||rows[i].classCode=='18'){//非药品不校验用法  hedong  2016-09-09  页面都无此输入项
								 if(rows[i].usageCode== null||rows[i].usageCode==''){
									 $.messager.alert('提示',rows[i].itemName+'用法为空，不能开立医嘱!');		
									 return;
								 }
							 }
							 
							 if(rows[i].useName== null){
								 rows[i].useName='';						 						 
							 }
							 if(rows[i].doseModelCode== null||rows[i].doseModelCode==''){
								 $.messager.alert('提示',rows[i].itemName+'剂型为空，不能开立医嘱!');		
								 return;
							 }
							 if(rows[i].drugQuality== null||rows[i].drugQuality==''){
								 $.messager.alert('提示',rows[i].itemName+'性质为空，不能开立医嘱!');		
								 return;
							 }
							 if(rows[i].typeCode== null||rows[i].typeCode==''){
								 $.messager.alert('提示',rows[i].itemName+'医嘱类型为空，不能开立医嘱!');		
								 return;
							 }
							 if(rows[i].classCode== null||rows[i].classCode==''){
								 $.messager.alert('提示',rows[i].itemName+'系统类型为空，不能开立医嘱!');		
								 return;
							 }
							 if(rows[i].baseDose== null||rows[i].baseDose==''){
								 $.messager.alert('提示',rows[i].itemName+'基本剂量为空，不能开立医嘱!');
								 return;						 						 
							 }
						 }else{
							 if(rows[i].frequencyCode== null){
								 rows[i].frequencyCode='';						 						 
							 }
							 if(rows[i].frequencyName== null){
								 rows[i].frequencyName='';						 						 
							 }
							 if(rows[i].usageCode== null){
								 rows[i].usageCode='';						 						 
							 }
							 if(rows[i].useName== null){
								 rows[i].useName='';						 						 
							 }
							 if(rows[i].baseDose== null){
								 rows[i].baseDose=='';						 						 
							 }
						 }
						 if(rows[i].execDpcd== null){
							 rows[i].execDpcd='';						 						 
						 }
						 if(rows[i].execDpnm== null){
							 rows[i].execDpnm='';						 						 
						 }
						 if(rows[i].emcFlag== null){
							 rows[i].emcFlag='';						 						 
						 }
						 if(rows[i].labCode== null){
							 rows[i].labCode='';						 						 
						 }
						 if(rows[i].itemNote== null){
							 rows[i].itemNote='';						 						 
						 }
						 if(rows[i].moNote2== null){
							 rows[i].moNote2='';						 						 
						 }
						 if(rows[i].hypotest== null){
							 rows[i].hypotest='';						 						 
						 }
						 if(rows[i].dateBgn== null){
							 rows[i].dateBgn='';						 						 
						 }
						 if(rows[i].dateEnd== null){
							 rows[i].dateEnd='';						 						 
						 }
						 if(rows[i].permission== null){
							 rows[i].permission='';						 						 
						 }
						 if(rows[i].execTimes== null){
							 rows[i].execTimes='';						 						 
						 }	
						 if(rows[i].execDose== null){
							 rows[i].execDose='';						 						 
						 }	
						 if(rows[i].docName== null){
							 rows[i].docName='';						 						 
						 } 	
						 if(rows[i].combFlag== null){
							 rows[i].combFlag='';						 						 
						 } 
				 }
				 var inpatientNo = $("#inpatientNo").val();
				 var patientNo =$("#patientNo").val();
				 var deptCode = $("#deptCode").val();
				 var nurseCellCode = $("#nurseCellCode").val();
				 var babyFlag =$("#babyFlag").val();
				 var decmpsState = 1;
				
				 if(rows[0].typeCode!='CZ'&&rows[0].typeCode!='ZC'){
					 decmpsState = 0;
				 }
				 var str = encodeURI(JSON.stringify(rows)); 
				 if(name!=''){
					 $.extend($.messager.defaults,{  
					        ok:"是",  
					        cancel:"否"  
					 });  
					jQuery.messager.confirm('确认信息',name+'剂量太小,确认继续开立吗?',function(e){   
						if(e){ 
							$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
							$.ajax({
								url: '<%=basePath%>inpatient/docAdvManage/saveInpatientOrder.action',
								data:'str='+str+'&inpatientNo='+inpatientNo+'&patientNo='+patientNo+'&deptCode='+deptCode+'&nurseCellCode='+nurseCellCode+'&babyFlag='+babyFlag+'&decmpsState='+decmpsState,
								type:'post',
								success: function(docadvdata) {	
									$.messager.progress('close');	// 如果提交成功则隐藏进度条
									var dataMap = eval("("+docadvdata+")");
									$.extend($.messager.defaults,{  
								        ok:"确定"
								    });  
						   			if(dataMap.resMsg=="error"){
						   				$.messager.alert('提示',dataMap.resCode);			   				
						        		
						        	}else if(dataMap.resMsg=="success"){
						        		$.messager.alert('提示',dataMap.resCode);	
						        		$('#infolistA').datagrid('reload');
						        	}else{
						        		$.messager.alert('提示','未知错误,请联系管理员!');
						        	
						        	}
								}
							});	
						}
					});
				 }else{		
					 $.messager.progress({text:'医嘱保存中，请稍后...',modal:true});	// 显示进度条
					 $.ajax({
							url: '<%=basePath%>inpatient/docAdvManage/saveInpatientOrder.action',
							data:'str='+str+'&inpatientNo='+inpatientNo+'&patientNo='+patientNo+'&deptCode='+deptCode+'&nurseCellCode='+nurseCellCode+'&babyFlag='+babyFlag+'&decmpsState='+decmpsState,
							type:'post',
							success: function(docadvdata) {	
								$.messager.progress('close');	// 如果提交成功则隐藏进度条
								var dataMap = eval("("+docadvdata+")");
								$.extend($.messager.defaults,{  
							        ok:"确定"
							    });  
					   			if(dataMap.resMsg=="error"){
					   				$.messager.alert('提示',dataMap.resCode);			   				
					        		
					        	}else if(dataMap.resMsg=="success"){
					        		$.messager.alert('提示',dataMap.resCode);	
					        		$('#infolistA').datagrid('reload');
					        	}else{
					        		$.messager.alert('提示','未知错误,请联系管理员!');
					        	
					        	}
							}
						});	 
				 }				 
			 }
			 else if (tab.title=='临时医嘱'){  				
				 var rows = '';
				 if(data!=null&&data.length>0){
					 rows = data;
				 }else{
					 rows = $('#infolistB').datagrid('getRows');
					 if(rows.length == 0){
						 $.messager.alert('提示','尚未添加任何医嘱内容，请添加后保存');	
						 return ;
					 }
					 var array = new Array();
					 for(var i=0;i<rows.length;i++){
						 if(rows[i].changeNo==1){
							 array.push(rows[i]);
						 }
					 }
					rows = array;	
				 }				
				 var name='';
				 for(var i =0;i<rows.length;i++){
					 if((rows[i].doseOnce/rows[i].baseDose).toFixed(1)==0.0){
						 if(name==''){
							 name = rows[i].itemName;
						 }else{
							 name = name+','+rows[i].itemName;
						 }						 
					 }
					 if(rows[i].priceUnit==rows[i].drugpackagingUnit){
						 if((rows[i].doseOnce/rows[i].baseDose).toFixed(1)>rows[i].qtyTot*rows[i].packQty){
							 $.messager.alert('提示',rows[i].itemName+'开立数量小于每次剂量，不能开立医嘱!');		
							 return;
						 }
					 }else{
						 if((rows[i].doseOnce/rows[i].baseDose).toFixed(1)>rows[i].qtyTot){
							 $.messager.alert('提示',rows[i].itemName+'开立数量小于每次剂量，不能开立医嘱!');		
							 return;
						 }
					 }	
						 if(rows[i].id==null){
							 rows[i].id='';
						 }
						 if(rows[i].className==null){
							 rows[i].className='';
						 }
						 if(rows[i].combNo== null){
							 rows[i].combNo='';						 						 
						 }
						 if(rows[i].qtyTot== null){
							 rows[i].qtyTot='';						 						 
						 }
						 if(rows[i].packQty== null){
							 rows[i].packQty='';						 						 
						 }
						 if(rows[i].specs== null){
							 rows[i].specs='';						 						 
						 }
						 if(rows[i].drugpackagingUnit== null){
							 rows[i].drugpackagingUnit='';						 						 
						 }
						 if(rows[i].baseDose== null){
							 rows[i].baseDose='';						 						 
						 }
						 if(rows[i].doseOnce== null){
							 rows[i].doseOnce='';						 						 
						 }					 
						 if(rows[i].priceUnit== null){
							 rows[i].priceUnit='';						 						 
						 }
						 if(rows[i].doseOnces== null){
							 rows[i].doseOnces='';						 						 
						 }
						 if(rows[i].doseUnit== null){
							 rows[i].doseUnit='';						 						 
						 }
						 if(rows[i].useDays== null){
							 rows[i].useDays='';						 						 
						 }						 
						 if(rows[i].itemType== '1'){
							 if(rows[i].priceUnit==rows[i].drugpackagingUnit&&rows[i].priceUnit!=''){
								 if(rows[i].doseOnce!=''&&rows[i].baseDose!=''&&rows[i].qtyTot!=''&&rows[i].packQty){
									 if((rows[i].doseOnce/rows[i].baseDose).toFixed(1)>rows[i].qtyTot*rows[i].packQty){
										 $.messager.alert('提示',rows[i].itemName+'开立数量小于每次剂量，不能开立医嘱!');		
										 return;
									 }
								 }								 
							 }else{
								 if(rows[i].doseOnce!=''&&rows[i].baseDose!=''&&rows[i].qtyTot!=''&&rows[i].packQty){
									 if((rows[i].doseOnce/rows[i].baseDose).toFixed(1)>rows[i].qtyTot){
										 $.messager.alert('提示',rows[i].itemName+'开立数量小于每次剂量，不能开立医嘱!');		
										 return;
									 }
								 }								 
							 }
							 if(rows[i].classCode=='16'||rows[i].classCode=='17'||rows[i].classCode=='18'){//非药品不校验频次 hedong  2016-09-09  页面都无此输入项
								  if(rows[i].frequencyCode== null||rows[i].frequencyCode==''){
									 $.messager.alert('提示',rows[i].itemName+'频次为空，不能开立医嘱!');		
									 return;						 						 
								   } 
							 }
							 
							 if(rows[i].frequencyName== null){
								 rows[i].frequencyName='';						 						 
							 }
							 if(rows[i].classCode=='16'||rows[i].classCode=='17'||rows[i].classCode=='18'){//非药品不校验用法  hedong  2016-09-09  页面都无此输入项
								 if(rows[i].usageCode== null||rows[i].usageCode==''){
									 $.messager.alert('提示',rows[i].itemName+'用法为空，不能开立医嘱!');		
									 return;
								 }
							 }
							 if(rows[i].useName== null){
								 rows[i].useName='';						 						 
							 }
							 if(rows[i].doseModelCode== null||rows[i].doseModelCode==''){
								 $.messager.alert('提示',rows[i].itemName+'剂型为空，不能开立医嘱!');		
								 return;
							 }
							 if(rows[i].drugQuality== null||rows[i].drugQuality==''){
								 $.messager.alert('提示',rows[i].itemName+'性质为空，不能开立医嘱!');		
								 return;
							 }
							 if(rows[i].typeCode== null||rows[i].typeCode==''){
								 $.messager.alert('提示',rows[i].itemName+'医嘱类型为空，不能开立医嘱!');		
								 return;
							 }
							 if(rows[i].classCode== null||rows[i].classCode==''){
								 $.messager.alert('提示',rows[i].itemName+'系统类型为空，不能开立医嘱!');		
								 return;
							 }
							 if(rows[i].baseDose== null||rows[i].baseDose==''){
								 $.messager.alert('提示',rows[i].itemName+'基本剂量为空，不能开立医嘱!');
								 return;						 						 
							 }
						 }else{		
							 if(rows[i].frequencyCode== null){
								 rows[i].frequencyCode='';						 						 
							 }
							 if(rows[i].frequencyName== null){
								 rows[i].frequencyName='';						 						 
							 }
							 if(rows[i].usageCode== null){
								 rows[i].usageCode='';						 						 
							 }
							 if(rows[i].useName== null){
								 rows[i].useName='';						 						 
							 }
							 if(rows[i].baseDose== null){
								 rows[i].baseDose=='';						 						 
							 }
						 }
						 if(rows[i].execDpcd== null){
							 rows[i].execDpcd='';						 						 
						 }
						 if(rows[i].execDpnm== null){
							 rows[i].execDpnm='';						 						 
						 }
						 if(rows[i].emcFlag== null){
							 rows[i].emcFlag='';						 						 
						 }
						 if(rows[i].labCode== null){
							 rows[i].labCode='';						 						 
						 }
						 if(rows[i].itemNote== null){
							 rows[i].itemNote='';						 						 
						 }
						 if(rows[i].moNote2== null){
							 rows[i].moNote2='';						 						 
						 }
						 if(rows[i].hypotest== null){
							 rows[i].hypotest='';						 						 
						 }
						 if(rows[i].dateBgn== null){
							 rows[i].dateBgn='';						 						 
						 }
						 if(rows[i].dateEnd== null){
							 rows[i].dateEnd='';						 						 
						 }
						 if(rows[i].permission== null){
							 rows[i].permission='';						 						 
						 }
						 if(rows[i].execTimes== null){
							 rows[i].execTimes='';						 						 
						 }	
						 if(rows[i].execDose== null){
							 rows[i].execDose='';						 						 
						 }	
						 if(rows[i].docName== null){
							 rows[i].docName='';						 						 
						 } 
						 if(rows[i].combFlag== null){
							 rows[i].combFlag='';						 						 
						 } 			 
				 }
				 var inpatientNo = $("#inpatientNo").val();
				 var patientNo =$("#patientNo").val();
				 var deptCode = $("#deptCode").val();
				 var nurseCellCode = $("#nurseCellCode").val();
				 var babyFlag =$("#babyFlag").val();
				 var decmpsState = 0;
				 if(rows[0].typeCode=='CZ'||rows[0].typeCode=='ZC'){
					 decmpsState = 1;
				 }
				 var str = encodeURI(JSON.stringify(rows));
				 if(name!=''){
					 $.extend($.messager.defaults,{  
					        ok:"是",  
					        cancel:"否"  
					 });  
					jQuery.messager.confirm('确认信息',name+'剂量太小,确认继续开立吗?',function(e){   
						if(e){ 	
							$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
							$.ajax({
								url: '<%=basePath%>inpatient/docAdvManage/saveInpatientOrder.action',
								data:'str='+str+'&inpatientNo='+inpatientNo+'&patientNo='+patientNo+'&deptCode='+deptCode+'&nurseCellCode='+nurseCellCode+'&babyFlag='+babyFlag+'&decmpsState='+decmpsState,
								type:'post',
								success: function(docadvdata) {	
									$.messager.progress('close');	// 如果提交成功则隐藏进度条
									var dataMap = eval("("+docadvdata+")");
									$.extend($.messager.defaults,{  
								        ok:"确定"
								    });  
						   			if(dataMap.resMsg=="error"){
						   				$.messager.alert('提示',dataMap.resCode);			   				
						        		
						        	}else if(dataMap.resMsg=="success"){
						        		$.messager.alert('提示',dataMap.resCode);	
						        		$('#infolistB').datagrid('reload');
						        	}else{
						        		$.messager.alert('提示','未知错误,请联系管理员!');
						        	
						        	}
								}
							});	
						}
					});
				 }else{		
					 $.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
					 $.ajax({
							url: '<%=basePath%>inpatient/docAdvManage/saveInpatientOrder.action',
							data:'str='+str+'&inpatientNo='+inpatientNo+'&patientNo='+patientNo+'&deptCode='+deptCode+'&nurseCellCode='+nurseCellCode+'&babyFlag='+babyFlag+'&decmpsState='+decmpsState,
							type:'post',
							success: function(docadvdata) {	
								$.messager.progress('close');	// 如果提交成功则隐藏进度条
								var dataMap = eval("("+docadvdata+")");
								$.extend($.messager.defaults,{  
							        ok:"确定"
							    });  
					   			if(dataMap.resMsg=="error"){
					   				$.messager.alert('提示',dataMap.resCode);			   				
					        		
					        	}else if(dataMap.resMsg=="success"){
					        		$.messager.alert('提示',dataMap.resCode);	
					        		$('#infolistB').datagrid('reload');
					        	}else{
					        		$.messager.alert('提示','未知错误,请联系管理员!');
					        	
					        	}
							}
						});	 
				 }				 
			 }
			 else{
				 $.messager.alert('提示',"无保存的记录！");
			 }
		}
		//退出开立
		function adEndAdvice(){	
			$('#inpatientId').val('');
			$('#adNameId').text('');//姓名
			$('#adSexId').text('');//性别
			$('#adFreeCostId').text('');//年龄
			$('#adPactId').text('');//合作单位
			$('#adProjectDivId').hide();
			$('#adWestMediDivId').hide();
			$('#adEndAdviceBtn').menubutton('disable');//退出开立
			$('#adSaveAdviceBtn').menubutton('disable');//保存医嘱
			$('#adDelAdviceBtn').menubutton('disable');//删除医嘱
			$('#adStopAdviceBtn').menubutton('disable');//停止医嘱
			$('#adAuditAdviceBtn').menubutton('disable');//审核医嘱
			$('#adHerbMedicineBtn').menubutton('disable');//草药查询
			$('#adAddGroupBtn').menubutton('disable');//添加组套
			$('#adCancelGroupBtn').menubutton('disable');//取消组套
			$('#adSaveGroupBtn').menubutton('disable');//保存组套
			$('#adStackTree').tree('loadData',[]);//清除组套Tree
			$("#cc").layout('collapse','west');//隐藏组套信息
			$("#comboboxId").val(0);
			clearDgAdDgList();//清空datagrid数据
			$('#docAdvTypeFilt').combobox('enable');
			clearProject();//清空项目类别区域信息
		}
		//清空datagrid数据
		function clearDgAdDgList(){
			var qq = $('#ttta').tabs('getSelected');				
			var tab = qq.panel('options');
			if(tab.title=='长期医嘱'){				
				var rowsA = $('#infolistA').datagrid('getRows');
				var lenA = rowsA.length;
				for(var i=0;i<lenA;i++){
					$('#infolistA').datagrid('deleteRow',0);
				}
			}else{
				var rows = $('#infolistB').datagrid('getRows');
				var len = rows.length;
				for(var i=0;i<len;i++){
					$('#infolistB').datagrid('deleteRow',0);
				}
			}					
		}
		//清空并禁用项目类别区域信息
		function clearProject(){
			$('#adProjectTdId').combobox('clear');//项目类别下拉框清空
			$('#adProjectTdId').combobox('disable');//禁用项目类别下拉框
			$('#adProjectNameTdId').textbox('clear');
			$('#adProjectNameTdId').textbox('disable');
			$('#adProjectNumTdId').numberspinner('clear');
			$('#adProjectNumTdId').numberspinner('disable');
			$('#adProjectUnitTdId').combobox('clear');
			$('#adProjectUnitTdId').combobox('disable');			
			$('#adChinMediDivId').hide();
			$('#adWestMediDivId').hide();
			$('#adNotDrugDivId').hide();
		}
		 //时间点
		 function adTimePoints(){	
			 var fre = null;
			 var qq = $('#ttta').tabs('getSelected');				
			 var tab = qq.panel('options');
			 if(tab.title=='长期医嘱'){
				 var rowA = $('#infolistA').datagrid('getSelected');				 
				 if(rowA==null){
					 $.messager.alert('提示',"请选择一条医嘱!");	
					 return;
				 }else if(rowA.frequencyCode==null||rowA.frequencyCode==''){
					 $.messager.alert('提示',"所选择医嘱频次为空，不能进行特殊频次设置!");
					 return;
				 }else{										
					 $.ajax({
						 url:'<%=basePath%>inpatient/docAdvManage/queryFrePeriod.action',
						 data:'inpatientOrder.frequencyCode='+rowA.frequencyCode,
						 type:'post',
						 success:function(data){
							 fre = data;							 
							 if(parseInt(fre)>5){//放在ajax里面才能同步								 
								 $.messager.alert('提示',"所选择医嘱频次执行频率次数超过5次，不能进行特殊频次设置!");
						 		 return;
						 	 }else{				 		 
						 		AdddilogModel("adSpeFreData-window","特殊频次",'<%=basePath%>inpatient/docAdvManage/speFreInfo.action?inpatientOrder.frequencyCode='+rowA.frequencyCode+'&inpatientOrder.frequencyName='+rowA.frequencyName+'&inpatientOrder.execTimes='+rowA.execTimes+'&inpatientOrder.execDose='+rowA.execDose+'&inpatientOrder.doseOnce='+rowA.doseOnce,'22%','43%');
						 	 }	
						 }
					 });				 	 				 
				 }	
			 }else{
				 var row = $('#infolistB').datagrid('getSelected');				 
				 if(row==null){
					 $.messager.alert('提示',"请选择一条医嘱!");	
					 return;
				 }else if(row.frequencyCode==null||row.frequencyCode==''){
					 $.messager.alert('提示',"所选择医嘱频次为空，不能进行特殊频次设置!");
					 return;			 
				 }else{			
					 $.ajax({
						 url:'<%=basePath%>inpatient/docAdvManage/queryFrePeriod.action',
						 data:'inpatientOrder.frequencyCode='+row.frequencyCode,
						 type:'post',
						 success:function(data){
							 fre = data;							 
							 if(parseInt(fre)>5){//放在ajax里面才能同步	
								 $.messager.alert('提示',"所选择医嘱频次执行频率次数超过5次，不能进行特殊频次设置!");
						 		 return;
						 	 }else{				 		 
						 		AdddilogModel("adSpeFreData-window","特殊频次",'<%=basePath%>inpatient/docAdvManage/speFreInfo.action?inpatientOrder.frequencyCode='+row.frequencyCode+'&inpatientOrder.frequencyName='+row.frequencyName+'&inpatientOrder.execTimes='+row.execTimes+'&inpatientOrder.execDose='+row.execDose+'&inpatientOrder.doseOnce='+row.doseOnce,'22%','43%');
						 	 }	
						 }
					 });				 	 				 
				 }	
			 }			 			 			 		 
		 }
		 //加急连动
		   function onClickIsUrgent(id){
			   var qq = $('#ttta').tabs('getSelected');				
			   var tab = qq.panel('options');
			   if(tab.title=='长期医嘱'){	
				    var indexA = getIndexForAdDgListA();
					var rowA = $('#infolistA').datagrid('getSelected');						
					if(indexA>=0){
						if($('#'+id).is(':checked')){
							$('#infolistA').datagrid('updateRow',{
								index: indexA,
								row: {
									emcFlag:1,
									isUrgent:'加急',
									changeNo:1
								}
							});
						}else{
							$('#infolistA').datagrid('updateRow',{
								index: indexA,
								row: {
									emcFlag:0,
									isUrgent:'普通',
									changeNo:1
								}
							});
						}
					}
					else{
						$.messager.alert('提示',"请选中一条医嘱！");
					}	
			   }else if(tab.title=='临时医嘱'){
				   var index = getIndexForAdDgList();
					var row = $('#infolistB').datagrid('getSelected');						
					if(index>=0){
						if($('#'+id).is(':checked')){
							$('#infolistB').datagrid('updateRow',{
								index: index,
								row: {
									emcFlag:1,
									isUrgent:'加急',
									changeNo:1
								}
							});
						}else{
							$('#infolistB').datagrid('updateRow',{
								index: index,
								row: {
									emcFlag:0,
									isUrgent:'普通',
									changeNo:1
								}
							});
						}
					}
					else{
						$.messager.alert('提示',"请选中一条医嘱！");
					} 
			   }				
			}
			//加急列表页 显示		
			function emcFamater(value,row,index){			
				if(row.emcFlag!=null&&row.emcFlag!=""){
					if(row.emcFlag==0){
						return '普通';
					}
					if(row.emcFlag==1){
						return '加急';
					}
				}			
			}
			//医嘱名称列表页 显示		
 			function itemNameFamater(value,row,index){	
				if((row.typeCode=='003'||row.typeCode=='004')&&(row.classCode=='17'||row.classCode=='18')){//是自备药					
						if(row.permission==1){//判断患者是否同意
							if(row.moStat>=0){
				 				if(row.moNote1!=''&&row.moNote1!=null){	
									return '<span style=\'position: relative; left:-5px;top:-6px;color:red;\'>*</span>'+'√'+value+'['+row.specs+']'+'[自备]';																		
				 				}else{
				 					return '√'+value+'['+row.specs+']'+'[自备]';																	
				 				}
							}else{
								if(row.moNote1!=''&&row.moNote1!=null){	
									return '<div style=\'float:right;margin-top:-1px;margin-right:-4px;width:12px;height:12px;background:url(<%=basePath%>/themes/system/images/button/shen1.png)\'></div>'+'<span style=\'position: relative; left:-5px;top:-6px;color:red;\'>*</span>'+'√'+value+'['+row.specs+']'+'[自备]';									
				 				}else{
				 					return '<div style=\'float:right;margin-top:-1px;margin-right:-4px;width:12px;height:12px;background:url(<%=basePath%>/themes/system/images/button/shen1.png)\'></div>'+'√'+value+'['+row.specs+']'+'[自备]';																
				 				}
							}
						}else{
							if(row.moStat>=0){
				 				if(row.moNote1!=''&&row.moNote1!=null){	
									return '<span style=\'position: relative; left:-5px;top:-6px;color:red;\'>*</span>'+value+'['+row.specs+']'+'[自备]';																	
				 				}else{			 				
									return value+'['+row.specs+']'+'[自备]';																	
				 				}
							}else{
								if(row.moNote1!=''&&row.moNote1!=null){	
									return '<div style=\'float:right;margin-top:-1px;margin-right:-4px;width:12px;height:12px;background:url(<%=basePath%>/themes/system/images/button/shen1.png)\'></div>'+'<span style=\'position: relative; left:-5px;top:-6px;color:red;\'>*</span>'+value+'['+row.specs+']'+'[自备]';																	
				 				}else{
				 					return '<div style=\'float:right;margin-top:-1px;margin-right:-4px;width:12px;height:12px;background:url(<%=basePath%>/themes/system/images/button/shen1.png)\'></div>'+value+'['+row.specs+']'+'[自备]';																
				 				}
							}
						}												
				}else{//不是自备药	
					if(row.itemType=='1'){//药品医嘱
						if(row.permission==1){//判断患者是否同意
							if(row.moStat>=0){
				 				if(row.moNote1!=''&&row.moNote1!=null){	
									return '<span style=\'position: relative; left:-5px;top:-6px;color:red;\'>*</span>'+'√'+value+'['+row.specs+']';																		
				 				}else{
				 					return '√'+value+'['+row.specs+']';																	
				 				}
							}else{
								if(row.moNote1!=''&&row.moNote1!=null){	
									return '<div style=\'float:right;margin-top:-1px;margin-right:-4px;width:12px;height:12px;background:url(<%=basePath%>/themes/system/images/button/shen1.png)\'></div>'+'<span style=\'position: relative; left:-5px;top:-6px;color:red;\'>*</span>'+'√'+value+'['+row.specs+']';									
				 				}else{
				 					return '<div style=\'float:right;margin-top:-1px;margin-right:-4px;width:12px;height:12px;background:url(<%=basePath%>/themes/system/images/button/shen1.png)\'></div>'+'√'+value+'['+row.specs+']';																
				 				}
							}
						}else{
							if(row.moStat>=0){
				 				if(row.moNote1!=''&&row.moNote1!=null){	
									return '<span style=\'position: relative; left:-5px;top:-6px;color:red;\'>*</span>'+value+'['+row.specs+']';																	
				 				}else{			 				
									return value+'['+row.specs+']';																	
				 				}
							}else{
								if(row.moNote1!=''&&row.moNote1!=null){	
									return '<div style=\'float:right;margin-top:-1px;margin-right:-4px;width:12px;height:12px;background:url(<%=basePath%>/themes/system/images/button/shen1.png)\'></div>'+'<span style=\'position: relative; left:-5px;top:-6px;color:red;\'>*</span>'+value+'['+row.specs+']';																	
				 				}else{
				 					return '<div style=\'float:right;margin-top:-1px;margin-right:-4px;width:12px;height:12px;background:url(<%=basePath%>/themes/system/images/button/shen1.png)\'></div>'+value+'['+row.specs+']';																
				 				}
							}
						}
					}
					else{//非药品医嘱
						if(row.permission==1){//判断患者是否同意
							if(row.moStat>=0){
				 				if(row.moNote1!=''&&row.moNote1!=null){	
									return '<span style=\'position: relative; left:-5px;top:-6px;color:red;\'>*</span>'+'√'+value;																		
				 				}else{
				 					return '√'+value;																	
				 				}
							}else{
								if(row.moNote1!=''&&row.moNote1!=null){	
									return '<div style=\'float:right;margin-top:-1px;margin-right:-4px;width:12px;height:12px;background:url(<%=basePath%>/themes/system/images/button/shen1.png)\'></div>'+'<span style=\'position: relative; left:-5px;top:-6px;color:red;\'>*</span>'+'√'+value;									
				 				}else{
				 					return '<div style=\'float:right;margin-top:-1px;margin-right:-4px;width:12px;height:12px;background:url(<%=basePath%>/themes/system/images/button/shen1.png)\'></div>'+'√'+value;																
				 				}
							}
						}else{
							if(row.moStat>=0){
				 				if(row.moNote1!=''&&row.moNote1!=null){	
									return '<span style=\'position: relative; left:-5px;top:-6px;color:red;\'>*</span>'+value;																	
				 				}else{			 				
									return value;																	
				 				}
							}else{
								if(row.moNote1!=''&&row.moNote1!=null){	
									return '<div style=\'float:right;margin-top:-1px;margin-right:-4px;width:12px;height:12px;background:url(<%=basePath%>/themes/system/images/button/shen1.png)\'></div>'+'<span style=\'position: relative; left:-5px;top:-6px;color:red;\'>*</span>'+value;																	
				 				}else{
				 					return '<div style=\'float:right;margin-top:-1px;margin-right:-4px;width:12px;height:12px;background:url(<%=basePath%>/themes/system/images/button/shen1.png)\'></div>'+value;																
				 				}
							}
						}
					}													
				}					
 			}
		 //每次量剂量
 			function doseOnceFamater(value,row,index){
				if(row.doseOnce!=''&&row.doseOnce!=null){	
					var doseOnces = (row.doseOnce/row.baseDose).toFixed(1)+drugpackagingunitMaps[row.minUnit]+'='+row.doseOnce+drugdoseunitMaps[row.doseUnit];
					return doseOnces;									
				}
				else{
					return '';
				}				
 			} 
 			//检查部位渲染
 		   function checkPointFamater(value,row,index){	
 				if(value!=null&&value!=""){									
 					return colorInfoCheckpointMap[value];
 				}	
 		   }
 		 	//样本类型渲染
 		   function sampleFamater(value,row,index){			
 				if(value!=null&&value!=""){									
 					return colorInfoSampleTeptMap[value];					
 				}	
 		   }
		 //是否皮试
		 $('#adWestMediSkiTdId').combobox({  			 
			 onSelect:function(record){
			 	var indexA = getIndexForAdDgListA();
	    		var index = getIndexForAdDgList();
	    		if(indexA>=0){
					$('#infolistA').datagrid('updateRow',{
						index: indexA,
						row: {
							hypotest:record.id,
							hypotestName:record.text,
							changeNo:1
						}
					});
				}
	    		if(index>=0){
					$('#infolistB').datagrid('updateRow',{
						index: index,
						row: {
							hypotest:record.id,
							hypotestName:record.text,
							changeNo:1
						}
					});
				}
			 }
		 });
		 function skinFamater(value,row,index){			 	
				if(row.hypotest==1){
					return '不需要皮试';
				}
				if(row.hypotest==2){
					return '需要皮试，未做';
				}
				if(row.hypotest==3){
					return '皮试阳';
				}
				if(row.hypotest==4){
					return '皮试阴';
				}			
		 }
						
		 	//删除医嘱
			function adDelAdvice(){
				$.extend($.messager.defaults,{  
			        ok:"是",  
			        cancel:"否"  
			    });  
				jQuery.messager.confirm('','是否确定删除医嘱?此操作不可撤销!',function(e){   
					if(e){
						var qq = $('#ttta').tabs('getSelected');				
						var tab = qq.panel('options');
						if(tab.title=='长期医嘱'){	
							var rowA = $('#infolistA').datagrid('getChecked');
							if(rowA.length>1){
								var itemName = '';
								for(var i = 0;i<rowA.length;i++){								
									if(rowA[i].id==''){											
										$('#infolistA').datagrid('deleteRow',$('#infolistA').datagrid('getRowIndex',rowA[i]));											
									}else{
										if(itemName==''){
											itemName=rowA[i].itemName;
										}else{
											itemName+='，'+rowA[i].itemName;
										}									
										continue;
									}	
								}
								if(itemName!=''){
									$.messager.alert('提示',"医嘱"+itemName+"已保存，请逐条删除!");	
								}								
								return;	
							}else if(rowA.length==1){
								if(rowA[0].moStat<=0){																
									if(rowA[0].id==''){											
										$('#infolistA').datagrid('deleteRow',$('#infolistA').datagrid('getRowIndex',rowA[0]));	
										reloadRow();
									}else{										
										$.ajax({
						 					url: '<%=basePath%>inpatient/docAdvManage/delDocAdvInfo.action',
						 					data:'inpatientOrder.combNo='+rowA[0].combNo,
						 					type:'post',
						 					success: function(data) {						 						
						 						if(data='success'){
						 							$.messager.alert('提示',"删除成功！");
						 							$('#infolistA').datagrid('deleteRow',$('#infolistA').datagrid('getRowIndex',rowA[0]));	
						 							reloadRow();
						 						}else{
						 							$.messager.alert('提示',"删除失败！");									
						 						}
						 					}
						 				});										
									}																				
								}else if(rowA[0].moStat==3){
									$.messager.alert('提示','医嘱'+rowA[0].itemName+'已作废不可删除或再作废！ ');
								}else if(rowA[0].moStat==1||rowA[0].moStat==2){	
									AdddilogModel("adStopData-window","停止医嘱","<%=basePath%>inpatient/docAdvManage/stopAdviceInfo.action",'18%','26%');
								}	
							}else{
								$.messager.alert('提示',"请选择要删除的医嘱信息!");	
								return;	
							}													
						}
						else if(tab.title=='临时医嘱'){
							var row = $('#infolistB').datagrid('getChecked');
							if(row.length>1){
								var itemName = '';
								for(var i = 0;i<row.length;i++){								
									if(row[i].id==''){											
										$('#infolistB').datagrid('deleteRow',$('#infolistB').datagrid('getRowIndex',row[i]));											
									}else{
										if(itemName==''){
											itemName=row[i].itemName;
										}else{
											itemName+='，'+row[i].itemName;
										}									
										continue;
									}	
								}
								if(itemName!=''){
									$.messager.alert('提示',"医嘱"+itemName+"已保存，请逐条删除!");	
								}	
								return;	
							}else if(row.length==1){
								if(row[0].moStat<=0){																
									if(row[0].id==''){											
										$('#infolistB').datagrid('deleteRow',$('#infolistB').datagrid('getRowIndex',row[0]));	
										reloadRow();
									}else{										
										$.ajax({
						 					url: '<%=basePath%>inpatient/docAdvManage/delDocAdvInfo.action',
						 					data:'inpatientOrder.combNo='+row[0].combNo,
						 					type:'post',
						 					success: function(data) {						 						
						 						if(data='success'){
						 							$.messager.alert('提示',"删除成功！");
						 							$('#infolistB').datagrid('deleteRow',$('#infolistB').datagrid('getRowIndex',row[0]));	
						 							reloadRow();
						 						}else{
						 							$.messager.alert('提示',"删除失败！");									
						 						}
						 					}
						 				});										
									}																				
								}else if(row[0].moStat==3){
									$.messager.alert('提示','医嘱'+row[0].itemName+'已作废不可删除或再作废！ ');
								}else if(row[0].moStat==1||row[0].moStat==2){	
									AdddilogModel("adStopData-window","停止医嘱","<%=basePath%>inpatient/docAdvManage/stopAdviceInfo.action",'18%','26%');
								}	
							}else{
								$.messager.alert('提示',"请选择要删除的医嘱信息!");	
								return;	
							}	
						}					
					}
				});							
			}
		 	//停止医嘱
		 	function adStopAdvice(){		 		
				AdddilogModel("adStopData-window","停止医嘱","<%=basePath%>inpatient/docAdvManage/stopAdviceInfo.action",'18%','26%');														 											
		 	}
		 	
		 	//审核医嘱
		 	function adAuditAdvice(){
		 		var qq = $('#ttta').tabs('getSelected');				
				var tab = qq.panel('options');
				if(tab.title=='长期医嘱'){
					var rowA = $('#infolistA').datagrid('getSelected');
			 		if(rowA!=null){
			 			if(rowA.moStat==-1 && rowA.id!=''){
				 			AdddilogModel("adAuditData-window","审核","<%=basePath%>inpatient/docAdvManage/auditAdviceInfo.action",'21%','32%');
				 		}else{
				 			$.messager.alert('提示',"该条医嘱无需审核！");
				 		}
			 		}else{
			 			$.messager.alert('提示',"请选择一条医嘱!");
			 		}
				}else if(tab.title=='临时医嘱'){
					var row = $('#infolistB').datagrid('getSelected');
			 		if(row!=null){
			 			if(row.moStat==-1 && row.id!=''){
				 			AdddilogModel("adAuditData-window","审核","<%=basePath%>inpatient/docAdvManage/auditAdviceInfo.action",'21%','32%');
				 		}else{
				 			$.messager.alert('提示',"该条医嘱无需审核！");
				 		}
			 		}else{
			 			$.messager.alert('提示',"请选择一条医嘱!");
			 		}	
				}		 				 			 				 		
		 	}
		 	//关闭模式窗口
			function closeAuditLayout(){
				$('#adAuditData-window').window('close');
			}
			function closeStopLayout(){
				$('#adStopData-window').window('close');
			}
			function closeSpeFreLayout(){
				$('#adSpeFreData-window').window('close');
			}
			//页面中最大的组合号
			function maxCombNo(){
				var qq = $('#ttta').tabs('getSelected');				
				var tab = qq.panel('options');
				if(tab.title=='长期医嘱'){	
					var rows = $('#infolistA').datagrid('getRows');
					if(rows.length>0){
						var maxComnNo = rows[0].combNo;
						for(var i=1;i<rows.length;i++){												
							if(maxComnNo<rows[i].combNo){
								maxComnNo = rows[i].combNo;
							}											
						}
					}else{
						maxComnNo = '000000000000';
					}					
					return maxComnNo;
				}else if(tab.title=='临时医嘱'){
					var rows = $('#infolistB').datagrid('getRows');
					if(rows.length>0){
						var maxComnNo = rows[0].combNo;
						for(var i=1;i<rows.length;i++){												
							if(maxComnNo<rows[i].combNo){
								maxComnNo = rows[i].combNo;
							}											
						}
					}else{
						maxComnNo = '000000000000';
					}	
					return maxComnNo;
				}					
			}
			//组合
			function adAddGroup(){
				var qq = $('#ttta').tabs('getSelected');				
				var tab = qq.panel('options');
				if(tab.title=='长期医嘱'){	
					var rowsA = $('#infolistA').datagrid('getChecked');
					if(rowsA!=null&&rowsA.length>0){
						if(rowsA.length==1){
							$.messager.alert('提示',"无法对一条记录进行组合！");
							return;
						}		
						var one = '';
						var combFlag = '';
						if(rowsA[0].combNo!=''&&rowsA[0].combNo!=null){
							one = rowsA[0].combNo;
							combFlag = '1';
						}else{
							var maxCombNo1 = parseInt(maxCombNo())+1;
							var len = maxCombNo().length-maxCombNo1.toString().length;
							var maxNo = '';
							for(var j=0;j<len;j++){
								maxNo = maxNo+'0';
							}
							one = maxNo+maxCombNo1;
							combFlag = '1';
						}
						var fre = null;//频次
						var usageName = null;//用法
						var sysType = null;//系统类别
						var executiveDeptHid = null;//系统类别
						var setNum = null;//付数
						var totalNum = null;//总量
						var totalNum1 = null;//总量
						var sampleTept = null;//样本类型
						var checkPoint =null;//部位
						for(var i=0;i<rowsA.length;i++){
							if(fre==null){
								fre = rowsA[i].frequencyCode;
							}
							if(rowsA[i].frequencyCode!=fre){
								$.messager.alert('提示',"所选记录的频次信息不同，无法进行组合操作！");
								return;
							}
							if(usageName==null){
								usageName = rowsA[i].useName;
							}
							if(rowsA[i].useName!=usageName){
								$.messager.alert('提示',"所选记录的用法信息不同，无法进行组合操作！");
								return;
							}
							if(sysType==null){
								sysType = rowsA[i].classCode;
							}
							if(rowsA[i].classCode!=sysType){
								$.messager.alert('提示',"所选记录的类别不同，无法进行组合操作！");
								return;
							}
							if(executiveDeptHid==null){
								executiveDeptHid = rowsA[i].execDpcd;
							}
							if(rowsA[i].execDpcd!=executiveDeptHid){
								$.messager.alert('提示',"所选记录的执行科室不同，无法进行组合操作！");
								return;
							}
							if(setNum==null){
								setNum = rowsA[i].useDays;
							}
							if(rowsA[i].useDays!=setNum){
								$.messager.alert('提示',"所选记录的付数不同，无法进行组合操作！");
								return;
							}
							if(rowsA[i].itemType=='1'){
								if(rowsA[i].packQty==null){
									rowsA[i].packQty = 1;
								}
								if(totalNum==null){
									if(rowsA[i].qtyTot==rowsA[i].drugpackagingUnit){
										totalNum = rowsA[i].qtyTot*rowsA[i].packQty;
									}else{
										totalNum = rowsA[i].qtyTot;
									}									
								}
								if(rowsA[i].qtyTot==rowsA[i].drugpackagingUnit){
									totalNum1 = rowsA[i].qtyTot*rowsA[i].packQty;
								}else{
									totalNum1 = rowsA[i].qtyTot;
								}
								if(totalNum1!=totalNum){
									$.messager.alert('提示',"所选记录的总量不同，无法进行组合操作！");
									return;
								}
							}							
							if(sampleTept==null){
								sampleTept = rowsA[i].labCode;
							}
							if(rowsA[i].labCode!=sampleTept){
								$.messager.alert('提示',"所选记录的样本类型不同，无法进行组合操作！");
								return;
							}
							if(checkPoint==null){
								checkPoint = rowsA[i].itemNote;
							}
							if(rowsA[i].itemNote!=checkPoint){
								$.messager.alert('提示',"所选记录的检查部位不同，无法进行组合操作！");
								return;
							}
						}
						var group = null;
						var datas = new Array();//存放数据的数组
						var datasHis = new Array();//存放数据的数组
						for(var i=0;i<rowsA.length;i++){
							datas[datas.length] = rowsA[i];
						}
						for(var i=0;i<rowsA.length;i++){
							$('#infolistA').datagrid('deleteRow',$('#infolistA').datagrid('getRowIndex',rowsA[i]));
						}					
						if(one!=null){
							var rowsHis = $('#infolistA').datagrid('getRows');
							for(var i=0;i<rowsHis.length;i++){
								if(rowsHis[i].combNo==one){
									datas[datas.length] = rowsHis[i];
								}else{
									datasHis[datasHis.length] = rowsHis[i];
								}
							}
							group = one;
						}else{
							var rowsHis = $('#infolistA').datagrid('getRows');
							for(var i=0;i<rowsHis.length;i++){
								datasHis[datasHis.length] = rowsHis[i];
							}
							group = groupVal;
							groupVal += 1;
						}
						var rwosNewHis = $('#infolistA').datagrid('getRows');
						var rwosNewHisLen = rwosNewHis.length;
						for(var i=0;i<rwosNewHisLen;i++){
							$('#infolistA').datagrid('deleteRow',$('#infolistA').datagrid('getRowIndex',rwosNewHis[0]));
						}
						if(datasHis.length>0){
							for(var i=datasHis.length-1;i>=0;i--){
								$('#infolistA').datagrid('insertRow',{
									index: 0,	
									row:datasHis[i]
								});
							}
						}
	                    if(datas.length>0){
	                    	for(var i=0;i<datas.length;i++){
	                    		$('#infolistA').datagrid('insertRow',{
									index: i,	
									row:datas[i]
								});
	                    		$('#infolistA').datagrid('checkRow',i);
								$('#infolistA').datagrid('updateRow',{
									index: i,
									row: {
										combNo:group,
										changeNo:1,
										combFlag:combFlag
									}
								});
								if(i==0){
									$('#infolistA').datagrid('updateRow',{
										index: i,
										row: {
											combNoFlag:'┓'
										}
									});
								}else if(i>0 && i<datas.length-1){
									$('#infolistA').datagrid('updateRow',{
										index: i,
										row: {
											combNoFlag:'┃'
										}
									});								
								}else if(i==datas.length-1){
									$('#infolistA').datagrid('updateRow',{
										index: i,
										row: {
											combNoFlag:'┛'
										}
									});	
								}								
	                    	}
	                    }                   
					}else{
						$.messager.alert('提示',"请选择要组合的医嘱信息！");
						return;			
					}
				}else if(tab.title=='临时医嘱'){//长期医嘱、临时医嘱判断
					var rows = $('#infolistB').datagrid('getChecked');
					if(rows!=null&&rows.length>0){
						if(rows.length==1){
							$.messager.alert('提示',"无法对一条记录进行组合！");
							return;
						}
						var one = '';
						var combFlag = '';
						if(rows[0].combNo!=''&&rows[0].combNo!=null){
							one = rows[0].combNo;
							combFlag = '1';
						}else{							
							var maxCombNo1 = parseInt(maxCombNo())+1;
							var len = maxCombNo().length-maxCombNo1.toString().length;
							var maxNo = '';
							for(var j=0;j<len;j++){
								maxNo = maxNo+'0';
							}
							one = maxNo+maxCombNo1;
							combFlag = '1';
						}
						var fre = null;//频次
						var usageName = null;//用法
						var sysType = null;//系统类别
						var executiveDeptHid = null;//系统类别
						var setNum = null;//付数
						var totalNum = null;//总量
						var totalNum1 = null;//总量
						var sampleTept = null;//样本类型
						var checkPoint =null;//部位
						for(var i=0;i<rows.length;i++){						
							if(fre==null){
								fre = rows[i].frequencyCode;
							}
							if(rows[i].frequencyCode!=fre){
								$.messager.alert('提示',"所选记录的频次信息不同，无法进行组合操作！");
								return;
							}
							if(usageName==null){
								usageName = rows[i].useName;
							}
							if(rows[i].useName!=usageName){
								$.messager.alert('提示',"所选记录的用法信息不同，无法进行组合操作！");
								return;
							}
							if(sysType==null){
								sysType = rows[i].classCode;
							}
							if(rows[i].classCode!=sysType){
								$.messager.alert('提示',"所选记录的类别不同，无法进行组合操作！");
								return;
							}
							if(executiveDeptHid==null){
								executiveDeptHid = rows[i].execDpcd;
							}
							if(rows[i].execDpcd!=executiveDeptHid){
								$.messager.alert('提示',"所选记录的执行科室不同，无法进行组合操作！");
								return;
							}
							if(setNum==null){
								setNum = rows[i].useDays;
							}
							if(rows[i].useDays!=setNum){
								$.messager.alert('提示',"所选记录的付数不同，无法进行组合操作！");
								return;
							}
							if(rows[i].itemType=='1'){
								if(rows[i].packQty==null){
									rows[i].packQty = 1;
								}
								if(totalNum==null){
									if(rows[i].qtyTot==rows[i].drugpackagingUnit){
										totalNum = rows[i].qtyTot*rows[i].packQty;
									}else{
										totalNum = rows[i].qtyTot;
									}									
								}
								if(rows[i].qtyTot==rows[i].drugpackagingUnit){
									totalNum1 = rows[i].qtyTot*rows[i].packQty;
								}else{
									totalNum1 = rows[i].qtyTot;
								}
								if(totalNum1!=totalNum){
									$.messager.alert('提示',"所选记录的总量不同，无法进行组合操作！");
									return;
								}
							}							
							if(sampleTept==null){
								sampleTept = rows[i].labCode;
							}
							if(rows[i].labCode!=sampleTept){
								$.messager.alert('提示',"所选记录的样本类型不同，无法进行组合操作！");
								return;
							}
							if(checkPoint==null){
								checkPoint = rows[i].itemNote;
							}
							if(rows[i].itemNote!=checkPoint){
								$.messager.alert('提示',"所选记录的检查部位不同，无法进行组合操作！");
								return;
							}
						}
						var group = null;
						var datas = new Array();//存放数据的数组
						var datasHis = new Array();//存放数据的数组
						for(var i=0;i<rows.length;i++){
							datas[datas.length] = rows[i];
						}
						for(var i=0;i<rows.length;i++){
							$('#infolistB').datagrid('deleteRow',$('#infolistB').datagrid('getRowIndex',rows[i]));
						}					
						if(one!=null){
							var rowsHis = $('#infolistB').datagrid('getRows');
							for(var i=0;i<rowsHis.length;i++){
								if(rowsHis[i].combNo==one){
									datas[datas.length] = rowsHis[i];
								}else{
									datasHis[datasHis.length] = rowsHis[i];
								}
							}
							group = one;
						}else{
							var rowsHis = $('#infolistB').datagrid('getRows');
							for(var i=0;i<rowsHis.length;i++){
								datasHis[datasHis.length] = rowsHis[i];
							}
							group = groupVal;
							groupVal += 1;
						}
						var rwosNewHis = $('#infolistB').datagrid('getRows');
						var rwosNewHisLen = rwosNewHis.length;
						for(var i=0;i<rwosNewHisLen;i++){
							$('#infolistB').datagrid('deleteRow',$('#infolistB').datagrid('getRowIndex',rwosNewHis[0]));
						}
						if(datasHis.length>0){
							for(var i=datasHis.length-1;i>=0;i--){
								$('#infolistB').datagrid('insertRow',{
									index: 0,	
									row:datasHis[i]
								});
							}
						}
	                    if(datas.length>0){
	                    	for(var i=0;i<datas.length;i++){
	                    		$('#infolistB').datagrid('insertRow',{
									index: i,	
									row:datas[i]
								});
	                    		$('#infolistB').datagrid('checkRow',i);
								$('#infolistB').datagrid('updateRow',{
									index: i,
									row: {
										combNo:group,
										changeNo:1,
										combFlag:combFlag
									}
								});
								if(i==0){
									$('#infolistB').datagrid('updateRow',{
										index: i,
										row: {
											combNoFlag:'┓'
										}
									});
								}else if(i>0 && i<datas.length-1){
									$('#infolistB').datagrid('updateRow',{
										index: i,
										row: {
											combNoFlag:'┃'
										}
									});								
								}else if(i==datas.length-1){
									$('#infolistB').datagrid('updateRow',{
										index: i,
										row: {
											combNoFlag:'┛'
										}
									});	
								}
	                    	}
	                    }
					}else{
						$.messager.alert('提示',"请选择要组合的医嘱信息！");
						return;			
					}
				}								
			}
			//取消组合
			function adCancelGroup(){	
				var qq = $('#ttta').tabs('getSelected');				
				var tab = qq.panel('options');
				if(tab.title=='长期医嘱'){	
					var rowsA = $('#infolistA').datagrid('getChecked');
					if(rowsA!=null&&rowsA.length>0){
						var datas = new Array();//存放需要拆分的组合的标记
						for(var i=0;i<rowsA.length;i++){					
							if(rowsA[i].combNo!=null&&rowsA[i].combNo!=''){
								if(datas.indexOf(rowsA[i].combNo)==-1){
									datas[datas.length] = rowsA[i].combNo;
								}
							}
						}
						if(datas.length>0){
							var datasDel = new Array();//存放需要拆分的组合
							var datasArr = new Array();//存放数据的数组
							var rowsHis = $('#infolistA').datagrid('getRows');
							$('#infolistA').datagrid('uncheckAll');
							for(var i=0;i<rowsHis.length;i++){
								for(var j=0;j<datas.length;j++){
									if(rowsHis[i].combNo==datas[j]){
										datasDel[datasDel.length] = rowsHis[i];
										$('#infolistA').datagrid('checkRow',$('#infolistA').datagrid('getRowIndex',rowsHis[i]));
									}
								}
							}
							for(var i=0;i<rowsA.length;i++){
								$('#infolistA').datagrid('deleteRow',$('#infolistA').datagrid('getRowIndex',rowsA[i]));							
							}							
							for(var i=0;i<rowsA.length;i++){
								var leng = $('#infolistA').datagrid('getRows').length;							
								$('#infolistA').datagrid('insertRow',{
									row:rowsA[i]
								});
								$('#infolistA').datagrid('updateRow',{
									index:leng,
									row: {
										combNo:"",
										changeNo:1,
										combFlag:'',
										combNoFlag:''
									}
								});
							}
						}else{
							$.messager.alert('提示',"没有符合拆分条件的组合！");
							return;
						}
					}else{
						$.messager.alert('提示',"请选择需要拆分的组合！");
						return;
					}
				}else if(tab.title=='临时医嘱'){
					var rows = $('#infolistB').datagrid('getChecked');
					if(rows!=null&&rows.length>0){
						var datas = new Array();//存放需要拆分的组合的标记
						for(var i=0;i<rows.length;i++){					
							if(rows[i].combNo!=null&&rows[i].combNo!=''){
								if(datas.indexOf(rows[i].combNo)==-1){
									datas[datas.length] = rows[i].combNo;
								}
							}
						}
						if(datas.length>0){
							var datasDel = new Array();//存放需要拆分的组合
							var datasArr = new Array();//存放数据
							var rowsHis = $('#infolistB').datagrid('getRows');
							$('#infolistB').datagrid('uncheckAll');
							for(var i=0;i<rowsHis.length;i++){
								for(var j=0;j<datas.length;j++){
									if(rowsHis[i].combNo==datas[j]){
										datasDel[datasDel.length] = rowsHis[i];
										$('#infolistB').datagrid('checkRow',$('#infolistB').datagrid('getRowIndex',rowsHis[i]));
									}
								}
							}
							for(var i=0;i<rows.length;i++){
								$('#infolistB').datagrid('deleteRow',$('#infolistB').datagrid('getRowIndex',rows[i]));							
							}							
							for(var i=0;i<rows.length;i++){
								var leng = $('#infolistB').datagrid('getRows').length;
								$('#infolistB').datagrid('insertRow',{
									row:rows[i]
								});
								$('#infolistB').datagrid('updateRow',{
									index:leng,
									row: {
										combNo:"",
										changeNo:1,
										combFlag:'',
										combNoFlag:''
									}
								});
							}
						}else{
							$.messager.alert('提示',"没有符合拆分条件的组合！");
							return;
						}
					}else{
						$.messager.alert('提示',"请选择需要拆分的组合！");
						return;
					}
				}			
			}
			
			var datasArr;//存放数据的数组
			//复制医嘱
			function adviceCopy(){					
				var rowsA = $('#infolistA').datagrid('getChecked');
				var rowsB = $('#infolistB').datagrid('getChecked');
				var rowsC = $('#infolistC').datagrid('getChecked');
				var rowsD = $('#infolistD').datagrid('getChecked');
				datasArr = new Array();//存放数据的数组
				if(rowsA!=null&&rowsA.length>0){					
					for(var i=0;i<rowsA.length;i++){
						datasArr[datasArr.length] = jQuery.extend(true,{},rowsA[i]);
					}
				}else if(rowsB!=null&&rowsB.length>0){
					for(var i=0;i<rowsB.length;i++){
						datasArr[datasArr.length] = jQuery.extend(true,{},rowsB[i]);
					}
				}else if(rowsC!=null&&rowsC.length>0){
					for(var i=0;i<rowsC.length;i++){
						datasArr[datasArr.length] = jQuery.extend(true,{},rowsC[i]);
					}
				}else if(rowsD!=null&&rowsD.length>0){
					for(var i=0;i<rowsD.length;i++){
						datasArr[datasArr.length] = jQuery.extend(true,{},rowsD[i]);
					}
				}else{
					$.messager.alert('提示',"请选择需要复制的医嘱信息！");
				}									
		    }
			//医嘱粘贴
			function advicePaste(){
				var qq = $('#ttta').tabs('getSelected');				
				var tab = qq.panel('options');
				if(tab.title=='长期医嘱'){
					if(datasArr.length>0){
						for(var i=0;i<datasArr.length;i++){
							if(datasArr[i].typeCode=="CZ"||datasArr[i].typeCode=="ZC"){
								var lastIndex =$('#infolistA').datagrid('insertRow',{
									row:datasArr[i]
								}).datagrid('getRows').length-1;
								$('#infolistA').datagrid('updateRow',{
									index: lastIndex,
									row: {
										combNo:null
									}
								});
							}
						}
					}					
				}else if(tab.title=='临时医嘱'){					
					if(datasArr.length>0){
						for(var i=0;i<datasArr.length;i++){
							if(datasArr[i].typeCode=="CZ"||datasArr[i].typeCode=="ZC"){
							}else{
								var lastIndex =$('#infolistB').datagrid('insertRow',{
									row:datasArr[i]
								}).datagrid('getRows').length-1;
								$('#infolistB').datagrid('updateRow',{
									index: lastIndex,
									row: {
										combNo:null
									}
								});
							}
						}
					}				
				}
			}
			//添加颜色标识
			function functionColour(value,row,index){
				if(row.moStat==0){					
					return 'background-color:#00FF00;';									
				}	
				if(row.moStat==1){					
					return 'background-color:#4A4AFF;';									
				}
				if(row.moStat==2){					
					return 'background-color:#EEEE00;';									
				}
				if(row.moStat==3){					
					return 'background-color:red;';									
				}
				if(row.moStat==4){					
					return 'background-color:grey;';										
				}
				if(row.moStat==-1){					
					return 'background-color:#00FF00;';										
				}
				if(row.moStat==-3){					
					return 'background-color:#00FF00;';										
				}
			}
			//行号生成
			function functionRowNum(value,row,index){
				return index+1;
			}
			//刷新行
			function reloadRow(){
				var qq = $('#ttta').tabs('getSelected');				
				var tab = qq.panel('options');
				if(tab.title=='长期医嘱'){	
					 var rowsA = $('#infolistA').datagrid('getRows');
					 for(var i=0;i<rowsA.length;i++){
						 $('#infolistA').datagrid('refreshRow',$('#infolistA').datagrid('getRowIndex',rowsA[i])); 
					 }
				}else if(tab.title=='临时医嘱'){
					 var rows = $('#infolistB').datagrid('getRows');
					 for(var i=0;i<rows.length;i++){
						 $('#infolistB').datagrid('refreshRow',$('#infolistB').datagrid('getRowIndex',rows[i])); 
					 }
				}
			}
			//保存组套
			function adSaveGroup(){
				var qq = $('#ttta').tabs('getSelected');				
				var tab = qq.panel('options');
				if(tab.title=='长期医嘱'){	
					var rowsA = $('#infolistA').datagrid('getChecked');
					if(rowsA!=null&&rowsA.length>0){
						if(rowsA.length==1){
							$.messager.alert('提示',"至少选择两条记录添加组套!");
							return
						}else{
							$('#stackSaveModleDivId').show();
							$('#stackSaveModleDivId').dialog({    
							    title:'确认组套信息',    
							    width:'80%',    
							    height:'54%',    
							    closed: false,
							    closable:false,    
							    cache: false,
							    modal: true   
							});
							StackInfoSource();
							for(var i=0;i<rowsA.length;i++){
								$('#adStackInfoSaveModelDgId').datagrid('appendRow',{
									id:rowsA[i].itemCode,//名称
									name:rowsA[i].itemName,//名称
									spec:rowsA[i].specs,//规格
									packagingnum:'',//包装数量
									unit:rowsA[i].drugpackagingUnit,//单位
									unitView:colorInfoUnitsMap.get(rowsA[i].drugpackagingUnit),//单位
									defaultprice:rowsA[i].itemPrice,//默认价
									frequencyCode:rowsA[i].frequencyCode,//频次编码
									frequencyCodeView:rowsA[i].frequencyName,//频次编码
									usageCode:rowsA[i].usageCode,//用法名称
									usageCodeView:rowsA[i].useName,//用法名称
									onceDose:rowsA[i].doseOnce,//每次服用剂量
									doseUnit:rowsA[i].doseUnit,//剂量单位
									doseUnitView:drugdoseunitMaps[rowsA[i].doseUnit],//剂量单位
									mainDrugshow:1,//主药标记
									dateBgn:rowsA[i].dateBgn,//医嘱开始时间
									dateEnd:rowsA[i].dateEnd,//医嘱结束时间
									itemNote:rowsA[i].itemNote,//检查部位
									days:rowsA[i].useDays,//草药付数
									remark:rowsA[i].moNote2,//备注
									isDrugShow:rowsA[i].ty,//是否药品
									isDrugShowView:rowsA[i].ty==1?'是':'否',
									stackInfoNum:rowsA[i].packQty//开立数量
								});
							} 
						}
					}else{
						$.messager.alert('提示',"请选择需要添加组套的信息!");
						return;
					}
				}else if(tab.title=='临时医嘱'){
					var rows = $('#infolistB').datagrid('getChecked');
					if(rows!=null&&rows.length>0){
						if(rows.length==1){
							$.messager.alert('提示',"至少选择两条记录添加组套!");
							return
						}else{
							$('#stackSaveModleDivId').show();
							$('#stackSaveModleDivId').dialog({    
							    title:'确认组套信息',    
							    width:'80%',    
							    height:'54%',    
							    closed: false,
							    closable:false,    
							    cache: false,
							    modal: true   
							});
							for(var i=0;i<rows.length;i++){
								$('#adStackInfoSaveModelDgId').datagrid('appendRow',{
									id:rows[i].itemCode,//名称
									name:rows[i].itemName,//名称
									spec:rows[i].specs,//规格
									packagingnum:'',//包装数量
									unit:rows[i].drugpackagingUnit,//单位
									unitView:colorInfoUnitsMap.get(rows[i].drugpackagingUnit),//单位
									defaultprice:rows[i].itemPrice,//默认价
									frequencyCode:rows[i].frequencyCode,//频次编码
									frequencyCodeView:rows[i].frequencyName,//频次编码
									usageCode:rows[i].usageCode,//用法名称
									usageCodeView:rows[i].useName,//用法名称
									onceDose:rows[i].doseOnce,//每次服用剂量
									doseUnit:rows[i].doseUnit,//剂量单位
									doseUnitView:drugdoseunitMaps[rows[i].doseUnit],//剂量单位
									mainDrugshow:1,//主药标记
									dateBgn:rows[i].dateBgn,//医嘱开始时间
									dateEnd:rows[i].dateEnd,//医嘱结束时间
									itemNote:rows[i].itemNote,//检查部位
									days:rows[i].useDays,//草药付数
									remark:rows[i].moNote2,//备注
									isDrugShow:rows[i].ty,//是否药品
									isDrugShowView:rows[i].ty==1?'是':'否',
									stackInfoNum:rows[i].packQty//开立数量
								});
							} 
						}
					}else{
						$.messager.alert('提示',"请选择需要添加组套的信息!");
						return;
					}
				}				
			}
			//草药查询
			function adHerbMedicine(){
				var qq = $('#ttta').tabs('getSelected');				
				var tab = qq.panel('options');
				if(tab.title=='长期医嘱'){
					if($('#longDocAdvType').combobox('getValue')==''){
						$.messager.alert('提示',"请先选择医嘱类型!");
						return;
					}
				}else if(tab.title=='临时医嘱'){
					if($('#temDocAdvType').combobox('getValue')==''){
						$.messager.alert('提示',"请先选择医嘱类型!");
						return;
					}
				}
				AdddilogModel("chinMediModleDivId","草药信息","<%=basePath%>inpatient/docAdvManage/getChinMediModle.action",'80%','50%');
			}
			//打印医嘱
			function adPrintAdvice(){
				var qq = $('#ttta').tabs('getSelected');				
				var tab = qq.panel('options');
				if(tab.title=='长期医嘱'){
					var timerStr = Math.random();
					var inpatientNo = $("#inpatientNo").val(); 
					window.open ("<c:url value='/iReport/iReportPrint/iReportToChangqiOrder.action?randomId='/>"+timerStr+"&inpatientNo="+inpatientNo+"&fileName=changqiyizhudan",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
				}else if(tab.title=='临时医嘱'){
					var timerStr = Math.random();
					var inpatientNo = $("#inpatientNo").val();
					window.open ("<c:url value='/iReport/iReportPrint/iReportToLinshiOrder.action?randomId='/>"+timerStr+"&inpatientNo="+inpatientNo+"&fileName=linshiyizhudan",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
				}
			} 
			 /**
			* 回车弹出频次选择窗口
			* @author  zhuxiaolu
			* @param textId 页面上commbox的的id
			* @date 2016-03-22 14:30   
			* @version 1.0
			*/
			
			function popWinToBusinessFrequency(){
				var tempWinPath = "<%=basePath%>popWin/popWinBusinessFrequency/toBusinessFrequencyPopWin.action?textId=adWestMediFreTdId";
				window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -900) +',height='+ (screen.availHeight-370) 

			+',scrollbars,resizable=yes,toolbar=yes')
			}
			
			/**
			* 回车弹出用法选择窗口
			* @author  zhuxiaolu
			* @param textId 页面上commbox的的id
			* @date 2016-03-22 14:30   
			* @version 1.0
			*/

			function popWinToUsa(){
				popWinCommCallBackFn = function(node){
					$("#adWestMediUsaTdId").combobox('setValue',node.id);
					var qq = $('#ttta').tabs('getSelected');				
					var tab = qq.panel('options');
					if(tab.title=='长期医嘱'){
						var index = getIndexForAdDgListA();
						if(index>=0){
							var row = $('#infolistA').datagrid('getSelected');
							if(row!=null&&row.combNo!=null&&row.combNo!=''){
								var rows = $('#infolistA').datagrid('getRows');
								for(var i=0;i<rows.length;i++){
									if(rows[i].combNo==row.combNo){
										var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
										$('#infolistA').datagrid('updateRow',{
											index: indexRow,
											row: {
												usageCode:node.id,
												useName:node.name,
												changeNo:1
											}
										});
									}
								}
							}else{								
								$('#infolistA').datagrid('updateRow',{
									index: index,
									row: {
										usageCode:node.id,
										useName:node.name,
										changeNo:1
									}
								});
							}
						}
					}else if(tab.title=='临时医嘱'){
						var index = getIndexForAdDgList();
						if(index>=0){
							var row = $('#infolistB').datagrid('getSelected');
							if(row!=null&&row.combNo!=null&&row.combNo!=''){
								var rows = $('#infolistB').datagrid('getRows');
								for(var i=0;i<rows.length;i++){
									if(rows[i].combNo==row.combNo){
										var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
										$('#infolistB').datagrid('updateRow',{
											index: indexRow,
											row: {
												usageCode:node.id,
												useName:node.name,
												changeNo:1
											}
										});
									}
								}
							}else{								
								$('#infolistB').datagrid('updateRow',{
									index: index,
									row: {
										usageCode:node.id,
										useName:node.name,
										changeNo:1
									}
								});
							}
						}
					}
				
				};
				var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinXMLList.action?classNameTmp=CodeUseage&textId=adWestMediUsaTdId";
				var aaa=window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth - 800) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
			}
			
			/**
			 * 打开单位界面弹框
			 * @author  zhuxiaolu
			 * @date 2015-5-25 10:53
			 * @version 1.0
			 */
			
			function popWinToNonmedicineencoding(){
				 popWinCommCallBackFn = function(node){
					 $("#adProjectUnitTdId").combobox('setValue',node.name);
					 var index = getIndexForAdDgList();
		   		    	var indexA = getIndexForAdDgListA();
							if(indexA>=0){
								$('#infolistA').datagrid('updateRow',{
									index: indexA,
									row: {									
										priceUnit:node.name
									}
								});
							}
							if(index>=0){
								$('#infolistB').datagrid('updateRow',{
									index: index,
									row: {									
										priceUnit:node.name
									}
								});
							}
				 };
				var tempWinPath ="<%=basePath%>popWin/popWinCode/toCodePopWinXMLList.action?classNameTmp=CodeDrugPackagingunit&textId=adProjectUnitTdId";
				var aaa=window.open (tempWinPath,'newwindow',' left=300,top=200,width='+ (screen.availWidth - 550) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
						
			}
			/**
			* 回车弹出中草药用法选择窗口
			* @author  zhuxiaolu
			* @param textId 页面上commbox的的id
			* @date 2016-03-22 14:30   
			* @version 1.0
			*/

			function popWinToUsaChin(){
				popWinCommCallBackFn = function(node){
					var index = getIndexForAdDgList();
	   		    	var indexA=getIndexForAdDgListA();
	   		    	$("#adChinMediUsaTdId").combobox('setValue',node.id);
	   		    	var qq = $('#ttta').tabs('getSelected');				
					var tab = qq.panel('options');
					if(tab.title=='长期医嘱'){	
						var index = getIndexForAdDgListA();
	    		    	var row = $('#infolistA').datagrid('getSelected');
						if(row!=null&&row.combNo!=null&&row.combNo!=''){
							var rows = $('#infolistA').datagrid('getRows');
							for(var i=0;i<rows.length;i++){
								if(rows[i].combNo==row.combNo){
									var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
									$('#infolistA').datagrid('updateRow',{
										index: indexRow,
										row: {
											usageCode:node.id,
											useName:node.name
										}
									});
								}
							}
						}else{
							if(index>=0){
								$('#infolistA').datagrid('updateRow',{
									index: index,
									row: {
										usageCode:node.id,
										useName:node.name
									}
								});
							}
						}
					}else if(tab.title=='临时医嘱'){
						var index = getIndexForAdDgList();
	    		    	var row = $('#infolistB').datagrid('getSelected');
						if(row!=null&&row.combNo!=null&&row.combNo!=''){
							var rows = $('#infolistB').datagrid('getRows');
							for(var i=0;i<rows.length;i++){
								if(rows[i].combNo==row.combNo){
									var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
									$('#infolistB').datagrid('updateRow',{
										index: indexRow,
										row: {
											usageCode:node.id,
											useName:node.name
										}
									});
								}
							}
						}else{
							if(index>=0){
								$('#infolistB').datagrid('updateRow',{
									index: index,
									row: {
										usageCode:node.id,
										useName:node.name
									}
								});
							}
						}
					}					
				};
				var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinXMLList.action?classNameTmp=CodeUseage&textId=adChinMediUsaTdId";
				var aaa=window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth - 800) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
			
			}
			/**
			 * 打开西药中药备注界面弹框
			 * @author  zhuxiaolu
			 * @date 2015-5-25 10:53
			 * @version 1.0
			 */
			
			function popWinToCodeOpendocadvmark(){
				popWinCommCallBackFn = function(node){
	   		    	$("#adWestMediRemTdId").textbox('setValue',node.name);
	   		    	var index = getIndexForAdDgList();
	   		    	var indexA=getIndexForAdDgListA();
	   		    		if(indexA>=0){							
							$('#infolistA').datagrid('updateRow',{
							index: indexA,
								row: {								
									moNote2:node.name
								}
							});
						}
						if(index>=0){							
							$('#infolistB').datagrid('updateRow',{
							index: index,
								row: {									
									moNote2:node.name
								}
							});
						}
				};
				var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinXMLList.action?classNameTmp=CodeOpendocadvmark&textId=adWestMediRemTdId";
				var aaa=window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth - 800) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
						
			}
			/**
			 * 打开中草药备注界面弹框
			 * @author  zhuxiaolu
			 * @date 2015-5-25 10:53
			 * @version 1.0
			 */
			
			function popWinToChinMediRemTd(){
				 popWinCommCallBackFn = function(node){
						$('#adChinMediRemTdId').combobox('setValue',node.id);
						var qq = $('#ttta').tabs('getSelected');				
						var tab = qq.panel('options');
						if(tab.title=='长期医嘱'){	
							var index = getIndexForAdDgListA();
		    		    	var row = $('#infolistA').datagrid('getSelected');
							if(row!=null&&row.combNo!=null&&row.combNo!=''){
								var rows = $('#infolistA').datagrid('getRows');
								for(var i=0;i<rows.length;i++){
									if(rows[i].combNo==row.combNo){
										var indexRow = $('#infolistA').datagrid('getRowIndex',rows[i]);
										$('#infolistA').datagrid('updateRow',{
											index: indexRow,
											row: {
												moNote2:node.name
											}
										});
									}
								}
							}else{
								if(index>=0){
									$('#infolistA').datagrid('updateRow',{
										index: index,
										row: {
											moNote2:node.name
										}
									});
								}
							}
						}else if(tab.title=='临时医嘱'){
							var index = getIndexForAdDgList();
		    		    	var row = $('#infolistB').datagrid('getSelected');
							if(row!=null&&row.combNo!=null&&row.combNo!=''){
								var rows = $('#infolistB').datagrid('getRows');
								for(var i=0;i<rows.length;i++){
									if(rows[i].combNo==row.combNo){
										var indexRow = $('#infolistB').datagrid('getRowIndex',rows[i]);
										$('#infolistB').datagrid('updateRow',{
											index: indexRow,
											row: {
												moNote2:node.name
											}
										});
									}
								}
							}else{
								if(index>=0){
									$('#infolistB').datagrid('updateRow',{
										index: index,
										row: {
											moNote2:node.name
										}
									});
								}
							}
						}
				 };
				 
				var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinXMLList.action?classNameTmp=CodeOpendocadvmark&textId=adChinMediRemTdId";
				var aaa=window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth - 800) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
						
			}
			/**
			 * 打开中非药品备注界面弹框
			 * @author  zhuxiaolu
			 * @date 2015-5-25 10:53
			 * @version 1.0
			 */
			
			function popWinToNotDrugRem(){
				popWinCommCallBackFn = function(node){
					$("#adNotDrugRemTdId").combobox('setValue',node.id);
					var index = getIndexForAdDgList();
	   		    	var indexA=getIndexForAdDgListA();
	   		    		if(indexA>=0){							
							$('#infolistA').datagrid('updateRow',{
							index: indexA,
								row: {								
									moNote2:node.name
								}
							});
						}
						if(index>=0){							
							$('#infolistB').datagrid('updateRow',{
							index: index,
								row: {									
									moNote2:node.name
								}
							});
						}
				};
				var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinXMLList.action?classNameTmp=CodeOpendocadvmark&textId=adNotDrugRemTdId";
				var aaa=window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth - 800) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')					
			}
			function ProjectName(adProjectTdId,projectName){
				var systemTypeMap = "";//系统类别Map			
				var druggradeMap = "";//药品等级Map			
				var diseasetypeList = "";//疾病分类Map			
				var usemodeMap = "";//用法Map
				var drugpackagingunitMap = "";//包装单位Map
				var nonmedicineencodingMap="";//非药品单位Map
				//查询系统类别
				$.ajax({
					url: "<%=basePath%>inpatient/docAdvManage/querySystemtype.action",				
					type:'post',
					success: function(systemTypedata) {					
						systemTypeMap = systemTypedata;										
					}
				});	
				//查询包装单位
				$.ajax({
					url: "<%=basePath%>inpatient/docAdvManage/queryDrugpackagingunit.action",				
					type:'post',
					success: function(drugpackagingunitdata) {					
						drugpackagingunitMap= drugpackagingunitdata;										
					}
				});
				//查询非药品单位
				$.ajax({
					url: "<%=basePath%>inpatient/docAdvManage/queryNonmedicineencoding.action",				
					type:'post',
					success: function(nonmedicineencodingdata) {					
						nonmedicineencodingMap = nonmedicineencodingdata;										
					}
				});
				//查询药品等级
				$.ajax({
					url: "<%=basePath%>inpatient/docAdvManage/queryDruggrade.action",				
					type:'post',
					success: function(druggradedata) {					
						druggradeMap = druggradedata;							
					}
				});	
				//查询疾病分类
				$.ajax({
					url: "<%=basePath%>inpatient/docAdvManage/queryDiseasetype.action",				
					type:'post',
					success: function(diseasetypedata) {					
						diseasetypeList = diseasetypedata;					
					}
				});	
				$('#adProjectNameTdId').combogrid({        
				    url:'<%=basePath%>inpatient/docAdvManage/queryDrugOrUndrugInfos.action', 
				    queryParams:{'inpatientOrder.classCode':adProjectTdId,'inpatientOrder.className':projectName},	
				    pagination:true,
	        		required:true,
	        		pageSize:10,
					pageList:[10,30,50,80,100],
	        		panelWidth:800,
	        		panelHeight:300,
		            idField:'itemCode',    
		            textField:'name', 
		            mode:'remote',
				    columns:[[    
				        {field:'name',title:'名称',width:200,styler:gradeStyler},    
				        {field:'sysType',title:'类别',width:100,formatter:sysTypeFamater},    
				        {field:'specs',title:'规格',width:120},    
				        {field:'defaultprice',title:'价格',width:100}, 
				        {field:'drugPackagingunit',title:'单位',width:60,formatter:drugpackagingunitFamater},    
				        {field:'drugGrade',title:'医保标记',width:100,formatter:speidFamater},    
				        {field:'undrugIsprovincelimit',title:'是否省限制',width:120,hidden:true},    
				        {field:'undrugIscitylimit',title:'是否市限制',width:100,hidden:true},
				        {field:'undrugIsownexpense',title:'是否自费',width:60,hidden:true},    
				        {field:'undrugIsspecificitems',title:'是否特定项目',width:100,hidden:true},    
				        {field:'inputCode',title:'自定义码',width:120},    
				        {field:'drugCommonname',title:'药品通用名',width:100},
				        {field:'storeSum',title:'库存可用数量',width:60},    
				        {field:'dept',title:'执行科室',width:100,formatter:implDepartmentFamater},    
				        {field:'inspectionSite',title:'检查检体',width:120},    
				        {field:'diseaseClassification',title:'疾病分类',width:100,formatter:diseasetypeFamater},
				        {field:'specialtyName',title:'专科名称',width:120},    
				        {field:'medicalHistory',title:'病史及检查',width:100},
				        {field:'requirements',title:'检查要求',width:60},    
				        {field:'notes',title:'注意事项',width:100},    
				        {field:'gbcode',title:'国家基本药物编码',width:120,hidden:true},    
				        {field:'drugInstruction',title:'说明书',width:100,hidden:true},
				        {field:'drugOncedosage',title:'一次用量',width:120,hidden:true},    
				        {field:'drugDoseunit',title:'剂量单位',width:100,hidden:true,formatter:drugpdoseunitFamaters},
				        {field:'drugFrequency',title:'频次',width:60,hidden:true,formatter:drugfrequencyFamater},    
				        {field:'lowSum',title:'最低库存',width:100,hidden:true},    
				        {field:'drugDosageform',title:'剂型代码',width:120,hidden:true},    
				        {field:'drugType',title:'药品类别',width:100,hidden:true},
				        {field:'drugNature',title:'药品性质',width:120,hidden:true},    
				        {field:'drugRetailprice',title:'零售价',width:100,hidden:true},
				        {field:'remark',title:'备注',width:60,hidden:true},    
				        {field:'drugUsemode',title:'使用方法',width:100,hidden:true,formatter:drugusemodeFamater},    
				        {field:'drugBasicdose',title:'基本剂量',width:120,hidden:true},    
				        {field:'undrugIsinformedconsent',title:'是否知情同意书',width:100,hidden:true}
				    ]],
				    onClickRow:function(rowIndex, rowData){
						var bool = false;
						if($('#adWestMediUrgTdId').is(':checked')){//判断复选框是否被选中
							bool = true;
						}
						var userId = $('#userid').val();
						var mytime = new Date().format("yyyy-MM-dd hh:mm:ss");
						var qq = $('#ttta').tabs('getSelected');				
						var tab = qq.panel('options');
						if(tab.title=='长期医嘱'){
							if(rowData.ty=='1'){//判断是药品还是非药品
								var drugNature= rowData.drugNature!=null?rowData.drugNature:'';
								$.ajax({
									url: "<%=basePath%>inpatient/docAdvManage/queryAdvdrugnature.action",		
									data:'proInfoVo.drugNature='+drugNature,
									type:'post',
									success: function(bdata) {
									var drugNatureData =  bdata;
									if(drugNatureData.length==0){	
										if(rowData.drugRestrictionofantibiotic==3){//-----b
											$.ajax({
												url: "<%=basePath%>inpatient/docAdvManage/queryAuditInfo.action",		
												data:'userId='+userId+'&parameterCode='+'yzshzj',
												type:'post',
												success: function(auditdata) {
													if(auditdata==0){//-----a
														$.extend($.messager.defaults,{  
													        ok:"是",  
													        cancel:"否"  
													    });  
														jQuery.messager.confirm('确认信息',rowData.name+'开立需要上级医生审核,确认开立吗?',function(e){   
															if(e){ 	                    
																if((rowData.sysType=='18')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
																	jQuery.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(event){   
																		if(event){
																			var lastIndex = $('#infolistA').datagrid('appendRow',{
																				changeNo:1,
																				id:'',
																				typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
																				typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
																				classCode:rowData.sysType,//系统类别Id
																				className:systemTypeMap[rowData.sysType],//系统类别名称
																				itemCode:rowData.itemCode,//医嘱Id
																				itemName:rowData.name,//医嘱名称
																				combNo:'',//组
																				qtyTot:1,//总量
																				priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																				drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																				packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量		
																				minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																				doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
//																				doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
																				doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																				specs:rowData.specs!=null?rowData.specs:'',//规格
																				doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																				drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
																				drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																				itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																				useDays:0,//付数
																				frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																				frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																				usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//Id	
																				useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
																				dateBgn:mytime,//开始时间 
																				dateEnd:'',//停止时间
																				moDate:mytime,//开立时间 
																				docCode:'',//开立医生代码
																				docName:$('#userName').val(),//开立医生名称
																				execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																				execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																				emcFlag:0,//加急标记
																				isUrgent:0,//急
																				labCode:'',//样本类型
																				itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																				pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																				moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																				recUsernm:$('#recUserName').val(),//录入人
																				listDpcd:$('#deptId').val(),//开立科室 
																				updateUser:'',//停止人 
																				baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																				permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																				hypotest:2,//皮试Id
																				hypotestName:'需要皮试，未做',//皮试名称
																				itemType:rowData.ty,//药品非药品标识
																				moStat:-1,//医嘱状态
																				sortId:sortIdCreate()//顺序号							
																			}).datagrid('getRows').length-1;
																			$('#infolistA').datagrid('selectRow',lastIndex);					

	
																			$('#adWestMediModlDivId').dialog('close');
																		}else{
																			var lastIndex = $('#infolistA').datagrid('appendRow',{
																				changeNo:1,
																				id:'',
																				typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
																				typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
																				classCode:rowData.sysType,//系统类别Id
																				className:systemTypeMap[rowData.sysType],//系统类别名称
																				itemCode:rowData.itemCode,//医嘱Id
																				itemName:rowData.name,//医嘱名称
																				combNo:'',//组
																				qtyTot:1,//总量
																				priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																				drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																				packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																				minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																				doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
																				doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																				specs:rowData.specs!=null?rowData.specs:'',//规格
																				doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																				drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
																				drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																				itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																				useDays:0,//付数
																				frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																				frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																				usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
																				useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
																				dateBgn:mytime,//开始时间 
																				dateEnd:'',//停止时间
																				moDate:mytime,//开立时间 
																				docCode:'',//开立医生代码
																				docName:$('#userName').val(),//开立医生
																				execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																				execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																				emcFlag:0,//加急标记
																				isUrgent:0,//急
																				labCode:'',//样本类型
																				itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																				//openDoctor:'当前医生',//
																				pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																				moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																				recUsernm:$('#recUserName').val(),//录入人
																				listDpcd:$('#deptId').val(),//开立科室 
																				updateUser:'',//停止人 
																				baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																				permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																				hypotest:1,//皮试Id
																				hypotestName:'不需要皮试',
																				itemType:rowData.ty,//药品非药品标识
																				moStat:-1,//医嘱状态
																				sortId:sortIdCreate()//顺序号							

	
																			}).datagrid('getRows').length-1;
																			$('#infolistA').datagrid('selectRow',lastIndex);					

	
																			$('#adWestMediModlDivId').dialog('close');
																		}   
																		});
																}else{//不是西药，不用考虑皮试
																	var lastIndex = $('#infolistA').datagrid('appendRow',{
																		changeNo:1,
																		id:'',
																		typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
																		typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
																		classCode:rowData.sysType,//系统类别Id
																		className:systemTypeMap[rowData.sysType],//系统类别名称
																		itemCode:rowData.itemCode,//医嘱Id
																		itemName:rowData.name,//医嘱名称
																		combNo:'',//组
																		qtyTot:1,//总量
																		priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																		drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																		packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																		minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																		doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
																		doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																		specs:rowData.specs!=null?rowData.specs:'',//规格
																		doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																		drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
																		drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																		itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																		useDays:0,//付数
																		frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																		frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																		usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
																		useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
																		dateBgn:mytime,//开始时间 
																		dateEnd:'',//停止时间
																		moDate:mytime,//开立时间 
																		docCode:'',//开立医生代码
																		docName:$('#userName').val(),//开立医生
																		execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																		execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																		emcFlag:0,//加急标记
																		isUrgent:0,//急
																		labCode:'',//样本类型
																		itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																		pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																		moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																		recUsernm:$('#recUserName').val(),//录入人
																		listDpcd:$('#deptId').val(),//开立科室 
																		updateUser:'',//停止人 
																		baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																		permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																		hypotest:'',//皮试Id
																		hypotestName:'',//皮试名称
																		itemType:rowData.ty,//药品非药品标识
																		moStat:-1,//医嘱状态
																		sortId:sortIdCreate()//顺序号								
																	}).datagrid('getRows').length-1;
																	$('#infolistA').datagrid('selectRow',lastIndex);						
																	$('#adWestMediModlDivId').dialog('close');
																}														

	
															}
														});
													}else{
														if((rowData.sysType=='402880ae51a4ade90151a4dfa02e0008')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
															jQuery.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(event){   
																if(event){  									
																	var lastIndex = $('#infolistA').datagrid('appendRow',{
																		changeNo:1,
																		id:'',
																		typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
																		typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
																		classCode:rowData.sysType,//系统类别id
																		className:systemTypeMap[rowData.sysType],//系统类别名称
																		itemCode:rowData.itemCode,//医嘱名称Id
																		itemName:rowData.name,//医嘱名称
																		combNo:'',//组
																		qtyTot:1,//总量
																		priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																		drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																		packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																		minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																		doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
																		doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																		specs:rowData.specs!=null?rowData.specs:'',//规格
																		doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																		drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
																		drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																		itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																		useDays:0,//付数
																		frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																		frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																		usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
																		useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
																		dateBgn:mytime,//开始时间 
																		dateEnd:'',//停止时间
																		moDate:mytime,//开立时间
																		docCode:'',//开立医生代码
																		docName:$('#userName').val(),//开立医生
																		execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																		execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																		emcFlag:0,//加急标记
																		isUrgent:0,//急
																		labCode:'',//样本类型
																		itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																		pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																		moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																		recUsernm:$('#recUserName').val(),//录入人
																		listDpcd:$('#deptId').val(),//开立科室 
																		updateUser:'',//停止人 
																		baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																		permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																		hypotest:2,
																		hypotestName:'需要皮试，未做',
																		itemType:rowData.ty,//药品非药品标识
																		moStat:0,//医嘱状态
																		sortId:sortIdCreate()//顺序号							
																	}).datagrid('getRows').length-1;
																	$('#infolistA').datagrid('selectRow',lastIndex);						
																	$('#adWestMediModlDivId').dialog('close');
																}else{
																	var lastIndex = $('#infolistA').datagrid('appendRow',{
																		changeNo:1,
																		id:'',
																		typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
																		typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
																		classCode:rowData.sysType,//系统类别Id
																		className:systemTypeMap[rowData.sysType],//系统类别名称
																		itemCode:rowData.itemCode,//医嘱名称Id
																		itemName:rowData.name,//医嘱名称
																		combNo:'',//组
																		qtyTot:1,//总量
																		priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																		drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																		packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																		minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																		doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
																		doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）													
																		specs:rowData.specs!=null?rowData.specs:'',//规格
																		doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																		drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
																		drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																		itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																		useDays:0,//付数
																		frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																		frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																		usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
																		useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
																		dateBgn:mytime,//开始时间 
																		dateEnd:'',//停止时间
																		moDate:mytime,//开立时间 
																		docCode:'',//开立医生代码
																		docName:$('#userName').val(),//开立医生
																		execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																		execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																		emcFlag:0,//加急标记
																		isUrgent:0,//急
																		labCode:'',//样本类型
																		itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																		pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																		moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																		recUsernm:$('#recUserName').val(),//录入人
																		listDpcd:$('#deptId').val(),//开立科室 
																		updateUser:'',//停止人 
																		baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																		permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																		hypotest:1,//皮试Id
																		hypotestName:'不需要皮试',//皮试名称
																		itemType:rowData.ty,//药品非药品标识
																		moStat:0,//医嘱状态
																		sortId:sortIdCreate()//顺序号								
																	}).datagrid('getRows').length-1;
																	$('#infolistA').datagrid('selectRow',lastIndex);						
																	$('#adWestMediModlDivId').dialog('close');
																}   
																});
														}else{
															var lastIndex = $('#infolistA').datagrid('appendRow',{
																changeNo:1,
																id:'',
																typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
																typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
																classCode:rowData.sysType,//医嘱类别Id
																className:systemTypeMap[rowData.sysType],//医嘱类别名称
																itemCode:rowData.itemCode,//医嘱名称Id
																itemName:rowData.name,//医嘱名称
																combNo:'',//组
																qtyTot:1,//总量
																priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
																doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																specs:rowData.specs!=null?rowData.specs:'',//规格
																doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
																drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																useDays:0,//付数
																frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',//用法Id		
																useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
																dateBgn:mytime,//开始时间 
																dateEnd:'',//停止时间
																moDate:mytime,//开立时间 
																docCode:'',//开立医生代码
																docName:$('#userName').val(),//开立医生
																execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																emcFlag:0,//加急标记
																isUrgent:0,//急
																labCode:'',//样本类型
																itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																recUsernm:$('#recUserName').val(),//录入人
																listDpcd:$('#deptId').val(),//开立科室 
																updateUser:'',//停止人 
																baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																hypotest:'',//皮试Id
																hypotestName:'',//皮试名称
																itemType:rowData.ty,//药品非药品标识
																moStat:0,//医嘱状态
																sortId:sortIdCreate()//顺序号						
															}).datagrid('getRows').length-1;
															$('#infolistA').datagrid('selectRow',lastIndex);						
															$('#adWestMediModlDivId').dialog('close');
														}														
													}													
												}
											});
										}
										else{
											if((rowData.sysType=='402880ae51a4ade90151a4dfa02e0008')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
												jQuery.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(event){   
													if(event){  									
														var lastIndex = $('#infolistA').datagrid('appendRow',{
															changeNo:1,
															id:'',
															typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
															typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
															classCode:rowData.sysType,
															className:systemTypeMap[rowData.sysType],
															itemCode:rowData.itemCode,//医嘱名称Id
															itemName:rowData.name,//医嘱名称
															combNo:'',//组
															qtyTot:1,//总量
															priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
															drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
															packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
															minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
															doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
															doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
															specs:rowData.specs!=null?rowData.specs:'',
															doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
															drugType:rowData.drugType!=null?rowData.drugType:'',
															drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
															itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
															useDays:0,//付数
															frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
															frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
															usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
															useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',
															dateBgn:mytime,//开始时间 
															dateEnd:'',//停止时间
															moDate:mytime,//开立时间 
															docCode:'',//开立医生代码
															docName:$('#userName').val(),//开立医生
															execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
															execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
															emcFlag:0,//加急标记
															isUrgent:0,//急
															labCode:'',//样本类型
															itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
															pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
															moNote2:rowData.remark!=null?rowData.remark:'',//备注 
															recUsernm:$('#recUserName').val(),//录入人
															listDpcd:$('#deptId').val(),//开立科室 
															updateUser:'',//停止人 
															baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
															permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
															hypotest:2,//皮试Id
															hypotestName:'需要皮试，未做',//皮试名称
															itemType:rowData.ty,//药品非药品标识
															moStat:0,//医嘱状态
															sortId:sortIdCreate()//顺序号							
														}).datagrid('getRows').length-1;
														$('#infolistA').datagrid('selectRow',lastIndex);						
														$('#adWestMediModlDivId').dialog('close');
													}else{ 
														var lastIndex = $('#infolistA').datagrid('appendRow',{
															changeNo:1,
															id:'',
															typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
															typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
															classCode:rowData.sysType,//系统类别Id
															className:systemTypeMap[rowData.sysType],//系统类别名称
															itemCode:rowData.itemCode,//医嘱名称Id
															itemName:rowData.name,//医嘱名称
															combNo:'',//组
															qtyTot:1,//总量
															priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
															drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
															packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
															minUnit:rowData.unit!=null?rowData.unit:'',//最小单位		
															doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
															doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
															specs:rowData.specs!=null?rowData.specs:'',//规格
															doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
															drugType:rowData.drugType!=null?rowData.drugType:'',
															drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
															itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
															useDays:0,//付数
															frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
															frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
															usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',//用法Id		
															useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
															dateBgn:mytime,//开始时间 
															dateEnd:'',//停止时间
															moDate:mytime,//开立时间 
															docCode:'',//开立医生代码
															docName:$('#userName').val(),//开立医生
															execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
															execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
															emcFlag:0,//加急标记
															isUrgent:0,//急
															labCode:'',//样本类型
															itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
															pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
															moNote2:rowData.remark!=null?rowData.remark:'',//备注 
															recUsernm:$('#recUserName').val(),//录入人
															listDpcd:$('#deptId').val(),//开立科室 
															updateUser:'',//停止人 
															baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
															permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
															hypotest:1,//皮试Id
															hypotestName:'不需要皮试',//皮试名称
															itemType:rowData.ty,//药品非药品标识
															moStat:0,//医嘱状态
															sortId:sortIdCreate()//顺序号							
														}).datagrid('getRows').length-1;
														$('#infolistA').datagrid('selectRow',lastIndex);						
														$('#adWestMediModlDivId').dialog('close');
													}   
													});
											}else{
												var lastIndex = $('#infolistA').datagrid('appendRow',{
													changeNo:1,
													id:'',
													typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
													typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
													classCode:rowData.sysType,
													className:systemTypeMap[rowData.sysType],
													itemCode:rowData.itemCode,
													itemName:rowData.name,//医嘱名称
													combNo:'',//组
													qtyTot:1,//总量
													priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
													drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
													packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
													minUnit:rowData.unit!=null?rowData.unit:'',//最小单位		
													doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
													doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
													specs:rowData.specs!=null?rowData.specs:'',
													doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
													drugType:rowData.drugType!=null?rowData.drugType:'',
													drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
													itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
													useDays:0,//付数
													frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
													frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
													usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
													useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',
													dateBgn:mytime,//开始时间 
													dateEnd:'',//停止时间
													moDate:mytime,//开立时间 
													docCode:'',//开立医生代码
													docName:$('#userName').val(),//开立医生
													execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
													execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
													emcFlag:0,//加急标记
													isUrgent:0,//急
													labCode:'',//样本类型
													itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
													pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
													moNote2:rowData.remark!=null?rowData.remark:'',//备注 
													recUsernm:$('#recUserName').val(),//录入人
													listDpcd:$('#deptId').val(),//开立科室 
													updateUser:'',//停止人 
													baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
													permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
													hypotest:'',//皮试Id
													hypotestName:'',//皮试名称
													itemType:rowData.ty,//药品非药品标识
													moStat:0,//医嘱状态
													sortId:sortIdCreate()//顺序号								
												}).datagrid('getRows').length-1;
												$('#infolistA').datagrid('selectRow',lastIndex);						
												$('#adWestMediModlDivId').dialog('close');
											}											
										}																		
									}else{
										$.messager.alert('提示',"限制药品性质的药品不可以开立！");
									}
								}
								});
							}
							else{//非药品
								if($('#westMediInfoViewTypeId').val()=='402880ae51a4ade90151a4df08520005'){//类型为护理级别
									var lastIndex = $('#infolistA').datagrid('appendRow',{
										changeNo:1,
										id:'',
										typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
										typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
										classCode:rowData.sysType,
										className:systemTypeMap[rowData.sysType],
										itemCode:rowData.itemCode,//医嘱Id
										itemName:rowData.name,//医嘱名称
										combNo:'',//组
										qtyTot:1,//总量
										priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
										drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
										packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
										minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
										doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:0,//每次量
										doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
										specs:rowData.specs!=null?rowData.specs:'',//规格
										doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
										drugType:rowData.drugType!=null?rowData.drugType:'',
										drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
										itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
										useDays:0,//付数
										frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
										frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
										usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
										useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',
										dateBgn:mytime,//开始时间 
										dateEnd:'',//停止时间
										moDate:mytime,//开立时间 
										docCode:'',//开立医生代码
										docName:$('#userName').val(),//开立医生
										execDpcd:$('#nurseCellCode').val(),//执行科室 id
										execDpnm:implDepartmentMap[$('#nurseCellCode').val()]!=null?implDepartmentMap[$('#nurseCellCode').val()]:'',//执行科室 
										emcFlag:0,//加急标记
										isUrgent:0,//急
										labCode:'',//样本类型
										itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
										pharmacyCode:'',//扣库科室
										moNote2:rowData.remark!=null?rowData.remark:'',//备注 
										recUsernm:$('#recUserName').val(),//录入人
										listDpcd:$('#deptId').val(),//开立科室 
										updateUser:'',//停止人 
										baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
										permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
										hypotest:'',//皮试Id
										hypotestName:'',//皮试									
										itemType:rowData.ty,//药品非药品标识
										moStat:0,//医嘱状态
										sortId:sortIdCreate()//顺序号								
									}).datagrid('getRows').length-1;
									$('#infolistA').datagrid('selectRow',lastIndex);						
									$('#adWestMediModlDivId').dialog('close');
								}else{
									var lastIndex = $('#infolistA').datagrid('appendRow',{
										changeNo:1,
										id:'',
										typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
										typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
										classCode:rowData.sysType,
										className:systemTypeMap[rowData.sysType],
										itemCode:rowData.itemCode,//医嘱Id
										itemName:rowData.name,//医嘱名称
										combNo:'',//组
										qtyTot:1,//总量
										priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
										drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
										packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
										minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
										doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:0,//每次量
										doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
										specs:rowData.specs!=null?rowData.specs:'',//规格
										doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
										drugType:rowData.drugType!=null?rowData.drugType:'',
										drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
										itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
										useDays:0,//付数
										frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
										frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
										usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
										useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',
										dateBgn:mytime,//开始时间 
										dateEnd:'',//停止时间
										moDate:mytime,//开立时间 
										docCode:'',//开立医生代码
										docName:$('#userName').val(),//开立医生
										execDpcd:$('#deptId').val(),//执行科室 id
										execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
										emcFlag:0,//加急标记
										isUrgent:0,//急
										labCode:'',//样本类型
										itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
										pharmacyCode:'',//扣库科室
										moNote2:rowData.remark!=null?rowData.remark:'',//备注 
										recUsernm:$('#recUserName').val(),//录入人
										listDpcd:$('#deptId').val(),//开立科室 
										updateUser:'',//停止人 
										baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
										permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
										hypotest:'',//皮试Id
										hypotestName:'',//皮试									
										itemType:rowData.ty,//药品非药品标识
										moStat:0,//医嘱状态
										sortId:sortIdCreate()//顺序号								
									}).datagrid('getRows').length-1;
									$('#infolistA').datagrid('selectRow',lastIndex);						
									$('#adWestMediModlDivId').dialog('close');
								}								
							}									
						}
						else{//临时医嘱						
							if(rowData.ty=='1'){//判断是药品还是非药品															
								if(rowData.drugRestrictionofantibiotic==3){//-----b
									$.ajax({
										url: "<%=basePath%>inpatient/docAdvManage/queryAuditInfo.action",		
										data:'userId='+userId+'&parameterCode='+'yzshzj',
										type:'post',
										success: function(auditdata) {
											if(auditdata==0){//-----a
												$.extend($.messager.defaults,{  
											        ok:"是",  
											        cancel:"否"  
											    });  
												jQuery.messager.confirm('确认信息',rowData.name+'开立需要上级医生审核,确认开立吗?',function(e){   
													if(e){ 
														if((rowData.sysType=='402880ae51a4ade90151a4dfa02e0008')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
															jQuery.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(event){   
																if(event){  									
																	var lastIndex = $('#infolistB').datagrid('appendRow',{
																		changeNo:1,
																		id:'',
																		itemCode:'',//特殊标识
																		typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
																		typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
																		classCode:rowData.sysType,
																		className:systemTypeMap[rowData.sysType],
																		itemCode:rowData.itemCode,//医嘱Id
																		itemName:rowData.name,//医嘱名称
																		combNo:'',//组
																		qtyTot:1,//总量
																		priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																		drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																		packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																		minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																		doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
																		doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																		specs:rowData.specs!=null?rowData.specs:'',//规格
																		doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																		drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
																		drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																		itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																		useDays:0,//付数
																		frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																		frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																		usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
																		useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
																		dateBgn:mytime,//开始时间 
																		dateEnd:'',//停止时间
																		moDate:mytime,//开立时间 
																		docCode:'',//开立医生代码
																		docName:$('#userName').val(),//开立医生
																		execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																		execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																		emcFlag:0,//加急标记
																		isUrgent:0,//急
																		labCode:'',//样本类型
																		itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																		pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																		moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																		recUsernm:$('#recUserName').val(),//录入人
																		listDpcd:$('#deptId').val(),//开立科室 
																		updateUser:'',//停止人 
																		baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																		permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																		hypotest:2,//皮试Id
																		hypotestName:'需要皮试，未做',//皮试名称
																		itemType:rowData.ty,//药品非药品标识
																		moStat:-1,//医嘱状态
																		sortId:sortIdCreate()//顺序号								
																	}).datagrid('getRows').length-1;
																	$('#infolistB').datagrid('selectRow',lastIndex);						
																	$('#adWestMediModlDivId').dialog('close');
																}else{ 
																	var lastIndex = $('#infolistB').datagrid('appendRow',{
																		changeNo:1,
																		id:'',
																		itemCode:'',//特殊标识
																		typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
																		typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
																		classCode:rowData.sysType,
																		className:systemTypeMap[rowData.sysType],
																		itemCode:rowData.itemCode,
																		itemName:rowData.name,//医嘱名称
																		combNo:'',//组
																		qtyTot:1,//总量
																		priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																		drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																		packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																		minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																		doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
																		doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																		specs:rowData.specs!=null?rowData.specs:'',//规格
																		doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																		drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
																		drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																		itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																		useDays:0,//付数
																		frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																		frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																		usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
																		useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
																		dateBgn:mytime,//开始时间 
																		dateEnd:'',//停止时间
																		moDate:mytime,//开立时间 
																		docCode:'',//开立医生代码
																		docName:$('#userName').val(),//开立医生
																		execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																		execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																		emcFlag:0,//加急标记
																		isUrgent:0,//急
																		labCode:'',//样本类型
																		itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																		pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																		moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																		recUsernm:$('#recUserName').val(),//录入人
																		listDpcd:$('#deptId').val(),//开立科室 
																		updateUser:'',//停止人 
																		baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																		permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																		hypotest:1,//皮试Id
																		hypotestName:'不需要皮试',//皮试名称
																		itemType:rowData.ty,//药品非药品标识
																		moStat:-1,//医嘱状态
																		sortId:sortIdCreate()//顺序号								
																	}).datagrid('getRows').length-1;
																	$('#infolistB').datagrid('selectRow',lastIndex);						
																	$('#adWestMediModlDivId').dialog('close');
																}   
																});
														}else{
															var lastIndex = $('#infolistB').datagrid('appendRow',{
																changeNo:1,
																id:'',
																itemCode:'',//特殊标识
																typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
																typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
																classCode:rowData.sysType,
																className:systemTypeMap[rowData.sysType],
																itemCode:rowData.itemCode,//医嘱名称Id
																itemName:rowData.name,//医嘱名称
																combNo:'',//组
																qtyTot:1,//总量
																priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
																doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																specs:rowData.specs!=null?rowData.specs:'',//规格
																doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																drugType:rowData.drugType!=null?rowData.drugType:'',
																drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																useDays:0,//付数
																frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
																useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',
																dateBgn:mytime,//开始时间 
																dateEnd:'',//停止时间
																moDate:mytime,//开立时间 
																docCode:'',//开立医生代码
																docName:$('#userName').val(),//开立医生
																execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																emcFlag:0,//加急标记
																isUrgent:0,//急
																labCode:'',//样本类型
																itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																recUsernm:$('#recUserName').val(),//录入人
																listDpcd:$('#deptId').val(),//开立科室 
																updateUser:'',//停止人 
																baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																hypotest:'',//皮试Id
																hypotestName:'',//皮试名称
																itemType:rowData.ty,//药品非药品标识
																moStat:-1,//医嘱状态
																sortId:sortIdCreate()//顺序号								
															}).datagrid('getRows').length-1;
															$('#infolistB').datagrid('selectRow',lastIndex);						
															$('#adWestMediModlDivId').dialog('close');
														}												
													}
												});
											}else{
												if((rowData.sysType=='402880ae51a4ade90151a4dfa02e0008')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
													jQuery.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(event){   
														if(event){  									
															var lastIndex = $('#infolistB').datagrid('appendRow',{
																changeNo:1,
																id:'',
																itemCode:'',//特殊标识
																typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
																typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
																classCode:rowData.sysType,
																className:systemTypeMap[rowData.sysType],
																itemCode:rowData.itemCode,//医嘱名称Id
																itemName:rowData.name,//医嘱名称
																combNo:'',//组
																qtyTot:1,//总量
																priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
																doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																specs:rowData.specs!=null?rowData.specs:'',
																doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																drugType:rowData.drugType!=null?rowData.drugType:'',
																drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																useDays:0,//付数
																frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
																useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',
																dateBgn:mytime,//开始时间 
																dateEnd:'',//停止时间
																moDate:mytime,//开立时间 
																docCode:'',//开立医生代码
																docName:$('#userName').val(),//开立医生
																execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																emcFlag:0,//加急标记
																isUrgent:0,//急
																labCode:'',//样本类型
																itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																//openDoctor:'当前医生',//
																pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																recUsernm:$('#recUserName').val(),//录入人
																listDpcd:$('#deptId').val(),//开立科室 
																updateUser:'',//停止人 
																baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																hypotest:2,
																hypotestName:'需要皮试，未做',
																itemType:rowData.ty,//药品非药品标识
																moStat:0,
																sortId:sortIdCreate()//顺序号								
															}).datagrid('getRows').length-1;
															$('#infolistB').datagrid('selectRow',lastIndex);						
															$('#adWestMediModlDivId').dialog('close');
														}else{ 
															var lastIndex = $('#infolistB').datagrid('appendRow',{
																changeNo:1,
																id:'',
																itemCode:'',//特殊标识
																typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
																typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
																classCode:rowData.sysType,
																className:systemTypeMap[rowData.sysType],
																itemCode:rowData.itemCode,
																itemName:rowData.name,//医嘱名称
																combNo:'',//组
																qtyTot:1,//总量
																priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
																drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
																packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
																minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
																doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
																doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
																specs:rowData.specs!=null?rowData.specs:'',
																doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
																drugType:rowData.drugType!=null?rowData.drugType:'',
																drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
																itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
																useDays:0,//付数
																frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
																frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
																usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
																useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',
																dateBgn:mytime,//开始时间 
																dateEnd:'',//停止时间
																moDate:mytime,//开立时间 
																docCode:'',//开立医生代码
																docName:$('#userName').val(),//开立医生
																execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
																execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
																emcFlag:0,//加急标记
																isUrgent:0,//急
																labCode:'',//样本类型
																itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
																pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
																moNote2:rowData.remark!=null?rowData.remark:'',//备注 
																recUsernm:$('#recUserName').val(),//录入人
																listDpcd:$('#deptId').val(),//开立科室 
																updateUser:'',//停止人 
																baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
																permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
																hypotest:1,
																hypotestName:'不需要皮试',
																itemType:rowData.ty,//药品非药品标识
																moStat:0,
																sortId:sortIdCreate()//顺序号								
															}).datagrid('getRows').length-1;
															$('#infolistB').datagrid('selectRow',lastIndex);						
															$('#adWestMediModlDivId').dialog('close');
														}   
														});
												}else{
													var lastIndex = $('#infolistB').datagrid('appendRow',{
														changeNo:1,
														id:'',
														itemCode:'',//特殊标识
														typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
														typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
														classCode:rowData.sysType,
														className:systemTypeMap[rowData.sysType],
														itemCode:rowData.itemCode,
														itemName:rowData.name,//医嘱名称
														combNo:'',//组
														qtyTot:1,//总量
														priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
														drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
														packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
														minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
														doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
														doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
														specs:rowData.specs!=null?rowData.specs:'',
														doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
														drugType:rowData.drugType!=null?rowData.drugType:'',
														drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
														itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
														useDays:0,//付数
														frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
														frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
														usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
														useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',
														dateBgn:mytime,//开始时间 
														dateEnd:'',//停止时间
														moDate:mytime,//开立时间 
														docCode:'',//开立医生代码
														docName:$('#userName').val(),//开立医生
														execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
														execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
														emcFlag:0,//加急标记
														isUrgent:0,//急
														labCode:'',//样本类型
														itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
														pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
														moNote2:rowData.remark!=null?rowData.remark:'',//备注 
														recUsernm:$('#recUserName').val(),//录入人
														listDpcd:$('#deptId').val(),//开立科室 
														updateUser:'',//停止人 
														baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
														permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
														hypotest:'',//皮试Id
														hypotestName:'',//皮试名称
														itemType:rowData.ty,//药品非药品标识
														moStat:0,//医嘱状态
														sortId:sortIdCreate()//顺序号								
													}).datagrid('getRows').length-1;
													$('#infolistB').datagrid('selectRow',lastIndex);						
													$('#adWestMediModlDivId').dialog('close');
												}												
											}//-----a														
										}
									});
								}
								else{
									if((rowData.sysType=='402880ae51a4ade90151a4dfa02e0008')&&(rowData.drugIstestsensitivity==1||rowData.drugIstestsensitivity==2)){//判断是否为西药
										jQuery.messager.confirm('确认信息',rowData.name+'是否需要皮试?',function(event){   
											if(event){  									
												var lastIndex = $('#infolistB').datagrid('appendRow',{
													changeNo:1,
													id:'',
													itemCode:'',//特殊标识
													typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
													typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
													classCode:rowData.sysType,
													className:systemTypeMap[rowData.sysType],
													itemCode:rowData.itemCode,
													itemName:rowData.name,//医嘱名称
													combNo:'',//组
													qtyTot:1,//总量
													priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
													drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
													packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
													minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
													doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
		//											doseOnces:(rowData.drugOncedosage!=null?rowData.drugOncedosage:1)/parseFloat(rowData.drugBasicdose)+drugpackagingunitMap[rowData.unit]+'='+rowData.drugOncedosage!=null?rowData.drugOncedosage:1+drugdoseunitMap[rowData.drugDoseunit],//每次量计算											
													doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
													specs:rowData.specs!=null?rowData.specs:'',
													doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
													drugType:rowData.drugType!=null?rowData.drugType:'',
													drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
													itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
													useDays:0,//付数
													frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
													frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
													usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
													useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
													dateBgn:mytime,//开始时间 
													dateEnd:'',//停止时间
													moDate:mytime,//开立时间 
													docCode:'',//开立医生代码
													docName:$('#userName').val(),//开立医生
													execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
													execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
													emcFlag:0,//加急标记
													isUrgent:0,//急
													labCode:'',//样本类型
													itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
													pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
													moNote2:rowData.remark!=null?rowData.remark:'',//备注 
													recUsernm:$('#recUserName').val(),//录入人
													listDpcd:$('#deptId').val(),//开立科室 
													updateUser:'',//停止人 
													baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
													permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
													hypotest:2,
													hypotestName:'需要皮试，未做',
													itemType:rowData.ty,//药品非药品标识
													moStat:0,
													sortId:sortIdCreate()//顺序号								
												}).datagrid('getRows').length-1;
												$('#infolistB').datagrid('selectRow',lastIndex);						
												$('#adWestMediModlDivId').dialog('close');
											}else{ 
												var lastIndex = $('#infolistB').datagrid('appendRow',{
													changeNo:1,
													id:'',
													itemCode:'',//特殊标识
													typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
													typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
													classCode:rowData.sysType,
													className:systemTypeMap[rowData.sysType],
													itemCode:rowData.itemCode,
													itemName:rowData.name,//医嘱名称
													combNo:'',//组
													qtyTot:1,//总量
													priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
													drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
													packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
													minUnit:rowData.unit!=null?rowData.unit:'',//最小单位		
													doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
													doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
													specs:rowData.specs!=null?rowData.specs:'',
													doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
													drugType:rowData.drugType!=null?rowData.drugType:'',
													drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
													itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
													useDays:0,//付数
													frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
													frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
													usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',		
													useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//零售价(药品)
													dateBgn:mytime,//开始时间 
													dateEnd:'',//停止时间
													moDate:mytime,//开立时间 
													docCode:'',//开立医生代码
													docName:$('#userName').val(),//开立医生
													execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
													execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
													emcFlag:0,//加急标记
													isUrgent:0,//急
													labCode:'',//样本类型
													itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
													pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
													moNote2:rowData.remark!=null?rowData.remark:'',//备注 
													recUsernm:$('#recUserName').val(),//录入人
													listDpcd:$('#deptId').val(),//开立科室 
													updateUser:'',//停止人 
													baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
													permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
													hypotest:1,
													hypotestName:'不需要皮试',
													itemType:rowData.ty,//药品非药品标识
													moStat:0,
													sortId:sortIdCreate()//顺序号								
												}).datagrid('getRows').length-1;
												$('#infolistB').datagrid('selectRow',lastIndex);						
												$('#adWestMediModlDivId').dialog('close');
											}   
											});
									}else{
										var lastIndex = $('#infolistB').datagrid('appendRow',{
											changeNo:1,
											id:'',
											itemCode:'',//特殊标识
											typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
											typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
											classCode:rowData.sysType,
											className:systemTypeMap[rowData.sysType],
											itemCode:rowData.itemCode,
											itemName:rowData.name,//医嘱名称
											combNo:'',//组
											qtyTot:1,//总量
											priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
											drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
											packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
											minUnit:rowData.unit!=null?rowData.unit:'',//最小单位		
											doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:1,//每次量		
											doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
											specs:rowData.specs!=null?rowData.specs:'',
											doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
											drugType:rowData.drugType!=null?rowData.drugType:'',
											drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
											itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
											useDays:0,//付数
											frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
											frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
											usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
											useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
											dateBgn:mytime,//开始时间 
											dateEnd:'',//停止时间
											moDate:mytime,//开立时间 
											docCode:'',//开立医生代码
											docName:$('#userName').val(),//开立医生
											execDpcd:rowData.dept!=null?rowData.dept:'',//执行科室 id
											execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
											emcFlag:0,//加急标记
											isUrgent:0,//急
											labCode:'',//样本类型
											itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
											pharmacyCode:$('#pharmacyInputName').val(),//扣库科室
											moNote2:rowData.remark!=null?rowData.remark:'',//备注 
											recUsernm:$('#recUserName').val(),//录入人
											listDpcd:$('#deptId').val(),//开立科室 
											updateUser:'',//停止人 
											baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
											permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
											hypotest:'',//皮试Id
											hypotestName:'',//皮试名称
											itemType:rowData.ty,//药品非药品标识
											moStat:0,//医嘱状态
											sortId:sortIdCreate()//顺序号								
										}).datagrid('getRows').length-1;
										$('#infolistB').datagrid('selectRow',lastIndex);						
										$('#adWestMediModlDivId').dialog('close');
									}								
								}//-----b																					

																														

											    
							}
							else{//非药品
								if($('#westMediInfoViewTypeId').val()=='402880ae51a4ade90151a4df08520005'){//类型为护理级别
									var lastIndex = $('#infolistB').datagrid('appendRow',{
										changeNo:1,
										id:'',
										itemCode:'',//特殊标识
										typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
										typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
										classCode:rowData.sysType,
										className:systemTypeMap[rowData.sysType],
										itemCode:rowData.itemCode,//医嘱Id
										itemName:rowData.name,//医嘱名称
										combNo:'',//组
										qtyTot:1,//总量
										priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
										drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
										packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
										minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
										doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:0,//每次量
										doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
										specs:rowData.specs!=null?rowData.specs:'',//规格
										doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
										drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
										drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
										itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
										useDays:0,//付数
										frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
										frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
										usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
										useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
										dateBgn:mytime,//开始时间 
										dateEnd:'',//停止时间
										moDate:mytime,//开立时间 
										docCode:'',//开立医生代码
										docName:$('#userName').val(),//开立医生
										execDpcd:$('#nurseCellCode').val(),//执行科室 id
										execDpnm:implDepartmentMap[$('#nurseCellCode').val()]!=null?implDepartmentMap[$('#nurseCellCode').val()]:'',//执行科室 
										emcFlag:0,//加急标记
										isUrgent:0,//急
										labCode:'',//样本类型
										itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
										pharmacyCode:'',//扣库科室
										moNote2:rowData.remark!=null?rowData.remark:'',//备注 
										recUsernm:$('#recUserName').val(),//录入人
										listDpcd:$('#deptId').val(),//开立科室 
										updateUser:'',//停止人 
										baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
										permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
										hypotest:'',//皮试Id
										hypotestName:'',//皮试									
										itemType:rowData.ty,//药品非药品标识
										moStat:0,//医嘱状态
										sortId:sortIdCreate()//顺序号								
									}).datagrid('getRows').length-1;
									$('#infolistB').datagrid('selectRow',lastIndex);						
									$('#adWestMediModlDivId').dialog('close');
								}else{
									var lastIndex = $('#infolistB').datagrid('appendRow',{
										changeNo:1,
										id:'',
										itemCode:'',//特殊标识
										typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
										typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
										classCode:rowData.sysType,
										className:systemTypeMap[rowData.sysType],
										itemCode:rowData.itemCode,//医嘱Id
										itemName:rowData.name,//医嘱名称
										combNo:'',//组
										qtyTot:1,//总量
										priceUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//单位（总量单位）
										drugpackagingUnit:rowData.drugPackagingunit!=null?rowData.drugPackagingunit:'',//包装单位
										packQty:rowData.packagingnum!=null?rowData.packagingnum:1,//包装数量	
										minUnit:rowData.unit!=null?rowData.unit:'',//最小单位	
										doseOnce:rowData.drugOncedosage!=null?rowData.drugOncedosage:0,//每次量
										doseUnit:rowData.drugDoseunit!=null?rowData.drugDoseunit:'',//单位（剂量单位）
										specs:rowData.specs!=null?rowData.specs:'',//规格
										doseModelCode:rowData.drugDosageform!=null?rowData.drugDosageform:'',//剂型代码
										drugType:rowData.drugType!=null?rowData.drugType:'',//药品类别
										drugQuality:rowData.drugNature!=null?rowData.drugNature:'',//药品性质
										itemPrice:rowData.drugRetailprice!=null?rowData.drugRetailprice:'',//零售价(药品)
										useDays:0,//付数
										frequencyCode:rowData.drugFrequency!=null?rowData.drugFrequency:'',//频次代码
										frequencyName:frequencyMap[rowData.drugFrequency]!=null?frequencyMap[rowData.drugFrequency]:'',//频次名称
										usageCode:rowData.drugUsemode!=null?rowData.drugUsemode:'',	//用法Id	
										useName:usemodeMap[rowData.drugUsemode]!=null?usemodeMap[rowData.drugUsemode]:'',//用法名称
										dateBgn:mytime,//开始时间 
										dateEnd:'',//停止时间
										moDate:mytime,//开立时间 
										docCode:'',//开立医生代码
										docName:$('#userName').val(),//开立医生
										execDpcd:$('#deptId').val(),//执行科室 id
										execDpnm:implDepartmentMap[rowData.dept]!=null?implDepartmentMap[rowData.dept]:'',//执行科室 
										emcFlag:0,//加急标记
										isUrgent:0,//急
										labCode:'',//样本类型
										itemNote:rowData.inspectionSite!=null?rowData.inspectionSite:'',//检查部位
										pharmacyCode:'',//扣库科室
										moNote2:rowData.remark!=null?rowData.remark:'',//备注 
										recUsernm:$('#recUserName').val(),//录入人
										listDpcd:$('#deptId').val(),//开立科室 
										updateUser:'',//停止人 
										baseDose:rowData.drugBasicdose!=null?rowData.drugBasicdose:'',//基本剂量
										permission:rowData.undrugIsinformedconsent!=null?rowData.undrugIsinformedconsent:'',//患者是否同意
										hypotest:'',//皮试Id
										hypotestName:'',//皮试									
										itemType:rowData.ty,//药品非药品标识
										moStat:0,//医嘱状态
										sortId:sortIdCreate()//顺序号								
									}).datagrid('getRows').length-1;
									$('#infolistB').datagrid('selectRow',lastIndex);						
									$('#adWestMediModlDivId').dialog('close');
								}								
							}
						}
					}						       
				});
			
				//药品使用限制等级为乙类的，在列表中以绿色字体标识
				function gradeStyler(value,row,index){			
					if(row.drugGrade=='402880a5506094360150609b225a0005'){					
						return 'color:green;';									
					}					
				}	
				//系统类别 列表页 显示		
				function sysTypeFamater(value,row,index){			
					if(value!=null&&value!=""){
						return systemTypeMap[value];									
					}			
				}
				//单位 列表页 显示	
				function drugpackagingunitFamater(value,row,index){	
					if(value!=null&&value!=""){
						if(row.ty=='1'){
							if(drugpackagingunitMap[value]!=null&&drugpackagingunitMap[value]!=""){
								return drugpackagingunitMap[value];
							}
							return value;
						}
						if(row.ty=='0'){
							if(nonmedicineencodingMap[value]!=null&&nonmedicineencodingMap[value]!=""){
								return nonmedicineencodingMap[value];
							}
							return value;
						}
					}			
				}
				//医保标志
				function speidFamater(value,row,index){	
					var retVal="";				
					if(value!=null&&value!=""){	
						return druggradeMap[value];
					}
					if(row.undrugIsprovincelimit==1){
						retVal=retVal+'X';
					}
					if(row.undrugIscitylimit==1){
						retVal=retVal+'S';
					}
					if(row.undrugIsownexpense==1){
						retVal=retVal+'Z';
					}
					if(row.undrugIsspecificitems==1){
						retVal=retVal+'T';
					}
					return retVal;
				}
				//执行科室 列表页 显示		
				function implDepartmentFamater(value,row,index){			
					if(value!=null&&value!=""){					
						return implDepartmentMap[value];									
					}			
				}
				//疾病分类 列表页 显示		
				function diseasetypeFamater(value,row,index){			
					if(value!=null&&value!=""){
						for(var i=0;i<diseasetypeList.length;i++){
							if(value==diseasetypeList[i].id){
								return diseasetypeList[i].name;					
							}
						}
					}			
				}
				//药品频次列表页 显示		
				function drugfrequencyFamater(value,row,index){			
					if(value!=null&&value!=""){
						return frequencyMap[value];					
					}			
				}			
				//药品用法列表页 显示		
				function drugusemodeFamater(value,row,index){			
					if(value!=null&&value!=""){
						return usemodeMap[value];					
					}			
				}
			}	
</script>
</body>
</html>