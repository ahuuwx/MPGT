package mgpt.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.auth.FirebaseAuthException;
import mgpt.dao.Sprint;
import mgpt.dao.Task;
import mgpt.repository.SprintRepository;
import mgpt.repository.TaskRepository;
import mgpt.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FireBaseService {
    StorageOptions storageOptions;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    SprintRepository sprintRepository;

    //<editor-fold desc="Upload file in task">
    //luu file tu post man ve may
    public ResponseEntity<?> uploadToThisMachine(List<MultipartFile> fileUp, int taskId) throws IOException, FirebaseAuthException {
        try {
            //tạo sẵn thư mục uploads, r sau đó code lấy tới url của file
            Path currentRelativePath = Paths.get("uploads");

            String s = currentRelativePath.toAbsolutePath().toString();
            List<String> stringList = new ArrayList<>();
            //de luu file voi root va ten file
            for (MultipartFile multipartFile : fileUp) {
                String root = s + "\\" + multipartFile.getOriginalFilename();
                String fileName = multipartFile.getOriginalFilename();

                //chuyen tu multipartFile qua file
                multipartFile = new org.springframework.mock.web.MockMultipartFile(String.valueOf(fileUp), multipartFile.getBytes());
                File file = new File(root, multipartFile.getOriginalFilename());
                multipartFile.transferTo(file);

                stringList.add(this.uploadFile(file, fileName));
                file.delete();

                Task task = taskRepository.findByTaskId(taskId);
                task.setFileUrl(stringList.toString());
                taskRepository.save(task);
            }

            return ResponseEntity.ok(stringList);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getCause());
        }
    }
    //</editor-fold>

    //<editor-fold desc="upload file in sprint">
    public ResponseEntity<?> uploadToThisMachineInSprint(List<MultipartFile> fileUp, int sprintId) throws IOException, FirebaseAuthException {
        try {
            Sprint sprint = sprintRepository.findBySprintId(sprintId);
            if(sprint == null )
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Constant.INVALID_SPRINT);


            //tạo sẵn thư mục uploads, r sau đó code lấy tới url của file
            Path currentRelativePath = Paths.get("uploads");

            String s = currentRelativePath.toAbsolutePath().toString();
            List<String> stringList = new ArrayList<>();
            //de luu file voi root va ten file
            for (MultipartFile multipartFile : fileUp) {
                String root = s + "\\" + multipartFile.getOriginalFilename();
                String fileName = multipartFile.getOriginalFilename();

                //chuyen tu multipartFile qua file
                multipartFile = new org.springframework.mock.web.MockMultipartFile(String.valueOf(fileUp), multipartFile.getBytes());
                File file = new File(root, multipartFile.getOriginalFilename());
                multipartFile.transferTo(file);

                stringList.add(this.uploadFile(file, fileName));
                file.delete();
                    sprint.setFileUrl(stringList.toString());
                    sprintRepository.save(sprint);
                }

            return ResponseEntity.ok(stringList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getCause());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Upload File">
    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(Constant.BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        FileInputStream fileInputStream = new FileInputStream("./mpgt-2022.json");
        storageOptions = StorageOptions.newBuilder().setProjectId(Constant.PROJECT_ID).setCredentials(GoogleCredentials.fromStream(fileInputStream)).build();
        Storage storage;

        if (storageOptions == null) {
            storage = StorageOptions.getDefaultInstance().getService();
        } else {
            storage = storageOptions.getService();
        }
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        String dowloadURL = String.format("https://firebasestorage.googleapis.com/v0/b/mpgt-2022.appspot.com/o/%s?alt=media", URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8)));
        return dowloadURL;
    }
    //</editor-fold>


}
