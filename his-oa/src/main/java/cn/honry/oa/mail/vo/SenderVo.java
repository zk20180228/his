package cn.honry.oa.mail.vo;

public class SenderVo {
    private String id;

    private String nickName;

    private String email;

    private String sender;

    private Boolean selected;

    public SenderVo(){}

    public SenderVo(String id, String nickName, String email, String sender, Boolean selected) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.sender = sender;
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

}
