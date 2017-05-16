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
    private CachedRowSet rowSet;
    private final String username;
    private final String password;
    public DataBaseCommunication(String url, String username, String password, String driver)throws ClassNotFoundException, SQLException{
        this.username=username;
        this.password=password;
        pooledDataSource.setUrl(url);
        pooledConnection = pooledDataSource.getPooledConnection(username, password);
        Class.forName(driver);
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
    public void update(Message message){
        Connection connection = null;
        try {
            LinkedList<NormalHuman> NewData = message.getData();
            connection = pooledConnection.getConnection();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            if(message.getTypeOfOperation()==Message.change) {
                statement.execute("update normalhuman set name = '" + NewData.get(0).getName() + "', age = " + NewData.get(0).getAge() + ", troubleswiththelaw = " + NewData.get(0).getTroublesWithTheLaw() + " where id = " + NewData.get(0).getId() + ";");
                statement.execute("delete from thoughts where id=" + NewData.get(0).getId()+";");
                if(NewData.get(0).getThoughtsCount()!=0) {
                    StringBuilder sql = new StringBuilder();
                    sql.append("insert into thoughts values('" + NewData.get(0).getThoughts(0) + "', " + NewData.get(0).getId() + " )");
                    for (int i = 1; i < NewData.get(0).countOfThoughts(); i++) {
                        sql.append(",('" + NewData.get(0).getThoughts(i) + "', " + NewData.get(0).getId() + " )");
                    }
                    sql.append(";");

                    statement.execute(sql.toString());
                }
            }
            else if(message.getTypeOfOperation()==Message.add) {
                statement.execute("insert into normalhuman values (" + NewData.get(0).getId() + ", '" + NewData.get(0).getName() + "'," + NewData.get(0).getAge() + "," + NewData.get(0).getTroublesWithTheLaw() + ");");
                if(NewData.get(0).getThoughtsCount()!=0) {
                    StringBuilder sql = new StringBuilder();
                    sql.append("insert into thoughts values('" + NewData.get(0).getThoughts(0) + "', " + NewData.get(0).getId() + " )");
                    for (int i = 1; i < NewData.get(0).countOfThoughts(); i++) {
                        sql.append(",('" + NewData.get(0).getThoughts(i) + "', " + NewData.get(0).getId() + " )");
                    }
                    sql.append(";");
                    statement.execute(sql.toString());
                }
            }
            else if(message.getTypeOfOperation()==Message.delete) {
                statement.execute("delete from thoughts where id=" + NewData.get(0).getId() + ";");
                statement.execute("delete from normalhuman where id = " + NewData.get(0).getId() + ";");
            }
            connection.commit();
            connection.setAutoCommit(true);
            System.out.println("БД изменена");
            statement.close();
            connection.close();
        }catch(SQLException e){
            try {
                System.out.println("Ошибка при изменении БД.");
                Main.exc = true;
                connection.rollback();
            }catch (SQLException ez){
                System.out.println("Не удаётся произвести откат изменений БД.");
            }
        }
    }
}
