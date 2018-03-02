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

	//员工Id、name Map
	var userMap=null;
	$(function(){
		//查询userMap
		$.ajax({
			url:'<%=basePath%>finance/invoiceUsageRecord/queryUserRecord.action',
			async:false,
			success:function(data){
				userMap=data;
			}
		});
		searchdatagrid();
		$('#name2').combobox({
			valueField: 'code',
			textField: 'text',
			data: [{
				code: '1',
				text: '组'
			},{
				code: '2',
				text: '员工'
			}]
		});
		//GH 添加回车事件
		bindEnterEvent('name1',searchdatagrid,'easyui');//姓名
		bindEnterEvent('name2',searchdatagrid);//姓名
		bindEnterEvent('name3',searchdatagrid,'easyui');//姓名
	});
	//查询发票使用记录
	function searchdatagrid(){
		var code=$('#name1').textbox('getValue');
		code=code.replace(/(^\s*)|(\s*$)/g, "");
		var type=$('#name2').combobox('getValue');
		var num=$('#name3').textbox('getValue');
		num=num.replace(/(^\s*)|(\s*$)/g, "");
		$('#table1').datagrid({
			url:'<%=basePath%>finance/invoiceUsageRecord/queryDatagrid.action',
			queryParams:{'code':code,'type':type,'num':num},
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
	}
	function functionstatue(value,row,index){
		if(value==2){
			return '员工';
		}else if(value==1){
			return '组';
		}
	}
	function functiontype(value,row,index){
		if(value==0){
			return '未使用';
		}else if(value==1){
			return '已使用';
		}else if(value==2){
			return '已召回';
		}
	}
	//渲染用户
	function functionuser(value,row,index){
		if(value!=null&&value!=''){
			return userMap[value];
		}
	}
	//将选择的未使用的发票进行召回	
	function save(){
		//获取所有被选中（checked）的行
		var rows=$('#table1').datagrid('getChecked');
		if(rows!=null&&rows.length>0){
				var rowss=JSON.stringify(rows);
				$.messager.progress({text:'保存中，请稍后...',modal:true});
				$.ajax({
					url:'<%=basePath%>finance/invoiceUsageRecord/saveData.action',
					data:{rowss:rowss},
					success:function(data){
						if(data.key=='success'){
							$.messager.progress('close');
							searchdatagrid();
							$.messager.alert('提示',data.value);
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}else if(data.key='used'){
							$.messager.progress('close');
							$.messager.alert('提示',data.value);
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}else if(data.key='null'){
							$.messager.progress('close');
							$.messager.alert('提示',data.value);
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}else if(data.key='zhaohui'){
							$.messager.progress('close');
							$.messager.alert('提示',data.value);
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}else if(data.key='error'){
							$.messager.progress('close');
							$.messager.alert('提示',data.value);
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}
						$('#table1').datagride('reload');
					},
					error:function(){
						$.messager.progress('close');
						$.messager.alert('提示','召回失败');
					}
				});
			}else{
			$.messager.alert('提示','请选择要进行召回的发票');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	function clear(){
		$('#name1').textbox('setValue',"");
		$('#name2').combobox('setValue',"");
		$('#name3').textbox('setValue',"");
		searchdatagrid();
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true" style="padding:7px 5px 5px 5px;">
	<div data-options="region:'north',border:false" style="width: 100%;height: 50px;padding:17px 7px 7px 7px;">
		账务组或员工编号：<input class="easyui-textbox" id="name1"/>
		&nbsp;领取人类型：<input class="easyui-combobox" id="name2" />
		&nbsp;发票号：<input class="easyui-textbox" id="name3"/>
		&nbsp;&nbsp;
		<shiro:hasPermission name="${menuAlias}:function:query">
			<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="searchdatagrid()" data-options="iconCls:'icon-search'" style="margin:0px 0px 0px 15px" >查询</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:set">		
			<a href="javascript:clear();void(0)"  class="easyui-linkbutton" onclick="clear()" data-options="iconCls:'reset'" style="margin:0px 0px 0px 15px" >重置</a>
		</shiro:hasPermission>
	</div>
	<div data-options="region:'center',border:false" style="padding:5px 0px 0px 0px;">
		<table class="easyui-datagrid" id="table1" data-options="fit:true,pagination:true,pageSize:20,pageList:[20,30,50,80,100]">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th> 
					<th data-options="field:'userName',width:'15%',align:'center'">账务组/员工</th> 
					<th data-options="field:'userId',width:'13%',align:'center'">账务组或员工编码</th> 
					<th data-options="field:'userType',width:'10%',align:'center',formatter:functionstatue">领取人类型</th> 
					<th data-options="field:'invoiceNo',width:'15%',align:'center'">发票号</th>
					<th data-options="field:'recoveryOpcd',width:'10%',align:'center',formatter:functionuser">召回操作员</th>
					<th data-options="field:'recoveryInvoiceNo',width:'15%',align:'center'">召回发票号</th>
					<th data-options="field:'recoveryDate',width:'10%',align:'center'">召回时间</th>
					<th data-options="field:'invoiceUsestate',width:'10%',align:'center',formatter:functiontype">使用状态</th>
				</tr>			
			</thead>
		</table>
	</div>
</body>
</html>