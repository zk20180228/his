<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>ICD编辑</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px;padding: 0px">
	<div class="easyui-panel" id="panelEast" data-options="iconCls:'icon-form',fit:true,border:false">
		<div style="padding:5px">
    		<form id="editForm" method="post">
				<input type="hidden" id="id" name="id" value="${icd.id }">
				<input type="hidden" id="createUser" name="createUser" value="${icd.createUser }">
		  		<input type="hidden" id="createDept" name="createDept" value="${icd.createDept }">
		  		<input type="hidden" id="createTime" name="createTime" value="${icd.createTime }"/>
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
					<tr>
						<td class="honry-lable">
						<span>诊断码：</span></td>
		    			<td class="honry-info">
		    			<input type="hidden" id="type" value="${type }"> 
		    			<input class="easyui-textbox " id="code" name="code" value="${icd.code }" data-options="required:true" missingMessage="请输入诊断码" style="width: 200"/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">
						<span>诊断名称：</span></td>
		    			<td class="honry-info">
		    			<input class="easyui-textbox "id="name" name="name" value="${icd.name }" data-options="required:true" missingMessage="请输入诊断名称" style="width: 200"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">
						<span>诊断别名1：</span></td>
		    			<td class="honry-info">
		    			<input class="easyui-textbox "id="alias" name="alias" value="${icd.alias }" style="width: 200"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">
						<span>诊断别名2：</span></td>
		    			<td class="honry-info">
		    			<input class="easyui-textbox "id="alias2" name="alias2" value="${icd.alias2 }" style="width: 200"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">
						<span>医保诊断编码：</span></td>
		    			<td class="honry-info">
		    			<input class="easyui-textbox" id="dsCode" name="dsCode" value="${icd.dsCode }" style="width: 200"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">
						<span>附加码：</span></td>
		    			<td class="honry-info">
		    			<input class="easyui-textbox "id="addcode" name="addcode" value="${icd.addcode }" style="width: 200"/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">
						<span>自定义码：</span></td>
		    			<td class="honry-info">
		    			<input class="easyui-textbox "id="inputcode" name="inputcode" value="${icd.inputcode }"  missingMessage="请输入代码" style="width: 200"/></td>
	    			</tr>
	    			
		    				
	    			<tr>
						<td class="honry-lable">
						<span>疾病分类：</span></td>
		    			<td class="honry-info"><input type="hidden" id="diseHid" value="${icd.diseasetype }">
							<input id="diseasetype" name="diseasetype"  data-options="width:200"> 
							<a href="javascript:delSelectedData('diseasetype');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
		    			</td>
		    			
		    			
	    			</tr>
	    			<tr>
						<td class="honry-lable">
						<span>疾病死亡原因：</span></td>
		    			<td class="honry-info">
							<input class="easyui-textbox" id="diereason" name="diereason" value="${icd.diereason }" data-options="width:200"> 
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">
						<span>适用性别：</span></td>
		    			<td class="honry-info">
		    			    <input type="hidden" id="sexHid" value="${icd.sex }">
		    				<input id="sex" name="sex"  data-options="width:200"> 
		    			</td>
	    			</tr>
	    			<tr>					
						<td colspan="2">
							<span>30中疾病：</span>
			    			<input type="hidden" id="isThirtyHidden" name="isThirty" value="${icd.isThirty }"/>
			    			<input type="checkBox" id="isThirty" onclick="javascript:onclickBox('isThirty')"/>
							&nbsp;<span>传染病：</span>
			    			<input type="hidden" id="isComHidden" name="isCom" value="${icd.isCom }"/>
			    			<input type="checkBox" id="isCom" onclick="javascript:onclickBox('isCom')"/>				    			
							&nbsp;<span>肿瘤：</span>
			    			<input type="hidden" id="isTumorHidden" name="isTumor" value="${icd.isTumor }"/>
			    			<input type="checkBox" id="isTumor" onclick="javascript:onclickBox('isTumor')"/>
							&nbsp;<span>中医诊断：</span>
			    			<input type="hidden"  id="isTcmHidden" name="isTcm" value="${icd.isTcm }"/>
			    			<input type="checkBox" id="isTcm" onclick="javascript:onclickBox('isTcm')"/>
							&nbsp;<span>停用：</span>
			    			<input type="hidden" id="stop_flgHidden" name="stop_flg" value="${icd.stop_flg }"/>
			    			<input type="checkBox" id="stop_flg" onclick="javascript:onclickBox('stop_flg')"/>
		    			</td>
	    			</tr>
	    	</table>
		    <div style="text-align:center;padding:5px">
			    <c:if test="${icd.id==null }">
			    	<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
		    	</c:if>
		    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
		    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
		    	<a href="javascript:closeDialog();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
		    </div>
    	</form>
    </div>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
	//页面加载
	$(function(){
		//疾病分类
		$('#diseasetype').combobox({
			url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionaryICD.action?type=diseasetype'/>",
		    valueField:'encode',    
		    textField:'name',
		    filter: function(q, row){
		        var keys = new Array();
		        keys[keys.length] = 'encode';
		        keys[keys.length] = 'name';
		        keys[keys.length] = 'pinyin';
		        keys[keys.length] = 'wb';
		        keys[keys.length] = 'inputCode';
		        return filterLocalCombobox(q, row, keys);
		    },onHidePanel:function(none){
		        var data = $(this).combobox('getData');
		        var val = $(this).combobox('getValue');
		        var result = true;
		        for (var i = 0; i < data.length; i++) {
		            if (val == data[i].encode) {
		                result = false;
		            }
		        }
		        if (result) {
		            $(this).combobox("clear");
		        }else{
		            $(this).combobox('unselect',val);
		            $(this).combobox('select',val);
		        }
		    }
		});
		var diseHid = $('#diseHid').val();
		if(diseHid!=null&&diseHid!=""){
			$('#diseasetype').combobox('setValue',diseHid);
		}
		//适用性别
		$('#sex').combobox({
			data : [{
				"id":'A',
				"text":"全部"
			},{
				"id":'M',
				"text":"男"
			},{
				"id":'F',
				"text":"女",
				"selected":true
			},{
				"id":'U',
				"text":"未知"
			}],
			valueField : 'id',
			textField : 'text',
		    editable:false
		});
		var sexHid = $('#sexHid').val();
		if(sexHid!=null&&sexHid!=""){
			$('#sex').combobox('setValue',sexHid);
		}
		
		
		//复选框赋值
		if($('#isThirtyHidden').val()==1){
			$('#isThirty').attr("checked", true); 
		}
		if($('#isComHidden').val()==1){
			$('#isCom').attr("checked", true); 
		}
		if($('#isTumorHidden').val()==1){
			$('#isTumor').attr("checked", true); 
		}
		if($('#isTcmHidden').val()==1){
			$('#isTcm').attr("checked", true); 
		}
		if($('#stop_flgHidden').val()==1){
			$('#stop_flg').attr("checked", true); 
		}
		//给疾病类型方法绑定弹窗事件
		bindEnterEvent('diseasetype',popWinToCodeDiseasetype,'easyui');
	});
	
	//诊断码唯一验证
	function submit(){ 
		var id = "${icd.id}";
		if(id!=null&&id!=""){
			if("${icd.code}"!=$('#editForm :input[name="code"]').val()){
				$.post("<c:url value='/baseinfo/icd/selectIcdCode.action'/>?code="+$('#code').val()+"&type="+$("#type").val(),function(result){
		            if(result==0){//不存在
		            	submitForm();
		            }else{
		            	$('#code').textbox('setValue','');
		            	$.messager.alert('提示信息','诊断码已存在！');
		            	setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						$("#code").focus();
						return;
		            }
			    });		
			}else{
				submitForm();
			}
		}else{
			$.post("<c:url value='/baseinfo/icd/selectIcdCode.action'/>?code="+$('#code').val()+"&type="+$("#type").val(),function(data){
	            if(data=="0"){//不存在
	            	submitForm();
	            }else{
	            	$('#code').textbox('setValue','');
	            	$.messager.alert('提示信息','诊断码已存在！');
	            	setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					$("#code").focus();
					return;
	            }
		    });	
		}
    }	    
	//表单提交
	function submitForm(){
		 $('#editForm').form('submit',{  
		    	url: "<c:url value='/baseinfo/icd/saveIcd.action'/>?type="+$("#type").val(),
		        onSubmit:function(){
					if (!$('#editForm').form('validate')) {
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
		        },  
		        success:function(data){  
		        	if(data=="error"){
		        		$.messager.alert('提示信息','保存失败，您无此操作权限!');
		        		setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
		        	}else{
		        		$.messager.alert('提示信息','保存成功!');
		        	}
		        	closeDialog();
		        	reloadpage();
		        },
				error : function(data) {
					$.messager.alert('提示信息','保存失败!');
				}							         
		    }); 
    }	    
    
	//连续添加
	function addContinue(){ 
	$.post("<c:url value='/baseinfo/icd/selectIcdCode.action'/>?code="+ $('#code').val() + "&type=" + $("#type").val(), function(result) {
			if (result == 0) {//不存在
				$('#editForm').form('submit',{url : "<c:url value='/baseinfo/icd/saveIcd.action'/>?type="+ $("#type").val(),
					onSubmit : function() {
						if (!$('#editForm').form('validate')) {
							$.messager.show({title : '提示信息',msg : '验证没有通过,不能提交表单!'});
							return false;
						}
					},
					success : function() {
						clear();
						reloadpage();
					},
					error : function(data) {
						$.messager.alert('提示信息', '保存失败!');
					}
				});
			} else {
				$('#code').textbox('setValue', '');
				$.messager.alert('提示信息', '诊断码已存在！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				$("#code").focus();
				return;
			}
		});
		
	}
	
	function reloadpage() {
		//实现刷新栏目中的数据
		$('#list').datagrid('reload');
	}

	//清除页面填写信息
	function clear() {
		$('#editForm').form('reset');
	}
	//复选框赋值
	function onclickBox(id) {
		if ($('#' + id).is(':checked')) {
			$('#' + id + 'Hidden').val(1);
		} else {
			$('#' + id + 'Hidden').val(0);
		}
	}

	/**
	 * 打开疾病分类界面弹框
	 * @author  zhuxiaolu
	 * @date 2015-5-25 10:53
	 * @version 1.0
	 */

	function popWinToCodeDiseasetype() {

		var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?textId=diseasetype&type=diseasetype";
		var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+(screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
				
	}
	
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>