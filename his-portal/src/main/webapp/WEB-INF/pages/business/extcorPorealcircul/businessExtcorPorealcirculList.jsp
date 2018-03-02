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
<script type="text/javascript">

var sexMap=new Map();
//性别渲染
	var flag = "${deptType }";
	$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});
	$(function(){
		$('iframe').attr('src','<%=basePath %>publics/businessExtcorPorealcircul/editBusinessExtcorPorealcircul.action?menuAlias=${menuAlias}&deptType=${deptType}');
		//已登记列表
		$('#exList').datagrid({
			url:'<%=basePath %>publics/businessExtcorPorealcircul/queryExtcorPorealcirculList.action',
			queryParams: {'deptType': flag},
			pagination : true,
			pageSize : 20,
			pageList : [ 20, 30, 50, 80, 100 ],
			onDblClickRow : function(rowIndex, rowData){
				$('iframe').attr('src','<%=basePath %>publics/businessExtcorPorealcircul/editBusinessExtcorPorealcircul.action?menuAlias=${menuAlias}&deptType=${deptType}&no='+rowData.id);
			},onLoadSuccess:function(row, data){
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
				}}
		});
		if(flag == 'C'){//门诊
			$('#noType').html('门诊号：');
			//患者信息列表
			$('#parientList').datagrid({
				pagination : true,
				pageSize : 20,
				pageList : [ 20, 30, 50, 80, 100 ],
				url:'<%=basePath %>publics/businessExtcorPorealcircul/registerList.action',
			    columns:[[    
			              {field:'clinicCode',title:'门诊号',width:'20%'},
			              {field:'midicalrecordId',title:'病历号',width:'20%'},
			              {field:'patientName',title:'姓名',width:'20%'},
			              {field:'patientSex',title:'性别',width:'10%',formatter: function(value,row,index){
			  				return sexMap.get(value);
							}},
			              {field:'regDate',title:'挂号时间',width:'30%'},
			          ]],
	          onDblClickRow : function(rowIndex, rowData){
	        	  var agedate=DateOfBirth(rowData.patientBirthday);
	        	  var name = encodeURIComponent(encodeURIComponent(rowData.patientName));
	        	  $.ajax({
	        		  url:'<%=basePath %>publics/businessExtcorPorealcircul/queryExtcorPorealcirculList.action',
	        		  data : {no : rowData.clinicCode,'deptType': flag},
	        		  success : function(data){
	        			  if(data.total > 0){
	        				  $.messager.alert('提示信息','该患者已经登记，请修改登记信息');
	        				  setTimeout(function(){$(".messager-body").window('close')},3500);
	        				  $('iframe').attr('src','<%=basePath %>publics/businessExtcorPorealcircul/editBusinessExtcorPorealcircul.action?menuAlias=${menuAlias}&deptType=${deptType}&inpatientNo=' + data.rows[0].inpatientNo+ '&businessExtcorPorealcircul.patientFlg=C'+'&businessExtcorPorealcircul.age='+encodeURIComponent(encodeURIComponent(agedate.get('nianling')+agedate.get('ageUnits'))));
	        			  }else{
	    		        	  $('iframe').attr('src','<%=basePath %>publics/businessExtcorPorealcircul/editBusinessExtcorPorealcircul.action?menuAlias=${menuAlias}&deptType=${deptType}&businessExtcorPorealcircul.inpatientNo='
	    		        			  + rowData.clinicCode + '&businessExtcorPorealcircul.patientNo=' + rowData.midicalrecordId + '&businessExtcorPorealcircul.name=' + name
	    		        			  +'&businessExtcorPorealcircul.sex=' + rowData.patientSex + '&businessExtcorPorealcircul.patientFlg=C'+'&businessExtcorPorealcircul.age='+encodeURIComponent(encodeURIComponent(agedate.get('nianling')+agedate.get('ageUnits')))
	    		        			  );
	        			  }
	        		  }
	        	  });
				},onLoadSuccess:function(row, data){
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
					}}
			});
		}else{//住院
			$('#noType').html('住院号：');
			//患者信息列表
			$('#parientList').datagrid({
				pagination : true,
				pageSize : 20,
				pageList : [ 20, 30, 50, 80, 100 ],
				url:'<%=basePath %>publics/businessExtcorPorealcircul/inpatientList.action',
			    columns:[[    
			              {field:'medicalrecordId',title:'病历号',width:'25%',hidden:'true'},
			              {field:'bedName',title:'床号',width:'25%'},
			              {field:'inpatientNo',title:'住院号',width:'25%'},
			              {field:'patientName',title:'姓名',width:'25%'},
			              {field:'reportSex',title:'性别',width:'25%',formatter: function(value,row,index){
			  				return sexMap.get(value);
						}}
			          ]],
			   data:[],
	          onDblClickRow : function(rowIndex, rowData){
	        	  var agedate=DateOfBirth(rowData.reportBirthday);
	        	  var name = encodeURIComponent(encodeURIComponent(rowData.patientName));
	        	  $.ajax({
	        		  url:'<%=basePath %>publics/businessExtcorPorealcircul/queryExtcorPorealcirculList.action',
	        		  data : {no : rowData.inpatientNo,'deptType': flag},
	        		  success : function(data){
	        			  if(data.total > 0){
	        				  $.messager.alert('提示信息','该患者已经登记，请修改登记信息');
	        				  setTimeout(function(){$(".messager-body").window('close')},3500);
	        				  $('iframe').attr('src','<%=basePath %>publics/businessExtcorPorealcircul/editBusinessExtcorPorealcircul.action?menuAlias=${menuAlias}&deptType=${deptType}&inpatientNo='+rowData.inpatientNo+'&businessExtcorPorealcircul.age='+encodeURIComponent(encodeURIComponent(agedate.get('nianling')+agedate.get('ageUnits'))));
	        			  }else{
	        				  $('iframe').attr('src','<%=basePath %>publics/businessExtcorPorealcircul/editBusinessExtcorPorealcircul.action?menuAlias=${menuAlias}&deptType=${deptType}&businessExtcorPorealcircul.inpatientNo='
	    		        			  + rowData.inpatientNo + '&businessExtcorPorealcircul.patientNo=' + rowData.medicalrecordId + '&businessExtcorPorealcircul.name=' + name
	    		        			  + '&businessExtcorPorealcircul.sex=' + rowData.reportSex+'&businessExtcorPorealcircul.patientFlg=I'+'&businessExtcorPorealcircul.age='+encodeURIComponent(encodeURIComponent(agedate.get('nianling')+agedate.get('ageUnits'))));
	        			  }
	        		  }
	        	  });
	          }
			});
		}
		bindEnterEvent('queryName', searchFrom, 'easyui');
	});
	
	/**
	 * 表单提交
	 * @author  ldl
	 * @date 2015-7-2 10:53
	 * @version 1.0
	 */
	 function reloadExList(){ 
		 $('#exList').datagrid('reload');
     }	
     /**
	 * 查询
	 * @author  ldl
	 * @date 2015-7-2 14:52
	 * @version 1.0
	 */
     function searchFrom(){
     	var queryName = $.trim($('#queryName').textbox('getText'));
     	$('#parientList').datagrid('reload',{'no':queryName});
     	$('#exList').datagrid('reload',{'no':queryName,'deptType': flag});
     }
</script>
<style>
.layout-split-south{
	border:0
}
.panel-header{
	border-top:0;
}
</style>
</head>
<body>
<script type="text/javascript">
		var nowlogInDept="${nowLoginDept}";
		console.info(nowlogInDept)
		if(nowlogInDept!=null&&nowlogInDept!=''){
		 	 $("body").setLoading({
		 			id:"body",
		 			isImg:false,
		 			text:"请选择登录科室"
		 	});
		 }
</script>
	<div class="easyui-layout" style="width:100%;height:100%;">
		<div id="tiddiv" data-options="region:'west',split:false,border:false" style="width:30%;">
			<div class="easyui-layout" style="width:100%;height:100%;">
				<div id="que" data-options="region:'north',split:false,border:false" style="height: 40px;">		
						<input type="hidden" id="deptType" value="${deptType }"/>
					<table style="width:100%;border:false;padding:3px;" border="0" id="searchTab">
						<tr>
							<td style="text-align:left;"><span id="noType">病历号：</span><input class="easyui-textbox" id="queryName" name="queryName"/>
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search" id="search">查询</a></td>
						</tr>
					</table>
				</div>
				<div id="parient" data-options="region:'center',split:false,title:'患者信息',border:false" style="height: 45%;padding:5px;">
					<table id="parientList" data-options="fit:true,method:'post',idField: 'id',striped:true,border:true,checkOnSelect:false,selectOnCheck:false,singleSelect:true,fitColumns:false">
					</table>
				</div>
				<div id="ex" data-options="region:'south',split:'false',title:'已登记患者信息',border:false" style="height: 45%;width: 100%;padding:5px;">
					<table id="exList" data-options="fit:true,idField: 'id',striped:true,border:true,checkOnSelect:false,selectOnCheck:false,singleSelect:true,fitColumns:false">
						<thead>
							<tr>
								<c:choose>
									<c:when test="${deptType == 'C'}">
									<th data-options="field:'inpatientNo',width:'25%'" id="cOri">门诊号</th>
									</c:when>
									<c:when test="${deptType == 'I'}">
									<th data-options="field:'inpatientNo',width:'25%'" id="cOri">住院号</th>
									</c:when>
									<c:otherwise>
									</c:otherwise>
								</c:choose>
								<th data-options="field:'patientNo',width:'25%'">病历号</th>
								<th data-options="field:'name',width:'25%'">姓名</th>
								<th data-options="field:'operationName',width:'25%'">手术名称</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
		<div data-options="region:'center',split:false,title:'患者体外循环登记列表'" style="width: 70%; height: 100%;border-top:0">
			<iframe style="width:99%; height:99%;border: none;" id="editFrame" />
		</div>
	</div>
</body>

</html>