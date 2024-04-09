package MVC;

import java.io.IOException;import java.util.List;

public class TextEditorController {
    private TextEditorModel model;
    private TextEditorView view;

    public TextEditorController(TextEditorModel model, TextEditorView view) {
        this.model = model;
        this.view = view;
        this.view.setController(this);
    }

    public void addText(String text) {
        model.addText(text);
    }

    public List<String> getTextList() {
        return model.getTextList();
    }

    public void loadTextList(List<String> textList) {
        model.loadTextList(textList);
    }

    public void clearTextList() {
        model.clearTextList();
    }

    public void displayTextList() {
        view.displayTextList();
    }
}
