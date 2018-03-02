<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<body style="margin: 0px; padding: 0px">
	<div class="easyui-panel" id = "panelEast" data-options="title:'查看',iconCls:'icon-form',fit:true">
		<div style="padding:5px">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%">
					<tr>
						<td class="honry-lable">医嘱类型：</td>
		    			<td class="honry-view"  >${tname}</td>
	    			</tr>
					<tr>
						<td class="honry-lable">系统类别：</td>
						<td class="honry-view">
							<c:if test="${null!=codeLists && !codeLists.isEmpty() }">
							        <c:forEach var="list" items="${codeLists }">
							             <option value="${list.code }">${list.name }</option>
							        </c:forEach>
						 </c:if>
	    			</tr>	    			
				</table>
		    <div style="text-align:center;padding:5px">
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
		    </div>
		</div>
	</div>
<script type="text/javascript">
	/**
	 * 关闭查看窗口
	 *
	 */
	function closeLayout(){
		$('#divLayout').layout('remove','east');
	}
				
	
</script>
</body>
