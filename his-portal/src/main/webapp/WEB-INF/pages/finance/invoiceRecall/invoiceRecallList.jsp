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
<script type="text/javascript">
	$(function(){
		//回车事件 
		bindEnterEvent('invoiceGetperson',queryGetperson,'easyui');
	})
	var invoiceTypeMap="";
	var empMap="";
	$(function(){
		$('#win').window('close');  // close a window  
		//查询发票类型map
		$.ajax({
			url:'<%=basePath%>finance/invoiceStorage/queryInvoiceTypeAJAX.action',
			success:function(data){
				invoiceTypeMap=data;
			}
		});
		//查询员工map
		$.ajax({
			url:'<%=basePath%>finance/InvoiceRecall/queryEmpMapRecall.action',
			async:false,
			success:function(data){
				empMap=data;
			}
		});
		//查询发票领取记录
		$('#table1').datagrid({
			url:'<%=basePath%>finance/InvoiceRecall/queryInvoiceRecall.action',
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
	});
	function invoiceEmp(value,row,index){
		if(value!=null&&value!=''){
			return empMap[value];
		}
	}
	function invoiceType(value,row,index){
		if(value!=null&&value!=''){
			return invoiceTypeMap[value];
		}
	}
	function invoicestate(value,row,index){
		if(value==0){
			return '未用';
		}else if(value==1){
			return '使用';
		}else if(value=-1){
			return '已用';
		}
	}
	function save(){
		var row=$('#table1').datagrid('getSelected');
		if(row!=null){
			var start=parseInt(row.invoiceUsedno.substring(1));
			var end=parseInt(row.invoiceEndno.substring(1));
			var num=end-start;
			$('#win').window('open');  
			$('#zhaohui').focus();
			$('#shengyu').textbox('setValue',num);
		}else{
			$.messager.alert('提示','请选择要召回的发票记录');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return fasle;
		}
	}
	function queryGetperson(){
		var name = $('#invoiceGetperson').textbox('getValue');
		name=name.replace(/(^\s*)|(\s*$)/g, "");
		$('#table1').datagrid({
			queryParams: {
				name: name,
			}
		});
	}
	function queren(){
		var rows=$('#table1').datagrid('getSelected');
		if($('#zhaohui').val()==''){
			$.messager.alert('提示','请输入召回数量');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		if($('#shengyu').val()==''){
			$.messager.alert('提示','剩余数量为空，不能进行召回操作');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		var num1=parseInt($('#shengyu').val());
		var num2=parseInt($('#zhaohui').val());
		var num=num1-num2;		
		var date=JSON.stringify(rows);
		if(num2>num1){
			$.messager.alert('提示','召回发票数量不得大于剩余发票数量，请重新输入');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			$('#zhaohui').val('');
			return false;
		}else if(num2==0){
			$.messager.alert('提示','请输入有效的召回发票张数');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			$('#zhaohui').val('');
			return false;
		}else if(num2==''){
			$.messager.alert('提示','请输入有效的召回发票张数');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			$('#zhaohui').val('');
			return false;
		}

		$.ajax({
			url:'<%=basePath%>finance/InvoiceRecall/recallInvoice.action',
			data:{date:date,num:num2},
			success:function(data){
				if(data=="success"){
					$.messager.alert("提示","召回成功");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					$('#zhaohui').textbox('clear');
					$('#win').window('close');  // close a window  
					$('#table1').datagrid('reload');					
				}
			}
		});
	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false" style="width: 100%;padding:15px 15px 15px 5px;">
		领取人：<input id="invoiceGetperson" class="easyui-textbox"  data-options="prompt:'输入回车执行查询'" style="width: 200px;" />
		&nbsp;
		<shiro:hasPermission name="${menuAlias}:function:query">
			<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queryGetperson()" data-options="iconCls:'icon-search'" style="margin:0px 0px 0px 10px" >查&nbsp;询&nbsp;</a>
		</shiro:hasPermission>	
		 &nbsp;
		<shiro:hasPermission name="${menuAlias}:function:operation">
		 <a href="javascript:void(0)"  class="easyui-linkbutton" onclick="save()" data-options="iconCls:'icon-recall'" style="margin:0px 0px 0px 15px" >召&nbsp;回&nbsp;</a>
		</shiro:hasPermission>
	</div>
	<div data-options="region:'center',title:'发票领取记录'" style="width: 100%;">
		<table id="table1" class="easyui-datagrid" data-options="fit:true,border:false,rownumbers:true,pagination:true,pageSize:20,pageList:[20,30,50,80,100],singleSelect:true">
			<thead>
				<tr>
					<th data-options="field:'invoiceGetperson',width:'10%',align:'center',formatter:invoiceEmp">领取人</th>
					<th data-options="field:'invoiceType',width:'10%',align:'center',formatter:invoiceType">发票种类</th>
					<th data-options="field:'invoiceStartno',width:'15%',align:'center'">发票开始号</th>
					<th data-options="field:'invoiceEndno',width:'15%',align:'center'">发票终止号</th>
					<th data-options="field:'invoiceUsedno',width:'15%',align:'center'">发票已用号</th>
					<th data-options="field:'invoiceUsestate',width:'10%',align:'center',formatter:invoicestate">使用状态</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="win" class="easyui-window"  style="width:300px;height:200px" title="召回" data-options="modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false,resizable:false">
			<div style="margin:20px 5px 5px 13px;" align="center">
				<table>
					<tr>
						<td>
							剩余发票数量：<input id="shengyu" class="easyui-textbox" readonly="readonly"/>
						</td>
					</tr>
				</table>
				<table style="padding-top: 8px;">
					<tr >
						<td >
							召回发票数量：<input id="zhaohui" class="easyui-textbox" data-options="required:true" />
						</td>
					</tr>
				</table>
				<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queren()" data-options="iconCls:'icon-save'"  style="margin-top: 20px">确&nbsp;认&nbsp;</a>
			</div>
	</div>   
</body>
</html>