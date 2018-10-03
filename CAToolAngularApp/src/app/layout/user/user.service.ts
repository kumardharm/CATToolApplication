import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
// import 'rxjs/add/observable/merge';
// import 'rxjs/add/operator/map';

@Injectable({
providedIn: 'root'
})
export class UsersService {
   private addUserURL ='http://localhost:8090/user/addUser';
   private addUrl = 'http://localhost:8090/user/addUser';
   private updateUrl = 'http://localhost:8090/user/updateUser';
   private deleteUrl = 'http://localhost:8090/user/deleteUserById';

constructor(private http:HttpClient) { }

CollectData(){
const url = 'http://localhost:8090/user/getAll';

return this.http.get(url);
}

addUser(user : Object):Observable<Object>{
   
    return this.http.post(`${this.addUrl}` + `/create`, user);
}

private comptransfer = new BehaviorSubject("Hello");
        users = this.comptransfer.asObservable();
        
        sendMsgtoOtherComponent(messsage){
        this.comptransfer.next(messsage);
        } 

        sendIpAddresstoOtherComponent(messsage)
        {
          this.comptransfer.next(messsage);
        }

      updateUser(user: Object): Observable<Object> {
        return this.http.put(`${this.updateUrl}`+ `/update`, user);
      }

      deleteUser(userId: number): Observable<any> {
        return this.http.delete(`${this.deleteUrl}/${userId}`, { responseType: 'text' });
      }

      getIpAddress() : Observable<any>{
        // const headers = new HttpHeaders({ 'Content-Type': 'application/json' ,'Origin' : 'http://localhost:3000', "Access-Control-Allow-Origin" : "*" });
       const headers = new HttpHeaders({ "Access-Control-Allow-Origin" : "*" });
        return this.http
             //.get('http://freegeoip.net/json/?callback', { headers: headers });
              // .get('http://freegeoip.net/json/?callback');
                  .get('http://ipinfo.io');
                // .get('https://jsonip.com');
              //  .get('http://freegeoip.net/json/',{ headers: headers });
             // .map(response => response || {})
              //.catch(this.handleError);
    }

    private handleError(error: HttpErrorResponse):
      Observable<any> {
        //Log error in the browser console
        console.error('observable error: ', error);
        return Observable.throw(error);
      }

} 