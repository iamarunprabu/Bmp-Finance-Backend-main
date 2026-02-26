package com.security.JWT.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.security.JWT.Bean.User;
import com.security.JWT.Exception.EmailExistException;
import com.security.JWT.Exception.EmailNotFoundException;
import com.security.JWT.Exception.NotAnImageFileException;
import com.security.JWT.Exception.UserNotFoundException;
import com.security.JWT.Exception.UsernameExistException;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import jakarta.mail.MessagingException;

public interface UserService {
	User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException;

    List<User> getUsers();

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;

    User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;

    void deleteUser(String username) throws IOException;

    void resetPassword(String email) throws MessagingException, EmailNotFoundException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException;

    User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;

    Page<User> findPaginatedUsers(int page, int size);

	Page<User> findPaginatedUsersKeyword(String keyword, int page, int size);

	 long getUserCount();
}

