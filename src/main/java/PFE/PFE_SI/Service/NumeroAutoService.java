package PFE.PFE_SI.Service;

import PFE.PFE_SI.Model.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class NumeroAutoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Génère un numéro unique au format yyMMdd-XXX
     * Exemple : 250713-001
     */
    public String genererNumero() {
        // Préfixe date au format yyMMdd
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

        // Création d'une requête atomique pour incrémenter le compteur du jour
        Query query = new Query(Criteria.where("_id").is(datePrefix));
        Update update = new Update().inc("sequenceValue", 1);
        FindAndModifyOptions options = FindAndModifyOptions.options()
                .returnNew(true)
                .upsert(true);

        // Mise à jour atomique du compteur dans la collection "counters"
        Counter counter = mongoTemplate.findAndModify(query, update, options, Counter.class, "counters");

        int increment = counter.getSequenceValue();

        // Formatage du numéro avec 3 chiffres incrémentaux
        return String.format("%s-%03d", datePrefix, increment);
    }
}
