package com.migomed.Security;

import com.migomed.Entity.Users;
import com.migomed.Entity.Worker;
import com.migomed.Repository.UsersRepository;
import com.migomed.Repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User; // Spring Security User
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final WorkerRepository workerRepository;

    @Autowired
    public CustomUserDetailsService(UsersRepository usersRepository, WorkerRepository workerRepository) {
        this.usersRepository = usersRepository;
        this.workerRepository = workerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        Users user;

        if (input.matches("\\d+")) {
            Long userId = Long.parseLong(input);
            Optional<Users> optionalUser = usersRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                throw new UsernameNotFoundException("Пользователь с id " + userId + " не найден");
            }
            user = optionalUser.get();
        } else {
            Optional<Users> optionalUser = usersRepository.findBySurname(input);
            if (optionalUser.isEmpty()) {
                throw new UsernameNotFoundException("Пользователь с фамилией " + input + " не найден");
            }
            user = optionalUser.get();
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Optional<Worker> optionalWorker = workerRepository.findByUser_Id(user.getId());
        if (optionalWorker.isPresent() && Boolean.TRUE.equals(optionalWorker.get().getAdmin())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return new User(String.valueOf(user.getId()), user.getPassword(), authorities);

    }
}
