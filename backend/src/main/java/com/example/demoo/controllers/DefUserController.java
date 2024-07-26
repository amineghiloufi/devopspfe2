package com.example.demoo.controllers;

import com.example.demoo.domain.DefUser;
import com.example.demoo.domain.HttpResponse;
import com.example.demoo.domain.UserPrinciple;
import com.example.demoo.exceptions.ExceptionHandling;
import com.example.demoo.exceptions.domain.EmailExistException;
import com.example.demoo.exceptions.domain.EmailNotFoundException;
import com.example.demoo.exceptions.domain.UserNotFoundException;
import com.example.demoo.exceptions.domain.UsernameExistException;
import com.example.demoo.repositories.DefUserRepository;
import com.example.demoo.services.DefUserService;
import com.example.demoo.utility.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

import static com.example.demoo.constants.SecurityConstant.JWT_TOKEN_HEADER;


@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:4200")
public class DefUserController extends ExceptionHandling {

    public static final String EMAIL_SENT = "An email with a new password is sent to : ";
    public static final String USER_DELETED = "User has been deleted successfully";
    private DefUserService defUserService;
    private DefUserRepository defUserRepository;
    private AuthenticationManager authenticationManager;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public DefUserController(DefUserService defUserService,
                             DefUserRepository defUserRepository,
                             AuthenticationManager authenticationManager,
                             JWTTokenProvider jwtTokenProvider) {
        this.defUserService = defUserService;
        this.defUserRepository = defUserRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/home")
    public String showUser()throws UserNotFoundException {
        throw new UserNotFoundException("User was not found");
    }

    @PostMapping("/registerCustomer")
    public ResponseEntity<DefUser> registerCustomer(@RequestBody DefUser user)
            throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {
        DefUser newUser = defUserService.registerCustomer(user.getUsername(), user.getEmail(),
                user.getCustomerFullName(), user.getCustomerBirthDate(), user.getPhoneNumber());
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @PostMapping("/registerAgency")
    public ResponseEntity<DefUser> registerAgency(@RequestBody DefUser user)
            throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {
        DefUser newUser = defUserService.registerAgency(user.getUsername(), user.getEmail(),
                user.getAgencyName(), user.getAgencyAddress(), user.getAgencyOpeningHours(),
                user.getPhoneNumber());
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<DefUser> register(@RequestBody DefUser user)
            throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {
        DefUser newUser = defUserService.registerAgency(user.getUsername(), user.getEmail(),
                user.getAgencyName(), user.getAgencyAddress(), user.getAgencyOpeningHours(),
                user.getPhoneNumber());
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<DefUser> login(@RequestBody DefUser user) {
        authenticate(user.getUsername(), user.getPassword());
        DefUser loginUser = defUserService.findDefUserByUsername(user.getUsername());
        UserPrinciple userPrinciple = new UserPrinciple(loginUser);
        HttpHeaders jwtHeader = getJWTHeader(userPrinciple);
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<DefUser> addNewUser(@RequestParam("username") String username,
                                              @RequestParam("email") String email,
                                              @RequestParam("role") String role,
                                              @RequestParam("isActive") String isActive,
                                              @RequestParam("isNotLocked") String isNotLocked)
            throws EmailExistException, UsernameExistException {
        DefUser newUser = defUserService.addNewUser(username, email, role, Boolean.parseBoolean(isActive),
                Boolean.parseBoolean(isNotLocked));
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<DefUser> updateUser(@RequestParam("currentUser") String currentUser,
                                              @RequestParam("username") String username,
                                              @RequestParam("email") String email,
                                              @RequestParam("role") String role,
                                              @RequestParam("isActive") String isActive,
                                              @RequestParam("isNotLocked") String isNotLocked)
            throws EmailExistException, UsernameExistException {
        DefUser updatedUser = defUserService.updateUser(currentUser, username, email, role,
                Boolean.parseBoolean(isActive),
                Boolean.parseBoolean(isNotLocked));
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<DefUser> getUser(@PathVariable("username") String username) {
        DefUser user = defUserService.findDefUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @GetMapping("/list")
    public ResponseEntity<List<DefUser>> getAllUsers() {
        List<DefUser> users = defUserService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/resetpassword/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email)
            throws EmailNotFoundException, MessagingException {
        defUserService.resetPassword(email);
        return response(HttpStatus.OK, EMAIL_SENT + email);
    }

    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username) {
        defUserService.deleteUser(username);
        return response(HttpStatus.OK, USER_DELETED);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        HttpResponse body = new HttpResponse(httpStatus.value(),
                httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(),
                message.toUpperCase());
        return new ResponseEntity<>(body, httpStatus);
    }

    private HttpHeaders getJWTHeader(UserPrinciple user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJWTToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}