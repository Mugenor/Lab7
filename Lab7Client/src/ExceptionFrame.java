import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * Created by Mugenor on 13.04.2017.
 */
public class ExceptionFrame {
    public static final int EXIT_ON_CLOSE=3;
    public static final int DISPOSE_ON_CLOSE=2;
    static String exc;
    static void init(String exc, int i) {
        JFrame exceptionFrame = new JFrame(exc);
        exceptionFrame.setSize(new Dimension(300,100));
        exceptionFrame.setResizable(false);
        exceptionFrame.setLocationRelativeTo(null);
        exceptionFrame.setDefaultCloseOperation(i);
        JLabel exceptionLabel = new JLabel(exc);
        exceptionFrame.setLayout(new GridLayout(2,1));
        JButton exceptionButton = new JButton(ResourceBundle.getBundle("Locale", Interface.getLocale()).getString("Ok"));
        exceptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(i==3)System.exit(1); else exceptionFrame.dispose();
            }
        });
        exceptionFrame.add(exceptionLabel);
        exceptionFrame.add(exceptionButton);
        exceptionFrame.setVisible(true);
    }
}
