package com.examflow.backend.dto;

import java.util.List;

public class InstructorTeacherResponse {

    private String id;

    private String teacher_name;

    private String subject_area;

    private String employee_id;

    private String bio;

    private List<InstructorTeacherClassesResponse> classes;

    private List<PaperInstructorTutorResponse> papers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getSubject_area() {
        return subject_area;
    }

    public void setSubject_area(String subject_area) {
        this.subject_area = subject_area;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<InstructorTeacherClassesResponse> getClasses() {
        return classes;
    }

    public void setClasses(List<InstructorTeacherClassesResponse> classes) {
        this.classes = classes;
    }

    public List<PaperInstructorTutorResponse> getPapers() {
        return papers;
    }

    public void setPapers(List<PaperInstructorTutorResponse> papers) {
        this.papers = papers;
    }


    

    

}
