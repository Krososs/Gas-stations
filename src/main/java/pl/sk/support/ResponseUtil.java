package pl.sk.support;

public enum ResponseUtil {

    USERNAME_TAKEN,
    PASSWORD_TOO_SHORT,
    COMMENT_TOO_LONG,
    STATION_DOES_NOT_EXISTS;

    public String ToString(){

        switch(this) {
            case USERNAME_TAKEN -> {
                return "Username is already taken";
            }
            case PASSWORD_TOO_SHORT -> {
                return "Password is too short";
            }
            case STATION_DOES_NOT_EXISTS -> {
                return "Given station does not exists";
            }
            case COMMENT_TOO_LONG -> {
                return "Comment is too long";
            }
            default -> {return "Problem not found";}
        }
    }

}
