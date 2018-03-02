<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<title>项目变更</title>
</head>
<body style="margin: 0px; padding: 0px;">
		<div id="cc" class="easyui-layout informationChange" data-options="fit:true">   
		    <div data-options="region:'north',border:false" style="width: 100%;height: 60px;">
		    	<table style="padding: 5px 5px 5px 5px">
		    		<tr>
		    			<td>
		    				查询名称：<input id="querydrug" class="easyui-textbox"  style="width:155px;" data-options="prompt:'商品名称查询'">
		    				&nbsp;<input type="checkbox" id="queryundrug" onclick="javascript:onclickBoxtaijia()"/>忽略药品
		    			</td>
		    			<td width="20px;"></td>
		    			<td>
		    							
		    			<!--<input id="beginDate" style="width: 170px;" class="easyui-datetimebox">  -->
		    			<!-- <input id="endDate" value="${now }" style="width: 170px;" class="easyui-datetimebox"> -->
		    				查询时间：	<input id="beginDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="height:22px; width:175px;border: 1px solid #95b8e7;border-radius: 5px;"/> 到 <input id="endDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  value="${now }" style="height:22px; width:175px;border: 1px solid #95b8e7;border-radius: 5px;"/>
		    			</td>
		    			<td>
							&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search">查询</a>
			    		</td>
		    			<td>
							&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="print()" iconCls="icon-2012081511202">打印</a>
			    		</td>
						<td>
							&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportList()" iconCls="icon-down">导出</a>
						</td>
		    		</tr>
		    	</table>
		    </div>   
<!-- 		    <div data-options="region:'center',border:false" style="width: 100%;height: 50px;"> -->
<!-- 		    	<table style="padding: 5px 5px 5px 5px"> -->
<!-- 		    		<tr> -->
<!-- 		    			<td> -->
<!-- 		    				&nbsp;&nbsp;查询名称：<input id="querydrug" class="easyui-textbox" data-options="prompt:'商品名称查询'"> -->
<!-- 		    				&nbsp;<input type="checkbox" id="queryundrug" onclick="javascript:onclickBoxtaijia()"/>忽略药品 -->
<!-- 		    			</td> -->
<!-- 		    			<td width="70px;"></td> -->
<!-- 		    			<td> -->
<%-- 		    				查询时间：<input id="beginDate" style="width: 170px;" class="easyui-datetimebox"> 到 <input id="endDate" value="${now }" style="width: 170px;" class="easyui-datetimebox"> --%>
<!-- 		    			</td> -->
<!-- 		    		</tr> -->
<!-- 		    	</table> -->
<!-- 		    </div>    -->
		    <div data-options="region:'south',border:false" style="width: 100%;height: 93%">
		    	<table id="billSearchHzList" class="easyui-datagrid" style="padding:5px 5px 5px 5px;" data-options="url:'<%=basePath%>baseinfo/changeRecord/queryBusinessChangeRecord.action',singleSelect:'true',pagination:true,pageSize:20,pageList:[20,40,60,80,100],rownumbers:true,fit:true">
					<thead>
						<tr>
							<th data-options="field:'itemName',width:'12%',align:'center'">商品名称</th>
							<th data-options="field:'spec',width:'8%',align:'center'">规格</th>
							<th data-options="field:'newDataCode',width:'12%',align:'center'">变更字段名</th>
							<th data-options="field:'oldDataName',width:'12%',align:'center'">变更前数据</th>
							<th data-options="field:'newDataName',width:'12%',align:'center'">变更前数据</th>
							<th data-options="field:'changeCause',width:'12%',align:'center'">变更原因</th>
							<th data-options="field:'operCode',width:'8%',align:'center'">更改人</th>
							<th data-options="field:'operDate',width:'10%',align:'center'">更改时间</th>
						</tr>
					</thead>
				</table>
				<form id="saveForm" method="post"/>
		    </div>   
		</div> 
		<div id="hisMenu" class="easyui-menu" style="width:100px; display: none;">
			<div id="hisMenuBtn" data-options="iconCls:'icon-edit'" onclick="copyHis()">修改变更原因</div>
		</div>
		<div id="addMatKindinfo"></div>
		<script type="text/javascript">
		var changeCause="";
		var id = "";
			$(function(){
				//bindEnterEvent("querydrug",searchFrom,'easyui');//绑定回车事件
				$('#querydrug').textbox('textbox').bind('keyup', function(event) {
					searchFrom();
				});
				
				$('#beginDate').blur(function(){
						searchFrom();
				});
				$('#endDate').blur(function(){
						searchFrom();
				});
				
				$('#billSearchHzList').datagrid({
					onRowContextMenu:function(e,index,row){
						e.preventDefault();
						$('#hisMenu').menu('show', {//显示右键菜单
			                left: e.pageX,//在鼠标点击处显示菜单
			                top: e.pageY
			            });
						changeCause = row.changeCause;
						id = row.id;
					},
					onLoadSuccess: function(data){
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
					}
				});
			})
			function copyHis(){
				Adddilogs("修改变更原因","<c:url value='/baseinfo/changeRecord/updateMark.action'/>?changeCause="+encodeURIComponent(encodeURIComponent(changeCause))+"&id="+id);
			}
			function Adddilogs(title, url) {
				$('#addMatKindinfo').dialog({    
				    title: title,    
				    width: '15%',    
				    height:'20%',    
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true   
				});    
			}
			function searchFrom(){
				var itemName = $('#querydrug').textbox('getText');
				var beginDate = $('#beginDate').val();
				var endDate = $('#endDate').val();/* datetimebox('getValue') */
				var neglect = $('#queryundrug').val();
				if(neglect=='on'){
					var neglect ='';
				}
				$('#billSearchHzList').datagrid('load',{
					itemName: itemName,
					beginDate:beginDate,
					engDate:endDate,
					neglect:neglect
				});
			}
			//复选按钮赋值抬价
			function onclickBoxtaijia(){
				if($('#queryundrug').is(':checked')){
					$('#queryundrug').val(1);
					searchFrom()
				}else{
					$('#queryundrug').val("");
					searchFrom()
			   }
			}
			//导出列表
			function exportList() {
				var itemName = $('#querydrug').textbox('getValue');
				var beginDate = $('#beginDate').val();/*datetimebox('getValue');  */
				var endDate = $('#endDate').val();/*datetimebox('getValue')  */
				var neglect = $('#queryundrug').val();
				if(neglect=='on'){
					var neglect ='';
				}
				$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
					if (res) {
						$('#saveForm').form('submit', {
							url :"<%=basePath%>baseinfo/changeRecord/expChangeRecordList.action",
							queryParams:{itemName:itemName,beginDate:beginDate,endDate:endDate,neglect:neglect},
							onSubmit : function() {
								return $(this).form('validate');
							},
							success : function(data) {
								$.messager.alert("操作提示", "导出成功！", "success");
							},
							error : function(data) {
								$.messager.alert("操作提示", "导出失败！", "error");
							}
						});
					}
				});
			}
			function print(){
				var itemName = $('#querydrug').textbox('getValue');
				var beginDate = $('#beginDate').val();/* datetimebox('getValue'); */
				var endDate = $('#endDate').val();/*datetimebox('getValue')  */
				var neglect = $('#queryundrug').val();
				if(neglect=='on'){
					var neglect ='';
				}
				var timerStr = Math.random();
				window.open ("<c:url value='/iReport/iReportPrint/iReportInformationaction.action?randomId='/>"+timerStr+"&itemName="+encodeURIComponent(encodeURIComponent(itemName))+"&beginDate="+beginDate+"&endDate="+endDate+"&neglect="+neglect+"&fileName=XMBG",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
			}
		</script>
</body>
</html>