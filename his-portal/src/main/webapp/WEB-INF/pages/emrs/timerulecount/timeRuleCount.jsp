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
<title>科室质控书写时限统计</title>
<script type="text/javascript">
var medicTypeMap = new Map();
	$(function(){
		$('#tt').tree({
			url:'<%=basePath%>emrs/emrSetCount/queryTimeRule.action',
			onDblClick:function(node){
				$('#list').datagrid('load',{patientNo:node.id});
			}
		});
		$('#list').datagrid({
			url:'<%=basePath%>emrs/emrSetCount/getTimeRuleCount.action'
		});
		$.ajax({
			url:"<%=basePath%>emrs/medcialRecord/getemrtypeCombox.action",
			success: function(data){
				for(var i=0;i<data.length;i++){
					medicTypeMap.put(data[i].encode,data[i].name);
				}
			}
		});
	});
	//超时渲染
	function formatterTimeOut(value,row,index){
		if(value==1){
			return "√";
		}
	}
	//剩余时间装换成时分秒
	function formatterLeastT(value,row,index){
		return formatSeconds(value);
	}
	//渲染病历分类
	function formtterType(value,row,index){
		return medicTypeMap.get(value);
	}
	//将秒装换成时分秒
	function formatSeconds(value) {
		var flag = true;//true 正数  false负数
	    var theTime = parseInt(value);// 秒
	    if(theTime<0){
	    	flag = false;
	    	theTime = -parseInt(value);
	    }
	    var theTime1 = 0;// 分
	    var theTime2 = 0;// 小时
	    if(theTime > 60) {
	        theTime1 = parseInt(theTime/60);
	        theTime = parseInt(theTime%60);
	            if(theTime1 > 60) {
	            theTime2 = parseInt(theTime1/60);
	            theTime1 = parseInt(theTime1%60);
	            }
	    }
	        var result = ""+parseInt(theTime)+"秒";
	        if(theTime1 > 0) {
	        result = ""+parseInt(theTime1)+"分"+result;
	        }
	        if(theTime2 > 0) {
	        result = ""+parseInt(theTime2)+"小时"+result;
	        }
	        if(!flag){
	        	result = "-"+result;
	        }
	    return result;
	}
</script>
<style type="text/css">
.panel-header{
	border-top:0
}
</style>
</head>
<body>
	<div id="cc" class="easyui-layout" style="width:100%;height:100%;">   
    <div data-options="region:'west',title:'出院患者'" style="width:200px;">
    	<ul id="tt"></ul>  
    </div>   
    <div data-options="region:'center',border:false" style="padding:5px;">
    	<table id="list" class="easyui-datagrid" style="width:auto;height:auto" data-options="fitColumns:true,singleSelect:true">   
		    <thead>   
		        <tr>   
		            <th data-options="field:'name',width:100,formatter:formtterType">项目名称</th>   
		            <th data-options="field:'startTime',width:100">起始时间</th>   
		            <th data-options="field:'endTime',width:100">结束时间</th>   
		            <th data-options="field:'lestTime',width:100,formatter:formatterLeastT">剩余时间</th>   
		            <th data-options="field:'lastTime',width:100">完成/记录时间</th>   
		            <th data-options="field:'timeOut',width:100,formatter:formatterTimeOut">超时</th>   
		        </tr>   
		    </thead>   
		</table>  
    </div>   
</div>  
</body>
</html>