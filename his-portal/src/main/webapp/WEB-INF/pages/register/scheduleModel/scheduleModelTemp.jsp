<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	String deptId = request.getAttribute("deptId")==null?"":request.getAttribute("deptId").toString();
// 	String week = request.getAttribute("week")==null?"":request.getAttribute("week").toString();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript">
	
</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout" data-options="fit:true">
        <div data-options="region:'north',split:false" style="padding:5px;height: 40px">	        
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td style="padding: 5px 15px;">查询条件：</td>
					<td>
						<input class="easyui-textbox" id="tempSearchId1" placeholder="医生名称,诊室名称"/>
					</td>
					<td>
						&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchTempModel()" data-options="iconCls:'icon-search'">查询</a>
						&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="closeTemp()" data-options="iconCls:'icon-close'">关闭</a>
					</td>
				</tr>
			</table>
		</div>	
		<div data-options="region:'center',split:false,fit:true" style="padding:10px 10px 30px 10px ;">
			<div id="tt1" class="easyui-tabs" data-options="fit:true">
						<input type="hidden" id="inpDeptId1"/>	
						<input type="hidden" id="inpWeekId1" value="1"/>	
						<input type="hidden" id="inpWeekEnId1" value="Monday"/>	
						<c:if test="${weeken!='1' }" >
							<div title="星期一" style="padding: 5px 5px 5px 5px;">
								<table id="Monday1" style="width: 100%;"></table> 
							</div>
						</c:if>	
						<c:if  test="${weeken!='2' }">
							<div title="星期二" style="padding: 5px 5px 5px 5px;">
								<table id="Tuesday1" style="width: 100%;"></table> 
							</div>
						</c:if>
						<c:if test="${weeken!='3' }">
							<div title="星期三" style="padding: 5px 5px 5px 5px;">
								<table id="Wednesday1" style="width: 100%;"></table> 
							</div>
						</c:if>	
						<c:if test="${weeken!='4' }">
							<div title="星期四" style="padding: 5px 5px 5px 5px;">
								<table id="Thursday1" style="width: 100%;"></table> 
							</div>
						</c:if>	
						<c:if test="${weeken!='5' }">
							<div title="星期五" style="padding: 5px 5px 5px 5px;">
								<table id="Friday1" style="width: 100%;"></table>
							</div>
						</c:if>	
						<c:if test="${weeken!='6' }">
							<div title="星期六" style="padding: 5px 5px 5px 5px;">
								<table id="Saturday1" style="width: 100%;"></table> 
							</div>
						</c:if>	
						<c:if test="${weeken!='7' }">
							<div title="星期日" style="padding: 5px 5px 5px 5px;">
								<table id="Sunday1" style="width: 100%;"></table> 
							</div>
						</c:if>
			</div>	
		</div>
	</div>
	
	
	<script type="text/javascript">
	var deptId1 = '<%=deptId%>';
	$('#inpDeptId1').val(deptId1);
	var midDayMap=new Map();
	//加载页面
	$(function(){
		$.ajax({
		    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=midday",
			type:'post',
			success: function(data) {
				var type = data;
				for(var i=0;i<type.length;i++){
					midDayMap.put(type[i].encode,type[i].name);
				}
			}
		});
		var sq = "Monday1";
		var week1 = 1;
		$('#tt1').tabs({
		  onSelect: function(title){
		  	$('#infoJson').val("");
		  	$('#name').val("");
			if(title=="星期一"){
				sq = "Monday1";
				week1 = 1;
				$('#inpWeekId1').val(1);
			}else if(title=="星期二"){
				sq = "Tuesday1";
				week1 = 2;
				$('#inpWeekId1').val(2);
			}else if(title=="星期三"){
				sq = "Wednesday1";
				week1 = 3;
				$('#inpWeekId1').val(3);
			}else if(title=="星期四"){
				sq = "Thursday1";
				week1 = 4;
				$('#inpWeekId1').val(4);
			}else if(title=="星期五"){
				sq = "Friday1";
				week1 = 5;
				$('#inpWeekId1').val(5);
			}else if(title=="星期六"){
				sq = "Saturday1";
				week1 = 6;
				$('#inpWeekId1').val(6);
			}else if(title=="星期日"){
				sq = "Sunday1";
				week1 = 7;
				$('#inpWeekId1').val(7);
			}
			$('#inpWeekEnId1').val(sq);
			loadDatagrid1(deptId1,sq,week1);
		  }
		});
		loadDatagrid1(deptId1,sq,week1);
		bindEnterEvent('tempSearchId1',searchTempModel);
	});
	
	//加载可编辑表格
	function loadDatagrid1(deptId,sq,week1){
		$('#'+sq).datagrid({   
			striped:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			pagination:true,
			rownumbers:true,
			fit:true,
			pageSize:20,
	   		pageList:[20,30,50,100],
		    url: "<%=basePath%>outpatient/scheduleModel/querySchedulemodel.action",
		    queryParams:{deptId:deptId,week:week1,search:''},
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
		        {field:'modelWorkdept',title:'工作科室',width:'150',formatter:
		        	function(value,row,index){
			        	if (row.modelWorkdept&&row.modelWorkdept!=null){
			        		return deptMap[row.modelWorkdept];
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
			        	if(value!=null&&value!=''){
			        		return midDayMap.get(value);
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
				id: 'btnAddTemp1',
                text: '导入',
                iconCls: 'icon-add',
                handler: function () {
                	var pp = $('#tt').tabs('getSelected'); 
        			var tabTitle = pp.panel('options').title;
        			var weeken =0;
        			if(tabTitle=="星期一"){
    					weeken = 1;
    				}else if(tabTitle=="星期二"){
    					weeken = 2;
    				}else if(tabTitle=="星期三"){
    					weeken = 3;
    				}else if(tabTitle=="星期四"){
    					weeken = 4;
    				}else if(tabTitle=="星期五"){
    					weeken = 5;
    				}else if(tabTitle=="星期六"){
    					weeken = 6;
    				}else if(tabTitle=="星期日"){
    					weeken = 7;
    				}
                	var rows = $('#'+sq).datagrid('getChecked');
                	if (rows.length > 0) {//选中几行的话触发事件	 
                		$.messager.confirm('确认', '确定要导入选中信息吗?', function(res){
                			if (res){
                				var ids = '';
								for(var i=0; i<rows.length; i++){
									if(ids!=''){
										ids += ',';
									}
									ids += rows[i].id;
								};
								$.messager.progress({text:'导入中，请稍后...',modal:true});
									
								$.ajax({
									url:"<%=basePath%>outpatient/scheduleModel/schedulModelAddTemp.action?id="+ids+"&deptId="+deptId+"&nowWeek="+weeken+"&week="+$('#inpWeekId1').val(),
									type:'get',
									success: function(data) {
										$.messager.progress('close');
										var dataMap = data;
										if(dataMap.resMsg=="error"){
											$.messager.alert('提示','导入模版失败,请联系管理员!'+"<br>该提示2秒后自动关闭！");
										}else if(dataMap.resMsg=="notExist"){
											$.messager.alert('提示','没有可导入的模板数据,请添加模板!'+"<br>该提示2秒后自动关闭！");
										}else if(dataMap.resMsg=="allSucc"){
											$.messager.alert('提示','导入模板信息成功,共计: '+dataMap.resSuc+' 条记录!'+"<br>该提示2秒后自动关闭！");
										}else if(dataMap.resMsg=="notImp"){
											$.messager.alert('提示','模板排班日程已全部存在！');
										}else if(dataMap.resMsg=="partSucc"){
											$.messager.alert('提示','导入模板信息成功,共计导入: '+dataMap.resSuc+' 条记录,存在未导入记录: '+dataMap.retFai+' 条!'+"<br>该提示2秒后自动关闭！");
										}
										setTimeout(function(){
											$(".messager-body").window('close');  
										},3500);
										$('#divLayout').layout('remove','east');
										var w = $('#inpWeekEnId').val();
										$('#'+w).datagrid('reload');
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
                id: 'btnReloadTemp1',
                text: '刷新',
                iconCls: 'icon-reload',
                handler: function () {
                	$('#'+sq).datagrid('reload');
                }
            }]
		});
	}
	//关闭
	function closeTemp(){
		$('#divLayout').layout('remove','east');
	}
	
	//查询
	function searchTempModel(){
		var deptId = deptId1;
	   	if(deptId==null||deptId==""){
	   		$.messager.alert("提示","请选择具体科室！");
	   		return;
	   	}
	   	var datagridId = $('#inpWeekEnId1').val();
	   	var w = $('#inpWeekEnId1').val();
	   	$('#'+w).datagrid('load', {
	   		deptId:deptId,
	   		week:$('#inpWeekId1').val(),
			search: $('#tempSearchId1').val()
		});
	}	
	</script>
		                       
</body>
</html>