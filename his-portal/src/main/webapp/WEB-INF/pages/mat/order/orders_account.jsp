<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body class="easyui-layout" style="margin: 0px;padding: 0px;"data-options="fit:true">
<div  data-options="region:'north'" style="height:50px;width:100%; margin-top: -10px;"><br>
<shiro:hasPermission name="${menuAlias}:function:add">
	&nbsp;<a  href="javascript:saveData();" class="easyui-linkbutton" data-options="iconCls:'icon-fengzhang',disabled:false">封账</a>&nbsp;
</shiro:hasPermission>
&nbsp;<a  href="javascript:batchSealing();" class="easyui-linkbutton" data-options="iconCls:'icon-fengzhang',disabled:false">批量封账</a>
<!-- <a  href="javascript:deleteRow();" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:false">删除</a> -->
<%-- <a  href="<%=basePath%>material/orderbaseInfo/list_WZPDD.action"  --%>
<!-- class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:false">盘点单</a> -->
<!-- <a  href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:true">结存</a> -->
<!-- <a  href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:true">解封</a> -->
<!-- <a  href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-page_white_excel',disabled:true">导出</a> -->
<!-- <a  href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:true">打印</a> -->

</div>
<div data-options="region:'west',title:'库存明细',iconCls:'icon-book',split:true" style="width:45%; height: 93%;">
<div class="easyui-layout" style="width: 100%; height: 100%;"data-options="fit:true">
<div data-options="region:'north',border:false" style="width:100%;height: 88px">
		<form id="searchForm" style="width: 100%;height: 100%"><br>
			&nbsp;切换仓库: <input class="easyui-combobox" id="queryStorage" style="width:150px"/>&nbsp;
			&nbsp;物资编号: <input class="easyui-textbox " name="t1.itemCode" style="width:150px"/><br/><br/>
			&nbsp;物资名称: <input class="easyui-textbox" name="t1.itemName" style="width:150px"/>&nbsp;
			&nbsp;供货公司: <input id="queryCompanyCode" class="easyui-combobox" name="t1.companyCode"style="width:150px"/>&nbsp;&nbsp;
			<button type="button" id="btnSearch" class="easyui-linkbutton" iconCls="icon-search">查询</button>
		</form>
</div>    
<div data-options="region:'center'" style="width:100%;height: 85%;border-right:0">
<table id="grid1" style="width:100%" data-options="fit:true,border:false"></table>
</div>
</div>
</div>
<div data-options="region:'center',title:'封账列表',iconCls:'icon-book'" style="width:55%; height: 94%">
<div id="tb">
    <shiro:hasPermission name="${menuAlias}:function:add">
<a   onclick="saveData()" class="easyui-linkbutton" data-options="iconCls:'icon-fengzhang',plain:true">封账</a>
</shiro:hasPermission>
<a  onclick="deleteRow()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
</div>
<table id="grid2" style="width:100%" data-options="fit:true,border:false"></table>
</div>
<div id="win" class="easyui-window" title="请选择仓库" 
data-options="iconCls:'icon-save',modal:true,closed:true,closable:true,collapsible:false,minimizable:false,maximizable:false"
style="width:300px;height:150px"> 
<div style="margin:30px 10px ">
仓库列表:<input id="storage" class="easyui-combobox" name="deptId"/>
</div>
</div>
<div id="pdd" class="easyui-window" title="盘点单"
data-options="modal:true,closed:true,closable:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false" 
style="width: 500px;height: 300px">
	      <table class="honry-table" style="width: 100%;height: 100%;">
	            <tr>
	            <td>盘点流水号：</td><td><form id="orderForm"><input class="easyui-textbox" id="pdName" name="t.memo"></form></td>
	            </tr>
	          <tr><td colspan="2">
	           <input id="saveName" type="button" value="确定">
	           <input id="delName" type="button"  value="取消">
	           </td></tr>
	           
	      </table>
	      
	 </div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
var isEditingRowIndex=0;//记录当前的行索引
var arrIds=[];//记录已添加的条目Id
var i=0;//用于控制仓库窗口的弹出时机
$(function(){
	$('#queryStorage').combobox({//仓库
		url : "<c:url value='/material/orderDepartment/getStorage.action'/>",
		valueField : 'deptCode',
		textField : 'deptName',
		onSelect: function(rec){
			var url='<%=basePath%>material/orderStockdetail/getData.action?t1.storeNum=0&t1.storageCode='+rec.deptCode;
			$('#grid1').datagrid('load', url);
		}
	});
	$('#queryCompanyCode').combobox({//供货公司
		url : "<c:url value='/material/orderCompany/companyList.action'/>",
		valueField : 'companyCode',
		textField : 'companyName',
		filter : function(q,row){
			var keys = new Array();
			keys[keys.length] = "companyCode";
			keys[keys.length] = "companyName";
			return filterLocalCombobox(q, row, keys);
		}
	});
	if(typeof(listParam)=='undefined'){
		listParam="";
	}
	var href="";//库存明细数据的加载路径
	$('#grid1').datagrid({
		onBeforeLoad:function(param){
			if(i==0){
			$('#win').window('open');
			}
			i++;
			$('#storage').combobox({
				url : "<c:url value='/material/orderDepartment/getStorage.action'/>",
				valueField : 'deptCode',
				textField : 'deptName',
				onSelect:function(rec){
					var ul='<%=basePath%>material/orderStockdetail/getData.action?t1.storeNum=0&t1.storageCode='+rec.deptCode;
					$('#grid1').datagrid('load', ul);
					$('#win').window('close');
				}
			});
			href='<%=basePath%>material/orderStockdetail/getData.action?t1.storeNum=0&'+listParam;
		},
		url:href,
		pageSize:20,
		pageList:[20,40,60,100],
		columns:[[
		          {field:'batchNo',title:'批号',width:'15%'},
		          {field:'itemCode',title:'物品编号',width:'15%'},
		          {field:'itemName',title:'物品名称',width:'20%'},
		          {field:'storeNum',title:'库存数量',width:'9%'},
		          {field:'packUnit',title:'包装单位',width:'10%'},
		          {field:'packQty',title:'包装数量',width:'9%'},
		          {field:'minUnit',title:'最小单位',width:'8%'},
		          {field:'inPrice',title:'购入价格',width:'8%'},
		          {field:'specs',title:'规格',width:'8%'},
		          {field:'storeCost',title:'库存金额',hidden:true},
		          {field:'storageCode',title:'仓库编码',hidden:true},
		          {field:'factoryCode',title:'生产厂家',hidden:true},
		          {field:'companyCode',title:'供货公司',hidden:true},
		          {field:'salePrice',title:'零售价格',hidden:true},
		          {field:'placeCode',title:'货位号',hidden:true},
		          {field:'inNum',title:'入库数量',hidden:true},
		          {field:'inNo',title:'入库流水号',hidden:true},
		          {field:'stockCode',title:'库存流水号',hidden:true},
		          {field:'stockNo',title:'库存序号',hidden:true},
		          {field:'id',title:'ID',hidden:true}
		          ]],
		singleSelect:true,
		pagination:true,
		onDblClickRow:function(rowIndex, rowData){//双击赋值
			var flag=true;
			var row = $('#grid1').datagrid('getSelected');
			if (row) {
				//判断物资是否处于封账状态
				var stockCode=row.stockCode;//库存流水号
				var storageCode=row.storageCode;//仓库编码
				$.ajax({
					url:'<%=basePath%>material/orderDetail/getCheckDetail.action?t1.stockCode='+stockCode+'&t1.storageCode='+storageCode,
					type:'post',
					async: false,
					success: function(value){
						if(!value.status){
							$.messager.alert('提示',value.message);
							flag=false;
						}
					}
				});
				if(!flag){
					return;
				}
				//判断记录是否已经添加到右侧列表中
				var rs=$('#grid2').datagrid('getRows');
				var tag=row.id;
				if(rs.length>0){
					var i=arrIds.indexOf(tag);
					if(i!=-1){
						$.messager.alert("提示", "列表中已有此条记录！","warning");
						$('#grid2').datagrid('uncheckAll');
						$('#grid2').datagrid('checkRow',i);
						return;
					}
				}
				//添加一行并赋值
				$('#grid2').datagrid('appendRow', {
					stockCode:row.stockCode,
					stockNo:row.stockNo,
					itemCode:row.itemCode,
					itemName : row.itemName,
					storageCode:row.storageCode,
					inPrice : row.inPrice,
					salePrice:row.salePrice,
					companyCode : row.companyCode,
					factoryCode:row.factoryCode,
					fstoreNum:row.storeNum,
					inNum:row.inNum,
					packUnit : row.packUnit,
					packQty:row.packQty,
					specs : row.specs,
					minUnit:row.minUnit,
					placeCode:row.placeCode,
					id:row.id
				});
				arrIds.push(tag);
			}
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
	$('#grid2').datagrid({
		columns:[[
		          {field:'-',title:'全选',width:'1%',checkbox:true},
		          {field:'stockCode',title:'库存流水号',width:'8%',align:'right'},
		          {field:'itemCode',title:'物品编号',width:'8%'},
		          {field:'itemName',title:'物品名称',width:'15%'},
		          {field:'packUnit',title:'包装单位',width:'8%'},
		          {field:'packQty',title:'包装数量',width:'8%',align:'right'},
		          {field:'minUnit',title:'最小单位',width:'8%'},
		          {field:'inPrice',title:'购入价格',width:'8%',align:'right'},
		          {field:'salePrice',title:'零售价格',width:'8%',align:'right'},
		          {field:'specs',title:'规格',width:'8%'},
		          {field:'fstoreNum',title:'封账库存数量',width:'10%',align:'right'},
		          {field:'inNum',title:'原始购入数量',width:'10%',align:'right'},
		          {field:'storageCode',title:'仓库编码',hidden:true},
		          {field:'factoryCode',title:'生产厂家',hidden:true},
		          {field:'companyCode',title:'供货公司',hidden:true},
		          {field:'placeCode',title:'货位号',hidden:true},
		          {field:'stockNo',title:'库存序号',hidden:true},
		          {field:'id',title:'ID',hidden:true}
		          ]],
		toolbar:'#tb'
	});
	
	//条件 查询
	$('#btnSearch').bind('click',function(){
		var formData=getFormData('searchForm');
		$('#grid1').datagrid('load',formData);		
	});
});

/**
 * 提交数据
 */
function saveData(){
	//结束编辑框
	$('#grid2').datagrid('endEdit',isEditingRowIndex);
	//提取表格数据
	var rows= $('#grid2').datagrid('getRows');
	if(rows.length==0){
		$.messager.alert("提示", "请选择要封账的数据", "info");
		return;
	}
	$.messager.confirm('提示','确定要封账吗?',function(res){
		if(res){
			$.messager.progress({text:'保存中，请稍后...',modal:true});
			$('#pdd').window('open');
			var retVal="";
			var date=new Date();
			var year = date.getFullYear();
			var month = date.getMonth()+1;       //获取当前月份(0-11,0代表1月)
			var ndate=date.getDate();
			var hours=date.getHours();       //获取当前小时数(0-23)
			var minut=date.getMinutes();     //获取当前分钟数(0-59)
			retVal += year+''+month+''+ndate+''+hours+''+minut;
			$("#pdName").textbox("setValue",retVal);
			$('#delName').click(function(){
				$('#pdd').window('close');
				$.messager.progress('close');
			});
		}
	})
}
	$('#saveName').click(function(){
	//提取表单数据 
	var rows= $('#grid2').datagrid('getRows');
	var formdata= getFormData('orderForm');	
	//向formdata 追加属性json, 值为datagrid的数据转换的json字符串.
	formdata['json']=JSON.stringify(rows);
	$('#orderForm').form('clear');
	$('#pdd').window('close');
	$.ajax({
		url:'<%=basePath%>material/orderDetail/addCheck.action',
		data:formdata,
		type:'post',
		dataType:'json',
		success:function(value){
			$.messager.progress('close');
			$.messager.alert('提示',value.message);
			//弹出提示成功的信息,1秒之后自动关闭提示框 
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			if(value.status){
				//清除表格数据
				$('#grid2').datagrid('loadData',{total:0,rows:[]});	
				$('#grid1').datagrid('reload');
				arrIds=[];
			}
		}			
	});
});
/**
 * 移除行
 */
 function deleteRow(){
	 var rows=$('#grid2').datagrid('getChecked');
		if(rows.length==0){
			$.messager.alert("提示", "请选择要删除的行！", "info"); 
			return;
		}else{
			for (var i = 0; i < rows.length; i++) {
			var index=$('#grid2').datagrid("getRowIndex",rows[i]);
			if(arrIds.indexOf(rows[i].id)!=-1){
				arrIds.remove(rows[i].id);
			}
			//结束编辑框
		 	$('#grid2').datagrid('endEdit',isEditingRowIndex);
		 	//删除行
		 	$('#grid2').datagrid('deleteRow',index);
		 	//获取表格数据
		 	var data=$('#grid2').datagrid('getData');
		 	//重新将数据赋给表格
		 	$('#grid2').datagrid('loadData',data);
		}
		}
 }

/**
 * 批量封账
 */
function batchSealing(){
	var row = $('#grid1').datagrid('getSelected');
	if(row){
		//判断物资是否处于封账状态
		var stockCode=row.stockCode;//库存流水号
		var storageCode=row.storageCode;//仓库编码
		var itemCode=row.itemCode;//物资编码
		$.ajax({
			url:'<%=basePath%>material/orderDetail/getCheckDetail.action?t1.stockCode='+stockCode+'&t1.storageCode='+storageCode,
			type:'post',
			async: false,
			success: function(value){
				if(!value.status){
					$.messager.alert('提示',value.message);
				}else{
				var url='<%=basePath%>material/orderStockdetail/getData.action?t1.storeNum=0&t1.itemCode='+itemCode+'&t1.storageCode='+storageCode;
					//$('#grid1').datagrid('reload',url);
					$('#grid1').datagrid({
						url:url,
						onLoadSuccess:function(data){
							var rows=$('#grid1').datagrid('getRows');
							for (var i = 0; i < rows.length; i++) {
								//判断记录是否已经添加到右侧列表中
								var rs=$('#grid2').datagrid('getRows');
								var tag=rows[i].id;
								if(rs.length>0){
									var j=arrIds.indexOf(tag);
									if(j!=-1){
										continue;
									}
								}
								$('#grid2').datagrid('appendRow', {
									stockCode:rows[i].stockCode,
									stockNo:rows[i].stockNo,
									itemCode:rows[i].itemCode,
									itemName : rows[i].itemName,
									storageCode:rows[i].storageCode,
									inPrice : rows[i].inPrice,
									salePrice:rows[i].salePrice,
									companyCode : rows[i].companyCode,
									factoryCode:rows[i].factoryCode,
									fstoreNum:rows[i].storeNum,
									inNum:rows[i].inNum,
									packUnit : rows[i].packUnit,
									packQty:rows[i].packQty,
									specs : rows[i].specs,
									minUnit:rows[i].minUnit,
									placeCode:rows[i].placeCode,
									id:rows[i].id
								});
								arrIds.push(rows[i].id);
							}
						}
					});
				}
			}
		});
	}else{
		$.messager.alert("提示", "请选择一种物资！", "info"); 
		return;
	}
}

function conveterParamsToJson(paramsAndValues) {  
    var jsonObj = {};  
  
    var param = paramsAndValues.split("&");  
    for ( var i = 0; param != null && i < param.length; i++) {  
        var para = param[i].split("=");  
        jsonObj[para[0]] = para[1];  
    }  
  
    return jsonObj;  
}  
 
/**
 * 将表单数据封装为json
 * @param form
 * @returns
 */
function getFormData(form) {  
    var formValues = $("#" + form).serialize();  
  
    //关于jquery的serialize方法转换空格为+号的解决方法  
    formValues = formValues.replace(/\+/g," ");   // g表示对整个字符串中符合条件的都进行替换  
    var temp =  decodeURIComponent(JSON.stringify(conveterParamsToJson(formValues)));  
    var queryParam = JSON.parse(temp);  
    return queryParam;  
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</html>