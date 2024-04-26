import lemmings.DrawingPanel;
import lemmings.audio.AudioPlayer;

import javax.print.attribute.standard.Media;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        DrawingPanel drawingPanel = new DrawingPanel();
        JFrame jFrame = new JFrame("Template");
        jFrame.setContentPane(drawingPanel);
        jFrame.setSize(1400,700);
        jFrame.setDefaultCloseOperation(3);
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.setUndecorated(true);
        jFrame.setVisible(true);
    }
}