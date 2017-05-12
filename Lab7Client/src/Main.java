import classes.NormalHuman;
import com.sun.xml.internal.ws.encoding.MtomCodec;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Mugenor on 06.05.2017.
 */
public class Main {
    public static void main(String args[]){
        try{
            Socket socket = new Socket(InetAddress.getLocalHost(), 1000);
            InputStream is =  socket.getInputStream();
            char type = (char) is.read();
            System.out.println(type);
            System.out.println("получил inputstream");
            ObjectInputStream ois = new ObjectInputStream(is);
            System.out.println("Пробую прочитать");
            LinkedList<NormalHuman> list = (LinkedList)ois.readObject();
            System.out.println("Прочитал");
            for(NormalHuman nh: list){
                System.out.println(nh + "\n");
            }
            ois.close();
            System.out.println(socket.isClosed());
            System.out.println("Я вышел");

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
