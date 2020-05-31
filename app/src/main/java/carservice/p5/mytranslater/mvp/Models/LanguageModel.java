package carservice.p5.mytranslater.mvp.Models;

public class LanguageModel {
    private String code, name;
    private final String EMPTY = "";

    public  LanguageModel(String code,String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code==null ? EMPTY:code;
    }

    public String getName() {
        return name==null ? EMPTY:name;
    }

}
