package api;

import lombok.Getter;

public class RegisterData extends UnsuccessRegData{

    @Getter
    private String password;

    public RegisterData() {}

    public RegisterData(String email, String password) {
        super(email);
        this.password = password;
    }
}
