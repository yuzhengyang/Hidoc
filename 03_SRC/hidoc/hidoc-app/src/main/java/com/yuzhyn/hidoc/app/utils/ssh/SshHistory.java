package com.yuzhyn.hidoc.app.utils.ssh;

public class SshHistory {
    private boolean isSend;
    private String content;

    public SshHistory(boolean isSend, String content) {
        this.setSend(isSend);
        this.setContent(content);
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
