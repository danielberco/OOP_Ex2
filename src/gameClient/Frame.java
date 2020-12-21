package gameClient;

import javax.swing.*;

public class Frame extends JFrame {
    Arena manage;
    Pannel panel;

    public Frame () {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void initFrame(Arena a) {
        manage = a;
        initPannel();

    }

    public void initPannel () {
        panel = new Pannel(manage);
        this.add(panel);
    }

    public Pannel getPanel() {
        return panel;
    }

}
