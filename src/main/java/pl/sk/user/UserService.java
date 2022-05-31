package pl.sk.user;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sk.support.AuthUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserService  implements UserDetailsService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public boolean usernameTaken(String username){
        return userRepository.findByUsername(username).isPresent();
    }

    public Long getUserId(String token){
        String username;
        try {
            username = AuthUtil.getUsernameFromToken(token);

        } catch (TokenExpiredException exception){
            return -1L;
        }
        return userRepository.findByUsername(username).get().getId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> tuser = userRepository.findByUsername(username);

        if(tuser.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }else{
            User user = userRepository.findByUsername(username).get();
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),authorities);
        }
    }

}
