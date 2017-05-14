import classes.KarlsonNameException;
import classes.NormalHuman;
import com.google.gson.Gson;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.*;

/**
 * Created by Mugenor on 11.05.2017.
 */
public class ClientThread extends Thread {
    long correctRequest=0;
    private Message message;
    private SocketChannel channel;
    private SelectionKey key;
    private BlockingQueue<Byte> requests;
    private long currentMessageID;
    private boolean needData;
    private boolean isConnected;
    private ByteBuffer bb;
    private Gson gson;
    public ClientThread(SocketChannel channel , SelectionKey key){
        message = new Message(ConnectionState.NEW_DATA);
        this.channel=channel;
        this.key = key;
        this.requests = new ArrayBlockingQueue<>(5);
        this.currentMessageID=-1;
        needData=false;
        isConnected=true;
        bb = ByteBuffer.allocate(512);
        gson = new Gson();
    }
    public ClientThread(Message message, SocketChannel channel, SelectionKey key){
        this.message=message;
        this.channel = channel;
        this.key = key;
        this.currentMessageID=message.getID();
        this.requests = new ArrayBlockingQueue<>(5);
        needData=false;
        isConnected=true;
        bb = ByteBuffer.allocate(512);
        gson = new Gson();
    }
    public long getCurrentMessageID(){return currentMessageID;}
    public void makeRequest(byte i) throws InterruptedException{
        requests.put(i);
    }
    public void setMessage(Message message){
        this.message = message;
    }
    public Message getMessage(){
        return message;
    }
    public void setConnectionState(byte i){
        message.setState(i);
    }
    public void run(){
        try {
            synchronized (this) {
                while (isConnected) {
                    switch (requests.take()) {
                        case ConnectionState.READ:
                            System.out.println("In run READING");
                            read();
                            break;
                        case ConnectionState.NEED_DATA:
                            System.out.println("In run SENDINGDATA");
                            sendData();
                            break;
                        case ConnectionState.NEW_DATA:
                            break;
                        case ConnectionState.DISCONNECT:
                            disconnect();
                            break;
                        case ConnectionState.FINAL_ITERATE:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void read(){
        try {
            StringBuilder mesIn = new StringBuilder();
            bb.clear();
            int i = channel.read(bb);
            bb.flip();
            byte size = bb.get();
            System.out.println(size);
            for(int j=1;j<i;j++){
                mesIn.append((char)bb.get());
            }
            for(int k=1;k<size;k++){
                bb.clear();
                int l = channel.read(bb);
                bb.flip();
                for(int j=0;j<l;j++){
                    mesIn.append((char)bb.get());
                }
            }
            System.out.println("Строка" + mesIn);
            message = gson.fromJson(mesIn.toString(), Message.class);
            System.out.println("Объект создан");
            makeRequest(message.getState());
            System.out.println(requests.size());
            System.out.println("Запрос сделан");
            key.interestOps(SelectionKey.OP_WRITE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void disconnect(){
        try {
            channel.close();
            key.cancel();
            isConnected=false;
            requests.put(ConnectionState.FINAL_ITERATE);
            System.out.println("Disconnected");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void sendData() throws SQLException, IOException, KarlsonNameException{
            LinkedList<NormalHuman> list = new LinkedList<>();
            while (Main.normalHumans.next()) {
                NormalHuman nh = new NormalHuman();
                nh.setName(Main.normalHumans.getString("name"));
                nh.setAge(Main.normalHumans.getLong("age"));
                nh.setTroublesWithTheLaw(Main.normalHumans.getBoolean("troublesWithTheLaw"));
                while (Main.thoughts.next()) {
                    if (Main.normalHumans.getInt("id") == Main.thoughts.getInt("id"))
                        nh.thinkAbout(Main.thoughts.getString("thought"));
                }
                Main.thoughts.beforeFirst();
                System.out.println(nh);
                list.add(nh);
            }
            Main.normalHumans.beforeFirst();
            message.setState(ConnectionState.NEW_DATA);
            message.setData(list);
            String mes = gson.toJson(message);
           /* ByteBuffer buf = ByteBuffer.allocate(mes.getBytes().length + 1);
            buf.put((byte)mes.getBytes().length);
            buf.put(mes.getBytes());*/
            ByteBuffer buf = ByteBuffer.wrap(mes.getBytes());
            channel.write(buf);
            key.interestOps(SelectionKey.OP_READ);
            System.out.println("Сделаль");
    }
}
