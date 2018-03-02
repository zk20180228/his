<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>床位绑定费用维护查看页面</title>
</head>
<body>
<div class="easyui-panel" id = "panelEast" data-options="title:'病床费用',iconCls:'icon-form'" style="height: 100%;width: 100%">
	<form id="editForm" method="post">
		<input type="hidden" id="id" name="id" value="${financeFixedcharge.id}">	
	    <table class="honry-table" style="width:550px;height: 320px;border-left:0 ;align:center;margin-left:auto;margin-right:auto;" >
	     	<tr>
		    	<td align="right" style="font: 14px;width:152px;border-left:0" class="changeskinbg">床位等级：</td>　　
		    	<td id="bedLevel">${financeFixedcharge.chargeBedlevel }</td>
		    </tr>
		    <tr>
		    	<td  align="right" style="font: 14px;border-left:0" class="changeskinbg">项目名称：</td>
		    	<td >${financeFixedcharge.drugUndrug}</td>
		    </tr>
		    <tr>
		    	<td  align="right" style="font: 14px;border-left:0" class="changeskinbg">单价：</td>
		    	<td >${financeFixedcharge.chargeUnitprice}</td>
		    </tr>
		    <tr>
			    <td  align="right" style="font: 14px;border-left:0" class="changeskinbg">数量：</td>
			    <td >${financeFixedcharge.chargeAmount}</td>
		    </tr>
		     <tr>
		    	<td  align="right" style="font: 14px;border-left:0" class="changeskinbg"> 开始时间：</td>
		    	<td >${financeFixedcharge.chargeStarttime}</td>
		    </tr>
		    <tr>
		    	<td  align="right" style="font: 14px;border-left:0" class="changeskinbg">结束时间：</td>
		    	<td >${financeFixedcharge.chargeEndtime}</td>
		    </tr>
		     <tr>
		    	<td   style="font: 14px;width: 200px;padding-left: 180px;border-left:0" colspan="2">
		    		<input name="isabOutChildeen" id="isabOutChildeen"  type="checkbox" disabled >婴儿相关 
		    		<input id="isabOutTime" name="isabOutTime" type="checkbox" disabled > 时间相关
		    	</td>
		    </tr>
		    <tr>
			    <td  align="right" style="font: 14px;border-left:0" class="changeskinbg">状态：</td>
			    <td id="state"></td>
		    </tr>
	    </table>
	    <div style="text-align:center;padding:5px;margin-top: 20px;">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
	    </div>
	</form>
</div>
<script type="text/javascript">
var level="${financeFixedcharge.chargeBedlevel }";
$(function(){
	$.ajax({
		url:'<%=basePath%>baseinfo/financeFixedcharge/comboboxBedGrade.action',
		success:function(data){
			var leng=data.length;
			for(var i=0;i<leng;i++){
				if(data[i].encode==level){
					$('#bedLevel').text(data[i].name);
				}
			}
			
		}
	});
	var isabOutChildeen='${financeFixedcharge.chargeIsaboutchildren }';
	if(isabOutChildeen==1){
		$("#isabOutChildeen").attr("checked",'true'); 
	}
	var isabOutTime = '${financeFixedcharge.chargeIsabouttime }';
	if(isabOutTime==1){
		$("#isabOutTime").attr("checked",'true'); 
	}
	/**
	 * 是否有效 -- 1在用2停用3废弃
	 */
	 var chargeState='${financeFixedcharge.chargeState}';
	 if(chargeState=='1'){
		 $('#state').text("在用");
	 }else if(chargeState=='2'){
		 $('#state').text("停用");
	 }else if(chargeState=='3'){
		 $('#state').text("废弃");
	 }
});
  //关闭
  function closeLayout(){
	$('#divLayout').layout('remove','east');
  }
</script>
</body>
</html>