
//First Home Start
//草药查询
function adHerbMedicineIn(){
	var qq = $('#ttta').tabs('getSelected');				
	var tab = qq.panel('options');
	if(tab.title=='长期医嘱'){
		if($('#longDocAdvType').combobox('getValue')==''){
			$.messager.alert('提示',"请先选择医嘱类型!");
			return;
		}
	}else if(tab.title=='临时医嘱'){
		if($('#temDocAdvType').combobox('getValue')==''){
			$.messager.alert('提示',"请先选择医嘱类型!");
			return;
		}
	}
	AdddilogModel("chinMediModleDivId","草药信息",basePath+"inpatient/docAdvManage/getChinMediModleNew.action",'80%','50%');
}

//Sixth Other End