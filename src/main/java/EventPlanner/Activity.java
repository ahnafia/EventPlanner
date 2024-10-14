package EventPlanner;

public class Activity{
    String name;
    String category;
    String resources;
    int time;
    int assembly_id;

    public Activity(String name, String category, String resources, int time, int assembly_id) {
        this.name = name;
        this.category = category;
        this.resources = resources;
        this.time = time;
        this.assembly_id = assembly_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
