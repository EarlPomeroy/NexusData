package org.nexusdata.core;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.nexusdata.metamodel.EntityDescription;
import org.nexusdata.metamodel.RelationshipDescription;


public abstract class PersistentStore {

    private static final String UUID_KEY    = "_UUID";

    private PersistentStoreCoordinator storeCoordinator;
    private final File location;
    private final Map<String,Object> metadata = new HashMap<String,Object>();

    PersistentStore(File location) {
        this.location = location;
    }

    protected abstract void loadMetadata();

    public PersistentStoreCoordinator getCoordinator() {
        return storeCoordinator;
    }

    public File getLocation() {
        return location;
    }

    public UUID getUuid() {
        return (UUID) metadata.get(UUID_KEY);
    }

    protected void setUuid(UUID uuid) {
        metadata.put(UUID_KEY, uuid);
    }

    void setPersistentStoreCoordinator(PersistentStoreCoordinator coordinator) {
        storeCoordinator = coordinator;
    }

    ObjectID createObjectID(EntityDescription<?> entity, Object referenceObject) {
        ObjectID id = new ObjectID(this, entity, referenceObject);
        return id;
    }

    protected Object getReferenceObjectForObjectID(ObjectID objectID) {
        return objectID.getReferenceObject();
    }

    abstract List<ObjectID> getPermanentIDsForObjects(List<ManagedObject> objects);

    abstract StoreCacheNode getObjectValues(ObjectID objectID, ObjectContext context);

    abstract ObjectID getToOneRelationshipValue(ObjectID objectID, RelationshipDescription relationship, ObjectContext context);

    abstract Collection<ObjectID> getToManyRelationshipValue(ObjectID objectID, RelationshipDescription relationship, ObjectContext context);

    abstract <T extends ManagedObject> List<T> executeFetchRequest(FetchRequest<T> request, ObjectContext context);
    abstract void executeSaveRequest(SaveChangesRequest request, ObjectContext context);

    void contextRegisteredObjectIDs(Collection<ObjectID> objectIDs) {
    }

    void contextUnregisteredObjectIDs(Collection<ObjectID> objectIDs) {
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+" [location=" + location + ", UUID=" + getUuid() + "]";
    }
}
