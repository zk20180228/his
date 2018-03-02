<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

	<body>
		<div class="easyui-panel" id="panelEast" data-options="title:'行政区编码查看',iconCls:'icon-form',fit:true" >
			<div>
			<input type="hidden" id="id" name="id" value="${district.id }">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;margin-top:10px;">
				<tr>
					<td style="font-size: 14" class="honry-lable">城市代码  ：</td>
					<td style="font-size: 14" class="honry-view">
						${district.cityCode }&nbsp;
					 </td>
				    </tr>
				<tr>
					<td style="font-size: 14" class="honry-lable">城市名称  ：</td>
					<td style="font-size: 14" class="honry-view">
						${district.cityName }&nbsp;
					 </td>
				    </tr>
				<tr>
					<td  style="font-size: 14"class="honry-lable">城市简称  ：</td>
					<td  style="font-size: 14"class="honry-view">
						${district.shortname }&nbsp;
					 </td>
				    </tr>
				<tr>
					<td style="font-size: 14" class="honry-lable">城市英文名称   ：</td>
					<td  style="font-size: 14"class="honry-view">
						${district.ename }&nbsp;
					 </td>
				    </tr>
				<tr>
					<td style="font-size: 14" class="honry-lable">城市层级   ：</td>
					<td  style="font-size: 14"class="honry-view">
						${district.level }&nbsp;
					 </td>
				    </tr>
				<tr>
					<td style="font-size: 14" class="honry-lable">直辖市  ：</td>
					<td style="font-size: 14" class="honry-view" id="municpalityFlag" >
						${district.municpalityFlag }&nbsp;
					 </td>
				    </tr>
				<tr>
					<td  style="font-size: 14"class="honry-lable">自定义码  ：</td>
					<td style="font-size: 14" class="honry-view">
						${district.defined }&nbsp;
					 </td>
				    </tr>
				<tr>
					<td style="font-size: 14" class="honry-lable">拼音码  ：</td>
					<td style="font-size: 14" class="honry-view">
						${district.pinyin }&nbsp;
					 </td>
				    </tr>
				<tr>
					<td style="font-size: 14" class="honry-lable">五笔码  ：</td>
					<td style="font-size: 14" class="honry-view">
						${district.wb }&nbsp;
					 </td>
				    </tr>
				<tr>
					<td style="font-size: 14" class="honry-lable">有效标识  ：</td>
					<td style="font-size: 14" class="honry-view">
						${district.validFlag == 1 ? '有效' : '无效' }&nbsp;
					 </td>
				    </tr>
				<tr>
					<td style="font-size: 14" class="honry-lable">备注  ：</td>
					<td style="font-size: 14" class="honry-view">
						${district.remark }&nbsp;
					 </td>
				    </tr>
			</table>
			<div style="text-align:center;padding:5px">
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
		    </div>
		</div>
	</div>
	<script type="text/javascript">
			var flag="${district.municpalityFlag }";
		    if(flag==1){
		    	$('#municpalityFlag').text("非直辖市");
		    }else{
		    	$('#municpalityFlag').text("直辖市");
		    }
		    
		    function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
	 </script>
	</body>
</html>