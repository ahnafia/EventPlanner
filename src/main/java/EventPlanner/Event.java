package EventPlanner;

import java.util.ArrayList;

public class Event {
    String eventName;
    String date;
    ArrayList<Task> tasks;
    Analysis analytics;
    String eventType;
    String description;
    String location;

    public Event(String eventName, String date, String description, String location) {
        this.eventName = eventName;
        this.date = date;
        this.description = description;
        this.location = location;
        this.tasks = new ArrayList<>();
        this.eventType = "GradeEvent";
        analytics = new Analysis();
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Creates an arraylist which stores all the
     * uncompleted tasks in the event
     * @returns arraylist with all pending tasks for this event
     */
    public ArrayList<Task> getPendingTasks(){
        ArrayList<Task> pending = new ArrayList<>(); //Creats arraylist for all the pending tasks
        for (int i = 0; i < tasks.size(); i++) {
            if (!tasks.get(i).getStatus()){
                pending.add(tasks.get(i));
            } //Loops through all tasks to find any uncompleted ones
        }

        return pending;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public Analysis getAnalytics() {
        return analytics;
    }

    public void setAnalytics(Analysis analytics) {
        this.analytics = analytics;
    }

    public String GetOverworkedString(){
        ArrayList<Object> membersAndNum = new ArrayList<>();//Creates arraylist holding a member and their number of tasks
        int numOfTasks;
        for (int i = 0; i < tasks.size(); i++) {
            numOfTasks = 0;
            for (int j = 0; j < tasks.size(); j++) {
                if (tasks.get(j).getWho().equals(tasks.get(i).getWho())){
                    numOfTasks++;
                }
            }//Adds the number of tasks for specific member
            membersAndNum.add(tasks.get(i).getWho());//adds name of member
            membersAndNum.add(numOfTasks);//adds number of tasks next to them
        }

        int max = 0;
        int index = 0;
        for (int i = 0; i < membersAndNum.size(); i++) {
            if (i % 2 != 0){
                if (max < (Integer)membersAndNum.get(i)){
                    max = (Integer)membersAndNum.get(i);
                    index = i;
                }
            }
        }//Find max algorithm

        return membersAndNum.get(index - 1).toString();//returns the name of the member with most tasks
    }

    public String GetUnderworkedString(){
        ArrayList<Object> membersAndNum = new ArrayList<>();
        int numOfTasks;
        for (int i = 0; i < tasks.size(); i++) {
            numOfTasks = 0;
            for (int j = 0; j < tasks.size(); j++) {
                if (tasks.get(j).getWho().equals(tasks.get(i).getWho())){
                    numOfTasks++;
                }
            }
            membersAndNum.add(tasks.get(i).getWho());
            membersAndNum.add(numOfTasks);
        }

        int max = 0;
        int index = 0;
        for (int i = 0; i < membersAndNum.size(); i++) {
            if (i % 2 != 0){
                if (max > (Integer)membersAndNum.get(i)){
                    max = (Integer)membersAndNum.get(i);
                    index = i;
                }
            }
        }

        return membersAndNum.get(index - 1).toString();
    }

    public String GetTasksFrom(String name){
        String tasksFromMember = "";
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getWho().endsWith(name)){
                tasksFromMember += ("," + tasks.get(i).getName());
            }
        }
        return tasksFromMember;
    }

    public void EditTaskStatus(int index, boolean status){
        tasks.get(index).setStatus(status);
    }
}
