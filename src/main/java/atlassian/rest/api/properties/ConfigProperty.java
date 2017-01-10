package atlassian.rest.api.properties;

public enum ConfigProperty {
    USERNAME(0),
    PASSWORD(1),
    BASE_URL(2),
    PAGE_ID(3),
    FILE_PATH(4);

    private final int id;

    ConfigProperty(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ConfigProperty findById(int id) {
        for (ConfigProperty property : values()) {
            if (property.getId() == id) {
                return property;
            }
        }
        throw new RuntimeException("Unsupported parameter is passed");
    }
}