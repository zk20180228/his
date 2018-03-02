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
	</style>
<script type="text/javascript">

	var deptMap = null;//渲染科室
	var empMap = null;//渲染员工
	
     var menuAlias = '${menuAlias}';//栏目别名
     var flag;
	$(function(){
				//科室渲染
				$.ajax({
					url: "<%=basePath%>baseinfo/department/getDeptMap.action",
					success: function(date) {
						deptMap = date;
					}
				});
				$('#roleMenuList').datagrid({
					toolbar:'#toolbarIdRoleMenu',
					fit:true,
					singleSelect:true,
					remoteSort:false,
					pagination:true,
		        	pageSize:20,
		        	rownumbers:true,
		        	pageList:[20,30,50,100]
				});
		
			//选择科室
			 $(".deptInput").MenuList({
					width :530, //设置宽度，不写默认为530，不要加单位
					height :400, //设置高度，不写默认为400，不要加单位
					dropmenu:"#m2",//弹出层id，必须要写
					isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
					para:"I",//要传的参数，科室类型，多个参数逗号分开，如果不写，查询全部
					firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
					relativeInput:".doctorInput",	//与其级联的文本框，必须要写
					relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
				});
			 	$('#m2 .addList h2 input').click();
				$('a[name=\'menu-confirm\']').click();
				flag=$('#ksnew').getMenuIds();
				$('a[name=\'menu-confirm-clear\']').click();
				if(flag==''){
					$('#tableList').datagrid();
					$("body").setLoading({
						id:"body",
						isImg:false,
						text:"无数据权限"
					});
				}else{
					
					//员工渲染
					$.ajax({
						url: "<%=basePath%>baseinfo/employee/getEmplMap.action",
						success: function(date) {
							empMap = date;
						}
					});
					
					$('#roleMenuList').datagrid({
						method:'post',
						toolbar:'#toolbarIdRoleMenu',
						fit:true,
						singleSelect:true,
						rownumbers:true,
						remoteSort:false,
						pagination:true,
						pageSize:20,
			        	pageList:[20,30,50,100],
			        	data:{total:0,rows:[]}
					});
				
				}
						
	});
	setTimeout(function(){
		//选择医生
		$(".doctorInput").MenuList({
			width :530,
			height :400,
			dropmenu:"#m3",//弹出层
			isSecond:true,	//是否是二级联动的第二级
			para:"I",//要传的参数，科室类型，多个参数逗号分开，如果不写，查询全部
			firsturl: "<%=basePath %>statistics/RegisterInfoGzltj/getDoctorList.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
			relativeInput:".deptInput"	//与其级联的文本框
		});
	},3000)
	
	/**
	 * 查询
	 * @author zpty
	 * @date 2015-08-31
	 * @version 1.0
	 */
	 function searchFrom(){
		var Stime = $('#Stime').val();
		var Etime = $('#Etime').val();
		if(Stime==null || Stime=="" || Etime==null || Etime==""){
			$.messager.alert("提示","请填写正确的时间范围！");
			return ;
  		}
		if(Stime&&Etime){
			if(Stime>Etime){
				$.messager.alert("提示","开始时间不能大于结束时间！");
				return ;
			}
		}
		var dd=new Date(Etime); 
	     dd.setDate(dd.getDate()+1);//获取AddDayCount天后的日期
	     var y = dd.getFullYear();
	     var m = dd.getMonth()+1;//获取当前月份的日期
	     if(Number(m)<10){
	           m = "0"+m;
	         }
	         var d = dd.getDate();
	         if(Number(d)<10){
	           d = "0"+d;
	         }
	     Etime =  y+"-"+m+"-"+d;
	     searchFrom1(Stime,Etime);
	}
	
	function searchFrom1(Stime,Etime) {
		if(Stime&&Etime){
			if(Stime>Etime){
				$.messager.alert("提示","开始时间不能大于结束时间！");
				return ;
			}
		}
		var depts = $('#ksnew').getMenuIds();
		if(depts==''){
			depts=flag;
		}
		var doctors = $('#doctornew').getMenuIds();
		$('#roleMenuList').datagrid({
			url:'<%=basePath%>/statistics/WordLoadDoctorGZLTJ/queryDoctors.action',
			queryParams:{
				begin: Stime,
				end: Etime,
				depts: depts,
				doctors: doctors,
				menuAlias: menuAlias
			},
			method:'post',
			toolbar:'#toolbarIdRoleMenu',
			fit:true,
			singleSelect:true,
			rownumbers:true,
			remoteSort:false,
			pagination:true,
			pageSize:20,
        	pageList:[20,30,50,100],
			onLoadSuccess:function(data){
	    		$('#roleMenuList').datagrid("autoMergeCells", ['deptCode']);
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
			}
		});
	}
	
	function forDept(value,row,index){
		if(deptMap == null){
			return ""
		}
		return deptMap[value];
	}
	function forEmp(value,row,index){
		if(empMap == null){
			return ""
		}
		return empMap[value];
	}
	/**
	 * 重置
	 * @author huzhenguo
	 * @date 2017-03-17
	 * @version 1.0
	 */
	function clears(){
		$('#Stime').val('${sTime}');
		$('#Etime').val('${eTime}');
		$('#ksnew').val('');
		$('#doctornew').val('');
		$("a[name='menu-confirm-clear']").click();

		searchFrom();
	}
	function rateFormater(value,row,index){
			var outVis=row.outVisitors;//出院人数
			if(!isNaN(value)&&!isNaN(outVis)){
				return parseFloat(value/(outVis)*100).toFixed(2)+'%'
			}else{
				return '0.00%';
			}
	}
	function outFormater(value,row,index){
		var outVis=row.outVisitors;//出院人数
		if(!isNaN(value)&&!isNaN(outVis)){
			return parseFloat(value/outVis).toFixed(2);
		}else{
			return 0;
		}
	}
	$.extend($.fn.datagrid.methods, {
		autoMergeCells: function (jq, fields) {
			return jq.each(function () {
				var target = $(this);
				if (!fields) {
					fields = target.datagrid("getColumnFields");
				}
				var rows = target.datagrid("getRows");
				var i = 0,
				j = 0,
				temp = {};
				for (i; i < rows.length; i++) {
					var row = rows[i];
					j = 0;
					for (j; j < fields.length; j++) {
						var field = fields[j];
						var tf = temp[field];
						if (!tf) {
							tf = temp[field] = {};
							tf[row[field]] = [i];
						} else {
							var tfv = tf[row[field]];
							if (tfv) {
								tfv.push(i);
							} else {
								tfv = tf[row[field]] = [i];
							}
						}
					}
				}
				$.each(temp, function (field, colunm) {
					$.each(colunm, function () {
					var group = this;
						if (group.length > 1) {
							var before,
							after,
							megerIndex = group[0];
							for (var i = 0; i < group.length; i++) {
								before = group[i];
								after = group[i + 1];
								if (after && (after - before) == 1) {
								    continue;
								}
								var rowspan = before - megerIndex + 1;
								if (rowspan > 1) {
									target.datagrid('mergeCells', {
										index: megerIndex,
										field: field,
										rowspan: rowspan
									});
								}
								if (after && (after - before) != 1) {
								    megerIndex = after;
								}
							}
						}
					});
				});
			});
		}
	});
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
</head>
<body>
		<div id="divLayout" class="easyui-layout" data-options="fit:true" style="width: 100%; height: 100%;">
			<div data-options="region:'north',border:false" style="height:38px;padding:5px 5px 0px 5px;">
						<table id="searchTab" style="width: 100%;">
							<tr>
								<!-- 开始时间 --> 
								<td style="width:40px;" align="left">日期:</td>
								<td style="width:110px;">
									<input id="Stime" class="Wdate" type="text" name="Stime" value="${sTime}" onClick="WdatePicker()" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								<!-- 结束时间 --> 
								<td style="width:40px;" align="center">至</td>
								<td style="width:110px" id="td1">
									<input id="Etime" class="Wdate" type="text" name="Etime" value="${eTime}" onClick="WdatePicker()" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								<td style="width:55px;" align="center">科室:</td>
				    			<td style="width:120px;" class="newMenu">
					    	       	<div class="deptInput menuInput" style="width:120px"><input style="width:95px" class="ksnew" id="ksnew" readonly="readonly"/><span></span></div> 
					    	       	<div id="m2" class="xmenu" style="display: none;">
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
											<div class="addList"></div>
											<div class="tip" style="display:none">没有检索到数据</div>
										</div>													 
									</div>
								</td>
					    		<td style="width:55px;" align="center">医生:</td>
				    			<td style="width:120px;" class="newMenu">
					    			<div class="doctorInput menuInput" style="width:120px"><input style="width:95px" class="ksnew" id="doctornew" readonly="readonly"/><span></span></div> 
					    	       	<div id="m3" class="xmenu" style="display: none;">
					    	       		<div class="searchDept">
					    	       			<input type="text" name="searchByDeptName" placeholder="回车查询"/>
					    	       			<span class="searchMenu"><i></i>查询</span>
					    	       			<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn" >
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
											<label class="top-label">已选医生：</label>																						
											<ul class="addDept">
											
											</ul>											
										</div>	
										<div class="depts-dl">
											<div class="addList"></div>
											<div class="tip" style="display:none">没有检索到数据</div>	
										</div>
													 
									</div>
					    		</td>
								<td>
								<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search" style="margin-top:-3px">查询</a>
									<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset" style="margin-top:-3px">重置</a>
								</shiro:hasPermission>
								</td>
							</tr>
						</table>
					</div>
				<div data-options="region:'center',border:false" >
					<table  style="width: 100%;" id="roleMenuList">
						<thead>
							<tr>
								<th data-options="field:'deptCode',width:'10%',formatter:forDept">科室</th>
								<th data-options="field:'doctor',width:'10%',formatter:forEmp">医生</th>
								<th data-options="field:'hosVisitors',width:'10%'">住院人数</th>
								<th data-options="field:'outVisitors',width:'10%'">出院人数</th>
								<th data-options="field:'averageInhost',width:'10%',formatter:outFormater">平均住院天数</th>
								<th data-options="field:'concurVisitors',width:'10%',formatter:rateFormater">并发症发生率</th>
								<th data-options="field:'cureVisitors',width:'10%',formatter:rateFormater">治愈率</th>
								<th data-options="field:'betterVisitors',width:'10%',formatter:rateFormater">好转率</th>
								<th data-options="field:'unCureVisitors',width:'10%',formatter:rateFormater">未愈率</th>
								<th data-options="field:'deathVisitors',width:'8%',formatter:rateFormater">死亡率</th>
							</tr>
						</thead>
					</table>
				</div>
		</div>
	</body>
</html>
