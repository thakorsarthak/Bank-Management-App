import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-admin-component',
  imports: [RouterLink],
  templateUrl: './admin-component.component.html',
  styleUrl: './admin-component.component.css'
})
export class AdminComponentComponent implements OnInit {
  ngOnInit(): void {
    console.log('admin component loaded');
    
    throw new Error('Method not implemented.');
  }

  

}
