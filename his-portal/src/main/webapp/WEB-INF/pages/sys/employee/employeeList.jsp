<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<script>
		var sexMap=new Map();
		//性别渲染
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
	
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</head>
	<body>
		<div class="easyui-layout" class="easyui-layout" fit=true
			style="width: 100%; height: 100%; overflow-y: auto;">
			<div id="p" data-options="region:'west',split:true ,tools:'#toolSMId'"" title="部门科室"
				style="width:300px;overflow: hidden;height: 35px;padding-top: 5px">
				<div id="toolSMId">
				<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
				<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
				<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
				</div>
				&nbsp;<input id="searchDept" class="easyui-combobox" style="width: 200px;" data-options="prompt:'拼音,五笔,自定义,编码,名称'"  />
				<a href="javascript:clearComboboxValue('searchDept');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
				<div style="width: 100%; height: 100%; overflow-y: auto;">
					<ul id="tDt"></ul>
				</div>
			</div>
			<div data-options="region:'center'" style="border-top:0">
				<div id="divLayout" class="easyui-layout" fit=true
					style="width: 100%; height: 100%; ">
					<div id="divLayout" class="easyui-layout" data-options="fit:true">
						<div data-options="region:'north',split:false,border:false" style="width:100%;height: 40px;">
							<form id="search" method="post">
								<table
									style="width: 100%; border: 0px; padding: 5px 5px 0px 5px;">
									<tr>
										<td style="width: 500px;">
										查询条件:<input class="easyui-textbox" id="queryName"  name="queryName"  data-options="prompt:'姓名,拼音,五笔,自定义,工作号'"onkeydown="KeyDown(0)"style="width: 230px;"/>
										员工类型:<input id="workType" class="easyui-combobox" style="width:120px"/>
										职务:<input id="workPost" class="easyui-combobox" style="width:120px"/>
										职称:<input id="workTitle" class="easyui-combobox" style="width:120px"/>
											<a href="javascript:void(0)" onclick="searchFrom()"
												class="easyui-linkbutton" iconCls="icon-search">查询</a>
												<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
										<input id="deptid" type="hidden"></input>		
										</td>
									</tr>
								</table>
							</form>
						</div>
						<div data-options="region:'center',split:false,iconCls:'icon-book',border:true" style="border-left:0">
							<table id="list" 
								data-options="url:'${pageContext.request.contextPath}/baseinfo/employee/queryEmployee.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
								<thead>
									<tr>
										<th data-options="field:'ck',checkbox:true" ></th>
										<th data-options="field:'jobNo', width : 120">工作号</th>
										<th data-options="field:'name', width : 120" >姓名</th>
										<th data-options="field:'inputCode',width :70" >自定义码</th>
										<th data-options="field:'sex' ,width :50,formatter: function(value,row,index){
																									return sexMap.get(value);
																									}" >性别</th>
										<th data-options="field:'family', width : '90',formatter:familyFamater" >民族</th>
										<th data-options="field:'birthday', width : 90" >出生日期</th>
										<th data-options="field:'education', width : 90,formatter:degreeFamater" >学历</th>
										<th data-options="field:'type', width : 90,formatter:typeFormatter" >员工类型</th>						
										<th data-options="field:'post', width : 90,formatter:dutiesFamater" >职务</th>
										<th data-options="field:'title', width : 90,formatter:titleFamater" >职称</th>
										<th data-options="field:'mobile', width : 150 ">电话</th> 
										<th data-options="field:'isExpert' ,formatter:formatCheckBox, width : 100" >是否是专家</th>
										<th data-options="field:'remark' , width : 170" >备注</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
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
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="resetEmailpwd()" class="easyui-linkbutton" data-options="iconCls:'icon-resetPassward',plain:true">重置邮箱密码</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="resetWagespwd()" class="easyui-linkbutton" data-options="iconCls:'icon-resetPassward',plain:true">重置工资查询密码</a>
		</shiro:hasPermission>
		
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="synChEmpInfo()" class="easyui-linkbutton" data-options="iconCls:'reset',plain:true">同步员工扩展信息</a>
		</shiro:hasPermission>
		</div>
		<div id="photoImageWin"><img id="photoImageWinImg"></div>  
		<div id="esignImageWin"><img id="esignImageWinImg"></div>
		<script type="text/javascript">
		var nationalityMap=new Map();
			var dutiesList=null;
			var drgreeList=null;
			var titleList=null;
			var deptid=null;
			var typeList = null;
			var havSelect = true;
			//员工类型下拉框 
			$('#workType').combobox({
				url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=empType'/>",
				valueField:'encode',
				textField:'name',
				multiple:false,
				filter:function(q,row){
					var keys = new Array();
					keys[keys.length] = 'encode';
					keys[keys.length] = 'name';
					keys[keys.length] = 'pinyin';
					keys[keys.length] = 'wb';
					if(filterLocalCombobox(q, row, keys)){
						row.selected=true;
					}else{
						row.selected=false;
					}
					return filterLocalCombobox(q, row, keys);
			    },
				onLoadSuccess:function(data){
					if(data!=null && data.length==1){
						var code= data[0].encode;
						$('#workType').combobox('select',code);
					}
				},
				onSelect:function(rec){
					var code=rec.encode;
					havSelect = false;
				},
				onHidePanel:function(){
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
					if(havSelect){
						var isOnly = 0;
						var onlyOne = null;
						for(var i = 0;i<$("#workType").combobox("getData").length;i++){
							if($("#workType").combobox("getData")[i].selected){
								isOnly++;
								onlyOne = $("#workType").combobox("getData")[i];
							}
						}
						if((isOnly-1)==0){
							var encode = onlyOne.encode;
							$('#workType').combobox('setValue',deptMap[encode]);
							$('#workType').combobox('select',encode);
						}
					}
					havSelect=true;							
				}
			});
			//职务下拉框
			$('#workPost').combobox({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=duties'/>",
				valueField : 'encode',
				textField : 'name',
				filter:function(q,row){
					var keys = new Array();
					keys[keys.length] = 'encode';
					keys[keys.length] = 'name';
					keys[keys.length] = 'pinyin';
					keys[keys.length] = 'wb';
					if(filterLocalCombobox(q, row, keys)){
						row.selected=true;
					}else{
						row.selected=false;
					}
					return filterLocalCombobox(q, row, keys);
			    },
				onLoadSuccess:function(data){
					if(data!=null && data.length==1){
						var code= data[0].encode;
						$('#workPost').combobox('select',code);
					}
				},
				onSelect:function(rec){
					var code=rec.encode;
					havSelect = false;
				},
				onHidePanel:function(){
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
					if(havSelect){
						var isOnly = 0;
						var onlyOne = null;
						for(var i = 0;i<$("#workPost").combobox("getData").length;i++){
							if($("#workPost").combobox("getData")[i].selected){
								isOnly++;
								onlyOne = $("#workPost").combobox("getData")[i];
							}
						}
						if((isOnly-1)==0){
							var encode = onlyOne.encode;
							$('#workPost').combobox('setValue',deptMap[encode]);
							$('#workPost').combobox('select',encode);
						}
					}
					havSelect=true;							
				}
			});	
			//职称下拉框
			$('#workTitle').combobox({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=title'/>",
				valueField : 'encode',
				textField : 'name',
				filter:function(q,row){
					var keys = new Array();
					keys[keys.length] = 'encode';
					keys[keys.length] = 'name';
					keys[keys.length] = 'pinyin';
					keys[keys.length] = 'wb';
					if(filterLocalCombobox(q, row, keys)){
						row.selected=true;
					}else{
						row.selected=false;
					}
					return filterLocalCombobox(q, row, keys);
			    },
				onLoadSuccess:function(data){
					if(data!=null && data.length==1){
						var code= data[0].encode;
						$('#workTitle').combobox('select',code);
					}
				},
				onSelect:function(rec){
					var code=rec.encode;
					havSelect = false;
				},
				onHidePanel:function(){
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
					if(havSelect){
						var isOnly = 0;
						var onlyOne = null;
						for(var i = 0;i<$("#workTitle").combobox("getData").length;i++){
							if($("#workTitle").combobox("getData")[i].selected){
								isOnly++;
								onlyOne = $("#workTitle").combobox("getData")[i];
							}
						}
						if((isOnly-1)==0){
							var encode = onlyOne.encode;
							$('#workTitle').combobox('setValue',deptMap[encode]);
							$('#workTitle').combobox('select',encode);
						}
					}
					havSelect=true;							
				}
			});	
			$.ajax({//下拉框员工类型 
			      url :"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action" ,  
				    type : "post",  
					data:{"type":"empType"},
					success: function(list) { 
						typeList=list;
						
				    }
		    });	
			$.ajax({
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
				data:{type : "nationality"},
				type:'post',
				success: function(nationalityData) {
					var len=nationalityData.length;
					for( var i=0;i<len;i++){
						nationalityMap.put(nationalityData[i].encode,nationalityData[i].name);
					}
				}
			});
			$.ajax({
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
				data:{type : "duties"},
				type:'post',
				success: function(dutiesData) {
					dutiesList = dutiesData;
				}
			});
			$.ajax({
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
				data:{type : "degree"},
				type:'post',
				success: function(drgreeData) {
					drgreeList = drgreeData;
				}
			});
			
			$.ajax({
				url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
				data:{type : "title"},
				type:'post',
				success: function(titleData) {
					titleList = titleData;
				}
			});
		//科室下拉框及时定位查询 
		$('#searchDept').combobox({
			url: "<%=basePath%>baseinfo/department/departmentCombobox.action", 
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
					$('#searchDept').combobox('select',code);
					selectNode(code);
				}
			},
			onSelect:function(rec){
				var code=rec.deptCode;
				havSelect = false;
				selectNode(code);
			},
			onHidePanel:function(){
			 	var data = $(this).combobox('getData');
			    var val = $(this).combobox('getValue');
			    var result = true;
			    for (var i = 0; i < data.length; i++) {
			        if (val == data[i].deptCode) {
			            result = false;
			        }
			    }
			    if (result) {
			        $(this).combobox("clear");
			    }else{
			        $(this).combobox('unselect',val);
			        $(this).combobox('select',val);
			    }
				if(havSelect){
					var isOnly = 0;
					var onlyOne = null;
					for(var i = 0;i<$("#searchDept").combobox("getData").length;i++){
						if($("#searchDept").combobox("getData")[i].selected){
							isOnly++;
							onlyOne = $("#searchDept").combobox("getData")[i];
						}
					}
					if((isOnly-1)==0){
						var depCode = onlyOne.deptCode;
						$('#searchDept').combobox('setValue',deptMap[depCode]);
						$('#searchDept').combobox('select',depCode);
						selectNode(depCode);
					}
				}
				havSelect=true;							
			}
		});
		//清除科室信息查询下拉框，查询条件的值后,动态加载页面
	   	function clearComboboxValue(id){
			delSelectedData(id);
	   		delSelectedData('queryName');
	   		refresh();
	   		$('#deptid').val("");
	   		var deptid = $('#deptid').val();
			var queryName = $.trim($('#queryName').textbox('getValue'));
			$('#list').datagrid('load', {
				ename : queryName,
				dname: deptid,
				dtype: ""
			});
	   	}
			//加载页面
			$(function(){
				var id="${id}"; //存储数据ID
				//添加datagrid事件及分页
				$('#list').datagrid({
					pagination:true,
			   		pageSize:20,
			   		pageList:[10,20,30,40,50],
			   		onBeforeLoad:function (param) {
						//GH 2017年2月17日 翻页时清空前页的选中项
						$('#list').datagrid('clearChecked');
						$('#list').datagrid('clearSelections');
			        },
					onLoadSuccess: function (data) {//默认选中
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
			           var rowData = data.rows;
			            $.each(rowData, function (index, value) {
			            	if(value.id == id){
			            		$("#list").datagrid("checkRow", index);
			            	}
			            });
			        },onDblClickRow: function (rowIndex, rowData) {//双击查看
							if(getIdUtil("#list").length!=0){
								Adddilog("<c:url value='/baseinfo/employee/viewEmployee.action'/>?id="+getIdUtil("#list"));
						   	}
						}    
					});
					bindEnterEvent('queryName',searchFrom,'easyui');
			});
		
			
			
			function add(){
				   Adddilog("<c:url value='/baseinfo/employee/addEmployee.action'/>?deptid="+$('#deptid').val());
				   $('#list').datagrid('reload');
			}
			
			function edit(){
				var row = $('#list').datagrid('getSelected'); //获取当前选中行     
	            if(row != null){
                   	Adddilog("<c:url value='/baseinfo/employee/editEmployee.action'/>?id="+row.id);
                   	$('#list').datagrid('reload');
		   		}else{
		   			$.messager.alert("操作提示", "请选择一条记录！", "warning");
		   		}
			}
			function resetEmailpwd(){
				var row = $('#list').datagrid('getSelected'); //获取当前选中行     
	            if(row != null){
	            	var url = "<%=basePath%>baseinfo/employee/editEmailpwd.action?menuAlias=${menuAlias}&id=" + row.id;
	            	window.open(url,'newwindow',' left=373,top=249,width=600,height=300');
		   		}else{
		   			$.messager.alert("操作提示", "请选择一条记录！", "warning");
		   		}
			}
			function resetWagespwd(){
				var row = $('#list').datagrid('getSelected'); //获取当前选中行     
	            if(row != null){
	            	var url = "<%=basePath%>baseinfo/employee/editWagespwd.action?menuAlias=${menuAlias}&id=" + row.id;
	            	window.open(url,'newwindow',' left=373,top=249,width=600,height=300');
		   		}else{
		   			$.messager.alert("操作提示", "请选择一条记录！", "warning");
		   		}
			}
			
			function del(){
				 //选中要删除的行
		        var iid = $('#list').datagrid('getChecked');
		        if (iid.length > 0) {//选中几行的话触发事件	                        
				 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
						if (res){
							var ids = '';
							for(var i=0; i<iid.length; i++){
								if(ids!=''){
									ids += ',';
								}
								ids += iid[i].id;
							};
							$.ajax({
								url: "<c:url value='/baseinfo/employee/delEmployee.action'/>?id="+ids,
								type:'post',
								success: function() {
									$.messager.alert('提示','删除成功');
								$('#list').datagrid('reload');
								}
							});										
						}
			        });
		        }else{
          	    	 $.messager.alert('提示信息','请选择要删除的信息！');
        	    	 setTimeout(function(){
        					$(".messager-body").window('close');
        				},3500);
        	     }	
			}
	
			function reload(){
				//实现刷新栏目中的数据
				 $("#list").datagrid("reload");
			}
			
			function typeFormatter(value, row, index){
				if (value != null) {
					for ( var i = 0; i < typeList.length; i++) {
						if (value == typeList[i].encode) {//id改为encode
							return typeList[i].name; //value改为name 
						}
					}
				}
			}		
					
	   			/**
				 * 格式化复选框
				 * @author  liujinliang
				 * @date 2015-5-26 9:25
				 * @version 1.0
				 */
				function formatCheckBox(val){
					var text = '';
					switch (val) {
					case 0:
						text = '否 ';
						break;
					case 1:
						text = '是 ';
						break;
					default:
						text='否';
						break;
					}
					return text;
				}
			
				/**
				 * 查询
				 */
				function searchFrom() {
					var node = $('#tDt').tree('getSelected')
					var queryName = $.trim($('#queryName').textbox('getValue'));
					var workPost = $('#workPost').combobox('getValue');
					var workTitle = $('#workTitle').combobox('getValue');
					var workType = $('#workType').combobox('getValue');
					var deptid = $('#deptid').val();
					$('#list').datagrid('load', {
						ename : queryName,
						dname: deptid,
						dtype:node.attributes.hasson,
						workTitle:workTitle,
						workPost:workPost,
						workType:workType
					});
				}
			//加载dialog
			function Adddilog(url) {//是否有滚动条,是否居中显示,是否可以改变大小
				    return window.open(url,'newwindow',' left=273,top=49,width='+ (screen.availWidth -505) +',height='+ (screen.availHeight-152));
			}

			/**
			 * 回车键查询
			 * @author  liujl
			 * @param flg 标识：0=查询；1=编辑
			 * @date 2015-05-27
			 * @version 1.0
			 */
			function KeyDown(flg)  
			{   
		    	if(flg==0){
				    if (event.keyCode == 13)  
				    { 
				        event.returnValue=false;  
				        event.cancel = true;  
				        searchFrom();  
				    }  
			    }
			} 
   
   			/**
			 * 在列别页面插入科室树
			 * @author  zpty
			 * @param 
			 * @date 2015-06-03
			 * @version 1.0
			 */		
			//加载部门树
		   	$('#tDt').tree({    
		   		url:"<c:url value='/baseinfo/department/treeDepartmen.action'/>",
			    method:'get',
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
				},
				onLoadSuccess : function(node, data) {
					var n=$('#tDt').tree('find',1);
					$('#tDt').tree('select',n.target);
					if(data.length>0){//节点收缩
						$('#tDt').tree('collapseAll');
					}
				},
				onClick: function(node){//点击节点
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
					deptid=node.id;
				    $('#deptid').val(deptid);
					$('#list').datagrid('load', {
						dname: node.id,
						dtype: node.attributes.hasson
					});
					
				},
				onBeforeCollapse:function(node){
					if(node.id=="1"){
						return false;
					}
				}
			}); 
		   	
		   	
		   	//科室部门树操作
		   	function refresh(){//刷新树
		   		$('#deptid').val('');
		   		$('#tDt').tree('options').url = "<c:url value='/baseinfo/department/treeDepartmen.action'/>";
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
					var id = node.id;
					return id;
				}
			}

			/**
			 * 树查询的方法
			 * @author  zpty
			 * @param 
			 * @date 2015-06-03
			 * @version 1.0
			 */
			function searchTree(){//刷新树
	   			$.ajax({
						url: "<c:url value='/baseinfo/employee/searchTree.action'/>?searcht="+encodeURI(encodeURI($('#searchTree').val())),//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
						type:'post',
						success: function(data) {
							$('#tDt').tree('collapseAll');//单独展开一个节点,先收缩树
							var node = $('#tDt').tree('find',data);
							$('#tDt').tree('expandTo', node.target).tree('select', node.target); //展开指定id的节点
							$("#list").datagrid("uncheckAll");
							$('#list').datagrid('load', {
								deptName: data
							});
						}
					});					
		}

		/**
		 * 为树查询的方法加入回车查询方式
		 * @author  zpty
		 * @param 
		 * @date 2015-06-10
		 * @version 1.0
		 */
		function KeyDownTree(){
		    if (event.keyCode==13){  
		    	event.returnValue=false;  
		        event.cancel = true;  
		        searchTree(); 
		    }
		}
		/**
		 * 格式化生日
		 * @author  zpty
		 * @date 2015-6-19 9:25
		 * @version 1.0
		 */
		function formatDatebox(value) {
            if (value == null || value == '') {
                return '';
            }
            var dt;
            if (value instanceof Date) {
                dt = value;
            } else {

                dt = new Date(value);

            }
            return dt.format("yyyy-MM-dd"); //扩展的Date的format方法
        }
        //扩展的Date的format方法
		Date.prototype.format = function(format) {
	        var o = {
	            "M+": this.getMonth() + 1, // month
	            "d+": this.getDate(), // day
	            "h+": this.getHours(), // hour
	            "m+": this.getMinutes(), // minute
	            "s+": this.getSeconds(), // second
	            "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
	            "S": this.getMilliseconds()
	            // millisecond
	        };
	        if (/(y+)/.test(format))
	            format = format.replace(RegExp.$1, (this.getFullYear() + "")
	                .substr(4 - RegExp.$1.length));
	        for (var k in o)
	            if (new RegExp("(" + k + ")").test(format))
	                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
	        return format;
	    };
		//显示民族格式化
		function familyFamater(value){
			if(nationalityMap.get(value)){
				return nationalityMap.get(value);
			}else{
				return value;
			}
		}
		//显示职位格式化
		function dutiesFamater(value){
			var duties=value;
			if(value!=null){
				for(var i=0;i<dutiesList.length;i++){
					if(value==dutiesList[i].encode){
						duties=dutiesList[i].name;
						break;
					}
				}	
			}
			return duties;
		}
		//显示学历格式化
		function degreeFamater(value){
			var degree=value;
			if(value!=null){
				for(var i=0;i<drgreeList.length;i++){
					if(value==drgreeList[i].encode){
						degree=drgreeList[i].name;
						break;
					}
				}	
			}
			return degree;
		}
		//显示职称格式化
		function titleFamater(value){
			var retVal = value;
			if(value!=null){
				for(var i=0;i<titleList.length;i++){
					if(value==titleList[i].encode){
						retVal = titleList[i].name;
						break;
					}
				}	
			}
			return retVal;
		}
		function refreshdata(){
			$('#list').datagrid('reload');
		}
		/**
		 * 关闭编辑窗口
		 * @author  zpty
		 * @date 2015-6-2 10:53
		 * @version 1.0
		 */
		function closeLayout(){
			$('#divLayout').layout('remove','east');
			$('#selectUser').dialog('destroy');
			$('#selectDept').dialog('destroy');
		}
		
		function selectNode(code){
			var node = $('#tDt').tree('find',code);
			if(node!=null){
				var pid= node.attributes.pid;
				var pnode= $('#tDt').tree('find',pid);
				$('#tDt').tree('expand', pnode.target).tree('scrollTo', node.target).tree('select',
						node.target);
				if(node.attributes.hasson!=null&&node.attributes.hasson!=''&&node.attributes.hasson!='1'){
					$('#deptid').val(node.id);
						$('#list').datagrid('load', {
							dname: node.id,
						});
				}else{
					$('#deptid').val('');
				}
				searchFrom();
			}
		}
		
		// 列表查询重置
		function searchReload() {
			$('#queryName').textbox('setValue','');
			$('#workPost').combobox('setValue','');
			$('#workTitle').combobox('setValue','');
			$('#workType').combobox('setValue','');
			searchFrom();
		}
		function submit(){
			var dept = $('#ksnew').getMenuIds();
			if(dept==null||dept==""){
				$.messager.alert('提示','请选择科室!');
				return ;
			}
			var menulist = $('#menulist').treegrid('getChecked');
			if(menulist==null||menulist==""){
				$.messager.alert('提示','请选择栏目!');
			}
			var menu = "";
			for(var i=0;i<menulist.length;i++){
				if(menu!=""){
					menu += ",";
				}
				menu += menulist[i].alias;
			}
			var rows = $('#list').datagrid('getChecked');
			var acounts = "";
			for(var i=0;i<rows.length;i++){
				if(acounts!=""){
					acounts += ",";
				}
				acounts += rows[i].jobNo;
			}
			$.ajax({
				url:"<%=basePath%>baseinfo/columnDept/saveColumnDept.action",
				data:{"deptCode":dept,"mAlias":menu,"acount":acounts},
				success:function(result){
					$.messager.alert('提示',result.resMsg);
					$('#win').window('close');
					return;
				}
				,error:function(){
					$.messager.alert('提示','网络繁忙,请稍后重试!');
					return;
				}
			});
		}
		function searchMenu(){
			var acount = $('#searchUser').textbox('getValue');
			$('#searchMenu').datagrid('reload',{acount : acount});
		}
		//查询
		function searchmenualiras(){
		  	var name =$('#search :input[name="menu.name"]').val();
		    $('#menulist').treegrid({
				url: "<c:url value='/sys/searchMenusByParams.action?menuAlias=${menuAlias}'/>",
				queryParams:{name:name}
		    });
		}
		function submitInit(value){
			var menulist = $('#menulist').treegrid('getChecked');
			if(menulist==null||menulist==""){
				$.messager.alert('提示','请选择栏目!');
			}
			var menu = "";
			for(var i=0;i<menulist.length;i++){
				if(menu!=""){
					menu += ",";
				}
				menu += menulist[i].alias;
			}
			var rows = $('#list').datagrid('getChecked');
			var acounts = "";
			for(var i=0;i<rows.length;i++){
				if(acounts!=""){
					acounts += ",";
				}
				acounts += rows[i].jobNo;
			}
			$.ajax({
				url:"<%=basePath%>baseinfo/columnDept/initColumnDept.action",
				data:{"isAll":value,"mAlias":menu,"acount":acounts},
				success:function(result){
					$.messager.alert('提示',result.resMsg);
					$('#win').window('close');
					return;
				}
				,error:function(){
					$.messager.alert('提示','网络繁忙,请稍后重试!');
					return;
				}
			});
		}
		function bindDeptOpen(){
			var rows= $('#list').datagrid('getChecked');
			if(rows!=null&&rows.length>0){
				var id = '';
				var jobNos = "";
				for(var i=0;i<rows.length;i++){
					if(id!=''){
						id += ',';
					}
					id += rows[i].id;
					if(jobNos!=""){
						jobNos += ",";
					}
					jobNos += rows[i].jobNo;
				}
				var url='<%=basePath%>baseinfo/department/empDeptAut.action?acount='+jobNos;
				var name='科室授权';
				var width=1200;
				var height=800;
				var top=(window.screen.availHeight-30-height)/2;
				var left=(window.screen.availWidth-10-width)/2;
				if($("#winOpenFrom").length<=0){  
					var form = "<form id='winOpenFrom' action='"+url+"' method='post' target='"+name+"'>" +
							"<input type='hidden' id='winOpenFromInpId' name='id'/></form>";
					$("body").append(form);
				} 
				$('#winOpenFromInpId').val(id);
				openWindow(url,name,width,height,top,left);
				$('#winOpenFrom').attr('action',url);
				$("#winOpenFrom").submit();  
			}else{
				$.messager.alert('提示','请选择需要授权的员工！');
			}
		}

		function openWindow(url,name,width,height,top,left){
			window.open(url, name, 'height=' + height + ',,innerHeight=' + height + ',width=' + width + ',innerWidth=' + width + ',top=' + top + ',left=' + left + ',status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no'); 
		}
		
		//同步操作 将员工扩展表中的不为空的手机号、身份证号同步更新到员工表中，可单个、批量、全部同步，同步时要有确认提示框
		//若未选择员工则提示“确认同步全部员工扩展信息？”若选择部分员工则提示“确认同步选中的员工扩展信息？”
		function synChEmpInfo(){
			var rows = $('#list').datagrid('getChecked');
			var jobNos = "";//员工号
			var tipMsg = null;
			//获取员工号
			if(rows!=null&&rows.length!=0){
				for(var i=0;i<rows.length;i++){
					if(jobNos!=""){
						jobNos += ",";
					}
					jobNos += rows[i].jobNo;
				}
				tipMsg='确认同步选中的员工扩展信息？';
			}else{
				tipMsg='确认同步全部员工扩展信息？';
			}
			console.info(jobNos);
			$.messager.confirm('确认', tipMsg, function(res){//提示是否进行员工扩展表同步操作
				if(res){
					$.ajax({
						url: "<c:url value='/baseinfo/employee/synChEmpInfo.action'/>",
						type:'post',
						data:{jobNos:jobNos},
						dataType:'json',
						success: function(data) {
// 					$.messager.progress({text:'同步中，请稍后...',modal:true});
							console.info(data);
							$.messager.alert('提示','同步完成！');
							 setTimeout(function(){
		        					$(".messager-body").window('close');
		        				},3500);
						}
					});				
				}
			});
		}
		</script>
	</body>
</html>