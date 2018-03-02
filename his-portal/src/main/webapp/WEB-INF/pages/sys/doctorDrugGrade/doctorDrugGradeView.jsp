<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<body>
	<div class="easyui-panel" id = "panelEast" data-options="title:'',iconCls:'icon-form',fit:true" style="width:580px">
		<form id="editForms" method="post">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;border-top:0">
					<input id="sddsid" name="sdds.id" type="hidden" value="${ sdds.id}"/>
	    			<tr>
		    			<td class="honry-lable" style="border-top:0">医生级别名称:</td>
		    			<td class="honry-info" style="border-top:0">${sdds.postname }</td>
		    		</tr>
					<tr>
						<td class="honry-lable">医生职级代码:</td>
		    			<td class="honry-info">${sdds.tpost }</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">药品等级名称:</td>
		    			<td class="honry-info">${sdds.graadename}</td>
	    			</tr>
					<tr>
						<td class="honry-lable">药品等级代码:</td>
		    			<td class="honry-info">${sdds.druggraade }</td>
	    			</tr>
	    			<tr>
		    			<td class="honry-lable">是否适用:</td>
		    			<td class="honry-info">
			    			<c:if test="${param.useFlag==0 }">否</c:if>
			    			<c:if test="${param.useFlag==1 }">是</c:if>
		    			</td>					
					</tr>
					<tr>
						<td class="honry-lable">医院:</td>
		    			<td class="honry-info" id="hos"></td>
	    			</tr>
					<tr>					
						<td class="honry-lable">排序:</td>
		    			<td class="honry-info">${sdds.order }</td>
	    			</tr>
	    			<tr style="width:98%;height:300px">
						<td class="honry-lable">说明:</td>
						<td colspan="3">
							<textarea rows="4" cols="50" style="width:98%;height:100%;border: none;background: white;" disabled="disabled">${sdds.description}</textarea>
						</td>
					</tr>
	    	</table>
	    	<div  style="text-align:center;padding:5px">
	
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()" >关闭</a>
		    </div>
		</form>			
	</div>
	
<script type="text/javascript">
	var hosId = ${param.hospitalId };
	var hospitalMap1 = null;
	$(function(){
		//渲染医院
		$.ajax({
			url:'<%=basePath%>renderingHospital.action',
			type:'post',
			success: function(payData) {
				hospitalMap1 = payData;
				var text = hospitalMap1[hosId];
				document.getElementById("hos").innerHTML=text;
			}
		});
	})
	//关闭
	function closeLayout(){
		$('#divLayout').layout('remove','east');
	}
	
</script>
</body>