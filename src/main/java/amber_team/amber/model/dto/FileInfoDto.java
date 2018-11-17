package amber_team.amber.model.dto;

public class FileInfoDto {
    private String filename;

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    private String fileSize;

    public FileInfoDto(String filename, String fileSize) {
        this.filename = filename;
        this.fileSize = fileSize;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
