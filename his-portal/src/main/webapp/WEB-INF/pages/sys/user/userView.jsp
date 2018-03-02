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
<script>
	var sexMap=new Map();
	//性别渲染
	$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});
</script>
</head>
	<body>
		<div class="easyui-panel" id="panelEast" data-options="title:'用户查看',iconCls:'icon-form',fit:true" style="width:580px;border:0">
			<div style="padding:10px">
			<input type="hidden" id="id" name="id" value="${user.id }">
			<table class="honry-table removeBorders" cellpadding="1" cellspacing="1">
				<tr>
					<td class="honry-lable">
						账号:
					</td>
					<td class="honry-view">
						${user.account}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						姓名:
					</td>
					<td class="honry-view">
						${user.name}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						昵称:
					</td>
					<td class="honry-view">
						${user.nickName}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						系统编码:
					</td>
					<td class="honry-view">
						${user.code }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						性别:
					</td>
					<td id="sexTd"   class="honry-view">
				<input type="hidden" id="sex" value="${user.sex }"/>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						出生日期:
					</td>
					<td class="honry-view">
						${strBirthDay }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						电话:
					</td>
					<td class="honry-view">
						${user.phone }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						电子邮箱:
					</td>
					<td class="honry-view">
						${user.email }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						排序:
					</td>
					<td>
						${user.order }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						能否授权:
					</td>
					<td  class="honry-view" id="canAuthorizeId">
						<input type="hidden"  id="canAuthorize"  value="${user.canAuthorize }"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						最后一次登录时间:
					</td>
					<td class="honry-view">
						<fmt:formatDate value="${user.lastLoginTime }" pattern="yyyy-MM-dd hh:mm:ss"/>	
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						登录失败次数:
					</td>
					<td  >
						${user.failedTimes }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						移动端设备码:
					</td>
					<td  >
						${user.deviceCode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable" >
						移动端使用状态:
					</td>
					<td  id="userAppUsageStatusId">
						<input type="hidden"  id="userAppUsageStatus"  value="${user.userAppUsageStatus }"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						停用标识:
					</td>
					<td id="stopFlgTd"   class="honry-view">
						<input type="hidden" id="stopFlg" value="${user.stop_flg }"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						备注:
					</td>
					<td class="honry-view">
						${user.remark }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						删除标志:
					</td>
					<td id="delFlgTd"  class="honry-view">
						<input type="hidden"  id="delFlg"  value="${user.del_flg }"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						创建时间:
					</td>
					<td  class="honry-view">
						<fmt:formatDate value="${user.createTime }" pattern="yyyy-MM-dd hh:mm:ss"/>						
					</td>
				</tr>
			</table>
			<div style="text-align:center;padding:5px">
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
		    </div>
		    <script type="text/javascript">
		    //能否授权
		    if($('#canAuthorize').val()==1){
			    $('#canAuthorizeId').text("授权");
			}else if($('#canAuthorize').val()==0){
			    $('#canAuthorizeId').text("否");
			}else{
				$('#canAuthorizeId').text("");
			}
		    //移动端使用状态
		    if($('#userAppUsageStatus').val()){
		    	if($('#userAppUsageStatus').val()==0){
				    $('#userAppUsageStatusId').text("从未登录");
				}else if($('#userAppUsageStatus').val()==1){
				    $('#userAppUsageStatusId').text("登录过");
				}else if($('#userAppUsageStatus').val()==2){
				    $('#userAppUsageStatusId').text("已退出");
				}else{
					$('#userAppUsageStatusId').text("");
				}
		    }else{
		    	$('#userAppUsageStatusId').text("");
		    }
		   
		     if($('#stopFlg').val()==1){
		    	$('#stopFlgTd').text("已停用");
		    }else{
		    	$('#stopFlgTd').text("未停用");
		    }
		    if($('#delFlg').val()==1){
		    	$('#delFlgTd').text("已删除");
		    }else{
		    	$('#delFlgTd').text("未删除");
		    }
		     if($('#canAuthorize').val()==1){
		    	$('#canAuthorizeTd').text("授权");
		    }else{
		    	$('#canAuthorizeTd').text("否");
		    }
		    
		    var sex =$('#sex').val();
		    $('#sexTd').text(sexMap.get(sex));
		  
		    if($('#type').val()==1){
		    $('#typeTd').text("系统用户");
		    }else{
		    $('#typeTd').text("一般用户");
		    }
		  
		   
		    function closeLayout(){
			$('#divLayout').layout('remove','east');
		}
		    
		    </script>
		    
	</body>
</html>