package aroundwise.nepi.network.requests;

/**
 * Created by mihai on 22/09/16.
 */
public class ChangePasswordRequest {

    public String current_password;
    public String new_password;
    public String confirm_password;


    public ChangePasswordRequest(String current_password, String confirm_password, String new_password) {
        this.current_password = current_password;
        this.new_password = new_password;
        this.confirm_password = confirm_password;
    }


}