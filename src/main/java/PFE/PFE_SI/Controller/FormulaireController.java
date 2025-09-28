package PFE.PFE_SI.Controller;

import PFE.PFE_SI.Model.Formulaire;
import PFE.PFE_SI.Model.User;
import PFE.PFE_SI.Repository.UserRepository;
import PFE.PFE_SI.Service.FormulaireService;
import PFE.PFE_SI.Service.NumeroAutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/formulaires")
public class FormulaireController {

    @Autowired
    private FormulaireService formulaireService;

    @Autowired
    private NumeroAutoService numeroAutoService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Formulaire> getAll() {
        return formulaireService.getAllFormulaires();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Formulaire> getById(@PathVariable String id) {
        return formulaireService.getFormulaireById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Formulaire> create(@RequestBody Formulaire formulaire, Principal principal) {
        String numero = numeroAutoService.genererNumero();
        formulaire.setNumero(numero);

        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            formulaire.setCreatedBy(user.getFirstname() + " " + user.getLastname());
        } else {
            formulaire.setCreatedBy("Utilisateur inconnu");
        }

        formulaire.setCreatedAt(LocalDate.now());

        Formulaire created = formulaireService.createFormulaire(formulaire);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Formulaire> update(@PathVariable String id, @RequestBody Formulaire formulaire) {
        return formulaireService.updateFormulaire(id, formulaire)
                .map(updated -> ResponseEntity.ok(updated))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        formulaireService.deleteFormulaire(id);
    }

    @GetMapping("/kpi")
    public Map<String, Object> getKpis() {
        List<Formulaire> all = formulaireService.getAllFormulaires();

        long total = all.size();
        long enCours = all.stream()
                .filter(f -> "en cours".equalsIgnoreCase(f.getStatus()))
                .count();
        long termines = all.stream()
                .filter(f -> "terminé".equalsIgnoreCase(f.getStatus()) || "terminée".equalsIgnoreCase(f.getStatus()))
                .count();
        long enRetard = all.stream()
                .filter(f -> "en cours".equalsIgnoreCase(f.getStatus()) && isDeadlinePassed(f.getDeadline()))
                .count();

        Map<String, Object> kpis = new HashMap<>();
        kpis.put("total", total);
        kpis.put("enCours", enCours);
        kpis.put("termines", termines);
        kpis.put("enRetard", enRetard);

        return kpis;
    }

    private boolean isDeadlinePassed(Date deadline) {
        if (deadline == null) return false;
        return deadline.before(new Date());
    }

    // Endpoint POST bulk pour importer plusieurs formulaires (Excel import)
    @PostMapping("/bulk")
    public ResponseEntity<List<Formulaire>> bulkInsert(@RequestBody List<Formulaire> formulaires, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        String createdBy = (user != null) ? user.getFirstname() + " " + user.getLastname() : "Utilisateur inconnu";

        LocalDate now = LocalDate.now();

        // Préparer les formulaires : numéro automatique + createdBy + createdAt si manquant
        for (Formulaire f : formulaires) {
            if (f.getNumero() == null || f.getNumero().isEmpty()) {
                f.setNumero(numeroAutoService.genererNumero());
            }
            if (f.getCreatedBy() == null || f.getCreatedBy().isEmpty()) {
                f.setCreatedBy(createdBy);
            }
            if (f.getCreatedAt() == null) {
                f.setCreatedAt(now);
            }
        }

        List<Formulaire> saved = formulaireService.saveAll(formulaires);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
