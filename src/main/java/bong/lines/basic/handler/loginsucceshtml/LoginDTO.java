package bong.lines.basic.handler.loginsucceshtml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class LoginDTO {
    private String name;
    private String email;
    private String userId;
    private String password;
}