package com.example.nyaysetu;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class QueryPreprocessing {
    Map<Character,QueryPreprocessing> characterhashmapMap;
    boolean isEnd;
    QueryPreprocessing(){
        characterhashmapMap  = new HashMap<>();
        isEnd = false;
    }
    static QueryPreprocessing root = new QueryPreprocessing();
    static SharedPreferences sharedPreferences;

    static void insert(String key){
        QueryPreprocessing curr = root;
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            assert curr != null;
            curr.characterhashmapMap.putIfAbsent(ch,new QueryPreprocessing());
            if (i==key.length()-1){
                Objects.requireNonNull(curr.characterhashmapMap.get(ch)).isEnd = true;
            }
            curr = curr.characterhashmapMap.get(ch);
        }
    }
    static boolean search(String key) {
        QueryPreprocessing curr = root;
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            if (!curr.characterhashmapMap.containsKey(ch)) {
                return false;
            }
            curr = curr.characterhashmapMap.get(ch);
        }
        return curr.isEnd;
    }
    public static void checkTerm(Context context){
        sharedPreferences = context.getSharedPreferences("LegalTerms",Context.MODE_PRIVATE);
        boolean alreadypresent = sharedPreferences.getBoolean("Already",false);
        if (!alreadypresent){
            insertLegalTerms();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("Already",true);
            editor.apply();
        }
        else {
            insertLegalTerms();
        }
    }

    private static void insertLegalTerms() {
        String[] legalTerms = {"law","section","legal", "attorney", "lawyer", "court", "justice", "legal aid", "legal advice", "legislation", "constitution", "IPC", "crime", "criminal",
                "legal rights", "legal system", "civil rights", "criminal law", "legal assistance", "legal proceedings", "legal representation", "legal framework", "judicial system", "penal code", "legal recourse", "legal aid center", "legal documents",
                "legal counsel", "legal jurisdiction", "legal responsibility", "legal defense", "legal remedies", "legal support", "legal dispute", "legal terminology", "legal obligations", "legal regulations", "legal principles", "legal consequences",
                "legal procedures", "legal enforcement", "legal process", "legal protection","कानून", "वकील", "अदालत", "न्याय", "सहायता", "कानूनी", "अधिकार", "विधि", "संविधान", "भारतीय दंड संहिता (IPC)", "अपराध", "अपराधी", "कानूनी सलाह", "कानूनी मदद", "कानूनी सहायता", "न्यायिक प्रक्रिया",
                "न्यायिक संगठन", "कानूनी अधिकार", "कानूनी प्रतिनिधि", "न्यायिक सिद्धांत", "कानूनी प्रक्रिया",
                "कानूनी उत्तराधिकार", "कानूनी जुरिसडिक्शन", "दंड संहिता", "न्यायिक सहायता केंद्र",
                "कानूनी कार्यवाही", "कानूनी निपटान", "कानूनी संज्ञान", "कानूनी अधिकार", "कानूनी प्राधिकरण",
                "कानूनी प्रक्रिया", "कानूनी उत्तराधिकार", "कानूनी विवाद", "कानूनी उत्तराधिकार", "कानूनी समर्थन",
                "कानूनी अदालत", "कानूनी उत्तराधिकार", "कानूनी प्रक्रिया", "कानूनी प्रतिनिधि", "कानूनी बचाव",
                "कानूनी सिद्धांत", "कानूनी उत्तराधिकार", "कानूनी जिम्मेदारी", "कानूनी नियम", "कानूनी जरूरतें",
                "कानूनी कार्रवाई", "कानूनी प्रक्रिया", "कानूनी संप्रेषण", "कानूनी उत्तराधिकार", "कानूनी संरक्षण"};
        for (String keys:
             legalTerms) {
            insert(keys);
        }
    }
}
