<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript">
var MaxUnit ="";
var MinUnit ="";
$(function(){
	bindEnterEvent('code',query,'easyui');
	
	$('#deptC').combobox({    
		url: '<%=basePath %>material/materials/findDeptCCombobox.action',
	    valueField:'id',    
	    textField:'deptName',
	    onSelect:function(record){
	    	$('#list').datagrid('load', {
	    		deptC:record.deptId
	    	});
	    	var rows = $('#lists').datagrid('getRows');
	    	for(var i=rows.length-1;i>=0;i--){
	    		$('#lists').datagrid('deleteRow',$('#lists').datagrid('getRowIndex',rows[i]));
	    	}
	    }
	});
	$('#deptR').combobox({    
		url: '<%=basePath %>material/materials/findDeptRCombobox.action',
	    valueField:'id',    
	    textField:'deptName'
	});		
	
	//渲染包装单位
	$.ajax({
		url: '<%=basePath %>material/materials/findFunctionMaxUnit.action',
		type:'post',
		success: function(MaxUnitData) {
			MaxUnit = eval("("+MaxUnitData+")");
		}
	});
	
	$.ajax({
		url: '<%=basePath %>material/materials/findFunctionMinUnit.action',
		type:'post',
		success: function(MinUnitData) {
			MinUnit = eval("("+MinUnitData+")");
		}
	});
	
	//加载左侧列表
	$('#list').datagrid({
		url: '<%=basePath %>material/materials/findMatStockdetail.action',
		pagination: true,
		onDblClickRow: function (rowIndex, rowData) {//双击查看
			var deptHidden = $('#deptHidden').val();
			$.ajax({
				url: '<%=basePath %>material/materials/verificationMatStockdetail.action?deptId='+deptHidden+'&itemCode='+rowData.itemCode,
				type:'post',
				success: function(data) {
					dataObj = eval("("+data+")");
					if(dataObj.resMsg=="error"){
						$.messager.alert("操作提示", "该物资库存不足");
					}else if(dataObj.resMsg=="success"){
						var start = "";//状态
						var rows = $('#lists').datagrid('getRows');
						if(rows.length>0){
							for(var i=0;i<rows.length;i++){
								if(rowData.itemCode==rows[i].itemCode){
									start = "1";//状态
									$.messager.alert("操作提示", "该物资存在");
								}
							}
							if(start!="1"){
								var index = $('#lists').edatagrid('appendRow',{
									itemName: rowData.itemName,
									itemCode: rowData.itemCode,
									specs: rowData.specs,
									cqty: 1,
									unit:"",
									cPrice: rowData.reprice,
									unitQty:rowData.maxQty,
									price: rowData.price,
									reprice: rowData.reprice,
									qty: rowData.qty,
									remark: "",
									isBarCode: "",
									barCode: ""
								}).datagrid('getRows').length-1;
							}
						}else{
							var index = $('#lists').edatagrid('appendRow',{
								itemName: rowData.itemName,
								itemCode: rowData.itemCode,
								specs: rowData.specs,
								cqty: 1,
								unit:"",
								cPrice: rowData.reprice,
								unitQty:rowData.maxQty,
								price: rowData.price,
								reprice: rowData.reprice,
								qty: rowData.qty,
								remark: "",
								isBarCode: "",
								barCode: ""
							}).datagrid('getRows').length-1;
						}
					}
				}
			});
	    }
	});
	
	
	$('#lists').datagrid({
		onSelect: function(rowIndex, rowData){
			var onDbListRows = $('#lists').datagrid('getRows');
			if(onDbListRows.length>0){
				for(var m=0;m<onDbListRows.length;m++){
					var indexRows = $('#lists').datagrid('getRowIndex',onDbListRows[m]);
					$('#lists').datagrid('endEdit',indexRows);
				}
			}
			$('#lists').datagrid('beginEdit',rowIndex);
			var ed = $('#lists').datagrid('getEditor', {index:rowIndex,field:'cqty'});
			var t = $(ed.target).numberbox('getText');
			$(ed.target).next("span").children().first().val("").focus().val(t);
			var  reprice = rowData.reprice;
			$(ed.target).numberbox('textbox').bind('keyup', function(event) {
				var ed1 = $('#lists').datagrid('getEditor', {index:rowIndex,field:'cPrice'});
				var ed2 = $('#lists').datagrid('getEditor', {index:rowIndex,field:'remark'});
				var remork = $(ed2.target).numberbox('getText');
				var totalNumSum = rowData.qty;
				var totalNum = $(ed.target).numberbox('getText');
				if(totalNum>totalNumSum){
					$.messager.alert("操作提示", "出库量已经超过库存量");
					$('#lists').datagrid('updateRow',{
						index: rowIndex,
						row: {
							cPrice: 0,
							cqty: 0
						}
					});
				}else{
					var zjes = totalNum*reprice;
					$('#lists').datagrid('updateRow',{
						index: rowIndex,
						row: {
							cPrice: zjes,
							cqty: totalNum,
							remark:remork
						}
					});
				}
				$('#lists').datagrid('selectRow',rowIndex);
			});
		}
	});
	
});

//渲染包装单位
function functionMaxUnit(value,row,index){
	if(value!=null&&value!=''){
		return MaxUnit[value];
	}
}
//渲染最小单位
function functionMinUnit(value,row,index){
	if(value!=null&&value!=''){
		return MinUnit[value];
	}
}

//保存
function save(){
	var rows = $('#lists').datagrid('getRows');
	var jsonString = encodeURIComponent(encodeURIComponent(JSON.stringify(rows))); 
	var deptC = $('#deptHidden').val();
	var deptR = $('#deptR').combobox('getValue');
	if(deptR==""||deptR==null){
		$.messager.alert("操作提示","请选择申请科室");
		return;
	}
	if(rows.length<=0){
		$.messager.alert("操作提示","请选择申请物资");
		return;
	}
	$.ajax({
		url: '<%=basePath %>material/materials/verificationMatStockdetailSave.action?jsonString='+jsonString+'&deptC='+deptC,
		type:'post',
		success: function(dataMap) {
			
			var dataMapObj = eval("("+dataMap+")");
			if(dataMapObj.resMsg=="error"){
				$.messager.alert("操作提示",dataMapObj.resCode );
			}else if(dataMapObj.resMsg=="success"){
				$.ajax({
					url: '<%=basePath %>material/materials/saveMatApply.action?jsonString='+jsonString+'&deptC='+deptC+'&deptR='+deptR,
					type:'post',
					success: function(data) {
						if(data=="ok"){
							$.messager.alert("操作提示", "出库成功");
							window.location.href="<c:url value='/material/materials/listmaterials.action'/>?menuAlias=${menuAlias}";
						}else{
							$.messager.alert("操作提示", "出库失败");
						}
					}
				});
			}
		}
	});
}

//删除
function del(){
	var rows = $('#lists').datagrid('getChecked');
	for(var i=rows.length-1;i>=0;i--){
		$('#lists').datagrid('deleteRow',$('#lists').datagrid('getRowIndex',rows[i]));
	}
}

//查询
function query(){
	var code = $('#code').textbox('getValue');
	$('#list').datagrid('load', {
		code:code
	});
}
//渲染
function functionIsBarCode(value,row,index){
	if(value=="1"){
		return '是';
	}else if(value=="2"){
		return '不是';
	}
}

</script>
</head>
<body>
<div id="divLayout" class="easyui-layout" fit=true style="width: 100%;height: 100%;">
	<div style="padding:5px 5px 5px 5px;">	
		<table style="width:100%;border:1px solid #95b8e7;padding:5px;">
			<tr>
				<td>
					<a href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" iconCls="icon-save">保存</a>
					<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" iconCls="icon-remove">删除</a>
					<a href="javascript:void(0)" onclick="exper()" class="easyui-linkbutton" iconCls="icon-down">打印</a>
				</td>
			</tr>
		</table>	   
	</div>
	<div style="padding: 0px 5px 5px 5px;">
		<input id = "deptHidden" type="hidden" value="${deptId }">
		<table id="list" style="width:100%;height:300px" class="easyui-datagrid" data-options="idField: 'id',border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
			<thead>
				<tr>
					<th data-options="field:'id',checkbox:true" style="width:5%; text-align: center">id</th>
					<th data-options="field:'itemName'"  style="width:10%" >物品名称</th>
					<th data-options="field:'specs'"  style="width:10%">规格</th>
					<th data-options="field:'price'" style="width:10% " >入库单价</th>
					<th data-options="field:'reprice'"  style="width:10%">零售单价</th>
					<th data-options="field:'qty'"  style="width:10% ">库存数量</th>
					<th data-options="field:'minUnit',formatter:functionMinUnit"  style="width:10% ">最小单位</th>
					<th data-options="field:'maxUnit',formatter:functionMaxUnit"  style="width:10% ">大包装单位</th>
					<th data-options="field:'maxQty'" style="width:10% ">大包装数量</th>
					<th data-options="field:'barCode'"  style="width:10% ">高值耗材条形码</th>
					<th data-options="field:'itemCode',hidden:true"  style="width:10% ">物品编码</th>
				</tr>
			</thead>
		</table>
	</div>
	<div style="padding: 0px 5px 5px 5px;">
		<table id="lists" style="width:100%;" class="easyui-datagrid" data-options="striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarIds'">
			<thead>
				<tr>
					<th data-options="field:'id',checkbox:true" style="width:5%; text-align: center">id</th>
					<th data-options="field:'itemName'"  style="width:10%" >物品名称</th>
					<th data-options="field:'specs'"  style="width:5%">规格</th>
					<th data-options="field:'cqty',editor:{type:'numberbox'}" style="width:5% " >出库数量</th>
					<th data-options="field:'unit'"  style="width:10%">单位</th>
					<th data-options="field:'cPrice'"  style="width:5% ">出库金额</th>
					<th data-options="field:'unitQty'"  style="width:5% ">包装数量</th>
					<th data-options="field:'price'"  style="width:5% ">购入价格</th>
					<th data-options="field:'reprice'" style="width:5% ">零售价</th>
					<th data-options="field:'qty'"  style="width:5% ">库存数量</th>
					<th data-options="field:'remark',editor:{type:'textbox'}"  style="width:20% ">备注</th>
					<th data-options="field:'isBarCode',formatter:functionIsBarCode,editor:{type:'combobox',options:{valueField: 'id', textField: 'value',data: [{ id: '1', value: '是'},{ id: '2', value: '不是'}]}}"  style="width:10% ">是否高值耗材条形码</th>
					<th data-options="field:'barCode'"  style="width:10% ">高值耗材条形码</th>
					<th data-options="field:'itemCode',hidden:true"  style="width:10% ">物品编码</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="toolbarId">
		检索条件：<input id="code" name="code" class="easyui-textbox">&nbsp;&nbsp;&nbsp;&nbsp;
		出库科室：<input id="deptC" class="easyui-combobox">&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
	<div id="toolbarIds">
		入库科室：<input id="deptR" class="easyui-combobox">&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
</div>
</body>
</html>