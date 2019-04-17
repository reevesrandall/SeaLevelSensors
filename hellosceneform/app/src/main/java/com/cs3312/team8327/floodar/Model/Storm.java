package com.cs3312.team8327.floodar.Model;

/**
 * Model for storms/hurricanes
 */
public class Storm {
    private String name;
    private float level;
    private String description;
    private int category;
    private String subtext;
    private String link;

    /**
     * public constructor for making a new storm
     * @param name name of the storm
     * @param level water level in meters for the storm
     * @param description A description of the storm event
     */
    public Storm(String name, float level, String description, int category, String subtext) {
        this.name = name;
        this.level = level;
        this.description = description;
        this.category = category;
        this.subtext = subtext;
    }

    public float getLevel() {
        return level;
    }

    public void setLevel(float level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory() {
        return category;
    }

    public String getSubtext() {
        return subtext;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
