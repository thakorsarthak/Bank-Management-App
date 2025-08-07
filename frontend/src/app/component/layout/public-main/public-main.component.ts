import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-public-main',
  imports: [CommonModule, CardModule, ButtonModule,RouterLink ],
  templateUrl: './public-main.component.html',
  styleUrl: './public-main.component.css'
})
export class PublicMainComponent {

  features = [
    {
      icon: 'pi pi-shield',
      title: 'Secure Banking',
      description: 'State-of-the-art security protocols to keep your finances safe'
    },
    {
      icon: 'pi pi-credit-card',
      title: 'Smart Cards',
      description: 'Contactless cards with real-time notifications and controls'
    },
    {
      icon: 'pi pi-wallet',
      title: 'Savings Goals',
      description: 'Set personalized savings goals and track your progress'
    },
    {
      icon: 'pi pi-chart-line',
      title: 'Investment Options',
      description: 'Diverse investment opportunities to grow your wealth'
    }
  ];
   testimonials = [
    {
      quote: "SecureBank has transformed the way I manage my finances. Their mobile app is intuitive and their customer service is exceptional.",
      author: "Sarah Johnson",
      role: "Small Business Owner"
    },
    {
      quote: "I've been with many banks over the years, but none compare to the personalized service and innovative features that SecureBank offers.",
      author: "Michael Chen",
      role: "Software Engineer"
    },
    {
      quote: "The savings goals feature helped me save for my dream vacation. I recommend SecureBank to all my friends and family.",
      author: "Emily Rodriguez",
      role: "Healthcare Professional"
    }
  ];
}
