<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>设备采购计划申报</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px;padding: 0px">
	<div id="cc" class="easyui-layout" fit="true";"> 
    	<div data-options="region:'center',border:false" ">
			<form id="editForm" method="post" enctype="multipart/form-data">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin:10px auto 0;">
					<input type="hidden" id="userId" name="assetsPurchplan.id" value="${assetsPurchplan.id }">
					<input type="hidden" id="createUser" name="assetsPurchplan.createUser" value="${assetsPurchplan.createUser }">
					<input type="hidden" id="createDept" name="assetsPurchplan.createDept" value="${assetsPurchplan.createDept }">
					<input type="hidden" id="createTime" name="assetsPurchplan.createTime" value="${assetsPurchplan.createTime} }"/>
					<tr>
						<td class="honry-lable">办公用途：</td>
						<input type="hidden" id="officeCode" name="assetsPurchplan.officeCode" value="${assetsPurchplan.officeCode }">
		    			<td class="honry-info"><input class="easyui-combogrid" id="officeName" name="assetsPurchplan.officeName" value="${assetsPurchplan.officeName }" data-options="required:true,width:300"/></td>
					</tr>
					<tr>
						<td class="honry-lable">类别代码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="classCode" name="assetsPurchplan.classCode" readonly="readonly" value="${assetsPurchplan.classCode }" data-options="required:true,width:300"/></td>
					</tr>
					<tr>
						<td class="honry-lable">设备分类：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="className" name="assetsPurchplan.className" readonly="readonly" value="${assetsPurchplan.className }" data-options="required:true,width:300"/></td>
					</tr>
					<tr>
						<td class="honry-lable">设备名称：</td>
						<input type="hidden" id="deviceCode" name="assetsPurchplan.deviceCode" value="${assetsPurchplan.deviceCode }">
		    			<td class="honry-info"><input class="easyui-textbox" id="deviceName" name="assetsPurchplan.deviceName" readonly="readonly" value="${assetsPurchplan.deviceName }" data-options="required:true,width:300"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">计量单位：</td>
					    <td class="honry-info"><input class="easyui-textbox" id="meterUnit" name="assetsPurchplan.meterUnit" readonly="readonly" value="${assetsPurchplan.meterUnit }" data-options="width:300"/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">计划单价(元)：</td>
		    			<td class="honry-info"><input class="easyui-numberbox" id="planPrice" name="assetsPurchplan.planPrice" value="${assetsPurchplan.planPrice }" data-options="required:true,
		    			width:300,min:0,precision:2,prompt:'请输入数字'"/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">计划数量：</td>
		    			<td class="honry-info"><input class="easyui-numberbox" id="planNum" name="assetsPurchplan.planNum" value="${assetsPurchplan.planNum }" data-options="required:true,
		    			width:300,min:0,prompt:'请输入整数'"/></td>
	    			</tr>				
					<tr>
						<td class="honry-lable">计划总价(元)：</td>
		    			<td class="honry-info"><input class="easyui-textbox" readonly="readonly" id="planTotal" name="assetsPurchplan.planTotal" value="${assetsPurchplan.planTotal }" data-options="width:300"/></td>
	    			</tr>				
				</table>
			</form>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存草稿</a>
				<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">提交申请</a>
			</div>
   		</div>
	</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
	$(function(){
		$('#createTime').val("${assetsPurchplan.createTime}".substring(0,19))
		var name = $.trim($('#officeName').val());
		$('#officeName').combogrid({
			  url:'<%=basePath%>assets/assetsPurchase/queryAssetsDeviceseRvice.action', 
			    queryParams:{officeName:name},	
			    editable:true,
				pagination:true,
	    		required:true,
	    		pageSize:10,
				pageList:[10,30,50,80,100],
	    		panelWidth:500,
	    		panelHeight:320,
	            idField:'officeName',    
	            textField:'officeName', 
	            mode:'remote',
			    columns:[[    
			        {field:'officeName',title:'办公用途',width:200},    
			        {field:'classCode',title:'类别代码',width:100},    
			        {field:'className',title:'设备分类',width:100},    
			        {field:'deviceName',title:'设备名称',width:200}, 
			        {field:'meterUnit',title:'计量单位',width:100}  
			    ]],
			    onClickRow:function(rowIndex, rowData){
			    	$('#classCode').textbox('setValue',rowData.classCode);
			    	$('#className').textbox('setValue',rowData.className);
			    	$('#deviceName').textbox('setValue',rowData.deviceName);
			    	$('#meterUnit').textbox('setValue',rowData.meterUnit);
			    	$('#officeCode').val(rowData.officeCode);
			    	$('#deviceCode').val(rowData.deviceCode);
			    },
			    onLoadSuccess: function(data){
			    	console.info(data);
			    }
		})
	
	});
	function changeCost(){
		var planNum = $("#planNum").numberbox("getValue");
        var planPrice = $("#planPrice").numberbox("getValue");
        if(planNum!=null && planNum!='' && planNum!=undefined){
        	var cost = (planNum*planPrice).toFixed(2)
        	$('#planTotal').textbox('setValue',cost);
        }
	}
	$('#planNum').numberbox({  
	    onChange: function(value) {
	    	changeCost()        
	    }
	});
	$('#planPrice').numberbox({  
	    onChange: function(value) {
	    	changeCost()        
	    }
	});
	function submit(flg){
		$.messager.progress({text:'保存中，请稍后...',modal:true});
		var applState;
		if(flg==0){
			applState =0;
		}else if(flg==1){
			applState =1;
		}
		$('#editForm').form('submit', {
			url : "<c:url value='/assets/assetsPurchase/saveOrUpdateAssets.action'/>",
			queryParams:{'assetsPurchplan.applState':applState},
			onSubmit : function() {
				if (!$('#editForm').form('validate')) {
					$.messager.progress('close');
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					return false;
				}
			},
			success : function(data) {
				var dataMap = eval('('+data+')');; 
				$.messager.progress('close');
				if('success'==dataMap.resCode){
					//实现刷新
					$('#editForm').form('reset');
					closeDialog();
					var state;
					if(flg==0){
						$('#tab').tabs('select',"草稿箱");
						state=0;
						$("#drafts").datagrid({
							url: '<%=basePath %>assets/assetsPurchase/queryAllAssets.action?state='+state,
							pageSize:10,
							pageList:[10,20,30,50,80,100],
							pagination:true
						})
					}else if(flg==1){
						$('#tab').tabs('select',"申报中");
						state =1;
						$("#declaring").datagrid({
							url: '<%=basePath %>assets/assetsPurchase/queryAllAssets.action?state='+state,
							pageSize:10,
							pageList:[10,20,30,50,80,100],
							pagination:true
						})
					}
					$.messager.alert('提示',dataMap.resMsg);
				}else if('error'==dataMap.resCode){
					$.messager.alert('提示',dataMap.resMsg);
				}
			}
		});
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>