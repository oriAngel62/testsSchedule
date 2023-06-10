package api;

import java.util.List;
import java.util.Map;

public class Mission {
    private int id;
    private String title;
    private String description;
    private String type;
    private int length;
    private List<OptionalDay> optionalDays;
    private List<OptionalHour> optionalHours;
    private String deadLine;
    private int priority;
    private Map<String, Object> setting;

    public Mission() {
    }

    public Mission(String title, String description, String type, int length,
                   List<OptionalDay> optionalDays, List<OptionalHour> optionalHours,
                   String deadLine, int priority, Map<String, Object> setting) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.length = length;
        this.optionalDays = optionalDays;
        this.optionalHours = optionalHours;
        this.deadLine = deadLine;
        this.priority = priority;
        this.setting = setting;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<OptionalDay> getOptionalDays() {
        return optionalDays;
    }

    public void setOptionalDays(List<OptionalDay> optionalDays) {
        this.optionalDays = optionalDays;
    }

    public List<OptionalHour> getOptionalHours() {
        return optionalHours;
    }

    public void setOptionalHours(List<OptionalHour> optionalHours) {
        this.optionalHours = optionalHours;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Map<String, Object> getSetting() {
        return setting;
    }

    public void setSetting(Map<String, Object> setting) {
        this.setting = setting;
    }


}
