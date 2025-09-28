package PFE.PFE_SI.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "counters")
public class Counter {

    @Id
    private String id; // Format : yyMMdd

    private int sequenceValue;

    // Getter pour sequenceValue
    public int getSequenceValue() {
        return sequenceValue;
    }

    // Setter pour sequenceValue
    public void setSequenceValue(int sequenceValue) {
        this.sequenceValue = sequenceValue;
    }

    // Getter pour id
    public String getId() {
        return id;
    }

    // Setter pour id
    public void setId(String id) {
        this.id = id;
    }
}
