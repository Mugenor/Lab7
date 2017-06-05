import classes.NormalHuman;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
public class CollectTable extends AbstractTableModel{
    private static String col1=ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Name");
    private static String col2=ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Age");
    private static String col3=ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("TroublesWithTheLaw");
    private static String col4=ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Time");
    private Locale l = Interface.getLocale();
    public ArrayList<String []> data = new ArrayList<>();
    public CollectTable(){
        for(int i = 0;i < data.size();i++){
            data.add(new String[getColumnCount()]);
        }
    }
    @Override
    public int getRowCount() {return data.size();}
    @Override
    public String getColumnName(int column){
        switch(column){
            case 0: return col1;
            case 1: return col2;
            case 2: return col3;
            case 3: return col4;
            default: return null;
        }
    }
    public void updateLanguage(){
        col1=ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Name");
        col2=ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Age");
        col3=ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("TroublesWithTheLaw");
        col4=ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Time");
        for(int i = 0;i < data.size();i++){
            if(data.get(i)[2].equals(ResourceBundle.getBundle("Locale", l).getString("false")))
                data.get(i)[2] = ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("false");
            else
                data.get(i)[2] = ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("true");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("DateTime"));
            char[] ss = data.get(i)[3].toCharArray();
            if(Interface.getFormatter().equals(formatter))
                continue;
            if(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("DateTime").equals("dd.MM.yyyy hh:mm:ss")){
                ss[2] = '.';
                ss[5] = '.';
                data.get(i)[3] = new String(ss);
            }
            else
            {
                ss[2] = '/';
                ss[5] = '/';
                data.get(i)[3] = new String(ss);
            }
            Interface.setFormatter(formatter);
        }
        l = Interface.getLocale();
        fireTableDataChanged();
        fireTableStructureChanged();
    }
    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }
    public void removeData(int i){
        data.remove(i);
        fireTableDataChanged();
    }
    public void addData(String []row){
        data.add(row);
        fireTableDataChanged();
    }
    public  void addData(NormalHuman nh){
        String[] str= new String[4];
        str[0]=nh.getName();
        str[1]=nh.getAge().toString();
        str[2]=ResourceBundle.getBundle("Locale", Interface.getLocale()).getString(nh.getTroublesWithTheLaw().toString());
        str[3]=nh.getTimeOfCreate().format(Interface.getFormatter());
        data.add(str);
        fireTableDataChanged();
    }
    public void removeAll(){
        data.clear();
        fireTableDataChanged();
    }
    public void editData(String []row, int numberRow ){
        removeData(numberRow-1);
        data.add(numberRow-1,row);
        fireTableDataChanged();
    }
    public void editData(NormalHuman nh,int numberRow){
        String[] str= new String[4];
        str[0]=nh.getName();
        str[1]=nh.getAge().toString();
        str[2]=ResourceBundle.getBundle("Locale", Interface.getLocale()).getString(nh.getTroublesWithTheLaw().toString());
        str[3]=nh.getTimeOfCreate().format(Interface.getFormatter());
        removeData(numberRow);
        data.add(numberRow,str);
        fireTableDataChanged();
    }
    public void setData(int i, NormalHuman nh){
        String[] str = new String[3];
        str[0]=nh.getName();
        str[1]=nh.getAge().toString();
        str[2]=ResourceBundle.getBundle("Locale", Interface.getLocale()).getString(nh.getTroublesWithTheLaw().toString());
    }
    public String toString(){
        String s="";
        for(int i =0 ;i<data.size();i++){
            s=s+"\n" + data.get(i)[0]+ "  " + data.get(i)[1] + "  " + data.get(i)[2] + " " + data.get(i)[3];
        }
        return s;
    }
}
