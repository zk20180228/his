/**
 * 分页js
 */
function pageFunction(formId,pageIndex,pageSize,totalPageNum){
	var _form=$("#"+formId);
	var _base=$("base").attr("href");
	if(!pageIndex){
		pageIndex=$("#pageCousmeId").val();
		if(pageIndex==""||pageIndex==null){
			pageIndex=1;
			$("#pageCousmeId").val(1);
		}
		if(!isPositiveInteger(pageIndex)){
			alert("请输入正确的正整数!");
			return ;
		}
		if(pageIndex<=1){
			pageIndex=1;
			$("#pageCousmeId").val(1);
		}
		if(pageIndex>totalPageNum){
			pageIndex = totalPageNum;
		}
	}
	if(_form.serialize())
		window.location.href=_base+_form.attr("action")+"?"+_form.serialize()+"&page.pageIndex="+pageIndex+"&page.pageSize="+pageSize;
	else
		window.location.href=_base+_form.attr("action")+"?page.pageIndex="+pageIndex+"&page.pageSize="+pageSize;
}

//是否为正整数
function isPositiveInteger(num){
	var _reg=/^\d+$/;
	if(!_reg.test(num)){
		return false;
	}
	return true;
}