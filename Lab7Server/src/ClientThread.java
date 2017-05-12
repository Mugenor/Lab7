import classes.KarlsonNameException;
import classes.NormalHuman;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
    private BlockingQueue<Integer> requests;
    private long currentMessageID;
    private boolean needData;
    public ClientThread(SocketChannel channel , SelectionKey key){
        message = new Message(ConnectionState.NEW_DATA);
        this.channel=channel;
        this.key = key;
        this.requests = new ArrayBlockingQueue<>(5);
        this.currentMessageID=-1;
        needData=false;
    }
    public ClientThread(Message message, SocketChannel channel, SelectionKey key, Exchanger<Boolean> exchanger){
        this.message=message;
        this.channel = channel;
        this.key = key;
        this.currentMessageID=message.getID();
        this.requests = new ArrayBlockingQueue<>(5);
        needData=false;
    }
    public long getCurrentMessageID(){return currentMessageID;}
    public void makeRequest(int i) throws InterruptedException{
        requests.put(i);
    }
    public void setMessage(Message message){
        this.message = message;
    }
    public Message getMessage(){
        return message;
    }
    public void setConnectionState(int i){
        message.setState(i);
    }
    public void run(){
        try {
            synchronized (this) {
                while (requests.size() != 0) {
                    switch (requests.take()) {
                        case ConnectionState.NEW_DATA:
                            sendData();
                            break;
                        case ConnectionState.DISCONNECT:
                            disconnect();
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void disconnect(){
        try {
            channel.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void sendData() throws SQLException, IOException, KarlsonNameException{
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
            oos.writeObject(message);
            channel.write(ByteBuffer.wrap(baos.toByteArray()));
            oos.flush();
            oos.close();
            key.interestOps(SelectionKey.OP_READ);
    }
}
