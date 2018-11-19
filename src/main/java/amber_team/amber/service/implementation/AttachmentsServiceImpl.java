package amber_team.amber.service.implementation;

import amber_team.amber.model.dto.FileInfoDto;
import amber_team.amber.model.dto.ListFilesDto;
import amber_team.amber.service.interfaces.AttachmentsService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service(value = "attachmentsService")
public class AttachmentsServiceImpl implements AttachmentsService {
    private final String MAIN_PATH = "./src/main/resources/attachments/";

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
    public ResponseEntity<byte[]> downloadAttachment(String requestId, String filename) throws IOException {
        Path filePath = Paths.get(MAIN_PATH + requestId + "/" + filename);
        byte[] fileContent = Files.readAllBytes(filePath);
        String fileName = filePath.getFileName().toString();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf("multipart/form-data"));
        header.setContentLength(fileContent.length);
        header.set("Content-Disposition", "attachment; filename=" + fileName);
        return new ResponseEntity<>(fileContent, header, HttpStatus.OK);
    }

    @Override
    public ResponseEntity listFiles(String requestId) {
        if (Files.notExists(Paths.get(MAIN_PATH + requestId))) return ResponseEntity.ok(new ListFilesDto());
        List<FileInfoDto> files = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(MAIN_PATH + requestId))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach((path) -> {
                        File file = path.toFile();
                        files.add(new FileInfoDto(file.getName(), fileSizeMb(file)));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        ListFilesDto listFilesDto = new ListFilesDto();
        listFilesDto.setListFiles(files);
        return ResponseEntity.ok(listFilesDto);
    }

    private String fileSizeMb(File file) {
        NumberFormat mNumberFormat = NumberFormat.getInstance();
        mNumberFormat.setMinimumFractionDigits(2);
        mNumberFormat.setMaximumFractionDigits(2);
        return mNumberFormat.format((double) file.length() / (1024 * 1024)) + " mb";
    }

    public ResponseEntity<byte[]> downloadAsZIP(String requestId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/zip"));
        String outputFilename = "attachments.zip";
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        headers.set("Content-Disposition", "attachment; filename=" + outputFilename);


        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteOutputStream);

        try (Stream<Path> paths = Files.walk(Paths.get(MAIN_PATH + requestId))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach((path) -> {
                        File file = path.toFile();
                        try {
                            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                            FileInputStream fileInputStream = new FileInputStream(file);
                            IOUtils.copy(fileInputStream, zipOutputStream);
                            fileInputStream.close();
                            zipOutputStream.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
            zipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        byte[] result = byteOutputStream.toByteArray();
        headers.setContentLength(result.length);

        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }


}