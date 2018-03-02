package cn.honry.oa.activiti.bpm.form.vo;

import java.util.HashMap;
import java.util.Map;

public class ButtonVoHelper {

	private Map<String, ButtonVo> map = new HashMap<String, ButtonVo>();

    public ButtonVoHelper() {
        this.addButton("saveDraft", "保存草稿");
        this.addButton("taskConf", "配置任务");
        this.addButton("confirmStartProcess", "提交申请");
        this.addButton("startProcess", "发起流程");
        this.addButton("completeTask", "完成");

        this.addButton("selectAssignee","指定下一环节负责人");
        this.addButton("claimTask", "认领任务");
        this.addButton("releaseTask", "释放任务");
        this.addButton("transfer", "转办");
        this.addButton("rollback", "退回");
        this.addButton("rollbackPrevious", "退回（上一步）");
        this.addButton("rollbackAssignee", "退回（指定负责人）");
        this.addButton("rollbackActivity", "退回（指定步骤）");
        this.addButton("rollbackActivityAssignee", "退回（指定步骤，指定负责人）");
        this.addButton("rollbackStart", "退回（开始节点）");
        this.addButton("rollbackInitiator", "退回（发起人）");
        this.addButton("delegateTask", "协办");
        this.addButton("delegateTaskCreate", "协办（链式）");
        this.addButton("resolveTask", "还回");
        this.addButton("endProcess", "终止流程");
        this.addButton("suspendProcess", "暂停流程");
        this.addButton("resumeProcess", "恢复流程");
        this.addButton("viewHistory", "查看流程状态");
        this.addButton("addCounterSign", "加签");
        this.addButton("jump", "自由跳转");
        this.addButton("reminder", "催办");
        this.addButton("withdraw", "撤销");

        this.addButton("communicate", "沟通");
        this.addButton("callback", "反馈");
    }

    public void addButton(String name, String label) {
        this.addButton(new ButtonVo(name, label));
    }

    public void addButton(ButtonVo buttonDto) {
        this.map.put(buttonDto.getName(), buttonDto);
    }

    public ButtonVo findButton(String name) {
        ButtonVo buttonDto = map.get(name);
        return buttonDto;
    }

    public Map<String, ButtonVo> getMap() {
        return this.map;
    }
}
