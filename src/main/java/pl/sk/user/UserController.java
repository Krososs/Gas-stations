package pl.sk.user;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sk.support.AuthUtil;

import java.util.HashMap;
import java.util.Map;

import static pl.sk.support.ResponseUtil.PASSWORD_TOO_SHORT;
import static pl.sk.support.ResponseUtil.USERNAME_TAKEN;

@RestController
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users/authorize")
    public ResponseEntity<?> authorize(@RequestHeader("token") String token) {
        try{
            AuthUtil.getUsernameFromToken(token);
        }catch (TokenExpiredException exception){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Map<String, String> data = new HashMap<>();
        data.put("username", AuthUtil.getUsernameFromToken(token));
        return new ResponseEntity<>(data,HttpStatus.OK);
    }

    @PostMapping("users/register")
    public ResponseEntity<?> register(@ModelAttribute User user){

        JSONObject error = new JSONObject();

        if(userService.usernameTaken(user.getUsername())){
            error.put("error", USERNAME_TAKEN.ToString());
            return new ResponseEntity<>(error.toMap(), HttpStatus.CONFLICT);
        }

        if(user.getPassword().length()<=5){
            error.put("error", PASSWORD_TOO_SHORT.ToString());
            return new ResponseEntity<>(error.toMap(), HttpStatus.CONFLICT);
        }

        userService.register(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
