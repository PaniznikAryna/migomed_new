package com.migomed.Security;

import com.migomed.Entity.Users;
import com.migomed.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Autowired
    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        try {
            Long id = Long.parseLong(input);
            return usersRepository.findById(id)
                    .map(CustomUserDetails::new)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        } catch (NumberFormatException ex) {
            List<Users> users = usersRepository.findBySurname(input);
            if (users.isEmpty()) {
                throw new UsernameNotFoundException("User not found with surname: " + input);
            }
            return new CustomUserDetails(users.get(0));
        }
    }
}
