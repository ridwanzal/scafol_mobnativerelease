package com.release.sharedexternalmodule;

public class Kriteria {

    public static String get_kriteria(String date, String real, String target ){
        String kriteria = "undefined";
        real = real.replace(",",".");
        target = target.replace(",",".");

        double r = Double.parseDouble(real);
        double t = Double.parseDouble(target);
        double defisit = r - t;

        if (r == 100 && defisit == 0){
            kriteria = "Selesai";
        }else if (r == 100){
            kriteria = "Belum Mulai";
        }else {
            if (t == 0 || t <= 70){
                kriteria = rencanafisik1(defisit);
            }else if (t > 70 && t <= 100){
                kriteria = rencanafisik2(defisit);
            }
        }

        return kriteria;
    }

    public static String rencanafisik1(double defisit){
        String kriteria = "undefined";
        if (defisit > 0){
            kriteria = "Baik";
        }else {
            if(defisit == 0 || defisit >= (-7)){
                kriteria = "Wajar";
            }else if (defisit <(-7) && defisit >=(-10)){
                kriteria = "Terlambat";
            }else {
                kriteria = "Kritis";
            }
        }
        return kriteria;
    }

    public static String rencanafisik2(double defisit){
        String kriteria = "undefinded";
        if (defisit > 0){
            kriteria = "Baik";
        }else{
            if (defisit == 0 || defisit >= (-4)) {
                kriteria = "Wajar";
            }else if(defisit <(-4) && defisit >= (-5)){
                kriteria = "Terlambat";
            }else{
                kriteria = "Kritis";
            }
        }
        return kriteria;
    }


}
