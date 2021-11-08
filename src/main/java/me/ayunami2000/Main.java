package me.ayunami2000;

import com.github.strikerx3.jxinput.XInputAxes;
import com.github.strikerx3.jxinput.XInputButtons;
import com.github.strikerx3.jxinput.XInputComponents;
import com.github.strikerx3.jxinput.XInputDevice;
import com.github.strikerx3.jxinput.exceptions.XInputNotLoadedException;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static boolean started=false;
    public static List<ControllerValues> controllerValues = new ArrayList<>();

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("vibrator by ayunami2000");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JLabel label = new JLabel("<html>Hello! Welcome to vibrator by ayunami2000. (https://github.com/ayunami2000)<br/><br/>Usage:<br/> - A = Enable vibration<br/> - B = Disable vibration<br/> - Left JoyStick (Vertical) = Increase/Decrease vibration on left motor.<br/> - Right JoyStick (Vertical) = Increase/Decrease vibration on right motor.<br/><br/>Press any key to start!</html>");
            frame.getContentPane().add(label);

            frame.pack();
            frame.setVisible(true);

            if (XInputDevice.isAvailable()) {
                frame.addKeyListener(new KeyListener() {
                    public void keyPressed(KeyEvent ev) {
                        if (!started) {
                            started = true;
                            new Thread(() -> {
                                while(true) {
                                    XInputDevice[] devices = new XInputDevice[0];
                                    try {
                                        devices = XInputDevice.getAllDevices();
                                    } catch (XInputNotLoadedException e) {
                                        System.exit(1);
                                    }
                                    label.setText("");
                                    String text = "<html>";
                                    for (int i=0;i<devices.length;i++) {
                                        while(controllerValues.size()<=i){
                                            controllerValues.add(new ControllerValues());
                                        }
                                        XInputDevice device = devices[i];
                                        if (device.poll()) {
                                            XInputComponents components = device.getComponents();

                                            XInputButtons buttons = components.getButtons();
                                            XInputAxes axes = components.getAxes();

                                            if (buttons.a) {
                                                controllerValues.get(i).setEnabled(true);
                                            }
                                            if (buttons.b) {
                                                controllerValues.get(i).setEnabled(false);
                                                device.setVibration(0, 0);
                                            }

                                            if(Math.abs(axes.ly)>0.3)controllerValues.get(i).addLvibr(axes.ly);
                                            if(Math.abs(axes.ry)>0.3)controllerValues.get(i).addRvibr(axes.ry);

                                            controllerValues.get(i).setLvibr(Math.max(0,Math.min(1000,controllerValues.get(i).getLvibr())));
                                            controllerValues.get(i).setRvibr(Math.max(0,Math.min(1000,controllerValues.get(i).getRvibr())));

                                            if(controllerValues.get(i).getEnabled())device.setVibration((int)(controllerValues.get(i).getLvibr()*65.535), (int)(controllerValues.get(i).getRvibr()*65.535));
                                        }
                                        text+="Device #"+i+" | "+(controllerValues.get(i).getEnabled()?"En":"Dis")+"abled | Left Motor: "+((int)controllerValues.get(i).getLvibr()/10.0)+" | Right Motor: "+((int)controllerValues.get(i).getRvibr()/10.0)+"<br/>";
                                    }
                                    text+="</html>";
                                    label.setText(text);
                                    try {
                                        Thread.sleep(1);
                                    } catch (InterruptedException e) {}
                                }
                            }).start();
                        }
                    }

                    public void keyReleased(KeyEvent e) {
                    }

                    public void keyTyped(KeyEvent e) {
                    }
                });
            }else{
                label.setText("Error: No XInput support!");
            }
        });
    }
}
