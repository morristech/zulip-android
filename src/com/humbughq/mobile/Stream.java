package com.humbughq.mobile;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "streams")
public class Stream {
    public static final int DEFAULT_COLOR = Color.GRAY;

    public static final String NAME_FIELD = "name";
    public static final String MESSAGES_FIELD = "messages";
    public static final String COLOR_FIELD = "color";
    public static final String INHOMEVIEW_FIELD = "inHomeView";
    public static final String INVITEONLY_FIELD = "inviteOnly";

    @DatabaseField(columnName = NAME_FIELD, id = true)
    private String name;
    @ForeignCollectionField(columnName = MESSAGES_FIELD)
    private ForeignCollection<Message> messages;
    @DatabaseField(columnName = COLOR_FIELD)
    private int color;
    @DatabaseField(columnName = INHOMEVIEW_FIELD)
    private Boolean inHomeView;
    @DatabaseField(columnName = INVITEONLY_FIELD)
    private Boolean inviteOnly;

    /**
     * Construct a new Stream from JSON returned by the server.
     * 
     * @param message
     *            The JSON object parsed from the server's output
     * @throws JSONException
     *             Thrown if the JSON provided is malformed.
     */
    public Stream(JSONObject message) throws JSONException {
        name = message.getString("name");
        color = parseColor(message.getString("color"));
        inHomeView = message.getBoolean("in_home_view");
        inviteOnly = message.getBoolean("invite_only");
    }

    /**
     * Construct an empty Stream object.
     */
    public Stream() {

    }

    /**
     * Construct a new Stream object when all that's known is the name.
     * 
     * These should be sensible defaults.
     * 
     * @param name
     *            The stream name
     */
    public Stream(String name) {
        this.name = name;
        color = DEFAULT_COLOR;
        inHomeView = true; // Sure, why not
        inviteOnly = false; // Most probably
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public Boolean getInHomeView() {
        return inHomeView;
    }

    public Boolean getInviteOnly() {
        return inviteOnly;
    }

    public static int parseColor(String color) {
        // Color.parseColor does not handle colors of the form #f00.
        // Pre-process them into normal 6-char hex form.
        if (color.length() == 4) {
            char r = color.charAt(1);
            char g = color.charAt(2);
            char b = color.charAt(3);
            color = "#" + r + r + g + g + b + b;
        }
        return Color.parseColor(color);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(name).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Stream rhs = (Stream) obj;
        return new EqualsBuilder().append(this.name, rhs.name).isEquals();
    }
}
