package PFE.PFE_SI.Controller;

import PFE.PFE_SI.DTO.UserDto;
import PFE.PFE_SI.Model.Role;
import PFE.PFE_SI.Model.User;
import PFE.PFE_SI.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200") //
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Ajout du PasswordEncoder pour le hashing

    // Récupérer tous les utilisateurs (accessible uniquement aux administrateurs)
    @GetMapping
    public List<User> getAll() {
        return userService.getAllUser();
    }

    // Récupérer un utilisateur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/name/{lastname}")
    public ResponseEntity<User> getUserByLastname(@PathVariable String lastname) {
        User user = userService.getUserByLastname(lastname);  // corriger le nom de méthode ici
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        try {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    // Mise à jour d'un utilisateur (seulement s'il est authentifié)
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable String id, @RequestBody UserDto userDto) {
        User existingUser = userService.getUserById(id);
        if (existingUser != null) {
            existingUser.setFirstname(userDto.getFirstname());
            existingUser.setLastname(userDto.getLastname());
            existingUser.setEmail(userDto.getEmail());
            existingUser.setRole(Role.valueOf(userDto.getRole()));

            // Mettre à jour le mot de passe seulement si présent
            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }

            User updatedUser = userService.updateUser(id, existingUser);

            UserDto updatedDto = new UserDto();
            updatedDto.setId(updatedUser.getId());
            updatedDto.setFirstname(updatedUser.getFirstname());
            updatedDto.setLastname(updatedUser.getLastname());
            updatedDto.setEmail(updatedUser.getEmail());
            updatedDto.setRole(updatedUser.getRole().name());

            return ResponseEntity.ok(updatedDto);
        }
        return ResponseEntity.notFound().build();
    }


    // Suppression d'un utilisateur (accessible uniquement par un admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        User existingUser = userService.getUserById(id);
        if (existingUser != null) {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}

