package com.rcr.controller;

import com.rcr.configuration.JwtTokenProvider;
import com.rcr.model.User;
import com.rcr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    @GetMapping("/jwt/user")
    public ResponseEntity<User> getUserByJwt(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @GetMapping("/email/user")
    public ResponseEntity<User> getUserByEmail(@RequestHeader("Authorization") String jwt,
                                               @RequestBody String email) throws Exception {
        User user = userService.findUserByEmail(email);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
