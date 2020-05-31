package carservice.p5.mytranslater.mvp.Models;

public class TranslateModel  {

    private int id;
    private String textIn, textTo,langCodeIn,langCodeTo;

    public TranslateModel(int id, String textIn, String textTo, String langCodeIn,String langCodeTo){
        this.id = id;
        this.textIn = textIn;
        this.textTo = textTo;
        this.langCodeIn = langCodeIn;
        this.langCodeTo = langCodeTo;
    }

    public int getId() {
        return id;
    }

    public String getTextIn() {
        return textIn;
    }

    public String getTextTo() {
        return textTo;
    }

    public String getLangCodeIn() {
        return langCodeIn;
    }

    public String getLangCodeTo() {
        return langCodeTo;
    }


}
