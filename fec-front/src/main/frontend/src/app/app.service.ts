import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { FecFile } from './my-upload-item';

@Injectable()
export class FecService {

    constructor(private http: Http) { }

    addBookWithObservable(fecfile: FecFile): Observable<FecFile> {
        //const url = 'http://172.23.140.63:8080/api/file.ats';
        const url = 'http://localhost:8888/api/file.ats';
        let formData: FormData = new FormData();
        formData.append('uploadFile', fecfile.file);
        let headers = new Headers();
        //headers.append('Content-Type', 'false');
        //headers.append('boundary', 'gc0p4Jq0M2Yt08jU534c0p');
        //headers.append('Accept', '*/*');
        let options = new RequestOptions({ headers: headers });
        return this.http.post(url, formData, options)
            .map(this.extractData)
            .catch(this.handleErrorObservable);
    }

    getStr() {
        //const url = 'http://172.23.140.63:8080/api/test.ats';
        const url = 'http://localhost:8888/api/test.ats';
        return this.http.get(url).map((res: Response) => res.json());
    }

    private extractData(res: Response) {
        let body = res.json();
        alert(body);
        return body.data || {};
    }

    private handleErrorObservable(error: Response | any) {
        console.error('tez ' + error.message || 'tezz ' + error);
        return Observable.throw(error.message || error);
    }

} 
