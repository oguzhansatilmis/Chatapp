package com.example.chatapp;

public class MesajModel {
    private String from;
    private String text;
    private  String tarih;
    public MesajModel(){
        //bos cons tanımlanması gerekiyormus
    }
    public MesajModel(String from, String text,String tarih) {
        this.from = from;
        this.text = text;
        this.tarih = tarih;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    @Override
    public String toString() {
        return "MesajModel{" +
                "from='" + from + '\'' +
                ", text='" + text + '\'' +
                ", tarih='" + tarih + '\'' +
                '}';
    }
}
