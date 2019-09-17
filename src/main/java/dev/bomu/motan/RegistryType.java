package dev.bomu.motan;

public enum RegistryType {

    REGISTRY_CONSUL("consul"),
    REGISTRY_ZOOKEEPER("zookeeper"),
    REGISTRY_LOCAL("local");

    private String value;



    // 构造方法
    private RegistryType(String value) {
        this.value = value;

    }
    // 普通方法
    public static String getName(String value) {
        for (RegistryType c : RegistryType.values()) {
            if (c.getValue().equalsIgnoreCase(value)) {
                return c.value;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
