package com.examflow.backend.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.examflow.backend.dto.CashierResponse;
import com.examflow.backend.dto.InstructorResponse;
import com.examflow.backend.entity.Cashier;

import jakarta.servlet.http.HttpServletRequest;

import com.examflow.backend.entity.Instructor;
import com.examflow.backend.repository.InstructorRepository;
import com.examflow.backend.service.InstructorControllerManager;
import com.examflow.backend.dto.GeneralResponse;
import com.examflow.backend.dto.InstructorSignUpRequest;
import com.examflow.backend.dto.InstructorTeacherResponse;

@Service
public class InstructorControllerManagerImpl implements InstructorControllerManager {

    private final BCryptPasswordEncoder passwordEncoder;
    private HttpServletRequest request;
    private final InstructorRepository instructorRepository;

    @Autowired
    public InstructorControllerManagerImpl(InstructorRepository instructorRepository,
            BCryptPasswordEncoder passwordEncoder,
            HttpServletRequest request) {
        this.instructorRepository = instructorRepository;
        this.passwordEncoder = passwordEncoder;
        this.request = request;
    }

    @Override
    public List<InstructorResponse> getAllInstructors() {

        List<Instructor> instructors = instructorRepository.findByStatusNot(0);

        List<InstructorResponse> instructorResponseList = new ArrayList<>();

        for (Instructor instructor : instructors) {

            InstructorResponse instructorResponse = new InstructorResponse();
            String fullName = instructor.getFullName();

            if (fullName != null && !fullName.isBlank()) {
                int lastSpaceIndex = fullName.lastIndexOf(" ");

                if (lastSpaceIndex > 0) {
                    instructorResponse.setFirst_name(
                            fullName.substring(0, lastSpaceIndex));

                    instructorResponse.setLast_name(
                            fullName.substring(lastSpaceIndex + 1));
                } else {
                    // Only one name exists
                    instructorResponse.setFirst_name(fullName);
                    instructorResponse.setLast_name("");
                }
            }

            instructorResponse.setId(instructor.getInstructorSeq());
            instructorResponse.setEmployee_id(instructor.getInstrutorNo());
            instructorResponse.setEmail(instructor.getEmail());
            instructorResponse.setContact_number(instructor.getContactNumber());
            instructorResponse.setSubject_area(instructor.getSubjectArea());
            instructorResponse.setStatusSeq(instructor.getStatus());
            if (instructor.getStatus() == 2) {
                instructorResponse.setStatus("ACTIVE");
            } else if (instructor.getStatus() == 0) {
                instructorResponse.setStatus("DELETED");

            } else {
                instructorResponse.setStatus("INACTIVE");
            }
            instructorResponse.setProfile_photo_url(null);

            instructorResponseList.add(instructorResponse);
        }

        return instructorResponseList;
    }

    @Override
    public InstructorResponse getInstructorById(Integer id) {
        Instructor instructor = instructorRepository.findByInstructorSeq(id);
        if (instructor == null) return null;

        InstructorResponse instructorResponse = new InstructorResponse();
        instructorResponse.setId(instructor.getInstructorSeq());
        instructorResponse.setEmployee_id(null);
        instructorResponse.setFirst_name(instructor.getFullName());
        instructorResponse.setLast_name(null);
        instructorResponse.setEmail(instructor.getEmail());
        instructorResponse.setContact_number(instructor.getContactNumber());
        instructorResponse.setSubject_area(null);
        instructorResponse.setStatusSeq(instructor.getStatus());
        instructorResponse.setProfile_photo_url(null);

        return instructorResponse;
    }

    @Override
    public GeneralResponse updateInstructor(Integer id, InstructorSignUpRequest instructorRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        GeneralResponse response = new GeneralResponse();
        Instructor instructor = instructorRepository.findByInstructorSeq(id);
        if (instructor == null) {
            response.setIsSuccess(false);
            response.setMessage("Instructor not found");
            return response;
        }
        instructor.setFullName(instructorRequest.getFirstName() + ' ' + instructorRequest.getLastName());
        instructor.setEmail(instructorRequest.getEmail());
        instructor.setInstrutorNo(instructorRequest.getEmployeeId());
        instructor.setContactNumber(instructorRequest.getContactNumber());
        instructor.setLastModifiedBy(username);
        instructor.setLastModifiedDateTime(LocalDateTime.now());
        if (instructorRequest.getStatus() == "ACTIVE") {
            instructor.setStatus(2);
        } else {
            instructor.setStatus(1);
        }
        instructor.setAddress(instructorRequest.getAddress());
        instructor.setSubjectArea(instructorRequest.getSubjectArea());

        if (instructorRequest.getPassword() != null) {
            if (instructorRequest.getPassword().equals(instructorRequest.getConfirmPassword())) {
                instructor.setPassword(passwordEncoder.encode(instructorRequest.getPassword()));
                instructor.setConfirmPassword(passwordEncoder.encode(instructorRequest.getPassword()));
                instructor.setFinalPassword(passwordEncoder.encode(instructorRequest.getPassword()));

            } else {
                response.setIsSuccess(false);
                response.setMessage("Confirmed Password and Password Not equal ");
                return response;
            }
        }

        instructorRepository.save(instructor);
        response.setIsSuccess(true);
        response.setMessage("Instructor updated successfully");
        return response;

    }

    @Override
    public List<InstructorTeacherResponse> getIntructorTeachers() {

        return null;

    }

}
