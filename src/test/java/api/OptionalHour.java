package api;

public class OptionalHour {
    private int id;
    private String hour;

    public OptionalHour() {
    }

    public OptionalHour(int id, String hour) {
        this.id = id;
        this.hour = hour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
