package com.ernest.tcp.remote.pojo;


public class Clients {
    private String ip;
    private String name;
    private Boolean state;
    
    
    public Clients(String ip, String name, Boolean state) {
        this.ip = ip;
        this.name = name;
        this.state = state;
    }
    
    public String getInfos() {
        return ip + "-" + name;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Boolean getState() {
        return state;
    }
    
    public void setState(Boolean state) {
        this.state = state;
    }
    
    
}
