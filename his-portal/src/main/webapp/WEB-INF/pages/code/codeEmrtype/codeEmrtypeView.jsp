<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<html>
  <body>
  	<div class="easyui-panel" id="panelEast" data-options="title:'电子病历分类查看',iconCls:'icon-form',border:false" style="width:580px">
		<div data-options="region:'center',border:false" style="padding:10px">
			<input type="hidden" id="id" name="id" value="${codeEmrtype.id }">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="0px" style="width:100%">
					<tr>
						<td class="honry-lable">
							电子病历分类名称:
						</td>
						<td class="honry-view">
							${codeEmrtype.name}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							代码:
						</td>
						<td class="honry-view">
							 ${codeEmrtype.encode}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							子编码标志:
						</td>
						<td class="honry-view">
							${codeEmrtype.haveson}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							说明:
						</td>
						<td class="honry-view">
							${codeEmrtype.description}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							排序:
						</td>
						<td class="honry-view">
							${codeEmrtype.order}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							路径:
						</td>
						<td class="honry-view">
							${codeEmrtype.path}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							层级:
						</td>
						<td class="honry-view">
							${codeEmrtype.levell}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							可选标志:
						</td>
						<td class="honry-view">
							${codeEmrtype.canSelect}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							默认标志:
						</td>
						<td class="honry-view">
							${codeEmrtype.isdefault}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							适用医院:
						</td>
						<td class="honry-view">
							${codeEmrtype.hospital}
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							不适用医院:
						</td>
						<td class="honry-view">
							${codeEmrtype.nonhospital}
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
