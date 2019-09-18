package dev.bomu.motan;

import com.weibo.api.motan.config.ProtocolConfig;
import com.weibo.api.motan.config.RefererConfig;
import com.weibo.api.motan.config.RegistryConfig;

public class MotanClient {

    public static <T>  T test(Class<T> clazz,String group,String version,Integer timeout,RegistryConfig registryConfig){
        RefererConfig<T> motanDemoServiceReferer = new RefererConfig<T>();

        // 设置接口及实现类
        motanDemoServiceReferer.setInterface(clazz);

        // 配置服务的group以及版本号
        motanDemoServiceReferer.setGroup("user-info");
        motanDemoServiceReferer.setVersion("1.0");
        motanDemoServiceReferer.setRequestTimeout(1000);

        motanDemoServiceReferer.setRegistry(registryConfig);

        // 配置RPC协议
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setId("motan");
        protocol.setName("motan");
        motanDemoServiceReferer.setProtocol(protocol);

        // 使用服务
        return motanDemoServiceReferer.getRef();
    }
}
