<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

	<body>
		<div class="easyui-panel" id = "panelEast" data-options="title:'医院查看',iconCls:'icon-form',border:false">
			<div >
			<input type="hidden" id="id" name="id" value="${hospital.id }">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin-left:auto;margin-right:auto;margin-top:10px;">
				<tr>
					<td class="honry-lable">
						医院名称:
					</td>
					<td class="honry-view">
						${hospital.name }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						系统编号:
					</td>
					<td class="honry-view">
						${hospital.code}&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						简称:
					</td>
					<td class="honry-view">
						${hospital.brev }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						LOGO:
					</td>
					<td colspan="3" style="text-align: left;">
						<img  id="copyLogo" alt="" src=""> 
						<input id="logo" type="hidden" value="${hospital.logo}"> 
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						照片:
					</td>
					<td colspan="3" style="text-align: left;">
						<img  id="copyPhoto" alt="" src=""> 
						<input id="photo" type="hidden" value="${hospital.photo}"> 
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						所在省市县:
					</td>
					<td class="honry-view">
						${hospital.district }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						创建时间:
					</td>
					<td class="honry-view">
						${hospital.deleteUser }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						描述:
					</td>
					<td class="honry-view">
						${hospital.description }&nbsp;
					</td>
				</tr>
   				<tr>
					<td class="honry-lable">
						交通路线:
					</td>
					<td class="honry-view">
						${hospital.trafficRoutes }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						详细地址:
					</td>
					<td class="honry-view">
						${hospital.address }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						等级:
					</td>
					<td class="honry-view">
					<input id="leve"  value="${hospital.level }"  type="hidden">
					<input id="level" style="border:none" readonly="readonly" ></input>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						盈利性:
					</td>
					<td class="honry-view">
						<c:if test="${hospital.rentability == 1}">盈利性</c:if>
						<c:if test="${hospital.rentability == 2}">非盈利性</c:if>&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						性质:
					</td>
					<td class="honry-view">
						<c:if test="${hospital.property == 1}">公立</c:if>
						<c:if test="${hospital.property == 2}">私营</c:if>&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						医生总数:
					</td>
					<td class="honry-view">
						${hospital.doctorNum }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						护士总数:
					</td>
					<td class="honry-view">
						${hospital.nurseNum }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						备注:
					</td>
					<td class="honry-view">
						${hospital.remark }&nbsp;
					</td>
				</tr>
			</table>
			<div style="text-align:center;padding:5px">
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
		    </div>
	<script>
	var fileServerURL="${fileServerURL}";
		$(function(){
			if($('#leve').val()!=null){
				var value =$('#leve').val();
				for(var i=0; i < lvlList.length;i++){
					if(lvlList[i].encode == value){
						$('#level').val(lvlList[i].name);
					}
				}
			}
			//编辑时用为了只显示名称 而不是把路径显示出来
			var logo = $('#logo').val();
			var photo = $('#photo').val();
			if(logo!=null&&logo!=""){
				logo = logo.replace(/\\/g,"/");//等同于replaceAll（“\\”)的用法 js没有replaceAll 用法可以用词正则代替 或者（str.replace(new RegExp("\\","gm"),"/")*/
				var flogo = logo.split(".");
				var copyLogo = logo.split("/");
				var relpath =copyLogo[copyLogo.length-4]+"/"+copyLogo[copyLogo.length-3]+"/"+copyLogo[copyLogo.length-2]+"/162X122."+flogo[flogo.length-1];
				$("#copyLogo").attr("src",fileServerURL + logo);
			}
			if(photo!=null&&photo!=""){
				photo = photo.replace(/\\/g,"/");
				var fphoto = photo.split(".");
				var copyPhoto = photo.split("/");
				var relphoto = copyPhoto[copyPhoto.length-4]+"/"+copyPhoto[copyPhoto.length-3]+"/"+copyPhoto[copyPhoto.length-2]+"/162X122."+fphoto[fphoto.length - 1];
				$("#copyPhoto").attr("src",fileServerURL + photo);
			}
		});
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