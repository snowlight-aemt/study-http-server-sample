package bong.lines.basic;

import bong.lines.basic.comm.WebHandlerInf;
import bong.lines.basic.handler.getloginhtml.LoginHandler;
import bong.lines.basic.handler.helloworld.HelloWorldHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);

    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        runServer(args, (connection) -> {
            LoginHandler helloWorldHandler = new LoginHandler(connection);

            helloWorldHandler.start();
        });
    }

    private static void runServer(String[] args, WebHandlerInf<Socket> webHandlerInf) throws IOException {
        int port = 0;
        if ( args == null || args.length == 0){
            port = DEFAULT_PORT;
        }else {
            port = Integer.parseInt(args[0]);
        }

        // 서버 소켓을 생성한다. 웹 서버는 기본적으로 8080번 포트를 생성한다.
        try(ServerSocket listenSocket = new ServerSocket(port)){  // try-with-resources
            log.info("Web Application Server Start {} port", port);

            // 클라이언트가 연결할 때 까지 대기한다.
            Socket connection;

            while((connection = listenSocket.accept()) != null){
                webHandlerInf.run(connection);
            }
        }
    }
}
