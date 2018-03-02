<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<body>
	<form id="treeEditForm" method="post" >
		<div title="Tab1" style="padding: 20px;">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr style="display: none;" align="center">
					<td class="honry-lable">药房名称:</td>
					<td>
						<input id="pharmacyCombobox" value="${modelVsItem.drugRoomName }" class="easyui-combobox" style="width:290px;">
		    			<input type="hidden" id="pharmacy" name="modelVsItem.drugRoom" value="${modelVsItem.drugRoom }"/>
		    			<input type="hidden" id="pharmacyName" name="modelVsItem.drugRoomName" value="${modelVsItem.drugRoomName }"/>
					</td>
				</tr>
				<tr align="center">
	    			<td class="honry-lable">药品名称:</td>
	    			<td>
	    				<input id="itemName" value="${modelVsItem.itemName }" style="width:290px"/>
		    			<input type="hidden" id="itemCode" name="modelVsItem.itemCode" value="${modelVsItem.itemCode }"/>
		    			<input type="hidden" id="itemNameHidden" name="modelVsItem.itemName" value="${modelVsItem.itemName }"/>
						<input type="hidden" id="id" name="modelVsItem.id" value="${modelVsItem.id }"/>
						<input type="hidden" name="modelVsItem.modelId" value="${modelVsItem.modelId }"/>
	    			</td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">模板类型:</td>
	    			<td><input class="easyui-combobox" type="text" id="modelClass" name="modelVsItem.modelClass" value="${modelVsItem.modelClass }" data-options="required:true" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">医嘱类型:</td>
	    			<td><input class="easyui-combobox" type="text" id="flag" name="modelVsItem.flag" value="${modelVsItem.flag }" data-options="required:true" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">数量:</td>
	    			<td><input class="easyui-textbox" type="text" id="num" name="modelVsItem.num" value="${modelVsItem.num }" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">单位:</td>
	    			<td><input class="easyui-textbox" type="text" id="unit" name="modelVsItem.unit" value="${modelVsItem.unit }" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">频次:</td>
	    			<td><input class="easyui-textbox" type="text" id="frequencyCode" name="modelVsItem.frequencyCode" value="${modelVsItem.frequencyCode }" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">用法:</td>
	    			<td><input class="easyui-textbox" type="text" id="directionCode" name="modelVsItem.directionCode" value="${modelVsItem.directionCode }" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">是否为必选项:</td>
	    			<td>
	    				<input class="easyui-combobox" type="text" id="chooseFlag" name="modelVsItem.chooseFlag" value="${modelVsItem.chooseFlag }" 
								data-options="panelHeight:'60px' ,valueField: 'value',textField: 'label',data: [{   
				                label: '否',  
				                value: '0', 
				                selected:true}, 
				                {label: '是',
				                value: '1'}]"   
							 style="width:290px"/>
	    			</td>
	    		</tr>
			</table>	
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="submitTreeForm()" class="easyui-linkbutton">确定</a>
				<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeEast()" class="easyui-linkbutton">关闭</a>
			</div>
		</div>		
	</form>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<script type="text/javascript">
	$(function() {
		/**模板类型下拉**/
		$('#modelClass').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=systemType",
			valueField : 'encode',
			textField : 'name',
			multiple : false,
		});
		/**医嘱类型下拉**/
		$('#flag').combobox({
			url: "<%=basePath%>inpatient/clinicalPathwayModelAction/flagCombobox.action",
			valueField : 'typeCode',
			textField : 'typeName',
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'typeCode';
				keys[keys.length] = 'typeName';
				if(filterLocalCombobox(q, row, keys)){
					row.selected=true;
				}else{
					row.selected=false;
				}
				return filterLocalCombobox(q, row, keys);
		    }
		});
	});
	/**药房下拉**/
	$('#pharmacyCombobox').combobox({	
		url:"<%=basePath%>publics/putGetDrug/PGDjsonForPutDeptID.action",
		panelHeight:'60px', 
		valueField:'id',
		textField:'deptName',
		multiple:false,
		editable:false,
		onLoadSuccess: function (data) {
			if (data.length > 0) {
				$('#pharmacyCombobox').combobox('select', data[0].id);
			}
		},
		onSelect:function(record){
			$.ajax({
				type:"post",
				url:"<%=basePath%>outpatient/advice/savaPharmacyInfo.action",
				data:{pharmacyId:record.id},
				success: function(dataMap) {
					if(dataMap.resMsg=="error"){
						$.messager.alert('提示',dataMap.resCode);
					};
// 					$("#pharmacyName").val(record.deptName);
// 					$("#pharmacy").val(record.id);
				},
				error: function(){
					$.messager.alert('提示','请求失败！');
				}
			});
		}
	}); 
	/**药品下拉**/
	$('#itemName').combogrid({
		prompt:'请选择药品',
		panelWidth:650,
		panelHeight:325,
		rownumbers:true,
		pagination:true,
		mode:'remote',
		delay:1000,
		textField: 'name',
		url:"<%=basePath%>outpatient/advice/queryViewInfo.action",
		queryParams:{name:null,typeChi:null},
		pageSize:20,
		pageList:[20,30,50,80,100],
		/* rowStyler: function(index,row){
			if (row.ty==1&&row.surSum<=0){//库存不足
				return 'background-color:#9D9DFF;color:#fff;';
			}
			if (row.stop_flg==1){//停用
				return 'background-color:#FF9191;color:#fff;';
			}
		}, */
		onClickRow:function(rowIndex, rowData){
			initAdviceInfo(rowData);
		},
		columns:[[	
			{field:'id',title:'id',width:60,hidden:true},	
			{field:'name',title:'通用名',width:200,halign:'center'},	
			{field:'ty',title:'类别',width:50,align:'right',halign:'center'},	
			{field:'spec',title:'规格',width:80,align:'right',halign:'center'},
			{field:'price',title:'价格',width:70,align:'right',halign:'center'},
			{field:'insured',title:'医保标记',width:70,align:'right',halign:'center'},
// 			{field:'surSum',title:'库存可用数量',width:100,align:'right',halign:'center'},
			{field:'inputcode',title:'自定义码',width:100,align:'right',halign:'center'},
//				{field:'commonName',title:'名称',width:200,align:'right',halign:'center'},
			{field:'inspectionsite',title:'检查检体',width:70,align:'right',halign:'center'},
			{field:'diseaseclassification',title:'疾病分类',width:80,align:'right',halign:'center'},
			{field:'specialtyName',title:'专科名称',width:80,align:'right',halign:'center'},
			{field:'medicalhistory',title:'病史及检查',width:100,align:'right',halign:'center'},
			{field:'requirements',title:'检查要求',width:80,align:'right',halign:'center'},
			{field:'notes',title:'注意事项',width:80,align:'right',halign:'center'},
		]],
		keyHandler:{
			left: function(e){
				e.preventDefault();
				var pager = $(this).combogrid('grid').datagrid("getPager");
				var options = pager.pagination('options');
				var page = options.pageNumber;
				if(page>1){
					pager.pagination('select',options.pageNumber-1);
				}
			},
			right: function(e){
				e.preventDefault();
				var pager = $(this).combogrid('grid').datagrid("getPager")
				var options = pager.pagination('options');
				var total = options.total; 
				var max = Math.ceil(total/options.pageSize);
				var page = options.pageNumber;
				if(page<max){
					pager.pagination('select',options.pageNumber+1);
				}
			},
			up: function(e){
				e.preventDefault();
				var pClosed = $(this).combogrid('panel').panel('options').closed;
				if (pClosed) {
					$(this).combogrid('showPanel');
				}
				var grid = $(this).combogrid('grid');
				var rowSelected = grid.datagrid('getSelected');
				if (rowSelected != null) {
					var rowIndex = grid.datagrid('getRowIndex', rowSelected);
					if (rowIndex > 0) {
						rowIndex = rowIndex - 1;
						grid.datagrid('selectRow', rowIndex);
					}
				} else if (grid.datagrid('getRows').length > 0) {
					grid.datagrid('selectRow', 0);
				}
			},
			down: function(e){
				e.preventDefault();
				var pClosed = $(this).combogrid('panel').panel('options').closed;
				if (pClosed) {
					$(this).combogrid('showPanel');
				}
				var grid = $(this).combogrid('grid');
				var rowSelected = grid.datagrid('getSelected');
				if (rowSelected != null) {
					var totalRow = grid.datagrid('getRows').length;
					var rowIndex = grid.datagrid('getRowIndex', rowSelected);
					if (rowIndex < totalRow - 1) {
						rowIndex = rowIndex + 1;
						grid.datagrid('selectRow', rowIndex);
					}
				} else if (grid.datagrid('getRows').length > 0) {
					grid.datagrid('selectRow', 0);
				}
			},
			enter: function(e){
				e.preventDefault();
				var pClosed = $(this).combogrid('panel').panel('options').closed;
				if (!pClosed) {
					$(this).combogrid('hidePanel');
				}
				var rowData = $(this).combogrid('grid').datagrid('getSelected');
				if (rowData == null || rowData == undefined) {
					return;
				}else {
					initAdviceInfo(rowData);
				}
			},
			query: function(q,e){
				$(this).combogrid('grid').datagrid('load',{name:q,typeChi:$(this).combobox('getText')});
				$(this).combogrid('setValue',q);
			}
		}
	});
	
	function initAdviceInfo(row){
		console.log(row);
		$("#unit").textbox("setValue", row.unit);
		$("#frequencyCode").textbox("setValue", row.frequency);
		$("#directionCode").textbox("setValue", row.usemode);
		$("#itemCode").val(row.code);
		$("#itemNameHidden").val(row.name);
	}
	//提交验证
	 function submitTreeForm(){
		$.messager.progress({text:'查询中，请稍后...',modal:true});
		$('#treeEditForm').form('submit', {
			url:"<c:url value='/inpatient/clinicalPathwayModelAction/saveOrUpdatePathwayDetail.action'/>",
			data:$('#treeEditForm').serialize(),
	        dataType:'json',
			onSubmit : function() {
				if(!$('#treeEditForm').form('validate')){
					$.messager.show({  
				         title:'提示信息' ,   
				         msg:'验证没有通过,不能提交表单!'  
				    }); 
					$.messager.progress('close');
				    return false ;
				}
			},
			success : function(data) {
				$.messager.progress('close');
				if(data=='true'){
					$.messager.alert('提示','保存成功！');
					closeEast();
					$("#list").datagrid("reload");
				}else{
					$.messager.alert('提示','保存失败');
				}
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示','操作失败！');	
			}
		}); 
	} 
</script>
</body>
</html>