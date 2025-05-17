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
    public UserDetails loadUserByUsername(String surname) throws UsernameNotFoundException {
        // Получаем список пользователей с данной фамилией
        List<Users> users = usersRepository.findBySurname(surname);
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("User not found with surname: " + surname);
        }
        // Если найдено несколько записей, выбираем первого
        Users user = users.get(0);
        return new CustomUserDetails(user); // Убедитесь, что класс CustomUserDetails существует
    }
}
