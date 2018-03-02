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
<title>特殊物资入库</title>
<style type="text/css">
.numberbox .textbox-text {
text-align: right;
}
</style>
</head>
	<body style="margin: 0px; padding: 0px;">
<div class="easyui-layout" style="width: 100%; height: 100%;"data-options="fit:true">
	<div data-options="region:'north'" style="width: 100%; height:50px;padding-top: 10px;border-top:0">
		<form id="orderForm" style="padding: 5px 20px;">
          操作类型：<input class="easyui-combobox" id="queryName" value="特殊入库申请" style="width:150px">&nbsp;&nbsp;
         选择仓库:<input class="easyui-combobox" id="queryStorage" name="deptId" >
</form>	
	</div>
		<div
			data-options="region:'west',title:'物资列表',iconCls:'icon-book',split:true"
			style="width: 580px;">
			<div class="easyui-layout" style="width: 100%; height: 100%;"
				data-options="fit:true">
				<div data-options="region:'north',border:false" style="width: 100%; height: 90px">
					<form id="searchForm" style="width: 100%; height: 100%"><br>
						  &nbsp;物资编号: <input class="easyui-textbox " name="t1.itemCode" style="width:150px"/>&nbsp;
						    物资名称: <input class="easyui-textbox" name="t1.itemName" style="width:150px"/><br/><br/> 
						  &nbsp;供货公司: <input id="queryCompanyCode" class="easyui-combobox" name="t1.companyCode" style="width:150px"/>&nbsp;&nbsp;
						<button type="button" id="btnSearch" class="easyui-linkbutton" iconCls="icon-search">查询</button>
					</form>
				</div>
				<div data-options="region:'center'" style="width: 100%; height: 82%; border-right:0">
					<table id="grid1" style="width: 100%" data-options="fit:true,border:false"></table>
				</div>
			</div>
		</div>
		<div data-options="region:'center',title:'入库申请'" style="background:#eee;width:70%;">
    <div id="tb">
    <shiro:hasPermission name="${menuAlias}:function:add">
<a   onclick="saveData()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">提交</a>
</shiro:hasPermission>
<a  onclick="deleteRow()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
</div>
<div style="width:100%;height:90%">
<table id="grid2" style="width:100%" data-options="fit:true,border:false"></table>
</div>
<div style="width:100%;height:61px">
合计：<span id="sum">0.00</span>
</div>
    </div>   
		</div>
</body>
<script type="text/javascript">
var isEditingRowIndex=0;//记录正在编辑的行索引
var arrIds=[];//记录已添加的条目Id
$(function(){
	$('#queryName').combobox({
		valueField:'id',
		textField:'value',
		data:[{
			id:'WZTSRK',
			value:'特殊入库申请'
		},{
			id:'TSRKHZ',
			value:'特殊入库核准'
		}],
		onSelect:function(record){
			window.location.href='<%=basePath%>material/orderbaseInfo/list_'+record.id+'.action?menuAlias='+record.id
		}
	});
	$('#queryStorage').combobox({
		url : "<c:url value='/material/orderDepartment/getStorage.action'/>",
		valueField : 'deptCode',
		textField : 'deptName',
		onLoadSuccess:function(){
			var data = $('#queryStorage').combobox('getData');
			if (data.length > 0) {
                $("#queryStorage").combobox('select', data[0].deptCode);
            }
		},
		required: true,
		missingMessage:'请选择目标科室'
	});
	$('#queryCompanyCode').combobox({
		url : "<c:url value='/material/orderCompany/companyList.action'/>",
		valueField : 'companyCode',
		textField : 'companyName',
		mode:'remote',
		onSelect:function(rec){
			var url='<%=basePath%>material/orderbaseInfo/listByPage.action?t1.companyCode='+rec.companyCode;
			$('#grid1').datagrid('reload', url);
		},
		onChange:function(){
			var url='<%=basePath%>material/orderbaseInfo/listByPage.action?';
			$('#grid1').datagrid('reload', url);
		}
	});
	if(typeof(listParam)=='undefined'){
		listParam="";
	}
	$('#grid1').datagrid({//URL中,orderbaseInfo是显示特殊入库物资列表的.
		url:'<%=basePath%>material/orderbaseInfo/listByPage.action'+listParam, 
		pageSize:20,
		pageList:[20,40,60,100],
		columns:[[
		          {field:'itemCode',title:'物品编号',width:'20%'},
		          {field:'itemName',title:'物品名称',width:'30%'},
		          {field:'specs',title:'物品规格',width:'13%'},
		          {field:'minUnit',title:'最小单位',width:'13%'},
		          {field:'packUnit',title:'包装单位',width:'12%'},
		          {field:'packQty',title:'包装数量',width:'15%'},
		          {field:'companyCode',title:'供货公司',hidden:true},
		          {field:'kindCode',title:'科目编码',hidden:true},
		          {field:'packPrice',title:'包装价格',hidden:true},
		          {field:'financeFlag',title:'收费标志',hidden:true},
		          {field:'validFlag',title:'停用标志',hidden:true},
		          {field:'inPrice',title:'入库单价',hidden:true},
		          {field:'storageCode',title:'仓库代码',hidden:true},
		          {field:'id',title:'ID',hidden:true}
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
					itemCode : row.itemCode,
					itemName : row.itemName  ,
					specs : row.specs,
					minUnit : row.minUnit,
					packUnit : row.packUnit,
					inPrice : row.inPrice,
					inCost : row.inCost,
					companyCode : row.companyCode,
					factoryCode : row.factoryCode,
					salePrice:row.salePrice,
					packQty:row.packQty,
					packPrice:row.packPrice,
					kindCode:row.kindCode,
					storageCode:row.storageCode,
					inNum:1,
					validState:1,
					inCost:row.inPrice,
					inClass3mean:3,
					id:row.id
				});
				arrIds.push(tag);
				$('#grid2').datagrid('endEdit',isEditingRowIndex);//关闭上次编辑的行
				isEditingRowIndex=$('#grid2').datagrid('getRows').length - 1;//获取最后一行的索引
				$('#grid2').datagrid('beginEdit',isEditingRowIndex);//设置最后一行为编辑状态
				bindGridEvent();//绑定键盘输入事件
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
	$.extend($.fn.validatebox.defaults.rules, {    
	    minValue: {    
	        validator: function(value){
	        	return value>0;
	        },    
	        message: '请输入一个大于0的数'   
	    }    
	});  

	$('#grid2').datagrid({
		columns:[[
		          {field:'-',title:'-',width:'2%',checkbox:true},
		          {field:'inState',title:'核准',width:'5%',align:'center',
		        	  editor:{
		        		  type : 'combobox',
						  options:{
								required:true,
								missingMessage:'请选择是否核准',
								editable:false,
								valueField:'id',
								textField:'text',
								data:[{"id":2,"text":"是"},{"id":1,"text":"否"}],//1-正式入库,2-核准入库
								onSelect:function(r){
									var inEdt= $('#grid2').datagrid('getEditor',{index:isEditingRowIndex,field:'inState'});
									$(inEdt.target).combobox('setValue',r.text);
								}
						  }
		          }},
		          {field:'itemCode',title:'物品编码',width:'7%'},
		          {field:'itemName',title:'物品名称',width:'6%'},
		          {field:'minUnit',title:'最小单位',width:'5%'},
		          {field:'specs',title:'物品规格',width:'6%'},
		          {field:'packQty',title:'大包装数量',width:'6%',align:'right'},
		          {field:'inNum',title:'入库数量(包装数)',width:'9%',
		        	  editor:{
		        		  type:'numberbox',
		        		  options:{
		        			  required:true,
		        			  validType:'minValue',
		        			  missingMessage:'申请数量必须填写',
		        			  invalidMessage:'申请数量须大于0'
		        			  }
		          }},
		          {field:'packUnit',title:'大包装单位',width:'6%'},
		          {field:'inPrice',title:'入库价格',width:'6%',
		        	  editor:{
		        		  type:'numberbox',
		        		  options:{
		        			  precision:2,
		        			  disabled:true
		        			      }
		              }
		          },
		          {field:'inCost',title:'申请金额',width:'6%',editor:{type:'numberbox',options:{precision:2,disabled:true}}},
		          {field:'salePrice',title:'零售价格',width:'6%',align:'right'},
		          {field:'produceDate',title:'生产日期',width:112,formatter:function(value,row,index){
		        	  return "<input  class=\"Wdate\" onClick=\"WdatePicker({dateFmt:'yyyy-MM-dd',readOnly: true})\" style=\"width:104px\"/>";
		          	}
		          },
		          {field:'validDate',title:'有效期',width:112,formatter:function(value,row,index){
		        	  return "<input  class=\"Wdate\" onClick=\"WdatePicker({dateFmt:'yyyy-MM-dd',readOnly: true})\" style=\"width:104px\"/>";
		          	}
		          },
		          {field:'invoiceNo',title:'发票号码',width:'6%',editor:{type:'textbox'}},
		          {field:'invoiceDate',title:'发票日期',width:111,formatter:function(value,row,index){
		        	  return "<input  class=\"Wdate\" onClick=\"WdatePicker({dateFmt:'yyyy-MM-dd',readOnly: true})\" style=\"width:103px\" />";
		          	}
		          },
		          {field:'companyCode',title:'供货公司编码',hidden:true},
		          {field:'inClass3mean',title:'系统定义类型',hidden:true},
		          {field:'validState',title:'有效性状态',hidden:true},
		          {field:'kindCode',title:'物品科目编码',hidden:true},
		          {field:'id',title:'ID',hidden:true}
		          ]],
		 toolbar:'#tb',
		 onClickRow:function(rowIndex, rowData){
				//上次编辑的行索引
				$('#grid2').datagrid('endEdit',isEditingRowIndex);
				//得到当前点击的行索引					
				isEditingRowIndex= rowIndex;								
				//开启编辑行
				$('#grid2').datagrid('beginEdit',isEditingRowIndex);
				//绑定数量编辑框的键盘输入事件
				bindGridEvent();
				
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
	$('#grid2').datagrid('endEdit',isEditingRowIndex);
	//提取表单数据
	var formdata= getFormData('orderForm');		
	//提取表格数据
	var rows= $('#grid2').datagrid('getRows');	
	if(rows.length==0){
		$.messager.alert("提示", "没有数据,无法提交", "info");
		return;
	}
	for (var i = 0; i < rows.length; i++) {
		var f= $('#grid2').datagrid('validateRow',i);
		if(!f){
			$.messager.alert("提示", "包含非法数据,请重新填写！", "error");
			$('#grid2').datagrid('highlightRow',i);
			return;
		}else{
			if(rows[i].inState=='是'){
			rows[i].inState=2;
			}else{rows[i].inState=1;}
		}
	}
	$.messager.confirm('提示','确定要提交数据吗?',function(res){
		if(res){
			$.messager.progress({text:'保存中，请稍后...',modal:true});
		//向formdata 追加属性json, 值为datagrid的数据转换的json字符串.
		formdata['json']=JSON.stringify(rows);	
		$.ajax({
			url:'<%=basePath%>material/orderInput/specialApply.action',
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
					//清除合计数
					$('#sum').html('0.00');
					arrIds=[];
				}
			}			
		});
		}
	});
}
/**
 * 绑定键盘输入事件
 */
function bindGridEvent(){
	var numEdt= $('#grid2').datagrid('getEditor',{index:isEditingRowIndex,field:'inNum'});
	$(numEdt.target).numberbox('textbox').bind('keyup', function(event) {
		cal();
		sum();
	});
	$(numEdt.target).numberbox('textbox').blur(function(){
		cal();
		sum();
	});
	var priceEdt=$('#grid2').datagrid('getEditor',{index:isEditingRowIndex,field:'inPrice'});
	$(priceEdt.target).numberbox('textbox').bind('keyup',function(event){
		cal();
		sum();
	});
}


/**
 * 计算金额
 */
 function cal(){
	    //获取数量的编辑框
		var numEdt= $('#grid2').datagrid('getEditor',{index:isEditingRowIndex,field:'inNum'});
		//得到数量编辑框里的值
		var num=$(numEdt.target).numberbox('getText');
		if(num==null){
			num=0;
		}
		//获取价格的编辑框
		var priceEdt=$('#grid2').datagrid('getEditor',{index:isEditingRowIndex,field:'inPrice'});
		//得到价格编辑框里的值
		var price=$(priceEdt.target).numberbox('getText');	
		//计算金额
		var money=(price*num).toFixed(2);
		//获取金额的编辑框
		var moneyEdt=$('#grid2').datagrid('getEditor',{index:isEditingRowIndex,field:'inCost'});
		//对金额编辑框赋值
		$(moneyEdt.target).numberbox('setValue',money);
		//给金额的单元格赋值
		$('#grid2').datagrid('getRows')[isEditingRowIndex].inCost=money;
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
			 	sum();//重新计算合计
			}
		}
 }
 /**
  * 求合计数
  */
 function sum(){
 	//获取所有行对象
 	var rows=$('#grid2').datagrid('getRows');
 	var money=0;//合计数
 	for(var i=0;i<rows.length;i++)
 	{		
 		money+=parseFloat(rows[i].inCost);
 	}
 	$('#sum').html(money.toFixed(2));
 	
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