package mx.uv;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer extends WebSocketServer {
    private Map<String, WebSocket> clients = new ConcurrentHashMap<>();

    public ChatServer(int port) {
        super(new InetSocketAddress(port));
    }

    public void guardarUsuario(WebSocket conn, String usuario){
        clients.put(usuario, conn);
    }

    public String obtenerUsuario(String mensaje){
        String usuario;

        String[] parts = mensaje.split(":", 2);
        usuario = parts[0];

        return usuario;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        String mensaje = "Rapi cakeFactory: Bienvenido al asistente de ayuda. \n escriba sus dudas y en un momento le responderemos.";
        conn.send(mensaje);
        //broadcast(mensaje);
        System.out.println("New connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received message: " + message);
        String usuario = obtenerUsuario(message);

        if(!clients.containsKey(usuario)){
            clients.put(usuario, conn);
            conn.send(message);
            clients.get("admin").send(message);
            conn.send("Rapi cakeFactory: En un momento lo atendemos.");
        }else{

            if(usuario.equals("admin")){
                // Mensaje del admin: "admin:usuario_destino:contenido"
                String[] parts = message.split(":");
                String cliente = parts[1];
                clients.get(cliente).send(parts[0]+":"+parts[2]);
            }
            clients.get("admin").send(message);
            conn.send(message);
        }
        // Broadcast the message to all clients
        
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started successfully");
        setConnectionLostTimeout(100);
    }
}
