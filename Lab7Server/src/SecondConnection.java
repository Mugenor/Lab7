import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Mugenor on 14.05.2017.
 */
public class SecondConnection extends Thread {
    private Socket socket;
    private BlockingQueue<Message> queue;
    private Gson gson = new Gson();
    private boolean isAlive;
    public SecondConnection(){
        queue = new ArrayBlockingQueue<Message>(5);
        isAlive=true;
    }
    public void run(){
        try {
            while(isAlive){
                Message message = queue.take();
                if(message.getState()!=ConnectionState.FINAL_ITERATE) {
                    System.out.println("здеся");
                    String mes = gson.toJson(message);
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(mes.getBytes());
                }
            }
            System.out.println("USER DISCONNECTED");
        }catch (InterruptedException | IOException e){
            e.printStackTrace();
        }
    }
    public void disconnect()throws InterruptedException{
        isAlive=false;
        queue.put(new Message(ConnectionState.FINAL_ITERATE));
    }
    public void connect(Socket socket){
        this.socket=socket;
    }
    public Socket getSocket(){
        return socket;
    }
    public void giveMessage(Message message){
        try{
            queue.put(message);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
