package ui;

import com.intellij.notification.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author lcl100
 * @create 2022-06-18 20:16
 */
public class ConvertComponent {
    private JPanel mainPane;
    private JTabbedPane tabbedPane;
    private JButton copyButton;
    private JPanel panel2;
    private JPanel tablePane;
    private JScrollPane table_scroll_pane;
    private JTable table;
    private JPanel inputPane;
    private JTextArea textArea;

    public Component getInstance() {
        return mainPane;
    }

    public ConvertComponent() {
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                if (selectedIndex == 1) {// 第二块面板
                    Object[][] data = getData();
                    Object[] columnNames = {"", "key", "value"};
                    MyTableModel model = new MyTableModel(data, columnNames);
                    table.setModel(model);
                    table.getColumnModel().getColumn(0).setPreferredWidth(20);
                    table.getColumnModel().getColumn(1).setPreferredWidth(80);
                    table.getColumnModel().getColumn(2).setPreferredWidth(220);
                    table_scroll_pane.add(new JButton("xxx"));
                } else {// 第一块面板
                    System.out.println("0000000000000");
                    StringBuilder content = new StringBuilder();
                    int rowCount = table.getRowCount();
                    for (int i = 0; i < rowCount; i++) {
                        String notes = "";
                        Boolean isSelected = (Boolean) table.getValueAt(i, 0);
                        if (!isSelected) {
                            notes = "//";// 如果未被选中则添加注释 "//"
                        }
                        String key = table.getValueAt(i, 1).toString();
                        String value = table.getValueAt(i, 2).toString().replaceAll("\r", "");
                        content.append(notes).append(key).append(": ").append(value).append("\n");
                    }
                    // 将内容重新赋值给内容面板
                    textArea.setText(content.toString());
                }
            }
        });
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Map<String, String> headers = new HashMap<String, String>();\n");
                int rowCount = table.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    Boolean isSelected = (Boolean) table.getValueAt(i, 0);
                    if (isSelected) {
                        String key = table.getValueAt(i, 1).toString();
                        String value = table.getValueAt(i, 2).toString().replaceAll("\r", "");
                        String headerText = "headers.put(\"" + key + "\", \"" + value + "\");\n";
                        sb.append(headerText);
                    }
                }
                String headersText = sb.toString();
                System.out.println(headersText);
                // 将文本粘贴到系统剪贴板上
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable trans = new StringSelection(headersText);
                clipboard.setContents(trans, null);
                // 复制成功，弹出提示
                NotificationGroup notificationGroup = new NotificationGroup("notificationGroup", NotificationDisplayType.BALLOON, true);
                Notification notification = notificationGroup.createNotification("请求头代码复制成功！", NotificationType.INFORMATION);
                Notifications.Bus.notify(notification);
            }
        });
    }

    private Object[][] getData() {
        String text = textArea.getText();
        String[] lines = text.split("\n");
        Object[][] data = new Object[lines.length][3];
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String[] infos = line.split(":\\s");
            String key = infos[0];
            String value = infos[1];
            data[i][0] = !line.startsWith("//");
            data[i][1] = key.startsWith("//") ? key.substring(2) : key;
            data[i][2] = value;
        }
        return data;
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("xx");
        frame.setContentPane(new ConvertComponent().mainPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPane = new JPanel();
        mainPane.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPane.setAlignmentX(1.0f);
        mainPane.setAlignmentY(1.0f);
        mainPane.setMinimumSize(new Dimension(200, 250));
        mainPane.setPreferredSize(new Dimension(400, 300));
        tabbedPane = new JTabbedPane();
        mainPane.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        inputPane = new JPanel();
        inputPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Input", inputPane);
        final JScrollPane scrollPane1 = new JScrollPane();
        inputPane.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textArea = new JTextArea();
        scrollPane1.setViewportView(textArea);
        tablePane = new JPanel();
        tablePane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Table", tablePane);
        table_scroll_pane = new JScrollPane();
        tablePane.add(table_scroll_pane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        table = new JTable();
        table.setCellSelectionEnabled(false);
        table.setEnabled(true);
        table_scroll_pane.setViewportView(table);
        panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPane.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        copyButton = new JButton();
        copyButton.setText("Copy");
        panel2.add(copyButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPane;
    }

}

class MyTableModel extends DefaultTableModel {

    public MyTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Boolean.class;
        }
        return String.class;
    }
}