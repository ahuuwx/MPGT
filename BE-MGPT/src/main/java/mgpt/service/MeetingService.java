package mgpt.service;

import mgpt.dao.Project;
import mgpt.dao.Sprint;
import mgpt.repository.MeetingRepository;
import mgpt.repository.ProjectRepository;
import mgpt.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class MeetingService {
    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    ProjectRepository projectRepository;

    public ResponseEntity<?> createNewMeetingsInProject(HashMap<String,Object> reqBody) throws Exception {
        try {
            int projectId= (int) reqBody.get("projectId");
            int slot= (int) reqBody.get("slot");

            Project project= projectRepository.findByProjectId(projectId);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
