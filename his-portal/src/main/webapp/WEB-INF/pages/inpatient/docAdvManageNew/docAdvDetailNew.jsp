<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>医嘱管理（新）</title>
</head>
<body>
	<div id="cc" class="easyui-layout docAdvDetailNewDateSize" data-options="fit:true">   
	    <div data-options="region:'north',border:false" style="height:15%;width: 100%">
	    	<table>
	    		<tr>
	    			<td width="90px" align="right">姓名：</td><td width="90px" id="adNameId" align="left"></td>
	    			<td width="70px" align="right">性别：</td><td width="70px" id="adSexId" align="left"></td>
	    			<td width="100px" align="right">合同单位：</td><td width="100px" id="adPactId" align="left"></td>
	    			<td width="90px" align="right">费用总额：</td><td width="90px" id="adTotCostId" align="left"></td>
	    			<td width="50px" align="right">余额：</td><td width="50px" id="adFreeCostId" align="left"></td>
	    			<td width="120px" align="right">预交金总额：</td><td width="120px" id="adPrepayCostId" align="left"></td>
	    			<td width="80px" align="right"></td>
	    			<td width="140px">
	    			<input id="userid" type="hidden" value="${user.id }"/>
	    			<input id="username" type="hidden" value="${user.name }"/>
	    			<input id="deptId" type="hidden" value="${departmentSerch.deptCode }"/>
	    			<input id="inpatientId" type="hidden">		    			
					<input type="hidden" id="comboboxId" value="0">	
					<input type="hidden" id="inpatientNo">
					<input type="hidden" id="patientNo">
					<input type="hidden" id="nurseCellCode">
					<input type="hidden" id="babyFlag">	
					<input type="hidden" id="deptCode">	
					<input type="hidden" id="pid">	
					<input type="hidden" id="createOrderFlag">	
					<input id="userName" type="hidden" value="${user.name }"/>
					<input id="deptId" type="hidden" value="${departmentSerch.deptCode}"/>
					<input id="recUserName" type="hidden" value="${user.name  }"/>    					 
					</td>
	    		</tr>
	    		<tr>
	    			<td id="fypCost" width="120px" align="right" style="display: none">非药品:<span id="fypCost1"></span>元</td>
	    			<td id="ssCost" width="120px" align="right" style="display: none">膳食:<span id="ssCost1"></span>元</td>
	    			<td id="hljbCost" width="120px" align="right" style="display: none">护理级别:<span id="hljbCost1"></span>元</td>
	    			<td id="msyzCost" width="120px" align="right" style="display: none">描述医嘱:<span id="msyzCost1"></span>元</td>
	    			<td id="zcyCost" width="120px" align="right" style="display: none">中成药:<span id="zcyCost1"></span>元</td>
	    			<td id="xyCost" width="120px" align="right" style="display: none">西药:<span id="xyCost1"></span>元</td>
	    			<td id="shoushuCost" width="120px" align="right" style="display: none">手术:<span id="shoushuCost1"></span>元</td>
	    			<td id="jcCost" width="120px" align="right" style="display: none">检查:<span id="jcCost1"></span>元</td>
	    			<td id="jyCost" width="120px" align="right" style="display: none">检验:<span id="jyCost1"></span>元</td>
	    			<td id="yycyCost" width="120px" align="right" style="display: none">预约出院:<span id="yycyCost1"></span>元</td>
	    			<td id="zcCost" width="120px" align="right" style="display: none">转床:<span id="zcCost1"></span>元</td>
	    			<td id="zkCost" width="120px" align="right" style="display: none">转科:<span id="zkCost1"></span>元</td>
	    			<td id="zcaoyCost" width="120px" align="right" style="display: none">中草药:<span id="zcaoyCost1"></span>元</td>
	    			<td id="hzCost" width="120px" align="right" style="display: none">会诊:<span id="hzCost1"></span>元</td>
	    			<td id="bqCost" width="120px" align="right" style="display: none">病情:<span id="bqCost1"></span>元</td>
	    			<td id="qtCost" width="120px" align="right" style="display: none">其它:<span id="qtCost1"></span>元</td>
	    			<td id="zlCost" width="120px" align="right" style="display: none">治疗:<span id="zlCost1"></span>元</td>
	    			<td id="jlCost" width="120px" align="right" style="display: none">计量:<span id="jlCost1"></span>元</td>
	    		</tr>
	    	</table>
	    	<div id="adProjectDivId" style="padding:3px;display:none;">	
		    			<table border="0">
		    			<tr>
		    				<td width="80px" align="right" nowrap="nowrap">医嘱类型：</td>
		    				<td width="120px">
			    				<span id ="long"><input id="longDocAdvType" class="easyui-combobox" style="width:120px;"/></span> 
				    			<span id ="tem"><input id="temDocAdvType" class="easyui-combobox" style="width:120px;"/></span>  		       		    					 
							</td>
		    				<td width="80px" align="right" nowrap="nowrap">类别：</td>
		    				<td width="120px"><input id="adProjectTdId" class="easyui-combobox" style="width:120px;"></td>
		    				<td width="80px" align="right" nowrap="nowrap">名称：</td>
		    				<td width="20px"><input id="adProjectNameTdId" class="easyui-combogrid" style="width:120px;"/></td>
		    				<td width="80px" align="right" nowrap="nowrap">数量：</td>
		    				<td width="70px"><input id="adProjectNumTdId" class="easyui-numberbox" data-options="min:1" style="width:70px;"/></td>
		    				<td width="80px" align="right" nowrap="nowrap">单位：</td>
		    				<td align="center"><input id="adProjectUnitTdId" class="easyui-combobox" style="width:80px;"/></td>
		    				<td width="100px" align="right" nowrap="nowrap">开始时间：</td>
		    				<td width="120px">
		    					<input id="startTime" class="Wdate" type="text" onchange="startTime()" name="startTime" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d-%H-%m-%s',maxDate:'%y-%M-{%d+7}-%H-%m-%s'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
		    				</td>
		    				<td width="100px" align="right" id="endTimeTitle" nowrap="nowrap">停止时间：</td>
		    				<td width="120px" id="endTimeText">
		    					<input id="endTime" class="Wdate" type="text" onchange=" endTime()" name="endTime" data-options="showSeconds:true" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}',maxDate:'%y-%M-{%d+9}-%H-%m-%s'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
		    			</tr>
		    		</table>
		    	</div>
		    	<div id="adWestMediDivId" style="padding:3px;display:none;">
		    		<table>
		    			<tr>
		    				<td width="80px" align="right" nowrap="nowrap">频次：</td>
		    				<td width="120px"align="center"><input id="adWestMediFreTdId" class="easyui-combobox" data-options="" style="width:120px;"/></td>		  			
		    				<td width="80px" align="right" nowrap="nowrap">用法：</td>
		    				<td width="120px"align="center"><input id="adWestMediUsaTdId" class="easyui-combobox" data-options="" style="width:120px;"/></td>
		    				<td width="81px" align="right" nowrap="nowrap">每次用量：</td>
		    				<td width="46px" nowrap="nowrap"><input id="adWestMediDosMaxTdId" class="easyui-numberbox" data-options="min:0,max:999,precision:1" style="width:46px;"/></td>
		    				<td width="18px" id="adWestMediMinUnitTdId" nowrap="nowrap"></td>
		    				<td width="8px" nowrap="nowrap">=</td>
		    				<td width="50px"><input id="adWestMediDosMinTdId" class="easyui-numberbox" data-options="precision:2" readonly="readonly" style="width:50px;"/></td>
		    				<td width="30px" id="adWestMediDosDosaTdId" nowrap="nowrap"></td>
		    				<td>
		    					<div style="width:70px;overflow:hidden;" id="shijiandianPc">
		    					</div>
		    				</td>    				
		    				<td width="70px"align="center" nowrap="nowrap"><a id="timePoint" href="javascript:adTimePoints();" style="color:blue;">时间点</a></td>
		    				<td width="110px" align="right" nowrap="nowrap">是否加急：</td>
		    				<td width="20px" nowrap="nowrap"><input id="adWestMediUrgTdId" type="checkbox" onclick="onClickIsUrgent('adWestMediUrgTdId')"/></td>
		    				<td width="131px" align="right" nowrap="nowrap">备注：</td>
		    				<td width="120px"><input id="adWestMediRemTdId" class="easyui-textbox" data-options="editable:true" style="width:120px;"/></td>		    				
		    				<td width="100px" align="right" id="skiTdId" nowrap="nowrap">皮试：</td> 
		    				<td width="120px"align="center" id="skiTdName"><input id="adWestMediSkiTdId" class="easyui-combobox" style="width:120px;" data-options="
		    							   valueField: 'id',    
									       textField: 'text',    									      
									        data: [{
												id: '1',
												text: '不需要皮试'
											},{
												id: '2',
												text: '需要皮试，未做'
											},{
												id: '3',
												text: '皮试阳'
											},{
												id: '4',
												text: '皮试阴'
											}]      
									       " /></td> 											
		    			</tr>
		    		</table>	    			    					    					    		
		    	</div>
		    	<div id="adChinMediDivId" style="padding:3px;display:none;">
		    		<table>
		    			<tr>
		    				<td width="71px" align="right" nowrap="nowrap">频次：</td>
		    				<td width="120px"><input id="adChinMediFreTdId" class="easyui-combobox" data-options="editable:false" style="width:120px;"/></td>
		    				<td width="78px" align="right" nowrap="nowrap">用法：</td>
		    				<td width="120px"><input id="adChinMediUsaTdId" class="easyui-combobox" data-options="" style="width:120px;"/></td>
		    				<td width="80px" align="right" nowrap="nowrap">付数：</td>
		    				<td width="120px"><input id="adChinMediNumTdId" class="easyui-numberbox" data-options="min:1,max:999" style="width:80px;"/></td>
		    				<td width="80px" align="right" nowrap="nowrap">备注：</td>
		    				<td width="120px"><input id="adChinMediRemTdId" class="easyui-combobox" data-options="" style="width:120px;"/></td>
		    			</tr>
		    		</table>
		    	</div>
		    	<div id="adNotDrugDivId" style="padding:3px;display:none;">
		    		<table>
		    			<tr>
		    				<td width="80px" align="right" nowrap="nowrap">执行科室：</td>
		    				<td width="120px"><input id="adExeDeptTd" type="hidden"/><input id="adExeDeptTdId" class="easyui-textbox" data-options="prompt:'回车查询'" readonly="readonly" style="width:120px;"/></td>
		    				<td width="78px" align="right" id="bodyParts" nowrap="nowrap">部位：</td>
		    				<td width="120px" id="bodyPartss"><input id="adNotDrugInsTdId" class="easyui-combobox" data-options="" style="width:120px;"/></td>
		    				<td width="80px" align="right" id="sampleType" nowrap="nowrap">样本类型：</td>
		    				<td width="120px" id="sampleTypes"><input id="adNotDrugSamTdId" class="easyui-combobox" data-options="" style="width:120px;"/></td>
		    				<td width="80px" align="right" nowrap="nowrap">备注：</td>
		    				<td width="120px"><input id="adNotDrugRemTdId" class="easyui-textbox" data-options="editable:true" style="width:120px;"/></td>
		    				<td width="110px" align="right" nowrap="nowrap">是否加急：</td>
		    				<td width="20px"><input id="adNotDrugUrgTdId" type="checkbox" onclick="onClickIsUrgent('adNotDrugUrgTdId')"/></td>
		    			</tr>
		    		</table>
		    	</div>
	    </div>   
	    <div data-options="region:'west',title:'组套信息',collapsed:true,split:true" style="height:85%;width: 15%">
	    	<div style="padding-top:5px;padding-left:5px;padding-bottom:5px;">
				<input class="easyui-textbox" id="adStackTreeSearch" data-options="prompt:'组套查询',buttonText:'查询',buttonIcon:'icon-search'" style="width:180px"/>
			</div>
			<ul id="adStackTree"></ul><input id="selectIndex" type="hidden"/>
	    </div>   
	    <div data-options="region:'center'" style="height:85%;width: 85%">
	    	<div id="ttta" class="easyui-tabs" data-options="fit:true,tabPosition:'bottom',border:false" style="height:100%;">
				<div title="长期医嘱" id="longda" style="padding: 5px;height:100%;"> 
					<div style="border: 1px solid #95b8e7; padding: 5px;min-height:5px;">
	    				&nbsp;<span style="font-size:12px">新开立：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#00FF00">&nbsp;&nbsp;&nbsp;&nbsp;</span>
	    				&nbsp;<span style="font-size:12px">已审核：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#4A4AFF">&nbsp;&nbsp;&nbsp;&nbsp;</span>
	    				&nbsp;<span style="font-size:12px">已执行：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#EEEE00">&nbsp;&nbsp;&nbsp;&nbsp;</span>
	    				&nbsp;<span style="font-size:12px">已作废：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#FF0000">&nbsp;&nbsp;&nbsp;&nbsp;</span>
	    				&nbsp;<span style="font-size:12px">医嘱存在批注：</span><span style="color:red">*</span>
	    				&nbsp;<span style="font-size:12px">知情同意书：</span><span style="font-size:12px">√</span>
	    				&nbsp;<span style="font-size:12px">需审核药品：</span><span style="display:inline-block;width:12px;height:12px;background-image:url(${pageContext.request.contextPath}/themes/system/images/button/shen1.png)"></span>
	    				&nbsp;<span style="font-size:12px">复制/粘贴：CTRL+C/CTRL+V</span>
	    			</div>
	    			<div style="height:1%"></div> 
	    			<div style="width:100%;height:94%;"> 
					 <table id="infolistA" class="easyui-datagrid" style="width:100%;height:auto;" data-options="fit:true">
			       	 		<thead frozen="true" id="adDgList">  
						        <tr>  
						        	<th data-options="field:'colour',width:24,align:'center',styler:functionColour,formatter:functionRowNum" ></th>	
						        	<th data-options="field:'ck',checkbox:true"></th>								           
						        </tr>  
						    </thead>
							<thead>
								<tr><th data-options="field:'changeNo',hidden:true,width:80">改变标志</th>													
									<th data-options="field:'moStat',hidden:true,width:100"></th>												
									<th data-options="field:'id',hidden:true,width:100">医嘱执行单编号(id)</th>
									<th data-options="field:'typeCode',hidden:true,width:100">医嘱类别代码</th>												
									<th data-options="field:'typeName',width:80">医嘱类型</th>
									<th data-options="field:'classCode',hidden:true,width:100">系统类型</th>
									<th data-options="field:'className',hidden:true,width:100">系统名称</th>
									<th data-options="field:'itemCode',hidden:true,width:100">医嘱Id</th>
									<th data-options="field:'itemName',hidden:true,width:230">医嘱名称</th>
									<th data-options="field:'itemNameView',width:230">医嘱名称</th>
									<th data-options="field:'combNoFlag',width:23">组</th>
									<th data-options="field:'combNo',hidden:true,width:100">组</th>
									<th data-options="field:'qtyTot',width:50">总量</th>
									<th data-options="field:'priceUnit',width:120,formatter:colorInfoUnitsMapFamater">单位（总量单位）</th>
									<th data-options="field:'drugpackagingUnit',width:100,formatter:drugpackagingunitFamaters">包装单位</th>
									<th data-options="field:'priceUnitName',width:100,hidden:true">包装单位字段</th>  
									<th data-options="field:'packQty',hidden:true,width:50">包装数量</th>  
		            				<th data-options="field:'minUnit',width:100,hidden:true">最小单位</th>   
									<th data-options="field:'doseOnce',width:50">每次量</th>
									<th data-options="field:'doseUnit',width:120,formatter:drugpdoseunitFamaters">单位（剂量单位）</th>
									<th data-options="field:'doseOnces',width:150,formatter:doseOnceFamater">每次剂量</th>												
									<th data-options="field:'specs',hidden:true,width:100">规格</th>
									<th data-options="field:'doseModelCode',hidden:true,width:100">剂型代码</th>
									<th data-options="field:'drugType',hidden:true,width:100">药品类型</th>
									<th data-options="field:'drugQuality',hidden:true,width:100">药品性质</th>
									<th data-options="field:'itemPrice',hidden:true,width:100">价格</th>														
									<th data-options="field:'useDays',width:50">付数</th>
									<th data-options="field:'frequencyCode',hidden:true,width:100">频次id</th>					
									<th data-options="field:'frequencyName',width:160">频次</th>
									<th data-options="field:'usageCode',hidden:true,width:100">用法Id</th>
									<th data-options="field:'useName',width:160">用法名称</th>																								
									<th data-options="field:'dateBgn',width:150">开始时间</th>
									<th data-options="field:'dateEnd',width:150">停止时间</th>
									<th data-options="field:'moDate',width:150">开立时间</th>
									<th data-options="field:'docCode',hidden:true,width:100">开立医生Id</th>
									<th data-options="field:'docName',width:100">开立医生</th>
									<th data-options="field:'execDpcd',hidden:true,width:100">执行科室Id</th>												
									<th data-options="field:'execDpnm',width:100">执行科室</th>
									<th data-options="field:'emcFlag',hidden:true,width:100">加急标记</th>
									<th data-options="field:'isUrgent',width:40,align:'center',formatter:emcFamater">急</th>																	           					  
									<th data-options="field:'labCode',width:100,formatter:colorInfoSampleTeptMapFamater">样本类型</th>  												
									<th data-options="field:'itemNote',width:100,formatter:colorInfoCheckpointMapFamater">检查部位</th>
									<th data-options="field:'pharmacyCode',width:100,formatter:implDepartmentFamater">扣库科室</th>
									<th data-options="field:'moNote2',width:150">备注</th>
									<th data-options="field:'recUsernm',width:100">录入人</th>
									<th data-options="field:'listDpcd',width:100,formatter:implDepartmentFamater">开立科室</th>
									<th data-options="field:'dcUsernm',width:100">停止人</th>
									<th data-options="field:'baseDose',hidden:true,width:100">基本剂量</th>								
									<th data-options="field:'permission',hidden:true,width:100">患者是否同意</th>	
		            				<th data-options="field:'hypotest',hidden:true,width:100">皮试Id</th> 
		            				<th data-options="field:'hypotestName',width:120,formatter:skinFamater">皮试</th>  	
		            				<th data-options="field:'itemType',hidden:true,width:100">药品非药品标志</th> 		            				 
									<th data-options="field:'sortId',width:80">顺序号</th>
									<th data-options="field:'execTimes',hidden:true,width:80">执行时间点[特殊频次]</th>	
									<th data-options="field:'execDose',hidden:true,width:80">执行剂量[特殊频次]</th>
									<th data-options="field:'combFlag',hidden:true,width:80">该组合中的医嘱是否都未保存标志</th>
									<th data-options="field:'splitattr',hidden:true">拆分属性</th>
									<th data-options="field:'property',hidden:true">药品拆分维护表属性</th>										
								</tr>
							</thead>
						</table>
					</div>
			    </div>   
			     <div title="临时医嘱" id="temda" style="padding: 5px;height:100%;">  
				     <div style="border: 1px solid #95b8e7; padding: 5px;min-height:5px;">
	    				&nbsp;<span style="font-size:12px">新开立：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#00FF00">&nbsp;&nbsp;&nbsp;&nbsp;</span>
	    				&nbsp;<span style="font-size:12px">已审核：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#4A4AFF">&nbsp;&nbsp;&nbsp;&nbsp;</span>
	    				&nbsp;<span style="font-size:12px">已执行：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#EEEE00">&nbsp;&nbsp;&nbsp;&nbsp;</span>
	    				&nbsp;<span style="font-size:12px">已作废：</span><span style="height:8px;line-height:5px;display:inline-block;background-color:#FF0000">&nbsp;&nbsp;&nbsp;&nbsp;</span>
	    				&nbsp;<span style="font-size:12px">省限制级药品：</span><span style="font-size:12px">X</span>
	    				&nbsp;<span style="font-size:12px">市限制级药品：</span><span style="font-size:12px">S</span>
	    				&nbsp;<span style="font-size:12px">知情同意书：</span><span style="font-size:12px">√</span>
	    				&nbsp;<span style="font-size:12px">需审核药品：</span><span style="display:inline-block;width:12px;height:12px;background-image:url(${pageContext.request.contextPath}/themes/system/images/button/shen1.png)"></span>
	    				&nbsp;<span style="font-size:12px">复制/粘贴：CTRL+C/CTRL+V</span>
	    			</div>
	    			<div style="height:1%"></div> 
	    			<div style="width:100%;height:94%;">  
			        <table id="infolistB" class="easyui-datagrid" style="width:100%;height:auto;" data-options="fit:true">
			        		<thead frozen="true">  
						        <tr>  
						        	<th data-options="field:'colour',width:24,align:'center',styler:functionColour,formatter:functionRowNum" ></th>	
						        	<th data-options="field:'ck',checkbox:true"></th>								           
						        </tr>  
						    </thead>
							<thead>
								<tr><th data-options="field:'changeNo',hidden:true,width:80">改变标志</th>																									
									<th data-options="field:'moStat',hidden:true,width:100"></th>												
									<th data-options="field:'id',hidden:true,width:100">医嘱执行单编号(id)</th>
									<th data-options="field:'typeCode',hidden:true,width:100">医嘱类别代码</th>				
									<th data-options="field:'typeName',width:80">医嘱类型</th>
									<th data-options="field:'classCode',hidden:true,width:100">系统类型</th>
									<th data-options="field:'className',hidden:true,width:100">系统名称</th>
									<th data-options="field:'itemCode',hidden:true,width:100">医嘱Id</th>
									<th data-options="field:'itemName',hidden:true,width:230">医嘱名称</th>
									<th data-options="field:'itemNameView',width:230">医嘱名称</th>
									<th data-options="field:'combNoFlag',width:23">组</th>
									<th data-options="field:'combNo',hidden:true,width:100">组</th>
									<th data-options="field:'qtyTot',width:50">总量</th>
									<th data-options="field:'priceUnit',width:120,formatter:colorInfoUnitsMapFamater">单位（总量单位）</th>
									<th data-options="field:'drugpackagingUnit',width:100,formatter:drugpackagingunitFamaters">包装单位</th> 
									<th data-options="field:'packQty',hidden:true,width:50">包装数量</th>   
		            				<th data-options="field:'minUnit',width:100,hidden:true">最小单位</th>   
									<th data-options="field:'doseOnce',width:50">每次量</th>
									<th data-options="field:'doseUnit',width:120,formatter:drugpdoseunitFamaters">单位（剂量单位）</th>
									<th data-options="field:'doseOnces',width:150,formatter:doseOnceFamater">每次剂量</th>												
									<th data-options="field:'specs',hidden:true,width:100">规格</th>
									<th data-options="field:'doseModelCode',hidden:true,width:100">剂型代码</th>
									<th data-options="field:'drugType',hidden:true,width:100">药品类型</th>
									<th data-options="field:'drugQuality',hidden:true,width:100">药品性质</th>
									<th data-options="field:'itemPrice',hidden:true,width:100">价格</th>								
									<th data-options="field:'useDays',width:50">付数</th>
									<th data-options="field:'frequencyCode',hidden:true,width:100">频次id</th>												
									<th data-options="field:'frequencyName',width:160">频次</th>
									<th data-options="field:'usageCode',hidden:true,width:100">用法Id</th>
									<th data-options="field:'useName',width:160">用法名称</th>																								
									<th data-options="field:'dateBgn',width:150">开始时间</th>
									<th data-options="field:'dateEnd',width:150">停止时间</th>
									<th data-options="field:'moDate',width:150">开立时间</th>
									<th data-options="field:'docCode',hidden:true,width:100">开立医生Id</th>
									<th data-options="field:'docName',width:100">开立医生</th>
									<th data-options="field:'execDpcd',hidden:true,width:100">执行科室Id</th>					
									<th data-options="field:'execDpnm',width:100">执行科室</th>
									<th data-options="field:'emcFlag',hidden:true,width:100">加急标记</th>
									<th data-options="field:'isUrgent',width:40,align:'center',formatter:emcFamater">急</th>																	           					  					            				
									<th data-options="field:'labCode',width:100,formatter:colorInfoSampleTeptMapFamater">样本类型</th>  												
									<th data-options="field:'itemNote',width:100,formatter:colorInfoCheckpointMapFamater">检查部位</th>
									<th data-options="field:'pharmacyCode',width:100,formatter:implDepartmentFamater">扣库科室</th>
									<th data-options="field:'moNote2',width:150">备注</th>
									<th data-options="field:'recUsernm',width:100">录入人</th>
									<th data-options="field:'listDpcd',width:100,formatter:implDepartmentFamater">开立科室</th>
									<th data-options="field:'dcUsernm',width:100">停止人</th>
									<th data-options="field:'baseDose',hidden:true,width:100">基本剂量</th>
									<th data-options="field:'permission',hidden:true,width:100">患者是否同意</th>	
		            				<th data-options="field:'hypotest',hidden:true,width:100">皮试Id</th> 
		            				<th data-options="field:'hypotestName',width:120,formatter:skinFamater">皮试</th>
		            				<th data-options="field:'itemType',hidden:true,width:100">药品非药品标志</th>
									<th data-options="field:'sortId',width:80">顺序号</th>
									<th data-options="field:'execTimes',hidden:true,width:80">执行时间点[特殊频次]</th>	
									<th data-options="field:'execDose',hidden:true,width:80">执行剂量[特殊频次]</th>	
									<th data-options="field:'combFlag',hidden:true,width:80">该组合中的医嘱是否都未保存标志</th>
									<th data-options="field:'splitattr',hidden:true">拆分属性</th>
									<th data-options="field:'property',hidden:true">药品拆分维护表属性</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		</div> 
	</div>
	<script type="text/javascript">
	function endTime(){
		//结束时间
		var beginTime=$('#startTime').val();
		var newdate=$('#endTime').val();
		if(beginTime!='' && newdate!=''){
			var sDate = new Date(beginTime.replace(/\-/g, "\/"));  
			var eDate = new Date(newdate.replace(/\-/g, "\/"));  
			if(sDate>eDate){
				$.messager.alert('操作提示', '开始时间不能大于结束时间！');	
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return;
			}
		}
		if(newdate==null ||newdate ==''){
			var qq = $('#ttta').tabs('getSelected');
			var tab = qq.panel('options');
			if(tab.title=='长期医嘱'){
				var row = $('#infolistA').datagrid('getSelected')
				newdate=row.dateEnd;
			}else if(tab.title=='临时医嘱'){
				var row = $('#infolistB').datagrid('getSelected')
				newdate=row.dateEnd;
			}						
		}
		var index = getIndexForAdDgList();
		var indexA=getIndexForAdDgListA();
		if(indexA>=0){							
			$('#infolistA').datagrid('updateRow',{
				index: indexA,
				row: {									
					dateEnd:newdate,
					changeNo:1
				}
			});
		}
		if(index>=0){
			$('#infolistB').datagrid('updateRow',{
				index: index,
				row: {									
					dateEnd:newdate,
					changeNo:1
				}
			});
		}
	}
	function startTime(){
		//开始时间
		var endTime=$('#endTime').val();
		var newdate=$('#startTime').val();
		if(newdate!='' && endTime!=''){
			var sDate = new Date(newdate.replace(/\-/g, "\/"));  
			var eDate = new Date(endTime.replace(/\-/g, "\/"));  
			if(sDate>eDate){
				$.messager.alert('操作提示', '开始时间不能大于结束时间！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return;
			}
		}
		if(newdate==null ||newdate ==''){
			var qq = $('#ttta').tabs('getSelected');				
			var tab = qq.panel('options');
			if(tab.title=='长期医嘱'){
				var row = $('#infolistA').datagrid('getSelected')
				newdate=row.dateBgn;
			}else if(tab.title=='临时医嘱'){
				var row = $('#infolistB').datagrid('getSelected')
				newdate=row.dateBgn;
			}						
		}					
 		var index = getIndexForAdDgList();
 		var indexA=getIndexForAdDgListA();
 		if(indexA>=0){							
			$('#infolistA').datagrid('updateRow',{
				index: indexA,
				row: {									
					dateBgn:newdate,
					changeNo:1
				}
			});
		}
		if(index>=0){
			$('#infolistB').datagrid('updateRow',{
				index: index,
				row: {									
					dateBgn:newdate,
					changeNo:1
				}
			});
		}
	}
	$(function(){
		$("#adStackTreeSearch").textbox({onClickButton:function(){
			searchStackTreeNodes();
		    }});
		
		//长期医嘱
		$('#infolistA').datagrid({//点击选择行，相应数据反显到列表上方编辑区域
			onRowContextMenu: function(e, rowIndex, rowData) { //右键时触发事件
	            e.preventDefault(); //阻止浏览器捕获右键事件
	            $(this).datagrid("selectRow", rowIndex); //根据索引选中该行	 
	            $('#adviceListMenu').menu('show', {//显示右键菜单
	                left: e.pageX,//在鼠标点击处显示菜单
	                top: e.pageY
	            });
	        },
			onSelect:function(rowIndex,rowData){
				var ycf = document.getElementById("adProjectDivId").style.display;
				if(ycf=="none"){
					return;
				}
				if(rowData.moStat==1){
					$('#adDelAdviceBtn').hide();
					$('#adStopAdviceBtn').show();
					return;
				}else if(rowData.moStat==2){
					$('#adDelAdviceBtn').hide();
					$('#adStopAdviceBtn').show();
					return;
				}else if(rowData.moStat==3){
					$('#adDelAdviceBtn').show();
					$('#adStopAdviceBtn').hide();
					return;
				}else{
					$('#adDelAdviceBtn').show();
					$('#adStopAdviceBtn').hide();
					if(rowData.itemType=='1'){
						initCombobox(false,colorInfoUnitsMap,rowData.drugpackagingUnit,rowData.minUnit);
						$('#adProjectUnitTdId').combobox('select',rowData.priceUnit);
					}else{
						$('#adProjectUnitTdId').combobox('select',rowData.drugpackagingUnit);
					}
					if(rowData.typeCode!=null&&rowData.typeCode!=''){
						$('#longDocAdvType').combobox('select',rowData.typeCode);
					}else{
						$('#longDocAdvType').combobox('clear');
					}
					if(rowData.classCode!=null&&rowData.classCode!=''){
						$('#adProjectTdId').combobox('select',rowData.classCode);
					}else{
						$('#adProjectTdId').combobox('clear');
					}
					if(rowData.frequencyCode!=null&&rowData.frequencyCode!=''){//中、西药品频次
						$('#adWestMediFreTdId').combobox('select',rowData.frequencyCode);	
					}else{
						$('#adWestMediFreTdId').combobox('clear');
					}
					if(rowData.usageCode!=null&&rowData.usageCode!=''){//中、西药品用法
						$('#adWestMediUsaTdId').combobox('select',rowData.usageCode);	
					}else{
						$('#adWestMediUsaTdId').combobox('clear');
					}
					
					$('#adProjectNumTdId').numberspinner('setValue',rowData.qtyTot);
					$('#adProjectNameTdId').textbox('setValue',rowData.itemName);
					var temp;//剂量
					if(rowData.baseDose=="0"){
						temp=1;
						$('#adWestMediDosMaxTdId').numberbox('setValue',(rowData.doseOnce/1).toFixed(2));
					}else{
						temp=rowData.baseDose*rowData.doseOnce;
						$('#adWestMediDosMaxTdId').numberbox('setValue',rowData.doseOnce);
					}
					$('#adWestMediMinUnitTdId').text(mindataMaps[rowData.minUnit]);
					$('#adWestMediDosMinTdId').numberbox('setValue',temp);
					$('#adWestMediDosDosaTdId').text(drugdoseunitMaps[rowData.doseUnit]);
					$('#adExeDeptTdId').textbox('setValue',rowData.execDpnm);//执行科室
					if(rowData.moNote2!=null&&rowData.moNote2!=''){
						$('#adWestMediRemTdId').combobox('setText',rowData.moNote2);//西药中成药备注
					}else{
						$('#adWestMediRemTdId').combobox('clear');
					}
					$('#adChinMediNumTdId').numberbox('setValue',rowData.useDays);//草药付数					
					if(rowData.frequencyCode!=null&&rowData.frequencyCode!=''){//中、西药品频次
						$('#adChinMediFreTdId').combobox('select',rowData.frequencyCode);	
					}else{
						$('#adChinMediFreTdId').combobox('clear');
					}
					if(rowData.usageCode!=null&&rowData.usageCode!=''){
						$('#adChinMediUsaTdId').combobox('select',rowData.usageCode);//中草药用法	
					}else{
						$('#adChinMediUsaTdId').combobox('clear');
					}
					if(rowData.moNote2!=null&&rowData.moNote2!=''){
						$('#adChinMediRemTdId').combobox('setText',rowData.moNote2);//中草药备注 
					}else{
						$('#adChinMediRemTdId').combobox('clear');
					}	
					if(rowData.itemNote!=null&&rowData.itemNote!=''){
						$('#adNotDrugInsTdId').combobox('select',rowData.itemNote);//部位
					}else{
						$('#adNotDrugInsTdId').combobox('clear');
					}
					if(rowData.labCode!=null&&rowData.labCode!=''){
						$('#adNotDrugSamTdId').combobox('select',rowData.labCode);//样本类型
					}else{
						$('#adNotDrugSamTdId').combobox('clear');
					}	
					if(rowData.moNote2!=null&&rowData.moNote2!=''){
						$('#adNotDrugRemTdId').combobox('setText',rowData.moNote2);//非药品备注
					}else{
						$('#adNotDrugRemTdId').combobox('clear');
					}
					if(rowData.emcFlag==1){
						$('#adWestMediUrgTdId').prop("checked", true);
						$('#adNotDrugUrgTdId').prop("checked", true); 
					}else{
						$('#adWestMediUrgTdId').prop("checked", false);
						$('#adNotDrugUrgTdId').prop("checked", false); 
					}
					if(rowData.dateBgn!=null&&rowData.dateBgn!=''){
						$('#startTime').val(rowData.dateBgn);//医嘱开始时间
					}else{
						$('#startTime').val('');
					}
					if(rowData.dateEnd!=null&&rowData.dateEnd!=''){
						$('#endTime').val(rowData.dateEnd);//医嘱结束时间
					}else{
						$('#endTime').val('');
					}
					if(rowData.classCode=='07'){//类型为检查	
						$('#bodyParts').show();	
				    	$('#bodyPartss').show();
				    	$('#sampleType').hide();	
				    	$('#sampleTypes').hide();					    	
				    }else{
				    	$('#bodyParts').hide();	
				    	$('#bodyPartss').hide();	
				    	$('#sampleType').show();	
				    	$('#sampleTypes').show();
				    }			    	
			    	if(rowData.classCode=='12'){//类型为转科				    		
			    		$('#adNotDrugInsTdId').combobox('disable');
			    		$('#adNotDrugSamTdId').combobox('disable');
				    }		
			    	if(rowData.classCode!='12'){		    		
			    		$('#adNotDrugInsTdId').combobox('enable');
			    		$('#adNotDrugSamTdId').combobox('enable');
				    }
			    	if(rowData.classCode=='17'||rowData.classCode=='18'){//中药/西药
			    		$('#adNotDrugDivId').hide();
			    		$('#adChinMediDivId').hide();
			    		$('#adWestMediDivId').show();
			    	}else if(rowData.classCode=='16'){
			    		$('#adNotDrugDivId').hide();
			    		$('#adWestMediDivId').hide();
			    		$('#adChinMediDivId').show();
			    	}else{
			    		$('#adChinMediDivId').hide();
			    		$('#adWestMediDivId').hide();
			    		$('#adNotDrugDivId').show();
			    	}
					if(rowData.hypotest==2 ||rowData.hypotest==3||rowData.hypotest==4){
						$('#skiTdId').show();
						$('#skiTdName').show();
						$('#adWestMediSkiTdId').combobox('setValue',rowData.hypotest);	
					}else{
						$('#skiTdId').hide();
						$('#skiTdName').hide();
					}
				}
			}	
		});
		//临时医嘱
		$('#infolistB').datagrid({
			onRowContextMenu: function(e, rowIndex, rowData) { //右键时触发事件
	            e.preventDefault(); //阻止浏览器捕获右键事件
	            $(this).datagrid("selectRow", rowIndex); //根据索引选中该行	                
	            $('#adviceListMenu').menu('show', {//显示右键菜单
	                left: e.pageX,//在鼠标点击处显示菜单
	                top: e.pageY
	            });
	        },
			onSelect:function(rowIndex,rowData){
				var ycf = document.getElementById("adProjectDivId").style.display;
				if(ycf=="none"){
					return;
				}
				if(rowData.moStat==1){
					$('#adDelAdviceBtn').hide();
					$('#adStopAdviceBtn').show();
					return;
				}else if(rowData.moStat==2){
					$('#adDelAdviceBtn').hide();
					$('#adStopAdviceBtn').show();
					return;
				}else if(rowData.moStat==3){
					$('#adDelAdviceBtn').show();
					$('#adStopAdviceBtn').hide();
					return;
				}else{ 
					$('#adDelAdviceBtn').show();
					$('#adStopAdviceBtn').hide();
					if(rowData.itemType=='1'){
						initCombobox(false,colorInfoUnitsMap,rowData.drugpackagingUnit,rowData.minUnit);
						$('#adProjectUnitTdId').combobox('select',rowData.priceUnit)
					}else{
						$('#adProjectUnitTdId').combobox('select',rowData.drugpackagingUnit);
					}
					if(rowData.typeCode!=null&&rowData.typeCode!=''){
						$('#temDocAdvType').combobox('select',rowData.typeCode);	
					}else{
						$('#temDocAdvType').combobox('clear');
					}
					if(rowData.classCode!=null&&rowData.classCode!=''){
						$('#adProjectTdId').combobox('select',rowData.classCode);	
					}else{
						$('#adProjectTdId').combobox('clear');
					}
					if(rowData.frequencyCode!=null&&rowData.frequencyCode!=''){//中、西药品频次
						$('#adWestMediFreTdId').combobox('select',rowData.frequencyCode);	
					}else{
						$('#adWestMediFreTdId').combobox('clear');
					}
					if(rowData.usageCode!=null&&rowData.usageCode!=''){//中、西药品用法
						$('#adWestMediUsaTdId').combobox('select',rowData.usageCode);	
					}else{
						$('#adWestMediUsaTdId').combobox('clear');
					}
					var seValue = $('#selectIndex').val();
					$('#selectIndex').val(rowIndex+$('#selectIndex').val());
					$('#adProjectNumTdId').numberspinner('setValue',rowData.qtyTot);
					$('#adProjectNameTdId').textbox('setValue',rowData.itemName);
					$('#adWestMediDosMaxTdId').numberbox('setValue',(rowData.doseOnce));
					$('#adWestMediMinUnitTdId').text(mindataMaps[rowData.minUnit]);
					
					$('#adWestMediDosMinTdId').numberbox('setValue',rowData.doseOnce);
					
					$('#adWestMediDosDosaTdId').text(drugdoseunitMaps[rowData.doseUnit]);
					$('#adExeDeptTdId').textbox('setValue',rowData.execDpnm);//执行科室
					if(rowData.moNote2!=null&&rowData.moNote2!=''){
						$('#adWestMediRemTdId').combobox('setText',rowData.moNote2);//西药中成药备注
					}else{
						$('#adWestMediRemTdId').combobox('clear');
					}
					$('#adChinMediNumTdId').numberbox('setValue',rowData.useDays);//草药付数					
					if(rowData.frequencyCode!=null&&rowData.frequencyCode!=''){//中、西药品频次
						$('#adChinMediFreTdId').combobox('select',rowData.frequencyCode);	
					}else{
						$('#adChinMediFreTdId').combobox('clear');
					}
					if(rowData.usageCode!=null&&rowData.usageCode!=''){
						$('#adChinMediUsaTdId').combobox('select',rowData.usageCode);//中草药用法	
					}else{
						$('#adChinMediUsaTdId').combobox('clear');
					}
					if(rowData.moNote2!=null&&rowData.moNote2!=''){
						$('#adChinMediRemTdId').combobox('setText',rowData.moNote2);//中草药备注 
					}else{
						$('#adChinMediRemTdId').combobox('clear');
					}	
					if(rowData.itemNote!=null&&rowData.itemNote!=''){
						$('#adNotDrugInsTdId').combobox('select',rowData.itemNote);//部位
					}else{
						$('#adNotDrugInsTdId').combobox('clear');
					}
					if(rowData.labCode!=null&&rowData.labCode!=''){
						$('#adNotDrugSamTdId').combobox('select',rowData.labCode);//样本类型
					}else{
						$('#adNotDrugSamTdId').combobox('clear');
					}	
					if(rowData.moNote2!=null&&rowData.moNote2!=''){
						$('#adNotDrugRemTdId').combobox('setText',rowData.moNote2);//非药品备注
					}else{
						$('#adNotDrugRemTdId').combobox('clear');
					}
					if(rowData.emcFlag==1){
						$('#adWestMediUrgTdId').prop("checked", true);
						$('#adNotDrugUrgTdId').prop("checked", true); 
					}else{
						$('#adWestMediUrgTdId').prop("checked", false);
						$('#adNotDrugUrgTdId').prop("checked", false); 
					}
					if(rowData.dateBgn!=null&&rowData.dateBgn!=''){
						$('#startTime').val(rowData.dateBgn);//医嘱开始时间
					}else{
						$('#startTime').val('');
					}
					if(rowData.dateEnd!=null&&rowData.dateEnd!=''){
						$('#endTime').val(rowData.dateEnd);//医嘱结束时间
					}else{
						$('#endTime').val('');
					}
					changeCostunitB();
					if(rowData.classCode=='07'){//类型为检查	
						$('#bodyParts').show();	
				    	$('#bodyPartss').show();
				    	$('#sampleType').hide();	
				    	$('#sampleTypes').hide();					    	
				    }else{
				    	$('#bodyParts').hide();	
				    	$('#bodyPartss').hide();	
				    	$('#sampleType').show();	
				    	$('#sampleTypes').show();
				    }			    	
			    	if(rowData.classCode=='12'){//类型为转科				    		
			    		$('#adNotDrugInsTdId').combobox('disable');
			    		$('#adNotDrugSamTdId').combobox('disable');
				    }		
			    	if(rowData.classCode!='12'){		    		
			    		$('#adNotDrugInsTdId').combobox('enable');
			    		$('#adNotDrugSamTdId').combobox('enable');
				    }
			    	if(rowData.classCode=='17'||rowData.classCode=='18'){//中药/西药
			    		$('#adNotDrugDivId').hide();
			    		$('#adChinMediDivId').hide();
			    		$('#adWestMediDivId').show();
			    	}else if(rowData.classCode=='16'){
			    		$('#adNotDrugDivId').hide();
			    		$('#adWestMediDivId').hide();
			    		$('#adChinMediDivId').show();
			    		$('#adProjectNumTdId').numberbox('readonly',true);
			    	}else{
			    		$('#adChinMediDivId').hide();
			    		$('#adWestMediDivId').hide();
			    		$('#adNotDrugDivId').show();
			    	}
					if(rowData.hypotest==2 ||rowData.hypotest==3||rowData.hypotest==4){
						$('#skiTdId').show();
						$('#skiTdName').show();
						$('#adWestMediSkiTdId').combobox('setValue',rowData.hypotest);	
					}else{
						$('#skiTdId').hide();
						$('#skiTdName').hide();
					}
				}
			}
	        
		});	
	})
	//日期格式
	Date.prototype.format = function(format){ 
    	var o = { 
	    	"M+" : this.getMonth()+1, //month 
	    	"d+" : this.getDate(), //day 
	    	"h+" : this.getHours(), //hour 
	    	"m+" : this.getMinutes(), //minute 
	    	"s+" : this.getSeconds(), //second 
	    	"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
	    	"S" : this.getMilliseconds() //millisecond 
    	} 
    	if(/(y+)/.test(format)) { 
    		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
    	} 
    	for(var k in o) { 
    		if(new RegExp("("+ k +")").test(format)) { 
    			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
    		} 
    	} 
    	return format; 
    } 
	//页面序列号生成
	function sortIdCreate(){
		var qq = $('#ttta').tabs('getSelected');				
		var tab = qq.panel('options');
		var sortId ='';
		if(tab.title=='长期医嘱'){
			var rows = $('#infolistA').datagrid('getRows');
			if(rows.length>0){
				sortId = rows[0].sortId;
				for(var i=0;i<rows.length;i++){							
					for(var j=i+1;j<rows.length;j++){							
						if(sortId<rows[j].sortId){
							sortId = rows[j].sortId;
						}
					}
				}
				return sortId +1;
			}else{
				return 1;
			}					
		}else if(tab.title=='临时医嘱'){
			var rows = $('#infolistB').datagrid('getRows');
			if(rows.length>0){
				sortId = rows[0].sortId;
				for(var i=0;i<rows.length;i++){							
					for(var j=i+1;j<rows.length;j++){							
						if(sortId<rows[j].sortId){
							sortId = rows[j].sortId;
						}
					}
				}
				return sortId +1;
			}else{
				return 1;
			}
		}
	}
	</script> 
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>     
</body>
</html>