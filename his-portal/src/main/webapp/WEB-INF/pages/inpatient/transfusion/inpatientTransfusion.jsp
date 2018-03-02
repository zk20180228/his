<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>输血申请单</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
	var blood="";
	var chengfen = "";
	var ss="${menuAlias}"
	$(function(){
		
		$.ajax({
			url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=bloodType'/>",
			success: function(unitData) {
				blood = unitData;
			}
		});
		$.ajax({
			url : '<%=basePath%>publics/transfusion/queryInpatientComponentMap.action',
			success: function(unitData) {
				chengfen =unitData;
			}
		});
		//查询
		$('#list').datagrid({
			url:"<%=basePath %>publics/InpatientApply/queryInpatientApplyList.action?menuAlias=${menuAlias}&state="+2,
			onBeforeLoad:function(){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			},
			onDblClickRow:function(rowIndex, rowData){
				window.open('<%=basePath %>publics/transfusion/transfusionApplicationView.action?id='+rowData.id,'信息查看','width='+(window.screen.availWidth-150)+',height='+(window.screen.availHeight-220)+
				',top=20,left=60,resizable=yes,status=yes,menubar=no,scrollbars=yes');
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
		//状态(查询)
		$("#state").combobox({ 
			valueField: 'id',
			textField: 'value',
			data:[
			{id:'1',value:'申请'},
			{id:'2',value:'核准'},
			{id:'3',value:'配血'},
			{id:'4',value:'发血'},
			{id:'5',value:'不核收'},
			{id:'6',value:'作废'}
			]
		   });
		bindEnterEvent('name',searchFrom,'easyui');
		bindEnterEvent('cardNo',searchFrom,'easyui');
		bindEnterEvent('state',searchFrom,'easyui');
	});
	
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
				$('#cardNo').textbox('setValue',data);
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
					$('#cardNo').textbox('setValue',data);
					searchFrom();
				}
			});
		};
	/*******************************结束读身份证***********************************************/
	function functionxuexing(value,row,index){
    		for ( var i = 0; i < blood.length; i++) {
				if (value == blood[i].encode) {
					return blood[i].name;
				}
			}
	}
	function functionchengfen(value,row,index){
		for ( var i = 0; i < chengfen.length; i++) {
			if (value == chengfen[i].bloodTypeCode) {
				return chengfen[i].bloodTypeName;
			}
		}
	}
	function functionRH(value,row,index){
		switch (row.rh) {
		case '0':
			text = '待查';
			break;
		case '1':
			text = '阴性';
			break;
		case '2':
			text = '阳性';
			break;
		default:
			text = '';
			break;
		}
		return text;
	}
	function functionzhuangtai(value,row,index){
		switch (row.applyState) {
		case '1':
			text = '申请';
			break;
		case '2':
			text = '核准';
			break;
		case '3':
			text = '配血';
			break;
		case '4':
			text = '发血';
			break;
		case '5':
			text = '不核收';
			break;
		case '6':
			text = '作废';
			break;
		default:
			text = '';
			break;
		}
		return text;
	}
	//删除
	function del() {
		var rows=$('#list').datagrid('getSelections');
		if (rows.length > 0) {
			var ids = '';
			for ( var i = 0; i < rows.length; i++) {
				if (rows[i].id == null) {//如果id为null 则为新添加行
					var dd = $('#list').edatagrid('getRowIndex', rows[i]);//获得行索引
					$('#list').edatagrid('deleteRow', dd);//通过索引删除该行
				} else {
					if(ids != null && ids != ""){
						ids += ",";
					}
					ids += rows[i].id;
				}
			}
			if (ids != null && ids != "") {
				$.messager.confirm('确认', '确定要删除选中信息吗?', function(res) {//提示是否删除
					if (res) {
						$.ajax({
							url : '<%=basePath%>publics/InpatientApply/delInpatientApply.action?ids=' + ids,
							type : 'post',
							success : function(date) {
								if(date.resCode=="success"){
									$.messager.alert("操作提示", "删除成功！", "success");
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
									searchFrom()
								}else{
									$.messager.alert("操作提示", "删除失败！", "error");
								}
							}
						});
					}
				});
			}
		} else {
			$.messager.alert("操作提示", "请选择删除数据");
		}
	}
	
	//查询
	function searchFrom(){
	    var name =$('#name').textbox('getText');
	    var cardNo =$('#cardNo').textbox('getText');
	    var applyState =$('#state').combobox('getValue');
	    $('#list').datagrid({
	    	url:"<%=basePath%>publics/InpatientApply/queryInpatientApplyList.action",
	    	method:'post',
	    	queryParams: {
	    		'inpatientApply.name': name,
	    		'state':"2",
	    		'cardNo':cardNo,
	    		'menuAlias':"${menuAlias}",
	    		'inpatientApply.applyState':applyState
	    	},onLoadSuccess:function(data){
	    		if(data.rows.length>0){
	    			if(name!=''||cardNo!=''){
		    			$('#name').textbox('setText',data.rows[0].name);
		    			$('#cardNo').textbox('setText',data.rows[0].patientNo);
		    		}
	    		}
	    	}
	    	
		});
	}

	
		function add(){
			window.open('<%=basePath %>publics/transfusion/transfusionApplicationList.action?menuAlias='+ss,'输血申请','width='+(window.screen.availWidth-150)+',height='+(window.screen.availHeight-90)+
			',top=20,left=60,resizable=yes,status=yes,menubar=no,scrollbars=yes');
		}
		
		function edit(){
			var data=$('#list').datagrid('getSelected');
			if(data==""||data==null){
				$.messager.alert("操作提示", "请选择操作数据");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}else{
				window.open('<%=basePath %>publics/transfusion/inpatientApplicationEdit.action?id='+data.id+'&menuAlias='+ss,'输血申请','width='+(window.screen.availWidth-150)+',height='+(window.screen.availHeight-90)+
				',top=20,left=60,resizable=yes,status=yes,menubar=no,scrollbars=yes');
			}
			
		}
		function clear(){
			$('#cardNo').textbox('setValue',"");
			$('#name').textbox('setValue',"");
			$('#state').combobox('setValue',"");
			searchFrom();
		}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
	<div id="divLayout" class="easyui-layout" fit="true">
		<fieldset style="border: 0px;">
			<div id="toolbarId" style="width: 100%;">
				<shiro:hasPermission name="${menuAlias}:function:add">
					<a href="javascript:add();" class="easyui-linkbutton" style="font-size: 14px;" data-options="iconCls:'icon-add',plain:true">添加</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:edit">
					<a href="javascript:edit();" class="easyui-linkbutton" style="font-size: 14px;" data-options="iconCls:'icon-edit',plain:true">编辑</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:delete">
					<a href="javascript:del();" class="easyui-linkbutton" style="font-size: 14px;" data-options="iconCls:'icon-remove',plain:true">删除</a>
				</shiro:hasPermission>
			</div>
		</fieldset>
		<div data-options="region:'north',split:false,border:false" style="width: 100%;height: 50px;padding-top: 10px;padding-left:5px">
			病历号: 
			<input class="easyui-textbox" type="text" id="cardNo"   style="width:160px;"/>
			姓名: 
			<input class="easyui-textbox" type="text" id="name"   style="width:160px;"/>
			状态: 
			<input class="easyui-combobox"  id="state"   style="width:160px;"/>
			<shiro:hasPermission name="${menuAlias}:function:query">
				&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:readCard">
				<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
			</shiro:hasPermission>
        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
			</shiro:hasPermission>
				<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" iconCls="reset">重置</a>
		</div>
			<div id="divLayout" region="center" style="width: 100%;height: 95%;border:0px;"> 
					<table id="list" border="false"
						class="easyui-datagrid" style="font-size: 14px;"
						data-options="method:'post',rownumbers:true,idField: 'id',border:true,singleSelect:true,checkOnSelect:false,selectOnCheck:false,pagination:true,pageSize:20,pageList:[20,40,60,80,100],fit:true,toolbar:'#toolbarId'">
						<thead>
							<tr>
								<th data-options="field:'name'" width="11.5%" style="font-size: 14">
									申请人
								</th>
								<th data-options="field:'applyTime'" width="11.5%" style="font-size: 14">
									申请时间
								</th>
								<th data-options="field:'orderTime'" width="11.5%" style="font-size: 14">
									预定输血日期
								</th>
								<th data-options="field:'bloodKind',formatter:functionxuexing" width="11.5%" style="font-size: 14">
									申请血型
								</th>
								<th data-options="field:'bloodTypeCode',formatter:functionchengfen" width="12.5%" style="font-size: 14">
									血液成分
								</th>



								<th data-options="field:'rh',formatter:functionRH" width="11.5%" style="font-size: 14">
									RH
								</th>
								<th data-options="field:'quantity'" width="11.5%" style="font-size: 14">
									申请输血量
								</th>
								<th
									data-options="field:'stockUnit'" width="11.5%" style="font-size: 14">
									申请数量单位
								</th>
								
								
								<th
									data-options="field:'applyState',formatter:functionzhuangtai" width="10%" style="font-size: 14">
									状态
								</th>


							</tr>
						</thead>
					</table>
 			</div> 

	</div>
</body>
</html>