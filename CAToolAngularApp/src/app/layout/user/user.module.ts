import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DataTablesModule } from 'angular-datatables';
import { FormsModule } from '@angular/forms';
import { UserRoutingModule } from './user-routing.module';
import { UserComponent } from './user.component';
import { AddUserComponent } from './add-user/add-user.component';
import { UpdateUserComponent } from './update-user/update-user.component';


@NgModule({
    imports: [CommonModule, UserRoutingModule,DataTablesModule,FormsModule],
    declarations: [UserComponent, AddUserComponent, UpdateUserComponent]
})

export class UserModule {}
