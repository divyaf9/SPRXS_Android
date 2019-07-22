package com.divya.sprxs.model;

public class Skills {

    private int id;
    private String skillId;
    private String skillName;
    private int skillLevel;

    public Skills(int id, String skillId, String skillName, int skillLevel) {
        this.id = id;
        this.skillId = skillId;
        this.skillName = skillName;
        this.skillLevel = skillLevel;
    }

    public int getId() {
        return id;
    }

    public String getSkillId() {
        return skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public int getSkillLevel() {
        return skillLevel;
    }
}
