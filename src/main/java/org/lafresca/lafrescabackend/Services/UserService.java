package org.lafresca.lafrescabackend.Services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lafresca.lafrescabackend.DTO.Request.StockRequestDTO;
import org.lafresca.lafrescabackend.DTO.UserDTO;
import org.lafresca.lafrescabackend.Models.User;
import org.lafresca.lafrescabackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.lafresca.lafrescabackend.Validations.UserValidation.isValidEmail;


@Service
@Data
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final SystemLogService systemLogService;

//    @Autowired
//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    public List<UserDTO> getUsers() {
        System.out.println("Getting all users");
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user: users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getUserId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setAddress(user.getAddress());
            userDTO.setRole(user.getRole());
            userDTO.setCafeId(user.getCafeId());
            userDTO.setStatus(user.getStatus());
            userDTOS.add(userDTO);
        }

        String user= SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();

        String logmessage = now + " " + user + " " + "Retrieve all users";
        systemLogService.writeToFile(logmessage);
        log.info(logmessage);
        return userDTOS;
    }

    public ResponseEntity<UserDTO> addNewUser(User user) {
        UserDTO newUserDTO = new UserDTO(user.getId(),user.getFirstName(),user.getLastName(),user.getEmail(),user.getPhoneNumber(),user.getAddress(),user.getRole(),user.getCafeId(),user.getStatus());
        Optional<User> userByEmail = userRepository.findUserByEmail(user.getEmail());
        if (userByEmail.isPresent()) {
            return ResponseEntity.status(401).body(null);
        }
        else {
            user.setEmail(user.getEmail());
        }
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            return ResponseEntity.status(403).body(newUserDTO);
        }
        if(user.getLastName() == null || user.getLastName().isEmpty()) {
            return ResponseEntity.status(403).body(newUserDTO);
        }
        if(user.getEmail() == null || user.getEmail().isEmpty() || !isValidEmail(user.getEmail()) ) {
            return ResponseEntity.status(403).body(newUserDTO);
        }
        if(user.getPhoneNumber() == null || user.getPhoneNumber().length() != 10) {
            return ResponseEntity.status(403).body(newUserDTO);
        }
        if(user.getRole() == null || user.getRole().isEmpty()) {
            return ResponseEntity.status(403).body(newUserDTO);
        }
        if(!(user.getRole()!="ADMIN" || user.getRole()!="CUSTOMER" || user.getRole()!="TOP_LEVEL_MANAGER" || user.getRole()!="CAFE_MANAGER" || user.getRole()!="CASHIER" || user.getRole()!="KITCHEN_MANAGER" || user.getRole()!="WAITER" || user.getRole()!="DELIVERY_PERSON" || user.getRole()!="STOCKKEEPER" || user.getRole()!="BRANCH_MANAGER")) {
            return ResponseEntity.status(403).body(newUserDTO);
        }
        if(user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.status(403).body(newUserDTO);
        }
        if(user.getPassword().length() < 6) {
            return ResponseEntity.status(403).body(newUserDTO);
        }
        if(user.getAddress()==null || user.getAddress().isEmpty()) {
            return ResponseEntity.status(403).body(newUserDTO);
        }
        if(user.getRole().equals("WAITER") || user.getRole().equals("DELIVERY_PERSON")) {
            user.setStatus("AVAILABLE");
            user.setStatusUpdatedAt(System.currentTimeMillis());
        }
//        need to add
//        Optional<Cafe> cafe = cafeRepository.findById(user.getCafeId());
//        if(!cafe.isPresent()) {
//            throw new IllegalStateException("Cafe with id " + user.getCafeId() + " does not exist");
//        }

        if (Objects.equals(user.getRole(), "BRANCH_MANAGER")){
            user.setCafeId("");
        }
        User saveduser = userRepository.save(user);

        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();

        String logmessage = now + " " + username + " " + "Created new user (id: " + saveduser.getId() + ")";
        systemLogService.writeToFile(logmessage);
        log.info(logmessage);

        return ResponseEntity.status(201).body(newUserDTO);
    }

    public void deleteUser(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with id " + userId + " does not exist"));

        String user= SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();

        String logmessage = now + " " + user + " " + "Deleted user (id: " + userId + ")";
        systemLogService.writeToFile(logmessage);
        log.info(logmessage);

        userRepository.deleteById(userId);
    }

    public ResponseEntity<User> updateUser(User user) {
        User userToUpdate = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new IllegalStateException("User with id " + user.getUserId() + " does not exist"));

        if (user.getFirstName() != null && !user.getFirstName().isEmpty() && !userToUpdate.getFirstName().equals(user.getFirstName())) {
            userToUpdate.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null && !user.getLastName().isEmpty() && !userToUpdate.getLastName().equals(user.getLastName())) {
            userToUpdate.setLastName(user.getLastName());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty() && !userToUpdate.getEmail().equals(user.getEmail()) && isValidEmail(user.getEmail())) {
            userToUpdate.setEmail(user.getEmail());
        }
        if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty() && !userToUpdate.getPhoneNumber().equals(user.getPhoneNumber()) && user.getPhoneNumber().length() == 10){
            userToUpdate.setPhoneNumber(user.getPhoneNumber());
        }
        if (user.getRole() != null && !user.getRole().isEmpty() && !userToUpdate.getRole().equals(user.getRole()) && ((user.getRole()=="ADMIN" || user.getRole()=="CUSOTMER" || user.getRole()=="TOP_LEVEL_MANAGER" || user.getRole()=="BRANCH_MANAGER" || user.getRole()=="CASHIER" || user.getRole()=="KITCHEN_MANAGER" || user.getRole()=="WAITER" || user.getRole()=="DELIVERY_PERSON" || user.getRole()=="STOCKKEEPER"))) {
            userToUpdate.setRole(user.getRole());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty() && !userToUpdate.getPassword().equals(user.getPassword())) {
            userToUpdate.setPassword(user.getPassword());
        }
        if (user.getAddress() != null && !user.getAddress().isEmpty() && !userToUpdate.getAddress().equals(user.getAddress())) {
            userToUpdate.setAddress(user.getAddress());
        }
//        Optional<Cafe> cafe = cafeRepository.findById(user.getCafeId());
//        if (user.getCafeId() != null && user.getCafeId() != 0 && !userToUpdate.getCafeId().equals(user.getCafeId()) && cafe.isPresent()) {
//            userToUpdate.setCafeId(user.getCafeId());
//        }
        userRepository.save(userToUpdate);

        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();

        String logmessage = now + " " + username + " " + "Updated user (id: " + user.getId() + ")";
        systemLogService.writeToFile(logmessage);
        log.info(logmessage);
        return ResponseEntity.status(200).body(user);
    }

    public UserDTO getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with id " + userId + " does not exist"));
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getUserId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setAddress(user.getAddress());
        userDTO.setRole(user.getRole());
        userDTO.setCafeId(user.getCafeId());

        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();

        String logmessage = now + " " + username + " " + "Retrieve user details (id: " + userId + ")";
        systemLogService.writeToFile(logmessage);
        log.info(logmessage);

        return userDTO;
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with email " + email + " does not exist"));
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getUserId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setAddress(user.getAddress());
        userDTO.setRole(user.getRole());
        userDTO.setCafeId(user.getCafeId());
        userDTO.setStatus(user.getStatus());

        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();

        String logmessage = now + " " + username + " " + "Retrieve user by email (email: " + email + ")";
        systemLogService.writeToFile(logmessage);
        log.info(logmessage);

        return userDTO;
    }

    public List<UserDTO> getUsersByCafeId(Long cafeId) {
        List<User> users = userRepository.findUsersByCafeId(cafeId);
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user: users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getUserId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setAddress(user.getAddress());
            userDTO.setRole(user.getRole());
            userDTO.setCafeId(user.getCafeId());
            userDTOS.add(userDTO);
        }

        String user= SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();

        String logmessage = now + " " + user + " " + "Retrieve users related to cafe id (id: " + cafeId + ")";
        systemLogService.writeToFile(logmessage);
        log.info(logmessage);

        return userDTOS;
    }

    public List<UserDTO> getUsersByRole(String role) {
        System.out.println("Getting all users by role "+role);
        List<User> users = userRepository.findUsersByRole(role);
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user: users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getUserId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setAddress(user.getAddress());
            userDTO.setRole(user.getRole());
            userDTO.setCafeId(user.getCafeId());
            userDTOS.add(userDTO);
        }

        String user= SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();

        String logmessage = now + " " + user + " " + "Retrieve users have role " + role;
        systemLogService.writeToFile(logmessage);
        log.info(logmessage);

        return userDTOS;
    }

    public List<User> getAllUsers() {
        String user= SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();

        String logmessage = now + " " + user + " " + "Retrieve all users with passwords";
        systemLogService.writeToFile(logmessage);
        log.info(logmessage);

        return userRepository.findAll();
    }

    public List<UserDTO> getAvaliableBranchManagers(){
        List<User> users = userRepository.findUserByCafeIdAndRole("","BRANCH_MANAGER");
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user: users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getUserId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setAddress(user.getAddress());
            userDTO.setRole(user.getRole());
            userDTO.setCafeId(user.getCafeId());
            userDTO.setStatus(user.getStatus());
            userDTOS.add(userDTO);
        }

        String user= SecurityContextHolder.getContext().getAuthentication().getName();
        LocalDateTime now = LocalDateTime.now();

        String logmessage = now + " " + user + " " + "Retrieve available branch managers";
        systemLogService.writeToFile(logmessage);
        log.info(logmessage);

        return userDTOS;

    }

    public List<UserDTO> getUsersByRoleAndCafeId(String role, String cafeId) {
        List<User> users =  userRepository.findUserByCafeIdAndRole(cafeId,role);
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : users){
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setRole(user.getRole());
            userDTO.setCafeId(user.getCafeId());
            userDTO.setEmail(user.getEmail());
            userDTO.setAddress(user.getAddress());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setStatus(user.getStatus());

            userDTOList.add(userDTO);
        }
        return userDTOList;
    }
}