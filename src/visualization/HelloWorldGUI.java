package visualization;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by max on 25-4-14.
 */
public class HelloWorldGUI extends JPanel implements ActionListener {

    private JButton helloButton;

    public HelloWorldGUI() {
        super(new BorderLayout());
        helloButton = new JButton("Hello World");
        add(helloButton, BorderLayout.CENTER);
        helloButton.addActionListener(this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Hello World GUI");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JComponent newContentPane = new HelloWorldGUI();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("Clicked "+actionEvent.getSource());
        Toolkit.getDefaultToolkit().beep();
    }
}
