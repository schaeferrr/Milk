package us.rengo.milk.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import lombok.Getter;
import org.bson.Document;
import us.rengo.milk.MilkPlugin;
import us.rengo.milk.rank.Rank;

import java.util.HashMap;
import java.util.Map;

@Getter
public class RankManager {

    private Map<String, Rank> ranks = new HashMap<>();
    private MongoCollection<Document> collection = MilkPlugin.getInstance().getMongoDatabase().getCollection("ranks");

    public RankManager() {
        this.loadRanks();
    }

    private void loadRanks() {
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Rank rank = new Rank(document.getString("name"));

                rank.load(document);

                this.ranks.put(rank.getName(), rank);
            }
        }
    }

    public void saveRanks() {
        for (Rank rank : this.ranks.values()) {
            rank.save();
        }
    }
}
