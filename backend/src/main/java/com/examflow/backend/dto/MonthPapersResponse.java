package com.examflow.backend.dto;

import java.util.List;

public class MonthPapersResponse {

    private Integer month;

    private Integer year;

    private String month_label;

    private List<PaperResponse> papers;

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getMonth_label() {
        return month_label;
    }

    public void setMonth_label(String month_label) {
        this.month_label = month_label;
    }

    public List<PaperResponse> getPapers() {
        return papers;
    }

    public void setPapers(List<PaperResponse> papers) {
        this.papers = papers;
    }

}
