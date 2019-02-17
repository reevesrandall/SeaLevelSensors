package com.cs3312.team8327.floodar.Model;

public class Storm {
    private String name;
    private float level;
    private String description;

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

    public Storm(String name, float level, String description) {
        this.name = name;
        this.level = level;
        this.description = description;
    }

    public Storm(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
