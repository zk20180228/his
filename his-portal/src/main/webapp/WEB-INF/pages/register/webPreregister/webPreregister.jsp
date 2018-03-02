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
<title>网络预约挂号</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<style type="text/css">
.panel-header{
	border-top:0;
}
#panelEast{
	border-left:0;
}
</style>
</head>
<body style="margin: 0px;padding: 0px;">
<div class="easyui-layout" data-options="fit:true">
			<div id="p" data-options="region:'west',split:true,title:'科室信息',tools:'#toolSMId',border:true" style="width:18%;overflow: hidden;">
				<div style="padding-top: 3px"><input id="deptSearch" data-options="prompt:'科室名称'" />
				<a href="javascript:clearComboboxValue('deptSearch');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
				</div>
				<div id="toolSMId">
					<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
					<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
					<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
				</div>
				<div style="width: 100%;height: 100%; overflow-y: auto;"><ul id="tDt">数据加载中...</ul></div>
				  
			</div>
			<div data-options="region:'center',border:false" style="width: 82%;height: 100%;">
				<div id="divLayout" class="easyui-layout" data-options="fit:true">
			        <div data-options="region:'north',split:false" style="width:100%;height:45px;border-top:0">	        
						<table cellspacing="0" cellpadding="0" border="0" style="width: 100%;">
							<tr>
								<td style="padding: 8px 5px;">查询条件：
									<input id="sName" class="easyui-textbox" data-options="prompt:'医生名称,诊室名称'"/>
									<a href="javascript:clearComboboxValue1('sName');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>&nbsp;&nbsp;
									<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" data-options="iconCls:'icon-search'">查询</a>
								</td>
							</tr>
						</table>
					</div>			
					<div class="easyui-droppable targetarea" data-options="region:'center',split:false,border:false" style="width:100%;">
						<div id="tt" class="easyui-tabs" data-options="tabHeight:40,fit:true">
							<input type="hidden" id="deptId"/>
							<input type="hidden" id="deptName"/>
							<input type="hidden" id="rq"/>
							<c:forEach var="list" items="${dayXxList }" varStatus="status">
								<div title="${list.name }" data-options="fit:true" >
									<c:if test="${status.index==0}">
										<input type="hidden" id="rqInit" value="${dayXxList[0].id }"/>
									</c:if>
									<table id="${list.id }" style="width: 100%;" data-options="toolbar:'#toolbarId'"></table>
								</div>
							</c:forEach>
						</div>				
					</div>
				</div>
		</div> 
	</div>
	<div id="toolbarId" data-options="border:false">
		<a id="btnAdd" onclick="btnAdd()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">预约</a> 
		<a id="btnReload" onclick="btnReload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">刷新</a>
		<span style='color:red;font-size:small;' class="webPreregisterFont">底色为红色代表该医生已挂满</span>
	</div>
	
	
</body>
<script type="text/javascript">

	var midDayMap='';
	var empMap = null;
	var deptMap = null;
	var gradeMap="";
	var rq = "";
	var row_data="";
	var deptName="";
$(function(){


	$.ajax({//午别
		url:'<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=midday',
		type:'post',
		success: function(gradData) {
			midDayMap = gradData;
		}
	});
	
	$.ajax({//挂号级别
		url: "<%=basePath%>outpatient/grade/getGradeMap.action",
		type:'post',
		success: function(data) {
			gradeMap = data;
		}
	});
	
	$('#rq').val($('#rqInit').val());
	rq = $('#rqInit').val();
	//给选项卡添加选中事件
	$('#tt').tabs({
	  onSelect: function(title,index){
		 var rqAxq = title.split("<br>");
		 rq = rqAxq[0];
		 rq=rq.substring(0,4)+"-"+rq.substring(5,7)+"-"+rq.substring(8,10);//时间格式是yyyy-MM-dd
		 $('#rq').val(rq);
		 loadDatagrid(getSelected(),rq);
	  }
	});
	$('#btnTb').linkbutton('disable');
	loadDatagrid(getSelected(),rq);
	bindEnterEvent('sName',searchFrom,'easyui');
	bindEnterEvent('deptSearch',deptSearch,'easyui');
	
	$.ajax({
		url: "<c:url value='/outpatient/scheduleModel/queryEmpCodeAndNameMap.action'/>",
		type:'post',
		success: function(empData) {
			empMap = empData;
		}
	});	
	$.ajax({
		url: "<c:url value='/outpatient/scheduleModel/querydeptCodeAndNameMap.action'/>",
		type:'post',
		success: function(deptData) {
			deptMap = deptData;
		}
	});	
	
	
});

//科室回车查询
function deptSearch(){
			var nodeCode=$("#deptSearch").combobox("getValue");
			selectNode(nodeCode);
}

//加载可编辑表格
function loadDatagrid(deptId,rq){
	$('#'+rq).datagrid({ 
		striped:true,
		checkOnSelect:true,
		selectOnCheck:false,
		singleSelect:true,
		fitColumns:false,
		pagination:true,
		rownumbers:true,
		fit:true,
		border:false,
		pageSize:20,
   		pageList:[20,30,50,100],
   		url: "<c:url value='/outpatient/webPreregister/getList.action'/>",
	    queryParams:{deptId:deptId,rq:rq,search:$('#sName').val()},
	    columns:[[    
	        {field:'scheduleDoctorname',title:'专家名称',width:'150',formatter:
	        	function(value,row,index){
	        	
	        	if(typeof(value)=='undefined'&& row.doctor){
	        		return empMap[row.doctor];
	        	}else{
	        		return value;
	        	}
	        }
	        }, 
	        {field:'reggrade',title:'专家级别',width:'150',formatter:
	            function(value,row,index){
	        	if(row.reggrade){
	        		return gradeMap[row.reggrade];
	        	}else{
	        		return value;
	        	}
	        }	
	        },
	        {field:'scheduleWorkdept',title:'科室',width:'150',formatter:
	        	function(value,row,index){
		        	if (row.scheduleWorkdept&&row.scheduleWorkdept!=null){
		        		return deptMap[row.scheduleWorkdept];
					} else {
						return value;
					}
        		}
	        },  
	        {field:'midday',title:'午别',width:'150',formatter: 
	        	function(value,row,index){
		        	for(var i=0;i<midDayMap.length;i++){
	        			if(value==midDayMap[i].encode){
			        		return midDayMap[i].name;
			        	}
	        		}
	        	}
	        },    
	        {field:'startTime',title:'开始时间',hidden:true},
	        {field:'endTime',title:'结束时间',hidden:true},
	        {field:'netLimit',title:'预约剩余',width:'150'},   
	    ]],
	    onLoadSuccess : function(data){
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
	    rowStyler: function(index,row){
			 if(row.netLimit==0){
				 return 'background-color:#FF0000;color:black;';
			 }
		 }
	});
	if(getSelected()==null||getSelected()==""){
		//禁用按钮
		$('#btnAdd').linkbutton('disable');
		$('#btnSave').linkbutton('disable');
		$('#btnDelete').linkbutton('disable');
		$('#btnReload').linkbutton('disable');
		$('#btnMb').linkbutton('disable');
	}else{
		//启用按钮
		$('#btnAdd').linkbutton('enable');
		$('#btnSave').linkbutton('enable');
		$('#btnDelete').linkbutton('enable');
		$('#btnReload').linkbutton('enable');
		$('#btnMb').linkbutton('enable');
	}
	$('#rq').val(rq);
	$('#'+rq).datagrid('reload');
}

function AddOrShowEast(title,url,width) {
	var eastpanel = $('#divLayout').layout('panel', 'east'); //获取右侧收缩面板
	if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
		$('#divLayout').layout('remove','east');
		$('#divLayout').layout('add', {
			title : title,
			region : 'east',
			width : width,
			href : url,
			closable : false,
			collapsible:false
		});
	} else {//打开新面板
		$('#divLayout').layout('add', {
			title : title,
			region : 'east',
			width : width,
			href : url,
			closable : false,
			collapsible:false
		});
	}
}

//查询
	function searchFrom(){
		var deptId = $('#deptId').val();
	if(deptId==null||deptId==""){
		$.messager.alert("提示","请选择具体科室！");
		return;
	}
	var datagridId = $('#inpWeekEnId').val();
	var rq = $('#rq').val();
   	$('#'+rq).datagrid('load', {
   		deptId:deptId,
   		rq:rq,
		search: $('#sName').val()
	});
}




	//加载部门树
   	$('#tDt').tree({  
   		url:"<c:url value='/outpatient/webPreregister/getDeptTree.action'/>", 
	    method:'get',
	    animate:true,
	    lines:true,
	    formatter:function(node){//统计节点总数
			var s = node.text;
			if (node.children&&node.children.length>0){
				s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
			}
			return s;
		},
		onClick: function(node){//点击节点
			if(node.attributes.hasson!=null&&node.attributes.hasson!=''&&node.attributes.hasson!='0'&&node.attributes.hasson!='1'){
				$('#deptId').val(node.id);
				$("#deptName").val(node.text);
			}else{
				$('#deptId').val('');
			}
			loadDatagrid(getSelected(),$('#rq').val());
		},
		onDblClick: function(node){
	   		$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
	    },
	  
	    onBeforeCollapse:function(node){
			if(node.id=="1"){
				return false;
			}
	    }
	}); 
//清除科室信息查询下拉框，查询条件的值后,动态加载页面
   	function clearComboboxValue(id){
   		delSelectedData(id);
   		delSelectedData('sName');
   		refresh();
   		$('#deptId').val("");
   		var deptId = $('#deptId').val();
   		var datagridId = $('#inpWeekEnId').val();
   		var rq = $('#rq').val();
   	   	$('#'+rq).datagrid('load', {
   	   		deptId:deptId,
   	   		rq:rq,
   			search: $('#sName').val()
   		});
   	}
   	function clearComboboxValue1(id){
   		delSelectedData(id);
   		refresh();
   		var deptId = $('#deptId').val();
   		var datagridId = $('#inpWeekEnId').val();
   		var rq = $('#rq').val();
   	   	$('#'+rq).datagrid('load', {
   	   		deptId:deptId,
   	   		rq:rq,
   			search: $('#sName').val()
   		});
   	}
  //科室部门树操作
   	function refresh(){//刷新树
   		$('#tDt').tree('options').url = "<c:url value='/outpatient/webPreregister/getDeptTree.action'/>";//只要门诊的，因此后边的不对"<c:url value='/outpatient/scheduleModel/treeDeptSchedule.action'/>";
		$('#tDt').tree('reload'); 
	}
   	function expandAll(){//展开树
		$('#tDt').tree('expandAll');
	}
   	function collapseAll(){//关闭树
		$('#tDt').tree('collapseAll');
	}

	function getSelected(){//获得选中节点
		var node = $('#tDt').tree('getSelected');
		if (node){
			if(node.attributes.hasson!=null&&node.attributes.hasson!=''&&node.attributes.hasson!='0'&&node.attributes.hasson!='1'){
				$('#btnTb').linkbutton('enable');
				return node.id;
			}else{
				$('#btnTb').linkbutton('disable');
				return "";
			}
		}else{
			return "";
		}
	}
	
	//查询树
	function searchTree(){
			$.ajax({
			url:"<c:url value='/baseinfo/employee/searchTree.action'/>?searcht="+encodeURI(encodeURI($('#searchTree').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
			type:'post',
			success: function(data) {
				$('#tDt').tree('collapseAll');//单独展开一个节点,先收缩树
				var node = $('#tDt').tree('find',data);
				if(node!=null){
					$('#tDt').tree('expandTo', node.target).tree('select', node.target); //展开指定id的节点
					loadDatagrid(getSelected(),$('#rq').val());
				}
			}
		});					
	}
	
	function selectNode(code){
		var node = $('#tDt').tree('find',code);
		if(node!=null){
			var pid= node.attributes.pid;
			var pnode= $('#tDt').tree('find',pid);
			$('#tDt').tree('expand', pnode.target).tree('scrollTo', node.target).tree('select',
					node.target);
			$('#divLayout').layout('remove','east');
			if(node.attributes.hasson!=null&&node.attributes.hasson!=''&&node.attributes.hasson!='0'&&node.attributes.hasson!='1'){
				$('#deptId').val(node.id);
				$('#deptName').val(node.text);
				
			}else{
				$('#deptId').val('');
			}
			$('#divLayout').layout('remove','east');
			if(node.attributes.hasson!=null&&node.attributes.hasson!=''&&node.attributes.hasson!='0'&&node.attributes.hasson!='1'){
				$('#deptId').val(node.id);
				$("#deptName").val(node.text);
			}else{
				$('#deptId').val('');
			}
			loadDatagrid(getSelected(),$('#rq').val());
		}
	}
	function btnAdd(){
		var deptId = $('#deptId').val();
    	if(deptId==null||deptId==""){
    		$.messager.alert("提示","请选择具体科室！");
    		return;
    	}
    	var row = $('#'+rq).datagrid('getSelected'); //获取当前选中行      
        if(row!=null&&row.id!=null&&row.id!=''){
        	if(row.netLimit==0){
        		$.messager.alert("提示","该专家号源已满,请选择其他专家！");
        		return;
        	}
        	row_data=row;
        	deptName=$('#deptName').val();
    	    AddOrShowEast("预约挂号","<c:url value='/outpatient/webPreregister/registerAdd.action'/>","580");
		}else{
			$.messager.alert("提示","请选择要预约的专家！");
		}
	}
	function btnReload(){
		$('#'+rq).datagrid('reload');
	}
	
	
	$('#deptSearch').combobox({
		url: "<%=basePath%>register/newInfo/deptCombobox.action", 
		valueField : 'deptCode',
		textField : 'deptName',
		filter:function(q,row){
			var keys = new Array();
			keys[keys.length] = 'deptCode';
			keys[keys.length] = 'deptName';
			keys[keys.length] = 'deptPinyin';
			keys[keys.length] = 'deptWb';
			keys[keys.length] = 'deptInputcode';
			if(filterLocalCombobox(q, row, keys)){
				row.selected=true;
			}else{
				row.selected=false;
			}
			return filterLocalCombobox(q, row, keys);
	    },
		onLoadSuccess:function(data){
			if(data!=null && data.length==1){
				var code= data[0].deptCode;
				$('#deptSearch').combobox('select',code);
				selectNode(code);
			}
		},
		onSelect:function(rec){
			var code=rec.deptCode;
			selectNode(code);
		}
	});
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</html>