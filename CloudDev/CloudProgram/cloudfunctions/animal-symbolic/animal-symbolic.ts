import CloudDBZoneWrapper from "./CloudDBZoneWrapper"
import {animal} from "./animal"


let myHandler = async function (event, context, callback, logger) {
  logger.info(event);

  var res = new context.HTTPResponse(context.env, {
    "res-type":"context.env",
    "faas-content-type":"json",
  },"application/json", "200");
  var year;
  var body = {
    result: ""
  };

  const cloudDBZoneWrapper = new CloudDBZoneWrapper(logger);

  if (event.body) {
    var _body = JSON.parse(event.body);
    year = _body.year;
  } else {
    year = event.year;
  };

  body.result = getAnimal(year);
  res.body = body;

  function getAnimal (inputYear) {
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

  let data = new animal();
  data.setAnimal(getAnimal(year));

  await cloudDBZoneWrapper.executeUpsert(data);



  callback(res);
};

export { myHandler };