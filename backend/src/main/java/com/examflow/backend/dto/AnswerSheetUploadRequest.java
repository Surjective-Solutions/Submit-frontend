package com.examflow.backend.dto;

public class AnswerSheetUploadRequest {
 
     private Integer classId;

     private Integer paperId;

	 public Integer getClassId() {
		 return classId;
	 }

	 public void setClassId(Integer classId) {
		 this.classId = classId;
	 }

	 public Integer getPaperId() {
		 return paperId;
	 }

	 public void setPaperId(Integer paperId) {
		 this.paperId = paperId;
	 }

     
}
