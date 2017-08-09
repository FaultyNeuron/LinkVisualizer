import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by Georg Plaz.
 */
public class Main {
    public static void main(String... args) throws IOException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException {
        new VisualizerFrame();
    }
}
