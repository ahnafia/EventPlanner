package EventPlanner;

import java.util.ArrayList;

public class Analysis {
    ArrayList<Double> satisfation;
    ArrayList<String> feedback;

    public Analysis() {
        this.satisfation = new ArrayList<>();
        this.feedback = new ArrayList<>();
    }

    public double getAverageSatisfaction(){
        double accum = 0;
        for (int i = 0; i < satisfation.size(); i++) {
            accum += satisfation.get(i);
        }
        return accum / satisfation.size();
    }

    public String getFeedback(){
        String allFeedback = "";
        for (int i = 0; i < feedback.size(); i++) {
            allFeedback = feedback.get(i) + "\n";
        }

        return allFeedback;
    }
}
