package dataStructures;
/**
 *
 * @author Hobbes
 */
public class User {
    private String username;
    private String password;
    private String name;

    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public User(String username, String password) {
        this(username, password, "");
    }

    public String getName() {
        return name;
    }
    
    public String getUsername(){
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    @Override
    public String toString(){
        return username;
    }
    
}
