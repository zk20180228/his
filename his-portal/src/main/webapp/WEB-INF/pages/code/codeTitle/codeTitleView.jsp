<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<html>
  <body>
  	<div class="easyui-panel" id="panelEast" data-options="title:'职称查看',iconCls:'icon-form',border:false" style="width:580px">
		<div data-options="region:'center',border:false" style="padding:10px">
			<input type="hidden" id="id" name="id" value="${codeTitle.id }">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="0px" style="width:100%">
					<tr>
						<td class="honry-lable">
							职称名称:
						</td>
						<td class="honry-view">
							${codeTitle.name}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							代码:
						</td>
						<td class="honry-view">
							 ${codeTitle.encode}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							子编码标志:
						</td>
						<td class="honry-view">
							${codeTitle.haveson}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							说明:
						</td>
						<td class="honry-view">
							${codeTitle.description}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							排序:
						</td>
						<td class="honry-view">
							${codeTitle.order}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							路径:
						</td>
						<td class="honry-view">
							${codeTitle.path}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							层级:
						</td>
						<td class="honry-view">
							${codeTitle.levell}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							可选标志:
						</td>
						<td class="honry-view">
							${codeTitle.canSelect}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							默认标志:
						</td>
						<td class="honry-view">
							${codeTitle.isdefault}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							适用医院:
						</td>
						<td class="honry-view">
							${codeTitle.hospital}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							不适用医院:
						</td>
						<td class="honry-view">
							${codeTitle.nonhospital}
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
