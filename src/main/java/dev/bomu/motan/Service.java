package dev.bomu.motan;

public class Service {

    private Class clazz;
    private RpcConfig rpcConfig;

    public Service(Class clazz, RpcConfig rpcConfig) {
        this.clazz = clazz;
        this.rpcConfig = rpcConfig;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public RpcConfig getRpcConfig() {
        return rpcConfig;
    }

    public void setRpcConfig(RpcConfig rpcConfig) {
        this.rpcConfig = rpcConfig;
    }
}
