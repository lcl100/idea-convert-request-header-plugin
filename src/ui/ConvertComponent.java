package ui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

/**
 * @author lcl100
 * @create 2022-06-18 20:16
 */
public class ConvertComponent {
    private JPanel mainPane;
    private JTabbedPane tabbedPane;
    private JPanel tablePane;
    private JScrollPane table_scroll_pane;
    private JTable table;
    private JPanel inputPane;
    private JTextArea textArea;

    public JComponent getInstance() {
        // 返回当前面板组件，供其他类调用
        return mainPane;
    }

    public ConvertComponent() {
        // 选项卡切换监听器
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // 获取被选中的选项卡索引，从 0 开始
                int selectedIndex = tabbedPane.getSelectedIndex();
                // 如果选中第二块选项卡面板
                if (selectedIndex == 1) {
                    // 读取文本域的内容，分解成二维数组，即表格组件的表数据
                    Object[][] data = getData();
                    // 表头
                    Object[] columnNames = {"", "key", "value"};
                    MyTableModel model = new MyTableModel(data, columnNames);
                    table.setModel(model);
                    table.getColumnModel().getColumn(0).setPreferredWidth(20);
                    table.getColumnModel().getColumn(1).setPreferredWidth(80);
                    table.getColumnModel().getColumn(2).setPreferredWidth(220);
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
    }

    /**
     * 读取文本域组件内的内容，然后分解成二维数组
     *
     * @return 二维数组
     */
    private Object[][] getData() {
        // 获取文本域组件的内容
        String text = textArea.getText();
        // 按换行符进行切割，获取每一行的内容
        String[] lines = text.split("\n");
        // 创建一个多行三列的二维数组
        Object[][] data = new Object[lines.length][3];
        // 循环每一行内容
        for (int i = 0; i < lines.length; i++) {
            // 获取当前行的内容
            String line = lines[i];
            // 按指定正则表达式切割每一行内容
            String[] infos = line.split(":\\s");
            // 键名
            String key = infos[0];
            // 键值
            String value = infos[1];
            // 如果文本行以双斜杠 "//" 开头表示是注释行，该行则在表格面板的对应行复选框不勾选
            data[i][0] = !line.startsWith("//");
            // 第二列是键名
            data[i][1] = key.startsWith("//") ? key.substring(2) : key;
            // 第三列是键值
            data[i][2] = value;
        }
        return data;
    }

    /**
     * 读取表格种每一行的内容转换成 Java 代码字符串
     *
     * @return 代码字符串
     */
    public StringBuilder createJavaCode() {
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
        return sb;
    }

    /**
     * 自定义 TableModel，要求第一列是复选框
     */
    class MyTableModel extends DefaultTableModel {

        public MyTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            // 如果是第一列，要求值是布尔值，即使用复选框组件
            if (columnIndex == 0) {
                return Boolean.class;
            }
            return String.class;
        }
    }
}