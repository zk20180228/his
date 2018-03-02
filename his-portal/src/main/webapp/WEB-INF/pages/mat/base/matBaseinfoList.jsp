<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>物资分类字典</title>
<%@ include file="/common/metas.jsp" %>
<script type="text/javascript">
			var effectAreaData = [{"id":0,"text":"本科室"},{"id":1,"text":"本科室及下级科室"},{"id":2,"text":"全院"},{"id":3,"text":"指定科室"}];//有效范围
			var validData = [{"id":0,"text":"全部"},{"id":1,"text":"使用"},{"id":2,"text":"停用"}];
			var xq = "1";//判断当前显示页签
			var matKinddataMap = null;//物资分类Map
			var addRateList ="";//加价规则
			var empMap="";//渲染操作人
			var factorymap = "";
			var companyList=null;
			$(function(){
				$('#fileBaseName').filebox({
					buttonText: '选择文件',
				});
		  
			
				$('#fileRegName').filebox({
					buttonText: '选择文件',
				});
				
				//渲染生产厂家
	    		$.ajax({
<%-- 				    			url:"<%=basePath %>drug/manufacturer/queryManufacturerMapList.action",     --%>
	    			url : '<%=basePath%>material/base/queryMatCompany.action',
	    			type:'post',
	    			success: function(data){
	    				factorymap = data;
	    			}
	    		});
				//查询物品分类
				$.ajax({
					url: "<%=basePath%>material/base/findMatKindinfos.action",				
					type:'post',
					success: function(matKinddata) {					
						matKinddataMap = matKinddata ;
					}
				});
					$('#list').datagrid({
						url:'<%=basePath%>material/base/queryMatBaseinfo.action',
						onDblClickRow:function (rowIndex, rowData) {
							$("#baseId").val(rowData.id);
							$("#itemCode").textbox('setValue',rowData.itemCode);
							$("#itemName").textbox('setValue',rowData.itemName);
							$("#customCode").textbox('setValue',rowData.customCode);
							$("#otherName").textbox('setValue',rowData.otherName);
							$("#otherCustom").textbox('setValue',rowData.otherCustom);
							$("#specs").textbox('setValue',rowData.specs);
							$("#salePrice").numberbox('setValue',rowData.salePrice);
							$("#minUnit").combobox('setValue',rowData.minUnit);
							$("#gbCode").textbox('setValue',rowData.gbCode);
							$("#kindCode").combobox('setValue',rowData.kindCode);
							$("#packQty").textbox('setValue',rowData.packQty);
							$("#packUnit").combobox('setValue',rowData.packUnit);
							$("#inPrice").numberbox('setValue',rowData.inPrice);
							$("#effectArea").combobox('setValue',rowData.effectArea);
							$("#packPrice").numberbox('setValue',rowData.packPrice);
							$("#storageCode").textbox('setValue',rowData.storageCode);
							$("#factoryCode").combobox('setValue',rowData.factoryCode);
							$("#addRate").combobox('setValue',rowData.addRate);
							$("#inSource").textbox('setValue',rowData.inSource);
							$("#usage").textbox('setValue',rowData.usage);
							$("#companyCode").combobox('setValue',rowData.companyCode);
							$("#memo").textbox('setValue',rowData.memo);
							$("#operDate").val(rowData.operDate);
							if(rowData.validFlag !=null && rowData.validFlag !=""){
								if(rowData.validFlag==0){
									$('#validFlag').prop("checked", true);
								}
							}if(rowData.financeFlag !=null && rowData.financeFlag !=""){
								if(rowData.financeFlag==1){
									$('#financeFlag').prop("checked", true);
								}
							}if(rowData.highvalueFlag !=null && rowData.highvalueFlag !=""){
								if(rowData.highvalueFlag==1){
									$('#highvalueFlag').prop("checked", true);
								}
							}if(rowData.specialFlag !=null && rowData.specialFlag !=""){
								if(rowData.specialFlag==1){
									 $('#specialFlag').prop("checked", true);
								}
							}if(rowData.packFlag !=null && rowData.packFlag !=""){
								if(rowData.packFlag==1){
									$('#packFlag').prop("checked", true);
								}
							}if(rowData.norecycleFlag !=null && rowData.norecycleFlag !=""){
								if(rowData.norecycleFlag==1){
									$('#specialFlag').prop("checked", true);
								}
							}if(rowData.batchFlag !=null && rowData.batchFlag !=""){
								if(rowData.batchFlag==1){
									 $('#batchFlag').prop("checked", true);
								}
							}if(rowData.plan !=null && rowData.plan !=""){
								if(rowData.plan==1){
									$('#plan').prop("checked", true);
								}
							}
							
							
							$('#baseFile').datagrid({
								url:"<c:url value='/material/base/getMatFileList.action'/>?itemCode="+rowData.itemCode+"&fileKind=2"
							});
							$('#regInfo').datagrid({
								url:"<c:url value='/material/base/getMatBaseRegInfoList.action'/>?itemCode="+rowData.itemCode
							});
							$('#regFile').datagrid({
								url:"<c:url value='/material/base/getMatFileList.action'/>?itemCode="+rowData.itemCode+"&fileKind=0"
							});
							//这句代码并没有什么意义，但是没有他，双击列表时，会出现单击不能触发文件上传功能
							$("#BaseName input").focus();
							$("#RegName input").focus();
							
							
							
						},
						onLoadSuccess : function(data){
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
					});
				$('#regInfo').datagrid({
					onDblClickRow:function (rowIndex, rowData) {
						$('#matBaseRegInfoForm').form('clear');
						$("#factoryCodeReg").combobox('setValue',rowData.factoryCode);
						$("#registerCodeReg").textbox('setValue',rowData.registerCode);
						$("#registerDateReg").val(rowData.registerDate);
						$("#overDateReg").val(rowData.overDate);
						$("#specsReg").textbox('setValue',rowData.specs);
						$("#packUnitReg").combobox('setValue',rowData.packUnit);
						$("#packPriceReg").numberbox('setValue',rowData.packPrice);
						if(rowData.defaultFlag=="1"){
							$('#defaultFlagReg').prop('checked',true);
						}
						if(rowData.validFlagReg=="0"){
							$('#validFlagReg').prop("checked", true);
						}
					}
				});
				$('#tabDiv').tabs({
					  onSelect: function(title){
						if(title=="基本信息"){
							xq = "1";
						}else if(title=="注册信息"){
							xq = "0";
						}
					  }
				});
				//加价规则
			    $('#addRate').combobox({
				    url:'<%=basePath%>material/base/queryMatAddrate.action',
				    valueField:'id',
				    textField:'addRate',
				    multiple:false,
				    onLoadSuccess:function(none){
				    	addRateList=none;
				    }
				});
				//加载部门树===分类树吧。。。
			   	$('#tDt').tree({    
				    url:"<%=basePath%>material/orderKindInfo/queryTree.action?t1.validFlag=1",
				    method:'get',
				    animate:true,
				    lines:true,
				    formatter:function(node){//统计节点总数
						var s = node.text;
						if (node.children){
							s += node.children.length===0?'':'&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
						}
						return s;
					},
				    onBeforeLoad : function(node, param){
				    	if(node!=null&&node!=''){
				    		return false;
				    	}
				    },
				    onContextMenu : function(e, node) {
						e.preventDefault();
						$(this).tree('select',node.target);
						if(node.attributes.pid==1){
							$('#addMatKindinfoTree').css("display","block");
							$('#deleteMatKindinfoTree').css("display","none");
							$('#updateMatKindinfoTree').css("display","none");
							$('#mm').menu('show',{
								left: e.pageX,
								top: e.pageY
							});
						}else {
							$('#addMatKindinfoTree').css("display","block");
							$('#deleteMatKindinfoTree').css("display","block");
							$('#updateMatKindinfoTree').css("display","block");
							$('#mm').menu('show',{
								left: e.pageX,
								top: e.pageY
							});
						}
					},onClick: function(node){//点击节点
						if(node.id==0){
							return
						}else{
							$('#list').datagrid('load', {
								kindCode : node.id,
							});
						}
					},onLoadSuccess:function(node, data){
						/* if(data.length>0){//节点收缩
							$('#tDt').tree('collapseAll');
						    $('#tDt').tree('expand',$('#tDt').tree('find', 0).target);
						} */
					}
				});
			  
				
			
				bindEnterEvent('queryName',searchFrom,'easyui');//回车查询用法
				$('#state').combobox({
					onSelect:function(record){
 						if(record.id==1){
							$('#list').datagrid('load', {
								ValidFlag : 1,
							});
						}else if(record.id==2){
							$('#list').datagrid('load', {
								ValidFlag : 0,
							});
						}else if(record.id==0){
							$('#list').datagrid('load', {
								ValidFlag : null,
							});
						}
					}
				});
			   //费用类别 下拉
				$("#recipeNo").combobox({
					url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=casminfee',
					valueField:'encode',
					textField:'name'
				});
			    //渲染操作人
	    		$.ajax({
	    			url:"<%=basePath %>baseinfo/employee/getEmplMap.action",    
	    			type:'post',
	    			success: function(deptData){
	    				empMap = deptData;
	    			}
	    		});
	    		//最小单位
			    $('#minUnit').combobox({
				    url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action',
				    queryParams:{"type":"minunit"},
				    valueField:'encode',    
				    textField:'name',
				    multiple:false
				});
			   
			  /**
				* 绑定最小单位回车事件
				* @author  zhuxiaolu
				* @date 2016-03-22 14:30   
				* @version 1.0
				*/
// 				bindEnterEvent('minUnit',popWinToCodeMinimumunit,'easyui'); //给最小单位方法绑定弹窗事件
				//包装单位
			    $('#packUnit').combobox({
			    	url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action',
				    queryParams:{"type":"packunit"},
				    valueField:'encode',    
				    textField:'name',
				    multiple:false
				});
			  	
			    /**
				* 绑定包装单位回车事件
				* @author  zhuxiaolu
				* @date 2016-03-22 14:30   
				* @version 1.0
				*/
// 				bindEnterEvent('packUnit',popWinToCodeDrugpackagingunit,'easyui');//给包装单位方法绑定弹窗事件
			    $('#packUnitReg').combobox({
			    	url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action',
				    queryParams:{"type":"packunit"},
				    valueField:'encode',    
				    textField:'name',
				    multiple:false
				});
				
			  //物资分类
				$('#kindCode').combobox({
					url : "<%=basePath%>/material/kind/getMatKindinfoList.action",
					valueField : 'kindCode',
					textField : 'kindName'
				});
				
				//初始化供货公司下拉
				$('#companyCode').combobox({
					url : "<c:url value='/material/orderCompany/companyList.action'/>",
					valueField : 'companyCode',
					textField : 'companyName',
					onLoadSuccess:function(data){
						companyList=data;
						$('#factoryCode').combobox({
							data:companyList,
							valueField : 'companyCode',
							textField : 'companyName'
						});
						//注册信息物资生产厂家下拉框
						$('#factoryCodeReg').combobox({
							data:companyList,
							valueField : 'companyCode',
							textField : 'companyName'
						});
					}
				}); 
				
				 /**
				* 绑定供货单位回车事件
				* @author  zhuxiaolu
				* @date 2016-03-22 14:30   
				* @version 1.0
				*/
// 				bindEnterEvent('companyCode',popWinToCompany,'easyui');//绑定回车事件
				
				 /**
				* 绑定生产厂家回车事件
				* @author  zhuxiaolu
				* @date 2016-03-22 14:30   
				* @version 1.0
				*/
// 				bindEnterEvent('factoryCode',popWinToFactory,'easyui');//绑定回车事件
				
			});
			//=========================================================================
			function update(){
				var node = $('#tDt').tree('getSelected');
				var id = node.attributes.kindId;
				if(id<1){
					 $.messager.alert('提示',"请选择要修改的物资分类！");
					return false;
				}
				if(id!=null&&id!=""){
					Adddilogs("编辑分类","<c:url value='/material/base/updateMatKindinfoUrl.action'/>?id="+id);
				}else{
					 $.messager.alert('提示',"请选择要修改的物资分类！");
				}
			}
			function removeReg(){
				var row=$("#regFile").datagrid('getSelected');
				if(row!=null){
					var index=$("#regFile").datagrid("getRowIndex",row);
					$("#regFile").datagrid("deleteRow",index);
				}else{
					 $.messager.alert('提示',"请选择要删除的记录！");
				}
			}
			function delFiles(){
				var row=$("#baseFile").datagrid('getSelected');
				if(row!=null){
					var index=$("#baseFile").datagrid("getRowIndex",row);
					$("#baseFile").datagrid("deleteRow",index);
				}else{
					
					 $.messager.alert('提示',"请选择要删除的记录！");
				}
			}
			function delFilesReg(){
				var row=$("#regFile").datagrid('getSelected');
				if(row!=null){
					var index=$("#regFile").datagrid("getRowIndex",row);
					$("#regFile").datagrid("deleteRow",index);
				}else{
					 $.messager.alert('提示',"请选择要删除的记录！");
				}
			}
			
			function addRemark(tag){
				var rowIndex;
				if(xq == "1"){
					rowIndex=$("#baseFile").datagrid('getRowIndex',$("#baseFile").datagrid('getSelected'));
				}else if(xq == "0"){
					rowIndex=$("#regFile").datagrid('getRowIndex',$("#regFile").datagrid('getSelected'));
				}
				Adddilog("添加备注","<c:url value='/material/base/addCertifyRemark.action'/>?tag="+tag+"&row="+rowIndex,'addRemarkDiv');
			}
			//过滤已经存在的数据
			function selectNewItem(allItem){
				var allItemJson = eval('('+allItem+')');
				var selectNewItemJson = [];
				for(var i = 0;i<allItemJson.length;i++){
					if(allItemJson[i].loadStatus){
						delete allItemJson[i].loadStatus;
						var json = JSON.stringify(allItemJson[i]);
						selectNewItemJson.push(eval('('+json+')'));
					}
				}
				return JSON.stringify(selectNewItemJson);
			}
			//保存物资信息 
			function saveBaseInfo(){
				if($('#itemCode').textbox('getValue')!=null || $('#itemCode').textbox('getValue')!=""){
					var baseFileM = $('#baseFile').datagrid("getRows");
					for(var i = 0;i<baseFileM.length;i++){
						$('#baseFile').datagrid("endEdit",i);
					}
					var regFileM = $('#regFile').datagrid("getRows");
					for(var i = 0;i<regFileM.length;i++){
						$('#regFile').datagrid("endEdit",i);
					}
					 var baseFileAll= JSON.stringify( $('#baseFile').edatagrid("getRows")); 
					 var regInfoAll= JSON.stringify( $('#regInfo').edatagrid("getRows"));
					 var regFileAll= JSON.stringify( $('#regFile').edatagrid("getRows"));
					 
					 var baseFile = selectNewItem(baseFileAll);
					 var regFile =  selectNewItem(regFileAll);
					 var regInfo = selectNewItem(regInfoAll);
				     $("#matBaseInfoForm").form('submit', {
						url : "<c:url value='/material/base/saveMatBaseinfo.action'/>",
						queryParams:{baseFile:baseFile,regInfo:regInfo,regFile:regFile},
						success : function(data) {
							$('#matBaseInfoForm').form('clear');
							$('#matBaseRegInfoForm').form('clear');
							var rows=$("#baseFile").datagrid("getRows");
							for(var i=0;i<rows.length;i++){
								var index=$("#baseFile").datagrid("getRowIndex",rows[i]);
								$("#baseFile").datagrid("deleteRow",index);
							}
							var rows=$("#regInfo").datagrid("getRows");
							for(var i=0;i<rows.length;i++){
								var index=$("#regInfo").datagrid("getRowIndex",rows[i]);
								$("#regInfo").datagrid("deleteRow",index);
							}
							var rows=$("#regFile").datagrid("getRows");
							for(var i=0;i<rows.length;i++){
								var index=$("#friTable").datagrid("getRowIndex",rows[i]);
								$("#friTable").datagrid("deleteRow",index);
							}
							$("#list").datagrid("reload");
						},
						success : function(data) {
							 $.messager.alert('提示',"保存成功");
							$("#list").datagrid("reload");
							 $('#matBaseInfoForm').form('reset');
						},
						error : function(data) {
							$.messager.alert('提示',"保存失败");
						}
					 });
				}else{
					$.messager.alert('提示',"请填写物品编码！");
				}
			}
			//物资基本信息文件上传
			function upload(){
				var fileBaseName= $('#fileBaseName').filebox('getValue');
				if(fileBaseName!=''){
					$('#baseUploadForm').form('submit', {
						url : "<%=basePath %>material/base/matBaseUpload.action",
						queryParams:{filePath:$('#fileBaseName').filebox('getValue')},
						success : function(data) {
							if(data!=""&&data!=null){
								var dataMap = eval("("+data+")");
								var fullFileName = dataMap.fileServerURL+dataMap.fileName;
								$("#baseFile").datagrid("appendRow",{
									fileKind:2,
									fileName:fileBaseName,
									filePath:fullFileName,
									loadStatus:'true',
								});
								editIndex = $("#baseFile").datagrid('getRows').length-1;
						 	 	$("#baseFile").datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
							}else{
								$.messager.alert('提示',"上传的文件格式不对！");
							}
						},
						error : function(data) {
							$.messager.alert('提示',"上传失败！");
						}
					 });
				}else{
					$.messager.alert('提示',"请选择要上传的文件！");
				}
			}
			//注册证件文件上传
			function uploadReg(){
				var fileRegName= $('#fileRegName').filebox('getValue');
				if(fileRegName!=''){
					$('#regUploadForm').form('submit', {
						url : "<%=basePath %>material/base/matRegUpload.action",
						queryParams:{filePath:$('#fileRegName').filebox('getValue')},
						success : function(data) {	
							if(data!=""&&data!=null){
								var dataMap = eval("("+data+")");
								var fullFileName = dataMap.fileServerURL+dataMap.fileName;
								$("#regFile").datagrid("appendRow",{
									foreignCode:$('#itemCode').textbox('getValue'),
									fileKind:0,
									fileName:fileRegName,
									filePath:fullFileName,
									loadStatus:'true'
								});
								editIndex = $("#regFile").datagrid('getRows').length-1;
						 	 	$("#regFile").datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
							}else{
								$.messager.alert('提示',"上传的文件格式不对！");
							}
						},
						error : function(data) {
							$.messager.alert('提示',"上传失败！");
						}
					 });
				}else{
					$.messager.alert('提示',"请选择要上传的文件！");
				}
			}
			//查看注册文件
			function checkReg(){
				var row = $('#regFile').datagrid('getSelected');
				if(row!=null){
		 			window.open(row.filePath); 
				}else{
					$.messager.alert('提示',"请选择要查看的文件！");
				}
			}
			//查看基本信息文件
			function checkBase(){
				var row = $('#baseFile').datagrid('getSelected');
				if(row!=null){
		 			window.open(row.filePath); 
				}else{
					$.messager.alert('提示',"请选择要查看的文件！");
				}
			}
			//添加注册信息 
			function addRegister(){
				var validFlag;
				var validFlags;
				if($('#validFlagRegh').val()==1){
					validFlag='停用';
					validFlags=0;
				}else{
					validFlag='使用';
					validFlags=1;
				}
				var defaultFlag;
				var defaultFlags;
				if($('#defaultFlagRegh').val()==0){
					defaultFlag='否';
					defaultFlags=0;
				}else{
					defaultFlag='是';
					defaultFlags=1;
				}
				$('#regInfo').datagrid('appendRow',{
					factoryCodeShow:$('#factoryCodeReg').combobox('getText') ,
					factoryCode:$('#factoryCodeReg').combobox('getValue') ,
					validFlag: validFlags,
					validFlagShow: validFlag,
					defaultFlag: defaultFlags,
					defaultFlagShow: defaultFlag,
					specs: $('#specsReg').textbox('getValue'),
					itemCode: $('#itemCode').textbox('getValue'),
					registerCode: $('#registerCodeReg').textbox('getValue'),
					loadStatus:'true',
					packUnit: $('#packUnitReg').combobox('getValue'),
					packPrice: $('#packPriceReg').numberbox('getValue'),
					registerDate: $('#registerDateReg').val(),
					overDate: $('#overDateReg').val(), 
				});
			}
			//删除注册信息 
			function delRegister(){
				var row=$("#regInfo").datagrid('getSelected');
				if(row!=null){
					var index=$("#regInfo").datagrid("getRowIndex",row);
					$("#regInfo").datagrid("deleteRow",index);
				}else{
					$.messager.alert('提示',"请选择要删除的记录！");
				}
			}
			//添加分类
			function addMatKindinfoTree(){
				var row =  $('#tDt').tree('getSelected');
				if(row==''||row==null){
					var node = $('#tDt').tree('find', 0);
					$('#tDt').tree('select', node.target);
					var parentId=$('#tDt').tree('getSelected').id;
					var kindType = "";
					if("undefined"!=typeof($('#tDt').tree('getSelected').attributes)){
						kindType=$('#tDt').tree('getSelected').attributes.kindType;
					}
					var kindPath = "";
					if("undefined"!=typeof($('#tDt').tree('getSelected').attributes)){
						kindPath=$('#tDt').tree('getSelected').attributes.kindPath;
					}
					if(parentId=="0"){
						kindPath="00000000";
						parentId=0;
						kindType=getRanNum();
					}
				}else if("undefined"==typeof(row.attributes)){
					var node = $('#tDt').tree('find', 0);
					$('#tDt').tree('select', node.target);
					var parentId=$('#tDt').tree('getSelected').id;
					var kindType = "";
					if("undefined"!=typeof($('#tDt').tree('getSelected').attributes)){
						kindType=$('#tDt').tree('getSelected').attributes.kindType;
					}
					var kindPath = "";
					if("undefined"!=typeof($('#tDt').tree('getSelected').attributes)){
						kindPath=$('#tDt').tree('getSelected').attributes.kindPath;
					}
					if(parentId=="0"){
						kindPath="00000000";
						parentId=0;
						kindType=getRanNum();
					}
				}else if(row.attributes.pid==0){
					var parentId=$('#tDt').tree('getSelected').id;
					var kindType = "";
					if("undefined"!=typeof($('#tDt').tree('getSelected').attributes.kindType)){
						kindType=$('#tDt').tree('getSelected').attributes.kindType;
					}
					var kindPath = "";
					if("undefined"!=typeof($('#tDt').tree('getSelected').attributes.kindPath)){
						kindPath=$('#tDt').tree('getSelected').attributes.kindPath;
					}
				}else{
					var parentId=$('#tDt').tree('getSelected').id;
					var kindType = "";
					if("undefined"!=typeof($('#tDt').tree('getSelected').attributes.kindType)){
						kindType=$('#tDt').tree('getSelected').attributes.kindType;
					}
					var kindPath = "";
					if("undefined"!=typeof($('#tDt').tree('getSelected').attributes.kindPath)){
						kindPath=$('#tDt').tree('getSelected').attributes.kindPath;
					}
				}
					Adddilogs("编辑分类","<c:url value='/material/base/addMatKindinfoUrl.action'/>?parentId="+parentId+"&kindType="+kindType+"&kindPath="+kindPath);
			}
			/**
			* 删除
			* @version 1.0
			*/
			function deleteMatKindinfoTree(){
				var row =  $('#tDt').tree('getSelected');
				var id = getSelected();
				if(id<1){
					$.messager.alert('提示',"请选择物资分类！");
					return false;
				}
				if(row==''||row==null){
					$.messager.alert('提示',"请选择要删除的物资分类！");
				}else{
					$.messager.confirm('确认','您确认想要删除该分类吗？',function(r){
						if (r){
							var row =  $('#tDt').tree('getSelected');
							var id = row.id;
							var pid = row.attributes.pid;
							var kindId = row.attributes.kindId;
							$.ajax({
								url: "<%=basePath%>material/kind/deleteMatKindinfo.action",
								data:{matKindinfoId:kindId,pid:pid},
								type:'post',
								success: function(dataMap) {
			 						$.messager.alert('提示',dataMap.resCode);
			 						$('#tDt').tree('reload');
			 					}
							});
						}
					});
				}
			}
			function getRanNum(){
				result = "";
				for(var i=0;i<6;i++){
					var ranNum = Math.ceil(Math.random() * 25); //生成一个0到25的数字
					//大写字母'A'的ASCII是65,A~Z的ASCII码就是65 + 0~25;然后调用String.fromCharCode()传入ASCII值返回相应的字符并push进数组里
					result=result+String.fromCharCode(65+ranNum);
				} 
				return result;
			}
			function edit(){
				var row =  $('#tDt').tree('getSelected');; //获取当前选中行     
	            if(row){
	            	Adddilog("编辑分类","<c:url value='/material/base/editMatKindinfoUrl.action'/>?id="+row.id,'selectStorage');
					$('#tDt').tree('reload');
		   		}
			}
			//复选框
			function checkBoxSelect(id,defalVal,selVal){
				var hiddenId=id+"h";
				if($('#'+id).is(':checked')){
					$('#'+hiddenId).val(selVal);
				}else{
					$('#'+hiddenId).val(defalVal);
				}
			}
			function searchFrom(){
				var queryName = $('#queryName').textbox('getValue');
				var ValidFlag = $('#state').textbox('getValue');
				if(ValidFlag==0){
					$('#list').datagrid('load', {
						queryName : queryName,
						ValidFlag : null
					});
				}else if(ValidFlag==1){
					$('#list').datagrid('load', {
						queryName : queryName,
						ValidFlag : 1
					});
				}else if(ValidFlag==2){
					$('#list').datagrid('load', {
						queryName : queryName,
						ValidFlag : 0
					});
				}
			}
		
		   	function getSelected(){//获得选中节点
				var node = $('#tDt').tree('getSelected');
				if (node){
					var id = node.id;
					return id;
				}
			}
		
    		//操作人
    		function funccomTypeCZR(value,row,index){
    			if(value!=null&&value!=''){
    				return empMap[value];
    			}else{
    				return '';
    			}
        	}
    		//注册信息生产厂家
    		function factoryFamater(value,row,index){
    			for(var i=0;i<factorymap.length;i++){
  				  if(factorymap[i].companyCode==value){
  					  return factorymap[i].companyName;
  				  }
  				}
    		}
			 //加价规则
			function addRateFamater(value){
				if(value!=null){
					for(var i=0;i<addRateList.length;i++){
						if(value==addRateList[i].id){
							return addRateList[i].addRate;
						}
					}
				}	
			}
		    //显示有效范围格式化
			function effectAreaFamater(value){
				if(value!=null){
					for(var i=0;i<effectAreaData.length;i++){
						if(value==effectAreaData[i].id){
							return effectAreaData[i].text;
						}
					}	
				}
			}
		    //物资分类渲染
		    function drugSpecFamater(value,row,index){
		    	if(row.kindCode!=null&&row.kindCode!=""){									
					return matKinddataMap[row.kindCode];					
				}
		    }
			//打开dialog
			function openDialog(id) {
				$('#'+id).dialog('open'); 
			}
			//关闭dialog
			function closeDialog(id) {
				$('#'+id).dialog('close');  
			}
			//加载dialog
			function Adddilog(title, url,id) {
				$('#'+id).dialog({    
				    title: title,    
				    width: '18%',    
				    height:'15%',   
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true 
				   });  
			}
			function Adddilogs(title, url) {
				$('#addMatKindinfo').dialog({    
				    title: title,    
				    width: '53%',    
				    height:'20%',    
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true   
				});    
			}
			function openDialogs() {
				$('#addMatKindinfo').dialog('open'); 
			}
			function closeDialogs() {
				$('#addMatKindinfo').dialog('close');  
			}
			function addDic(){
				$('#itemCode').next("span").children().first().focus();
			}
			function clear(){
				$('#matBaseInfoForm').form('clear');
				$('#matBaseRegInfoForm').form('clear');
			}
			/**
			   * 回车弹出供货公司弹框
			   * @author  zhuxiaolu
			   * @param textId 页面上commbox的的id
			   * @date 2016-03-22 14:30   
			   * @version 1.0
			   */
			   function popWinToCompany(){
					var tempWinPath = "<%=basePath%>popWin/popWinSupplyCompany/toDrugSupplycompanyPopWin.action?textId=companyCode";
					window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -900) +',height='+ (screen.availHeight-370)+',scrollbars,resizable=yes,toolbar=yes')
				}
		   /**
			   * 回车弹出生产厂家弹框
			   * @author  zhuxiaolu
			   * @param textId 页面上commbox的的id
			   * @date 2016-03-22 14:30   
			   * @version 1.0
			   */
			   function popWinToFactory(){
					var tempWinPath = "<%=basePath%>popWin/popWinFactory/toMatCompanyPopWin.action?textId=factoryCode";
					window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -900) +',height='+ (screen.availHeight-500)+',scrollbars,resizable=yes,toolbar=yes')
				}
		
			/**
			 * 打开包装单位界面弹框
			 * @author  zhuxiaolu
			 * @date 2015-5-25 10:53
			 * @version 1.0
			 */
			
			function popWinToCodeDrugpackagingunit(){
				
				var tempWinPath ="<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=matpackorminiunit&textId=packUnit";
				var aaa=window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
						
			}
			/**
			 * 打开最小单位界面弹框
			 * @author  zhuxiaolu
			 * @date 2015-5-25 10:53
			 * @version 1.0
			 */
			
			function popWinToCodeMinimumunit(){
				
				var tempWinPath ="<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=matpackorminiunit&textId=minUnit";
				var aaa=window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
						
			}
		</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<style type="text/css">
#tabDiv .tabs-panels{
	border:0
}
</style>
</head>
<body style="margin: 0px; padding: 0px;">
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'north'" style="height:50px;width: 100%;padding-top: 10px;border-top:0">
	    	<a id="fashPause" href="javascript:void(0)" onclick="addMatKindinfoTree()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加分类</a>
			<a id="Pause" href="javascript:void(0)" onclick="deleteMatKindinfoTree()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除分类</a>
			<a id="autorefashPause" href="javascript:void(0)" onclick="update()" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改分类</a>
	    </div>   
	    <div data-options="region:'west'" style="width:12%;">
	    	<ul id="tDt"></ul>
	    </div>   
	    <div data-options="region:'center',border:false" style="width: 88%">
	    	<div id="cc" class="easyui-layout" data-options="fit:true">   
			    <div data-options="region:'north'" style="height:50px;padding-top: 10px;">
			    	<table>
						<tr>
							<td>
								查询条件：<input class="easyui-textbox" id="queryName" name="queryName" data-options="prompt:'物资名称，别名，物资编码'"  style="width:200px">&nbsp;&nbsp;
								状态：<input class="easyui-combobox" id="state" name="state" data-options="valueField:'id',textField:'text',data:validData">
								&nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search" >查询</a>
							</td>
						</tr>
					</table>
			    </div>   
			    <div data-options="region:'center'" style="height:50%;border-bottom:0">
			    	<table id="list"  style="overflow-y:auto;height:309px;" data-options="method:'post',idField: 'id',striped:true,border:false,fitColumns:false,pagination : true,
										rownumbers : true,fit:true,singleSelect:true,pageSize : 10,pageList : [ 10, 20, 30, 50, 100 ]">
						<thead>
							<tr>
								<th data-options="field:'id',hidden:true" >id</th>
								<th data-options="field:'itemCode',hidden:true">物品编码</th>
								<th data-options="field:'drugSpec',hidden:true">物品分类编码</th>
								<th data-options="field:'kindCode',formatter:drugSpecFamater" style="width:5%">物品分类</th>
								<th data-options="field:'itemName'" style="width:8%">物品名称</th>
								<th data-options="field:'otherName'" style="width:8%">别名</th>
								<th data-options="field:'effectArea',formatter:effectAreaFamater" style="width:5%">有效范围</th>
								<th data-options="field:'specs'" style="width:5%">规格</th>
								<th data-options="field:'minUnit'" style="width:4%">最小单位</th>
								<th data-options="field:'inPrice'" style="width:6%">最新入库单价</th>
								<th data-options="field:'salePrice'" style="width:6%">零售价格</th>
								<th data-options="field:'packUnit'" style="width:5%">大包装单位</th>
								<th data-options="field:'packQty'" style="width:5%">大包装数量</th>
								<th data-options="field:'packPrice'" style="width:6%">大包装价格</th>
								<th data-options="field:'addRate',formatter:addRateFamater" style="width:8%">加价规则</th>
								<th data-options="field:'feeCode'" style="width:6%">最小费用</th>
								<th data-options="field:'financeFlag',formatter: function(value,row,index){
																				if (value==1){
																					return '是';
																				} else {
																					return '否';
																				}
																			}" style="width:4%">财务收费</th>
								<th data-options="field:'updateUser',formatter: funccomTypeCZR" style="width:6%">操作员</th>
								<th data-options="field:'updateTime'" style="width:12%">操作日期</th>
							</tr>
						</thead>
					</table>
			    </div>   
			    <div data-options="region:'south',border:false" style="height:50%;"> 
			    	<div id="tabDiv" class="easyui-tabs" data-options="fit:true">
			    		<div title="基本信息" data-options="fit:true">   
					        <div id="cc" class="easyui-layout" data-options="fit:true,border:false">   
							    <div data-options="region:'west',border:false" style="width:66%;">
							    	<form id="matBaseInfoForm" method="post">
					    				<table id="friTable" class="honry-table" style="border-top:0;border-right:0">
					    						<input type="hidden" id="operDate" name="operDate"/>
											<tr>
												<td class="honry-lable" style="border-top:0">物品编码：</td><td style="border-top:0"><input type="hidden" id="baseId" name="id"><input class="easyui-textbox" id="itemCode" name="itemCode"  style="width:112px; " data-options="required : true"></td>
												<td class="honry-lable" style="border-top:0">物品名称：</td><td style="border-top:0"><input class="easyui-textbox" id="itemName" name="itemName"style="width:112px; " data-options="required : true"></td>
												<td class="honry-lable" style="border-top:0">自定义码：</td><td style="border-top:0"><input class="easyui-textbox" id="customCode" name="customCode"style="width:112px; "></td>
												<td class="honry-lable" style="border-top:0">物品别名：</td><td style="border-top:0;border-right:0"><input class="easyui-textbox" id="otherName" name="otherName"style="width:112px; "></td>
											</tr>
											<tr> 
												<td class="honry-lable">别名自定义码：</td><td><input class="easyui-textbox" id="otherCustom" name="otherCustom"style="width:112px; "></td>
												<td class="honry-lable">物品规格：</td><td><input class="easyui-textbox" id="specs" name="specs" data-options="required : true"  style="width:112px; "></td>
												<td class="honry-lable">零售价：</td><td><input class="easyui-numberbox" id="salePrice" name="salePrice" min="0.01" max="1000000000" value="1" precision="2" type="text" data-options="required : true"  style="width:112px; "></td>
												<td class="honry-lable">最小单位：</td><td style="border-right:0"><input  id="minUnit" name="minUnit" style="width:112px;" data-options="required : true">
													<a href="javascript:delSelectedData('minUnit');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
												</td>
											</tr>
											<tr> 
												<td class="honry-lable">国际码：</td><td><input class="easyui-textbox" id="gbCode" name="gbCode" style="width:112px; "></td>
												<td class="honry-lable">物品分类：</td><td><input class="easyui-combobox" id="kindCode" name="kindCode" data-options="required : true" style="width:112px;"></td>
												<td class="honry-lable">大包装数量：</td><td><input class="easyui-numberbox" id="packQty" name="packQty" style="width:112px;" data-options="required : true"></td>
												<td class="honry-lable">大包装单位：</td><td style="border-right:0"><input id="packUnit" name="packUnit" style="width:112px;" data-options="required : true">
													<a href="javascript:delSelectedData('packUnit');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
												</td>
											</tr>
											<tr> 
												<td class="honry-lable">最新入库价：</td><td><input class="easyui-numberbox" id="inPrice" min="0.01" max="1000000000" value="1" precision="2" name="inPrice" style="width:112px; "></td>
												<td class="honry-lable">有效范围：</td><td><input class="easyui-combobox" id="effectArea" name="effectArea" style="width:112px;" data-options="valueField:'id',textField:'text',data:effectAreaData"></td>
												<td class="honry-lable">大包装价格：</td><td><input class="easyui-numberbox" id="packPrice" name="packPrice" style="width:112px; "></td>
												<td class="honry-lable">费用类别：</td><td style="border-right:0"><input class="easyui-textbox" id="recipeNo" name="feeCode" style="width:112px; "></td>
											</tr>
											<tr> 
												<!-- <td>所属库房：</td><td><input class="easyui-textbox" id="storageCode" name="storageCode"style="width:112px; "></td> -->
												<td class="honry-lable">生产厂家：</td><td><input id="factoryCode" name="factoryCode" style="width:112px; ">
												<a href="javascript:delSelectedData('factoryCode');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
												</td>
												<td class="honry-lable">加价规则：</td><td><input id="addRate" name="addRate" style="width:112px; "></td>
												<td class="honry-lable">来源：</td><td><input class="easyui-textbox" id="inSource" name="inSource"style="width:112px; "></td>
												<td class="honry-lable">用途：</td><td style="border-right:0"><input class="easyui-textbox" id="usage" name="usage"style="width:112px; "></td>
											</tr>
											<tr>
												
												<td class="honry-lable">供货公司：</td><td><input id="companyCode" name="companyCode"style="width:112px; ">
												<a href="javascript:delSelectedData('companyCode');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
												</td>
												<td class="honry-lable">备注：</td><td colspan="5" style="border-right:0"><input class="easyui-textbox" id="memo" name="memo" style="width:200px; "></td>
											</tr>
											<tr>
												<td colspan="8" style="border-right:0">
													停用：
									    			<input type="hidden" id="validFlagh" name="validFlag"/>
									    			<input type="checkBox"  id="validFlag" onclick="javascript:checkBoxSelect('validFlag',1,0)"/>
									    			&nbsp;&nbsp;财务收费：
									    			<input type="hidden" id="financeFlagh" name="financeFlag"/>
									    			<input type="checkBox"  id="financeFlag" onclick="javascript:checkBoxSelect('financeFlag',0,1)"/>
									    			&nbsp;&nbsp;高值耗材：
									    			<input type="hidden" id="highvalueFlagh" name="highvalueFlag"/>
									    			<input type="checkBox"  id="highvalueFlag" onclick="javascript:checkBoxSelect('highvalueFlag',0,1)"/>
									    			&nbsp;&nbsp;特殊标志：
									    			<input type="hidden" id="specialFlagh" name="specialFlag"/>
									    			<input type="checkBox"  id="specialFlag" onclick="javascript:checkBoxSelect('specialFlag',0,1)"/>
									    			&nbsp;&nbsp;是否打包：
									    			<input type="hidden" id="packFlagh" name="packFlag"/>
									    			<input type="checkBox"  id="packFlag" onclick="javascript:checkBoxSelect('packFlag',0,1)"/>
									    			&nbsp;&nbsp;一次性耗材：
									    			<input type="hidden" id="norecycleFlagh" name="norecycleFlag"/>
									    			<input type="checkBox"  id="norecycleFlag" onclick="javascript:checkBoxSelect('norecycleFlag',0,1)"/>
									    			&nbsp;&nbsp;是否按批次处理：
									    			<input type="hidden" id="batchFlagh" name="batchFlag"/>
									    			<input type="checkBox"  id="batchFlag" onclick="javascript:checkBoxSelect('batchFlag',0,1)"/>
									    			&nbsp;&nbsp;是否按月计划入库：
									    			<input type="hidden" id="planh" name="plan"/>
									    			<input type="checkBox"  id="plan" onclick="javascript:checkBoxSelect('plan',0,1)"/>
												</td>
											</tr>
										</table>
					    			</form>
							    </div>   
							    <div data-options="region:'center'" style="width:17%;border-top:0">
							    	<table id="baseFile" class="easyui-datagrid"  data-options="method:'post',idField: 'id',striped:true,border:false,fitColumns:true,singleSelect:true,selectOnCheck:true,fit:true">
										<thead>
											<tr>
												<th data-options="field:'id',hidden:true"></th>
												<th data-options="field:'foreignCode',hidden:true"></th>
												<th data-options="field:'fileKind',hidden:true"></th>
												<th data-options="field:'loadStatus',hidden:true"></th>
												<th data-options="field:'fileName', width : '50%'">文件名</th>
												<th data-options="field:'memo',editor:{type:'textbox'}, width : '45%'">备注</th>
												<th data-options="field:'filePath',hidden:true">路径</th>
											</tr>
										</thead>
									</table>
							    </div>   
							    <div data-options="region:'east'" style="width:17%;border-top:0;padding:2px 0 0 2px;">
							    	<form id="baseUploadForm" method="post" enctype="multipart/form-data">
					    				<table id="BaseName">
					    				<!-- 选择文件 class="easyui-filebox" -->
											<tr><td><input id="fileBaseName" name="fileBase" style="width:200px"></input></td></tr>
											<tr><td><a href="javascript:void(0)" name="filePhoto" onclick="upload()" class="easyui-linkbutton" iconCls="icon-disk_upload">上传文件</a></td></tr>
											<tr><td><a href="javascript:void(0)" onclick="checkBase();" class="easyui-linkbutton" iconCls="icon-see">查看文件</a></td></tr>
											<tr><td><a href="javascript:void(0)" id="remove" onclick="delFiles()" class="easyui-linkbutton" iconCls="icon-remove">删除文件</a></td></tr>
										</table>
					    			</form>
							    </div>
							    <div data-options="region:'south'" style="height:50px;padding-top: 10px" align="center">
							    	<a  href="javascript:void(0)" onclick="saveBaseInfo()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
									<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" >清除</a>
							    </div>   
							</div>   
					    </div>   
					    <div title="注册信息" data-options="fit:true">   
					        <div id="dd" class="easyui-layout" data-options="fit:true">
					        	<div data-options="region:'center'" style="width: 66%;border-top:0">
					        		<div id="rr" class="easyui-layout" data-options="fit:true">   
									    <div data-options="region:'west',border:false" style="width:40%;">
									    	<form id="matBaseRegInfoForm">
							    				<table id="secTable" class="honry-table" cellpadding="1" cellspacing="1" border="0"  style="width:100%;border:0 ">
													<tr><td class="honry-lable" style="border-left:0;border-top:0">生产厂家</td><td colspan="3" style="border-top:0;border-right:0"><input id="factoryCodeReg" name="factoryCode"></td></tr>
													<tr><td class="honry-lable" style="border-left:0">注册号</td><td colspan="3" style="border-right:0"><input class="easyui-textbox" id="registerCodeReg" name="registerCode" style="width:177px; "></td></tr>
													<tr><td class="honry-lable" style="border-left:0">注册时间</td><td colspan="3" style="border-right:0">
													<input id="registerDateReg" name="registerDate" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly: true})"/>
													</td></tr>
													<tr><td class="honry-lable" style="border-left:0">到期时间</td><td colspan="3" style="border-right:0">
													<input id="overDateReg" name="overDate" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly: true})"/>
													</td></tr>
													<tr><td class="honry-lable" style="border-left:0">物品规格</td><td colspan="3" style="border-right:0"><input  class="easyui-textbox" id="specsReg" name="specs" style="width:177px; "></td></tr>
													<tr><td class="honry-lable" style="border-left:0">包装单位</td><td style="border-right:0"><input id="packUnitReg" name="packUnit" style="width:75px; "></td><td>大包装价</td><td><input  class="easyui-numberbox" id="packPriceReg" name="packPrice" style="width:75px; "></td></tr>
													<tr><td colspan="4" style="border-left:0">默认：
											    			<input type="hidden" id="defaultFlagRegh" name="defaultFlag"/>
											    			<input type="checkBox"  id="defaultFlagReg" onclick="javascript:checkBoxSelect('defaultFlagReg',0,1)"/>
											    			&nbsp;&nbsp;作废：
											    			<input type="hidden" id="validFlagRegh" name="validFlag"/>
											    			<input type="checkBox"  id="validFlagReg" onclick="javascript:checkBoxSelect('validFlagReg',1,0)"/>
											    		</td>
											    	</tr>
												</table>
							    			</form>
							    			<div id="totalDivId" style="height:35px;margin-top: 4px"  align="center">
												<a  href="javascript:void(0)" onclick="addRegister()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">添加</a>
												<a href="javascript:void(0)" onclick="delRegister()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" >删除</a>
											</div>
									    </div>   
									    <div data-options="region:'center'" style="width: 27%;border-top:0">
									    	<table id="regFile" class="easyui-datagrid" style="width:100%;" data-options="singleSelect:true,fit:true,selectOnCheck:true,border:false">
												<thead>
													<tr>  
														<th data-options="field:'id',hidden:true"></th>
														<th data-options="field:'foreignCode',hidden:true"></th>
														<th data-options="field:'fileKind',hidden:true"></th>
														<th data-options="field:'loadStatus',hidden:true"></th>
														<th data-options="field:'fileName', width : '50%'">文件名</th>
														<th data-options="field:'memo',editor:{type:'textbox'}, width : '45%'">备注</th>
														<th data-options="field:'filePath',hidden:true">路径</th>
													</tr>
												</thead>
											</table>
									    </div>   
									    <div data-options="region:'east'" style="width:27%;border-top:0;padding:2px 0 0 2px;">
									    	<form id="regUploadForm" method="post" enctype="multipart/form-data" >
						    					<table id="RegName">
						    					<!-- class="easyui-filebox" -->
													<tr><td><input  id="fileRegName" name="fileReg"  style="width:200px"></input></td></tr>
													<tr><td><a href="javascript:void(0)" onclick="uploadReg()" class="easyui-linkbutton" iconCls="icon-disk_upload">上传文件</a></td></tr>
													<tr><td><a href="javascript:void(0)" id="baseReg" onclick="checkReg();" class="easyui-linkbutton" iconCls="icon-see">查看文件</a></td></tr>
													<tr><td><a href="javascript:void(0)" id="removeReg" onclick="delFilesReg()" class="easyui-linkbutton" iconCls="icon-remove">删除文件</a></td></tr>
												</table>
											</form>
									    </div>   
									</div> 
					        	</div>
					        	<div data-options="region:'west'" style="width:40%;border-top:0">
					        		<table id="regInfo" class="easyui-datagrid" data-options="border:false,fit:true">
										<thead>
											<tr>
												<th data-options="field:'id',hidden:true">id</th>
												<th data-options="field:'factoryCode',formatter:factoryFamater,width : '30%'">生产厂家</th>
												<th data-options="field:'validFlagShow', width : '12%'">状态</th>
												<th data-options="field:'specs', width : '15%'">物品规格</th>
												<th data-options="field:'defaultFlagShow', width : '12%'">默认</th>
												<th data-options="field:'itemCode',hidden:true">物品编码</th>
												<th data-options="field:'registerCode'">注册号</th>
												<th data-options="field:'registerDate'">注册时间</th>
												<th data-options="field:'overDate'">到期时间</th>
												<th data-options="field:'packUnit',hidden:true">包装单位</th>
												<th data-options="field:'packPrice',hidden:true">大包装价</th>
												<th data-options="field:'validFlag',hidden:true">状态</th>
												<th data-options="field:'defaultFlag',hidden:true">默认</th>
												<th data-options="field:'loadStatus',hidden:true"></th>
											</tr>
										</thead>
									</table>
					        	</div>   
							    <div data-options="region:'south'" style="height: 50px;padding-top: 10px" align="center">
							    	<a  href="javascript:void(0)" onclick="saveBaseInfo()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
									<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" >清除</a>
							    </div>  
					        </div>
					    </div>
			    	</div>
			    </div>   
			</div> 
	    </div>   
	</div>
	<div id="selectStorage"></div>
	<div id="addRemarkDiv"></div>
	<div id="addMatKindinfo"></div> 
	<div id="mm" class="easyui-menu" data-options="" style="width: 100px;">
		<div id="addMatKindinfoTree" onclick="addMatKindinfoTree()" data-options="name:'new',iconCls:'icon-add',">
			添加物资分类
		</div>
		<div id="deleteMatKindinfoTree" onclick="deleteMatKindinfoTree()" data-options="name:'new',iconCls:'icon-bullet_minus'">
			删除物资分类
		</div>
		<div id="updateMatKindinfoTree" onclick="update()" data-options="name:'new',iconCls:'icon-bullet_edit'">
			修改物资分类
		</div>
	</div>
</body>
</html>