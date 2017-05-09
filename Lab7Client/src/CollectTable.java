import classes.NormalHuman;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Created by bespa on 11.04.2017.
 */
public class CollectTable extends AbstractTableModel{

    public ArrayList<String []> data = new ArrayList<>();
    public CollectTable(){
        for(int i = 0;i < data.size();i++){
            data.add(new String[getColumnCount()]);
        }
    }
    @Override
    public int getRowCount() {
        return data.size();
    }
    @Override
    public String getColumnName(int column){
        switch(column){
            case 0: return "Name";
            case 1: return "Age";
            case 2: return "Troubles with the law";
            default: return null;
        }
    }
    @Override
    public int getColumnCount() {
        return 3;
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
        String[] str= new String[3];
        str[0]=nh.getName();
        str[1]=nh.getAge().toString();
        str[2]=nh.getTroublesWithTheLaw().toString();
        data.add(str);
        fireTableDataChanged();
    }
    public void editData(String []row, int numberRow ){
        removeData(numberRow-1);
        data.add(numberRow-1,row);
        fireTableDataChanged();
    }
    public void editData(NormalHuman nh,int numberRow){
        String[] str= new String[3];
        str[0]=nh.getName();
        str[1]=nh.getAge().toString();
        str[2]=nh.getTroublesWithTheLaw().toString();
        removeData(numberRow);
        data.add(numberRow,str);
        fireTableDataChanged();
    }
    public String toString(){
        String s="";
        for(int i =0 ;i<data.size();i++){
            s=s+"\n" + data.get(i)[0]+ "  " + data.get(i)[1] + "  " + data.get(i)[2];
        }
        return s;
    }
}
