package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import ui.ConvertComponent;

import javax.swing.*;
import java.awt.*;

/**
 * @author lcl100
 * @create 2022-06-19 16:43
 */
public class ConvertRequestHeaderAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        ConvertComponent component = new ConvertComponent();
        JDialog dialog = new JDialog();
        dialog.setContentPane((Container) component.getInstance());
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        ConvertComponent component = new ConvertComponent();
        JDialog dialog = new JDialog();
        dialog.setContentPane((Container) component.getInstance());
        dialog.setSize(400,300);
        dialog.setLocation(500,500);
        dialog.setVisible(true);
    }
}
