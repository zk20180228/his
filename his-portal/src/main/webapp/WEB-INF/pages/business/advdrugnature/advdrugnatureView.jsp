<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
  <body>
  	<div class="easyui-panel" id="panelEast" data-options="title:'长期医嘱限制性药品性质查看',iconCls:'icon-form',fit: true">
		<div style="padding:10px">
			<input type="hidden" id="id" name="id" value="${advdrugnature.id }">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;">
	    		
	    		<tr>
	    			<td class="honry-lable">代码：</td>
	    			<td class="honry-view"><p>${advdrugnature.encode }&nbsp;</p></td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">名称：</td>
	    			<td class="honry-view"><p>${advdrugnature.name }&nbsp;</p></td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">拼音码：</td>
	    			<td class="honry-view"><p>${advdrugnature.pinyin }&nbsp;</p></td>
	    		</tr>  
	    		<tr>
	    			<td class="honry-lable">五笔码：</td>
	    			<td class="honry-view"><p>${advdrugnature.wb }&nbsp;</p></td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">排序：</td>
	    			<td class="honry-view"><p>${advdrugnature.order }&nbsp;</p></td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">适用医院:</td>
	    			<td class="honry-view">
	    				<input id="hospital" type="text" style="border:none" readonly="readonly"></input>
	    				<input id="hospitalhidden" type="hidden" value="${advdrugnature.hospital}"></input>
	    			</td>
	    		</tr>
	    		
	    		
	    		<tr>
	    			<td class="honry-lable">说明：</td>
	    			<td class="honry-view"><p>${advdrugnature.description }&nbsp;</p></td>
	    		</tr>
	    		<tr>
					<td colspan="2">
						可选标志:
						<c:choose>
						<c:when test="${advdrugnature.canselect eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
						&nbsp;&nbsp;默认标志:
						 <c:choose>
						<c:when test="${advdrugnature.isdefault eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
						&nbsp;&nbsp;停用标志:
						<c:choose>
						<c:when test="${advdrugnature.stop_flg eq '1'}">
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
 
 	$(function(){
 	   /**
 		* @Description 医院集合map渲染
 		* @author   
 		* @Modifier tangfeishuai
 		* @ModifyDate 2016-04-12
 		* @ModifyRmk 
 		* @CreateDate 2015-05-23
 		* @version   1.0 
 		*/
 		var hoid=$('#hospitalhidden').val();
		$.ajax({
			url: '<%=basePath %>inpatient/advdrugnatrue/getHospitalMap.action',
			type:'post',
			success: function(data) {
				hoslist = data;
				if(hoid!=null){
					$('#hospital').val(hoslist[hoid]);	
				}
			}
		});
 	})
   /**
	* @Description 关闭页面
	* @author   
	* @Modifier tangfeishuai
	* @ModifyDate 2016-04-12
	* @ModifyRmk 
	* @CreateDate 2015-05-23
	* @version   1.0 
	*/
  	function closeLayout(){
		$('#divLayout').layout('remove','east');
	}
</script>
</body>
</html>
