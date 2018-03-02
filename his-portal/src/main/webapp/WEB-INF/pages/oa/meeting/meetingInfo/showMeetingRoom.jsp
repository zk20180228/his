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
<title>会议室编辑</title>
</style>
</head>
<body style="margin: 0px; padding: 0px">
		<div class="easyui-panel" id="panelEast" data-options="iconCls:'icon-form',border:false,fit:true">
			<div style="padding:5px">
	    		<form id="editForm" method="post">
					<input type="hidden" id="id" name="oaMeetingInfo.id" value="${oaMeetingInfo.id }">
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
						<tr>
							<td class="honry-lable">所属院区：</td>
			    			<td class="honry-info">
								${oaMeetingInfo.areaCode }
			    			</td>
		    			</tr>
		    			<tr>
			    			 <td class="honry-lable">会议室编号：</td>
			    			<td class="honry-info">${oaMeetingInfo.meetCode }</td>
			    		</tr>
		    			<tr>
			    			 <td class="honry-lable">会议室名称：</td>
			    			<td class="honry-info">${oaMeetingInfo.meetName }</td>
			    		</tr>
		    			<tr>
			    			 <td class="honry-lable">会议室地点：</td>
			    			<td class="honry-info">${oaMeetingInfo.meetPlace }</td>
			    		</tr>
		    			<tr>
			    			 <td class="honry-lable">容纳人数：</td>
			    			<td class="honry-info">${oaMeetingInfo.meetNumber }</td>
			    		</tr>
		    			<tr>
			    			<td class="honry-lable">会议室状态：</td>
			    			<td class="honry-info">
								${oaMeetingInfo.meetState == 'Y' ? '正常' : '维修'  }
			    			</td>
			    		</tr>
		    			<tr>
			    			<td class="honry-lable">会议室类型：</td>
			    			<td class="honry-info">
								${oaMeetingInfo.meetType }
			    			</td>
			    		</tr>
		    			<tr>
			    			 <td class="honry-lable">会议室管理员：</td>
			    			<td class="honry-info">
								${oaMeetingInfo.meetAdmin }
			    			</td>
			    		</tr>
		    			<tr>
			    			 <td class="honry-lable">会议室设备情况：</td>
			    			<td class="honry-info">${oaMeetingInfo.meetEquipment }</td>
			    		</tr>
<!-- 		    			<tr> -->
<!-- 							<td class="honry-lable">是否有投影：</td> -->
<!-- 							<td> -->
<%-- 							<input class="easyui-combobox" style="width:95%" id="meetProjector" name="oaMeetingInfo.meetProjector" value="${oaMeetingInfo.meetProjector }" data-options="panelHeight:'60',valueField: 'value',textField: 'text',data:[{value: 'Y',text: '是'},{value: 'N',text: '否'}] "/> --%>
<!-- 							</td> -->
<!-- 						</tr> -->
<!-- 				    	<tr> -->
<!-- 							<td class="honry-lable">是否有音响：</td> -->
<!-- 			    			<td class="honry-info"> -->
<%-- 							<input class="easyui-combobox" style="width:95%" id="meetSound" name="oaMeetingInfo.meetSound" value="${oaMeetingInfo.meetSound }" data-options="panelHeight:'60',valueField: 'value',textField: 'text',data:[{value: 'Y',text: '是'},{value: 'N',text: '否'}] "/> --%>
<!-- 							</td> -->
<!-- 		    			</tr> -->
		    			<tr>
							<td class="honry-lable">是否可申请 ：</td>
			    			<td class="honry-info">
								${oaMeetingInfo.meetIsapply  == 'Y' ? '是' : '否'  }
							</td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">联系方式 ：</td>
			    			<td class="honry-info">
								${oaMeetingInfo.meetPhone }
							</td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">会议室描述：</td>
			    			<td>
								${oaMeetingInfo.meetDescribe }
			    			</td>
		    			</tr>
		    	</table>
			    <div style="text-align:center;padding:5px">
<%-- 			     <c:if test="${frequency.id==null }"> --%>
<!-- 			    	<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a> -->
<%-- 			    </c:if> --%>
<!-- 			    	<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a> -->
<!-- 			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a> -->
			    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
			    <div id="roleWins"></div>
	    	</form>
	    </div>
	</div>
<%-- 	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script> --%>
	<style type="text/css">
		.window .panel-header .panel-tool .panel-tool-close{
  	  		background-color: red;
  	  	}
	</style>
	<script type="text/javascript">
	

		function hospitalName(map){
			var code=$('#hospitalCode').val();
			var noCode=$('#nohospitalCode').val();
			if(code){
				var codes=code.split(',');
				var len=codes.length;
				var name='';
				for(var a=0;a<len;a++){
					if(map.get(codes[a])){
						name=name+map.get(codes[a])+',';
					}
				}
				$('#hospital').val(name.substring(0, name.length-1));
			}
			if(noCode){
				var codes1=noCode.split(',');
				var len=codes1.length;
				var name1='';
				for(var i=0;i<len;i++){
					if(map.get(codes1[i])){
						name1=name1+map.get(codes1[i])+',';
					}
					
				}
				$('#nonHospital').val(name1.substring(0, name1.length-1));
			}
		}
		/**
		 * 页面加载
		 * @author  zxh
		 * @date 2015-5-22 10:53
		 * @version 1.0
		 */
		 var hospitalMap=new Map();

        /**
         * 关闭编辑窗口
         * @author  zxh
         * @date 2015-5-22 10:53
         * @version 1.0
         */
        function closeLayout(){
            $('#divLayout').layout('remove','east');
        }
	
		/**
		 * 打开用法界面弹框
		 * @author  zxh
		 * @date 2017-07-15
		 * @version 1.0
		 */
		
		function popWinToUseage(){
			
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?textId=useMode&type=useage";
			var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+(screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
					
		}
		
	</script>
<%-- 	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script> --%>
	</body>
</html>