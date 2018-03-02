<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<style type="text/css">
		.tableCss {
			border-collapse: collapse;
			border-spacing: 0;
			border-left: 1px solid #95b8e7;
			border-top: 1px solid #95b8e7;
		}
		
		.tableLabel {
			text-align: right;
			width: 150px;
		}
		
		.tableCss td {
			border-right: 1px solid #95b8e7;
			border-bottom: 1px solid #95b8e7;
			padding: 5px 15px;
			word-break: keep-all;
			white-space: nowrap;
		}
		</style>
</head>
<body>
	<form id="saveForm" method="post">
		<div style="padding:5px 5px 5px 5px;" align="center">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0"> 
				<tr>
					<td class="tableLabel">支付方式：</td>
					<td id="payWayTdId"><input id="payWay" name="inPrepay.payWay" class="easyui-combobox" style="width:150px" missingMessage="请选择支付方式"></td>
				</tr>
				<tr>
					<td class="tableLabel">预交金额：</td>
					<td id="prepayCostTdId"><input id="prepayCost" name="inPrepay.prepayCost" class="easyui-numberbox" data-options="required:true,precision:2,validType:'keyUpForPrepay'" min=0.01 style="width:150px" missingMessage="请填写预交金额"></td>
				</tr>
				<tr id="actualCostTrId">
					<td class="tableLabel">实收金额：</td>
					<td id="actualCostTdId"><input id="actualCost" class="easyui-numberbox" data-options="precision:2,validType:'keyUpForActual'" style="width:150px"></td>
				</tr>
				<tr id="backCostTrId">
					<td class="tableLabel">找回金额：</td>
					<td id="backCostTdId"><input id="backCost" class="easyui-numberbox" data-options="precision:2,readonly:true" style="width:150px"></td>
				</tr>
				<tr id="openBankTrId" style="display: none">
					<td class="tableLabel">开户银行：</td>
					<td id="openBankTdId"><input id="openBank" name="inPrepay.openBank" class="easyui-combobox" style="width:150px" missingMessage="请选择开户银行"></td>
				</tr>
				<tr id="openAccountsTrId" style="display: none">
					<td class="tableLabel">银行账号：</td>
					<td id="openAccountsTdId"><input id="openAccounts" name="inPrepay.openAccounts" class="easyui-textbox" style="width:150px" missingMessage="请填写银行账号"></td>
				</tr>
				<tr id="oldRecipenoTrId" style="display: none">
					<td class="tableLabel">小票号：</td>
					<td id="oldRecipenoTdId"><input id="oldRecipeno" name="inPrepay.oldRecipeno" class="easyui-textbox" style="width:150px" missingMessage="请填写小票号"></td>
				</tr>
			</table>
		</div>
		<div style="text-align:center;padding:5px">
	    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-money_yen'">收费</a>
	    	<a href="javascript:closeInfoModl('modlDivId');" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
	    </div>
	</form>	
<script type="text/javascript">
$(function(){
	/**
	 *  
	 * @Description：收款与找回计算
	 * @Author：aizhonghua
	 * @CreateDate：2016-3-09 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-09 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	$.extend($.fn.validatebox.defaults.rules, {
		keyUpForPrepay: {    
	        validator: function(value, param){ 
	        	var yujiaos = $('#actualCost').numberbox('getValue');
	        	if(yujiaos!=null&&yujiaos!=''){
	        		var shishous = value;
		        	var shishou= shishous*100;
		        	
		        	var yujiao = yujiaos*100;
		        	var giveChanges =shishou-yujiao;
		        	var giveChange = giveChanges/100;
		        	$('#backCost').numberbox('setValue',giveChange);
	        	}
	            return true;    
	        }
	    }, 
		keyUpForActual: {    
	        validator: function(value, param){ 
	        	var shishous = value;
	        	var shishou= shishous*100;
	        	var yujiaos = $('#prepayCost').numberbox('getValue');
	        	var yujiao = yujiaos*100;
	        	var giveChanges =shishou-yujiao;
	        	var giveChange = giveChanges/100;
	        	$('#backCost').numberbox('setValue',giveChange);
	            return true;    
	        }
	    }    
	});
	
	/**
	 *  
	 * @Description：支付方式下拉框
	 * @Author：aizhonghua
	 * @CreateDate：2016-3-09 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-3-09 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	
	 $('#payWay').combobox({    
		   url:'<%=basePath%>inpatient/inprePay/queryPayType.action',
			valueField:'encode',    
			textField:'name',
			multiple:false,
			required:true,
			editable:false,
			onSelect:function(record){
				if(record.encode=='CH'){
					chequeShow();
				}else{
					chequeHide();
				}
			}
		});

});

/**
 *  
 * @Description：过滤院内账户
 * @Author：aizhonghua
 * @CreateDate：2016-3-09 下午18:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-3-09 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 * @return: 支付方式
 *
 */

function getPayTypData(payTypeArray){
	var rev = "[";
	for(var i=0;i<payTypeArray.length;i++){
		if(payTypeArray[i].id!="YS"){
			if(rev.length>1){
				rev+= ",";
			}
			rev +="{'id':'"+payTypeArray[i].id+"','value':'"+payTypeArray[i].value+"'}"; 
		}
	}
	
	
	rev+="]"
	return eval("("+ rev +")");
}

/**
 *  
 * @Description：支付方式为支票，显示支票信息，隐藏找回信息
 * @Author：aizhonghua
 * @CreateDate：2016-3-09 下午18:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-3-09 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function chequeShow(){
	$('#openBankTrId').show();
	$('#openAccountsTrId').show();
	$('#oldRecipenoTrId').show();
	$('#actualCostTrId').hide();
	$('#backCostTrId').hide();
	//加载银行
	$('#openBank').combobox({
		url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=bank',
	    valueField:'encode',
	    textField:'name',
	    multiple:false,
	    editable:false,
	    required:true
	});
	$('#openAccounts').textbox({    
		required:true
	})
	$('#oldRecipeno').textbox({    
		required:true
	})
}

/**
 *  
 * @Description：支付方式为支票，隐藏支票信息，显示找回信息
 * @Author：aizhonghua
 * @CreateDate：2016-3-09 下午18:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-3-09 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function chequeHide(){
	$('#openBankTrId').hide();
	$('#openAccountsTrId').hide();
	$('#oldRecipenoTrId').hide();
	$('#actualCostTrId').show();
	$('#backCostTrId').show();
	$('#openBank').combobox({
		editable:false,
		required:false
	})
	$('#openAccounts').textbox({
		required:false
	})
	$('#oldRecipeno').textbox({
		required:false
	})
}

/**
 *  
 * @Description：表单提交
 * @Author：aizhonghua
 * @CreateDate：2016-3-09 下午18:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-3-09 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
function submit(){ 
	$('#saveForm').form('submit',{
		url:'<%=basePath%>inpatient/inprePay/savaInprePay.action',
		onSubmit:function(param){
			param.name = $('#name').text();
			param.inpatientNo = $('#inpatientNo').val();
			return $(this).form('validate');
		},  
		success:function(data){
			var dataMap = eval("("+data+")");
			$.messager.confirm('提示',dataMap.resCode+'是否打印预交金收据单？',function(r){
				if (r){
					printout(dataMap.id);
				}
			});
			if(dataMap.resMsg=='success'){
				$('#tt').tabs('select','预交金记录');
				$('#list').datagrid('load',{inpatientNo:$('#inpatientNo').val()});
				closeInfoModl('modlDivId');
			}
		},
		error : function(data) {
			$.messager.alert('提示','添加失败!');
		}
	}); 
}
//保存成功后打印
function printout(mid){
 	var timerStr = Math.random();
 	window.open ("<c:url value='/inpatient/inprePay/iReportzyyjj.action?randomId='/>"+timerStr+"&id="+mid+"&fileName=zhuyuanyujiaojin",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
}
</script>
</body>
</html>