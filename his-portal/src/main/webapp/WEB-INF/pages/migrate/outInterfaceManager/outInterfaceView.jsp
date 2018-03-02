<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

</head>
<body>
	<div class="easyui-panel" id = "panelEast" data-options="title:'接口预览',iconCls:'icon-form',fit:true,border:false" >
		<div style="width: 100%;height: 98%">
				<table class="honry-table" cellpadding="1" cellspacing="1" data-options="fit:true"  style="margin-left:auto;margin-right:auto;margin-top:10px;">
	    			<tr>
		    			 <td class="honry-lable">接口代码:</td>
		    			<td class="honry-info">${exterInter.code}</td>
		    		</tr>
					<tr>
						<td class="honry-lable">接口名称:</td>
		    			<td class="honry-info">${exterInter.name }</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">接口服务:</td>
		    			<td class="honry-info" >${exterInter.serve }</td>
	    			</tr>
	    			
					<tr>
						<td class="honry-lable">服务名称:</td>
		    			<td class="honry-info" id="serveCode"></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">参数字段:</td>
		    			<td class="honry-info">${exterInter.parameterField }</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">厂商编号:</td>
		    			<td class="honry-info" id="firmCode"></td>
	    			</tr>
	    			<tr>
		    			<td class="honry-lable">当前方式:</td>
						<td class="honry-info">${exterInter.curWay }</td>
					</tr>
					<tr>					
						<td class="honry-lable">接口读写:</td>
		    			<td id="rwJuri"></td>
	    			</tr>
	    			<tr>
		    			<td class="honry-lable">接口调用间隔:</td>
						<td>${exterInter.callSapce }</td>
	    			</tr>
	    			<tr>					
						<td class="honry-lable">间隔单位:</td>
		    			<td class="honry-info" id="callUnit"></td>
	    			</tr>
	    			<tr>
		    			<td class="honry-lable">频次:</td>
						<td>${exterInter.frequency }</td>
	    			</tr>
	    			<tr>
		    			<td class="honry-lable">是否安全认证:</td>
						<td class="honry-info" id="isAuth"></td>
					</tr>
					<tr>
						<td class="honry-lable">认证有效期:</td>
		    			<td>${exterInter.authVali }</td>
					</tr>
					<tr>
						<td class="honry-lable">认证有效期单位:</td>
		    			<td id="authUnit"></td>
					</tr>
					<tr>
						<td class="honry-lable">有效期开始时间:</td>
		    			<td>${exterInter.authStimeSDF }</td>
					</tr>
					<tr>
						<td class="honry-lable">有效期结束时间:</td>
		    			<td>${exterInter.authEtimeSDF }</td>
					</tr>
					<tr>
						<td class="honry-lable">状态:</td>
		    			<td id="state"></td>
					</tr>
					<tr>
						<td class="honry-lable">执行SQL:</td>
		    			<td>
		    				<div style="width:100%;height:60px;">
		    					<textarea style="width:100%;height:60px;" readonly="readonly">
		    						${exterInter.implementSql }
		    					</textarea>
		    				</div>
		    			</td>
					</tr>
					<tr>
						<td class="honry-lable">备注:</td>
		    			<td>
			    			<div style="width:100%;height:60px;">
								<textarea style="width:100%;height:60px;" readonly="readonly">${exterInter.remarks }</textarea>
		    				</div>
		    			</td>
					</tr>
	    		</table>
		    <div style="text-align:center;padding:5px">
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
		    </div>
    </div>
</div>
<script type="text/javascript">
var menuAlias="${menuAlias}";
var timeMap={'S':'秒','M':'分','H':'时','D':'天','W':'周'};
var rwJuriMap={'0':'只读','1':'只写','2':'读写'};
var isAuthMap={'0':'是','1':'否'};
var stateMap={'0':'正常','1':'停用'};
var serverMap;//服务渲染
var firedMap;//厂商渲染
$(function(){
	//服务
	$.ajax({
		url: "<%=basePath%>migrate/outInterfaceManager/renderServer.action",		
	type:'post',
	async:false,
	success: function(date) {					
		serverMap= date;	
	}
	});
	//厂商
	$.ajax({
		url: "<%=basePath%>migrate/outInterfaceManager/renderFired.action",		
		type:'post',
		async:false,
		success:function(date) {					
			firedMap= date;	
		}
	});
	var serve='${exterInter.serveCode }';//服务名称
	if(serve=='0'||serve!=null&&serve!=''){
		$('#serveCode').html(serverMap[serve]);
	}
	var firmCode='${exterInter.firmCode }';//厂商
	if(firmCode=='0'||firmCode!=null&&firmCode!=''){
		$('#firmCode').html(firedMap[firmCode]);
	}
	
	var rwJuri='${exterInter.rwJuri }';//读写
	if(rwJuri=='0'||rwJuri!=null&&rwJuri!=''){
		$('#rwJuri').html(rwJuriMap[rwJuri]);
	}
	var callUnit='${exterInter.callUnit }';//间隔单位
	if(callUnit!=null&&callUnit!=''){
		$('#callUnit').html(timeMap[callUnit]);
	}
	
	var authUnit='${exterInter.authUnit }';//认证有效期
	if(authUnit!=null&&authUnit!=''){
		$('#authUnit').html(timeMap[authUnit]);
	}
	var isAuth='${exterInter.isAuth }';//是否安全认证
	if(isAuth=='0'||isAuth!=null&&isAuth!=''){
		$('#isAuth').html(isAuthMap[isAuth]);
	}
	
	var state='${exterInter.state }';//是否安全认证
	if(state=='0'||state!=null&&state!=''){
		$('#state').html(stateMap[state]);
	}
	
});
function closeLayout(){
	$('#divLayout').layout('remove','east');
}
</body>
</script>
</html>