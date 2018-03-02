package cn.honry.oa.mail.vo;

import javax.mail.Message;
import javax.mail.Part;

import cn.honry.base.bean.model.MailAttachment;

public class MailAttachmentVo extends MailAttachment {
    private Message message;

    private Part part;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

}
