import java.io.Console;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by Mugenor on 06.05.2017.
 */
public class Main {
    public static void main(String args[]){
        try{
            Socket socket = new Socket(InetAddress.getLocalHost(), 1000);
            byte[] buf = new byte[1024];
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();
            Console console = System.console();
            if(console!=null){
                String s;
                while(!(s=console.readLine()).equals("q")){
                    os.write(s.getBytes());
                    int count = is.read(buf);
                    System.out.println(new String(buf, 0, count));
                }
                }else {
                System.out.println("Консоль недоступна");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
