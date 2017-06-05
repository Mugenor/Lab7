import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * Created by bespa on 16.05.2017.
 */
public class Dialog extends JDialog {
    JButton ok = new JButton("Ok");
    Dialog(String error,Color c){
        JDialog dialog = new JDialog();
        dialog.setLayout(null);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        JLabel label = new JLabel(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString(error));
        label.setFont(new Font("Verdana", Font.BOLD, 12));
        dialog.add(label);
        label.setLocation(30,0);
        label.setSize(300,50);
        dialog.setSize(330, 140);
        ok.setSize(80, 30);
        ok.setLocation(120, 50);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!error.equals("Разрыв соединения!Сервер отключён!"))
                    dialog.dispose();
                else
                    System.exit(1);
            }
        });
        ok.setBackground(c);
        dialog.add(ok);
        dialog.isResizable();
        dialog.setVisible(true);
    }
}