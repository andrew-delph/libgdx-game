package app.user;

import java.util.UUID;

public class UserID {

    UUID id;

    private UserID(UUID id) {
        this.id = id;
    }

    public static UserID createUserID(String id){
        return new UserID(UUID.fromString(id));
    }

    public static UserID createUserID(UUID id){
        return new UserID(id);
    }

    public static UserID createUserID(){
        return new UserID(UUID.randomUUID());
    }
}
