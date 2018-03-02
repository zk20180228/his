<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<html>
	<body>
	<div class="easyui-panel" id="panelEast" data-options="title:'',iconCls:'icon-form'" fit=true style="width:580px">
		<div data-options="region:'center',border:false" style="padding:10px">
			<input type="hidden" id="id" name="id" value="${codeTitle.id }">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%">
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
		<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="closeWin()">关闭</a>
	</div>
  </div>
 <script>
			/**
			 * 关闭弹出窗口
			 * @author  hedong
			 * @date 2015-6-3
			 * @version 1.0
			 */
			function closeWin(){
				parent.win.dialog('close');
			}
		</script>

</body>
</html>