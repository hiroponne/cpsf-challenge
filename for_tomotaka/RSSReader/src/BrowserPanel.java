import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class BrowserPanel extends JPanel{

    private JEditorPane editorPane;

    public BrowserPanel(){

        editorPane=new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");

        doMyLayout();
    }
    private void doMyLayout(){
        setLayout(new BorderLayout());
        add(new JScrollPane(editorPane),BorderLayout.CENTER);
    }

    public void setText(String text){
        editorPane.setText(text);
    }
    public String getText(){
        return editorPane.getText();
    }
}