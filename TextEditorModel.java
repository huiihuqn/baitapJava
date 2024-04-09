package MVC;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TextEditorModel implements Serializable {
    private List<String> textList;

    public TextEditorModel() {
        this.textList = new ArrayList<>();
    }

    public void addText(String text) {
        textList.add(text);
    }

    public List<String> getTextList() {
        return textList;
    }

    public void loadTextList(List<String> textList) {
        this.textList = textList;
    }

    public void clearTextList() {
        this.textList.clear();
    }
}