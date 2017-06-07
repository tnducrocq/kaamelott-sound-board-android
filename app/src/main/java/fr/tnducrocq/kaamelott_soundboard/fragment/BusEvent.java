package fr.tnducrocq.kaamelott_soundboard.fragment;

import java.util.List;

import fr.tnducrocq.kaamelott_soundboard.model.Sound;

/**
 * Created by tony on 06/06/2017.
 */

public class BusEvent {

    public static abstract class Event {
    }

    public static class JSONParsed extends Event {
    }

    public static class Progress extends Event {

        public String fileName;
        public int current;
        public int total;

        public Progress(String fileName, int current, int total) {
            this.fileName = fileName;
            this.current = current;
            this.total = total;
        }
    }

    public static class Finish extends Event {

        public final List<Sound> soundList;

        public Finish(final List<Sound> soundList) {
            this.soundList = soundList;
        }
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
