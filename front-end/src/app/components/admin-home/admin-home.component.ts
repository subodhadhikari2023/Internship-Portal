import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.css']
})
export class AdminHomeComponent implements OnInit {
  saveInstructor(_t39: number) {
    const updatedInstructor = this.instructors[_t39];
    this.adminService.updateInstructor(updatedInstructor).subscribe({
      next:(res)=>{
        this.instructors[_t39].editMode = false;
        
      },error:(err)=>{
        console.error(err);
        
      }
    })
    
  }


  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.loadAllData();
  }



  cancelEdit(index: number) {
    this.instructors[index] = { ...this.instructors[index].backup, editMode: false };
    delete this.instructors[index].backup;
  }



  editInstructor(index: number) {
    // Enable edit mode and store backup
    this.instructors[index].editMode = true;
    this.instructors[index].backup = { ...this.instructors[index] };
  }

  departments: string[] = [];
  users: any[] = [];
  instructors: any[] = [];
  students: any[] = [];

  newDepartment = '';
  newInstructor = {
    userEmail: '',
    userName: '',
    phoneNumber: '',
    department: '',
  };



  // Load all data from API
  loadAllData(): void {
    this.adminService.getAllDepartments().subscribe((res) => {
      this.departments = res || [];
    });

    this.adminService.getAllUsers().subscribe((res) => {
      this.users = res || [];
    });

    this.adminService.getAllInstructors().subscribe((res) => {
      this.instructors = res || [];
    });

    this.adminService.getAllStudents().subscribe((res) => {
      this.students = res || [];
    });
  }

  // Add new department
  addDepartment(): void {
    if (!this.newDepartment.trim()) return; // Prevent empty input
    this.adminService.createNewDepartment(this.newDepartment).subscribe(() => {
      this.newDepartment = '';  // Clear input field
      this.loadAllData();
    });
  }

  // Add new instructor
  addInstructor(form: NgForm): void {
    if (!this.validateInstructorForm()) return;
  
    this.adminService.createInstructor(this.newInstructor).subscribe(() => {
      this.newInstructor = {
        userEmail: '',
        userName: '',
        phoneNumber: '',
        department: '',
      };
      form.resetForm(); // This resets both values AND validation states!
      this.loadAllData();
    });
  }
  

  // Validate instructor form before submission
  validateInstructorForm(): boolean {
    const { userEmail, userName, phoneNumber, department } = this.newInstructor;

    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    const phoneRegex = /^[0-9]{10}$/;

    if (!userEmail || !emailRegex.test(userEmail)) {
      alert('Please enter a valid email address.');
      return false;
    }

    if (!userName.trim()) {
      alert('Please enter a valid name.');
      return false;
    }

    if (!phoneNumber || !phoneRegex.test(phoneNumber)) {
      alert('Phone number must be exactly 10 digits.');
      return false;
    }

    if (!department) {
      alert('Please select a department.');
      return false;
    }

    return true;
  }
  
}
