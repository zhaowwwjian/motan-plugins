package dev.bomu.motan;

public enum RegistryType {

    REGISTRY_CONSUL("consul"),
    REGISTRY_ZOOKEEPER("zookeeper"),
    REGISTRY_LOCAL("local");

    private String value;

    RegistryType(String value) {
        this.value = value;

    }
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
