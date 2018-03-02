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
		<div id="divLayout" class="easyui-layout" fit=true style="width:100%;height:100%;overflow-y: auto;">
			<div style="padding:5px 5px 0px 5px;">
				<form id="addMatKindForm" method="post">
					<input type="hidden" id="kindType" name="kindType" value="${kindType}">
					<input type="hidden" id="kindPath" name="kindPath" value="${kindPath}">
					<input type="hidden" id="preCode" name="preCode" value="${preCode}">
					<input type="hidden" id="id" name="id" value="${matKindinfo.id }">
					<table >
						<tr>
							<td>分类名称：</td>
							<td><input class="easyui-textbox" id="kindName" name="kindName" value="${matKindinfo.kindName}" data-options="required:true"></td>
							<td>&nbsp;物品科目编码：</td>
							<td><input class="easyui-textbox" id="kindCode" name="kindCode" value="${matKindinfo.kindCode}" data-options="required:true"  missingMessage="请输入物品科目编码"></td>
							<td>&nbsp;自定义码：</td>
							<td><input class="easyui-textbox" id="customCode" name="customCode" value="${matKindinfo.customCode}"></td>
						</tr>
						<tr>	
							<td>国家编码：</td>
							<td><input class="easyui-textbox" id="gbCode" name="gbCode" value="${matKindinfo.gbCode}"></td>
							<td>&nbsp;财务分类：</td>
							<td><input class="easyui-combobox" id="accountCode" name="accountCode" value="${matKindinfo.accountCode}"></td>
							<td  colspan="6">
				    			&nbsp;停用：
				    			<input type="hidden" id="validFlag" name="validFlag" value="${matKindinfo.validFlag}"/>
				    			<input type="checkBox" id="validFlagBox"/>
				    			&nbsp;财务收费标志：
				    			<input type="hidden" id="financeFlag" name="financeFlag" value="${matKindinfo.financeFlag}"/>
				    			<input type="checkBox"  id="financeFlagBox"/>
							</td>
						</tr>
						<tr>
							<td>备注：</td>
							<td colspan="7"><input class="easyui-textbox" id="memo" name="memo"  style="width:80%;" value="${matKindinfo.memo}"></td>
						</tr>
					</table>
				</form>
				<div style="text-align: center; padding: 5px">
					<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			var effectAreaData = [{"id":0,"text":"本科室"},{"id":1,"text":"本科室及下级科室"},{"id":2,"text":"全院"},{"id":3,"text":"指定科室"}];//有效范围
			$(function(){
				//财务分类 下拉
				$("#accountCode").combobox({
					url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=financial',
					valueField:'encode',
					textField:'name'
				});
				//复选框的回显
				var validFlag = "${matKindinfo.validFlag}";
				var financeFlag = "${matKindinfo.financeFlag}";
				if(validFlag==0){
					$('#validFlagBox').prop("checked","checked");
				}
				if(financeFlag==1){
					$('#financeFlagBox').prop("checked","checked");
				}
			});
			//验证当效范围选择指定科室时有效科室为必填
			function validEffectDepartment(){
				var effectArea=$('#effectArea').combobox('getValue');
				if(effectArea=="3"){
					var effectDept=$('#effectDept').combotree('getValue');
					if(effectDept==null ||effectDept==""){
						$.messager.alert('提示',"有效范围选择指定科室时，请选择有效科室！");
						return false;
					}
				}
				return true;
			}
			function submit(){
				if($('#validFlagBox').is(':checked')){
					$('#validFlag').val(0);
				}else{
					$('#validFlag').val(1);
				}
				if($('#financeFlagBox').is(':checked')){
					$('#financeFlag').val(1);
				}else{
					$('#financeFlag').val(0);
				}
				$('#addMatKindForm').form('submit', {
					url : "<%=basePath%>material/kind/saveMatKindinfo.action",
					queryParams : {validFlag1:$('#validFlag').val(),financeFlag1:$('#financeFlag').val()},
					onSubmit : function() {
						if(!$('#addMatKindForm').form('validate')){
							$.messager.show({  
						         title:'提示信息' ,   
						         msg:'验证没有通过,不能提交表单!'  
						    }); 
						    return false ;
						}
						if(!validEffectDepartment()){
							return false ;
						}
					},
					success : function(dataMap) {
						$.messager.alert('提示',dataMap);
						closeDialogs();
						$('#tDt').tree('options').url = "<c:url value='../orderKindInfo/queryTree.action?t1.validFlag=1'/>" ;
						$('#tDt').tree('reload');
					},
					error : function(data) {
						$.messager.alert('提示',"操作失败！");	
					}
				});
			}
		</script>
	</body>
</html>