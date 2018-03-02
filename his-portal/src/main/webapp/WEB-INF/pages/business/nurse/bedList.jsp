<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
	 <div class="easyui-layout" data-options="fit:true">
		<div id="p" data-options="region:'west'" title="病房" style="width: 15%;height:100%; padding: 10px">
			<ul id="tDt"></ul>
		</div>
		<div data-options="region:'center',fit:'true'" title="病床信息" style="height:100%;width: 80%">
			<div id="cc" class="easyui-layout" data-options="fit:true" style="height:100%;width: 70%">
				<div data-options="region:'north',border:false" style="height:7%;padding-top: 12px;padding-left: 5px;">
					姓名：<span id="patientName" style="width: 100px;"></span>&nbsp;&nbsp;&nbsp;
					病历号：<span id="medicalrecordId" style="width: 100px;"></span>
				</div>
				<div data-options="region:'center'" style="height: 7%px;padding-top: 10px;padding-left: 5px;">
					病床号：<input class="easyui-textbox" id="bedName"  />
					床位等级：<input class="easyui-combobox" id="bedLevel"   name="employeeType"/>
					<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0)" id="searchBtn" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:set">
					<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</shiro:hasPermission>	
				</div>
				<div data-options="region:'south',border:false" style="height:85%;width: 80%">
					<table id="list1" class="easyui-datagrid" style="width:85%;height:100%"; data-options="rownumbers:true,fitColumns:true,pageSize:20,pagination:true,striped:true,singleSelect:true" >   
						<thead>
							<tr>
								<th data-options="field:'bedName'" width="25%" align="center">病床号</th>
								<th data-options="field:'bedLevel',formatter:bedLevel" width="25%" align="center">等级</th>
								<th data-options="field:'bedState',formatter:bedTypeFamater" width="25%" align="center">状态</th>
								<th data-options="field:'bedwardName',formatter:function(value,row,index){
													if(row.businessBedward){
														return row.businessBedward.bedwardName
													}else{
														return value;
													}
												}" width="20%" align="center">病房</th>
							</tr>
						</thead>
					</table> 
				</div>
			</div>
		</div> 
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<script type="text/javascript">
	var noId="${noId}";
	var inpatientNo="${inpatientNo}"
	var bedLevelMap = new Map();//床位等级
	var bedStateMap= new Map() ;
	//加载病房树
	$(function(){
		$.ajax({
			url: '<%=basePath %>nursestation/nurse/replaceDoctor.action',
			data:{id:inpatientNo},
			type:'post',
			success: function(data) {
				$('#patientName').text(data.patientName);
				$('#medicalrecordId').text(data.medicalrecordId);
			}
		});
		$('#list1').datagrid({
			url:"<%=basePath%>inpatient/info/queryPatientRoomBed.action",
			onClickRow: function (rowIndex, rowData) {
				$.messager.confirm("确认","是否选择"+rowData.bedName+"号床包床?",function(r){
			    	 if(r){
			    		 $.ajax({
			    				url: '<%=basePath %>nursestation/nurse/savePackbed.action',
			    				data:{id:rowData.id,inpatientNo:inpatientNo},
			    				type:'post',
			    				success: function(data) {
			    					if(data=="success"){
			    						$.messager.alert('提示','包床保存成功！');
			    						setTimeout(function(){
											$(".messager-body").window('close');
											window.close();
										},3500);
			    						searchFrom();
			    					}else{
			    						$.messager.alert('提示','包床保存失败！');
			    					}
			    				}
			    			});
			    	 }
			    });
			},onLoadSuccess : function(data){
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
		$('#tDt').tree({
			url:'<%=basePath%>inpatient/info/queryPatientRoom.action?noId='+noId,
		    animate:true,
		    lines:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if(node.children.length>0){
					if (node.children){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
					}
				}
				return s;
			},onClick:function(data){
				$('#list1').datagrid('load',{
					roomId:data.id
				}); 
			}
		});
		$('#bedLevel').combobox({    
			url: "<%=basePath%>baseinfo/hospitalbed/queryBedLevelList.action",
			valueField : 'encode',
			textField : 'name',
			multiple:false,
			onHidePanel:function(none){
	    	    var data = $(this).combobox('getData');
	    	    var val = $(this).combobox('getValue');
	    	    var result = true;
	    	    for (var i = 0; i < data.length; i++) {
	    	        if (val == data[i].encode) {
	    	            result = false;
	    	        }
	    	    }
	    	    if (result) {
	    	        $(this).combobox("clear");
	    	    }else{
	    	        $(this).combobox('unselect',val);
	    	        $(this).combobox('select',val);
	    	    }
	    	},
	    	filter: function(q, row){
	    	    var keys = new Array();
	    	    keys[keys.length] = 'encode';
	    	    keys[keys.length] = 'name';
	    	    keys[keys.length] = 'pinyin';
	    	    keys[keys.length] = 'wb';
	    	    keys[keys.length] = 'inputCode';
	    	    return filterLocalCombobox(q, row, keys);
	    	},
		});
		$.ajax({
			url: '<%=basePath %>baseinfo/hospitalbed/queryBedLevel.action',
			type:'post',
			success: function(payData) {
				bedLevelMap = payData;
			}
		});
		$.ajax({
		    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=bedtype",
			type:'post',
			success: function(data) {
				$('#bedState').combobox({ 
					data:data,
					valueField:'encode',    
					textField:'name',
					multiple:false
				});
				var type = data;
				for(var i=0;i<type.length;i++){
					bedStateMap.put(type[i].encode,type[i].name);
				}
			}
		});

	});
	//渲染列表中的床位状态
	function bedTypeFamater(value,row,index){
		if(value!=null&&value!=''){
			return bedStateMap.get(value);
		}
	}
	function bedLevel(value,row,index){
		if(value!=null&&value!=''){
			return bedLevelMap[value];
		}
	}
	function searchFrom() {
		var node = $('#tDt').tree('getSelected');
		var nodeId = null;
		if(node!=null){
			if(node.id!="1"){
				nodeId = node.id;
			}
		}
		var bedName = $('#bedName').textbox('getValue');
		var bedLevel =$('#bedLevel').combobox('getValue');
		$('#list1').datagrid('load', {
			bedName:bedName,
			bedLevel:bedLevel,
			roomId:nodeId
		});
	}
	function clear(){
		$('#bedName').textbox('setValue',"");
		$('#bedLevel').combobox('setValue',"");
		$('#list1').datagrid('load', {
		});
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>