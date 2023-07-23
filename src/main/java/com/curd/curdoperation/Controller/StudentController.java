package com.curd.curdoperation.Controller;

import com.curd.curdoperation.Dao.StudentRepository;
import com.curd.curdoperation.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/students/")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("showForm")
    public String showStudentForm(Student student) {
        return "add-student";
    }

    @GetMapping("list")
    public String students(Model model) {
        model.addAttribute("students", this.studentRepository.findAll());
        return "index";
    }

    @PostMapping("add")
    public String addStudent(@Valid Student student, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "add-student";
        }

        this.studentRepository.save(student);
        return "redirect:list";
    }


    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable ("id") long id, Model model) {
        Student student = this.studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student id : " + id));

        model.addAttribute("student", student);
        return "update-student";
    }

    @PostMapping("update/{id}")
    public String updateStudent(@PathVariable("id") long id, @Valid Student student, BindingResult result, Model model) {
        if(result.hasErrors()) {
            student.setId(id);
            return "update-student";
        }

        // update student
        studentRepository.save(student);

        // get all students ( with update)
        model.addAttribute("students", this.studentRepository.findAll());
        return "index";
    }

    @GetMapping("delete/{id}")
    public String deleteStudent(@PathVariable ("id") long id, Model model) {

        Student student = this.studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student id : " + id));

        this.studentRepository.delete(student);
        model.addAttribute("students", this.studentRepository.findAll());
        return "index";

    }


    @GetMapping("showlogin")
    public String showLoginForm() {
        return "login";
    }


    @PostMapping("login")
    public String processLogin(Student student) {
        // Retrieve user from the database based on the provided email
        Student user = studentRepository.findByEmail(student.getEmail());

        if (user != null && user.getPassword().equals(student.getPassword())) {
            // Authentication successful, you can add further logic or redirect to the dashboard
            return "index";
        } else {
            // Authentication failed, you can display an error message or redirect back to the login form
            return "login";
        }
    }
}
