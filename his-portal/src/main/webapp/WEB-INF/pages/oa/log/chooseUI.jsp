<%--
  DESC:
  User: zhangkui
  Date: 2018/1/5
  Time: 14:27
--%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <%@ include file="/common/metas.jsp"%>
    <title>维度选择</title>
</head>
<body>

<div id="tt" class="easyui-tabs" style="width:100%;height:100%;">
    <div title="流程分类" style="height: 96%;width: 100%;" data-options="closable:false">
            <div class="easyui-layout" data-options="fit:true" >
                    <div data-options="region:'north'" style="height:7%;width:100%;border-top:0;border-left:0;border-right:0;">
                        <table style="width:100%;padding: 0px 5px 0px 5px;height: 40px;">
                            <tr>
                                <td nowrap="nowrap">流程名字:
                                    <input id="process" class="easyui-textbox" data-options="prompt:'请输入流程名字'" style="width:240px;height: 27px;">
                                    <a href="javascript:void(0)"  onclick="searchData()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                                    <a href="javascript:void(0)"  onclick="delData()" class="easyui-linkbutton" iconCls="icon-remove">清空数据</a>
                                </td>
                            </tr>
                        </table>
                    </div>

                    <div data-options="region:'center',border: false" style="height:92%;width:100%;">
                        <table id="processList" class="easyui-datagrid" data-options="pagination: true">
                            <thead>
                            <tr>
                                <th data-options="field:'id',checkbox:true,align:'left'" ></th>
                                <th data-options="field:'processName',align:'left'"  style="width: 100px;" >名称</th>
                                <th data-options="field:'name',hidden:true" ></th>
                                <th data-options="field:'remark',align:'left'"  style="width: 100px;" >备注</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
            </div>
    </div>
    <div title="员工列表" data-options="closable:false" style="height: 96%;width: 100%;">
        <div class="easyui-layout" data-options="fit:true" >
            <div data-options="region:'north'" style="height:7%;width:100%;border-top:0;border-left:0;border-right:0;">
                <table style="width:100%;padding: 0px 5px 0px 5px;height: 40px;">
                    <tr>
                        <td nowrap="nowrap">
                            员工号(姓名): <input id="emp" class="easyui-textbox" data-options="prompt:'请输入员工号或姓名'" style="width:240px;height: 27px;">
                            <a href="javascript:void(0)"  onclick="searchData()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                            <a href="javascript:void(0)"  onclick="delData()" class="easyui-linkbutton" iconCls="icon-remove">清空数据</a>
                        </td>
                    </tr>
                </table>
            </div>

            <div data-options="region:'center',border: false" style="height:92%;width:100%;">
                <table id="empList" class="easyui-datagrid" data-options="pagination: true">
                    <thead>
                    <tr>
                        <th data-options="field:'id',checkbox:true,align:'left'" ></th>
                        <th data-options="field:'jobNo',align:'left'" >员工号</th>
                        <th data-options="field:'name',align:'left'"  style="width: 100px;" >员工名称</th>
                        <th data-options="field:'deptName',align:'left'"  style="width: 100px;" >所在科室</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
    <div title="科室列表" data-options="closable:false" style="height: 96%;width: 100%;">
        <div class="easyui-layout" data-options="fit:true" >
            <div data-options="region:'north'" style="height:7%;width:100%;border-top:0;border-left:0;border-right:0;">
                <table style="width:100%;padding: 0px 5px 0px 5px;height: 40px;">
                    <tr>
                        <td nowrap="nowrap">
                            科室编号(名字): <input id="dept" class="easyui-textbox" data-options="prompt:'请输入科室编号或名字'" style="width:240px;height: 27px;">
                            <a href="javascript:void(0)"  onclick="searchData()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                            <a href="javascript:void(0)"  onclick="delData()" class="easyui-linkbutton" iconCls="icon-remove">清空数据</a>
                        </td>
                    </tr>
                </table>
            </div>

            <div data-options="region:'center',border: false" style="height:92%;width:100%;">
                <table id="deptList" class="easyui-datagrid" data-options="pagination: true">
                    <thead>
                    <tr>
                        <th data-options="field:'id',checkbox:true,align:'left'" ></th>
                        <th data-options="field:'deptCode',align:'left'" >科室编号</th>
                        <th data-options="field:'deptName',align:'left'"  style="width: 100px;" >科室名</th>
                        <th data-options="field:'name',hidden:true" ></th>
                        <th data-options="field:'areaName',align:'left'"  style="width: 100px;" >所在院区</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>




</body>
</html>
<script type="text/javascript">

    $(function(){

        $('#tt').tabs({
            onSelect:function(title,index){
                loadData(index);

            }
        });
        //初始化
        loadData(0);
    })


    //加载表格
    function loadData(index){
        var $id='';
        var url='';
        if(index==1){
            $id="empList";
            url="${pageContext.request.contextPath}/oa/log/findEmpList.action";
        }else if(index==2){
            $id="deptList";
            url="${pageContext.request.contextPath}/oa/log/findDeptList.action";
        }else if(index==0){
            $id="processList";
            url="${pageContext.request.contextPath}/oa/log/findProcessList.action";
        }


        $("#"+$id).datagrid({
            method: 'post',
            url:url,
            fit: true,
            remoteSort: false,
            pagination: true,
            pageSize: 20,
            idField:'id',
            fitColumns:true,
            rownumbers:true,
            pageList: [20, 30, 50, 100],
            onLoadSuccess: function(data) {
                //分页工具栏作用提示
                var pager = $(this).datagrid('getPager');
                var aArr = $(pager).find('a');
                var iArr = $(pager).find('input');
                $(iArr[0]).tooltip({
                    content: '回车跳转',
                    showEvent: 'focus',
                    hideEvent: 'blur',
                    hideDelay: 1
                });
                for(var i = 0; i < aArr.length; i++) {
                    $(aArr[i]).tooltip({
                        content: toolArr[i],
                        hideDelay: 1
                    });
                    $(aArr[i]).tooltip('hide');
                }

                if("empList"==$id){
                    //删除另外两个datagrid列表中被选择的项
                    $("#deptList").datagrid('clearSelections');
                    $("#processList").datagrid('clearSelections');
                }

                if("deptList"==$id){
                    //删除另外两个datagrid列表中被选择的项
                    $("#empList").datagrid('clearSelections');
                    $("#processList").datagrid('clearSelections');
                }
                if("processList"==$id){
                    //删除另外两个datagrid列表中被选择的项
                    $("#deptList").datagrid('clearSelections');
                    $("#empList").datagrid('clearSelections');
                }

            }
        });
    }

    //查询
    function searchData(){

        var tab = $('#tt').tabs('getSelected');
        var index = $('#tt').tabs('getTabIndex',tab);
        var $id='';
        var data='';
        if(index==1){
            $id="empList";
            data=$('#emp').textbox('getText');
        }else if(index==2){
            $id="deptList";
            data=$('#dept').textbox('getText');
        }else if(index==0){
            $id="processList";
            data=$('#process').textbox('getText');
        }

        $('#'+$id).datagrid('load', {
            eName:data,
            jobNo:data,
            dName:data,
            deptCode:data,
            pName:data
        });

    }

    //删除数据
    function delData(){

        var tab = $('#tt').tabs('getSelected');
        var index = $('#tt').tabs('getTabIndex', tab);
        var $id = '';
        if (index == 1) {
            $id = "empList";
        } else if (index == 2) {
            $id = "deptList";
        } else if (index == 0) {
            $id = "processList";
        }

        //获取列表中被选中的行
        var ids = $('#' + $id).datagrid('getChecked');
        var condition = '';
        var t_value='';//类型值
        if (ids != null && ids != undefined && ids.length > 0) {
            for (var i = 0; i < ids.length; i++) {
                if (i == (ids.length - 1)) {
                    condition += ids[i].id;
                    t_value += ids[i].name;
                } else {
                    condition += ids[i].id + ",";
                    t_value += ids[i].name + ",";
                }
            }
        } else {
            $.messager.alert('提示', '请至少选择一条数据！');
            return;
        }
        $.messager.confirm('确认','您确认想要清空数据吗？',function(r){
            if (r){

                $.ajax({
                    type:"post",
                    url:"${pageContext.request.contextPath}/oa/log/updateTaskInfo.action",
                    data:"flag="+(index+1)+"&condition="+condition+"&t_value="+t_value,
                    success:function (backData) {
                        if(backData.data=="true"){
                            $.messager.alert('提示','删除成功！');
                            //清除所选择的行
                            $('#'+$id).datagrid('clearSelections');
                            //刷新父列表
                            window.parent.searchList();
                            //关闭当前窗口
                            window.parent.$("#win").panel("close");

                        }else{
                            $.messager.alert('提示','删除失败！');
                        }
                    }
                });

            }
        });



    }




</script>
