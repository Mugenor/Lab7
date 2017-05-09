import classes.NormalHuman;
import com.sun.xml.internal.ws.encoding.MtomCodec;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * Created by Mugenor on 06.05.2017.
 */
public class Main {
    public static void main(String args[]){
        try{
            Socket socket = new Socket(InetAddress.getLocalHost(), 1000);
            InputStream is =  socket.getInputStream();
            System.out.println("получил inputstream");
            char type = (char) is.read();
            System.out.println(type);
            int length = is.read();
            System.out.println(length);
            BufferedInputStream bis = new BufferedInputStream(is);
            ObjectInputStream ois = new ObjectInputStream(bis);
            System.out.println("Пробую прочитать");
            ArrayList<NormalHuman> list = (ArrayList)ois.readObject();
            System.out.println("Прочитал");
            for(NormalHuman nh: list){
                System.out.println(nh + "\n");
            }
            System.out.println("Я вышел");

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
