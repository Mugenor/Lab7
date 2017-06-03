import classes.KarlsonNameException;
import classes.NormalHuman;
import exceptions.ORMException;
import myAnnotations.Column;
import myAnnotations.Id;
import myAnnotations.Property;
import myAnnotations.Table;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Mugenor on 02.06.2017.
 */
public class test {
    public static void main(String... args) throws ORMException, ClassNotFoundException, IllegalAccessException {

        NormalHuman nh = new NormalHuman();
        nh.setAge(12);
        nh.setId(20);
        try{
            nh.setName("Ilya");
        }catch (KarlsonNameException e){
            e.printStackTrace();
        }
        nh.setTroublesWithTheLaw(true);
        nh.thinkAbout("pidor");
        nh.thinkAbout("Vlad");
        writeObj(nh);
    }
    static void writeObj(Object obj)throws ORMException, ClassNotFoundException, IllegalAccessException{
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
            System.out.println(field);
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if(column!=null) {
                quary.append(column.name()).append(",");
                values.append(field);

            }
            Property property = field.getAnnotation(Property.class);
            if(property!=null){
                String type = property.type();
                writeProperty(field, obj, type, id);
            }
            field.setAccessible(false);
        }
        System.out.println(quary);
    }
    static void writeProperty(Field prop, Object obj, String type, int id)throws ORMException, ClassNotFoundException, IllegalAccessException{
        Class<?> cl = Class.forName(type);
        Table table = cl.getAnnotation(Table.class);
        if(table!=null) {
            List list =  (List)prop.get(obj);
            System.out.println(list);
            StringBuilder quary = new StringBuilder("insert into ").append(table.name()).append("(");
            StringBuilder values = new StringBuilder("values(");
            Field[] fields = cl.getDeclaredFields();
            for (Field field : fields) {

            }
        }
    }
}
