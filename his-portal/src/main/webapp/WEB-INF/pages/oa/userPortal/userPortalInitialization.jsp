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
</head>
<body>
	<div id="divLayout" class="easyui-layout" class="easyui-layout" data-options="fit:true" style="width: 100%; height: 100%; overflow-y: auto;">
		<div  class="easyui-layout" fit="true">
		<form id="editForm" method="post">
			<div style="padding: 5px 5px 5px 5px;margin-left:auto;margin-right:auto;">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 60% ;margin-left:auto;margin-right:auto;" >
					<tr id="menuId">
						<td class="honry-lable">组件资源:</td>
				    	<td>
				    	   <table style="border: 0px">
							<tr>
								<td id="buttonListId" style="border: 0px">所有组件:<br>
									<select multiple="multiple" id="selectAll" style="width:200px;height:320px;">
										<c:forEach var="list" items="${portalWidgetList }">
											<option value="${list.id }">${list.name }</option>
										</c:forEach>
									</select>
								</td>
								<td style="border: 0px">
								<input type="button" value="&nbsp;&gt;&nbsp;" id="add_this"><br>
								<input type="button" value="&nbsp;&lt;&nbsp;" id="remove_this"><br>
								<input type="button" value="&nbsp;&gt;&gt;&nbsp;" id="add_all"><br>
								<input type="button" value="&nbsp;&lt;&lt;&nbsp;" id="remove_all"><br>
								</td>
								<td style="border: 0px">初始化组件:<br>
									<select multiple="multiple" id="selectBut" name="butName" style="width: 200px;height:320px;"></select>
								</td>
							</tr>
							</table>
			    		</td>
					</tr>
							</table>
					 	<div id="dialog"></div>  
					  <div id="toolbarId" style="text-align:center;width:70%;padding:5px;margin-left:auto;margin-right:auto;">
					 <a href="javascript:submit();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">初&nbsp;始&nbsp;化</a>
			    </div>
			</div>
			</form>
		</div>
		</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<style type="text/css">
.easyui-dialog .panel-header .panel-tool a{
    background-color: red;	
}
</style>
	<script type="text/javascript">
	//表单提交
	function submit(){
		$('#selectBut option').prop("selected",true);
		$('#editForm').form('submit',{  
			url:"<c:url value='/oa/userPortal/initializationUserPortal.action'/>",
        	onSubmit:function(){
				if ($('#selectBut option').length==0) {
					$.messager.alert('提示','请选择要初始化的组件信息!');
	    			$('#selectBut option').prop("selected",false);
					return false;
				}
				$.messager.progress({text:'保存中，请稍后...',modal:true});
        	},  
	        success:function(result){  
	        	$.messager.progress('close');
	        	if(result == 'success'){
	        		$.messager.alert('提示','保存成功！');
	        	}else{
					$.messager.alert('提示','保存失败！');	
	        	}
	        }						         
   		}); 
	}		
			//移到右边
		    $('#add_this').click(function() {
		    //获取选中的选项，删除并追加给对方
		        $('#selectAll option:selected').appendTo('#selectBut');
		    });
		    //移到左边
		    $('#remove_this').click(function() {
		        $('#selectBut option:selected').appendTo('#selectAll');
		    });
		    //全部移到右边
		    $('#add_all').click(function() {
		        //获取全部的选项,删除并追加给对方
		        if(!$('#selectAll').prop("disabled")){
		        	 $('#selectAll option').appendTo('#selectBut');
		        }
		    });
		    //全部移到左边
		    $('#remove_all').click(function() {
		        $('#selectBut option').each(function(){
		        	if(!$(this).prop("disabled")){
		        		$(this).appendTo('#selectAll');
		        	}
		        });
		    });
		    //双击选项
		    $('#selectAll').dblclick(function(){ //绑定双击事件
		        //获取全部的选项,删除并追加给对方
		        $("option:selected",this).appendTo('#selectBut'); //追加给对方
		    });
		    //双击选项
		    $('#selectBut').dblclick(function(){
		       $("option:selected",this).appendTo('#selectAll');
		    });
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>