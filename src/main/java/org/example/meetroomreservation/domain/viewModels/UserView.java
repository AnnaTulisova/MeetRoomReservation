package org.example.meetroomreservation.domain.viewModels;

public class UserView {
    private Integer id;
    private String email;
    private String login;

    public UserView(Integer id, String email, String login) {
        this.id = id;
        this.email = email;
        this.login = login;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
