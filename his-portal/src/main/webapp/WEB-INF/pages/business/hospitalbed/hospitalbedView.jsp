<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div class="easyui-panel" data-options="title:'病床详细信息',iconCls:'icon-book'" style="width: 580px">
		<div style="padding: 10px">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr>
					<td class="honry-lable">
						床号:
					</td>
					<td class="honry-view">
						${hospitalbed.bedName }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						床位等级:
					</td>
					<td class="honry-view" >
						${hospitalbed.bedLevel }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						床位状态:
					</td>
					<td class="honry-view">
						${hospitalbed.bedState }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						床位编制:
					</td>
					<td class="honry-view">
						${hospitalbed.bedOrgan }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						床位电话:
					</td>
					<td>
						${hospitalbed.bedPhone }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						归属:
					</td>
					<td>
						${hospitalbed.bedBelong }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						费用:
					</td>
					<td class="honry-view">
						${hospitalbed.bedFee }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						当前病人编号:
					</td>
					<td class="honry-view">
						${hospitalbed.patientId }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						排序:
					</td>
					<td class="honry-view">
						${hospitalbed.bedOrder }&nbsp;
					</td>
				</tr>
			</table>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			</div>
		</div>
	</div>
	
	
	<script type="text/javascript">
	var bedLevelMap="";
	
	$(function(){
		//渲染床位等级
		$.ajax({
			url: "<c:url value='/baseinfo/hospitalbed/queryBedLevel.action'/>",
			type:'post',
			success: function(payData) {
				bedLevelMap = eval("("+payData+")");
			}
		});
	});
	
	
	//渲染床位等级
	function funBedLevel(value){
		if(value!=null&&value!=''){
			return bedLevelMap[value];
		}
	}
	
	
	</script>
</body>
</html>