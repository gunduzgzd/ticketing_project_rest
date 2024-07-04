package com.cydeo.controller;


import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getUser() {

        List<UserDTO> users = userService.listAllUsers();

        return ResponseEntity.ok( new ResponseWrapper("Users are successfully retrieved",users, HttpStatus.OK));

    }

    @GetMapping("{username}")
    public ResponseEntity<ResponseWrapper> getUserByUserName(@PathVariable ("username") String userName) {

        UserDTO user =userService.findByUserName(userName);

        return ResponseEntity.ok( new ResponseWrapper("Users are successfully retrieved",user, HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDTO user) {

        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("user is created",user,HttpStatus.CREATED));
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO user) {
        userService.update(user);
        return ResponseEntity.ok(new ResponseWrapper("user updated",HttpStatus.OK));
    }

    @DeleteMapping("{username}")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable ("username") String userName) {
        userService.delete(userName);
        return ResponseEntity.ok(new ResponseWrapper("user is deleted",HttpStatus.OK));
    }


}
