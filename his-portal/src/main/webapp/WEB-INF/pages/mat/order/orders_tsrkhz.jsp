<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<title>特殊入库核准</title>
</head>
<body style="margin: 0px; padding: 0px;">
<div class="easyui-layout" style="width: 100%; height: 100%;"data-options="fit:true">
<div data-options="region:'north'" style="width: 100%; height:50px;padding-top: 10px;border-top:0">
		<form id="orderForm" style="padding: 5px 20px;">
          操作类型：<input class="easyui-combobox" id="queryName" value="特殊入库核准" style="width:150px">&nbsp;&nbsp;&nbsp;&nbsp;
</form>	
	</div>
    <div  data-options="region:'west',title:'已审核列表',iconCls:'icon-book',split:true" style="width:600px ;">
			<div class="easyui-layout" style="width: 100%; height: 100%;"
				data-options="fit:true">
				<div data-options="region:'north',border:false" style="width: 100%; height: 60px">
					<form id="searchForm" style="width: 100%; height: 100%"><br>
				  		 &nbsp;物资编号: <input class="easyui-textbox " name="t2.itemCode" style="width:150px"/>&nbsp;
						物资名称: <input class="easyui-textbox" name="t2.itemName" style="width:150px"/>&nbsp;
						<button type="button" id="btnSearch" class="easyui-linkbutton" iconCls="icon-search">查询</button>
					</form>
				</div>
				<div data-options="region:'center'" style="width: 100%; height: 85%; border-right: 0">
					<table id="grid1" style="width: 100%" data-options="fit:true,border:false"></table>
				</div>
			</div>
		</div>   
    <div data-options="region:'center',title:'核准入库'" style="background:#eee;">
    <div id="tb">
    <shiro:hasPermission name="${menuAlias}:function:add">
<a   onclick="saveData()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">提交</a>
</shiro:hasPermission>
<a  onclick="deleteRow()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
</div>
    	<table id="grid2" style="width:100%" data-options="fit:true,border:false"></table>
    </div>   
		</div>
</body>
<script type="text/javascript">
var isEditingRowIndex=0;//记录正在编辑的行索引
var arrIds=[];//记录已添加的条目Id
$(function(){
	$('#queryName').combobox({
		valueField:'id',
		textField:'text',
		data:[{
			id:'WZTSRK',
			text:'特殊入库申请'
		},{
			id:'TSRKHZ',
			text:'特殊入库核准'
		}],
		onSelect:function(record){
			window.location.href='<%=basePath%>material/orderbaseInfo/list_'+record.id+'.action?menuAlias='+record.id
		}
	});
	if(typeof(listParam)=='undefined'){
		listParam="";
	}
	$('#grid1').datagrid({
		url:'<%=basePath%>material/orderInput/specialData.action?t2.inState=1'+listParam,
		pageSize:20,
		pageList:[20,40,60,100],
		columns:[[
		          {field:'inListCode',title:'入库单号',width:'10%'},
		          {field:'inSerialNo',title:'单内序号',width:'10%'},
		          {field:'itemName',title:'物品名称',width:'13%'},
		          {field:'inPrice',title:'入库价格',width:'10%'},
		          {field:'inNum',title:'申请数量',width:'10%'},
		          {field:'packUnit',title:'单位',width:'10%'},
		          {field:'specs',title:'规格',width:'10%'},
		          {field:'invoiceNo',title:'发票号码',width:'10%'},
		          {field:'invoiceDate',title:'发票日期',width:'10%'},
		          {field:'storageCode',title:'仓库编码',width:'10%'},
		          {field:'batchNo',title:'批号',hidden:true},
		          {field:'inCost',title:'金额',hidden:true},
		          {field:'validDate',title:'有效期',hidden:true},
		          {field:'highvalueBarcode',title:'高耗材条形码',hidden:true},
		          {field:'companyName',title:'供货公司',hidden:true},
		          {field:'applyNo',title:'申请单号',hidden:true},
		          {field:'id',title:'ID',hidden:true},
		          {field:'memo',title:'备注',hidden:true}
		          
		          ]],
		singleSelect:true,
		pagination:true,
		onDblClickRow:function(rowIndex, rowData){//双击赋值
			var row = $('#grid1').datagrid('getSelected');
			if (row) {
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
					inListCode:row.inListCode,
					inSerialNo:row.inSerialNo,
					itemName : row.itemName  ,
					inPrice : row.inPrice,
					companyName : row.companyName,
					inNum:row.inNum,
					packUnit : row.packUnit,
					specs : row.specs,
					inCost : row.inCost,
					batchNo : row.batchNo,
					validDate:row.validDate,
					highvalueBarcode:row.highvalueBarcode,
					invoiceDate:row.invoiceDate,
					invoiceNo:row.invoiceNo,
					meom:row.memo,
					id:row.id
				});
				arrIds.push(tag);
				$('#grid2').datagrid('endEdit',isEditingRowIndex);//关闭上次编辑的行
				isEditingRowIndex=$('#grid2').datagrid('getRows').length - 1;//获取最后一行的索引
				$('#grid2').datagrid('beginEdit',isEditingRowIndex);//设置最后一行为编辑状态
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
		          {field:'-',title:'-',width:'2%',checkbox:true},
		          {field:'inListCode',title:'入库单号',width:'7%'},
		          {field:'inSerialNo',title:'单内序号',width:'6%'},
		          {field:'itemName',title:'物品名称',width:'7%'},
		          {field:'inPrice',title:'入库价格',width:'5%'},
		          {field:'inNum',title:'入库数量',width:'4%'},
		          {field:'packUnit',title:'单位',width:'4%'},
		          {field:'specs',title:'规格',width:'4%'},
		          {field:'inCost',title:'金额',width:'4%'},
		          {field:'batchNo',title:'批号',width:'7%'},
		          {field:'validDate',title:'有效期',width:'7%'},
		          {field:'invoiceNo',title:'发票号码',width:'7%'},
		          {field:'invoiceDate',title:'发票日期',width:'10%'},
		          {field:'companyName',title:'供货公司',width:'13%'},
		          {field:'memo',title:'备注',width:'6%',editor:{type:'textbox',options:{}}},
		          {field:'highvalueBarcode',title:'高耗材条形码',width:'7%'},
		          {field:'id',title:'ID',hidden:true},
		          ]],
		toolbar:'#tb',
		onClickRow:function(rowIndex, rowData){
				//上次编辑的行索引
				$('#grid2').datagrid('endEdit',isEditingRowIndex);
				//得到当前点击的行索引					
				isEditingRowIndex= rowIndex;								
				//开启编辑行
				$('#grid2').datagrid('beginEdit',isEditingRowIndex);
			}
		
	});
	//条件 查询
	$('#btnSearch').bind('click',function(){
		var formData=getFormData('searchForm');
		$('#grid1').datagrid('load',formData);		
	});
})
/**
 * 提交数据
 */
function saveData(){
	//结束编辑框
	$('#grid2').datagrid('endEdit',isEditingRowIndex);
	//提取表单数据
	var formdata= getFormData('orderForm');	
	//提取表格数据
	var rows= $('#grid2').datagrid('getRows');
	if(rows.length==0){
		$.messager.alert("提示", "没有数据,无法提交", "info");
		return;
	}
	$.messager.confirm('提示','确定要提交吗?',function(res){
		if(res){
			$.messager.progress({text:'保存中，请稍后...',modal:true});
			//向formdata 追加属性json, 值为datagrid的数据转换的json字符串.
			formdata['json']=JSON.stringify(rows);	
			$.ajax({
				url:'<%=basePath%>material/orderInput/approveInput.action',
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
		}
	});
}

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