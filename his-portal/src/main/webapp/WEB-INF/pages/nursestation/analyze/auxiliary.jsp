<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
	<div id="AuxDivId" class="easyui-layout" data-options="fit:true,border:false" style="border-left:1px solid #95b8e7">   
		<div data-options="region:'center',title:'${name}附材管理',border:false">
			<input type="hidden" id="auxId" value="${id}">
			<input type="hidden" id="auxName" value="${name}">
			<table id="auxEd"></table>
		</div> 
	</div>
	<script>
	/**  
	 *  
	 * 医嘱审核-附材管理列表
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	$('#auxEd').datagrid({  
		method:'post',
		rownumbers:true,
		striped:true,
		border:false,
		checkOnSelect:true,
		selectOnCheck:false,
		singleSelect:true,
		fitColumns:false,
		pagination:false,
		fit:true,
		url:'<%=basePath%>nursestation/analyze/queryAuxiliaryForOrder.action',
		queryParams: {id:$('#auxId').val()},
		columns: [  
			[	{field:'ck',checkbox:true},
				{field:'itemName',title:'附材名称',width:'150'},
				{field:'specs',title:'规格',width:'150'},
				{field:'itemPrice',title:'价格',width:'150'},
				{field:'qtyTot',title:'数量',width:'100'},
				{field:'minUnit',title:'单位',width:'50'},
				{field:'frequencyName',title:'频次',width:'150'},
				{field:'useName',title:'用法',width:'150'},
				{field:'execDpnm',title:'执行科室',width:'150'},
				{field:'dateBgn',title:'开始时间',width:'170'},
				{field:'dateEnd',title:'停止时间',width:'170'},
				{field:'moNote2',title:'备注',width:'150'},
			]
		],
		toolbar: [{
			text:'添加',
			iconCls: 'icon-add',
			handler: function(){
				AdddilogInfoView('auxModelDivId',$('#auxName').val()+'附材添加','<%=basePath%>nursestation/analyze/auxiliaryAdd.action?id='+$('#auxId').val(),1200,400);
			}
		},'-',{
			text:'删除',
			iconCls: 'icon-remove',
			handler: function(){
				var rows = $('#auxEd').datagrid('getChecked');
				if(rows!=null&&rows.length>0){
					var id = '';
					for(var i=0;i<rows.length;i++){
						if(id!=''){
							id += ',';
						}
						id += rows[i].id;
					}
					if(id==''){
						$.messager.alert('提示','请选择要删除的附材信息！',null,function(){});	
					}
					$.ajax({
						url:"<%=basePath%>nursestation/analyze/delAuxInfo.action",
						type:'post',
						data:{advIdData:id},
						success:function(dataMap) {
							$.messager.alert('提示',dataMap.resCode,null,function(){});
							if(dataMap.resMsg=='success'){
								$('#auxEd').datagrid('reload');
							}
						},
						error:function(){
							$.messager.alert('提示','请求失败！',null,function(){});	
						}
					});
				}else{
					$.messager.alert('提示','请选择要删除的附材信息！',null,function(){});	
				}
			}
		},'-',{
			text:'关闭',
			iconCls: 'icon-cancel',
			handler: function(){
				$('#audEl').layout('remove','east');
			}
		}]
	});  
	
	/**  
	 *  
	 * 医嘱审核-添加组件
	 * @Author：aizhonghua
	 * @CreateDate：2016-6-22 下午04:41:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-6-22 下午04:41:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	function AdddilogInfoView(id,title,url,width,height) {
		$('#'+id).dialog({  
			title: title,	
			width: width,	
			height: height, 
			closed: false,	
			cache: false,
			top:100,
			href: url,  
			modal: true   
		});   
	}
	</script>
</body>
</html>