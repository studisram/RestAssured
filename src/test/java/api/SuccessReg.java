package api;

import lombok.Getter;

public class SuccessReg {

    @Getter private Integer id;
    @Getter private String token;

    public SuccessReg() {

    }
    public SuccessReg(Integer id, String token) {
        this.id = id;
        this.token = token;
    }
}
