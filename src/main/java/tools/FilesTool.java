/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.File;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public final class FilesTool {

    private FilesTool() {

    }

    public static boolean checkIfFileExists(File file) {
        return file.exists() && !file.isDirectory();
    }
}
