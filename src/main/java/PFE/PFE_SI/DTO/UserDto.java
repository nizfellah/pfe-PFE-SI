package PFE.PFE_SI.DTO;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
    private String password; // Nouveau champ
}
