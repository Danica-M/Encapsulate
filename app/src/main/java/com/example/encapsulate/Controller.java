package com.example.encapsulate;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    static List<String> uriList = new ArrayList<>();
    static List<String> captionList = new ArrayList<>();

    public Controller() {

    }

    public void NewList(){
        uriList.clear();
        captionList.clear();
    }

    public List<String> getCaptionList() {
        return captionList;
    }

    public List<String> getUriList(){return uriList;}

    public static void addItem(String item){
        uriList.add(item);
    }
    public void addCaptionItem(String cItem){
        captionList.add(cItem);
    }
}
