<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div class="easyui-panel" id="panelEast"
		data-options="iconCls:'icon-form',border:false" style="width: 100%">
		<div style="padding: 5px">
			<form id="editForm" method="post">
				<input type="hidden" id="id" name="deptId" value="${fictitiousDept.deptId }">
				<table class="honry-table" style="width: 100%" cellpadding="1" cellspacing="1"
					border="1px solid black" style="margin-left:auto;margin-right:auto;">
					<tr>
						<td class="honry-lable">
							科室分类:
						</td>
						<td class="honry-info">
							<input id="deptType" class="easyui-combobox" 
								name="deptType"  value="${fictitiousDept.deptType }" 
							 data-options="required:true,editable:false" 
							 missingMessage="请输入选择分类"" style="width: 290px" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							虚拟部门:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="deptName"
								name="deptName" value="${fictitiousDept.deptName }"
								data-options="required:true" style="width: 290px" 
								missingMessage="请输入名称" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							部门Code:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="deptCode"
								name="deptCode" value="${fictitiousDept.deptCode }"
								data-options="required:true" style="width: 290px" 
								missingMessage="请输入名称" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							部门简称:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="deptBrev"
								name="deptBrev" value="${fictitiousDept.deptBrev }"
								data-options="required:true" style="width: 290px"
								missingMessage="请输入部门简称" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							部门英文:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="deptEname"
								name="deptEname"
								value="${fictitiousDept.deptEname }" data-options=""
								style="width: 290px" missingMessage="请输入部门英文" />
						</td>
					</tr>
					<!-- TODO -->
					<!-- 2017-5-22 11:05:22 杜天亮 渲染有问题，临时注释掉 -->
					<%-- <tr>
						<td class="honry-lable">
							所在院区:
						</td>
						<td class="honry-info">
							<input class="easyui-combobox"  id="deptAddress" 
								name="deptDistrict"
								value="${fictitiousDept.deptDistrict }"
								data-options="required:true,valueField:'id',textField:'text',data: [{id:'1',text:'河西院区'},{id:'2',text:'郑东院区'},{id:'1',text:'惠济院区'}],onChange:function(newValue,oldValue){
									if(newValue==1){
										$('#deptDistrictName').val('河西院区');
									}else if(newValue==2){
										$('#deptDistrictName').val('郑东院区');
									}else{
										$('#deptDistrictName').val('惠济院区');
									}
								}" style="width: 290px"
								missingMessage="请输入部门地点" />
							<input type="hidden" id="deptDistrictName" name="deptDistrictName" value="${fictitiousDept.deptDistrictName }"> 
						</td>
					</tr> --%>
					<tr>
						<td class="honry-lable">
							自定义码:
						</td>
						<td class="honry-info">
							<input class="easyui-textbox"  id="deptInputcode"
								name="deptInputCode"
								value="${fictitiousDept.deptInputCode }" style="width: 290px"
								missingMessage="请输入自定义码" />
						</td>
					</tr>
					<tr >
	    			<td class="honry-lable" align="right">排序:</td>
	    			<td><input  type="text" id="deptOrder" name="deptOrder" value="${fictitiousDept.deptOrder }"  style="width:290px"  /></td>
	    			</tr>
					<tr>
						<td class="honry-lable">
							是否是挂号部门:
						</td>
						<td>
							<input id="deptIsforregisterHidden" type="hidden"
								name="deptIsforregister"
								value="${fictitiousDept.deptIsforregister }">
							<input type="checkBox" id="deptIsforregister"
								onclick="javascript:onclickBoxDep()" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							停用标志:
						</td>
						<td>
							<input id="stopFlgHidden" type="hidden"
								name="stop_flg" value="${fictitiousDept.stop_flg }">
							<input type="checkBox" id="stopFlg"
								onclick="javascript:onclickBox('stopFlg')" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							备注:
						</td>
						<td class="honry-info">
							<textarea class="easyui-validatebox" rows="4" cols="32" id="deptRemark" name="deptRemark"
								data-options="multiline:true">${fictitiousDept.deptRemark }</textarea>
						</td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<a href="javascript:submit();void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-save'">保存</a>
					<c:if test="${sysDepartment.id==null }">
						<a href="javascript:clear();void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-clear'">清除</a>
					</c:if>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog()">关闭</a>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	var order="${fictitiousDept.deptOrder }";
	$(function() {
		$('#deptType').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=depttype",
			valueField : 'encode',
			textField : 'name',
			multiple : false,
		});

		if ($('#deptIsforregisterHidden').val() == 1) {
			$('#deptIsforregister').attr("checked", true);
			$('#deptRegisterno').textbox({
				disabled : false
			});
		} else {
			$('#deptRegisterno').textbox({
				disabled : true
			});
		}
		if ($('#stopFlgHidden').val() == 1) {
			$('#stopFlg').attr("checked", true);
		}
	});
	//排序操作
	$('#deptOrder').spinner({    
	    required:true,
	    min:1,
	    max:10000,
	    value:1,
	    increment:1,
	    onSpinUp:function(){
	    	var val=parseInt($('#deptOrder').spinner('getValue'));
	    	$('#deptOrder').spinner('setValue',val+1);	
	    },
	    onSpinDown:function(){
	    	var val=parseInt($('#deptOrder').spinner('getValue'));
	    	if(val>1){
	    		$('#deptOrder').spinner('setValue',val-1);
	    	}
	    }
	});
	$('#deptOrder').spinner('setValue',order);
	//编码回车事件
	function KeyDown(flg, tag) {
		if (flg == 1) {//回车键光标移动到下一个输入框
			if (event.keyCode == 13) {
				event.keyCode = 9;
			}
		}
		if (flg == 0) { //空格键打开弹出窗口
			if (event.keyCode == 32) {
				event.returnValue = false;
				event.cancel = true;
			}
		}
	}
	//所属院区更改事件
	function chan(){
		alert($('#deptAddress').combobox('getText'));
	}
	//表单提交   
	function submit() {
		$('#editForm').form('submit', {
			url : "<%=basePath%>baseinfo/fictitiousDept/editFictitiousDept.action",
			data : $('#editForm').serialize(),
			dataType : 'json',
			onSubmit : function() {
				if (!$('#editForm').form('validate')) {
					$.messager.progress('close');
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					$.messager.alert('提示信息','验证没有通过,不能提交表单!','warning');
					close_alert();
					return false;
				}
				$.messager.progress({text:'保存中，请稍后...',modal:true});
			},
			success : function(data) {
				$.messager.progress('close');
				if(data=='true'){
					$.messager.alert('提示','保存成功！');
					//实现刷新科室树
					closeDialog();
					$('#tDt').tree('reload');
				}else{
					$.messager.alert('提示','保存失败,虚拟科室code重复');
				}
				
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示','保存失败！');
			}
		});
	}
	//清除所填信息
	function clear() {
		$('#editForm').form('reset');
	}
	function onclickBox(id) {
		if ($('#' + id).is(':checked')) {
			$('#' + id + 'Hidden').val(1);
		} else {
			$('#' + id + 'Hidden').val(0);
		}
	}
	//单选按钮赋值
	function onclickBoxDep(id) {
		if ($('#deptIsforregister').is(':checked')) {
			$('#deptIsforregisterHidden').val(1);
			$('#deptRegisterno').textbox({
				disabled : false,
				required : true
			});
		} else {
			$('#deptIsforregisterHidden').val(0);
			$('#deptRegisterno').textbox({
				disabled : true
			});
		}
	}

	/**
	 * 弹出框
	 * @author  lt
	 * @date 2015-06-29
	 * @version 1.0
	 */
	var win;
	function showWin(title, url, width, height) {
		var content = '<iframe id="myiframe" src="'
				+ url
				+ '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
		var divContent = '<div id="treeDeparWin">';
		win = $('<div id="treeDeparWin"><div/>').dialog({
			content : content,
			width : width,
			height : height,
			modal : true,
			minimizable : false,
			maximizable : true,
			resizable : true,
			shadow : true,
			center : true,
			title : title
		});
		win.dialog('open');
	}
</script>
</body>
</html>