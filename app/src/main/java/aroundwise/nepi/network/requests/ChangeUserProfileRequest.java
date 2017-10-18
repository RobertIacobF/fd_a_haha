package aroundwise.nepi.network.requests;

/**
 * Created by mihai on 22/09/16.
 */
public class ChangeUserProfileRequest {

    String first_name;
    String last_name;


    public ChangeUserProfileRequest(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;

    }


}
