import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import classes.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.*;
public class Interface{
    static HashSet<Integer> notEditable;
    static Socket secondSocket;
    static ObjectOutputStream dos;
    static ObjectInputStream dis;
    static Socket socket;
    static InputStream socketIS;
    static OutputStream socketOS;
    static Message message;
    static Gson gson;
    static ByteArrayOutputStream baos;
    static ByteArrayInputStream bais;
    private static boolean isChanged = false;
    private static JFrame jf = new JFrame();
    private static JPanel panelu = new JPanel();
    private static JPanel paneld= new JPanel(null);
    private static JPanel panelc= new JPanel();
    private static Locale locale = new Locale("ru");
    private static JFrame colorFrame = new JFrame(ResourceBundle.getBundle("Locale",locale).getString("ChooseColor"));
    private static JButton showThoughtsButton = new JButton(ResourceBundle.getBundle("Locale",locale).getString("ShowThoughts"));
    private static JButton editButton = new JButton(ResourceBundle.getBundle("Locale",locale).getString("Edit"));
    private static JButton deleteButton = new JButton(ResourceBundle.getBundle("Locale",locale).getString("Delete"));
    private static JButton ok = new JButton(ResourceBundle.getBundle("Locale",locale).getString("Ok"));
    private static JFrame language = new JFrame("Choose language!");
    private static String file="";
    private static Color color=null;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ResourceBundle.getBundle("Locale",locale).getString("DateTime"));
    private static JButton colorChooserButton = new JButton(ResourceBundle.getBundle("Locale",locale).getString("ChooseColor"));
    private static JButton languageChooserButton = new JButton(ResourceBundle.getBundle("Locale",locale).getString("ChooseLanguage"));
    private static JButton sortButton = new JButton(ResourceBundle.getBundle("Locale",locale).getString("Sort"));
    static LinkedList<NormalHuman> coll =null;
    private static DefaultListModel<String> dlm= new DefaultListModel<>();
    private static JList<String> listCommands = new JList<>(dlm);
    private static JButton doButton = new JButton(ResourceBundle.getBundle("Locale",locale).getString("Do"));
    private static CollectTable collt = new CollectTable();
    static JTable collections = new JTable(collt);
    private static ButtonsUnderTable but=null;
    private static ButtonsWithCommands bwc=null;
    private static JButton cb = new JButton(ResourceBundle.getBundle("Locale",locale).getString("ChooseColor"));
    private static CloseFrame cf = new CloseFrame(bwc);
    public static void setIsChanged(boolean changed){isChanged = changed;}
    public static Point getFrameLocation(){return jf.getLocation();}
    public synchronized static String getFile(){return file;}
    public static Color getColor() {return color;}
    public static DateTimeFormatter getFormatter() {return formatter;}
    public static void setFormatter(DateTimeFormatter formatter) {Interface.formatter = formatter;}
    public static Locale getLocale() {return locale;}
    public static void setLocale(Locale locale) {
        Interface.locale = locale;
        cb.setText(ResourceBundle.getBundle("Locale",locale).getString("ChooseColor"));
        colorFrame.setTitle(ResourceBundle.getBundle("Locale",locale).getString("ChooseColor"));
        colorChooserButton.setText(ResourceBundle.getBundle("Locale",locale).getString("ChooseColor"));
        languageChooserButton.setText(ResourceBundle.getBundle("Locale",locale).getString("ChooseLanguage"));
        showThoughtsButton.setText(ResourceBundle.getBundle("Locale",locale).getString("ShowThoughts"));
        language.setTitle(ResourceBundle.getBundle("Locale",locale).getString("ChooseLanguage"));
        deleteButton.setText(ResourceBundle.getBundle("Locale",locale).getString("Delete"));
        ok.setText(ResourceBundle.getBundle("Locale",locale).getString("Ok"));
        editButton.setText(ResourceBundle.getBundle("Locale",locale).getString("Edit"));
        doButton.setText(ResourceBundle.getBundle("Locale",locale).getString("Do"));
        jf.setTitle(ResourceBundle.getBundle("Locale",locale).getString("MalishAndKarlson"));
        sortButton.setText(ResourceBundle.getBundle("Locale",locale).getString("Sort"));
        bwc.updateLanguage();
        but.updateLanguage();
        collt.updateLanguage();
        collections.getColumnModel().getColumn(3).setResizable(false);
        collections.getColumnModel().getColumn(2).setResizable(false);
        collections.getColumnModel().getColumn(1).setResizable(false);
        collections.getColumnModel().getColumn(0).setResizable(false);
        dlm.clear();
        dlm.addElement(ResourceBundle.getBundle("Locale",locale).getString("Remove"));
        dlm.addElement(ResourceBundle.getBundle("Locale",locale).getString("AddPerson"));
        dlm.addElement(ResourceBundle.getBundle("Locale",locale).getString("AddInJson"));
        dlm.addElement(ResourceBundle.getBundle("Locale",locale).getString("hust"));
        dlm.addElement(ResourceBundle.getBundle("Locale",locale).getString("Filter"));
    }
    private static void languageChooser(){
        language.setLayout(null);
        language.setForeground(Color.white);
        language.setResizable(false);
        language.setAutoRequestFocus(true);
        language.setLocation(600,250);
        language.setSize(new Dimension(150,200));
        ok.setLocation(36,120);
        ok.setSize(70,30);
        ButtonGroup group = new ButtonGroup();
        JRadioButton rus = new JRadioButton("Русский");
        JRadioButton isl = new JRadioButton("Íslensk");
        JRadioButton grec = new JRadioButton("Ελληνικά");
        JRadioButton isp = new JRadioButton("Español");
        rus.setSelected(true);
        rus.setFont(new Font("Verdana", Font.PLAIN, 12));
        isl.setFont(new Font("Verdana", Font.PLAIN, 12));
        grec.setFont(new Font("Verdana", Font.PLAIN, 12));
        isp.setFont(new Font("Verdana", Font.PLAIN, 12));
        rus.setSize(80, 30);
        isl.setSize(100, 30);
        grec.setSize(100, 30);
        isp.setSize(80, 30);
        rus.setLocation(10, 10);
        isl.setLocation(10, 35);
        grec.setLocation(10, 60);
        isp.setLocation(10, 85);
        group.add(rus);
        group.add(isl);
        group.add(isp);
        group.add(grec);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(rus.isSelected())
                    setLocale(new Locale("ru"));
                if(isl.isSelected())
                    setLocale(new Locale("isl"));
                if(isp.isSelected())
                    setLocale(new Locale("isp"));
                if(grec.isSelected())
                    setLocale(new Locale("gr"));
            }
        });
        language.add(ok);
        language.add(rus);
        language.add(isl);
        language.add(isp);
        language.add(grec);
        //
        language.setVisible(true);

    }
    private static void sort(){
        List<NormalHuman> c = coll.stream().sorted().collect(Collectors.toList());
        coll.clear();
        coll.addAll(c);
        collt.removeAll();
        for(int i=0;i<coll.size();i++){
            String[] obj = {coll.get(i).getName(),coll.get(i).getAge().toString(), ResourceBundle.getBundle("Locale", Interface.getLocale()).getString(coll.get(i).getTroublesWithTheLaw().toString()),coll.get(i).getTimeOfCreate().format(formatter)};
            collt.addData(obj);
        }
    }
    private static void colorChooser(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                colorFrame = new JFrame(ResourceBundle.getBundle("Locale",locale).getString("ChooseColor"));
                colorFrame.setAutoRequestFocus(true);
                colorFrame.setSize(new Dimension(700,400));
                colorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                colorFrame.setLocationRelativeTo(null);
                colorFrame.setLayout(new GridBagLayout());
                JColorChooser cc = new JColorChooser();
                colorFrame.add(cc, new GridBagConstraints(
                        0,0,3,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,
                        new Insets(0,0,0,0),0,0));
                cb.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        colorFrame.dispose();
                        color=cc.getColor();
                        cb.setBackground(color);
                        ok.setBackground(color);
                        showThoughtsButton.setBackground(color);
                        editButton.setBackground(color);
                        deleteButton.setBackground(color);
                        sortButton.setBackground(color);
                        colorChooserButton.setBackground(color);
                        listCommands.setForeground(color);
                        doButton.setBackground(color);
                        collections.setForeground(color);
                        but.setColor(color);
                        bwc.setColor(color);
                        cf.setColor(color);
                        languageChooserButton.setBackground(color);
                    }
                });
                colorFrame.add(cb, new GridBagConstraints(
                        2,1,1,1, 1,1, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                        new Insets(0,0,0,0), 0, 0));
                colorFrame.setVisible(true);
            }
        });
    }
    Interface(){
        but =  new ButtonsUnderTable(collections, collt, coll);
        bwc = new ButtonsWithCommands(listCommands, coll, collt, collections);
        showThoughtsButton.setFont(new Font("Verdana", Font.BOLD,13));
        editButton.setFont(new Font("Verdana", Font.BOLD,13));
        deleteButton.setFont(new Font("Verdana", Font.BOLD,13));
        jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jf.setResizable(false);
        jf.setSize(new Dimension(600,400));
        jf.setTitle("Малыш и Карлсон");
        jf.setLocationRelativeTo(null);
        jf.setLayout(new GridLayout(3,1));
        jf.addWindowListener(new WindowAdapter() {
        });
        jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                message.clearData();
                message.setState(ConnectionState.DISCONNECT);
                sendMessage();
                System.exit(0);
            }
        });
        panelu.setBackground(Color.white);
        panelc.setBackground(Color.white);
        paneld.setBackground(Color.white);
        for(int i=0;i<coll.size();i++){
            String[] obj = {coll.get(i).getName(),coll.get(i).getAge().toString(), ResourceBundle.getBundle("Locale", Interface.getLocale()).getString(coll.get(i).getTroublesWithTheLaw().toString()),coll.get(i).getTimeOfCreate().format(formatter)};
            collt.addData(obj);
        }
        collections.setForeground(Color.BLACK);
        collections.getColumnModel().getColumn(3).setResizable(false);
        collections.getColumnModel().getColumn(2).setResizable(false);
        collections.getColumnModel().getColumn(1).setResizable(false);
        collections.getColumnModel().getColumn(0).setResizable(false);
        collections.getTableHeader().setReorderingAllowed(false);
        collections.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(collections);
        scroll.setPreferredSize(new Dimension(300,500));
        panelu.setLayout(new GridBagLayout());
        panelu.add(scroll,new GridBagConstraints(0,0,1,1,1,1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,new Insets(1,1,1,1)
                ,0,0));
        panelu.setVisible(true);
        jf.add(panelu);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                but.delete();
            }
        });
        showThoughtsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                but.showThoughts();
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                but.edit();
            }
        });

        collections.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2 && e.getButton()==MouseEvent.BUTTON1) but.edit();
            }
        });
        doButton.setFont(new Font("Verdana", Font.BOLD, 14));
        dlm.addElement(ResourceBundle.getBundle("Locale",locale).getString("Remove"));
        dlm.addElement(ResourceBundle.getBundle("Locale",locale).getString("AddPerson"));
        dlm.addElement(ResourceBundle.getBundle("Locale",locale).getString("AddInJson"));
        dlm.addElement(ResourceBundle.getBundle("Locale",locale).getString("hust"));
        dlm.addElement(ResourceBundle.getBundle("Locale",locale).getString("Filter"));
        listCommands.setFont(new Font("Verdana", Font.PLAIN, 12));
        listCommands.setForeground(Color.BLUE);
        listCommands.setBackground(Color.white);
        listCommands.setBackground(Color.LIGHT_GRAY.brighter());
        listCommands.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        doButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bwc.doCommand();
            }
        });

        panelc.setLayout(new GridBagLayout());
        panelc.add(deleteButton,new GridBagConstraints(2, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,150), 0,0));
        panelc.add(showThoughtsButton,
                new GridBagConstraints(0, 0, 1, 1, 1, 1,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,150,0,0), 0,0));
        panelc.add(editButton,new GridBagConstraints(1, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0,0));
        panelc.add(doButton, new GridBagConstraints(0,1,1,2,1,1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL
                ,new Insets(0,200,0,0), 0,0));
        panelc.add(listCommands, new GridBagConstraints(1,1,2,2,1,1, GridBagConstraints.CENTER,
                GridBagConstraints.VERTICAL, new Insets(0,0,0,200), 0, 0));

        jf.add(panelc);
        colorChooserButton.setFont(new Font("Verdana", Font.BOLD,15));
        sortButton.setFont(new Font("Verdana", Font.BOLD,15));
        sortButton.setLocation(0,50);
        sortButton.setSize(140,40);
        languageChooserButton.setFont(new Font("Verdana", Font.BOLD,15));
        languageChooserButton.setSize(270,40);
        languageChooserButton.setLocation(330,50);
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sort();
            }
        });
        colorChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorChooser();
            }
        });
        languageChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {languageChooser();}
        });
        colorChooserButton.setSize(200,40);
        colorChooserButton.setLocation(130,50);
        paneld.add(colorChooserButton);
        paneld.add(languageChooserButton);
        paneld.add(sortButton);
        jf.add(paneld);
        jf.setVisible(true);
    }

    public static LinkedList<String> fromFileToString(String path) throws FileNotFoundException, SecurityException, IOException{
        Scanner sc=null;
        try {
            LinkedList<String> lines = new LinkedList<String>();
            File f = new File(path);
            if(f.exists()&&(!f.canRead()|| !f.canWrite())) throw new SecurityException();
            if(f.isDirectory()) throw new IOException();
            FileReader fin = new FileReader(f);
            sc = new Scanner(fin);
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
            return lines;
        }
        finally {
            if(sc!=null)
                sc.close();

        }
    }

    public static ArrayList<String> firstParse(List<String> lines) {
        ArrayList<String> jsonLines = new ArrayList<String>();
        int x = 0, y = 0, i1 = 0;
        int[] lastObj = {0, 0};
        while (i1 < lines.size()) {
            String a = lines.get(i1);
            for (int i2 = 0; i2 < a.length(); i2++) {
                if (a.charAt(i2) == '{') x++;
                if (a.charAt(i2) == '}') y++;
                if ((x != 0) && (y != 0) && (x == y)) {
                    StringBuffer sb = new StringBuffer("");
                    if (lastObj[0] == i1) sb.append(lines.get(i1).substring(lastObj[1], i2 + 1));
                    else {
                        sb.append(lines.get(lastObj[0]).substring(lastObj[1] + 1, lines.get(lastObj[0]).length()));
                        for (int j1 = lastObj[0] + 1; j1 < i1; j1++) {
                            sb = sb.append(lines.get(j1));
                        }
                        sb.append(lines.get(i1).substring(0, i2 + 1));
                    }
                    jsonLines.add(sb.toString());
                    lastObj[0] = i1;
                    lastObj[1] = i2;
                    x = 0;
                    y = 0;
                }
            }
            i1++;
        }
        return jsonLines;
    }

    public static LinkedList<NormalHuman> toObject(LinkedList<NormalHuman> coll, ArrayList<String> jsonLines) {
        int a=0;
        for (int i = 0; i < jsonLines.size(); i++) {
            NormalHuman nh;
            try{nh = StringToObject(jsonLines.get(i));
                coll.add(nh);}catch (NullPointerException | KarlsonNameException e){a++;}
        }
        if(a==1)System.out.println(a + " of NormalHuman's is not correct in the file");
        if(a>1) System.out.println(a + " of NormalHuman's are not correct in the file");
        return coll;
    }

    public static NormalHuman StringToObject(String str) throws KarlsonNameException, NullPointerException  {

        NormalHuman nh = new NormalHuman();
        JSONObject obj = (JSONObject) JSONValue.parse(str);
        JSONArray ar = (JSONArray) obj.get("thoughts");
        for (int j = 0; j < ar.size(); j++) {
            String th = null;
            try{JSONObject object = (JSONObject) ar.get(j);
                th = (String) object.get("thought");}catch(NullPointerException e){}
            nh.thinkAbout(th);
            }
        Long age=5l;
        String name = null;
        boolean troublesWithTheLaw = false;
        try{age= (Long)obj.get("age");}catch (ClassCastException e){age=Long.parseLong((String)obj.get("age"));}
        name = (String) obj.get("name");
        try{troublesWithTheLaw = (Boolean) obj.get("troublesWithTheLaw");}catch(ClassCastException e){troublesWithTheLaw=Boolean.parseBoolean((String)obj.get("age"));}
        nh.setName(name);
        nh.setAge(age);
        nh.setTroublesWithTheLaw(troublesWithTheLaw);
        return nh;

    }

    public static void getMessage(){
        try {
            int size = socketIS.read();
            byte[] mes = new byte[size*512];
            socketIS.read(mes);
            bais = new ByteArrayInputStream(mes);
            dis = new ObjectInputStream(bais);
            message = (Message) dis.readObject();
            bais.close();
            dis.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void sendMessage(){
        try {
            baos = new ByteArrayOutputStream();
            dos = new ObjectOutputStream(baos);
            dos.writeObject(message);
            baos.flush();
            dos.flush();
            System.out.println(message.getState());
            socketOS.write((int)Math.ceil((double)baos.size()/(double)512));
            socketOS.write(baos.toByteArray());
            socketOS.flush();
            baos.reset();
            dos.reset();
        }catch (IOException e){

        }
    }
    public static void main(String[] args){
        try {
            gson = new GsonBuilder().addDeserializationExclusionStrategy(new GsonDeserializeExclusion()).create();
            socket = new Socket("172.16.172.217", 23500);
            secondSocket = new Socket("172.16.172.217", 23501);
            socketOS = socket.getOutputStream();
            socketIS = socket.getInputStream();
            baos = new ByteArrayOutputStream();
            dos = new ObjectOutputStream(baos);
            message = new Message(ConnectionState.NEED_DATA);
            message.maxID=-10;
            message.clearData();
            sendMessage();
            getMessage();
            notEditable = message.getNotEditable();
            coll = new LinkedList<>(message.getData());
            new AnotherConnection(secondSocket, coll, collt).start();
            SwingUtilities.invokeLater(() -> new Interface());
        }catch(ConnectException e) {
            new Dialog("Нет подключения!Сервер отключён!", Interface.getColor());
            System.exit(1);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}