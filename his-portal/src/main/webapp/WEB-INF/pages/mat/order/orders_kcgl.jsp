<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<title>物资库存管理</title>
<style type="text/css">
.orders_kcglLeft .panel-header{
	border-top:0;
	border-right:0;
}
.orders_kcglLeft .panel-body{
	border-right:0
}
.order_kcList .panel-header,.order_kcList .panel-body{
	margin-left:1px;
}
.orders_kcgl .panel-header,.orders_kcgl .panel-body{
	border-left:0;
}
.datagrid-wrap{
	border-top:0;
	border-left:0;
	border-right:0;
}
</style>
</head>
<body style="margin: 0px; padding: 0px;">
	<div class="easyui-layout orders_kcglLeft" style="width: 100%; height: 100%"data-options="fit:true">
		<div class="easyui-layout orders_kcgl" data-options="region:'west',title:'查询',iconCls:'icon-search'"
			style="width: 15%; height: 100%">
			<div class="easyui-layout" style="width: 100%; height: 100%;"data-options="fit:true">
			<div data-options="region:'north'" style="width: 100%; height:40%;border-top:0;"><br>
				&nbsp;选择仓库:<input class="easyui-combobox" id="queryStorage" name="deptId"/><br/>
			<form  id="searchForm" style="margin:20px 0 0;">
		    	&nbsp;物资编号:<input class="easyui-textbox " name="t1.itemCode" style="width:150px"/><br/><br/>
				&nbsp;物资名称:<input class="easyui-textbox"  name="t1.itemName" style="width:150px"/><br/><br/>
				&nbsp;供货公司:<input id="queryCompanyCode" class="easyui-combobox"  name="t1.companyCode" style="width:150px"/><br/><br/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<button type="button" id="btnSearch" class="easyui-linkbutton" iconCls="icon-search">查询</button>
	    	</form>
    	
			</div>
			<div data-options="region:'center',title:'分类查询'" style="width:100%; height: 50%;border-right:0">
			<form id="tree1">
			</form>
			</div>
			</div>
			</div>
		<div class="easyui-layout order_kcList" data-options="region:'center',fit:true,border:false"
			style="width: 85%; height: 100%;">
			<div data-options="region:'center',title:'库存列表',iconCls:'icon-book'" style="height:60%;">
			<div id="tb">
    <shiro:hasPermission name="${menuAlias}:function:save">
<a   onclick="saveData()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">保存</a>
</shiro:hasPermission>
</div>
			<table id="grid1" style="width:100%" data-options="fit:true"></table>
			</div>
			<div data-options="region:'south',title:'库存明细',iconCls:'icon-book'" style="height:52%;">
			<table id="grid2" style="width:100%" data-options="fit:true,border:false"></table>
			</div>

		</div>
	</div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
 var isEditingRowIndex=0;//记录正在编辑的行索引
$(function(){
	$('#queryStorage').combobox({//仓库
		url : "<c:url value='/material/orderDepartment/getStorage.action'/>",
		valueField : 'deptCode',
		textField : 'deptName',
		required: true,
		missingMessage:'请选择仓库',
		onLoadSuccess:function(){
// 			var data = $('#queryStorage').combobox('getData');
// 			if (data.length > 0) {
//                 $("#queryStorage").combobox('select', data[0].deptCode);
//             }
			var data = $('#queryStorage').combobox('getData');
			if (data.length > 0) {
				for (var i = 0; i < data.length; i++) {
					if(data[i].deptCode=="${deptCode}"){
		                $("#queryStorage").combobox('select', data[i].deptCode);
		                break;
					}else{
						$("#queryStorage").combobox('select', data[0].deptCode);
					}
      		    }	
     		 }		
		},
		onSelect: function(rec){
			var url='<%=basePath%>material/order/matStockInfo/matData.action?t1.validState=1&deptId='+rec.deptCode;
			$('#grid1').datagrid('reload', url);
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
	$('#tree1').tree({
		url:"<%=basePath%>material/orderKindInfo/queryTree.action?t1.validFlag=1",
		method : 'get',
		animate : true,
		lines : true,
		formatter : function(node) {//统计节点总数
			var s = node.text;
			if (node.children) {
				s += '&nbsp;<span style=\'color:blue\'>('
						+ node.children.length + ')</span>';
			}
			return s;
		},onDblClick:function(node){
			var children= node.children;
			if(children==''){//叶子节点
			//var itemKind= node.attributes.kindCode;
			var id= node.id;
			var url='<%=basePath%>material/order/matStockInfo/matData.action?t1.validState=1&t1.itemKind='+id;
			$('#grid1').datagrid('reload',url);
			}
		},onBeforeLoad : function(node, param){
	    	if(node!=null&&node!=''){
	    		return false;
	    	}
	    }
	});
	
	if(typeof(listParam)=='undefined'){
		listParam="";
	}
	
	$.extend($.fn.validatebox.defaults.rules, {    
	    minValue: {    
	        validator: function(value){
	        	return value>=0;
	        },    
	        message: '请输入一个大于0的数'   
	    }    
	});
	$('#grid1').datagrid({
		url:'<%=basePath%>material/order/matStockInfo/matData.action?t1.validState=1'+listParam,
		columns:[[
		          {field:'itemCode',title:'物品编号',width:'6%'},
		          {field:'itemName',title:'物品名称',width:'10%'},
		          {field:'storeSum',title:'库存数量',width:'8%',formatter:function(value,row,index){
		        	  if(row.showFlag==0){
		        		  return row.packQty*(row.storeSum-row.preoutSum);
		        	  }else{
		        		  return (row.storeSum-row.preoutSum);
		        	  }
		          }},
		          {field:'specs',title:'单位',width:'6%',formatter:function(value,row,index){
		        	  if(row.showFlag==0){
		        		  return row.minUnit;
		        	  }else{
		        		  return row.packUnit;
		        	  }
		          }},
		          {field:'topNum',title:'上限数量',width:'6%',
		        	  editor:{
	        		  type:'numberbox',
	        		  options:{
	        			  validType:'minValue',
	        			  invalidMessage:'上限数量不能为负'
	        			  }}
		          },
		          {field:'lowNum',title:'下限数量',width:'6%',
		        	  editor:{
		        	  type:'numberbox',
	        		  options:{
	        			  validType:'minValue',
	        			  invalidMessage:'下限数量不能为负'
	        			  }}
		          },
		          {field:'placeCode',title:'货位号',width:'8%',editor:{type:'textbox'}},
		          {field:'mark',title:'备注',width:'8%',editor:{type:'textbox'}},
		          {field:'minUnit',title:'最小单位',width:'8%'},
		          {field:'packQty',title:'包装数量',width:'8%'},
		          {field:'packUnit',title:'包装单位',width:'8%'},
		          {field:'retailPrice',title:'零售价格',width:'8%'},
		          {field:'itemKind',title:'科目编码',width:'10%'},
		          {field:'companyCode',title:'供货公司',hidden:true},
		          {field:'showFlag',title:'显示的单位',hidden:true},
		          {field:'validState',title:'有效状态',hidden:true},
		          {field:'itemDeptCode',title:'仓库代码',hidden:true},
		          {field:'id',title:'ID',hidden:true}
		          ]],
		toolbar:'#tb',
		singleSelect:true,
		pagination:true,
		onDblClickRow:function(rowIndex, rowData){
			var row = $('#grid1').datagrid('getSelected');
			if(row){
				$('#grid1').datagrid('endEdit',isEditingRowIndex);//关闭上次编辑的行
				isEditingRowIndex=rowIndex;//获取当前行的索引
				$('#grid1').datagrid('beginEdit',rowIndex);//设置当前行为编辑状态
				var itemCode=row.itemCode;//物品编码
				var storageCode=row.itemDeptCode;//仓库编码
				$('#grid2').datagrid({
					url:'<%=basePath%>material/orderStockdetail/getData.action?t1.itemCode='+itemCode+'&t1.storageCode='+storageCode,
					columns:[[
					          {field:'batchNo',title:'批号',width:'8%'},
					          {field:'itemCode',title:'物品编号',width:'8%'},
					          {field:'itemName',title:'物品名称',width:'8%'},
					          {field:'storeNum',title:'库存数量',width:'8%'},
					          {field:'storeCost',title:'库存金额',width:'8%'},
					          {field:'packUnit',title:'包装单位',width:'8%'},
					          {field:'packQty',title:'包装数量',width:'8%'},
					          {field:'minUnit',title:'最小单位',width:'8%'},
					          {field:'inPrice',title:'购入价格',width:'8%'},
					          {field:'specs',title:'规格',width:'8%'},
					          {field:'storageCode',title:'仓库编码',width:'10%'},
					          {field:'kindCode',title:'科目编码',hidden:true},
					          {field:'companyCode',title:'供货公司',hidden:true},
					          {field:'salePrice',title:'零售价格',hidden:true},
					          {field:'inNo',title:'入库流水号',hidden:true},
					          {field:'id',title:'ID',hidden:true}
					          ]],
					singleSelect:true,
					pagination:true		
				});
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
	$('#grid1').datagrid('endEdit',isEditingRowIndex);
	//提取表单数据
	var formdata= getFormData('orderForm');	
	//提取表格数据
	var rows= $('#grid1').datagrid('getRows');
	if(rows.length==0){
		$.messager.alert("提示", "没有数据,无法提交", "info");
		return;
	}
	for (var i = 0; i < rows.length; i++) {
		var f= $('#grid1').datagrid('validateRow',i);
		if(!f){
			$.messager.alert("提示", "数量不能为负数！", "error");
			$('#grid1').datagrid('highlightRow',i);
			return;
		}else{
			if(rows[i].lowNum==''){
				rows[i].lowNum=0;
			}
			if(rows[i].topNum==''){
				rows[i].topNum=0;
			}
			if(rows[i].lowNum>rows[i].topNum){
				$.messager.alert("提示", "下限数量不能大于上限数量！", "error");
				$('#grid1').datagrid('uncheckAll');
				$('#grid1').datagrid('checkRow',i);
				return;
			}
		}
	}
	$.messager.confirm('提示','确定要提交数据吗?',function(res){
		if(res){
			$.messager.progress({text:'保存中，请稍后...',modal:true});
			//向formdata 追加属性json, 值为datagrid的数据转换的json字符串.
			formdata['json']=JSON.stringify(rows);	
			$.ajax({
				url:'<%=basePath%>material/order/matStockInfo/updateData.action',
				data:formdata,
				type:'post',
				dataType:'json',
				success:function(value){
					$.messager.progress('close');
					$.messager.alert('提示',value.message);
						$('#grid1').datagrid('reload');
						//弹出提示成功的信息,1秒之后自动关闭提示框 
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					if(value.status){
						//清除表格数据
						$('#grid2').datagrid('loadData',{total:0,rows:[]});	
						$('#grid1').datagrid('loadData',{total:0,rows:[]});
					}
				}			
			});
		}
	})
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