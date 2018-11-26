import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { LocalStorageService } from '../utility/service/localStorage.service';
// import 'rxjs/add/observable/merge';
// import 'rxjs/add/operator/map';

@Injectable({
providedIn: 'root'
})
export class UsersService {
  ipAddress : string;
  
   private addUserURL ='http://localhost:8090/user/addUser';
   private addUrl = 'http://localhost:8090/user/addUser';
   private updateUrl = 'http://localhost:8090/user/updateUser';
   private deleteUrl = 'http://localhost:8090/user/deleteUserById';
   private changePasswordUrl = 'http://localhost:8090/user/changePassword';
   private deactivateUrl = 'http://localhost:8090/user/deactivateUser';
constructor(private http:HttpClient,private myStorage:LocalStorageService) { }

CollectData(clientName : string): Observable<Object>{
return this.http.get(this.myStorage.getLocalhostURL()+`/user/getAll/`+clientName);
}

getAllUsers(clientName : string): Observable<Object>{
  console.log(this.myStorage.getLocalhostURL()+`/user/getAll/`+this.myStorage.getClient());
  return this.http.get(this.myStorage.getLocalhostURL()+`/user/getAll/`+clientName);
  }

countNumberOfUsers()
{
  const getCount='http://localhost:8090/user/getUserCount';
  return this.http.get(getCount);
}

newAddURL: string = 'http://localhost:8090/user/addUser';
  
addUser(user: Object): Observable<Object> {
  console.log(this.myStorage.getCurrentUser());
  return this.http.post(`${this.newAddURL}` + `/create/`+this.myStorage.getCurrentUser(), user);
}

deactivate(userId: number)
{
  console.log('************deactivate ********');
  return this.http.put(`${this.deactivateUrl}/${userId}`,  { responseType: 'text' });
}

changePassword(userName: String,password: String,newPassword: String){
  console.log(`${this.changePasswordUrl}`+ `/`+userName+`/`+password+`/`+newPassword);
  return this.http.get(`${this.changePasswordUrl}`+ `/`+userName+`/`+password+`/`+newPassword);
}

private comptransfer = new BehaviorSubject("Hello");
        users = this.comptransfer.asObservable();

        sendUsertoOtherComponent(messsage){
         this.comptransfer.next(messsage);
        }
        
        sendMsgtoOtherComponent(messsage){
        this.comptransfer.next(messsage);
        } 

        sendIpAddresstoOtherComponent(messsage)
        {
          this.comptransfer.next(messsage);
        }

      updateUser(user: Object): Observable<Object> {
        return this.http.put(`${this.updateUrl}`+ `/update/`+this.myStorage.getCurrentUser(), user);
      }

      deleteUser(userId: number): Observable<any> {
        return this.http.delete(`${this.deleteUrl}/${userId}`, { responseType: 'text' });
      }

      getIpAddress() : Observable<any>{
        // const headers = new HttpHeaders({ 'Content-Type': 'application/json' ,'Origin' : 'http://localhost:3000', "Access-Control-Allow-Origin" : "*" });
       const headers = new HttpHeaders({ "Access-Control-Allow-Origin" : "*" });
        return this.http.get('http://ipinfo.io');
    }

    private handleError(error: HttpErrorResponse):
      Observable<any> {
        //Log error in the browser console
        console.error('observable error: ', error);
        return Observable.throw(error);
      }

} 