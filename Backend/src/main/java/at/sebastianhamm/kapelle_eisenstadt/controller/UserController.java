package at.sebastianhamm.kapelle_eisenstadt.controller;

import at.sebastianhamm.kapelle_eisenstadt.dto.UserDto;
import at.sebastianhamm.kapelle_eisenstadt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public UserDto saveUser(@RequestBody UserDto user){
        return userService.save(user);
    }

    @GetMapping
    //@Secured("ROLE_ADMIN")
    public List<UserDto> listUser(){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id){
        return userService.findById(id);
    }

    @PutMapping("/{id}")
    public UserDto update(@RequestBody UserDto userDto) {
        return userService.update(userDto);
    }

    @DeleteMapping("/{id}")
    public Void delete(@PathVariable Long id) {
        userService.delete(id);
        return null;
    }
}
