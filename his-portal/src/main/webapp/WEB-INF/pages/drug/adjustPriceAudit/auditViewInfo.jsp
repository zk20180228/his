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
		<form id="treeEditForm" method="post" >
			<div title="Tab1" style="padding: 20px;">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width: 100%;">
					<tr>
						<td  class="honry-lable" style="font-size: 14" style="width: 50px">名称:</td>
		    			<td  style="width: 200px" >${drugName}</td>
		    			<td  class="honry-lable" style="font-size: 14" style="width: 50px">名称自定义:</td>
		    			<td   style="width: 200px">${drugNameinputcode}</td>
		    		</tr>
		    		<tr>
						<td class="honry-lable" style="font-size: 14">通用名称:</td>
		    			<td  >${drugCommonname}</td>
						<td class="honry-lable" style="font-size: 14">通用名称自定义码:</td>
		    			<td  >${drugCnameinputcode}</td>
		    		</tr>
		    		<tr>
						<td class="honry-lable" style="font-size: 14">英文品名:</td>
		    			<td  >${drugEnname}</td>
		    			<td class="honry-lable" style="font-size: 14">规格:</td>
		    			<td  >${spec}</td>
		    		</tr>
		    		<tr>
						<td class="honry-lable" style="font-size: 14">药品类别:</td>
		    			<td  >${drugType}</td>
		    			<td class="honry-lable" style="font-size: 14">药品性质:</td>
		    			<td  >${drugNature}</td>
		    		</tr>
		    		<tr>
						<td class="honry-lable" style="font-size: 14">包装单位:</td>
		    			<td  >${drugPackUnit}</td>
		    			<td class="honry-lable" style="font-size: 14">批发价:</td>
		    			<td  >${drugInfoAdjustVo.drugWholesaleprice}</td>
		    		</tr>
		    		<tr>
						<td class="honry-lable" style="font-size: 14">零售价:</td>
		    			<td  >${drugInfoAdjustVo.drugRetailprice}</td>
		    			<td class="honry-lable" style="font-size: 14">购入价:</td>
		    			<td  >${drugInfoAdjustVo.drugPurchaseprice}</td>
		    		</tr>
				</table>	
			</div>		
		</form>
		<script type="text/javascript">
		
		</script>
	</body>
</html>