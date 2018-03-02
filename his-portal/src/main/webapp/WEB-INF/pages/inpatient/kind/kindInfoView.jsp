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
			<input type="hidden" name="kindInfo.id" value="${kindInfo.id }">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0"  style="width: 100%">
					<tr>
						<td class="honry-lable">医嘱类别名称：</td>
		    			<td class="honry-view">${kindInfo.typeName }&nbsp;</td>
	    			</tr>
					<tr>
						<td class="honry-lable">适用范围：</td>
		    			<td class="honry-view">
		    				<c:choose>
							<c:when test="${kindInfo.fitExtent   eq '1'}">
							<span>门诊</span>
							</c:when>
							<c:when test="${kindInfo.fitExtent   eq '2'}">
							<span>住院</span>
							</c:when>
							<c:when test="${kindInfo.fitExtent   eq '3'}">
							<span>全院</span>
							</c:when>
						</c:choose>
						</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">医嘱状态：</td>
		    			<td class="honry-view" >
		    				<c:choose>
							<c:when test="${kindInfo.decmpsState   eq '1'}">
							<span>长期医嘱</span>
							</c:when>
							<c:otherwise>
							<span>临时医嘱</span>
							</c:otherwise>
						</c:choose>
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">是否计费：</td>
		    			<td class="honry-view">
		    			<c:choose>
							<c:when test="${kindInfo.chargeState   eq '1'}">
							<span>是</span>
							</c:when>
							<c:otherwise>
							<span>否</span>
							</c:otherwise>
						</c:choose>
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">药房是否配药：</td>
		    			<td class="honry-view">
		    				<c:choose>
							<c:when test="${kindInfo.needDrug  eq '1'}">
							<span>是</span>
							</c:when>
							<c:otherwise>
							<span>否</span>
							</c:otherwise>
						   </c:choose>
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">是否打印执行单：</td>
		    			<td class="honry-view">
		    				<c:choose>
							<c:when test="${kindInfo.prnExelist  eq '1'}">
							<span>是</span>
							</c:when>
							<c:otherwise>
							<span>否</span>
							</c:otherwise>
						    </c:choose>
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">是否需要确认：</td>
		    			<td class="honry-view">
		    				<c:choose>
							<c:when test="${kindInfo.needConfirm  eq '1'}">
							<span>是</span>
							</c:when>
							<c:otherwise>
							<span>否</span>
							</c:otherwise>
						</c:choose>
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">是否能开总量：</td>
		    			<td class="honry-view">
		    			<c:choose>
							<c:when test="${kindInfo.totqtyFlag  eq '1'}">
							<span>是</span>
							</c:when>
							<c:otherwise>
							<span>否</span>
							</c:otherwise>
						</c:choose>
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">是否打印医嘱单：</td>
		    			<td class="honry-view">
		    			<c:choose>
							<c:when test="${kindInfo.prnMorlist eq '1'}">
							<span>是</span>
							</c:when>
							<c:otherwise>
							<span>否</span>
							</c:otherwise>
						</c:choose>
		    			</td>
	    			</tr>
	    			<tr>
					<td class="honry-lable">创建时间：</td>
					<td class="honry-view"><fmt:formatDate value="${kindInfo.createTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
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
