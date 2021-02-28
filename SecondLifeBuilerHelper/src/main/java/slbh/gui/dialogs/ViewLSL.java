/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package slbh.gui.dialogs;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ViewLSL extends JFrame implements ActionListener {
    private JTextArea code;
    private JButton bOk = new JButton("OK");
    private JButton bClip = new JButton(slbh.lang.Config.getMyLanguage().buttonToClipboard());

    public ViewLSL(String text) {
        // Set the window
        setTitle("LSL Code");
        setSize(800, 600);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set the components
        code = new JTextArea(text, 30, 70);
        bOk.setActionCommand("ok");
        bOk.addActionListener(this);
        add(new JScrollPane(code));
        bClip.setActionCommand("clip");
        bClip.addActionListener(this);
        add(bClip);
        add(bOk);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().compareTo("clip") == 0) {
            code.selectAll();
            String selection = code.getSelectedText();
            StringSelection data = new StringSelection(selection);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(data, data);
        } else {
            dispose();
        }
    }
}
