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
			<input type="hidden" id="id" name="businessOproom.id" value="${businessOproom.id }">
			<input type="hidden" id="createUser" name="businessOproom.createUser" value="${businessOproom.createUser }">
			<input type="hidden" id="createDept" name="businessOproom.createDept" value="${businessOproom.createDept }">
			<input type="hidden" id="createTime" name="businessOproom.createTime" value="${businessOproom.createTime }">
			<input type="hidden" id="stop_flg" name="businessOproom.stop_flg" value="${businessOproom.stop_flg }">
			<input type="hidden" id="del_flg" name="businessOproom.del_flg" value="${businessOproom.del_flg }">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr>
	    			<td align="right">房间代码:</td>
	    			<td>${businessOproom.roomId }</td>
	    		</tr>
	    		<tr>
	    			<td align="right">房间名称:</td>
	    			<td>${businessOproom.roomName }</td>
	    		</tr>
	    		<tr>
	    			<td align="right">助记码:</td>
	    			<td>${businessOproom.inputCode }</td>
	    		</tr>
	    		<tr >
	    			<td align="right">是否有效:</td>
	    			<td id="validFlag">
	    			</td>
	    		</tr>
	    		<tr>
	    			<td align="right">所属科室:</td>
	    			<td id="ladept"></td>
	    		</tr>
				<tr>
					<td colspan="2" align="center">
   					<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" class="easyui-linkbutton" onclick="closeDialog()">关闭</a>
   					</td>
				</tr>
			</table>	
		</form>
	</div>
<script type="text/javascript">
var operatTableEffective=new Map();;

	$(function(){
			//渲染手术台是否有效
		$.ajax({
			url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?',
			data:{"type" : "operatTableEffective"},
			type:'post',
			success: function(data) {
				for(var i=0;i<data.length;i++){
					operatTableEffective.put(data[i].encode,data[i].name);
				}
				var validFlag="${businessOproom.validFlag}";
				$('#validFlag').html(operatTableEffective.get(validFlag));
			}
		});	
		var date=$('#tDt').tree("getParent",$('#tDt').tree('getSelected').target);
		$("#ladept").text(date.text);
		
		
	});
</script>	
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>	
<style  type="text/css">
  .window .panel-header .panel-tool .panel-tool-close{
  	  	background-color: red;
  	}
 </style>
</body>

</html>