package timetrackclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hobbes
 */
public class SettingsManger {
    private static final String FILENAME = "settings.txt";
    
    private boolean remember;
    private String username;
    private String password;
    private String serverAddrs;
    private String port;
    
    public SettingsManger(){
        try{//try open settings file
            BufferedReader br = new BufferedReader(new FileReader(FILENAME));
            String line;
            try {
                line = br.readLine();
                remember = Boolean.parseBoolean(line.substring(line.indexOf(":")+1));
                line = br.readLine();
                username = line.substring(line.indexOf(":")+1);
                line = br.readLine();
                password = line.substring(line.indexOf(":")+1);
                line = br.readLine();
                serverAddrs = line.substring(line.indexOf(":")+1);
                line = br.readLine();
                port = line.substring(line.indexOf(":")+1);
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(SettingsManger.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {//catch filenotfound and make the file
            FileWriter file;
            // set the defualts
            remember = false;
            username = "";
            password = "";
            serverAddrs = "localhost";
            port = "3000";
            try {
                file = new FileWriter(FILENAME);
                BufferedWriter out = new BufferedWriter(file);
                out.write(genFile());
                out.close();
                file.close();
            } catch (IOException ex1) {
                Logger.getLogger(SettingsManger.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    
    public void writeFile(){
        FileWriter file;
        try {
            file = new FileWriter(FILENAME);
            BufferedWriter out = new BufferedWriter(file);
            out.write(genFile());
            out.close();
            file.close();
        } catch (IOException ex1) {
            Logger.getLogger(SettingsManger.class.getName()).log(Level.SEVERE, null, ex1);
        }    
    }
    
    private String genFile(){
        return "remember:"+remember+"\nusername:"+username+"\npassword:"+password+"\nserverAddrs:"+serverAddrs+"\nport:"+port;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerAddrs() {
        return serverAddrs;
    }

    public void setServerAddrs(String serverAddrs) {
        this.serverAddrs = serverAddrs;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
    
    public String getAddres(){
        return "http://"+this.serverAddrs+":"+this.port;
    }
    
}
