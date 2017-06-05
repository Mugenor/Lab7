import classes.KarlsonNameException;
import classes.NormalHuman;
import org.json.simple.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.io.*;
import java.util.ResourceBundle;

/**
 * Created by Mugenor on 13.04.2017.
 */
public class ButtonsWithCommands {
    private JButton resume=new JButton(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Resume"));
    private JButton stop=new JButton(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Stop"));
    private JButton pause=new JButton(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Pause"));
    private JButton play=new JButton(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Play"));
    private JFrame jf1= new JFrame(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Remove"));
    private JLabel alabel = new JLabel("");
    private Color c = null;
    private JFrame jf2 = new JFrame(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("AddInJson"));
    private JLabel label= new JLabel(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("PutHere"));
    private boolean openedRemoveWindow=false;
    private boolean openedAddJsonWindow=false;
    private boolean openedAddWindow=false;
    private boolean openedHustWindow=false;
    private boolean openedFilterWindow=false;
    private JFrame jf = new JFrame();
    private JList<String> listCommands;
    private LinkedList<NormalHuman> coll;
    private CollectTable collt;
    private JTable collections;
    private static File f = new File(System.getenv("Source") + "\\Rammstein_-_Du_hast.wav");
    private static MyPlayer sound = new MyPlayer(f);
    private EditWindow ew = new EditWindow();
    public void setColor(Color colo){
        c = colo;
        if(colo!=null) {
            play.setBackground(colo);
            stop.setBackground(colo);
            resume.setBackground(colo);
            pause.setBackground(colo);
            ew.setColor(c);
        }
    }
    public void updateLanguage(){
        ew.updateLocale();
        resume.setText(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Resume"));
        stop.setText(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Stop"));
        pause.setText(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Pause"));
        play.setText(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Play"));
        jf.setTitle(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("DoHust"));
        jf1.setTitle(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Remove"));
        label.setText(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("PutHere"));
        jf2.setTitle(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("AddInJson"));
        alabel.setText("");
    }
    ButtonsWithCommands(JList<String> listCommands, LinkedList<NormalHuman> coll, CollectTable collt, JTable collections){
        this.listCommands=listCommands;
        this.coll=coll;
        this.collt=collt;
        this.collections=collections;
    }
    public void doCommand(){
        switch(listCommands.getSelectedIndex()){
            case 0: remove();
                break;
            case 1: addPerson();
                break;
            case 2: addInJson();
                break;
            case 3: hust();
                break;
            case 4: filter();
        }
    }
    public void filter(){
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JDialog language = new JDialog();
                    language.setModal(true);
                    JButton ok = new JButton(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Ok"));
                    JButton cancel = new JButton(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Reset"));
                    language.setLayout(null);
                    language.setForeground(Color.white);
                    language.setResizable(false);
                    language.setAutoRequestFocus(true);
                    language.setLocation(600,250);
                    language.setSize(400,200);
                    ok.setLocation(28,120);
                    cancel.setLocation(250,120);
                    ok.setSize(100,30);
                    cancel.setSize(100,30);
                    JCheckBox rus = new JCheckBox(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Name"));
                    JCheckBox isl = new JCheckBox(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Age"));
                    JCheckBox isp = new JCheckBox(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("TroublesWithTheLaw"));
                    JCheckBox grec = new JCheckBox(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Time"));
                    //
                    JRadioButton fl = new JRadioButton(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("false"));
                    JRadioButton tr = new JRadioButton(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("true"));
                    JTextField name = new JTextField(20);
                    name.setSize(180,20);
                    name.setLocation(200,15);
                    SpinnerNumberModel snm = new SpinnerNumberModel(1,1,100,1);
                    SpinnerNumberModel snm1 = new SpinnerNumberModel(1,1,100,1);
                    JSpinner spin = new JSpinner(snm);
                    spin.setSize(70,20);
                    JSpinner spin1 = new JSpinner(snm1);
                    spin1.setSize(70,20);
                    spin.setLocation(200,38);
                    spin1.setLocation(300,38);
                    JTextField date = new JTextField(20);
                    date.setSize(70,20);
                    date.setLocation(200,90);
                    JTextField time = new JTextField(20);
                    time.setSize(70,20);
                    time.setLocation(300,90);
                    //
                    fl.setSelected(true);
                    ButtonGroup gr = new ButtonGroup();
                    fl.setSize(80,20);
                    tr.setSize(80,20);
                    gr.add(fl);
                    gr.add(tr);
                    fl.setLocation(200,62);
                    tr.setLocation(300,62);
                    rus.setFont(new Font("Verdana", Font.PLAIN, 12));
                    isl.setFont(new Font("Verdana", Font.PLAIN, 12));
                    grec.setFont(new Font("Verdana", Font.PLAIN, 12));
                    isp.setFont(new Font("Verdana", Font.PLAIN, 12));
                    rus.setSize(80, 30);
                    isl.setSize(180, 30);
                    grec.setSize(180, 30);
                    isp.setSize(180, 30);
                    rus.setLocation(10, 10);
                    isl.setLocation(10, 35);
                    isp.setLocation(10, 60);
                    grec.setLocation(10, 85);
                    ok.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(rus.isSelected())
                                ;
                            if(isl.isSelected())
                                ;
                            if(isp.isSelected())
                                ;
                            if(grec.isSelected())
                                ;
                        }
                    });
                    cancel.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ;
                        }
                    });
                    language.add(date);
                    language.add(time);
                    language.add(spin);
                    language.add(spin1);
                    language.add(name);
                    language.add(fl);
                    language.add(tr);
                    language.add(ok);
                    language.add(cancel);
                    language.add(rus);
                    language.add(isl);
                    language.add(isp);
                    language.add(grec);
                    //
                    language.setVisible(true);
                }
            });
    }
    public void remove(){
        if(!openedRemoveWindow)
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    openedRemoveWindow=true;
                    jf1 = new JFrame(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Remove"));
                    JTextField tf = new JTextField("", 50);
                    simpleFrame(jf1, label, alabel, tf);
                    if(coll.size()!=0)
                    tf.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String s = tf.getText();
                            try {
                                NormalHuman nh = Interface.StringToObject(s);
                                int i = coll.indexOf(nh);
                                int id = coll.get(i).getId();
                                if (i != -1) {
                                    System.out.println(Interface.notEditable + " : " + id);
                                    if (Interface.notEditable.contains(id)) throw new IllegalArgumentException();
                                    collt.removeData(i);
                                    coll.remove(nh);
                                    System.out.println(coll);
                                    Interface.message.getData().clear();
                                    Interface.message.getData().add(nh);
                                    Interface.message.setTypeOfOperation(Message.delete);
                                    Interface.message.setState(ConnectionState.NEW_DATA);
                                    Interface.sendMessage();
                                    jf1.dispose();
                                    openedRemoveWindow = false;
                                }
                            }
                            catch (NullPointerException | KarlsonNameException exc){
                                alabel.setText(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("WrongNormalHuman"));
                            }
                            catch (ArrayIndexOutOfBoundsException exc){
                                alabel.setText(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("NoNormalHumanLikeThis"));
                            }
                            catch (IllegalArgumentException exc){
                                new Dialog("Данный человек ещё редактируется!!!",Interface.getColor());
                            }
                        }
                    }); else alabel.setText(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("NothingToRemove"));
                    jf1.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            openedRemoveWindow=false;
                        }
                    });
                }
            });
    }
    public void save() {
       /* Thread t= new Thread(new Runnable() {
            @Override
            public void run() {
                File f = new File(Interface.getFile());
                FileOutputStream FOS=null;
                PrintWriter pw=null;
                try{
                    FOS = new FileOutputStream(f);
                    pw = new PrintWriter(FOS, true);
                    for (int i = 0; i < coll.size(); i++) {
                        JSONArray ar = new JSONArray();
                        for (int j = 0; j < coll.get(i).countOfThoughts(); j++) {
                            JSONObject itemOfCollectionThoughts = new JSONObject();
                            itemOfCollectionThoughts.put("thought", coll.get(i).getThoughts(j));
                            ar.add(itemOfCollectionThoughts);
                        }
                        JSONObject obj = new JSONObject();
                        obj.put("age", coll.get(i).getAge());
                        obj.put("name", coll.get(i).getName());
                        obj.put("troublesWithTheLaw", coll.get(i).getTroublesWithTheLaw());
                        obj.put("thoughts", ar);
                        pw.println(obj.toString());
                        Interface.setIsChanged(false);
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("File not found");
                } catch (NullPointerException e){
                    System.out.println("Null point");
                }
                finally{
                    try{Interface.setIsChanged(false);
                        FOS.close();
                        pw.close();
                        System.out.println("Collection saved!");}
                    catch(IOException | NullPointerException e)
                    {
                        System.out.println("Can't save collection into " + f);
                    }
                }
            }
        });
        t.start();*/
    }
    public void addPerson(){
        if(!openedAddWindow) {
            openedAddWindow=true;
            ew = new EditWindow(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("AddPerson"), collt, coll, new EditExit() {
                @Override
                public void doOnExit() {
                    openedAddWindow = false;
                }
            });
            ew.setLocation((int)Interface.getFrameLocation().getX()+620,(int)Interface.getFrameLocation().getY());
            ew.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    openedAddWindow=false;
                }
            });
        }
    }
    public void addInJson(){
        if(!openedAddJsonWindow) {
            openedAddJsonWindow=true;
            jf2 = new JFrame(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("AddInJson"));
            JLabel alabel = new JLabel("");
            JTextField tf = new JTextField("", 50);
            simpleFrame(jf2, label, alabel, tf);
            tf.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String str = tf.getText();
                    try {
                        NormalHuman nh = Interface.StringToObject(str);
                        String[] strArray = {nh.getName(), nh.getAge().toString(), nh.getTroublesWithTheLaw().toString()};
                        System.out.println("Old coll: " + coll);
                        Interface.message.maxID++;
                        nh.setId(Interface.message.maxID);
                        collt.addData(strArray);
                        coll.add(nh);
                        System.out.println("New coll: " + coll);
                        System.out.println(coll.indexOf(nh)+ " of " + coll.size()+": " + nh);
                        Interface.setIsChanged(true);
                        openedAddJsonWindow = false;
                        Interface.message.getData().clear();
                        Interface.message.getData().add(nh);
                        Interface.message.setTypeOfOperation(Message.add);
                        Interface.message.setState(ConnectionState.NEW_DATA);
                        Interface.sendMessage();
                        nh.setTimeOfCreate();
                        jf2.dispose();
                    } catch (NullPointerException | KarlsonNameException exc) {
                        alabel.setText(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("WrongNormalHuman"));
                    }
                }
            });
            jf2.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    openedAddJsonWindow=false;
                }
            });
        }
    }
    public void hust() {
        if(!openedHustWindow&&sound.isReleased()){openedHustWindow=true;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try{
                        JFrame jf = new JFrame();
                        jf.setLocation(300,30);
                        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        jf.setResizable(false);
                        jf.setTitle(ResourceBundle.getBundle("Locale",Interface.getLocale()).getString("DoHust"));
                        jf.setLayout(new GridBagLayout());
                        play.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                sound.setVolume(1f);
                                sound.play();
                            }
                        });
                        stop.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                sound.stop();
                            }
                        });
                        pause.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                sound.pause();
                            }
                        });
                        resume.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                sound.resume();
                            }
                        });
                        ImageIcon du_hust= new ImageIcon(System.getenv("Source") + "\\du_hust.jpg");
                        JLabel du = new JLabel(du_hust);
                        jf.add(du, new GridBagConstraints(0,0,4,1,0,
                                0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(10,0,0,0),0,0));
                        jf.add(play, new GridBagConstraints(0,1,1,1,0,
                                0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(10,200,10,10),0,0));
                        jf.add(stop, new GridBagConstraints(1,1,1,1,0,
                                0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(10,10,10,10),0,0));
                        jf.add(pause, new GridBagConstraints(2,1,1,1,0,
                                0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(10,10,10,10),0,0));
                        jf.add(resume, new GridBagConstraints(3,1,1,1,0,
                                0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(10,10,10,190),0,0));
                        jf.pack();
                        jf.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosing(WindowEvent e) {
                                openedHustWindow=false;
                                sound.stop();
                            }
                        });
                        System.out.println(jf.getSize());
                        jf.setVisible(true);}catch (Exception e){}

                }
            });}
    }
    private void simpleFrame(JFrame jf, JLabel label, JLabel alabel, JTextField tf){
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.setResizable(false);
        jf.setSize(new Dimension(600,100));
        jf.setLocationRelativeTo(null);
        jf.setLayout(new FlowLayout());
        jf.add(label);
        jf.add(tf);
        jf.add(alabel);
        jf.setVisible(true);
    }
}
