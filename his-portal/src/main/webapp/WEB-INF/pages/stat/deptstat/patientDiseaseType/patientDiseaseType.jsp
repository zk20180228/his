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
<title>患者疾病类型统计分析</title>
</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script> 
<script type="text/javascript">
var menuAlias = '${menuAlias}';
var icd1 = [];
var icd2 = [];
inntView1();
inntView2();
$(function(){
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
	$('#m3 .addList h2 input').click();
	$('a[name=\'menu-confirm\']').click();
	var flag=$('#deptName').getMenuIds();
	$('a[name=\'menu-confirm-clear\']').click();
	if(flag==''||flag==null){
		$("#tableList").datagrid();
		$("body").setLoading({
			id:"body",
			isImg:false,
			text:"无数据权限"
		});
	}else{
		echarts1();
		echarts2();
		$('#tableList').datagrid({    
			url: '<%=basePath%>statistics/patientDiseaseType/querylistPatientDiseaseType.action',
			queryParams: {
				deptCode: $('#deptName').getMenuIds(),
				startTime:$('#startTime').val(),
				endTime:$('#endTime').val(),
				sex:$('#CodeSex').combobox('getValue')
			},
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,100],
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
				}
			}
		});
	}
	//性别
	$('#CodeSex').combobox({
		url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
	    valueField:'encode',    
	    textField:'name',
	    editable:false
	});
});
function echarts1(){
	$("#main1").parent().setLoading("main1ed")
	//未治愈
	$.ajax({ 
		   url:"<%=basePath%>statistics/patientDiseaseType/queryIcdHealed.action",
		   type:"post",
		   data: {
			    deptCode: $('#deptName').getMenuIds(),
				startTime:$('#startTime').val(),
				endTime:$('#endTime').val(),
				sex:$('#CodeSex').combobox('getValue')
			},
           dataType: "json",
		   success:function(data){
				$("#main1").parent().rmoveLoading("main1ed")
			   	icd1=data;
				inntView1();
		   }
	 	});
	}
function echarts2(){
	$("#main2").parent().setLoading("main1ed")
	//死亡
	$.ajax({ 
		   url:"<%=basePath%>statistics/patientDiseaseType/queryIcdDeath.action",
		   type:"post",
		   data: {
			    deptCode: $('#deptName').getMenuIds(),
				startTime:$('#startTime').val(),
				endTime:$('#endTime').val(),
				sex:$('#CodeSex').combobox('getValue')
			},
           dataType: "json",
		   success:function(data){
		   		$("#main2").parent().rmoveLoading("main1ed")
   			 	icd2=data;
   			 	inntView2();
		   }
	 	});
}
//查询按钮
 function searchFrom(){
	 $('#tableList').datagrid('load', {
		deptCode: $('#deptName').getMenuIds(),
		startTime:$('#startTime').val(),
		endTime:$('#endTime').val(),
		sex:$('#CodeSex').combobox('getValue')
	});
	 echarts1();
	 echarts2();
 }
 
//重置按钮
 function clear(){
	$('#startTime').val('');
	$('#endTime').val('');
 	$('#deptName').val('');
	$('#deptName').attr('name','');
	$('#CodeSex').combobox('setValue','');
	searchFrom();
 }
	
 	function inntView1(){
 		var icdNames1=[];
 		var icdNums1=[];
			// 路径配置
			require.config({
			    paths: {
			        echarts: '${pageContext.request.contextPath}/javascript/echarts'
			    }
			});
			require(
				    [
				        'echarts',
				        'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
				        'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
				    ],
				    function (ec) {
					 	var myChart1 = ec.init(document.getElementById('main1'));
					 	var dNum = 0;
						if(icd1.length==0){
							icdNames1.push('');
							icdNums1.push(0);
							persent = 100;
						}else{
							for(i=0;i<icd1.length;i++){
								icdNames1.push(icd1[i].icdName);
								icdNums1.push(icd1[i].icdNum);
								dNum++;
							}
							persent = 10/(dNum<10?10:dNum)*100;
						}
						var option1 = {
							title : {
							        text: 'ICD未治愈例数统计'
							    },
						    tooltip : {
						        trigger: 'axis',
						        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
						            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
						        },
						    },
						    legend: {
						        data:['未治愈'],
						        selected: {
			  						'未治愈': true
			  					}
						    },
						    grid: {
						        left: '3%',
						        right: '4%',
						        bottom: '3%',
						        containLabel: true
						    },
						    dataZoom : {
						        show : true,
						        realtime : true,
						        zoomLock : true,
						        start : 0,
						        end : persent
						    },
						    xAxis : [
						        {
						            type : 'category',
						            data : icdNames1
						        }
						    ],
						    yAxis : [
						        {
						            type : 'value',
						        }
						    ],
						    series : [
						        {
						            name:'未治愈',
						            type:'bar',
						            data: icdNums1,
						            barWidth : 30,
						            markPoint : {
							                data : [
							                    {type : 'max', name: '最大值'},
							                    {type : 'min', name: '最小值'}
							                ]
							            },
							            markLine : {
							                data : [
							                    {type : 'average', name: '平均值'}
							                ]
							            }
						        }
						    ]
						};
					myChart1.setOption(option1);
				    }
				  );
		}
 	function inntView2(){
 		var icdNames2=[];
 		var icdNums2=[];
		// 路径配置
		require.config({
		    paths: {
		        echarts: '${pageContext.request.contextPath}/javascript/echarts'
		    }
		});
		require(
			    [
			        'echarts',
			        'echarts/chart/bar', // 使用柱状图就加载bar模块，按需加载
			        'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
			    ],
			    function (ec) {
				 	var myChart2 = ec.init(document.getElementById('main2'));
				 	var dNum = 0;
					if(icd2.length==0){
						icdNames2.push('');
						icdNums2.push(0);
						persent = 100;
					}else{
						for(i=0;i<icd2.length;i++){
							icdNames2.push(icd2[i].icdName);
							icdNums2.push(icd2[i].icdNum);
							dNum++;
						}
						persent = 10/(dNum<10?10:dNum)*100;
					}
					var option1 = {
						title : {
					        text: 'ICD死亡例数统计'
					    },
					    tooltip : {
					        trigger: 'axis',
					        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
					        },
					    },
					    legend: {
					        data:['死亡'],
					        selected: {
		  						'死亡': true
		  					}
					    },
					    grid: {
					        left: '3%',
					        right: '4%',
					        bottom: '3%',
					        containLabel: true
					    },
					    dataZoom : {
					        show : true,
					        realtime : true,
					        zoomLock : true,
					        start : 0,
					        end : persent
					    },
					    xAxis : [
					        {
					            type : 'category',
					            data : icdNames2
					        }
					    ],
					    yAxis : [
					        {
					            type : 'value'
					        }
					    ],
					    series : [
					        {
					            name:'死亡',
					            type:'bar',
					            data: icdNums2,
					            barWidth : 30,
					            markPoint : {
						                data : [
						                    {type : 'max', name: '最大值'},
						                    {type : 'min', name: '最小值'}
						                ]
						            },
						            markLine : {
						                data : [
						                    {type : 'average', name: '平均值'}
						                ]
						            }
					        }
					    ]
					};
				myChart2.setOption(option1);
			    }
			  );
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<body style="margin: 0px; padding: 0px;">
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	 	<div data-options="region:'north',collapsed:false,border:false" style="width: 100%;height: 50%;overflow: hidden;"> 
	 		<table border="0" style="width: 100%;height: 10%;padding:7px 10px">
	 			<tr>
		 			<td style="width: 50px;">科室:</td>
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
									<ul class="addDept">
									</ul>
								</div>	
								<div class="depts-dl">
									<div class="addList">
									</div>
									<div class="tip" style="display:none">没有检索到数据</div>
								</div>	
										
							</div>
		    		</td>
		    		<td style="width:45px;" align="left">性别:</td>
					<td style="width:100px;">
						<input id="CodeSex" class="easyui-combobox" style="width:100px;"/>
					</td>
		    		<td style="width:80px;" align="right">出生日期:</td>
					<td style="width:110px;">
						<input id="startTime" class="Wdate" type="text" name="startTime" value="${startTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}'})" style="width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
						
					</td>
					<td style="width:40px;" align="center">至</td>
					<td style="width:110px;">
						<input id="endTime" class="Wdate" type="text" name="endTime" value="${endTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'})" style="width:110px !important;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
		    		<td>
			    		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search">查询</a>
			 			<a href="javascript:clear();void(0)" class="easyui-linkbutton" onclick="clear()" iconCls="reset">重置</a>
		    		</td>
	    		</tr>
	    	 	
	    	 	<tr>
	    	 		<td align="center" colspan="10"  ><font style = "font-size: 32px !important;" class="empWorkTit" size="6">患者疾病类型统计分析</font></td>
	    	 	</tr>
	    	 </table>
	    <div data-options="region:'center',border:false" style="width: 100%;height: 80%;">
	    	<table id="tableList" class="easyui-datagrid" style="padding:5px 5px 5px 5px;" data-options="singleSelect:'true',striped:true,fitColumns:true,rownumbers:true,fit:true">
				<thead>
					<tr>
						<th data-options="field:'deptName',width:'9%',align:'center'">部门</th>
						<th data-options="field:'total',width:'5%',align:'center'">患者数量</th>
						<th data-options="field:'cure',width:'5%',align:'center'">治愈数量</th>
						<th data-options="field:'curePer',width:'7%',align:'center'">治愈数量百分比%</th>
						<th data-options="field:'better',width:'5%',align:'center'">好转数量</th>
						<th data-options="field:'betterPer',width:'7%',align:'center'">好转数量百分比%</th>
						<th data-options="field:'healed',width:'5%',align:'center'">未治愈数量</th>
						<th data-options="field:'healedPer',width:'7%',align:'center'">未治愈数量百分比%</th>
						<th data-options="field:'death',width:'5%',align:'center'">死亡数量</th>
						<th data-options="field:'deathPer',width:'7%',align:'center'">死亡数量百分比%</th>
						<th data-options="field:'normal',width:'5%',align:'center'">正常产数量</th>
						<th data-options="field:'normalPer',width:'7%',align:'center'">正常产数量百分比%</th>
						<th data-options="field:'planning',width:'5%',align:'center'">计生数量</th>
						<th data-options="field:'planningPer',width:'7%',align:'center'">计生数量百分比%</th>
						<th data-options="field:'other',width:'5%',align:'center'">其他数量</th>
						<th data-options="field:'otherPer',width:'7%',align:'center'">其他数量百分比%</th>
					</tr>
				</thead>
			</table>
	    </div>   
	</div>
	<div data-options="region:'west',collapsible:false" id="main1" style="height:100%;width: 50%;"></div>
	<div data-options="region:'east',collapsible:false" id="main2" style="height:100%;width: 50%;"></div>
	</div>	
</body>
</html>
