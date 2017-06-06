import com.sun.rowset.CachedRowSetImpl;
import exceptions.ORMException;
import myAnnotations.*;

import javax.sql.PooledConnection;
import javax.sql.rowset.CachedRowSet;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.util.*;

/**
 * Created by Mugenor on 02.06.2017.
 */
public class LittleORM {
    private PooledConnection pooledConnection;
    public LittleORM(PooledConnection pooledConnection){
        this.pooledConnection=pooledConnection;
    }
    public void writeObject(Object obj)throws ORMException, ClassNotFoundException, IllegalAccessException, SQLException{
        List<String> queriesProp = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        int id=-1;
        Class<?> cl = obj.getClass();
        Table objTable = cl.getAnnotation(Table.class);
        if(objTable==null){ throw new ORMException();}
        StringBuilder quary = new StringBuilder("insert into ").append(objTable.name()).append("(");
        StringBuilder values = new StringBuilder("values(");
        Collections.addAll(fields, cl.getDeclaredFields());
        Class<?> tClass = cl.getSuperclass();
        while(tClass!=null){
            Collections.addAll(fields, tClass.getDeclaredFields());
            tClass = tClass.getSuperclass();
        }
        for(Field field: fields){
            Id idi = field.getAnnotation(Id.class);
            if(idi!=null){
                field.setAccessible(true);
                id = field.getInt(obj);
                field.setAccessible(false);
                break;
            }
        }
        for(Field field: fields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if(column!=null) {
                quary.append(column.name()).append(",");
                DateTime dateTime = field.getAnnotation(DateTime.class);
                if (dateTime != null) {
                    ZonedDateTime time = (ZonedDateTime) field.get(obj);
                    values.append("'").append(time.getYear()).append("-").append(time.getMonth().getValue())
                            .append("-").append(time.getDayOfMonth()).append(" ").append(time.getHour())
                            .append(":").append(time.getMinute()).append(":").append(time.getSecond()).append("',");
                } else
                if (field.getType() == String.class)
                    values.append("'").append(field.get(obj)).append("',");
                else values.append(field.get(obj)).append(",");
            }
            Property property = field.getAnnotation(Property.class);
            if(property!=null){
                String quaryProp = writeProperty(field, obj, property.type(), property.refColumn(), id);
                if(quaryProp!=null) queriesProp.add(quaryProp);
            }
            field.setAccessible(false);
        }
        values.deleteCharAt(values.length()-1).append(");");
        quary.deleteCharAt(quary.length()-1).append(") ").append(values);
        Connection connection = pooledConnection.getConnection();
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        System.out.println(quary);
        statement.execute(quary.toString());
        for(String quaryProp: queriesProp){
            statement.execute(quaryProp);
        }
        connection.commit();
        connection.setAutoCommit(true);
        statement.close();
        connection.close();
    }
    private String writeProperty(Field prop, Object obj, String type, String refColumn, int id)throws ORMException, ClassNotFoundException, IllegalAccessException{
            List list =  (List)prop.get(obj);
            if(!list.isEmpty()) {
                Class<?> cl = Class.forName(type);
                Table table = cl.getAnnotation(Table.class);
                if (table == null) throw new ORMException();
                HashMap<String, Field> columns = new HashMap<>();
                String bValues = "(";
                StringBuilder quary = new StringBuilder("insert into " + table.name() + "(" + refColumn + ",");
                StringBuilder values = new StringBuilder();
                Field[] fields = cl.getDeclaredFields();
                for (Field field : fields) {
                    Column col = field.getAnnotation(Column.class);
                    if (col != null) {
                        columns.put(col.name(), field);
                        quary.append(col.name()).append(",");
                    }
                }
                quary.deleteCharAt(quary.length() - 1).append(") values");
                for (Object value : list) {
                    values.append(bValues);
                    values.append(id).append(",");
                    for (String col : columns.keySet()) {
                        Field curField = columns.get(col);
                        curField.setAccessible(true);
                        if (curField.getType() == String.class) {
                            values.append("'").append(curField.get(value)).append("',");
                        } else values.append(curField.get(value)).append(",");
                        curField.setAccessible(false);
                    }
                    values.deleteCharAt(values.length() - 1).append("),");
                    quary.append(values);
                    values.delete(0, values.length());
                }
                quary.deleteCharAt(quary.length() - 1).append(";");
                return quary.toString();
            }
            return null;
        }
    public <T> LinkedList<T> getAllObjects(Class<T> cl)throws ORMException, ClassNotFoundException, SQLException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException{
        Table table = (Table) cl.getAnnotation(Table.class);
        if (table == null) throw new ORMException();
        LinkedList<T> result = new LinkedList<>();
        StringBuilder quary = new StringBuilder("select * from " + table.name()).append(";");
        List<Field> fields = new ArrayList<>();
        List<Field> properties = new ArrayList<>();
        HashMap<String, Field> match = new HashMap<>();
        Collections.addAll(fields, cl.getDeclaredFields());
        Class<?> tClass = cl.getSuperclass();
        while (tClass != null) {
            Collections.addAll(fields, tClass.getDeclaredFields());
            tClass = tClass.getSuperclass();
        }
        String idName = "";
        for (Field field : fields) {
            Id idi = field.getAnnotation(Id.class);
            if (idi != null) {
                idName = field.getName();
            }
            Property prop = field.getAnnotation(Property.class);
            if (prop != null) {
                properties.add(field);
            }
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                match.put(column.name(), field);
            }
        }
        if(idName.equals("")) throw new ORMException();
        Connection connection = pooledConnection.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(quary.toString());
        CachedRowSet rowSet = new CachedRowSetImpl();
        rowSet.populate(rs);
        rs.close();
        while (rowSet.next()) {
            T newInstance = cl.newInstance();
            int id = rowSet.getInt(idName);
            for(String column: match.keySet()){
                Field field = match.get(column);
                field.setAccessible(true);
                DateTime date = field.getAnnotation(DateTime.class);
                if(date!=null){
                    ZonedDateTime time = ZonedDateTime.ofInstant(rowSet.getTimestamp(column).toLocalDateTime().toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
                    System.out.println(time);
                    Class<?> dateType = field.getType();
                    Method fac = dateType.getMethod("ofInstant", Instant.class, ZoneId.class);
                    field.set(newInstance, fac.invoke(null, rowSet.getTimestamp(column).toLocalDateTime().toInstant(ZoneOffset.UTC), ZoneOffset.UTC));
                }else {
                    field.set(newInstance, rowSet.getObject(column));
                    field.setAccessible(false);
                }
            }
            for(Field property: properties){
                Property prop = property.getAnnotation(Property.class);
                Class<?> clProp = Class.forName(prop.type());
                Table tableProp = clProp.getAnnotation(Table.class);
                if(tableProp==null)throw new ORMException();
                List list = (List) property.getType().newInstance();
                String queryProp = "select * from " + tableProp.name() + " where " + prop.refColumn() + "=" + id + ";";
                ResultSet resProp = statement.executeQuery(queryProp);
                CachedRowSet rowSetProp = new CachedRowSetImpl();
                rowSetProp.populate(resProp);
                resProp.close();
                while(rowSetProp.next()){
                    Object newProp =  clProp.newInstance();
                    Field[] propFields = clProp.getDeclaredFields();
                    for(Field field: propFields){
                        Column column = field.getAnnotation(Column.class);
                        if(column!=null){
                            field.setAccessible(true);
                            field.set(newProp, rowSetProp.getObject(column.name()));
                            field.setAccessible(false);
                        }
                    }
                    list.add(newProp);
                }
                property.setAccessible(true);
                property.set(newInstance, list);
                property.setAccessible(false);
            }
            result.add(newInstance);
        }
        statement.close();
        connection.close();
        return result;
    }
    public void deleteObject(Object obj)throws ORMException, ClassNotFoundException, IllegalAccessException, SQLException{
        Class<?> cl = obj.getClass();
        Table table = cl.getAnnotation(Table.class);
        if(table==null) throw new ORMException();
        List<String> queriesProp = new ArrayList<>();
        StringBuilder quaryObj = new StringBuilder("delete from ").append(table.name()).append(" where ");
        List<Field> fields = new ArrayList<>();
        List<Field> proeprties = new ArrayList<>();
        Collections.addAll(fields, cl.getDeclaredFields());
        Class<?> tClass = cl.getSuperclass();
        while(tClass!=null){
            Collections.addAll(fields, tClass.getDeclaredFields());
            tClass = tClass.getSuperclass();
        }
        int id=-10;
        String idName="";
        for(Field field: fields){
            Id idi = field.getAnnotation(Id.class);
            if(idi!=null){
                idName = field.getName();
                field.setAccessible(true);
                id = field.getInt(obj);
                field.setAccessible(false);
            }else{
                Property prop = field.getAnnotation(Property.class);
                if(prop!=null) proeprties.add(field);
            }
        }
        if(id==-10) throw new ORMException();
        for(Field field: proeprties){
            Property prop = field.getAnnotation(Property.class);
            Class<?> clProp = Class.forName(prop.type());
            Table tableProp = clProp.getAnnotation(Table.class);
            StringBuilder quaryProp = new StringBuilder("delete from ").append(tableProp.name()).append(" where ").append(prop.refColumn())
                    .append("=").append(id).append(";");
            queriesProp.add(quaryProp.toString());
        }
        quaryObj.append(idName).append("=").append(id).append(";");
        Connection connection = pooledConnection.getConnection();
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        for(String quaryProp: queriesProp){
            statement.execute(quaryProp);
        }
        statement.execute(quaryObj.toString());
        connection.commit();
        connection.setAutoCommit(true);
        statement.close();
        connection.close();
        }
    public void getObjectById(Class cl, int id) throws ORMException, ClassNotFoundException {
        Table table = (Table) cl.getAnnotation(Table.class);
        if (table == null) throw new ORMException();
        StringBuilder quaryObj = new StringBuilder("select * from ").append(table.name()).append(" where ");
        List<Field> fields = new ArrayList<>();
        Collections.addAll(fields, cl.getDeclaredFields());
        Class<?> tClass = cl.getSuperclass();
        while (tClass != null) {
            Collections.addAll(fields, tClass.getDeclaredFields());
            tClass = tClass.getSuperclass();
        }
        String idName = "";
        for (Field field : fields) {
            Id idi = field.getAnnotation(Id.class);
            if (idi != null) {
                idName = field.getName();
            } else {
                Property prop = field.getAnnotation(Property.class);
                if (prop != null) {
                    Class<?> clProp = Class.forName(prop.type());
                    Table tableProp = clProp.getAnnotation(Table.class);
                    if(tableProp==null) throw new ORMException();
                    StringBuilder quaryProp = new StringBuilder("select * from ").append(tableProp.name()).append(" where ").append(prop.refColumn())
                            .append("=").append(id).append(";");
                    System.out.println(quaryProp);
                }
            }
        }
        quaryObj.append(idName).append("=").append(id).append(";");
        System.out.println(quaryObj);
    }
    public void updateObject(Object obj)throws ORMException, ClassNotFoundException, IllegalAccessException, SQLException{
            Class<?> cl = obj.getClass();
            Table table = cl.getAnnotation(Table.class);
            if(table==null) throw new ORMException();
            List<String> queriesProp = new ArrayList<>();
            List<Field> fields = new ArrayList<>();
            List<Field> properties = new ArrayList<>();
            StringBuilder quaryObj = new StringBuilder("update ").append(table.name()).append(" set ");
            Collections.addAll(fields, cl.getDeclaredFields());
            Class<?> tClass = cl.getSuperclass();
            while(tClass!=null){
                Collections.addAll(fields, tClass.getDeclaredFields());
                tClass = tClass.getSuperclass();
            }
            String idName = "";
            int id = -1;
            for(Field field: fields){
                Id idi = field.getAnnotation(Id.class);
                if(idi!=null){
                    idName=field.getName();
                    field.setAccessible(true);
                    id = field.getInt(obj);
                    field.setAccessible(false);
                } else {
                    Property prop = field.getAnnotation(Property.class);
                    if(prop!=null){
                        properties.add(field);
                    }
                }
            }
            if(id==-1) throw new ORMException();
            for(Field field: fields){
                Column column = field.getAnnotation(Column.class);
                Id idi = field.getAnnotation(Id.class);
                DateTime dateTime = field.getAnnotation(DateTime.class);
                field.setAccessible(true);
                if (dateTime != null && column!=null) {
                    ZonedDateTime zonedDateTimetime = (ZonedDateTime) field.get(obj);
                    LocalDateTime localDateTime = zonedDateTimetime.toLocalDateTime();
                    ZonedDateTime time = ZonedDateTime.ofInstant(localDateTime, ZoneOffset.UTC, ZoneId.systemDefault());
                    quaryObj.append(column.name()).append("='").append(time.getYear()).append("-")
                            .append(time.getMonth().getValue())
                            .append("-").append(time.getDayOfMonth()).append(" ").append(time.getHour())
                            .append(":").append(time.getMinute()).append(":").append(time.getSecond()).append("',");
                } else
                if(column!=null && idi==null){
                    if(field.getType()==String.class)
                        quaryObj.append(column.name()).append("='").append(field.get(obj)).append("',");
                    else quaryObj.append(column.name()).append("=").append(field.get(obj)).append(",");
                }
                field.setAccessible(false);
            }
            quaryObj.deleteCharAt(quaryObj.length()-1);
            quaryObj.append(" where ").append(idName).append("=").append(id).append(";");
            for(Field property: properties){
                Property prop = property.getAnnotation(Property.class);
                Class<?> clProp = Class.forName(prop.type());
                Table tableProp = clProp.getAnnotation(Table.class);
                if(tableProp==null) throw new ORMException();
                StringBuilder queryProp = new StringBuilder("delete from ").append(tableProp.name()).append(" where ").append(prop.refColumn())
                        .append("=").append(id).append(";");
                queriesProp.add(queryProp.toString());
                property.setAccessible(true);
                String a = writeProperty(property, obj, prop.type(), prop.refColumn(), id);
                property.setAccessible(false);
                if(a!=null) {
                    queriesProp.add(a);
                }
            }
        Connection connection = pooledConnection.getConnection();
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        statement.execute(quaryObj.toString());
        for(String quaryProp: queriesProp){
            statement.execute(quaryProp);
        }
        connection.commit();
        connection.setAutoCommit(true);
        statement.close();
        connection.close();
        }
    private Connection getConnection()throws SQLException {
        return pooledConnection.getConnection();
    }
}
