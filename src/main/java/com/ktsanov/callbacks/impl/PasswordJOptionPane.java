/* 
 * The MIT License
 *
 * Copyright 2017 Konstantin Tsanov <k.tsanov@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.ktsanov.callbacks.impl;

import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import com.ktsanov.callbacks.PasswordCallback;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public class PasswordJOptionPane extends JOptionPane implements PasswordCallback {

    private final Component _parent;

    public PasswordJOptionPane(Component parent) {
        _parent = parent;
    }

    @Override
    public char[] getPin() {
        ResourceBundle r = ResourceBundle.getBundle("Bundle");
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
