import classes.KarlsonNameException;
import classes.NormalHuman;
import com.google.gson.Gson;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.HashSet;
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
    private boolean isConnected;
    private ByteBuffer bb;
    private Gson gson;
    private SecondConnection secondConnection;
    public ClientThread(SocketChannel channel , SelectionKey key, SecondConnection secondConnection){
        this.message = new Message(ConnectionState.NEW_DATA);
        this.channel=channel;
        this.key = key;
        this.requests = new ArrayBlockingQueue<>(5);
        this.isConnected=true;
        this.bb = ByteBuffer.allocate(512);
        this.gson = new Gson();
        this.secondConnection = secondConnection;
    }
    public void makeRequest(byte i) throws InterruptedException{
        requests.put(i);
    }
    public void run(){
        try {
            synchronized (this) {
                while (isConnected) {
                    switch (requests.take()) {
                        case ConnectionState.READ:
                            read();
                            break;
                        case ConnectionState.NEED_DATA:
                            sendData();
                            break;
                        case ConnectionState.NEW_DATA:
                            update();
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
           disconnect();
        }
    }
    private void update(){
        if(message.getTypeOfOperation()!=Message.notEdit) {
            message.setState(ConnectionState.NEW_DATA);
            if(message.getTypeOfOperation() == Message.change)
                Main.notEditable = new HashSet<>(message.getNotEditable());
            Main.getDbc().update(message);
        } else Main.notEditable = new HashSet<>(message.getNotEditable());
        synchronized (message){
            synchronized (secondConnection) {
                if(!Main.exc) {
                    System.out.println("Происходит рассылка сообщения остальным пользователям...");
                    Main.threadHandler.sendMessage(message, secondConnection);
                    System.out.println("Рассылка закончена.");
                } else Main.exc = true;
            }
        }
        key.interestOps(SelectionKey.OP_READ);
    }
    private void read(){
        try {
            StringBuilder mesIn = new StringBuilder();
            bb.clear();
            int i = channel.read(bb);
            bb.flip();
            byte size = bb.get();
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
            System.out.println("Принял сообщение от " + channel.getRemoteAddress());
            synchronized (message) {
                message = gson.fromJson(mesIn.toString(), Message.class);
                makeRequest(message.getState());
                if (message.maxID != -10)
                    Main.maxID = message.maxID;
            }
            key.interestOps(SelectionKey.OP_WRITE);
        }catch (IOException e){
            try{
                makeRequest(ConnectionState.NEED_DATA);
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    private void disconnect(){
        try {
            String str = channel.getRemoteAddress().toString();
            channel.close();
            key.cancel();
            isConnected=false;
            synchronized (Main.threadHandler) {
                Main.threadHandler.removeConnection(secondConnection);
            }
            requests.put(ConnectionState.FINAL_ITERATE);
            System.out.println("Клиент " + str + " был отключён.");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void sendData() throws SQLException, IOException, KarlsonNameException{
            LinkedList<NormalHuman> list = new LinkedList<>();
            Main.normalHumans = Main.getDbc().registerQueryAndGetRowSet("select * from normalhuman;");
            Main.thoughts = Main.getDbc().registerQueryAndGetRowSet("select * from thoughts;");
            while (Main.normalHumans.next()) {
                NormalHuman nh = new NormalHuman();
                nh.setName(Main.normalHumans.getString("name"));
                nh.setAge(Main.normalHumans.getLong("age"));
                nh.setTroublesWithTheLaw(Main.normalHumans.getBoolean("troublesWithTheLaw"));
                nh.setId(Main.normalHumans.getInt("id"));
                while (Main.thoughts.next()) {
                    if (Main.normalHumans.getInt("id") == Main.thoughts.getInt("id"))
                        nh.thinkAbout(Main.thoughts.getString("thought"));
                }
                Main.thoughts.beforeFirst();
                list.add(nh);
            }
            Main.normalHumans.beforeFirst();
            String mes;
            synchronized (message) {
                message.setState(ConnectionState.NEED_DATA);
                message.setData(list);
                message.maxID = Main.maxID;
                message.reinitialize(Main.notEditable);
                mes = gson.toJson(message);
            }
            ByteBuffer buf = ByteBuffer.wrap(mes.getBytes());
            channel.write(buf);
            key.interestOps(SelectionKey.OP_READ);
            System.out.println("Клиенту " + channel.getRemoteAddress() + " отправлены начальные данные.");
    }
}
