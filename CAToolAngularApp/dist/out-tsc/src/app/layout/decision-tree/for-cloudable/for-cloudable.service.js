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
var http_1 = require("@angular/common/http");
var localStorage_service_1 = require("../../utility/service/localStorage.service");
var ForCloudableService = /** @class */ (function () {
    function ForCloudableService(http, myStorage) {
        this.http = http;
        this.myStorage = myStorage;
        this.collectRulesUrl = "http://localhost:8090/cloudableRule/getAll";
        this.CollectCloudableQuestionUrl = "http://localhost:8090/assessmentQuestions/getCloudableQuestion";
        this.baseUrl = 'http://localhost:8090/cloudableRule/save';
    }
    ForCloudableService.prototype.addClodableRule = function (cloudablerule) {
        return this.http.post(this.myStorage.getdomainURL() + "/cloudableRule/save/create", cloudablerule);
    };
    ForCloudableService.prototype.collectRule = function (clientId) {
        return this.http.get(this.myStorage.getdomainURL() + "/cloudableRule/getAll/" + clientId);
    };
    ForCloudableService.prototype.collectOptions = function () {
        return this.http.get(this.myStorage.getdomainURL() + "/option/getAll");
    };
    ForCloudableService.prototype.collectQuestion = function (clientId) {
        return this.http.get(this.myStorage.getdomainURL() + "/assessmentQuestions/getCloudableQuestion/" + clientId);
    };
    ForCloudableService = __decorate([
        core_1.Injectable({
            providedIn: 'root'
        }),
        __metadata("design:paramtypes", [http_1.HttpClient, localStorage_service_1.LocalStorageService])
    ], ForCloudableService);
    return ForCloudableService;
}());
exports.ForCloudableService = ForCloudableService;
//# sourceMappingURL=for-cloudable.service.js.map