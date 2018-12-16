package amber_team.amber.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public enum Status {
    @JsonProperty("Opened")
    OPENED("Opened"),
    @JsonProperty("In progress")
    IN_PROGRESS("In progress"),
    @JsonProperty("On reviewing")
    ON_REVIEWING("On reviewing"),
    @JsonProperty("Canceled")
    CANCELED("Canceled"),
    @JsonProperty("Rejected")
    REJECTED("Rejected"),
    @JsonProperty("Completed")
    COMPLETED("Completed"),
    @JsonProperty("On hold")
    ON_HOLD("On hold"),
    @JsonProperty("Delivering")
    DELIVERING("Delivering"),
    @JsonProperty("Waiting for equipment")
    WAITING_FOR_EQUIPMENT("Waiting for equipment"),
    @JsonProperty("Waiting for replenishment")
    WAITING_FOR_REPLENISHMENT("Waiting for replenishment");

    private static Map<String, Status> map = null;
    private final String name;

    private Status(String s) {
        name = s;
    }

    public static Status valueOfStatus(String name) {
        if (map == null) {
            map = new HashMap<>();
            for (Status v : Status.values()) {
                map.put(v.name, v);
            }
        }
        Status result = map.get(name);
        if (result == null) {
            throw new IllegalArgumentException(
                    "No enum const " + Status.class + "@status." + name);
        }

        return result;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}