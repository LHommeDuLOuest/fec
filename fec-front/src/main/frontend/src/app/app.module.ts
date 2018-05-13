import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

import { FormsModule } from '@angular/forms';
import { HttpModule, Http, Response } from '@angular/http';
import { FecService } from './app.service';

@NgModule({
  declarations: [AppComponent],
  imports: [BrowserModule,FormsModule,HttpModule,NgbModule.forRoot()],
    
  providers: [FecService],
  bootstrap: [AppComponent]
})
export class AppModule { }
