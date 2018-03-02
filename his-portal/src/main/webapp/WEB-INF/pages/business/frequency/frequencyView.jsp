<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>频次查看</title>
<%@ include file="/common/metas.jsp"%>
</head>
	<body style="margin: 0px;padding: 0px">
		<div class="easyui-panel"  id="panelEast" data-options="title:'查看',iconCls:'icon-form',fit:true,border:false">
			<div style="padding: 5px">
				<input type="hidden" id="id" name="id" value="${frequency.id }">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
					<tr>
						<td class="honry-lable">代码：</td>
						<td class="honry-view">${frequency.encode }&nbsp;</td>
					</tr>
					<tr>
						<td class="honry-lable">名称：</td>
						<td class="honry-view">${frequency.name}&nbsp;</td>
					</tr>
					<tr>
						<td class="honry-lable">拼音码：</td>
						<td class="honry-view">${frequency.pinyin }&nbsp;</td>
					</tr>
					<tr>
						<td class="honry-lable">五笔码：</td>
						<td class="honry-view">${frequency.wb }&nbsp;</td>
					</tr>
					<tr>
						<td class="honry-lable">自定义码：</td>
						<td class="honry-view">${frequency.inputCode }&nbsp;</td>
					</tr>
					<tr>
						<td class="honry-lable">用法：</td>
						<td class="honry-view">${codeUseageMap[frequency.useMode] }&nbsp;</td>
					</tr>
						<tr>
							<td class="honry-lable">单位：</td>
							<td class="honry-view" id="unit">
								<input type="hidden" id="frequencyUnit" name="frequency.frequencyUnit" value="${frequency.frequencyUnit }"/>
							</td>
						</tr>
						<c:choose>
								<c:when test="${frequency.frequencyUnit eq 'H' or frequency.frequencyUnit eq 'M' }" >
									<tr >
										<td class="honry-lable">是否持续：</td>
										<td class="honry-view">
											<c:if test="${frequency.alwaysFlag=='1'}">是</c:if>
											<c:if test="${frequency.alwaysFlag=='0'}">否</c:if>
					    				</td>
					    			</tr>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
				    	<tr>
							<td class="honry-lable">频次数目：</td>
			    			<td class="honry-view">${frequency.frequencyNum }&nbsp;</td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">频次次数：</td>
			    			<td class="honry-view">${frequency.frequencyTime }&nbsp;</td>
		    			</tr>
						<tr>				
					<tr>
						<td class="honry-lable">时间点：</td>
						<td class="honry-view">${frequency.period }&nbsp;</td>
					</tr>
					<tr>
						<td class="honry-lable">排序：</td>
						<td class="honry-view">${frequency.order }&nbsp;</td>
					</tr>
					<tr>
						<td class="honry-lable">适用医院：</td>
						<input type="hidden" id="hospitalCode" name="hospitalCode" value="${frequency.hospital }">&nbsp;
						<td class="honry-view" id="hospital">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">不适用医院：</td>
						<input type="hidden" id="nohospitalCode" name="nohospitalCode" value="${frequency.nonHospital }">
						<td class="honry-view" id="nonHospital">
						</td>
					</tr>
					<tr>
						<td class="honry-lable">说明：</td>
						<td class="honry-view">${frequency.description }&nbsp;</td>
					</tr>
					<tr>
						<td class="honry-lable">创建时间：</td>
						<td class="honry-view"><fmt:formatDate value="${frequency.createTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
					</tr>
					<tr>
						<td colspan="2">可选标志：
							<c:choose>
								<c:when test="${frequency.canSelect eq '1'}">
									是
								</c:when>
								<c:otherwise>
									否
								</c:otherwise>
							</c:choose>
							&nbsp;&nbsp;默认标志：
							<c:choose>
								<c:when test="${frequency.isDefault eq '1'}">
									是
								</c:when>
								<c:otherwise>
									否
								</c:otherwise>
							</c:choose>
							&nbsp;&nbsp;停用标志：
							<c:choose>
								<c:when test="${frequency.stop_flg eq '1'}">
									是
								</c:when>
								<c:otherwise>
									否
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</table>
			</div>
			<div style="text-align:center;padding:5px">
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
				</div>
		</div>
			<script>
			 var hospitalMap=new Map();
			$(function(){
				var unit = $('#frequencyUnit').val();
				if(unit != null && unit != ''){
					if(unit == 'M'){
						unit = '分钟';
					}
					if(unit == 'H'){
						unit = '小时';
					}
					if(unit == 'D'){
						unit = '天';
					}
					if(unit == 'W'){
						unit = '周';
					}
					if(unit == 'T'){
						unit = '必须时';
					}
					if(unit == 'ONCE'){
						unit = '仅一次';
					}
					$('#unit').html(unit);
				}
				$('#useMode').combobox({
				    url:"<c:url value='/comboBox.action'/>?str="+"CodeUseage",    
				    valueField:'id',    
				    textField:'name',
				    multiple:false
				});
				
				$.ajax({
				    url:  basePath+"/baseinfo/hospital/queryHospitalName.action",
					type:'post',
					asyns:false,
					success: function(data) {
						var type = data;
						for(var i=0;i<type.length;i++){
							hospitalMap.put(type[i].code,type[i].name);
						}
							hospitalName(hospitalMap);
					}
				});
				
			});
			function hospitalName(map){
				
				var code=$('#hospitalCode').val();
				var noCode=$('#nohospitalCode').val();
				if(code){
					var codes=code.split(',');
					var len=codes.length;
					var name='';
					for(var a=0;a<len;a++){
						if(map.get(codes[a])){
							name=name+map.get(codes[a])+',';
						}
					}
					$('#hospital').html(name.substring(0, name.length-1));
				}
				if(noCode){
					var codes1=noCode.split(',');
					var len=codes1.length;
					var name1='';
					for(var i=0;i<len;i++){
						if(map.get(codes1[i])){
							name1=name1+map.get(codes1[i])+',';
						}
						
					}
					$('#nonHospital').html(name1.substring(0, name1.length-1));
				}
			}
			/**
			 * 关闭查看窗口
			 * @author  liujinliang
			 * @date 2015-5-22 10:53
			 * @version 1.0
			 */
			function closeLayout(){
				$('#divLayout').layout('remove','east');
			}
			
		</script>
	</body>
</html>