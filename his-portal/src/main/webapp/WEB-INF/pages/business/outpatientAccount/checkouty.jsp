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
<body>
	<div class="easyui-panel" id="panelEast">
		<form id="saveForm" method="post">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0">
				<tr style="display: none">
					<td class="honry-lable">menuAlias：</td>
	    			<td class="honry-info">
	    				<input type="text" name="menuAlias" value="${menuAlias}" style="width: 200"/></td>
				</tr>
				<tr style="display: none">
	    			<td class="honry-lable">患者就诊卡号：</td>
    				<td class="honry-info">
	    				<input class="easyui-textbox" id="b_idcardNo" name="idcardNo" data-options="editable:false" 
	    					style="width: 200"/></td>
	    		</tr>
				<tr>
	    			<td class="honry-lable">支付方式：</td>
    				<td class="honry-info">
	    				<input class="easyui-combobox" id="b_prepayType" name="outprepay.prepayType" data-options="required:true,editable:false" 
	    					style="width: 200"/></td>
	    		</tr>
				<tr>
					<td class="honry-lable">预交金额（/元）：</td>
	    			<td class="honry-info">
	    				<input class="easyui-numberbox" id="b_prepayCost" name="outprepay.prepayCost" data-options="required:true,precision:2" 
	    					style="width: 200"/></td>
    			</tr>
				<tr id="b_shishou">
	    			<td class="honry-lable">实收金额（/元）：</td>
	    			<td class="honry-info">
	    				<input class="easyui-numberbox" id="shishou" name="" data-options="required:true,precision:2,validType:'keyUpOnChange'" 
	    					style="width: 200"/></td>
	    		</tr>
				<tr id="b_zhaohui">
					<td class="honry-lable">找回金额（/元）：</td>
	    			<td class="honry-info">
	    				<input class="easyui-numberbox" id="zhaohui" name=""  data-options="precision:2,readonly:true" 
	    					style="width: 200"/></td>
    			</tr>
				<tr id="b_workName" style="display: none">
					<td class="honry-lable">开户单位：</td>
	    			<td class="honry-info">
	    				<input class="easyui-textbox" id="workName" name="outprepay.workName"  data-options="" 
	    					style="width: 200"/></td>
    			</tr>
				<tr id="b_openBank" style="display: none">
					<td class="honry-lable">开户银行：</td>
	    			<td class="honry-info">
	    				<input class="easyui-combobox" id="CodeBank" name="outprepay.openBank"  data-options="editable:false" 
	    					style="width: 200"/></td>
    			</tr>
				<tr id="b_openAccounts" style="display: none">
					<td class="honry-lable">银行账号：</td>
	    			<td class="honry-info">
	    				<input class="easyui-textbox" id="openAccounts" name="outprepay.openAccounts"  data-options="" 
	    					style="width: 200"/></td>
    			</tr>
				<tr id="b_bankTransno" style="display: none">
					<td class="honry-lable">小票号：</td>
	    			<td class="honry-info">
	    				<input class="easyui-textbox" id="bankTransno" name="outprepay.bankTransno"  data-options="" 
	    					style="width: 200"/></td>
    			</tr>
			</table>
			<div style="text-align:center;padding:5px">
		    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-money_yen'">收费</a>
		    	<a href="javascript:closeDialog();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
		    </div>
		</form>	
	</div>
<script type="text/javascript">
//收款与找回计算
$.extend($.fn.validatebox.defaults.rules, {
    keyUpOnChange: {    
        validator: function(value, param){ 
        	var shishous = value;
        	var shishou= shishous*100;
        	var yujiaos = $('#b_prepayCost').numberbox('getValue');
        	var yujiao = yujiaos*100;
        	var giveChanges =shishou-yujiao;
        	var giveChange = giveChanges/100;
        	$('#zhaohui').numberbox('setValue',giveChange);
            return true;    
        }
    }    
});
$(function(){
	    	//支付方式下拉框
	    	$('#b_prepayType').combobox({
	    		//data:payTypeVal,
	    		data:[{
		    			 encode:'CA',
		    			 name:'现金'
		    		 },{
		    			 encode:'CD',
			    		 name:'信用卡'
		    		 },{
		    			 encode:'CH',
			    		 name:'支票'
		    		 },{
		    			 encode:'DB',
			    		 name:'银联卡'
		    		 }],
	    		valueField:'encode',    
	    		textField:'name',
	    		multiple:false,
	    		required:true,
	    		onSelect:function(record){
	    			if(record.encode=='CH'){
	    				chequeShow();
	    			}else{
	    				chequeHide();
	    			}
	    		}
	    	});
//	    }
//	 });
	var cardno=$('#idcardNo').textbox('getValue');
 	$('#b_idcardNo').val(cardno);
 	
	
});
//支付方式为支票，显示支票信息，隐藏找回信息
function chequeShow(){
	$('#b_workName').show();
	$('#b_openBank').show();
	$('#b_openAccounts').show();
	$('#b_bankTransno').show();
	$('#b_shishou').hide();
	$('#b_zhaohui').hide();
	//加载银行
	idCombobox("CodeBank");
	
	$('#workName').textbox({    
		required:true
	})
	$('#CodeBank').combobox({    
		required:true
	})
	$('#openAccounts').textbox({    
		required:true
	})
	$('#bankTransno').textbox({    
		required:true
	})
	$('#shishou').textbox({    
		required:false
	})

}
//支付方式为支票，隐藏支票信息，显示找回信息
function chequeHide(){
	$('#b_workName').hide();
	$('#b_openBank').hide();
	$('#b_openAccounts').hide();
	$('#b_bankTransno').hide();
	$('#b_shishou').show();
	$('#b_zhaohui').show();
	
	$('#workName').textbox({    
		required:false
	})
	$('#CodeBank').combobox({    
		required:false
	})
	$('#openAccounts').textbox({    
		required:false
	})
	$('#bankTransno').textbox({    
		required:false
	})
	$('#shishou').textbox({    
		required:true
	})
}

//公共编码表，加载银行信息	
function idCombobox(param){
	$('#'+param).combobox({
		url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
		queryParams:{'type':'bank'},
	    valueField:'encode',
	    textField:'name',
	    multiple:false
	});
}

//	过滤支付方式(院内账户)
//[{id:'1',value:'现金'},{id:'2',value:'银联卡'},{id:'3',value:'支票'},{id:'4',value:'院内账户'}]
function getPayTypData(payTypeArray){
	var rev = "[";
	$.each(payTypeArray,function(i,n){
		if(n.id!="4"){
			if(rev.length>1){
				rev+= ",";
			}
			rev +="{'id':'"+n.id+"','value':'"+n.value+"'}"; 
		}
	});
	rev+="]";
	return eval("("+ rev +")");
}

//表单提交
function submit(){ 
	$('#saveForm').form('submit',{  
		url:'<%=basePath%>finance/outAccount/saveOutprepay.action',
        onSubmit:function(){
			if (!$('#saveForm').form('validate')) {
				$.messager.alert('提示信息','请填写完整信息!','warning');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}
			var v = $('#b_prepayCost').numberbox('getValue');
			var shs=$('#shishou').numberbox('getValue');
			var zh=$('#zhaohui').numberbox('getValue');
			if(v <= 0){
				$.messager.alert('提示信息','请正确填写预存金额信息!','warning');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}
			if(zh < 0){
				$.messager.alert('提示信息','请检查填写的金额信息是否正确!','warning');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}
			$.messager.progress({text:'保存中，请稍后...',modal:true});
        },
        success:function(data){
        	$.messager.progress('close');	
        	data = jQuery.parseJSON(data);
        	if(data.resMsg == 'success'){
        		$('#accountBalance').text(data.accountBalance.toFixed(2));
        		closeDialog();
        		$.messager.confirm('确认','操作成功，是否打印预交金收据？',function(r){    
        		    if (r){    
        		    	  var timerStr = Math.random();
	        	          //门诊预交金票据
	        	  		  window.open ("<c:url value='/iReport/iReportPrint/iReportForOutpatientPrepaid.action?randomId='/>"+timerStr+"&tid= "+$('#idcardNo').textbox('getValue')+"&fileName=outpatient_account",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
        		    }    
        		});
        		prestoreReload();
        	}else{
        		$.messager.alert('友情提示',data.resMes,'warning');
        		setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
        	}
        },
		error : function(data) {
			$.messager.progress('close');	
			$.messager.alert('提示','添加失败!');	
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}							         
    }); 
}	    

</script>
</body>
</html>