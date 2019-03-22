package org.apereo.openequella.adminconsole;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class App extends JFrame implements WindowListener {
    private static final long serialVersionUID = 4440091132569751172L;

    public static void main(String[] args) {
        new App();
    }

    public App() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // layout = new TableLayout(rows, cols);
            setTitle("Admin console launcher");
            setResizable(false);
            addWindowListener(this);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(300, 200);
            setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void onExit(){
        dispose();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        onExit();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
