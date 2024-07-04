package com.cydeo.controller;


import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @RolesAllowed({"Manager","Admin"})
    public ResponseEntity<ResponseWrapper> getUser() {

        List<UserDTO> users = userService.listAllUsers();

        return ResponseEntity.ok( new ResponseWrapper("Users are successfully retrieved",users, HttpStatus.OK));

    }

    @GetMapping("{username}")
    @RolesAllowed({"Admin"})
    public ResponseEntity<ResponseWrapper> getUserByUserName(@PathVariable ("username") String userName) {

        UserDTO user =userService.findByUserName(userName);

        return ResponseEntity.ok( new ResponseWrapper("Users are successfully retrieved",user, HttpStatus.OK));
    }

    @PostMapping
    @RolesAllowed({"Admin"})
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDTO user) {

        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("user is created",user,HttpStatus.CREATED));
    }

    @PutMapping
    @RolesAllowed({"Admin"})
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO user) {
        userService.update(user);
        return ResponseEntity.ok(new ResponseWrapper("user updated",HttpStatus.OK));
    }

    @DeleteMapping("{username}")
    @RolesAllowed({"Admin"})
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable ("username") String userName) {
        userService.delete(userName);
        return ResponseEntity.ok(new ResponseWrapper("user is deleted",HttpStatus.OK));
    }


}
