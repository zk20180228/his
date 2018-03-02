<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div class="easyui-panel" id="panelEast"
		data-options="iconCls:'icon-form',border:false" style="width: 100%">
		<div style="padding: 5px">
			<table class="honry-table" style="width: 100%" cellpadding="1" cellspacing="1"
					border="1px solid black" style="margin-left:auto;margin-right:auto;">
				<tr>
					<td class="honry-lable">
						临床路径名称:
					</td>
					<td class="honry-info">
						<input id="clinicalPath" class="easyui-combobox" 
						 missingMessage="请选择临床路径" style="width: 400px" />
					</td>
					<td class="honry-lable">
						版本号:
					</td>
					<td class="honry-info">
						<input id="clinicalVersion" class="easyui-combobox" 
						 missingMessage="请选择版本号"" style="width: 400px" />
					</td>
				</tr>
			</table>
		</div>
		<div style="height: calc(100% - 101px);border-top: 1px solid #77d7d6;border-bottom: 1px solid #77d7d6;" >
			<div style="float: left;width: 20%;">
				<div id="treeDiv" data-options="region:'center',border:false" style="width: 85%">
					<ul id="tDt1">尚未选择临床路径与版本</ul>
				</div>
			</div>
			<div style="float: left;width: calc(40% - 2px);height: 100%;border-left: 1px solid #77d7d6 ;">
				<div >
					<table id="listModel" data-options="method:'post',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
						<thead>
							<tr>
								<th field="id" checkbox="true" data-options="width:'5%'"></th>
								<th data-options="field:'modelCode',formatter: flagModelNature,width:'95%'">临床路径</th>
								<th data-options="field:'modelNature',hidden:'true'"></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
			<div style="float: left;width: 40%">
				<div style="border-left: 1px solid #77d7d6;height: 100%">
					<table id="listModelDetail" data-options="method:'post',idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
						<thead>
							<tr>
								<th field="id" checkbox="true" data-options="width:'5%'"></th>
								<th data-options="field:'itemName',width:'95%'">临床路径明细</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
		<div>
			<table style="width: 100%; border:0px;padding:5px">
				<tr style="width: 100%">
					<td style="width: 100%;text-align: center;">
						<a href="javascript:void(0)" onclick="importClinical()"   class="easyui-linkbutton">导入</a>
						<a href="javascript:void(0)" onclick="closeDouble()" class="easyui-linkbutton">取消</a>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<script type="text/javascript">
	var pageData = {}  //所有数据
	var treeIdTmp = null //树临时id
	var tableTmp = null// 第一个表格临时id
	function closeDouble(){
		console.log(parent.$("#dialogWindows"))
		parent.$("#dialogWindows").modal("hide");
	}
	var impPatientNo = '${impPatientNo}';
	var impdeptCode = '${impdeptCode}';//入径科室
	var cpId = '';
	var versionId = '';
	var modelNatureMap=new Map();
	var isSelect3 = false;
	var isSelect2 = false;
	$(function() {
		/**临床路径模板渲染**/
		$.ajax({
			url: "<%=basePath%>inpatient/clinicalPathwayModelAction/searchAllClinicalModel.action",
			type:'post',
			success: function(data){
				var j = data;
				for(var i=0;i<j.length;i++){
					modelNatureMap.put(j[i].id,j[i].modelName);
				}
			}
		});
		/**临床路径下拉**/
		$('#clinicalPath').combobox({
			url: "<%=basePath%>/inpatient/clinicalPathwayAction/queryCpWay.action",
			valueField : 'id',
			textField : 'cpName',
			multiple : false,
			onLoadSuccess : function(node, data) {
				$('#listModel').datagrid({});
				$('#listModelDetail').datagrid({});
			},
			onSelect: function(rec){
			cpId = rec.id;
            $('#clinicalVersion').combobox({
            	url: "<%=basePath%>/inpatient/clinicalPathwayAction/queryVersionByCpWay.action?cpId="+cpId,
	    			valueField : 'id',//版本id
	    			textField : 'versionNo',
	    			multiple : false,
	    			editable:false,
	    			onSelect: function(rec1){
	    				versionId = rec1.id;
	    				$('#tDt1').tree('options').url = "<c:url value='/inpatient/clinicalPathwayAction/treeStage.action'/>?cpId="+versionId,
						$('#tDt1').tree('reload');
	    				$('#listModel').datagrid({});
						$('#listModelDetail').datagrid({});
	    			}
	    		});
	        }
		});
	});
	//是否当前节点的标识,用节点id记录
	var isNowNode = "";
	//是否保存的标识
	var haveSaveModel = false;
	/**加载时间段树**/
	$('#tDt1').tree({
// 		url : "<c:url value='/inpatient/clinicalPathwayAction/treeStage.action'/>?cpId="+versionId,
		method : 'get',
		animate : true,
		lines : true,
		formatter : function(node) {//统计节点总数
					   var s = node.text;
							if(node.children.length>0){
								if (node.children) {
									s += '&nbsp;<span style=\'color:blue\'>('+ node.children.length + ')</span>';
								}
							}
							return s;
						},
						//设置默认选中根节点
		onLoadSuccess : function(node, data) {
			if(data.length>0){//节点收缩
				$('#tDt1').tree('collapseAll');
			}
		},
		onBeforeSelect : function(node){
			if("undefined"!=typeof(node.attributes)){
				var parent = $('#tDt1').tree('getParent', node.target);
				var id = node.attributes.id;
				if(id){
					//记录之前所勾选的临床路径和临床路径明细
// 					if(isNowNode != id || isNowNode == ""){
// 						isNowNode = id;
// 						if(haveSaveModel){
// 							$.messager.alert('提示','尚未保存组套信息');
// 							return false;
// 						}

// 					}
				}
			}
		},
		onSelect : function(node) {//点击节点
			$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
			var node = $('#tDt1').tree('getSelected');
			if (node) {
				if("undefined"!=typeof(node.attributes)){
					var parent = $('#tDt1').tree('getParent', node.target);
					var id = node.attributes.id;
					if(id){
							modelNatureAll = node.id;//组套id
							idAll = parent.id;//第几天---临床路径计划
							cpIdAll = parent.attributes.cpId;
							stageIdAll = parent.attributes.stageId;
							treeIdTmp = modelNatureAll+"|"+idAll
							if(!pageData[modelNatureAll+"|"+idAll]){
								pageData[modelNatureAll+"|"+idAll] = {}
							}
							
							//记录选中的临床路径和路径明细
							
							//显示节点对应信息
							$('#listModelDetail').datagrid('loadData', { total: 0, rows: [] });
// 							haveSaveModel = true;
							$('#listModel').datagrid({
								url: "<%=basePath%>inpatient/clinicalPathwayModelAction/searchClinicalModelByStage.action?modelNature="+id+"&planId="+cpIdAll+"&stageId="+stageIdAll,
							    onLoadSuccess:function(data){  
									var row = $("#listModel").datagrid("getRows")
									for(var i = 0 ;i<row.length;i++){
										if(pageData[treeIdTmp][row[i].modelCode]){
											$("#listModel").datagrid("checkRow", i);
										} 
									}
							    },
							    onBeforeSelect: function (rowIndex, rowData) {
									var rows = $('#listModel').datagrid('getChecked');
									for(var i=0 ; i<rows.length; i++){
										if(rows[i].id==rowData.id){
											isSelect2 = true;
										}
									}
							    },
								onClickRow: function (rowIndex, rowData) {//单击查看
									tableTmp = rowData.modelCode
									
									if(isSelect2){
										$("#listModel").datagrid("uncheckRow", rowIndex);
										pageData[treeIdTmp][rowData.modelCode] = {}
										$("#listModelDetail").datagrid("uncheckAll");
										isSelect2 = false;
									}else{
										$("#listModel").datagrid("checkRow", rowIndex);
										if(!pageData[treeIdTmp][rowData.modelCode]){
											pageData[treeIdTmp][rowData.modelCode] = {}
										}									
										isSelect2 = false;
									}
// 									haveSaveModel = true;
									$('#listModelDetail').datagrid({
										url: "<%=basePath%>inpatient/clinicalPathwayModelAction/queryClinicalPathModelDetail.action?modelId="+rowData.modelCode,
										onLoadSuccess:function(data){ 
											var row = $('#listModelDetail').datagrid("getRows")
											for(var i = 0 ;i<row.length;i++){
												if(pageData[treeIdTmp][tableTmp][row[i].id] ){
													console.log(row[i])
													$('#listModelDetail').datagrid("checkRow", i);
												} 
											}
											
										}
									});
								},
							});
							

							
							
							
							/**临床路径模板下拉**/
							$('#searchClinicalModel').combobox({
								url: "<%=basePath%>inpatient/clinicalPathwayModelAction/searchClinicalModelByNature.action?modelNature="+id,
								valueField : 'modelCode',
								textField : 'modelName',
								filter:function(q,row){
									var keys = new Array();
									keys[keys.length] = 'modelCode';
									keys[keys.length] = 'modelName';
									if(filterLocalCombobox(q, row, keys)){
										row.selected=true;
									}else{
										row.selected=false;
									}
									return filterLocalCombobox(q, row, keys);
							    },
	 						    onSelect: function(rec){
	 						    	if(checkHaveModel(rec.id)){
	 						    		haveSaveModel = true;
		 						    	$("#listModel").datagrid('insertRow',
	 						    			{index: 0,	// 索引从0开始
		 						    		row: {
												modelCode:rec.id,  
												modelNature : rec.modelNature}
	 						    			}
	 						    		);
	 						    	}
	 						    }
							});
						}
					}
				}
		},
		onBeforeCollapse:function(node){
			if(node.id=="1"){
				return false;
			}
	    }
	});
	//第三个表的点击事件
	$('#listModelDetail').datagrid({
		onClickRow: function (rowIndex, rowData) {
			if(isSelect3){
				$('#listModelDetail').datagrid("uncheckRow", rowIndex);
				pageData[treeIdTmp][tableTmp][rowData["id"]] = false
				isSelect3 = false;
			}else{
				$('#listModelDetail').datagrid("checkRow", rowIndex);
				pageData[treeIdTmp][tableTmp][rowData["id"]] = true
				isSelect3 = false;
			}
		} ,onBeforeSelect: function (rowIndex, rowData) {
			var rows = $('#listModelDetail').datagrid('getChecked');
			for(var i=0 ; i<rows.length; i++){
				if(rows[i].id==rowData.id){
					isSelect3 = true;
				}
			}
	    },
	});
	
	
	var selectClinicalId = '';
	var selectDetailId = '';
	/**记录之前所勾选的临床路径和临床路径明细**/
	function saveClinicalAndDetail(){
		
	}
	
	/**导入临床路径**/
	function importClinical(){
		
		
		console.log(pageData)
		$.ajax({
// 			url : "<c:url value='/inpatient/clinicalPathwayAction/savePatientClincal.action'/>?modelCode="+ ids +"&modelNature="+modelNatureAll+"&id="+idAll+"&cpId="+cpId+"&stageId="+stageIdAll+"&impPatientNo="+impPatientNo+"&versionId="+versionId+"&impdeptCode="+impdeptCode+"&iddetails="+iddetails,
			url : "<c:url value='/inpatient/clinicalPathwayAction/savePatientClincal.action'/>",
			data : {
					data:JSON.stringify(pageData),
					versionId:versionId,
					impPatientNo:impPatientNo,
					impdeptCode:impdeptCode,
					cpId:cpId
				},
			type : 'post',
			success : function() {
				$.messager.alert('提示','导入成功');
				haveSaveModel = false;
				closeDouble();
// 				$('#listModel').datagrid('reload');
			}
		});
		//选中要保存的行
// 		var rows = $('#listModel').datagrid('getChecked');
// 		var rowsDetail = $('#listModelDetail').datagrid('getChecked');
// 		if (rows.length > 0) {//选中几行的触发事件	                        
// 			$.messager.confirm(
// 				'确认',
// 				'确定要保存选中模板吗?',
// 				function(res) {//提示是否保存
// 					if (res) {
// 						//临床路径保存
// 						var ids = '';
// 						for ( var i = 0; i < rows.length; i++) {
// 							if (ids != '') {
// 								ids += ',';
// 							}
// 							ids += rows[i].modelCode;
// 						};
// 						var iddetails = '';
// 						for ( var i = 0; i < rowsDetail.length; i++) {
// 							if (iddetails != '') {
// 								iddetails += ',';
// 							}
// 							iddetails += rowsDetail[i].id;
// 						};
// 						var node = $('#tDt1').tree('getSelected');
// 						var parent = $('#tDt1').tree('getParent', node.target);
// 						$.ajax({
// 							url : "<c:url value='/inpatient/clinicalPathwayAction/savePatientClincal.action'/>?modelCode="+ ids +"&modelNature="+modelNatureAll+"&id="+idAll+"&cpId="+cpId+"&stageId="+stageIdAll+"&impPatientNo="+impPatientNo+"&versionId="+versionId+"&impdeptCode="+impdeptCode+"&iddetails="+iddetails,
// 							type : 'post',
// 							success : function() {
// 								$.messager.alert('提示','导入成功');
// 								haveSaveModel = false;
// 								$('#listModel').datagrid('reload');
// 							}
// 						});
// 					}
// 				}
// 			);
// 		}
	}
	/**临床路径模板渲染**/
	function flagModelNature(value,row,index){
		return modelNatureMap.get(value);
	}
	
	
</script>
</body>
</html>