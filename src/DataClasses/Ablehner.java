package DataClasses;

import java.util.HashMap;
import java.util.Map;

public class Ablehner {
    //Items die nicht in die Datenbank aufgenommen werden und warum. Id -> Grund
    public static Map<String, String> abgelehnt =  new HashMap<>();

    public static void ablehnen(String id, String grund){
        if(abgelehnt.get(id) == null){
            abgelehnt.put(id,grund);
        } else {
            String grundNeu = abgelehnt.get(id) + "und " + grund;
        }
    }

}
