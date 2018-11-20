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
var FeedbackComponent = /** @class */ (function () {
    function FeedbackComponent(router) {
        this.router = router;
        this.rating = 0;
    }
    FeedbackComponent.prototype.ngOnInit = function () {
        this.rating = 0;
    };
    FeedbackComponent.prototype.oneStar = function () {
        console.log("One Star");
        this.rating = 1;
    };
    FeedbackComponent.prototype.twoStar = function () {
        this.rating = 2;
        console.log("Two Star");
    };
    FeedbackComponent.prototype.threeStar = function () {
        this.rating = 3;
        console.log("Three Star");
    };
    FeedbackComponent.prototype.fourStar = function () {
        this.rating = 4;
        console.log("Four Star");
    };
    FeedbackComponent.prototype.fiveStar = function () {
        this.rating = 5;
        console.log("Five Star");
    };
    FeedbackComponent = __decorate([
        core_1.Component({
            selector: 'app-feedback',
            templateUrl: './feedback.component.html',
            styleUrls: ['./feedback.component.scss']
        }),
        __metadata("design:paramtypes", [router_1.Router])
    ], FeedbackComponent);
    return FeedbackComponent;
}());
exports.FeedbackComponent = FeedbackComponent;
//# sourceMappingURL=feedback.component.js.map