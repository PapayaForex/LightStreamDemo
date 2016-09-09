package com.allen.lightstreamdemo.bean;

import java.util.List;

/**
 * Created by: allen on 16/9/1.
 */

public class WatchList {
    public List<Watch> watchlists;

    private class Watch {
        /**
         * name : Popular Markets
         * id : Popular Markets
         * editable : false
         * deleteable : false
         * modifiedDate : null
         * defaultSystemWatchlist : true
         */

        private String name;
        private String id;
        private boolean editable;
        private boolean deleteable;
        private String modifiedDate;
        private boolean defaultSystemWatchlist;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isEditable() {
            return editable;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }

        public boolean isDeleteable() {
            return deleteable;
        }

        public void setDeleteable(boolean deleteable) {
            this.deleteable = deleteable;
        }

        public Object getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public boolean isDefaultSystemWatchlist() {
            return defaultSystemWatchlist;
        }

        public void setDefaultSystemWatchlist(boolean defaultSystemWatchlist) {
            this.defaultSystemWatchlist = defaultSystemWatchlist;
        }
    }

}
