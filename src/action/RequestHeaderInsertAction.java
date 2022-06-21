package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.search.GlobalSearchScope;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class RequestHeaderInsertAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        // 不允许插件在主线程中进行实时的文件写入，只能通过异步任务来完成写入
        WriteCommandAction.runWriteCommandAction(event.getProject(), () -> {
            PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);
            Project project = event.getProject();
            if (project == null) {
                return;
            }
            if (psiFile instanceof PsiJavaFile) {
                // 获取当前光标所在位置
                Editor editor = event.getData(LangDataKeys.EDITOR);
                PsiElement psiElement = psiFile.findElementAt(editor.getCaretModel().getOffset());
                PsiElement codeBlock = psiElement;

                while (!(codeBlock instanceof PsiCodeBlock)) {
                    codeBlock = codeBlock.getParent();
                }
                // 使用 PsiElementFactory 来创建表达式元素
                PsiElementFactory factory = PsiElementFactory.getInstance(psiElement.getProject());
                // 注意，如果是插入字段（如 String msg="hello world"）则用 createFieldFromText 方法
                // 从剪贴板中获取粘贴的内容
                String text = getClipboardString();
                // 将键值对文本转换成Java的Map代码
                text = createJavaCode(text).toString();
                String[] lines = text.split("\n");
                for (String line : lines) {
                    PsiElement element = factory.createStatementFromText(line, null);
                    codeBlock.addBefore(element, psiElement);
                    psiElement = psiFile.findElementAt(editor.getCaretModel().getOffset());
                }

                // 根据全类名获取指定类对应的 PsiClass
                PsiClass mapClass = JavaPsiFacade.getInstance(project).findClass("java.util.Map", GlobalSearchScope.allScope(project));
                // 创建 Import 的 PsiElement
                PsiElement mapClassImportStatement = factory.createImportStatement(mapClass);
                // 添加到导入列表
                ((PsiJavaFile) psiFile).getImportList().add(mapClassImportStatement);
                PsiClass hashMapClass = JavaPsiFacade.getInstance(project).findClass("java.util.HashMap", GlobalSearchScope.allScope(project));
                PsiElement hashMapClassImportStatement = factory.createImportStatement(hashMapClass);
                ((PsiJavaFile) psiFile).getImportList().add(hashMapClassImportStatement);

            } else {
                System.out.println("当前文件不是Java文件");
            }
        });
    }

    /**
     * 获取剪贴板中的文本内容
     *
     * @return 剪贴板文本
     */
    private String getClipboardString() {
        // 获取系统的剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 获取剪贴板中的内容
        Transferable transferable = clipboard.getContents(null);

        if (transferable != null) {
            // 判断剪贴板中的内容是否支持文本
            if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    return (String) transferable.getTransferData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public StringBuilder createJavaCode(String text) {
        text = text.replaceAll("\"", "\\\\\"");
        StringBuilder sb = new StringBuilder();
        sb.append("Map<String, String> headers = new HashMap<String, String>();\n");
        String[] lines = text.split("\n");
        for (String line : lines) {
            String[] infos = line.split(":\\s");
            String key = infos[0];
            String value = infos[1];
            String headerText = "headers.put(\"" + key + "\", \"" + value + "\");\n";
            sb.append(headerText);
        }
        return sb;
    }
}
