package bong.lines.basic.handler.loginsucceshtml;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ResponseDTO {
    private String name;
    private String email;
    private String userId;
    private String password;
    private Boolean bool;
    private int num;
}
