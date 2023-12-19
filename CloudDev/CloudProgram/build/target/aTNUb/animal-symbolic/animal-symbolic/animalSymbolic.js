"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (g && (g = 0, op[0] && (_ = 0)), _) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.myHandler = void 0;
var CloudDBZoneWrapper_1 = __importDefault(require("./CloudDBZoneWrapper"));
var animal_1 = require("./animal");
var myHandler = function (event, context, callback, logger) {
    return __awaiter(this, void 0, void 0, function () {
        function getAnimal(inputYear) {
            var resultString;
            var remainder = inputYear % 12;
            switch (remainder) {
                case 0:
                    resultString = "Monkey";
                    break;
                case 1:
                    resultString = "Chicken";
                    break;
                case 2:
                    resultString = "Dog";
                    break;
                case 3:
                    resultString = "Pig";
                    break;
                case 4:
                    resultString = "Mouse";
                    break;
                case 5:
                    resultString = "Cow";
                    break;
                case 6:
                    resultString = "Tiger";
                    break;
                case 7:
                    resultString = "Rabbit";
                    break;
                case 8:
                    resultString = "Dragon";
                    break;
                case 9:
                    resultString = "Snake";
                    break;
                case 10:
                    resultString = "Horse";
                    break;
                case 11:
                    resultString = "Sheep";
                    break;
                default:
                    resultString = "No symbolic Animal";
            }
            return resultString;
        }
        var res, year, body, cloudDBZoneWrapper, _body, data;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    logger.info(event);
                    res = new context.HTTPResponse(context.env, {
                        "res-type": "context.env",
                        "faas-content-type": "json",
                    }, "application/json", "200");
                    body = {
                        result: ""
                    };
                    cloudDBZoneWrapper = new CloudDBZoneWrapper_1.default(logger);
                    if (event.body) {
                        _body = JSON.parse(event.body);
                        year = _body.year;
                    }
                    else {
                        year = event.year;
                    }
                    ;
                    body.result = getAnimal(year);
                    res.body = body;
                    data = new animal_1.animal();
                    data.setAnimal(getAnimal(year));
                    return [4 /*yield*/, cloudDBZoneWrapper.executeUpsert(data)];
                case 1:
                    _a.sent();
                    callback(res);
                    return [2 /*return*/];
            }
        });
    });
};
exports.myHandler = myHandler;
//# sourceMappingURL=animalSymbolic.js.map