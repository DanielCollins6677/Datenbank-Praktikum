package DataClasses;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    public static void abgelehntDateiErstellen() {

        String basePath = new File("").getAbsolutePath();

        try (
            FileWriter fw = new FileWriter(new File(basePath + "/src/main/Abgelehnt"));
            BufferedWriter bw = new BufferedWriter(fw);
        ) {
            for(String abgelehnte : abgelehnt.keySet()){
                String grund = abgelehnt.get(abgelehnte);

                bw.write("\'" + abgelehnte + "\' wurde abgelehnt weil: " + grund + "\n");
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
