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
	<title>医用图片查看界面</title>
</head>
<body>
	<div class="easyui-panel" id = "panelEast" data-options="title:'查看',iconCls:'icon-form',border: false,fit:true">
		<div style="padding:10px">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="margin-left:auto;margin-right:auto;" >
					<tr>
						<td class="honry-lable">图片名称：</td>
		    			<td class="honry-info">${emcPicture.picName }&nbsp;</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">身体部位：</td>
		    			<td class="honry-info" id="picBody">&nbsp;</td>
		    			<input id="picBodyHid" type="hidden" value="${emcPicture.picBody }"/>
	    			</tr>	
	    			<tr>
						<td class="honry-lable">图片路径：</td>
		    			<td class="honry-info">${emcPicture.picPath }&nbsp;</td>		    			
	    			</tr> 
	    			<tr>
						<td class="honry-lable">备注：</td>
		    			<td class="honry-info">${emcPicture.picBak }&nbsp;</td>
	    			</tr> 	    			
				</table>
		    <div style="text-align:center;padding:5px">
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
		    </div>
		</div>
	</div>
	
	<script type="text/javascript">
		$(function(){
			var value = $('#picBodyHid').val();
			if(value!=null){
				for(var i=0;i<checkpointList.length;i++){
					if(value==checkpointList[i].encode){
						$('#picBody').html(checkpointList[i].name);
						break;
					}
				}
			}
		});
</script>
</body>
</html>
