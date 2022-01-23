package app.user;

import com.google.inject.Inject;

public class User {
    private final UserID userID;

    @Inject
    public User() {
        this.userID = UserID.createUserID();
    }

    public UserID getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return userID.toString();
    }
}
