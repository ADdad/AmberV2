package amber_team.amber.model.dto;

import java.util.List;

public class ListFilesDto {
    public List<FileInfoDto> getListFiles() {
        return listFiles;
    }

    public void setListFiles(List<FileInfoDto> listFiles) {
        this.listFiles = listFiles;
    }

    private List<FileInfoDto> listFiles;
}
