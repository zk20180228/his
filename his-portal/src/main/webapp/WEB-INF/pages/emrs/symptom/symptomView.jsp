<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>电子病历医技症状维护</title>
	<%@ include file="/common/metas.jsp"%>
	<script>
			var symptomTypeList;
			$(function(){
				$.ajax({
					url: "<%=basePath %>emrs/symptom/symptomTypeCombobox.action",
	 				type:'post',
	 				success: function(data) {
	 					symptomTypeList = eval(data);
					}
				});
				if($('#symptomType').val()!=null){
					var value =$('#symptomType').val();
					for(var i = 0;i < symptomTypeList.length;i ++){
						if(value == symptomTypeList[i].encode){
							$('#type').val(symptomTypeList[i].name);
						}
					}
					
				}
				
			});
		</script>
</head>
	<body>
		<div class="easyui-panel"  id="panelEast" data-options="title:'查看',iconCls:'icon-form',border:false">
			<input type="hidden" id="id" name="symptom.id" value="${symptom.id }">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin-left:auto;margin-right:auto;margin-top:10px;" >
				<tr>
					<td class="honry-lable">
						代码:
					</td>
					<td class="honry-view">
						${symptom.symptomCode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						名称:
					</td>
					<td class="honry-view">
						${symptom.symptomName}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						类别:
					</td>
					<td class="honry-view">
					<input id="type" style="border:none" readonly="readonly" ></input>
							<input id="symptomType"  value="${symptom.symptomType }"  type="hidden">
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						拼音码:
					</td>
					<td class="honry-view">
						${symptom.symptomPinYin }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						五笔码:
					</td>
					<td class="honry-view">
						${symptom.symptomWb }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						自定义码:
					</td>
					<td class="honry-view">
						${symptom.symptomInputCode }&nbsp;
					</td>
				</tr>
			</table>
				<div style="text-align:center;padding:5px">
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
		</div>
	</body>
</html>