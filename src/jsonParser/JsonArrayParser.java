package jsonParser;

import java.util.ArrayList;

/**
 *
 * @author Hobbes
 */
public class JsonArrayParser {
    private ArrayList<JsonParser> js = new ArrayList<>();
    private int pos = 0;

    public JsonArrayParser(String jsonarr) {
        if(jsonarr.equals("[]")){
            pos = 2;
        }else{
            jsonarr = jsonarr.substring(1);
            jsonarr = jsonarr.replaceAll("\\Q},{\\E", "}{");
            jsonarr = jsonarr.substring(0, jsonarr.length() - 1);
            String[] iteams = jsonarr.split("}");
            for(String iteam : iteams) {
                js.add(new JsonParser(iteam+"}"));
            } 
        }        
    }
    
    public JsonParser next(){
        JsonParser j = js.get(pos);
        pos++;
        return j;
    }
    
    public boolean hasNext(){
        return js.size()>pos;
    }
}
