package org.nexusdata.core;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ObjectContextNotifier {

    public static interface ObjectContextListener {
        public void contextObjectsDidChange(ObjectContext context, ObjectsChangedNotification changedObjects);
        public void contextWillSave(ObjectContext context);
        public void contextDidSave(ObjectContext context, ChangedObjectsSet changedObjects);
    }

    public static abstract class DefaultObjectContextListener implements ObjectContextListener {
        @Override
        public void contextWillSave(ObjectContext context) {
            // do nothing by default
        }

        @Override
        public void contextObjectsDidChange(ObjectContext context, ObjectsChangedNotification objectsChanged) {
            // do nothing by default
        }

        @Override
        public void contextDidSave(ObjectContext context, ChangedObjectsSet changedObjects) {
            // do nothing by default
        }
    }

    //TODO: should contexts and/or listeners be weak references?
    static Set<ObjectContextListener> allContextsListeners = new LinkedHashSet<ObjectContextListener>();
    static Map<ObjectContext, Set<ObjectContextListener>> contextListeners = new HashMap<ObjectContext, Set<ObjectContextListener>>();

    static void notifyListenersOfWillSave(ObjectContext context) {
        for (ObjectContextListener listener : getListeners(context)) {
            listener.contextWillSave(context);
        }
    }

    static void notifyListenersOfDidSave(ObjectContext context, ChangedObjectsSet changedObjects) {
        for (ObjectContextListener listener : getListeners(context)) {
            listener.contextDidSave(context, changedObjects);
        }
    }

    static void notifyListenersOfObjectsDidChange(ObjectContext context, ObjectsChangedNotification changedObjects) {
        for (ObjectContextListener listener : getListeners(context)) {
            listener.contextObjectsDidChange(context, changedObjects);
        }
    }

    static boolean hasListeners(ObjectContext context) {
        Set<ObjectContextListener> listeners = contextListeners.get(context);
        return !allContextsListeners.isEmpty() || (listeners != null && !listeners.isEmpty());
    }

    private static Set<ObjectContextListener> getListeners(ObjectContext context) {
        Set<ObjectContextListener> allListeners = getListenersForContext(context);

        if (!allContextsListeners.isEmpty()) {
            // make a copy so we don't modify original
            allListeners = new LinkedHashSet<ObjectContextListener>(allListeners);
            allListeners.addAll(allContextsListeners);
        }

        return allListeners;
    }

    private static Set<ObjectContextListener> getListenersForContext(ObjectContext context) {
        Set<ObjectContextListener> listeners = contextListeners.get(context);

        if (listeners == null) {
            listeners = new LinkedHashSet<ObjectContextListener>();
            contextListeners.put(context, listeners);
        }

        return listeners;
    }

    public static void registerListener(ObjectContext forContext, ObjectContextListener listener) {
        if (!allContextsListeners.contains(listener)) {
            Set<ObjectContextListener> listeners = getListenersForContext(forContext);
            listeners.add(listener);
        }
    }

    public static void registerListener(ObjectContextListener listener) {
        allContextsListeners.add(listener);
    }

    public static void unregisterListener(ObjectContext forContext, ObjectContextListener listener) {
        Set<ObjectContextListener> listeners = getListenersForContext(forContext);
        listeners.remove(listener);
        if (listeners.isEmpty()) {
            contextListeners.remove(forContext);
        }
    }

    public static void unregisterListener(ObjectContextListener listener) {
        allContextsListeners.remove(listener);
    }
}
