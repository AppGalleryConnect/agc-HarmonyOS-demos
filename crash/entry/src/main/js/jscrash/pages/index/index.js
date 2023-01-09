export default {
    data: {
        title: ""
    },
    onInit() {
    },
    async onCreateJsFatal(){
        console.info("=======>ljp test: click button to create js fatal");
        setTimeout(function throwAnError () {
            console.log('Hello Yoda')
            throw new Error('=======>ljp test: intentionally throw an error');
        }, 1000)
    },
    async onCreateJsFatal1(){
        console.info("=======>ljp test: click button to create js fatal 1");
        this.createJs1();
    },
    createJs1(){
        this.createJs2();
    },
    createJs2() {
        console.info("=======>ljp test: throw an error");
        setTimeout(function createError () {
            throw new Error('=======>ljp test: create error');
        }, 1000)
    },
}
