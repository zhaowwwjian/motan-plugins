package dev.bomu.motan;

import com.jfinal.aop.Aop;
import com.jfinal.plugin.IPlugin;
import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.config.AbstractInterfaceConfig;
import com.weibo.api.motan.config.ProtocolConfig;
import com.weibo.api.motan.config.RegistryConfig;
import com.weibo.api.motan.config.ServiceConfig;
import com.weibo.api.motan.util.MotanSwitcherUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MotanPlugin implements IPlugin {

    private RegistryConfig registryConfig = new RegistryConfig();
    private ProtocolConfig protocolConfig = new ProtocolConfig();
    protected List<Service> classList = new ArrayList<Service>();
    private String address;
    private RegistryType registryType;
    private String id="motan";
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
                throw new RuntimeException(String.format("class[%s] has no interface", service.getClazz()));
            }
            // TODO: 2019-09-20  添加例外的接口
            Class[] excludes = new Class[]{
                    Serializable.class
            };
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
        // TODO: 2019-09-17 销毁删除
        return true;
    }
}
