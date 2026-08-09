package com.examflow.backend.dto;

public class QuestionGradeResponse {
    

    private String question_id;

    private Integer questoinseq;

    private Integer marks_awarded;

    private Integer max_marks;

    private String comment;

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public Integer getQuestoinseq() {
        return questoinseq;
    }

    public void setQuestoinseq(Integer questoinseq) {
        this.questoinseq = questoinseq;
    }

    public Integer getMarks_awarded() {
        return marks_awarded;
    }

    public void setMarks_awarded(Integer marks_awarded) {
        this.marks_awarded = marks_awarded;
    }

    public Integer getMax_marks() {
        return max_marks;
    }

    public void setMax_marks(Integer max_marks) {
        this.max_marks = max_marks;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    

}
