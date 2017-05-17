import classes.NormalHuman;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import sun.awt.image.ImageWatched;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by Mugenor on 15.05.2017.
 */
public class AnotherConnection extends Thread {
    private Socket socket;
    private DataInputStream dis;
    private Gson gson;
    private Message message;
    private LinkedList<NormalHuman> list;
    private CollectTable collt;
    public AnotherConnection(Socket socket, LinkedList<NormalHuman> list, CollectTable collt)throws IOException{
        this.socket = socket;
        dis = new DataInputStream(socket.getInputStream());
        gson = new Gson();
        this.list = list;
        this.collt = collt;
    }
    public void run(){
        try {
            while (true) {
                StringBuilder mesIn = new StringBuilder();
                mesIn.append((char) dis.read());
                while (dis.available() != 0) {
                    mesIn.append((char) dis.read());
                }
                message = gson.fromJson(mesIn.toString(), Message.class);
                if(message.getState() == ConnectionState.NEED_DATA){
                    synchronized (list) {
                        synchronized (collt) {
                            Interface.notEditable = message.getNotEditable();
                            list = new LinkedList<>(message.getData());
                            collt.removeAll();
                            for (int i = 0; i < list.size(); i++) {
                                String[] obj = {list.get(i).getName(), list.get(i).getAge().toString(), list.get(i).getTroublesWithTheLaw().toString()};
                                collt.addData(obj);
                            }
                        }
                    }
                }
                else if(message.getTypeOfOperation() == Message.add){
                    synchronized (list){
                        synchronized (collt){
                            list.add(message.getData().get(0));
                            collt.addData(message.getData().get(0));
                        }
                    }
                }
                else if (message.getTypeOfOperation() == Message.delete){
                    synchronized (list){
                        synchronized (collt){
                            collt.removeData(list.indexOf(message.getData().get(0)));
                            list.remove(message.getData().get(0));
                        }
                    }
                }
                else if(message.getTypeOfOperation() == Message.change){
                    synchronized (list){
                        synchronized (collt){
                            synchronized (Interface.notEditable) {
                                Interface.notEditable = new HashSet<>(message.getNotEditable());
                                boolean notChanged = true;
                                int i = 0;
                                while (notChanged) {
                                    if (message.getData().get(0).getId() == list.get(i).getId()) {
                                        list.set(i, message.getData().get(0));
                                        collt.editData(message.getData().get(0), i);
                                        notChanged = false;
                                    }
                                    if (i > list.size()) notChanged = false;
                                    i++;
                                }
                            }
                        }
                    }
                }
                else if(message.getTypeOfOperation() == Message.notEdit){
                    Interface.notEditable = new HashSet<>(message.getNotEditable());
                }
            }
        }
        catch(SocketException e) {
            new Dialog("Разрыв соединения!Сервер отключён!",Interface.getColor());
            System.exit(1);
        }catch (IOException e){} catch(IllegalStateException e){}catch(JsonSyntaxException e){}
    }
}
