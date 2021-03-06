/**
 * Created by Mugenor on 14.04.2017.
 */
/**
 * Created by Mugenor on 14.04.2017.
 */
import classes.Karlson;
import classes.KarlsonNameException;
import classes.NormalHuman;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditWindow extends JFrame {
    private static Color c = null;
    private boolean opened = false;
    private boolean legalAge = true;
    private int numberRow=-1;
    private CollectTable collections;
    private LinkedList<NormalHuman> linkedList;
    private JPanel panel = new JPanel();
    private NormalHuman nh = new NormalHuman();
    private JTextField field = new JTextField(20);
    private JTextField thoughtsField = new JTextField();
    private JLabel label = new JLabel(ResourceBundle.getBundle("Locale",Interface.getLocale()).getString("Age"));
    private JButton Add = new JButton(ResourceBundle.getBundle("Locale",Interface.getLocale()).getString("Add"));
    private JButton Del = new JButton(ResourceBundle.getBundle("Locale",Interface.getLocale()).getString("Delete"));
    private JRadioButton True = new JRadioButton(ResourceBundle.getBundle("Locale",Interface.getLocale()).getString("true"));
    private JRadioButton False = new JRadioButton(ResourceBundle.getBundle("Locale",Interface.getLocale()).getString("false"));
    private JButton ok = new JButton(ResourceBundle.getBundle("Locale",Interface.getLocale()).getString("Ok"));
    private JButton canc = new JButton(ResourceBundle.getBundle("Locale",Interface.getLocale()).getString("Cancel"));
    private JLabel name = new JLabel(ResourceBundle.getBundle("Locale",Interface.getLocale()).getString("Name"));
    private JLabel trobL = new JLabel(ResourceBundle.getBundle("Locale",Interface.getLocale()).getString("TroublesWithTheLaw"));
    private DefaultListModel<String> dlm = new DefaultListModel();
    private JList<String> list = new JList(dlm);
    private SpinnerNumberModel snm = new SpinnerNumberModel(1,1,100,1);
    private JSpinner spin = new JSpinner(snm);
    private JTextField tf = ((JSpinner.DefaultEditor) spin.getEditor()).getTextField();
    private JLabel excNameLabel= new JLabel();
    private JLabel excAgeLabel = new JLabel();
    public EditWindow(){}
    public EditWindow(String name, CollectTable collections, LinkedList<NormalHuman> linkedList , EditExit ee){
        setTitle(name);
            this.collections = collections;
            this.linkedList = linkedList;
            try {
                nh = new NormalHuman();
                nh.setName("SetName");
            } catch (KarlsonNameException e) {
            }

            init();
            setExitOpereation(ee);
    }
    public EditWindow(String name, NormalHuman nh, CollectTable collections, int numberRow, EditExit ee){
            setTitle(name);
            this.numberRow = numberRow;
            this.nh = nh;
            this.collections = collections;
            init();
            setExitOpereation(ee);
    }
    public  void setColor(Color colo){
        c = colo;
        doColors();
    }
    public void updateLocale(){
        try {
            Add.setText(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Add"));
            label.setText(ResourceBundle.getBundle("Locale",Interface.getLocale()).getString("Age"));
            Del.setText(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Delete"));
            True.setText(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("true"));
            False.setText(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("false"));
            ok.setText(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Ok"));
            canc.setText(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Cancel"));
            name.setText(ResourceBundle.getBundle("Locale",Interface.getLocale()).getString("Name"));
            this.setTitle(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("AddPerson"));
            trobL.setText(ResourceBundle.getBundle("Locale",Interface.getLocale()).getString("TroublesWithTheLaw"));
        }catch(Exception e){e.printStackTrace();}
    }
    public boolean isOpened(){
        return opened;
    }
    private void doColors(){
        if(c!=null)tf.setForeground(c);
        if(c!=null)ok.setBackground(c);
        if(c!=null)Add.setBackground(c);
        if(c!=null)Del.setBackground(c);
        if(c!=null)field.setSelectionColor(c);
        if(c!=null)canc.setBackground(c);
        if(c!=null)ok.setBackground(c);
    }
    private void init() {
        for (int i = 0; i < nh.getThoughtsCount(); i++) {
            dlm.addElement(nh.getThoughts(i));
        }
        setLocationRelativeTo(null);
        setFocusable(true);
        setResizable(false);
        setSize(300, 420);
        panel.setBackground(Color.white);
        panel.setLayout(null);
        panel.setFocusable(true);
        setFocusable(true);
        add(panel);
        field.setSize(240, 20);
        field.setLocation(27, 30);
        field.setText(nh.getName());
        //
        label.setLocation(28, 48);
        label.setSize(50, 30);
        label.setFont(new Font("Verdana", Font.PLAIN, 13));
        //
        name.setLocation(27, 0);
        name.setSize(50, 30);
        name.setFont(new Font("Verdana", Font.PLAIN, 13));
        //
        spin.setSize(60, 20);
        tf.setText(nh.getAge().toString());
        tf.setEditable(true);

        spin.setLocation(70, 55);
        spin.setValue(nh.getAge());
        //
        JLabel trob = new JLabel("");
        trob.setLocation(27, 70);
        trob.setSize(80, 30);
        trob.setFont(new Font("Verdana", Font.BOLD, 13));
        //
        thoughtsField.setSize(new Dimension(200, 20));
        thoughtsField.setLocation(new Point(27, 180));
        //
        trobL.setLocation(27, 200);
        trobL.setSize(180, 30);
        trobL.setFont(new Font("Verdana", Font.BOLD, 13));
        //
        ButtonGroup group = new ButtonGroup();
        if (nh.getTroublesWithTheLaw()) True.setSelected(true);
        else False.setSelected(true);
        False.setFont(new Font("Verdana", Font.PLAIN, 12));
        True.setFont(new Font("Verdana", Font.PLAIN, 12));
        True.setSize(80, 30);
        False.setSize(80, 30);
        True.setLocation(30, 230);
        False.setLocation(29, 252);
        group.add(True);
        group.add(False);
        True.setForeground(Color.BLACK);
        True.setBackground(Color.WHITE);
        False.setForeground(Color.BLACK);
        False.setBackground(Color.WHITE);
        //
        canc.setSize(80, 30);
        canc.setLocation(40, 285);
        ok.setSize(80, 30);
        ok.setLocation(160, 285);
        //

        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(3);
        list.setFont(new Font("Verdana", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(list);
        scroll.setSize(150, 75);
        scroll.setLocation(30, 100);
        //

        Add.setSize(80, 30);
        Add.setLocation(200, 100);
        Del.setSize(80, 30);
        Del.setLocation(200, 143);
        doColors();
        excAgeLabel.setForeground(Color.RED);
        excAgeLabel.setLocation(130,45);
        excAgeLabel.setSize(500,30);
        excAgeLabel.setFont(new Font("Verdana", Font.BOLD, 13));
        panel.add(excAgeLabel);
        excNameLabel.setForeground(Color.RED);
        excNameLabel.setLocation(70,0);
        excNameLabel.setSize(500,30);
        excNameLabel.setFont(new Font("Verdana", Font.BOLD, 13));
        panel.add(excNameLabel);
        panel.add(True);
        panel.add(name);
        panel.add(False);
        panel.add(Add);
        panel.add(Del);
        panel.add(spin);
        panel.add(trob);
        panel.add(trobL);
        panel.add(field);
        panel.add(label);
        panel.add(ok);
        panel.add(canc);
        panel.add(scroll);
        panel.add(thoughtsField);
        addListeners();
        setLocation(1000,150);
        setVisible(true);
    }
    private void addThought(){
        if(!thoughtsField.getText().trim().equals("")){
            nh.thinkAbout(thoughtsField.getText());
            dlm.addElement(nh.getThoughts(nh.getThoughtsCount()-1));
        }
    }
    private void deleteThought(){
        if(list.getSelectedIndex()!=-1){
        nh.forgetThought(list.getSelectedIndex());
        dlm.remove(list.getSelectedIndex());}
    }
    private void exit(EditExit ee){
        if(!legalAge){
            legalAge=true;
            excAgeLabel.setText("");
            return;
        }
        try{
            if(field.getText().equals("")) throw new KarlsonNameException("empty");
            nh.setName(field.getText());
            nh.setAge(snm.getNumber().longValue());
            nh.setTroublesWithTheLaw(True.isSelected());
            Interface.message.getData().clear();
            Interface.message.getData().add(nh);
            if(numberRow==(-1)) {
                Interface.message.maxID++;
                nh.setId(Interface.message.maxID);
                collections.addData(nh);
                linkedList.add(nh);
                Interface.message.setTypeOfOperation(Message.add);}
            else {
                collections.editData(nh, numberRow);
                Interface.notEditable.remove(nh.getId());
                Interface.message.setTypeOfOperation(Message.change);
                Interface.message.reinitialize(Interface.notEditable);
            }
            ee.doOnExit();
            Interface.message.setState(ConnectionState.NEW_DATA);
            Interface.sendMessage();
            dispose();
        }catch(KarlsonNameException exc){
            if(exc.getMessage().equals("empty"))excNameLabel.setText(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Write name of NormalHuman"));
                    else excNameLabel.setText("अल्लाह अकबर");
            panel.updateUI();
        }
    }
    public void setExitOpereation(EditExit ee){
        EditWindow ew = this;

        field.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit(ee);
            }
        });

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit(ee);
                Interface.setIsChanged(true);
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getExtendedKeyCode()==KeyEvent.VK_ENTER){
                    exit(ee);
                }
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(numberRow!=-1) {
                    Interface.notEditable.remove(Interface.coll.get(numberRow).getId());
                    Interface.message.getData().clear();
                    Interface.message.reinitialize(Interface.notEditable);
                    Interface.message.setTypeOfOperation(Message.notEdit);
                    Interface.message.setState(ConnectionState.NEW_DATA);
                    Interface.sendMessage();
                }
            }
        });
        canc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(numberRow!=-1) {
                    Interface.notEditable.remove(Interface.coll.get(numberRow).getId());
                    Interface.message.getData().clear();
                    Interface.message.reinitialize(Interface.notEditable);
                    Interface.message.setTypeOfOperation(Message.notEdit);
                    Interface.message.setState(ConnectionState.NEW_DATA);
                    Interface.sendMessage();
                }
                ew.dispose();
                ee.doOnExit();
            }
        });
    }
    private void addListeners(){
        thoughtsField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addThought();
            }
        });
        Del.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteThought();
            }
        });
        Add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addThought();
            }
        });
        list.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if(e.getKeyChar()==KeyEvent.VK_DELETE){
                    if(list.getSelectedIndex()!=-1)
                        deleteThought();
                }
            }
        });
        Pattern pat = Pattern.compile("0*[0-9]?[0-9]?");
        tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                Matcher mat=pat.matcher(tf.getText());
                if(!mat.matches() || tf.getText().equals("")){
                    legalAge=false;
                    excAgeLabel.setText("Illegal age");
                    panel.updateUI();
                } else {legalAge=true;
                        excAgeLabel.setText("");
                        panel.updateUI();}
            }
        });
    }
}