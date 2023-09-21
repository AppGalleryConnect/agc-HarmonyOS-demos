(function() {
    const OVER_SIZE = 0
    let BOX_RECT = {
        boxWidth: 0,
        boxHeight: 0,
        boxTop: 0,
        boxLeft: 0
    }
    let IMG_RECT = {
        imgMinWidth: 0,
        imgMinHeight: 0,
        imgMaxWidth: 0,
        imgMaxHeight: 0,
    }
    var scaleOption = {
        minScale: 1,
        maxScale: 3
    }
    let preTouchPosition = {};
    let preTouchesClientx1y1x2y2 = [];
    let originHaveSet = false;
    
    var app = new Vue({
        el: '#app',
        data: function() {
            return {
                currPlayVoice: null,
                showPop: false,
                points: [],
                translateX: 0,
                translateY: 0,
                scaleRatio: 1,
                scaleOrigin: {
                    x: 0,
                    y: 0
                },
                // 弹出内容
                popData: {
                    title: '',
                    address: '',
                    content: '',
                    voiceList: [],
                    images: []
                },
                nowVoiceIndex:-1
            }
        },
        computed: {
            imgWidth() {
                this.scaleRatio * IMG_RECT.imgMinWidth
            },
            imgHeight() {
                this.scaleRatio * IMG_RECT.imgMinHeight
            },
            containerRef() {
                return this.$refs.containerRef
            },
            boxRef() {
                return this.$refs.boxRef
            },
            imgRef() {
                return this.$refs.bgRef
            },
            pointsRef() {
                return this.$refs.pointsRef
            },
            pointRefs() {
                return this.$refs.pointRefs
            }
        },
        methods: {
            handleBgLoaded(e) {
                const containerClientRect = this.containerRef.getBoundingClientRect()
                const boxClientRect = this.boxRef.getBoundingClientRect()
                const bgClientRect = this.imgRef.getBoundingClientRect()
                BOX_RECT = {
                    boxWidth: containerClientRect.width,
                    boxHeight: containerClientRect.height,
                    boxTop: containerClientRect.top,
                    boxLeft: containerClientRect.left
                }
                IMG_RECT = {
                    imgMinWidth: bgClientRect.width,
                    imgMinHeight: bgClientRect.height,
                    imgMaxWidth: this.imgRef.naturalWidth,
                    imgMaxHeight: this.imgRef.naturalHeight
                }
                scaleOption.maxScale = Math.round(IMG_RECT.imgMaxHeight / IMG_RECT.imgMinHeight * 1000) / 1000
                scaleOption.minScale = Math.round(BOX_RECT.boxWidth / IMG_RECT.imgMinWidth * 1000) / 1000
                console.log('初始化默认属性：', IMG_RECT.imgMinWidth, IMG_RECT.imgMinHeight)
                console.table({...BOX_RECT, ...IMG_RECT, ...scaleOption})
                this.init()
            },
            init() {
                let pointList = []
                if (window.getMapPointList) {
                    let pointListStr = window.getMapPointList.call('123')
                    pointList = JSON.parse(pointListStr)
                } else {
                    pointList = window.points
                }
                console.log(pointList)
                this.points = pointList.map(item => {
                    return {
                        ...item,
                        x: item.point[0] / scaleOption.maxScale,
                        y: item.point[1] / scaleOption.maxScale,
                        w: item.width / scaleOption.maxScale,
                        h: item.height / scaleOption.maxScale,
                    }
                })
                this.imgRef.addEventListener('touchstart', this.handleImgStart);
                this.imgRef.addEventListener('touchmove', this.handleImgMove);
                this.imgRef.addEventListener('touchend', this.handleImgEnd);
                this.imgRef.addEventListener('touchcancel', this.handleImgCancel);
            },
            recordPreTouchPosition(touch) {
                preTouchPosition = {
                    x: touch.clientX,
                    y: touch.clientY
                };
            },
            handleImgStart(e) {
                setStyle(this.imgRef, 'transition', '');
                setStyle(this.pointsRef, 'transition', '');
                let touches = e.touches;
                if (touches.length > 1) {
                    let one = touches[0];
                    let two = touches[1];
                    preTouchesClientx1y1x2y2 = [one.clientX, one.clientY, two.clientX, two.clientY];
                    originHaveSet = false;
                }
                this.recordPreTouchPosition(touches[0]);
            },
            handleImgMove(e) {
                const rect1 = this.imgRef.getBoundingClientRect()
                const rect2 = this.containerRef.getBoundingClientRect()
                let touches = e.touches;
                if (touches.length === 1) {
                    let oneTouch = touches[0];
                    let translated = getTranslate(oneTouch.target);
                    const distanceX = oneTouch.clientX - preTouchPosition.x
                    const distanceY = oneTouch.clientY - preTouchPosition.y
                    const targetX = distanceX + translated.left
                    const targetY = distanceY + translated.top
                    const minLeft = translated.left - (rect1.left - rect2.left)
                    const minTop = translated.top - (rect1.top - rect2.top)
                    const maxLeft = minLeft + rect2.width - rect1.width
                    const maxTop = minTop + rect2.height - rect1.height
                    if (targetX >= (minLeft + OVER_SIZE)) {
                        this.translateX = (minLeft + OVER_SIZE);
                    } else if (targetX <= (maxLeft - OVER_SIZE)) {
                        this.translateX = (maxLeft - OVER_SIZE);
                    } else {
                        this.translateX = targetX;
                    }
                    // if (targetY >= (minTop + OVER_SIZE)) {
                    //     this.translateY = (minTop + OVER_SIZE);
                    // } else if (targetY <= (maxTop - OVER_SIZE)) {
                    //     this.translateY = (maxTop - OVER_SIZE);
                    // } else {
                        this.translateY = targetY;
                    // }
                    let matrix = `matrix(${this.scaleRatio}, 0, 0, ${this.scaleRatio}, ${this.translateX}, ${this.translateY})`;
                    setStyle(this.imgRef, 'transform', matrix);
                    setStyle(this.pointsRef, 'transform', matrix);
                    this.recordPreTouchPosition(oneTouch);
                } else {
                   /* let one = touches[0];
                    let two = touches[1];
                    const ratio = getDistance(one.clientX, one.clientY, two.clientX, two.clientY) / getDistance(...preTouchesClientx1y1x2y2) * this.scaleRatio || 1;
                    // 限制缩放级别
                    this.scaleRatio = Math.min(Math.max(ratio, scaleOption.minScale), scaleOption.maxScale);
                    if (!originHaveSet) {
                        originHaveSet = true;
                        // 移动视线中心
                        let origin = relativeCoordinate((one.clientX + two.clientX) / 2, (one.clientY + two.clientY) / 2, this.imgRef.getBoundingClientRect(), this.scaleRatio);
                        // 修正视野变化带来的平移量
                        setStyle(this.imgRef, 'transform-origin', `${origin.x}px ${origin.y}px`);
                        setStyle(this.pointsRef, 'transform-origin', `${origin.x}px ${origin.y}px`);
                        this.translateX = (this.scaleRatio - 1) * (origin.x - this.scaleOrigin.x) + this.translateX;
                        this.translateY = (this.scaleRatio - 1) * (origin.y - this.scaleOrigin.y) + this.translateY;
                        // console.log(this.translateX, this.translateY)
                        this.scaleOrigin = origin;
                    }
                    let matrix = `matrix(${this.scaleRatio}, 0, 0, ${this.scaleRatio}, ${this.translateX}, ${this.translateY})`;
                    setStyle(this.imgRef, 'transform', matrix);
                    setStyle(this.pointsRef, 'transform', matrix);
                    preTouchesClientx1y1x2y2 = [one.clientX, one.clientY, two.clientX, two.clientY];*/
                }
                e.preventDefault();
            },
            handleImgEnd(e) {
                let touches = e.touches;
                if (touches.length === 1) {
                    this.recordPreTouchPosition(touches[0]);
                }
                const rect1 = this.imgRef.getBoundingClientRect()
                const rect2 = this.containerRef.getBoundingClientRect()
                if (rect1.left > rect2.left) {
                    this.translateX = this.translateX - (rect1.left - rect2.left)
                } else if (rect1.left < (rect2.width - rect1.width + rect2.left)) {
                    this.translateX = this.translateX - (rect1.left + (rect1.width - rect2.left - rect2.width))
                }
                // if (rect1.top > rect2.top) {
                //     this.translateY = this.translateY - (rect1.top - rect2.top)
                // } else if (rect1.top < (rect2.height - rect1.height) + rect2.top) {
                //     console.log(this.translateY, rect1.top, (rect1.height - rect2.top - rect2.height))
                //     this.translateY = this.translateY - (rect1.top + (rect1.height - rect2.top - rect2.height))
                // }
                if ((rect1.left > rect2.left) || (rect1.left < (rect2.width - rect1.width) + rect2.left) || (rect1.top > rect2.top) || (rect1.top < (rect2.height - rect1.height) + rect2.top)) {
                    let matrix = `matrix(${this.scaleRatio}, 0, 0, ${this.scaleRatio}, ${this.translateX}, ${this.translateY})`;
                    setStyle(this.imgRef, 'transform', matrix);
                    setStyle(this.imgRef, 'transition', 'transform 0.3s cubic-bezier(0.21, 0.42, 0.59, 1.21) 0s');
                    setStyle(this.pointsRef, 'transform', matrix);
                    setStyle(this.pointsRef, 'transition', 'transform 0.3s cubic-bezier(0.21, 0.42, 0.59, 1.21) 0s');
                }
            },
            handleImgCancel(e) {
                let touches = e.touches;
                if (touches.length === 1) {
                    this.recordPreTouchPosition(touches[0]);
                }
            },
            zoom(type){
                const rect1 = this.imgRef.getBoundingClientRect()
                const rect2 = this.containerRef.getBoundingClientRect()
                const ratio = this.scaleRatio + (type === 1 ? 0.1 : -0.1);
                this.scaleRatio = Math.min(Math.max(ratio, scaleOption.minScale), scaleOption.maxScale);
                let origin = relativeCoordinate(BOX_RECT.boxLeft, (BOX_RECT.boxTop + BOX_RECT.boxHeight) / 2, this.imgRef.getBoundingClientRect(), this.scaleRatio);
                // 修正视野变化带来的平移量
                setStyle(this.imgRef, 'transform-origin', `${origin.x}px ${origin.y}px`);
                setStyle(this.pointsRef, 'transform-origin', `${origin.x}px ${origin.y}px`);
                this.translateX = (this.scaleRatio - 1) * (origin.x - this.scaleOrigin.x) + this.translateX;
                this.translateY = (this.scaleRatio - 1) * (origin.y - this.scaleOrigin.y) + this.translateY;
                // console.log(this.translateX, this.translateY)
                this.scaleOrigin = origin;
                if (rect1.left > rect2.left) {
                    this.translateX = this.translateX - (rect1.left - rect2.left)
                } else if (rect1.left < (rect2.width - rect1.width + rect2.left)) {
                    this.translateX = this.translateX - (rect1.left + (rect1.width - rect2.left - rect2.width))
                }
                let matrix = `matrix(${this.scaleRatio}, 0, 0, ${this.scaleRatio}, ${this.translateX}, ${this.translateY})`;
                setStyle(this.imgRef, 'transform', matrix);
                setStyle(this.pointsRef, 'transform', matrix);
            },
            openDialog(e, index) {
                console.log(e, index)

                if (!e.loaded) {
                    if (window.getMapPointInfo) {
                        var infoStr = window.getMapPointInfo.call(e.id)
                        var info = JSON.parse(infoStr)
                        if (info.title) {
                            var arr = info.title.split("|")
                            info.title = arr[0]
                            info.address = arr[1]
                            info.voiceList = []
                            if (info.summary) {
                                var voiceArr = info.summary.split('|')
                                info.voiceList = voiceArr.map(item => {
                                    var nameArr = item.split("#")
                                    return {
                                        name: nameArr[0],
                                        url: nameArr[1],
                                        isPlaying: false
                                    }
                                })
                            }
                            var content = info.content || ""
                            info.content = content.replace(/<\/?.+?\/?>/g, "").replace("&amp;","&")
                            info.images = info.showImagesUrl
                            info.loaded = true
                        }
                        e = {...e, ...info}
                        this.$set(this.points, index, { ...e, ...info })
                    } else {
                        return
                    }
                }
                this.showPop = true
                this.popData = e
                const _this = this
                setTimeout(function() {
                    _this.mySwiper = new Swiper ('.swiper-container', {
                        direction: 'horizontal',
                        loop: true,
                        // 如果需要分页器
                        pagination: '.swiper-pagination',
                    })
                }, 16)
            },
            voicePlay(voice, ind) {
                //var isplay = voice.isPlaying
                if (window.goPlay) {
                    window.goPlay.call(voice.url)
                } else {
                    console.log('播放/暂停语音', voice)
                }
                this.points = this.points.map((item,i) => {
                    if (item.voiceList) {
                        item.voiceList = item.voiceList.map(q => {
                            q.isPlaying = false
                            return q
                        })
                    }
                    return item
                })
                /*this.popData.voiceList = this.popData.voiceList.map(q => {
                    q.isPlaying = false
                    return q
                })
                voice.isPlaying = !isplay*/
                this.nowVoiceIndex = ind
            },
            goDetail() {
                if (window.goDetail) {
                    window.goDetail.call(this.popData.id)
                } else {
                    console.log('打开详情')
                }
            },
            closeDialog(e) {
                this.showPop = false
                this.popData = {
                    title: '',
                    address: '',
                    content: '',
                    voiceList: [],
                    images: []
                }
                if (this.mySwiper) {
                    this.mySwiper.destroy(true, true)
                }
            },
        }
    })

    window.voicePlayCallback = function(isPlaying) {
        console.log(app.nowVoiceIndex , isPlaying)
        // 关闭播放状态
        if (app.showPop) {
            if (app.popData.voiceList) {
                var list = app.popData.voiceList.map((item,i) => {
                    if(app.nowVoiceIndex==i){
                        item.isPlaying = isPlaying
                    }
                    else{
                        item.isPlaying = false;
                    }
                    return item
                })
                app.$set(app.popData, 'voiceList', list)
            }
        } else {
            console.log('弹窗已关闭！当前播放播放状态：' + isPlaying)
        }
    }
    
    /**
     * 获取两个点坐标的距离
     * @param {*} x1 
     * @param {*} y1 
     * @param {*} x2 
     * @param {*} y2 
     * @returns 
     */
    function getDistance(x1, y1, x2, y2) {
        let a = x1 - x2;
        let b = y1 - y2;
        return Math.sqrt(a * a + b * b);
    }

    function getStyle(target, style) {
        let styles = window.getComputedStyle(target, null);
        return styles.getPropertyValue(style);
    }

    function setStyle(target, key, value) {
        target.style[key] = value;
    }
    function getTranslate(target) {
        let matrix = getStyle(target, 'transform');
        let nums = matrix.substring(7, matrix.length - 1).split(', ');
        let left = parseInt(nums[4]) || 0;
        let top = parseInt(nums[5]) || 0;
        return {
        left: left,
        top: top
        }
    }

    function relativeCoordinate(x, y, rect, scaleRatio) {
        let cx = (x - rect.left) / scaleRatio;
        let cy = (y - rect.top) / scaleRatio;
        return {
        x: cx,
        y: cy
        };
    };

})();