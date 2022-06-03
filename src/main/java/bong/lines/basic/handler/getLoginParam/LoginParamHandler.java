package bong.lines.basic.handler.getLoginParam;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bong.lines.basic.comm.Mapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginParamHandler extends Thread{
    private static final Logger log = LoggerFactory.getLogger(LoginParamHandler.class);
    private Socket connection;

    public LoginParamHandler(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}, Host Address : {}", connection.getInetAddress(), connection.getPort(), connection.getInetAddress().getHostAddress());

        try(InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            String line = " ";

            byte[] body = null;

            do {
                line = bufferedReader.readLine();

                if(line != null && line.contains("GET") && line.contains("user/create")){
                    log.info(line);
                    String screenName = line.split(" ")[1];
                    String url = screenName.split("\\?")[0];
                    String param = screenName.split("\\?")[1];
                    
                    LoginDTO loginDTO = new Mapper(param).map(LoginDTO.class);
                    System.out.println(loginDTO.toString());
                    // body = Objects.requireNonNull(
                    //         IndexHTMLHandler.class
                    //                 .getResourceAsStream("/templates" + screenName))
                    //                 .readAllBytes();

                    

                }
            } while (body == null);

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        }catch (Exception exception){
            log.error(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent){
        try{
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        }catch (Exception exception){
            log.error(exception.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body){
        try{
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        }catch (Exception exception){
            exception.printStackTrace();
            log.error(exception.getMessage());
        }
    }
}
