
package com.houarizegai.atm.java;

import java.util.concurrent.Semaphore;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import static com.houarizegai.atm.java.App.*;

public class Person extends Thread {
      
    private final Semaphore machine; // This Semapbore of the Machine ATM
    private final Semaphore mutex; // This Semapbore of the Text Area of Processes
    
    Person (Semaphore machine, String name, Semaphore mutex) {
        this.machine = machine;
        this.mutex = mutex;
        this.setName(name); // Change the name of Thread
        this.start(); // Start Execution the Thread
    }

    @Override
    public void run () {
        
        try {
            waitingMachine();
                        
            machine.acquire(); // P(machine) of Semaphore
            
            usingMachine();
            
            machine.release(); // V(machine) of Semaphore
            
            releaseMachine();
            
            if (--counterReset == 0) // I use this counter to activate the "Reset" button
                btnReset.setDisable(false); // Activate the Reset Button
            
        } catch (InterruptedException ex) {}

    }
    
    public void waitingMachine() {
        try {
            mutex.acquire(); // P(mutex) de Semaphore
            
            for(int i = 0; i < Constants.MEN_NAME.length; i++)
                if (this.getName().equals(Constants.MEN_NAME[i])) {
                    fadeTransition(manImage[i]);
                    break;
                }
            
            processesTextArea.setText(processesTextArea.getText() + this.getName() + " is Waiting For Machine ...\n");
            mutex.release(); // V(mutex) de Semaphore
        } catch (InterruptedException ex) {}
    
    }
    
    public void usingMachine() {
        try {
            sleep(3000);
            for(int i = 0; i < Constants.MEN_NAME.length; i++)
                if (this.getName().equals(Constants.MEN_NAME[i])) {
                    translateTransition(210 + (4 - i) * 90, 125, manImage[i]);
                }
            
            mutex.acquire();
            processesTextArea.setText(processesTextArea.getText() + this.getName() + " is Using Machine ...\n");
            mutex.release();
            sleep(3000);
        } catch (InterruptedException ex) {}
            
    }
    
    public void releaseMachine() {
        try {
            mutex.acquire();
            
            for(int i = 0; i < Constants.MEN_NAME.length; i++)
                if (this.getName().equals(Constants.MEN_NAME[i])) {
                    translateTransition(0, 360, manImage[i]);
                    break;
                }
            
            processesTextArea.setText(processesTextArea.getText() + this.getName() + " Release\n");
           mutex.release();
            
            sleep(2000); // I make sleep for complete the translation and finally make the Reset Button Enabled
        } catch (InterruptedException ex) {}
    }

    public void translateTransition(int x, int y, ImageView image) {
        /* This animation for change the Position of element in specific duration */
        TranslateTransition tt = new TranslateTransition(Duration.millis(2000), image);
        tt.setToX(x); // Change X Position Of The Element
        tt.setToY(y); // Change Y Position Of The Element
        tt.play();
    }
    
    public void fadeTransition(ImageView image) {
        /* This animation for change the Opacity of element in specific duration */
        FadeTransition ft = new FadeTransition(Duration.millis(2000), image); // Duration Is The Time to do it
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }
    
}