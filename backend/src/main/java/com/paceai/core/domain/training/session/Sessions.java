package com.paceai.core.domain.training.session;

import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

public final class Sessions implements Iterable<Session> {

    private final List<Session> values;

    private Sessions(List<Session> values) {
        this.values = Collections.unmodifiableList(new ArrayList<>(values));
    }

    public static Sessions empty() {
        return new Sessions(Collections.emptyList());
    }

    public static Sessions of(List<Session> sessions) {
        return new Sessions(sessions);
    }

    public List<Session> asList() {
        return values;
    }
    
    public int count() {
        return values.size();
    }

    @Override
    public Iterator<Session> iterator() {
        return values.iterator();
    }
}