import classes.NormalHuman;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Mugenor on 08.05.2017.
 */
public class testSer {
    public static void main(String args[]){
        NormalHuman nh1 = new NormalHuman();
        try{
            nh1.setName("name");
            nh1.setAge(12L);
            nh1.setTroublesWithTheLaw(true);
            nh1.thinkAbout("thought");
            System.out.println("До сериализации \n" + nh1);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(nh1);
            byte[] ser = byteArrayOutputStream.toByteArray();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ser);
            ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);
            NormalHuman nh2 =(NormalHuman) inputStream.readObject();
            NormalHuman nh3 = new NormalHuman();
            nh3.setName("name");
            nh3.setAge(12L);
            nh3.setTroublesWithTheLaw(true);
            nh3.thinkAbout("thought");
            System.out.println("После сериализации \n" + nh2);
            System.out.println("Equals: " + nh1.equals(nh2));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
