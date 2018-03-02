<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%-- <%@ include file="/javascript/js/hisUtil.jsp"%> --%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div class="easyui-layout" style="width:100%;height:100%;" id="treeLayOut">
		<div data-options="region:'center'"  id="content">
				<div id="divLayout" class="easyui-layout" fit=true style="width: 100%; height: 100%; overflow: hidden;">
					<div style="padding: 5px 5px 0px 5px;">
						<form id="search" method="post">
							<table
								style="width: 100%; border: 1px solid #95b8e7; padding: 5px;">
								<tr>
									<td style="width: 320px;" nowrap="nowrap">
										关键字： <input class="easyui-textbox" name="queryName" id="queryName"  onkeydown="keyDown()"  style="width:220px" />
									</td>
									<td>
										&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									</td>
								</tr>
							</table>
						</form>
					</div>
				<div style="padding: 0px 5px 5px 5px;">
					<table id="list" style="width:100%" data-options="url:'queryBusinessHospitalBed.action?classNameTmp=${classNameTmp}',method:'get',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
						<thead>
							<tr>
			    				<th data-options="field:'bedName'" style="width:20%">床号</th>
			    				<th data-options="field:'name'" style="width:20%">床位等级</th>
			    				<th data-options="field:'bedState'" style="width:20%" formatter="bedState">床位状态 </th>
			    				<th data-options="field:'bedPhone'" style="width:20%">床位电话</th>
			    				<th data-options="field:'bedFee'" style="width:20%">费用</th>
							</tr>
						</thead>
					</table>
				</div>
				</div>
			</div>
</div>
</body>
	<script type="text/javascript">
	var classNameTmp = "${classNameTmp}";
	var textId= "${textId}";
	var textName= "${textName}";
	//加载页面
		$(function(){
			var winH=$("body").height();
			$('#p').height(winH-78-30-27-2);
			$('#treeDiv').height(winH-78-30-27-2);
			$('#list').height(winH-78-30-27-22);
			
			var id="${id}"; //存储数据ID
			//添加操作按钮
			$('#list').datagrid({
				singleSelect:true,
				pagination:true,pageList:[20,30,50,80,100],pageSize:20,
				onLoadSuccess: function (data) {//默认选中
				var rowData = data.rows;
				$.each(rowData, function (index, value) {
				  if(value.id == id){
				     $("#list").datagrid("checkRow", index);
				  }
			    });
			    },toolbar: ['-', {
	                  id: 'btnReload',
	                  text: '刷新',
	                  iconCls: 'icon-reload',
	                  handler: function () {
	                    //实现刷新
	                    $("#list").datagrid("reload");
	                  }
	             }],
	             onDblClickRow: function (rowIndex, rowData) {//双击查看
					//if(getIdUtil("#list").length!=0){
						var tmpId ="#"+textId;
						window.opener.$(tmpId).combobox('setValue',rowData.encode);
			    		window.close();
					//}
				}    
			});	
			bindEnterEvent('queryName',searchFrom,'easyui');
		});
		/*查询
		*多个条件组合成一个条件查询 
		*
		*
		*/
		function searchFrom(){ 
	   		var queryName = $('#queryName').val();
	   		var state;
	   		var flg = 0;
	   		if(queryName == "挂"  || queryName == "挂床"){
	   			queryName="1";
	   			flg = 1;
	   		}
	   		if(queryName == "包" || queryName == "包床"){
	   			queryName="2";
	   			flg = 1;
	   		}
	   		if(queryName == "假" || queryName == "假床"){
	   			queryNamg="3";
	   			flg = 1;
	   		}
	   		if(queryName == "占" || queryName == "用" || queryName == "占用"){
	   			queryName="4";
	   			flg = 1;
	   		}
	   		if(queryName == "隔" || queryName == "离" || queryName == "隔离"){
	   			queryName="5";
	   			flg = 1;
	   		}
	   		if(queryName == "污" || queryName == "染" || queryName == "污染"){
	   			queryName="6";
	   			flg = 1;
	   		}
	   		if(queryName == "空" || queryName == "闲" || queryName == "空闲"){
	   			queryName="7";
	   			flg = 1;
	   		}
	   		if(queryName == "关" || queryName == "闭" || queryName == "关闭"){
	   			queryName="8";
	   			flg = 1;
	   		}
	   		if(queryName == "床"){
	   			queryName = "1,2,3";
	   			flg = 1;
	   		} 
	   		$('#list').datagrid({
				url:'<%=basePath%>popWin/popWinBed/queryBusinessHospitalBed.action',
				method:'post',
				singleSelect:true,
				pagination:true,pageList:[20,30,50,80,100],pageSize:20,
				queryParams: {
					queryNameParam: queryName,
					classNameTmp: classNameTmp,
					flg : flg,
					state : state
				},
				onLoadSuccess: function (data) {//默认选中
				var rowData = data.rows;
				$.each(rowData, function (index, value) {
				  if(value.id == id){
				     $("#list").datagrid("checkRow", index);
				  }
			    });
			    },toolbar: ['-', {
	                  id: 'btnReload',
	                  text: '刷新',
	                  iconCls: 'icon-reload',
	                  handler: function () {
	                    //实现刷新
	                    $("#list").datagrid("reload");
	                  }
	             }] 
			});	
		}
		//回车键
		function keyDown(){  
			if (event.keyCode == 13){  //目前只支持IE
			    event.returnValue=false;  
			    event.cancel = true;  
			    searchFrom();  
			}  
		} 	
		
		function bedState(value,row,index){
			if (value == "1"){
				return "挂床";
			} 
			if (value == "2"){
				return "包床";
			} 
			if (value == "3"){
				return "假床";
			} 
			if (value == "4"){
				return "占用";
			} 
			if (value == "5"){
				return "隔离";
			} 
			if (value == "6"){
				return "污染";
			} 
			if (value == "7"){
				return "空闲";
			} 
			if (value == "8"){
				return "关闭";
			} else {
				return value;
			}
		}
	</script>
</html>
