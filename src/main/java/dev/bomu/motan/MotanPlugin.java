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
    protected List<Class> classList = new ArrayList<Class>();
    //启动时检查注册中心是否存在
    private boolean check = false;
    //注册中心地址，支持多个ip+port，格式：ip1:port1,ip2:port2,ip3，如果没有port，则使用默认的port
    private String address;
    //注册中心类型 有consul和zookeeper 或者local
    private String regProtocol = "consul";
    //注册配置名称
    private String name;
    //协议id 不确定用处
    private String id;
    //filter, 多个filter用","分割，blank string 表示采用默认的filter配置
    private String filter;
    //序列化方式
    private String serialization;
    //当有多个IP的时候可以指定某个IP
    private String hosts;
    private void init(){
        registryConfig.setCheck(String.valueOf(check));
        registryConfig.setAddress(address);
        registryConfig.setRegProtocol(regProtocol);
        protocolConfig.setName(name);
        protocolConfig.setId(id);
        protocolConfig.setFilter(filter);
        protocolConfig.setSerialization(serialization);

    }

    private <T> void registry(Class<T>interfaceClass,Object object,RpcConfig config){

        MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, false);
        ServiceConfig<T> motanServiceConfig = new ServiceConfig<T>();
        motanServiceConfig.setRegistry(registryConfig);
        motanServiceConfig.setProtocol(protocolConfig);

        // 设置接口及实现类
        motanServiceConfig.setInterface(interfaceClass);
        motanServiceConfig.setRef((T) object);
        motanServiceConfig.setHost(hosts);

        motanServiceConfig.setShareChannel(true);
        motanServiceConfig.setExport(String.format("motan:%s", config.getPort()));
        motanServiceConfig.setCheck(String.valueOf(check));

        initInterface(motanServiceConfig, config);

        motanServiceConfig.export();

        MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, true);
    }

    private static void initInterface(AbstractInterfaceConfig interfaceConfig, RpcConfig config) {

        interfaceConfig.setGroup(config.getGroup());
        interfaceConfig.setVersion(config.getVersion());
        interfaceConfig.setRequestTimeout(config.getTimeout());


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

        interfaceConfig.setProxy(config.getProxy());
        interfaceConfig.setFilter(config.getFilter());
    }

    public void addService(Class clazz){
        classList.add(clazz);
    }


    public boolean start() {
        init();
        for(Class clazz : classList){
            Class[] inters = clazz.getInterfaces();
            if (inters == null || inters.length == 0) {
                throw new RuntimeException(String.format("class[%s] has no interface", clazz));
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

                registry(inter,Aop.get(clazz),new RpcConfig());
            }
        }


        return true;
    }

    public boolean stop() {
        return true;
    }
}
