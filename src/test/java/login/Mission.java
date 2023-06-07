package login;

public class Mission {
    private int id;
    private String title;
    private String description;
    private String type;
    private int length;

    public Mission(int id, String title, String description, String type, int length) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.length = length;
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
}
