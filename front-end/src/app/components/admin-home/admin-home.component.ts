import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.css']
})
export class AdminHomeComponent implements OnInit {

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

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.loadAllData();
  }

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
  addInstructor(): void {
    if (!this.validateInstructorForm()) return; // Prevent invalid data submission

    this.adminService.createInstructor(this.newInstructor).subscribe(() => {
      this.newInstructor = { userEmail: '', userName: '', phoneNumber: '', department: '' }; // Reset form
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
