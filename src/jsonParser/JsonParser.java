package jsonParser;

import java.util.HashMap;

/**
 *
 * @author Hobbes
 */
public class JsonParser {
    private HashMap hm = new HashMap();
    
    public JsonParser(String json){
        json = json.substring(1);
        json = json.substring(0, json.length() - 1);
        json = json.replaceAll("\"", "");
        String[] iteams = json.split(","); 
        String[][] kv = new String[iteams.length][2];
        for(int i =0; i<iteams.length; i++){
            kv[i] = iteams[i].split(":");
            hm.put(kv[i][0], kv[i][1]);
        }
    }
    
    public String getVal(String key){
        if(hm.containsKey(key)){
            return (String)hm.get(key);
        }
        return "";
    }
}
