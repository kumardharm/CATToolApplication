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
var http_1 = require("@angular/common/http");
var reassessment_service_1 = require("./reassessment.service");
var ReassessmentComponent = /** @class */ (function () {
    function ReassessmentComponent(router, reassessmentService, http) {
        this.router = router;
        this.reassessmentService = reassessmentService;
        this.http = http;
        this.AllData = [];
    }
    ReassessmentComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.reassessmentService.CollectData().subscribe(function (result) {
            _this.AllData = result;
            console.log(_this.AllData[2].applicationName + 'KKKK(((**************)))JJJJ');
        });
    };
    ReassessmentComponent = __decorate([
        core_1.Component({
            selector: 'app-reassessment',
            templateUrl: './reassessment.component.html',
            styleUrls: ['./reassessment.component.scss']
        }),
        __metadata("design:paramtypes", [router_1.Router, reassessment_service_1.ServiceService, http_1.HttpClient])
    ], ReassessmentComponent);
    return ReassessmentComponent;
}());
exports.ReassessmentComponent = ReassessmentComponent;
//# sourceMappingURL=reassessment.component.js.map