package ui;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
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
        DialogWrapperExitAction exitAction = new DialogWrapperExitAction("Cancel", CANCEL_EXIT_CODE);
        DialogWrapperAction okAction = new CustomOKAction();
        // 设置默认的焦点按钮
        okAction.putValue(DialogWrapper.DEFAULT_ACTION, true);
        return new Action[]{okAction, exitAction};
    }

    protected class CustomOKAction extends DialogWrapperAction {
        private CustomOKAction() {
            super("Copy");
        }

        @Override
        protected void doAction(ActionEvent actionEvent) {
            StringBuilder code = component.createJavaCode();
            String headersText = code.toString();
            // 将文本粘贴到系统剪贴板上
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable trans = new StringSelection(headersText);
            clipboard.setContents(trans, null);
            // 复制成功，弹出提示
            NotificationGroup notificationGroup = new NotificationGroup("notificationGroup", NotificationDisplayType.BALLOON, true);
            Notification notification = notificationGroup.createNotification("Request header code copied successfully。", NotificationType.INFORMATION);
            Notifications.Bus.notify(notification);
            // 然后关闭
            close(CANCEL_EXIT_CODE);
        }
    }
}
