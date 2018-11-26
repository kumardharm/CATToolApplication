"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var router_1 = require("@angular/router");
var core_2 = require("@ngx-translate/core");
var localStorage_service_1 = require("../../utility/service/localStorage.service");
var Users_1 = require("../../catlogin/Users");
var HeaderComponent = /** @class */ (function () {
    function HeaderComponent(router, myStorage, translate) {
        this.router = router;
        this.myStorage = myStorage;
        this.translate = translate;
        this.user = new Users_1.Users();
    }
    HeaderComponent.prototype.ngOnInit = function () {
        this.clientNameValue = this.myStorage.getCurrentUserObject().clientName;
        this.user = JSON.parse(localStorage.getItem('user'));
        this.userName = this.user.userName;
    };
    HeaderComponent.prototype.changeLang = function (language) {
        this.translate.use(language);
        localStorage.setItem('language', language);
    };
    HeaderComponent.prototype.onLoggedout = function () {
        this.myStorage.clearCurrentUser();
        this.myStorage.clearLoggedIn();
    };
    HeaderComponent = __decorate([
        core_1.Component({
            selector: 'app-header',
            templateUrl: './header.component.html',
            styleUrls: ['./header.component.scss']
        }),
        __metadata("design:paramtypes", [router_1.Router, localStorage_service_1.LocalStorageService, core_2.TranslateService])
    ], HeaderComponent);
    return HeaderComponent;
}());
exports.HeaderComponent = HeaderComponent;
//# sourceMappingURL=header.component.js.map