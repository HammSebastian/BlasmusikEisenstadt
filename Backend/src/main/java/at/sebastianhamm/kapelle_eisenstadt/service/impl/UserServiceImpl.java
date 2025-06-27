package at.sebastianhamm.kapelle_eisenstadt.service.impl;

import at.sebastianhamm.kapelle_eisenstadt.dao.UserDao;
import at.sebastianhamm.kapelle_eisenstadt.dto.UserDto;
import at.sebastianhamm.kapelle_eisenstadt.exception.UserAlreadyExistsException;
import at.sebastianhamm.kapelle_eisenstadt.models.User;
import at.sebastianhamm.kapelle_eisenstadt.service.UserService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service(value = "userService")
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    private BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("loading user {}",username);
        Optional<User> user = userDao.findByUsername(username);

        if(user.isEmpty()){
            logger.warn("User {} not found", username);
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(), getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    public List<UserDto> findAll() {
        logger.debug("Retrieving user list");
        List<UserDto> list = new ArrayList<>();
        userDao.findAll().iterator().forEachRemaining(u -> list.add(map(u)));
        return list;
    }

    @Override
    public void delete(Long id) {
        Validate.notNull(id, "id must not be null");
        logger.info("Deleting user with id {}",id);
        userDao.deleteById(id);
    }

    @Override
    public UserDto findByName(String username) {
        Validate.notNull(username,"username must not be null");
        logger.debug("Find user by useranme {}",username);
        Optional<User> optionalUser = userDao.findByUsername(username);
        return optionalUser.map(this::map).orElse(null);
    }

    @Override
    public UserDto findById(Long id) {
        Validate.notNull(id,"id must not be null");
        logger.debug("Find user by id {}",id);
        Optional<User> optionalUser = userDao.findById(id);
        return optionalUser.map(this::map).orElse(null);
    }

    @Override
    public UserDto update(UserDto userDto) {
        Validate.notNull(userDto);
        Validate.notNull(userDto.getId(), "userDto.id must not be null");
        logger.info("Updating user {}",userDto.getId());

        Optional<User> optionalUser = userDao.findById(userDto.getId());
        Validate.isTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        BeanUtils.copyProperties(userDto, user, "password");
        userDao.save(user);
        return userDto;
    }

    @Override
    public UserDto save(UserDto userDto) {
        Validate.notNull(userDto);
        Validate.notNull(userDto.getUsername(), "username must not be null");

        logger.info("Saving user new user {}",userDto.getUsername());

        //Username must be unique. Therefore, we search if there is already a user with the same username.
        //If a user with this username already exists we cancel saving with an exception.
        Optional<User> user = userDao.findByUsername(userDto.getUsername());
        if(user.isPresent()){
            logger.warn("User {} already exists", userDto.getUsername());
            throw new UserAlreadyExistsException("Username already exists!");
        }

        User newUser = new User();
        BeanUtils.copyProperties(userDto, newUser, "password");
        newUser.setPassword(bcryptEncoder.encode(userDto.getPassword()));

        newUser=userDao.save(newUser);
        return map(newUser);
    }

    private UserDto map(User user){
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto, "password");
        return userDto;
    }

}
