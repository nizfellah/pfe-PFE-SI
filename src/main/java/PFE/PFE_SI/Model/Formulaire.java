package PFE.PFE_SI.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;

@Document(collection = "formulaires")
@Data
public class Formulaire {

    @Id
    private String numero; // format : yyMMdd-001

    private String finding;
    private String concernedProcess;
    private String type;
    private String source;
    private String causesAnalysis;
    private String action;
    private String type2;
    private String owner;
    private Date deadline;
    private String status;
    private Date effectivenessDate;
    private String effectivenessResults;









    private String piloteId;
    private String utilisateurId;

    // 🟢 Nouveau champ : nom complet de l'admin
    private String createdBy;

    // 📅 Nouveau champ : date de création (sans heure)
    private LocalDate createdAt;
}
