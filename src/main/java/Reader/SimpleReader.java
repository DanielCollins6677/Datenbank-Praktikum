package Reader;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SimpleReader {

    public static List<String> readFile(File file) {
        List<String> res = new ArrayList<>();
        try (
                java.io.FileReader fr = new java.io.FileReader(file);
                BufferedReader br = new BufferedReader(fr);
        ) {
            res.add(br.readLine());
            String temp;
            while (true) {
                temp = br.readLine();
                if (temp == null) {
                    break;
                }
                res.add(temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
