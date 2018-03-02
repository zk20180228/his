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
<title>电子病历常用词常用词维护查看界面</title>
</head>
	<body>
		<div class="easyui-panel"  id="panelEast" data-options="title:'查看',iconCls:'icon-form',fit:true">
			<input type="hidden" id="id" name="id" value="${emrWord.id }">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin-left:auto;margin-right:auto;margin-top:10px;">
				<tr>
					<td class="honry-lable">
						代码:
					</td>
					<td class="honry-view">
						${emrWord.wordCode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						名称:
					</td>
					<td class="honry-view">
						${emrWord.wordName}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						类别:
					</td>
					<td class="honry-view">
					<input id="type" style="border:none" readonly="readonly" ></input>
							<input id="wordType"  value="${emrWord.wordType }"  type="hidden">
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						拼音码:
					</td>
					<td class="honry-view">
						${emrWord.wordPinYin }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						五笔码:
					</td>
					<td class="honry-view">
						${emrWord.wordWb }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						自定义码:
					</td>
					<td class="honry-view">
						${emrWord.wordInputCode }&nbsp;
					</td>
				</tr>
			</table>
				<div style="text-align:center;padding:5px">
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
		</div>
			<script>
			var wordTypeList;
			$(function(){
				if($('#wordType').val()!=null){
					var value =$('#wordType').val();
					for(var i = 0;i < wordTypeList.length;i ++){
						if(value == wordTypeList[i].encode){
							$('#type').val(wordTypeList[i].name);
						}
					}
					
				}
				$.ajax({
					url: "<%=basePath %>emrs/word/wordTypeCombobox.action",
	 				type:'post',
	 				success: function(data) {
	 					wordTypeList = eval(data);
					}
				});
			});
			
			/**
			 * 关闭查看窗口
			 */
			function closeLayout(){
				$('#divLayout').layout('remove','east');
			}
			
		</script>
	</body>
</html>