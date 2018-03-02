//时间处理
function formattime(val) {
	var year = parseInt(val.year) + 1900;
	var month = (parseInt(val.month) + 1);
	month = month > 9 ? month : ('0' + month);
	var date = parseInt(val.date);
	date = date > 9 ? date : ('0' + date);
	var hours = parseInt(val.hours);
	hours = hours > 9 ? hours : ('0' + hours);
	var minutes = parseInt(val.minutes);
	minutes = minutes > 9 ? minutes : ('0' + minutes);
	var seconds = parseInt(val.seconds);
	seconds = seconds > 9 ? seconds : ('0' + seconds);
	var time = year + '-' + month + '-' + date + ' ' + hours + ':' + minutes
			+ ':' + seconds;
	return time;
}

/*
 * 选项卡切换时激活当前选中，禁用未选中
 * 
 * 
 */
function disableTabs(tableID, arrLenth) {
	var arr = [];
	for (i = 0; i < arrLenth; i++) {
		arr[i] = i;
	}
	var tab = $(tableID).tabs('getSelected');
	var index = $(tableID).tabs('getTabIndex', tab);
	var tab = $(tableID).tabs('getSelected');
	var index = $(tableID).tabs('getTabIndex', tab);
	arr.splice($.inArray(index, arr), 1);
	$.each(arr, function(n, value) {
		if (n != 0)
			$(tableID).tabs('disableTab', n);
	});
	$(tableID).tabs('enableTab', index);
	// console.info('需要禁用的选项卡有：'+arr);
}

// 删除选中table row
function delRow(tableID) {
	var rows = $(tableID).datagrid("getSelections");
	var copyRows = [];
	for (var j = 0; j < rows.length; j++) {
		copyRows.push(rows[j]);
	}
	for (var i = 0; i < copyRows.length; i++) {
		var index = $(tableID).datagrid('getRowIndex', copyRows[i]);
		$(tableID).datagrid('deleteRow', index);
	}
}

// 获取datagrid选中row的ID(单选) 注：该ID没有拼接单引号 例如：ID = 123456
function getIdUtil(tableID) {
	var row = $(tableID).datagrid("getSelections");
	var i = 0;
	var getid = "";
	if (row.length != 1) {
		$.messager.alert("操作提示", "请选择一条记录！", "warning");
		return null;
	} else {
		for (i; i < row.length; i++) {
			getid = row[i].id;
		}
	}
	return getid;
}

function getIdUtilsy(tableID, id) {
	var row = $(tableID).datagrid("getSelections");
	var i = 0;
	var getid = "";
	if (row.length != 1) {
		$.messager.alert("操作提示", "请选择一条记录！", "warning");
		return null;
	} else {
		for (i; i < row.length; i++) {
			getid = row[i].id;
		}
	}
	return getid;
}

// 获取datagrid选中row的ID（多选） 注：该ID有拼接单引号 例如：ID = '123456'
function getbachIdUtil(id, objId) {
	var row = $("#" + objId).datagrid("getSelections");
	var dgID = "";
	if (row.length < 1) {
		$.messager.alert("操作提示", "请选择一条记录！", "warning");
	}
	var i = 0;
	for (i; i < row.length; i++) {
		dgID += "\'" + row[i].id + "\'";
		if (i < row.length - 1) {
			dgID += ',';
		} else {
			break;
		}
	}
	return dgID;
}

// 获取datagrid选中row的ID（多选） 注：该ID没有拼接单引号 例如：ID = 123456
function getbachIdUtil2(id, objId) {
	var row = $("#" + objId).datagrid("getSelections");
	var dgID = "";
	if (row.length < 1) {
		$.messager.alert("操作提示", "请选择一条记录！", "warning");
	}
	var i = 0;
	for (i; i < row.length; i++) {
		dgID += row[i].id;
		if (i < row.length - 1) {
			dgID += ',';
		} else {
			break;
		}
	}
	return dgID;
}
// 如果datagtid没有选中任何数据则返回第一条数据ID！
function getTabNocheckedID(tableID) {
	var row = $(tableID).datagrid("getSelections");
	var id = "";
	if (row.length < 1) {// 如果没有选择数据，默认选中第一行数据
		$(tableID).datagrid('selectRow', 0);
		var row = $(tableID).datagrid("getSelections");
	}
	id += row[0].USER_ID;
	return id;
}

// 字符串
function Encode(value) {
	return encodeURI(value);
}

// 字符串
function encodeJson(Json) {
	var s = Edo.util.Json.encode(Json);
	var json = Edo.util.Json.decode(s);

	$.each(json, function(val) {
		o = json[val];
		if (o != null) {
			if (typeof (json[val]) == "string") {
				json[val] = Encode(json[val]);
			} else if (typeof (json[val]) == "object") {
				if (o.length) {
					for (var i = 0; i < o.length; i++) {
						$.each(o[i], function(val) {
							o[i][val] = Encode(o[i][val]);
						});
					}
				} else {
					o[val] = Encode(o[val]);
				}
			}
		}
	});
	return json;
}

// 字符串
function alertJson(json) {
	alert(Edo.util.Json.encode(json));
}

// 当光标移上去,图片自动放大,缩小功能
function images_AutoMax(e, o) {
	var zoom = parseInt(o.style.zoom, 10) || 100;
	zoom += event.wheelDelta / 12;
	if (zoom > 0) {
		o.style.zoom = zoom + '%';
	}
	return false;
}
// 图片缩放
function DrawImage(ImgD, ImgW, ImgH) {

	var image = new Image();
	image.src = ImgD.src;
	if (image.width > 0 && image.height > 0) {
		if (image.width / image.height >= ImgW / ImgH) {
			if (image.width > ImgW) {
				ImgD.width = ImgW;
				ImgD.height = (image.height * ImgW) / image.width;
			} else {
				ImgD.width = image.width;
				ImgD.height = image.height;
			}
		} else {
			if (image.height > ImgH) {
				ImgD.height = ImgH;
				ImgD.width = (image.width * ImgH) / image.height;
			} else {
				ImgD.width = image.width;
				ImgD.height = image.height;
			}
		}
		ImgD.alt = image.width + "×" + image.height;
	}
}

function openIE(ieName, ieWidth, ieHeight) // 打开一个窗口
{
	var openIe = null;
	var scroll = "auto";
	var leftPosition = (screen.width) ? (screen.width - ieWidth) / 2 : 0;
	var topPositon = (screen.height) ? (screen.height - ieHeight) / 2 : 0;
	var settings = 'height=' + ieHeight + ',width=' + ieWidth + ',top='
			+ topPositon + ',left=' + leftPosition + ',scrollbars=' + scroll
			+ ',resizable';
	openIe = window.open(ieName, "", settings)
} // 新开的IE名,必须有参数,打开方式
function openHelp(ieURL) // 打开一个窗口
{
	var openIe = "系统帮助";
	var scroll = "auto";
	var ieWidth = 500;
	var ieHeight = 400;
	var leftPosition = (screen.width) ? (screen.width - ieWidth) / 2 : 0;
	var topPositon = (screen.height) ? (screen.height - ieHeight) / 2 : 0;
	var settings = 'height=' + ieHeight + ',width=' + ieWidth + ',top='
			+ topPositon + ',left=' + leftPosition + ',scrollbars=' + scroll
			+ ',resizable';
	openIe = window.open(ieURL, "MisHelp", settings)
} // 新开的IE名,必须有参数,打开方式

function trimAll(empValue) {
	var returnValue = empValue;
	// if(returnValue!="")
	// {
	while ((returnValue.length > 0) && (returnValue.charAt(0) == ' ')) {
		returnValue = returnValue.substring(1, returnValue.length);
	}
	while ((returnValue.length > 0)
			&& (returnValue.charAt(returnValue.length - 1) == ' ')) {
		returnValue = returnValue.substring(0, returnValue.length - 1);
	}
	// }
	return returnValue;
}
// 验证身份证格式是否正确
function isIdCardNo(idCard) {
	var id = idCard;
	var id_length = id.length;

	if (id_length == 0) {
		$.messager.alert("操作提示", "请输入身份证号码!");
		return false;
	}

	if (id_length != 15 && id_length != 18) {
		$.messager.alert("操作提示", "身份证号长度应为15位或18位！");
		return false;
	}

	if (id_length == 15) {
		yyyy = "19" + id.substring(6, 8);
		mm = id.substring(8, 10);
		dd = id.substring(10, 12);

		if (mm > 12 || mm <= 0) {
			$.messager.alert("操作提示", "输入身份证号,月份非法！");
			return false;
		}

		if (dd > 31 || dd <= 0) {
			$.messager.alert("操作提示", "输入身份证号,日期非法！");
			return false;
		}

		birthday = yyyy + "-" + mm + "-" + dd;

		if ("13579".indexOf(id.substring(14, 15)) != -1) {
			sex = "1";
		} else {
			sex = "2";
		}
	} else if (id_length == 18) {
		if (id.indexOf("X") > 0 && id.indexOf("X") != 17 || id.indexOf("x") > 0
				&& id.indexOf("x") != 17) {
			$.messager.alert("操作提示", "身份证中\"X\"输入位置不正确！");
			return false;
		}

		yyyy = id.substring(6, 10);
		if (yyyy > 2200 || yyyy < 1900) {
			$.messager.alert("操作提示", "输入身份证号,年份非法！");
			return false;
		}

		mm = id.substring(10, 12);
		if (mm > 12 || mm <= 0) {
			$.messager.alert("操作提示", "输入身份证号,月份非法！");
			return false;
		}

		dd = id.substring(12, 14);
		if (dd > 31 || dd <= 0) {
			$.messager.alert("操作提示", "输入身份证号,日期非法！");
			return false;
		}

		/*
		 * if (id.charAt(17)=="x" || id.charAt(17)=="X") { if
		 * ("x"!=GetVerifyBit(id) && "X"!=GetVerifyBit(id)){
		 * alert("身份证校验错误，请检查最后一位！"); return false; }
		 * 
		 * }else{ if (id.charAt(17)!=GetVerifyBit(id)){
		 * alert("身份证校验错误，请检查最后一位！"); return false; } }
		 */

		birthday = id.substring(6, 10) + "-" + id.substring(10, 12) + "-"
				+ id.substring(12, 14);
		if ("13579".indexOf(id.substring(16, 17)) > -1) {
			sex = "1";
		} else {
			sex = "2";
		}
	}

	return true;
}

// 15位转18位身份证中,计算校验位即最后一位
function GetVerifyBit(id) {
	var result;
	var nNum = eval(id.charAt(0) * 7 + id.charAt(1) * 9 + id.charAt(2) * 10
			+ id.charAt(3) * 5 + id.charAt(4) * 8 + id.charAt(5) * 4
			+ id.charAt(6) * 2 + id.charAt(7) * 1 + id.charAt(8) * 6
			+ id.charAt(9) * 3 + id.charAt(10) * 7 + id.charAt(11) * 9
			+ id.charAt(12) * 10 + id.charAt(13) * 5 + id.charAt(14) * 8
			+ id.charAt(15) * 4 + id.charAt(16) * 2);
	nNum = nNum % 11;
	switch (nNum) {
	case 0:
		result = "1";
		break;
	case 1:
		result = "0";
		break;
	case 2:
		result = "X";
		break;
	case 3:
		result = "9";
		break;
	case 4:
		result = "8";
		break;
	case 5:
		result = "7";
		break;
	case 6:
		result = "6";
		break;
	case 7:
		result = "5";
		break;
	case 8:
		result = "4";
		break;
	case 9:
		result = "3";
		break;
	case 10:
		result = "2";
		break;
	}
	// document.write(result);
	return result;
}
// 15位转18位身份证
function Get18(idCard) {
	if (CheckValue(idCard)) {
		var id = idCard;
		var id18 = id;
		if (id.length == 0) {
			$.messager.alert("操作提示", "请输入15位身份证号！");
			return false;
		}
		if (id.length == 15) {
			if (id.substring(6, 8) > 20) {
				id18 = id.substring(0, 6) + "19" + id.substring(6, 15);
			} else {
				id18 = id.substring(0, 6) + "20" + id.substring(6, 15);
			}

			id18 = id18 + GetVerifyBit(id18);
		}

		return id18;
	} else {
		return false;
	}
}

// 判断一个字符串是否为合法日期
function isDate(strDate) {
	var strSeparator = "-"; // 日期分隔符
	var strDateArray;
	var intYear;
	var intMonth;
	var intDay;
	var boolLeapYear;

	strDateArray = strDate.split(strSeparator);

	if (strDateArray.length != 3)
		return false;

	intYear = parseInt(strDateArray[0], 10);
	intMonth = parseInt(strDateArray[1], 10);
	intDay = parseInt(strDateArray[2], 10);

	if (isNaN(intYear) || isNaN(intMonth) || isNaN(intDay))
		return false;

	if (intMonth > 12 || intMonth < 1)
		return false;

	if ((intMonth == 1 || intMonth == 3 || intMonth == 5 || intMonth == 7
			|| intMonth == 8 || intMonth == 10 || intMonth == 12)
			&& (intDay > 31 || intDay < 1))
		return false;

	if ((intMonth == 4 || intMonth == 6 || intMonth == 9 || intMonth == 11)
			&& (intDay > 30 || intDay < 1))
		return false;

	if (intMonth == 2) {
		if (intDay < 1)
			return false;

		boolLeapYear = false;
		if ((intYear % 100) == 0) {
			if ((intYear % 400) == 0)
				boolLeapYear = true;
		} else {
			if ((intYear % 4) == 0)
				boolLeapYear = true;
		}

		if (boolLeapYear) {
			if (intDay > 29)
				return false;
		} else {
			if (intDay > 28)
				return false;
		}
	}

	return true;
}
function Jtrim(str)// 去字符串前后空格
{
	var i = 0;
	var len = str.length;
	if (str == "")
		return (str);
	j = len - 1;
	flagbegin = true;
	flagend = true;
	while ((flagbegin == true) && (i < len)) {
		if (str.charAt(i) == " ") {
			i = i + 1;
			flagbegin = true;
		} else {
			flagbegin = false;
		}
	}

	while ((flagend == true) && (j >= 0)) {
		if (str.charAt(j) == " ") {
			j = j - 1;
			flagend = true;
		} else {
			flagend = false;
		}
	}

	if (i > j)
		return ("");

	trimstr = str.substring(i, j + 1);
	return trimstr;
}

// 获得一个字符串str中含有s的个数
function getStringCharNumber(str, s) {
	var nCount = 1;
	if (str.indexOf(s) != -1) {
		nCount = str.split(s).length;
	}
	return nCount;
}

function isValidDate(year, month, day) // 判断日期格式是否正确
{
	year = parseInt(year, 10);
	month = parseInt(month, 10);
	day = parseInt(day, 10);

	if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
		if ((day < 1) || (day > 30)) {
			alert("日期在1 - 30之间");
			return (false);
		}
	} else {
		if (month != 2) {
			if ((day < 1) || (day > 31)) {
				alert("日期在1 - 31之间");
				return (false);
			}
		} else { // month == 2
			if ((year % 100) != 0 && (year % 4 == 0) || (year % 100) == 0
					&& (year % 400) == 0) {
				if (day > 29) {
					alert("日期在1 - 29之间");
					return (false);
				}
			} else {
				if (day > 28) {
					alert("日期在1 - 28之间");
					return (false);
				}
			}
		}
	}
	return (true);
}

function isFloat(val) {
	var reg = /^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/;
	return val.match(reg) != null;
	/*
	 * if(!IsMatch(val,"(1|2|3|4|5|6|7|8|9)(0|1|2|3|4|5|6|7|8|9*).(0|1|2|3|4|5|6|7|8|9*)") &&
	 * !IsMatch(val,"0.(0|1|2|3|4|5|6|7|8|9*)") && !isnumeric(val)){ return
	 * false ; } return true;
	 */
}
function IsMatch(s, sReg) {
	var len = sReg.length;
	var loc;
	if (s == "") {
		if (sReg == "") {
			return true;
		} else {
			if (sReg.substring(0, 1) != "(")
				return false;
			loc = getCorCharAddress("(", 0, ")", sReg)
			if (sReg.substring(loc - 1, loc) != "*")
				return false;
			return IsMatch("", RemoveStar(sReg, 0));
		}
	}
	if (s != "" && sReg == "")
		return false;
	if (s.substring(0, 1) == sReg.substring(0, 1)) {
		return IsMatch(s.substring(1), sReg.substring(1));
	}
	if (sReg.substring(0, 1) == "(") {
		loc = getCorCharAddress("(", 0, ")", sReg)
		if (sReg.substring(loc - 1, loc) == "*")
			return IsMatch(RemoveChar(s, sReg.substring(1, loc - 1), 0), sReg
					.substring(loc + 1));
		else if (sReg.substring(loc - 1, loc) == "+") {
			if (!StartsWith(s, sReg.substring(1, loc - 1)))
				return false;
			else
				return IsMatch(RemoveChar(s, sReg.substring(1, loc - 1), 0),
						sReg.substring(loc + 1));
		} else {
			if (!StartsWith(s, sReg.substring(1, loc)))
				return false;
			else {
				return IsMatch(RemoveChar(s, sReg.substring(1, loc), 1), sReg
						.substring(loc + 1));
			}
		}
	} else if (sReg.substring(0, 1) == "[") {
		loc = getCorCharAddress("[", 0, "]", sReg)
		if (sReg.substring(0, 1) == "~") {
			if (StartsWith(s, Insert(sReg.substring(2, loc), "|")))
				return false;
			else
				return IsMatch(s.substring(1), sReg.substring(loc + 1));
		} else
			return IsMatch(
					RemoveChar(s, Insert(sReg.substring(2, loc), "|"), 1), sReg
							.substring(loc + 1));
	} else
		return false;
}
function RemoveChar(s1, s2, num) {
	var charlist = s2.split("|");
	if (num == 0) {
		while (StartsWith(s1, s2)) {
			s1 = RemoveChar(s1, s2, 1);
		}
	} else {
		for (var i = 1; i <= num; i++) {
			if (!StartsWith(s1, s2))
				break;
			else {
				for (var j = 0; j <= charlist.length - 1; j++) {
					if (StartWith(s1, charlist[j])) {
						s1 = s1.substring(charlist[j].length);
						break;
					}
				}
			}
		}
	}
	return s1;
}

function RemoveStar(s, num) {
	var loc;
	var s1 = s;
	var b = false;
	if (s == "")
		return "";
	if (s.substring(0, 1) != "(")
		return s;
	loc = getCorCharAddress("(", 0, ")", s1);
	if (s1.substring(loc - 1, loc) != "*")
		return s;
	if (num == 0) {
		while (!b) {
			s1 = RemoveStar(s1, 1);
			if (s1 == "")
				b = true;
			else if (s1.substring(0, 1) != "(")
				b = true;
			else {
				loc = getCorCharAddress("(", 0, ")", s1);
				if (s1.substring(loc - 1, loc) != "*")
					b = true;
			}
		}
		return s1;
	} else {
		for (var i = 1; i <= num; i++) {
			if (s1.substring(0, 1) != "(")
				break;
			loc = getCorCharAddress("(", 0, ")", s1);
			if (s1.substring(loc - 1, loc) != "*")
				break;
			s1 = s1.substring(loc + 1);
			if (s1 == "")
				break;
		}
		return s1;
	}
}

function StartsWith(s1, s2) {
	var charlist = s2.split("|");
	for (var j = 0; j <= charlist.length - 1; j++) {
		if (StartWith(s1, charlist[j]))
			return true;
	}
	return false;
}

function StartWith(s1, s2) {
	return s1.substring(0, s2.length) == s2;
}

function Insert(s, c) {
	var len = s.length;
	var tempstr = "";
	for (var i = 0; i <= len - 1; i++) {
		if (tempstr == "")
			tempstr = s.substring(i, i + 1);
		else
			tempstr += c + s.substring(i, i + 1);
	}
	return tempstr;
}

function getCorCharAddress(s1, loc, s2, str) {
	var s = str.substring(loc);
	var intCharNum = 1;
	var ret = 0;
	for (var i = 1; i <= s.length - 1; i++) {
		if (s.substring(i, i + 1) == s1)
			intCharNum++;
		else if (s.substring(i, i + 1) == s2)
			intCharNum--;
		if (intCharNum == 0) {
			ret = i;
			break;
		}
	}
	return loc + ret;
}

function isNumeric(val) // 是否是正整数
{
	if (Jtrim(val) != "") {
		if (!IsMatch(val, "(1|2|3|4|5|6|7|8|9)(0|1|2|3|4|5|6|7|8|9*)")
				|| IsMatch(val, "0")) {
			return "输入的字符必须为整数！";
		}
	}
}

function Compare(str, dY, dM, dD) {
	var sY, sM, sD;
	var dY, dM, dD;
	sY = parseInt(str.split("-")[0]);
	sM = parseInt(str.split("-")[1]);
	sD = parseInt(str.split("-")[2]);
	if (sY > dY)
		return 1;
	else if (sY < dY)
		return -1;
	else {
		if (sM > dM)
			return 1;
		else if (sM < dM)
			return -1;
		else {
			if (sD > dD)
				return 1;
			else if (sD < dD)
				return -1;
			else
				return 0;
		}
	}
}
// 光标是停在文本框文字的最后
function TextLast() {
	var e = event.srcElement;
	var r = e.createTextRange();
	r.moveStart("character", e.value.length);
	r.collapse(true);
	r.select();
}

// ////////////////////////////////////////////////////////////////////////////////////
// 编码和反编码
var base64EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
var base64DecodeChars = new Array(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1,
		63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1,
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
		20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31,
		32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49,
		50, 51, -1, -1, -1, -1, -1);

function encode(str) {
	var out, i, len;
	var c1, c2, c3;

	len = str.length;
	i = 0;
	out = "";
	while (i < len) {
		c1 = str.charCodeAt(i++) & 0xff;
		if (i == len) {
			out += base64EncodeChars.charAt(c1 >> 2);
			out += base64EncodeChars.charAt((c1 & 0x3) << 4);
			out += "==";
			break;
		}
		c2 = str.charCodeAt(i++);
		if (i == len) {
			out += base64EncodeChars.charAt(c1 >> 2);
			out += base64EncodeChars.charAt(((c1 & 0x3) << 4)
					| ((c2 & 0xF0) >> 4));
			out += base64EncodeChars.charAt((c2 & 0xF) << 2);
			out += "=";
			break;
		}
		c3 = str.charCodeAt(i++);
		out += base64EncodeChars.charAt(c1 >> 2);
		out += base64EncodeChars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
		out += base64EncodeChars.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >> 6));
		out += base64EncodeChars.charAt(c3 & 0x3F);
	}
	return out;
}

function decode(str) {
	var c1, c2, c3, c4;
	var i, len, out;

	len = str.length;
	i = 0;
	out = "";
	while (i < len) {
		/* c1 */
		do {
			c1 = base64DecodeChars[str.charCodeAt(i++) & 0xff];
		} while (i < len && c1 == -1);
		if (c1 == -1)
			break;

		/* c2 */
		do {
			c2 = base64DecodeChars[str.charCodeAt(i++) & 0xff];
		} while (i < len && c2 == -1);
		if (c2 == -1)
			break;

		out += String.fromCharCode((c1 << 2) | ((c2 & 0x30) >> 4));

		/* c3 */
		do {
			c3 = str.charCodeAt(i++) & 0xff;
			if (c3 == 61)
				return out;
			c3 = base64DecodeChars[c3];
		} while (i < len && c3 == -1);
		if (c3 == -1)
			break;

		out += String.fromCharCode(((c2 & 0XF) << 4) | ((c3 & 0x3C) >> 2));

		/* c4 */
		do {
			c4 = str.charCodeAt(i++) & 0xff;
			if (c4 == 61)
				return out;
			c4 = base64DecodeChars[c4];
		} while (i < len && c4 == -1);
		if (c4 == -1)
			break;
		out += String.fromCharCode(((c3 & 0x03) << 6) | c4);
	}
	return out;
}

function getOrg(sFormName, sInputCode, sInputName) {
	var sSub = window
			.open(
					"../include/selectorg.jsp?form=" + sFormName + "&objName="
							+ sInputName + "&objCode=" + sInputCode,
					"请选择国家",
					"height=480,width=640,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
	sSub.focus();
}

function getUser(sFormName, sInputCode, sInputName) {
	var sSub = window
			.open(
					"../include/selectuser.jsp?form=" + sFormName + "&objName="
							+ sInputName + "&objCode=" + sInputCode,
					"请选择人员",
					"height=480,width=640,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
	sSub.focus();
}

function trim(str) { // 删除左右两端的空格
	return str.replace(/(^\s*)|(\s*$)/g, "");
}
function ltrim(str) { // 删除左边的空格
	return str.replace(/(^\s*)/g, "");
}
function rtrim(str) { // 删除右边的空格
	return str.replace(/(\s*$)/g, "");
}

function focusTextArea(obj, num) {
	obj.rows = num;
	Pub.setTabbarSize();
}

function BackList(bRemove, tabId) {
	var t = window.parent.tabbar;
	if (bRemove) {
		t.removeTab(tabId, "true");
	}
	t.setTabActive("list");
}

function countDown(secs) {
	tiao.innerHTML = secs;
	if (--secs > 0) {
		setTimeout("countDown('" + secs + "')", 1000);
	} else {
		window.parent.location.reload();
	}
}
// 关闭下载的层
function onMouseClose() {
	document.getElementById('fileDiv').innerHTML = "";
	document.getElementById('fileDiv').style.display = "none";
}
// 显示下载的层
function onMouseOpen(files, path, evt) {
	document.getElementById('fileDiv').style.display = "block";
	if (isIE) {
		document.getElementById('fileDiv').style.left = event.clientX;
		document.getElementById('fileDiv').style.top = event.clientY;
	} else {
		document.getElementById('fileDiv').style.left = evt.clientX;
		document.getElementById('fileDiv').style.top = evt.clientY;
	}
	var sFileDivContent = "";
	if (files != "") {
		var sFileArr = files.split("@@@");
		for (i = 0; i < sFileArr.length; i++) {
			// 单个附件的名称和id
			if (sFileArr[i] != "") {
				sFileDivContent = sFileDivContent
						+ "<a href='../include/jsp/FileDownload.jsp?file="
						+ sFileArr[i]
						+ "&path="
						+ path
						+ "'>&nbsp;<img src='../images/icon/file.gif' align='absmiddle' border=0> "
						+ sFileArr[i] + "</a><br>";
			}
		}
	}
	sFileDivContent += "<br><br>";
	document.getElementById('fileDiv').innerHTML = sFileDivContent;
}

function selectCode(inputvar) {
	var newwin;
	var url = "../include/jsp/CodeSelect.jsp?code=" + inputvar;
	newwin = window
			.open(
					url,
					"",
					"height=360,width=400,top=400,left=400,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no, status=no");
}
/**
 * 功能：替换所有的字符为某字符 参数： strOrg 源字符串 strFind 要替换的字符 strReplace 替换成的字符 返回： 替换后的字符串
 * 调用方法： var openUsr=replaceAll(openUsr,"\r\n","");
 */
function replaceAll(strOrg, strFind, strReplace) {
	var index = 0;
	while (strOrg.indexOf(strFind, index) != -1) {
		strOrg = strOrg.replace(strFind, strReplace);
		index = strOrg.indexOf(strFind, index);
	}
	return strOrg
}
/*
 * 日期相加 type:选取类型 NumDay：间隔数 dtDate：字符串日期 Format:格式 (- 或 \)
 */
function addDate(type, NumDay, dtDate, Format) {
	var date = new Date(dtDate)
	type = parseInt(type) // 类型
	lIntval = parseInt(NumDay)// 间隔
	var num = 0;
	if (!document.all) {
		num = 1900;
	}
	switch (type) {
	case 6:// 年
		date.setYear(date.getYear() + num + lIntval)
		break;
	case 7:// 季度
		date.setMonth(date.getMonth() + (lIntval * 3))
		break;
	case 5:// 月
		date.setMonth(date.getMonth() + lIntval)
		break;
	case 4:// 天
		date.setDate(date.getDate() + lIntval)
		break
		// case 3 ://时
		// date.setHours(date.getHours() + lIntval)
		// break
		// case 2 ://分
		// date.setMinutes(date.getMinutes() + lIntval)
		// break
		// case 1 ://秒
		// date.setSeconds(date.getSeconds() + lIntval)
		// break;
	default:
	}
	if (date.getMonth() + 1 < 10) {
		if (date.getDate() < 10) {
			return date.getYear() + num + Format + '0' + (date.getMonth() + 1)
					+ Format + '0' + date.getDate();
		} else {
			return date.getYear() + num + Format + '0' + (date.getMonth() + 1)
					+ Format + date.getDate();
		}
	} else {
		if (date.getDate() < 10) {
			return date.getYear() + num + Format + (date.getMonth() + 1)
					+ Format + '0' + date.getDate();
		} else {
			return date.getYear() + num + Format + (date.getMonth() + 1)
					+ Format + date.getDate();
		}
	}
}

function getYear() {
	var myDate = new Date();
	return myDate.getFullYear();
}
function getMonth() {
	var myDate = new Date();
	return myDate.getMonth() + 1;
}
function getDate() {
	var myDate = new Date();
	return myDate.getDate();
}
function getWeekDay() {
	var myDate = new Date();
	var weekDay = myDate.getDay();
	if (weekDay == 0) {
		return "星期日";
	} else if (weekDay == 1) {
		return "星期一";
	} else if (weekDay == 2) {
		return "星期二";
	} else if (weekDay == 3) {
		return "星期三";
	} else if (weekDay == 4) {
		return "星期四";
	} else if (weekDay == 5) {
		return "星期五";
	} else if (weekDay == 6) {
		return "星期六";
	}
}

function getPageSize(obj) {
	var xScroll, yScroll;
	if (window.innerHeight && window.scrollMaxY) {
		xScroll = obj.document.body.scrollWidth;
		yScroll = obj.window.innerHeight + obj.window.scrollMaxY;
	} else if (document.body.scrollHeight > document.body.offsetHeight) { // all
		// but
		// Explorer
		// Mac
		xScroll = obj.document.body.scrollWidth;
		yScroll = obj.document.body.scrollHeight;
	} else { // Explorer Mac...would also work in Explorer 6 Strict, Mozilla
		// and Safari
		xScroll = obj.document.body.offsetWidth;
		yScroll = obj.document.body.offsetHeight;
	}

	var windowWidth, windowHeight;
	if (self.innerHeight) { // all except Explorer
		obj.windowWidth = obj.self.innerWidth;
		obj.windowHeight = obj.self.innerHeight;
	} else if (document.documentElement
			&& document.documentElement.clientHeight) { // Explorer 6 Strict
		// Mode
		windowWidth = obj.document.documentElement.clientWidth;
		windowHeight = obj.document.documentElement.clientHeight;
	} else if (document.body) { // other Explorers
		windowWidth = obj.document.body.clientWidth;
		windowHeight = obj.document.body.clientHeight;
	}

	// for small pages with total height less then height of the viewport
	if (yScroll < windowHeight) {
		pageHeight = windowHeight;
	} else {
		pageHeight = yScroll;
	}

	if (xScroll < windowWidth) {
		pageWidth = windowWidth;
	} else {
		pageWidth = xScroll;
	}

	arrayPageSize = new Array(pageWidth, pageHeight, windowWidth, windowHeight)
	return arrayPageSize;
}

// **********
// ************
// 打开新窗口
// WinUrl值为相对于main.jsp
function OpenWindow(WinObj, WinTitle, WinUrl, WinLeft, WinTop, WinWidth,
		WinHeight, WinSkin, ImagesPath) {
	if (WinTitle == null) {
		WinTitle = "窗口";
	}
	if (WinWidth == null) {
		WinWidth = 300;
	}
	if (WinHeight == null) {
		WinHeight = 200;
	}
	if (WinLeft == null) {
		WinLeft = (window.top.document.body.clientWidth - WinWidth) / 2;
	}
	if (WinTop == null) {
		WinTop = (window.top.document.body.clientHeight - WinHeight) / 2;
	}
	if (WinSkin == null) {
		WinSkin = "clear_blue";
	}
	if (ImagesPath == null) {
		ImagesPath = "../images/dhtmlxwindow/";
	}
	if (!window.top.dhxWins.isWindow(WinObj)) {
		if (window.top.dhxWins.window(WinObj)) {
			return window.top.dhxWins.window(WinObj);
		} else {
			WinObj = window.top.dhxWins.createWindow(WinObj, WinLeft, WinTop,
					WinWidth, WinHeight);
			WinObj.setText(WinTitle);
			WinObj.progressOn();
			WinObj.attachURL(WinUrl);
			WinObj.progressOff();
			return WinObj;
		}
	} else {
		return window.top.dhxWins.window(WinObj);
	}
}

function show_loader(mode) {
	var loader = document.getElementById("loading");
	var details = document.getElementById("details");
	loader.style.display = (mode ? "block" : "none");
	loader.style.left = details.offsetWidth / 2 - 150 + "px"
}
$.ajaxSetup({
	cache : false, // close AJAX cache
	contentType : "application/x-www-form-urlencoded;charset=utf-8",
	complete : function(XHR, textStatus) {
		var resText = XHR.responseText;
		// console.log(resText);
		if (resText == 'ajaxSessionTimeOut') {
			sessionTimeOut();
		}
	}
});
function sessionTimeOut() {
	$.messager.alert('操作提示', '用户登录会话已过期，请重新登录！', 'warning');
	window.location.reload();
	// setTimeout('window.top.location.href = "../../login.jsp"', 2000);
}
function DateOfBirth(age) {
	var ageMap = new Map();
	var aDate = new Date();
	// 获取当前年份（4位）
	var thisYear = aDate.getFullYear();
	// 截取（患者的出生日期的年份）
	ageYear = age.substr(0, 4);
	// 计算（年龄）
	ages = (thisYear - ageYear);
	// 判断是否未满1岁
	if (ages == 0) {
		// 未满一岁的儿童算出月份或天数
		// 获取当前月份
		var thisMonth = aDate.getMonth() + 1;
		// 获取当前几号
		var thisDate = aDate.getDate();
		thisMonths = parseInt(thisMonth);
		thisDates = parseInt(thisDate);
		// 计算当前未满一年工多少天
		var months = (thisMonths + 1) * 30 + thisDates;
		// 截取患者月份
		ageMonth = age.substr(5, 2);
		ageMonths = parseInt(ageMonth);
		// 截取患者date
		ageDate = age.substr(8, 2);
		ageDates = parseInt(ageDate);
		// 计算患者患者未满一年一共多少天
		var monthss = (ageMonths + 1) * 30 + ageDates;
		var dates = months - monthss;
		dates = parseInt(dates);
		// 判断是否满月
		if (dates / 30 < 1) {
			ageMap.put('nianling', dates);
			ageMap.put('ageUnits', '天');
			ageMap.put('ageUnit', '日');
			return ageMap;

		} else {
			var monthAge = Math.floor(dates / 30);
			ageMap.put('nianling', monthAge);
			ageMap.put('ageUnits', '月');
			ageMap.put('ageUnit', '月');
			return ageMap;
		}
	} else if (ages != 0) {
		if (ages == 1) {
			var strSeparator = "-"; // 日期分隔符
			var ysdate = aDate.getFullYear();
			var msdate = (aDate.getMonth() + 1) < 10 ? "0"
					+ (aDate.getMonth() + 1) : aDate.getMonth() + 1;
			var dsdate = (aDate.getDate() < 10) ? "0" + aDate.getDate() : aDate
					.getDate();
			var timedate = ysdate + "-" + msdate + "-" + dsdate;
			oDate1 = timedate.split(strSeparator);
			var ymdage = age.split(" ");
			oDate2 = ymdage[0].split(strSeparator);
			var strDateS = new Date(oDate1[0], oDate1[1] - 1, oDate1[2]);
			var strDateE = new Date(oDate2[0], oDate2[1] - 1, oDate2[2]);
			iDays = parseInt(Math.abs(strDateS - strDateE) / 1000 / 60 / 60
					/ 24);// 把相差的毫秒数转换为天数
			if (iDays > 30) {
				var monthsage = Math.floor(iDays / 30);
				if (monthsage > 12) {
					ageMap.put('nianling', ages);
					ageMap.put('ageUnits', '岁');
					ageMap.put('ageUnit', '年');
					return ageMap;
				} else {
					ageMap.put('nianling', monthsage);
					ageMap.put('ageUnits', '月');
					ageMap.put('ageUnit', '月');
					return ageMap;
				}
			} else {
				ageMap.put('nianling', iDays);
				ageMap.put('ageUnits', '天');
				ageMap.put('ageUnit', '日');
				return ageMap;
			}
		} else {
			ageMap.put('nianling', ages);
			ageMap.put('ageUnits', '岁');
			ageMap.put('ageUnit', '年');
			return ageMap;
		}
	}
}
//    
/**
 * 删除弹出窗口选中数据（下拉框的基础上，可以清除普通的input和easyui-textbox）可传递多个下拉框的id
 * 
 * @author hedong
 * @param deptIsforregister
 *            是否是挂号科室 1是 0否
 * @param textId
 *            页面上commbox的的id
 * @date 2016-03-22
 * @version 1.0
 */
function delSelectedData(popWinId) {
	var selArr = popWinId.split(',');
	for (var i = 0; i < selArr.length; i++) {
		if ($('#' + selArr[i]).attr("class")
				&& /combobox/ig.test($('#' + selArr[i]).attr("class"))) {
			$('#' + selArr[i]).combobox('clear');
		} else if ($('#' + selArr[i]).attr("class")
				&& /textbox/ig.test($('#' + selArr[i]).attr("class"))) {
			$('#' + selArr[i]).textbox('clear');
		} else {
			$('#' + selArr[i]).val('');
		}

	}
}

/**
 * 自动关闭alert
 * 
 * @author dutianliang
 */
var close_alert_id;
function close_alert() {
	var time = 3500;
	close_alert_id = setTimeout(function(){
		if($(".messager-body .messager-button>a").length == 1){
			$(".messager-body").window('close');
		}
	},time);
}