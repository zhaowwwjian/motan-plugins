package dev.bomu.motan;

public class RpcConfig {

    private Integer port;
    private String group;
    private String version;
    private Integer timeout;
    private Integer retries;
    private Integer actives;
    private String loadbalance;
    private Boolean async;
    private Boolean check;
    private String proxy;
    private String filter;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Integer getActives() {
        return actives;
    }

    public void setActives(Integer actives) {
        this.actives = actives;
    }

    public String getLoadbalance() {
        return loadbalance;
    }

    public void setLoadbalance(String loadbalance) {
        this.loadbalance = loadbalance;
    }

    public Boolean getAsync() {
        return async;
    }

    public void setAsync(Boolean async) {
        this.async = async;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public RpcConfig(Integer port, String group, String version, Integer timeout, Integer retries, Integer actives, String loadbalance, Boolean async, Boolean check, String proxy, String filter) {
        this.port = port;
        this.group = group;
        this.version = version;
        this.timeout = timeout;
        this.retries = retries;
        this.actives = actives;
        this.loadbalance = loadbalance;
        this.async = async;
        this.check = check;
        this.proxy = proxy;
        this.filter = filter;
    }

    public static RpcConfig.RpcConfigBuilder builder() {
        return new RpcConfig.RpcConfigBuilder();
    }





    public static class RpcConfigBuilder{

        private Integer port;
        private String group;
        private String version;
        private Integer timeout;
        private Integer retries;
        private Integer actives;
        private String loadbalance;
        private Boolean async;
        private Boolean check;
        private String proxy;
        private String filter;

        RpcConfigBuilder(){

        }

        public RpcConfig.RpcConfigBuilder port(Integer port){
            this.port = port;
            return this;
        }

        public RpcConfig.RpcConfigBuilder group(String group){
            this.group = group;
            return this;
        }

        public RpcConfig.RpcConfigBuilder version(String version){
            this.version = version;
            return this;
        }

        public RpcConfig.RpcConfigBuilder timeout(Integer timeout){
            this.timeout = timeout;
            return this;
        }

        public RpcConfig.RpcConfigBuilder retries(Integer retries){
            this.retries = retries;
            return this;
        }

        public RpcConfig.RpcConfigBuilder actives(Integer actives){
            this.actives = actives;
            return this;
        }

        public RpcConfig.RpcConfigBuilder loadbalance(String loadbalance){
            this.loadbalance = loadbalance;
            return this;
        }

        public RpcConfig.RpcConfigBuilder async(Boolean async){
            this.async = async;
            return this;
        }

        public RpcConfig.RpcConfigBuilder check(Boolean check){
            this.check = check;
            return this;
        }

        public RpcConfig.RpcConfigBuilder proxy(String proxy){
            this.proxy = proxy;
            return this;
        }

        public RpcConfig.RpcConfigBuilder filter(String filter){
            this.filter = filter;
            return this;
        }

        public RpcConfig build() {
            return new RpcConfig(this.port, this.group, this.version,this.timeout,this.retries,this.actives,this.loadbalance,this.async,this.check,this.proxy,this.filter);
        }
    }
}
