/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litesigner.callbacks.impl;

import callbacks.GuiPasswordCallback;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public class PasswordJOptionPane extends JOptionPane implements GuiPasswordCallback {

    private final Component _parent;

    public PasswordJOptionPane(Component parent) {
        _parent = parent;
    }

    @Override
    public char[] getPassword() {
        String messageTitle = "Enter PIN";
        JPasswordField password = new JPasswordField();
        password.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorRemoved(final AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(final AncestorEvent event) {
            }

            @Override
            public void ancestorAdded(final AncestorEvent event) {
                password.requestFocusInWindow();
            }
        });

        Object[] objs = {"Enter PIN:", password};
        int result = JOptionPane.showConfirmDialog(_parent, objs, messageTitle, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE, null);

        while (result == JOptionPane.OK_OPTION && password.getPassword().length == 0) {
            JOptionPane.showMessageDialog(_parent, "Please enter a valid PIN.", "PIN is not valid",
                    JOptionPane.ERROR_MESSAGE, null);
            result = JOptionPane.showConfirmDialog(_parent, objs, messageTitle, JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null);
        }
        return password.getPassword();
    }
}
