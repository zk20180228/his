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
<title>人事信息统计与分析</title>
<style type="text/css">
 	#topLeft_echar_box{ 
 		float: left;
 	} 
 	#topRight_echar_box{ 
 		float: right; 
 	} 
 	#lowerLeft_echar_box{ 
 		float: left;
 	} 
 	#lowerRight_echar_box{ 
 		float: right; 
 	} 
	.tiemBox {
		width: 100%;
		top:0;
		left: 0;
		height: 40px;
		float: left;
		padding-left: 15px;
		line-height: 20px;
		padding-top:10px;
	    box-sizing: border-box;
	    z-index: 10;
	    position: relative;
	}
	.echar_main {
		position: absolute;
		padding-top: 40px;
		width: 100%;
		height: 100%;
		 box-sizing: border-box;
	}
</style>
</head>
<body>
	<div class = "tiemBox">
		科室：<input id="searchDept" class="easyui-combobox" style="width: 200px;" data-options="prompt:'拼音,五笔,自定义,编码,名称'"  />
				<a href="javascript:clearComboboxValue('searchDept');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
	</div>
	<div class="echar_main clearfix">
		<div id="topLeft_echar_box" style="width:50%;height: 50%">
			<div id="topLeftechar" style="width:80%;height:80%;"></div>
		</div>
		<div id="topRight_echar_box" style="width:50%;height: 50%;">
			<div id="topRightechar" style="width:80%;height:80%;"></div>
		</div>
		<div id="lowerLeft_echar_box" style="width:50%;height: 50%">
			<div id="lowerLeftechar" style="width:80%;height:80%;"></div>
		</div>
		<div id="lowerRight_echar_box" style="width:50%;height: 50%">
			<div id="lowerRightechar" style="width:80%;height:80%;"></div>
		</div>
	</div>
</body>
</html>
<script src="<%=basePath%>javascript/echarts/echarts.min.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
    var code='';
    $(function(){
    	start();
    });
	function start(){
		var echarObj = {
			topLeftechar: echarts.init(document.getElementById('topLeftechar')),
			topRightechar: echarts.init(document.getElementById('topRightechar')),
			lowerLeftechar: echarts.init(document.getElementById('lowerLeftechar')),
			lowerRightechar: echarts.init(document.getElementById('lowerRightechar'))
		};
		(function(echarObj,$) {
 			var topLeftResData = {
				title: {
					text: '学历',
					x: 'center',
					y: "top"
				},
				color: ['#27727B', '#FDB12A', '#79C2F5', '#009966', '#FF9190', "#CCA6D9", "#0593D3", "#E1B070"],
				tooltip: {
					trigger: 'item',
					formatter: "{a} <br/>{b} : {c} ({d}%)"
				},
				legend: {
					orient: 'vertical',
					x: 'left',
					data: [],
					padding:[40, 0, 0, 40]
				},
				series: [{
					name: '人数',
					type: 'pie',
					radius: '60%',
					center: ['60%', '60%'],
					data: [],
					itemStyle: {
						normal:{ 
	                        label:{ 
	                            show: true, 
	                            formatter: '{b} : {c} ({d}%)' 
	                        }, 
	                        labelLine :{show:true} 
	                    }
					},
						fontSize:30
					
				}]

			}
			echarObj.topLeftechar.setOption(topLeftResData);
			var topRightResData = {
				title: {
					text: '职称',
					x: 'center',
					y: "top"

				},
				color: ['#27727B', '#FDB12A', '#79C2F5', '#009966', '#FF9190', "#CCA6D9", "#0593D3", "#E1B070"],
				tooltip: {
					trigger: 'item',
					formatter: "{a} <br/>{b} : {c} ({d}%)"
				},
				legend: {
					orient: 'vertical',
					x: 'left',
					data: [],
					padding:[40, 0, 0, 40]
					
				},
				series: [{
					name: '人数',
					type: 'pie',
					radius: '60%',
					center: ['60%', '60%'],
					data: [],
					itemStyle: {
						normal:{ 
	                        label:{ 
	                            show: true, 
	                            formatter: '{b} : {c} ({d}%)' 
	                        }, 
	                        labelLine :{show:true} 
	                    } 
					}

				}]

			}
			echarObj.topRightechar.setOption(topRightResData);
			var lowerLeftResData = {
					title: {
						text: '性别',
						x: 'center',
						y: "top"
					},
					color: ['#27727B', '#FDB12A', '#79C2F5', '#009966', '#FF9190', "#CCA6D9", "#0593D3", "#E1B070"],
					tooltip: {
						trigger: 'item',
						formatter: "{a} <br/>{b} : {c} ({d}%)"
					},
					legend: {
						orient: 'vertical',
						x: 'left',
						data: [],
						padding:[40, 0, 0, 40]
					},
					series: [{
						name: '人数',
						type: 'pie',
						radius: '60%',
						center: ['60%', '60%'],
						data: [],
						itemStyle: {
							normal:{ 
		                        label:{ 
		                            show: true, 
		                            formatter: '{b} : {c} ({d}%)' 
		                        }, 
		                        labelLine :{show:true} 
		                    }
						},
							fontSize:30
						
					}]

				}
				echarObj.lowerLeftechar.setOption(lowerLeftResData);
				var lowerRightResData = {
					title: {
						text: '年龄',
						x: 'center',
						y: "top"

					},
					color: ['#27727B', '#FDB12A', '#79C2F5', '#009966', '#FF9190', "#CCA6D9", "#0593D3", "#E1B070"],
					tooltip: {
						trigger: 'item',
						formatter: "{a} <br/>{b} : {c} ({d}%)"
					},
					legend: {
						orient: 'vertical',
						x: 'left',
						data: [],
						padding:[40, 0, 0, 40]
						
					},
					series: [{
						name: '人数',
						type: 'pie',
						radius: '60%',
						center: ['60%', '60%'],
						data: [],
						itemStyle: {
							normal:{ 
		                        label:{ 
		                            show: true, 
		                            formatter: '{b} : {c} ({d}%)' 
		                        }, 
		                        labelLine :{show:true} 
		                    } 
						}

					}]

				}
				echarObj.lowerRightechar.setOption(lowerRightResData);
				
			//学历
			$.ajax({
				url:"<%=basePath%>statistics/personnelInformation/queryEducation.action",
				type:"post",
				data: {deptCode:$("#searchDept").combobox('getValue')},
				success:function(data){
					$('#topLeft_echar_box').rmoveLoading ("topLeftload")
					 if(data.length==undefined){
		   				 $("#topLeft_echar_box").setLoading({
		   					 id:"topLeftload",
		   					 isImg:false,
		   					 text:"暂无数据",
		   					 opacity:1,
		   				 	 backgroudColor:"#ffffff"
		   				 })
		   			 }else{
						var lefttmpArr = []
						for(var i = 0 ;i <data.length ;i++){
							lefttmpArr.push(data[i].name)
						}
						echarObj.topLeftechar.setOption({
							legend: {
								data:lefttmpArr
							},
		       				series: [{
		    					data:data
		    				}]
						})
		   			 }
				}
			});
			//职称
			$.ajax({
				url:"<%=basePath%>statistics/personnelInformation/queryTitle.action",
				type:"post",
				data: {deptCode:$("#searchDept").combobox('getValue')},
				success:function(data){
					$('#topRight_echar_box').rmoveLoading ("topRightload")
					 if(data.length==undefined){
		   				 $("#topRight_echar_box").setLoading({
		   					 id:"topRightload",
		   					 isImg:false,
		   					 text:"暂无数据",
		   					 opacity:1,
		   				 	 backgroudColor:"#ffffff"
		   				 })
		   			 }else{
						var righttmpArr = []
						for(var i = 0 ;i <data.length ;i++){
							righttmpArr.push(data[i].name)
						}
						echarObj.topRightechar.setOption({
							legend: {
								data:righttmpArr
							},
		       				series: [{
		    					data:data
		    				}]
						})
		   			 }
				}
			});
			//性别
			$.ajax({
				url:"<%=basePath%>statistics/personnelInformation/querySex.action",
				type:"post",
				data: {deptCode:$("#searchDept").combobox('getValue')},
				success:function(data){
					$('#lowerLeft_echar_box').rmoveLoading ("lowerLeftload")
					 if(data.length==undefined){
		   				 $("#lowerLeft_echar_box").setLoading({
		   					 id:"lowerLeftload",
		   					 isImg:false,
		   					 text:"暂无数据",
		   					 opacity:1,
		   				 	 backgroudColor:"#ffffff"
		   				 })
		   			 }else{
						var righttmpArr = []
						for(var i = 0 ;i <data.length ;i++){
							righttmpArr.push(data[i].name)
						}
						echarObj.lowerLeftechar.setOption({
							legend: {
								data:righttmpArr
							},
		       				series: [{
		    					data:data
		    				}]
						})
		   			 }
				}
			});
			//年龄
			$.ajax({
				url:"<%=basePath%>statistics/personnelInformation/queryAge.action",
				type:"post",
				data: {deptCode:$("#searchDept").combobox('getValue')},
				success:function(data){
					$('#lowerRight_echar_box').rmoveLoading ("lowerRightload")
					 if(data.length==undefined){
		   				 $("#lowerRight_echar_box").setLoading({
		   					 id:"lowerRightload",
		   					 isImg:false,
		   					 text:"暂无数据",
		   					 opacity:1,
		   				 	 backgroudColor:"#ffffff"
		   				 })
		   			 }else{
						var righttmpArr = []
						for(var i = 0 ;i <data.length ;i++){
							righttmpArr.push(data[i].name)
						}
						echarObj.lowerRightechar.setOption({
							legend: {
								data:righttmpArr
							},
		       				series: [{
		    					data:data
		    				}]
						})
		   			 }
				}
			});
		}
		)(echarObj,$);
	}
	var havSelect = true;
		//科室下拉框及时定位查询 
		$('#searchDept').combobox({
			url: '<%=basePath%>/baseinfo/department/departmentCombobox.action',
			valueField:'deptCode',    
			textField:'deptName',
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
				}
			},
			onSelect:function(rec){
				var code=rec.deptCode;
				havSelect = false;
				start();
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
						var deptCode = onlyOne.deptCode;
						$('#searchDept').combobox('setValue',deptMap[deptCode]);
						$('#searchDept').combobox('select',deptCode);
					}
				}
				havSelect=true;							
			}
		});
		//清除科室信息查询下拉框，查询条件的值后,动态加载页面
	   	function clearComboboxValue(id){
	   		$('#searchDept').combobox('setValue','');
			start();
	   	}
	    function filterLocalCombobox(q, row, keys){
			if(keys!=null && keys.length > 0){
				for(var i=0;i<keys.length;i++){
					if(row[keys[i]]!=null&&row[keys[i]]!=''){
						var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1||row[keys[i]].indexOf(q) > -1;
						if(istrue==true){
							return true;
						}
					}
				}
			}else{
				var opts = $(this).combobox('options');
				return row[opts.textField].indexOf(q.toUpperCase()) > -1||row[opts.textField].indexOf(q) > -1;
			}
		}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	