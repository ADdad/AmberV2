package amber_team.amber.model;

import com.fasterxml.jackson.annotation.JsonIgnore;



public class User {


    private String id;

    private String email;

    @JsonIgnore
    private String password;

    private String f_name;

    private String s_name;


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
        return s_name;
    }

    public void setSecondName(String s_name) {
        this.s_name = s_name;
    }

    public String getFirstName() {
        return f_name;
    }

    public void setFirstName(String f_name) {
        this.f_name = f_name;
    }

}
