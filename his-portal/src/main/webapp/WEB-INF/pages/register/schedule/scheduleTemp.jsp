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
	<div id="divLayout" class="easyui-layout" data-options="fit:true" style="border: 0px">
        <div data-options="region:'north',split:false" style="padding:5px;height:40px ">	        
			<table cellspacing="0" cellpadding="0" border="0" >
				<tr>
					<td style="padding: 0px 15px;">查询条件：</td>
					<td>
						<input class="easyui-textbox" id="tempSearchId"/>
					</td>
					<td>
						&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchTemp()" data-options="iconCls:'icon-search'">查询</a>
						&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="closeTemp()" data-options="iconCls:'icon-close'">关闭</a>
					</td>
				</tr>
			</table>
		</div>
		<div class="easyui-droppable targetarea"  data-options="region:'center',split:false,fit:true" style="padding:5px;">
			<div id="ttt" class="easyui-tabs" data-options="title:'挂号排班信息',tabHeight:40,fit:true">
			<input type="hidden" id="mdateTime"/>
				<c:forEach var="list" items="${dayXxList }" varStatus="status">
					<div title="${list.name }"   fit="true" style="padding:5px 10px 30px 10px ;">
						<form id="editFormSchedule" method="post" style=" width: 100%;height: 100%"> 
							<c:if test="${status.index==0}">
								<input type="hidden" id="dateTimeInit" value="${dayXxList[0].id }"/>
							</c:if>
							 <table id="${list.id }" style="width: 100%;" fit="true"></table> 
							<!-- <table id="list"></table> -->
						</form>
					</div>
				</c:forEach>	
			</div>				
		</div>	
		<div data-options="region:'center',split:false" style="padding:10px;">
			<input type="hidden" id="tempDeptId" value="${deptId }"/>
			<input type="hidden" id="tempdateTimeId" value="${dateTime }" />
			
		</div>
	</div>
	<script type="text/javascript">
	var midDayMap;
	$(function(){
			$.ajax({
				url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=midday',
				type:'post',
				success: function(gradData) {
					midDayMap = gradData;
				}
			});	
			
			/* var deptId = $('#tempDeptId').val();
			var dateTime = $('#tempdateTimeId').val(); */
			var deptId = $('#tempDeptId').val()
			$('#mdateTime').val($('#dateTimeInit').val());
			var dateTime = $('#dateTimeInit').val();
			$('#tempSearchId').textbox({prompt:'医生名称,诊室名称'});
			var search = $('#tempSearchId').textbox('getText');
			
			
			setTimeout(function(){
				bindEnterEvent('tempSearchId',searchTemp,'easyui'); 
			},100);
			$('#ttt').tabs({
				  onSelect: function(title){
					var dateTimeAxq = title.split("<br>");
					var dateTimeArr = dateTimeAxq[0].split(/['年''月''日']/);
					var dateTime = dateTimeArr[0]+'-'+dateTimeArr[1]+'-'+dateTimeArr[2];
					$('#mdateTime').val(dateTime);
					loadModelDatagrid(deptId,dateTime,search);
				  }
			});
			
		});
		function loadModelDatagrid(deptId,dateTime,search){
			$('#1'+dateTime).datagrid({   
				striped:true,
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				fitColumns:false,
				pagination:true,
				rownumbers:true,
				pageSize:20,
		   		pageList:[20,30,50,100],
			    url: "<c:url value='/outpatient/scheduleModel/getSchedulemodel.action'/>",
			    queryParams:{deptId:deptId,dateTime:dateTime,search:search},
			    onLoadSuccess:function(){
			    	$('#1'+dateTime).datagrid('checkAll');
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
			    },
			    columns:[[    
			        {field:'ck',checkbox:true},    
			        {field:'modelClass',title:'模板类型',width:'100',formatter:
			        	function(value,row,index){
				        	if(value==1){
				        		return '挂号排班模板';
							}else if(value==2){
								return '工作排班模板';
							}else{
								return value;
							}
		        		}
			        },
			        {field:'doctorId',title:'员工',width:'100',formatter:
			        	function(value,row,index){
				        	if (row.modelDoctor&&row.modelDoctor!=null){
				        		return empMap[row.modelDoctor];
							} else {
								return value;
							}
		        		}
			        }, 
			        {field:'clinicId',title:'工作诊室',width:'100',formatter:
			        	function(value,row,index){
				        	if (row.clinic&&row.clinic!=null){
				        		return cliMap[row.clinic];
							} else {
								return value;
							}
		        		}
			        },  
			        {field:'modelMidday',title:'午别',width:'100',formatter: 
			        	function(value,row,index){
				        	for(var i=0;i<midDayMap.length;i++){
			        			if(value==midDayMap[i].encode){
					        		return midDayMap[i].name;
					        	}
			        		}
			        	}
			        },    
			        {field:'modelLimit',title:'挂号限额',width:'100'},    
			        {field:'modelPrelimit',title:'预约限额',width:'100'},    
			        {field:'modelPhonelimit',title:'电话限额',width:'100'},    
			        {field:'modelNetlimit',title:'网络限额',width:'100'},
			        {field:'modelSpeciallimit',title:'特诊限额',width:'100'}
			    ]],  
				toolbar: [{
					id: 'btnAddTemp',
                    text: '导入',
                    iconCls: 'icon-add',
                    handler: function () {
                    	var rows = $('#1'+dateTime).datagrid('getChecked');
                    	if (rows.length > 0) {//选中几行的话触发事件	 
                    		$.messager.confirm('确认', '确定要导入选中信息吗?', function(res){
                    			if (res){
                    				$.messager.progress({text:'导入中，请稍后...',modal:true});
                    				var ids = '';
									for(var i=0; i<rows.length; i++){
										if(ids!=''){
											ids += ',';
										}
										ids += rows[i].id;
									};
									$.ajax({
										url: "<c:url value='/outpatient/schedule/schedulAddTTemp.action'/>?id="+ids+"&deptId="+$('#deptId').val()+"&mdateTime="+$('#mdateTime').val()+"&dateTime="+$('#tempdateTimeId').val(),
										success: function(data) {
											$.messager.progress('close');
											var dataMap = eval("("+data+")");
											if(dataMap.resMsg=="error"){
												$.messager.alert('提示','导入模版失败,请联系管理员!'+"<br>该提示2秒后自动关闭！");
											}else if(dataMap.resMsg=="notExist"){
												$.messager.alert('提示','没有可导入的模板数据,请添加模板!'+"<br>该提示2秒后自动关闭！");
											}else if(dataMap.resMsg=="allSucc"){
												$.messager.alert('提示','导入模板信息成功,共计: '+dataMap.resSuc+' 条记录!'+"<br>该提示2秒后自动关闭！");
											}else if(dataMap.resMsg=="notImp"){
												$.messager.alert('提示','模板排班日程已全部存在！'+"<br>该提示2秒后自动关闭！");
											}else if(dataMap.resMsg=="partSucc"){
												$.messager.alert('提示','导入模板信息成功,共计导入: '+dataMap.resSuc+' 条记录,存在未导入记录: '+dataMap.retFai+' 条!'+"<br>该提示2秒后自动关闭！");
											}
											setTimeout(function(){
												$(".messager-body").window('close');  
											},3500);
											$('#divLayout').layout('remove','east');
											$('#'+$('#dateTime').val()).datagrid('reload');
											
										}
									});
                    			}
                    		});
                    	}else{
                    		$.messager.progress('close');
                    		$.messager.alert('提示',"请选择导入的信息！");
                    	}
                    }
                }, '-', {
                    id: 'btnReloadTemp',
                    text: '刷新',
                    iconCls: 'icon-reload',
                    handler: function () {
                    	$('#1'+dateTime).datagrid('reload');
                    }
                }]
			});
		}
		function closeTemp(){
			$('#divLayout').layout('remove','east');
		}
		function searchTemp(){
			var deptId=$('#tempDeptId').val();
			var dateTime = $('#mdateTime').val();
			$('#mdateTime').val(dateTime);
			var search=$('#tempSearchId').textbox('getText');
			loadModelDatagrid(deptId,dateTime,search);
		   	/* $('#1'+dateTime).datagrid('load', {
		   		deptId:$('#tempDeptId').val(),
		   		dateTime:$('#tempdateTimeId').val(),
		   		search:$('#tempSearchId').textbox('getText')
			}); */
		}
	</script>
</body>
</html>