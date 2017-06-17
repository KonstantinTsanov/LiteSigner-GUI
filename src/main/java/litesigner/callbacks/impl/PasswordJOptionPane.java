/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litesigner.callbacks.impl;

import java.awt.Component;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import callbacks.PasswordCallback;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public class PasswordJOptionPane extends JOptionPane implements PasswordCallback {

    private final Component _parent;
    private static Locale _locale;

    public PasswordJOptionPane(Component parent) {
        _parent = parent;
    }

    public static void setPaneLocale(Locale locale) {
        _locale = locale;
    }

    @Override
    public char[] getPassword() {
        ResourceBundle r = ResourceBundle.getBundle("Bundle", _locale);
        String messageTitle = r.getString("passwordJOptionPane.messageTitle");
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

        Object[] objs = {r.getString("passwordJOptionPane.message"), password};
        int result = JOptionPane.showConfirmDialog(_parent, objs, messageTitle, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE, null);

        while (result == JOptionPane.OK_OPTION && password.getPassword().length == 0) {
            JOptionPane.showMessageDialog(_parent, r.getString("passwordJOptionPane.errorMessage.title"), r.getString("passwordJOptionPane.errorMessage.title"),
                    JOptionPane.ERROR_MESSAGE, null);
            result = JOptionPane.showConfirmDialog(_parent, objs, messageTitle, JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null);
        }
        if (result == JOptionPane.CLOSED_OPTION || result == JOptionPane.CANCEL_OPTION) {
            password.setText("");
            return null;
        }
        return password.getPassword();
    }
}
