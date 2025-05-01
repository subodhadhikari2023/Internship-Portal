import { Component, OnInit } from '@angular/core';
import { ProjectService } from 'src/app/services/project.service';

@Component({
  selector: 'app-view-projects',
  templateUrl: './view-projects.component.html',
  styleUrls: ['./view-projects.component.css']
})
export class ViewProjectsComponent implements OnInit {

  searchText: string = '';
  allProjects: any[] = [];        // 👈 Empty initially
  filteredProjects: any[] = [];   // 👈 Empty initially

  constructor(private projectService: ProjectService) { }

  ngOnInit(): void {
    this.projectService.getAllProjects().subscribe({
      next: (res) => {
        this.allProjects = res;
        this.filteredProjects = [...this.allProjects];  
        console.log(res);
        
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  filterProjects(): void {
    const text = this.searchText.trim().toLowerCase();
    this.filteredProjects = this.allProjects.filter(project =>
      project.projectName.toLowerCase().includes(text) ||   
      project.description.toLowerCase().includes(text) ||
      project.department.toLowerCase().includes(text) ||
      project.studentName.toLowerCase().includes(text)
    );
  }
  downloadFile(filePath: string, fileName: string): void {
    this.projectService.downloadFile(filePath).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = fileName;    // you set a meaningful name here
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Download error:', err);
      }
    });
  }
  
}
