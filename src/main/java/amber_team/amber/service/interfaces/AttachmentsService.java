package amber_team.amber.service.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AttachmentsService {

    ResponseEntity uploadAttachments(List<MultipartFile> files, String requestId) throws IOException;
    ResponseEntity<byte[]> downloadAttachment(String requestId, String filename) throws IOException;
    ResponseEntity listFiles(String requestId);
    ResponseEntity<byte[]> downloadAsZIP(String requestId);
}
