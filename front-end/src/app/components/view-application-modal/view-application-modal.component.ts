import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-view-application-modal',
  templateUrl: './view-application-modal.component.html',
  styleUrls: ['./view-application-modal.component.css']
})
export class ViewApplicationModalComponent {

  constructor(
    public dialogRef: MatDialogRef<ViewApplicationModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  close(): void {
    this.dialogRef.close();
  }

}
