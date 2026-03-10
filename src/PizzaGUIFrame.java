import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * A Swing-based GUI application that allows a user to build a pizza order.
 * The interface includes crust selection (radio buttons), size selection
 * (combo box), topping selection (checkboxes), a receipt display area,
 * and buttons to order, clear, or quit the program.
 *
 */
public class PizzaGUIFrame extends JFrame {

    // Radio Buttons

    /** Radio button for thin crust selection. */
    private JRadioButton thinCrust;

    /** Radio button for regular crust selection. */
    private JRadioButton regularCrust;

    /** Radio button for deep-dish crust selection. */
    private JRadioButton deepDish;



    // Combo Boxes


    /** Combo box for selecting pizza size. */
    private JComboBox<String> sizeBox;

    /** Array of checkboxes representing available toppings. */
    private JCheckBox[] toppings;


    // Misc.


    /** Text area used to display the formatted receipt. */
    private JTextArea receiptArea;

    /** Names of available pizza sizes. */
    private String[] sizeNames = {"Small", "Medium", "Large", "Xtra Large"};

    /** Prices corresponding to each pizza size. */
    private double[] sizePrices = {8.00, 12.00, 16.00, 20.00};

    /** Names of available toppings. */
    private final String[] toppingNames = {
            "Eidolon Cores",
            "Basil Carcinoma",
            "Fungus Woods",
            "Green Eggs N Ham",
            "Ghosts and Goblins",
            "Yogurt and Rice"
    };

    /**
     * Constructs the main pizza GUI frame, initializes all components,
     * and arranges them using a BorderLayout.
     */
    public PizzaGUIFrame() {
        super("Pizza GUI");
        setLayout(new BorderLayout());

        // |  Crust Panel  |  Size Panel  |  Toppings Panel  |
        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        topPanel.add(createCrustPanel());
        topPanel.add(createSizePanel());
        topPanel.add(createToppingPanel());



        // top
        // reciept
        // buttons

        add(topPanel, BorderLayout.NORTH);
        add(createReceiptPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                confirmQuit();
            }
        });

        // tells swing window to resize itself
        // so all components fit at their
        // pref. sizes.
        pack();


        setVisible(true);
    }

    /**
     * Creates the panel containing crust selection radio buttons.
     *
     * @return a JPanel with crust options and a titled border
     */
    private JPanel createCrustPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Crust Type"));

        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDish = new JRadioButton("Deep-dish");

        ButtonGroup crustGroup = new ButtonGroup();
        crustGroup.add(thinCrust);
        crustGroup.add(regularCrust);
        crustGroup.add(deepDish);

        regularCrust.setSelected(true);

        panel.add(thinCrust);
        panel.add(regularCrust);
        panel.add(deepDish);

        return panel;
    }

    /**
     * Creates the panel containing the pizza size combo box (drop down menu)
     *
     * @return a JPanel with size selection components
     */
    private JPanel createSizePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Pizza Size"));

        sizeBox = new JComboBox<>(sizeNames);
        panel.add(sizeBox);

        return panel;
    }

    /**
     * Creates the panel containing topping checkboxes.
     *
     * @return a JPanel with topping options arranged in a grid
     */
    private JPanel createToppingPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.setBorder(new TitledBorder("Toppings ($1 each)"));

        toppings = new JCheckBox[toppingNames.length];

        // Remember: 6 toppings!

        for (int i = 0; i < toppings.length; i++) {
            toppings[i] = new JCheckBox(toppingNames[i]);
            panel.add(toppings[i]);
        }

        return panel;
    }

    /**
     * Creates the panel containing the receipt display area.
     *
     * @return a JPanel containing a scrollable JTextArea
     */
    private JPanel createReceiptPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Order Receipt"));

        receiptArea = new JTextArea(30, 80);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptArea.setEditable(false);

        panel.add(new JScrollPane(receiptArea));

        return panel;
    }

    /**
     * Creates the panel containing the Order, Clear, and Quit buttons.
     * Each button is assigned its corresponding event handler.
     *
     * @return a JPanel containing the action buttons
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();

        JButton orderBtn = new JButton("Order");
        JButton clearBtn = new JButton("Clear");
        JButton quitBtn = new JButton("Quit");

        orderBtn.addActionListener(e -> {

            // Check for at least one topping
            boolean hasTopping = false;
            for (JCheckBox cb : toppings) {
                if (cb.isSelected()) {
                    hasTopping = true;
                    break;
                }
            }

            if (!hasTopping) {
                JOptionPane.showMessageDialog(
                        this,
                        "You must select at least one ingredient.",
                        "Missing Ingredient",
                        JOptionPane.WARNING_MESSAGE
                );
                return; // Stop — do NOT generate receipt
            }

            generateReceipt();
        });


        clearBtn.addActionListener(e -> {


            // Initializes to Reg Crust
            regularCrust.setSelected(true);

            // Initializes to Small
            sizeBox.setSelectedIndex(0);


            // Unselects boxes
            for (JCheckBox cb : toppings) {
                cb.setSelected(false);
            }

            receiptArea.setText("");
        });


        //confirm quit
        quitBtn.addActionListener(e -> confirmQuit());

        panel.add(orderBtn);
        panel.add(clearBtn);
        panel.add(quitBtn);

        return panel;
    }

    /**
     * Generates a simple receipt based on the user's selections.
     * This includes size, crust, selected toppings, and total cost.
     * The formatted receipt is displayed in the JTextArea.
     */
    private void generateReceipt() {
        StringBuilder receipt = new StringBuilder();

        // Header
        receipt.append("=========================================\n");
        receipt.append(String.format("%-30s %10s\n", "Item", "Price"));
        receipt.append("=========================================\n\n");

        double subtotal = 0;

        // Crust + Size
        String crust = regularCrust.isSelected() ? "Regular"
                : thinCrust.isSelected() ? "Thin"
                : "Deep-dish";

        int sizeIndex = sizeBox.getSelectedIndex();
        String sizeName = sizeNames[sizeIndex];
        double sizePrice = sizePrices[sizeIndex];

        receipt.append(String.format("%-30s $%9.2f\n", crust + " / " + sizeName, sizePrice));
        subtotal += sizePrice;

        // Toppings
        receipt.append("\nToppings:\n");

        int toppingCount = 0;
        for (int i = 0; i < toppings.length; i++) {
            if (toppings[i].isSelected()) {
                receipt.append(String.format("%-30s $%9.2f\n", toppings[i].getText(), 1.00));
                subtotal += 1.00;
                toppingCount++;
            }
        }

        if (toppingCount == 0) {
            receipt.append(String.format("%-30s %10s\n", "None selected", ""));
        }

        // Totals
        double tax = subtotal * 0.07;
        double total = subtotal + tax;

        receipt.append("\n");
        receipt.append(String.format("%-30s $%9.2f\n", "Sub-total:", subtotal));
        receipt.append(String.format("%-30s $%9.2f\n", "Tax (7%):", tax));
        receipt.append("=========================================\n");
        receipt.append(String.format("%-30s $%9.2f\n", "Total:", total));
        receipt.append("=========================================\n");

        receiptArea.setText(receipt.toString());
    }





    /**
     * Configures the frame size and behavior based on screen dimensions.
     * Also installs a window listener to confirm quitting.
     */
    private void configureFrame() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        int width = (int)(screenSize.width * 0.75);
        int height = (int)(screenSize.height * 0.75);

        setSize(width, height);
        setLocationRelativeTo(null);

        setVisible(true);
    }

    /**
     * Displays a confirmation dialog asking the user if they want to quit.
     * If the user selects YES, the application exits.
     */
    private void confirmQuit() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to quit?",
                "Quit",
                JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) System.exit(0);
    }
}

