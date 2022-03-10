package mgpt.service;

import mgpt.dao.Project;
import mgpt.dao.Sprint;
import mgpt.repository.MeetingRepository;
import mgpt.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class MeetingService {
    @Autowired
    MeetingRepository meetingRepository;

    public ResponseEntity<?> createNewMeetingsInProject(HashMap<String,Object> reqBody) throws Exception {
        try {
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
