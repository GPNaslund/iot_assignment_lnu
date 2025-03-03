import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { LedControlComponent } from './components/led-control/led-control.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, LedControlComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})


export class AppComponent {
  title = 'client';
}
