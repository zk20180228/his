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
<script type="text/javascript">
	var noId="${noId}"
	var bedLevelMap = new Map();//床位等级
	var bedStateMap= new Map() ;
	 var empMap=new Map();
	//渲染员工
	$.ajax({
			url: "<%=basePath%>inpatient/admission/getEmpList.action", 
			type:'post',
			success: function(data) {
				if(data!=null&&data!=""){
					empMap=data;
				}		
			}
		});	
	//加载病房树
	$(function(){
		$('#list1').datagrid({
			url:"<%=basePath%>inpatient/info/queryPatientRoomBed.action",
			pagination:true,
			pageSize:20,
			pageNumber:1,
			rownumbers:true,
			onClickRow: function (rowIndex, rowData) {
				$.messager.confirm("确认","确认选择"+rowData.bedName+"号床吗?",function(r){
			    	 if(r){
			    		window.opener.$('#roombedId').val(rowData.bedName);
			    		var s=rowData.bedName;
			    		var r=rowData.businessBedward.bedwardName;
			    		var q=r+"病房"+s+"号床";
			    		window.opener.$('#chuanghao').textbox("setText",s);
			    		window.opener.$('#bedName').val(rowData.bedName);
			    		window.opener.$('#bedwardId').val(rowData.businessBedward.id);
			    		window.opener.$('#bedwardName').val(rowData.businessBedward.bedwardName);
			    		window.opener.$('#bedNo').val(rowData.id);
			    		
			    		window.opener.$('#houseDocCode').combogrid("setValue",empMap[rowData.houseDocCode]);
			    		window.opener.$('#houseDocCode1').val(rowData.houseDocCode);
			    		window.opener.$('#houseDocName').val(rowData.houseDocName);
			    		
			    		window.opener.$('#chiefDocCode').combogrid("setValue",empMap[rowData.chiefDocCode]);
			    		window.opener.$('#chiefDocCode1').val(rowData.chiefDocCode);
			    		window.opener.$('#chiefDocName').val(rowData.chiefDocName);
			    		
			    		window.opener.$('#chargeDocCode').combogrid("setValue",empMap[rowData.chargeDocCode]);
			    		window.opener.$('#chargeDocCode1').val(rowData.chargeDocCode);
			    		window.opener.$('#chargeDocName').val(rowData.chargeDocName);
			    		
			    		window.opener.$('#dutyNurseCode').combogrid("setValue",empMap[rowData.dutyNurseCode]);
			    		window.opener.$('#dutyNurseCode1').val(rowData.dutyNurseCode);
			    		window.opener.$('#dutyNurseName').val(rowData.dutyNurseName);
			    		window.close();
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
				$('#list1').datagrid({
					url:"<%=basePath %>inpatient/info/queryPatientRoomBed.action?roomId="+data.id,
					onClickRow: function (rowIndex, rowData) {
						$.messager.confirm("确认","确认选择"+rowData.bedName+"号床吗?",function(r){
					    	 if(r){
					    		window.opener.$('#roombedId').val(rowData.id);
					    		var s=rowData.bedName;
					    		window.opener.$('#chuanghao').textbox("setText",s);
					    		window.opener.$('#bedName').val(rowData.bedName);
					    		window.opener.$('#bedwardId').val(rowData.businessBedward.id);
					    		window.opener.$('#bedwardName').val(rowData.businessBedward.bedwardName);
					    		window.opener.$('#bedNo').val(rowData.id);
					    		
					    		window.opener.$('#houseDocCode').combogrid("setValue",empMap[rowData.houseDocCode]);
					    		window.opener.$('#houseDocCode1').val(rowData.houseDocCode);
					    		window.opener.$('#houseDocName').val(rowData.houseDocName);
					    		
					    		window.opener.$('#chiefDocCode').combogrid("setValue",empMap[rowData.chiefDocCode]);
					    		window.opener.$('#chiefDocCode1').val(rowData.chiefDocCode);
					    		window.opener.$('#chiefDocName').val(rowData.chiefDocName);
					    		
					    		window.opener.$('#chargeDocCode').combogrid("setValue",empMap[rowData.chargeDocCode]);
					    		window.opener.$('#chargeDocCode1').val(rowData.chargeDocCode);
					    		window.opener.$('#chargeDocName').val(rowData.chargeDocName);
					    		
					    		window.opener.$('#dutyNurseCode').combogrid("setValue",empMap[rowData.dutyNurseCode]);
					    		window.opener.$('#dutyNurseCode1').val(rowData.dutyNurseCode);
					    		window.opener.$('#dutyNurseName').val(rowData.dutyNurseName);
					    		window.close();
					    	 }
					    });
					},
					onLoadSuccess:function(row, data){
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
			}
		});
		$('#bedLevel').combobox({    
			url: "<%=basePath%>baseinfo/hospitalbed/queryBedLevelList.action",
			valueField : 'encode',
			textField : 'name',
			multiple:false
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
		var bedName = $('#bedName').textbox('getValue');
		var bedLevel =$('#bedLevel').combobox('getValue');
		$('#list1').datagrid('load', {
			bedName:bedName,
			bedLevel:bedLevel,
		});
	}
	function clear(){
		$('#bedName').textbox('setValue',"");
		$('#bedLevel').combobox('setValue',"");
		$('#list1').datagrid('load', {
		});
	}
</script>
<body>
	 <div class="easyui-layout" style="width: 100%; height: 100%;">
		<div id="p" data-options="region:'west'" title="病房" style="width: 15%;height:100%; padding: 10px">
		    <ul id="tDt"></ul>
		</div>
		<div data-options="region:'center',fit:'true'" title="病床信息" style="height:100%;width:85%;">
			<div style="padding: 5px 0px 0px 5px;margin-bottom: 5px;" data-options="fit:true">
							<table  style="border: 1px solid #95b8e7;width:86%;height: 7%; padding: 5px;">
								<tr>
									<td style="width: 230px" nowrap="nowrap">
										病床号：<input class="easyui-textbox" id="bedName"  />
									</td>
									<td style="width: 230px" nowrap="nowrap">
										床位等级：<input class="easyui-combobox" id="bedLevel"   name="employeeType"/>
									</td>
									<td>
										<a href="javascript:void(0)" id="searchBtn" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
										<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" iconCls="reset">重置</a>
									</td>
								</tr>
							</table>
			</div>
			<table id="list1" class="easyui-datagrid" style="width:85%;height:93%" data-options="fitColumns:true,pagination:true,striped:true,singleSelect:true" >   
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
					            				}" width="24%" align="center">病房</th>    
					        </tr>   
					    </thead>   
					</table> 
		</div> 
	</div>
</body>
</html>