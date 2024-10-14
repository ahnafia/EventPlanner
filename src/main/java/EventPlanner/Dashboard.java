package EventPlanner;


import java.util.ArrayList;

public class Dashboard {
    ArrayList<String> ClassCouncil;
    ArrayList<Event> events;

    public Dashboard() {
        this.ClassCouncil = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    public void AddEvent(Event event){
        if (event != null){
            events.add(event);
        }
    }

    public void AddAssembly(Assembly assembly){
        if (assembly != null){
            events.add(assembly);
        }
    }

    /**
     * Returns arraylist of the name : number of pending tasks for all class council members
     * @return
     */
    public ArrayList<String> AllPendingTasks(){
        ArrayList<String> tasks = new ArrayList<>(); //Creates arraylist for "name : number of pending tasks"

        for (int i = 0; i < events.size(); i++) { //Loops through every event
            ArrayList<Task> pending = events.get(i).getPendingTasks(); //Finds pending tasks for specific event
            for (int j = 0; j < pending.size(); j++) {
                tasks.add(pending.get(j).getWho() +  " - " + pending.get(j).getName());
            }
        }

        return tasks;
    }

    public String[] WorkDistribution(){
        String[] distribution = new String[6];
        int tasklength = 0;
        int name1Tasks = 0;
        int name2Tasks = 0;
        int name3Tasks = 0;
        int name4Tasks = 0;
        int name5Tasks = 0;
        int name6Tasks = 0;
        for (int i = 0; i < events.size(); i++) {
            tasklength += events.get(i).getTasks().size();
            for (int j = 0; j < events.get(i).getTasks().size(); j++) {
                if (events.get(i).tasks.get(j).getWho().equals(ClassCouncil.get(0))){
                    name1Tasks++;
                } else if (events.get(i).tasks.get(j).getWho().equals(ClassCouncil.get(1))){
                    name2Tasks++;
                } else if (events.get(i).tasks.get(j).getWho().equals(ClassCouncil.get(2))){
                    name3Tasks++;
                } else if (events.get(i).tasks.get(j).getWho().equals(ClassCouncil.get(3))){
                    name4Tasks++;
                } else if (events.get(i).tasks.get(j).getWho().equals(ClassCouncil.get(4))){
                    name5Tasks++;
                } else if (events.get(i).tasks.get(j).getWho().equals(ClassCouncil.get(5))){
                    name6Tasks++;
                }
            }
        }

        distribution[0] = ClassCouncil.get(0) + ": " +  name1Tasks + " tasks";
        distribution[1] = ClassCouncil.get(1) + ": " +  name2Tasks + " tasks";
        distribution[2] = ClassCouncil.get(2) + ": " +  name3Tasks + " tasks";
        distribution[3] = ClassCouncil.get(3) + ": " +  name4Tasks + " tasks";
        distribution[4] = ClassCouncil.get(4) + ": " +  name5Tasks + " tasks";
        distribution[5] = ClassCouncil.get(5) + ": " +  name6Tasks + " tasks";

        return distribution;
    }
}
