package com.divya.sprxs.model;

public class IdeaFilesResponse {
    private int id;
    private String filename;
    private String ideaId;
    private String fileExtension;

    public IdeaFilesResponse(int id, String filename, String ideaId, String fileExtension) {
        this.id = id;
        this.filename = filename;
        this.ideaId = ideaId;
        this.fileExtension = fileExtension;
    }

    public int getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getIdeaId() {
        return ideaId;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}
