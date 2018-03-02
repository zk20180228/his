<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>统计图表设置</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
</head>
<body style="margin: 0px; padding: 0px">
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'center',border:false" data-options="fit:true">
	    	<form method="post" id="addForm">
		    	<table class="honry-table" align="center" style="width: 100%;">
					<tr>
						<td class="honry-lable" style="width: 25%;">所在分组：<input type="hidden" id="statSetid" name="statSet.id" value="${idss}"></td>
						<td style="width: 20%;">
						<input type="hidden" name="statSet.setGroupname" id="setGroupname" value="${statSet.setGroupname}">
						<input name="statSet.setGroupid" id="setGroupid" value="${statSet.setGroupid}" class="easyui-combobox" data-options="valueField: 'value',textField: 'label',
							data: [{label: '第一组',value: '1'},{label: '第二组',value: '2'},{label: '第三组',value: '3'},
							{label: '第四组',value: '4'}],required:true,editable:false"></td>
						<td style="width: 25%;" class="honry-lable">统计名称：</td>
						<td style="width: 30%;">
							<input name="statSet.setStatname" id="setStatname" data-options="required:true" value="${statSet.setStatname}" class="easyui-textbox" >
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="width: 25%;">图表类型：</td>
						<td style="width: 20%;"><input name="statSet.setType" id="setType" class="easyui-combobox" data-options="valueField: 'value',textField: 'label',
							data: [{label: '统计图',value: '1'},{label: '统计表',value: '2'}],editable:false,required:true" value="${statSet.setType}"></td>
						<td style="width: 25%;" class="honry-lable">目标表（视图）：</td>
						<td style="width: 30%;">
							<input id="setTvname" name="statSet.setTvname" class="easyui-combobox" data-options="required:true" value="${statSet.setTvname}">
						</td>		
					</tr>
					<tr>
						<td class="honry-lable"  style="width: 25%;" class="honry-lable">动态表名类型：</td>
						<td style="width: 20%;">
							<input name="statSet.setSqlType" id="setSqlType" class="easyui-combobox" data-options="valueField: 'value',textField: 'label',
							data: [{label: '原表名',value: '1'}],editable:false,required:true" value="${statSet.setSqlType}">
						</td>
						<td style="width: 25%;" class="honry-lable">唯一标识：</td>
						<td style="width: 30%;">
							<input id="setKeyField" name="statSet.setKeyField" value="${statSet.setKeyField}" class="easyui-combobox" data-options="prompt:'请先选择目标表',required:true">
						</td>
					</tr>
					<tr>
						<td class="honry-lable"  style="width: 25%;" class="honry-lable">维度选择：</td>
						<td style="width: 20%;">
							<input name="" id="biDimension" class="easyui-combobox" data-options="prompt:'请选择维度',required:true">
						</td>
						<td colspan="2">
							<input id="weidu" style="width: 400px;">
							<input id="weidubianhao" type="hidden" name="statSet.dimensionNumber">
							<input id="weidubian" type="hidden">
						</td>
					</tr>
					<tr id="men">
						<td class="honry-lable">排序字段：</td>
				    	<td colspan="3">
				    	   <table border="0">
							<tr>
								<td id="buttonListId1"><br>
									<select multiple="multiple" id="selectAll1" style="width:180px;height:160px;">
									</select>
								</td>
								<td>
								<input type="button" value="  添加选中&nbsp;&gt;&nbsp;  " id="add_this1"><br>
								<input type="button" value="  移除选中&nbsp;&lt;&nbsp;  " id="remove_this1"><br>
								<input type="button" value="  添加全部&gt;&gt;  " id="add_all1"><br>
								<input type="button" value="  移除全部&lt;&lt;  " id="remove_all1"><br>
								</td>
								<td><br>
									<select multiple="multiple" id="selectBut1" name="statSet.setOrderField" style="width: 180px;height:160px;">
										<c:forEach var="list" items="${statSet.setOrderField }">
											<option selected="selected" value="${list }">${list }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							</table>
			    		</td>
					</tr>
					<tr>
						<td class="honry-lable"  style="width: 25%;" class="honry-lable">排序中文：</td>
						<td style="width: 20%;" colspan="3">
							<input name="statSet.setOrderChinese" value="${statSet.setOrderChinese}" id="setOrderChinese" class="easyui-textbox" data-options="prompt:'请用逗号分隔',required:true" style="width: 100%">
						</td>
					</tr>
					<tr>
						<td class="honry-lable"  style="width: 25%;" class="honry-lable">图片类型：</td>
						<td style="width: 20%;">
							<input name="statSet.setPicType" id="setPicType" value="${statSet.setPicType}" class="easyui-combobox" data-options=" valueField: 'value',textField: 'label',
							data: [{label: '',value: ''},{label: '柱状图',value: '1'},{label: '折线图',value: '2'},{label: '散点图',value: '3'},
							{label: 'K线图',value: '4'},{label: '饼图',value: '5'},{label: '雷达图',value: '6'},{label: '和弦图',value: '7'},
							{label: '地图',value: '8'},{label: '仪表盘',value: '9'},{label: '沙漏图',value: '10'},{label: '力导布局图',value: '11'},{label: '孤岛',value: '12'}],required:true">
						</td>
						<td style="width: 25%;" class="honry-lable">是否分页：</td>
						<td style="width: 30%;">
							<input type="hidden" id="setIsAutoPagesize1" name="statSet.setIsAutoPagesize" value="${statSet.setIsAutoPagesize}">
							<input type="checkBox" id="setIsAutoPagesize"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable"  style="width: 25%;" class="honry-lable">横轴字段：</td>
						<td style="width: 20%;">
							<input name="statSet.setXField" id="setXField" value="${statSet.setXField}" class="easyui-combobox" data-options="prompt:'请先选择目标表'">
						</td>
						<td style="width: 25%;" class="honry-lable">横轴描述：</td>
						<td style="width: 30%;">
							<input class="easyui-textbox" id="setXDescription" value="${statSet.setXDescription}" name="statSet.setXDescription" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable"  style="width: 25%;" class="honry-lable">纵轴字段：</td>
						<td style="width: 20%;">
							<input name="statSet.setYField" id="setYField" value="${statSet.setYField}" class="easyui-combobox" data-options="prompt:'请先选择目标表'">
						</td>
						<td style="width: 25%;" class="honry-lable">纵轴描述：</td>
						<td style="width: 30%;">
							<input class="easyui-textbox" id="setYDescription" name="statSet.setYDescription" value="${statSet.setYDescription}"/>
						</td>
					</tr>
				</table>
			</form>	
		    <div style="text-align:center;padding:5px">
		    	<a id="save" href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
		    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
		    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
		    </div>
	    </div>
	</div>
	<div id="menuWin1"></div>
	<div id="diaInpatient" class="easyui-dialog" title="维度排序" style="width:400px;height:400px;" data-options="modal:true, closed:true">   
		<table id="infoDatagrid"  style="width:380px;height:300px;" data-options="fitColumns:true,singleSelect:true,border:false">   
		</table>
	</div>
	<script type="text/javascript">
	var map;
	$(function(){
		$('#weidu').textbox({prompt:'维度,回车查看进行排序',required:true});
		bindEnterEvent('weidu',dimension,'easyui');
		var setTvname = "${statSet.setTvname}";
		var setIsAutoPagesize = "${statSet.setIsAutoPagesize}";
		if(setIsAutoPagesize==1){
			$('#setIsAutoPagesize').prop("checked","checked");
		}
		if(setTvname!=null||setTvname!=''){
			$.ajax({
				url:"<%=basePath%>statistics/bi/statisticalSetting/querycolumnNameList.action",
	    		data:{columnName:setTvname},
				type:'post',
				success: function(json) {
					var obj = $("#selectAll");
					for(var o=0;o<json.length;o++){
					    var listText = json[o].columnName;
			            obj.append("<option id='option_"+listText+"' value='" + listText + "'>"  + listText + "</option>");    
					}
					var obj = $("#selectAll1");
					for(var o=0;o<json.length;o++){
					    var listText = json[o].columnName;
			            obj.append("<option id='option_"+listText+"' value='" + listText + "'>"  + listText + "</option>");    
					}
				}
			});
		}
		$('#setTvname').combobox({
			url:"<%=basePath%>statistics/bi/statisticalSetting/querytableList.action",  
		    valueField:'tableName', 
		    textField:'tableName',
		    onSelect:function(record){
		    	$('#setKeyField').combobox({
		    		url:"<%=basePath%>statistics/bi/statisticalSetting/querycolumnNameList.action",
		    		queryParams:{columnName:record.tableName},
		    	    valueField:'columnName',
		    	    textField:'columnName',
		    	    onLoadSuccess:function(json){
		    	    	var optionIds = $('option[id^=option_]');
		    	    	if(optionIds!=null&&optionIds.length>0){
		    	    		optionIds.each(function(){
		    	    			$(this).remove();
		    	    		})
		    	    	}
		    	    	var obj = $("#selectAll");
						for(var o=0;o<json.length;o++){
						    var listText = json[o].columnName;
				            obj.append("<option id='option_"+listText+"' value='" + listText + "'>"  + listText + "</option>");    
						}
						var obj = $("#selectAll1");
						for(var o=0;o<json.length;o++){
						    var listText = json[o].columnName;
				            obj.append("<option id='option_"+listText+"' value='" + listText + "'>"  + listText + "</option>");    
						}
		    	    }
		    	});
		    	$('#setXField').combobox({
		    		url:"<%=basePath%>statistics/bi/statisticalSetting/querycolumnNameList.action",
		    		queryParams:{columnName:record.tableName},
		    	    valueField:'columnName',    
		    	    textField:'columnName'
		    	});
		    	$('#setYField').combobox({
		    		url:"<%=basePath%>statistics/bi/statisticalSetting/querycolumnNameList.action",
		    		queryParams:{columnName:record.tableName},
		    	    valueField:'columnName',    
		    	    textField:'columnName'
		    	});
		    }
		});
		$('#setGroupid').combobox({
			onSelect:function(record){
				if(record.value=="1"){
					$('#setGroupname').val("第一组");
				}else if(record.value==2){
					$('#setGroupname').val("第二组");
				}else if(record.value==3){
					$('#setGroupname').val("第三组");
				}else if(record.value==4){
					$('#setGroupname').val("第四组");
				}
			}
		});
		//维度下拉
		$('#biDimension').combobox({
    		url:"<%=basePath%>statistics/bi/statisticalSetting/queryBiDimensionSetList.action",
    	    valueField:'dimensionNumber', 
    	    textField:'dimensionName',
    	    onSelect: function(param){
    	    	var setGroupid = $('#setGroupid').combobox('getValue');
    	    	Adddilog2("统计维度、分段、指标-设置",'<%=basePath%>statistics/bi/statisticalSetting/biDimensionSetadd.action?dimensionNumber='+param.dimensionNumber+'&setGroupid='+setGroupid,'43%','60%');
    			$('#list').datagrid('reload');
			}
    	});
		//加载模式窗口
		function Adddilog2(title, url, width, height) {
			$('#menuWin1').dialog({    
			    title: title,
			    width: width,
			    height: height,    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true   
			});
		}
	})
	//回车查询 维度
	function dimension(){
		var dimensi = $("#weidubian").val();
		if(dimensi==null||dimensi==''){
			$.messager.alert('提示','请先选择进行配置！');
		}else{
			$("#diaInpatient").window('open');
			$("#infoDatagrid").datagrid({
				url: '<%=basePath%>statistics/bi/statisticalSetting/queryBiDimensionSetList.action',
				queryParams:{dimensionNumber:dimensi},
				columns:[[
					{field:'dimensionName',title:'维度',width:'30%',align:'center'} ,
					{field:'sort',title:'排序',width:'30%',align:'center'},
					{field:'sortOpr',title:'排序操作',width:'40%',align:'center'}
				]],
				onLoadSuccess: function(data){
		        	var grid = $('#infoDatagrid');  
					var options = grid.datagrid('getPager');  
					var rows = data.rows;
					map=null;
					map=new Map();
		            var rowData = data.rows;
		            $.each(rowData, function (index, value) {
		            	map.put(index,value.dimensionNumber);
		            	if(value.dimensionNumber == dimensionNumber){
		            		$('#list').datagrid('checkRow', index);
		            	}
		            });
					for(var i=0;i<rows.length;i++){
						var index = $('#infoDatagrid').datagrid('getRowIndex',rows[i]);
						var a = "";
						if(index==0){//第一行
							a='<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:icon-cancel" onclick="toDown(\''+rows[i].dimensionNumber+'\','+index+')">下移</a>';
						}else if((index+1)==rows.length){//最后一行
							a='<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:icon-cancel" onclick="toUp(\''+rows[i].dimensionNumber+'\','+index+')">上移</a>';
						}else{
							a= '<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:icon-cancel" onclick="toUp(\''+rows[i].dimensionNumber+'\','+index+')">上移</a>';
							a+='&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:icon-cancel" onclick="toDown(\''+rows[i].dimensionNumber+'\','+index+')">下移</a>';
						}
						$('#infoDatagrid').datagrid('updateRow',{
							index: index,
							row: {
								sortOpr : a
							}
						});
					}
		        },
				onDblClickRow:function(rowIndex, rowData){
					$("#diaInpatient").window('close');
				}
			});
		}
	}
	function toUp(rowId,index){//根据索引获取上一行ID
		editOrder(rowId,index,"1");
	}
	function toDown(rowId,index){//根据索引获取下一行ID
		editOrder(rowId,index,"2");
	}
	function editOrder(currentId,indexId,weiyi){//发请求到后台 把 值传到后台
		var dimensi = $("#weidubian").val();
		$("#infoDatagrid").datagrid({
			url: '<%=basePath%>statistics/bi/statisticalSetting/updateSort.action',
			queryParams:{dimensionNumber:currentId,indexId:indexId,weiyi:weiyi,dimensionNumbers:dimensi},
			columns:[[
				{field:'dimensionName',title:'维度',width:'30%',align:'center'} ,
				{field:'sort',title:'排序',width:'30%',align:'center'},
				{field:'sortOpr',title:'排序操作',width:'40%',align:'center'}
			]],
			onLoadSuccess: function(data){
	        	var grid = $('#infoDatagrid');  
				var options = grid.datagrid('getPager');  
				var rows = data.rows;
				map=null;
				map=new Map();
	            var rowData = data.rows;
	            $.each(rowData, function (index, value) {
	            	map.put(index,value.dimensionNumber);
	            	if(value.dimensionNumber == dimensionNumber){
	            		$('#list').datagrid('checkRow', index);
	            	}
	            });
				for(var i=0;i<rows.length;i++){
					var index = $('#infoDatagrid').datagrid('getRowIndex',rows[i]);
					var a = "";
					if(index==0){//第一行
						a='<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:icon-cancel" onclick="toDown(\''+rows[i].dimensionNumber+'\','+index+')">下移</a>';
					}else if((index+1)==rows.length){//最后一行
						a='<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:icon-cancel" onclick="toUp(\''+rows[i].dimensionNumber+'\','+index+')">上移</a>';
					}else{
						a= '<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:icon-cancel" onclick="toUp(\''+rows[i].dimensionNumber+'\','+index+')">上移</a>';
						a+='&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:icon-cancel" onclick="toDown(\''+rows[i].dimensionNumber+'\','+index+')">下移</a>';
					}
					$('#infoDatagrid').datagrid('updateRow',{
						index: index,
						row: {
							sortOpr : a
						}
					});
				}
				//维度赋值
				var dimension = "";
				var dimensionNu = "";
				var dimensionNuaa = "";
				for (var i = 0; i < rows.length; i++) {
					if(dimension != null && dimension != ""){
						if(rows[i].dimensionType==1){
							dimension += ","+rows[i].dimensionName+"_";
						}else{
							dimension += ","+rows[i].dimensionName;
						}
					}else if(dimension == null || dimension == ""){
						if(rows[i].dimensionType==1){
							dimension = rows[i].dimensionName+"_";
						}else{
							dimension = rows[i].dimensionName;
						}
					}
					if(dimensionNu != null && dimensionNu != ""){
						if(rows[i].dimensionType==1){
							dimensionNu += ","+rows[i].dimensionNumber+"_";
						}else{
							dimensionNu += ","+rows[i].dimensionNumber;
						}
					}else if(dimensionNu == null || dimensionNu == ""){
						if(rows[i].dimensionType==1){
							dimensionNu = rows[i].dimensionNumber+"_";
						}else{
							dimensionNu = rows[i].dimensionNumber;
						}
					}
					if(dimensionNuaa != null && dimensionNuaa != ""){
						dimensionNuaa += ","+rows[i].dimensionNumber;
					}else if(dimensionNuaa == null || dimensionNuaa == ""){
						dimensionNuaa = rows[i].dimensionNumber;
					}
				}
				$('#weidu').textbox('setValue',dimension);
		    	$('#weidubianhao').val(dimensionNu);
		    	$('#weidubian').val(dimensionNuaa);
	        },
			onDblClickRow:function(rowIndex, rowData){
				$("#diaInpatient").window('close');
			}
		});
	}
	//刷新
	function reload(){
		//实现刷新栏目中的数据
		$('#diaInpatient').datagrid('reload');
	}
	//提交表单
	function submit(){
		if($('#setIsAutoPagesize').is(':checked')){
			$('#setIsAutoPagesize1').val(1);
		}else{
			$('#setIsAutoPagesize1').val(0);
		}
		var select1 = $('#selectBut1').val();
		if(select1==null||select1==''){
			$.messager.alert('通知','请选择排序字段！');
		}else{
			$('#addForm').form('submit',{
		  		url:"<%=basePath%>statistics/bi/statisticalSetting/saveStatSet.action",
				success:function(data){
				    if(data=='success'){
				     	$.messager.alert('通知','保存成功！');
				     	$('#menuWin').dialog('close');
				     	$('#list').datagrid('load',{
						});
				    }else if(data=='error'){
				    	$.messager.alert('通知','保存失败！');
				    }
				 }
		  	});
		}
	}
    //移到右边
    $('#add_this1').click(function() {
    //获取选中的选项，删除并追加给对方
        $('#selectAll1 option:selected').appendTo('#selectBut1');
    });
	 //移到左边
    $('#remove_this1').click(function() {
        $('#selectBut1 option:selected').appendTo('#selectAll1');
    });
    //全部移到右边
    $('#add_all1').click(function() {
        //获取全部的选项,删除并追加给对方
        if(!$('#selectAll1').prop("disabled")){
        	 $('#selectAll1 option').appendTo('#selectBut1');
        }
    });
    //全部移到左边
    $('#remove_all1').click(function() {
        $('#selectBut1 option').each(function(){
        	if(!$(this).prop("disabled")){
        		$(this).appendTo('#selectAll1');
        	}
        });
    });
    //双击选项
    $('#selectAll1').dblclick(function(){ //绑定双击事件
        //获取全部的选项,删除并追加给对方
        $("option:selected",this).appendTo('#selectBut1'); //追加给对方
    });
    //双击选项
    $('#selectBut1').dblclick(function(){
       $("option:selected",this).appendTo('#selectAll1');
    });
    //关闭
    function closeLayout(){
    	$('#menuWin').dialog('close');
    }
    //清空
    function clear(){
    	$('#addForm').form('clear');
    	var optionIds = $('option[id^=option_]');
    	if(optionIds!=null&&optionIds.length>0){
    		optionIds.each(function(){
    			$(this).remove();
    		})
    	}
    }
    //维度赋值
    function weidu(dimensionName,dimensionNumber,direction){
    	var dimension = $('#weidu').textbox('getText');
    	var dimensionNu = $('#weidubianhao').val();
    	var dimensionNuaa = $('#weidubian').val();
    	if(dimension==null||dimension==''){
    		if(direction==1){
    			dimension = dimensionName+"_";
    		}else if(direction==2){
    			dimension = dimensionName;
    		}
    	}else{
    		if(direction==1){
    			dimension += ","+dimensionName+"_";
    		}else if(direction==2){
    			dimension += ","+dimensionName;
    		}
    	}
    	if(dimensionNu==null||dimensionNu==''){
    		if(direction==1){
    			dimensionNu = dimensionNumber+"_";
    		}else if(direction==2){
    			dimensionNu = dimensionNumber;
    		}
    	}else{
    		if(direction==1){
    			dimensionNu += ","+dimensionNumber+"_";
    		}else if(direction==2){
    			dimensionNu += ","+dimensionNumber;
    		}
    	}
    	if(dimensionNuaa==null||dimensionNuaa==''){
    		dimensionNuaa = dimensionNumber;
    	}else{
    		dimensionNuaa += ","+dimensionNumber;
    	}
    	$('#weidu').textbox('setValue',dimension);
    	$('#weidubianhao').val(dimensionNu);
    	$('#weidubian').val(dimensionNuaa);
    }
	</script>
</body>
</html>