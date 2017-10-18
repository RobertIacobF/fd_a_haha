package aroundwise.nepi.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mihai on 07/09/16.
 */
public class Profile {
    String account_type;
    String birthdate;
    Reward current_reward;
    String external_refresh_token;
    String facebook_id;
    String facebook_pick_url;
    String gender;
    String image;
    long id;
    String last_notification;
    String location;
    Mall mall;
    int notification_count;
    int points;
    String resource_uri;
    Shop store;
    String uuid;
    @SerializedName("corporate_code")
    String corporate_code;
    @SerializedName("corporate_code_validated")
    boolean corporate_code_validated;
    @SerializedName("corporate_logo")
    String corporateLogo;
    @SerializedName("phone_number")
    String phoneNumber;

    public Profile(String account_type, String birthdate, Reward current_reward,
                   String external_refresh_token, String facebook_id, String facebook_pick_url,
                   String gender, String image, long id, String last_notification, String location,
                   Mall mall, int notification_count, int points, String resource_uri, Shop store, String uuid) {
        this.account_type = account_type;
        this.birthdate = birthdate;
        this.current_reward = current_reward;
        this.external_refresh_token = external_refresh_token;
        this.facebook_id = facebook_id;
        this.facebook_pick_url = facebook_pick_url;
        this.gender = gender;
        this.image = image;
        this.id = id;
        this.last_notification = last_notification;
        this.location = location;
        this.mall = mall;
        this.notification_count = notification_count;
        this.points = points;
        this.resource_uri = resource_uri;
        this.store = store;
        this.uuid = uuid;
    }

    public Profile(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Reward getCurrent_reward() {
        return current_reward;
    }

    public void setCurrent_reward(Reward current_reward) {
        this.current_reward = current_reward;
    }

    public String getExternal_refresh_token() {
        return external_refresh_token;
    }

    public void setExternal_refresh_token(String external_refresh_token) {
        this.external_refresh_token = external_refresh_token;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getFacebook_pick_url() {
        return facebook_pick_url;
    }

    public void setFacebook_pick_url(String facebook_pick_url) {
        this.facebook_pick_url = facebook_pick_url;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLast_notification() {
        return last_notification;
    }

    public void setLast_notification(String last_notification) {
        this.last_notification = last_notification;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Mall getMall() {
        return mall;
    }

    public void setMall(Mall mall) {
        this.mall = mall;
    }

    public int getNotification_count() {
        return notification_count;
    }

    public void setNotification_count(int notification_count) {
        this.notification_count = notification_count;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public String getCorporateLogo() {
        return corporateLogo;
    }

    public String getResource_uri() {
        return resource_uri;
    }

    public void setResource_uri(String resource_uri) {
        this.resource_uri = resource_uri;
    }

    public Shop getStore() {
        return store;
    }

    public void setStore(Shop store) {
        this.store = store;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setCorporate_code(String corporate_code) {
        this.corporate_code = corporate_code;
    }

    public void setCorporate_code_validated(boolean corporate_code_validated) {
        this.corporate_code_validated = corporate_code_validated;
    }

    public String getCorporateCode() {
        return corporate_code;
    }

    public boolean isCorporateCodeValidated() {
        return corporate_code_validated;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "account_type='" + account_type + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", current_reward='" + current_reward + '\'' +
                ", external_refresh_token='" + external_refresh_token + '\'' +
                ", facebook_id='" + facebook_id + '\'' +
                ", facebook_pick_url='" + facebook_pick_url + '\'' +
                ", gender='" + gender + '\'' +
                ", id='" + id + '\'' +
                ", last_notification='" + last_notification + '\'' +
                ", location='" + location + '\'' +
                ", mall=" + mall +
                ", notification_count=" + notification_count +
                ", points=" + points +
                ", resource_uri='" + resource_uri + '\'' +
                ", store='" + store + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
