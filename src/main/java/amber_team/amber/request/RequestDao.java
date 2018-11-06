package amber_team.amber.request;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository(value = "requestDao")
public class RequestDao implements IRequestDao {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public ResponseEntity save(Request request){
        // todo
        return null;
    }

    @Override
    public ResponseEntity open(RequestStatusChangeDto request) {
        // todo
        return null;
    }

    @Override
    public ResponseEntity cancel(RequestStatusChangeDto request) {
        // todo
        return null;
    }

    @Override
    public ResponseEntity review(RequestStatusChangeDto request) {
        // todo
        return null;
    }

    @Override
    public ResponseEntity reject(RequestStatusChangeDto request) {
        // todo
        return null;
    }

    @Override
    public ResponseEntity progress(RequestStatusChangeDto request) {
        // todo
        return null;
    }

    @Override
    public ResponseEntity hold(RequestStatusChangeDto request) {
        // todo
        return null;
    }

    @Override
    public ResponseEntity deliver(RequestStatusChangeDto request) {
        // todo
        return null;
    }

    @Override
    public ResponseEntity complete(RequestStatusChangeDto request) {
        // todo
        return null;
    }

    @Override
    public ResponseEntity getRequestInfo(RequestStatusChangeDto principal) {
        // todo
        return null;
    }

}
