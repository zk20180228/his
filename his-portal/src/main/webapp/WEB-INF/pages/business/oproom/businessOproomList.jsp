<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>手术房间维护</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/themes/system/css/loader.css">
	<%@ include file="/common/metas.jsp" %>
	<%-- <jsp:include page="/common/head_content.jsp"></jsp:include> --%>
	<style type="text/css">
		.businessOproomList .panel-header{
			border-top:0;
			border-bottom:0;
		}
	</style>
</head> 
<body style="margin: 0px;padding: 0px;">
	<div class="easyui-layout businessOproomList" data-options="fit:true">
		<div id="p" data-options="region:'west',tools:'#toolSMId',border:true,split:true" title="手术房间管理" style="width:15%;">
			<div  id="toolSMId">
				<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
				<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
				<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
			</div>
			<div id="treeDiv"  style="width: 100%;">
				<ul id="tDt">数据加载中.....</ul>  
			</div>
			<div id="tDtmm" class="easyui-menu" style="width:100px;">
				<shiro:hasPermission name="${menuAlias}:function:tadd"> 
					<div onclick="addOproom()" data-options="iconCls:'icon-add'" id="add">添加手术房间</div>
				</shiro:hasPermission>
				 <shiro:hasPermission name="${menuAlias}:function:tedit"> 
					<div onclick="editOproom()" data-options="iconCls:'icon-edit'" id="edit">修改手术房间</div>
				</shiro:hasPermission>
				 <shiro:hasPermission name="${menuAlias}:function:tdelete"> 
					<div onclick="delOproom()" data-options="iconCls:'icon-remove'" id="remove">移除手术房间</div>
				</shiro:hasPermission>
				<div onclick="viewOproom()" data-options="iconCls:'icon-search'" id="search">查看手术房间</div>
			</div>
		 </div>
		<div data-options="region:'center',border:false" style="width: 85%;height: 100%;">
			<div id="divLayout" class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',split:false,border:true" style="width:100%;height: 40px;border-top:0">
					<table cellspacing="0" cellpadding="0" border="0" style="width: 100%;">
						<tr>
							<td style="padding: 5px;">
								<input class="easyui-textbox" ID="queryName" name="queryName" data-options="prompt:'手术台名称,输入码'" style="width: 190px;"/>
								<shiro:hasPermission name="${menuAlias}:function:query">
								&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								</shiro:hasPermission>
								<a href="javascript:void(0)" onclick="clearQuery()" class="easyui-linkbutton" iconCls="reset">重置</a>
							</td>
						</tr>
					</table>
				</div>	
				<div data-options="region:'center',split:false,iconCls:'icon-book',border:true">
					<input type="hidden" id="treeId" name="treeId">
					<table id="list" class="easyui-datagrid"
								data-options="
									iconCls: 'icon-edit',
									singleSelect: true,
									toolbar:'#toolbarId',
									checkOnSelect:true,selectOnCheck:false,
									fitColumns:true,
									autoRowHeight:false,
									pagination:true,
									showFooter:false,
									border:false,
									fit:true
								">
							<thead> 
								<tr>
									<th field="getIdUtil" checkbox="true" style="width: 5%"></th>
								    <th data-options="field:'consoleName',align:'center'"  style="width:20%" >手术台名称</th>
									<th data-options="field:'inputCode',align:'center'" id="yz" style="width:10%">输入码</th>
									<th data-options="field:'deptCode',formatter:formatCheckBox,align:'center'"  style="width:10%" >所属科室</th>
						        	<th data-options="field:'usingState',formatter:funUsingState,align:'center'" style="width: 10%">当前使用状态</th>
									<th data-options="field:'remark',align:'center'" style="width:45%">备注</th>
								</tr>
							</thead>
						</table>
				</div>
			</div>
		</div>
	</div>
	<div id="dept"></div>
	<div id="toolbarId">
		<shiro:hasPermission name="SSFJWH:function:add">
			<a href="javascript:void(0)" onclick="addRoom()"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-add',plain:true">添加</a>
		</shiro:hasPermission>
				<shiro:hasPermission name="SSFJWH:function:edit">
					<a href="javascript:void(0)" onclick="editRoom()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>	
				</shiro:hasPermission>
		<shiro:hasPermission name="SSFJWH:function:delete">
			<a href="javascript:void(0)" onclick="del()"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
	</div>
	<script type="text/javascript">
	var menuAlias="${menuAlias}";
	//科室
	var deptName = "";
	//选中行标
	var editIndex = undefined;
	//存放手术台状态
	var operatTableStatus="";
	$(function(){
		//加载房间树
	   	$('#tDt').tree({    
		    url:'<%=basePath%>operation/oproom/treebusinessOproom.action',
		    method:'get',
		    animate:true,
		    lines:true,
		    formatter:function(node){//统计节点总数
		    		var s = node.text;
		    		if(node.iconCls=='icon-bullet_home'){//hedong 20170306 第三级节点（手术房间）没必要再统计其有多少个子节点故加判断。
						if (node.children){
							s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
						 }
		    	    }
		    		return s;
			},onContextMenu: function(e,node){//添加右键菜单
				e.preventDefault();
				$(this).tree('select',node.target);
				if(node.iconCls=="icon-user_brown"){
					$("#add").hide();
					$("#search").show();
					$("#edit ").show();
					$("#remove").show();
					$('#tDtmm').menu('show',{
						left: e.pageX,
						top: e.pageY
					});
				}
				if(node.iconCls=='icon-bullet_home'){
					$("#add").show();
					$("#search").hide();
					$("#edit ").hide();
					$("#remove").hide();
					$('#tDtmm').menu('show',{
						left: e.pageX,
						top: e.pageY
					});
				}
			},onClick: function(node){//点击节点
				$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
				closeLayout();
				$('#treeId').val(node.id);
				$('#list').datagrid('load', {
					id: node.id,
					isdept:$('#tDt').tree('getSelected').attributes.isdept
				});
				$('#modelWeek').val();
			},onLoadSuccess:function(node,data){
				$('#tDt').tree('select',$('#tDt').tree('find', 1).target);
			},onBeforeCollapse:function(node){
				 if(node.id=="1"){
					return false;
				} 
			}
		});
		
		//渲染手术台状态
		$.ajax({
			url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?',
			data:{"type" : "operatTableStatus"},
			type:'post',
			success: function(data) {
				operatTableStatus=data;
			}
		});	
		//渲染科室
		$.ajax({
			url: '<%=basePath %>operation/oproomsole/deptNameCombobox.action',
			type:'post',
			success: function(deptData) {
				deptName = deptData;
			}
		});	
		setTimeout(function(){
			var id="${id}"; //存储数据ID
			//添加edatagrid事件及分页
			$('#list').datagrid({
				url:'<%=basePath %>operation/oproomsole/queryBusinessOpconsole.action?menuAlias=${menuAlias}',
				pageSize:20,
		   		pageList:[20,30,50,100],
		   		onLoadSuccess: function (data) {//默认选中
		            var rowData = data.rows;
		            $.each(rowData, function (index, value) {
		            	if(value.id == id){
		            		$("#list").edatagrid("checkRow", index);
		            	}
		            });
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
					}
					
	  	        },onDblClickRow: function (rowIndex, rowData) {//双击查看
	  	        	if($('#tDt').tree('getSelected').attributes.isdept=='N'){//2017-03-03 hedong  目前修改手术台是根据具体择手术室来进行修改的 不然会报错（room_code外键）,故加一层判断。
		  	        	var ids = rowData.id;
		  	        	var treeId = $('#treeId').val();
		  				AddOrShowEast('修改手术台','<%=basePath%>operation/oproomsole/oproomEdit.action?id='+ids+'&treeId='+treeId);
	  	        	}else{
	  	      	  	    $.messager.alert("提示","请先选择具体手术室！");
		  	      	  	setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
	  	  	        }
				} 
			});		
            },600);
		bindEnterEvent('queryName',searchFrom,'easyui');
	});

	//使用状态的判断
	function funUsingState(value,row,index){
		for(var i=0;i<operatTableStatus.length;i++){
			if(value==operatTableStatus[i].encode){
				return operatTableStatus[i].name;
			}
		}
	}
	
	/**
	 * 添加手术台
	 * @author  zhangjin
	 * @date 2017-02-27
	 * @version 1.0
	 */
	function addRoom(){
		var treeId = $('#treeId').val();
	    if($('#tDt').tree('getSelected').attributes.isdept=='N'){
	    	$('#list').datagrid('clearSelections');
	    	AddOrShowEast('添加手术台','<%=basePath%>operation/oproomsole/oproomAdd.action?treeId='+treeId);
	    }else{
    	  	$.messager.alert("提示","请先选择具体手术室！");
    	  	setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
	    }
	}
	
	/**
	 * 修改手术台
	 * @author  zhangjin
	 * @date 2017-02-27
	 * @version 1.0
	 */
	function editRoom(){
		if($('#tDt').tree('getSelected').attributes.isdept=='N'){//2017-03-03 hedong  目前修改手术台是根据具体择手术室来进行修改的 不然会报错（room_code外键）,故加一层判断。
			var rows = $('#list').datagrid('getSelected');
			if(!rows){
				$.messager.alert("提示","请选择至少一条数据");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}
		    var ids = rows.id;
		    var treeId = $('#treeId').val();
			AddOrShowEast('修改手术台','<%=basePath%>operation/oproomsole/oproomEdit.action?id='+ids+'&treeId='+treeId);
		}else{
		    $.messager.alert("提示","请先选择具体手术室！");
		    setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}    
	 }
	/**
	 * 删除
	 * @author  zhangjin
	 * @param 
	 * @param index =点击行的index
	 * @date 2017-02-27
	 * @version 1.0
	 */
	 function del(){
		var rows = $('#list').datagrid('getChecked');
        var ids = '';
 			for(var i=0;i<rows.length;i++){   
 				if(rows[i].id==null){//如果id为null 则为新添加行
 					var dd = $('#list').edatagrid('getRowIndex',rows[i]);//获得行索引
 					$('#list').edatagrid('deleteRow',dd);//通过索引删除该行
 				}else{
 					if(rows[i].usingState==0){
 						if(ids!=''){
 	 						ids+=',';
 	 					}
 	 					ids += rows[i].id;
 					}else{
 						$.messager.alert("提示","该手术台正在使用，不能进行删除。");
 						setTimeout(function(){
 							$(".messager-body").window('close');
 						},3500);
 						return;
 					}
 					
 				}
			}
 			if(ids!=null&&ids!=""){
 				closeLayout();
	 			$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
					if (res){
						$.messager.progress({text:'删除中，请稍后...',modal:true});
						$.ajax({
							url: '<%=basePath%>operation/oproomsole/delBusinessOpconsole.action?id='+ids,
							type:'post',
							success: function(data) {
								$.messager.progress('close');
								$.messager.alert("提示",data.resMsg);
								if("success"==data.resCode){
									$('#list').edatagrid('reload');
								}
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}
						});
					}
	            });
 			}else{//hedong 20170306   添加删除的判断 未选中时提示用户选择要删除的手术台
 				$.messager.alert("提示",'请先选择要删除的手术台！');	
 				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
 			}
	  }
	 /**
		 *点击事件
		 * @author  zhangjin
		 * @param 
		 * @param index =点击行的index
		 * @date 2017-02-27
		 * @version 1.0
		 */
	function onClickRow(index){
			if (editIndex != index){
				if (true){
					$('#list').edatagrid('selectRow', index)
							.edatagrid('beginEdit', index);
					editIndex = index;
				} else {
					$('#list').edatagrid('selectRow', editIndex);
				}
			}
		}
		//清除所填信息
		function clear(){
			$('#editForm').form('clear');
		}
		function closeLayout(){
			$('#divLayout').layout('remove','east');
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
			var queryName = $('#queryName').val().trim();
			var id=$('#tDt').tree('getSelected').id;
			var isdept= $('#tDt').tree('getSelected').attributes.isdept;
			$('#list').edatagrid('load', {
				queryName:queryName,
				id:id,
				isdept:isdept
			});
			
		}
		/**
		 * 重置
		 * @author  houzhaoqi
		 * @date 2017-3-16
		 * @version 1.0
		 */
		 function clearQuery(){
			$("#queryName").textbox('clear');
			searchFrom();
		}
		 
		//获得选中id	
		function getId(tableID,str){
			var row = $(tableID).datagrid("getChecked");  
			var dgID = "";
			if(row.length < 1){
				$.messager.alert("操作提示","请选择一条记录！","warning");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
			var i = 0;
			for(i;i < row.length;i++){
				if(str = 0){
					dgID += "\'" + row[i].BED_ID + "\'";
				}else{
					dgID += row[i].BED_ID;
				}
				if(i < row.length - 1){
					dhID += ',';
				}else{
					break;
				}
			}
			return dgID;
		}
		
		//手术房间树操作
	   	function refresh(){//刷新树
	   		$('#tDt').tree('options').url = "<%=basePath%>operation/oproom/treebusinessOproom.action";
			$('#tDt').tree('reload'); 
		}
	   	function expandAll(){//展开树
			$('#tDt').tree('expandAll');
		}
	   	function collapseAll(){//关闭树
			$('#tDt').tree('collapseAll');
		}
	   	function addOproom(){//添加部门科室
	   		var id = getSelected();
	   		Adddilog("添加房间",'<%=basePath%>operation/oproom/addTreebusinessOproom.action?menuAlias='+menuAlias+'&id='+id);
		}
	   	function editOproom(){//修改部门科室
	   		var id = getSelected();
	   		Adddilog("编辑房间",'<%=basePath%>operation/oproom/eidtTreebusinessOproom.action?menuAlias='+menuAlias+'&id='+id);
	   		
		}
		function viewOproom(){//查看部门科室
	   		var id = getSelected();
	   		Adddilog("查看房间",'<%=basePath%>operation/oproom/viewTreeBusinessOproom.action?menuAlias='+menuAlias+'&id='+id);
		}
	   	function delOproom(){//移除部门科室
	   		if($('#tDt').tree('getSelected').attributes.isdept=='N'){
	   			$.messager.confirm('确认', '确定要删除选中信息吗?', function(){//提示是否删除
					$.ajax({
						url: '<%=basePath%>operation/oproom/delTreebusinessOproom.action?id='+getSelected(),
						type:'post',
						success: function(data) {
							$.messager.alert("提示",data.resMsg);
							if("success"==data.resCode){
								$('#tDt').tree('reload');
								$('#list').datagrid('reload');
							}
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							
						}
					});										
				});
	   		}else{
	   			$.messager.alert('警告','请选择具体房间删除');  
	   			setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
	   		}
	   		
		}
		//加载dialog
			function Adddilog(title, url) {
				$('#dept').dialog({    
				    title: title,    
				    width: '30%',    
				    height:'50%',    
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true   
				   });    
			}
			//打开dialog
			function openDialog() {
				$('#dept').dialog('open'); 
			}
			//关闭dialog
			function closeDialog() {
				$('#dept').dialog('close');  
			}
		   	
		   	//添加弹出框
			function showWin ( title,url, width, height) {
			    var content = '<iframe src="' + url + '" width="100%" height="99%" frameborder="0" scrolling="no"></iframe>';
			    var divContent = '<div id="treeDeparWin">';
			    var win = $('<div/>').dialog({
			        content: content,
			        width: width,
			        height: height,
			        modal: true,
			        resizable:true,
			        shadow:true,
			        center:true,
			        title: title,
			    });
			    win.dialog('open');
			    
			}

			function getSelected(){//获得选中节点
				var node = $('#tDt').tree('getSelected');
				if (node){
					var id = node.id;
					return id;
				}
			}
			//科室的id转变成deptName  deptCode
			function formatCheckBox(value,row,index){
				if(value){
					return deptName[value];
				}
				return "";
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
			   			title:title,
                           href:url 
                    });
				}else{//打开新面板
					$('#divLayout').layout('add', {
						region : 'east',
						title:title,
						width : 580,
						href : url
					});
				}
			}
				
			//关闭Layout
			function closeLayout() {
				$('#divLayout').layout('remove', 'east');
			}
			
</script>
  </body>
</html>
