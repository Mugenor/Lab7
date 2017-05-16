import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Closeable;

/**
 * Created by Mugenor on 23.04.2017.
 */
public class CloseFrame extends JFrame {
    private ButtonsWithCommands bwc;
    private Color c = null;
    private JButton yes = new JButton("Yes");
    private JButton no = new JButton("No");
    private JLabel label = new JLabel("Do you want to save collection?");
    private boolean opened = false;
    public void setColor(Color col){
        c = col;
        yes.setBackground(c);
        no.setBackground(c);

    }
    CloseFrame(ButtonsWithCommands bwc){
        this.bwc=bwc;

    }
    public void init(){
        if(!opened) {
            opened=true;
            setLocationRelativeTo(null);
            setFocusable(true);
            setResizable(false);
            setSize(250, 100);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

            yes.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    bwc.save();
                    Interface.message.setState(ConnectionState.DISCONNECT);
                    Interface.message.getData().clear();
                    Interface.sendMessage();
                    System.exit(0);
                }
            });
            no.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            label.setFont(new Font("Verdana", Font.BOLD, 12));
            add(label);
            add(yes);
            add(no);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    opened=false;
                }
            });
            setVisible(true);
        }
    }
}
