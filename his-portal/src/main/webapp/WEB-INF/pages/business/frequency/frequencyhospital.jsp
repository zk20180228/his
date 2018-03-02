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
</head>
	<body>
		<div id="divLayout" class="easyui-layout" fit=true>
			<div data-options="region:'north',split:false,title:'已选医院       双击行进行删除',iconCls:'icon-book'" style="height: 50%;">
				<table id="listHospital" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarIdHospitals'">
					<thead>
						<tr>
							<th data-options="field:'code'"width="5%">系统编号</th>
							<th data-options="field:'name'"width="15%">医院名称</th>
							<th data-options="field:'brev'"width="8%">简称</th>
							<th data-options="field:'district'"width="7%">所在省市县</th>
							<th data-options="field:'description'"width="15%">描述</th>
							<th data-options="field:'trafficRoutes'"width="15%">交通路线</th>
							<th data-options="field:'address'"width="15%">详细地址</th>
							<th data-options="field:'level',formatter:formatLevel"width="6%">等级</th>
							<th data-options="field:'createDate'"width="7%">创建时间</th>
						</tr>
					</thead>
				</table>
			</div>
			<div  data-options="region:'center',split:false,title:'未选医院       双击行进行添加',iconCls:'icon-book'" style="height: 50%;">
				<table id="listHospitals" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">
					<thead>
						<tr>
							<th data-options="field:'code'"width="5%">系统编号</th>
							<th data-options="field:'name'"width="15%">医院名称</th>
							<th data-options="field:'brev'"width="8%">简称</th>
							<th data-options="field:'district'"width="7%">所在省市县</th>
							<th data-options="field:'description'"width="15%">描述</th>
							<th data-options="field:'trafficRoutes'"width="15%">交通路线</th>
							<th data-options="field:'address'"width="15%">详细地址</th>
							<th data-options="field:'level',formatter:formatLevel"width="6%">等级</th>
							<th data-options="field:'createDate'"width="7%">创建时间</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<div id="toolbarIdHospitals">
		<input type="hidden" id="hospitalCode"name="hospitalCode"value="${hospitalCode }">
		<input type="hidden" id="nohospitalCode"name="nohospitalCode"value="${nohospitalCode }">
		<input type="hidden" id="type" name="type"value="${type }">
			<a href="javascript:void(0)" onclick="addHospital()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">保存</a>
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
			<a href="javascript:closedialog();"  class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true">关闭</a>
		</div>
		<div id="toolbarIdHospital">
		</div>
		<script type="text/javascript">
		//加载页面
		var lvlList = "";
		/* var rentabList = ""; */
			$(function(){
				$.ajax({
					url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=hospitalGrade",
					type:'post',
					asyns:false,
					success: function(lvlData) {
						lvlList = lvlData
					}
				});	
				var id='${id}'; //存储数据ID
				var hospital=$('#hospitalCode').val();
				var nohospital=$('#nohospitalCode').val();
				//添加datagrid事件及分页     已选医院
				$('#listHospital').datagrid({
					url:'<%=basePath %>baseinfo/hospital/queryhospitalval.action',
					queryParams:{hospitalCode:hospital,nohospitalCode:nohospital,type:$('#type').val()},
					onDblClickRow :function (rowIndex, rowData){
		        	var rows = rowData;  
                   	if(rows.length<=0){
                   		$.messager.alert('提示信息','请选择信息！');
                   		setTimeout(function(){
    						$(".messager-body").window('close');
    					},3500);
                   		return;
                   	}
                  		$('#listHospitals').datagrid('appendRow',{
                  			code:rows.code,
                  			name:rows.name,
                  			brev:rows.brev,
                  			district:rows.district,
                  			description:rows.description,
                  			trafficRoutes:rows.trafficRoutes,
                  			address:rows.address,
                  			level:rows.level,
                  			rentability:rows.rentability,
                  			createDate:rows.createDate
				 	});
                  	 	$('#listHospital').datagrid('deleteRow',rowIndex); 	
		        }
				});
				//未选医院
				$('#listHospitals').datagrid({
					url:'<%=basePath %>baseinfo/hospital/queryhospitalval.action',
					queryParams:{hospitalCode:hospital,nohospitalCode:nohospital},
					pagination:true,
					pageSize:10,
					pageList:[10,20,30,50,80,100],
					onDblClickRow :function (rowIndex, rowData){
			        	var rows = rowData;  
                    	if(rows.length<=0){
                    		$.messager.alert('提示信息','请选择信息！');
                    		setTimeout(function(){
        						$(".messager-body").window('close');
        					},3500);
                    		return;
                    	}
                   		$('#listHospital').datagrid('appendRow',{
                   			code:rows.code,
                   			name:rows.name,
                   			brev:rows.brev,
                   			district:rows.district,
                   			description:rows.description,
                   			trafficRoutes:rows.trafficRoutes,
                   			address:rows.address,
                   			level:rows.level,
                   			rentability:rows.rentability,
                   			createDate:rows.createDate
					 	});
                   	 	$('#listHospitals').datagrid('deleteRow',rowIndex); 	
			        }
					});
				});
			function addHospital(){
				 //选中要添加的行
                var rows = $('#listHospital').datagrid('getRows');
		 		$.messager.confirm('确认', '确定要添加选中信息吗?', function(res){//提示是否添加
						if (res){
							var names = '';
							var codes = '';
							type=$('#type').val();
							for(var i=0; i<rows.length; i++){
								if(names!=''){
									names += ',';
								}
								names =names+ rows[i].name;
								if(codes!=''){
									codes+=',';
								}
								codes=codes+ rows[i].code;
							};//hospital
							if(type=='yes'){
								$('#hospitalCode').val(codes);
								$('#hospital').val(names);
							}else if(type=='no'){
								$('#nohospitalCode').val(codes);
								$('#nonHospital').val(names);
							}
							closedialog();
						}
                   });
			}
			function reload(){
				//实现刷新栏目中的数据
				$('#listHospital').datagrid('reload');
			}
			//等级显示
			function formatLevel(value,row,index){
				for(var i=0;i<lvlList.length;i++){
					if(value==lvlList[i].id){
						return lvlList[i].value;
					}
				}
			}	
			//销毁dialog
			function closedialog(){
				$('#roleWins').dialog('close'); 
			}
		</script>
	</body>
</html>