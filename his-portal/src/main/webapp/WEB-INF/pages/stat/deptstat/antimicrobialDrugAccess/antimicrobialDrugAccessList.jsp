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
<style type="text/css">
		.panel-body,.panel{
			overflow:visible;
		}
		.addList dl:first-child ul {
			overflow:visible; !important; 
			clear:both;
		}
		.clearfix:after{
		    content:"";
		    display:table;
		    height:0;
		    visibility:hidden;
		    clear:both;
		}
		.xmenu dl dd ul{
			overflow:visible; !important; 
			clear:both;
		}
		.clearfix{
		*zoom:1;    /* IE/7/6*/
		}
		
</style>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
	var deptMap=new Map();
	var empMap=new Map();
	var packunit= new Map();
	var menuAlias = '${menuAlias}';
	var flag;//全局科室
	$(function(){
		$(".deptInput").MenuList({
			width :530, //设置宽度，不写默认为530，不要加单位
			height :400, //设置高度，不写默认为400，不要加单位
			menulines:2, //设置菜单每行显示几列（1-5），默认为2
			dropmenu:"#m3",//弹出层id，必须要写
			isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
			haveThreeLevel:false,//是否有三级菜单
			para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
			firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
		});
		$('#m3 .addList h2 input').click();
		$('a[name=\'menu-confirm\']').click();
		flag=$('#deptName').getMenuIds();
		$('a[name=\'menu-confirm-clear\']').click();
		if(flag==''||flag==null){
			$("#countList").datagrid();
			$("body").setLoading({
				id:"body",
				isImg:false,
				text:"无数据权限"
			});
		}else{
			var winH=$("body").height();
			$("#countList").datagrid({
				pagination:true,
				pageSize : 20,
				pageList : [ 20, 30, 50, 80, 100 ],
				idField:'id',
				border:true,
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				fitColumns:false,
				rownumbers:true,
				fit:true,
				data:{total:0,rows:[]}
			});
		}
	});
	function searchFrom(){
		var deptCode = $('#deptName').getMenuIds();
		if(deptCode==''){
			deptCode=flag;
		}
		console.info(deptCode);
		//加载数据表格
		$("#countList").datagrid({
			pagination:true,
			pageSize : 20,
			pageList : [ 20, 30, 50, 80, 100 ],
			idField:'id',
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			rownumbers:true,
			fit:true,
			queryParams:{dept: deptCode,menuAlias:menuAlias},
			url:'<%=basePath%>/statistics/deptstat/antimicrobialDrugAccessAction/queryAntimicrobialDrugAccess.action',
			onLoadSuccess:function(data){
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


	}
	
	//导出功能
	function exportList (){
	    var rows=$('#countList').datagrid('getRows');
		if(rows==null||rows.length==0){
			$.messager.alert("提示","没有数据，不能导出！");
			return;
		}else{
		$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
			if (res) {
				  //给表单的隐藏字段赋值
			    $("#rows").val(JSON.stringify(rows));
				$('#saveForm').form('submit', {
					url :'<%=basePath%>statistics/deptstat/antimicrobialDrugAccessAction/exportList.action',
					onSubmit : function() {
						return $(this).form('validate');
					},
					success : function(data) {
						$.messager.alert("提示", "导出成功！", "success");
					},
					error : function(data) {
						$.messager.alert("提示", "导出失败！", "error");
					}
				});
			}
		});
		}
	}
	//打印功能
	function printList(){
		var rows=$('#countList').datagrid('getRows');
		var deptCode = $('#deptName').getMenuIds();
		if(rows!=null&&rows!=''){
			$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否打印
	 			if (res) {
	 				//hedong 20170316  报表打印参数传递改造为表单POST提交的方式示例
				    //给表单的隐藏字段赋值
				    $("#reportToQueryDept").val(deptCode);
				    $("#reportToRows").val(JSON.stringify(rows));
				    $("#reportToFileName").val("KJYWSYQX");
				    //表单提交 target
				    var formTarget="hiddenFormWin";
			        var tmpPath = "<%=basePath%>statistics/deptstat/antimicrobialDrugAccessAction/printList.action";
			        //设置表单target
			        $("#reportToHiddenForm").attr("target",formTarget);
			        //设置表单访问路径
					$("#reportToHiddenForm").attr("action",tmpPath); 
			        //表单提交时打开一个空的窗口
				    $("#reportToHiddenForm").submit(function(e){
				    	 var timerStr = Math.random();
						 window.open('about:blank',formTarget,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');   
					});  
				    //表单提交
				    $("#reportToHiddenForm").submit();
	 			}
	 		});
		}else{
			$.messager.alert("提示","没有数据，不能打印！"); 	
		}
	}
	//重置
	function clear(){
		$("#time2").val("${Etime}");
		var dept = $('#deptName').val("");
		$('#deptName').attr("name","");
		$("#countList").datagrid('loadData', { total: 0, rows: [] });
	}
	</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body style = "background-color: #ffffff">
	<div id="divLayout"  class="easyui-layout" data-options="fit:true"  >
		 <div data-options="region:'north',border:false" style="height:90px;padding: 2px 5px 10px;">
		    <div style="border: none;">
				<table style="padding-top: 10px">
				<tr>
							 <td style="width: 50px">科室：</td>
							 <td  class="newMenu" style="width:160px;z-index:1;position: relative;">
							<div class="deptInput menuInput" style="margin-top:0px;">
								<input id="deptName" class="ksnew"  readonly/>
								<span></span>
							</div>
							<div id="m3" class="xmenu" style="display: none;">
								<div class="searchDept">
									<input type="text" name="searchByDeptName" placeholder="回车查询"/>
									<span class="searchMenu"><i></i>查询</span>
									<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn">
										<span class="a-btn-text">取消</span>
									</a>						
									<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn">
										<span class="a-btn-text">清空</span>
									</a>
									<a name="menu-confirm" href="javascript:void(0);" class="a-btn">
										<span class="a-btn-text">确定</span>
									</a>
								</div>
								<div class="select-info" style="display:none">
									<label class="top-label">已选部门：</label>
									<ul class="addDept"></ul>
								</div>	
								<div class="depts-dl">
									<div class="addList">
									</div>
									<div class="tip" style="display:none">没有检索到数据</div>
								</div>	
							</div>
		    		</td>
				<td>	
				<shiro:hasPermission name="${menuAlias}:function:query">
					<a href="javascript:void(0);searchFrom();" onclick="" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left: 0px">查询</a>
				</shiro:hasPermission>
					<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
				<shiro:hasPermission name="${menuAlias}:function:print">
					<a href="javascript:void(0)" onclick="printList()" class="easyui-linkbutton" data-options="iconCls:'icon-printer'" style="margin-left: 3px">打印</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:export">
					<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" data-options="iconCls:'icon-down'" style="margin-left: 3px">导出</a>
				</shiro:hasPermission>
				</td>
				</tr>
				</table>
		    </div>
		    <div style="padding-top: 2px;">
					<h5 id="hh" style="font-size: 30;font: bold;text-align: center;padding-top: 5px">抗菌药物使用权限</h5>
		   </div>
		</div>
		 <div data-options="region:'center',border:false" style="height:90%;">
				<table id="countList"  data-options="fit:true,method:'post'">
					<thead>
						<tr>
							<th data-options="field:'ename'," width="16%" align="center">姓名</th>
							<th data-options="field:'ecode'" width="16%" align="center">工号</th>
							<th data-options="field:'elevel'" width="15%" align="center" halign="center">级别</th>
							<th data-options="field:'eaccess'" width="25%" align="center">抗菌药物使用权限</th>
						</tr>
					</thead>
				</table>
				<form id="saveForm" method="post"/>
				  <input type="hidden" name="rows" id="rows" value=""/>
				</form>
				<form method="post" id="reportToHiddenForm">
					<input type="hidden" name="type" id="reportType" value=""/>
					<input type="hidden" name="dept" id="reportToQueryDept" value=""/>
					<input type="hidden" name="rows" id="reportToRows" value=""/>
				</form>
		</div>
	</div>
	</body>
</html>
