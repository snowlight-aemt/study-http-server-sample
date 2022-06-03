package bong.lines.basic.comm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bong.lines.basic.handler.getLoginParam.LoginDTO;

public class MapperTest {
    private Mapper mapper;

    @BeforeEach
    void setup() {
        mapper = new Mapper();
    }

    @Test
    void testMapToJSON() {
		LoginDTO loginDTO = new LoginDTO("name_", "email_", "userId_", "password_");

        mapper.mapToJSON(loginDTO);
    }
}
