package bong.lines.basic.comm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bong.lines.basic.handler.loginsucceshtml.LoginDTO;
import bong.lines.basic.handler.loginsucceshtml.ResponseDTO;

public class MapperTest {
    private Mapper mapper;

    @BeforeEach
    void setup() {
        mapper = new Mapper();
    }

    @Test
    void testMap() {
        String url = "name=name_&email=email_&userId=userId_&password=password_&num=5";
        LoginDTO map = mapper.mapMapToObject(mapper.mapUrlToMap(url), LoginDTO.class);
        System.out.println(map);

        assertEquals("name_", map.getName());
        assertEquals("email_", map.getEmail());
        assertEquals("userId_", map.getUserId());
        assertEquals("password_", map.getPassword());
    }

    // TODO 수정 중 필요.
    @Test
    void testMapToJSON() {
        String actual = mapper.mapObjectToJSON(new ResponseDTO("name_", "email_", "userId_", "password_", false, 5));
        System.out.println(actual);
    }

    @Test
    void testMapUrlToObject() {
        String url = "name=name_&email=email_&userId=userId_&password=password_&num=5";
        LoginDTO map = mapper.mapUrlToObject(url, LoginDTO.class);
        System.out.println(map);

        assertEquals("name_", map.getName());
        assertEquals("email_", map.getEmail());
        assertEquals("userId_", map.getUserId());
        assertEquals("password_", map.getPassword());
    }
}
