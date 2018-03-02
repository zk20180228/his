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
<title>设备采购申报</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px;padding: 0px">
	<div id="cc" class="easyui-layout" fit="true";"> 
    	<div data-options="region:'center',border:false" ">
			<form id="editForm" method="post" enctype="multipart/form-data">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin:10px auto 0;">
					<input type="hidden" id="userId" name="assetsPurch.id" value="${assetsPurch.id }">
					<input type="hidden" id="createUser" name="assetsPurch.createUser" value="${assetsPurch.createUser }">
					<input type="hidden" id="createDept" name="assetsPurch.createDept" value="${assetsPurch.createDept }">
					<input type="hidden" id="createTime" name="assetsPurch.createTime" value="${assetsPurch.createTime} }"/>
<!-- 					<tr> -->
<!-- 						<td class="honry-lable">办公用途编码：</td> -->
<%-- 		    			<td class="honry-info"><input id="officeCode" name="assetsPurch.officeCode" value="${assetsPurch.officeCode }" style="border:none;outline:medium;" readonly/></td> --%>
<!-- 					</tr> -->
					<tr>
						<td class="honry-lable">办公用途：</td>
		    			<td class="honry-info">
		    				<input id="officeName" name="assetsPurch.officeName" value="${assetsPurch.officeName }" style="border:none;outline:medium;" readonly/>
		    				<input id="officeCode" name="assetsPurch.officeCode" value="${assetsPurch.officeCode }" type="hidden"/>
		    			</td>
					</tr>
<!-- 					<tr> -->
<!-- 						<td class="honry-lable">类别代码：</td> -->
<%-- 		    			<td class="honry-info"><input id="classCode" name="assetsPurch.classCode" value="${assetsPurch.classCode }" style="border:none;outline:medium;" readonly/></td> --%>
<!-- 					</tr> -->
					<tr>
						<td class="honry-lable">设备分类：</td>
		    			<td class="honry-info">
		    				<input id="className" name="assetsPurch.className" value="${assetsPurch.className }" style="border:none;outline:medium;" readonly/>
		    				<input id="classCode" name="assetsPurch.classCode" value="${assetsPurch.classCode }" type="hidden" />
		    			</td>
					</tr>
<!-- 					<tr> -->
<!-- 						<td class="honry-lable">设备代码：</td> -->
<%-- 		    			<td class="honry-info"><input id="deviceCode" name="assetsPurch.deviceCode" value="${assetsPurch.deviceCode }" style="border:none;outline:medium;" readonly/></td> --%>
<!-- 	    			</tr> -->
					<tr>
						<td class="honry-lable">设备名称：</td>
		    			<td class="honry-info">
		    				<input id="deviceName" name="assetsPurch.deviceName" value="${assetsPurch.deviceName }" style="border:none;outline:medium;" readonly/>
		    				<input id="deviceCode" name="assetsPurch.deviceCode" value="${assetsPurch.deviceCode }" type="hidden"/>
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">计量单位：</td>
					    <td class="honry-info"><input id="meterUnit" name="assetsPurch.meterUnit" value="${assetsPurch.meterUnit }" style="border:none;outline:medium;" readonly/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">采购单价(元)：</td>
		    			<td class="honry-info"><input id="purchPrice" name="assetsPurch.purchPrice" value="${assetsPurch.purchPrice }" style="border:none;outline:medium;" readonly/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">采购数量：</td>
		    			<td class="honry-info"><input class="easyui-numberbox" id="purchNum" name="assetsPurch.purchNum" value="${assetsPurch.purchNum }" data-options="required:true,
		    			width:300,min:0,prompt:'请输入整数'"/><span style="color:red">剩余采购数量${overNum}</span></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">运费(元)：</td>
		    			<td class="honry-info"><input class="easyui-numberbox" id="tranCost" name="assetsPurch.tranCost" value="${assetsPurch.tranCost }" data-options="required:true,
		    			width:300,min:0,precision:2,prompt:'请输入数字'"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">安装费(元)：</td>
		    			<td class="honry-info"><input class="easyui-numberbox" id="instCost" name="assetsPurch.instCost" value="${assetsPurch.instCost }" data-options="required:true,
		    			width:300,min:0,precision:2,prompt:'请输入数字'"/></td>
	    			</tr>				
					<tr>
						<td class="honry-lable">采购总价(元)：</td>
		    			<td class="honry-info"><input id="purchTotal" name="assetsPurch.purchTotal" value="${assetsPurch.purchTotal }" style="border:none;outline:medium;"/></td>
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
	$('#purchNum').numberbox({  
	    onChange: function(value) {
	    	var num = $('#purchNum').numberbox("getValue")
	    	var overNum = '${overNum}'
	    	if(num-overNum>0){
	    		$.messager.alert('提示',"采购数量不能大于剩余采购数量！");
	    		$('#purchNum').numberbox("setValue","")
	    	}else{
	    		changeCost()
	    	}
	    }
	});
	function changeCost(){
		var purchPrice = $('#purchPrice').val();
		var num = $('#purchNum').numberbox("getValue");
		var tranCost = $("#tranCost").numberbox("getValue");
        var instCost = $("#instCost").numberbox("getValue");
        if(num!=null && num!='' && num!=undefined){
        	var cost =  parseInt(purchPrice*num)+ parseInt(tranCost)+ parseInt(instCost)
        	$('#purchTotal').val(cost.toFixed(2))
        }
	}
	$('#tranCost').numberbox({  
	    onChange: function(value) {
	    	changeCost()        
	    }
	});
	$('#instCost').numberbox({  
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
			url : "<c:url value='/assets/assetsPurch/saveOrUpdateAssets.action'/>",
			queryParams:{'assetsPurch.applState':applState},
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
					$.messager.alert('提示',dataMap.resMsg);
					//实现刷新
					$('#editForm').form('reset');
					closeDialog();
					if(flg==0){
						$('#tab').tabs('select',"草稿箱");
					}else if(flg==1){
						$('#tab').tabs('select',"申报中");
					}
					$('#tab').tabs({
						onSelect: function(title,index){
							if(title=="草稿箱"){
			 					$('#drafts').datagrid('reload');  
							}else if(title=="申报中"){
			 					$('#declaring').datagrid('reload');  
							}
						}
					})
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