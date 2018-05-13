import { Component } from '@angular/core';

import { Observable } from 'rxjs';
import { Http, Response } from '@angular/http';
import { FecService } from './app.service';
import { FecFile } from './my-upload-item';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']

})
export class AppComponent {

  title = 'fec app';

  fecfiles: FecFile[];
  fecfile: FecFile;
  errorMessage: String;
  appResp: String;

  constructor(private fecService: FecService) { }

  submit(): void {
     let uploadFile: File = (<HTMLInputElement>window.document.getElementById('myFileInputField')).files[0];
     this.fecfile = new FecFile(uploadFile);
     this.fecService.addBookWithObservable(this.fecfile)
         .subscribe( fecfile => {
                            this.appResp = 'rien';
                        },
                         error => this.errorMessage = <any>error);
   }

   testGet() {
      this.fecService.getStr().subscribe(
                                  (ress: Response) => {
                                   this.appResp = ress.toString();
                                   },
                                 error => console.log(error)
                                 );
   }

}