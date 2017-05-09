import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import classes.NormalHuman;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import org.postgresql.ds.*;

/**
 * Created by Mugenor on 05.05.2017.
 */
public class Main {
    private static final String url = "jdbc:postgresql://localhost:2345/lab7";
    private static final String username="postgres";
    private static final String password="123";
    private static final int serverPort=1000;
    private static InetAddress host;
    private static final int capacity=1024;
    public static void main(String args[]){

        try {
            //Определение хоста
            host=InetAddress.getLocalHost();
            System.out.println("Адрес хоста: " + host.getHostAddress());
        }catch (UnknownHostException e){
            e.printStackTrace();
        }
        //Загрузка драйверов и подключение к бд
        PGConnectionPoolDataSource pooledDataSource=null;
        try{
            Class.forName("org.postgresql.Driver");

            pooledDataSource = new PGConnectionPoolDataSource();
            pooledDataSource.setUrl(url);
            pooledDataSource.setUser(username);
            pooledDataSource.setPassword(password);
            System.out.println("Создан пулл соединений");
        }catch (ClassNotFoundException e) {
            System.out.println("Не получается найти драйвер для psql");
            System.exit(1);
        }
        ByteBuffer buffer = ByteBuffer.allocate(capacity);
        Selector selector=null;
        ServerSocketChannel server = null;
        SelectionKey serverKey=null;
        //Создание селектора
        try{
            selector = SelectorProvider.provider().openSelector();
        }catch (IOException e){
            System.out.println("Не удаётся открыть селектор");
            System.exit(1);
        }
        //Открытие канала сервера и его регистрирование в селекторе
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(InetAddress.getLocalHost(), serverPort));
            serverKey = server.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            System.out.println("Не удаётся открыть канал сервера");
            System.exit(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        //Цикл сервера
        try {
            while (true) {
                selector.select();
                Set keys = selector.selectedKeys();
                Iterator it = keys.iterator();
                while(it.hasNext()){
                    SelectionKey key =(SelectionKey) it.next();
                    it.remove();
                    //Если кто-то подключился, то регистрируем в селекторе и записываем в него массив людей
                    if(key.isAcceptable()){
                        SocketChannel newChannel = server.accept();
                        newChannel.configureBlocking(false);
                        SelectionKey newKey = newChannel.register(selector, SelectionKey.OP_WRITE);
                        newKey.attach(new ConnectionState(ConnectionState.NEW_DATA));
                        System.out.println("Новое соединение: " + newChannel.getLocalAddress());
                    }
                    //Чтение из каналов
                    else if(key.isReadable()){

                    }
                    //Запись в каналы
                    else if(key.isWritable()){
                        ConnectionState currentConnectionState =(ConnectionState) key.attachment();
                        //Просят новых людей
                        if(currentConnectionState.is(ConnectionState.NEW_DATA)){
                            SocketChannel channel = (SocketChannel) key.channel();
                            List<NormalHuman> list = new ArrayList<>();
                            Connection dataBase = pooledDataSource.getConnection();
                            Statement state = dataBase.createStatement();
                            ResultSet rs = state.executeQuery("select * from normalhuman");
                            //Создание ArrayList людей
                            while(rs.next()){
                                NormalHuman nh = new NormalHuman();
                                nh.setName(rs.getString("name"));
                                nh.setAge(rs.getLong("age"));
                                nh.setTroublesWithTheLaw(rs.getBoolean("troublesWithTheLaw"));
                                ResultSet rs1 = state.executeQuery("select thought from thoughts where id=" + rs.getInt("id")+";");
                                while(rs1.next()){
                                    nh.thinkAbout(rs1.getString("thought"));
                                }
                                rs1.close();
                                list.add(nh);
                            }
                            //Приготавливаем буффер к записи
                            buffer.flip();
                            //Сериализуем ArrayList в байты
                            ByteArrayOutputStream baos=new ByteArrayOutputStream();
                            ObjectOutputStream oos = new ObjectOutputStream(baos);
                            oos.writeObject(list);
                            oos.flush();
                            oos.close();
                            //Сериализованный ArrayList
                            byte[] nhArray = baos.toByteArray();
                            //Длина всей инфы, которую надо передать. 4 байта на длину буффера и ещё 1 на определение типа информации
                            int bufferLengthInt = nhArray.length+5;
                            byte[] bufferLengthByte = new byte[4];
                            //Получение длины инфы в 4ех байтах
                            for(int i=0;i<bufferLengthByte.length;i++){
                                bufferLengthByte[i]=(byte)bufferLengthInt;
                                bufferLengthInt=bufferLengthInt>>>8;
                            }
                            //положили в буффер длину сообщения
                            buffer.put(bufferLengthByte);
                            //положили в буффер информацию о содержимом 'd'=данные, 'c'=запрос
                            buffer.put((byte)'d');
                            //кладём в буффер саму информацию
                            for(int i=0;i<Math.ceil(bufferLengthInt/capacity);i++){
                                buffer.put(nhArray,i*capacity, buffer.remaining()>capacity ? capacity: buffer.remaining());
                                channel.write(buffer);
                                buffer.flip();
                            }
                            state.close();
                            rs.close();
                            dataBase.close();
                            key.interestOps(SelectionKey.OP_READ);
                        }else System.out.println("Используй другой ключ");
                    }
                }
            }
        }catch (Exception e){e.printStackTrace();}
        finally {
            try {
                server.close();
                selector.close();
            }catch (IOException e){
                System.out.println("Не удаётся закрыть что-то");
            }
        }



        /*    for(;;){
                selector.select();
                Set selectedKeys = selector.selectedKeys();
                Iterator it = selectedKeys.iterator();
                while (it.hasNext()){
                    SelectionKey selectionKey =(SelectionKey) it.next();
                    it.remove();
                    if(selectionKey.isAcceptable()){
                        SocketChannel socketChannel = serverChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }else
                    if(selectionKey.isReadable()){
                        SocketChannel channel =(SocketChannel)selectionKey.channel();
                        buffer.clear();
                        int count = channel.read(buffer);
                        System.out.println("Принял: " + new String(buffer.array(),0,count));
                        selectionKey.interestOps(SelectionKey.OP_WRITE);
                    }else
                        if(selectionKey.isWritable()){
                            SocketChannel channel=(SocketChannel) selectionKey.channel();
                            ByteBuffer bb = ByteBuffer.wrap("pidor".getBytes());
                            channel.write(bb);
                            selectionKey.interestOps(SelectionKey.OP_READ);
                        }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }
}
