package mgpt.service;

import mgpt.dao.Meeting;
import mgpt.dao.Project;
import mgpt.dao.Sprint;
import mgpt.repository.MeetingRepository;
import mgpt.repository.ProjectRepository;
import mgpt.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class MeetingService {
    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    ProjectRepository projectRepository;
    //<editor-fold desc="Convert CN to 1">
    public String[] convertDowToInteger(String[] daysOfWeek) {
        String[] daysOfWeekCopy = new String[daysOfWeek.length];
        // Copy the old one to the new one
        System.arraycopy(daysOfWeek, 0, daysOfWeekCopy, 0, daysOfWeek.length);
        // Replace the last element from "CN" to "1"
        if (daysOfWeekCopy[daysOfWeek.length - 1].equalsIgnoreCase("CN")) {
            daysOfWeekCopy[daysOfWeek.length - 1] = "1";
        }
        // Append
        daysOfWeek = daysOfWeekCopy;
        return daysOfWeek;
    }
    //</editor-fold>

    //<editor-fold desc="Is Days In Shift">
    public boolean isDaysInShift(String[] daysOfWeek, Calendar calendar) {
        return Arrays.stream(convertDowToInteger(daysOfWeek))
                .anyMatch(dayOfWeek -> calendar.get(Calendar.DAY_OF_WEEK) == Integer.valueOf(dayOfWeek));
    }
    //</editor-fold>
    public ResponseEntity<?> createNewMeetingsInProject(HashMap<String,String> reqBody) throws Exception {
        try {
            int projectId= Integer.parseInt(reqBody.get("projectId"));
            Project project= projectRepository.findByProjectId(projectId);
            int slot= Integer.parseInt(reqBody.get("slot"));
            String timeStart=  reqBody.get("timeStart");
            //2-4-6 or 3-5-7
            String dayOfWeek= reqBody.get("dayOfWeek");
            String[] daysOfWeek = dayOfWeek.split("-");

            daysOfWeek = convertDowToInteger(daysOfWeek);
            String[] timeStarts = timeStart.split(":");
            int totalSession = 0;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(project.getStartDate());
            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timeStarts[0]));
            calendar.set(Calendar.MINUTE, Integer.valueOf(timeStarts[1]));


            List<Date> dateList = new ArrayList<>();

            while (totalSession < slot) {
                if (isDaysInShift(daysOfWeek, calendar)) {
                    totalSession++;
                    dateList.add(calendar.getTime());
                }
                calendar.add(Calendar.DATE, 1);
            }

            // Insert information to Session
            List<Meeting> meetingList;
            try {
                meetingList = new ArrayList<>();
                for (Date date : dateList) {
                    Meeting meeting = new Meeting();
                    meeting.setMeeting_link(null);
                    meeting.setMeetingDate(date);
                    meeting.setMeetingTime(90);
                    meeting.setProject(project);

                    meetingList.add(meeting);
                }
                meetingRepository.saveAll(meetingList);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
