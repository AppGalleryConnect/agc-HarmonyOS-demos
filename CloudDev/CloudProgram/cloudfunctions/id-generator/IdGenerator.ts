import * as crypto from 'crypto';

let myHandler = async function (event, context, callback, logger) {
    logger.info(`Input event: ${JSON.stringify(event)}`);

    const uuid = crypto.randomUUID();
    logger.info(`Generated UUID: ${uuid}`);

    let res = new context.HTTPResponse({"uuid": uuid}, {
        "faas-content-type": "json"
    }, "application/json", "200");

    callback(res);
};

export { myHandler }