package src;

import java.security.InvalidParameterException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
/**
 * Utility class for planning the rally.
 */
public class RallyPlanner {
    public static final int MAX_DAILY_DISTANCE = 50; // 50 km

    /**
     * Makes the rally plan, considering the available campings and their distances to the next camping.
     * @param campings the list of available campings, where the first element is the starting line and the last elemet is the last camping before the finishing line.
     * @return a dictionary that maps each camping in the 'campings' parameter to the day when it will be visited.
     * @throws InvalidParameterException if there's a camping in the 'campings' paramter list whose distance to the next camping is grater than the maximum daily distance.
     */
    public static Map<Camping, Integer> makePlan(List<Camping> campings) throws InvalidParameterException {        
        Map<Camping, Integer> plan = new LinkedHashMap<>();

        int currentDay = 1;
        int remainingDailyDistance = MAX_DAILY_DISTANCE;
        for (Camping c : campings) {
            if (c.distanceToNext() <= remainingDailyDistance) { // GREEDY DECISION
                remainingDailyDistance -= c.distanceToNext();
            }
            else if (c.distanceToNext() > MAX_DAILY_DISTANCE) {
                throw new InvalidParameterException(String.format("Camping %s's distance to the next camping is greater than the maximum daily distance, which is not allowed according to Premise 2 (P2).", c.id()));
            }
            else {
                currentDay++; // the team will sleep here because the next camping cannot be reached before night today
                remainingDailyDistance = MAX_DAILY_DISTANCE;
            }
            plan.put(c, currentDay);
        }

        return plan;
    }

    public static void printPlan(Map<Camping, Integer> plan) {
        System.out.println("Plan:");
        for (Map.Entry<Camping, Integer> entry : plan.entrySet()) {
            System.out.printf("\tCamping: %15s | Day: %d\n", entry.getKey().id(), entry.getValue());
        }
    }

    public static void main(String[] args) {
        List<Camping> campings = new ArrayList<>();
        campings.add(new Camping("Starting line", 10));
        campings.add(new Camping("B", 30));
        campings.add(new Camping("C", 25));
        campings.add(new Camping("D", 5));
        campings.add(new Camping("Last before end", 48));

        long startTime = System.currentTimeMillis();
        Map<Camping, Integer> plan = makePlan(campings);
        long endTime = System.currentTimeMillis();
        printPlan(plan);
        System.out.printf("Execution time: %d ms\n", endTime-startTime);
    }
}