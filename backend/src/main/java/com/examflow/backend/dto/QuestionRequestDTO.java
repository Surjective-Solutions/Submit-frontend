package com.examflow.backend.dto;

import java.util.List;

public class QuestionRequestDTO {

    private Integer key;

    private Integer marks;

    private List<SubQuestionRequestDTO> subparts;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    public List<SubQuestionRequestDTO> getSubparts() {
        return subparts;
    }

    public void setSubparts(List<SubQuestionRequestDTO> subparts) {
        this.subparts = subparts;
    }

}
