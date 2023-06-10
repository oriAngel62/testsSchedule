package api;
import java.util.ArrayList;
import java.util.List;


public class MissionGenerator {
    public static List<Mission> generateMissionList() {
        List<Mission> missionList = new ArrayList<>();
        // Mission 1
            Mission mission1 = new Mission();
            mission1.setTitle("Mission 1");
            mission1.setDescription("Description for Mission 1");
            mission1.setType("Type for Mission 1");
            mission1.setLength(60);
            mission1.setDeadLine("2023-06-10T10:00:00");
            mission1.setPriority(1);

            // Set optional days for Mission 1
            List<OptionalDay> optionalDays1 = new ArrayList<>();
            OptionalDay optionalDay1 = new OptionalDay();
            optionalDay1.setDay("Sunday");
            optionalDays1.add(optionalDay1);
            mission1.setOptionalDays(optionalDays1);

            // Set optional hours for Mission 1
            List<OptionalHour> optionalHours = new ArrayList<>();
            OptionalHour optionalHour1 = new OptionalHour();
            optionalHour1.setHour("12:00:00");
            OptionalHour optionalHour2 = new OptionalHour();
            optionalHour2.setHour("16:00:00");
            optionalHours.add(optionalHour2);
            optionalHours.add(optionalHour1);
            mission1.setOptionalHours(optionalHours);
            missionList.add(mission1);


        return missionList;
    }
}

