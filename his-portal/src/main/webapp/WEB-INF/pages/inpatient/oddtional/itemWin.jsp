<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
    <div class="easyui-panel" id="panelEast" data-options="title:'',iconCls:'icon-form'" style="padding-top: 20px;border: none;">
		<div style="padding:15px">
			<form id="editForm" method="post">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
	    		<tr>
	    			<td class="honry-lable">项目名称：</td>
	    			<td class="honry-view">
	    			  <input type="hidden" name="itemCode" value="${itemCode}"/>${itemCode}
	    			</td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">数量：</td>
	    			<td class="honry-view"><input class="easyui-numberbox"  style="width: 95%" id="qty" name="qty" value="${qty}" data-options="required:true"  missingMessage="请输入数量"/></td></td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">单位：</td>
	    			<td class="honry-view">
	    			 <input type="hidden" name="unit" value="${unit}"/>${unit}
	    			</td>
	    		</tr>  
	    		<tr>
	    			<td class="honry-lable">单价：</td>
	    			<td class="honry-view">
	    			<input type="hidden" name="price" value="${price}"/>${price}
	    			</td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">总价格：</td>
	    			<td  class="honry-view">
	    			<input type="hidden" name="total" value="${total}"/>${total}
	    			</td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">间隔：</td>
	    			<td class="honry-view">
	    			<input type="hidden" name="useInterval" value="${useInterval}"/>${useInterval}
	    			</td>
	    		</tr>
	    	</table>
	    	</form>
    </div>
    <div style="text-align:center;padding:5px">
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:window.close();">关闭</a>
		&nbsp;&nbsp;&nbsp;<a  href="javascript:void(0)" onclick="javascript:alert('功能建设中！');" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
	</div>
  </div>
</body>
<script type="text/javascript">
</script>
</html>
