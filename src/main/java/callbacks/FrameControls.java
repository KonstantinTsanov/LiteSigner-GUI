/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package callbacks;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public interface FrameControls {

    public void showSigningPanel();

    public void showChooseOptionPanel();

    public void showFileAndSignaturePanel();

    public void hideFileAndSignaturePanel();

    public void showSignatureVerificationPanel();
}
