import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Mugenor on 14.05.2017.
 */
public class SecondConnection extends Thread {
    private Socket socket;
    private BlockingQueue<Message> queue;
    private Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(new GsonDeserializeExclusion()).create();;
    private boolean isAlive;
    private OutputStream socketOS;
    private ObjectOutputStream oos;
    private ByteArrayOutputStream baos;
    public SecondConnection(Socket socket)throws IOException{
        queue = new ArrayBlockingQueue<Message>(5);
        isAlive=true;
        this.socket = socket;
        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);
        socketOS = socket.getOutputStream();
    }
    public void run(){
        try {
            while(isAlive){
                Message message = queue.take();
                if(message.getState()!=ConnectionState.FINAL_ITERATE) {
                    System.out.println("здеся");
                    baos = new ByteArrayOutputStream();
                    oos = new ObjectOutputStream(baos);
                    oos.writeObject(message);
                    baos.flush();
                    oos.flush();
                    socketOS.write((int)Math.ceil((double)baos.size()/(double)512));
                    socketOS.write(baos.toByteArray());
                    socketOS.flush();
                    baos.reset();
                    oos.reset();
                }
            }
            baos.close();
            oos.close();
            socketOS.close();
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
