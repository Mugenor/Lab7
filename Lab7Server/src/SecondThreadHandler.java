import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Mugenor on 15.05.2017.
 */
public class SecondThreadHandler {
    List<SecondConnection> connections = new ArrayList<>();
    Executor executor = Executors.newCachedThreadPool();
    public synchronized void addConnection(SecondConnection connection){
        connections.add(connection);
        executor.execute(connection);
    }
    public synchronized void removeConnection(SecondConnection connection){
        synchronized (connection) {
            try {
                connection.getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connections.remove(connection);
        }
    }
    public synchronized void sendMessage(Message message , SecondConnection connection){
        for(SecondConnection secondConnection: connections){
            if(secondConnection!=connection){
                secondConnection.giveMessage(message);
            }
        }
    }
    public synchronized void disconnectUser(SecondConnection sc){
        try {
            connections.get(connections.indexOf(sc)).disconnect();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
