package fr.tnducrocq.kaamelott_soundboard.fragment;

/**
 * Created by tony on 29/09/2017.
 */

public class BusEvent {

    public static abstract class Event {
    }

    public static class QueryEvent extends Event {

        String query;

        public QueryEvent(String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }

    }
}