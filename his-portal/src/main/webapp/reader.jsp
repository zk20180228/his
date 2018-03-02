<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!doctype html>
<html>
<head>
    <title>新中新二代证阅读器控件测试程序JavaScript</title> 
    <script src="<%=basePath%>javascript/js/jquery-1.7.2.js" type="text/javascript"></script>
	<script language="JavaScript">
	 var timer1=null;
	function FindReader_onclick()
	  {
	  	var str;
	  	str = SynCardOcx1.FindReader();
	  	if (str > 0)
	  	{
	  		if(str>1000)
	  		{
	  			str =document.all['S1'].value+ "读卡器连接在USB " + str+"\r\n" ;
	  		}
	  		else
	  		{
	  			str =document.all['S1'].value+ "读卡器连接在COM " + str+"\r\n" ;
	  		}
	  	}
	  	else
	  	{
	  		str =document.all['S1'].value+ "没有找到读卡器\r\n";
	  	}
	  	document.all['S1'].value=str;
	 	
	  }
	  function ReadSAMID_onclick()
	  {
	  	var str=SynCardOcx1.GetSAMID();
	  	document.all['S1'].value=document.all['S1'].value+"读卡器SAMID为:"+str+"\r\n";
	  }
	  function Clear_onclick()
	  {
	  	document.all['S1'].value="";
	  	$(':input') 
	  	.not(':button, :submit, :reset, :hidden') 
	  	.val('');
	  	
	  	var file = $("#fileID") ;
	  	file.after(file.clone().val(""));      
	  	file.remove();  
	  	
	  	$("#photoid").hide();
	  	$('#tbl2').hide();
	  }
	  function ReadCard_onclick()
	  {
		  clearInterval(timer1);
		  readerCar();
	  }
	  
	  function readerCar(){
// 		  var asd=idCardJson();
// 		  if(asd.resCode==0){
// 			  alert(asd.age);
// 		  }else{
// 			  alert(asd.resMSg);
// 		  }
		  var nRet;
			 SynCardOcx1.SetUserLifeBType(2);//返回有效期开始日期的格式0=YYYYMMDD,1=YYYY年MM月DD日,2=YYYY.MM.DD,3=YYYY-MM-DD,4=YYYY/MM/DD
	     	 SynCardOcx1.SetUserLifeEType(2,0);//返回有效期结束日期的格式0=YYYYMMDD,1=YYYY年MM月DD日,2=YYYY.M
	    	 SynCardOcx1.SetBornType(3);//返回出生日期的格式0=YYYYMMDD,1=YYYY年MM月DD日,2=YYYY.MM.DD,　　3=YYY-MM-DD,4=YYYY/MM/DD
	    	 SynCardOcx1.SetNationType(2);//返回民族的格式0=卡内存储的数据 ， 1=解释之后的数据	，2=解释之后+“族”
	    	 SynCardOcx1.SetSexType(1);//返回性别	0=代码   1 解释之后
	    	 nRet = SynCardOcx1.ReadCardMsg();
		  	if(nRet==0)
		  	{
		  		document.all['S1'].value=document.all['S1'].value+"读卡成功\r\n";	
		  		$('#named').val(SynCardOcx1.NameA);
		  		$('#sex').val(SynCardOcx1.Sex);
		  		$('#age').val(getage(SynCardOcx1.CardNo)+"岁");
		  		$('#nation').val(SynCardOcx1.Nation);
		  		$('#born').val(SynCardOcx1.Born);
		  		$('#address').val(SynCardOcx1.Address);
		  		$('#cardNo').val(SynCardOcx1.CardNo);
		  		$('#userLifeB').val(SynCardOcx1.UserLifeB);
		  		$('#userLifeE').val(SynCardOcx1.UserLifeE);
		  		$('#police').val(SynCardOcx1.Police);
		  		$('#photoName').val(SynCardOcx1.PhotoName);
		  		$("#photoPathId").html(SynCardOcx1.PhotoName);
		  		
		  		$('#tbl2').show();
		  	}
			
	  }
	  
	  function ReadCardAuto_onclick()
	  {
		  timer1= setInterval('readerCar()',5000);
		  
// 		SynCardOcx1.FindReader(); 
// 		SynCardOcx1.SetloopTime(1000);
// 	  	SynCardOcx1.SetReadType(1);
	  }

	  function PhotoPath_onclick()
	  {
	  	var str="";
	  	SynCardOcx1.SetPhotoPath(0,str);
	  	document.all['S1'].value=document.all['S1'].value+"照片保存路径设置为C盘根目录\r\n";
	  }
	  function PhotoPath2_onclick()
	  {
	  	var str="";
	  	SynCardOcx1.SetPhotoPath(1,str);
	  	document.all['S1'].value=document.all['S1'].value+"照片保存路径设置为当前目录\r\n";
	  }
	  function PhotoPath3_onclick()
	  {
	  	var str="D:\\Photo";
	  	var nRet;
	  	nRet= SynCardOcx1.SetPhotoPath(2,str);
	  	if(nRet == 0)
	  	{
	  		document.all['S1'].value=document.all['S1'].value+"照片保存路径设置为"+str+"\r\n";
	  	}
	  	else
	  	{
	  		document.all['S1'].value=document.all['S1'].value+"照片保存路径设置失败\r\n";  	
	  	}
	  }
	  function PhotoType_onclick()
	  {
	  	var nRet;
		nRet=SynCardOcx1.SetPhotoType(0);
		if(nRet==0)
		{
			document.all['S1'].value=document.all['S1'].value+"照片保存格式设置为Bmp\r\n";
		}
	  }
	  function PhotoType2_onclick()
	  {
	  	var nRet;
		nRet=SynCardOcx1.SetPhotoType(1);
		if(nRet==0)
		{
			document.all['S1'].value=document.all['S1'].value+"照片保存格式设置为Jpg\r\n";
		}
	  }
	  function PhotoType3_onclick()
	  {
	  	var nRet;
		nRet=SynCardOcx1.SetPhotoType(2);
		if(nRet==0)
		{
			document.all['S1'].value=document.all['S1'].value+"照片保存格式设置为Base64\r\n";
		}
	  }
	  function PhotoName_onclick()
	  {
	   	var nRet;
		nRet=SynCardOcx1.SetPhotoName(0);
		if(nRet==0)
		{
			document.all['S1'].value=document.all['S1'].value+"照片保存文件名设置为tmp\r\n";
		}
	  }
	  function PhotoName2_onclick()
	  {
	   	var nRet;
		nRet=SynCardOcx1.SetPhotoName(1);
		if(nRet==0)
		{
			document.all['S1'].value=document.all['S1'].value+"照片保存文件名设置为 姓名\r\n";
		}
	  }
	  function PhotoName3_onclick()
	  {
	   	var nRet;
		nRet=SynCardOcx1.SetPhotoName(2);
		if(nRet==0)
		{
			document.all['S1'].value=document.all['S1'].value+"照片保存文件名设置为 身份证号\r\n";
		}
	  }
	  function PhotoName4_onclick()
	  {
	   	var nRet;
		nRet=SynCardOcx1.SetPhotoName(3);
		if(nRet==0)
		{
			document.all['S1'].value=document.all['S1'].value+"照片保存文件名设置为 姓名_身份证号\r\n";
		}
	  }
	  function PhotoName5_onclick()
	  {
	  	var str= SynCardOcx1.Base64Photo;
		document.all['S1'].value=document.all['S1'].value+str+"  \r\n";
	  }
	  function M1Reset_onclick()
	  {
	  	var str= SynCardOcx1.M1Reset();
		document.all['S1'].value=document.all['S1'].value+str+"  \r\n";
	  }
	  function M1Halt_onclick()
	  {
	  	var str= SynCardOcx1.M1Halt;
		document.all['S1'].value=document.all['S1'].value+str+"  \r\n";
	  }
	  function M1AuthenKey_onclick()
	  {
	  	//BSTR M1AuthenKey(LONG KeyType, LONG BlockNo, LPCTSTR uKey, LPCTSTR uKey2, LPCTSTR uKey3, LPCTSTR uKey4, LPCTSTR uKey5, LPCTSTR uKey6)
	  	//LONG KeyType  0=A密钥  1=B密钥
	  	//LONG BlockNo  扇区号 
	  	//uKey~uKey6 密钥 
	  	//返回0成功
	  	var str= SynCardOcx1.M1AuthenKey(0,0,"255","255","255","255","255","255");
		document.all['S1'].value=document.all['S1'].value+str+"  \r\n";
	  }
	  function M1Read_onclick()
	  {
	  	//BSTR M1ReadBlock(LONG BlockNo)
	  	//LONG BlockNo 块号
	  	//返回数据位16进制 16个字节 空格分隔
	  	var str= SynCardOcx1.M1ReadBlock(1);
		document.all['S1'].value=document.all['S1'].value+str+"  \r\n";
	  }
	  function M1Write_onclick()
	  {
	  	//BSTR M1WriteBlock(LONG BlockNo, LPCTSTR uData, LPCTSTR uData2, LPCTSTR uData3, LPCTSTR uData4, LPCTSTR uData5, LPCTSTR uData6, LPCTSTR uData7, LPCTSTR uData8, LPCTSTR uData9, LPCTSTR uData10, LPCTSTR uData11, LPCTSTR uData12, LPCTSTR uData13, LPCTSTR uData14, LPCTSTR uData15, LPCTSTR uData16)
			//LONG BlockNo 块号
			//uData 数据 数据位0，应写为00， 1写为01,12写为12 
	  	var str= SynCardOcx1.M1WriteBlock(1,"0","0","0","0","0","1","0","1","0","0","0","0","0","0","0","0");
		document.all['S1'].value=document.all['S1'].value+str+"  \r\n";
	  }

	function load(){
		$('#photoid').hide();
		$('#tbl2').hide();
		PhotoPath_onclick();
		PhotoName3_onclick();
		PhotoType2_onclick();
		FindReader_onclick();
	}

	function getage(sfz){
	  var str = sfz;		// 身份证编码
	  var len = str.length;	// 身份证编码长度
	  if (len < 15) {
		  alert("不是有效身份证编码");
	  } else {
		  if ((len != 15) && (len != 18)) {	// 判断编码位数等于15或18
			  alert("不是有效身份证编码");
		  } else {
			  if (len == 15) {				// 15位身份证
				  var s = str.substr(len - 1, 1);
				  var date1 = new Date();	// 取得当前日期
				  var year1 = date1.getFullYear();	// 取得当前年份
				  var month1 = date1.getMonth();	// 取得当前月份
				  if (month1 > parseInt(str.substr(8, 2))){	// 判断当前月分与编码中的月份大小
				      var age = year1 - ("19" + str.substr(6, 2));
				  	 return age;
				  }
				  else{
					  var age = year1 - ("19" + str.substr(6, 2)) - 1;
					  return age;
				  }
			  }
			  if (len == 18) {				// 18位身份证
				  var date1 = new Date();	// 取得当前日期
				  var year1 = date1.getFullYear();	// 取得当前年份
				  var month1 = date1.getMonth();	// 取得当前月份
				  if (month1 > parseInt(str.substr(10, 2))){	// 判断当前月分与编码中的月份大小
					  var age = year1 - str.substr(6, 4);
					  return age;
				  }
				  else{
					  var age = year1 - str.substr(6, 4) - 1;
					  return age;
				  }
			  }
		  }
	  }
	} 
	
		//提交信息
	function sbm() {
		var f = $('#fileID').val();
		if (f == "") {
			alert("请选择图片");
			return false;
		} else {
			if (!/\.(gif|jpg|jpeg|png|bmp)$/.test(f.toLowerCase())) {
				alert("图片类型必须是.gif,jpeg,jpg,png中的一种")
				return false;
			}else{
				$('#form2').submit();
			}
		}

	}
	
	
	function callback(obj)   
	{
		if(obj.resCode==0){
			$("#photoid").attr("src","<%=basePath%>"+obj.path);
			$('#photoid').show();
			$('#serverPhotoName').val(obj.path);
			alert(obj.resMsg);
		}else{
			alert(obj.resMsg);
		}
	}  
	
	</script>
</head>
<body leftmargin="0" topmargin="0" onLoad="load();" style="background-color:#fff;">
<p>
	<object classid="clsid:46E4B248-8A41-45C5-B896-738ED44C1587" id="SynCardOcx1" 
	codeBase="${pageContext.request.contextPath}/common/0128cab.CAB#version=1,0,0,1" width="0" height="0" ></object>
<!-- 	<script language="JavaScript"  for="SynCardOcx1" event="CardIn(State)"></script> -->
</p>
<form  method="post" action='<%=basePath %>business/data.action'>
  	<table>
		<tr>
			<td>姓名：</td>
			<td><input id="named" name="name"/></td>
		</tr>
		<tr>
			<td>性别：</td>
			<td><input id="sex" name="sex"/></td>
		</tr>
		<tr>
			<td>年龄：</td>
			<td><input id="age" name="age"/></td>
		</tr>
		<tr>
			<td>民族：</td>
			<td><input id="nation" name="nation"/></td>
		</tr>
		<tr>
			<td>出生日期：</td>
			<td><input id="born" name="born"/></td>
		</tr>
		<tr>
			<td>地址：</td>
			<td><input id="address" name="address"/></td>
		</tr>
		<tr>
			<td>身份证号：</td>
			<td><input id="cardNo" name="cardNo"/></td>
		</tr>
		<tr>
			<td>有效期开始：</td>
			<td><input id="userLifeB" name="userLifeB"/></td>
		</tr>
		<tr>
			<td>有效期结束：</td>
			<td><input id="userLifeE" name="userLifeE"/></td>
		</tr>
		<tr>
			<td>发证机关：</td>
			<td><input id="police" name="police"/></td>
		</tr>
		<tr>
			<td>照片文件名：</td>
			<td><input  id="photoName" name="photoName"/></td>
		</tr>
		<tr>
			<td>服务端地址：</td>
			<td><input  id="serverPhotoName" name="serverPhoto"/></td>
		</tr>
		<tr>
			<td><img id="photoid" alt="身份证图片"  style="height: 102px;width: 126px;"></td>
			<td><input type="button" value="信息提交" onclick="submit()"/></td>
		</tr>
	</table>
</form>	
<iframe name="upload" style="display:none"></iframe> 
<form id="form2" enctype="multipart/form-data" target="upload" method="post" action='<%=basePath %>business/reader.action'>
	<table id="tbl2">
		<tr> 
			<td>身份证头像所在路径：<span id="photoPathId"></span></td>
		</tr>
		<tr><td>请选择文件，并上传</td></tr>
		<tr>
			<td><input id="fileID" name="photo" type="file"/></td>
			<td><input id="fileupload" type="button" value="上传" onclick="sbm()" /></td>
		</tr>
	</table>
</form>
  <p><input type="button" value="自动寻找读卡器" name="FindReadBtn" onclick="FindReader_onclick()">
  <input type="button" value="获得SAMID" name="GetSAMIDBtn" onclick="ReadSAMID_onclick()">
  <input type="button" value="手动读卡" name="ReadCardBtn" onclick="ReadCard_onclick()">
  <input type="button" value="自动读卡" name="ReadCardAutoBtn" onclick="ReadCardAuto_onclick()">
  <input type="button" value="清除所有信息" name="ClearBtn" onclick="Clear_onclick()"></p>
  <p><textarea rows="17" name="S1" cols="82"></textarea></p>
</body>
</html>