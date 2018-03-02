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
	<div class="easyui-panel" id = "panelEast" data-options="title:'服务管理查看',iconCls:'icon-form',border:false,fit:true" style="width:580px">
		<div style="padding:10px">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
					<tr>
						<td class="honry-lable">服务编码:</td>
		    			<td class="honry-info">${serviceManagement.code }</td>
	    			</tr>
					<tr>
						<td class="honry-lable">服务名称:</td>
		    			<td class="honry-info">${serviceManagement.name }</td>
	    			</tr>
					<tr>
						<td class="honry-lable">服务类型:</td>
		    			<td id="type" class="honry-info"></td>
	    			</tr>
					<tr>
						<td class="honry-lable">IP:</td>
		    			<td class="honry-info">${serviceManagement.ip }</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">占用端口:</td>
		    			<td class="honry-info">${serviceManagement.port }</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">心跳频率:</td>
		    			<td class="honry-info">${serviceManagement.heartRate }</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">心跳频率单位:</td>
		    			<td id="hearUnit" class="honry-info"></td>
	    			</tr>
	    			<tr >
						<td class="honry-lable">最新心跳时间:</td>
						<td class="honry-info">${heartNewtime }</td>
					</tr>
	    			<tr>
						<td class="honry-lable">状态:</td>
		    			<td id="state" class="honry-info"></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">服务状态:</td>
		    			<td id="masterprePare" class="honry-info"></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">备注:</td>
		    			<td class="honry-info">${serviceManagement.remarks }</td>
	    			</tr>
				</table>
				<div style="text-align:center;padding:5px">
			    	<a href="javascript:void(0);closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >关闭</a>
			    </div>
		</div>
	</div>
<script type="text/javascript">
	var menuAlias="${menuAlias}";
	var typeMap={'0':'同步服务','1':'接口服务'};
	var heartUnitMap={'S':'秒','M':'分','H':'时','D':'天','W':'周'};
	var stateMap={'0':'正常','1':'停用'};
	var masterMap={'1':'主','2':'备'};//主备
	$(function(){
		var type='${serviceManagement.type }';//服务类型
		if(type=='0'||type!=null&&type!=''){
			$('#type').html(typeMap[type]);
		}
		var hearUnit='${serviceManagement.heartUnit }';//心跳频率单位
		if(hearUnit!=null&&hearUnit!=''){
			$('#hearUnit').html(heartUnitMap[hearUnit]);
		}
		var state='${serviceManagement.state }';
		if(state=='0'||state!=null&&state!=''){//状态
			$('#state').html(stateMap[state]);
		}
		var masterprePare='${serviceManagement.masterprePare }';
		if(masterprePare!=null&&masterprePare!=''){//主从
			$('#masterprePare').html(masterMap[masterprePare]);
		}
	})
   //关闭页面
	function closeLayout(){
		$('#divLayout').layout('remove','east');
	}
</script>
</body>