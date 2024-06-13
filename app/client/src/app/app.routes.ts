import { Routes } from "@angular/router";
import { DataComponent } from "./pages/data/data.component";
import { DashboardComponent } from "./pages/dashboard/dashboard.component";

const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'data', component: DataComponent }
];

export { routes };



