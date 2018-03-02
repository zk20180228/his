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
var	reprices ="";
$(function(){
	//回车事件 模糊查询
	bindEnterEvent('code',query,'easyui');
	
	$('#rateIncrease').numberbox('textbox').bind('keyup', function(event) {
		var rateIncrease = $('#rateIncrease').numberbox('getText');
		var price = $('#price').val();
		var rows = $('#lists').datagrid('getRows');
		if(rows.length>0){
			if(price==0){//采用进价的方式：零售价=加价率*进价
				for(var i=0;i<rows.length;i++){
					reprices = ((rows[i].price)*rateIncrease).toFixed(2);
					$('#lists').datagrid('updateRow',{
						index: i,
						row: {
							reprice: reprices
						}
					});
				}
			}else if(price==1){//采用零售价的方式：零售价=零售价*加价率
				for(var i=0;i<rows.length;i++){
					reprices = ((rows[i].reprice)*rateIncrease).toFixed(2);
					$('#lists').datagrid('updateRow',{
						index: i,
						row: {
							reprice: reprices
						}
					});
				}
			}
		}
	});
	
	//加载list列表
	$('#list').datagrid({
		url: '<%=basePath %>material/priceLetOut/findMatStockdetail.action',
		pagination: true,
		pageSize : 10,
		pageList : [ 10, 20, 50, 100 ],
		onDblClickRow: function (rowIndex, rowData) {//双击查看
			var deptHidden = $('#deptHidden').val();//出库科室
			var rateIncrease = $('#rateIncrease').val();//加价率
			$.ajax({
				url: '<%=basePath %>material/applyLibrary/verificationMatStockdetail.action?deptId='+deptHidden+'&itemCode='+rowData.itemCode,
				type:'post',
				success: function(data) {
					dataObj = eval("("+data+")");
					if(dataObj.resMsg=="error"){
						$.messager.alert("操作提示", "该物资库存不足");
					}else if(dataObj.resMsg=="success"){
						var price = $('#price').val();
						if(price==0){//采用进价的方式：零售价=加价率*进价
							reprices = ((rowData.price)*rateIncrease).toFixed(2);
						}else if(price==1){//采用零售价的方式：零售价=零售价*加价率
							reprices = ((rowData.reprice)*rateIncrease).toFixed(2)	;
						}
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
									unit:rowData.minUnit,
									cPrice: rowData.reprice,
									unitQty:rowData.maxQty,
									price: rowData.price,
									reprice:reprices,
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
								unit:rowData.minUnit,
								cPrice: rowData.reprice,
								unitQty:rowData.maxQty,
								price: rowData.price,
								reprice:reprices,
								qty: rowData.qty,
								remark: "",
								isBarCode: "",
								barCode: ""
							}).datagrid('getRows').length-1;
						}
					}
				}
			});
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
	
	//出库下拉框 
	$('#deptC').combobox({    
		  url : "<c:url value='/material/orderInoutdept/deptList.action?t1.stockClass=1'/>",
			valueField : 'objectDeptCode',
			textField : 'objectDeptName',
			mode:'remote',
			onSelect: function(rec){
				var url='<%=basePath %>material/priceLetOut/findMatStockdetail.action?deptId='+rec.objectDeptCode;
				$('#list').datagrid('reload', url);
			}
<%-- 		url: '<%=basePath %>material/materials/findDeptCCombobox.action', --%>
// 		valueField:'id',    
// 		textField:'deptName',
// 		onSelect:function(record){
// 			$('#deptHidden').val(record.deptId);
// 			$('#list').datagrid('load', {
// 				deptId:record.deptId
// 			});
// 			var rows = $('#lists').datagrid('getRows');
// 			for(var i=rows.length-1;i>=0;i--){
// 				$('#lists').datagrid('deleteRow',$('#lists').datagrid('getRowIndex',rows[i]));
// 			}
			
// 		}
	});
	//入库科室下拉
	$('#deptR').combobox({    
	    url : "<c:url value='/material/orderInoutdept/deptList.action?t1.stockClass=1'/>",
		valueField : 'objectDeptCode',
		textField : 'objectDeptName',
		mode:'remote',
		onSelect: function(rec){
				var url='<%=basePath %>material/priceLetOut/findMatStockdetail.action?deptId='+rec.objectDeptCode;
<%-- 			var url='<%=basePath%>material/orderApply/loadData.action?t1.applyState=0&t1.targetDept='+rec.objectDeptCode; --%>
			$('#list').datagrid('reload', url);
		}
	});		
	
	//渲染包装单位
	$.ajax({
		url: '<%=basePath %>material/materials/findFunctionMaxUnit.action',
		type:'post',
		success: function(MaxUnitData) {
			MaxUnit = eval("("+MaxUnitData+")");
		}
	});
	//渲染最小单位
	$.ajax({
		url: '<%=basePath %>material/materials/findFunctionMinUnit.action',
		type:'post',
		success: function(MinUnitData) {
			MinUnit = eval("("+MinUnitData+")");
		}
	});
	
});
	
	
//模糊查询
function query(){
	var code = $('#code').textbox('getValue');
	$('#list').datagrid('load', {
		code:code
	});
}

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

//删除
function del(){
	var rows = $('#lists').datagrid('getChecked');
	for(var i=rows.length-1;i>=0;i--){
		$('#lists').datagrid('deleteRow',$('#lists').datagrid('getRowIndex',rows[i]));
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
		url: '<%=basePath %>material/priceLetOut/verificationMatStockdetailSave.action?jsonString='+jsonString+'&deptC='+deptC,
		type:'post',
		success: function(dataMap) {
			var dataMapObj = eval("("+dataMap+")");
			if(dataMapObj.resMsg=="error"){
				$.messager.alert("操作提示",dataMapObj.resCode );
			}else if(dataMapObj.resMsg=="success"){
				var price = $('#price').val();
				$.ajax({
					url: '<%=basePath %>material/priceLetOut/saveMatApply.action?jsonString='+jsonString+'&deptC='+deptC+'&deptR='+deptR+'&price='+price,
					type:'post',
					success: function(data) {
						if(data=="ok"){
							$.messager.alert("操作提示", "出库成功");
							window.location.href="<c:url value='/material/priceLetOut/listPriceLetOut.action'/>?menuAlias=${menuAlias}";
						}else{
							$.messager.alert("操作提示", "出库失败");
						}
					}
				});
			}
		}
	});
}
</script>
</head>
<body>
<div id="divLayout" class="easyui-layout" fit=true style="width: 100%;height: 100%;">
	<div style="padding:5px 5px 5px 5px;">	
		<table style="width:100%;border:1px solid #95b8e7;padding:10px;" class="changeskin">
			<tr>
				<td>
					<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					<a href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" iconCls="icon-save">保存</a>
					<a href="javascript:void(0)" onclick="preview()" class="easyui-linkbutton" iconCls="icon-see">预览</a>
					<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" iconCls="icon-remove">删除</a>
					<a href="javascript:void(0)" onclick="exper()" class="easyui-linkbutton" iconCls="icon-2012081511202">打印</a>
				</td>
			</tr>
		</table>	   
	</div>
	<div style="padding: 0px 5px 5px 5px;;height:50%">
		<input type="hidden" id="deptHidden" value="${deptId}">
		<input type="hidden" id="price" value="${price}">
		<table id="list" style="width:100%;height:100%" class="easyui-datagrid" data-options="idField: 'id',border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
			<thead>
				<tr>
					<th data-options="field:'id',checkbox:true" style="width:5%; text-align: center">id</th>
					<th data-options="field:'itemName'"  style="width:10%" >物品名称</th>
					<th data-options="field:'specs'"  style="width:10%">规格</th>
					<th data-options="field:'price'" style="width:10% " align='right' >入库单价</th>
					<th data-options="field:'reprice'"  style="width:10%" align='right' >零售单价</th>
					<th data-options="field:'qty'"  style="width:10% " align='right' >库存数量</th>
					<th data-options="field:'minUnit'"  style="width:10% ">最小单位</th>
					<th data-options="field:'maxUnit'"  style="width:10% ">大包装单位</th>
					<th data-options="field:'maxQty'" style="width:10% " align='right' >大包装数量</th>
					<th data-options="field:'barCode'"  style="width:10% ">高值耗材条形码</th>
					<th data-options="field:'itemCode',hidden:true"  style="width:10% ">物品编码</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="listsDiv" style="padding: 0px 5px 5px 5px;height:35%">
		<table id="lists" style="width:100%;height:100%" class="easyui-datagrid" data-options="striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarIds'">
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
					<th data-options="field:'isBarCode',editor:{type:'combobox',options:{valueField: 'id', textField: 'value',data: [{ id: '1', value: '是'},{ id: '2', value: '不是'}]}}"  style="width:10% ">是否高值耗材条形码</th>
					<th data-options="field:'barCode'"  style="width:10% ">高值耗材条形码</th>
					<th data-options="field:'itemCode',hidden:true"  style="width:10% ">物品编码</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="toolbarId">
		检索条件：<input id="code" name="code" class="easyui-textbox">&nbsp;&nbsp;&nbsp;&nbsp;
		出库科室：<input id="deptC" class="easyui-combobox">&nbsp;&nbsp;&nbsp;&nbsp;
		加价率：<input id="rateIncrease" class="easyui-numberbox" data-options="precision:2" value="${rateIncrease}">&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
	<div id="toolbarIds">
		入库科室：<input id="deptR" class="easyui-combobox">&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
</div>
</body>
</html>