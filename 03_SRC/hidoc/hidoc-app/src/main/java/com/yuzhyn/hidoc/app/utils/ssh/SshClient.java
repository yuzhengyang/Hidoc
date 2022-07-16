package com.yuzhyn.hidoc.app.utils.ssh;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.yuzhyn.azylee.core.datas.collections.MapTool;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class SshClient {

    private LocalDateTime createTime;

    private Map<String, String> attach;

    private JSch jSch;

    private Session session;

    private Channel channel;

    private List<SshHistory> history;

    /**
     * 获取附属信息：名称（固定扩展方法）
     * @return
     */
    public String getName() {
        return MapTool.get(attach, "name", "");
    }

    //region getter and setter
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Map<String, String> getAttach() {
        return attach;
    }

    public void setAttach(Map<String, String> attach) {
        this.attach = attach;
    }

    public JSch getjSch() {
        return jSch;
    }

    public void setjSch(JSch jSch) {
        this.jSch = jSch;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public List<SshHistory> getHistory() {
        return history;
    }

    public void setHistory(List<SshHistory> history) {
        this.history = history;
    }
//endregion
}
