package mgpt.service;

import mgpt.dao.Account;
import mgpt.dao.Meeting;
import mgpt.dao.Project;
import mgpt.dao.Task;
import mgpt.model.MeetingsResponseDto;
import mgpt.repository.MeetingRepository;
import mgpt.repository.ProjectRepository;
import mgpt.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


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

    //<editor-fold desc="Create New Meeting In Project">
    public ResponseEntity<?> createNewMeetingsInProject(HashMap<String,String> reqBody) throws Exception {
        try {
            int projectId= Integer.parseInt(reqBody.get("projectId"));
            List<Meeting> meetings=meetingRepository.findAllByProject_ProjectId(projectId);
            //kiem tra trong prj nếu ko có meeting thì mới dc tạo cái mới
            if(meetings.size()!=0){
                throw new Exception(Constant.MEETING_NOT_NULL);
            }
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

            // insert data to meeting
            List<Meeting> meetingList;
            try {
                meetingList = new ArrayList<>();
                for (Date date : dateList) {
                    Meeting meeting = new Meeting();
                    meeting.setMeetingLink(null);
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
    //</editor-fold>

    //<editor-fold desc="Update Meeting Link">
    public ResponseEntity<?> updateMeetingLinkByLeader(int meetingId, HashMap<String,String> reqBody) throws Exception {
        try{
            Meeting meeting=meetingRepository.findMeetingByMeetingId(meetingId);
            String meetingLink=reqBody.get("meetingLink");
            if(meetingLink.equals("")){
                throw new Exception(Constant.MEETING_LINK_NOT_NULL);
            }
            if(meeting!=null){
                meeting.setMeetingLink(meetingLink);
                meetingRepository.save(meeting);
                return ResponseEntity.ok(true);
            } else
                throw new Exception(Constant.INVALID_SPRINT);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Get Meeting In Projects">
    public ResponseEntity<?> getMeetingsInProject(int projectId) {
        List<Meeting> meetings=meetingRepository.findAllByProject_ProjectId(projectId);
        List<MeetingsResponseDto> dtos= meetings.stream().map(meeting -> meeting.convertToDto()).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    //</editor-fold>

    //<editor-fold desc="Delete All Meetings In Project">
    public ResponseEntity<?> deleteAllMeetingsInProject(int projectId) {
        try {
            List<Meeting> meetingList=meetingRepository.findAllByProject_ProjectId(projectId);
            if (meetingList.size()==0) {
                throw new  Exception(Constant.MEETING_NULL);
            } else {
                meetingRepository.deleteAllInBatch(meetingList);
                return ResponseEntity.ok(Boolean.TRUE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //</editor-fold>
}
