import classes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

/**
 * Created by Mugenor on 13.04.2017.
 */
public class ButtonsUnderTable {
    private static Color c = null;
    private CollectTable collt;
    private JTable collections;
    private LinkedList<NormalHuman> coll;
    private EditWindow ew = new EditWindow();
    private boolean openedShowWindow = false;
    private boolean openedEditWindow= false;
    private DefaultListModel<String> dlm= new DefaultListModel<>();
    private JList<String> list = new JList<>(dlm);
    public void setColor(Color col){
        c = col;
        ew.setColor(col);
        list.setForeground(c);
    }
    ButtonsUnderTable(JTable table, CollectTable ct, LinkedList<NormalHuman> coll){
        collt=ct;
        collections=table;
        this.coll=coll;
    }
    public void delete(){
        if(collections.getSelectedRow()!=-1){
            Interface.setIsChanged(true);
            coll.remove(collections.getSelectedRow());
            collt.removeData(collections.getSelectedRow());
            Interface.message.setData(coll);
            Interface.message.setState(ConnectionState.NEW_DATA);
            Interface.sendMessage();}
    }
    public void edit(){
        if(collections.getSelectedRow()!=-1&&!openedEditWindow) {
            openedEditWindow=true;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ew = new EditWindow("Edit Person",
                            coll.get(collections.getSelectedRow()),
                            collt, collections.getSelectedRow(),
                            new EditExit() {
                        @Override
                        public void doOnExit() {
                            openedEditWindow = false;
                        }
                    });
                    ew.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            openedEditWindow = false;
                        }
                    });
                    ew.setLocation((int)Interface.getFrameLocation().getX()-320,(int)Interface.getFrameLocation().getY());
                }
            });
        }
    }
    public void showThoughts(){
        if(collections.getSelectedRow()!=-1&&!openedShowWindow){
            openedShowWindow=true;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JFrame jf= new JFrame("Thoughts Frame");
                    jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    jf.setResizable(false);
                    jf.setSize(new Dimension(400,200));
                    jf.setLocationRelativeTo(null);
                    dlm.removeAllElements();
                    for(int i=0;i<coll.get(collections.getSelectedRow()).getThoughtsCount();i++){
                        dlm.addElement(coll.get(collections.getSelectedRow()).getThoughts(i));
                    }
                    list.setFont(new Font("Verdana", Font.PLAIN, 13));
                    list.setLayoutOrientation(JList.VERTICAL);
                    list.setVisibleRowCount(3);
                    JScrollPane scroll = new JScrollPane(list);
                    scroll.setSize(150,75);
                    scroll.setLocation(30,100);
                    jf.add(scroll);
                    jf.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            openedShowWindow=false;
                        }
                    });
                    jf.setVisible(true);
                }
            });}
    }
}
