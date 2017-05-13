import classes.KarlsonNameException;
import classes.NormalHuman;

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
    private Message message;
    private SocketChannel channel;
    private SelectionKey key;
    private BlockingQueue<Byte> requests;
    private long currentMessageID;
    private boolean needData;
    private boolean isConnected;
    public ClientThread(SocketChannel channel , SelectionKey key){
        this.channel=channel;
        this.key = key;
        this.requests = new ArrayBlockingQueue<>(5);
        this.currentMessageID=-1;
        needData=false;
        isConnected=true;
    }
    public ClientThread(Message message, SocketChannel channel, SelectionKey key){
        this.message=message;
        this.channel = channel;
        this.key = key;
        this.currentMessageID=message.getID();
        this.requests = new ArrayBlockingQueue<>(5);
        needData=false;
        isConnected=true;
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
            while (!requests.isEmpty()) {
                System.out.println("Кручусь");
                synchronized (this) {
                    switch (requests.take()) {
                        case ConnectionState.READ:
                            read();
                            break;
                        case ConnectionState.NEED_DATA:
                            sentData();
                            break;
                        case ConnectionState.DISCONNECT:
                            disconnect();
                            break;
                        case ConnectionState.FINAL_ITERATE:
                            break;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void read(){
    /*    try {
            ByteBuffer size = ByteBuffer.allocate(1);
            channel.read(size);
            byte j = size.get(0);
            System.out.println(j+ " : " +2048*j);
            ByteBuffer mes = ByteBuffer.allocate(2048 * j);
            channel.read(mes);
            ByteArrayInputStream bais = new ByteArrayInputStream(mes.array());
            ObjectInputStream ois = new ObjectInputStream(bais);
            message = (Message) ois.readObject();
            if (currentMessageID != message.getID()) {
                currentMessageID = message.getID();
                requests.put(message.getState());
            }
            ois.close();
            System.out.println("Закончил чтение");
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }
    private void disconnect(){
        try {
            channel.close();
            key.cancel();
            isConnected=false;
            requests.put(ConnectionState.FINAL_ITERATE);
        }catch (InterruptedException | IOException e){
            e.printStackTrace();
        }
    }
    private void sentData() throws SQLException, IOException, KarlsonNameException{
            LinkedList<NormalHuman> list = new LinkedList<>();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
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
            message.updateID();
            oos.writeObject(message);
            channel.write(ByteBuffer.wrap(baos.toByteArray()));
            oos.flush();
            oos.close();
    }
}
