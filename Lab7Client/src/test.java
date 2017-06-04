import classes.KarlsonNameException;
import classes.NormalHuman;
import exceptions.ORMException;
import myAnnotations.*;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Mugenor on 02.06.2017.
 */
public class test {
    public static void main(String... args) throws ORMException, ClassNotFoundException, IllegalAccessException {

        NormalHuman nh = new NormalHuman();
        nh.setAge(12);
        nh.setId(110);
        try{
            nh.setName("Ilya");
        }catch (KarlsonNameException e){
            e.printStackTrace();
        }
        nh.thinkAbout("bla");
        nh.thinkAbout("Ilya");
        nh.thinkAbout("Liza");
        nh.setTroublesWithTheLaw(true);
        writeObject(nh);
        getAllObjects(NormalHuman.class);
        deleteObject(nh);
        getObjectById(NormalHuman.class, 110);
        updateObject(nh);
    }
    static void writeObject(Object obj)throws ORMException, ClassNotFoundException, IllegalAccessException{
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
        System.out.println("Class: " + cl + "\nFields: " + fields.size());
        for(Field field: fields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if(column!=null) {
                quary.append(column.name()).append(",");
                if(field.getType()==String.class)
                    values.append("'").append(field.get(obj)).append("',");
                else values.append(field.get(obj)).append(",");

            }
            Property property = field.getAnnotation(Property.class);
            if(property!=null){
                writeProperty(field, obj, property.type(), property.refColumn(), id);
            }
            field.setAccessible(false);
        }
        values.deleteCharAt(values.length()-1).append(");");
        quary.deleteCharAt(quary.length()-1).append(") ").append(values);
        System.out.println(quary);
    }
    static void writeProperty(Field prop, Object obj, String type, String refColumn, int id)throws ORMException, ClassNotFoundException, IllegalAccessException{
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
            System.out.println(quary);
        }
    }
    static void getAllObjects(Class cl)throws ORMException, ClassNotFoundException{
        Table table = (Table) cl.getAnnotation(Table.class);
        if(table==null) throw new ORMException();
        Object[] result;
        StringBuilder quary = new StringBuilder("select * from " + table.name()).append(";");
        List<Field> fields = new ArrayList<>();
        List<Field> properties = new ArrayList<>();
        Collections.addAll(fields, cl.getDeclaredFields());
        Class<?> tClass = cl.getSuperclass();
        while(tClass!=null){
            Collections.addAll(fields, tClass.getDeclaredFields());
            tClass = tClass.getSuperclass();
        }
        String id = "id";
        for(Field field: fields){
            Id idi = field.getAnnotation(Id.class);
            if(idi!=null){
                id = field.getName();
                break;
            } else {
                Property prop = field.getAnnotation(Property.class);
                if(prop!=null){
                    properties.add(field);
                }
            }
        }
        for(Field property: properties){
            Property prop = property.getAnnotation(Property.class);
            if(prop!=null){
                Class<?> propClass = Class.forName(prop.type());
                Table propTable = propClass.getAnnotation(Table.class);
                if(propTable==null) throw new ORMException();
                quary.append(" inner join ").append(propTable.name()).append(" on (").append(propTable.name()).append(".").append(prop.refColumn()).append("=").append(table.name())
                        .append(".").append(id).append(")");
            }
        }
        quary.append(";");
        System.out.println(quary);
    }
    static void deleteObject(Object obj)throws ORMException, ClassNotFoundException, IllegalAccessException{
        Class<?> cl = obj.getClass();
        Table table = cl.getAnnotation(Table.class);
        if(table==null) throw new ORMException();
        StringBuilder quaryObj = new StringBuilder("delete from ").append(table.name()).append(" where ");
        List<Field> fields = new ArrayList<>();
        List<Field> proeprties = new ArrayList<>();
        Collections.addAll(fields, cl.getDeclaredFields());
        Class<?> tClass = cl.getSuperclass();
        while(tClass!=null){
            Collections.addAll(fields, tClass.getDeclaredFields());
            tClass = tClass.getSuperclass();
        }
        int id=0;
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
        if(id==0) throw new ORMException();
        for(Field field: proeprties){
            Property prop = field.getAnnotation(Property.class);
            Class<?> clProp = Class.forName(prop.type());
            Table tableProp = clProp.getAnnotation(Table.class);
            StringBuilder quaryProp = new StringBuilder("delete from ").append(tableProp.name()).append(" where ").append(prop.refColumn())
                    .append("=").append(id).append(";");
            System.out.println(quaryProp);
        }
        quaryObj.append(idName).append("=").append(id).append(";");
        System.out.println(quaryObj);
    }
    static void getObjectById(Class cl, int id) throws ORMException, ClassNotFoundException {
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
    static void updateObject(Object obj)throws ORMException, ClassNotFoundException, IllegalAccessException{
        Class<?> cl = obj.getClass();
        Table table = cl.getAnnotation(Table.class);
        if(table==null) throw new ORMException();
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
            if(column!=null && idi==null){
                field.setAccessible(true);
                if(field.getType()==String.class)
                    quaryObj.append(column.name()).append("='").append(field.get(obj)).append("',");
                else quaryObj.append(column.name()).append("=").append(field.get(obj)).append(",");
            }
        }
        quaryObj.deleteCharAt(quaryObj.length()-1);
        quaryObj.append(" where ").append(idName).append("=").append(id).append(";");
        System.out.println(quaryObj);
        for(Field property: properties){
            Property prop = property.getAnnotation(Property.class);
            Class<?> clProp = Class.forName(prop.type());
            Table tableProp = clProp.getAnnotation(Table.class);
            if(tableProp==null) throw new ORMException();
            StringBuilder quaryProp = new StringBuilder("delete from ").append(tableProp.name()).append(" where ").append(prop.refColumn())
                    .append("=").append(id).append(";");
            System.out.println(quaryProp);
            property.setAccessible(true);
            writeProperty(property, obj, prop.type(), prop.refColumn(), id);
            property.setAccessible(false);
        }
    }
}
