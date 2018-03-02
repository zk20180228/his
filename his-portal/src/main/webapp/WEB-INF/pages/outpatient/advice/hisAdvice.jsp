 <%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
 <%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	</head>
	<body style="margin:0px;padding:0px">
		<div id="hisEl" class="easyui-layout" data-options="fit:true,border:false">   
			<div data-options="region:'west',title:'患者信息',split:false,border:false" style="width:20%;padding-top:5px;padding-left:5px">
				<input id="idCardNoHis" style="width:114px;"/>
				<a href="javascript:void(0)" cardNo="" onclick="readOne()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
<!-- 				<a href="javascript:void(0)" onclick="searchInfoHis()" class="easyui-linkbutton" iconCls="icon-bullet_feed" style="height:24px;">读卡</a> -->
				<ul id="hisAdviceTree" style="padding-top:5px;"></ul>
			</div>   
			<div data-options="region:'center',title:'医嘱信息&nbsp&nbsp<span style=&quot;font-size:11px;border-top:0&quot; class=&quot;hisAdviceTip&quot;>复制医嘱：CTRL+C 全选：CTRL+A</span>'">
				<table id="hisAdvice" class="easyui-datagrid" data-options="striped:true,border:false,fit:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,width:'100%'">   
		    		<thead frozen="true">  
				        <tr>  
				        	<th data-options="field:'id',width:100,hidden:true">id*</th>
				        	<th data-options="field:'adviceNo',width:100,hidden:true">处方号*</th>
				        	<th data-options="field:'colour',width:24,align:'center',styler:functionColour,formatter:functionRowNum" ></th>
				            <th data-options="field:'ck',checkbox:true" ></th>
				            <th data-options="field:'limit',width:18" ></th>
				            <th data-options="field:'adviceNameView',width:260">医嘱名称</th>  
				        </tr>  
				    </thead>
				    <thead>   
				        <tr>   
				            <th data-options="field:'type',width:100,hidden:true">类型*</th>   
				            <th data-options="field:'ty',width:100,hidden:true">是否为药品*</th>   
				            <th data-options="field:'adviceType',width:100,hidden:true">医嘱类型</th>   
				            <th data-options="field:'adviceId',width:100,hidden:true">医嘱名称Id*</th>   
				            <th data-options="field:'adviceName',width:100,hidden:true">医嘱名称Hid*</th>   
				            <th data-options="field:'adPrice',width:100,hidden:true">价格*</th>   
				            <th data-options="field:'adPackUnitHid',width:100,hidden:true">包装单位*</th>   
				            <th data-options="field:'adMinUnitHid',width:100,hidden:true">单位*</th>   
				            <th data-options="field:'adDosaUnitHid',width:100,hidden:true">剂量*</th> 
				            <th data-options="field:'adDosaUnitHidJudge',width:100,hidden:true">剂量*</th>    
				            <th data-options="field:'adDrugBasiHid',width:100,hidden:true">基本剂量*</th>   
				            <th data-options="field:'specs',width:100,hidden:true">规格*</th>   
				            <th data-options="field:'sysType',width:100,hidden:true">系统类别*</th>   
				            <th data-options="field:'drugType',width:100,hidden:true">药品类别*</th>   
				            <th data-options="field:'minimumcost',width:100,hidden:true">最小费用代码*</th>   
				            <th data-options="field:'packagingnum',width:100,hidden:true">包装数量*</th>   
				            <th data-options="field:'nature',width:100,hidden:true">药品性质*</th>   
				            <th data-options="field:'ismanufacture',width:100,hidden:true">自制药标志*</th>   
				            <th data-options="field:'dosageform',width:100,hidden:true">剂型*</th>   
				            <th data-options="field:'isInformedconsent',width:100,hidden:true">是否知情同意书*</th>   
				            <th data-options="field:'auditing',width:100,hidden:true">是否需要审核*</th>   
				            <th data-options="field:'group',width:30,formatter:functionGroupHis">组</th>   
				            <th data-options="field:'totalNum',width:50">总量</th>   
				            <th data-options="field:'totalUnitHid',width:100,hidden:true">总单位Id*</th> 
				            <th data-options="field:'totalUnitHidJudge',width:100,hidden:true">总单位Id*</th>    
				            <th data-options="field:'totalUnit',width:50">总单位</th>   
				            <th data-options="field:'dosageHid',width:100,hidden:true">每次用量*</th>   
				            <th data-options="field:'dosage',width:100">每次用量</th>   
				            <th data-options="field:'unit',width:50">单位</th>   
				            <th data-options="field:'setNum',width:50">付数</th>   
				            <th data-options="field:'frequencyHid',width:100,hidden:true">频次Id*</th>   
				            <th data-options="field:'frequency',width:100" >频次编码</th>   
				            <th data-options="field:'usageNameHid',width:100,hidden:true">用法Id*</th>   
				            <th data-options="field:'usageName',width:100">用法名称</th>   
				            <th data-options="field:'injectionNum',width:80">院注次数</th>   
				            <th data-options="field:'openDoctor',width:100">开立医生</th>   
				            <th data-options="field:'executiveDeptHid',width:100,hidden:true">执行科室Id*</th>   
				            <th data-options="field:'executiveDept',width:100,">执行科室</th>   
				            <th data-options="field:'isUrgentHid',width:100,hidden:true">加急Id*</th>   
				            <th data-options="field:'isUrgent',width:30">加急</th>   
				            <th data-options="field:'inspectPartId',width:100,hidden:true">检查部位Id*</th>   
				            <th data-options="field:'inspectPart',width:100">检查部位</th>   
				            <th data-options="field:'sampleTeptHid',width:100,hidden:true">样本类型Id*</th>   
				            <th data-options="field:'sampleTept',width:100">样本类型</th>   
				            <th data-options="field:'minusDeptHid',width:100,hidden:true">扣库科室Id*</th>   
				            <th data-options="field:'minusDept',width:100">扣库科室</th>   
				            <th data-options="field:'remark',width:100">备注</th>   
				            <th data-options="field:'inputPeop',width:100">录入人</th>   
				            <th data-options="field:'openDept',width:100">开立科室</th>   
				            <th data-options="field:'startTime',width:100">开立时间</th>   
				            <th data-options="field:'endTime',width:100">停止时间</th>   
				            <th data-options="field:'stopPeop',width:100">停止人</th>   
				            <th data-options="field:'isSkinHid',width:100,hidden:true">是否需要皮试Id*</th>   
				            <th data-options="field:'isSkin',width:120">皮试</th>
				            <th data-options="field:'splitattr',width:120,hidden:true">拆分属性</th>
							<th data-options="field:'property',width:120,hidden:true">拆分属性维护</th>
				        </tr>   
				    </thead>   
				</table> 
			</div> 
		</div>
		<script type="text/javascript">

		/*******************************开始读卡***********************************************/
		//定义一个事件（读卡）
		function readOne(){
			var card_value = app.read_ic();
			if(card_value=='0'||card_value==undefined||card_value==''){
				$.messager.alert('提示','此卡号['+card_value+']无效');
				return;
			}
			//填写就诊卡号
			$("#idCardNoHis").textbox("setValue",card_value);
			searchInfoHis();
		};
		
		/*******************************结束读卡***********************************************/

		$('#idCardNoHis').textbox({
			prompt:'就诊卡号查询'
		});
		$('#hisAdviceTree').tree({    
		    animate:true,
		    lines:true,
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if (node.children!=null&&node.children!=''&&node.children.length!=0){
					s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}
				return s;
			},
			onBeforeCollapse:function(node){
				if(node.id=="root"){
					return false;
				}
			},
			onDblClick:function(node){
				if(node.iconCls!='icon-reload'&&node.iconCls!='icon-table'){
					$("#hisAdvice").datagrid("loading");
					$.ajax({
						type:"post",
						url:"<%=basePath%>outpatient/advice/queryMedicalrecordHisList.action",
						data:{clinicNo:node.id,para:node.attributes.para,q:node.attributes.time},
						success: function(dataMap) {
							if(dataMap.resMsg=='success'){
								$("#hisAdvice").datagrid("loaded");
								if(dataMap.resCode.length==0){
									clearDgAdDgList('hisAdvice');
									msgShow('提示','该挂号记录无医嘱信息！',3000);
									return;
								}
								insertRows("hisAdvice",true,dataMap.resCode);
								reloadHis();
							}else{
								$("#hisAdvice").datagrid("loaded");
								$.messager.alert('提示',dataMap.resCode);
							}
						},
						error: function(){
							$("#hisAdvice").datagrid("loaded");
							$.messager.alert('提示','请求失败！');
						}
					});
				}else{//加载下一分区或表的历史数据
					if(node.iconCls!='icon-table'){
						$("#hisAdvice").datagrid("loading");
						$.ajax({
							type:"post",
							url:"<%=basePath%>outpatient/advice/queryHisAdviceNext.action",
							data:{id:node.attributes.isParDb,patientNo:node.id,para:node.attributes.time},
							success: function(dataMap) {
								if(dataMap.resMsg=='success'){
									if(dataMap.resCode.length>0){
										$('#hisAdviceTree').tree('insert', {
											after: node.target,
											data: dataMap.resCode
										});
									}else{
										msgShow('提示','没有更多信息了！',3000);
									}
									$('#hisAdviceTree').tree('remove',node.target);
								}else{
									msgShow('提示',dataMap.resCode,3000);
								}
								$("#hisAdvice").datagrid("loaded");
							},
							error: function(){
								$("#hisAdvice").datagrid("loaded");
								$.messager.alert('提示','请求失败！');
							}
						});
					}else{
						msgShow('提示','请选择具体医嘱信息！');
					}
				}
			}
		});
		
		bindEnterEvent('idCardNoHis',searchInfoHis,'easyui');
		
		</script>
	</body>
</html>