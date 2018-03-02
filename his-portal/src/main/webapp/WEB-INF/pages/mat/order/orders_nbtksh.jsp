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
<title>内部退库审核</title>
</head>
<body style="margin: 0px; padding: 0px;">
<div class="easyui-layout"style="width: 100%; height: 100%;"data-options="fit:true">
<div data-options="region:'north',border:false" style="width: 100%; height:50px;padding-top: 10px;">
		<form id="orderForm" style="padding: 5px 20px;">
			          操作类型：<input class="easyui-combobox" id="queryName" value="内部退库审核" style="width:150px">&nbsp;&nbsp;
			          选择科室(仓库)：<input class="easyui-combobox" id="queryStorage" name="deptId" style="width:150px">
         				 <input type="hidden" name="t.inState" value="0">
</form>	
	</div>
		<div
			data-options="region:'west',title:'申请列表',iconCls:'icon-book',split:true"
			style="width: 520px;">
			<div class="easyui-layout" style="width: 100%; height: 100%;"
				data-options="fit:true">
				<div data-options="region:'north',border:false" style="width: 100%; height: 90px">
					<form id="searchForm" style="width: 100%; height: 100%"><br>
				  		物资编号：<input class="easyui-textbox" name="t1.itemCode" style="width:150px"/>&nbsp;&nbsp; 
						物资名称：<input class="easyui-textbox" name="t1.itemName" style="width:150px"/><br /><br> 
			      		供货公司：<input id="queryCompanyCode" class="easyui-combobox" name="t1.companyCode" style="width:150px"/>&nbsp;&nbsp;
						<button type="button" id="btnSearch" class="easyui-linkbutton"
							iconCls="icon-search">查询</button>
					</form>
				</div>
				<div data-options="region:'center'" style="width: 100%; height: 85%; border-right: 0;">
					<table id="grid1" style="width: 100%" data-options="fit:true,border:false"></table>
				</div>
			</div>
		</div>
		<div data-options="region:'center',title:'审核列表'" style="background:#eee;">
    <div id="tb">
    <shiro:hasPermission name="${menuAlias}:function:add">
<a   onclick="saveData()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">审核</a>
</shiro:hasPermission>
<a  onclick="deleteRow()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
</div>
    	<table id="grid2" style="width:100%" data-options="fit:true,border:false"></table>
    </div>   
		</div>
</body>
<script type="text/javascript">
var isEditingRowIndex=0;//记录正在编辑的行索引
var arrIds=[];//记录已添加条目Id
$(function(){
	$('#queryName').combobox({
		valueField:'id',
		textField:'text',
		data:[{
			id:'WZNBRK',
			text:'内部入库申请'
		},{
			id:'NBRKSH',
			text:'内部入库审核'
		},{
			id:'WZNBTK',
			text:'内部退库申请'
		},{
			id:'NBRKTKSH',
			text:'内部退库审核'
		},{
			id:'NBRKTKHZ',
			text:'内部退库核准'
		}],
		onSelect:function(record){
			window.location.href='<%=basePath%>material/orderbaseInfo/list_'+record.id+'.action?menuAlias='+record.id
		}
	});
	
	$('#queryStorage').combobox({
		url : "<c:url value='/material/orderInoutdept/deptList.action?t1.stockClass=1'/>",
		valueField : 'objectDeptCode',
		textField : 'objectDeptName',
		mode:'remote',
		onSelect: function(rec){
			var url='<%=basePath%>material/orderInput/checkData.action?t1.stop_flg=0&deptId='+rec.objectDeptCode;
			$('#grid1').datagrid('reload', url);
		}
	});
	$('#queryCompanyCode').combobox({
		url : "<c:url value='/material/orderCompany/companyList.action'/>",
		valueField : 'companyCode',
		textField : 'companyName',
		mode:'remote'
	});
	if(typeof(listParam)=='undefined'){
		listParam="";
	}
	$('#grid1').datagrid({
		url:'<%=basePath%>material/orderInput/checkData.action?t1.inState=0'+listParam,
		pageSize:20,
		pageList:[20,40,60,100],
		columns:[[
		          {field:'inListCode',title:'退库单号',width:'10%'},
		          {field:'inSerialNo',title:'单内序号',width:'10%'},
		          {field:'itemCode',title:'物品编码',width:'13%'},
		          {field:'itemName',title:'物品名称',width:'10%'},
		          {field:'inPrice',title:'入库价格',width:'10%'},
		          {field:'inNum',title:'退库数量',width:'10%',formatter:function(value,row,index){return -1*value;}},
		          {field:'packUnit',title:'单位',width:'10%'},
		          {field:'specs',title:'规格',width:'10%'},
		          {field:'inCost',title:'金额',width:'10%',formatter:function(value,row,index){return -1*value;}},
		          {field:'companyName',title:'供货公司',width:'10%'},
		          {field:'storageCode',title:'仓库编码',hidden:true},
		          {field:'batchNo',title:'批号',hidden:true},
		          {field:'validDate',title:'有效期',hidden:true},
		          {field:'highvalueBarcode',title:'高耗材条形码',hidden:true},
		          {field:'applyNo',title:'申请单号',hidden:true},
		          {field:'id',title:'ID',hidden:true}
		          
		          ]],
		singleSelect:true,
		pagination:true,
		onDblClickRow:function(rowIndex, rowData){//双击赋值
			var row = $('#grid1').datagrid('getSelected');
			if (row) {
				//判断记录是否已经添加到审批列表中
				var rs=$('#grid2').datagrid('getRows');
				var tag=row.id;
				if(rs.length>0){
					var i=arrIds.indexOf(tag);
					if(i!=-1){
						$.messager.alert("提示", "审核列表已有此条记录！","warning");
						$('#grid2').datagrid('uncheckAll');
						$('#grid2').datagrid('checkRow',i);
						return;
					}
				}
				//添加一行并赋值
				$('#grid2').datagrid('appendRow', {
					inSerialNo:row.inSerialNo,
					itemName : row.itemName  ,
					inPrice : row.inPrice,
					companyName : row.companyName,
					inNum:-1*row.inNum,
					packUnit : row.packUnit,
					specs : row.specs,
					inCost : -1*row.inCost,
					applyNo:row.applyNo,
					validDate:row.validDate,
					packInNum:-1*row.inNum,
					id:row.id,
					inListCode:row.inListCode
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
	        	return value>=0;
	        },    
	        message: '请输入一个大于0的数'   
	    }    
	});  
	
	$('#grid2').datagrid({
		columns:[[
		          {field:'-',title:'-',width:'2%',checkbox:true},
		          {field:'inListCode',title:'退库单号',width:'10%'},
		          {field:'itemName',title:'物品名称',width:'10%'},
		          {field:'inPrice',title:'入库价格',width:'10%',editor:{type:'numberbox',options:{precision:2,disabled:true}}},
		          {field:'packInNum',title:'申请数量',width:'10%'},
		          {field:'inNum',title:'审核数量',width:'10%',
		        	  editor:{
		        		  type:'numberbox',
		        		  options:{
		        			  required:true,
		        			  validType:'minValue',
		        			  missingMessage:'请输入审核数',
		        			  invalidMessage:'审核数量不能为负'
		        			  }}
		          },
		          {field:'packUnit',title:'单位',width:'8%'},
		          {field:'inCost',title:'金额',width:'10%',editor:{type:'numberbox',options:{precision:2,disabled:true}}},
		          {field:'specs',title:'规格',width:'8%'},
		          {field:'validDate',title:'有效期',width:'7%'},
		          {field:'companyName',title:'供货公司',width:'15%'},
		          {field:'applyNo',title:'申请单号',hidden:true},
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
				//绑定数量编辑框的键盘输入事件
				bindGridEvent();
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
		$.messager.alert("提示", "您还没有审核数据", "info");
		return;
	}
	for (var i = 0; i < rows.length; i++) {
		var f= $('#grid2').datagrid('validateRow',i);
		if(!f){
			$.messager.alert("提示", "包含非法数据,请重新填写！", "error");
			$('#grid2').datagrid('uncheckAll');
			$('#grid2').datagrid('checkRow',i);
			return;
		}else{
			if(rows[i].inNum>rows[i].packInNum){
				$.messager.alert("提示", "审核数量不能大于申请数量！", "error");
				$('#grid2').datagrid('uncheckAll');
				$('#grid2').datagrid('checkRow',i);
				return;
			}
		}
	}
	$.messager.confirm('提示','确定要审核吗?',function(res){
		if(res){
			$.messager.progress({text:'保存中，请稍后...',modal:true});
			//向formdata 追加属性json, 值为datagrid的数据转换的json字符串.
			formdata['json']=JSON.stringify(rows);	
			$.ajax({
				url:'<%=basePath%>material/orderInput/inputCheck.action',
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
 
/**
 * 绑定键盘输入事件
 */
function bindGridEvent(){
	var numEdt= $('#grid2').datagrid('getEditor',{index:isEditingRowIndex,field:'inNum'});
	$(numEdt.target).numberbox('textbox').bind('keyup', function(event) {
		cal();
	});
	$(numEdt.target).numberbox('textbox').blur(function(){
		cal();
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