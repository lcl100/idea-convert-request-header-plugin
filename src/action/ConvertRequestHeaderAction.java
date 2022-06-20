package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import ui.ConvertComponent;
import ui.ConvertDialogWrapper;

import javax.swing.*;
import java.awt.*;

/**
 * @author lcl100
 * @create 2022-06-19 16:43
 */
public class ConvertRequestHeaderAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        ConvertDialogWrapper wrapper = new ConvertDialogWrapper();
        wrapper.show();
    }
}
