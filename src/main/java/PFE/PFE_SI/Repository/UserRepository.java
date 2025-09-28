package PFE.PFE_SI.Repository;
import PFE.PFE_SI.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    // Recherche par nom (si tu veux l’utiliser quelque part)
    User findUserByLastname(String lastname);

    // Recherche par email (utile pour l'authentification Spring Security)
    Optional<User> findByEmail(String email);



}
