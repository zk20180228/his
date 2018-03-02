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
    <title>My JSP 'selectDept.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
    <div class="easyui-panel" data-options="title:'选择科室'">
		<table cellspacing="0" cellpadding="0" border="0" align="center">
			<tr>
				<td style="padding: 5px 15px;">
					入库类型：
				</td>
				<td>
					<input type="text" ID="CodeInstoretype" />
				</td>
				<td>
					<a href="javascript:forwardList();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确定</a>
				</td>
			</tr>
		</table>	
	</div>
	<script type="text/javascript">
		$(function(){
				//初始化下拉框
			idCombobox("CodeInstoretype");
		});
		 //从xml文件中解析，读到下拉框
		function idCombobox(param){
			$('#'+param).combobox({
			    url:"<c:url value='/comboBox.action'/>?str="+param,    
			    valueField:'id',    
			    textField:'name',
			    multiple:false
			});
			//下拉框的keydown事件   调用弹出窗口
			var instoretypedown = $('#'+param).combobox('textbox'); 
			instoretypedown.keyup(function(){
				KeyDown(0,"CodeInstoretype");
			});
		}
		//入库类型弹出
		function KeyDown(flg,tag){ 	    
	    	if(flg==1){//回车键光标移动到下一个输入框
		    	if(event.keyCode==13){	
		    		event.keyCode=9;
		    	}
		    } 
		    if(flg==0){	//空格键打开弹出窗口
			    if (event.keyCode == 32)  
			    { 
			        event.returnValue=false;  
			        event.cancel = true; 
			        if(tag=="CodeInstoretype"){
			        	showWin("药品入库类型","<c:url value='/ComboxOut.action'/>?xml="+"CodeInstoretype,0","50%","80%");
			        }
			    }
		    }
		} 
		function forwardList(){
			var value = $('#CodeInstoretype').combobox('getText');
<%--			if(value=="一般入库"){--%>
<%--				window.location.href("<%=path%>/instoreList.action");--%>
<%--			}else if(value == "发票入库"){--%>
<%--				window.location.href("<%=path%>/WarehouseReceiptList.action");--%>
<%--			}else if(value == "内部入库"){--%>
<%--				window.location.href("<%=path%>/applyoutList.action");--%>
<%--			}--%>
			$('#selectDept').dialog('close'); 
		}
	</script>
  </body>
</html>
