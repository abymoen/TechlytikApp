package model;

import java.io.Serializable;

/**
 * Created by alex on 2016-06-07.
 */
public class Monitor implements Serializable {
    private String monitorId, status;

    Monitor() {

    }

    public Monitor(String id, String status) {
        this.setStatus(status);
        this.setMonitorId(id);
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
