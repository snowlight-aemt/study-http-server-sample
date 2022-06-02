package bong.lines.basic.handler.getLoginParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class LoginDTO {
    private String name;
    private String email;
    private String userId;
    private String password;
}