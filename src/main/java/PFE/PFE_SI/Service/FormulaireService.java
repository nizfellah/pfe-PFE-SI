package PFE.PFE_SI.Service;

import PFE.PFE_SI.Model.Formulaire;
import PFE.PFE_SI.Repository.FormulaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FormulaireService {

    @Autowired
    private FormulaireRepository formulaireRepository;

    // Obtenir tous les rapports
    public List<Formulaire> getAllFormulaires() {
        return formulaireRepository.findAll();
    }

    // Obtenir un rapport par son numéro (ID)
    public Optional<Formulaire> getFormulaireById(String id) {
        return formulaireRepository.findById(id);
    }

    // Créer un nouveau rapport (le numéro doit être déjà généré dans le controller ou via une logique dédiée)
    public Formulaire createFormulaire(Formulaire formulaire) {
        // Exemple d'ajout de date de création si pas défini
        if (formulaire.getCreatedAt() == null) {
            formulaire.setCreatedAt(java.time.LocalDate.now());
        }
        return formulaireRepository.save(formulaire);
    }

    // Mettre à jour un rapport
    public Optional<Formulaire> updateFormulaire(String id, Formulaire formulaire) {
        return formulaireRepository.findById(id).map(existing -> {
            formulaire.setNumero(id); // s'assurer que le numéro reste identique
            // Copier ici d'autres champs de formulaire si besoin, ou simplement sauvegarder l'objet complet
            return formulaireRepository.save(formulaire);
        });
    }

    // Supprimer un rapport par son numéro
    public void deleteFormulaire(String id) {
        formulaireRepository.deleteById(id);
    }

    // Insertion multiple (bulk insert)
    public List<Formulaire> saveAll(List<Formulaire> formulaires) {
        // Optionnel: générer numéro unique si absent sur chaque formulaire
        // for (Formulaire f : formulaires) {
        //    if (f.getNumero() == null) {
        //        f.setNumero(generateNumeroUnique());
        //    }
        //    if (f.getCreatedAt() == null) {
        //        f.setCreatedAt(LocalDate.now());
        //    }
        // }
        return formulaireRepository.saveAll(formulaires);
    }

    // Méthode possible pour générer un numéro unique (à implémenter selon ta logique métier)
    // private String generateNumeroUnique() {
    //     // Ex : "yyMMdd-xxx" avec incrémentation, à récupérer depuis la BD pour éviter les collisions
    //     return "TODO";
    // }
}
