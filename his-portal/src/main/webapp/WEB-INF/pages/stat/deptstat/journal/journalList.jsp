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
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
	<script type="text/javascript">
	var time = '${time}';
	var dept = '${dept}';
	var menuAlias = '${menuAlias}';
	var hosiptyal = '${hosiptyal}';
	var cames;//院区
	var camesArr=[];//院区code
	$(function(){
		document.getElementById('hh').innerHTML = hosiptyal + '住院日报';
		$.ajax({
			url: "<c:url value='/baseinfo/department/getAuthorArea.action'/>?menuAlias="+menuAlias,
			type:"post",
			async:false,
			success: function(date){
				cames=date;//请求院区
			}
		});
		//科室下拉
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
		var sign=cames.length;
		if(sign==undefined||sign==0){//如果院区为空查看是否授权科室
			$('#m3 .addList h2 input').click();
			$('a[name=\'menu-confirm\']').click();
			$('#yuanqu').hide();
		}
		if(($('#queryDept').getMenuIds()=='')&&(sign==undefined||sign==0)){
			$("#countList").datagrid();
			$("body").setLoading({
				id:"body",
				isImg:false,
				text:"无数据权限"
			});
		}else{
			$('a[name=\'menu-confirm-clear\']').click();
			$('#campus').combobox({
				valueField : 'code',
				textField : 'name',
				multiple : true,
				data:cames
			});
			//如果授权院区 则遍历院区防止越权
			if(sign!=undefined||sign!=0){
				for(var i=0;i<sign;i++){
					camesArr.push(cames[i].code)
				}
// 				$('#campus').combobox('setValues',camesArr);
			}
			
		//加载数据表格
		$("#countList").datagrid({
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			rownumbers:true,
			fit:true,
		});
	}
	});
	/**
	* 打印方法
	*/
	function reportList(){
		var times = $('#time').val();
		if(times != ''){
			var rows = $('#countList').datagrid('getRows');
			if(rows.length > 0){
				$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否导出
			 		if (res) {
			 		$('#reportTime').val($('#time').val());
					$('#reportJson').val(JSON.stringify(rows));
					//表单提交 target
					var formTarget="reportForm";
					var tmpPath = "<%=basePath%>statistics/deptstat/journal/reportList.action";
					//设置表单target
					$("#reportForm").attr("target",formTarget);
					//设置表单访问路径
					$("#reportForm").attr("action",tmpPath); 
					//表单提交时打开一个空的窗口
					$("#reportForm").submit(function(e){
						var timerStr = Math.random();
						window.open('about:blank',formTarget,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');
						window.close();
					});
					$("#reportForm").submit();
			 		}
				});
			}else{
				$.messager.alert("提示","当前统计无数据，无法打印！");
				setTimeout(function(){$(".messager-body").window('close')},1500);
				return;
			}
		}else{
			$.messager.alert("提示","请先选择查询日期！");
			close_alert();
			return;
		}
	}
	/**
	* 导出方法
	*/
	function exportList(){
		var times = $('#time').val();
		if(times != ''){
			var rows = $('#countList').datagrid('getRows');
			if(rows.length > 0){
				$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
			 		if (res) {
				$('#exportJson').val(JSON.stringify(rows));
				$('#exportForm').form('submit', {
					url :"<%=basePath%>statistics/deptstat/journal/exportList.action",
					onSubmit : function(param) {
					
					},
					success : function(data) {
						$.messager.alert("操作提示", "导出失败！", "success");
					},
					error : function(data) {
						$.messager.alert("操作提示", "导出失败！", "error");
					}
				});
			 		}
				});
			}else{
				$.messager.alert("提示","当前统计无数据，无法打印！");
				setTimeout(function(){$(".messager-body").window('close')},1500);
				return;
			}
		}else{
			$.messager.alert("提示","请先选择查询日期！");
			close_alert();
			return;
		}
	}
	/**
	* 重置方法
	*/
	function clear(){
		$('#time').val(time);
		$('#queryDept').val('');
		$('#queryDept').attr('name','');
		$("#countList").datagrid('loadData', { total: 0, rows: [] });
		$('#campus').combobox('clear');
	}
	/**
	* 查询方法
	*/
	function searchFrom(){
		var rows = $("#countList").datagrid('getRows');
		var times = $('#time').val();
		var depts = $('#queryDept').getMenuIds();
		var campusSearch=$('#campus').combobox('getValues');
		if(campusSearch==null||campusSearch.length==0){
			campusSearch=camesArr;
		}
		if(times != ''){
				//加载数据表格
				$("#countList").datagrid({
					border:true,
					checkOnSelect:true,
					selectOnCheck:false,
					singleSelect:true,
					fitColumns:false,
					rownumbers:true,
					fit:true,
					queryParams: {time : times, dept : depts,menuAlias:menuAlias,campus:campusSearch},
					url:'<%=basePath %>statistics/deptstat/journal/queryListJournalVo.action',
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
		
		}else{
			$.messager.alert("提示","请先选择查询日期！");
			close_alert();
			return;
		}
	}
	
	</script>
<body>
	<div id="divLayout"  class="easyui-layout" data-options="fit:true" >
		<form id="searchForm"   style="padding-top:5px;padding-left:5px;padding-right:5px;height:100px">
			<table border="0" style="width: 100%;spadding:0px;">
				<tr align="left">
					<td style="width: 150px;" >
						日期:
						<input id="time" class="Wdate" type="text" value="${time}"onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;height: 24px;"/>
					</td>
					<td id="yuanqu" style="width:250px;">&nbsp;院区:
							<input id="campus" class="easyui-combobox" name="campus"  /> 
					</td>
					<td style="width: 60px;" >
					&nbsp;科室:
					</td>
					<td  class="newMenu" style="width:160px;z-index:1;position: relative;">
						<div class="deptInput menuInput" style="margin-top:0px;">
							<input id="queryDept" class="ksnew"  readonly/>
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
								<ul class="addDept">
								</ul>
							</div>	
							<div class="depts-dl">
								<div class="addList">
								</div>
							</div>	
							<div class="tip" style="display:none">没有检索到数据</div>		
						</div>
					</td>
					<td>
						<shiro:hasPermission name="${menuAlias}:function:query">
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left: 3px">查询</a>
						</shiro:hasPermission>
							<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
						<shiro:hasPermission name="${menuAlias}:function:print">
							<a href="javascript:void(0)" onclick="reportList()" class="easyui-linkbutton" data-options="iconCls:'icon-printer'" style="margin-left: 3px">打印</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:export">
							<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" data-options="iconCls:'icon-down'" style="margin-left: 3px">导出</a>
						</shiro:hasPermission>
					</td>
					</tr>
			</table>
			<h5 id="hh" style="font-size: 32;font: bold;text-align: center;padding-top: 15px">住院日报</h5>
		</form>
		<div data-options="region:'center',split:false,border:false" style="width: 100%; height:87%;padding:0px 0px 0px 0px;">
			<table id="countList"  data-options="fit:true,method:'post'">
				<thead>
					<tr>
						<th data-options="field:'deptName'," width="14%" align="left">科室</th>
						<th data-options="field:'oldNum'" width="6%" align="center">原有人数</th>
						<th data-options="field:'inNum'" width="6%" align="center" halign="center">入院</th>
						<th data-options="field:'exInNum'" width="6%" align="center">转入</th>
						<th data-options="field:'exOutNum'" width="6%"align="center" halign="center">转出</th>
						<th data-options="field:'outNum'" width="6%" align="center">出院</th>
						<th data-options="field:'nowNum'" width="6%" align="center">现有人数</th>
						<th data-options="field:'realBedNum'" width="6%" align="center">实占床位</th>
						<th data-options="field:'hangBedDays'" width="6%" align="center">挂床日数</th>
						<th data-options="field:'openBedNum'" width="6%" align="center">开放床位</th>
						<th data-options="field:'rateOfBed'" width="6%" align="center">病床使用率</th>
						<th data-options="field:'criticallyNum'" width="6%" align="center">危重病人</th>
						<th data-options="field:'grateOneNum'" width="6%" align="center">一级护理</th>
						<th data-options="field:'extraBedNum'" width="6%" align="center">加床</th>
						<th data-options="field:'emptyBedNum'" width="6%" align="center">空床</th>
					</tr>
				</thead>
			</table>
		</div>
		<form id="reportForm" method="post">
			<input type="hidden" id="reportTime" name="time"/>
			<input type="hidden" id="reportJson" name="reportJson">
			<input type="hidden" id="fileName" name="fileName" value="ZYRB">
		</form>
		<form id="exportForm" method="post">
			<input type="hidden" id="exportJson" name="exportJson">
		</form>
	</div>
	</body>
</html>
