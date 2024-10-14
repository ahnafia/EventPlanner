package EventPlanner;

import java.util.ArrayList;

public class Assembly extends Event{
    ArrayList<Activity> timeslots;
    int timescale;

    public Assembly(String eventName, String date) {
        super(eventName, date, null, null);
        this.eventType = "Assembly";
        timeslots = new ArrayList<>();
    }

    public String addToTimeSlot(Activity activity){
        if (TimeLimitReached(activity.getTime())){
            return "There is not enough time";
        } else {
            timeslots.add(activity);
            return "Succesfully added";
        }
    }

    public boolean TimeLimitReached(int time){
        double accum = 0;
        for (int i = 0; i < timeslots.size(); i++) {
            accum += timeslots.get(i).getTime();
        }

        if ((accum + time) == timescale){
            return true;
        } else {
            return false;
        }
    }

    public String deleteActivity(int index){
        timeslots.remove(index);
        return "Removed";
    }
}
