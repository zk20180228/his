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

</head>
<body>
	<div class="easyui-panel" id="panelEast" data-options="iconCls:'icon-form',fit:true" style="width: 100%;border:0">
		<div>
    		<form id="editForm" method="post" ">
				<input type="hidden" id="id" name="id" value="${menu.id}">
				<input type="hidden" id="morder" name="morder" value="${menu.morder}">
				<input type="hidden" id="path" name="path" value="${menu.path}">
				<input type="hidden" id="parentcode" name="parentcode" value="${menu.parentcode}">
				<input type="hidden" id="parentpath" name="parentpath" value="${menu.parentpath}">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin:10px auto 0;">
					<tr>
						<td class="honry-lable">上级栏目：</td>
						<td class="honry-view"><p>${menu.parent}</p></td>
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">栏目名称：</td>
						<td class="honry-view"><p>${menu.name}</p></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">栏目Code：</td>
						<td class="honry-view"><p>${menu.code}</p></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">栏目说明：</td>
						<td class="honry-view"><p>${menu.explain}</p></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">发布权限：</td>
		    			<td class="honry-view"><p>${mmpublish}</p></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">审核权限：</td>
		    			<td class="honry-view"><p>${mmcheck}</p></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">查看权限：</td>
		    			<td class="honry-view"><p>${mmview}</p></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">直接发布:</td>
						<td>
					        <p>
					            <c:if test="${menu.publishdirt !=1}">
					    			否,需审核!
					            </c:if>
					            <c:if test="${menu.publishdirt ==1}">
					    			是,可以直接发布!
					            </c:if>
					        </p>
			    		</td>
					</tr>
	    			<tr>
						<td class="honry-lable">停用标志:</td>
			    		<td>
					        <p>
					            <c:if test="${menu.stop_flag !=1}">
					    			启用!
					            </c:if>
					            <c:if test="${menu.stop_flag ==1}">
					    			未启用!
					            </c:if>
					        </p>
			    		</td>
					</tr>
		    	</table>
	    	</form>
	    </div>
	</div>    
	</body>
</html>