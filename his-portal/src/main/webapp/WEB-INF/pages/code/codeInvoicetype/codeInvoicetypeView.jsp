<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/javascript/default.jsp"></jsp:include>

  <body>
  	<div class="easyui-panel" id="panelEast" data-options="title:'发票类型查看',iconCls:'icon-form'" style="width:580px">
		<div style="padding:10px">
			<input type="hidden" id="id" name="id" value="${invoicetype.id }">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
	    		
	    		<tr>
	    			<td class="honry-lable">代码：</td>
	    			<td class="honry-view"><p>${invoicetype.encode }&nbsp;</p></td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">名称：</td>
	    			<td class="honry-view"><p>${invoicetype.name }&nbsp;</p></td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">拼音码：</td>
	    			<td class="honry-view"><p>${invoicetype.pinyin }&nbsp;</p></td>
	    		</tr>  
	    		<tr>
	    			<td class="honry-lable">五笔码：</td>
	    			<td class="honry-view"><p>${invoicetype.wb }&nbsp;</p></td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">自定义码：</td>
	    			<td  class="honry-view"><p>${invoicetype.inputCode }&nbsp;</p></td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">排序：</td>
	    			<td class="honry-view"><p>${invoicetype.order }&nbsp;</p></td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">适用医院:</td>
	    			<td class="honry-view"><p>${invoicetype.hospital }&nbsp;</p></td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">不适用医院:</td>
	    			<td class="honry-view"><p>${invoicetype.nonhospital }&nbsp;</p></td>
	    		</tr>
	    		
	    		<tr>
	    			<td class="honry-lable">说明：</td>
	    			<td class="honry-view"><p>${invoicetype.description }&nbsp;</p></td>
	    		</tr>
	    		<tr>
					<td colspan="2">
						可选标志:
						<c:choose>
						<c:when test="${invoicetype.canselect eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
						&nbsp;&nbsp;默认标志:
						 <c:choose>
						<c:when test="${invoicetype.isdefault eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
						&nbsp;&nbsp;停用标志:
						<c:choose>
						<c:when test="${invoicetype.stop_flg eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
					</td>
				</tr>
	    	</table>
    </div>
    <div style="text-align:center;padding:5px">
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
	</div>
  </div>
 <script type="text/javascript">
  	//关闭
  	function closeLayout(){
		$('#divLayout').layout('remove','east');
	}
</script>
</body>
</html>
