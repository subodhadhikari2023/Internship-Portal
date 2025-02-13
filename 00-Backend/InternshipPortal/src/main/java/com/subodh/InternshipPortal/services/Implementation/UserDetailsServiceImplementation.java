package com.subodh.InternshipPortal.services.Implementation;


import com.subodh.InternshipPortal.entities.UserPrincipal;
import com.subodh.InternshipPortal.entities.Users;
import com.subodh.InternshipPortal.repositories.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImplementation implements UserDetailsService {
    final UsersRepository usersRepository;

    @Autowired
    public UserDetailsServiceImplementation(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByUserEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrincipal(user);
    }

}
