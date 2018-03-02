<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north'" style="height:40px;width:100%;padding:5px 5px 0px 0px;border:0px solid #95b8e7;">
		<input type="hidden" id="id" name="id" value="${registerGrade.id }">
		<table style="width:100%;border:0px solid #95b8e7;padding-left:5px">
			<tr>
				<td>查询条件：<input  id="codes" data-options="prompt:'级别代码,拼音,五笔,自定义'"  class="easyui-textbox"  style="width: 200px"/>
				&nbsp;<input id="nameCodes" data-options="prompt:'挂号级别'"  style="width:200px" />
				<shiro:hasPermission name="${menuAlias}:function:query">
				&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
				</shiro:hasPermission>
				<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center'" style="height:90%;width:100%;border-left:0;border-right:0;">
		<table id="list" data-options="url:'${pageContext.request.contextPath}/outpatient/grade/queryGrade.action?menuAlias=${menuAlias}',iconCls: 'icon-edit',singleSelect: true,toolbar: '#toolbarId',fit:true" style="border:0px;"> 
			<thead>
				<tr>
					<th field="getIdUtil" checkbox="true" ></th>
					<th data-options="field:'code'"  style="width:10%" >级别代码</th>
					<th data-options="field:'name'" style="width:10%">级别名称</th>
					<th data-options="field:'expertno'" style="width:10% " formatter="formatCheckBox">是否专家号</th>
					<th data-options="field:'specialistno'" style="width:10%" formatter="formatCheckBox">是否专科号</th>
					<th data-options="field:'specialdiagnosisno'" style="width:10% " formatter="formatCheckBox">是否特诊号</th>
					<th data-options="field:'isdefault'"  style="width:10% " formatter="formatCheckBox">默认标识</th>
					<th data-options="field:'codePinyin'" style="width:8%">拼音码</th>
					<th data-options="field:'codeWb'" style="width:8%">五笔码</th>
					<th data-options="field:'codeInputcode'" style="width:10%">自定义码</th>
					<th data-options="field:'description',align:'center'" style="width:10%">说明</th>
				</tr>
			</thead>
		</table>
	</div>  
</div>
<div id="toolbarId">
<shiro:hasPermission name="${menuAlias}:function:add">
	<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
</shiro:hasPermission>
<shiro:hasPermission name="${menuAlias}:function:edit">
	<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
</shiro:hasPermission>
<shiro:hasPermission name="${menuAlias}:function:delete">		
	<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
</shiro:hasPermission>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script>
var editIndex = undefined;
$(function(){
    var winH=$("body").height();
	//$('#p').height(winH-78-30-27-2);//78:页面顶部logo的高度，30:菜单栏的高度，27：tab栏的高度，
	$('#list').height(winH-78-30-27-26);
	var id="${id}"; //存储数据ID
	//添加edatagrid事件及分页
	$('#list').datagrid({
		striped:true,
		checkOnSelect:true,
		selectOnCheck:false,
		singleSelect:true, 
		fitColumns:false,
		pagination:true,
		rownumbers:true,
   		pageSize:20,
   		pageList:[20,30,50,100],
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
		}
        });
	/**
	 * 回车弹出级别名称选择窗口
	 * @author  wanxing
	 * @date 2016-03-22  15:34
	 * @version 1.0
	 */
	 bindEnterEvent('nameCodes',popWinToNameCode,'easyui');//绑定回车事件
	 /**
		 * 级别代码框回车查询
		 * @author  GH
		 * @date 2016-11-03  10.23
		 * @version 1.0
		 */
	 bindEnterEvent('codes',searchFrom,'easyui');//绑定回车事件
	 function popWinToNameCode(){
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=title&textId=nameCodes";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth-300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	}
});
		        
    function add(){
		AddOrShowEast('EditForm',"<c:url value='/outpatient/grade/gradeAdd.action'/>");
	}
			
	function edit(){
  			var row = $('#list').datagrid('getSelected'); //获取当前选中行                        
                 if(row){
                    	AddOrShowEast('EditForm',"<c:url value='/outpatient/grade/gradeEdit.action'/>?id="+row.id);
   		}
	}
   //删除	
	function del(){
		var rows = $('#list').datagrid('getChecked');
        	if (rows.length > 0) {//选中几行的话触发事件	                        
	 		$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
				if (res){
					var ids = '';
					for(var i=0; i<rows.length; i++){
						if(ids!=''){
							ids += ',';
						}
						ids += rows[i].id;
					};
					$.messager.progress({text:'保存中，请稍后...',modal:true});
					$.ajax({
					 	url: "<c:url value='/outpatient/grade/delGrade.action'/>?id="+ids,   
						type:'post',
						success: function() {
							$.messager.progress('close');
							$.messager.alert('提示','删除成功');
							setTimeout(function(){
								  $(".messager-body").window('close');  
								},3500);
							$('#list').datagrid('reload');
						}
					});
				}
            });
           }
	}

function reload(){
	//实现刷新栏目中的数据
	$('#list').datagrid('reload');
}    
	
//清除所填信息
function clear(){
	$('#editForm').form('clear');
}
function closeLayout(){
	$('#divLayout').layout('remove','east');
}
/**
 * 格式化复选框
 * @author  
 * @date 2015-5-26 9:25       
 * @version 1.0
 */
function formatCheckBox(val,row){

	if (val == 1){
		return '是';
	} else {
		return '否';
	}
}
			
/**
 * 查询
 * @author  sunshuo
 * @param title 标签名称
 * @param title 跳转路径
 * @date 2015-05-21
 * @version 1.0
 */
function searchFrom() {
	var code = $('#codes').val();
	var name = $('#nameCodes').textbox('getText');
	$('#list').edatagrid('load', {
		code : code,
		name : name
	});
}
/**
 * 重置
 */
function searchReload(){
	delSelectedData('codes');
	delSelectedData('nameCodes');
	$('#codes').val("");
	$('#nameCodes').val("");
	searchFrom();
}
/**
 * 动态添加标签页
 * @author  sunshuo
 * @param title 标签名称
 * @param title 跳转路径
 * @date 2015-05-21
 * @version 1.0
 */
function AddOrShowEast(title, url) {
	$('#divLayout').layout('add', {
		region : 'east',
		width : 480,
		split : true,
		href : url,
		closable : true
	});
}
		
/**
 * 动态添加LayOut
 * @author  liujl
 * @param title 标签名称
 * @param url 跳转路径
 * @date 2015-05-21
 * @modifiedTime 2015-6-18
 * @modifier liujl
 * @version 1.0
 */
function AddOrShowEast(title, url) {
	var eastpanel=$('#panelEast'); //获取右侧收缩面板
	if(eastpanel.length>0){ //判断右侧收缩面板是否存在
		//重新装载右侧面板
   		$('#divLayout').layout('panel','east').panel({
                        href:url 
                 });
	}else{//打开新面板
		$('#divLayout').layout('add', {
			region : 'east',
			width : 480,
			split : true,
			href : url,
			closable : true
		});
	}
}
	
$('#nameCodes').combobox({    
	url: "<c:url value='/outpatient/grade/titleCombobox.action'/>", 
    valueField:'encode',    
    textField:'name',
    multiple:false,
    filter:function(q,row){
		var keys = new Array();
		keys[keys.length] = 'name';
		keys[keys.length] = 'encode';
		keys[keys.length] = 'pinyin';
		keys[keys.length] = 'wb';
		return filterLocalCombobox(q, row, keys);
    },
    onSelect:function(record){
    	var code = $('#codes').val();
    	$('#list').edatagrid('load', {
			name : record.name,
			code : code
		});
    }
});

</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>