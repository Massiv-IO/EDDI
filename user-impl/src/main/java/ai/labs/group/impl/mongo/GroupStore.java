package ai.labs.group.impl.mongo;

import ai.labs.group.IGroupStore;
import ai.labs.group.model.Group;
import ai.labs.persistence.IResourceStore;
import ai.labs.serialization.IJsonSerialization;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author ginccc
 */
@Slf4j
public class GroupStore implements IGroupStore {
    private static final String COLLECTION_GROUPS = "groups";
    private final MongoCollection<Document> collection;
    private final IJsonSerialization jsonSerialization;

    @Inject
    public GroupStore(MongoDatabase database, IJsonSerialization jsonSerialization) {
        collection = database.getCollection(COLLECTION_GROUPS);
        this.jsonSerialization = jsonSerialization;
    }

    public Group readGroup(String groupId) throws IResourceStore.ResourceStoreException, IResourceStore.ResourceNotFoundException {
        Document groupDocument = collection.find(new Document("_id", new ObjectId(groupId))).first();

        try {
            if (groupDocument == null) {
                String message = "Resource 'Group' not found. (groupId=%s)";
                message = String.format(message, groupId);
                throw new IResourceStore.ResourceNotFoundException(message);
            }

            groupDocument.remove("_id");

            return jsonSerialization.deserialize(groupDocument.toString(), Group.class);
        } catch (IOException e) {
            log.debug(e.getLocalizedMessage(), e);
            throw new IResourceStore.ResourceStoreException("Cannot parse json structure into Group entity.", e);
        }
    }


    @Override
    public void updateGroup(String groupId, Group group) {
        String jsonGroup = JSON.serialize(group);
        Document document = Document.parse(jsonGroup);

        document.put("_id", new ObjectId(groupId));

        collection.insertOne(document);
    }

    @Override
    public String createGroup(Group group) throws IResourceStore.ResourceStoreException {
        String jsonGroup = serialize(group);
        Document document = Document.parse(jsonGroup);

        collection.insertOne(document);

        return document.get("_id").toString();
    }

    private String serialize(Group group) throws IResourceStore.ResourceStoreException {
        try {
            return jsonSerialization.serialize(group);
        } catch (IOException e) {
            log.debug(e.getLocalizedMessage(), e);
            throw new IResourceStore.ResourceStoreException("Cannot serialize Group entity into json.", e);
        }
    }

    @Override
    public void deleteGroup(String groupId) {
        collection.deleteOne(new BasicDBObject("_id", new ObjectId(groupId)));
    }
}
