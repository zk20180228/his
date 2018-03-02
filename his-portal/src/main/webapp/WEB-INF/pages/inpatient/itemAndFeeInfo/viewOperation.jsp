<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
	String medicalrecordId= request.getAttribute("medicalrecordId").toString();
%>
<jsp:include page="/javascript/default.jsp"></jsp:include>
	<body>
		<div id="divLayout" class="easyui-layout" fit=true>	
			<input type="hidden" value="${medicalrecordId }" id="medicalrecordId"/>
			<div data-options="region:'center',split:false,title:'手术信息列表',iconCls:'icon-book'" style="padding:5px;">
				<table id="operlist" class="easyui-datagrid" data-options="url:'operation/itemAndFeeInfo/queryOperationListss.action?medicalrecordId=${medicalrecordId}'">
						<thead>
							<tr>
							<th data-options="field:'receptDate',formatter:formatDatebox">按患者时间</th>
							<th data-options="field:'preDate',formatter:formatDatebox">预约时间</th>
							<th data-options="field:'clinical'">临床表现</th> 
							<th data-options="field:'duration'">预订用时</th>
							<th data-options="field:'stitution'">目前病人情况</th>
							<th data-options="field:'preparation'">术前讨论情况</th>							
							</tr>
						</thead>
					</table>
			</div>
		</div>
		<script type="text/javascript">
		//初始化页面
		$('#operlist').datagrid({
			onDblClickRow: function (rowIndex, rowData) {//加载之前获得数据权限类型
			   	    $('#operId').val(rowData.id);
			   	    $('#oper').dialog('close');
			}
		});



		/**
		 * 格式化生日
		 * @author  zpty
		 * @date 2015-6-19 9:25
		 * @version 1.0
		 */
		function formatDatebox(value) {
            if (value == null || value == '') {
                return '';
            }
            var dt;
            if (value instanceof Date) {
                dt = value;
            } else {

                dt = new Date(value);

            }

            return dt.format("yyyy-MM-dd"); //扩展的Date的format方法
        }
		</script>
	</body>
</html>