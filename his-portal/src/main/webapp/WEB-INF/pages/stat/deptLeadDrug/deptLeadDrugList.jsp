<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
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
var empFlag=true;
var startTime="${sTime}";
var endTime="${eTime}";
var packunit="";
var deptMap="";
var ids;//存取当前登录科室Code
var menuAlias = '${menuAlias}';
var flag;
	$(function(){
		$('#table2').datagrid({
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
		$("#table2").datagrid('loadData', { total: 0, rows: [] });
		$("#Stime").val("${sTime}");
		$("#Etime").val("${eTime}");
		//药品性质下拉
		$('#drugxz').combobox({
			url:'<%=basePath%>statistics/DeptLeadDrug/querydrugxz.action',
			valueField:'encode',    
		    textField:'name'   
		});
		//药品名称下拉
		$('#drugName').combobox({
			url:'<%=basePath%>statistics/DeptLeadDrug/querydrugName.action',
			valueField:'code',    
		    textField:'name'   
		});
			$(".deptInput").MenuList({
				width :530, //设置宽度，不写默认为530，不要加单位
				height :400, //设置高度，不写默认为400，不要加单位
				menulines:2, //设置菜单每行显示几列（1-5），默认为2
				dropmenu:"#m3",//弹出层id，必须要写
				isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
				para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
				firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
				relativeInput:".doctorInput",	//与其级联的文本框，必须要写
				relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
			});
			$('#m3 .addList h2 input').click();
			$('a[name=\'menu-confirm\']').click();
			flag=$('#drugDept').getMenuIds();
			$('a[name=\'menu-confirm-clear\']').click();
			if(flag==''){
				$("#table2").datagrid();
				$("body").setLoading({
					id:"body",
					isImg:false,
					text:"无数据权限"
				});
			}
		//查询包装单位
		$.ajax({
			url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionaryMap.action",		
			data:{type:'packunit'},
			type:'post',
			success: function(drugpackagingunitdata) {					
				packunit= drugpackagingunitdata;	
			}
		});
		//查询科室
		$.ajax({
			url: "<%=basePath%>statistics/DeptLeadDrug/queryDeptMap.action",		
			type:'post',
			success: function(data) {
				deptMap=data;	
			}
		});
		
	});
	//查询各科室领药记录
	function queryList(){
		var Stime=$("#Stime").val();
		var Etime=$("#Etime").val();
		if(Stime&&Etime){
		    if(Stime>Etime){
		      $.messager.alert("提示","开始时间不能大于结束时间！");
		      close_alert();
		      return ;
		    }
		  }
		if(flag=='true'){
			var drugDept=$('#drugDept').getMenuIds();
		}else{
			var drugDept=ids;
		}
		var drugxz=$('#drugxz').combobox('getValue');
		var drugName=$('#drugName').combobox('getValue');
		$('#st').text(Stime);
		$('#et').text(Etime);
		$('#table2').datagrid({
			type:'post',
			pagination:true,
			pageSize:20,
			pageList:[20,40,80],
			fit:true,
			rownumbers:true,
			fitColumns:true,
			queryParams:{sTime:Stime,eTime:Etime,drugDept:drugDept,drugxz:drugxz,drugName:drugName},
			url:'<%=basePath%>statistics/DeptLeadDrug/queryTableList.action?menuAlias=${menuAlias}',
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
	//导出
	function daochu(){
		var rowss = $('#table2').datagrid('getRows');
		if(rowss!=null&&rowss!=''){
			var rows=JSON.stringify(rowss);
			$.ajax({
				url:'<%=basePath%>statistics/DeptLeadDrug/exportout.action',
				data:{rows:rows},
				type:'post',
				success:function(data){
					var date=data;
					if(date=="success"){
						$.messager.alert('提示',"导出成功");
					}else{
						$.messager.alert('提示',"导出失败");
					}
				},
				error:function(data){
					$.messager.alert('提示',"导出失败");
				}
			});
		}else{
			$.messager.alert('提示','列表中没有数据无法进行导出');
		}
	}
	//渲染单位
	function unitFunction(value,row,index){
		if(value!=null&&value!=""){
			if(packunit[value]!=null&&packunit[value]!==""){
			return packunit[value];					
			}else{
				return value;
			}
		}
	}
	//渲染部门
	function deptFunction(value,row,index){
		if(value!=null&&value!=""){
			if(deptMap[value]!=null&&deptMap[value]!==""){
			return deptMap[value];					
			}else{
				return value;
			}
		}
	}
	//按时间段查询
	function queryMidday(val){
		if(val==1){
			var myDate = new Date();
			var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			var date=myDate.getDate();
			month=month<10?"0"+month:month;
			date=date<10?"0"+date:date;
			var day=year+"-"+month+"-"+date;
			var Stime = $('#Stime').val(day);
		    var Etime = $('#Etime').val(day);
		    queryList();
		}else if(val==2){
			var nowd = new Date();
			var myDate=new Date(nowd.getTime() - 3 * 24 * 3600 * 1000);
			var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			var date=myDate.getDate();
			month=month<10?"0"+month:month;
			date=date<10?"0"+date:date;
			var day2=year+"-"+month+"-"+date;
			var Stime = $('#Stime').val(day2);
			 nowd = new Date();
			 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
			 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			 date=myDate.getDate();
		  	month=month<10?"0"+month:month;
			 date=date<10?"0"+date:date;
			 day2=year+"-"+month+"-"+date;
		    var Etime = $('#Etime').val(day2);
		    queryList();
		}else if(val==3){
			var nowd = new Date();
			var myDate=new Date(nowd.getTime() - 7 * 24 * 3600 * 1000);
			var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			var date=myDate.getDate();
			month=month<10?"0"+month:month;
			date=date<10?"0"+date:date;
			var day3=year+"-"+month+"-"+date;
			var Stime = $('#Stime').val(day3);
			 nowd = new Date();
			 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
			 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			 date=myDate.getDate();
		  	month=month<10?"0"+month:month;
			 date=date<10?"0"+date:date;
			 day3=year+"-"+month+"-"+date;
		    var Etime = $('#Etime').val(day3);
		    queryList();
		}else if(val==4){
			var myDate = new Date();
			var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			month=month<10?"0"+month:month;
			var day=year+"-"+month+"-"+"01";
			var Stime = $('#Stime').val(day);
			  myDate = new Date();
				 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
				 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
				 date=myDate.getDate();
				 month=month<10?"0"+month:month;
				 date=date<10?"0"+date:date;
				 day=year+"-"+month+"-"+date;
		    var Etime = $('#Etime').val(day);
		    queryList();
		}else if(val==5){
			var myDate = new Date();
			var year=myDate.getFullYear();
			var day=year+"-"+"01"+"-"+"01";
			var Stime = $('#Stime').val(day);
			  myDate = new Date();
				 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
				 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
				 date=myDate.getDate();
				 month=month<10?"0"+month:month;
				 date=date<10?"0"+date:date;
				 day=year+"-"+month+"-"+date;
		    var Etime = $('#Etime').val(day);
		    queryList();
		}else if(val==6){
			var nowd = new Date();
			var myDate=new Date(nowd.getTime() - 15 * 24 * 3600 * 1000);
			var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			var date=myDate.getDate();
			month=month<10?"0"+month:month;
			date=date<10?"0"+date:date;
			var day3=year+"-"+month+"-"+date;
			var Stime = $('#Stime').val(day3);
			 nowd = new Date();
			 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
			 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
			 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
			 date=myDate.getDate();
		  	month=month<10?"0"+month:month;
			 date=date<10?"0"+date:date;
			 day3=year+"-"+month+"-"+date;
		    var Etime = $('#Etime').val(day3);
		    queryList();
		}else if(val==7){
			var date=new Date();
			var year=date.getFullYear();
			var month=date.getMonth();
			if(month==0){
				year=year-1;
				month=12;
			}
			var startTime=year+'-'+month+'-01';
			 $('#Stime').val(startTime);
			var date=new Date(year,month,0);
			var endTime=year+'-'+month+'-'+date.getDate();
			$('#Etime').val(endTime);
			 queryList();
		}
		
	}
//重置按钮
function clear(){
	$('#Stime').val(startTime);
	$('#Etime').val(endTime);
	if(flag=='true'){
		$('#drugDept').val('');
	}
	$("a[name='menu-confirm-cancel']").click();
	$("a[name='menu-confirm-clear']").click();
	$('#drugxz').combobox('setValue','');
	$('#drugName').combobox('setValue','');
	$('#table2').datagrid('loadData', { total: 0, rows: [] });
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body class="easyui-layout"  style="width:100%;height: 100%;" >
<!-- 		<div > -->
			<form  style="width: 100%;padding:5px 5px 5px 5px;height: 50px;" >
				<table border="0" style="width: 100%;height:100%;" cellspacing="0" cellpadding="0">
				    <tr >
				    	<td style="width: 370px"></td>
				    	<td></td>
						<td >
							<a href="javascript:void(0)" onclick="queryMidday(5)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当年</a>&nbsp;
							<a href="javascript:void(0)" onclick="queryMidday(4)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当月</a>&nbsp;
							<a href="javascript:void(0)" onclick="queryMidday(7)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">上月</a>&nbsp;
							<a href="javascript:void(0)" onclick="queryMidday(6)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">十五天</a>&nbsp;
							<a href="javascript:void(0)" onclick="queryMidday(3)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">七天</a>&nbsp;
							<a href="javascript:void(0)" onclick="queryMidday(2)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">三天</a>&nbsp;
							<a href="javascript:void(0)" onclick="queryMidday(1)" style="float: right;margin-left: 10px" class="easyui-linkbutton" iconCls="icon-date">当天</a>&nbsp;
						</td>
				    </tr>
					<tr>
						<td class="deptLeadDrugListSize1" style="width:400px;">
							日期：
							<input id="Stime" name="Stime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							至
							<input id="Etime" name="Etime"  class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							&nbsp;领药科室：
						</td>
						<td class="newMenu" style="width:120px;position: relative;">
							<div class="deptInput menuInput" style="margin-top:-4px;">
							<input id="drugDept" class="ksnew"  readonly/><span></span></div>
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
												<div class="addList"></div>
												<div class="tip" style="display:none">没有检索到数据</div>
												</div>	
												
							</td>
							<td>					
								&nbsp;药品性质：<input id="drugxz" class="easyui-combobox"/>
								&nbsp;药品名称：<input id="drugName" class="easyui-combobox"/>
								<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)"   class="easyui-linkbutton" onclick="queryList()" data-options="iconCls:'icon-search'" style="margin:0px 0px 0px 0px" >查询</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:set">
								<a href="javascript:clear();void(0)" onclick="" class="easyui-linkbutton" style="margin:0px 0px 0px 0px" data-options="iconCls:'reset'">重置</a>
								</shiro:hasPermission>
							</td>
					</tr>
				</table>
			</form>
			<div data-options="region:'center',border:false" style="width: 100%;height:90%;" >
				<table id="table2" class="easyui-datagrid" style="width:100%;height:100%;" data-options="fit:true,pagination:true" >
					<thead>
						<tr>
							 <th data-options="field:'deptCode',formatter:deptFunction,width:'10%'">领药科室</th>   
							 <th data-options="field:'drugCnameinputcode',width:'10%'">通用编码</th>   
							 <th data-options="field:'drugCode',width:'10%'">药品编码</th>   
							 <th data-options="field:'tradeName',width:'10%'">药品名称</th>   
							 <th data-options="field:'specs',width:'10%'">规格</th>   
							 <th data-options="field:'packUnit',formatter:unitFunction,width:'10%'"align="right"halign="left">包装单位</th>   
							 <th data-options="field:'applyNum',width:'9%'" align="right"halign="left">数量</th>   
							 <th data-options="field:'sumCost',width:'9%'" align="right" halign="left">金额</th>   
							 <th data-options="field:'drugPlaceoforigin',width:'10%'">产地</th>   
						</tr>
					</thead>
				</table>
			</div>
<!-- 	</div> -->
</body>
</html>