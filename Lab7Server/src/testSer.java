import classes.NormalHuman;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Mugenor on 08.05.2017.
 */
public class testSer {
    public static void main(String args[]){
        try{
            ServerSocketChannel serverChannel  = ServerSocketChannel.open();
            Selector selector = SelectorProvider.provider().openSelector();
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(InetAddress.getLocalHost(), 1000));
            SelectionKey serverKey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select();
                Set keys = selector.selectedKeys();
                Iterator it = keys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = (SelectionKey) it.next();
                    it.remove();
                    if (key.isAcceptable()) {
                        SocketChannel newChannel = serverChannel.accept();
                        newChannel.configureBlocking(false);
                        SelectionKey newKey = newChannel.register(selector, SelectionKey.OP_WRITE);
                        newKey.attach(ConnectionState.NEW_DATA);
                        System.out.println("Новое соединение: " + newChannel.getLocalAddress());
                    } else if (key.isWritable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ArrayList<NormalHuman> list = new ArrayList<>();
                        NormalHuman nh = new NormalHuman();
                        nh.setTroublesWithTheLaw(true);
                        nh.setAge(12);
                        nh.setName("ilya");
                        nh.thinkAbout("pidor");
                        NormalHuman nh1 = new NormalHuman();
                        nh1.setTroublesWithTheLaw(false);
                        nh1.setAge(254);
                        nh1.setName("kolya");
                        nh1.thinkAbout("tozhe pidor");
                        list.add(nh);
                        list.add(nh1);
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ObjectOutputStream oos= new ObjectOutputStream(os);
                        oos.writeObject(list);
                        ByteBuffer buffer = ByteBuffer.wrap(os.toByteArray());
                        channel.write(buffer);
                    }
                }
            }
        }catch (Exception e){}

    }
}
