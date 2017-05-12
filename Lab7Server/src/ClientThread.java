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
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Mugenor on 11.05.2017.
 */
public class ClientThread extends Thread {
    private Message message;
    private CyclicBarrier lock;
    private SocketChannel channel;
    private SelectionKey key;
    private Exchanger<Boolean> exchanger;

    public ClientThread(SocketChannel channel , SelectionKey key, Exchanger<Boolean> exchanger){
        lock = new CyclicBarrier(2);
        message = new Message(ConnectionState.NEW_DATA);
        this.channel=channel;
        this.key = key;
        this.exchanger=exchanger;
    }
    public ClientThread(Message message, SocketChannel channel, SelectionKey key, Exchanger<Boolean> exchanger){
        this.message=message;
        lock = new CyclicBarrier(2);
        this.channel = channel;
        this.key = key;
        this.exchanger=exchanger;
    }
    public Exchanger<Boolean> getExchanger(){
        return exchanger;
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
                switch (message.getState()) {
                    case ConnectionState.NEW_DATA:
                        sendData();
                        message.setState(ConnectionState.WAITING);
                        break;
                    case ConnectionState.WAITING: break;
                }
                System.out.println("Kruchus'");
        }catch (Exception e){
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
