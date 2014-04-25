package visualization;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by max on 25-4-14.
 */
public class MatchingPlot extends JFrame {
    private JPanel rootPanel;
    private JButton fooButton;

    public MatchingPlot() {
        super("Matching Plot");

        setContentPane(rootPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        fooButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.err.println("foobar");
            }
        });

        pack();
        setVisible(true);

    }


}
