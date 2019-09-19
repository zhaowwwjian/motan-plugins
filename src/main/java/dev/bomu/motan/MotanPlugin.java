package dev.bomu.motan;

import com.jfinal.aop.Aop;
import com.jfinal.plugin.IPlugin;
import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.config.AbstractInterfaceConfig;
import com.weibo.api.motan.config.ProtocolConfig;
import com.weibo.api.motan.config.RegistryConfig;
import com.weibo.api.motan.config.ServiceConfig;
import com.weibo.api.motan.util.MotanSwitcherUtil;

import java.util.ArrayList;
import java.util.List;

public class MotanPlugin implements IPlugin {

    //注册中心
    private RegistryConfig registryConfig = new RegistryConfig();
    //协议
    private ProtocolConfig protocolConfig = new ProtocolConfig();
    //接口
    protected List<Service> classList = new ArrayList<Service>();

    //注册中心地址，支持多个ip+port，格式：ip1:port1,ip2:port2,ip3，如果没有port，则使用默认的port
    private String address="127.0.0.1";
    //注册中心类型 有consul和zookeeper 或者local
    private RegistryType registryType = RegistryType.REGISTRY_CONSUL;

    //协议id 默认motan
    private String id="motan";
    //注册配置名称 默认motan
    private String name="motan";

    public MotanPlugin(String address,RegistryType registryType){
        this.address = address;
        this.registryType = registryType;
    }

    private void init(){
        protocolConfig.setId(id);
        protocolConfig.setName(name);
        registryConfig.setRegProtocol(registryType.getValue());
        registryConfig.setAddress(address);
    }

    private <T> void registry(Class<T>interfaceClass, Object object, RpcConfig config){

        MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, false);
        ServiceConfig<T> motanServiceConfig = new ServiceConfig<T>();
        motanServiceConfig.setRegistry(registryConfig);
        motanServiceConfig.setProtocol(protocolConfig);

        // 设置接口及实现类以及实现接口
        motanServiceConfig.setInterface(interfaceClass);
        motanServiceConfig.setRef((T) object);
        motanServiceConfig.setExport(String.format(id+":%s", config.getPort()));

        initInterface(motanServiceConfig, config);

        motanServiceConfig.export();

        MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, true);
    }

    private static void initInterface(AbstractInterfaceConfig interfaceConfig, RpcConfig config) {

        interfaceConfig.setGroup(config.getGroup());
        interfaceConfig.setVersion(config.getVersion());
        interfaceConfig.setRequestTimeout(config.getTimeout());
        interfaceConfig.setProxy(config.getProxy());
        interfaceConfig.setFilter(config.getFilter());

        if (config.getActives() != null) {
            interfaceConfig.setActives(config.getActives());
        }

        if (config.getAsync() != null) {
            interfaceConfig.setAsync(config.getAsync());
        }

        if (config.getRetries() != null) {
            interfaceConfig.setRetries(config.getRetries());
        }

        if (config.getCheck() != null) {
            interfaceConfig.setCheck(config.getCheck().toString());
        }


    }

    public void addService(Class clazz, RpcConfig rpcConfig){
        classList.add(new Service(clazz,rpcConfig));
    }


    public boolean start() {
        init();
        for(Service service : classList){
            Class[] inters = service.getClazz().getInterfaces();
            if (inters == null || inters.length == 0) {
                throw new RuntimeException(String.format("class[%s] has no interface", service.getRpcConfig()));
            }

            //对某些系统的类 进行排除，例如：Serializable 等
            Class[] excludes = new Class[0];
            for (Class inter : inters) {
                boolean isContinue = false;
                for (Class ex : excludes) {
                    if (ex.isAssignableFrom(inter)) {
                        isContinue = true;
                        break;
                    }
                }

                if (isContinue) {
                    continue;
                }

                registry(inter,Aop.get(service.getClazz()), service.getRpcConfig());
            }
        }
        return true;
    }

    public boolean stop() {
        // TODO: 2019-09-17
        return true;
    }
}
