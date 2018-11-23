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
var ForCloudableService = /** @class */ (function () {
    function ForCloudableService(http) {
        this.http = http;
        this.baseUrl = 'http://localhost:8090/cloudableRule/save';
    }
    ForCloudableService.prototype.CollectData = function () {
        //const url = 'http://localhost:8090/option/getAll';
        this.clientNameValue = localStorage.getItem('clientName');
        var url = 'http://localhost:8090/assessmentQuestions/getAllCloudableQuestions';
        return this.http.get(url + "/" + this.clientNameValue);
    };
    ForCloudableService.prototype.addClodableRule = function (cloudablerule) {
        console.log(this.baseUrl + "/create");
        return this.http.post("" + this.baseUrl + "/create", cloudablerule);
    };
    ForCloudableService.prototype.collectRule = function () {
        var collectRulesUrl = "http://localhost:8090/cloudableRule/getAll";
        return this.http.get(collectRulesUrl);
    };
    ForCloudableService.prototype.collectOptions = function () {
        var collectOptionsUrl = "http://localhost:8090/option/getAll";
        return this.http.get(collectOptionsUrl);
    };
    ForCloudableService.prototype.collectQuestion = function () {
        var CollectQuestionUrl = "http://localhost:8090/assessmentQuestions/getAllQuestions";
        return this.http.get(CollectQuestionUrl);
    };
    ForCloudableService = __decorate([
        core_1.Injectable({
            providedIn: 'root'
        }),
        __metadata("design:paramtypes", [http_1.HttpClient])
    ], ForCloudableService);
    return ForCloudableService;
}());
exports.ForCloudableService = ForCloudableService;
//# sourceMappingURL=for-cloudable.service.js.map