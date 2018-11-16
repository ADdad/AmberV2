package amber_team.amber.service.implementation;

import amber_team.amber.service.interfaces.AttachmentsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service(value = "attachmentsService")
public class AttachmentsServiceImpl implements AttachmentsService {
    private static final String MAIN_PATH = "./src/main/resources/";

    @Override
    public ResponseEntity uploadAttachments(List<MultipartFile> files, String requestId) throws IOException {
        if (files.size() > 6) return new ResponseEntity(HttpStatus.FORBIDDEN);
        folderDropAndCreate(requestId);
        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue; //next pls
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(MAIN_PATH + requestId + "/" + file.getOriginalFilename());
            Files.write(path, bytes);

        }

        return ResponseEntity.ok("Good");
    }

    private void folderDropAndCreate(String requestId) {
        File folder = new File(MAIN_PATH + requestId);
        if (folder.exists()) {
            String[] entries = folder.list();
            for (String s : entries) {
                File currentFile = new File(folder.getPath(), s);
                currentFile.delete();
            }
            folder.delete();
        }
        folder.mkdir();


    }

    @Override
    public ResponseEntity downloadAttachments(String requestId) {
        return null;
    }
}