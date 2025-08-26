package com.LostFound.MainProject.Controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.LostFound.MainProject.Dto.ClaimDto;
import com.LostFound.MainProject.Dto.UserDto;
import com.LostFound.MainProject.Entities.UserType;
import com.LostFound.MainProject.Service.ClaimService;
import com.LostFound.MainProject.Service.UserService;

@RestController
@RequestMapping("/admin")                           // <── base path
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminController {

    private final UserService  userService;
    private final ClaimService claimService;

    public UserAdminController(UserService userService,
                               ClaimService claimService) {
        this.userService  = userService;
        this.claimService = claimService;
    }

    /* ════════════════════  USERS  ═════════════════════════════════ */

    /** all users – lightweight DTO                              */
    @GetMapping("/users")
    public List<UserDto> allUsers() {
        return userService.findAllDto();
    }

    /** finder only (userType = FINDER)                          */
    @GetMapping("/users/finder")
    public List<UserDto> listFinders() {
        return userService.findByUserTypeDto(UserType.FINDER);   // ← helper in service
    }

    /** loser only (userType = LOSER)                            */
    @GetMapping("/users/loser")
    public List<UserDto> listLosers() {
        return userService.findByUserTypeDto(UserType.LOSER);
    }

    /** update role / userType / enabled flag                    */
    @PutMapping("/users/{id}")
    public UserDto updateUser(@PathVariable Long id,
                              @RequestBody UserDto dto) {
        return userService.adminUpdateUser(id, dto);
    }

    /** delete user + cascade their items / claims               */
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.adminDeleteUser(id);
    }

    /* ════════════════════  CLAIMS  ═════════════════════════════ */

    /** full claim list for grid                                 */
    @GetMapping("/claims")
    public List<ClaimDto> allClaims() {
        return claimService.findAll();          // no DTO → Angular already has ClaimDto
    }

                        
   
}
