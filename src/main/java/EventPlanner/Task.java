package EventPlanner;
import javafx.scene.control.Button;

public class Task {
    String name;
    String who;
    Boolean status;
    int event_id;

    public Task(String name, String who, Boolean status, int event_id) {
        this.name = name;
        this.who = who;
        this.status = status;
        this.event_id = event_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
