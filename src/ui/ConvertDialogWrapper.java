package ui;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

public class ConvertDialogWrapper extends DialogWrapper {
    private ConvertComponent component;

    public ConvertDialogWrapper() {
        super(true);
        component = new ConvertComponent();
        init();
        setTitle("Convert Request Header");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return component.getInstance();
    }

    @Override
    protected Action[] createActions() {
        // 弹出框种的 Cancel 按钮
        DialogWrapperExitAction exitAction = new DialogWrapperExitAction("Cancel", CANCEL_EXIT_CODE);
        // 弹出框中的 OK 按钮
        DialogWrapperAction okAction = new CustomOKAction();
        // 设置默认的焦点按钮
        okAction.putValue(DialogWrapper.DEFAULT_ACTION, true);
        // 返回两个按钮
        return new Action[]{okAction, exitAction};
    }

    /**
     * 自定义弹出框的 OK 按钮
     */
    protected class CustomOKAction extends DialogWrapperAction {
        private CustomOKAction() {
            // OK 按钮的实际显示的名字
            super("Copy");
        }

        /**
         * 点击该按钮的事件
         */
        @Override
        protected void doAction(ActionEvent event) {
            // 获取编辑器
            // 获取转换面板中生成的 Java 代码字符串
            StringBuilder code = component.createJavaCode();
            String headersText = code.toString();
            // 将文本粘贴到系统剪贴板上
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable trans = new StringSelection(headersText);
            clipboard.setContents(trans, null);
            System.out.println(event.getSource());
            // 复制成功，弹出提示
            NotificationGroupManager.getInstance()
                    .getNotificationGroup("Custom Notification Group")
                    .createNotification("Request header code copied successfully.", NotificationType.INFORMATION)
                    .notify(null);
            // 然后关闭
            close(CANCEL_EXIT_CODE);
        }
    }
}
