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
<style type="text/css">
	.tableCss{
		border-collapse: collapse;
		border-spacing: 0;
		border: 1px solid #95b8e7;
		width:100%;
	}
	.tableCss td{
		border: 1px solid #95b8e7;
		padding: 5px 15px;
		word-break: keep-all;
		white-space:nowrap;
	}
	.tableCss .TDlabel{
		text-align: right;
		width: 10%;
	}
	.tableCss .TDinput {
	    width: 15%;
    }
</style>
<script type="text/javascript">
	//发票类型map
	var invoiceTypeMap="";
	$(function(){
		//发票类型下拉框
		$('#invoiceType').combobox({    
		    url:'<%=basePath%>finance/invoiceStorage/queryInvoiceType.action',
		    valueField:'encode',    
		    textField:'name',
		    multiple:false,
		    onSelect:function(data){
		    	$('#invoiceCode').textbox('setValue',data.encode);
		    	$('#invoicePinyin').textbox('setValue',data.pinyin);
		    	$('#invoiceWb').textbox('setValue',data.wb);
		    	$('#invoiceInputcode').textbox('setValue',data.inputCode);
		    	if(data.name=='门诊'){
			    	$('#invoiceStartnos').html('A');
		    		$('#invoiceEndnoe').html('A');

		    	}else if(data.name=='住院'){
		    		$('#invoiceStartnos').html('B');
		    		$('#invoiceEndnoe').html('B');
		    	}else if(data.name=='账户'){
		    		$('#invoiceStartnos').html('C');
		    		$('#invoiceEndnoe').html('C');

		    	}else if(data.name=='收费'){
		    		$('#invoiceStartnos').html('D');
		    		$('#invoiceEndnoe').html('D');

		    	}else if(data.name=='预交收据'){
		    		$('#invoiceStartnos').html('E');
		    		$('#invoiceEndnoe').html('E');
		    	}
		    		

		    }
		});
		//查询发票类型map
		$.ajax({
			url:'<%=basePath%>finance/invoiceStorage/queryInvoiceTypeAJAX.action',
			success:function(data){
				invoiceTypeMap=data;
			}
		});
		//使用状态下拉框
		$('#invoiceUseState').combobox({
			valueField: 'code',
			textField: 'text',
			data: [{
				code: '0',
				text: '在用',
				"selected":true  
			},{
				code: '1',
				text: '停用'
			}]
		});
		$('#table2').datagrid({
			url:'<%=basePath%>finance/invoiceStorage/queryInvoiceStorage.action',
			onDblClickRow:function(index, row){
				var invoiceStartno=(row.invoiceStartno).substr(0, 1);
				var invoiceEndno = (row.invoiceEndno).substr(0, 1);
				//发票类型
				$('#invoiceType').combobox('setValue',row.invoiceCode);
				//发票类型编码
				$('#invoiceCode').textbox('setValue',row.invoiceCode);
				//在用状态
				$('#invoiceUseState').combobox('setValue',row.invoiceUseState);
				//拼音码
				$('#invoicePinyin').textbox('setValue',row.invoicePinyin);
				//五笔码
				$('#invoiceWb').textbox('setValue',row.invoiceWb);
				//自定义码
				$('#invoiceInputcode').textbox('setValue',row.invoiceInputcode);
				if($('#invoiceStartnos').html()!=''){
					$('#invoiceStartnos').html('')
				}
				if($('#invoiceStartnos').html()!=''){
					$('#invoiceEndnoe').html('')
				}
				$('#invoiceStartnos').html(invoiceStartno);
	    		$('#invoiceEndnoe').html(invoiceEndno);
				//发票开始号
				$('#invoiceStartno').textbox('setValue',(row.invoiceStartno).substr(1,14));
				//发票终止号
				$('#invoiceEndno').textbox('setValue',(row.invoiceEndno).substr(1,14));
				//备注
				$('#invoiceRemark').textbox('setValue',row.invoiceRemark);
				//主键Id
				$('#id').val(row.id);
				$('#invoiceUsedno').val(row.invoiceUsedno);
			},onLoadSuccess : function(data){
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
	});
	//查询
	function searchForm(){
		var  invoiceType=$('#invoiceType').combobox('getValue');//发票类型
		var  invoiceEndno=$('#invoiceEndno').textbox('getValue');
		var  invoiceStartno=$('#invoiceStartno').textbox('getValue');//发票号
		var invoiceUseState=$('#invoiceUseState').combobox('getValue');//状态invoiceUseState
		var invoiceStartnos=$('#invoiceStartnos').html();
		$('#table2').datagrid('load',{
			invoiceType:invoiceType,
			invoiceEndno:invoiceEndno,
			invoiceStartno:invoiceStartno,
			invoiceUseState:invoiceUseState,
			invoiceStartnos:invoiceStartnos
		});
	};
	// 保存（修改）发票入库记录
	function save(){
		var  a=$('#invoiceStartno').textbox('getValue');
		var  b=$('#invoiceEndno').textbox('getValue');
		var  c=$('#invoiceCode').textbox('getValue');
		var  d=$('#invoiceType').combobox('getValue');
		var invoiceStartnos=$('#invoiceStartnos').html();
		var aa=invoiceStartnos+a;
		var bb=invoiceStartnos+b;
		if(a==''||b==''||c==''||d==''){
			$.messager.alert('提示','请将必填信息填写完整，再进行保存或修改操作');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		if(aa.length!==14){
			$.messager.alert('提示','发票开始号必须是14位');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		if(bb.length!==14){
			$.messager.alert('提示','发票结束号必须是14位');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		var startCom =aa.substring(1,14);
		var endCom=bb.substring(1,14);
		if(parseInt(startCom)>parseInt(endCom)){
			$.messager.alert('提示','发票开始号必须小于发票结束号');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		if(parseInt(startCom)==0){
			$.messager.alert('提示','发票号不能为0');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		$.messager.progress({text:'保存中，请稍后...',modal:true});
		$('#form1').form('submit', {    
		    url:'<%=basePath%>finance/invoiceStorage/saveform.action?invoiceStartnos='+invoiceStartnos,    
		    success:function(data){  
		    	if(data=='success'){
		    		$.messager.progress('close');
		    		$.messager.alert('提示','保存成功');
		    		setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
		    		$('#table2').datagrid({
		    			url:'<%=basePath%>finance/invoiceStorage/queryInvoiceStorage.action'
		    		});
		    		$('#invoiceStartnos').html('');
		    		$('#invoiceEndnoe').html('');
		    		$('#form1').form('clear');
		    	}else if(data=='update'){
		    		$.messager.progress('close');
		    		$.messager.alert('提示','修改成功');
		    		setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
		    		$('#table2').datagrid({
		    			url:'<%=basePath%>finance/invoiceStorage/queryInvoiceStorage.action'
		    		});
		    		$('#invoiceStartnos').html('');
		    		$('#invoiceEndnoe').html('');
		    		$('#form1').form('clear');
		    	}else if(data=='used'){
		    		$.messager.progress('close');
		    		$.messager.alert('提示','发票已经领取请重新输入');
		    		setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
		    		
		    	}else{
		    		$.messager.progress('close');
		    		$.messager.alert('提示','保存失败');
		    	}
		    	$('#invoiceUseState').combobox('setValue','0');
		    },
		    error:function(){
		    	$.messager.progress('close');
		    	$.messager.alert('提示','保存失败');
		    }
		});  
	}
	//渲染发票类型
	function invoiceType(value,row,index){
		if(value!=null&&value!=''){
			return invoiceTypeMap[value];
		}
	}
	//渲染使用状态
	function statue(value,row,index){
		if(value==0){
			return '在用';
		}else if(value==1){
			return '停用';
		}
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false" style="width: 100%;height: 150px;padding:5px 5px 5px 5px;">
	
		<shiro:hasPermission name="${menuAlias}:function:save">
			<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="save()" data-options="iconCls:'icon-save'" style="margin:0px 0px 0px 1px" >保&nbsp;存&nbsp;</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="save()" data-options="iconCls:'icon-edit'" style="margin:0px 0px 0px 15px" >修&nbsp;改&nbsp;</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:query">
			<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="searchForm()" data-options="iconCls:'icon-search'" style="margin:0px 0px 0px 15px" >查&nbsp;询&nbsp;</a>
		</shiro:hasPermission>
		<form id="form1"  method="post">
			<input id="id" name="financeInvoiceStorage.id" type="hidden"/>
			<table class="tableCss" style="margin-top: 7px">
				<tr>
					<td class="TDlabel">发票类型：</td>
					<td class="TDinput">
						<input class="easyui-combobox" id="invoiceType" name="financeInvoiceStorage.invoiceType" data-options="required:true"/>
					</td>
					<td class="TDlabel">发票类型编码：</td>
					<td class="TDinput">
						<input class="easyui-textbox" readonly="readonly" id="invoiceCode" name="financeInvoiceStorage.invoiceCode" data-options="required:true"/>
					</td>
					<td class="TDlabel">在用状态：</td>
					<td class="TDinput">
						<input class="easyui-combobox"  id="invoiceUseState" name="financeInvoiceStorage.invoiceUseState"/>
					</td>
				</tr>
				<tr>
					<td class="TDlabel">拼音码：</td>
					<td class="TDinput">
						<input class="easyui-textbox" readonly="readonly" id="invoicePinyin" name="financeInvoiceStorage.invoicePinyin"/>
					</td>
					<td class="TDlabel">五笔码：</td>
					<td class="TDinput">
						<input class="easyui-textbox" readonly="readonly" id="invoiceWb" name="financeInvoiceStorage.invoiceWb"/>
					</td>
					<td class="TDlabel">自定义码：</td>
					<td class="TDinput">
						<input class="easyui-textbox" readonly="readonly" id="invoiceInputcode" name="financeInvoiceStorage.invoiceInputcode"/>
					</td>
				</tr>
				<tr>
					<input id="invoiceUsedno" name="financeInvoiceStorage.invoiceUsedno" type="hidden"/>
					<td class="TDlabel">发票开始号：<span id="invoiceStartnos"/></td>
					<td class="TDinput">
						<input class="easyui-textbox" id="invoiceStartno" name="financeInvoiceStorage.invoiceStartno" data-options="required:true"/>
					</td>
					<td class="TDlabel">发票终止号：<span id="invoiceEndnoe"/></td>
					<td class="TDinput">
						<input class="easyui-textbox" id="invoiceEndno" name="financeInvoiceStorage.invoiceEndno" data-options="required:true"/>
					</td>
					<td class="TDlabel">备注：</td>
					<td class="TDinput">
						<input class="easyui-textbox" id="invoiceRemark" name="financeInvoiceStorage.invoiceRemark"/>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',title:'发票入库记录'" style="width:100%;">
		<table id="table2" class="easyui-datagrid" data-options="fit:true,border:false,rownumbers:true,pagination:true,pageSize:20,pageList:[20,30,50,80,100],singleSelect:true">
			<thead>
				<tr>
					<th data-options="field:'id',hidden:true">
					<th data-options="field:'invoiceCode',width:'8%',align:'center',formatter:invoiceType">发票类型</th>					 
					<th data-options="field:'invoiceStartno',width:'14%',align:'center'">发票开始号</th> 
					<th data-options="field:'invoiceEndno',width:'14%',align:'center'">发票终止号</th> 
					<th data-options="field:'invoiceUsedno',width:'14%',align:'center'">发票已用号</th>
					<th data-options="field:'invoiceUseState',align:'center',width:'8%',formatter:statue">在用状态</th>
					<th data-options="field:'invoicePinyin',width:'8%',align:'center'">拼音码</th>
					<th data-options="field:'invoiceWb',width:'8%',align:'center'">五笔码</th>
					<th data-options="field:'invoiceInputcode',width:'8%',align:'center'">自定义码</th>
					<th data-options="field:'invoiceRemark',width:'15%',align:'center'">备注</th>
					 
				</tr>			
			</thead>
		</table>
	</div>
</body>
</html>