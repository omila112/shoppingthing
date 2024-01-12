import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ShoppingCartJFrame extends JFrame {

    private static final String[] COLUMN_NAMES = {"Product", "Qty", "Price"};
    private final ShoppingCart shoppingCart;
    private JPanel panel;

    public ShoppingCartJFrame(final ShoppingCart shoppingCart) {
        super("Shopping Cart");

        this.shoppingCart = shoppingCart;

        setSize(800, 600);

        initComponents();

        addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    e.getWindow().dispose();
                    Main.shoppingManager.getGUI().f.setEnabled(true);
                    Main.shoppingManager.getGUI().shoppingCartJFrame = null;
                }
            }
        );
    }

    private void initComponents() {
        initTable();
        initTotalText();
    }

    private void initTable() {
        DefaultTableModel tableModel = new DefaultTableModel(shoppingCartToTableComps(shoppingCart), COLUMN_NAMES);
        JTable table = new JTable(tableModel);

        panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(table);

        table.setAutoCreateRowSorter(true);
        table.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        scrollPane.setPreferredSize(new Dimension(800, 400));
        panel.add(scrollPane);
        add(panel);
    }

    private void initTotalText() {
        JLabel text = new JLabel();

        text.setText("Total: " + shoppingCart.calculateCost());

        panel.add(text);
    }

    private static Object[][] shoppingCartToTableComps(final ShoppingCart shoppingCart) {
        Object[][] elements = new Object[shoppingCart.size()][COLUMN_NAMES.length];

        int i = 0;
        for (Product product : shoppingCart.stream().toList()) {
            String productInfo = "";
            if (product instanceof Clothing prod) {
                productInfo = String.format("Size: %d, Color: %s", prod.getSize(), prod.getColour());
            } else if (product instanceof Electronics prod) {
                productInfo = String.format("Brand: %s, Warranty: %.2f", prod.getBrand(), prod.getWarranty());
            }

            int qty = shoppingCart.getQty(product);
            double price = Math.max(0, product.getPrice() * qty);

            String productName = String.format("%s %s [%s]",
                    product.getProductID(),
                    product.getProductName(),
                    productInfo
            );

            elements[i++] = new Object[]{
                    productName,
                    qty,
                    price
            };
        }
        return elements;
    }
}
