import myAnnotations.Table;
import classes.NormalHuman;
import exceptions.ORMException;
import javax.sql.PooledConnection;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

/**
 * Created by Mugenor on 02.06.2017.
 */
public class LittleORM {
//    private PooledConnection pooledConnection;
//    public LittleORM(PooledConnection pooledConnection){
//        this.pooledConnection=pooledConnection;
//    }
//    public void writeObject(Object obj)throws SQLException, ORMException{
//        Class<?> cl = obj.getClass();
//        Field[] fields = cl.getFields();
//
//        Connection connection = getConnection();
//        Statement statement = connection.createStatement();
//        Table objTable = cl.getAnnotation(Table.class);
//        if(objTable==null){ throw new ORMException();}
//        String quary = "insert into " + objTable.name() + " values("+  +");" ;
//        connection.close();
//    }
//    public NormalHuman readHuman(){
//
//    }
//    public void updateHuman(){
//
//    }
//    public void deleteHuman(){
//
//    }
//    public LinkedList<NormalHuman> readAllHuman(){
//
//    }
//    private Connection getConnection()throws SQLException{
//        return pooledConnection.getConnection();
//    }
}
