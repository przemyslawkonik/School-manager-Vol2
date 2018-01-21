package pl.schoolmanager.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.schoolmanager.entity.Mark;
import pl.schoolmanager.entity.Student;
import pl.schoolmanager.entity.Subject;
import pl.schoolmanager.repository.MarkRepository;
import pl.schoolmanager.repository.StudentRepository;
import pl.schoolmanager.repository.SubjectRepository;

@Controller
@RequestMapping("/mark")
public class MarkController {

	@Autowired
	private MarkRepository markRepository;
	
	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private SubjectRepository subjectRepository;
	
	
	//CREATE FOR
	@GetMapping("/create")
	public String createMark(@RequestParam long subject, @RequestParam long student, Model m) {
		Subject thisSubject = this.subjectRepository.findOne(subject);
		Student thisStudent = this.studentRepository.findOne(student);
		Mark mark = new Mark();
		mark.setSubject(thisSubject);
		mark.setStudent(thisStudent);
		m.addAttribute("mark", mark);
		return "mark/new_mark"; //view to be developed
	}
	
	@PostMapping("/create")
	public String createMarkPost(@Valid @ModelAttribute Mark mark, BindingResult bindingResult, Model m) {
		if (bindingResult.hasErrors()) {
			return "mark/new_mark"; //view to be developed
		}
		this.markRepository.save(mark);
		Long divisionId = mark.getStudent().getDivision().getId();
		Long subjectId = mark.getSubject().getId();
		return "redirect:/division/inside/marks/"+divisionId+"/"+subjectId;
	}
	
	//READ
	@GetMapping("/view/{markId}")
	public String viewMark(Model m, @PathVariable long markId) {
		Mark mark = this.markRepository.findOne(markId);
		m.addAttribute("mark", mark);
		return "mark/show_mark"; //view to be developed
	}
	
	//UPDATE
	@GetMapping("/update/{markId}")	
	public String updateMark(@RequestParam long subject, @RequestParam long student, Model m, @PathVariable long markId) {
		Mark mark = this.markRepository.findOne(markId);
		m.addAttribute("mark", mark);
		return "mark/edit_mark"; //view to be developed
	}
	
	@PostMapping("/update/{markId}")
	public String updateMarkPost(@Valid @ModelAttribute Mark mark, BindingResult bindingResult, @PathVariable long markId) {
		if (bindingResult.hasErrors()) {
			return "mark/edit_mark"; //view to be developed
		}
		mark.setId(markId);
		
		
		this.markRepository.save(mark);
		Long divisionId = mark.getStudent().getDivision().getId();
		Long subjectId = mark.getSubject().getId();
		return "redirect:/division/inside/marks/"+divisionId+"/"+subjectId; //to decide where to return
	}
	
	//DELETE
	@DeleteMapping("/delete/{markId}")
	public String deleteMark(@PathVariable long markId) {
		this.markRepository.delete(markId);
		return "index"; //to decide where to return
	}
	
	//SHOW ALL
	@ModelAttribute("availableMarks")
	public List<Mark> getMarks() {
		return this.markRepository.findAll();
	}
}