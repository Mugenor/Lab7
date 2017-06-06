import classes.KarlsonNameException;
import classes.NormalHuman;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.ORMException;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
    private Gson gson;
    private SecondConnection secondConnection;
    private ByteArrayOutputStream baos;
    private ObjectOutputStream oos;
    public ClientThread(SocketChannel channel , SelectionKey key, SecondConnection secondConnection) throws IOException{
        this.message = new Message(ConnectionState.NEW_DATA);
        this.channel=channel;
        this.key = key;
        this.requests = new ArrayBlockingQueue<>(5);
        this.isConnected=true;
        this.gson = new GsonBuilder().addDeserializationExclusionStrategy(new GsonDeserializeExclusion()).create();
        this.secondConnection = secondConnection;
        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);
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
                            sendData(ConnectionState.NEED_DATA);
                            break;
                        case  ConnectionState.ERROR:
                            sendData(ConnectionState.ERROR);
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
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("У вас сломалась БД");
            disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
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
                } else {
                    try {
                        System.out.println("пересылаю");
                        Message errorMessage = new Message(ConnectionState.ERROR);
                        secondConnection.giveMessage(errorMessage);
                    }catch (Exception e){e.printStackTrace();}
                    Main.exc = true;
                }
            }
        }
        key.interestOps(SelectionKey.OP_READ);
    }
    private void read(){
        try {
            ByteBuffer size = ByteBuffer.allocate(1);
            channel.read(size);
            int j = size.get(0);
            ByteBuffer mes = ByteBuffer.allocate(512 * j);
            System.out.println("перед read");
            channel.read(mes);
            System.out.println("после read");
            System.out.println(mes);
            ByteArrayInputStream bais = new ByteArrayInputStream(mes.array());
            ObjectInputStream ois = new ObjectInputStream(bais);
            System.out.println("перед message");
            message = (Message) ois.readObject();
            System.out.println("после message");
            bais.close();
            ois.close();
            System.out.println(message.getState());
            System.out.println("Принял сообщение от " + channel.getRemoteAddress());
            synchronized (message) {
                makeRequest(message.getState());
                if (message.maxID != -10)
                    Main.maxID = message.maxID;
            }
            key.interestOps(SelectionKey.OP_WRITE);
        }catch (IOException e){
            e.printStackTrace();
            try{
                makeRequest(ConnectionState.NEED_DATA);
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
        } catch (Exception e){
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
                Main.threadHandler.disconnectUser(secondConnection);
                Main.threadHandler.removeConnection(secondConnection);
            }
            requests.put(ConnectionState.FINAL_ITERATE);
            Main.notEditable = new HashSet<>(message.getNotEditable());
            message.setState(ConnectionState.NEW_DATA);
            message.setTypeOfOperation(Message.notEdit);
            Main.threadHandler.sendMessage(message, secondConnection);
            System.out.println("Клиент " + str + " был отключён.");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void sendData(byte state) throws SQLException, IOException, KarlsonNameException, ORMException, ClassNotFoundException,
            IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException{
        message = getMessageWithAllHumans(message, state);
        oos.writeObject(message);
        oos.flush();
        baos.flush();
        byte[] size = new byte[1];
        size[0] = (byte) Math.ceil((int)Math.ceil((double)baos.size()/(double)512));
        channel.write(ByteBuffer.wrap(size));
        channel.write(ByteBuffer.wrap(baos.toByteArray()));
        baos.reset();
        oos.reset();
        key.interestOps(SelectionKey.OP_READ);
        System.out.println("Клиенту " + channel.getRemoteAddress() + " отправлены начальные данные.");
    }

    private Message getMessageWithAllHumans(Message message, byte state) throws SQLException, ORMException, ClassNotFoundException,
            IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
            LinkedList<NormalHuman> list = Main.getDbc().getOrm().getAllObjects(NormalHuman.class);
            list.sort((nh1, nh2) -> nh1.getId()-nh2.getId());
            synchronized (message) {
                message.setState(state);
                message.setData(list);
                message.maxID = Main.maxID;
                message.reinitialize(Main.notEditable);
            }
            return message;
    }
}
