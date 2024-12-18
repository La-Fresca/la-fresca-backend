package org.lafresca.lafrescabackend.Controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.lafresca.lafrescabackend.DTO.Request.StockRequestDTO;
import org.lafresca.lafrescabackend.DTO.UserDTO;
import org.lafresca.lafrescabackend.Models.Role;
import org.lafresca.lafrescabackend.Models.User;
import org.lafresca.lafrescabackend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/lafresca/user")
@Tag(name="User Controller")
@AllArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(value = "/specificuserbyid/{userId}")
    public UserDTO getUser(@PathVariable("userId") String userId) {
        return userService.getUserById(userId);
    }

    @GetMapping(value = "/specificuserbyemail/{email}")
    public UserDTO getUserByEmail(@PathVariable("email") String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping(value = "/specificuserbycafeid/{cafeId}")
    @Operation(
            description = "Get user list by cafeId",
            summary = "Get user by specific id",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403")
            })
    public List<UserDTO> getUsersByCafeId(@PathVariable("cafeId") Long cafeId) {
        return userService.getUsersByCafeId(cafeId);
    }

    @GetMapping(value = "/specificuserbyrole/{role}")
    public List<UserDTO> getUsersByRole(@PathVariable("role") String role) {
        System.out.println("\n\n\n\n\nRole: " + role);
        return userService.getUsersByRole(role);
    }

    //get all users without filtering
    @GetMapping(value = "/allusers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<UserDTO> registerNewUser(@RequestBody User user) {
        return userService.addNewUser(user);
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping(value = "/availableBranchManagers")
    public List<UserDTO> getAvailableBranchManagers() {
        List<UserDTO> avaliableBranchManagers = userService.getAvaliableBranchManagers();
        return avaliableBranchManagers;
    }

    @GetMapping(value = "/getWaiters/{cafeId}")
    public List<UserDTO> getWaitersListByCafe(@PathVariable("cafeId") String cafeId){
        List<UserDTO> waiters = userService.getUsersByRoleAndCafeId(Role.WAITER.toString(),cafeId);
        return waiters;
    }

    @GetMapping(value = "/getDeliveryPersons/{cafeId}")
    public List<UserDTO> getDeliveryPersonsListByCafe(@PathVariable("cafeId") String cafeId){
        List<UserDTO> deliveryPersons = userService.getUsersByRoleAndCafeId(Role.DELIVERY_PERSON.toString(),cafeId);
        return deliveryPersons;
    }
}