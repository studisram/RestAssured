package api;

import lombok.Getter;

public class UnsuccessRegData {
    @Getter private String email;

    public UnsuccessRegData() {}

    public UnsuccessRegData(String email) {
        this.email = email;
    }
}
