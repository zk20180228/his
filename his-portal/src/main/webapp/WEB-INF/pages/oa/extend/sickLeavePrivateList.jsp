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
		<div id="divLayout" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',split:false" style="height:40px;padding-top:5px;padding-left:5px;">
				<input type="radio" name="type" value="" onclick="onclickRadio('')">全部</input>
				<input type="radio" name="type" value="0" onclick="onclickRadio('0')" checked="checked">未销假</input>
				<input type="radio" name="type" value="1" onclick="onclickRadio('1')">销假中</input>
				<input type="radio" name="type" value="2" onclick="onclickRadio('2')">已销假</input>
				<input id="iupId" value="${id}" type="hidden">
				<input id="iupTopFlow" value="${topFlow}" type="hidden">
			</div>
			<div data-options="region:'center'">
				<table id="ed">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true" ></th>
							<th data-options="field:'code',width:100,halign:'center',align:'right'">编号</th>
							<th data-options="field:'name',width:300,halign:'center',align:'right'">标题</th>
							<th data-options="field:'startTime',width:250,halign:'center',align:'right'">请假申请时间</th>
							<th data-options="field:'endTime',width:250,halign:'center',align:'right'">请假通过时间</th>
							<th data-options="field:'state',width:90,halign:'center',align:'right',formatter:forStatus">状态</th>
							<th data-options="field:'operation',width:100,halign:'center',align:'right'">操作</th>
					</thead>
				</table>
			</div> 
		</div>
		<script type="text/javascript">
		$(function(){
			$('#ed').datagrid({  
	            fit:true,
	            rownumbers:true,
	            striped:true,
	            border:true,
	            checkOnSelect:true,
	            selectOnCheck:false,
	            singleSelect:true,
// 	            pagination:true,
// 	            pageNumber:1,
// 	            pageSize:20,
// 	            pageList:[20,30,50,100],
	            toolbar:'#toolbarId',
	            url: "<%=basePath%>oa/extend/queryLeaveComplete.action",
	            queryParams:{type:'0',topFlow:$('#iupTopFlow').val()},
	            onLoadSuccess: function(data){
	            	var rows = data.rows;
	            	//分页工具栏作用提示
					var pager = $(this).datagrid('getPager');
					var aArr = $(pager).find('a');
					var iArr = $(pager).find('input');
					$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
					for(var i=0;i<aArr.length;i++){
						$(aArr[i]).tooltip({
							content:toolArr[i],
							hideDelay:1
						});
						$(aArr[i]).tooltip('hide');
					}
					if(rows.length>0){
						for(var i=0;i<rows.length;i++){
							if("0" == rows[i].state){
								$(this).datagrid('updateRow',{
									index: $(this).datagrid('getRowIndex',rows[i]),
									row: {
										operation : '<a class="sickCls" onclick="sick(\''+rows[i].code+'\')" href="javascript:void(0)"></a>'
									}
								});
							}
						}
						$('.sickCls').linkbutton({text:'销假',plain:false,width:'50px',height:'20px'}); 
					}
	            }
	        }); 
// 			bindEnterEvent('search',searchList,'easyui');//回车键查询
		});
		
		function sick(code){
			attWindow(code,"<%=basePath%>activiti/operation/viewStartForm.action");
		}
		
		//以post方式打开窗口
		function attWindow(code,url){
			var id = $("#iupId").val();
			var url = url;
			var name = '查看';
			var width = 1050;
			var height = 450;
			var top = (window.screen.availHeight-30-height)/2;
			var left = (window.screen.availWidth-10-width)/2;
			if($("#winOpenFrom").length<=0){  
				var form = "<form id='winOpenFrom' action='"+url+"' method='post' target='"+name+"'>" +
						"<input type='hidden' id='winOpenFromInpId' name='id'/><input type='hidden' id='winOpenFromInpInsId' name='processInstanceId'/></form>";
				$("body").append(form);
			} 
			$('#winOpenFromInpId').val(id);
			$('#winOpenFromInpInsId').val("leaveCode_"+code);
			openWindow('about:blank',name,width,height,top,left);
			$('#winOpenFrom').prop('action',url);
			$("#winOpenFrom").submit();
		}
		
		//打开窗口
		function openWindow(url,name,width,height,top,left){
			window.open(url, name, 'height=' + height + ',innerHeight=' + height + ',width=' + width + ',innerWidth=' + width + ',top=' + top + ',left=' + left + ',status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no'); 
		}
		
		function forStatus(value, row, index){
			if("0" == value){
				return "未销假";
			}else if("1" == value){
				return "销假中";
			}else if("2" == value){
				return "已销假";
			}else{
				return "请假中";
			}
			
		}
		
		function onclickRadio(type){
			$('#ed').datagrid('load',{type:type,topFlow:$('#iupTopFlow').val()});
		}
		</script>
	</body>
</html>