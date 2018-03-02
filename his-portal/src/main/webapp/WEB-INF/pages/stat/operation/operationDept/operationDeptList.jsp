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
<style type="text/css">
	.panel-body,.panel{
		overflow:visible;
	}
</style>
<body>
	<div id="cc" class="easyui-layout" data-options="fit:true" >
	    <div  data-options="region:'north',split:false,border:true" style="width:100%;height:87px;border-top:0">
           		<div id="toolbarId" style="height:24px;padding: 6px 5px 5px 5px  ">
           		<shiro:hasPermission name="${menuAlias}:function:query"> 
					<a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:false">查询</a>
				</shiro:hasPermission>
				 <shiro:hasPermission name="${menuAlias}:function:print"> 
					<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-2012081511202',plain:false">打印</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:export"> 
				<a href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-down',plain:false">导出</a>
					</shiro:hasPermission>
					<a href="javascript:void(0)" onclick="clearQuery()" class="easyui-linkbutton" iconCls="reset">重置</a>
					 <shiro:hasPermission name="${menuAlias}:function:readCard">  
						<a href="javascript:void(0)"  class="easyui-linkbutton read_medical_card" type_id="read_medical_cardID" id="read_medical_cardID" type_value="read_medical_card" cardNo="" data-options="iconCls:'icon-bullet_feed'">读卡</a>
					  </shiro:hasPermission> 
			         <shiro:hasPermission name="${menuAlias}:function:readIdCard">  
			        		<a href="javascript:void(0)"  class="easyui-linkbutton read_identity" type_id="read_identityID" id="read_identityID" type_value="read_identity" cardNo="" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
					  </shiro:hasPermission>  
					<a href="javascript:onclickDay('6')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当年</a>      
					<a href="javascript:onclickDay('5')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当月</a>
					<a href="javascript:onclickDay('7')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">上月</a>            
					<a href="javascript:onclickDay('4')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">十五天</a>    
					<a href="javascript:onclickDay('3')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">七天</a>    
					<a href="javascript:onclickDay('2')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">三天</a>  
					<a href="javascript:onclickDay('1')" class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当天</a>
				</div>
				<div style="width:100%;height:60px ;border-top: 1px;padding:10px 0px 4px 0px; ">
			       <table class="operationDept"  data-options="fit:true" >
						<tr>
							<td style="width: 80px!important" nowrap='true' class="operationDeptList">开始时间：</td>
							<td>
							<input id="startTime" value="${beginTime}" class="Wdate" type="text"  onClick="WdatePicker()" style="width:120px !important;height:22px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							<input type="hidden" id="tmpStartTime" value="${beginTime}"/>
							</td>
							<td style="width:90px !important;text-align: right;" nowrap='true' class="operationDeptList">结束时间：</td>
							<td>
							<input id="endTime" value="${endTime}" class="Wdate" type="text"  onClick="WdatePicker()" style="width:120px !important;height:22px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							<input type="hidden" id="tmpEndTime" value="${endTime}"/>
							</td>
							<td style="width:85px !important;text-align: right;" nowrap='true' class="middleSize">医生科室：</td>
								    <td  style="width: 130px;" class="newMenu" >		     
								   <div class="deptInput menuInput" style="margin-top:0px;"><input class="ksnew" id="opDoctorDept" readonly="readonly" /><span></span></div> 
					    	       	<div id="m2" class="xmenu" style="display: none;">
					    	       		<div class="searchDept">
					    	       			<input type="text" name="searchByDeptName" placeholder="回车查询"/>
					    	       			<span class="searchMenu"><i></i>查询</span>
					    	       			<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn" id="clearDocDept">
												<span class="a-btn-text">取消</span>
											</a>						
											<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn" id="docDept">
												<span class="a-btn-text">清空</span>
											</a>
											<a name="menu-confirm" href="javascript:void(0);" class="a-btn"  >
												<span class="a-btn-text">确定</span>
											</a>
					    	       		</div>
										<div class="select-info" style="display:none">	
											<label class="top-label">已选部门:</label>																						
											<ul class="addDept">
											
											</ul>											
										</div>	
										<div class="depts-dl">
											<div class="addList"></div>
											<div class="tip" style="display:none">没有检索到数据</div>	
										</div>	
									</div>
							</td>
							<td style="width:85px !important;text-align: right;" nowrap='true' class="middleSize">手术医生：</td>
								<td style="width:130px !important;;" class="newMenu">
					    			<div class="doctorInput menuInput" style="margin-top:0px"><input class="ksnew" id="opDoctor"/><span></span></div> 
					    	       	<div id="m4" class="xmenu" style="display: none;">
					    	       		<div class="searchDept">
					    	       			<input type="text" name="searchByDeptName" placeholder="回车查询"/>
					    	       			<span class="searchMenu"><i></i>查询</span>
					    	       			<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn" >
												<span class="a-btn-text">取消</span>
											</a>
											<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn" id="clearDoctor">
												<span class="a-btn-text">清空</span>
											</a>
											<a name="menu-confirm" href="javascript:void(0);" class="a-btn" id="doctor">
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
							<td style="width:85px !important;text-align: right;" nowrap='true' class="middleSize">手术科室：</td>
							 <td  style="width: 130px;" class="newMenu" >		     
								   <div class="execDeptInput menuInput" style="margin-top:0px;"><input class="ksnew" id="execDept" readonly="readonly" /><span></span></div> 
					    	       	<div id="m3" class="xmenu" style="display: none;">
					    	       		<div class="searchDept">
					    	       			<input type="text" name="searchByDeptName" placeholder="回车查询"/>
					    	       			<span class="searchMenu"><i></i>查询</span>
					    	       			<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn">
												<span class="a-btn-text">取消</span>
											</a>						
											<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn" id="clearId">
												<span class="a-btn-text">清空</span>
											</a>
											<a name="menu-confirm" href="javascript:void(0);" class="a-btn" id="makeSure" >
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
							<td style="width:215px !important;text-align: right;" nowrap='true' ><span id="card" style="display: none">身份证号：<input class="easyui-textbox" id="identityCard" name="identityCard"   style="width:130px" /></span></td>
							<td></td>
						</tr>
					</table>
				</div>
	    </div> 
	    <div data-options="region:'center',split:false,border:false" style="width: 100%;height: 87%;" >	    	
		    <div id="tt" class="easyui-tabs" data-options="fit:true"tabPosition='bottom'>
			    <div title="手术科室汇总" style="width:100%;border: 0px;">
			    	<div id="cc" class="easyui-layout" data-options="fit:true" style="width:100%;height:100%;">
				   		<div data-options="region:'north',border:false" style="width: 100%;height: 85px;padding:5px 5px 0px 5px;overflow-x:auto;">
					   		<div style="text-align: center; width: 100%;height: 35px">
							    <h5 style="font-size: 30;font: bold;">手术科室汇总</h5>
							</div>
					   		<table style="width: 100%;padding-left: 10px;" >
					   			<tr>
					   				<td style="width: 190px;font-size: 18" nowrap='true'>统计时间：<span style="width: 200px" id="startTime2"> ${beginTime}</span></td>
					   				<td style="width: 260px;font-size: 18" nowrap='true'>至&nbsp;<span style="width: 200px"id="endTime2">${endTime }</span></td>
					   				<td style="width: 300px;font-size: 18" nowrap='true'></td>
					   				<td style="width: 350px;font-size: 18" nowrap='true'></td>
					   				<td style="width: 70px;font-size: 18;text-align: right;" nowrap='true'>科室：</td>
					   				<td nowrap='true'><span style="width: 150px" id="opDoctorDept2"></span></td>
					   				<td ></td>
					   			</tr>
					   		</table>
						</div>
						<div data-options="region:'center',border:false" style="height:88%">
					        <table id="listopkshz"  data-options="idField:'id',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:true,fixed:true,fit:true">
								<thead>
									<tr>
									    <th style="width: 7%;" data-options="field:'opDoctorDept',rowspan:2,formatter:funcdept" align="center">医生科室</th>
										<th data-options="colspan:2" align="center">择期</th>
										<th data-options="colspan:2" align="center">普通</th>
										<th data-options="colspan:2" align="center">急诊</th>
										<th data-options="colspan:2"align="center">感染</th>
										<th data-options="colspan:3" align="center">合计</th>
										<th style="width: 7%;" data-options="field:'pjje',rowspan:2,halign:'center'" align="right">平均每台费用</th>
									</tr>
									<tr>
										<th style="width: 7%;" data-options="field:'zqsl',halign:'center'" align="right">台数</th>
										<th style="width: 8%;" data-options="field:'zqje',halign:'center'" align="right">金额</th>
										<th style="width: 8%;" data-options="field:'ptsl',halign:'center'" align="right">台数</th>
										<th style="width: 8%;" data-options="field:'ptje',halign:'center'" align="right">金额</th>
										<th style="width: 8%;" data-options="field:'jzsl',halign:'center'" align="right">台数</th>
										<th style="width: 8%;" data-options="field:'jzje',halign:'center'" align="right">金额</th>
										<th style="width: 8%;" data-options="field:'grsl',halign:'center'" align="right">台数</th>
										<th style="width: 7%;" data-options="field:'grje',halign:'center'" align="right">金额</th>
										<th style="width: 7%;" data-options="field:'hjsl',halign:'center'" align="right">台数</th>
										<th style="width: 7%;" data-options="field:'hjje',halign:'center'" align="right">金额</th> 
										<th style="width: 7%;" data-options="field:'ltje',halign:'center'" align="right">医生连台费</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
			    </div>   
			    <div title="手术科室明细"  style="width:100%;">   
			        <div id="cc" class="easyui-layout" data-options="fit:true" style="width:100%;height:100%;">
				   		<div data-options="region:'north',border:false" style="width: 100%;height: 55px;overflow-x:auto;">
				   			<table style="width: 100%;padding: 10px 12px 7px 12px;">
					   			<tr>
					   				<td style="width: 185px;" nowrap='true'>统计时间：<span style="width: 200px" id="startTime3"> ${beginTime}</span></td>
					   				<td style="width: 260px;" nowrap='true'>至&nbsp;&nbsp;<span style="width: 200px"id="endTime3">${endTime }</span></td>
					   				<td style="width: 170px;font-size: 18" nowrap='true'></td>
					   				<td style="width: 170px;font-size: 18" nowrap='true'></td>
					   				<td style="width: 400px;text-align: right;" nowrap='true'>手术室：</td>
					   				<td nowrap='true'><span style="width: 350px" id="opDoctorDept3"></span></td>
					   				<td style="width: 300px;text-align: right;" nowrap='true'>手术医生：</td>
					   				<td nowrap='true'><span style="width: 150px" id="opDoctor3"></span></td>
					   			</tr>
					   		</table>
						</div>
						<div data-options="region:'center',border:false" style="height:88%">
					        <table id="listopksmx"  data-options="idField:'id',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:true,fixed:true,fit:true">
								<thead>
									<tr>
									    <th style="width: 7%;" data-options="field:'opDoctorDept',rowspan:2,formatter:funcdept" align="center">医生科室</th>
									    <th style="width: 7%;" data-options="field:'opDoctor',rowspan:2,formatter:funcemp" align="center">医生名称</th>
										<th data-options="colspan:2" align="center">择期</th>
										<th data-options="colspan:2" align="center">普通</th>
										<th data-options="colspan:2" align="center">急诊</th>
										<th data-options="colspan:2"align="center">感染</th>
										<th data-options="colspan:3,halign:'center'" align="right">合计</th>
									</tr>
									<tr>
										<th style="width: 8%;" data-options="field:'zqsl',halign:'center'" align="right">台数</th>
										<th style="width: 8%;" data-options="field:'zqje',halign:'center'" align="right">金额</th>
										<th style="width: 8%;" data-options="field:'ptsl',halign:'center'" align="right">台数</th>
										<th style="width: 8%;" data-options="field:'ptje',halign:'center'" align="right">金额</th>
										<th style="width: 8%;" data-options="field:'jzsl',halign:'center'" align="right">台数</th>
										<th style="width: 8%;" data-options="field:'jzje',halign:'center'" align="right">金额</th>
										<th style="width: 8%;" data-options="field:'grsl',halign:'center'" align="right">台数</th>
										<th style="width: 7%;" data-options="field:'grje',halign:'center'" align="right">金额</th>
										<th style="width: 7%;" data-options="field:'hjsl',halign:'center'" align="right">台数</th>
										<th style="width: 7%;" data-options="field:'hjje',halign:'center'" align="right">金额</th> 
										<th style="width: 7%;" data-options="field:'ltje',halign:'center'" align="right">医生连台费</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
			    </div>   
			    <div title="手术医生明细"  style="width:100%;">   
			      <div id="cc" class="easyui-layout" data-options="fit:true" style="width:100%;height:100%;">
				   		<div data-options="region:'north',border:false" style="width: 100%;height: 55px;overflow-x:auto;">
							<table style="width: 100%;padding: 10px 12px 7px 12px;">
					   			<tr>
					   				<td style="width: 185px;" nowrap='true'>统计时间：<span style="width: 200px" id="startTime4"> ${beginTime}</span></td>
					   				<td style="width: 260px;" nowrap='true'>至&nbsp;&nbsp;<span style="width: 200px"id="endTime4">${endTime }</span></td>
					   				<td style="width: 170px;font-size: 18" nowrap='true'></td>
					   				<td style="width: 170px;font-size: 18" nowrap='true'></td>
					   				<td style="width: 400px;text-align: right;" nowrap='true'>科室：</td>
					   				<td nowrap='true'>
					   					<span style="width: 350px;" id="opDoctorDept4" nowrap='true'></span>
					   					<input id="deptName" type="hidden">
					   				</td>
					   				<td ></td>
					   			</tr>
					   		</table>
						</div>
						<div data-options="region:'center',border:false" style="height:88%">
					        <table id="listopysmx"  data-options="idField:'id',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:true,fixed:true,fit:true">
								<thead>
									<tr>
										<th style="width: 10%;" data-options="field:'opDoctorDept',formatter:funcdept" align="center">医生科室</th>
										<th style="width: 10%;" data-options="field:'opDoctor',formatter:funcemp" align="center">医生名称</th>
										<th style="width: 10%;" data-options="field:'preDate'" align="center">手术时间</th>
										<th style="width: 10%;" data-options="field:'inpatientNo'" align="center">病历号</th>
										<th style="width: 10%;" data-options="field:'name'" align="center">患者姓名</th>
										<th style="width: 10%;" data-options="field:'itemName'" align="center">手术名称</th>
										<th style="width: 10%;" data-options="field:'totCost',halign:'center'" align="right">手术费</th>
										<th style="width: 10%;" data-options="field:'execDept',formatter:funcdept" align="center">记账科室</th> 
										<th style="width: 10%;" data-options="field:'feeOperCode',formatter:funcemp" align="center">操作员</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
			    </div> 
			    <form id="saveForm" method="post"/>  
			</div>  
	    </div>
    </div>  
<script type="text/javascript">
var beg="${beginTime}";
var end="${endTime}";
var empMap="";
var deptMap="";
$(function(){
	$('#makeSure').click(function(){
		var texts=new Array();
		var iflag = 0;
		$("#m3 .select-info li").each(function(key,value){					
			if($(this).attr("rel") != "none"){
				texts[iflag] = $(this).html();
				iflag++;
			}
		}); 
		var pp = $('#tt').tabs('getSelected'); 
		var tab = pp.panel('options').title;
		if(tab=="手术科室汇总"){
			$("#opDoctorDept2").text(texts);
		}else if(tab=="手术科室明细"){
			$("#opDoctorDept3").text(texts);
		}else if(tab=="手术医生明细"){
			$("#opDoctorDept4").text(texts);
		}
	
	});
	$('#clearId').click(function(){
		var pp = $('#tt').tabs('getSelected'); 
		var tab = pp.panel('options').title;
		if(tab=="手术科室汇总"){
			$("#opDoctorDept2").text("");
		}else if(tab=="手术科室明细"){
			$("#opDoctorDept3").text("");
		}else if(tab=="手术医生明细"){
			$("#opDoctorDept4").text("");
		}
	
	});
	$('#docDept').click(function(){
		var texts=new Array();
		var iflag = 0;
		$("#m2 .select-info li").each(function(key,value){					
			if($(this).attr("rel") != "none"){
				texts[iflag] = $(this).html();
				iflag++;
			}
		}); 
		var pp = $('#tt').tabs('getSelected'); 
		var tab = pp.panel('options').title;
			$("#deptName").val(texts);
	
	});
	$('#clearDocDept').click(function(){
		$("#deptName").val("");
	
	});
	$('#doctor').click(function(){
		var texts=new Array();
		var iflag = 0;
		$("#m4 .select-info li").each(function(key,value){					
			if($(this).attr("rel") != "none"){
				texts[iflag] = $(this).html();
				iflag++;
			}
		}); 
		var pp = $('#tt').tabs('getSelected'); 
		var tab = pp.panel('options').title;
		if(tab=="手术科室明细"){
			$("#opDoctor3").text(texts);
		}
	
	});
	$('#clearDoctor').click(function(){
		var pp = $('#tt').tabs('getSelected'); 
		var tab = pp.panel('options').title;
		if(tab=="手术科室明细"){
			$("#opDoctor3").text("");
		}
	
	});
	//手术医生编码
	$.ajax({
		url : '<%=basePath %>statistics/OperationDept/emplMapIninter.action',
		type:"post",
		success:function(data){
			empMap=data;
		}
	})
		//医生科室
	$.ajax({
		url : '<%=basePath %>statistics/OperationDept/deptMapIninter.action',
		type:"post",
		success:function(data){
			deptMap=data;
		}
	})
	setTimeout(function(){
		//手术科室汇总
		$("#listopkshz").datagrid({
			fit:true,
			url: '<%=basePath %>statistics/OperationDept/queryOpDeptTotalVo.action',
			queryParams:{beginTime:beg,endTime:end,opcDept:null,execDept:null},
			method:"post"
		})
	},600)
	
	//选择医生
	$(".doctorInput").MenuList({
		width :530,
		height :400,
		dropmenu:"#m4",//弹出层
		isSecond:false,	//是否是二级联动的第二级
		firsturl: "<%=basePath %>statistics/OperationDept/queryDoctor.action?deptTypes=", //获取列表的url，必须要写
		
	});

	
	//手术医生科室
	$(".deptInput").MenuList({
		width :530, //设置宽度，不写默认为530，不要加单位
		height :400, //设置高度，不写默认为400，不要加单位
		dropmenu:"#m2",//弹出层id，必须要写
		isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
		firsturl:"<%=basePath%>statistics/OperationDept/querysysDeptment.action?deptTypes=", //获取列表的url，必须要写
	});
	
	//手术科室
	$(".execDeptInput").MenuList({
		width :530, //设置宽度，不写默认为530，不要加单位
		height :400, //设置高度，不写默认为400，不要加单位
		dropmenu:"#m3",//弹出层id，必须要写
		isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
		firsturl:"<%=basePath%>statistics/OperationArrange/querysysDeptment.action?deptTypes=", //获取列表的url，必须要写
	});

	/**
	 * 下拉框过滤
	 * @param q
	 * @param row
	 * @param keys Array型
	 * @return
	 */
	function filterLocalCombobox(q, row, keys){
		if(keys!=null && keys.length > 0){//
			for(var i=0;i<keys.length;i++){ 
				if(row[keys[i]]!=null&&row[keys[i]]!=''){
						var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1;
						if(istrue==true){
							return true;
						}
				}
			}
		}else{
			var opts = $(this).combobox('options');
			return row[opts.textField].indexOf(q.toUpperCase()) > -1;
		}
	}
	//选项卡
	$('#tt').tabs({
		onSelect: function(title,index){
				if(title=="手术科室汇总"){
					$("#card").hide();
					var dept=$('#opDoctorDept').getMenuIds();
					var beginTime =$('#startTime').val();
					var endTime=$('#endTime').val();
					var execDept=$("#execDept").getMenuIds();
					if(beginTime&&endTime){
						if(beginTime>endTime){
							$.messager.alert("提示","开始时间不能大于结束时间！");
							return;
						}
					}
					if(beginTime){
						$("#startTime2").text(beginTime);
						beg=beginTime;
					}
					if(endTime){
						$("#endTime2").text(endTime);
						end=endTime;
					}
					var texts=new Array();
					var iflag = 0;
					$("#m3 .select-info li").each(function(key,value){					
						if($(this).attr("rel") != "none"){
							texts[iflag] = $(this).html();
							iflag++;
						}
					});
					$("#opDoctorDept2").text(texts);
					$("#listopkshz").datagrid('load',{
						beginTime:beg,endTime:end,opcDept:dept,execDept:execDept
					})
				}else if(title=="手术科室明细"){
					$("#card").hide();
					//科室关联医生
					var dept=$('#opDoctorDept').getMenuIds();
					var beginTime =$('#startTime').val();
					var endTime=$('#endTime').val();
					var opDoctor=$('#opDoctor').getMenuIds();
					var execDept=$("#execDept").getMenuIds();
					if(beginTime&&endTime){
						if(beginTime>endTime){
							$.messager.alert("提示","开始时间不能大于结束时间！");
							return;
						}
					}
					if(beginTime){
						$("#startTime3").text(beginTime);
						beg=beginTime;
					}
					if(endTime){
						$("#endTime3").text(endTime);
						end=endTime;
					}
					var textss=new Array();
					var iflags = 0;
					$("#m4 .select-info li").each(function(key,value){					
						if($(this).attr("rel") != "none"){
							textss[iflags] = $(this).html();
							iflags++;
						}
					});
					if(opDoctor){
						$("#opDoctor3").text(textss);
					}
					var texts=new Array();
					var iflag = 0;
					$("#m3 .select-info li").each(function(key,value){					
						if($(this).attr("rel") != "none"){
							texts[iflag] = $(this).html();
							iflag++;
						}
					}); 
					$("#opDoctorDept3").text(texts);
					  $("#listopksmx").datagrid({
						  url: '<%=basePath %>statistics/OperationDept/queryOpDeptDetailVo.action',
							pageSize:20,
							pageList:[20,30,50,80,100],
							pagination:true,
							fit:true,
							queryParams:{beginTime:beg,endTime:end,opcDept:dept,execDept:execDept,opDoctor:opDoctor},
							method:"post",
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
				}else{
					$("#card").show();
					//科室关联医生
					var dept=$('#opDoctorDept').getMenuIds();
					var beginTime =$('#startTime').val().trim();
					var endTime=$('#endTime').val().trim();
					var opDoctor=$('#opDoctor').getMenuIds();
					var execDept=$("#execDept").getMenuIds();
					var identityCard= $('#identityCard').textbox('getValue').trim();
					if(beginTime&&endTime){
						if(beginTime>endTime){
							$.messager.alert("提示","开始时间不能大于结束时间！");
							return;
						}
					}
					if(beginTime){
						$("#startTime4").text(beginTime);
						beg=beginTime;
					}
					if(endTime){
						$("#endTime4").text(endTime);
						end=endTime;
					}
					
					var texts=new Array();
					var iflag = 0;
					$("#m3 .select-info li").each(function(key,value){					
						if($(this).attr("rel") != "none"){
							texts[iflag] = $(this).html();
							iflag++;
						}
					});
					$("#opDoctorDept4").text(texts);
					 $("#listopysmx").datagrid({
						 url: '<%=basePath %>statistics/OperationDept/queryOpDoctorDetailVo.action',
							pageSize:20,
							pageList:[20,30,50,80,100],
							pagination:true,
							queryParams:{beginTime:beg,endTime:end,opcDept:dept,execDept:execDept,opDoctor:opDoctor,identityCard:identityCard},
							method:"post",
							fit:true,
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
		  }
		});
	
})	
	
	//查询按钮
	function find(){
		var beginTime=$("#startTime").val().trim();
		var endTime=$("#endTime").val().trim();
		var execDept=$("#execDept").getMenuIds();;
		var opDoctor=$("#opDoctor").getMenuIds();
		var dept=$('#opDoctorDept').getMenuIds();
		var pp = $('#tt').tabs('getSelected'); 
		var tab = pp.panel('options').title;
		var identityCard= $('#identityCard').textbox('getValue').trim();
		if(tab=="手术科室汇总"){
			$("#card").hide();
			if(beginTime&&endTime){
				if(beginTime>endTime){
					$.messager.alert("提示","开始时间不能大于结束时间！");
					return;
				}
			}
			if(beginTime){
				$("#startTime2").text(beginTime);
			}
			if(endTime){
				$("#endTime2").text(endTime);
			}
			 $("#listopkshz").datagrid('load',{beginTime:beginTime,endTime:endTime,opcDept:dept,execDept:execDept});
		}else if(tab=="手术科室明细"){
			$("#card").hide();
		//科室关联医生
					if(beginTime&&endTime){
						if(beginTime>endTime){
							$.messager.alert("提示","开始时间不能大于结束时间！");
							return;
						}
					}
					if(beginTime){
						$("#startTime3").text(beginTime);
					}
					if(endTime){
						$("#endTime3").text(endTime);
					}
					
			 $("#listopksmx").datagrid('load',{beginTime:beginTime,endTime:endTime,opcDept:dept,execDept:execDept,opDoctor:opDoctor});
		}else if(tab=="手术医生明细"){
			
			$("#card").show();
			//科室关联医生
					if(beginTime&&endTime){
						if(beginTime>endTime){
							$.messager.alert("提示","开始时间不能大于结束时间！");
							return;
						}
					}
					if(beginTime){
						$("#startTime4").text(beginTime);
					}
					if(endTime){
						$("#endTime4").text(endTime);
					}
					
			$("#listopysmx").datagrid('load',{beginTime:beginTime,endTime:endTime,opcDept:dept,execDept:execDept,opDoctor:opDoctor,identityCard:identityCard});
		}
	}
	//导出
	function save(){
		var beginTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		var opDoctor=$("#opDoctor").getMenuIds();
		var execDept=$("#execDept").getMenuIds();
		var dept=$('#opDoctorDept').getMenuIds();
		var identityCard= $('#identityCard').textbox('getValue').trim();
		var pp = $('#tt').tabs('getSelected'); 
		var tab = pp.panel('options').title;
		if(tab=="手术医生明细"){
			var data=$("#listopysmx").datagrid('getData');
			if(data.total==0){
				$.messager.alert("友情提示", "列表无数据，无法导出");
				setTimeout(function(){
					$(".messager-body").window('close');
				},2000);
				return;
			}
			$("#card").show();
			$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
				if (res) {
					$('#saveForm').form('submit', {
						url :"<%=basePath%>statistics/OperationDept/outOpDoctorDetailVo.action",
						onSubmit : function(param) {
							param.beginTime=beginTime,
							param.endTime=endTime,
							param.execDept=execDept,
							param.opcDept=dept,
							param.opDoctor=opDoctor,
							param.identityCard=identityCard
							
						},
						success : function(data) {
							if(data=="success"){
								$.messager.alert("操作提示", "导出成功！", "success");
							}else{
								$.messager.alert("操作提示", "导出失败！", "error");
							}
							out.clear();  
							out = pageContext.pushBody();
						},
						error : function(data) {
							$.messager.alert("操作提示", "导出失败！", "error");
						}
					})
				}
			});  
		}else{
			$.messager.alert("友情提示","只支持手术医生明细的导出！");
		}
	}
	//渲染医生信息
	function funcemp(value,row,index){
		if(value){
			return empMap[value];
		}
		return "";
	}
	//渲染医生信息
	function funcdept(value,row,index){
		if(value){
			return deptMap[value];
		}
		return "";
	}
	
	function edit(){
		
		var pp = $('#tt').tabs('getSelected'); 
		var tab = pp.panel('options').title;
		
		//科室关联医生
		var dept=$('#opDoctorDept').getMenuIds();
		var beginTime =$('#startTime').val();
		var endTime=$('#endTime').val();
		var opDoctor=$('#opDoctor').getMenuIds();
		var execDept=$("#execDept").getMenuIds();
		var identityCard= $('#identityCard').textbox('getValue').trim();
		if(beginTime&&endTime){
			if(beginTime>endTime){
				$.messager.alert("提示","开始时间不能大于结束时间！");
				return;
			}
		}
		if(beginTime){
			$("#startTime4").text(beginTime);
			beg=beginTime;
		}
		if(endTime){
			$("#endTime4").text(endTime);
			end=endTime;
		}
		var deptName=$("#opDoctorDept4").text();
		if(tab=="手术医生明细"){
			$("#card").show();
			var data=$("#listopysmx").datagrid('getData');
			if(data.total==0){
				$.messager.alert("友情提示", "列表无数据，无法打印");
				setTimeout(function(){
					$(".messager-body").window('close');
				},2000);
				return;
			}
			$.messager.confirm('打印手术医生明细', '是否打印手术医生明细?', function(res) {  
				if (res){
					var timerStr = Math.random();
					window.open ("<c:url value='/statistics/OperationDept/OpDoctorDetailReport.action?randomId='/>"+timerStr+"&beginTime="+beg+"&endTime="+end+"&identityCard="+identityCard+"&deptName="+encodeURI(encodeURI(deptName))+"&opcDept="+dept+"&execDept="+execDept+"&opDoctor="+opDoctor+"&fileName=OpDoctorDetailReport",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');     				    						
					};
			 })
		}else{
			$.messager.alert("友情提示","只支持打印手术医生明细！");
		}
	}
	/**
	 * @Description:清空所有查询条件
	 */
	function clearQuery(){
		$("#startTime").val($("#tmpStartTime").val());
		$("#endTime").val($("#tmpEndTime").val());
		$("#opDoctorDept").text('');
		$("#execDept").text('');
		$('#identityCard').textbox('clear');
		$("a[name='menu-confirm-clear']").click();
		$("#opDoctorDept2").text(''); 
		$("#opDoctorDept3").text(''); 
		$("#opDoctorDept4").text(''); 
		find();
	}
	function onclickDay(flg){
		if(flg=='1'){
			var st=beforeDay(0);
			var et=beforeDay(0);
			$('#startTime').val(st)
			$("#endTime").val(et);
			find();
		}else if(flg=='2'){
			var st=beforeDay(3);
			var et=beforeDay(1);
			$('#startTime').val(st)
			$("#endTime").val(et);
			find();
		}else if(flg=='3'){
			var st=beforeDay(7);
			var et=beforeDay(1);
			var start= st;
			var end= et;
			$('#startTime').val(st)
			$("#endTime").val(et);
			find();
		}else if(flg=='4'){
			var st=beforeDay(15);
			var et=beforeDay(1);
			$('#startTime').val(st)
			$("#endTime").val(et);
			find();
		}else if(flg=='5'){
			var myDate  = new Date();
			var month=(myDate.getMonth()+1)>9?(myDate.getMonth()+1).toString():'0' + (myDate.getMonth()+1);
			 var year = myDate.getFullYear();
			 var start= year+"-"+month+"-01";
			 var et=beforeDay(0);
			 $('#startTime').val(start)
				$("#endTime").val(et);
			 find();
		}else if(flg=='7'){
			var date = new Date();
			var year = date.getFullYear();
			var month = date.getMonth();
			if(month==0)
			{
				month=12;
				year=year-1;
			}
			if (month < 10) {
				month = "0" + month;
			}
			var login = year + "-" + month + "-" + "01";//上个月的第一天
			var lastDate = new Date(year, month, 0);
			var end = year + "-" + month + "-" + (lastDate.getDate() < 10 ? "0" + lastDate.getDate() : lastDate.getDate());//上个月的最后一天

			$('#startTime').val(login)
			$("#endTime").val(end);
			find();
		}else{
			var myDate  = new Date();
			var start= myDate.getFullYear()+"-01-01";
			 var et=beforeDay(0);
			$('#startTime').val(start)
			$("#endTime").val(et);
			find();
		}
	}
	
	function beforeDay(beforeDayNum) {
		var d = new Date();
		var endDate = dateToString(d);
		d = d.valueOf();
		d = d - beforeDayNum * 24 * 60 * 60 * 1000;
		d = new Date(d);
		var startDate = dateToString(d);
		return startDate;
	}
	function dateToString(d) {
		var y = d.getFullYear();
		var m = d.getMonth() + 1;
		var d = d.getDate();
		if (m.toString().length == 1) m = "0" + m;
		if (d.toString().length == 1) d = "0" + d;
		return y + "-" + m + "-" + d;
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>