import classes.NormalHuman;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.rowset.CachedRowSetImpl;
import org.postgresql.ds.PGConnectionPoolDataSource;

import javax.sql.PooledConnection;
import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

/**
 * Created by Mugenor on 11.05.2017.
 */
public class DataBaseCommunication {
    private final PGConnectionPoolDataSource pooledDataSource = new PGConnectionPoolDataSource();
    private final PooledConnection pooledConnection;
    private LittleORM orm;
    private CachedRowSet rowSet;
    private final String username;
    private final String password;
    public DataBaseCommunication(String url, String username, String password, String driver)throws ClassNotFoundException, SQLException{
        this.username=username;
        this.password=password;
        pooledDataSource.setUrl(url);
        pooledConnection = pooledDataSource.getPooledConnection(username, password);
        Class.forName(driver);
        orm = new LittleORM(pooledConnection);
    }
    public void registerQuery(String sql)throws SQLException{
        Connection connection = pooledConnection.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        rowSet = new CachedRowSetImpl();
        rowSet.populate(rs);
        rs.close();
        statement.close();
        connection.close();
    }
    public CachedRowSet registerQueryAndGetRowSet(String sql) throws SQLException{
        registerQuery(sql);
        return rowSet;
    }
    public LittleORM getOrm(){
        return orm;
    }
    public void update(Message message){
        Connection connection = null;
        try {
            LinkedList<NormalHuman> NewData = message.getData();
            connection = pooledConnection.getConnection();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            if(message.getTypeOfOperation()==Message.change) {
                orm.updateObject(NewData.get(0));
            }
            else if(message.getTypeOfOperation()==Message.add) {
                orm.writeObject(NewData.get(0));
            }
            else if(message.getTypeOfOperation()==Message.delete) {
                orm.deleteObject(NewData.get(0));
            }
        }catch(SQLException e){
            try {
                e.printStackTrace();
                System.out.println("Attention!!! БД СЛОМАЛАСЬ!!!");
                Main.exc = true;
                connection.rollback();
            }catch (SQLException ez){
                e.printStackTrace();
                System.out.println("Не удаётся произвести откат изменений БД.");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
