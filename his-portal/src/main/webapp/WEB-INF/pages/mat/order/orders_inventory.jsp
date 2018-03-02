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
<title>物资盘点单</title>
</head>
<body style="margin: 0px; padding: 0px;" class="easyui-layout" data-options="fit:true">
<div  data-options="region:'north'" style="height:50px;padding-left: 10px; margin-top: -10px;"><br>
<%-- <a  href="<%=basePath%>material/orderbaseInfo/list_WZFZPD.action" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:false">封账</a> --%>
<%-- <a  href="" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:false">批量封账</a> --%>
<a  href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-pandian',disabled:false">盘点单</a>
<shiro:hasPermission name="${menuAlias}:function:add">
<a  href="javascript:saveData();" class="easyui-linkbutton" data-options="iconCls:'icon-jiecun',disabled:false">结存</a>
</shiro:hasPermission>
<shiro:hasPermission name="${menuAlias}:function:save">
<a  href="javascript:cancleData();" class="easyui-linkbutton" data-options="iconCls:'icon-fengzhang',disabled:false">解封</a>
</shiro:hasPermission>
<a  href="javascript:deleteRows();" class="easyui-linkbutton" data-options="iconCls:'icon-remove',disabled:false">删除</a>
<shiro:hasPermission name="${menuAlias}:function:export">
<a  href="javascript:derive();" class="easyui-linkbutton" data-options="iconCls:'icon-down',disabled:false">导出</a>
</shiro:hasPermission>
<a  href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-2012081511202',disabled:false">打印</a>

</div>
<div data-options="region:'west',title:'盘点单',iconCls:'icon-book',split:true" style="width:15%; height: 93%;">
<!-- <div style="height:40%">盘点单列表:<form id="tree1"></form></div> -->
<!-- <div style="height:40%">未解封列表:<form id="tree2"></form></div> -->
&nbsp;<div style="height:40%">盘点单列表:<input class="easyui-combobox" id="queryList1"></div>
      <div style="height:40%">未解封列表:<input class="easyui-combobox" id="queryList2"></div>

</div>
<div data-options="region:'center',title:'盘点明细',iconCls:'icon-book'" style="width:85%; height: 94%">
<div  id="div2" style="display: none;height:100%;width:100%"><table id="grid2" style="height:60%; width:100%" data-options="fit:true,border:false"></table></div>
<div  id="div1" style="display: none;height:100%;width:100%"><table id="grid1" style="height:60%; width:100%" data-options="fit:true,border:false"></table></div>
</div>
</body>
<script type="text/javascript">
var isEditingRowIndex=0;//记录当前的行索引
var m=1;
$(function(){
	$('#queryList1').combobox({
		url : "<c:url value='/material/orderHead/queryList.action?t1.checkState=0'/>",
		valueField : 'checkCode',
		textField : 'checkName',
		mode:'remote',
		onSelect: function(rec){
			$('#div2').hide();
			$('#div1').show();
			m=1;
			var url='<%=basePath%>material/orderDetail/matData.action?t1.checkCode='+rec.checkCode;
			$('#grid1').datagrid('reload', url);
		}
	});
	$('#queryList2').combobox({
		url : "<c:url value='/material/orderHead/queryList.action?t1.checkState=1'/>",
		valueField : 'checkCode',
		textField : 'checkName',
		mode:'remote',
		onSelect: function(rec){
			$('#div2').show();
			$('#div1').hide();
			m=2;
			var url='<%=basePath%>material/orderDetail/listByPage.action?t1.checkState=1&t1.checkCode='+rec.checkCode;
			$('#grid2').datagrid('reload', url);
		}
	});
	$('#tree1').tree({//盘点单列表树
		url:"<%=basePath%>material/orderHead/queryTree.action?t1.checkState=0",
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
			$('#div2').hide();
			$('#div1').show();
			m=1;
			var children= node.children;
			if(children==''){//叶子节点
			var id= node.id;
			var url='<%=basePath%>material/orderDetail/matData.action?t1.checkCode='+id;
			$('#grid1').datagrid('reload',url);
			}
		}
	});
	
	$('#tree2').tree({//未解封列表树
		url:"<%=basePath%>material/orderHead/queryTree.action?t1.checkState=1",
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
			$('#div2').show();
			$('#div1').hide();
			m=2;
			var children= node.children;
			if(children==''){//叶子节点
			var id= node.id;
			var url='<%=basePath%>mat/order/matcheckDetail/listByPage.action?t1.checkState=1&t1.checkCode='+id;
			$('#grid2').datagrid('reload',url);
			}
		}
	});
	
	$.extend($.fn.validatebox.defaults.rules, {    
	    minValue: {    
	        validator: function(value){
	        	return value>=0;
	        },    
	        message: '请输入一个非负数'   
	    }    
	});
	$('#grid1').datagrid({//结存数据
		columns:[[
		          {field:'-',title:'全选',width:'1%',checkbox:true},
		          {field:'checkCode',title:'盘点单号',width:80 },
		          {field:'checkdetailCode',title:'明细流水号',width:80},
		          {field:'itemCode',title:'物品编号',width:80 },
		          {field:'itemName',title:'物品名称',width:80 },
		          {field:'fstoreNum',title:'封账库存数量',width:80},
		          {field:'adjustNum',title:'实际盘存数量',width:80,
		        	  editor:{
		        		  type:'numberbox',
		        		  options:{
		        			  required:true,
		        			  precision:2,
		        			  validType:'minValue',
		        			  missingMessage:'实际盘存数量必须填写',
		        			  invalidMessage:'实际盘存数量不能为负'
		        			  }
		          }},
		          {field:'cstoreNum',title:'结存库存数量',width:80,editor:{type:'numberbox',options:{precision:2,disabled:true}}},
		          {field:'profitLossNum',title:'盈亏数量',width:80,
		        	  editor:{type:'numberbox',
		        		  options:{
		        			  precision:2,
		        			  disabled:true
		        			  }
		              }
		          },
		          {field:'inNum',title:'原始购入数量',width:'6%'},
		          {field:'packUnit',title:'包装单位',width:'5%'},
		          {field:'packQty',title:'包装数量',width:'5%'},
		          {field:'minUnit',title:'最小单位',width:'5%'},
		          {field:'inPrice',title:'购入价格',width:'5%'},
		          {field:'salePrice',title:'零售价格',width:'5%'},
		          {field:'stockCode',title:'库存流水号',width:'5%'},
		          {field:'stockNo',title:'库存序号',width:'5%'},
		          {field:'specs',title:'规格',width:'5%'},
		          {field:'storageCode',title:'仓库编码',hidden:true},
		          {field:'factoryCode',title:'生产厂家',hidden:true},
		          {field:'companyCode',title:'供货公司',hidden:true},
		          {field:'profitFlag',title:'盈亏标记',hidden:true},
		          {field:'placeCode',title:'货位号',hidden:true},
		          {field:'id',title:'ID',hidden:true}
		          ]],
		pagination:true,
		fitColumns:true,
		rowStyler:function(index,row){
			if(row.profitLossNum>0){
				return 'color:blue;';
			}
			if(row.profitLossNum<0){
				return 'color:red;';
			}
		},
	   onClickRow:function(rowIndex, rowData){
					//上次编辑的行索引
					$('#grid1').datagrid('endEdit',isEditingRowIndex);
					//得到当前点击的行索引					
					isEditingRowIndex= rowIndex;								
					//开启编辑行
					$('#grid1').datagrid('beginEdit',isEditingRowIndex);
					//绑定数量编辑框的键盘输入事件
					bindGridEvent();
				}
	});
	$('#grid2').datagrid({//未解封数据
		columns:[[
		          {field:'-',title:'全选',width:'1%',checkbox:true},
		          {field:'checkCode',title:'盘点单号',width:'5%'},
		          {field:'checkdetailCode',title:'明细流水号',width:'5%'},
		          {field:'itemCode',title:'物品编号',width:'8%'},
		          {field:'itemName',title:'物品名称',width:'8%'},
		          {field:'fstoreNum',title:'封账库存数量',width:'6%'},
		          {field:'adjustNum',title:'实际盘存数量',width:'6%'},
		          {field:'cstoreNum',title:'结存库存数量',width:'6%',editor:{type:'numberbox',options:{precision:2,disabled:true}}},
		          {field:'profitLossNum',title:'盈亏数量',width:'6%',
		              styler: function(value){
		  				if (value < 0){
		  					return 'color:red;';
		  				}
		  				if(value>0){
		  					return 'color:blue;';
		  				}
		  			}
		          },
		          {field:'inNum',title:'原始购入数量',width:'6%'},
		          {field:'packUnit',title:'包装单位',width:'5%'},
		          {field:'packQty',title:'包装数量',width:'5%'},
		          {field:'minUnit',title:'最小单位',width:'5%'},
		          {field:'inPrice',title:'购入价格',width:'5%'},
		          {field:'salePrice',title:'零售价格',width:'5%'},
		          {field:'stockCode',title:'库存流水号',width:'5%'},
		          {field:'stockNo',title:'库存序号',width:'5%'},
		          {field:'specs',title:'规格',width:'5%'},
		          {field:'storageCode',title:'仓库编码',hidden:true},
		          {field:'factoryCode',title:'生产厂家',hidden:true},
		          {field:'companyCode',title:'供货公司',hidden:true},
		          {field:'profitFlag',title:'盈亏标记',hidden:true},
		          {field:'placeCode',title:'货位号',hidden:true},
		          {field:'id',title:'ID',hidden:true}
		          ]],
		pagination:true,
		fitColumns:true,
	});
});

/**
 * 提交数据(解封)
 */
function cancleData(){
	if(m==1){
		$.messager.alert("提示", "请选择未解封列表数据", "info");
		return;
	}
	//提取表单数据
	var formdata= getFormData('orderForm');	
	//提取表格数据
	var rows= $('#grid2').datagrid('getRows');
	if(rows.length==0){
		$.messager.alert("提示", "没有数据,无法解封", "info");
		return;
	}
	$.messager.confirm('提示','确定要解封吗？',function(res){
		if(res){
			$.messager.progress({text:'保存中，请稍后...',modal:true});
			//向formdata 追加属性json, 值为datagrid的数据转换的json字符串.
			formdata['json']=JSON.stringify(rows);	
			$.ajax({
				url:'<%=basePath%>material/orderDetail/cancles.action',
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
						$('#queryList2').combobox('reload');
						$('#queryList2').combobox('clear');
						
					}
				}			
			});
		}
	})
}
/**
 * 提交数据(结存)
 */
function saveData(){
	if(m==2){
		$.messager.alert("提示", "请选择盘点列表数据", "info");
		return;
	}
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
			$.messager.alert("提示", "请填写必要数据！", "error");
			$('#grid1').datagrid('highlightRow',i);
			return;
		}
	}
	$.messager.confirm('提示','结存后将更新库存，是否确认进行结存？',function(res){
		if(res){
			$.messager.progress({text:'保存中，请稍后...',modal:true});
			//向formdata 追加属性json, 值为datagrid的数据转换的json字符串.
			formdata['json']=JSON.stringify(rows);	
			$.ajax({
				url:'<%=basePath%>material/orderDetail/inventory.action',
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
						$('#grid1').datagrid('loadData',{total:0,rows:[]});	
						$('#queryList1').combobox('reload');
						$('#queryList2').combobox('reload');
						$('#queryList1').combobox('clear');
					}
				}			
			});
		}
	})
}

/**
 * 绑定键盘输入事件
 */
function bindGridEvent(){
	var csnumEdt= $('#grid1').datagrid('getEditor',{index:isEditingRowIndex,field:'cstoreNum'});
	$(csnumEdt.target).numberbox('textbox').bind('keyup', function(event) {
		cal();	
	});
	$(csnumEdt.target).numberbox('textbox').blur(function(){
		cal();	
	});
	var adnumEdt=$('#grid1').datagrid('getEditor',{index:isEditingRowIndex,field:'adjustNum'});
	$(adnumEdt.target).numberbox('textbox').bind('keyup',function(event){
		cal();	
	});
}


/**
 * 计算盈亏数量
 */
 function cal(){
	    //获取结存数量的编辑框
		var csnumEdt= $('#grid1').datagrid('getEditor',{index:isEditingRowIndex,field:'cstoreNum'});
		//得到结存数量编辑框里的值
		var cstoreNum=$(csnumEdt.target).numberbox('getText');	
		//获取实际盘存的编辑框
		var adnumEdt=$('#grid1').datagrid('getEditor',{index:isEditingRowIndex,field:'adjustNum'});
		//得到实际盘存编辑框里的值
		var adjustNum=$(adnumEdt.target).numberbox('getText');	
		//计算盈亏数量
		var pronum=adjustNum-cstoreNum;
		//获取盈亏数量的编辑框
		var pronumEdt=$('#grid1').datagrid('getEditor',{index:isEditingRowIndex,field:'profitLossNum'});
		//对盈亏数量编辑框赋值
		$(pronumEdt.target).numberbox('setValue',pronum);
		//给盈亏数量的单元格赋值
		$('#grid1').datagrid('getRows')[isEditingRowIndex].profitLossNum=pronum;
}

/**
 * 移除行
 */
 function deleteRows(){
	 var rows=$('#grid'+m).datagrid('getChecked');
		if(rows.length==0){
			$.messager.alert("提示", "请选择要删除的行！", "info"); 
			return;
		}else{
			for (var i = 0; i < rows.length; i++) {
			var index=$('#grid'+m).datagrid("getRowIndex",rows[i]);
			//结束编辑框
		 	$('#grid'+m).datagrid('endEdit',isEditingRowIndex);
		 	//删除行
		 	$('#grid'+m).datagrid('deleteRow',index);
		 	//获取表格数据
		 	var data=$('#grid'+m).datagrid('getData');
		 	//重新将数据赋给表格
		 	$('#grid'+m).datagrid('loadData',data);
		}
		}
 	
 }
 
 //导出
 function derive(){
	 if(m==1){
		 $.messager.alert("提示", "请选择已结存(未解封)列表数据", "info");
			return;
	 }
	//提取表单数据
	 var formdata= getFormData('orderForm');
	 var rows = $('#grid2').datagrid("getRows");
	 if(rows.length==0){
		 $.messager.alert("提示", "请选择要导出的行！", "info"); 
			return;
	 }else{
		 $.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否删除
			if (res) {
				//向formdata 追加属性json, 值为datagrid的数据转换的json字符串.
				formdata['json']=JSON.stringify(rows);
				window.open('<%=basePath%>material/orderDetail/exportExcel.action?json='+formdata['json']);
			}
		}); 
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