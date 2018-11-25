package amber_team.amber.model.dto;

import java.util.Date;

public class ReportOrdersDto extends PaginationDto {
    private Date toDate;
    private Date fromDate;

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
}
