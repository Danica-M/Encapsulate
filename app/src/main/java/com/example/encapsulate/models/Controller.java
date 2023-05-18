package com.example.encapsulate.models;

import com.example.encapsulate.models.File;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    static List<File> fileList = new ArrayList<>();

    public Controller() {

    }

    public void NewList(){
        fileList.clear();
    }

    public List<File> getUriList(){return fileList;}

    public static void addItem(File item){
        fileList.add(item);
    }




}
