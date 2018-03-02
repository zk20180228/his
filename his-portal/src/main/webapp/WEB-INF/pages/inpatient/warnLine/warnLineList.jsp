<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>警戒线设置</title>
<%@ include file="/common/metas.jsp" %>
<script type="text/javascript">
	/**
	 *证件类型的list
	 */
	var certiList = "";
	/**
	 *医疗类别list
	 */
	var medicalType="";
	/**
	 *科室map对象
	 */
	var deptMap="";
	
	var total ="";
	//性别
	var sexMap;
	
	/**
	 * 比较时间大小
	 */
	function compareTime(startDate,endDate){
		if(startDate.length>0&&endDate.length>0){  
		      var startDateTemp = startDate.split(" ");   
		      var endDateTemp = endDate.split(" ");   
		      var arrStartDate = startDateTemp[0].split("-");   
		      var arrEndDate = endDateTemp[0].split("-");   
		      var arrStartTime = startDateTemp[1].split(":");   
		      var arrEndTime = endDateTemp[1].split(":");   
		      var allStartDate = new Date(arrStartDate[0],arrStartDate[1],arrStartDate[2],arrStartTime[0],arrStartTime[1],arrStartTime[2]);   
		      var allEndDate = new Date(arrEndDate[0],arrEndDate[1],arrEndDate[2],arrEndTime[0],arrEndTime[1],arrEndTime[2]);   
		      if(allStartDate.getTime()>allEndDate.getTime()){ 
		       		return false;   
		      }   
		 }   
		 return true;   
	}
	
	/**
	 * 保存设置警戒线
	 */
	function setWarnLine(){
		var row = $('#infolist').datagrid('getSelected'); //获取当前选中行         
		if(row!=null){
			if(row.id!=null){
				Adddilog("设置警戒线","<%=basePath %>inpatient/warnLine/setWarnLineUrl.action?patientId="+row.id);
			}
		}else{
			$.messager.alert('提示',"请选择一条要设置警戒线数据");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	/**
	 * 查询
	 */
	function searchFrom() {
		var queryName = $('#queryName').textbox('getText');
		var deptCode = "";
		if($('#tDt').tree('getSelected').id=="1"){
			deptCode = "";
		}else{
			deptCode = $('#tDt').tree('getSelected').attributes.deptCode;
		}
		$('#infolist').datagrid('load', {
			queryName:queryName,
			deptId:deptCode,
		});
	}
	$(function(){
		var id='${id}'; //存储数据ID
		setTimeout(function(){
			//添加datagrid事件及分页
			$('#infolist').datagrid({
				pagination:true,
				url:'<%=basePath %>inpatient/warnLine/queryWarnLine.action?menuAlias=${menuAlias}&type=ALL',
				pageSize:20,
				pageList:[20,30,50,80,100],
				onLoadSuccess: function (data) {//默认选中
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
					total = data.total;
					var rowData = data.rows;
					$.each(rowData, function (index, value) {
					if(value.id == id){
						$('#infolist').datagrid('selectRow', index);
					}
				});
				$('#row').val(data.row);
				$('#page').val(data.page);
			},
			onBeforeLoad:function(param){
				$('#infolist').datagrid('clearChecked');
				$('#infolist').datagrid('clearSelections');
			}
		});
	},1);
		bindEnterEvent('queryName',searchFrom,'easyui');//绑定回车事件
		bindEnterEvent("searchTree",searchTree, "easyui");
		/**
		 * 加载科室部门树
		 */
		$('#tDt').tree({
				 url:'<%=basePath %>inpatient/warnLine/treeNurseStation.action',
				method : 'get',
				animate : true,
				lines : true,
				onClick : function(node) {//点击节点
					$('#infolist').datagrid('uncheckAll');
					$('#infolist').datagrid('load', {
						deptId : node.attributes.deptCode,
					});
					var icons = $(node.target).find('text');
				},onLoadSuccess:function(node,data){
					$('#tDt').tree('select',$('#tDt').tree('find', 1).target);
				}
		});
		$.ajax({
			url: '<%=basePath %>baseinfo/department/getDeptMap.action',
			type:'post',
			success: function(data) {
				deptMap=data;
			}
		});
		$.ajax({
			url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionaryMap.action?type=sex',
			type:'post',
			success: function(data) {
				sexMap=data;
			}
		});
		$.ajax({
			url:"<%=basePath %>inpatient/warnLine/likeCertificate.action",
			type:'post',
			success: function(data) {
				certiList=data;
			}
		});
		$.ajax({
			url:"<%=basePath %>inpatient/warnLine/likeMedicalType.action",
			type:'post',
			success: function(data) {
				medicalType=data;
			}
		});
	})
	
	/*******************************开始读卡***********************************************/
	//定义一个事件（读卡）
	function read_card_ic(){
		var card_value = app.read_ic();
		if(card_value=='0'||card_value==undefined||card_value==''){
			$.messager.alert('提示','此卡号['+card_value+']无效');
			return;
		}
		$.ajax({
			url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
			data:{idcardOrRe:card_value},
			type:'post',
			async:false,
			success: function(data) { 
				if(data==null||data==''){
					$.messager.alert('提示','此卡号无效');
					return;
				}
				$('#queryName').textbox('setValue',data);
				searchFrom();
			}
		});
	};
	/*******************************结束读卡***********************************************/
	/*******************************开始读身份证***********************************************/
		//定义一个事件（读身份证）
		function read_card_sfz(){
			var card_value = app.read_sfz();
			if(card_value=='0'||card_value==undefined||card_value==''){
				$.messager.alert('提示','此卡号['+card_value+']无效');
				return;
			}
			$.ajax({
				url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
				data:{idcardOrRe:card_value},
				type:'post',
				async:false,
				success: function(data) {
					if(data==null||data==''){
						$.messager.alert('提示','此卡号无效');
						return;
					}
					$('#queryName').textbox('setValue',data);
					searchFrom();
				}
			});
		};
	/*******************************结束读身份证***********************************************/
	/**
	 * 科室部门树操作
	 */
	function getSelected() {//获得选中节点
		var node = $('#tDt').tree('getSelected');
		if (node) {
			var id = node.attributes.deptCode;
			return id;
		}
	}
	/**
	 * 加载dialog
	 */
	function Adddilog(title, url) {
		$('#add').dialog({    
		    title: title,    
		    width: '400px',    
		    height:'250px',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		   });    
	}
	/**
	 * 打开dialog
	 */
	function openDialog() {
		$('#add').dialog('open'); 
	}
	/**
	 * 关闭dialog
	 */
	function closeDialog() {
		$('#add').dialog('close');  
	}
	/**
	 * 证件类型列表页 显示
	 */
	function certiFamater(value,row,index){
		for(var i=0;i<certiList.length;i++){
			if(value==certiList[i].encode){
				return certiList[i].name;
			}
		}	
	}
	/**
	*截取入院日期的时间
	*/
	function dateFormat(value,row,index){
		if(value!=null&&value!=''){
			var newDate=/\d{4}-\d{1,2}-\d{1,2}/g.exec(value);
			return newDate;
		}
	}
	/**
	 * 科室列表页 显示
	 */
	function deptFamater(value,row,index){
		if(value!=null&&value!=''){
			return deptMap[value];
		}
	}
	/**
	 * 医疗类别 显示
	 */
	function medicalTypeFamater(value,row,index){
		for(var i=0;i<medicalType.length;i++){
			if(value==medicalType[i].encode){
				return medicalType[i].name;
			}
		}	
	}
	
	function searchTree(){
		var nodes = $('#tDt').tree('getChecked');
		if (nodes.length > 0) {                        
			for(var i=0; i<nodes.length; i++){
				$('#tDt').tree('uncheck',nodes[i].target);
			};
		}		
		var queryName = $('#searchTree').textbox('getText');
		$.ajax({
			url : "<%=basePath %>inpatient/warnLine/queryDeptLsit.action",
			data:{queryName:queryName},
			type : 'post',
			success : function(data) {
				if(data!=null&&data.length>0){
					var node = $('#tDt').tree('find',data[0].id);
					$('#tDt').tree('expandTo', node.target).tree('select',node.target).tree('scrollTo',node.target); 
				}
			}
		});
	}
	
	//行号生成
	function functionRowNum(value,row,index){
		var row = $('#row').val();
		var page = $('#page').val();
		return parseInt(row)*parseInt(page)-parseInt(row)+1;
	}
	
	//添加颜色标识
	function functionColour(value,row,index){
		var myDate = new Date();
		var month=myDate.getMonth()+1;
		var startDate = myDate.getFullYear()+"-"+month+"-"+myDate.getDate()+" 00:00:00";
		var endDate = row.alterBegin+"";
		if(endDate.indexOf(":") >= 0){
			if(!compareTime(startDate,endDate)){
				return 'background-color:#FF0000;color:black;';
			}
		}
		if(row.freeCost<=row.moneyAlert){
			if(row.moneyAlert!=0){
				return 'background-color:#FF0000;color:black;';
			}
		}
	}
	function clear1(){
	  	$('#queryName').textbox('setValue',"");
	  	searchFrom();
  	}
	
	//性别渲染
	function sexFormatter(value,row,index){
		if(value!=null&&value!=''){
			if(sexMap[value]){
				return sexMap[value];
			}else{
				return value;
			}
		}
	}
</script>
</head>
<body style="margin: 0px; padding: 0px">
	<div id="divLayout" class="easyui-layout" data-options="fit:true">
		
		<div data-options="region:'west',split:true,border:true"  style="width: 300px;height: 100%;min-width: 290px;border-top:0">
			<div class="easyui-layout" data-options="fit:true" style="border: 0px;">
				<div  data-options="region:'north',border:false" style="width:300px;height: 30px;border: 0px;margin-top:10px">
					<table style="width: 100%;height: 100%;border: 0px;">
						<tr>
							<td > 
								&nbsp;<input  class="easyui-textbox" style="width:175px" ID="searchTree" name="" data-options="prompt:'输入护士站信息回车查询'" />
				   				<shiro:hasPermission name="${menuAlias}:function:query">
			  		 			<a href="javascript:searchTree()" class="easyui-linkbutton" style="width:80px" data-options="iconCls:'icon-search'">查&nbsp;询&nbsp;</a>
			  	 				 </shiro:hasPermission>	
			  	 			</td>
						</tr>
					</table>
					
				</div>
				<div data-options="region:'center',split:true,border:false" style="margin:5px">
					<ul id="tDt"></ul>
				</div>
			</div>
		</div>
		<div data-options="region:'center',border:true" style="width: 85%; height: 100%;border-top:0">
			<div id="divLayout1" class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',split:false" style="width:100%;height: 42px;border-top:0;border-left:0;">
					<table cellspacing="0" cellpadding="0" border="0" style="width: 100%">
						<tr>
							<td style="padding: 5px 5px;height: 30px;line-height: 30px;">过滤条件：<input class="easyui-textbox" id="queryName" data-options="prompt:'姓名,病历号,就诊卡号,床号'"  style="width: 190px;">
								<shiro:hasPermission name="${menuAlias}:function:query">
								&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:readCard">
       								<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
       							</shiro:hasPermission>
					        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
					        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
       							</shiro:hasPermission>
									<a onclick="clear1()" class="easyui-linkbutton" iconCls="reset">重置</a>
								<shiro:hasPermission name="${menuAlias }:function:set">
									<a href="javascript:setWarnLine();void(0)" onclick="setWarnLine()" class="easyui-linkbutton" data-options="iconCls:'icon-cog_edit'">设置</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</table>
				</div>
				<div data-options="region:'center',split:false,iconCls:'icon-book',border:false">
					<table id="infolist"  data-options="method:'post',striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true" >
						<thead>
							<tr>
								<th data-options="field:'orderId',width:24,align:'center',styler:functionColour" ></th>
								<th data-options="field:'inpatientNo',hidden:true" style="width:6%">住院流水号</th>
								<th data-options="field:'medicalrecordId'" style="width: 6%">病历号</th>
								<th data-options="field:'idcardNo'" style="width: 6%">就诊卡号</th>
								<th data-options="field:'patientName'" style="width: 5%">姓名</th>
								<th data-options="field:'reportSex',formatter:sexFormatter" style="width: 3%">性别</th>
								<th data-options="field:'bedId'" style="width:5%">床号</th>
								<th data-options="field:'inDate',formatter:dateFormat" style="width: 7%">入院日期</th>
								<th data-options="field:'freeCost'" style="width: 6%" align="right" halign="left">余额</th>
								<th data-options="field:'moneyAlert'" style="width:7%" align="right" halign="left">金额警戒线</th>
								<th data-options="field:'alterBegin'" style="width: 10%">警戒开始时间</th>
								<th data-options="field:'alterEnd'" style="width:10%">警戒结束时间</th>	
								<th data-options="field:'certificatesType',formatter:certiFamater" style="width: 5%">证件类型</th>
								<th data-options="field:'certificatesNo'" style="width: 10%">证件号码</th>						
								<th data-options="field:'deptCode',formatter:deptFamater" style="width: 7%">科室</th>
								<th data-options="field:'nurseCellCode',formatter:deptFamater" style="width:7%">护士站</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	<div id="add"></div>
</body>
</html>