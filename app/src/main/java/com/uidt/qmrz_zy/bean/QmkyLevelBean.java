package com.uidt.qmrz_zy.bean;

import java.io.Serializable;

/**
 * @author yijixin on 2020-02-13
 * 获取通行级别
 */
public class QmkyLevelBean implements Serializable {


    /**
     * data : {"passstatus":1,"safelevel":1}
     * msg : success
     * status : 10200
     * timestamp : 1581561148761
     * timestr : 2020-02-13 10:32:28
     */

    private DataBean data;
    private String msg;
    private int status;
    private String timestamp;
    private String timestr;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestr() {
        return timestr;
    }

    public void setTimestr(String timestr) {
        this.timestr = timestr;
    }

    public static class DataBean implements Serializable{
        /**
         * passstatus : 1 //0:不放行  1:放行
         * safelevel : 1 //0:未知  1:高风险 2:较高风险  3:较低风险  4:低风险   20:白名单
         */

        private int passstatus;
        private int safelevel;

        public int getPassstatus() {
            return passstatus;
        }

        public void setPassstatus(int passstatus) {
            this.passstatus = passstatus;
        }

        public int getSafelevel() {
            return safelevel;
        }

        public void setSafelevel(int safelevel) {
            this.safelevel = safelevel;
        }
    }
}
