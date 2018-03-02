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
	<div class="easyui-panel" id = "panelEast" data-options="title:'查看',iconCls:'icon-form',border: false,fit:true">
		<div style="padding:10px">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;">					
					<tr>
						<td class="honry-lable">名称：</td>
		    			<td class="honry-info">${dictionary.name }</td>
	    			</tr>	
	    			<tr>
						<td class="honry-lable">代码：</td>
		    			<td class="honry-info">${dictionary.encode }</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">拼音码：</td>
		    			<td class="honry-info">${dictionary.pinyin }</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">五笔码：</td>
		    			<td class="honry-info">${dictionary.wb }</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">自定义码：</td>
		    			<td class="honry-info">${dictionary.inputCode }</td>
	    			</tr>	    			    			  			
	    			<tr>
						<td class="honry-lable">备注：</td>
		    			<td class="honry-info">${dictionary.mark }</td>
	    			</tr> 
	    			<tr>
						<td class="honry-lable">是否停用：</td>
		    			<td class="honry-info" id="stop">${dictionary.validState == 1 ? '停用' : '在用' }</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">操作员：</td>
		    			<td class="honry-info">${dictionary.operCode }</td>
	    			</tr> 	
	    			<tr>
						<td class="honry-lable">操作时间：</td>
		    			<td class="honry-info"><fmt:formatDate value="${dictionary.operDate }" pattern="yyyy-MM-dd hh:mm:ss"/></td>
	    			</tr> 
	    			<tr>
						<td class="honry-lable">创建时间：</td>
		    			<td class="honry-info" id="createTime"><fmt:formatDate value="${dictionary.createTime }" pattern="yyyy-MM-dd hh:mm:ss"/></td>
	    			</tr>		    		 
				</table>
				<div style="text-align:center;padding:5px">
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
			</form>
		</div>
	</div>
</body>
</html>