<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
    <div align="center" class="easyui-panel" style="padding:10px">
		<form id="treeEditForm" method="post" >
			<input type="hidden" id="id" name="businessContractunit.id" value="${businessContractunit.id }">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr align="center">
	    			<td class="honry-lable">名称：</td>
	    			<td><input class="easyui-textbox" type="text" id="name" name="businessContractunit.name" value="${businessContractunit.name }" data-options="required:true" style="width:290px"/></td>
	    			<td class="honry-lable">代码：</td>
	    			<td><input class="easyui-textbox" type="text" id="encode" name="businessContractunit.encode" value="${businessContractunit.encode }" data-options="required:true" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">自定义码：</td>
	    			<td><input class="easyui-textbox" type="text" id="inputCode" name="businessContractunit.inputCode" value="${businessContractunit.inputCode }" data-options="required:true" style="width:290px"/></td>
	    			<td class="honry-lable">排序：</td>
	    			<td><input class="easyui-textbox" id="order" name="businessContractunit.order" value="${businessContractunit.order }" data-options="required:true" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">结算类别：</td>
	    			<td><input class="easyui-combobox" id="paykindCode" name="businessContractunit.paykindCode" value="${businessContractunit.paykindCode }" data-options="required:true" missingMessage="请输入选择结算类别" style="width:290px"/></td>
	    			<td class="honry-lable">价格形势：</td>
	    			<td><input class="easyui-combobox" id="priceForm" name="businessContractunit.priceForm" value="${businessContractunit.priceForm }" data-options="required:true" missingMessage="请输入选择价格形势" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">公费比例：</td>
	    			<td><input class="easyui-textbox"  id="pubRatio" name="businessContractunit.pubRatio" value="${businessContractunit.pubRatio }" style="width:290px"/></td>
	    			<td class="honry-lable">自负比例：</td>
	    			<td><input class="easyui-textbox"  id="payRatio" name="businessContractunit.payRatio" value="${businessContractunit.payRatio }" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">自费比例：</td>
	    			<td><input class="easyui-textbox" id="ownRatio" name="businessContractunit.ownRatio" value="${businessContractunit.ownRatio }" style="width:290px"/></td>
	    			<td class="honry-lable">优惠比例：</td>
	    			<td><input class="easyui-textbox" id="ecoRatio" name="businessContractunit.ecoRatio" value="${businessContractunit.ecoRatio }" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">欠费比例：</td>
	    			<td><input class="easyui-textbox"  id="arrRatio" name="businessContractunit.arrRatio" value="${businessContractunit.arrRatio }"  style="width:290px"/></td>
	    			<td class="honry-lable">标识：</td>
	    			<td><input class="easyui-combobox" id="flag" name="businessContractunit.flag" value="${businessContractunit.flag }" data-options="required:true" missingMessage="请输入选择标识" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">日限额：</td>
	    			<td><input class="easyui-textbox"  id="dayLimit" name="businessContractunit.dayLimit" value="${businessContractunit.dayLimit }"  style="width:290px"/></td>
	    			<td class="honry-lable">月限额：</td>
	    			<td><input class="easyui-textbox"  id="monthLimit" name="businessContractunit.monthLimit" value="${businessContractunit.monthLimit }"  style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">年限额：</td>
	    			<td><input class="easyui-textbox"  id="yearLimit" name="businessContractunit.yearLimit" value="${businessContractunit.yearLimit }"  style="width:290px"/></td>
	    			<td class="honry-lable">一次限额：</td>
	    			<td><input class="easyui-textbox"  id="onceLimit" name="businessContractunit.onceLimit" value="${businessContractunit.onceLimit }" style="width:290px"/></td>
	    		</tr>
	    		<tr align="center">
	    			<td class="honry-lable">床位上限：</td>
	    			<td><input class="easyui-textbox" id="bedLimit" name="businessContractunit.bedLimit" value="${businessContractunit.bedLimit }" style="width:290px"/></td>
	    			<td class="honry-lable">空调上限：</td>
	    			<td><input class="easyui-textbox" id="airLimit" name="businessContractunit.airLimit" value="${businessContractunit.airLimit }" style="width:290px"/></td>
	    		</tr>
	    		<tr>
	    			<td colspan="4">
	    						&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-size: 14">婴儿标志：</span>
					    			<input type="hidden" id="babyFlagh" name="businessContractunit.babyFlag" value="${businessContractunit.babyFlag }"/>
					    			<input type="checkBox" id="babyFlag" onclick="javascript:checkBoxSelect('babyFlag',0,1)"/>
								&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-size: 14">是否要求必须有医疗证号：</span>
					    			<input id="mcardFlagh" type="hidden" name="businessContractunit.mcardFlag" value="${businessContractunit.mcardFlag }"/>
					    			<input type="checkBox" id="mcardFlag" onclick="javascript:checkBoxSelect('mcardFlag',0,1)"/>
								&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-size: 14">是否监控：</span>
					    			<input id="controlFlagh" type="hidden" name="businessContractunit.controlFlag" value="${businessContractunit.controlFlag }"/>
					    			<input type="checkBox" id="controlFlag" onclick="javascript:checkBoxSelect('controlFlag',0,1)"/>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">说明：</td>
	    			<td colspan="3"><input class="easyui-textbox" id="description" name="businessContractunit.description" value="${businessContractunit.description }" style="width:290px"/></td>
	    		</tr>
				<tr>
					<td colspan="4" align="center">
	  					<a href="javascript:void(0)" data-options="iconCls:'icon-accept'" class="easyui-linkbutton" onclick="submitTreeForm()">提交</a>
	  					<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" class="easyui-linkbutton" onclick="closeDialog()">关闭</a>
	  					</td>
				</tr>
			</table>	
		</form>
   </div>
<script type="text/javascript">
	var flagArray = null;
	/**
		 * 页面加载
		 * @author  zpty
		 * @date 2015-12-26 10:53
		 * @version 1.0
		 */
		$(function(){
			$('#paykindCode').combobox({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=paykind",
				valueField : 'encode',
				textField : 'name',
				multiple : false,
			});
			$('#priceForm').combobox({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=priceform",
				valueField : 'encode',
				textField : 'name',
				multiple : false,
			});
			$('#flag').combobox({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=priceform",
				valueField : 'encode',
				textField : 'name',
				multiple : false,
			});
			//判断是否编辑状态，编辑状态修改复选框选中状态
			if($('#id').val()!=''){
				if($('#babyFlagh').val()==1){
					$('#babyFlag').attr("checked", true); 
				}
				if($('#mcardFlagh').val()==1){
					$('#mcardFlag').attr("checked", true); 
				}
				if($('#controlFlagh').val()==1){
					$('#controlFlag').attr("checked", true); 
				}
			}
		});
	
	//复选框
		function checkBoxSelect(id,defalVal,selVal){
			var hiddenId=id+"h";
			if($('#'+id).is(':checked')){
				$('#'+hiddenId).val(selVal);
			}else{
				$('#'+hiddenId).val(defalVal);
			}
		}
	

		//验证代码是否已存在
		function submitTreeForm(){
			$.ajax({
				url : "<%=basePath%>baseinfo/businessContractunit/checkEncode.action",
				data : {
					encode : $("#encode").val(),
					unitid : $("#id").val(),
				},
				type : 'post',
				success : function(result) {
					if (result == "no") {
						$.messager.alert('提示',"此代码已存在,不能提交表单,请修改后提交!");
						setTimeout(function(){
							$(".messager-body").window('close');
						},1500);
						return;
					}
					else{
						submitTreeForms();
					}
				}
			});
		}
	
	//提交验证
	function submitTreeForms(){
		$('#treeEditForm').form('submit', {
			url : "<c:url value='/baseinfo/businessContractunit/businesseditTreeInfo.action'/>",
			data:$('#treeEditForm').serialize(),
	        dataType:'json',
			onSubmit : function() {
				if(!$('#treeEditForm').form('validate')){
					$.messager.show({  
				         title:'提示信息' ,   
				         msg:'验证没有通过,不能提交表单!'  
				    }); 
				       return false ;
				}
			},
			success : function(data) {
				$.messager.alert("提示","操作成功！");
				closeDialog();
				refresh();
				$("#list").datagrid("reload");
			},
			error : function(data) {
				$.messager.alert("提示","操作失败！");
			}
		}); 
	}

	
</script>	
</body>

</html>