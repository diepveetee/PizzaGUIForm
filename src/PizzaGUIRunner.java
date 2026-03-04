import javax.swing.*;

public class PizzaGUIRunner {

    public static void main(String[] args)
    {
        // The invokeLater method ensures the GUI is created on the
        // Event Dispatch Thread (EDT) to avoid thread interference.

        SwingUtilities.invokeLater(() -> { new PizzaGUIFrame();

        });
    }


}
