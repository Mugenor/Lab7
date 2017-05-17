import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import classes.NormalHuman;
import org.postgresql.ds.*;
import javax.sql.rowset.CachedRowSet;

/**
 * Created by Mugenor on 05.05.2017.
 */
public class Main {
    private static final String url = "jdbc:postgresql://localhost:2345/lab7";
    private static final String username="postgres";
    private static final String password="123";
    private static final int serverPort=1000;
    private static InetAddress host;
    private static DataBaseCommunication dbc;
    private static Selector selector;
    static HashSet<Integer> notEditable = new HashSet<>();
    static SecondThreadHandler threadHandler;
    static ServerSocket secondServerSocket;
    static int maxID=-1;
    static CachedRowSet normalHumans;
    static CachedRowSet thoughts;
    static volatile boolean exc=false;
    public static DataBaseCommunication getDbc(){
        return dbc;
    }
    public static void main(String args[]){
        System.out.println("Запуск сервера");
        try {
            //Определение хоста
            host=InetAddress.getLocalHost();
            System.out.println("Адрес хоста: " + host.getHostAddress());
        }catch (UnknownHostException e){
            e.printStackTrace();
        }
        //Загрузка драйверов и подключение к бд
        try{dbc=new DataBaseCommunication(url, username, password,"org.postgresql.Driver");
            System.out.println("Создан пулл соединений к БД: "+ url);
        }catch (ClassNotFoundException e) {
            System.out.println("Ошибка: Не получается найти драйвер для psql");
            System.exit(1);
        }catch (SQLException e){
            System.out.println("Ошибка: Не получается подключится к БД");
            System.exit(1);
        }
        ServerSocketChannel server = null;
        SelectionKey serverKey=null;
        //Создание селектора
        try{
            selector = SelectorProvider.provider().openSelector();
        }catch (IOException e){
            System.out.println("Ошибка: Не удаётся открыть селектор");
            System.exit(1);
        }
        //Открытие канала сервера и его регистрирование в селекторе
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), serverPort));
            serverKey = server.register(selector, SelectionKey.OP_ACCEPT);
            secondServerSocket = new ServerSocket();
            secondServerSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), serverPort+1));
        }catch (IOException e){
            System.out.println("Ошибка: Не удаётся открыть канал сервера");
            System.exit(1);
        }catch (Exception e){
            e.printStackTrace();
        };
        try{
           normalHumans = dbc.registerQueryAndGetRowSet("select * from normalhuman;");
           thoughts = dbc.registerQueryAndGetRowSet("select * from thoughts;");
            while(normalHumans.next()){
                int id = normalHumans.getInt("id");
                if(id>maxID)maxID=id;
            }
            normalHumans.beforeFirst();
        }catch (SQLException e){
            System.out.println("Ошибка: Не получается выбрать данные из БД");
            e.printStackTrace();
            return;
        }
        threadHandler = new SecondThreadHandler();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                try {
                    selector.close();
                    normalHumans.close();
                    thoughts.close();
                    executor.shutdown();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        //Цикл сервера
        System.out.println("Ожидание входящих подключений...");
        try {
            while (true) {
                selector.select();
                Set keys = selector.selectedKeys();
                if (keys.size() != 0) {
                    Iterator it = keys.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = (SelectionKey) it.next();
                        it.remove();
                        //Если кто-то подключился, то регистрируем в селекторе и записываем в него массив людей
                        if(!key.isValid())continue;
                        else if (key.isAcceptable()) {
                            SocketChannel newChannel = server.accept();
                            newChannel.configureBlocking(false);
                            SelectionKey newKey = newChannel.register(selector, SelectionKey.OP_READ);
                            //Создание отдельного потока для пользователя и связывание его с ключом
                            SecondConnection secondConnection = new SecondConnection();
                            secondConnection.connect(secondServerSocket.accept());
                            synchronized (threadHandler) {
                                threadHandler.addConnection(secondConnection);
                            }
                            ClientThread newClientThread = new ClientThread(newChannel, newKey ,secondConnection);
                            executor.execute(newClientThread);
                            newKey.attach(newClientThread);
                            System.out.println("Новое соединение: " + newChannel.getRemoteAddress());
                        }
                        //Чтение из каналов
                        else if (key.isReadable()) {
                            ClientThread clientThread = (ClientThread) key.attachment();
                            if ((System.currentTimeMillis() - clientThread.correctRequest)>100) {
                                clientThread.correctRequest=System.currentTimeMillis();
                                clientThread.makeRequest(ConnectionState.READ);
                            }
                        }
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
    }
}
