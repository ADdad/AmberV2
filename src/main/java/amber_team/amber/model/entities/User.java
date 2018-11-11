package amber_team.amber.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;



public class User {


    private String id;

    private String email;

    @JsonIgnore
    private String password;

    private String fName;

    private String sName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecondName() {
        return sName;
    }

    public void setSecondName(String s_name) {
        this.sName = s_name;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String f_name) {
        this.fName = f_name;
    }

}
