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
</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
	<script type="text/javascript">
	var time = '${time}';
	var menuAlias = '${menuAlias}';
	var cames;
	var flag;//全局科室
	$(function(){
		document.getElementById('hh').innerHTML = '科室监督指标统计';
		//科室下拉
		$(".deptInput").MenuList({
			width :530, //设置宽度，不写默认为530，不要加单位
			height :400, //设置高度，不写默认为400，不要加单位
			menulines:2, //设置菜单每行显示几列（1-5），默认为2
			dropmenu:"#m3",//弹出层id，必须要写
			isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
			haveThreeLevel:false,//是否有三级菜单
			para:"I",//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
			async:false,
			firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
		});
		$.ajax({
			url: "<c:url value='/baseinfo/department/getAuthorArea.action'/>?menuAlias="+menuAlias,
			type:"post",
			async:false,
			success: function(date){
				cames=date;
			}
		});
		$('#m3 .addList h2 input').click();
		$('a[name=\'menu-confirm\']').click();
		flag=$('#queryDept').getMenuIds();
		$('a[name=\'menu-confirm-clear\']').click();
		var sign=cames.length;
		if((flag=='')&&(sign==undefined||sign==0)){
			$("#list").datagrid();
			$("body").setLoading({
				id:"body",
				isImg:false,
				text:"无数据权限"
			});
		}else{
			if(sign==undefined||sign==0){//如果没有院区权限  隐藏院区
				$('#yuanqu').hide();
			}else{
				$('#yuanqu').show();
			}
			$('#campus').combobox({
				valueField : 'code',
				textField : 'name',
				multiple : true,
				data:cames
			});
			searchList();
		
	}
	});
	
	//加载数据
	function searchList(){
		var begin=$('#begin').val();
		var end=$('#end').val();
		if(begin==''||end==''){
			$.messager.alert('提示','时间不能为空');
			return false;
		}
		var dept=$('#queryDept').getMenuIds();
		if(dept==''){
			dept=flag;
		}
		//加载数据表格
		$("#list").datagrid({
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			striped:true,
			url:'<%=basePath%>/dept/DeptSupervision/querySupervisionList.action',
			queryParams:{begin:begin,end:end,depts:dept,menuAlias:menuAlias,campus:$('#campus').combobox('getValues')},
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
	
	/**
	* 重置方法
	*/
	function clear(){
		$('#begin').val("${begin}");
		$('#end').val("${end}");
		$('#campus').combobox('clear');
		$('a[name=\'menu-confirm-clear\']').click();
		$("#list").datagrid('loadData',{total:0,rows:[]});
	}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<body>
	<div id="layoutId" class="easyui-layout" data-options="fit:true">   
	    	<form id="searchForm" style="padding-top:5px;padding-left:5px;padding-right:5px;">
				<table>
					<tr align="left">
						<td style="width: 400px;" >
							开始时间:
							<input id="begin" class="Wdate" type="text" value="${begin}" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'end\')}'})"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;height: 24px;"/>
							结束时间:
							<input id="end" class="Wdate" type="text" value="${end}" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'begin\')}'})"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;height: 24px;"/>
						</td>
						<td id="yuanqu" style="width:240px;display:none;">&nbsp;院区:
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
								<a href="javascript:void(0)" onclick="searchList()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left: 3px">查询</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:set">
								<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
							</shiro:hasPermission>
						</td>
						</tr>
				</table>
			</form>
			<h5 id="hh" style="font-size: 30;font: bold;text-align: center;padding-top: 15px"></h5>
	    <div data-options="region:'center',fit:true" style="width:100%;height:90%;">
	    	<table id="list" data-options="fit:true">
					<thead>
					 <tr>
						<th data-options="field:'deptCode',rowspan:2,width:100" align="center">院区</th>
						<th data-options="field:'oldNum',colspan:3" align="center">门诊人次</th>
						<th data-options="field:'inNum',colspan:3" align="center" halign="center">门诊人均诊察人次</th>
						<th data-options="field:'exInNum',colspan:3" align="center">入院患者例数</th>
						<th data-options="field:'exOutNum',colspan:3" align="center" halign="center">出院患者例数</th>
						<th data-options="field:'outNum',colspan:3" align="center">住院患者及危重患者例数</th>
						<th data-options="field:'nowNum',colspan:3" align="center">住院患者抢救成功例数</th>
						<th data-options="field:'realBedNum',colspan:3" align="center">入院患者手术例数</th>
						<th data-options="field:'hangBedDays',colspan:3" align="center">平均住院天数</th>
						<th data-options="field:'hangBedDays',colspan:3" align="center">住院患者治愈率（%）</th>
						<th data-options="field:'openBedNum',colspan:3" align="center">住院患者未治愈率（%）</th>
						<th data-options="field:'rateOfBed',colspan:3" align="center">住院患者好转率（%）</th>
						<th data-options="field:'criticallyNum',colspan:3" align="center">床位使用率（%）</th>
						<th data-options="field:'grateOneNum',colspan:3" align="center">床位周转次数</th>
						<th data-options="field:'extraBedNum',colspan:3" align="center">平均床位工作日</th>
						<th data-options="field:'zhuyuanfeiyong',colspan:3" align="center">平均住院费用</th>
						<th data-options="field:'chuyuansiwanglv',colspan:3" align="center">出院死亡例数</th>
						<th data-options="field:'returnRate',rowspan:2,width:100" align="center">重返率</th>
					</tr> 
					<tr>
						<th data-options="field:'nowOutCases',width:100"  align="center">本期</th>
						<th data-options="field:'beforOutCases',width:100"   align="center">上期</th>
						<th data-options="field:'outDecrease',width:100"   align="center">增减</th>
						
						<th data-options="field:'nowDiagnosCases',width:100"   align="center">本期</th>
						<th data-options="field:'beforDiagnosCases',width:100"   align="center">上期</th>
						<th data-options="field:'diagnosDecrease',width:100"   align="center">增减</th>
						
						<th data-options="field:'nowInhCases',width:100"   align="center">本期</th>
						<th data-options="field:'beforInhCases',width:100"   align="center">上期</th>
						<th data-options="field:'inhDecrease',width:100"   align="center">增减</th>
						
						<th data-options="field:'nowOutHostCases',width:100"   align="center">本期</th>
						<th data-options="field:'beforOutHostCases',width:100"   align="center">上期</th>
						<th data-options="field:'outHostDecrease',width:100"   align="center">增减</th>
						
						<th data-options="field:'nowCriticalCases',width:100"   align="center">本期</th>
						<th data-options="field:'beforCriticalCases',width:100"   align="center">上期</th>
						<th data-options="field:'criticalDecrease',width:100"   align="center">增减</th>
						
						<th data-options="field:'nowRescueCases',width:100"   align="center">本期</th>
						<th data-options="field:'beforRescueCases',width:100"   align="center">上期</th>
						<th data-options="field:'rescueDecrease',width:100"   align="center">增减</th>
						
						<th data-options="field:'nowSurgicalCases',width:100"   align="center">本期</th>
						<th data-options="field:'beforSurgicalCases',width:100"   align="center">上期</th>
						<th data-options="field:'surgicalDecrease',width:100"   align="center">增减</th>
						
						<th data-options="field:'nowDaysCases',width:100"   align="center">本期</th>
						<th data-options="field:'beforDaysCases',width:100"   align="center">上期</th>
						<th data-options="field:'daysDecrease',width:100"   align="center">增减</th>
						
						<th data-options="field:'nowCureCases',width:100"   align="center">本期</th>
						<th data-options="field:'beforCureCases',width:100"   align="center">上期</th>
						<th data-options="field:'cureDecrease',width:100"   align="center">增减</th>
						
						<th data-options="field:'nowUnCureCases',width:100"   align="center">本期</th>
						<th data-options="field:'beforUnCureCases',width:100"   align="center">上期</th>
						<th data-options="field:'unCureDecrease',width:100"   align="center">增减</th>
						
						<th data-options="field:'nowBetterCases',width:100"   align="center">本期</th>
						<th data-options="field:'beforBetterCases',width:100"   align="center">上期</th>
						<th data-options="field:'betterDecrease',width:100"   align="center">增减</th>
						
						<th data-options="field:'nowBedUsedCases',width:100"   align="center">本期</th>
						<th data-options="field:'beforBedUsedCases',width:100"   align="center">上期</th>
						<th data-options="field:'bedUsedDecrease',width:100"   align="center">增减</th>
						
						<th data-options="field:'nowTurnsCases',width:100"   align="center">本期</th>
						<th data-options="field:'beforTurnsCases',width:100"   align="center">上期</th>
						<th data-options="field:'turnsDecrease',width:100"   align="center">增减</th>
						
						<th data-options="field:'nowBedWorkCases',width:100"   align="center">本期</th>
						<th data-options="field:'beforBedWorkCases',width:100"   align="center">上期</th>
						<th data-options="field:'bedWorkDecrease',width:100"   align="center">增减</th>
						
						<th data-options="field:'nowExpensesCases',width:100"   align="center">本期</th>
						<th data-options="field:'beforExpensesCases',width:100"   align="center">上期</th>
						<th data-options="field:'expensesDecrease',width:100"   align="center">增减</th>
						
						<th data-options="field:'nowDeathCases',width:100"   align="center">本期</th>
						<th data-options="field:'beforDeathCases',width:100"   align="center">上期</th>
						<th data-options="field:'deathDecrease',width:100"   align="center">增减</th>
						
					</tr>
				</thead>
			</table>
	    </div>
	</div>  
</body>
</html>