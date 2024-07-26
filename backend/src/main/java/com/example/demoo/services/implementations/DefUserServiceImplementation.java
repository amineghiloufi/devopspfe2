package com.example.demoo.services.implementations;

import com.example.demoo.domain.DefUser;
import com.example.demoo.domain.UserPrinciple;
import com.example.demoo.enumeratation.Role;
import com.example.demoo.exceptions.domain.EmailExistException;
import com.example.demoo.exceptions.domain.EmailNotFoundException;
import com.example.demoo.exceptions.domain.UsernameExistException;
import com.example.demoo.repositories.DefUserRepository;
import com.example.demoo.services.DefUserService;


import com.example.demoo.services.EmailService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static com.example.demoo.constants.UserImplementationConstant.*;
import static com.example.demoo.enumeratation.Role.ROLE_AGENCY;
import static com.example.demoo.enumeratation.Role.ROLE_CUSTOMER;

@Service
@Transactional
@Qualifier("userDetailsService")
public class DefUserServiceImplementation implements DefUserService, UserDetailsService {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private DefUserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private EmailService emailService;

    @Autowired
    public DefUserServiceImplementation(DefUserRepository defUserRepository,
                                        BCryptPasswordEncoder passwordEncoder,
                                        EmailService emailService) {
        this.userRepository = defUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DefUser user = userRepository.findDefUserByUsername(username);
        if (user == null) {
            LOGGER.error(USER_NOT_FOUND + username);
            throw new UsernameNotFoundException(USER_NOT_FOUND + username);
        } else {
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrinciple userPrincipal = new UserPrinciple(user);
            LOGGER.info(USER_ALREADY_EXISTS + username);
            return userPrincipal;
        }
    }

    @Override
    public DefUser registerCustomer(String username, String email, String customerFullName, Date customerBirthDate,
                                    String phoneNumber)
            throws EmailExistException, UsernameExistException, UsernameNotFoundException, MessagingException {
        validateNewUsernameAndEmail(StringUtils.EMPTY, username, email);
        DefUser user = new DefUser();
        user.setUserId(generateUserId());
        String password = generatePassword();
        String encodedPassword = encodePassword(password);
        user.setUsername(username);
        user.setEmail(email);
        user.setCustomerFullName(customerFullName);
        user.setCustomerBirthDate(customerBirthDate);
        user.setPhoneNumber(phoneNumber);
        user.setJoinDate(new Date());
        user.setPassword(encodedPassword);
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(ROLE_CUSTOMER.name());
        user.setAuthorities(ROLE_CUSTOMER.getAuthorities());
        userRepository.save(user);
        LOGGER.info("New password sent to :  " + email);
        emailService.sendNewPasswordEmail(email, password);
        return user;
    }

    public DefUser registerAgency(String username, String email, String agencyName, String agencyAddress, String agencyOpeningHours,
                                    String phoneNumber)
            throws EmailExistException, UsernameExistException, UsernameNotFoundException, MessagingException {
        validateNewUsernameAndEmail(StringUtils.EMPTY, username, email);
        DefUser user = new DefUser();
        user.setUserId(generateUserId());
        String password = generatePassword();
        String encodedPassword = encodePassword(password);
        user.setUsername(username);
        user.setEmail(email);
        user.setAgencyName(agencyName);
        user.setAgencyAddress(agencyAddress);
        user.setAgencyOpeningHours(agencyOpeningHours);
        user.setPhoneNumber(phoneNumber);
        user.setJoinDate(new Date());
        user.setPassword(encodedPassword);
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(ROLE_AGENCY.name());
        user.setAuthorities(ROLE_AGENCY.getAuthorities());
        userRepository.save(user);
        LOGGER.info("New password sent to :  " + email);
        emailService.sendNewPasswordEmail(email, password);
        return user;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private Long generateUserId() {
        return RandomUtils.nextLong();
    }

    private DefUser validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail)
            throws EmailExistException, UsernameExistException, UsernameNotFoundException {
        DefUser userByNewUsername = findDefUserByUsername(newUsername);
        DefUser defUserByNewEmail = findDefUserByEmail(newEmail);
        if (StringUtils.isNotBlank(currentUsername)) {
            DefUser currentUser = findDefUserByUsername(currentUsername);
            if (currentUser == null) {
                throw new UsernameNotFoundException(USER_NOT_FOUND + currentUsername);
            }
            if (userByNewUsername != null && !currentUser.getUserId().equals(userByNewUsername.getUserId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (defUserByNewEmail != null && !currentUser.getUserId().equals(defUserByNewEmail.getUserId())) {
                throw new EmailExistException(EMAIL_ALREADY_ASSOCIATED_TO_USER);
            }
            return currentUser;
        } else {
            if (userByNewUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (defUserByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_ASSOCIATED_TO_USER);
            }
            return null;
        }
    }

    @Override
    public List<DefUser> getUsers() {

        return userRepository.findAll();
    }

    @Override
    public DefUser findDefUserByUsername(String username) {
        return userRepository.findDefUserByUsername(username);
    }

    @Override
    public DefUser findDefUserByEmail(String email) {
        return userRepository.findDefUserByEmail(email);
    }

    @Override
    public DefUser addNewUser(String username, String email, String role, boolean isNotLocked, boolean isActive)
            throws EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail(EMPTY, username, email);
        DefUser user = new DefUser();
        String password = generatePassword();
        user.setUserId(generateUserId());
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodePassword(password));
        user.setJoinDate(new Date());
        user.setActive(isActive);
        user.setNotLocked(isNotLocked);
        user.setRole(getRoleEnumName(role).name());
        user.setAuthorities(getRoleEnumName(role).getAuthorities());
        userRepository.save(user);
        return user;
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    @Override
    public DefUser updateUser(String currentUsername, String newUsername, String newEmail, String role,
                              boolean isNotLocked, boolean isActive)
            throws EmailExistException, UsernameExistException {
        DefUser currentUser = validateNewUsernameAndEmail(currentUsername, newUsername, newEmail);
        assert currentUser != null;
        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        currentUser.setJoinDate(new Date());
        currentUser.setActive(isActive);
        currentUser.setNotLocked(isNotLocked);
        currentUser.setRole(getRoleEnumName(role).name());
        currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        userRepository.save(currentUser);
        return currentUser;
    }

    @Override
    public void deleteUser(String username) {
        DefUser user = userRepository.findDefUserByUsername(username);
        userRepository.deleteById(user.getUserId());
    }

    @Override
    public void resetPassword(String email) throws EmailNotFoundException, MessagingException{
        DefUser user = userRepository.findDefUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException(USER_BY_EMAIL_NOT_FOUND);
        }
        String password = generatePassword();
        user.setPassword(encodePassword(password));
        userRepository.save(user);
        emailService.sendNewPasswordEmail(user.getUsername(), user.getEmail());
    }
}