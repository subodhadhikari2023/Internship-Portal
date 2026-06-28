import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-view-student-details-for-instructor',
  templateUrl: './view-student-details-for-instructor.component.html',
  styleUrls: ['./view-student-details-for-instructor.component.css']
})
export class ViewStudentDetailsForInstructorComponent implements OnInit {
  studentId: number | null = null;
  student: any = {};

  constructor(private userService: UserService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.studentId = Number(this.route.snapshot.paramMap.get('id'));
    this.fetchStudentDetails();


  }

  fetchStudentDetails() {
    this.userService.fetchStudentDetailsForInstructor(this.studentId).subscribe({
      next: (res) => {
        this.student = res;
        this.loadProfilePicture(this.student.profilePictureFilePath)
      }, error: (err) => {
        console.log(err);

      }
    });
  }
  downloadResume() {
    this.userService.downloadResume(this.student.resumeFilePath).subscribe({
      next: (blob) => {
        const objectURL = URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = objectURL;
        a.download = "resume.pdf";
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(objectURL);
      },
      error: (error) => console.error("File download error:", error)
    });
  }
  previewImage: string | ArrayBuffer | null = null;

  loadProfilePicture(filePath: string) {
    this.userService.loadProfilePicture(filePath).subscribe((safeUrl) => {
      this.student.profilePictureFilePath = safeUrl;
    });
  }
}
